package com.ytasia.dict.util;

import android.content.Context;

import com.facebook.CallbackManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ytasia.dict.dao.obj.UserObj;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by phucnt on 16/03/14.
 */
public class YTDictValues {
    public static int ENTRY_MAX_LEVEL = 5;
    public static int KANJI_MAX_LEVEL = 5;

    public static UserObj user;

    public static boolean isLogin = false;
    public static CallbackManager callbackManager;
    public static GoogleApiClient mGoogleApiClient;
    public static String gEmail;
    public static String gFullName;
    public static String gUserid;
    public static String fUserid;
    public static String guestid;

    public static String username = "";
    public static String uuid = "";
    public static boolean isRegister;
    public static String server_ddp = "ws://localhost:3000/websocket";
    public static Context appContext;
    public static String acc_type = "f";
    public static HashMap<String, String> hmSubscrible = new HashMap<>();

    public static ArrayList<String> entriesContent = new ArrayList<>();
    public static ArrayList<String> kanjiEntryIds = new ArrayList<>();

    public static RefreshInterface refreshInterface;

}
