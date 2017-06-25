package in.org.cris.icms.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import in.org.cris.icms.R;
import in.org.cris.icms.adapters.RakeConsistAdapter;
import in.org.cris.icms.models.Coach;

public class SendRakeActivity extends AppCompatActivity {
    private List<Coach> coachList = new ArrayList<>();
    private int selectedDate;
    private int selectedMonth;
    private int selectedYear;
    private int selectedHour;
    private int selectedMinute;
    private List<String> stationYardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_rake);

        Toolbar toolbar = (Toolbar)findViewById(R.id.send_rake_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.send_rake_to_yard);
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
        final TextView movementDate = (TextView)findViewById(R.id.movement_date_text_view);
        final TextView movementTime = (TextView)findViewById(R.id.movement_time_text_view);
        final TextView selectStationYard = (TextView)findViewById(R.id.select_station_yard_text_view);

        final ImageView consistHideShow = (ImageView)findViewById(R.id.consist_hide_show_image_view);

        LinearLayout showConsistDetails = (LinearLayout)findViewById(R.id.show_consist_details);
        LinearLayout movementDateLayout = (LinearLayout)findViewById(R.id.movement_date_layout);
        LinearLayout movementTimeLayout = (LinearLayout)findViewById(R.id.movement_time_layout);

        final RadioButton stationRadioButton = (RadioButton)findViewById(R.id.station_radio_button);
        stationRadioButton.setChecked(true);
        final RadioButton yardRadioButton = (RadioButton)findViewById(R.id.yard_radio_button);

        final Spinner stationYardSpinner = (Spinner)findViewById(R.id.station_yard_spinner);
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
        date.setText(getIntent().getStringExtra("date"));

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm", Locale.US);

        Date currentDate = new Date();
        movementDate.setText(sdf1.format(currentDate));
        movementTime.setText(sdf2.format(currentDate));
        final Calendar calendar = Calendar.getInstance();
        selectedDate = calendar.get(Calendar.DAY_OF_MONTH);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedYear = calendar.get(Calendar.YEAR);
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        movementDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(SendRakeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        selectedDate = i2;
                        selectedMonth = i1+1;
                        selectedYear = i;
                        movementDate.setText(String.format("%02d", selectedDate)+"/"+String.format("%02d", selectedMonth)+"/"+selectedYear);
                    }
                }, selectedYear, selectedMonth, selectedDate);
                dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                dialog.show();
            }
        });

        movementTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(SendRakeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        selectedHour = i;
                        selectedMinute = i1;
                        movementTime.setText(String.format("%02d", selectedHour)+":"+String.format("%02d", selectedMinute));
                    }
                }, selectedHour, selectedMinute, true).show();
            }
        });

        populateSpinner(1);
        stationYardSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_custom_spinner, stationYardList));
        stationYardSpinner.setSelection(0);

        stationRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    yardRadioButton.setChecked(false);
                    selectStationYard.setText(getString(R.string.select_station));
                    populateSpinner(1);
                    stationYardSpinner.setSelection(0);
                }
            }
        });

        yardRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    stationRadioButton.setChecked(false);
                    selectStationYard.setText(getString(R.string.select_yard));
                    populateSpinner(0);
                    stationYardSpinner.setSelection(0);
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stationYardSpinner.getSelectedItem().toString().equals(getString(R.string.none))){
                    new AlertDialog.Builder(SendRakeActivity.this).setMessage(getString(R.string.please_select_station)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
                else{
                    new AlertDialog.Builder(SendRakeActivity.this).setMessage(getString(R.string.rake_movement_successful)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    }).show();
                }
            }
        });

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
    }

    private void populateSpinner(int station){
        stationYardList.clear();
        stationYardList.add(getString(R.string.none));
        String prefix = "";
        if (station == 1){
           prefix = "Station ";
        }
        else{
            prefix = "Yard ";
        }
        for (int i=0; i<5; i++){
            stationYardList.add(prefix+(i+1));
        }
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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
