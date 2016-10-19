package singularfactory.app.models;


import singularfactory.app.models.interfaces.IModel;
import singularfactory.app.models.interfaces.IModelMain;
import singularfactory.app.models.interfaces.IModelSplash;

/**
 * Created by Óscar Adae Rodríguez on 08/05/2016.
 */
public class Model implements IModel {

    private static final String TAG = Model.class.getSimpleName();

    private static Model singleton = null;

    public Model() {
        //constructor

    }

    public static Model getInstance() {
        if (singleton == null) {
            singleton = new Model();
        }
        return singleton;
    }

    @Override
    public IModelMain getModelMain() {
        return ModelMain.getInstance();
    }

    @Override
    public IModelSplash getModelSplash() {
        return ModelSplash.getInstance();
    }
}
