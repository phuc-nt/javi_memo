package ytasia.dictionary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import dao.db_handle.TBEntryHandler;
import dao.obj.EntryObj;
import dao.obj.UserObj;

public class EntryQuizPlayActivity extends AppCompatActivity {
    public static final int RESULT_CODE_ENTRY_QUIZ_FAULT = 201;

    private Button answer1Bt, answer2Bt, answer3Bt, answer4Bt;
    private TextView scoreTv, timeTv, questionEntryTv, questionFuriganaTv;
    private UserObj userObj;
    private int quizTime;
    private int score = 0;
    private CountDownTimer timer;

    private Boolean isAnswer1, isAnswer2, isAnswer3, isAnswer4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_quiz_play);

        // Get user object
        Intent intent = getIntent();
        userObj = (UserObj) intent.getExtras().getSerializable("user_object");
        quizTime = intent.getIntExtra("quiz_time", 5);

        // Match object to layout elements
        matchObjectToLayout();

        // Set data
        setDefaultData();
        timer.start();

        answer1Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnswer1) {
                    onTrue();
                } else {
                    onFault();
                }
            }
        });

        answer2Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnswer2) {
                    onTrue();
                } else {
                    onFault();
                }
            }
        });

        answer3Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnswer3) {
                    onTrue();
                } else {
                    onFault();
                }
            }
        });

        answer4Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnswer4) {
                    onTrue();
                } else {
                    onFault();
                }
            }
        });
    }

    /**
     * Match object to layout elements
     */
    private void matchObjectToLayout() {
        answer1Bt = (Button) findViewById(R.id.entry_quiz_answer_1);
        answer2Bt = (Button) findViewById(R.id.entry_quiz_answer_2);
        answer3Bt = (Button) findViewById(R.id.entry_quiz_answer_3);
        answer4Bt = (Button) findViewById(R.id.entry_quiz_answer_4);
        scoreTv = (TextView) findViewById(R.id.entry_quiz_play_your_score_tv);
        timeTv = (TextView) findViewById(R.id.entry_quiz_play_time_tv);
        questionEntryTv = (TextView) findViewById(R.id.entry_quiz_play_question_entry_tv);
        questionFuriganaTv = (TextView) findViewById(R.id.entry_quiz_play_question_furigana_tv);
    }

    /**
     * Set default data
     */
    private void setDefaultData() {
        // Set score
        scoreTv.setText("" + score);

        // Set countdown
        timer = new CountDownTimer(quizTime * 1000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                /*timeTv.setText("" + String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));*/
                timeTv.setText("" + String.format("%2d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
            }

            public void onFinish() {
                onFault();
            }
        };

        // Set quiz data
        setQuizData();
    }

    /**
     * Set Quiz data
     */
    private void setQuizData() {
        // Get random question on user database
        TBEntryHandler entryHandler = new TBEntryHandler(this);
        List<EntryObj> entryObjs = entryHandler.getAll();
        Random random = new Random();
        int random1 = random.nextInt(entryObjs.size());
        questionEntryTv.setText(entryObjs.get(random1).getContent());
        questionFuriganaTv.setText(entryObjs.get(random1).getFurigana());

        // Set true answer to randomly location
        int random2 = random.nextInt(4) + 1;
        switch (random2) {
            case 1:
                isAnswer1 = true;
                isAnswer2 = false;
                isAnswer3 = false;
                isAnswer4 = false;
                answer1Bt.setText("TRUE");
                answer2Bt.setText("FALSE");
                answer3Bt.setText("FALSE");
                answer4Bt.setText("FALSE");
                break;
            case 2:
                isAnswer1 = false;
                isAnswer2 = true;
                isAnswer3 = false;
                isAnswer4 = false;
                answer1Bt.setText("FALSE");
                answer2Bt.setText("TRUE");
                answer3Bt.setText("FALSE");
                answer4Bt.setText("FALSE");
                break;
            case 3:
                isAnswer1 = false;
                isAnswer2 = false;
                isAnswer3 = true;
                isAnswer4 = false;
                answer1Bt.setText("FALSE");
                answer2Bt.setText("FALSE");
                answer3Bt.setText("TRUE");
                answer4Bt.setText("FALSE");
                break;
            case 4:
                isAnswer1 = false;
                isAnswer2 = false;
                isAnswer3 = false;
                isAnswer4 = true;
                answer1Bt.setText("FALSE");
                answer2Bt.setText("FALSE");
                answer3Bt.setText("FALSE");
                answer4Bt.setText("TRUE");
                break;
        }
    }

    /**
     * When user choose true answer, continue with new question,
     * score + 1 and reset time counter
     */
    private void onTrue() {
        timer.cancel();
        score++;
        scoreTv.setText("" + score);
        setQuizData();
        timer.start();
    }

    /**
     * When user choose wrong answer,
     * back to EntryQuizMainActivity
     */
    private void onFault() {
        timer.cancel();
        if (score > userObj.getEntryHighScore()) {
            userObj.setEntryHighScore(score);
        }

        // Show alert when user lose Quiz
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle(getResources().getString(R.string.lose_title));
        alert.setMessage(getResources().getString(R.string.lose_message));
        alert.setPositiveButton(getResources().getString(R.string.ok_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("user_object", userObj);
                        intent.putExtra("your_score", score);
                        setResult(RESULT_CODE_ENTRY_QUIZ_FAULT, intent);
                        finish();
                    }
                });
        alert.show();
    }
}
