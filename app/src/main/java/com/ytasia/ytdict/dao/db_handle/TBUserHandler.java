package com.ytasia.ytdict.dao.db_handle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.ytasia.ytdict.dao.obj.UserObj;
import com.ytasia.ytdict.dao.schema.YTDictSchema;

/**
 * Created by luongduy on 2/25/16.
 *
 * @author luongduy
 */
public class TBUserHandler extends ytdictDbHandler {
    public TBUserHandler(Context context) {
        super(context);
    }

    /**
     * This method is used get a list of all User
     *
     * @return List: This return of list of UserObj objects
     */
    public List<UserObj> getAll() {
        List<UserObj> list = new ArrayList<UserObj>();
        String query = "SELECT * FROM " + YTDictSchema.TBUser.TABLE_NAME;
        SQLiteDatabase db = getReadableDb();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                UserObj obj = getObjFromCursor(cursor);
                list.add(obj);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * get UserObj by Id
     *
     * @param id The Id of the object
     * @return UserObj This return the object that has userId of id
     */
    public UserObj getById(int id) {
        SQLiteDatabase db = getReadableDb();
        String query = "SELECT * FROM " + YTDictSchema.TBUser.TABLE_NAME +
                " WHERE " + YTDictSchema.TBUser.COLUMN_NAME_USER_ID + "=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) cursor.moveToFirst();
        else return null;
        return getObjFromCursor(cursor);
    }

    /**
     * add an UserObj to User table
     *
     * @param obj The UserObj
     */
    public void add(UserObj obj) {
        SQLiteDatabase db = getWritableDb();

        ContentValues values = generateContentValues(obj);
        // Inserting Row
        db.insert(YTDictSchema.TBUser.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    /***
     * Local Function
     ***/

    private UserObj getObjFromCursor(Cursor cursor) {
        UserObj obj = new UserObj();
        int index;
        index = cursor.getColumnIndex(YTDictSchema.TBUser.COLUMN_NAME_USER_ID);
        obj.setUserId(cursor.getInt(index));
        index = cursor.getColumnIndex(YTDictSchema.TBUser.COLUMN_NAME_PASSWORD);
        obj.setPassword(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBUser.COLUMN_NAME_EMAIL);
        obj.setEmail(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBUser.COLUMN_NAME_REGISTERED_DATE);
        obj.setRegisteredDate(java.sql.Date.valueOf(cursor.getString(index)));
        index = cursor.getColumnIndex(YTDictSchema.TBUser.COLUMN_NAME_STATUS);
        obj.setStatus(cursor.getString(index));
        index = cursor.getColumnIndex(YTDictSchema.TBUser.COLUMN_NAME_ENTRY_HIGH_SCORE);
        obj.setEntryHighScore(cursor.getInt(index));
        index = cursor.getColumnIndex(YTDictSchema.TBUser.COLUMN_NAME_KANJI_HIGH_SCORE);
        obj.setKanjiHighScore(cursor.getInt(index));
        return obj;
    }

    /* Generate the ContentValues to be inserted to db from user obj */
    private ContentValues generateContentValues(UserObj obj) {
        ContentValues values = new ContentValues();
        values.put(YTDictSchema.TBUser.COLUMN_NAME_EMAIL, obj.getEmail());
        values.put(YTDictSchema.TBUser.COLUMN_NAME_PASSWORD, obj.getPassword());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        values.put(YTDictSchema.TBUser.COLUMN_NAME_REGISTERED_DATE, dateFormat.format(obj.getRegisteredDate()));
        values.put(YTDictSchema.TBUser.COLUMN_NAME_STATUS, obj.getStatus());
        values.put(YTDictSchema.TBUser.COLUMN_NAME_ENTRY_HIGH_SCORE, obj.getEntryHighScore());
        values.put(YTDictSchema.TBUser.COLUMN_NAME_KANJI_HIGH_SCORE, obj.getKanjiHighScore());
        return values;
    }
}
