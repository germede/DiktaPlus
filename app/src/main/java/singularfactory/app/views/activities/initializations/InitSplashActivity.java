package singularfactory.app.views.activities.initializations;

import android.content.Context;
import android.view.View;

import singularfactory.app.AppCommon;
import singularfactory.app.R;


/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public class InitSplashActivity {

    private final static String TAG = InitSplashActivity.class.getSimpleName();

    private AppCommon appCommon;
    private Context context;
    private InitSplashActivityListener listener;

    public View view, tvLogo, pbLoading;


    public InitSplashActivity(Context context) {
        appCommon = AppCommon.getInstance();
        this.context = context;
    }

    public void initialize(View view) {
        this.view = view;
        tvLogo    = view.findViewById(R.id.tvLogo);
        pbLoading = view.findViewById(R.id.pbLoading);
    }

    public void initializeActions() {

    }

    public void initializeCustomFonts() {

    }

    public void showLoading(boolean enable) {
        if (!enable) {
            pbLoading.setVisibility(View.GONE);
            return;
        }

        pbLoading.setVisibility(View.VISIBLE);
    }

    /**
     * LISTENER
     */
    public interface InitSplashActivityListener {

    }

    public void setInitSplashActivityListener(InitSplashActivityListener listener) {
        this.listener = listener;
    }
}
