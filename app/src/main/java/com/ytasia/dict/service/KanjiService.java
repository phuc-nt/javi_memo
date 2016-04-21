package com.ytasia.dict.service;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ytasia.dict.dao.db_handle.TBKanjiEntryHandler;
import com.ytasia.dict.dao.obj.KanjiObj;

import java.util.ArrayList;
import java.util.List;

import ytasia.dictionary.R;

/**
 * Created by PhucNT on 16/March/22.
 */
public class KanjiService {

    /**
     * Customized Adapter for Kanji List Object
     */
    public static class KanjiListAdapter extends BaseAdapter implements Filterable {
        private final Context context;
        private final List<KanjiObj> values;
        private List<KanjiObj> filteredValues;
        private KanjiFilter kanjiFilter;

        // Constructor
        public KanjiListAdapter(Context context, List<KanjiObj> values) {
            this.context = context;
            this.values = values;
            this.filteredValues = values;

            getFilter();
        }

        @Override
        public int getCount() {
            return filteredValues.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredValues.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            KanjiObj kanjiObj = (KanjiObj) getItem(position);

            // Match ListView to Layout
            View itemView = inflater.inflate(R.layout.list_item_entry, parent, false);
            TextView entryName = (TextView) itemView.findViewById(R.id.entry_list_item_content_1);
            TextView entryLevel = (TextView) itemView.findViewById(R.id.entry_list_item_content_2);

            // Set data for ListItem
            entryName.setText(kanjiObj.toString());
            entryLevel.setText(Integer.toString(kanjiObj.getLevel()));

            return itemView;
        }

        @Override
        public Filter getFilter() {
            if (kanjiFilter == null) {
                kanjiFilter = new KanjiFilter();
            }
            return kanjiFilter;
        }

        private class KanjiFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    List<KanjiObj> tempList = new ArrayList<>();

                    for (KanjiObj obj : values) {
                        if (Character.toString(obj.getCharacter()).equals(constraint.toString())) {
                            tempList.add(obj);
                        }
                    }

                    filterResults.count = tempList.size();
                    filterResults.values = tempList;
                } else {
                    filterResults.count = values.size();
                    filterResults.values = values;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredValues = (List<KanjiObj>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    /**
     * Get all Kanji Id belongs to Entry
     *
     * @param context
     * @param entryId
     * @return List<Integer>
     */
    public List<Integer> getAllKanjiIdByEntryId(Context context, String entryId) {
        TBKanjiEntryHandler tbKanjiEntryHandler = new TBKanjiEntryHandler(context);
        return tbKanjiEntryHandler.getAllKanjiIdByEntryId(entryId);
    }
}
