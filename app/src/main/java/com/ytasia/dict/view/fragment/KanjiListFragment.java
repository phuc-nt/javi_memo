package com.ytasia.dict.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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

import android.widget.ListView;
;import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ytasia.dict.dao.db_handle.TBKanjiHandler;
import com.ytasia.dict.dao.obj.KanjiObj;
import com.ytasia.dict.service.KanjiService;

import com.ytasia.dict.view.activity.KanjiViewActivity;
import ytasia.dictionary.R;


public class KanjiListFragment extends Fragment {

    private Toolbar toolbar;
    private ListView kanjiList;
    List<String> list = new ArrayList<>();
    TBKanjiHandler kanjiHd;
    List<KanjiObj> listKanjiOb;
    KanjiService.KanjiListAdapter adapter;
    KanjiService kanjiService;


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
        adapter = new KanjiService.KanjiListAdapter(getActivity(), listKanjiOb);

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
        MenuItem sortByDateItem = menu.findItem(R.id.toolbar_sort_by_date);

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
                adapter = new KanjiService.KanjiListAdapter(getActivity(), listKanjiOb);
                // set adapter to ListView
                kanjiList.setAdapter(adapter);
                break;

            // Sort by A->Z
            case R.id.toolbar_sort_by_alphabet:
                Collections.sort(listKanjiOb, new Comparator<KanjiObj>() {
                    @Override
                    public int compare(KanjiObj ob1, KanjiObj ob2) {
                        return (ob1.getCharacter() - ob2.getCharacter());
                    }
                });
                // Set sorted data to adapter (customized)
                adapter = new KanjiService.KanjiListAdapter(getActivity(), listKanjiOb);
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
                adapter = new KanjiService.KanjiListAdapter(getActivity(), listKanjiOb);
                kanjiList.setAdapter(adapter);
        }
        return super.onOptionsItemSelected(item);
    }


}
