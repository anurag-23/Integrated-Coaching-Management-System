package in.org.cris.icms.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.org.cris.icms.R;
import in.org.cris.icms.models.login.LoginRequest;
import in.org.cris.icms.models.login.LoginResponse;
import in.org.cris.icms.models.login.User;
import in.org.cris.icms.network.ICMSClient;
import in.org.cris.icms.network.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int OVERWRITE_CODE = 2;
    private static final int FAILURE_CODE = 0;
    private static final int REFRESH_CODE = 3;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private String errorMessage = "";
    private LinearLayout childLayout;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
    private SharedPreferences sp;
    private volatile boolean active;
    private static final long SESSION_DURATION = (long)1.8e+6;
    private static final long BUFFER_TIME = (long)6e+4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        active = true;

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

        //Login button hidden in case of background login
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
            String loginTime = sp.getString(getString(R.string.login_time), "");
            String refreshToken = sp.getString(getString(R.string.refresh_token), "");

            //Background login; Required parameters: newSession, username, serviceID
            User user = new User();
            user.setUsername(username);
            LoginRequest request = new LoginRequest();
            request.setUser(user);
            request.setNewSession(false);
            request.setServiceID(serviceID);

            //If session is about to expire, also send refreshToken
            try {
                Date loginDate = SDF.parse(loginTime);
                Log.d("sysTime",(new Date()).getTime()+"");
                Log.d("limit", (loginDate.getTime()+SESSION_DURATION-BUFFER_TIME)+"");
                if ((new Date()).getTime() >= loginDate.getTime() + SESSION_DURATION - BUFFER_TIME){
                    request.setRefreshToken(refreshToken);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            attemptLogin(request);
        }else{
            //Manual login required
            childLayout.setVisibility(View.VISIBLE);
        }
    }

    private void attemptLogin(final LoginRequest loginRequest){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.attempting_login));
        progressDialog.setCancelable(false);
        if (loginRequest.isNewSession()) progressDialog.show(); //Shows progress dialog only in case of manual login

        //Calls login service using Retrofit
        Call<LoginResponse> call = ICMSClient.getICMSInterface().attemptLogin(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                LoginResponse loginResponse;

                if (response != null && (loginResponse = response.body()) != null){
                    SharedPreferences.Editor spEditor = sp.edit();
                    if (loginResponse.isSuccess()){
                        //Save details to SharedPreferences in case of successful login
                        if(loginRequest.isNewSession()){
                            spEditor.putBoolean(getString(R.string.logged_in), true);
                            spEditor.putString(getString(R.string.username), loginRequest.getUser().getUsername());
                            spEditor.putString(getString(R.string.service_id), loginResponse.getServiceID());
                            spEditor.putString(getString(R.string.refresh_token), loginResponse.getRefreshToken());
                            spEditor.putString(getString(R.string.login_time), SDF.format(new Date()));
                        }
                        else if (loginResponse.getResponseCode() == REFRESH_CODE){
                            //serviceID, refreshToken and loginTime must be updated in case of session refresh
                            spEditor.putString(getString(R.string.service_id), loginResponse.getServiceID());
                            spEditor.putString(getString(R.string.refresh_token), loginResponse.getRefreshToken());
                            spEditor.putString(getString(R.string.login_time), SDF.format(new Date()));
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
                            //Log out unauthorized user in case of background login failure
                            //Show retry message in case of background login error
                            if (loginRequest.isNewSession()) showAlertMessage(null, loginResponse.getMessage());
                            else if (loginResponse.getResponseCode() == FAILURE_CODE){
                                childLayout.setVisibility(View.VISIBLE);
                                spEditor.putBoolean(getString(R.string.logged_in), false);
                                spEditor.remove(getString(R.string.service_id));
                                spEditor.remove(getString(R.string.username));
                                spEditor.remove(getString(R.string.refresh_token));
                                spEditor.remove(getString(R.string.login_time));
                            }
                            else showRetryAlertMessage(null, loginResponse.getMessage(), true);
                        }
                    }
                    spEditor.apply();
                }else{
                    //Null response
                    onFailure(call, new Exception());
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                String errorTitle, errorMessage;

                if (t.getClass().getSimpleName().equals(ConnectException.class.getSimpleName())
                        || t.getClass().getSimpleName().equals(SocketTimeoutException.class.getSimpleName())) {
                    //Failure due to no connection
                    errorTitle = getString(R.string.cannot_connect);
                    errorMessage = getString(R.string.connection_failed);
                }else {
                    //Failure due to other errors
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

    private void showAlertMessage(String title, String message){
        if (active && message != null){
            new AlertDialog.Builder(this).setTitle(title == null ? "" : title).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    private void startMainActivity(){
        if (active){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.scale_up_fade_in, R.anim.scale_down_fade_out);
            finish();
        }
    }

    private void showOverwriteMessage(final LoginRequest request, final LoginResponse response){
        if (active && request != null && response != null){
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

    private void showRetryAlertMessage(String title, String message, final boolean backgroundLogin){
        if (active && message != null){
            new AlertDialog.Builder(this).setTitle((title == null) ? "" : title).setMessage(message).setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (NetworkUtils.isInternetConnected(LoginActivity.this)){
                        if (backgroundLogin) checkLogin();
                        else loginButton.callOnClick();
                    }else{
                        showRetryAlertMessage(getString(R.string.no_internet), getString(R.string.no_internet_message), backgroundLogin);
                    }
                }
            }).setCancelable(false).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
    }
}