package in.org.cris.icms.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.org.cris.icms.R;
import in.org.cris.icms.adapters.RakeConsistAdapter;
import in.org.cris.icms.models.consistverification.Coach;

/**
 * Created by anurag on 2/7/17.
 */
public class ReceiveRakeActivity extends AppCompatActivity {
    private List<Coach> coachList = new ArrayList<>();
    private String[] ends = {"End 1", "End 2"};
    private String[] lineTypes = {"None", "Platform", "Sick Line", "Stabling Line", "Spare Line", "Siding", "Washing Line"};
    private List<String> lines = new ArrayList<>();
    private Spinner lineTypeSpinner;
    private Spinner lineSpinner;
    private Spinner endSpinner;
    private TextView capacity;
    private TextView coachesOnLine;
    private TextView availableSpace;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_receive_rake);

        View receiveRakeDetails = findViewById(R.id.receive_rake_details_layout);
        receiveRakeDetails.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar)findViewById(R.id.send_receive_rake_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.receive_rake);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final RecyclerView rakeConsistRecyclerView = (RecyclerView)findViewById(R.id.rake_consist_recycler_view);
        RakeConsistAdapter adapter = new RakeConsistAdapter(coachList, this);
        rakeConsistRecyclerView.setAdapter(adapter);
        rakeConsistRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        rakeConsistRecyclerView.setNestedScrollingEnabled(false);
        rakeConsistRecyclerView.setFocusable(false);

        TextView trainNo = (TextView)findViewById(R.id.send_rake_train_no_text_view);
        TextView trainName = (TextView)findViewById(R.id.send_rake_train_name_text_view);
        TextView date = (TextView)findViewById(R.id.send_rake_date_text_view);
        final TextView consistDetails = (TextView)findViewById(R.id.consist_details_text_view);

        lineTypeSpinner = (Spinner)findViewById(R.id.line_type_spinner);
        lineSpinner = (Spinner)findViewById(R.id.line_spinner);
        endSpinner = (Spinner)findViewById(R.id.end_spinner);

        lineTypeSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_custom_spinner, lineTypes));
        endSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_custom_spinner, ends));
        lineTypeSpinner.setSelection(0);
        endSpinner.setSelection(0);

        populateLineSpinner();
        lineSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_custom_spinner, lines));
        lineSpinner.setSelection(0);

        final LinearLayout lineInfo = (LinearLayout)findViewById(R.id.line_info);
        final ImageView consistHideShow = (ImageView)findViewById(R.id.consist_hide_show_image_view);

        LinearLayout showConsistDetails = (LinearLayout)findViewById(R.id.show_consist_details);
        Button confirmButton = (Button)findViewById(R.id.send_rake_confirm_button);

        showConsistDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rakeConsistRecyclerView.getVisibility() == View.VISIBLE){
                    rakeConsistRecyclerView.setVisibility(View.GONE);
                    consistDetails.setText(getString(R.string.show_consist_details));
                    consistHideShow.setImageResource(R.drawable.ic_down);
                }
                else{
                    rakeConsistRecyclerView.setVisibility(View.VISIBLE);
                    consistDetails.setText(getString(R.string.hide_consist_details));
                    consistHideShow.setImageResource(R.drawable.ic_up);
                }
            }
        });

        trainNo.setText(getIntent().getIntExtra("trainNo", 0)+"");
        trainName.setText(getIntent().getStringExtra("trainName"));
        date.setText(getIntent().getStringExtra("startDate"));

        for (int i=0; i<15; i++){
            Coach coach = new Coach();
            coach.setSerialNo(i+1+"");
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

        lineTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lineInfo.setVisibility(View.GONE);
                populateLineSpinner();
                lineSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        lineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View childView, int i, long l) {
                lineInfo.setVisibility(View.GONE);
                if (!lineSpinner.getSelectedItem().toString().equals(getString(R.string.none))){
                    lineInfo.setVisibility(View.VISIBLE);
                    displayLineInfo();
                }
                else{
                    lineInfo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean error = false;
                String errorMessage = "";
                if (lineSpinner.getSelectedItem().toString().equals(getString(R.string.none))){
                    errorMessage = getString(R.string.specify_details);
                    error = true;
                }
                else if (Integer.parseInt(availableSpace.getText().toString()) < coachList.size()){
                    errorMessage = getString(R.string.no_sufficient_space);
                    error = true;
                }

                if (error){
                    new AlertDialog.Builder(ReceiveRakeActivity.this).setMessage(errorMessage).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                }else{
                    new AlertDialog.Builder(ReceiveRakeActivity.this).setMessage(getString(R.string.rake_movement_successful)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    }).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: onBackPressed(); break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.scale_up, R.anim.slide_out_right);
    }

    private void populateLineSpinner(){
        String lineType = lineTypeSpinner.getSelectedItem().toString();
        lines.clear();
        lines.add(getString(R.string.none));

        if (!lineType.equals(getString(R.string.none)))
            for (int j=1; j<=5; j++)
                lines.add(lineType+" "+j);
    }

    private void displayLineInfo(){
        if (capacity == null) capacity = (TextView)findViewById(R.id.capacity_text_view);
        if (coachesOnLine == null) coachesOnLine = (TextView)findViewById(R.id.coaches_on_line_text_view);
        if (availableSpace == null) availableSpace = (TextView)findViewById(R.id.available_space_text_view);

        capacity.setText("20");
        coachesOnLine.setText("6");
        availableSpace.setText("14");
    }
}
