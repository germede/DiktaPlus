package singularfactory.app.presenters;


import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import singularfactory.app.AppCommon;
import singularfactory.app.common.Volley;
import singularfactory.app.presenters.interfaces.IPresenterMain;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public class PresenterMain implements IPresenterMain {

    private static final String TAG = PresenterMain.class.getSimpleName();
    private AppCommon appCommon     = AppCommon.getInstance();
    private ProgressDialog pDialog  = null;

    /*******************/
    /**** API CALLS ****/
    /*******************/


    /*******************/
    /** API RESPONSES **/
    /*******************/

    /** Response error **/
    @Override
    public void responseError(Object object, String message) {

    }

    /**********************/
    /** VOLLEY ASYNCTASK **/
    /**********************/
    private void volleyAsynctask(final Object object, final String tagRequest, int verb, String url, String dialogMessage, boolean showDialog, final String... params) {

        if (showDialog) {
            pDialog = new ProgressDialog(AppCommon.getInstance().getApplicationContext());
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(dialogMessage);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        final AppCommon appCommon   = AppCommon.getInstance();
        final StringRequest request = new StringRequest(verb, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String json) {
                Log.i(TAG + "_" + tagRequest, "OK");

                //Dismiss dialog
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();

                AppCommon.getInstance().getModel().getModelMain().onResponse(object, json, tagRequest, 200);
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

                AppCommon.getInstance().getModel().getModelMain().onResponse(object, result, tagRequest, httpStatus);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
//                headers.put("Content-Type", "application/json; charset=utf-8");
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