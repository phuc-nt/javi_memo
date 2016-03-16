package ytasia.dictionary;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import hotchemi.android.rate.AppRate;

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
                Intent intent = new Intent(getActivity(), UpgradeActivity.class);
                startActivity(intent);
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
