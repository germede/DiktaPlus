package singularfactory.app.models;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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

import singularfactory.app.R;
import singularfactory.app.common.AppCommon;
import singularfactory.app.views.activities.BaseActivity;

public class Model {

    private static final String TAG = Model.class.getSimpleName();
    private static Model singleton = null;
    private final AppCommon appCommon = AppCommon.getInstance();
    private ProgressDialog pDialog = null;

    public Model() {
    }

    public static Model getInstance() {
        if (singleton == null) singleton = new Model();
        return singleton;
    }

    private void checkStatusOnResponse(Object object, JSONArray result, String tag, int httpStatus) throws JSONException {
        if (httpStatus >= 200 && httpStatus <= 201) {
            Log.i(tag, " - OK: " + httpStatus);
            onResponseOK(object, tag, result);
        } else {
            Log.e(tag, " - ERROR: " + httpStatus);
            onResponseError(object, tag);
        }
    }

    private void onResponseOK(Object object, String tag, JSONArray json) throws JSONException {
        switch (tag) {
            case "Register user":
                appCommon.getPresenterUser().registerUserResponse(object, json.getJSONObject(0));
                break;
            case "Login user":
                appCommon.getPresenterUser().loginUserResponse(object, json.getJSONObject(0));
                break;
            case "Get user info":
                appCommon.getPresenterUser().getUserInfoResponse(object, json.getJSONObject(0));
                break;
            case "Put user":
                appCommon.getPresenterUser().putUserResponse(object, json.getJSONObject(0));
                break;
            case "Delete user":
                appCommon.getPresenterUser().deleteUserResponse(object, json.getJSONObject(0));
                break;
            case "Get ranking":
                appCommon.getPresenterUser().getRankingResponse(object, json);
                break;
            case "Get users by username":
                appCommon.getPresenterUser().getUsersByUsernameResponse(object, json);
                break;
            case "Get friends":
                appCommon.getPresenterUser().getFriendsResponse(object, json);
                break;
            case "Get friend info":
                appCommon.getPresenterUser().getFriendInfoResponse(object, json.getJSONObject(0));
                break;
            case "Make friends":
                appCommon.getPresenterUser().makeFriendsResponse(object, json);
                break;
            case "Delete friends":
                appCommon.getPresenterUser().deleteFriendsResponse(object, json);
                break;
            case "Get texts":
                appCommon.getPresenterText().getTextsResponse(object, json);
                break;
            case "Get text content":
                appCommon.getPresenterText().getTextContentResponse(object, json.getJSONObject(0));
                break;
            case "Post game":
                appCommon.getPresenterGame().postGameResponse(object, json.getJSONObject(0));
                break;
            case "Get best score":
                appCommon.getPresenterGame().getBestScoreResponse(object, json.getJSONObject(0));
                break;
            default:
                break;
        }
    }

    private void onResponseError(Object object, String tag) {
        switch (tag) {
            case "Register user":
                appCommon.getPresenterUser().responseError(object, appCommon.getString(R.string.error_server));
                break;
            case "Login user":
                appCommon.getPresenterUser().responseError(object, appCommon.getString(R.string.error_login));
                break;
            case "Get user info":
                appCommon.getPresenterUser().responseError(object, appCommon.getString(R.string.error_get_user_info));
                break;
            case "Put user":
                appCommon.getPresenterUser().responseError(object, appCommon.getString(R.string.error_put_user));
                break;
            case "Delete user":
                appCommon.getPresenterUser().responseError(object, appCommon.getString(R.string.error_deleting_user));
                break;
            case "Get ranking":
                appCommon.getPresenterUser().responseError(object, appCommon.getString(R.string.error_no_ranking));
                break;
            case "Get users by username":
                appCommon.getPresenterUser().responseError(object, appCommon.getString(R.string.error_no_user_found));
                break;
            case "Get friends":
                appCommon.getPresenterUser().responseError(object, appCommon.getString(R.string.error_no_friends));
                break;
            case "Get friend info":
                appCommon.getPresenterUser().responseError(object, appCommon.getString(R.string.error_info_user));
                break;
            case "Make friends":
                appCommon.getPresenterUser().responseError(object, appCommon.getString(R.string.error_friendship_not_created));
                break;
            case "Delete friends":
                appCommon.getPresenterUser().responseError(object, appCommon.getString(R.string.error_friendship_not_deleted));
                break;
            case "Get texts":
                appCommon.getPresenterText().responseError(object, appCommon.getString(R.string.error_getting_texts));
                break;
            case "Get text content":
                appCommon.getPresenterText().responseError(object, appCommon.getString(R.string.error_text_content));
                break;
            case "Post game":
                appCommon.getPresenterGame().responseError(object, appCommon.getString(R.string.error_posting_game));
                break;
            case "Get best score":
                appCommon.getPresenterGame().responseError(object, appCommon.getString(R.string.error_best_score));
                break;
            default:
                break;
        }
    }

    public void volleyAsynctask(final Object object, final String tagRequest, int verb, String url,
                                String dialogMessage, boolean showDialog, String params) {
        final Context context;
        if (object instanceof BaseActivity) context = (BaseActivity)object;
        else context = ((Fragment) object).getActivity();
        if (showDialog) {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage(dialogMessage);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        final AppCommon appCommon = AppCommon.getInstance();
        JsonArrayRequest request = new JsonArrayRequest(verb, url, params
                , new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray result) {
                if (pDialog != null && pDialog.isShowing()) pDialog.dismiss();
                try {
                    checkStatusOnResponse(object, result, tagRequest, 200);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON error");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                int httpStatus = 500;
                if (networkResponse != null) httpStatus = networkResponse.statusCode;
                if (pDialog != null && pDialog.isShowing()) pDialog.dismiss();
                try {
                    checkStatusOnResponse(object, new JSONArray(), tagRequest, httpStatus);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON error");
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer " + appCommon.getUtils().sharedGetValue(context, "access_token", 1));
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
