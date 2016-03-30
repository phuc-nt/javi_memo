package com.ytasia.dict.util;

import com.facebook.CallbackManager;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by phucnt on 16/03/14.
 */
public class YTDictValues {
    public static  int ENTRY_MAX_LEVEL = 5;
    public static  int KANJI_MAX_LEVEL = 5;
    public static final int SQLITE_VERSION=1;

    public static boolean isLogin = false;
    public static CallbackManager callbackManager;
    public static GoogleApiClient mGoogleApiClient;
    public static String gEmail;
    public static String gFullName;
    public static String gUserid;
    public static String fUserid;
    public static String guestid;

}
