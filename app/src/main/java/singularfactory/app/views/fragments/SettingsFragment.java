package singularfactory.app.views.fragments;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.android.volley.Request;

import singularfactory.app.R;
import singularfactory.app.common.AppCommon;
import singularfactory.app.views.activities.BaseActivity;
import singularfactory.app.views.activities.MainActivity;

public class SettingsFragment extends PreferenceFragmentCompat {
    Preference deleteAccount;
    protected AppCommon appCommon;


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        appCommon = AppCommon.getInstance();
        addPreferencesFromResource(R.xml.preferences);

        deleteAccount = getPreferenceManager().findPreference("delete_account");
        deleteAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ((BaseActivity)getActivity()).showAlertWithReflectionTwoButtons(getActivity(),getClass(),
                        getString(R.string.delete_confirmation),
                        getString(android.R.string.ok),
                        getString(android.R.string.cancel),
                        "deleteAccount");
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
        Toast.makeText(getContext(),"Account successfully deleted",Toast.LENGTH_SHORT).show();
        ((MainActivity)getActivity()).logout();
    }

    public void showToast(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
