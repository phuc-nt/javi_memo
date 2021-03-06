package com.ytasia.dict.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ytasia.dict.dao.db_handle.TBEntryHandler;
import com.ytasia.dict.dao.obj.EntryObj;

import com.ytasia.dict.ddp.DBBasic;
import com.ytasia.dict.service.EntryService;
import com.ytasia.dict.util.JapaneseHandler;
import com.ytasia.dict.util.RefreshInterface;
import com.ytasia.dict.util.YTDictValues;
import com.ytasia.dict.view.activity.EntryAddActivity;
import com.ytasia.dict.view.activity.EntryViewActivity;
import com.ytasia.dict.view.activity.MainActivity;

import org.jsoup.helper.StringUtil;

import ytasia.dictionary.R;


public class EntryListFragment extends Fragment implements RefreshInterface {

    private ImageButton addBt;
    private EditText newEntryEt;
    private Toolbar toolbar;
    private ListView entryList;
    private EntryService.EntryListAdapter adapter;
    private List<EntryObj> listEntryOb;
    private TBEntryHandler entryHd;
    private EntryService entryService;

    public EntryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        entryHd = new TBEntryHandler(getActivity());
        listEntryOb = entryHd.getAll();
        YTDictValues.refreshInterface = EntryListFragment.this;

        DBBasic db = DBBasic.getInstance();
        db.checkServerData();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_entry, container, false);

        // Get data from database (list Entry Object)
        entryHd = new TBEntryHandler(getActivity());
        listEntryOb = entryHd.getAll();

        // Match object to layout elements
        matchObjectToLayout(view);

        // Set default data
        setDefaultData();

        // on click listView item
        entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Get Entry Object by position
                EntryObj ob = (EntryObj) parent.getItemAtPosition(position);

                // Set data object to Intent
                Intent intent = new Intent(getActivity(), EntryViewActivity.class);
                intent.putExtra("entry_object", ob);
                startActivityForResult(intent, MainActivity.REQUEST_CODE_ENTRY_EDIT);
            }
        });


        entryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final EntryObj deleteObj = listEntryOb.get(position);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(getResources().getString(R.string.warning_title));
                alert.setMessage(getResources().getString(R.string.delete_entry_confirm));
                alert.setPositiveButton(getResources().getString(R.string.yes_button), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete Entry
                        entryService.delete(getActivity(), deleteObj.getEntryId());
                        YTDictValues.refreshInterface = EntryListFragment.this;
                    }
                });
                alert.setNegativeButton(getResources().getString(R.string.no_button), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();
                return true;
            }
        });

        // on click Add Button
        addBt.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View arg0) {
                                         String input = newEntryEt.getText().toString().replaceAll("\\s", "");
                                         if (!input.equalsIgnoreCase("") && !StringUtil.isNumeric(input)) {
                                             String newEntry = newEntryEt.getText().toString();
                                             String newEntry2 = newEntry.replaceAll("\\s", "");

                                             JapaneseHandler handler = new JapaneseHandler();
                                             String kanji = handler.getAllKanji(newEntry2);

                                             /**
                                              * Check data on device
                                              * If new entry is existed, do nothing
                                              */
                                             if (!YTDictValues.entriesContent.contains(newEntry2)) {
                                                 /**
                                                  * Check number of kanji
                                                  * If > 4, do nothing
                                                  */
                                                 if (!(handler.getCodePointLength(kanji) > 4)) {
                                                     Intent intent = new Intent(getActivity(), EntryAddActivity.class);
                                                     intent.putExtra("new_entry_name", newEntry2);
                                                     startActivityForResult(intent, MainActivity.REQUEST_CODE_ENTRY_ADD);
                                                 } else {
                                                     final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                                     alert.setTitle(getResources().getString(R.string.warning_title));
                                                     alert.setMessage(getResources().getString(R.string.max_value_validate));
                                                     alert.setNegativeButton(getResources().getString(R.string.ok_button), new DialogInterface.OnClickListener() {

                                                         @Override
                                                         public void onClick(DialogInterface dialog, int which) {

                                                             dialog.dismiss();
                                                         }
                                                     });

                                                     alert.show();
                                                 }

                                             } else {
                                                 final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                                 alert.setTitle(getResources().getString(R.string.warning_title));
                                                 alert.setMessage(newEntry2 + getResources().getString(R.string.existed_warning));
                                                 alert.setNegativeButton(getResources().getString(R.string.ok_button), new DialogInterface.OnClickListener() {

                                                     @Override
                                                     public void onClick(DialogInterface dialog, int which) {

                                                         dialog.dismiss();
                                                     }
                                                 });

                                                 alert.show();
                                             }
                                         } else {
                                             newEntryEt.setError(getResources().getString(R.string.not_null_validate));
                                         }
                                     }
                                 }
        );

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tool_bar_main_menu, menu);

        // Match object to layout elements
        MenuItem searchItem = menu.findItem(R.id.toolbar_search);

        // Search function
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
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
                adapter = new EntryService.EntryListAdapter(getActivity(), listEntryOb);
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
                adapter = new EntryService.EntryListAdapter(getActivity(), listEntryOb);
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
                adapter = new EntryService.EntryListAdapter(getActivity(), listEntryOb);
                // set sorted adapter to ListView
                entryList.setAdapter(adapter);
                break;

            // Refresh Entry ListView
            case R.id.toolbar_refresh:
                refreshListView();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.REQUEST_CODE_ENTRY_ADD) {
            switch (resultCode) {
                // After add entry
                case MainActivity.RESULT_CODE_ENTRY_ADD:
                    final EntryObj entryObj = (EntryObj) data.getSerializableExtra("add_entry_object");

                    Log.i("Entry", entryObj.getContent());

                    entryService.add(entryObj);
                    YTDictValues.refreshInterface = EntryListFragment.this;

                    // Refresh add field
                    newEntryEt.clearFocus();
                    newEntryEt.setText("");
                    newEntryEt.setHint("add new word");
                    break;
            }
        }

        if (requestCode == MainActivity.REQUEST_CODE_ENTRY_EDIT) {
            switch (resultCode) {
                // After edit entry
                case MainActivity.RESULT_CODE_ENTRY_EDIT:
                    // Get new entry object from EntryViewActivity
                    EntryObj newObj = (EntryObj) data.getSerializableExtra("edit_entry_object");

                    // Update to server
                    entryService.update(newObj);
                    YTDictValues.refreshInterface = EntryListFragment.this;

                    break;
            }
        }

        refreshListView();
    }

    /**
     * Match object to layout elements
     *
     * @param view View of EntryListFragment
     */

    private void matchObjectToLayout(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.entry_list_fragment_toolbar);
        addBt = (ImageButton) view.findViewById(R.id.entry_list_add_new_button);
        newEntryEt = (EditText) view.findViewById(R.id.entry_list_new_text_edit);
        entryList = (ListView) view.findViewById(R.id.entry_list_view);
    }

    /**
     * Set default data
     */
    private void setDefaultData() {
        entryService = new EntryService();

        // Set data to adapter (customized)
        adapter = new EntryService.EntryListAdapter(getActivity(), listEntryOb);

        // set adapter to ListView
        entryList.setAdapter(adapter);
        entryList.setTextFilterEnabled(true);

        // set ActionBar function to toolsbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
    }

    /**
     * Reset adapter
     */
    public void refreshListView() {
        listEntryOb = entryHd.getAll();
        adapter = new EntryService.EntryListAdapter(YTDictValues.appContext, listEntryOb);
        entryList.setAdapter(adapter);
    }
}
