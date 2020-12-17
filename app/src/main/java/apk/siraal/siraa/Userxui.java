package apk.siraal.siraa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import hotchemi.android.rate.AppRate;

public class Userxui extends AppCompatActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mcardrecycler;
    private albumadapter adapter;
    private SearchView searchie;
    private RecyclerView.LayoutManager mLayoutManager;
    Toolbar toolbar;
    //drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Intent rateapp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_userxui);



        rateapp= new Intent(Intent.ACTION_VIEW);
        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);


        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
        navigationView.setCheckedItem(R.id.nav_home);
        }

        navigationView.setNavigationItemSelectedListener(this);

        //Hooks
        mcardrecycler = findViewById(R.id.card_recycler);
        cardrecycler();
        initViews();
        initListener();


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

   }

    private void initViews() {
        searchie = findViewById(R.id.searchie);
    }

    private void initListener() {
        searchie.setOnQueryTextListener(this);
    }

    public void cardrecycler() {

        mLayoutManager= new LinearLayoutManager(this);
       mcardrecycler.setHasFixedSize(true);
       mcardrecycler.setLayoutManager(mLayoutManager);

        ArrayList<cardhelper> cardalbums = new ArrayList<>();

        cardalbums.add(new cardhelper(R.drawable.new_disc, "Al Fatiha", R.raw.fatiha, "7 Versets"));
        cardalbums.add(new cardhelper(R.drawable.new_disc, "Al Baqarah", R.raw.verse, "286 Versets"));

        adapter = new albumadapter(cardalbums);
        mcardrecycler.setAdapter(adapter);


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filteri(newText);
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                break;

            case R.id.About:
                Intent intent = new Intent(Userxui.this,apropos.class);
                startActivity(intent);
                break;

            case R.id.Help:
                Intent help = new Intent(Userxui.this,contact.class);
                startActivity(help);
                break;

            case R.id.shar :
                try{
                    Intent shar = new Intent(Intent.ACTION_SEND);
                    shar.setType("text/plain");
                    shar.putExtra(Intent.EXTRA_SUBJECT,"J'apprend le Coran en Bambara avec l'appli Siraa. Toi aussi Rejoins-moi, disponible dans le playstore");
                    shar.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());
                    startActivity(Intent.createChooser(shar,"Partager avec"));

                }catch (Exception e){
                    Toast.makeText(this,"Impossible de partager cette application.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.rate :
                rateapp.setData(Uri.parse(""));
                startActivity(rateapp);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);


        return true;
    }


}
