package com.ytasia.ytdict.dao.obj;

import java.io.Serializable;

/**
 * Created by luongduy on 2/25/16.
 */
public class KanjiObj implements Serializable {
    private int kanjiId;
    private char character;
    private String onyomi;
    private String kunyomi;
    private String hanviet;
    private String meaning;
    private int level;
    private String associated;

    public String getAssociated() {
        return associated;
    }

    public void setAssociated(String associated) {
        this.associated = associated;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public KanjiObj(int kanjiId, char character, String onyomi, String kunyomi, String hanviet, String meaning, String associated, int level) {
        this.kanjiId = kanjiId;
        this.character = character;
        this.onyomi = onyomi;
        this.kunyomi = kunyomi;
        this.hanviet = hanviet;
        this.meaning = meaning;
        this.associated = associated;
        this.level = level;
    }

    public KanjiObj(char character) {
        this.kanjiId = 0;
        this.character = character;
        this.onyomi = "No Suggestion";
        this.kunyomi = "No Suggestion";
        this.hanviet = "No Suggestion";
        this.meaning = "No Suggestion";
        this.associated = "No Suggestion";
        this.level = 1;
    }

    public KanjiObj() {

    }

    public String toString() {
        return Character.toString(character);
    }

    public int getKanjiId() {
        return kanjiId;
    }

    public void setKanjiId(int kanjiId) {
        this.kanjiId = kanjiId;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public String getOnyomi() {
        return onyomi;
    }

    public void setOnyomi(String onyomi) {
        this.onyomi = onyomi;
    }

    public String getKunyomi() {
        return kunyomi;
    }

    public void setKunyomi(String kunyomi) {
        this.kunyomi = kunyomi;
    }

    public String getHanviet() {
        return hanviet;
    }

    public void setHanviet(String hanviet) {
        this.hanviet = hanviet;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
