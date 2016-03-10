package ytasia.dictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class EntryQuizMainActivity extends AppCompatActivity {
    private Button startBt;
    private TextView highScoreTv;
    private TextView yourScoreTv;
    private SeekBar levelSb;
    private TextView levelTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_quiz_main);

        // Match object to layout elements
        startBt = (Button) findViewById(R.id.entry_quiz_main_start_button);
        highScoreTv = (TextView) findViewById(R.id.entry_quiz_main_high_score_tv);
        yourScoreTv = (TextView) findViewById(R.id.entry_quiz_main_your_score_tv);
        levelSb = (SeekBar) findViewById(R.id.entry_quiz_main_level_sb);
        levelTv = (TextView) findViewById(R.id.entry_quiz_main_level_tv);

        // Get parent width
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int parentWidth = displaymetrics.widthPixels;


        // on click Start button
        startBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntryQuizMainActivity.this, EntryQuizPlayActivity.class);
                startActivity(intent);
            }
        });

        // on Level SeekBar change
        levelTv.setText(Integer.toString(levelSb.getProgress() + 1));
        levelSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                levelTv.setText(Integer.toString(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //levelTv.setText(Integer.toString(levelSb.getProgress()));
            }
        });
    }
}
