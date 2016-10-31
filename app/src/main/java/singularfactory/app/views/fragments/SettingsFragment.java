package singularfactory.app.views.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.juanpabloprado.countrypicker.CountryPicker;
import com.juanpabloprado.countrypicker.CountryPickerListener;

import singularfactory.app.R;
import singularfactory.app.common.AppCommon;
import singularfactory.app.views.activities.MainActivity;

public class SettingsFragment extends PreferenceFragmentCompat {
    protected AppCommon appCommon;

    Preference deleteAccount;
    Preference changeEmail;
    Preference changePassword;
    Preference changeCountry;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        appCommon = AppCommon.getInstance();
        setPreferencesFromResource(R.xml.preferences,s);

        deleteAccount = getPreferenceManager().findPreference("delete_account");
        deleteAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle)
                        .setMessage(getString(R.string.delete_confirmation))
                        .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { return; }
                        })
                        .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { deleteAccount(); }
                        })
                        .create()
                        .show();
                return false;
                    }
                });

        changeEmail = getPreferenceScreen().findPreference("change_email");
        changeEmail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });

        changePassword = getPreferenceScreen().findPreference("change_password");
        changePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });

        changeCountry = getPreferenceManager().findPreference("change_country");
        changeCountry.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CountryPicker picker = CountryPicker.getInstance("Select Country", new CountryPickerListener() {
                    @Override public void onSelectCountry(String name, String code) {
                        DialogFragment dialogFragment =
                                (DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CountryPicker");
                        dialogFragment.dismiss();
                    }
                });
                picker.show(getActivity().getSupportFragmentManager(), "CountryPicker");
                return true;
            }
        });

    }

    public void deleteAccount() {
        appCommon.getPresenterUser().deleteUser(
                this,
                "Delete user",
                Request.Method.DELETE,
                appCommon.getBaseURL() + "users/"+appCommon.getUser().getId(),
                "Deleting account...");
    }

    public void deleteAccountSuccess() {
        showToast("Account successfully deleted");
        ((MainActivity)getActivity()).logout();
    }

    public void showToast(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
