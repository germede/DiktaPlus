package singularfactory.app.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import singularfactory.app.R;

public class Utils {
    private HashMap<String, Typeface> cachedFonts = new HashMap<String, Typeface>();
    private static Utils singleton;
    public static Utils getInstance() {
        if (singleton == null) {
            singleton = new Utils();
        }
        return singleton;
    }

    /********************/
    /** PUBLIC METHODS **/
    /********************/

    public String formatDateFromServer(String serverDateToFormat){
        SimpleDateFormat curFormatter  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat postFormatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        Date dateObj = null;
        String date  = "";
        if (serverDateToFormat == null || serverDateToFormat.equals("")) return date;
        try {
            dateObj = curFormatter.parse(serverDateToFormat);
            if (dateObj != null) date = postFormatter.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
    public String formatDateFromView(String viewDateToFormat){
        SimpleDateFormat curFormatter  = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        SimpleDateFormat postFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date dateObj = null;
        String date  = "";
        try {
            TimeZone tz = TimeZone.getDefault();
            curFormatter.setTimeZone(tz);
            dateObj = curFormatter.parse(viewDateToFormat);
            if (dateObj != null) date = postFormatter.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        return dateFormat.format(calendar.getTime());
    }

    public Typeface getFont(Context ctx, String fontName) {
        Typeface outTypeFace = null;
        if (cachedFonts.containsKey(fontName)) outTypeFace = cachedFonts.get(fontName);
        else {
            outTypeFace = Typeface.createFromAsset(ctx.getAssets(), "fonts/" + fontName);
            if (outTypeFace != null) cachedFonts.put(fontName, outTypeFace);
        }
        return outTypeFace;
    }

    public boolean hasInternet(Context ctx) {
        ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null) return false;
        if (!i.isConnected()) return false;
        if (!i.isAvailable()) return false;
        return true;
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null && imm.isActive()) imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) return false;
        else return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
