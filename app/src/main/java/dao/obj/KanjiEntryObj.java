package dao.obj;

/**
 * Created by luongduy on 2/25/16.
 */
public class KanjiEntryObj {
    private int kanjiId;
    private int entryId;

    public KanjiEntryObj () {

    }
    public KanjiEntryObj(int kanjiId, int entryId) {
        this.kanjiId = kanjiId;
        this.entryId = entryId;
    }

    public int getKanjiId() {
        return kanjiId;
    }

    public void setKanjiId(int kanjiId) {
        this.kanjiId = kanjiId;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }
}
