package ca.elohello.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.ImageButton;
import android.widget.TextView;

public class AccountInfo extends AppCompatActivity {

    TextView info;
    ImageButton boutonMessages;
    ImageButton boutonFeu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MyPreferenceFragment())
                .commit();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @SuppressLint("ResourceType")
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            ListPreference listPreference = (ListPreference) findPreference("liste");
            CharSequence currText = listPreference.getEntry();
            String currValue = listPreference.getValue();

            final SwitchPreference dark = (SwitchPreference) findPreference("switch_preference_1");
            dark.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if (dark.isChecked()) {
                        dark.setChecked(false);
                    } else {
                        dark.setChecked(true);
                    }
                    return false;
                }
            });
        }
    }
}
