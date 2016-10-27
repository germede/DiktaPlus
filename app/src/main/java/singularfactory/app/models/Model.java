package singularfactory.app.models;


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
import singularfactory.app.models.interfaces.IModel;
import singularfactory.app.views.fragments.TextsFragment;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public class Model implements IModel {

    private static final String TAG = Model.class.getSimpleName();

    private static Model singleton = null;

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

    @Override
    public void volleyAsynctask(final Object object, final String tagRequest, int verb, String url, String dialogMessage, boolean showDialog, final String... params) {
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
                return new HashMap<>();
            }
        };
        //Adding request to request queue
        Volley.getInstance(appCommon.getApplicationContext()).addToRequestQueue(request, tagRequest);
    }
}
