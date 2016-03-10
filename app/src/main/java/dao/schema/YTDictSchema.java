package dao.schema;

import android.provider.BaseColumns;

/**
 * Created by luongduy on 2/24/16.
 */
public final class YTDictSchema {
    // empty constructor
    public YTDictSchema() {
    }

    /* Inner class that defines the User Table */
    public static abstract class TBUser implements BaseColumns {
        public static final String TABLE_NAME = "tbUser";
        public static final String COLUMN_NAME_USER_ID = "userId";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_REGISTERED_DATE = "registeredDate";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_ENTRY_HIGH_SCORE = "entryHS";
        public static final String COLUMN_NAME_KANJI_HIGH_SCORE = "kanjiHS";

        private static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TBUser.TABLE_NAME + " (" +
                        //TBUser._ID + " INTEGER PRIMARY KEY," +
                        TBUser.COLUMN_NAME_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                        TBUser.COLUMN_NAME_EMAIL + " VARCHAR(64) UNIQUE NOT NULL" + COMMA_SEP +
                        TBUser.COLUMN_NAME_PASSWORD + " VARCHAR(128) NOT NULL" + COMMA_SEP +
                        TBUser.COLUMN_NAME_REGISTERED_DATE + " DATE" + COMMA_SEP +
                        TBUser.COLUMN_NAME_STATUS + " VARCHAR(8)" + COMMA_SEP +
                        TBUser.COLUMN_NAME_ENTRY_HIGH_SCORE + " INTEGER " + COMMA_SEP +
                        TBUser.COLUMN_NAME_KANJI_HIGH_SCORE + " INTEGER " +
                        ");";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TBUser.TABLE_NAME;
    }

    /* Inner class that defines the Entry Table */
    public static abstract class TBEntry implements BaseColumns {
        public static final String TABLE_NAME = "tbEntry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryId";
        public static final String COLUMN_NAME_USER_ID = "userId";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_FURIGANA = "furigana";
        public static final String COLUMN_NAME_MEANING = "meaning";
        public static final String COLUMN_NAME_EXAMPLE = "example";
        public static final String COLUMN_NAME_LEVEL = "level";
        public static final String COLUMN_NAME_SOURCE = "source";
        public static final String COLUMN_NAME_CREATED_DATE = "createdDate";

        public static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TBEntry.TABLE_NAME + " (" +
                        //TBUser._ID + " INTEGER PRIMARY KEY," +
                        TBEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                        TBEntry.COLUMN_NAME_USER_ID + " INT NOT NULL" + COMMA_SEP +
                        TBEntry.COLUMN_NAME_CONTENT + " VARCHAR(256) NOT NULL" + COMMA_SEP +
                        TBEntry.COLUMN_NAME_MEANING + " VARCHAR(256)" + COMMA_SEP +
                        TBEntry.COLUMN_NAME_FURIGANA + " VARCHAR(256)" + COMMA_SEP +
                        TBEntry.COLUMN_NAME_EXAMPLE + " TEXT" + COMMA_SEP +
                        TBEntry.COLUMN_NAME_LEVEL + " TINYINT" + COMMA_SEP +
                        TBEntry.COLUMN_NAME_SOURCE + " VARCHAR(32)" + COMMA_SEP +
                        TBEntry.COLUMN_NAME_CREATED_DATE + " DATE" + COMMA_SEP +
                        //"PRIMARY KEY (" + TBEntry.COLUMN_NAME_ENTRY_ID + ")" + COMMA_SEP +
                        "UNIQUE (" + TBEntry.COLUMN_NAME_CONTENT + ")" + COMMA_SEP +
                        "CONSTRAINT tbEntry_ibfk_1 FOREIGN KEY (" + TBEntry.COLUMN_NAME_USER_ID +
                        ") REFERENCES " + TBUser.TABLE_NAME + "(" + TBUser.COLUMN_NAME_USER_ID + ")" +
                        ");";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TBEntry.TABLE_NAME;
    }

    /* Inner class that defines the Kanji Table */
    public static abstract class TBKanji implements BaseColumns {
        public static final String TABLE_NAME = "tbKanji";
        public static final String COLUMN_NAME_KANJI_ID = "kanjiId";
        public static final String COLUMN_NAME_CHARACTER = "character";
        public static final String COLUMN_NAME_ONYOMI = "onyomi";
        public static final String COLUMN_NAME_KUNYOMI = "kunyomi";
        public static final String COLUMN_NAME_HANVIET = "hanviet";
        public static final String COLUMN_NAME_MEANING = "meaning";
        public static final String COLUMN_NAME_ASSOCATED_WORD = "associated";
        public static final String COLUMN_NAME_LEVEL = "level";

        public static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TBKanji.TABLE_NAME + " (" +
                        TBKanji.COLUMN_NAME_KANJI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                        TBKanji.COLUMN_NAME_CHARACTER + " VARCHAR(1) NOT NULL" + COMMA_SEP +
                        TBKanji.COLUMN_NAME_ONYOMI + " VARCHAR(64) NOT NULL" + COMMA_SEP +
                        TBKanji.COLUMN_NAME_KUNYOMI + " VARCHAR(64)" + COMMA_SEP +
                        TBKanji.COLUMN_NAME_HANVIET + " VARCHAR(64)" + COMMA_SEP +
                        TBKanji.COLUMN_NAME_MEANING + " TEXT" + COMMA_SEP +
                        TBKanji.COLUMN_NAME_ASSOCATED_WORD + " VARCHAR(1024)" + COMMA_SEP +
                        TBKanji.COLUMN_NAME_LEVEL + " TINYINT" + COMMA_SEP +
                        "UNIQUE (" + TBKanji.COLUMN_NAME_CHARACTER + ")" +
                        ");";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TBKanji.TABLE_NAME;
    }

    /* Inner class that defines the KanjiEntry Table */
    public static abstract class TBKanjiEntry implements BaseColumns {
        public static final String TABLE_NAME = "tbKanjiEntry";
        public static final String COLUMN_NAME_KANJI_ID = "kanjiId";
        public static final String COLUMN_NAME_ENTRY_ID = "entryId";

        public static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TBKanjiEntry.TABLE_NAME + " (" +
                        TBKanjiEntry.COLUMN_NAME_KANJI_ID + " INTEGER" + COMMA_SEP +
                        TBKanjiEntry.COLUMN_NAME_ENTRY_ID + " INTEGER" + COMMA_SEP +
                        "PRIMARY KEY (" + TBKanjiEntry.COLUMN_NAME_KANJI_ID + COMMA_SEP +
                        TBKanjiEntry.COLUMN_NAME_ENTRY_ID + ")" + COMMA_SEP +
                        "CONSTRAINT tbKanjiEntry_ibfk_1 FOREIGN KEY (" + TBKanjiEntry.COLUMN_NAME_ENTRY_ID +
                        ") REFERENCES " + TBEntry.TABLE_NAME + "(" + TBEntry.COLUMN_NAME_ENTRY_ID + ")" +
                        "CONSTRAINT tbKanjiEntry_ibfk_2 FOREIGN KEY (" + TBKanjiEntry.COLUMN_NAME_KANJI_ID +
                        ") REFERENCES " + TBKanji.TABLE_NAME + "(" + TBKanji.COLUMN_NAME_KANJI_ID + ")" +
                        ")";// + "ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TBKanjiEntry.TABLE_NAME;
    }
}