package singularfactory.app.views.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import singularfactory.app.AppCommon;
import singularfactory.app.R;
import singularfactory.app.models.Model;
import singularfactory.app.views.activities.initializations.InitMainActivity;
import singularfactory.app.views.fragments.GameFragment;
import singularfactory.app.views.fragments.LoginFragment;
import singularfactory.app.views.fragments.SignupFragment;
import singularfactory.app.views.fragments.TextsFragment;

public class MainActivity extends BaseActivity implements InitMainActivity.InitMainActivityListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public InitMainActivity itemView;
    TextsFragment textsFragment;
    GameFragment gameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (AppCommon.getInstance().getModel() == null) {
            AppCommon.getInstance().setModel(Model.getInstance());
            //Detect if model is null. App lost resources... you may need to reinitialize something else...
        }

        initialize(findViewById(android.R.id.content));
        initializeActionBarAndToggle();

        textsFragment = new TextsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_main_container, textsFragment).commit();
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

        itemView = new InitMainActivity(this);
        itemView.initialize(view);
        itemView.initializeActions();
        itemView.initializeCustomFonts();
        itemView.setInitMainActivityListener(this);
    }

    private void initializeActionBarAndToggle() {
        //ActionBar
        setSupportActionBar(itemView.toolbar);

        //Drawer Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, itemView.drawer, itemView.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        itemView.drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Navigation
        itemView.navigationView.setNavigationItemSelectedListener(this);
    }

    public void onClickStartGame(View view) {
        // Replace the texts fragment with the game fragment
        gameFragment = new GameFragment();
        gameFragment.setTextToPlay(textsFragment.getSelectedText());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragment_main_container, gameFragment);
        transaction.commit();
    }

    public void onClickPlay(View view) {
        gameFragment.play();
    }

    /** Settings **/
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    /****/

    /** NavigationView.OnNavigationItemSelectedListener **/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dictation) {

        } else if (id == R.id.nav_ranking) {

        } else if (id == R.id.nav_friends) {

        } else if (id == R.id.nav_settings) {

        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        itemView.drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /****/


}
