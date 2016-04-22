/**
 *
 */
package com.ytasia.dict.service;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import org.apache.http.HttpStatus;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.ytasia.dict.util.YTDictValues;

import cz.msebera.android.httpclient.Header;

/**
 * @author truongnguyen
 */
public class AuthenService {
    // servlet param info
    private final static int timeout = 60000;
    private final static String http_param_username = "username";
    private final static String http_param_type = "type";
    private final static String http_param_uuid = "uuid";
    private final static String http_param_function = "f";
    private final static String httpUrl = "http://ytdict.ytasia.co.jp";
    private final static SyncHttpClient client = new SyncHttpClient();

    public String getLoginInfo(final String user, String pwd, String type) throws Exception {
        final Object ob = new Object();
        RequestParams params = new RequestParams();
        params.put(http_param_username, user);
        params.put(http_param_uuid, pwd);
        params.put(http_param_type, type);
        params.put(http_param_function, "a");
        client.post(httpUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                // TODO Auto-generated method stub
                arg3.printStackTrace();
            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                // TODO Auto-generated method stub
                if (HttpStatus.SC_OK == arg0) {
                    Log.v("AuthenService", arg2);
                    // SAVE TO CACHE
                    String[] s = arg2.split(System.lineSeparator());
                    YTDictValues.uuid = s[0];
                    YTDictValues.server_ddp = s[1];

                    Log.i("uuid", s[0]);
                    Log.i("server ddp", s[1]);

                    synchronized (ob) {
                        ob.notify();
                    }
                }
            }
        });
        if (YTDictValues.uuid == null || YTDictValues.uuid.isEmpty()) {
            synchronized (ob) {
                try {
                    ob.wait(timeout);
                    if (YTDictValues.uuid == null || YTDictValues.uuid.isEmpty())
                        throw new Exception("Connection timeout");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return YTDictValues.uuid;

    }


}
