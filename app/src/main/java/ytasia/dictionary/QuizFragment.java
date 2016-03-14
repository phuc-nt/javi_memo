package ytasia.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import dao.obj.UserObj;

public class QuizFragment extends Fragment {

    private Button entryQuizBt, kanjiQuizBt;
    private TextView entryHS, kanjiHS;
    private UserObj ob;

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
        setData();

        // on click Entry Quiz button
        entryQuizBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EntryQuizMainActivity.class);
                intent.putExtra("user_object", MainActivity.user);
                startActivity(intent);
            }
        });

        // on click Kanji Quiz button
        kanjiQuizBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KanjiQuizMainActivity.class);
                intent.putExtra("user_object", MainActivity.user);
                startActivity(intent);
            }
        });

        return view;
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
        entryHS.setText(Integer.toString(MainActivity.user.getEntryHighScore()));
        kanjiHS.setText(Integer.toString(MainActivity.user.getKanjiHighScore()));
    }
}
