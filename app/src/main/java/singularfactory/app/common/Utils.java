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

    public int convertDpToPx(Context ctx, int dp) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int convertPxToDp(Context ctx, int px) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

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

    public void launchActivity(Class invokedActivity, Object object, Intent extras) {
        Intent i = new Intent(AppCommon.getInstance(), invokedActivity);
        if (extras != null) i.putExtras(extras);
        if (!object.getClass().equals(Activity.class)){
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        AppCommon.getInstance().startActivity(i);
//        ((Activity) invocador).overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }

    public void loadFragment(View container, FragmentManager fragmentManager, Fragment fragment, String tag,
                             Bundle arguments, int enterAnim, int exitAnim, boolean showAnimation,
                             boolean addBackStack, int type) {
        if (fragment == null) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (arguments != null && arguments.size() > 0) fragment.setArguments(arguments);
        if (showAnimation) ft.setCustomAnimations(enterAnim, exitAnim, enterAnim, exitAnim);
        if (type == 0) ft.add(container.getId(), fragment, tag);
        if (type == 1) ft.replace(container.getId(), fragment, tag);
        if (addBackStack) ft.addToBackStack(tag);
        ft.commit();
        fragmentManager.executePendingTransactions();
    }

    public void removeFragment(FragmentManager fragmentManager, Fragment fragment, int enterAnim, int exitAnim, boolean showAnimation) {
        if (fragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (showAnimation)  ft.setCustomAnimations(enterAnim, exitAnim, enterAnim, exitAnim);
            ft.remove(fragment).commit();
        }
    }
    public float roundFloatToXDecimal(float value, int numOfDecimal){
        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(numOfDecimal, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
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
