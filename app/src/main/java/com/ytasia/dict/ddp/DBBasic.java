/**
 *
 */
package com.ytasia.dict.ddp;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ytasia.dict.dao.db_handle.TBEntryHandler;
import com.ytasia.dict.dao.db_handle.TBKanjiEntryHandler;
import com.ytasia.dict.dao.db_handle.TBKanjiHandler;
import com.ytasia.dict.dao.obj.EntryObj;
import com.ytasia.dict.dao.obj.KanjiEntryObj;
import com.ytasia.dict.dao.schema.YTDictSchema;
import com.ytasia.dict.service.EntryService;
import com.ytasia.dict.service.KanjiService;
import com.ytasia.dict.util.DictCache;

import android.app.Activity;
import android.content.Context;
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
    private Meteor meteor = null;
    //private static DBBasic instance;

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private static TBEntryHandler entryHandler = new TBEntryHandler(DictCache.appContext);
    private static TBKanjiHandler kanjiHandler = new TBKanjiHandler(DictCache.appContext);
    private static TBKanjiEntryHandler kanjiEntryHandler = new TBKanjiEntryHandler(DictCache.appContext);

    protected DBBasic() {
        super();
    }

    public static DBBasic getInstance() {
        /*if (instance == null) {
            instance = new DBBasic();
            instance.init();
        }
        return instance;*/

        DBBasic db = new DBBasic();
        db.init();
        return db;
    }

    public void init() {
        meteor = new Meteor(DictCache.appContext, DictCache.server_ddp);
        Log.i("DictCache.server_ddp", DictCache.server_ddp);
        meteor.addCallback(this);
        meteor.connect();
    }

    private void subscribeAll() {
        meteor.subscribe(TBKANJI_NAME);
        String[] user = new String[2];
        user[0] = DictCache.acc_type;
        user[1] = DictCache.username;
        meteor.subscribe(TBENTRY_NAME, user);
        meteor.subscribe(TBKANJIENTRY_NAME, user);
    }

    private void subscribe(String tbName) {
        String sub = null;
        if (TBKANJI_NAME.equals(tbName)) {
            sub = meteor.subscribe(TBKANJI_NAME);

        } else {

            String[] user = new String[2];
            user[0] = DictCache.acc_type;
            user[1] = DictCache.username;
            if ((TBENTRY_NAME.equals(tbName) || TBKANJIENTRY_NAME.equals(tbName)))
                sub = meteor.subscribe(tbName, user);
        }
        if (sub != null)
            DictCache.hmSubscrible.put(tbName, sub);
        Log.v("subscribe", "pull all from server");
    }

    public void update() {
        // MeteorSingleton.getInstance().update("Kanji",);

    }

    public void registerDB(String user, String pwd) {
        meteor.registerAndLogin(user, null, pwd, new ResultListener() {

            @Override
            public void onSuccess(String arg0) {
                // TODO Auto-generated method stub
                DictCache.isRegister = false;
            }

            @Override
            public void onError(String arg0, String arg1, String arg2) {
                // TODO Auto-generated method stub

            }
        });

    }

    public void loginDB(String user, String pwd) {
        meteor.loginWithUsername(user, pwd, new ResultListener() {
            @Override
            public void onSuccess(String arg0) {
                // TODO Auto-generated method stub
                //DictCache.isRegister=false;
            }

            @Override
            public void onError(String arg0, String arg1, String arg2) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void insertKanjiEntry(String kanjiId, String entryId) {
        Map<String, Object> values = new HashMap<>();
        values.put("kanjiId", kanjiId);
        values.put("entryId", entryId);
        meteor.insert(TBKANJIENTRY_NAME, values, new ResultListener() {
            @Override
            public void onSuccess(String s) {
                //subscribeAll();
                Log.i("Insert kanjientry to Server", "Success");
                subscribe(TBKANJIENTRY_NAME);
                //subscribeAll();
            }

            @Override
            public void onError(String s, String s1, String s2) {

            }
        });
    }

    public void insertEntry(EntryObj entry) {
        Map<String, Object> values = new HashMap<>();
        values.put("userId", "f" + entry.getUserId());
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
                //subscribeAll();
                Log.i("Insert entry", "Success");
                subscribe(TBENTRY_NAME);
                //subscribeAll();
            }

            @Override
            public void onError(String s, String s1, String s2) {
                Log.e("DDBasic", s + s1 + s2);

            }
        });

    }

    public void updateEntry(EntryObj entry) {
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
                subscribe(TBENTRY_NAME);
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
        //checkConnect();
//		MeteorSingleton.getInstance().insert(TBKANJI_NAME, values,new ResultListener() {
//
//			@Override
//			public void onSuccess(String arg0) {
//				// TODO Auto-generated method stub
//				Log.v("insertKanji", arg0);
//			}
//
//			@Override
//			public void onError(String arg0, String arg1, String arg2) {
//				// TODO Auto-generated method stub
//				Log.e("insertKanji", arg0 + "-" + arg1 + "-" + arg2);
//			}
//		});
        /*MeteorSingleton.getInstance().call("insertKanji", str, new ResultListener() {

			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				Log.v("insertKanji", arg0);
			}

			@Override
			public void onError(String arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				Log.e("insertKanji", arg0 + "-" + arg1 + "-" + arg2);
			}
		});*/
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

    private void checkConnect() {
        if (!MeteorSingleton.getInstance().isConnected())
            MeteorSingleton.getInstance().reconnect();
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
        //Log.i("Collection name ", collectionName);
        Log.i("onDataAdded", collectionName + " /n" + documentID + " /n" + newValuesJson);
        switch (collectionName) {
            case TBENTRY_NAME:
                EntryObj newEntry = gson.fromJson(newValuesJson, EntryObj.class);
                newEntry.setEntryId(documentID);
                entryHandler.add(newEntry, DictCache.appContext);
                Log.i("New EntryOcject", entryHandler.getAll().get(0).getEntryId());
                break;
            case TBKANJI_NAME:
                break;
            case TBKANJIENTRY_NAME:
                KanjiEntryObj newOb = gson.fromJson(newValuesJson, KanjiEntryObj.class);
                newOb.setServerId(documentID);
                kanjiEntryHandler.add(newOb);
                Log.i("New KanjiEntryObject", kanjiEntryHandler.getAll().get(0).getServerId());
                break;
        }
        disConnect();

    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson,
                              String removedValuesJson) {
        Log.i("onDataChanged", collectionName + " /n" + documentID + " /n" + updatedValuesJson);
        switch (collectionName) {
            case TBENTRY_NAME:
                Log.i("Entry Updated", updatedValuesJson);
                /*EntryObj newEntry = gson.fromJson(updatedValuesJson, EntryObj.class);
                newEntry.setEntryId(documentID);
                entryHandler.update(newEntry, documentID);*/
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
        switch (collectionName) {
            case TBENTRY_NAME:
                entryHandler.delete(DictCache.appContext, documentID);
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
