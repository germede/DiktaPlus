package singularfactory.app.models;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import singularfactory.app.AppCommon;
import singularfactory.app.BuildConfig;
import singularfactory.app.R;
import singularfactory.app.Tags;
import singularfactory.app.common.Logs;
import singularfactory.app.models.interfaces.IModelSplash;
import singularfactory.app.views.activities.SplashActivity;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public class ModelSplash implements IModelSplash {

    private static final String TAG = ModelSplash.class.getSimpleName();

    private static ModelSplash singleton = null;

    public ModelSplash() {
        //constructor
    }

    public static ModelSplash getInstance() {
        if (singleton == null) {
            singleton = new ModelSplash();
        }
        return singleton;
    }

    @Override
    public void onResponse(Object object, String result, String tag, int httpStatus) {

        if (httpStatus == 200) {
            Logs.SystemLog(tag + " - OK 200");
            onResponseOK(object, tag, result);

        } else if (httpStatus == 401) {
            Logs.SystemLog(tag + " - ERROR 401");

            //TODO Refresh token

        } else if (httpStatus == 400) {
            Logs.SystemLog(tag + " - ERROR 400");
            onResponseError(object, tag, result);

        } else {
            Logs.SystemLog(tag + " - ERROR 500");
//            BaseActivity.showSingleAlert(((Fragment) object).getActivity(), AppCommon.getInstance().getString(R.string.message_server_error));
        }
    }

    @Override
    public void onResponseError(Object object, String tag, String json) {

        Logs.SystemLog(TAG + " - onResponseError");

        switch (tag) {
            case Tags.WS_GET_GLOBAL_CONFIG:

                if (object instanceof SplashActivity) {
                    ErrorData errorData;

                    //Parse data into model
                    Gson gson = new Gson();
                    Type type = new TypeToken<ErrorData>(){}.getType();
                    errorData = gson.fromJson(json, type);
                    String errorMsg = (errorData != null) ? errorData.getMessage() : AppCommon.getInstance().getString(R.string.text_error_unknown);
                    AppCommon.getInstance().getPresenterSplash().responseError(object, errorMsg);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onResponseOK(Object object, String tag, String json) {

        Logs.SystemLog(TAG + " - onResponseOK");

        switch (tag) {
            case Tags.WS_GET_GLOBAL_CONFIG:

                if (object instanceof SplashActivity) {
                    GlobalConfigData globalConfigData;

                    //Parse data into model
                    Gson gson = new Gson();
                    Type type = new TypeToken<GlobalConfigData>(){}.getType();
                    globalConfigData = gson.fromJson(json, type);
                    AppCommon.getInstance().getPresenterSplash().responseGetGlobalConfig(object, globalConfigData.getApiVersion(), globalConfigData.getMediaUrl());
                }
                break;

            default:
                break;
        }
    }
}
