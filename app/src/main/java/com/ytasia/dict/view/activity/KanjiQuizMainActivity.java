package com.ytasia.dict.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ytasia.dict.dao.obj.UserObj;

import ytasia.dictionary.R;

public class KanjiQuizMainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_KANJI_QUIZ_START = 101;
    public static final int RESULT_CODE_KANJI_QUIZ_FAULT = 201;
    public static final int RESULT_CODE_KANJI_QUIZ_FINISH = 302;

    private Button startBt;
    private TextView highScoreTv;
    private TextView yourScoreTitle;
    private TextView yourScoreTv;
    private SeekBar levelSb;
    private TextView levelTv;
    private UserObj userObj;
    private int quizTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_quiz_main);

        // Get current user object
        Intent intent = getIntent();
        userObj = (UserObj) intent.getExtras().getSerializable("user_object");

        // Match object to layout elements
        matchObjectToLayout();

        // Set default data
        setDefaultData();

        // on click Start button
        startBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KanjiQuizMainActivity.this, KanjiQuizPlayActivity.class);

                int level = Integer.parseInt(levelTv.getText().toString());
                switch (level) {
                    case 1:
                        quizTime = 5;
                        break;
                    case 2:
                        quizTime = 4;
                        break;
                    case 3:
                        quizTime = 3;
                        break;
                    case 4:
                        quizTime = 2;
                        break;
                    case 5:
                        quizTime = 1;
                        break;
                }

                intent.putExtra("quiz_time", quizTime);
                intent.putExtra("user_object", userObj);
                startActivityForResult(intent, REQUEST_CODE_KANJI_QUIZ_START);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_KANJI_QUIZ_START) {
            switch (resultCode) {
                // Quiz end
                case RESULT_CODE_KANJI_QUIZ_FAULT:
                    userObj = (UserObj) data.getSerializableExtra("user_object");
                    //MainActivity.user = userObj;
                    refreshData(data.getIntExtra("your_score", 0));
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("user_object", userObj);
        setResult(RESULT_CODE_KANJI_QUIZ_FINISH, intent);
        finish();
    }

    /**
     * Match object to layout elements
     */
    private void matchObjectToLayout() {
        startBt = (Button) findViewById(R.id.kanji_quiz_main_start_button);
        highScoreTv = (TextView) findViewById(R.id.kanji_quiz_main_high_score_tv);
        yourScoreTv = (TextView) findViewById(R.id.kanji_quiz_main_your_score_tv);
        yourScoreTitle = (TextView) findViewById(R.id.kanji_quiz_main_your_score_title);
        levelSb = (SeekBar) findViewById(R.id.kanji_quiz_main_level_sb);
        levelTv = (TextView) findViewById(R.id.kanji_quiz_main_level_tv);
    }

    /**
     * Set default data
     */
    private void setDefaultData() {
        yourScoreTitle.setEnabled(false);
        yourScoreTv.setEnabled(false);
        highScoreTv.setText(Integer.toString(userObj.getKanjiHighScore()));
    }

    /**
     * Refresh data when quiz end
     *
     * @param yourScore new score
     */
    private void refreshData(int yourScore) {
        yourScoreTitle.setEnabled(true);
        yourScoreTv.setEnabled(true);
        yourScoreTv.setText(Integer.toString(yourScore));
        highScoreTv.setText(Integer.toString(userObj.getKanjiHighScore()));
    }
}
