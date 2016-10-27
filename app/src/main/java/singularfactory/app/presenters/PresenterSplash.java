package singularfactory.app.presenters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import singularfactory.app.AppCommon;
import singularfactory.app.BuildConfig;
import singularfactory.app.R;
import singularfactory.app.Tags;
import singularfactory.app.common.Logs;
import singularfactory.app.common.Volley;
import singularfactory.app.presenters.interfaces.IPresenterSplash;
import singularfactory.app.views.activities.BaseActivity;
import singularfactory.app.views.activities.SplashActivity;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public class PresenterSplash implements IPresenterSplash {

    private static final String TAG = PresenterSplash.class.getSimpleName();
    private AppCommon appCommon     = AppCommon.getInstance();
    private ProgressDialog pDialog  = null;

    /*******************/
    /**** API CALLS ****/
    /*******************/

    /** SplashActivity **/
    @Override
    public void getGlobalConfig(Object object) {

        Logs.SystemLog(Tags.WS_GET_GLOBAL_CONFIG);

        Activity activity;
        if (object instanceof Fragment)
            activity = ((Fragment) object).getActivity();
        else
            activity = (Activity) object;

        //No internet
        if (!AppCommon.getInstance().getUtils().hasInternet(AppCommon.getInstance().getApplicationContext())) {
            BaseActivity.showSingleAlert(activity, appCommon.getApplicationContext().getResources().getString(R.string.message_no_internet));
            return;
        }

//        String message  = AppMediator.getInstance().getString(R.string.message_update_status_child);
        String url      = BuildConfig.BASE_URL + "api/config/global";
        String[] params = {};

        appCommon.getModel().volleyAsynctask(object, Tags.WS_GET_GLOBAL_CONFIG, Request.Method.GET, url, "", false, params);
    }

    /*******************/
    /** API RESPONSES **/
    /*******************/

    /** Response error **/
    @Override
    public void responseError(Object object, String message) {

        if (object instanceof SplashActivity) {
            ((SplashActivity) object).responseErrorGetGlobalConfig(message);
        }
    }

    /** Response OK - GetGlobalConfig **/
    @Override
    public void responseGetGlobalConfig(Object object, String apiVersion, String mediaUrl) {

        if (object instanceof SplashActivity) {
            ((SplashActivity) object).responseGetGlobalConfig(apiVersion, mediaUrl);
        }
    }
}
