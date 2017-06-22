package in.org.cris.icms.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import in.org.cris.icms.R;
import in.org.cris.icms.adapters.TrainsAdapter;
import in.org.cris.icms.models.Train;

public class TrainsActivity extends AppCompatActivity {

    private int[] trainNumbers = {64466, 64422, 64076, 64463, 22403, 12801, 12397, 64074, 64421, 64003, 12426, 12391, 12206, 12565, 12002};
    private String[] trainNames = {"PNP NDLS MEMU", "NDLS GZB EMU", "NDLS PALWAL EMU", "NDLS KKDE MEMU", "PDY NDLS EXPRESS", "PURUSHOTTAM EXP", "MAHABODHI EXP", "NDLS KSV EMU", "GZB NDLS EMU", "NDLS SNP EMU", "JAMMU RAJDHANI", "SHRAMJEEVI EXP", "NANDA DEVI EXP", "BIHAR S KRANTI", "BHOPAL SHTBDI"};
    private String[] sources = {"PNP", "NDLS", "NDLS", "NDLS", "PDY", "PURI", "GAYA", "NDLS", "GZB", "NDLS", "JAT", "RGD", "DDN", "DBG", "NDLS"};
    private String[] destinations = {"NDLS", "GZB", "PWL", "KKDE", "NDLS", "NDLS", "NDLS", "KSV", "NDLS", "SNP", "NDLS", "NDLS", "NDLS", "NDLS", "HBJ"};
    private String[] arrTimes = {"00:05", "03:20", "04:30", "04:40", "04:55", "05:00", "05:10", "05:20", "05:35", "00:10", "00:15", "01:20", "04:45", "05:00", "06:00"};
    private String[] deptTimes = {"06:00", "05:00", "04:45", "01:20", "00:15", "00:10", "05:35", "05:20", "05:10", "05:00", "04:55", "04:40", "04:30", "03:20", "00:05"};

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
        TrainsAdapter adapter = new TrainsAdapter(trainsList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (int i=0; i<trainNumbers.length; i++) {
            Train train = new Train();
            train.setTrainNo(trainNumbers[i]);
            train.setName(trainNames[i]);
            train.setSource(sources[i]);
            train.setDestination(destinations[i]);
            train.setArrTime(arrTimes[i]);
            train.setDepTime(deptTimes[i]);
            train.setArrDate("21st June");
            train.setDepDate("20th June");
            trainsList.add(train);
            adapter.notifyDataSetChanged();
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
}
