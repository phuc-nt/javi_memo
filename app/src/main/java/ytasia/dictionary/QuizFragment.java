package ytasia.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class QuizFragment extends Fragment {

    private Button entryQuizBt;
    private Button kanjiQuizBt;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        // Match object to layout elements
        entryQuizBt = (Button) view.findViewById(R.id.quiz_select_vocabulary_button);
        kanjiQuizBt = (Button) view.findViewById(R.id.quiz_select_kanji_button);

        entryQuizBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EntryQuizMainActivity.class);
                startActivity(intent);
            }
        });

        // on click Kanji Quiz button
        kanjiQuizBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KanjiQuizMainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
