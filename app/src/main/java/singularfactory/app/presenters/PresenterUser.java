package singularfactory.app.presenters;

import org.json.JSONArray;
import org.json.JSONException;

import singularfactory.app.common.AppCommon;
import singularfactory.app.views.fragments.LoginFragment;

public class PresenterUser {
    private static final String TAG = PresenterUser.class.getSimpleName();
    private AppCommon appCommon     = AppCommon.getInstance();

    /*******************/
    /**** API CALLS ****/
    /*******************/
    public void loginUser(final Object object, final String tagRequest, int verb, String url, String dialogMessage, String [] params){
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true, params);
    }

    /*******************/
    /** API RESPONSES **/
    /*******************/
    public void loginUserResponse(Object object, JSONArray user) throws JSONException {
        LoginFragment loginFragment = (LoginFragment) object;
        loginFragment.setUser(user);

    }

    /** Response error **/
    public void responseError(Object object, String message) {
        LoginFragment loginFragment = (LoginFragment) object;
        loginFragment.showErrorToast(message);
    }

}