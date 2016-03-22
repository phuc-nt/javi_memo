package com.ytasia.ytdict.dao.db_handle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ytasia.ytdict.dao.schema.YTDictSchema;

/**
 * Created by luongduy on 2/25/16.
 */
public class ytdictDbHandler extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "ytdict";
    public static final int DATABASE_VERSION = 1;

    public ytdictDbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(YTDictSchema.TBUser.SQL_CREATE_ENTRIES);
        db.execSQL(YTDictSchema.TBEntry.SQL_CREATE_ENTRIES);
        db.execSQL(YTDictSchema.TBKanji.SQL_CREATE_ENTRIES);
        db.execSQL(YTDictSchema.TBKanjiEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(YTDictSchema.TBUser.SQL_DELETE_ENTRIES);
        db.execSQL(YTDictSchema.TBKanjiEntry.SQL_DELETE_ENTRIES);
        db.execSQL(YTDictSchema.TBEntry.SQL_DELETE_ENTRIES);
        db.execSQL(YTDictSchema.TBKanji.SQL_DELETE_ENTRIES);

        onCreate(db);
    }
    public SQLiteDatabase getReadableDb() {
        return this.getReadableDatabase();
    }
    public SQLiteDatabase getWritableDb() {
        return this.getWritableDatabase();
    }
    public void dropAllTables() {
        SQLiteDatabase db = getWritableDb();
        db.execSQL(YTDictSchema.TBUser.SQL_DELETE_ENTRIES);
        db.execSQL(YTDictSchema.TBKanjiEntry.SQL_DELETE_ENTRIES);
        db.execSQL(YTDictSchema.TBEntry.SQL_DELETE_ENTRIES);
        db.execSQL(YTDictSchema.TBKanji.SQL_DELETE_ENTRIES);

        onCreate(db);
    }
}
