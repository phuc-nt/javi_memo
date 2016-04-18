/**
 *
 */
package com.ytasia.dict.ddp;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ytasia.dict.dao.db_handle.TBEntryHandler;
import com.ytasia.dict.dao.db_handle.TBKanjiEntryHandler;
import com.ytasia.dict.dao.db_handle.TBKanjiHandler;
import com.ytasia.dict.dao.obj.EntryObj;
import com.ytasia.dict.dao.obj.KanjiEntryObj;
import com.ytasia.dict.util.YTDictValues;
import com.ytasia.dict.view.fragment.EntryListFragment;

import android.util.Log;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;

/**
 * @author truongnguyen
 */
public class DBBasic implements MeteorCallback {
    private static final String TBKANJI_NAME = "tbKanji";
    private static final String TBENTRY_NAME = "tbEntry";
    private static final String TBKANJIENTRY_NAME = "tbKanjiEntry";
    private static final String CHECKDATAFUNCTION = "checkEmpty";
    private Meteor meteor = null;
    //private static DBBasic instance;

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private static TBEntryHandler entryHandler = new TBEntryHandler(YTDictValues.appContext);
    private static TBKanjiEntryHandler kanjiEntryHandler = new TBKanjiEntryHandler(YTDictValues.appContext);


    public DBBasic() {
        super();
    }

    /**
     * Initial new DBBasic object
     *
     * @return DBBasic
     */
    public static DBBasic getInstance() {
        DBBasic db = new DBBasic();
        db.init();
        return db;
    }

    /**
     * Check on server, if data available, get all data to device,
     * if NOT, do nothing
     */
    public void checkServerData() {
        String[] user = new String[2];
        user[0] = YTDictValues.acc_type;
        user[1] = YTDictValues.username;
        meteor.call(CHECKDATAFUNCTION, user, new ResultListener() {
            @Override
            public void onSuccess(String s) {
                Log.i("Server data", s);
                if ("1".equals(s)) {
                    subscribe(TBENTRY_NAME);
                }
            }

            @Override
            public void onError(String s, String s1, String s2) {
            }
        });

    }

    /**
     * Initial meteor
     */
    public void init() {
        meteor = new Meteor(YTDictValues.appContext, YTDictValues.server_ddp);
        meteor.addCallback(this);
        meteor.connect();
    }

    /**
     * Subscribe data on server to get data immediately when data on server changed
     *
     * @param tbName table name
     */
    public void subscribe(String tbName) {
        String sub = null;
        if (TBKANJI_NAME.equals(tbName)) {
            sub = meteor.subscribe(TBKANJI_NAME);

        } else {

            String[] user = new String[2];
            user[0] = YTDictValues.acc_type;
            user[1] = YTDictValues.username;
            if ((TBENTRY_NAME.equals(tbName) || TBKANJIENTRY_NAME.equals(tbName)))
                sub = meteor.subscribe(tbName, user);
        }
        if (sub != null)
            YTDictValues.hmSubscrible.put(tbName, sub);
        Log.v("subscribe", "pull all from server");
    }

    /**
     * Insert new entry to server
     *
     * @param entry
     */
    public void insertEntry(final EntryObj entry) {
        Map<String, Object> values = new HashMap<>();

        if (YTDictValues.fUserid != null) {
            values.put("userId", "f" + entry.getUserId());
        } else if (YTDictValues.gUserid != null) {
            values.put("userId", "g" + entry.getUserId());
        } else {
            values.put("userId", "_" + entry.getUserId());
        }
        values.put("content", entry.getContent());
        values.put("furigana", entry.getFurigana());
        values.put("meaning", entry.getMeaning());
        values.put("example", entry.getExample());
        values.put("level", entry.getLevel());
        values.put("source", entry.getSource());
        values.put("createdDate", entry.getCreatedDate());
        meteor.insert(TBENTRY_NAME, values, new ResultListener() {
            @Override
            public void onSuccess(String s) {
                Log.i("Insert entry", "Success : " + s);
                //YTDictValues.refreshInterface.init(TBENTRY_NAME);

                subscribe(TBENTRY_NAME);
            }

            @Override
            public void onError(String s, String s1, String s2) {
                Log.e("DDBasic", s + s1 + s2);

            }
        });
    }

    /**
     * Update entry on server
     *
     * @param entry
     */
    public void updateEntry(final EntryObj entry) {
        Map<String, Object> query = new HashMap<>();
        query.put("_id", entry.getEntryId());

        Map<String, Object> values = new HashMap<>();

        values.put("userId", entry.getUserId());
        values.put("content", entry.getContent());
        values.put("furigana", entry.getFurigana());
        values.put("meaning", entry.getMeaning());
        values.put("example", entry.getExample());
        values.put("level", entry.getLevel());
        values.put("source", entry.getSource());
        values.put("createdDate", entry.getCreatedDate());
        meteor.update(TBENTRY_NAME, query, values, null, new ResultListener() {
            @Override
            public void onSuccess(String s) {
                Log.i("Update entry", "Success");
                entryHandler.update(entry, entry.getEntryId());
                if (YTDictValues.refreshInterface != null) {
                    YTDictValues.refreshInterface.refreshListView();
                }
            }

            @Override
            public void onError(String s, String s1, String s2) {

            }
        });
    }

