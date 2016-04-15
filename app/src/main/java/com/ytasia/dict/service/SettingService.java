package com.ytasia.dict.service;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.ytasia.dict.dao.db_handle.TBEntryHandler;
import com.ytasia.dict.dao.db_handle.TBKanjiHandler;
import com.ytasia.dict.dao.obj.EntryObj;
import com.ytasia.dict.dao.obj.KanjiObj;
import com.ytasia.dict.util.YTDictValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PhucNT on 16/March/22.
 */
public class SettingService {

    /**
     * Reset level of all Entries to 0 (default)
     */
    public void resetEntryLevel(Context context) {
        final TBEntryHandler entryHandler = new TBEntryHandler(context);
        final EntryService service = new EntryService();

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure reset level of all Entries");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<EntryObj> entryObjs = entryHandler.getAll();
                for (int i = 0; i < entryObjs.size(); i++) {
                    entryObjs.get(i).setLevel(0);
                    service.update(entryObjs.get(i));
                }
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

    /**
     * Reset level of all Kanjis to 0 (default)
     */
    public void resetKanjiLevel(Context context) {
        final TBKanjiHandler kanjiHandler = new TBKanjiHandler(context);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure reset level of all Entries");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<KanjiObj> kanjiObjs = kanjiHandler.getAll();
                for (int i = 0; i < kanjiObjs.size(); i++) {
                    kanjiObjs.get(i).setLevel(0);
                    kanjiHandler.update(kanjiObjs.get(i), kanjiObjs.get(i).getKanjiId());
                }
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

    /**
     * Clear all user entries
     */
    public void clearEntry(final Context context) {
        final TBEntryHandler entryHandler = new TBEntryHandler(context);
        final EntryService service = new EntryService();

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure clear all Entries");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<EntryObj> entryObjs = entryHandler.getAll();
                for (int i = 0; i < entryObjs.size(); i++) {
                    EntryObj obj = entryObjs.get(i);
                    service.delete(context, obj.getEntryId());
                }
                YTDictValues.entriesContent = new ArrayList<String>();
                YTDictValues.kanjiEntryIds = new ArrayList<String>();
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
