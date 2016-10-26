package singularfactory.app.presenters.interfaces;


import org.json.JSONArray;
import org.json.JSONException;

public interface IPresenterTexts {

    void getTextsList(Object object, String tagRequest, int verb, String url, String dialogMessage);

    /** API CALLS **/

    void setTextsList(Object object, JSONArray vehicles) throws JSONException;

    /** API RESPONSES **/
    void responseError(Object object, String message);
}
