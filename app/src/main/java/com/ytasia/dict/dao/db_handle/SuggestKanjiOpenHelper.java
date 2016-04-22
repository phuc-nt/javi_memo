package com.ytasia.dict.dao.db_handle;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by PhucNT on 16/April/22.
 */
public class SuggestKanjiOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "HanViet.db";
    private static final int DATABASE_VERSION = 1;

    public SuggestKanjiOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
