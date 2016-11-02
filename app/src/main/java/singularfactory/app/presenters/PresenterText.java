package singularfactory.app.presenters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import singularfactory.app.common.AppCommon;
import singularfactory.app.views.fragments.TextFragment;

public class PresenterText {
    private static final String TAG = PresenterText.class.getSimpleName();
    private AppCommon appCommon     = AppCommon.getInstance();

    /*******************/
    /**** API CALLS ****/
    /*******************/
    public void getTexts(final Object object, final String tagRequest, int verb, String url, String dialogMessage){
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,false,null);
    }

    public void getTextContent(final Object object, final String tagRequest, int verb, String url, String dialogMessage){
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,false,null);
    }

    /*******************/
    /** API RESPONSES **/
    /*******************/
    public void getTextsResponse(Object object, JSONArray texts) throws JSONException {
        TextFragment textFragment = (TextFragment) object;
        textFragment.setTextsList(texts);
    }

    public void getTextContentResponse(Object object, JSONObject text) throws JSONException {
        TextFragment textFragment = (TextFragment) object;
        textFragment.setTextContent(text);
    }

    /*******************/
    /** API RESPONSES **/
    /*******************/
    public void responseError(Object object) {
        TextFragment textFragment = (TextFragment) object;
        textFragment.onErrorGetTexts();
    }

}