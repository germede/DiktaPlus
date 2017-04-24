package singularfactory.app.views.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.juanpabloprado.countrypicker.CountryPicker;
import com.juanpabloprado.countrypicker.CountryPickerListener;

import org.json.JSONException;

import java.util.Locale;

import singularfactory.app.R;
import singularfactory.app.common.AppCommon;
import singularfactory.app.models.User;
import singularfactory.app.views.activities.MainActivity;

public class SettingsFragment extends PreferenceFragmentCompat {
    protected AppCommon appCommon;

    Preference deleteAccount;
    Preference editAccount;

    String newEmail, newCountry;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        appCommon = AppCommon.getInstance();
        setPreferencesFromResource(R.xml.preferences, s);

        deleteAccount = getPreferenceManager().findPreference("delete_account");
        deleteAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle)
                        .setMessage(getString(R.string.delete_confirmation))
                        .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {deleteAccount();}
                        })
                        .create()
                        .show();
                return false;
            }
        });

        editAccount = getPreferenceManager().findPreference("edit_account");
        editAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                (new EditAccountDialog(getActivity())).show();
                return false;
            }
        });


    }

    public void editAccount(String email, String country, String password) throws JSONException {
        String[] params = {
                email,
                country,
                password,
        };
        appCommon.getPresenterUser().putUser(
                this,
                "Put user",
                Request.Method.PUT,
                appCommon.getBaseURL() + "users/"+appCommon.getUser().getId(),
                "Trying to update account...",
                params);
    }

    public void updateAccountSuccess() {
        showToast(getString(R.string.successfully_changed_account));
        appCommon.setUser(new User(appCommon.getUser().getId(),
                newEmail,
                appCommon.getUser().getUsername(),
                newCountry,
                appCommon.getUser().getTotalScore(),
                appCommon.getUser().getLevel()));
    }

    public void deleteAccount() {
        appCommon.getPresenterUser().deleteUser(
                this,
                "Delete user",
                Request.Method.DELETE,
                appCommon.getBaseURL() + "users/" + appCommon.getUser().getId(),
                "Deleting account...");
    }

    public void deleteAccountSuccess() {
        ((MainActivity) getActivity()).logout();
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }



    class EditAccountDialog extends Dialog implements
            android.view.View.OnClickListener {
        private Button ok, cancel;
        private EditText changeEmail, changeCountry, changePassword, oldPassword;
        private String selectedCountry;


        EditAccountDialog(Activity a) {
            super(a);
        }

        @Override
        protected void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_settings_edit);
            selectedCountry = appCommon.getUser().getCountry();

            setTitle(appCommon.getUser().getUsername());
            changeEmail = (EditText) findViewById(R.id.email_edit_input);
            changeCountry = (EditText) findViewById(R.id.country_edit_input);
            changePassword = (EditText) findViewById(R.id.password_edit_input);

            changeEmail.setText(appCommon.getUser().getEmail());
            changeCountry.setText((new Locale("", appCommon.getUser().getCountry())).getDisplayCountry());
            changeCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCountryList();
                }
            });
            changeCountry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) showCountryList();
                }
            });

            ok = (Button) findViewById(R.id.btn_edit_ok);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (changePassword.getText().toString().length() < 6 && changePassword.getText().toString().length() > 0) {
                        showToast("Password should be longer");
                    } else {
                        newCountry = selectedCountry;
                        newEmail = changeEmail.getText().toString();
                        try {
                            editAccount(changeEmail.getText().toString(),
                                    selectedCountry,
                                    changePassword.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    dismiss();
                }
            });
            cancel = (Button) findViewById(R.id.btn_edit_cancel);
            cancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            dismiss();
        }

        private void showCountryList() {
            CountryPicker picker = CountryPicker.getInstance(getString(R.string.select_country),new CountryPickerListener() {
                @Override
                public void onSelectCountry(String name, String code) {
                    changeCountry.setText(name);
                    selectedCountry = code;
                    ((DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CountryPicker")).dismiss();
                }
            });
            picker.show(getActivity().getSupportFragmentManager(), "CountryPicker");
        }
    }
}
