package net.gerardomedina.diktaplus.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.gerardomedina.diktaplus.R;
import net.gerardomedina.diktaplus.common.AppCommon;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    protected AppCommon appCommon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
    }

    private void initialize() {
        appCommon = (AppCommon) getApplication();
    }
    public void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
    public static void showSingleAlert(final Context context, String message) {
        new android.support.v7.app.AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
    public static void showSingleAlertWithReflection(final Activity activity, final Object object, String text, final String processName) {

        if(object != null) {

            new android.support.v7.app.AlertDialog.Builder(activity, R.style.MyAlertDialogStyle)
                    .setMessage(text)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (processName == null) {
                                activity.finish();
                                return;
                            }
                            try {
                                Method method = object.getClass().getMethod(processName);
                                method.invoke(object);
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .create()
                    .show();
        }

    }
}
