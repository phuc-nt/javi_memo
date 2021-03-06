package com.ytasia.dict.dao.db_handle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ytasia.dict.service.KanjiEntryService;
import com.ytasia.dict.util.JapaneseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.ytasia.dict.dao.obj.EntryObj;
import com.ytasia.dict.dao.obj.KanjiEntryObj;
import com.ytasia.dict.dao.obj.KanjiObj;
import com.ytasia.dict.dao.schema.YTDictSchema;
import com.ytasia.dict.util.YTDictValues;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by luongduy on 2/26/16.
 */
public class TBEntryHandler extends YTDictDbHandler {
    private Context context;

    public TBEntryHandler(Context context) {
        super(context);
    }

    /**
     * This method is used get a list of all Entry
     *
     * @return List: This return of list of EntryObj objects
     */
    public List<EntryObj> getAll() {
        String user = YTDictValues.username;
        if (YTDictValues.fUserid != null) { // facebook user
            user = "f" + user;
        } else if (YTDictValues.gUserid != null) { // google user
            user = "g" + user;
        }

        List<EntryObj> list = new ArrayList<EntryObj>();
        String query = "SELECT * FROM " + YTDictSchema.TBEntry.TABLE_NAME
                + " WHERE " + YTDictSchema.TBEntry.COLUMN_NAME_USER_ID + " = '" + user + "'";
        SQLiteDatabase db = getReadableDb();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                EntryObj obj = getObjFromCursor(cursor);
                list.add(obj);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    /**
     * This method is used get a list of all Entry
     *
     * @return List: This return of list of EntryObj objects
     */
    public List<EntryObj> getAllWithout(String content) {
        List<EntryObj> list = new ArrayList<EntryObj>();
        String query = "SELECT * FROM " + YTDictSchema.TBEntry.TABLE_NAME +
                " WHERE " + YTDictSchema.TBEntry.COLUMN_NAME_CONTENT + " != " + "'" + content + "'";
        SQLiteDatabase db = getReadableDb();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                EntryObj obj = getObjFromCursor(cursor);
                list.add(obj);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    /**
     * This method is used get a list of all Entry have level < max level (for quiz)
     *
     * @param maxLevel
     * @return List: This return of list of EntryObj objects
     */
    public List<EntryObj> getQuizData(int maxLevel) {
        List<EntryObj> list = new ArrayList<EntryObj>();
        String query = "SELECT * FROM " + YTDictSchema.TBEntry.TABLE_NAME +
                " WHERE " + YTDictSchema.TBEntry.COLUMN_NAME_LEVEL + " < " + maxLevel;
        SQLiteDatabase db = getReadableDb();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                EntryObj obj = getObjFromCursor(cursor);
                list.add(obj);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    /**
     * get EntryObj by Id
     *
     * @param id The Id of the object
     * @return EntryObj This return the object that has entryId of id
     */
    public EntryObj getById(String id) {
        SQLiteDatabase db = getReadableDb();
        String query = "SELECT * FROM " + YTDictSchema.TBEntry.TABLE_NAME +
                " WHERE " + YTDictSchema.TBEntry.COLUMN_NAME_ENTRY_ID + "= '" + id + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) cursor.moveToFirst();
        else return null;
        db.close();
        return getObjFromCursor(cursor);
    }

    /**
     * add an EntryObj to entry table
     *
     * @param obj The EntryObj
     */
    public int add(EntryObj obj, Context context) {
        SQLiteDatabase db = getWritableDb();
        ContentValues values = generateContentValues(obj);

        // Inserting Row
        Long id = db.insert(YTDictSchema.TBEntry.TABLE_NAME, null, values);
        Integer intId = id.intValue();
        db.close(); // Closing database connection

        if (intId != -1) {
            getAndAddKanjiFromNewEntry(context, obj.getContent(), obj.getEntryId());
            Log.i("Add to SQLite", "success");
        } else {
            Log.i("Add to SQLite", "failed");
        }

        return intId;
    }

    public void update(EntryObj obj, String id) {
        SQLiteDatabase db = getWritableDb();
        ContentValues values = generateContentValues(obj);
        // Updating Row
        db.update(YTDictSchema.TBEntry.TABLE_NAME, values, YTDictSchema.TBEntry.COLUMN_NAME_ENTRY_ID + "= '" + id + "'", null);
        db.close(); // Closing database connection
    }

    public void delete(String id) {
        SQLiteDatabase db = getWritableDb();

        // Deleting Row
        db.delete(YTDictSchema.TBEntry.TABLE_NAME, YTDictSchema.TBEntry.COLUMN_NAME_ENTRY_ID + "= '" + id + "'", null);
        db.close(); // Closing database connection
    }

    /***
     * Local Functions
     ***/

    private EntryObj getObjFromCursor(Cursor cursor) {
        EntryObj obj = new EntryObj();
        int index;
        index = cursor.getColumnIndex(YTDictSchema.TBEntry.COLUMN_NAME_ENTRY_ID);
        obj.setEntryId(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBEntry.COLUMN_NAME_USER_ID);
        obj.setUserId(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBEntry.COLUMN_NAME_FURIGANA);
        obj.setFurigana(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBEntry.COLUMN_NAME_CONTENT);
        obj.setContent(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBEntry.COLUMN_NAME_MEANING);
        obj.setMeaning(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBEntry.COLUMN_NAME_EXAMPLE);
        obj.setExample(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBEntry.COLUMN_NAME_SOURCE);
        obj.setSource(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBEntry.COLUMN_NAME_LEVEL);
        obj.setLevel(cursor.getInt(index));
        index = cursor.getColumnIndex(YTDictSchema.TBEntry.COLUMN_NAME_CREATED_DATE);
        obj.setCreatedDate(java.sql.Date.valueOf(cursor.getString(index)));
        return obj;
    }

    /**
     * Generating the ContentValues to be inserted to db from entry obj
     */
    private ContentValues generateContentValues(EntryObj obj) {
        ContentValues values = new ContentValues();
        values.put(YTDictSchema.TBEntry.COLUMN_NAME_ENTRY_ID, obj.getEntryId());
        values.put(YTDictSchema.TBEntry.COLUMN_NAME_USER_ID, obj.getUserId());
        values.put(YTDictSchema.TBEntry.COLUMN_NAME_CONTENT, obj.getContent());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN);
        values.put(YTDictSchema.TBEntry.COLUMN_NAME_CREATED_DATE, dateFormat.format(obj.getCreatedDate()));
        values.put(YTDictSchema.TBEntry.COLUMN_NAME_EXAMPLE, obj.getExample());
        values.put(YTDictSchema.TBEntry.COLUMN_NAME_FURIGANA, obj.getFurigana());
        values.put(YTDictSchema.TBEntry.COLUMN_NAME_MEANING, obj.getMeaning());
        values.put(YTDictSchema.TBEntry.COLUMN_NAME_LEVEL, obj.getLevel());
        values.put(YTDictSchema.TBEntry.COLUMN_NAME_SOURCE, obj.getSource());
        return values;
    }

    private void getAndAddKanjiFromNewEntry(Context context, String newEntryContent, String newEntryId) {
        // Get all Kanji available on new Entry
        JapaneseHandler jpHd = new JapaneseHandler();
        String kanji = jpHd.getAllKanji(newEntryContent);

        // Add new kanji to database one-by-one
        int cpLength = jpHd.getCodePointLength(kanji);
        for (int i = 0; i < cpLength; ++i) {
            // Get unique kanji from all kanji
            KanjiObj kanjiObj = new KanjiObj(jpHd.getUniqueKanji(kanji, i, 1));

            // Set suggest meaning to new Kanji
            SuggestKanjiAccess dbAccess = SuggestKanjiAccess.getInstance(context);
            dbAccess.open();
            String res = dbAccess.getSuggestMeaning(Character.toString(kanjiObj.getCharacter()));
            dbAccess.close();

            kanjiObj.setMeaning(res);

            Document doc = Jsoup.parse(res);

            if (!doc.getElementsByTag("b").isEmpty()) {
                String hanviet = doc.getElementsByTag("b").get(0).text();
                kanjiObj.setHanviet(hanviet);
            }

            // Add new kanji to database
            TBKanjiHandler kanjiHandler = new TBKanjiHandler(context);
            int newKanjiId = kanjiHandler.add(kanjiObj);

            // Create new Entry-Kanji constrain and add to 'KanjiEntry Table'
            if (newKanjiId != -1) {
                KanjiEntryObj kanjiEntryObj = new KanjiEntryObj(newKanjiId, newEntryId);

                KanjiEntryService service = new KanjiEntryService();
                service.add(kanjiEntryObj);
            } else {
                KanjiObj o = kanjiHandler.getByCharater(kanjiObj.getCharacter());
                KanjiEntryObj kanjiEntryObj = new KanjiEntryObj(o.getKanjiId(), newEntryId);

                KanjiEntryService service = new KanjiEntryService();
                service.add(kanjiEntryObj);
            }
        }
    }
}
