package singularfactory.app.views.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.android.volley.Request;

import singularfactory.app.R;
import singularfactory.app.common.AppCommon;
import singularfactory.app.views.activities.MainActivity;

public class SettingsFragment extends PreferenceFragmentCompat {
    protected AppCommon appCommon;

    Preference deleteAccount;

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
