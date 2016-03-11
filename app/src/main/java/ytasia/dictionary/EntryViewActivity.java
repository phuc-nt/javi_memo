package ytasia.dictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import dao.db_handle.TBKanjiEntryHandler;
import dao.db_handle.TBKanjiHandler;
import dao.obj.EntryObj;
import dao.obj.KanjiObj;

public class EntryViewActivity extends AppCompatActivity {

    private Toolbar entryToolbar;
    private TextView entryContentTv, entryFuriganaTv, entryMeaningTv, entryExamleTv;
    private EntryObj ob;
    private ScrollView kanjiSv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_view);

        // Match object to Layout
        kanjiSv = (ScrollView) this.findViewById(R.id.entry_view_kanji_scroll_view);
        entryToolbar = (Toolbar) this.findViewById(R.id.entry_view_toolbar);
        entryContentTv = (TextView) this.findViewById(R.id.entry_view_content_text_view);
        entryFuriganaTv = (TextView) this.findViewById(R.id.entry_view_furigana_text_view);
        entryMeaningTv = (TextView) this.findViewById(R.id.entry_view_meaning_text_view);
        entryExamleTv = (TextView) this.findViewById(R.id.entry_view_example_text_view);

        // Get object from list
        Intent intent = getIntent();
        ob = (EntryObj) intent.getExtras().getSerializable("entry_object");

        // Create Kanji Button
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TBKanjiEntryHandler tbKanjiEntryHandler = new TBKanjiEntryHandler(this);
        List<Integer> list = tbKanjiEntryHandler.getAllKanjiIdByEntryId(ob.getEntryId());
        for (int j = 0; j < list.size(); j++) {
            final KanjiObj kanjiObj = new TBKanjiHandler(this).getById(list.get(j));
            Button bt = new Button(this);
            bt.setText(Character.toString(kanjiObj.getCharacter()));
            ll.addView(bt);
            // Set onClick function
            bt.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EntryViewActivity.this, KanjiViewActivity.class);
                    intent.putExtra("kanji_object", kanjiObj);
                    startActivity(intent);
                }
            });
        }
        // add kanji buttons to view
        kanjiSv.addView(ll);

        // Set data for view
        entryContentTv.setText(ob.getContent());
        entryFuriganaTv.setText(ob.getFurigana());
        entryMeaningTv.setText(Html.fromHtml(ob.getMeaning()));
        entryExamleTv.setText(ob.getExample());

        // Set ActionBar function for toolbar
        setSupportActionBar(entryToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar_view_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.toolbar_edit_button) {
            Intent intent = new Intent(EntryViewActivity.this, EntryEditActivity.class);
            intent.putExtra("entry_object", ob);
            startActivityForResult(intent, MainActivity.REQUEST_CODE_ENTRY_EDIT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_CODE_ENTRY_EDIT) {
            switch (resultCode) {
                // After edit entry
                case MainActivity.RESULT_CODE_ENTRY_EDIT:
                    //Get new object from EntryEditActivity
                    EntryObj newObj = (EntryObj) data.getSerializableExtra("edit_entry_object");
                    Intent myIntent = getIntent();
                    //myIntent.putExtra("fragmentPosition", "1");

                    //Put new Object to EntryListFragment
                    myIntent.putExtra("edit_entry_object", newObj);
                    setResult(MainActivity.RESULT_CODE_ENTRY_EDIT, myIntent);
                    finish();
                    break;
            }
        }
    }
}
