package singularfactory.app.models.interfaces;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public interface IModelMain {

    void onResponse(Object object, String result, String tag, int httpStatus);
    void onResponseError(Object object, String tag, String json);
    void onResponseOK(Object object, String tag, String json);
}
