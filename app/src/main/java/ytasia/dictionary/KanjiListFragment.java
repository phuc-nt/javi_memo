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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.TextView;
import android.widget.Toast;;import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dao.db_handle.TBKanjiHandler;
import dao.obj.KanjiObj;


public class KanjiListFragment extends Fragment {

    private Toolbar toolbar;
    private DynamicListView kanjiList;
    List<String> list = new ArrayList<>();
    TBKanjiHandler kanjiHd;
    List<KanjiObj> listKanjiOb;
    KanjiListAdapter adapter;


    public KanjiListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_kanji, container, false);

        // Match object to layout elements
        toolbar = (Toolbar) view.findViewById(R.id.kanji_list_fragment_toolbar);
        kanjiList = (DynamicListView) view.findViewById(R.id.kanji_list_view);

        // Get data from database
        kanjiHd = new TBKanjiHandler(getActivity());
        listKanjiOb = kanjiHd.getAll();

        // Set data to adapter (customized)
        adapter = new KanjiListAdapter(getActivity(), listKanjiOb);

        // set adapter to ListView
        kanjiList.setAdapter(adapter);
        kanjiList.setTextFilterEnabled(true);

        // on click listView item
        kanjiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                KanjiObj ob = (KanjiObj) parent.getAdapter().getItem(position);

                // Set data object to Intent
                Intent intent = new Intent(getActivity(), KanjiViewActivity.class);
                intent.putExtra("kanji_object", ob);
                startActivity(intent);
            }
        });

        // on swipe listView element
        kanjiList.enableSwipeToDismiss(
                new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (final int position : reverseSortedPositions) {
                            deleteKanji(position);
                        }
                    }
                }
        );

        // set ActionBar function to toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tool_bar_main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        MenuItem sortByAlphabetItem = menu.findItem(R.id.toolbar_sort_by_alphabet);
        MenuItem sortByDateItem = menu.findItem(R.id.toolbar_sort_by_date);
        MenuItem sortByLevelItem = menu.findItem(R.id.toolbar_sort_by_level);
        MenuItem refreshItem = menu.findItem(R.id.toolbar_refresh);

        sortByDateItem.setEnabled(false);

        //Search Function start
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    kanjiList.clearTextFilter();
                } else {
                    kanjiList.setFilterText(newText.toString());
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Refresh Kanji ListView
            case R.id.toolbar_refresh:
                //Get new List Object
                listKanjiOb = kanjiHd.getAll();
                // Set data to adapter (customized)
                adapter = new KanjiListAdapter(getActivity(), listKanjiOb);
                // set adapter to ListView
                kanjiList.setAdapter(adapter);
                break;
            case R.id.toolbar_sort_by_alphabet:
                // Sort by A->Z
                Collections.sort(listKanjiOb, new Comparator<KanjiObj>() {
                    @Override
                    public int compare(KanjiObj ob1, KanjiObj ob2) {
                        return (ob1.getCharacter() - ob2.getCharacter());
                    }
                });
                // Set sorted data to adapter (customized)
                adapter = new KanjiListAdapter(getActivity(), listKanjiOb);
                // set sorted adapter to ListView
                kanjiList.setAdapter(adapter);
                break;
            // Sort by Level
            case R.id.toolbar_sort_by_level:
                Collections.sort(listKanjiOb, new Comparator<KanjiObj>() {
                    @Override
                    public int compare(KanjiObj lhs, KanjiObj rhs) {
                        return (lhs.getLevel() - rhs.getLevel());
                    }
                });
                adapter = new KanjiListAdapter(getActivity(), listKanjiOb);
                kanjiList.setAdapter(adapter);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Customized Adapter for Kanji List Object
     */
    public class KanjiListAdapter extends ArrayAdapter<KanjiObj> {
        private final Context context;
        private final List<KanjiObj> values;

        // Constructor
        public KanjiListAdapter(Context context, List<KanjiObj> values) {
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
     * Delete Kanji on ListView by position
     *
     * @param position The position of the object
     */
    public void deleteKanji(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure to delete record");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // delete Kanji in database
                kanjiHd.delete(listKanjiOb.get(position).getKanjiId());
                // get new list Kanji object
                listKanjiOb = kanjiHd.getAll();
                // Set new data to adapter (customized)
                adapter = new KanjiListAdapter(getActivity(), listKanjiOb);
                // set new adapter to ListView
                kanjiList.setAdapter(adapter);

                // notify
                Toast.makeText(getActivity().getApplicationContext(),
                        "REMOVE Position :" + position + "  ListItem : " + kanjiList.getItemAtPosition(position), Toast.LENGTH_LONG)
                        .show();
                dialog.dismiss();
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
}
