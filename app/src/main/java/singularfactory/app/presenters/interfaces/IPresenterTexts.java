package singularfactory.app.presenters.interfaces;


public interface IPresenterTexts {

    /** API CALLS **/
    void getGlobalConfig(Object object);

    /** API RESPONSES **/
    void responseError(Object object, String message);
    void responseGetGlobalConfig(Object object, String apiVersion, String mediaUrl);
}
