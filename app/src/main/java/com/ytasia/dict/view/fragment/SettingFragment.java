package com.ytasia.dict.view.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ytasia.dict.dao.db_handle.TBEntryHandler;
import com.ytasia.dict.dao.db_handle.TBKanjiEntryHandler;
import com.ytasia.dict.dao.db_handle.TBUserHandler;
import com.ytasia.dict.dao.obj.EntryObj;
import com.ytasia.dict.dao.obj.KanjiEntryObj;
import com.ytasia.dict.util.YTDictValues;
import com.ytasia.dict.view.activity.AppSettingActivity;
import com.ytasia.dict.view.activity.FeedbackActivity;

import com.ytasia.dict.view.activity.LoginActivity;

import ytasia.dictionary.R;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


import java.util.List;

public class SettingFragment extends Fragment {

    private Button appSetBt;
    private Button upgradeSetBt;
    private Button feedbackBt;
    private Button logoutBt;
    private boolean logout_send;


    public SettingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        appSetBt = (Button) view.findViewById(R.id.app_setting_button);
        upgradeSetBt = (Button) view.findViewById(R.id.upgrade_setting_button);
        feedbackBt = (Button) view.findViewById(R.id.feedback_button);
        logoutBt = (Button) view.findViewById(R.id.logout_button);

        appSetBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppSettingActivity.class);
                startActivity(intent);
            }
        });

        upgradeSetBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TBUserHandler hd = new TBUserHandler(getActivity());
                hd.dropAllTables();

                /*Intent intent = new Intent(getActivity(), UpgradeActivity.class);
                startActivity(intent);*/


                /*TBEntryHandler entryHandler = new TBEntryHandler(getActivity());
                TBKanjiEntryHandler kanjiEntryHandler = new TBKanjiEntryHandler(getActivity());

                List<EntryObj> entries = entryHandler.getAll();
                List<KanjiEntryObj> kanjiEntries = kanjiEntryHandler.getAll();

                for (int i = 0; i < entries.size(); i++) {
                    Log.i("Check Entry " + i, entries.get(i).getEntryId());
                }

                for (int i = 0; i < kanjiEntries.size(); i++) {
                    Log.i("Check Entry-Kanji " + i, kanjiEntries.get(i).getServerId());
                }*/
            }
        });

        feedbackBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Context", getContext().getPackageName());
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
            }
        });

        logoutBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Alert !");
                alert.setMessage("Are you Loguot ?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogoutFunction();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });

        return view;
    }

    private void LogoutFunction() {
        //log out when logged with Facebook
        FacebookSdk.sdkInitialize(getContext());
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
            YTDictValues.fUserid = null;
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        //Logout when logged with Google Account
        if (YTDictValues.isLogin) {
            if (!YTDictValues.mGoogleApiClient.isConnected()) {
                //mGoogleApiClient reconnect();
                YTDictValues.mGoogleApiClient.connect();
                YTDictValues.mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        signOut();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                });
            }
        }
        //Logout when logged with User Guest
        else {
            YTDictValues.guestid = null;
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    //Google Logout function
    private void signOut() {
        YTDictValues.gUserid = null;
        YTDictValues.gEmail = null;
        YTDictValues.gFullName = null;
        Auth.GoogleSignInApi.signOut(YTDictValues.mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        YTDictValues.isLogin = false;
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }


}
