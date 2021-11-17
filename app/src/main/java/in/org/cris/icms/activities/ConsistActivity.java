package in.org.cris.icms.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import in.org.cris.icms.R;
import in.org.cris.icms.adapters.ConsistAdapter;
import in.org.cris.icms.fragments.ConfirmArrivalDialogFragment;
import in.org.cris.icms.models.consistverification.Coach;
import in.org.cris.icms.models.consistverification.Consist;
import in.org.cris.icms.network.ICMSClient;
import in.org.cris.icms.network.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsistActivity extends AppCompatActivity implements ConfirmArrivalDialogFragment.ConfirmArrivalInterface{
    private List<Coach> coachList = new ArrayList<>();
    private ConsistAdapter adapter;
    private volatile boolean active = false;
    private static final String PARAMETER_SERVICE = "CV";
    private static final String PARAMETER_TYPE = "detail";
    private String trainNo;
    private String trainStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consist);
        active = true;

        Toolbar toolbar = (Toolbar)findViewById(R.id.consist_toolbar);
        setSupportActionBar(toolbar);

        trainNo = getIntent().getStringExtra("trainNo");
        trainStartDate = getIntent().getStringExtra("startDate");
        String arrTime = getIntent().getStringExtra("arrTime");
        String trainName = getIntent().getStringExtra("trainName");

        if (getSupportActionBar() != null){
            if (trainNo != null && arrTime != null) getSupportActionBar().setTitle(trainNo + " (Arr: " + arrTime + ")");
            if (trainName != null) getSupportActionBar().setSubtitle(trainName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.consist_recycler_view);
        adapter = new ConsistAdapter(coachList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button verifyButton = (Button)findViewById(R.id.verify_button);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmArrivalDialogFragment dialog = ConfirmArrivalDialogFragment.createInstance();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dialog.show(getSupportFragmentManager(), getString(R.string.confirm_arrival));
            }
        });

        if (NetworkUtils.isInternetConnected(this)){
            loadConsist();
        }else{
            showRetryAlertMessage(getString(R.string.no_internet), getString(R.string.no_internet_message));
        }
    }

    private void loadConsist(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_consist));
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Calls Consist Service using Retrofit
        Call<Consist> call = ICMSClient.getICMSInterface().getConsist(PARAMETER_SERVICE, PARAMETER_TYPE, trainNo, trainStartDate);
        call.enqueue(new Callback<Consist>() {
            @Override
            public void onResponse(Call<Consist> call, Response<Consist> response) {
                Consist consist;
                if (response != null && (consist = response.body()) != null){
                    if (consist.getAlertMessage().equals("")){
                        //Update coachList and notify the adapter
                        coachList.clear();
                        coachList.addAll(consist.getCoachList());
                        adapter.notifyDataSetChanged();
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    }else{
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        showRetryAlertMessage(null, consist.getAlertMessage());
                    }
                }else{
                    //Null response
                    onFailure(call, new Exception());
                }
            }
            @Override
            public void onFailure(Call<Consist> call, Throwable t) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.consist_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: onBackPressed(); break;
            case R.id.consist_reverse:
                List<Coach> tempList = new ArrayList<>();
                tempList.addAll(coachList);
                coachList.clear();
                for (int i = tempList.size()-1; i>=0; i--){
                    tempList.get(i).setSerialNo(tempList.size()-i+"");
                    coachList.add(tempList.get(i));
                }
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_up, R.anim.slide_out_right);
    }

    @Override
    public void confirmArrival() {
        new AlertDialog.Builder(ConsistActivity.this).setMessage("Consist verification successful!").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        onBackPressed();
                    }
        }).show();
    }

    @Override
    public int getCoachCount() {
        if (coachList != null) return coachList.size();
        else return 0;
    }

    private void showRetryAlertMessage(final String title, final String message){
        if (active && message != null){
            new AlertDialog.Builder(this).setTitle((title == null) ? "" : title).setMessage(message).setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (NetworkUtils.isInternetConnected(ConsistActivity.this)){
                        loadConsist();
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
    protected void onDestroy() {
        super.onDestroy();
        active = false;
    }
}
