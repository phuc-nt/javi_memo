package com.ytasia.dict.view.fragment;


import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ytasia.dict.service.SettingService;
import com.ytasia.dict.util.YTDictValues;

import com.ytasia.dict.view.activity.LoginActivity;

import ytasia.dictionary.R;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class SettingFragment extends Fragment {

    private Button ratingBt;
    private Button logoutBt;
    private Spinner entryLevelSpinner, kanjiLevelSpinner;
    private Button resetEntryLevelBt, resetKanjiLevelBt, clearEntryBt;
    private SettingService settingService;


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

        settingService = new SettingService();

        // Match object to layout
        matchObjectToLayout(view);

        // Set data for view
        setData();

        // on new entry level selected
        entryLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                YTDictValues.ENTRY_MAX_LEVEL = Integer.parseInt(entryLevelSpinner.getSelectedItem().toString());
                //Log.i("new entry max level: ", "" + YTDictValues.ENTRY_MAX_LEVEL);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // on new kanji level selected
        kanjiLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                YTDictValues.KANJI_MAX_LEVEL = Integer.parseInt(kanjiLevelSpinner.getSelectedItem().toString());
                //Log.i("new kanji max level: ", "" + YTDictValues.KANJI_MAX_LEVEL);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // on reset entry level clicked
        resetEntryLevelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset level of all Entries to 0
                settingService.resetEntryLevel(getActivity());
            }
        });

        // on reset kanji level clicked
        resetKanjiLevelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset level of all Kanjis to 0
                settingService.resetKanjiLevel(getActivity());
            }
        });

        // on clear entries button clicked
        clearEntryBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingService.clearEntry(getActivity());
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

        ratingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRateDialog();
            }
        });

        return view;
    }

    /**
     * Match object to layout elements
     */
    private void matchObjectToLayout(View view) {
        //toolbar = (Toolbar) view.findViewById(R.id.app_setting_toolbar);

        logoutBt = (Button) view.findViewById(R.id.fragment_setting_log_out_bt);
        entryLevelSpinner = (Spinner) view.findViewById(R.id.fragment_setting_max_level_entry_spinner);
        kanjiLevelSpinner = (Spinner) view.findViewById(R.id.fragment_setting_max_level_kanji_spinner);
        resetEntryLevelBt = (Button) view.findViewById(R.id.fragment_setting_reset_level_entry_bt);
        resetKanjiLevelBt = (Button) view.findViewById(R.id.fragment_setting_reset_level_kanji_bt);
        clearEntryBt = (Button) view.findViewById(R.id.fragment_setting_clear_entry_bt);
        ratingBt = (Button) view.findViewById(R.id.fragment_setting_rating_bt);
    }

    /**
     * Set data for view
     */
    private void setData() {
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.level_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        entryLevelSpinner.setAdapter(adapter);
        kanjiLevelSpinner.setAdapter(adapter);
        entryLevelSpinner.setSelection(getIndex(entryLevelSpinner, Integer.toString(YTDictValues.ENTRY_MAX_LEVEL)));
        kanjiLevelSpinner.setSelection(getIndex(kanjiLevelSpinner, Integer.toString(YTDictValues.KANJI_MAX_LEVEL)));
    }

    /**
     * Get index of item on spinner by string value
     *
     * @param spinner
     * @param myString value of item
     * @return index of item
     */
    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Show rating app request
     */
    private void showRateDialog() {
        String context = "com.ytasia.javimemo";
        Uri uri = Uri.parse("market://details?id=" + context);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context)));
        }
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
