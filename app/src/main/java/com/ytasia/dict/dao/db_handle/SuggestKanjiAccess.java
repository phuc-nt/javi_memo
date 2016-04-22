package com.ytasia.dict.dao.db_handle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PhucNT on 16/April/22.
 */
public class SuggestKanjiAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static SuggestKanjiAccess instance;
    private static final String SUGGEST_TABLE_NAME = "dict";
    private static final String SUGGEST_TABLE_WORD_COLUMN_NAME = "Word";

    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private SuggestKanjiAccess(Context context) {
        this.openHelper = new SuggestKanjiOpenHelper(context);
    }

    /**
     * Return a singleton instance of SuggestKanjiAccess.
     *
     * @param context the Context
     * @return the instance of SuggestKanjiAccess
     */
    public static SuggestKanjiAccess getInstance(Context context) {
        if (instance == null) {
            instance = new SuggestKanjiAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public String getSuggestMeaning(String word) {
        String result = "No Suggest";
        Cursor cursor = database.rawQuery("select * from " + SUGGEST_TABLE_NAME
                + " where " + SUGGEST_TABLE_WORD_COLUMN_NAME
                + " = '" + word + "'", null);
        //System.out.println("Cursor count: " + cursor.getCount());
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("Meaning"));
        }
        return result;
    }
}
