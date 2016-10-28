package singularfactory.app.presenters;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import singularfactory.app.common.AppCommon;
import singularfactory.app.views.fragments.BaseFragment;
import singularfactory.app.views.fragments.LoginFragment;
import singularfactory.app.views.fragments.SignupFragment;

import static android.R.id.message;

public class PresenterUser {
    private static final String TAG = PresenterUser.class.getSimpleName();
    private AppCommon appCommon     = AppCommon.getInstance();

    /*******************/
    /**** API CALLS ****/
    /*******************/
    public void loginUser(final Object object, final String tagRequest, int verb, String url, String dialogMessage, String [] jsonParams) throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email",jsonParams[0]);
        jsonObject.put("username",jsonParams[0]);
        jsonObject.put("password",jsonParams[1]);
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true,jsonObject.toString());
    }

    public void registerUser(final Object object, final String tagRequest, int verb, String url, String dialogMessage, String [] jsonParams) throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",jsonParams[0]);
        jsonObject.put("email",jsonParams[1]);
        jsonObject.put("password",jsonParams[2]);
        jsonObject.put("country",jsonParams[3]);
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true,jsonObject.toString());
    }

    /*******************/
    /** API RESPONSES **/
    /*******************/
    public void loginUserResponse(Object object, JSONObject user) throws JSONException {
        LoginFragment loginFragment = (LoginFragment) object;
        loginFragment.setUser(user);

    }

    public void registerUserResponse(Object object, JSONObject json) {
        SignupFragment signupFragment = (SignupFragment) object;
        signupFragment.onSuccessfullyRegistration("Successfully signed up, please try to login");
    }

    /** Response error **/
    public void responseError(Object object, String message) {
        BaseFragment baseFragment = (BaseFragment) object;
        baseFragment.showErrorToast(message);
    }


}