package com.ytasia.dict.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ytasia.dict.dao.db_handle.TBKanjiEntryHandler;
import com.ytasia.dict.dao.obj.KanjiObj;

import java.util.List;

import ytasia.dictionary.R;

/**
 * Created by PhucNT on 16/March/22.
 */
public class KanjiService {

    /**
     * Customized Adapter for Kanji List Object
     */
    public static class KanjiListAdapter extends ArrayAdapter<KanjiObj> {
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
     * Get all Kanji Id belongs to Entry
     *
     * @param context
     * @param entryId
     * @return List<Integer>
     */
    public List<Integer> getAllKanjiIdByEntryId(Context context, int entryId) {
        TBKanjiEntryHandler tbKanjiEntryHandler = new TBKanjiEntryHandler(context);
        return tbKanjiEntryHandler.getAllKanjiIdByEntryId(entryId);
    }
}
