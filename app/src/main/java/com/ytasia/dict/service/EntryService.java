package com.ytasia.dict.service;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ytasia.dict.dao.db_handle.TBEntryHandler;
import com.ytasia.dict.dao.db_handle.TBKanjiEntryHandler;
import com.ytasia.dict.dao.obj.EntryObj;
import com.ytasia.dict.ddp.DBBasic;

import java.util.ArrayList;
import java.util.List;

import ytasia.dictionary.R;

/**
 * Created by PhucNT on 16/March/22.
 */
public class EntryService {

    private List<EntryObj> listEntryOb;
    private TBEntryHandler entryHd;


    /**
     * Customized Adapter for Entry List Object
     */
    public static class EntryListAdapter extends BaseAdapter implements Filterable {
        private Context context;
        private List<EntryObj> values;
        private List<EntryObj> filteredValues;
        private EntryFilter entryFilter;

        // Constructor
        public EntryListAdapter(Context context, List<EntryObj> values) {
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
            EntryObj entryObj = (EntryObj) getItem(position);

            // Match ListView to Layout
            View itemView = inflater.inflate(R.layout.list_item_entry, parent, false);
            TextView entryName = (TextView) itemView.findViewById(R.id.entry_list_item_content_1);
            TextView entryLevel = (TextView) itemView.findViewById(R.id.entry_list_item_content_2);

            // Set data for ListItem
            entryName.setText(entryObj.toString());
            entryLevel.setText(Integer.toString(entryObj.getLevel()));


            return itemView;
        }

        @Override
        public Filter getFilter() {
            if (entryFilter == null) {
                entryFilter = new EntryFilter();
            }
            return entryFilter;
        }

        private class EntryFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    List<EntryObj> tempList = new ArrayList<>();

                    for (EntryObj obj : values) {
                        if (obj.getContent().contains(constraint.toString())) {
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
                filteredValues = (List<EntryObj>) results.values;
                notifyDataSetChanged();
            }
        }
    }


    /**
     * Delete Entry on ListView by position
     *
     * @param context
     * @param id
     * @return adapter after delete
     */
    public void delete(Context context, String id) {
        DBBasic db = DBBasic.getInstance();

        TBKanjiEntryHandler kanjiEntryHandler = new TBKanjiEntryHandler(context);
        List<String> serverIds = kanjiEntryHandler.getAllServerIdByEntryId(id);

        for (int i = 0; i < serverIds.size(); i++) {
            db.deleteKanjiEntry(serverIds.get(i));
        }

        db.deleteEntry(id);
    }

    /**
     * Add new Entry to database by Entry Object.
     * Also get all Kanji on new Entry and add new Entry to database
     *
     * @param obj
     * @return ID of new entry
     */
    public void add(EntryObj obj) {
        DBBasic db = DBBasic.getInstance();
        db.insertEntry(obj);
    }

    /**
     * @param obj
     */
    public void update(EntryObj obj) {
        DBBasic db = DBBasic.getInstance();
        db.updateEntry(obj);
    }
}
