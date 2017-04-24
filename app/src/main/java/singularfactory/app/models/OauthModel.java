package singularfactory.app.models;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import singularfactory.app.R;
import singularfactory.app.common.AppCommon;
import singularfactory.app.views.activities.BaseActivity;

public class OauthModel {

    private static final String TAG = OauthModel.class.getSimpleName();
    private static OauthModel singleton = null;
    private final AppCommon appCommon = AppCommon.getInstance();
    private ProgressDialog pDialog = null;

    public OauthModel() {
    }

    public static OauthModel getInstance() {
        if (singleton == null) singleton = new OauthModel();
        return singleton;
    }

    private void checkStatusOnResponse(Object object, String result, String tag, int httpStatus) {
        if (httpStatus == 200) {
            Log.i(tag, " - OK: " + httpStatus);
            appCommon.getPresenterUser().getOauthTokenResponse(object, result);

        } else {
            Log.e(tag, " - ERROR: " + httpStatus);
            appCommon.getPresenterUser().responseError(object, appCommon.getString(R.string.error_login));
        }
    }


    public void volleyAsynctask(final Object object, final String tagRequest, int verb, String url,
                                String dialogMessage, boolean showDialog, final String [] usernameAndPassword) {
        if (showDialog) {
            Context context;
            if (object instanceof BaseActivity) context = (BaseActivity) object;
            else context = ((Fragment) object).getActivity();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage(dialogMessage);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        final AppCommon appCommon = AppCommon.getInstance();
        StringRequest request = new StringRequest(verb, url
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String result) {
                if (pDialog != null && pDialog.isShowing()) pDialog.dismiss();
                checkStatusOnResponse(object, result, tagRequest, 200);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                int httpStatus = 500;
                if (networkResponse != null) httpStatus = networkResponse.statusCode;
                if (pDialog != null && pDialog.isShowing()) pDialog.dismiss();
                checkStatusOnResponse(object, "", tagRequest, httpStatus);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();

                params.put("grant_type", "password");
                params.put("client_id", appCommon.getOauthClientId());
                params.put("client_secret", appCommon.getOauthClientSecret());
                params.put("username", usernameAndPassword[0]);
                params.put("password", usernameAndPassword[1]);

                return params;
            }
        };
        Volley.getInstance(appCommon.getApplicationContext()).addToRequestQueue(request, tagRequest);
    }
}
