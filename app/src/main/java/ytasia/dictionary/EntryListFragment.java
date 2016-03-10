package ytasia.dictionary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.text.TextWatcher;

import dao.db_handle.SuggestDataAccess;
import dao.db_handle.TBEntryHandler;
import dao.db_handle.TBKanjiEntryHandler;
import dao.db_handle.TBKanjiHandler;
import dao.obj.EntryObj;
import dao.obj.KanjiEntryObj;
import dao.obj.KanjiObj;
import util.JapaneseHandler;


public class EntryListFragment extends Fragment {

    private ImageButton addBt;
    private EditText newEntryEt;
    private Toolbar toolbar;
    private DynamicListView entryList;
    private EntryListAdapter adapter;
    private List<EntryObj> listEntryOb;
    private TBEntryHandler entryHd;

    public EntryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_entry, container, false);

        // Match object to layout elements
        toolbar = (Toolbar) view.findViewById(R.id.entry_list_fragment_toolbar);
        addBt = (ImageButton) view.findViewById(R.id.entry_list_add_new_button);
        newEntryEt = (EditText) view.findViewById(R.id.entry_list_new_text_edit);
        entryList = (DynamicListView) view.findViewById(R.id.entry_list_view);

        // Get data from database (list Entry Object)
        entryHd = new TBEntryHandler(getActivity());
        listEntryOb = entryHd.getAll();

        // Set data to adapter (customized)
        adapter = new EntryListAdapter(getActivity(), listEntryOb);

        // set adapter to ListView
        entryList.setAdapter(adapter);
        entryList.setTextFilterEnabled(true);

        // on click listView item
        entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Get Entry Object by position
                EntryObj ob = (EntryObj) parent.getAdapter().getItem(position);

                // Set data object to Intent
                Intent intent = new Intent(getActivity(), EntryViewActivity.class);
                intent.putExtra("entry_object", ob);
                startActivityForResult(intent, MainActivity.REQUEST_CODE_ENTRY_EDIT);
            }
        });

        // on swipe listView element
        entryList.enableSwipeToDismiss(
                new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            // Delete Entry
                            deleteEntry(position);
                        }
                    }
                }
        );

        // set ActionBar function to toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);

        // Set TextWatcher to 'new entry edit text'
        //newEntryEt.addTextChangedListener(newEntryWatcher);

        // on click Add Button
        addBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!newEntryEt.getText().toString().equalsIgnoreCase("")) {
                    Intent intent = new Intent(getActivity(), EntryAddActivity.class);
                    intent.putExtra("new_entry_name", newEntryEt.getText().toString());
                    startActivityForResult(intent, MainActivity.REQUEST_CODE_ENTRY_ADD);
                } else {
                    newEntryEt.setError("Please insert some text");
                }
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tool_bar_main_menu, menu);

        // Match object to layout elements
        MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        MenuItem sortByAlphabetItem = menu.findItem(R.id.toolbar_sort_by_alphabet);
        MenuItem sortByDateItem = menu.findItem(R.id.toolbar_sort_by_date);
        MenuItem sortByLevelItem = menu.findItem(R.id.toolbar_sort_by_level);
        MenuItem refreshItem = menu.findItem(R.id.toolbar_refresh);

        // Search function
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    entryList.clearTextFilter();
                } else {
                    entryList.setFilterText(newText.toString());
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Sort by Alphabet
            case R.id.toolbar_sort_by_alphabet:
                // Sort by A->Z
                Collections.sort(listEntryOb, new Comparator<EntryObj>() {
                    @Override
                    public int compare(EntryObj ob1, EntryObj ob2) {
                        return (ob1.getContent().compareToIgnoreCase(ob2.getContent()));
                    }
                });
                // Set sorted data to adapter (customized)
                adapter = new EntryListAdapter(getActivity(), listEntryOb);
                // set sorted adapter to ListView
                entryList.setAdapter(adapter);
                break;
            // Sort by Added Date
            case R.id.toolbar_sort_by_date:
                // Sort from nearest
                Collections.sort(listEntryOb, new Comparator<EntryObj>() {
                    @Override
                    public int compare(EntryObj ob1, EntryObj ob2) {
                        return (ob1.getCreatedDate().compareTo(ob2.getCreatedDate()));
                    }
                });
                // Set sorted data to adapter (customized)
                adapter = new EntryListAdapter(getActivity(), listEntryOb);
                // set sorted adapter to ListView
                entryList.setAdapter(adapter);
                break;
            // Sort by Level
            case R.id.toolbar_sort_by_level:
                // Sort by lower -> higher
                Collections.sort(listEntryOb, new Comparator<EntryObj>() {
                    @Override
                    public int compare(EntryObj ob1, EntryObj ob2) {
                        return (ob1.getLevel() - ob2.getLevel());
                    }
                });
                // Set sorted data to adapter (customized)
                adapter = new EntryListAdapter(getActivity(), listEntryOb);
                // set sorted adapter to ListView
                entryList.setAdapter(adapter);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_CODE_ENTRY_EDIT) {
            switch (resultCode) {
                // After edit entry
                case MainActivity.RESULT_CODE_ENTRY_EDIT:
                    // Get new entry object from EntryViewActivity
                    EntryObj newObj = (EntryObj) data.getSerializableExtra("edit_entry_object");
                    // Update to database
                    entryHd.update(newObj, newObj.getEntryId());

                    // Refresh ListView
                    listEntryOb = entryHd.getAll();
                    adapter = new EntryListAdapter(getActivity(), listEntryOb);
                    entryList.setAdapter(adapter);
                    break;
            }
        }

        if (requestCode == MainActivity.REQUEST_CODE_ENTRY_ADD) {
            switch (resultCode) {
                // After add entry
                case MainActivity.RESULT_CODE_ENTRY_ADD:
                    EntryObj entryObj = (EntryObj) data.getSerializableExtra("add_entry_object");
                    // Update to database
                    addEntry(entryObj);

                    // Refresh add field
                    newEntryEt.clearFocus();
                    newEntryEt.setText("");
                    newEntryEt.setHint("add new word");
                    break;
            }
        }
    }

    /**
     * Customized Adapter for Entry List Object
     */
    public class EntryListAdapter extends ArrayAdapter<EntryObj> {
        private final Context context;
        private final List<EntryObj> values;

        // Constructor
        public EntryListAdapter(Context context, List<EntryObj> values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Match ListView to Layout
            View itemView = inflater.inflate(R.layout.list_item_entry, parent, false);
            TextView entryName = (TextView) itemView.findViewById(R.id.entry_list_item_content_1);
            TextView entryLevel = (TextView) itemView.findViewById(R.id.entry_list_item_content_2);

            // Set data for ListItem
            entryName.setText(values.get(position).toString());
            entryLevel.setText(Integer.toString(values.get(position).getLevel()));

            return itemView;
        }
    }

    /**
     * Delete Entry on ListView by position
     *
     * @param position The position of the object
     */
    public void deleteEntry(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure to delete record");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get object user want to delete
                EntryObj entryObj = listEntryOb.get(position);

                // Delete all data related in this object on "KanjiEntry table"
                TBKanjiEntryHandler tbKanjiEntryHandler = new TBKanjiEntryHandler(getActivity());
                List<Integer> list = tbKanjiEntryHandler.getAllKanjiIdByEntryId(entryObj.getEntryId());
                for (int j = 0; j < list.size(); j++) {
                    tbKanjiEntryHandler.delete(list.get(j), entryObj.getEntryId());
                }

                //System.out.println("Size: " + tbKanjiEntryHandler.getAllKanjiIdByEntryId(entryObj.getEntryId()).size());

                // Delete on database
                entryHd.delete(entryObj.getEntryId());

                // notify
                Toast.makeText(getActivity().getApplicationContext(),
                        "REMOVE Position :" + position + "  ListItem : " + entryList.getItemAtPosition(position), Toast.LENGTH_LONG)
                        .show();
                dialog.dismiss();

                //Get new List Object
                listEntryOb = entryHd.getAll();
                // Set new data to adapter (customized)
                adapter = new EntryListAdapter(getActivity(), listEntryOb);
                // set new adapter to ListView
                entryList.setAdapter(adapter);

            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();
    }

    public void addEntry(EntryObj newEntryObj) {
        int newEntryId = entryHd.add(newEntryObj);
        if (newEntryId != -1) {
            // Get all Kanji available on new Entry
            JapaneseHandler jpHd = new JapaneseHandler();
            String kanji = jpHd.getAllKanji(newEntryObj.getContent());

            // Add new kanji to database one-by-one
            int cpLength = jpHd.getCodePointLength(kanji);
            for (int i = 0; i < cpLength; ++i) {
                // Get unique kanji from all kanji
                KanjiObj kanjiObj = new KanjiObj(jpHd.getUniqueKanji(kanji, i, 1));
                // Set suggest meaning to new Kanji
                SuggestDataAccess dbAccess = SuggestDataAccess.getInstance(getActivity());
                dbAccess.open();
                kanjiObj.setMeaning(dbAccess.getSuggestMeaning(Character.toString(kanjiObj.getCharacter())));
                dbAccess.close();

                // Add new kanji to database
                TBKanjiHandler kanjiHandler = new TBKanjiHandler(getActivity());
                int newKanjiId = kanjiHandler.add(kanjiObj);

                // Create new Entry-Kanji constrain and add to 'KanjiEntry Table'
                if (newKanjiId != -1) {
                    KanjiEntryObj kanjiEntryObj = new KanjiEntryObj(newKanjiId, newEntryId);
                    TBKanjiEntryHandler tbKanjiEntryHandler = new TBKanjiEntryHandler(getActivity());
                    tbKanjiEntryHandler.add(kanjiEntryObj);
                    Toast.makeText(getActivity(), "Add Kanji Successfully", Toast.LENGTH_LONG).show();
                } else {
                    KanjiObj o = kanjiHandler.getByCharater(kanjiObj.getCharacter());
                    KanjiEntryObj kanjiEntryObj = new KanjiEntryObj(o.getKanjiId(), newEntryId);
                    TBKanjiEntryHandler tbKanjiEntryHandler = new TBKanjiEntryHandler(getActivity());
                    tbKanjiEntryHandler.add(kanjiEntryObj);
                    Toast.makeText(getActivity(), "Add Kanji Failed", Toast.LENGTH_LONG).show();
                }
            }

            // Test
            TBKanjiEntryHandler tbKanjiEntryHandler = new TBKanjiEntryHandler(getActivity());
            List<Integer> list = tbKanjiEntryHandler.getAllKanjiIdByEntryId(newEntryId);
            System.out.println("List KanjiID Size : " + list.size());
            for (int j = 0; j < list.size(); j++) {
                System.out.println("New KanjiID : " + list.get(j));
                System.out.println("Kanji: " + new TBKanjiHandler(getActivity()).getById(list.get(j)).getCharacter());
            }


            // Refresh ListView
            listEntryOb = entryHd.getAll();
            adapter = new EntryListAdapter(getActivity(), listEntryOb);
            entryList.setAdapter(adapter);
            Toast.makeText(getActivity(), "Add Entry Successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Add Entry Failed", Toast.LENGTH_LONG).show();
        }
    }
}
