package com.moydev.cibertecproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.moydev.cibertecproject.db.Teams;

import java.util.List;


public class MainActivity extends ActionBarActivity implements TeamFragment.OnTeamSelected, PlayersFragment.OnPlayerSelected{

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    PlayersFragment players_fragment;
    FragmentManager fragment_manager;
    FragmentTransaction fragment_transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar_app);
        //Set the custom toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_abierto, R.string.drawer_cerrado) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();

            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                actionBarDrawerToggle.syncState();
            }
        });

        fragment_manager = getSupportFragmentManager();
        fragment_transaction = fragment_manager.beginTransaction();

        players_fragment = (PlayersFragment) fragment_manager.findFragmentById(R.id.players_fragment);

        createMockupDataTeams();
        startBackgroundService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_fb_logout){
            LoginManager.getInstance().logOut();
            goLoginActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnTeamSelected(Integer teamId) {
        Toast.makeText(this, ""+teamId, Toast.LENGTH_SHORT).show();
        players_fragment.makeRequest(teamId);
    }

    @Override
    public void OnPlayerSelected(Integer teamId) {
        Toast.makeText(this, teamId, Toast.LENGTH_SHORT).show();
    }

    public void goLoginActivity(){
        Intent it = new Intent(MainActivity.this, LoginActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
    }

    public void createMockupDataTeams() {
        Teams teams = new Teams();
        List<Teams> listTeams = teams.listAll(Teams.class);
        String arrayTeams[] = {"Argelia","Argentina","Australia","Belgica","Bosnia","Brasil","Camerun","Chile","Colombia","Costa Rica","Costa de Marfil","Croacia","Ecuador","Inglaterra","Francia","Alemania","Ghana","Grecia","Honduras","Iran","Italia","Japon","Korea","Mexico","Paises Bajos","Nigeria","Portugal","Rusia","España","Suiza","Uruguay","USA"};
        if(listTeams.size() == 0){
            for(int i = 0; i<arrayTeams.length;i++){
                Teams t = new Teams(arrayTeams[i]);
                t.save();
            }
        }
    }

    public void startBackgroundService() {
        Toast.makeText(this, "Inicio Servicio.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate
        int intervalMillis = 30000; // Cada 30 segs
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);
    }
}
