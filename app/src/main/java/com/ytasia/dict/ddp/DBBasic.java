/**
 *
 */
package com.ytasia.dict.ddp;

import java.util.HashMap;
import java.util.Map;
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
    private static Meteor meteor = null;
    private static DBBasic instance;

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private static TBEntryHandler entryHandler = new TBEntryHandler(DictCache.appContext);
    private static TBKanjiHandler kanjiHandler = new TBKanjiHandler(DictCache.appContext);
    private static TBKanjiEntryHandler kanjiEntryHandler = new TBKanjiEntryHandler(DictCache.appContext);

    protected DBBasic() {
        super();
    }

    public static DBBasic getInstance() {
        if (instance == null) {
            instance = new DBBasic();
            instance.init();
        }
        return instance;
    }

    public void init() {// localhost:3000

        //if (!MeteorSingleton.hasInstance()) //
        // MeteorSingleton.createInstance(context,
        // "ws://192.168.1.25:3000/websocket");
        //MeteorSingleton.createInstance(DictCache.appContext, DictCache.server_ddp);

        meteor = new Meteor(DictCache.appContext, DictCache.server_ddp);
        Log.i("DictCache.server_ddp", DictCache.server_ddp);
        //
        // // register the callback that will handle events and receive messages
        //

        meteor.setCallback(this);
        // // establish the connection
        // if (!meteor.isConnected())
        // meteor.reconnect();

        //MeteorSingleton.getInstance().setCallback(getInstance());
        //onConnect(true);

    }

    private void subscribeAll() {
        meteor.subscribe(TBKANJI_NAME);
        String[] user = new String[2];
        user[0] = DictCache.acc_type;
        user[1] = DictCache.username;
        meteor.subscribe(TBENTRY_NAME, user);
        meteor.subscribe(TBKANJIENTRY_NAME, user);
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
                subscribeAll();
                Log.i("Insert entry", "Success");
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

    public void delete() {

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
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
        Log.v("onDataAdded", "table name: " + collectionName);
        Log.v("onDataAdded", "data: " + newValuesJson);


        switch (collectionName) {
            case TBENTRY_NAME:
                EntryObj newEntry = gson.fromJson(newValuesJson, EntryObj.class);
                newEntry.setEntryId(documentID);
                entryHandler.add(newEntry, DictCache.appContext);
                break;
            case TBKANJI_NAME:
                break;
            case TBKANJIENTRY_NAME:
                break;
        }

    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson,
                              String removedValuesJson) {
        switch (collectionName) {
            case TBENTRY_NAME:
                EntryObj newEntry = gson.fromJson(updatedValuesJson, EntryObj.class);
                newEntry.setEntryId(documentID);
                entryHandler.update(newEntry, documentID);
                break;
            case TBKANJI_NAME:
                break;
            case TBKANJIENTRY_NAME:
                break;
        }
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
    }

    @Override
    public void onException(Exception e) {
    }

    @Override
    public void onDisconnect(int arg0, String arg1) {
        // TODO Auto-generated method stub
        // meteor.disconnect();
    }

}
