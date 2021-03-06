package net.gerardomedina.diktaplus.presenters;

import org.json.JSONException;
import org.json.JSONObject;

import net.gerardomedina.diktaplus.common.AppCommon;
import net.gerardomedina.diktaplus.views.fragments.BaseFragment;
import net.gerardomedina.diktaplus.views.fragments.GameFragment;
import net.gerardomedina.diktaplus.views.fragments.TextFragment;

public class PresenterGame {
    private static final String TAG = PresenterGame.class.getSimpleName();
    private AppCommon appCommon     = AppCommon.getInstance();

    /*******************/
    /**** API CALLS ****/
    /*******************/
    public void getBestScore(final Object object, final String tagRequest, int verb, String url, String dialogMessage) throws JSONException{
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,false,null);
    }

    public void postGame(final Object object, final String tagRequest, int verb, String url, String dialogMessage, int [] jsonParams) throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user",jsonParams[0]);
        jsonObject.put("text",jsonParams[1]);
        jsonObject.put("score",jsonParams[2]);
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,false,jsonObject.toString());
    }

    /*******************/
    /** API RESPONSES **/
    /*******************/
    public void getBestScoreResponse(Object object, JSONObject game) throws JSONException {
        TextFragment textFragment = (TextFragment) object;
        textFragment.setBestScore(game.getInt("score"));
    }

    public void postGameResponse(Object object, JSONObject game) throws JSONException {
        GameFragment gameFragment = (GameFragment) object;
        if (game.has("levelup")) gameFragment.showGameOverDialog(game.getInt("levelup"));
        else gameFragment.showGameOverDialog(0);
    }

    /** Response error **/
    public void responseError(Object object, String message) {
        if(!message.equals("")) {
            BaseFragment baseFragment = (BaseFragment) object;
            baseFragment.showDialog(message);
        }
    }


}