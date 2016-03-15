package ytasia.dictionary;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

import dao.db_handle.TBEntryHandler;
import dao.db_handle.TBKanjiEntryHandler;
import dao.db_handle.TBKanjiHandler;
import dao.obj.EntryObj;
import dao.obj.KanjiObj;
import util.YTDictValues;

public class AppSettingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner entryLevelSpinner, kanjiLevelSpinner;
    private Button resetEntryLevelBt, resetKanjiLevelBt, clearEntryBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

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
                resetEntryLevel();
            }
        });

        // on reset kanji level clicked
        resetKanjiLevelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset level of all Kanjis to 0
                resetKanjiLevel();
            }
        });

        // on clear entries button clicked
        clearEntryBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEntry();
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

    /**
     * Reset level of all Entries to 0 (default)
     */
    private void resetEntryLevel() {
        final TBEntryHandler entryHandler = new TBEntryHandler(this);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure reset level of all Entries");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<EntryObj> entryObjs = entryHandler.getAll();
                for (int i = 0; i < entryObjs.size(); i++) {
                    entryObjs.get(i).setLevel(0);
                    entryHandler.update(entryObjs.get(i), entryObjs.get(i).getEntryId());
                }
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

    /**
     * Reset level of all Kanjis to 0 (default)
     */
    private void resetKanjiLevel() {
        final TBKanjiHandler kanjiHandler = new TBKanjiHandler(this);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure reset level of all Entries");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<KanjiObj> kanjiObjs = kanjiHandler.getAll();
                for (int i = 0; i < kanjiObjs.size(); i++) {
                    kanjiObjs.get(i).setLevel(0);
                    kanjiHandler.update(kanjiObjs.get(i), kanjiObjs.get(i).getKanjiId());
                }
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

    /**
     * Clear all user entries
     */
    private void clearEntry() {
        final TBEntryHandler entryHandler = new TBEntryHandler(this);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure clear all Entries");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<EntryObj> entryObjs = entryHandler.getAll();
                for (int i = 0; i < entryObjs.size(); i++) {
                    EntryObj obj = entryObjs.get(i);
                    entryHandler.delete(AppSettingActivity.this, obj.getEntryId());
                }
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
}