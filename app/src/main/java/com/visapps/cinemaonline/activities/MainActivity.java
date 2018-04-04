package com.visapps.cinemaonline.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.visapps.cinemaonline.R;
import com.visapps.cinemaonline.api.ApiService;
import com.visapps.cinemaonline.enums.RequestError;
import com.visapps.cinemaonline.fragments.FavoritesFragment;
import com.visapps.cinemaonline.fragments.FilmsFragment;
import com.visapps.cinemaonline.fragments.HistoryFragment;
import com.visapps.cinemaonline.fragments.StatsFragment;
import com.visapps.cinemaonline.interfaces.MainActivityCallback;
import com.visapps.cinemaonline.interfaces.MainPresenterCallback;
import com.visapps.cinemaonline.presenters.MainPresenter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainPresenterCallback, MainActivityCallback {

    public static final int REQUEST_AUTH = 1001;
    public static final String LOGIN = "LOGIN";
    public static final String USERLOLES = "USERROLES";

    private ProgressDialog progress;
    private TextView nav_login;
    private NavigationView navigationView;

    private FilmsFragment filmsFragment;
    private FavoritesFragment favoritesFragment;
    private HistoryFragment historyFragment;
    private StatsFragment statsFragment;

    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(getString(R.string.loading));
        nav_login = navigationView.getHeaderView(0).findViewById(R.id.nav_login);
        ImageView nav_avatar = navigationView.getHeaderView(0).findViewById(R.id.nav_avatar);
        nav_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowProfile();
            }
        });
        mainPresenter = (MainPresenter) getLastCustomNonConfigurationInstance();
        if(mainPresenter == null){
            mainPresenter = new MainPresenter();
        }
        mainPresenter.onAttach(this);
        Auth();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id){
            case R.id.nav_films:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, new FilmsFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.films));
                mainPresenter.setFragmentname(getString(R.string.films));
                break;
            case R.id.nav_favourites:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, new FavoritesFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.favorites));
                mainPresenter.setFragmentname(getString(R.string.favorites));
                break;
            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, new HistoryFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.history));
                mainPresenter.setFragmentname(getString(R.string.history));
                break;
            case R.id.nav_stats:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, new StatsFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.statistics));
                mainPresenter.setFragmentname(getString(R.string.statistics));
                break;
            case R.id.nav_changeaccount:
                onLogOut();
                RequestAuth();
                break;
            case R.id.nav_exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==REQUEST_AUTH){
            if(resultCode==RESULT_OK){
                String login = data.getStringExtra(AuthActivity.LOGIN);
                String password = data.getStringExtra(AuthActivity.PASSWORD);
                SharedPreferences account = getSharedPreferences("Account", MODE_PRIVATE);
                SharedPreferences.Editor editor = account.edit();
                editor.putString("login", login);
                editor.putString("password", password);
                editor.apply();
                mainPresenter = new MainPresenter();
                mainPresenter.onAttach(this);
                mainPresenter.Auth(login,password);
            }
            else{
                finish();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDeattach();
    }

    @Override
    public void onNotLogged() {
        ApiService.getInstance().LogOut(MainActivity.this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mainPresenter;
    }

    @Override
    public void onLogIn(String login, Bundle roles) {
        nav_login.setText(login);
    }

    @Override
    public void onLogOut() {
        SharedPreferences account = getSharedPreferences("Account", MODE_PRIVATE);
        SharedPreferences.Editor editor = account.edit();
        editor.putString("login", null);
        editor.putString("password", null);
        editor.apply();
    }

    @Override
    public void onError(RequestError error) {
        showErrorDialog(ApiService.getInstance().getErrorMessage(this,error));
    }

    @Override
    public void InitFragments(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, new FilmsFragment()).commit();
        getSupportActionBar().setTitle(getString(R.string.films));
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void SetFragmentName(String name){
        getSupportActionBar().setTitle(name);
    }

    @Override
    public void RequestAuth(){
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivityForResult(intent, REQUEST_AUTH);
    }

    @Override
    public void ShowLoading() {
        progress.show();
    }

    @Override
    public void RemoveLoading() {
        progress.dismiss();
    }

    private void Auth(){
        SharedPreferences account = getSharedPreferences("Account", MODE_PRIVATE);
        String login = account.getString("login",null);
        String password = account.getString("password",null);
        mainPresenter.Auth(login,password);
    }

    private void ShowProfile(){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }

    private void showErrorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.error))
                .setMessage(message)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.reload), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Auth();
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
