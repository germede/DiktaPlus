package singularfactory.app.models.interfaces;
import org.json.JSONArray;
import org.json.JSONException;
import singularfactory.app.models.ModelTexts;
public interface IModelTexts {
    ModelTexts getInstance();
    void onResponse(Object object, JSONArray result, String tag, int httpStatus) throws JSONException;
    void onResponseError(Object object, String tag, String json);
    void onResponseOK(Object object, String tag, JSONArray json) throws JSONException;
}
