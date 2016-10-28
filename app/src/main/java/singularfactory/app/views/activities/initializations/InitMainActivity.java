package singularfactory.app.views.activities.initializations;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import singularfactory.app.common.AppCommon;
import singularfactory.app.R;

/**
 * Created by Óscar Adae Rodríguez on 09/05/2016.
 */
public class InitMainActivity {
    private final static String TAG = InitMainActivity.class.getSimpleName();

    private AppCommon appCommon;
    private Context context;
    private InitMainActivityListener listener;

    public Toolbar toolbar;
    public FloatingActionButton fab;
    public DrawerLayout drawer;
    public NavigationView navigationView;
    public View view, coordinatorLayout;


    public InitMainActivity(Context context) {
        appCommon = AppCommon.getInstance();
        this.context = context;
    }

    public void initialize(View view) {
        this.view = view;
        toolbar           = (Toolbar)              view.findViewById(R.id.toolbar);
        drawer            = (DrawerLayout)         view.findViewById(R.id.drawer_layout);
        navigationView    = (NavigationView)       view.findViewById(R.id.nav_view);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
    }

    public void initializeActions() {

    }

    public void initializeCustomFonts() {

    }

    /**
     * LISTENER
     */
    public interface InitMainActivityListener {
    }

    public void setInitMainActivityListener(InitMainActivityListener listener) {
        this.listener = listener;
    }
}
