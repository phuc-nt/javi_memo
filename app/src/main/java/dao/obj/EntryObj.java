package dao.obj;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by luongduy on 2/25/16.
 */
public class EntryObj implements Serializable {
    private int entryId;
    private int userId;
    private String content;
    private String furigana;
    private String meaning;
    private String example;
    private int level;
    private String source;
    private Date createdDate;

    public EntryObj() {

    }

    public EntryObj(int entryId, int userId, String content, String furigana, String meaning,
                    String example, int level, String source, Date createdDate) {
        this.entryId = entryId;
        this.userId = userId;
        this.content = content;
        this.furigana = furigana;
        this.meaning = meaning;
        this.example = example;
        this.level = level;
        this.source = source;
        this.createdDate = createdDate;
    }

    public String toString() {
        return content;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFurigana() {
        return furigana;
    }

    public void setFurigana(String furigana) {
        this.furigana = furigana;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
