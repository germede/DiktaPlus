package singularfactory.app.presenters;

import android.support.v4.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import singularfactory.app.common.AppCommon;
import singularfactory.app.views.activities.SplashActivity;
import singularfactory.app.views.fragments.BaseFragment;
import singularfactory.app.views.fragments.FriendFragment;
import singularfactory.app.views.fragments.LoginFragment;
import singularfactory.app.views.fragments.RankingFragment;
import singularfactory.app.views.fragments.SettingsFragment;
import singularfactory.app.views.fragments.SignupFragment;

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

    public void getUserInfo(final Object object, final String tagRequest, int verb, String url, String dialogMessage) {
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true,null);
    }

    public void deleteUser(final Object object, final String tagRequest, int verb, String url, String dialogMessage) {
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true,null);
    }

    public void getRanking(final Object object, final String tagRequest, int verb, String url, String dialogMessage) {
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true,null);
    }

    public void getUsersByUsername(final Object object, final String tagRequest, int verb, String url, String dialogMessage) {
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true,null);
    }

    public void getFriends(final Object object, final String tagRequest, int verb, String url, String dialogMessage) {
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true,null);
    }

    public void getFriendInfo(final Object object, final String tagRequest, int verb, String url, String dialogMessage) {
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true,null);
    }

    public void makeFriends(final Object object, final String tagRequest, int verb, String url, String dialogMessage) {
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true,null);
    }

    public void deleteFriends(final Object object, final String tagRequest, int verb, String url, String dialogMessage) {
        appCommon.getModel().volleyAsynctask(object,tagRequest,verb,url,dialogMessage,true,null);
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

    public void getUserInfoResponse(Object object, JSONObject user) {
        SplashActivity splashActivity = (SplashActivity) object;
        splashActivity.setUserInfo(user);
    }

    public void deleteUserResponse(Object object, JSONObject user) {
        SettingsFragment settingsFragment = (SettingsFragment) object;
        settingsFragment.deleteAccountSuccess();
    }

    public void getRankingResponse(Object object, JSONArray users) throws JSONException {
        RankingFragment rankingFragment = (RankingFragment) object;
        rankingFragment.setRanking(users);
    }

    public void getUsersByUsernameResponse(Object object, JSONArray users) throws JSONException {
        FriendFragment friendFragment = (FriendFragment) object;
        friendFragment.setUsersByUsername(users);
    }

    public void getFriendsResponse(Object object, JSONArray users) throws JSONException {
        FriendFragment friendFragment = (FriendFragment) object;
        friendFragment.setFriends(users);
    }

    public void getFriendInfoResponse(Object object, JSONObject user) {
        FriendFragment friendFragment = (FriendFragment) object;
        friendFragment.setFriendInfo(user);
    }

    public void makeFriendsResponse(Object object, JSONArray users) throws JSONException {
        FriendFragment friendFragment = (FriendFragment) object;
        friendFragment.showToast("Friendship successfully created");
        friendFragment.getFriends();
    }

    public void deleteFriendsResponse(Object object, JSONArray users) throws JSONException {
        FriendFragment friendFragment = (FriendFragment) object;
        friendFragment.showToast("Friendship successfully deleted");
        friendFragment.getFriends();
    }

    /** Response error **/
    public void responseError(Object object, String message) {
        if (!message.equals("")) {
            if (object instanceof Fragment) {
                BaseFragment baseFragment = (BaseFragment) object;
                baseFragment.showDialog(message);
            } else if (object instanceof SettingsFragment) {
                ((SettingsFragment)object).showToast(message);
            } else if (object instanceof SplashActivity){
                SplashActivity splashActivity = (SplashActivity) object;
                splashActivity.showSingleAlertWithReflection(splashActivity,splashActivity,"Your credentials are not valid anymore, please log in again","launchLoginActivity");
            }
        }
    }
}