package singularfactory.app.models;


import android.app.ProgressDialog;
import android.nfc.Tag;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import singularfactory.app.common.AppCommon;
import singularfactory.app.common.Volley;

public class Model {

    private static final String TAG = Model.class.getSimpleName();
    private static Model singleton = null;
    private final AppCommon appCommon   = AppCommon.getInstance();
    private ProgressDialog pDialog  = null;
    public Model() {
        //constructor
    }
    public static Model getInstance() {
        if (singleton == null) {
            singleton = new Model();
        }
        return singleton;
    }

    public void checkStatusOnResponse(Object object, JSONArray result, String tag, int httpStatus) throws JSONException {
        if (httpStatus == 200) {
            Log.i(tag, " - OK 200");
            onResponseOK(object, tag, result);
        } else if (httpStatus == 404) {
            Log.e(tag, " - ERROR 404");
            onResponseError(object, tag, "Not found");
        } else {
            Log.e(tag, " - ERROR 500");
            onResponseError(object, tag, "Server error");
        }
    }

    public void onResponseError(Object object, String tag, String message) {
        Log.e(TAG, " - onResponseError");
        switch (tag) {
            case "Get texts":
                appCommon.getPresenterTexts().responseError(object,message);
                break;
            case "Login user":
                appCommon.getPresenterUser().responseError(object,message);
                break;
            default:
                break;
        }
    }

    public void onResponseOK(Object object, String tag, JSONArray json) throws JSONException {
        Log.i(TAG, " - onResponseOK");
        switch (tag) {
            case "Get texts":
                appCommon.getPresenterTexts().getTextsResponse(object,json);
                break;
            case "Login user":
                appCommon.getPresenterUser().loginUserResponse(object,json);
                break;
            default:
                break;
        }
    }

    public void volleyAsynctask(final Object object, final String tagRequest, int verb, String url, String dialogMessage, boolean showDialog, final String... params) {
        if (showDialog) {
            Fragment fragment = (Fragment) object;
            pDialog = new ProgressDialog(fragment.getContext());
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(dialogMessage);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        final AppCommon appCommon   = AppCommon.getInstance();
        final JsonArrayRequest request = new JsonArrayRequest(verb, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.i(TAG + "_" + tagRequest, "OK");

                if (pDialog != null && pDialog.isShowing()) pDialog.dismiss();

                try {
                    checkStatusOnResponse(object, jsonArray, tagRequest, 200);
                } catch (JSONException e) {
                    Log.e(TAG,"JSON error");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;int httpStatus = 400;

                try {
                    if (networkResponse != null) httpStatus = networkResponse.statusCode;
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (pDialog != null && pDialog.isShowing()) pDialog.dismiss();

                try {
                    checkStatusOnResponse(object, new JSONArray(), tagRequest, httpStatus);
                } catch (JSONException e) {
                    Log.e(TAG,"JSON error");
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=utf-8");
//                headers.put("Authorization", "Bearer " + AppMediator.getInstance().sharedGetValue(AppMediator.getInstance().getApplicationContext(), Tags.SHARED_ACCESS_TOKEN, 1));
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> paramsMap = new HashMap<>();
                paramsMap.put("username",params[0]);
                paramsMap.put("password",params[1]);
                paramsMap.put("email",params[0]);
                Iterator it = paramsMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry)it.next();
                    Log.e(TAG,e.getKey() + " " + e.getValue());
                }
                return paramsMap;
            }
        };
        Volley.getInstance(appCommon.getApplicationContext()).addToRequestQueue(request, tagRequest);
    }
}
