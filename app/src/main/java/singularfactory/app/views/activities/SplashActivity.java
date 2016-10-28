package singularfactory.app.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import singularfactory.app.common.AppCommon;
import singularfactory.app.BuildConfig;
import singularfactory.app.R;
import singularfactory.app.views.activities.initializations.InitSplashActivity;

public class SplashActivity extends BaseActivity implements InitSplashActivity.InitSplashActivityListener {

    private static final String TAG = SplashActivity.class.getName();
    private static final int SPLASH_TIME_OUT = 1000; //ms

    private Handler handler;
    private Runnable runnable;

    public InitSplashActivity itemView;


    private AppCommon appCommon;
//    private BroadcastReceiver registrationBroadcastReceiver;
//    private ProgressBar registrationProgressBarLeft, registrationProgressBarRight;
//    private boolean googlePlayInstalled = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initialize(findViewById(android.R.id.content));
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver,
//                new IntentFilter(RegistrationIntentService.REGISTRATION_COMPLETE));
//    }
//
//    @Override
//    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(registrationBroadcastReceiver);
//        super.onPause();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
            handler = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void initialize(View view) {
        appCommon = AppCommon.getInstance();
//        registrationProgressBarLeft  = (ProgressBar) findViewById(R.id.registrationProgressBarLeft);
//        registrationProgressBarRight = (ProgressBar) findViewById(R.id.registrationProgressBarRight);

        itemView = new InitSplashActivity(this);
        itemView.initialize(view);
        itemView.initializeActions();
        itemView.initializeCustomFonts();
        itemView.setInitSplashActivityListener(this);

//        broadcastTokenGCM();

        handler  = new Handler();
        runnable = new Runnable() {
            public void run() {
//                if (!googlePlayInstalled) {
//                    return;
//                }

//                registrationProgressBarLeft.setVisibility(ProgressBar.GONE);
//                registrationProgressBarRight.setVisibility(ProgressBar.GONE);

//                getGlobalConfig();
                launchLoginActivity();

            }
        };
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    private void compareApiVersion(String currentApiVersion, String apiVersionFromServer) {

        if (!currentApiVersion.equals(apiVersionFromServer)) {
            showAlertWithReflectionTwoButtons(this, this,
                    appCommon.getString(R.string.message_new_api_version),
                    appCommon.getString(R.string.button_go_to_play_store),
                    appCommon.getString(R.string.button_exit),
                    "goToGooglePlay");
            return;
        }

        //API VERSION OK. Load app
        launchLoginActivity();
    }

    private void launchLoginActivity() {
        appCommon.getUtils().launchActivity(LoginActivity.class, SplashActivity.this, null);          //Launch next activity
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);  //Show animation
        finish();   //Close this activity
    }

    /** PUBLIC METHODS **/
    public void responseErrorGetGlobalConfig(String message) {
        itemView.showLoading(false);
        BaseActivity.showSingleAlert(this, message);
    }

    public void responseGetGlobalConfig(String apiVersion, String mediaUrl) {

        itemView.showLoading(false);
        appCommon.getUtils().sharedSetValue(appCommon.getApplicationContext(), AppCommon.Tags.SHARED_API_VERSION, apiVersion);
        appCommon.getUtils().sharedSetValue(appCommon.getApplicationContext(), AppCommon.Tags.SHARED_MEDIA_URL, mediaUrl);

        compareApiVersion(BuildConfig.API_VERSION, apiVersion);
    }
    /****/

    /** ACCESSED BY REFLECTION **/
    //Accessed by method "compareApiVersion"
    public void goToGooglePlay() {
        String urlMarket = "market://details?id=" + appCommon.getApplicationContext().getPackageName();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlMarket));  //Go to Play Store and update
        startActivity(intent);
    }
    /****/

    /** API CALLS **/
    public void getGlobalConfig() {
        itemView.showLoading(true);
        appCommon.getPresenterSplash().getGlobalConfig(SplashActivity.this);
    }
    /****/

//    public void broadcastTokenGCM() {
//        registrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//                boolean sentToken = sharedPreferences.getBoolean(RegistrationIntentService.SENT_TOKEN_TO_SERVER, false);
//
//                if (sentToken) {    //OK
//                    Log.i(TAG, appMediator.getString(R.string.debug_gcm_send_message));   //Token stored in RegistrationIntentService
//                } else {            //ERROR
//                    Log.e(TAG, appMediator.getString(R.string.debug_gcm_token_error));
//                }
//            }
//        };
//
//        //Check if Google Play Services are installed
//        if (!appMediator.checkPlayServices(this)) {
//            googlePlayInstalled = false;
//            showSingleAlertWithReflection(this, appMediator.getString(R.string.message_no_google_play_services), "onBackPressed");
//            return;
//        }
//
//        // Start IntentService to register this application with GCM.
//        Intent intent = new Intent(this, RegistrationIntentService.class);
//        startService(intent);
//    }

    /** InitSplashActivity.InitSplashActivityListener **/

    /****/
}
