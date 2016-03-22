package com.ytasia.ytdict.view.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

import com.ytasia.ytdict.dao.db_handle.TBEntryHandler;
import com.ytasia.ytdict.dao.db_handle.TBKanjiHandler;
import com.ytasia.ytdict.dao.obj.EntryObj;
import com.ytasia.ytdict.dao.obj.KanjiObj;
import com.ytasia.ytdict.service.SettingService;
import com.ytasia.ytdict.util.YTDictValues;

import ytasia.dictionary.R;

public class AppSettingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner entryLevelSpinner, kanjiLevelSpinner;
    private Button resetEntryLevelBt, resetKanjiLevelBt, clearEntryBt;
    private SettingService settingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        settingService = new SettingService();

        // Match object to layout
        matchObjectToLayout();

        // Set data for view
        setData();

        // Set support Action Bar for toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                settingService.resetEntryLevel(AppSettingActivity.this);
            }
        });

        // on reset kanji level clicked
        resetKanjiLevelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset level of all Kanjis to 0
                settingService.resetKanjiLevel(AppSettingActivity.this);
            }
        });

        // on clear entries button clicked
        clearEntryBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingService.clearEntry(AppSettingActivity.this);
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
        toolbar = (Toolbar) this.findViewById(R.id.app_setting_toolbar);
        entryLevelSpinner = (Spinner) this.findViewById(R.id.app_setting_max_level_entry_spinner);
        kanjiLevelSpinner = (Spinner) this.findViewById(R.id.app_setting_max_level_kanji_spinner);
        resetEntryLevelBt = (Button) this.findViewById(R.id.app_setting_reset_level_entry_bt);
        resetKanjiLevelBt = (Button) this.findViewById(R.id.app_setting_reset_level_kanji_bt);
        clearEntryBt = (Button) this.findViewById(R.id.app_setting_clear_entry_bt);
    }

    /**
     * Set data for view
     */
    private void setData() {
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.level_array,
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
}