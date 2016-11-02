package singularfactory.app.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import singularfactory.app.R;
import singularfactory.app.common.AppCommon;
import singularfactory.app.models.User;

public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getName();
    private static final int SPLASH_TIME_OUT = 1000; //ms

    private Handler handler;
    private Runnable runnable;

    private AppCommon appCommon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // No internet -> No App
        if (!AppCommon.getInstance().getUtils().hasInternet(AppCommon.getInstance().getApplicationContext())) {
            showSingleAlertWithReflection(this, this, getResources().getString(R.string.message_no_internet),"exitApp");
            return;
        }

        setContentView(R.layout.activity_splash);
        initialize(findViewById(android.R.id.content));
    }

    public void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
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