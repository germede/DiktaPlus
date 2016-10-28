package singularfactory.app.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import singularfactory.app.R;

public class SignupFragment extends BaseFragment {

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        return view;
    }


    public void registerUser() {
        String [] params = {username.getText().toString(),email.getText().toString(), password.getText().toString()};
        appCommon.getPresenterUser().loginUser(
                this,
                "Login user",
                Request.Method.POST,
                appCommon.getBaseURL()+"users/login",
                "Trying to log in...",
                params);

    }
}
