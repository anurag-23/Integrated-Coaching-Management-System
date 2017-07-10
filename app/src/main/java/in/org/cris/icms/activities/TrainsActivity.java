package in.org.cris.icms.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import in.org.cris.icms.R;
import in.org.cris.icms.adapters.TrainsAdapter;
import in.org.cris.icms.models.consistverification.Train;
import in.org.cris.icms.models.consistverification.Trains;
import in.org.cris.icms.network.ICMSClient;
import in.org.cris.icms.network.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainsActivity extends AppCompatActivity {
    private List<Train> trainsList = new ArrayList<>();
    private TrainsAdapter adapter;
    private volatile boolean active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trains);
        active = true;

        Toolbar toolbar = (Toolbar)findViewById(R.id.trains_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.trains);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.trains_recycler_view);
        adapter = new TrainsAdapter(trainsList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (NetworkUtils.isInternetConnected(this)){
            loadTrains();
        }else{
            showRetryAlertMessage(getString(R.string.no_internet), getString(R.string.no_internet_message));
        }
    }

    private void loadTrains(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_trains));
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Calls Trains Service using Retrofit
        Call<Trains> call = ICMSClient.getICMSInterface().getTrains();
        call.enqueue(new Callback<Trains>() {
            @Override
            public void onResponse(Call<Trains> call, Response<Trains> response) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                Trains trains;
                if (response != null && (trains = response.body()) != null){
                    if (trains.getAlertMessage().equals("")){
                        //Update trainsList and notify the adapter
                        trainsList.clear();
                        trainsList.addAll(trains.getTrains());
                        adapter.notifyDataSetChanged();
                    }else{
                        showRetryAlertMessage(null, trains.getAlertMessage());
                    }
                }else{
                    //Null response
                    onFailure(call, new Exception());
                }
            }
            @Override
            public void onFailure(Call<Trains> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                String errorTitle, errorMessage;
                if (t.getClass().getSimpleName().equals(ConnectException.class.getSimpleName())
                        || t.getClass().getSimpleName().equals(SocketTimeoutException.class.getSimpleName())){
                    errorTitle = getString(R.string.cannot_connect);
                    errorMessage = getString(R.string.connection_failed);
                }else{
                    errorTitle = null;
                    errorMessage = getString(R.string.server_error);
                }
                showRetryAlertMessage(errorTitle, errorMessage);
            }
        });
    }

    private void showRetryAlertMessage(final String title, final String message){
        if (active && message != null){
            new AlertDialog.Builder(this).setTitle((title == null) ? "" : title).setMessage(message).setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (NetworkUtils.isInternetConnected(TrainsActivity.this)){
                        loadTrains();
                    }else{
                        showRetryAlertMessage(getString(R.string.no_internet), getString(R.string.no_internet_message));
                    }
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    onBackPressed();
                }
            }).setCancelable(false).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: onBackPressed(); break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
    }
}
