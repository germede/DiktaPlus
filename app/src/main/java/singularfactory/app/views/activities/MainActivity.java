package singularfactory.app.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import singularfactory.app.R;
import singularfactory.app.common.Utils;
import singularfactory.app.views.fragments.FriendFragment;
import singularfactory.app.views.fragments.GameFragment;
import singularfactory.app.views.fragments.RankingFragment;
import singularfactory.app.views.fragments.SettingsFragment;
import singularfactory.app.views.fragments.TextFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        PreferenceFragmentCompat.OnPreferenceStartScreenCallback {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private int previousSelectedItem;
    private int correctAnimIn;
    private int correctAnimOut;

    TextFragment textFragment;
    GameFragment gameFragment;
    RankingFragment rankingFragment;
    FriendFragment friendFragment;
    SettingsFragment settingsFragment;

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
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initialize(View view) {

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) view.findViewById(R.id.nav_view);
    }

    private void initializeActionBarAndToggle() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setTitle(R.string.app_name);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    public void changeToTextFragment(int animIn, int animOut) {
        if (gameFragment != null) gameFragment.stopDictation();
        Utils.getInstance().hideKeyboard(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(animIn, animOut);
        transaction.replace(R.id.fragment_main_container, textFragment);
        transaction.commit();
        toolbar.setTitle(R.string.app_name);
    }

    public void changeToRankingFragment(int animIn, int animOut) {
        rankingFragment = new RankingFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(animIn, animOut);
        transaction.replace(R.id.fragment_main_container, rankingFragment);
        transaction.commit();
        toolbar.setTitle(R.string.ranking);
    }

    public void changeToFriendsFragment(int animIn, int animOut) {
        friendFragment = new FriendFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(animIn, animOut);
        transaction.replace(R.id.fragment_main_container, friendFragment);
        transaction.commit();
        toolbar.setTitle(R.string.friends);
    }

    public void changeToSettingsFragment(int animIn, int animOut) {
        settingsFragment = new SettingsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(animIn, animOut);
        transaction.replace(R.id.fragment_main_container, settingsFragment);
        transaction.commit();
        toolbar.setTitle(R.string.settings);
    }

    public void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.nav_settings || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_dictation:
                if (chooseCorrectAnimation(0)) changeToTextFragment(correctAnimIn,correctAnimOut);
                previousSelectedItem = 0;
                break;
            case R.id.nav_ranking:
                if (chooseCorrectAnimation(1)) changeToRankingFragment(correctAnimIn,correctAnimOut);
                previousSelectedItem = 1;
                break;
            case R.id.nav_friends:
                if (chooseCorrectAnimation(2)) changeToFriendsFragment(correctAnimIn,correctAnimOut);
                previousSelectedItem = 2;
                break;
            case R.id.nav_settings:
                if (chooseCorrectAnimation(3)) changeToSettingsFragment(correctAnimIn,correctAnimOut);
                previousSelectedItem = 3;
                break;
            case R.id.nav_logout:
                appCommon.getUtils().sharedRemoveValue(this, "id");
                logout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean chooseCorrectAnimation (int selectedItem) {
        if (selectedItem < previousSelectedItem) {
            correctAnimIn = R.anim.slide_in_top;
            correctAnimOut = R.anim.slide_out_bottom;
            return true;
        } else if (selectedItem > previousSelectedItem) {
            correctAnimIn = R.anim.slide_in_bottom;
            correctAnimOut = R.anim.slide_out_top;
            return true;
        } else {
            return false;
        }
    }


    public void onClickStartGame(View view) {
        gameFragment = new GameFragment();
        gameFragment.setTextToPlay(textFragment.getSelectedText());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragment_main_container, gameFragment);
        transaction.addToBackStack(TAG);
        transaction.commit();
        toolbar.setTitle(textFragment.getSelectedText().getTitle());
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat preferenceFragmentCompat, PreferenceScreen preferenceScreen) {
        preferenceFragmentCompat.setPreferenceScreen(preferenceScreen);
        return true;
    }

    public void setUsernameLabelAndLevelLabel() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        View hView =  navigationView.getHeaderView(0);
        TextView usernameLabel = (TextView)hView.findViewById(R.id.username_label);
        usernameLabel.setText(getString(R.string.username_label,
                appCommon.getUser().getUsername()));
        TextView levelLabel = (TextView)hView.findViewById(R.id.level_label);
        levelLabel.setText(getString(R.string.level_label,
                appCommon.getUser().getLevel()));
    }

}
