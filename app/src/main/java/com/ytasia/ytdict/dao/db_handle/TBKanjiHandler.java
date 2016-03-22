package com.ytasia.ytdict.dao.db_handle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ytasia.ytdict.dao.obj.KanjiObj;

import java.util.ArrayList;
import java.util.List;

import com.ytasia.ytdict.dao.schema.YTDictSchema;

/**
 * Created by luongduy on 3/2/16.
 */
public class TBKanjiHandler extends ytdictDbHandler {
    public TBKanjiHandler(Context context) {
        super(context);
    }

    /**
     * This method is used get a list of all Kanji
     *
     * @return List: This return of list of KanjiObj objects
     */
    public List<KanjiObj> getAll() {
        List<KanjiObj> list = new ArrayList<KanjiObj>();
        String query = "SELECT * FROM " + YTDictSchema.TBKanji.TABLE_NAME;
        SQLiteDatabase db = getReadableDb();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                KanjiObj obj = getObjFromCursor(cursor);
                list.add(obj);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    /**
     * get KanjiObj by Id
     *
     * @param id The Id of the object
     * @return KanjiObj This return the object that has kanjiId of id
     */
    public KanjiObj getById(int id) {
        SQLiteDatabase db = getReadableDb();
        String query = "SELECT * FROM " + YTDictSchema.TBKanji.TABLE_NAME +
                " WHERE " + YTDictSchema.TBKanji.COLUMN_NAME_KANJI_ID + "=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) cursor.moveToFirst();
        else return null;
        db.close();
        return getObjFromCursor(cursor);
    }

    /**
     * get KanjiObj by Character
     *
     * @param kanji The Character of the object
     * @return KanjiObj This return the object that has kanjiCharacer of kanji
     */
    public KanjiObj getByCharater(char kanji) {
        SQLiteDatabase db = getReadableDb();
        String query = "SELECT * FROM " + YTDictSchema.TBKanji.TABLE_NAME +
                " WHERE " + YTDictSchema.TBKanji.COLUMN_NAME_CHARACTER + "='" + kanji + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) cursor.moveToFirst();
        else return null;
        db.close();
        return getObjFromCursor(cursor);
    }

    /**
     * add an KanjiObj to Kanji table
     *
     * @param obj The KanjiObj
     */
    public int add(KanjiObj obj) {
        SQLiteDatabase db = getWritableDb();
        ContentValues values = generateContentValues(obj);
        // Inserting Row
        Long id = db.insert(YTDictSchema.TBKanji.TABLE_NAME, null, values);
        Integer intId = id.intValue();
        db.close(); // Closing database connection
        return intId;
    }

    /**
     * Update entry on database by object and id
     *
     * @param obj
     * @param id
     */
    public void update(KanjiObj obj, int id) {
        SQLiteDatabase db = getWritableDb();
        ContentValues values = generateContentValues(obj);
        // Updating Row
        db.update(YTDictSchema.TBKanji.TABLE_NAME, values, YTDictSchema.TBKanji.COLUMN_NAME_KANJI_ID + "=" + id, null);
        db.close(); // Closing database connection
    }

    /***
     * Local Functions
     ***/

    private KanjiObj getObjFromCursor(Cursor cursor) {
        KanjiObj obj = new KanjiObj();
        int index;
        index = cursor.getColumnIndex(YTDictSchema.TBKanji.COLUMN_NAME_KANJI_ID);
        obj.setKanjiId(cursor.getInt(index));
        index = cursor.getColumnIndex(YTDictSchema.TBKanji.COLUMN_NAME_CHARACTER);
        obj.setCharacter(cursor.getString(index).charAt(0));
        index = cursor.getColumnIndex(YTDictSchema.TBKanji.COLUMN_NAME_KUNYOMI);
        obj.setKunyomi(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBKanji.COLUMN_NAME_ONYOMI);
        obj.setOnyomi(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBKanji.COLUMN_NAME_MEANING);
        obj.setMeaning(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBKanji.COLUMN_NAME_HANVIET);
        obj.setHanviet(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBKanji.COLUMN_NAME_ASSOCATED_WORD);
        obj.setAssociated(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBKanji.COLUMN_NAME_LEVEL);
        obj.setLevel(cursor.getInt(index));
        return obj;
    }

    /**
     * Generating the ContentValues to be inserted to db from user obj
     */
    private ContentValues generateContentValues(KanjiObj obj) {
        ContentValues values = new ContentValues();
        //values.put(YTDictSchema.TBKanji.COLUMN_NAME_KANJI_ID, obj.getKanjiId());
        values.put(YTDictSchema.TBKanji.COLUMN_NAME_CHARACTER, Character.toString(obj.getCharacter()));
        values.put(YTDictSchema.TBKanji.COLUMN_NAME_KUNYOMI, obj.getKunyomi());
        values.put(YTDictSchema.TBKanji.COLUMN_NAME_ONYOMI, obj.getOnyomi());
        values.put(YTDictSchema.TBKanji.COLUMN_NAME_MEANING, obj.getMeaning());
        values.put(YTDictSchema.TBKanji.COLUMN_NAME_HANVIET, obj.getHanviet());
        values.put(YTDictSchema.TBKanji.COLUMN_NAME_ASSOCATED_WORD, obj.getAssociated());
        values.put(YTDictSchema.TBKanji.COLUMN_NAME_LEVEL, obj.getLevel());
        return values;
    }

    /**
     * get Delete Kanji object by Id
     *
     * @param id The Id of the object
     */
    public void delete(int id) {
        SQLiteDatabase db = getWritableDb();
        // Deleting Row
        db.delete(YTDictSchema.TBKanji.TABLE_NAME, YTDictSchema.TBKanji.COLUMN_NAME_KANJI_ID + "=" + id, null);
        db.close(); // Closing database connection
    }
}
