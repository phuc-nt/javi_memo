package com.ytasia.dict.dao.db_handle;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by phucnt on 16/03/08.
 */
public class SuggestDataOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "ja-vi.db";
    private static final int DATABASE_VERSION = 1;

    public SuggestDataOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
