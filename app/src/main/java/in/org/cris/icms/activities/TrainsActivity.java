package in.org.cris.icms.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import in.org.cris.icms.R;
import in.org.cris.icms.adapters.TrainsAdapter;
import in.org.cris.icms.models.consistverification.Train;
import in.org.cris.icms.models.consistverification.Trains;
import in.org.cris.icms.network.ICMSClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainsActivity extends AppCompatActivity {
    List<Train> trainsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trains);

        Toolbar toolbar = (Toolbar)findViewById(R.id.trains_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.trains);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.trains_recycler_view);
        final TrainsAdapter adapter = new TrainsAdapter(trainsList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_trains));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Calls Trains Service using Retrofit
        Call<Trains> call = ICMSClient.getICMSInterface().getTrains();
        call.enqueue(new Callback<Trains>() {
            @Override
            public void onResponse(Call<Trains> call, Response<Trains> response) {
                Trains trainsResponse;
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (response != null && (trainsResponse = response.body()) != null){
                    Log.d("Alert Message", trainsResponse.getAlertMessage());
                    //Update trainsList and notify the adapter
                    trainsList.clear();
                    trainsList.addAll(trainsResponse.getTrains());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<Trains> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
            }
        });
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
}
