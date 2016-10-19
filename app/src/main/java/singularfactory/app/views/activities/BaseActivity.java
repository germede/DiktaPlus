package singularfactory.app.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import singularfactory.app.AppCommon;
import singularfactory.app.R;
import singularfactory.app.common.Volley;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private AppCommon appCommon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
    }

    private void initialize() {
        appCommon = (AppCommon) getApplication();
    }

    /********************/
    /** PUBLIC METHODS **/
    /********************/

    /**
     * Load image with Volley
     * @param context
     * @param maxWidth
     * @param maxHeight
     * @param imageUrl
     * @param progressBar
     * @param image
     */
    public static ImageLoader loadImageWithVolley(final Context context, String imageUrl, final ProgressBar progressBar, final ImageView image, int maxWidth, int maxHeight) {

        ImageLoader imageLoader  = Volley.getInstance(context).getImageLoader();

        if (imageUrl != null && progressBar != null && image != null) {

            imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    progressBar.setVisibility(View.GONE);   // hide the spinner here
                    image.setVisibility(View.VISIBLE);      // set the image here
                    image.setImageBitmap(response.getBitmap());

                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);   // hide the spinner here
                    image.setVisibility(View.VISIBLE);      // set the image here
//                    image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_child));
                }
            }, maxWidth, maxHeight);
        }

        return imageLoader;
    }

    /**
     * Show alert dialog with informative message.
     * @param context
     * @param message
     */
    public static void showSingleAlert(final Context context, String message) {
        new android.support.v7.app.AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    /**
     * Show alert dialog with informative message and execute process passed by parameter.
     * @param object
     * @param text
     * @param processName
     */
    public static void showSingleAlertWithReflection(final Activity activity, final Object object, String text, final String processName) {

        if(object != null) {

            new android.support.v7.app.AlertDialog.Builder(activity, R.style.MyAlertDialogStyle)
                    //.setTitle(text)
                    .setMessage(text)
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

    /**
     * Show alert dialog with informative message and execute processes passed by parameters.
     * @param activity
     * @param currentClass
     * @param text
     * @param positiveButtonName
     * @param negativeButtonName
     * @param processName
     */
    public static void showAlertWithReflectionTwoButtons(final Activity activity, final Object currentClass, String text, String positiveButtonName, String negativeButtonName, final String processName) {
        if(activity != null){
            new android.support.v7.app.AlertDialog.Builder(activity, R.style.MyAlertDialogStyle)
                    //.setTitle(text)
                    .setMessage(text)
                    .setNegativeButton(negativeButtonName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                            return;
                        }
                    })
                    .setPositiveButton(positiveButtonName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (processName == null) {
                                activity.finish();
                                return;
                            }

                            try {
                                Method method = currentClass.getClass().getMethod(processName);
                                method.invoke(currentClass);

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

    /**
     * Show snackBar on the bottom of screen
     * @param view
     * @param contentText
     * @param actionText
     * @param onClickListener
     */
    public static void showSnackBar(View view, String contentText, String actionText, View.OnClickListener onClickListener) {
        Snackbar.make(view, contentText, Snackbar.LENGTH_LONG)
                .setAction(actionText, onClickListener).show();
    }
}
