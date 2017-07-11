package in.org.cris.icms.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import in.org.cris.icms.R;
import in.org.cris.icms.adapters.ShopMarkCoachAdapter;
import in.org.cris.icms.fragments.ShopMarkingDialogFragment;
import in.org.cris.icms.models.shopmarking.Coach;

public class ShopMarkingActivity extends AppCompatActivity implements ShopMarkingDialogFragment.ConfirmShopMarking {
    private String[] ownRlys = {"None", "CR", "NR", "SR", "ER", "WR"};
    private String[] coachTypes = {"None", "H", "A", "B", "C", "HA", "HB", "F"};
    private List<Coach> coachList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_marking);

        Toolbar toolbar = (Toolbar)findViewById(R.id.shop_marking_toolbar);
        setSupportActionBar(toolbar);

        final Spinner ownRlySpinner = (Spinner)findViewById(R.id.shop_own_rly_spinner);
        final Spinner coachTypeSpinner = (Spinner)findViewById(R.id.shop_coach_type_spinner);

        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.shop_marking_recycler_view);

        final LinearLayout coachesLayout = (LinearLayout)findViewById(R.id.shop_marking_coaches);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.shop_marking);
        }

        ownRlySpinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_custom_spinner, ownRlys));
        coachTypeSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_custom_spinner, coachTypes));

        ownRlySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!ownRlySpinner.getSelectedItem().toString().equals(getString(R.string.none))
                        && !coachTypeSpinner.getSelectedItem().toString().equals(getString(R.string.none))){
                    coachesLayout.setVisibility(View.VISIBLE);
                }
                else{
                    coachesLayout.setVisibility(View.GONE);
                }
                coachTypeSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        coachTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!ownRlySpinner.getSelectedItem().toString().equals(getString(R.string.none))
                        && !coachTypeSpinner.getSelectedItem().toString().equals(getString(R.string.none))){
                    coachesLayout.setVisibility(View.VISIBLE);
                }
                else{
                    coachesLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        ShopMarkCoachAdapter adapter = new ShopMarkCoachAdapter(coachList, this, getSupportFragmentManager());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

        for (int i=0; i<15; i++){
            Coach coach = new Coach();
            coach.setCoachNo(12580+i+"");
            coach.setCoachType("HA");
            coach.setOwnRly("SER");
            coach.setRemark("No remark");
            coach.setBaseDepot("NDLI");
            coach.setBuiltYear("2010");
            coach.setPohYear("2017");
            coach.setPohMonth("July");
            coachList.add(coach);
            adapter.notifyDataSetChanged();
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
        overridePendingTransition(R.anim.scale_up, R.anim.slide_out_right);
    }

    @Override
    public void confirmMarking() {
        new AlertDialog.Builder(this).setMessage(R.string.shop_mark_successful).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
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
}
