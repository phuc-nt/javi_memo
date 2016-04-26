package com.ytasia.dict.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.startapp.android.publish.StartAppAd;
import com.ytasia.dict.dao.obj.KanjiObj;

import ytasia.dictionary.R;

public class KanjiViewActivity extends AppCompatActivity {

    private Toolbar kanjiToolbar;
    private TextView kanjiContentTv, kanjiMeaningTv, levelTv;
    private KanjiObj ob;

    private StartAppAd startAppAd = new StartAppAd(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_view);

        // Match object to Layout
        matchObjectToLayout();

        // Get object from list
        Intent intent = getIntent();
        ob = (KanjiObj) intent.getExtras().getSerializable("kanji_object");

        // Set data for view
        setData();

        // Set ActionBar function for toolbar
        setSupportActionBar(kanjiToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        startAppAd.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        startAppAd.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startAppAd.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar_view_menu, menu);

        menu.getItem(0).setEnabled(false);
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
        kanjiToolbar = (Toolbar) findViewById(R.id.kanji_view_toolbar);
        kanjiContentTv = (TextView) findViewById(R.id.kanji_view_content_text_view);
        kanjiMeaningTv = (TextView) findViewById(R.id.kanji_view_meaning_text_view);
        levelTv = (TextView) findViewById(R.id.kanji_view_level_text_view);
    }

    /**
     * Set data for view
     */
    private void setData() {
        kanjiContentTv.setText(Character.toString(ob.getCharacter()));
        kanjiMeaningTv.setText(Html.fromHtml(ob.getMeaning()));
        levelTv.setText(Integer.toString(ob.getLevel()));
    }
}
