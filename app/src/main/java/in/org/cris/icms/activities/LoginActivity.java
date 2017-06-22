package in.org.cris.icms.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import in.org.cris.icms.R;

public class LoginActivity extends AppCompatActivity {
    private static final String LOGIN_ID = "CRIS";
    private static final String PASSWORD = "ICMS";
    private EditText userNameEditText;
    private EditText passwordEditText;
    private String errorMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button)findViewById(R.id.login_button);
        userNameEditText = (EditText)findViewById(R.id.username_edit_text);
        passwordEditText = (EditText)findViewById(R.id.password_edit_text);

        final CoordinatorLayout rootLayout = (CoordinatorLayout)findViewById(R.id.login_coordinator_layout);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorMessage = "";
                if (userNameEditText.getText().toString().isEmpty()){
                    errorMessage = getString(R.string.empty_username);
                }
                else if (passwordEditText.getText().toString().isEmpty()) {
                    errorMessage = getString(R.string.empty_password);
                }
                else if (!userNameEditText.getText().toString().equals(LOGIN_ID)){
                    errorMessage = getString(R.string.invalid_username);
                }
                else if (!passwordEditText.getText().toString().equals(PASSWORD)){
                    errorMessage = getString(R.string.incorrect_password);
                }
                else{
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.scale_up_fade_in, R.anim.scale_down_fade_out);
                    finish();
                }

                if (!errorMessage.isEmpty()){
                    Snackbar snackbar = Snackbar.make(rootLayout, errorMessage, Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.dark_grey_text_colour));
                    snackbar.show();
                }
            }
        });
    }
}
