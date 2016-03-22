package com.ytasia.ytdict.dao.obj;

import android.content.Context;

import com.ytasia.ytdict.dao.db_handle.SuggestDataAccess;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

    public EntryObj(int userId, String content, String furigana, String meaning, String example, String source) {
        this.entryId = 0;
        this.userId = userId;
        this.content = content;
        this.furigana = furigana;
        this.meaning = meaning;
        this.example = example;
        this.level = 0;
        this.source = "source";
        this.createdDate = new Date(new java.util.Date().getTime());
    }

    public EntryObj(Context context,int userId, String content) {
        this.entryId = 0;
        this.userId = userId;
        this.content = content;
        getSuggest(context,content);
        this.level = 0;
        this.source = "source";
        this.createdDate = new Date(new java.util.Date().getTime());
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

    private void getSuggest(Context context, String entry) {
        SuggestDataAccess dbAccess = SuggestDataAccess.getInstance(context);
        dbAccess.open();
        String input = dbAccess.getSuggestMeaning(entry);
        Document doc = Jsoup.parse(input);

        List<String> furi = new ArrayList<>();
        List<String> exam = new ArrayList<>();
        List<String> mexam = new ArrayList<>();

        for (Element element : doc.select("span[class=title]")) {
            furi.add(element.text());
        }

        for (Element element : doc.select("span[class=example]")) {
            exam.add(element.text());
        }

        for (Element element : doc.select("span[class=mexample]")) {
            mexam.add(element.text());
        }

        setFurigana(furi.get(0));
        StringBuffer exams = new StringBuffer();
        for (int i = 0; i < exam.size(); i++) {
            exams.append(exam.get(i) + "\n" + mexam.get(i) + "\n");
        }
        setExample(exams.toString());

        dbAccess.close();
    }
}
