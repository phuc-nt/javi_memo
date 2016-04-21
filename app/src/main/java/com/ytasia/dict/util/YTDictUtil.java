package com.ytasia.dict.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Created by PhucNT on 16/April/21.
 */
public class YTDictUtil {
    public static String skipBuffer(GZIPInputStream inputStream, int start, int len)throws Exception{
        int odd=start/1000;
        int remainder=start-odd*1000;
        byte[] buf = new byte[len];
        for(int j=0;j<odd;j++){
            inputStream.skip(1000);
            inputStream.reset();
        }
        inputStream.skip(remainder);
        inputStream.read(buf);
        inputStream.close();
        return new String(buf, "utf-8");
    }
}
