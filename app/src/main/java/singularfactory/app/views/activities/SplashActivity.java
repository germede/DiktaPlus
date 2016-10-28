package singularfactory.app.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import singularfactory.app.common.AppCommon;
import singularfactory.app.BuildConfig;
import singularfactory.app.R;
import singularfactory.app.models.User;
import singularfactory.app.views.activities.initializations.InitSplashActivity;

public class SplashActivity extends BaseActivity implements InitSplashActivity.InitSplashActivityListener {

    private static final String TAG = SplashActivity.class.getName();
    private static final int SPLASH_TIME_OUT = 1000; //ms

    private Handler handler;
    private Runnable runnable;

    public InitSplashActivity itemView;

    private AppCommon appCommon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initialize(findViewById(android.R.id.content));

        int id = (Integer)appCommon.getUtils().sharedGetValue(this,"id",2);
        if (id == 0) launchLoginActivity();
        else getUserInfo(id);
    }

    private void getUserInfo(int id) {
        try {
            appCommon.getPresenterUser().getUserInfo(
                    this,
                    "Get user info",
                    Request.Method.GET,
                    appCommon.getBaseURL()+"users/"+id,
                    "Trying to log in...");
        } catch (JSONException e) {
            Log.e(TAG,"JSON Exception");
        }
    }

    public void setUserInfo(JSONObject userJson) {
        try {
            appCommon.setUser(new User(userJson.getInt("id"),
                    userJson.getString("email"),
                    userJson.getString("username"),
                    userJson.getString("country"),
                    userJson.getInt("total_score"),
                    userJson.getInt("level")));
            launchMainActivity();
        } catch (JSONException e) {
            Log.e(TAG,"Error parsing received JSON");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
            handler = null;
        }
    }

    public void initialize(View view) {
        appCommon = AppCommon.getInstance();

        itemView = new InitSplashActivity(this);
        itemView.initialize(view);
        itemView.initializeActions();
        itemView.initializeCustomFonts();
        itemView.setInitSplashActivityListener(this);

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {

            }
        };
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    private void launchLoginActivity() {
        appCommon.getUtils().launchActivity(LoginActivity.class, SplashActivity.this, null);
        overridePendingTransition(R.anim.rotate_180, R.anim.rotate_180);
        finish();
    }

    private void launchMainActivity() {
        appCommon.getUtils().launchActivity(MainActivity.class, SplashActivity.this, null);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}