package com.ytasia.dict.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ytasia.dict.dao.db_handle.TBEntryHandler;
import com.ytasia.dict.dao.db_handle.TBKanjiEntryHandler;
import com.ytasia.dict.dao.obj.EntryObj;
import com.ytasia.dict.ddp.DBBasic;
import com.ytasia.dict.util.DictCache;

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
    public static class EntryListAdapter extends ArrayAdapter<EntryObj> {
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
     * @param context
     * @param id
     * @return adapter after delete
     */
    public void delete(Context context, String id) {
        entryHd = new TBEntryHandler(context);

        // Delete all data related in this object on "KanjiEntry table"
        /*TBKanjiEntryHandler tbKanjiEntryHandler = new TBKanjiEntryHandler(context);
        List<Integer> list = tbKanjiEntryHandler.getAllKanjiIdByEntryId(id);
        for (int j = 0; j < list.size(); j++) {
            tbKanjiEntryHandler.delete(list.get(j), id);
        }*/
        // Delete on database
        entryHd.delete(context, id);
    }

    /**
     * Add new Entry to database by Entry Object.
     * Also get all Kanji on new Entry and add new Entry to database
     *
     * @param context
     * @param obj
     * @return ID of new entry
     */
    public void add(Context context, EntryObj obj) {
        DBBasic db = DBBasic.getInstance();
        db.insertEntry(obj);
    }

    /**
     * @param obj
     * @param id
     */
    public void update(EntryObj obj, String id) {
        entryHd = new TBEntryHandler(DictCache.appContext);
        entryHd.update(obj, id);
    }
}
