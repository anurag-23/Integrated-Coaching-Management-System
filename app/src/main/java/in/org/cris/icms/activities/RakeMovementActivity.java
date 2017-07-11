package in.org.cris.icms.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import in.org.cris.icms.R;
import in.org.cris.icms.adapters.RakeMovementPagerAdapter;
import in.org.cris.icms.fragments.ReceiveRakeFragment;
import in.org.cris.icms.fragments.SendRakeFragment;

public class RakeMovementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rake_movement);

        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.rake_app_bar_layout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.rake_toolbar);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.rake_tab_layout);
        ViewPager viewPager = (ViewPager)findViewById(R.id.rake_view_pager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            toolbar.setElevation(0);
            appBarLayout.setElevation(0);
            appBarLayout.setTargetElevation(0);
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.rake_movement);
        }

        RakeMovementPagerAdapter adapter = new RakeMovementPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SendRakeFragment(), getString(R.string.send));
        adapter.addFragment(new ReceiveRakeFragment(), getString(R.string.receive));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
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
        finish();
        overridePendingTransition(R.anim.scale_up, R.anim.slide_out_right);
    }
}
