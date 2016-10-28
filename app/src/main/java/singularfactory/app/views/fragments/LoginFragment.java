package singularfactory.app.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import singularfactory.app.R;
import singularfactory.app.models.Text;
import singularfactory.app.models.User;
import singularfactory.app.views.activities.LoginActivity;
import singularfactory.app.views.activities.MainActivity;

public class LoginFragment extends BaseFragment {

    EditText usernameOrEmail;
    EditText password;
    String savedEmail;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameOrEmail = (EditText)view.findViewById(R.id.username_input);
        password = (EditText)view.findViewById(R.id.password_input);
        if (savedEmail!=null) usernameOrEmail.setText(savedEmail);

        return view;
    }

    public void getUser() throws JSONException {
        String [] params = {usernameOrEmail.getText().toString(), password.getText().toString()};
        appCommon.getPresenterUser().loginUser(
                this,
                "Login user",
                Request.Method.POST,
                appCommon.getBaseURL()+"users/login",
                "Trying to log in...",
                params);
    }

    public void setUser(JSONObject userJson) {
        try {
            appCommon.setUser(new User(userJson.getInt("id"),
                    userJson.getString("email"),
                    userJson.getString("username"),
                    userJson.getString("country"),
                    userJson.getInt("total_score"),
                    userJson.getInt("level")));
            appCommon.getUtils().sharedSetValue(getContext(),"id",userJson.getInt("id"));
        } catch (JSONException e) {
            Log.e(TAG,"Error parsing received JSON");
        }
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
