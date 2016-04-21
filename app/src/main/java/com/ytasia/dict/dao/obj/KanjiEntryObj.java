package com.ytasia.dict.dao.obj;

/**
 * Created by luongduy on 2/25/16.
 */
public class KanjiEntryObj {

    private int kanjiId;
    private String entryId;

    public KanjiEntryObj() {

    }

    public KanjiEntryObj(int kanjiId, String entryId) {
        this.kanjiId = kanjiId;
        this.entryId = entryId;
    }



    public int getKanjiId() {
        return kanjiId;
    }

    public void setKanjiId(int kanjiId) {
        this.kanjiId = kanjiId;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }
}
