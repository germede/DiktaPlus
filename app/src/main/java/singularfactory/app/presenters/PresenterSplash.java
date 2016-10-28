package singularfactory.app.presenters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import com.android.volley.Request;

import singularfactory.app.common.AppCommon;
import singularfactory.app.BuildConfig;
import singularfactory.app.R;
import singularfactory.app.views.activities.BaseActivity;
import singularfactory.app.views.activities.SplashActivity;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public class PresenterSplash {

    private static final String TAG = PresenterSplash.class.getSimpleName();
    private AppCommon appCommon     = AppCommon.getInstance();
    private ProgressDialog pDialog  = null;

    /*******************/
    /**** API CALLS ****/
    /*******************/

    public void getGlobalConfig(Object object) {
        Activity activity;
        if (object instanceof Fragment)
            activity = ((Fragment) object).getActivity();
        else
            activity = (Activity) object;

        if (!AppCommon.getInstance().getUtils().hasInternet(AppCommon.getInstance().getApplicationContext())) {
            BaseActivity.showSingleAlert(activity, appCommon.getApplicationContext().getResources().getString(R.string.message_no_internet));
            return;
        }

        String url      = BuildConfig.BASE_URL + "api/config/global";
        String[] params = {};

        appCommon.getModel().volleyAsynctask(object, AppCommon.Tags.WS_GET_GLOBAL_CONFIG, Request.Method.GET, url, "", false, params);
    }

    /*******************/
    /** API RESPONSES **/
    /*******************/

    /** Response error **/
    public void responseError(Object object, String message) {

        if (object instanceof SplashActivity) {
            ((SplashActivity) object).responseErrorGetGlobalConfig(message);
        }
    }

    /** Response OK - GetGlobalConfig **/
    public void responseGetGlobalConfig(Object object, String apiVersion, String mediaUrl) {

        if (object instanceof SplashActivity) {
            ((SplashActivity) object).responseGetGlobalConfig(apiVersion, mediaUrl);
        }
    }
}
