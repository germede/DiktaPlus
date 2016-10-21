package singularfactory.app.presenters.interfaces;


import org.json.JSONArray;

public interface IPresenterTexts {

    void getTextsList(Object object, String tagRequest, int verb, String url, String dialogMessage);

    /** API CALLS **/

    void setTextsList(Object object, JSONArray vehicles);

    /** API RESPONSES **/
    void responseError(Object object, String message);
}
