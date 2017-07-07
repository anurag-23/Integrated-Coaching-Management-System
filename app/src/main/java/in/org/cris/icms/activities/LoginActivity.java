package in.org.cris.icms.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.org.cris.icms.R;
import in.org.cris.icms.models.login.LoginRequest;
import in.org.cris.icms.models.login.LoginResponse;
import in.org.cris.icms.models.login.User;
import in.org.cris.icms.network.LoginClient;
import in.org.cris.icms.network.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int OVERWRITE_CODE = 2;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private String errorMessage = "";
    private LinearLayout childLayout;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        childLayout = (LinearLayout)findViewById(R.id.login_child_linear_layout);
        loginButton = (Button)findViewById(R.id.login_button);
        userNameEditText = (EditText)findViewById(R.id.username_edit_text);
        passwordEditText = (EditText)findViewById(R.id.password_edit_text);

        //Internet check
        if (!NetworkUtils.isInternetConnected(this)){
            showRetryAlertMessage(getString(R.string.no_internet), getString(R.string.no_internet_message), true);
        }else{
            checkLogin();
        }

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
                    showAlertMessage(null, errorMessage);
                else{
                    if (!NetworkUtils.isInternetConnected(LoginActivity.this)){
                        showRetryAlertMessage(getString(R.string.no_internet), getString(R.string.no_internet_message), false);
                    }else{
                        //Manual login; Required parameters: newSession, overwrite, username, password
                        User user = new User();
                        user.setUsername(userNameEditText.getText().toString());
                        user.setPassword(passwordEditText.getText().toString());

                        LoginRequest request = new LoginRequest();
                        request.setNewSession(true);
                        request.setOverwriteFlag(false);
                        request.setUser(user);

                        attemptLogin(request);
                    }
                }
            }
        });
    }

    private void checkLogin(){
        sp = getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE);

        if(sp.getBoolean(getString(R.string.logged_in), false)){
            //User already logged in
            String username = sp.getString(getString(R.string.username), "");
            String serviceID = sp.getString(getString(R.string.service_id),"");

            //Background login; Required parameters: newSession, loginTime, username, serviceID
            User user = new User();
            user.setUsername(username);
            LoginRequest request = new LoginRequest();
            request.setUser(user);
            request.setNewSession(false);
            request.setLoginTime(SDF.format(new Date()));
            request.setServiceID(serviceID);
            attemptLogin(request);
        }else{
            //Manual login required
            childLayout.setVisibility(View.VISIBLE);
        }
    }

    private void attemptLogin(final LoginRequest loginRequest){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.attempting_login));
        progressDialog.setCanceledOnTouchOutside(false);
        if (loginRequest.isNewSession()) progressDialog.show(); //Shows progress dialog only in case of manual login

        //Login call at a delay of 1s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Calls login service using Retrofit
                Call<LoginResponse> call = LoginClient.getLoginInterface().attemptLogin(loginRequest);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        LoginResponse loginResponse;

                        if (response != null && (loginResponse = response.body()) != null){
                            //Save details to SharedPreferences in case of successful login
                            SharedPreferences.Editor spEditor = sp.edit();
                            if (loginResponse.isSuccess()){
                                spEditor.putString(getString(R.string.service_id), loginResponse.getServiceID());
                                if(loginRequest.isNewSession()){
                                    spEditor.putBoolean(getString(R.string.logged_in), true);
                                    spEditor.putString(getString(R.string.username), loginRequest.getUser().getUsername());
                                    spEditor.putString(getString(R.string.password), loginRequest.getUser().getPassword());
                                }
                                startMainActivity();
                            }
                            else{
                                if (loginResponse.getResponseCode() == OVERWRITE_CODE){
                                    //Ask user if they wish to overwrite session
                                    showOverwriteMessage(loginRequest, loginResponse);
                                }
                                else{
                                    //Show failure/error message in case of manual login
                                    //Log out unauthorized user in case of background login
                                    if (loginRequest.isNewSession()) showAlertMessage(null, loginResponse.getMessage());
                                    else{
                                        childLayout.setVisibility(View.VISIBLE);
                                        spEditor.putBoolean(getString(R.string.logged_in), false);
                                        spEditor.remove(getString(R.string.service_id));
                                        spEditor.remove(getString(R.string.username));
                                        spEditor.remove(getString(R.string.password));
                                    }
                                }
                            }
                            spEditor.apply();
                        }else{
                            if (loginRequest.isNewSession())
                                showAlertMessage(null, getString(R.string.server_error));
                            else
                                showRetryAlertMessage(null, getString(R.string.server_error), true);
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        String errorTitle, errorMessage;

                        if (t.getClass().getSimpleName().equals(ConnectException.class.getSimpleName())
                                || t.getClass().getSimpleName().equals(SocketTimeoutException.class.getSimpleName())) {
                            errorTitle = getString(R.string.cannot_connect);
                            errorMessage= getString(R.string.connection_failed);
                        }else {
                            errorTitle = null;
                            errorMessage = getString(R.string.server_error);
                        }

                        //Show dismissable error message in case of manual login
                        //Show error message with retry button in case of background login
                        if (loginRequest.isNewSession()) showAlertMessage(errorTitle, errorMessage);
                        else showRetryAlertMessage(errorTitle, errorMessage, true);
                    }
                });
            }
        }, 1000);
    }

    private void showAlertMessage(String title, String message){
        if (message != null){
            new AlertDialog.Builder(this).setTitle(title == null ? "" : title).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
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

    private void showOverwriteMessage(final LoginRequest request, final LoginResponse response){
        if (request != null && response != null){
            new AlertDialog.Builder(this).setMessage(response.getMessage()).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Send new request which overwrites existing session
                    request.setOverwriteFlag(true);
                    dialogInterface.dismiss();
                    attemptLogin(request);
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    private void showRetryAlertMessage(String title, String message, final boolean checkLogin){
        if (message != null){
            new AlertDialog.Builder(this).setTitle((title == null) ? "" : title).setMessage(message).setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (NetworkUtils.isInternetConnected(LoginActivity.this)){
                        if (checkLogin) checkLogin();
                        else loginButton.callOnClick();
                    }else{
                        showRetryAlertMessage(getString(R.string.no_internet), getString(R.string.no_internet_message), checkLogin);
                    }
                }
            }).setCancelable(false).show();
        }
    }
}
