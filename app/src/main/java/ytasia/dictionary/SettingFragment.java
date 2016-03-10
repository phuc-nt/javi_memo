package ytasia.dictionary;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingFragment extends Fragment {

    private Button appSetBt;
    private Button userSetBt;
    private Button quizSetBt;
    private Button feedbackBt;
    private Button logoutBt;


    public SettingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        appSetBt = (Button) view.findViewById(R.id.app_setting_button);
        userSetBt = (Button) view.findViewById(R.id.user_setting_button);
        quizSetBt = (Button) view.findViewById(R.id.quiz_setting_button);
        feedbackBt = (Button) view.findViewById(R.id.feedback_button);
        logoutBt = (Button) view.findViewById(R.id.logout_button);

        appSetBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AppSettingActivity.class);
                startActivity(intent);
            }
        });

        userSetBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),UserSettingActivity.class);
                startActivity(intent);
            }
        });

        quizSetBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),QuizSettingActivity.class);
                startActivity(intent);
            }
        });

        feedbackBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FeedbackActivity.class);
                startActivity(intent);
            }
        });

        logoutBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }

}
