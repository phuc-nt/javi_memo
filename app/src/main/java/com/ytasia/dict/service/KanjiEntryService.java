package com.ytasia.dict.service;

import com.ytasia.dict.ddp.DBBasic;

/**
 * Created by PhucNT on 16/March/26.
 */
public class KanjiEntryService {

    /**
     * Add new Kanji-Entry constrain to server
     *
     * @param kanjiId
     * @param entryId
     */
    public void add(String kanjiId, String entryId) {
        DBBasic dbBasic = DBBasic.getInstance();
        dbBasic.insertKanjiEntry(kanjiId, entryId);
    }
}
