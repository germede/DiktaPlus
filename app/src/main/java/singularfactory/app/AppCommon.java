package singularfactory.app;

import android.graphics.Typeface;
import android.support.multidex.MultiDexApplication;

import singularfactory.app.common.Utils;
import singularfactory.app.models.Model;
import singularfactory.app.models.ModelTexts;
import singularfactory.app.models.interfaces.IModel;
import singularfactory.app.models.interfaces.IModelTexts;
import singularfactory.app.presenters.PresenterSplash;
import singularfactory.app.presenters.PresenterTexts;
import singularfactory.app.presenters.interfaces.IPresenterSplash;
import singularfactory.app.presenters.interfaces.IPresenterTexts;

public class AppCommon extends MultiDexApplication {

    private static AppCommon singleton;
    public static AppCommon getInstance() { return singleton; }

    private Typeface font;

    @Override
    public void onCreate() {
        super.onCreate();
        font = Typeface.createFromAsset(getAssets(),"fonts/Handlee-Regular.ttf");
        presenterSplash = null;
        singleton       = this;
    }

    /************/
    /** MODELS **/
    /************/

    private IModel model;
    public IModel getModel(){
        if (model == null) model = new Model();
        return model;
    }

    private IModelTexts modelTexts;
    public IModelTexts getModelTexts() {
        if (modelTexts == null) modelTexts = new ModelTexts();
        return modelTexts;
    }


    /****************/
    /** PRESENTERS **/
    /****************/

    private IPresenterSplash presenterSplash;
    public IPresenterSplash getPresenterSplash() {
        if (presenterSplash == null) presenterSplash = new PresenterSplash();
        return presenterSplash;
    }

    private IPresenterTexts presenterTexts;
    public IPresenterTexts getPresenterTexts() {
        if (presenterTexts == null) presenterTexts = new PresenterTexts();
        return presenterTexts;
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
    public Typeface getFont () {
        return font;
    }


}
