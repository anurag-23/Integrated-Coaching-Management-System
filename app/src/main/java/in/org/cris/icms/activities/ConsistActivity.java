package in.org.cris.icms.activities;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import in.org.cris.icms.fragments.ConfirmArrivalDialogFragment;
import in.org.cris.icms.R;
import in.org.cris.icms.adapters.ConsistAdapter;
import in.org.cris.icms.models.Coach;

public class ConsistActivity extends AppCompatActivity implements ConfirmArrivalDialogFragment.ConfirmArrivalInterface{
    private List<Coach> coachList = new ArrayList<>();
    private ConsistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consist);

        Toolbar toolbar = (Toolbar)findViewById(R.id.consist_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(getIntent().getStringExtra("trainName"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.consist_recycler_view);
        adapter = new ConsistAdapter(coachList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button verifyButton = (Button)findViewById(R.id.verify_button);

        for (int i=0; i<15; i++){
            Coach coach = new Coach();
            coach.setSerialNo(i+1);
            coach.setCoachNo(12580+i+"");
            coach.setCoachType("HA");
            coach.setFrom(getIntent().getStringExtra("source"));
            coach.setTo(getIntent().getStringExtra("destination"));
            coach.setOwnRly("SER");
            coach.setRemark("No remark");
            coach.setPrsID("12345");
            coachList.add(coach);
            adapter.notifyDataSetChanged();
        }

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmArrivalDialogFragment dialog = ConfirmArrivalDialogFragment.createInstance();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dialog.show(getSupportFragmentManager(), getString(R.string.confirm_arrival));
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
                    tempList.get(i).setSerialNo(tempList.size()-i);
                    coachList.add(tempList.get(i));
                }
                adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
}
