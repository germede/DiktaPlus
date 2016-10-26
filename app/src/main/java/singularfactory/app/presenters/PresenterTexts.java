package singularfactory.app.presenters;

import android.app.ProgressDialog;
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

import singularfactory.app.AppCommon;
import singularfactory.app.common.Volley;
import singularfactory.app.presenters.interfaces.IPresenterTexts;
import singularfactory.app.views.fragments.TextsFragment;

public class PresenterTexts implements IPresenterTexts {
    private static final String TAG = PresenterTexts.class.getSimpleName();
    private AppCommon appCommon     = AppCommon.getInstance();
    private ProgressDialog pDialog  = null;

    /*******************/
    /**** API CALLS ****/
    /*******************/
    @Override
    public void getTextsList(final Object object, final String tagRequest, int verb, String url, String dialogMessage){
        volleyAsynctask(object,tagRequest,verb,url,"Loading texts list...",true);
    }

    /*******************/
    /** API RESPONSES **/
    /*******************/
    @Override
    public void setTextsList(Object object, JSONArray texts) throws JSONException {
        TextsFragment textsFragment = (TextsFragment) object;
        textsFragment.setTextsList(texts);

    }

    /** Response error **/
    @Override
    public void responseError(Object object, String message) {
        TextsFragment textsFragment = (TextsFragment) object;
        textsFragment.showErrorToast(message);
    }

    /**********************/
    /** VOLLEY ASYNCTASK **/
    /**********************/
    private void volleyAsynctask(final Object object, final String tagRequest, int verb, String url, String dialogMessage, boolean showDialog, final String... params) {
        if (showDialog) {
            TextsFragment textsFragment = (TextsFragment) object;
            pDialog = new ProgressDialog(textsFragment.getContext());
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

                //Dismiss dialog
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();

                try {
                    appCommon.getModelTexts().onResponse(object, jsonArray, tagRequest, 200);
                } catch (JSONException e) {
                    e.printStackTrace();
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
                    appCommon.getModelTexts().onResponse(object, new JSONArray(), tagRequest, httpStatus);
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
                Map<String, String> parameters = new HashMap<>();
                return parameters;
            }
        };
        //Adding request to request queue
        Volley.getInstance(appCommon.getApplicationContext()).addToRequestQueue(request, tagRequest);
    }
}