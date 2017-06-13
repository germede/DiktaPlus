package net.gerardomedina.diktaplus.views.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.juanpabloprado.countrypicker.CountryPicker;
import com.juanpabloprado.countrypicker.CountryPickerListener;

import org.json.JSONException;

import net.gerardomedina.diktaplus.R;
import net.gerardomedina.diktaplus.common.Utils;
import net.gerardomedina.diktaplus.views.activities.LoginActivity;

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
            public void onFocusChange(View view, boolean b) {if (b) showCountryList();
            }
        });
        return view;
    }



    public void registerUser() throws JSONException{
        if (username.getText().toString().indexOf(' ') > -1) {
            showToast(getString(R.string.contain_spaces));
        } else if (password.getText().toString().length() < 6) {
            showToast(getString(R.string.password_longer));
        } else if (!Utils.getInstance().isValidEmail(email.getText().toString())) {
            showToast(getString(R.string.valid_email));
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

    public void registerUserSuccess() {
        showDialog(getString(R.string.successfully_signed_up));
        ((LoginActivity)getActivity()).onClickReturnLogin(getView());
    }

    private void showCountryList() {
        CountryPicker picker = CountryPicker.getInstance(getString(R.string.select_country),new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code) {
                country.setText(name);
                selectedCountry = code;
                ((DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CountryPicker")).dismiss();
            }
        });
        picker.show(getActivity().getSupportFragmentManager(), "CountryPicker");
    }
}
