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

    /*******************/
    /**** API CALLS ****/
    /*******************/
    @Override
    public void getTextsList(final Object object, final String tagRequest, int verb, String url, String dialogMessage){
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true);
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
        textsFragment.onResponseError(message);
    }

}