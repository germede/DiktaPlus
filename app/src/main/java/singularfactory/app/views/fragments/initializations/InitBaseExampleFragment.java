package singularfactory.app.views.fragments.initializations;

import android.view.View;

import singularfactory.app.AppCommon;

/**
 * Created by Óscar Adae Rodríguez on 18/08/2016.
 */
public class InitBaseExampleFragment {

    private AppCommon appCommon;
    private Object parent;
    private InitBaseExampleListener listener;

    public View view;


    public InitBaseExampleFragment(Object parent) {

        appCommon   = AppCommon.getInstance();
        this.parent = parent;
    }

    public void initialize(View view) {

        this.view = view;

        //TODO Initialize items on the view
    }

    public void initializeActions() {

        //TODO Actions button, imageButton, ...
    }

    public void initializeCustomFonts() {

        //TODO Custom fonts...
    }

    /** LISTENER **/
    public interface InitBaseExampleListener {

    }

    public void setInitBaseExampleListener(InitBaseExampleListener listener) {
        this.listener = listener;
    }
}
