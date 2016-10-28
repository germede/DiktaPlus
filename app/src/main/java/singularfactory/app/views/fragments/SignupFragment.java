package singularfactory.app.views.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.juanpabloprado.countrypicker.CountryPicker;
import com.juanpabloprado.countrypicker.CountryPickerListener;

import singularfactory.app.R;

public class SignupFragment extends BaseFragment {

    EditText username;
    EditText email;
    EditText password;
    EditText country;

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
        country.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    CountryPicker picker = CountryPicker.getInstance("Select Country", new CountryPickerListener() {
                        @Override public void onSelectCountry(String name, String code) {
                            country.setText(code);
                            DialogFragment dialogFragment =
                                    (DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CountryPicker");
                            dialogFragment.dismiss();

                        }
                    });
                    picker.show(getActivity().getSupportFragmentManager(), "CountryPicker");
                }
            }
        });

        return view;
    }


    public void registerUser() {

    }

    public void showCountryList() {

    }
}
