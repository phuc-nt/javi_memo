/**
 *
 */
package com.ytasia.dict.util;

import android.content.Context;

import java.util.HashMap;

/**
 * @author truongnguyen
 */
public class DictCache {
    public static String username = "";
    public static String uuid = "";
    public static boolean isRegister;
    public static String server_ddp = "ws://localhost:3000/websocket";
    public static Context appContext;
    public static String acc_type = "f";
    public static HashMap<String, String> hmSubscrible = new HashMap<>();
}
