package singularfactory.app.views.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import org.json.JSONException;

import singularfactory.app.R;
import singularfactory.app.views.fragments.LoginFragment;
import singularfactory.app.views.fragments.SignupFragment;

public class LoginActivity extends BaseActivity {

    LoginFragment loginFragment;
    SignupFragment signupFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_login_container, loginFragment).commit();
    }

    public void onClickSignup(View view) {
        signupFragment = new SignupFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragment_login_container, signupFragment);
        transaction.commit();
    }

    public void onClickLogin(View view) throws JSONException{
        loginFragment.getUser();
    }

    public void onClickCountry(View view) {
        signupFragment.showCountryList();
    }

    public void onClickSubmit (View view) {
        signupFragment.registerUser();
    }

    public void onClickReturnLogin(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.fragment_login_container, loginFragment);
        transaction.commit();
    }
}
