package singularfactory.app.common;

import android.support.multidex.MultiDexApplication;

import singularfactory.app.models.Model;
import singularfactory.app.models.OauthModel;
import singularfactory.app.models.User;
import singularfactory.app.presenters.PresenterGame;
import singularfactory.app.presenters.PresenterText;
import singularfactory.app.presenters.PresenterUser;

public class AppCommon extends MultiDexApplication {

    private static AppCommon singleton;
    public static AppCommon getInstance() { return singleton; }

    private User user;


    @Override
    public void onCreate() {
        super.onCreate();
        singleton       = this;
    }

    /************/
    /** MODELS **/
    /************/

    private Model model;
    public Model getModel(){
        if (model == null) model = new Model();
        return model;
    }

    private OauthModel oauthModel;
    public OauthModel getOauthModel(){
        if (oauthModel == null) oauthModel = new OauthModel();
        return oauthModel;
    }

    /****************/
    /** PRESENTERS **/
    /****************/


    private PresenterText presenterText;
    public PresenterText getPresenterText() {
        if (presenterText == null) presenterText = new PresenterText();
        return presenterText;
    }

    private PresenterUser presenterUser;
    public PresenterUser getPresenterUser() {
        if (presenterUser == null) presenterUser = new PresenterUser();
        return presenterUser;
    }

    private PresenterGame presenterGame;
    public PresenterGame getPresenterGame() {
        if (presenterGame == null) presenterGame = new PresenterGame();
        return presenterGame;
    }


    /***************/
    /** UTILITIES **/
    /***************/

    public Utils getUtils() {
        return Utils.getInstance();
    }
    public String getBaseURL() {
        return "http://xan.singularfactory.com/sf_diktaplus_web/web/api/";
//        return "http://192.168.1.15:8000/api/";
    }
    public String getOauthURL() {
        return "http://xan.singularfactory.com/sf_diktaplus_web/web/oauth/v2/token";
//        return "http://192.168.1.15:8000/oauth/v2/token";
    }

    public String getOauthClientId() {
        return "2_6byf1af7fww8gg8840ocgo4kos84cwgccg0cwccsck8ow0ggkk";
//        return "1_srz8361vdvk088oc4wkcc40koc0swk0w00c8o8gcw0os44k44";
    }

    public String getOauthClientSecret() {
        return "35ojd1ynjg8w0sk0ckg4ogossocw0o8kocgc4s40wg8cc48osw";
//        return "3y6wbidjwg84ksswkoswwgskosskokkgwg4kwg8wkkwwkgw8so";
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
