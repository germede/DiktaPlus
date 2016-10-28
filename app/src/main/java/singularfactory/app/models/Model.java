package singularfactory.app.models;


import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            onResponseError(object, tag, "Bad request, please check all the fields");
        } else if (httpStatus == 403) {
            Log.e(tag, " - ERROR 403");
            onResponseError(object, tag, "Forbidden access");
        } else if (httpStatus == 404) {
            Log.e(tag, " - ERROR 404");
            onResponseError(object, tag, "Not found");
        } else if (httpStatus == 500){
            Log.e(tag, " - ERROR 500");
            onResponseError(object, tag, "Server error");
        } else {
            Log.e(tag, " - ERROR "+httpStatus);
            onResponseError(object, tag, "UNKNOWN ERROR");
        }
    }

    private void onResponseOK(Object object, String tag, JSONArray json) throws JSONException {
        Log.i(TAG, " - onResponseOK");
        switch (tag) {
            case "Get texts":
                appCommon.getPresenterTexts().getTextsResponse(object,json);
                break;
            case "Login user":
                appCommon.getPresenterUser().loginUserResponse(object,json.getJSONObject(0));
                break;
            case "Register user":
                appCommon.getPresenterUser().registerUserResponse(object,json.getJSONObject(0));
                break;
            default:
                break;
        }
    }

    private void onResponseError(Object object, String tag, String message) {
        Log.e(TAG, " - onResponseError");
        switch (tag) {
            case "Get texts":
                appCommon.getPresenterTexts().responseError(object,message);
                break;
            case "Login user":
                appCommon.getPresenterUser().responseError(object,message);
                break;
            case "Register user":
                appCommon.getPresenterUser().responseError(object,message);
                break;
            default:
                break;
        }
    }

    public void volleyAsynctask(final Object object, final String tagRequest, int verb, String url,
                                String dialogMessage, boolean showDialog, String params) {
        if (showDialog) {
            Fragment fragment = (Fragment) object;
            pDialog = new ProgressDialog(fragment.getActivity());
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setMessage(dialogMessage);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(false);
            //pDialog.show();
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
                String body, result = "";
                int httpStatus = 400;   //Default value

                try {
                    if (networkResponse != null)
                        httpStatus = networkResponse.statusCode;

                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        body   = new String(error.networkResponse.data, "UTF-8");
                        result = (!body.equals("")) ? body : "";
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //Dismiss dialog
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();

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
