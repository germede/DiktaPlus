package singularfactory.app.models;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import java.util.Map;

import singularfactory.app.common.AppCommon;
import singularfactory.app.common.Volley;

public class Model {

    private static final String TAG = Model.class.getSimpleName();
    private static Model singleton = null;
    private final AppCommon appCommon   = AppCommon.getInstance();
    private ProgressDialog pDialog  = null;
    public Model() {}
    public static Model getInstance() {
        if (singleton == null) {
            singleton = new Model();
        }
        return singleton;
    }

    private void checkStatusOnResponse(Object object, JSONArray result, String tag, int httpStatus) throws JSONException {
        if (httpStatus == 200) {
            Log.i(tag, " - OK 200");
            onResponseOK(object, tag, result);
        } else if (httpStatus == 201) {
            Log.e(tag, " - CREATED 201");
            onResponseOK(object, tag, result);
        } else if (httpStatus == 400) {
            Log.e(tag, " - ERROR 400");
            onResponseError(object, tag, " bad request, please check all the fields");
        } else if (httpStatus == 403) {
            Log.e(tag, " - ERROR 403");
            onResponseError(object, tag, " forbidden access");
        } else if (httpStatus == 404) {
            Log.e(tag, " - ERROR 404");
            onResponseError(object, tag, " not found");
        } else if (httpStatus == 500){
            Log.e(tag, " - ERROR 500");
            onResponseError(object, tag, " server error");
        } else {
            Log.e(tag, " - ERROR "+httpStatus);
            onResponseError(object, tag, " unknown error");
        }
    }

    private void onResponseOK(Object object, String tag, JSONArray json) throws JSONException {
        Log.i(TAG, " - onResponseOK");
        switch (tag) {
            case "Get texts":
                appCommon.getPresenterText().getTextsResponse(object,json);
                break;
            case "Login user":
                appCommon.getPresenterUser().loginUserResponse(object,json.getJSONObject(0));
                break;
            case "Register user":
                appCommon.getPresenterUser().registerUserResponse(object,json.getJSONObject(0));
                break;
            case "Get user info":
                appCommon.getPresenterUser().getUserInfoResponse(object,json.getJSONObject(0));
                break;
            case "Get best score":
                appCommon.getPresenterGame().getBestScoreResponse(object,json.getJSONObject(0));
                break;
            default:
                break;
        }
    }

    private void onResponseError(Object object, String tag, String message) {
        Log.e(TAG, " - onResponseError");
        switch (tag) {
            case "Get texts":
                appCommon.getPresenterText().responseError(object,"Texts error:"+message);
                break;
            case "Login user":
                appCommon.getPresenterUser().responseError(object,"User error:"+message);
                break;
            case "Register user":
                appCommon.getPresenterUser().responseError(object,"User error:"+message);
                break;
            case "Get user info":
                appCommon.getPresenterUser().responseError(object,"User error:"+message);
                break;
            case "Get best score":
                appCommon.getPresenterUser().responseError(object,"Game error:"+message);
                break;
            default:
                break;
        }
    }

    public void volleyAsynctask(final Object object, final String tagRequest, int verb, String url,
                                String dialogMessage, boolean showDialog, String params) {
        if (showDialog) {
            if (object instanceof Activity) pDialog = new ProgressDialog((Activity)object);
            else pDialog = new ProgressDialog(((Fragment)object).getContext());
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(dialogMessage);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        final AppCommon appCommon   = AppCommon.getInstance();
        JsonArrayRequest request = new JsonArrayRequest(verb, url, params
                , new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray result) {
                Log.i(TAG + "_" + tagRequest, "OK");
                if (pDialog != null && pDialog.isShowing()) pDialog.dismiss();
                try {
                    checkStatusOnResponse(object, result, tagRequest, 200);
                } catch (JSONException e) {
                    Log.e(TAG,"JSON error");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG + "_" + tagRequest, "ERROR: " + error.getMessage() + "\n" + "CAUSE: " + error.getCause());
                NetworkResponse networkResponse = error.networkResponse;
                int httpStatus = 400;   //Default value
                if (networkResponse != null) httpStatus = networkResponse.statusCode;
                if (pDialog != null && pDialog.isShowing()) pDialog.dismiss();
                try {
                    checkStatusOnResponse(object, new JSONArray(), tagRequest, httpStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
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
                return new HashMap<>();
            }
        };

        Volley.getInstance(appCommon.getApplicationContext()).addToRequestQueue(request, tagRequest);
    }
}
