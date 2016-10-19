package singularfactory.app;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public class Tags {

    /** SharedPreferences **/
    public final static String SHARED_API_VERSION   = AppCommon.getInstance().getString(R.string.app_name) + "_API_VERSION";
    public final static String SHARED_ACCESS_TOKEN  = AppCommon.getInstance().getString(R.string.app_name) + "_ACCESS_TOKEN";
    public final static String SHARED_MEDIA_URL     = AppCommon.getInstance().getString(R.string.app_name) + "_MEDIA_URL";
    public final static String SHARED_REFRESH_TOKEN = AppCommon.getInstance().getString(R.string.app_name) + "_REFRESH_TOKEN";
    public final static String SHARED_EMAIL         = AppCommon.getInstance().getString(R.string.app_name) + "_EMAIL";

    /** WebService **/
    public final static String WS_GET_GLOBAL_CONFIG = "WS_GET_GLOBAL_CONFIG";
}