    /**
     * Delete entry on server
     *
     * @param entryId
     */
    public void deleteEntry(final String entryId) {
        meteor.remove(TBENTRY_NAME, entryId, new ResultListener() {
            @Override
            public void onSuccess(String s) {
                Log.i("Delete entry", "Success");
                String deleteEntry = entryHandler.getById(entryId).getContent();
                entryHandler.delete(entryId);
                YTDictValues.entriesContent.remove(deleteEntry);
                if (YTDictValues.refreshInterface != null) {
                    YTDictValues.refreshInterface.refreshListView();
                }
            }

            @Override
            public void onError(String s, String s1, String s2) {

            }
        });
    }

    /**
     * Insert new Kanji-Entry constrain on server
     *
     * @param kanjiId
     * @param entryId
     */
    public void insertKanjiEntry(String kanjiId, String entryId) {
        Map<String, Object> values = new HashMap<>();
        values.put("kanjiId", kanjiId);
        values.put("entryId", entryId);
        meteor.insert(TBKANJIENTRY_NAME, values, new ResultListener() {
            @Override
            public void onSuccess(String s) {
                Log.i("Insert Kanji-Entry to Server", "Success");
                subscribe(TBKANJIENTRY_NAME);
            }

            @Override
            public void onError(String s, String s1, String s2) {

            }
        });
    }

    /**
     * Delete Kanji-Entry constrain on server
     *
     * @param id
     */
    public void deleteKanjiEntry(final String id) {
        meteor.remove(TBKANJIENTRY_NAME, id, new ResultListener() {
            @Override
            public void onSuccess(String s) {
                Log.i("Delete kanji-entry", "Success");
                kanjiEntryHandler.delete(id);
                YTDictValues.kanjiEntryIds.remove(id);
            }

            @Override
            public void onError(String s, String s1, String s2) {

            }
        });
    }

    public void insertKanji(String kanji, String onyomi, String kunyomi, String hanviet, String meaning, String level) {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("character", kanji);
        values.put("kanjiId", "kanji-id");
        values.put("onyomi", onyomi);
        values.put("kunyomi", kunyomi);
        values.put("hanviet", "");
        values.put("meaning", meaning);
        values.put("associated", "");
        values.put("level", level);
        String[] str = new String[6];
        str[0] = kanji;
        str[1] = onyomi;
        str[2] = kunyomi;
        str[3] = hanviet;
        str[4] = meaning;
        str[5] = level;
        meteor.insert(TBKANJI_NAME, values);
    }

    public void updateKanji(String kanji, String onyomi, String kunyomi, String hanviet, String meaning, int level) {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("character", kanji);
        values.put("kanjiId", "kanji-id");
        values.put("onyomi", onyomi);
        values.put("kunyomi", kunyomi);
        values.put("hanviet", "");
        values.put("meaning", meaning);
        values.put("associated", "");
        values.put("level", level);
        meteor.insert(TBKANJI_NAME, values);
        //checkConnect();
        // MeteorSingleton.getInstance().update(TBKANJI_NAME, query, values);
    }

    public void disConnect() {

        if (meteor != null && meteor.isConnected())
            meteor.disconnect();
    }

    @Override
    public void onConnect(boolean signedInAutomatically) {
        Log.v("onConnect", "sign automaticaly " + signedInAutomatically);
        // subscribeAll();
        System.out.println("onConnect=" + signedInAutomatically);
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
        Log.i("onDataAdded", collectionName + "\n" + documentID + "\n" + newValuesJson);
        switch (collectionName) {
            case TBENTRY_NAME:
                EntryObj newEntry = gson.fromJson(newValuesJson, EntryObj.class);
                newEntry.setEntryId(documentID);

                if (!YTDictValues.entriesContent.contains(newEntry.getContent())) {
                    entryHandler.add(newEntry, YTDictValues.appContext);
                    YTDictValues.entriesContent.add(newEntry.getContent());
                }
                break;
            case TBKANJI_NAME:
                break;
            case TBKANJIENTRY_NAME:
                KanjiEntryObj newOb = gson.fromJson(newValuesJson, KanjiEntryObj.class);
                newOb.setServerId(documentID);

                if (!YTDictValues.kanjiEntryIds.contains(newOb.getServerId())) {
                    kanjiEntryHandler.add(newOb);
                    YTDictValues.kanjiEntryIds.add(newOb.getServerId());
                }
                break;
        }
        disConnect();

        if (YTDictValues.refreshInterface != null) {
            YTDictValues.refreshInterface.refreshListView();
        }
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson,
                              String removedValuesJson) {
        Log.i("onDataChanged", collectionName + "\n" + documentID + "\n" + updatedValuesJson);
        switch (collectionName) {
            case TBENTRY_NAME:
                Log.i("Entry Updated", updatedValuesJson);

                break;
            case TBKANJI_NAME:
                break;
            case TBKANJIENTRY_NAME:
                break;
        }
        disConnect();
    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {
        Log.i("onDataRemoved", collectionName + "\n" + documentID);
        switch (collectionName) {
            case TBENTRY_NAME:
                entryHandler.delete(documentID);
                break;
            case TBKANJI_NAME:
                break;
            case TBKANJIENTRY_NAME:
                break;
        }
        disConnect();
    }

    @Override
    public void onException(Exception e) {
    }

}
