package in.org.cris.icms.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import in.org.cris.icms.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.app_name);

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.main_drawer_layout);
        NavigationView navView = (NavigationView)findViewById(R.id.main_nav_view);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        CardView consistVerification = (CardView)findViewById(R.id.consist_verification);
        CardView rakeMovement = (CardView)findViewById(R.id.rake_movement);
        CardView sickMarking = (CardView)findViewById(R.id.sick_marking);
        CardView shopMarking = (CardView)findViewById(R.id.shop_marking);

        consistVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TrainsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        rakeMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RakeMovementActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        sickMarking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SickMarkingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        shopMarking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShopMarkingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_view_log_out: logOut(); break;
                }
                return true;
            }
        });

        TextView userName = (TextView)navView.getHeaderView(0).findViewById(R.id.nav_header_username_text_view);
        userName.setText(getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE).getString(getString(R.string.username), ""));
    }

    private void logOut(){
        SharedPreferences.Editor spEditor = getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE).edit();
        spEditor.putBoolean(getString(R.string.logged_in), false);
        spEditor.remove(getString(R.string.username));
        spEditor.remove(getString(R.string.password));
        spEditor.apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.scale_up_fade_in, R.anim.scale_down_fade_out);
        finish();
    }
}
