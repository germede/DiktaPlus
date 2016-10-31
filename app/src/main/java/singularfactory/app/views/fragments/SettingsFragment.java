package singularfactory.app.views.fragments;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import singularfactory.app.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    Preference deleteAccount;


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);

        deleteAccount = getPreferenceManager().findPreference("delete_account");
        deleteAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(),"hola",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}
