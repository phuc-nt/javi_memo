package com.ytasia.ytdict.util;

import com.google.common.base.CharMatcher;

/**
 * Created by phucnt on 16/03/09.
 */
public class JapaneseHandler {
    public static final CharMatcher HIRAGANA =
            CharMatcher.inRange((char) 0x3040, (char) 0x309f);

    public static final CharMatcher KATAKANA =
            CharMatcher.inRange((char) 0x30a0, (char) 0x30ff);

    public static final CharMatcher KANA = HIRAGANA.or(KATAKANA);

    public static final CharMatcher KANJI =
            CharMatcher.inRange((char) 0x4e00, (char) 0x9faf);

    public static String getAllKanji(String string) {
        return KANJI.retainFrom(string);
    }

    public int getCodePointLength(String s){
        return s.codePointCount(0, s.length());
    }

    public static char getUniqueKanji(String s, int startCodepoint, int numCodepoints) {
        int startIndex = s.offsetByCodePoints(0, startCodepoint);
        int endIndex = s.offsetByCodePoints(startIndex, numCodepoints);
        return s.substring(startIndex, endIndex).charAt(0);
    }

}
