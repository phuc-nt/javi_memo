package com.ytasia.dict.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import com.ytasia.dict.dao.db_handle.SuggestDataAccess;
import com.ytasia.dict.dao.obj.EntryObj;
import com.ytasia.dict.util.YTDictValues;

import ytasia.dictionary.R;

public class EntryAddActivity extends AppCompatActivity {

    private Toolbar entryToolbar;
    private TextView entryContentTv;
    private EditText entryFuriganaEt, entryMeaningEt, entryExampleEt;

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
            // Get new data
            //String newEntryContent = getIntent().getStringExtra("new_entry_name");
            String newEntryContent = entryContentTv.getText().toString();
            String newEntryFurigana = entryFuriganaEt.getText().toString();
            String newEntryMeaning = entryMeaningEt.getText().toString();
            String newEntryExample = entryExampleEt.getText().toString();

            // Create new object with new data
            EntryObj newObj = new EntryObj(YTDictValues.username, newEntryContent, newEntryFurigana, newEntryMeaning, newEntryExample, null);

            // Put Object to EntryListFragment
            Intent myIntent = getIntent();
            // myIntent.putExtra("fragmentPosition", "1");
            myIntent.putExtra("add_entry_object", newObj);
            setResult(MainActivity.RESULT_CODE_ENTRY_ADD, myIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getSuggest(String entry) {
        SuggestDataAccess dbAccess = SuggestDataAccess.getInstance(this);
        dbAccess.open();
        String input = dbAccess.getSuggestMeaning(entry);
        Document doc = Jsoup.parse(input);

        List<String> furi = new ArrayList<>();
        List<String> exam = new ArrayList<>();
        List<String> mexam = new ArrayList<>();

        for (Element element : doc.select("span[class=title]")) {
            furi.add(element.text());
        }

        for (Element element : doc.select("span[class=example]")) {
            exam.add(element.text());
        }

        for (Element element : doc.select("span[class=mexample]")) {
            mexam.add(element.text());
        }

        if (!furi.isEmpty()) {
            entryFuriganaEt.setText(furi.get(0));
        } else {
            entryFuriganaEt.setText("");
        }

        if (!exam.isEmpty()) {
            StringBuffer exams = new StringBuffer();
            for (int i = 0; i < exam.size(); i++) {
                exams.append(exam.get(i) + "\n" + mexam.get(i) + "\n");
            }
            entryExampleEt.setText(exams.toString());
        } else {
            entryExampleEt.setText("");
        }

        dbAccess.close();
    }
}