package in.org.cris.icms.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.org.cris.icms.R;
import in.org.cris.icms.models.login.LoginRequest;
import in.org.cris.icms.models.login.LoginResponse;
import in.org.cris.icms.models.login.User;
import in.org.cris.icms.network.LoginClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText userNameEditText;
    private EditText passwordEditText;
    private String errorMessage = "";
    private LinearLayout childLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        childLayout = (LinearLayout)findViewById(R.id.login_child_linear_layout);

        //Login check
        SharedPreferences sp = getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE);
        if(sp.getBoolean(getString(R.string.logged_in), false)){
            String username = sp.getString(getString(R.string.username), "");
            String password = sp.getString(getString(R.string.password), "");
            attemptLogin(false, username, password);
        }else{
            childLayout.setVisibility(View.VISIBLE);
        }

        Button loginButton = (Button)findViewById(R.id.login_button);
        userNameEditText = (EditText)findViewById(R.id.username_edit_text);
        passwordEditText = (EditText)findViewById(R.id.password_edit_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorMessage = "";
                if (userNameEditText.getText().toString().isEmpty() && passwordEditText.getText().toString().isEmpty())
                    errorMessage = getString(R.string.empty_username_password);
                else if (userNameEditText.getText().toString().isEmpty())
                    errorMessage = getString(R.string.empty_username);
                else if (passwordEditText.getText().toString().isEmpty())
                    errorMessage = getString(R.string.empty_password);

                if (!errorMessage.isEmpty())
                    showAlertMessage(errorMessage);
                else
                    attemptLogin(true, userNameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
    }

    private void attemptLogin(final boolean newSession, final String username, final String password){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        final LoginRequest request = new LoginRequest();
        request.setNewSession(newSession);
        request.setLoginTime(sdf.format(new Date()));
        request.setUser(user);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.attempting_login));
        progressDialog.setCanceledOnTouchOutside(false);
        if (newSession) progressDialog.show();

        //Login call at a delay of 500ms
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<LoginResponse> call = LoginClient.getLoginInterface().attemptLogin(request);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();

                        if (response != null){
                            LoginResponse loginResponse = response.body();
                            if (loginResponse != null){
                                if (loginResponse.isSuccess()){
                                    SharedPreferences.Editor spEditor = getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE).edit();
                                    spEditor.putString(getString(R.string.session_id), loginResponse.getSessionID());
                                    if(newSession){
                                        spEditor.putBoolean(getString(R.string.logged_in), true);
                                        spEditor.putString(getString(R.string.username), username);
                                        spEditor.putString(getString(R.string.password), password);
                                    }
                                    spEditor.apply();
                                    startMainActivity();
                                }
                                else{
                                    if (newSession) showAlertMessage(loginResponse.getMessage());
                                    else childLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        if (newSession) showAlertMessage(getString(R.string.login_failed));
                        else showAlertMessage(getString(R.string.check_internet_connection));
                    }
                });
            }
        }, 500);
    }

    private void showAlertMessage(String message){
        if (message != null){
            new AlertDialog.Builder(this).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    private void startMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.scale_up_fade_in, R.anim.scale_down_fade_out);
        finish();
    }
}
