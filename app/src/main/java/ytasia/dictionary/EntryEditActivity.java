package ytasia.dictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dao.db_handle.TBEntryHandler;
import dao.obj.EntryObj;

public class EntryEditActivity extends AppCompatActivity {


    private Toolbar entryToolbar;
    private TextView entryContentTv, entryFuriganaTv, entryMeaningTv, entryExamleTv;
    EntryObj obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_edit);

        // Get object from list
        Intent intent = getIntent();
        obj = (EntryObj) intent.getExtras().getSerializable("entry_object");

        // Match object to Layout
        matchObjectToLayout();

        // Set data for view
        setData();

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
            //java.sql.Date now = new java.sql.Date(Calendar.getInstance().getTime().getTime());

            //Set new value to Object
            obj.setFurigana(entryFuriganaTv.getText().toString());
            obj.setMeaning(entryMeaningTv.getText().toString());
            obj.setExample(entryExamleTv.getText().toString());
            //obj.setCreatedDate(now);

            //Put new Object to EntryViewActivity
            Intent myIntent = getIntent();
            myIntent.putExtra("edit_entry_object", obj);
            setResult(MainActivity.RESULT_CODE_ENTRY_EDIT, myIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Match object to layout elements
     */
    private void matchObjectToLayout() {
        entryToolbar = (Toolbar) this.findViewById(R.id.entry_edit_toolbar);
        entryContentTv = (TextView) this.findViewById(R.id.entry_edit_content_text_edit);
        entryFuriganaTv = (TextView) this.findViewById(R.id.entry_edit_furigana_text_edit);
        entryMeaningTv = (TextView) this.findViewById(R.id.entry_edit_meaning_text_edit);
        entryExamleTv = (TextView) this.findViewById(R.id.entry_edit_example_text_edit);
    }

    /**
     * Set data for view
     */
    private void setData() {
        entryContentTv.setText(obj.getContent());
        entryFuriganaTv.setText(obj.getFurigana());
        entryMeaningTv.setText(obj.getMeaning());
        entryExamleTv.setText(obj.getExample());
    }
}
