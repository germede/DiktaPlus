package net.gerardomedina.diktaplus.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import net.gerardomedina.diktaplus.R;

public class Utils {
    private static Utils singleton;
    public static Utils getInstance() {
        if (singleton == null) {
            singleton = new Utils();
        }
        return singleton;
    }
    public boolean hasInternet(Context ctx) {
        ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        return i != null && i.isConnected() && i.isAvailable();
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null && imm.isActive()) imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public boolean isValidEmail(CharSequence target) {
        return target != null && !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public Object sharedGetValue(Context context, String valueName, int objectType) {
        Object object = null;
        SharedPreferences confShared = context.getSharedPreferences(AppCommon.getInstance().getString(R.string.app_name), Context.MODE_PRIVATE);
        if (objectType == 0) object = confShared.getBoolean(valueName, false);
        if (objectType == 1) object = confShared.getString(valueName, "");
        if (objectType == 2) object = confShared.getInt(valueName, 0);
        return object;
    }

    public void sharedSetValue(Context context, String sharedName, Object value) {
        SharedPreferences confShared 	   	= context.getSharedPreferences(AppCommon.getInstance().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editShared = confShared.edit();
        if (value instanceof Boolean) editShared.putBoolean(sharedName, (Boolean) value);
        if (value instanceof Integer) editShared.putInt(sharedName, (Integer) value);
        if (value instanceof String) editShared.putString(sharedName, (String) value);
        editShared.apply();
    }

    public void sharedRemoveValue(Context context, String sharedName) {
        SharedPreferences confShared 	   	= context.getSharedPreferences(AppCommon.getInstance().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editShared = confShared.edit();
        editShared.remove(sharedName);
        editShared.apply();
    }
}
