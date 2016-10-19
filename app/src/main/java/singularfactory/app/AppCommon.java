package singularfactory.app;

import android.support.multidex.MultiDexApplication;

import singularfactory.app.common.Utils;
import singularfactory.app.models.interfaces.IModel;
import singularfactory.app.presenters.PresenterMain;
import singularfactory.app.presenters.PresenterSplash;
import singularfactory.app.presenters.interfaces.IPresenterMain;
import singularfactory.app.presenters.interfaces.IPresenterSplash;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public class AppCommon extends MultiDexApplication {

    //Singleton
    private static AppCommon singleton;

    //Fonts name
//    public final static String FONT_GOCHI_HAND_BOLD = "GochiHand-Regular.ttf";
//    public final static String FONT_HANDLEE_REGULAR = "Handlee-Regular.ttf";

    //Fragment animations
    public final static int NO_ANIMATION     = -1;
    public final static int SLIDE_IN_TOP     = R.anim.slide_in_top;
    public final static int SLIDE_IN_BOTTOM  = R.anim.slide_in_bottom;
    public final static int SLIDE_IN_LEFT    = R.anim.slide_in_left;
    public final static int SLIDE_IN_RIGHT   = R.anim.slide_in_right;
    public final static int SLIDE_OUT_TOP    = R.anim.slide_out_top;
    public final static int SLIDE_OUT_BOTTOM = R.anim.slide_out_bottom;
    public final static int SLIDE_OUT_LEFT   = R.anim.slide_out_left;
    public final static int SLIDE_OUT_RIGHT  = R.anim.slide_out_right;


    public static AppCommon getInstance() {

        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        presenterMain   = null;
        presenterSplash = null;
        singleton       = this;
    }

    /************/
    /** MODELS **/
    /************/

    private IModel model;

    public IModel getModel(){
        return model;
    }

    public void setModel(IModel model){
        this.model = model;
    }

    /****************/
    /** PRESENTERS **/
    /****************/

    private IPresenterMain presenterMain;
    private IPresenterSplash presenterSplash;

    //PresenterMain
    public IPresenterMain getPresenterMain() {
        if (presenterMain == null)
            presenterMain = new PresenterMain();

        return presenterMain;
    }

    public void setPresenterMain(IPresenterMain presenterMain) {
        this.presenterMain = presenterMain;
    }

    public void removePresenterMain() {presenterMain = null;}

    //PresenterSplash
    public IPresenterSplash getPresenterSplash() {
        if (presenterSplash == null)
            presenterSplash = new PresenterSplash();

        return presenterSplash;
    }

    public void setPresenterSplash(IPresenterSplash presenterSplash) {
        this.presenterSplash = presenterSplash;
    }

    public void removePresenterSplash() {
        presenterSplash = null;
    }

    /***************/
    /** UTILITIES **/
    /***************/

    public Utils getUtils() {
        return Utils.getInstance();
    }

    /********************/
    /** PUBLIC METHODS **/
    /********************/

}
