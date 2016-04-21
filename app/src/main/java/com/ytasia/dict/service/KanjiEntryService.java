package com.ytasia.dict.service;

import com.ytasia.dict.dao.db_handle.TBKanjiEntryHandler;
import com.ytasia.dict.dao.obj.KanjiEntryObj;
import com.ytasia.dict.util.YTDictValues;

/**
 * Created by PhucNT on 16/March/26.
 */
public class KanjiEntryService {

    /**
     * @param obj
     */
    public void add(KanjiEntryObj obj) {
        TBKanjiEntryHandler handler = new TBKanjiEntryHandler(YTDictValues.appContext);
        handler.add(obj);
    }
}
