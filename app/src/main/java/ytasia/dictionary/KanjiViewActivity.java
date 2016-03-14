package ytasia.dictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dao.obj.KanjiObj;

public class KanjiViewActivity extends AppCompatActivity {

    private Toolbar kanjiToolbar;
    private TextView kanjiContentTv, kanjiHanvietTv, kanjiOnyomiTv, kanjiKunyomiTv, kanjiMeaningTv, kanjiAssociatedTv;
    private KanjiObj ob;

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
        kanjiToolbar = (Toolbar) this.findViewById(R.id.kanji_view_toolbar);
        kanjiContentTv = (TextView) this.findViewById(R.id.kanji_view_content_text_view);
        kanjiHanvietTv = (TextView) this.findViewById(R.id.kanji_view_hanviet_text_view);
        kanjiOnyomiTv = (TextView) this.findViewById(R.id.kanji_view_onyomi_text_view);
        kanjiKunyomiTv = (TextView) this.findViewById(R.id.kanji_view_kunyomi_text_view);
        kanjiMeaningTv = (TextView) this.findViewById(R.id.kanji_view_meaning_text_view);
        kanjiAssociatedTv = (TextView) this.findViewById(R.id.kanji_view_associated_text_view);
    }

    /**
     * Set data for view
     */
    private void setData() {
        kanjiContentTv.setText(Character.toString(ob.getCharacter()));
        kanjiHanvietTv.setText(ob.getHanviet());
        kanjiOnyomiTv.setText(ob.getOnyomi());
        kanjiKunyomiTv.setText(ob.getKunyomi());
        kanjiMeaningTv.setText(Html.fromHtml(ob.getMeaning()));
        kanjiAssociatedTv.setText(ob.getAssociated());
    }
}
