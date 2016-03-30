package com.ytasia.dict.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ytasia.dict.dao.db_handle.TBEntryHandler;
import com.ytasia.dict.dao.db_handle.TBKanjiEntryHandler;
import com.ytasia.dict.dao.obj.EntryObj;
import com.ytasia.dict.dao.obj.KanjiEntryObj;
import com.ytasia.dict.view.activity.AppSettingActivity;
import com.ytasia.dict.view.activity.FeedbackActivity;

import ytasia.dictionary.R;

import com.ytasia.dict.view.activity.UpgradeActivity;

import java.util.List;

public class SettingFragment extends Fragment {

    private Button appSetBt;
    private Button upgradeSetBt;
    private Button feedbackBt;
    private Button logoutBt;


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
                /*Intent intent = new Intent(getActivity(), UpgradeActivity.class);
                startActivity(intent);*/
                TBEntryHandler entryHandler = new TBEntryHandler(getActivity());
                TBKanjiEntryHandler kanjiEntryHandler = new TBKanjiEntryHandler(getActivity());

                List<EntryObj> entries = entryHandler.getAll();
                List<KanjiEntryObj> kanjiEntries = kanjiEntryHandler.getAll();

                for (int i = 0; i < entries.size(); i++) {
                    Log.i("Check Entry " + i, entries.get(i).getEntryId());
                }

                for (int i = 0; i < kanjiEntries.size(); i++) {
                    Log.i("Check Entry-Kanji " + i, kanjiEntries.get(i).getServerId());
                }
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
            }
        });

        return view;
    }
}
