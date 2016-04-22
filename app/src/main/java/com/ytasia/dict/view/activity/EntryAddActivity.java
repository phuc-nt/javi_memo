package com.ytasia.dict.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.ytasia.dict.dao.db_handle.SuggestEntryAccess;
import com.ytasia.dict.dao.db_handle.SuggestKanjiAccess;
import com.ytasia.dict.dao.obj.EntryObj;
import com.ytasia.dict.service.EntryService;
import com.ytasia.dict.util.YTDictValues;

import ytasia.dictionary.R;

public class EntryAddActivity extends AppCompatActivity {

    private Toolbar entryToolbar;
    private TextView entryContentTv;
    private EditText entryFuriganaEt, entryMeaningEt, entryExampleEt;

    private EntryService service = new EntryService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_add);

        // Match object to Layout
        entryToolbar = (Toolbar) this.findViewById(R.id.entry_add_toolbar);
        entryContentTv = (TextView) this.findViewById(R.id.entry_add_content_text_view);
        entryFuriganaEt = (EditText) this.findViewById(R.id.entry_add_furigana_text_edit);
        entryMeaningEt = (EditText) this.findViewById(R.id.entry_add_meaning_text_edit);
        entryExampleEt = (EditText) this.findViewById(R.id.entry_add_example_text_edit);

        // Get new entry from ListView Screen to Add Screen
        String newEntry = getIntent().getStringExtra("new_entry_name");
        entryContentTv.setText(newEntry);

        // Get suggest for new entry
        getSuggest(newEntry);

        // Set ActionBar function for toolbar
        setSupportActionBar(entryToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar_add_edit_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.toolbar_done_button) {
            String furi = entryFuriganaEt.getText().toString().replaceAll("\\s", "");
            String mean = entryMeaningEt.getText().toString().replaceAll("\\s", "");
            if (!furi.equalsIgnoreCase("")) {
                if (!mean.equalsIgnoreCase("")) {
                    // Get new data
                    //String newEntryContent = getIntent().getStringExtra("new_entry_name");
                    String newEntryContent = entryContentTv.getText().toString();
                    String newEntryFurigana = entryFuriganaEt.getText().toString();
                    String newEntryMeaning = entryMeaningEt.getText().toString();
                    String newEntryExample = entryExampleEt.getText().toString();

                    // Create new object with new data
                    EntryObj newObj = new EntryObj(YTDictValues.username, newEntryContent, newEntryFurigana, newEntryMeaning, newEntryExample, null);
                    //service.add(newObj);

                    // Put Object to EntryListFragment
                    Intent myIntent = getIntent();
                    myIntent.putExtra("add_entry_object", newObj);
                    setResult(MainActivity.RESULT_CODE_ENTRY_ADD, myIntent);
                    finish();
                    return true;
                } else {
                    entryMeaningEt.setError("Please insert some text");
                }
            } else {
                entryFuriganaEt.setError("Please insert some text");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void getSuggest(String entry) {
        //SuggestKanjiAccess dbAccess = SuggestKanjiAccess.getInstance(this);
        SuggestEntryAccess dbAccess = SuggestEntryAccess.getInstance(this);
        dbAccess.open();
        String res = dbAccess.getSuggestMeaning(entry);
        dbAccess.close();

        String furi = "";
        String meaning = "";
        String example = "";

        String[] a = res.split("\n");
        for (int i = 0; i < a.length; i++) {
            a[i].replaceAll("[!*]", "").trim();
        }
        for (int i = 0; i < a.length; i++) {
            if (i == 0) {
                furi = a[i];
                if (a[i].equals("No Suggest"))
                    furi = "";
            } else {
                char c = a[i].replaceAll("[!*]", "").trim().charAt(0);
                int value = (int) c;
                if ((value >= 65 && value <= 90) || (value >= 97 && value <= 122)) {
                    meaning = meaning + a[i].replaceAll("[!*]", "").trim() + "\n";
                } else {
                    example = example + a[i].replaceAll("[!*]", "").trim() + "\n";
                }
            }
        }

        entryFuriganaEt.setText(furi);
        entryMeaningEt.setText(meaning);
        entryExampleEt.setText(example);
    }
}