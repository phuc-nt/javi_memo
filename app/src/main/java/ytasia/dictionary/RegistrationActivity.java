package ytasia.dictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameTx;
    private EditText passwordTx;
    private EditText passwordRetypeTx;
    private Button registrationBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameTx = (EditText) findViewById(R.id.reg_username_input);
        passwordTx = (EditText) findViewById(R.id.reg_password_input);
        passwordRetypeTx = (EditText) findViewById(R.id.reg_retype_password_input);
        registrationBt = (Button) findViewById(R.id.reg_button);

        registrationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
