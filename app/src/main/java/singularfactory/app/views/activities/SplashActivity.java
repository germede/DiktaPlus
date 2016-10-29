package singularfactory.app.views.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import singularfactory.app.common.AppCommon;
import singularfactory.app.R;
import singularfactory.app.models.User;

public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getName();
    private static final int SPLASH_TIME_OUT = 1000; //ms

    private Handler handler;
    private Runnable runnable;

    public View pbLoading;
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
        finish();
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

        pbLoading = view.findViewById(R.id.pbLoading);
        showLoading(true);

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

    private void launchLoginActivity() {
        appCommon.getUtils().launchActivity(LoginActivity.class, SplashActivity.this, null);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        finish();
    }

    private void launchMainActivity() {
        appCommon.getUtils().launchActivity(MainActivity.class, SplashActivity.this, null);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        finish();
    }

    public void showLoading(boolean enable) {
        if (!enable) {
            pbLoading.setVisibility(View.GONE);
            return;
        }
        pbLoading.setVisibility(View.VISIBLE);
    }
}