package net.gerardomedina.diktaplus.common;

import android.support.multidex.MultiDexApplication;

import net.gerardomedina.diktaplus.models.Model;
import net.gerardomedina.diktaplus.models.User;
import net.gerardomedina.diktaplus.presenters.PresenterGame;
import net.gerardomedina.diktaplus.presenters.PresenterText;
import net.gerardomedina.diktaplus.presenters.PresenterUser;

public class AppCommon extends MultiDexApplication {

    private static AppCommon singleton;
    public static AppCommon getInstance() { return singleton; }

    private User user;


    @Override
    public void onCreate() {
        super.onCreate();
        singleton       = this;
    }

    /* MODELS **/

    private Model model;
    public Model getModel(){
        if (model == null) model = new Model();
        return model;
    }


    /* PRESENTERS */

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

    /* Utils */

    public Utils getUtils() {
        return Utils.getInstance();
    }
    public String getBaseURL() {
        return "https://diktaplusws.herokuapp.com/api/";
    }
    public String getOauthURL() {
        return "https://diktaplusws.herokuapp.com/oauth/v2/token";
    }

    public String getOauthClientId() {
        return "4_9kqz9z7cg1s0ck8s4og80o8g4cogsw84c00wk8kwoowsco4gw";
    }

    public String getOauthClientSecret() {
        return "1cw6xjxmeen4o88gckosg8g80kcc4scg48sgg8kggwcc48ss8g";
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
