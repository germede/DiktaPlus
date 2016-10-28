package singularfactory.app.presenters;

import org.json.JSONArray;
import org.json.JSONException;

import singularfactory.app.common.AppCommon;
import singularfactory.app.views.fragments.TextsFragment;

public class PresenterTexts {
    private static final String TAG = PresenterTexts.class.getSimpleName();
    private AppCommon appCommon     = AppCommon.getInstance();

    /*******************/
    /**** API CALLS ****/
    /*******************/
    public void getTextsList(final Object object, final String tagRequest, int verb, String url, String dialogMessage){
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true);
    }

    /*******************/
    /** API RESPONSES **/
    /*******************/
    public void setTextsList(Object object, JSONArray texts) throws JSONException {
        TextsFragment textsFragment = (TextsFragment) object;
        textsFragment.setTextsList(texts);

    }

    /** Response error **/
    public void responseError(Object object, String message) {
        TextsFragment textsFragment = (TextsFragment) object;
        textsFragment.onResponseError(message);
    }

}