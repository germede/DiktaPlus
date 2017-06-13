package net.gerardomedina.diktaplus.views.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import net.gerardomedina.diktaplus.R;
import net.gerardomedina.diktaplus.common.AppCommon;
import net.gerardomedina.diktaplus.models.User;

public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getName();
    private static final int SPLASH_TIME_OUT = 1000;

    private Handler handler;
    private Runnable runnable;

    private AppCommon appCommon;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!AppCommon.getInstance().getUtils().hasInternet(AppCommon.getInstance().getApplicationContext())) {
            showSingleAlertWithReflection(this, this, getResources().getString(R.string.message_no_internet),"exitApp");
            return;
        }

        setContentView(R.layout.activity_splash);

        initialize(findViewById(android.R.id.content));
    }



    private void getUserInfo(int id) {
        appCommon.getPresenterUser().getUserInfo(
                this,
                "Get user info",
                Request.Method.GET,
                appCommon.getBaseURL()+"users/"+id,
                "Trying to log in...");

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
        progressBar = (ProgressBar)findViewById(R.id.splash_progress_bar);
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 100);
        animation.setDuration (1500);
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start();

        appCommon = AppCommon.getInstance();
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                int id = (Integer) appCommon.getUtils().sharedGetValue(getApplicationContext(), "id", 2);
                if (id == 0) launchLoginActivity();
                else getUserInfo(id);
            }
        };
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    public void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}