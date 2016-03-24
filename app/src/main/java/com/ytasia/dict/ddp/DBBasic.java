/**
 * 
 */
package com.ytasia.dict.ddp;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
 *
 */
public class DBBasic implements MeteorCallback {
	private static final String TBKANJI_NAME = "tbKanji";
	private static final String TBENTRY_NAME = "tbEntry";
	private static final String TBKANJIENTRY_NAME = "tbKanjiEntry";
	private static Meteor meteor = null;
	private static DBBasic instance;

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
		System.out.println("DictCache.server_ddp="+DictCache.server_ddp);
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

	private void subscribeKanji() {
//		MeteorSingleton.getInstance().subscribe(TBKANJI_NAME);
//		MeteorSingleton.getInstance().subscribe(TBENTRY_NAME);
//		MeteorSingleton.getInstance().subscribe(TBKANJIENTRY_NAME);
		meteor.subscribe(TBKANJI_NAME);
		meteor.subscribe(TBENTRY_NAME);
		meteor.subscribe(TBKANJIENTRY_NAME);
	}

	public void update() {
		// MeteorSingleton.getInstance().update("Kanji",);

	}

	public void registerDB(String user, String pwd) {
		meteor.registerAndLogin(user, null, pwd, new ResultListener() {

			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				DictCache.isRegister=false;
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
		subscribeKanji();
		System.out.println("onConnect="+signedInAutomatically);
	}

	@Override
	public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
		Log.v("onDataAdded", "collectionName" + collectionName);
	}

	@Override
	public void onDataChanged(String collectionName, String documentID, String updatedValuesJson,
			String removedValuesJson) {
	}

	@Override
	public void onDataRemoved(String collectionName, String documentID) {
	}

	@Override
	public void onException(Exception e) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see im.delight.android.ddp.MeteorCallback#onDisconnect(int,
	 * java.lang.String)
	 */
	@Override
	public void onDisconnect(int arg0, String arg1) {
		// TODO Auto-generated method stub
		// meteor.disconnect();
	}

}
