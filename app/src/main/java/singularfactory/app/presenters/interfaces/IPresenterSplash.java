package singularfactory.app.presenters.interfaces;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public interface IPresenterSplash {

    /** API CALLS **/
    void getGlobalConfig(Object object);

    /** API RESPONSES **/
    void responseError(Object object, String message);
    void responseGetGlobalConfig(Object object, String apiVersion, String mediaUrl);
}
