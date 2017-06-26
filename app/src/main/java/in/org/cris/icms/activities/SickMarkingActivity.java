package in.org.cris.icms.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.org.cris.icms.R;

public class SickMarkingActivity extends AppCompatActivity {
    private String[] ownRlys = {"None", "CR", "NR", "SR", "ER", "WR"};
    private String[] coachTypes = {"None", "H", "A", "B", "C", "HA", "HB", "F"};
    private String[] coachNos = {"None", "12340", "12341", "12343", "12344", "12345"};
    private String[] sickDepts = {"None", "Accidental Damage", "AC Defect", "Electrical Defects", "IOH"};
    private Spinner ownRlySpinner;
    private Spinner coachTypeSpinner;
    private Spinner coachNoSpinner;
    private Spinner sickDeptSpinner;
    private LinearLayout coachInfo;
    private LinearLayout sickMarkDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_marking);

        Toolbar toolbar = (Toolbar)findViewById(R.id.sick_marking_toolbar);
        setSupportActionBar(toolbar);

        TextView markingDate = (TextView)findViewById(R.id.marking_date_text_view);
        TextView coachStatus = (TextView)findViewById(R.id.coach_status_text_view);
        TextView trainDetails = (TextView)findViewById(R.id.train_details_text_view);
        TextView pohDueDate = (TextView)findViewById(R.id.poh_due_date_text_view);
        TextView lastEventTime = (TextView)findViewById(R.id.last_event_time_text_view);

        ownRlySpinner = (Spinner)findViewById(R.id.sick_own_rly_spinner);
        coachTypeSpinner = (Spinner)findViewById(R.id.sick_coach_type_spinner);
        coachNoSpinner = (Spinner)findViewById(R.id.sick_coach_no_spinner);
        sickDeptSpinner = (Spinner)findViewById(R.id.sick_department_spinner);

        Button confirmButton = (Button)findViewById(R.id.sick_marking_confirm_button);

        coachInfo = (LinearLayout) findViewById(R.id.sick_marking_coach_info);
        sickMarkDetails = (LinearLayout)findViewById(R.id.sick_marking_details);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.sick_marking);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.US);
        Date date = new Date();
        String timeStamp = sdf1.format(date)+" "+sdf2.format(date);
        markingDate.setText(timeStamp);

        ownRlySpinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_custom_spinner, ownRlys));
        coachTypeSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_custom_spinner, coachTypes));
        coachNoSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_custom_spinner, coachNos));
        sickDeptSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_custom_spinner, sickDepts));
        ownRlySpinner.setSelection(0);
        coachTypeSpinner.setSelection(0);
        coachNoSpinner.setSelection(0);
        sickDeptSpinner.setSelection(0);

        ownRlySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        coachTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerSelection(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        coachNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerSelection(2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sickDeptSpinner.getSelectedItem().toString().equals(getString(R.string.none))){
                    new AlertDialog.Builder(SickMarkingActivity.this).setMessage(R.string.sick_mark_successful).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    }).show();
                }
                else{
                    new AlertDialog.Builder(SickMarkingActivity.this).setMessage(R.string.fill_required_details).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private boolean checkSpinners(){
        try {
            if (!ownRlySpinner.getSelectedItem().toString().equals(getString(R.string.none))
                    && !coachTypeSpinner.getSelectedItem().toString().equals(getString(R.string.none))
                    && !coachNoSpinner.getSelectedItem().toString().equals(getString(R.string.none)))
                return true;
        }catch(NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void spinnerSelection(int spinnerID){
        switch(spinnerID){
            case 0: coachTypeSpinner.setSelection(0);
            case 1: coachNoSpinner.setSelection(0);
            case 2: sickDeptSpinner.setSelection(0); break;
        }
        if (checkSpinners()){
            coachInfo.setVisibility(View.VISIBLE);
            sickMarkDetails.setVisibility(View.VISIBLE);
        }
        else{
            coachInfo.setVisibility(View.GONE);
            sickMarkDetails.setVisibility(View.GONE);
        }
    }
}
