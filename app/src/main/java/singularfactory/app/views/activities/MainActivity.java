package singularfactory.app.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import singularfactory.app.R;
import singularfactory.app.views.fragments.BaseFragment;
import singularfactory.app.views.fragments.GameFragment;
import singularfactory.app.views.fragments.RankingFragment;
import singularfactory.app.views.fragments.TextFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    BaseFragment actualFragment;
    TextFragment textFragment;
    GameFragment gameFragment;
    RankingFragment rankingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initialize(findViewById(android.R.id.content));
        initializeActionBarAndToggle();

        textFragment = new TextFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_main_container, textFragment).commit();
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

    private void initialize(View view) {

        toolbar           = (Toolbar)              view.findViewById(R.id.toolbar);
        drawer            = (DrawerLayout)         view.findViewById(R.id.drawer_layout);
        navigationView    = (NavigationView)       view.findViewById(R.id.nav_view);
    }

    private void initializeActionBarAndToggle() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onClickStartGame(View view) {
        gameFragment = new GameFragment();
        gameFragment.setTextToPlay(textFragment.getSelectedText());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragment_main_container, gameFragment);
        transaction.addToBackStack(TAG);
        transaction.commit();
        actualFragment = gameFragment;
    }

    public void changeToTextFragment(int animIn, int animOut) {
        if (gameFragment != null) gameFragment.stopDictation();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(animIn, animOut);
        transaction.replace(R.id.fragment_main_container, textFragment);
        transaction.commit();
        actualFragment = textFragment;
    }

    public void changeToRankingFragment() {
        rankingFragment = new RankingFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top);
        transaction.replace(R.id.fragment_main_container, rankingFragment);
        transaction.commit();
        actualFragment = rankingFragment;
    }

    public void logout() {
        appCommon.getUtils().launchActivity(LoginActivity.class, MainActivity.this, null);
        finish();
    }



    /**
     * Settings
     **/
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_dictation:
                if (!(actualFragment instanceof TextFragment))
                    changeToTextFragment(R.anim.slide_in_top, R.anim.slide_out_bottom);
                break;
            case R.id.nav_ranking:
                if (!(actualFragment instanceof RankingFragment)) changeToRankingFragment();
                break;
            case R.id.nav_friends:

                break;
            case R.id.nav_settings:

                break;
            case R.id.nav_logout:
                appCommon.getUtils().sharedRemoveValue(this, "id");
                logout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
