package singularfactory.app.views.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;

import org.json.JSONException;

import singularfactory.app.R;
import singularfactory.app.common.Utils;
import singularfactory.app.views.activities.LoginActivity;

public class SignupFragment extends BaseFragment {

    EditText username;
    EditText email;
    EditText password;
    EditText country;

    String selectedCountry;

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

        username = (EditText)view.findViewById(R.id.username_signup_input);
        email= (EditText)view.findViewById(R.id.email_signup_input);
        password = (EditText)view.findViewById(R.id.password_signup_input);
        country = (EditText)view.findViewById(R.id.country_signup_input);
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCountryList();
            }
        });
        country.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showCountryList();
                }
            }
        });
        return view;
    }



    public void registerUser() throws JSONException{
        if (username.getText().toString().indexOf(' ') > -1) {
            showToast("Username should not contain spaces");
        } else if (password.getText().toString().length() < 6) {
            showToast("Password should be longer");
        } else if (Utils.getInstance().isValidEmail(email.getText().toString())) {
            showToast("Please use a valid email");
        }else {
            String[] params = {username.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    selectedCountry};
            appCommon.getPresenterUser().registerUser(
                    this,
                    "Register user",
                    Request.Method.POST,
                    appCommon.getBaseURL() + "users/register",
                    "Trying to register...",
                    params);
        }
    }

    public void registerUserSuccess(String message) {
        showDialog(message);
        ((LoginActivity)getActivity()).onClickReturnLogin(getView());
    }

    private void showCountryList() {
//        CountryPicker picker = CountryPicker.getInstance(new CountryPickerListener() {
//            @Override public void onSelectCountry(String name, String code) {
//                country.setText(name);
//                selectedCountry = code;
//                ((DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CountryPicker")).dismiss();
//            }
//        });
//        picker.show(getActivity().getSupportFragmentManager(), "CountryPicker");
    }
}
