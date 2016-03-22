package com.ytasia.ytdict.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ytasia.ytdict.dao.obj.UserObj;

import com.ytasia.ytdict.view.activity.EntryQuizMainActivity;
import com.ytasia.ytdict.view.activity.KanjiQuizMainActivity;
import com.ytasia.ytdict.view.activity.MainActivity;
import ytasia.dictionary.R;

public class QuizFragment extends Fragment {
    public static final int REQUEST_CODE_ENTRY_QUIZ_SELECT = 101;
    public static final int REQUEST_CODE_KANJI_QUIZ_SELECT = 102;
    public static final int RESULT_CODE_ENTRY_QUIZ_FINISH = 301;
    public static final int RESULT_CODE_KANJI_QUIZ_FINISH = 302;
    private Button entryQuizBt, kanjiQuizBt;
    private TextView entryHS, kanjiHS;
    private UserObj userObj;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        // Match object to layout elements
        matchObjectToLayout(view);

        // Set data for view
        userObj = MainActivity.user;
        setData();

        // on click Entry Quiz button
        entryQuizBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EntryQuizMainActivity.class);
                intent.putExtra("user_object", userObj);
                startActivityForResult(intent, REQUEST_CODE_ENTRY_QUIZ_SELECT);
            }
        });

        // on click Kanji Quiz button
        kanjiQuizBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KanjiQuizMainActivity.class);
                intent.putExtra("user_object", userObj);
                startActivityForResult(intent, REQUEST_CODE_KANJI_QUIZ_SELECT);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ENTRY_QUIZ_SELECT) {
            switch (resultCode) {
                // Quiz end
                case RESULT_CODE_ENTRY_QUIZ_FINISH:
                    userObj = (UserObj) data.getSerializableExtra("user_object");
                    setData();
                    break;
            }
        }

        if (requestCode == REQUEST_CODE_KANJI_QUIZ_SELECT) {
            switch (resultCode) {
                // Quiz end
                case RESULT_CODE_KANJI_QUIZ_FINISH:
                    userObj = (UserObj) data.getSerializableExtra("user_object");
                    setData();
                    break;
            }
        }
    }

    /**
     * Match object to layout elements
     */
    private void matchObjectToLayout(View view) {
        entryQuizBt = (Button) view.findViewById(R.id.quiz_select_vocabulary_button);
        kanjiQuizBt = (Button) view.findViewById(R.id.quiz_select_kanji_button);
        entryHS = (TextView) view.findViewById(R.id.quiz_main_entry_score_tv);
        kanjiHS = (TextView) view.findViewById(R.id.quiz_main_kanji_score_tv);
    }

    /**
     * Set data for view
     */
    private void setData() {
        entryHS.setText(Integer.toString(userObj.getEntryHighScore()));
        kanjiHS.setText(Integer.toString(userObj.getKanjiHighScore()));
    }
}
