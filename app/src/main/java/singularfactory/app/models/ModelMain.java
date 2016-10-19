package singularfactory.app.models;


import singularfactory.app.BuildConfig;
import singularfactory.app.common.Logs;
import singularfactory.app.models.interfaces.IModelMain;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public class ModelMain implements IModelMain {

    private static final String TAG = ModelMain.class.getSimpleName();

    private static ModelMain singleton = null;

    public ModelMain() {
        //constructor
    }

    public static ModelMain getInstance() {
        if (singleton == null) {
            singleton = new ModelMain();
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

//            case Tags. :
//                if (object instanceof SplashActivity) {
//                    ErrorData errorData;
//
//                    //Parse data into model
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<ErrorData>(){}.getType();
//                    errorData = gson.fromJson(json, type);
//                    AppCommon.getInstance().getPresenterMain().responseError(object, errorData.getMessage());
//                }
//                break;

            default:
                break;
        }
    }

    @Override
    public void onResponseOK(Object object, String tag, String json) {

        Logs.SystemLog(TAG + " - onResponseOK");

        switch (tag) {

            default:
                break;
        }
    }
}
