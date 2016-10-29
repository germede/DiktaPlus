package singularfactory.app.presenters;

import org.json.JSONException;
import org.json.JSONObject;

import singularfactory.app.common.AppCommon;
import singularfactory.app.views.fragments.BaseFragment;
import singularfactory.app.views.fragments.LoginFragment;
import singularfactory.app.views.fragments.SignupFragment;
import singularfactory.app.views.fragments.TextFragment;

public class PresenterGame {
    private static final String TAG = PresenterGame.class.getSimpleName();
    private AppCommon appCommon     = AppCommon.getInstance();

    /*******************/
    /**** API CALLS ****/
    /*******************/
    public void getBestScore(final Object object, final String tagRequest, int verb, String url, String dialogMessage) throws JSONException{
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,false,null);
    }



    /*******************/
    /** API RESPONSES **/
    /*******************/
    public void getBestScoreResponse(Object object, JSONObject game) throws JSONException {
        TextFragment textFragment = (TextFragment) object;
        textFragment.setBestScore(game.getInt("score"));
    }

    /** Response error **/
    public void responseError(Object object, String message) {
        BaseFragment baseFragment = (BaseFragment) object;
    }


}