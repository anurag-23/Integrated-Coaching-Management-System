package in.org.cris.icms.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import in.org.cris.icms.R;
import in.org.cris.icms.encryption.Encrypter;
import in.org.cris.icms.fragments.ImageSelector;
import in.org.cris.icms.models.logout.LogoutRequest;
import in.org.cris.icms.models.logout.LogoutResponse;
import in.org.cris.icms.network.LoginClient;
import in.org.cris.icms.network.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int FAILURE_CODE = 0;
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private SharedPreferences sp;
    private volatile boolean active;
    private ImageView profilePicture;
    private static final String IMAGES_DIRECTORY = "/ICMS/Media/Images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        active = true;

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.app_name);

        sp = getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE);

        final DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.main_drawer_layout);
        NavigationView navView = (NavigationView)findViewById(R.id.main_nav_view);

        //Set up Navigation Drawer
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        CardView consistVerification = (CardView)findViewById(R.id.consist_verification);
        CardView rakeMovement = (CardView)findViewById(R.id.rake_movement);
        CardView sickMarking = (CardView)findViewById(R.id.sick_marking);
        CardView shopMarking = (CardView)findViewById(R.id.shop_marking);

        //Set menu item onClickListeners
        consistVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TrainsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_diagonal);
            }
        });

        rakeMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RakeMovementActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_diagonal);
            }
        });

        sickMarking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SickMarkingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_diagonal);
            }
        });

        shopMarking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShopMarkingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_diagonal);
            }
        });

        //Set Navigation Drawer Item Selected Listener
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch(item.getItemId()){
                    case R.id.nav_view_log_out: if (NetworkUtils.isInternetConnected(MainActivity.this))
                                                    logOut();
                                                else
                                                    showNoConnectionMessage();
                                                break;
                }
                return true;
            }
        });

        //Display username in nav view header
        View headerView = navView.getHeaderView(0);
        TextView userName = (TextView)headerView.findViewById(R.id.nav_header_username_text_view);
        userName.setText(sp.getString(getString(R.string.username), ""));

        //Set profile picture in nav view header
        FrameLayout pictureLayout = (FrameLayout)headerView.findViewById(R.id.nav_header_picture_frame);
        profilePicture = (ImageView)headerView.findViewById(R.id.nav_header_profile_picture);
        Bitmap image = loadImage();
        if (image != null) profilePicture.setImageBitmap(image);

        pictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSelector.launchImageSelector(getSupportFragmentManager());
            }
        });
    }

    private void logOut(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.logging_out));
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Required parameters for log out request: username, serviceID
        final LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername(sp.getString(getString(R.string.username), ""));
        logoutRequest.setServiceID(sp.getString(getString(R.string.service_id), ""));

        //Log out call at a delay of 1s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Calls logout service using Retrofit
                Call<LogoutResponse> call = LoginClient.getLoginInterface().logOut(logoutRequest);
                call.enqueue(new Callback<LogoutResponse>() {
                    @Override
                    public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                        if(progressDialog.isShowing()) progressDialog.dismiss();
                        LogoutResponse logoutResponse;

                        if (response != null && (logoutResponse = response.body()) != null){
                            if (logoutResponse.isSuccess() || logoutResponse.getResponseCode() == FAILURE_CODE){
                                //Log out if successful
                                //Log out in case of failure, as the user is unauthorized
                                SharedPreferences.Editor spEditor = sp.edit();
                                spEditor.putBoolean(getString(R.string.logged_in), false);
                                spEditor.remove(getString(R.string.username));
                                spEditor.remove(getString(R.string.service_id));
                                spEditor.remove(getString(R.string.refresh_token));
                                spEditor.remove(getString(R.string.login_time));
                                spEditor.apply();
                                returnToLoginActivity();
                            }else{
                                //Show message in case of error
                                showAlertMessage(null, logoutResponse.getMessage());
                            }
                        }else{
                            //Show error message in case of null response
                            showAlertMessage(null, getString(R.string.server_error));
                        }
                    }
                    @Override
                    public void onFailure(Call<LogoutResponse> call, Throwable t) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        String errorTitle, errorMessage;

                        if (t.getClass().getSimpleName().equals(ConnectException.class.getSimpleName())
                                || t.getClass().getSimpleName().equals(SocketTimeoutException.class.getSimpleName())) {
                            //Failure do to no connection
                            errorTitle = getString(R.string.cannot_connect);
                            errorMessage= getString(R.string.connection_failed);
                        }else {
                            //Failure due to other errors
                            errorTitle = null;
                            errorMessage = getString(R.string.server_error);
                        }
                        showAlertMessage(errorTitle, errorMessage);
                    }
                });
            }
        }, 1000);
    }

    private void showAlertMessage(String title, String message){
        if (active && message != null){
            new AlertDialog.Builder(this).setTitle((title == null) ? "" : title).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }

    private void showNoConnectionMessage(){
        if (active){
            new AlertDialog.Builder(this).setTitle(getString(R.string.no_internet)).setMessage(getString(R.string.no_internet_message)).setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (NetworkUtils.isInternetConnected(MainActivity.this)){
                        logOut();
                    }else{
                        showNoConnectionMessage();
                    }
                }
            }).setCancelable(false).show();
        }
    }

    private void returnToLoginActivity(){
        if (active){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.scale_up_fade_in, R.anim.scale_down_fade_out);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null){
            switch (requestCode){
                case REQUEST_GALLERY:
                    //Image selected from gallery
                    try {
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        saveImage(bm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_CAMERA:
                    //New image clicked
                    Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
                    saveImage(thumbnail);
                    break;
            }
            //Load and display image
            Bitmap image = loadImage();
            if (image != null) profilePicture.setImageBitmap(image);
        }
    }

    private Bitmap loadImage(){
        String username = sp.getString(getString(R.string.username),"");
        String uri = "file://" + Environment.getExternalStorageDirectory().toString() + IMAGES_DIRECTORY + "/" + Encrypter.generateFileName(username) + ".jpg";
        try {
            return MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveImage(Bitmap bitmap){
        String root = Environment.getExternalStorageDirectory().toString();
        File destination = new File(root + IMAGES_DIRECTORY);
        //Create directory if it doesn't exist
        destination.mkdirs();

        String username = sp.getString(getString(R.string.username), "");

        if (!username.equals("")){
            File file = new File(destination, Encrypter.generateFileName(username)+".jpg");
            //Delete file if it already exists
            if (file.exists()) file.delete();

            try {
                FileOutputStream fo = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fo);
                fo.flush();
                fo.close();
            } catch (NullPointerException|IOException e) {
                e.printStackTrace();
            }
        }
    }
}
