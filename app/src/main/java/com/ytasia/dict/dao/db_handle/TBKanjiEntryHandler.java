package com.ytasia.dict.dao.db_handle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.ytasia.dict.dao.obj.KanjiEntryObj;
import com.ytasia.dict.dao.schema.YTDictSchema;

/**
 * Created by luongduy on 3/2/16.
 */
public class TBKanjiEntryHandler extends YTDictDbHandler {
    public TBKanjiEntryHandler(Context context) {
        super(context);
    }

    /**
     * This method is used get a list of all KanjiEntry obj
     *
     * @return List: This return of list of objects
     */
    public List<KanjiEntryObj> getAll() {
        List<KanjiEntryObj> list = new ArrayList<KanjiEntryObj>();
        String query = "SELECT * FROM " + YTDictSchema.TBKanjiEntry.TABLE_NAME;
        SQLiteDatabase db = getReadableDb();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                KanjiEntryObj obj = getObjFromCursor(cursor);
                list.add(obj);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<Integer> getAllKanjiIdByEntryId(String entryId) {
        List<Integer> list = new ArrayList<>();
        String query = "SELECT * FROM " + YTDictSchema.TBKanjiEntry.TABLE_NAME
                + " WHERE " + YTDictSchema.TBKanjiEntry.COLUMN_NAME_ENTRY_ID + " = '" + entryId + "'";
        SQLiteDatabase db = getReadableDb();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getInt(cursor.getColumnIndex(YTDictSchema.TBKanjiEntry.COLUMN_NAME_KANJI_ID)));
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public void delete(int kanjiId, String entryId) {
        String query = "DELETE FROM " + YTDictSchema.TBKanjiEntry.TABLE_NAME
                + " WHERE " + YTDictSchema.TBKanjiEntry.COLUMN_NAME_ENTRY_ID + " = '" + entryId + "'"
                + " AND " + YTDictSchema.TBKanjiEntry.COLUMN_NAME_KANJI_ID + " = " + kanjiId;
        SQLiteDatabase db = getReadableDb();
        db.execSQL(query);
        db.close();
    }

    /**
     * get KanjiEntryObj by Id
     *
     * @param id The Id of the object
     * @return KanjiEntryObj This return the object that has id
     */
    public KanjiEntryObj getByKanjiId(int id) {
        SQLiteDatabase db = getReadableDb();
        String query = "SELECT * FROM " + YTDictSchema.TBKanjiEntry.TABLE_NAME +
                " WHERE " + YTDictSchema.TBKanjiEntry.COLUMN_NAME_KANJI_ID + "=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) cursor.moveToFirst();
        else return null;
        db.close();
        return getObjFromCursor(cursor);
    }

    /**
     * add an KanjiEntryObj to table
     *
     * @param obj The KanjiEntryObj
     */
    public int add(KanjiEntryObj obj) {
        SQLiteDatabase db = getWritableDb();
        ContentValues values = generateContentValues(obj);
        // Inserting Row
        Long id = db.insert(YTDictSchema.TBKanjiEntry.TABLE_NAME, null, values);
        Integer intId = id.intValue();
        db.close(); // Closing database connection
        return intId;
    }

    /***
     * Local Functions
     ***/

    private KanjiEntryObj getObjFromCursor(Cursor cursor) {
        KanjiEntryObj obj = new KanjiEntryObj();
        int index;
        index = cursor.getColumnIndex(YTDictSchema.TBKanjiEntry.COLUMN_NAME_SERVER_ID);
        obj.setServerId(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBKanjiEntry.COLUMN_NAME_KANJI_ID);
        obj.setKanjiId(cursor.getInt(index));
        index = cursor.getColumnIndex(YTDictSchema.TBKanjiEntry.COLUMN_NAME_ENTRY_ID);
        obj.setEntryId(cursor.getString(index));
        return obj;
    }

    /**
     * Generating the ContentValues to be inserted to db from KanjiEntryObj obj
     */
    private ContentValues generateContentValues(KanjiEntryObj obj) {
        ContentValues values = new ContentValues();
        values.put(YTDictSchema.TBKanjiEntry.COLUMN_NAME_SERVER_ID, obj.getServerId());
        values.put(YTDictSchema.TBKanjiEntry.COLUMN_NAME_ENTRY_ID, obj.getEntryId());
        values.put(YTDictSchema.TBKanjiEntry.COLUMN_NAME_KANJI_ID, obj.getKanjiId());
        return values;
    }
}
