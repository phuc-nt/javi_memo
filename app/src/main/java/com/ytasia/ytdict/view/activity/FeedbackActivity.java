package com.ytasia.ytdict.view.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ytasia.dictionary.R;

public class FeedbackActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button mailFb, storeFb;
    private EditText fbMess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        // Match object to layout elements
        matchObjectToLayout();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mailFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        storeFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRateDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Match object to layout elements
     */
    private void matchObjectToLayout() {
        toolbar = (Toolbar) this.findViewById(R.id.feedback_toolbar);
        mailFb = (Button) this.findViewById(R.id.feedback_mail_bt);
        storeFb = (Button) this.findViewById(R.id.feedback_rate_app_bt);
        fbMess = (EditText) this.findViewById(R.id.feedback_content_edit_text);
    }

    /**
     * Show rating app request
     */
    private void showRateDialog() {
        String context = "jp.co.translimit.brainwars";
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

    /**
     * Send feedback by email
     */
    private void sendFeedback() {
        String message = fbMess.getText().toString();
        String to = "phucnt0@gmail.com";
        String subject = "(YTDict) Feedback";

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
        //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}
