package singularfactory.app.models;

import org.json.JSONArray;
import org.json.JSONException;

import singularfactory.app.AppCommon;
import singularfactory.app.common.Logs;
import singularfactory.app.models.interfaces.IModelTexts;

public class ModelTexts implements IModelTexts {

    private static final String TAG = ModelTexts.class.getSimpleName();

    private static ModelTexts singleton = null;

    public ModelTexts() {
        //constructor
    }

    public ModelTexts getInstance() {
        if (singleton == null) {
            singleton = new ModelTexts();
        }
        return singleton;
    }

    @Override
    public void onResponse(Object object, JSONArray result, String tag, int httpStatus) throws JSONException {
        if (httpStatus == 200) {
            Logs.SystemLog(tag + " - OK 200");
            onResponseOK(object, tag, result);
        } else if (httpStatus == 401) {
            Logs.SystemLog(tag + " - ERROR 401");
        } else if (httpStatus == 400) {
            Logs.SystemLog(tag + " - ERROR 400");
            onResponseError(object, tag, result.toString());
        } else {
            Logs.SystemLog(tag + " - ERROR 500");
        }
    }

    @Override
    public void onResponseError(Object object, String tag, String json) {
        Logs.SystemLog(TAG + " - onResponseError");
        switch (tag) {
            default:
                break;
        }
    }

    @Override
    public void onResponseOK(Object object, String tag, JSONArray json) throws JSONException {

        Logs.SystemLog(TAG + " - onResponseOK");

        switch (tag) {
            case "vehicleList":
                AppCommon.getInstance().getPresenterTexts().setVehicleNames(object,json);
                break;
            default:
                break;
        }
    }

}
