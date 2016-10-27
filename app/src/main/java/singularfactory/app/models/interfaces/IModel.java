package singularfactory.app.models.interfaces;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public interface IModel {

    void volleyAsynctask(Object object, String tagRequest, int verb, String url, String dialogMessage, boolean showDialog, String... params);
}
