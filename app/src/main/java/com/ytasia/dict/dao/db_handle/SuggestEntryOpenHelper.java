package com.ytasia.dict.dao.db_handle;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by phucnt on 16/03/08.
 */
public class SuggestEntryOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "NhatViet.db";
    private static final int DATABASE_VERSION = 1;

    public SuggestEntryOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
}
