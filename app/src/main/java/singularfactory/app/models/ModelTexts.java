package singularfactory.app.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import singularfactory.app.AppCommon;
import singularfactory.app.common.Logs;
import singularfactory.app.models.interfaces.IModelTexts;

public class ModelTexts implements IModelTexts {
    private static final String TAG = ModelTexts.class.getSimpleName();
    private static ModelTexts singleton = null;
    private final AppCommon appCommon   = AppCommon.getInstance();

    public ModelTexts() {
        //constructor
    }

    public ModelTexts getInstance() {
        if (singleton == null) singleton = new ModelTexts();
        return singleton;
    }

    @Override
    public void onResponse(Object object, JSONArray result, String tag, int httpStatus) throws JSONException {
        if (httpStatus == 200) {
            Log.i(tag, " - OK 200");
            onResponseOK(object, tag, result);
        } else if (httpStatus == 401) {
            Log.e(tag, " - ERROR 401");
        } else if (httpStatus == 400) {
            Log.e(tag, " - ERROR 400");
            onResponseError(object, tag, result.toString());
        } else {
            Log.e(tag, " - ERROR 500");
        }
    }

    @Override
    public void onResponseError(Object object, String tag, String json) {
        Log.e(TAG, " - onResponseError");
        switch (tag) {
            case "GET Texts":
                appCommon.getPresenterTexts().responseError(object,tag);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseOK(Object object, String tag, JSONArray json) throws JSONException {
        Log.i(TAG, " - onResponseOK");
        switch (tag) {
            case "GET Texts":
                Log.e(TAG, " - onResponseOKasdf");
                appCommon.getPresenterTexts().setTextsList(object,json);
                break;
            default:
                break;
        }
    }
}
