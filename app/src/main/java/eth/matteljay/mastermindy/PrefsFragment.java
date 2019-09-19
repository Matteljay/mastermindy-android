package eth.matteljay.mastermindy;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.Map;

public class PrefsFragment extends PreferenceFragment {
    private Toast infoToastMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        findPreference("btnCopyCrypto").setTitle("MasterMindy " + BuildConfig.BUILD_DATE_STAMP);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Map<String,?> keys = sharedPreferences.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()) {
            Preference pref = findPreference(entry.getKey());
            if(pref != null) {
                ChangeListener changeListener = new ChangeListener();
                if(pref instanceof ListPreference) {
                    String summary = sharedPreferences.getString(pref.getKey(), "");
                    if(TextUtils.isDigitsOnly(summary)) {
                        pref.setSummary(summary);
                    } else {
                        pref.setSummary(R.string.none);
                        ((ListPreference)pref).setValue(getString(R.string.none));
                    }
                }
                pref.setOnPreferenceChangeListener(changeListener);
            }
        }
        findPreference("btnCopyCrypto").setOnPreferenceClickListener(new CopyBTC());
    }

    @Override
    public void onPause() {
        if(infoToastMessage != null) {
            infoToastMessage.cancel();
        }
        super.onPause();
    }

    private class ChangeListener implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            if(preference instanceof ListPreference) {
                preference.setSummary(value.toString());
            }
            // 'Will restart game' functionality
            SettingsActivity settingsActivity = (SettingsActivity) getActivity();
            if(!settingsActivity.getDoRestart()) {
                settingsActivity.setDoRestart(true);
                infoToastMessage = Toast.makeText(preference.getContext(), getString(R.string.restart_notice), Toast.LENGTH_SHORT);
                infoToastMessage.show();
            }
            return true;
        }
    }

    private class CopyBTC implements Preference.OnPreferenceClickListener {
        private String getAddr() {
            byte[] buffer = new byte[34];
            int num = 1422870057; buffer[0] = (byte) (num >>> 5);
            num = -2103014202; buffer[1] = (byte) (num >>> 13);
            num = 895871199; buffer[2] = (byte) (num >>> 20);
            num = -352101645; buffer[3] = (byte) (num >>> 8);
            num = 1665554628; buffer[4] = (byte) (num >>> 24);
            num = 750050463; buffer[5] = (byte) (num >>> 15);
            num = -107753194; buffer[6] = (byte) (num >>> 11);
            num = -339705050; buffer[7] = (byte) (num >>> 3);
            num = -521407259; buffer[8] = (byte) (num >>> 17);
            num = -123070233; buffer[9] = (byte) (num >>> 15);
            num = 1978259462; buffer[10] = (byte) (num >>> 10);
            num = -827703635; buffer[11] = (byte) (num >>> 17);
            num = -320427955; buffer[12] = (byte) (num >>> 9);
            num = -1537215286; buffer[13] = (byte) (num >>> 1);
            num = -76879878; buffer[14] = (byte) (num >>> 9);
            num = -706937009; buffer[15] = (byte) (num >>> 18);
            num = 886898547; buffer[16] = (byte) (num >>> 24);
            num = -1700473566; buffer[17] = (byte) (num >>> 6);
            num = 1506319411; buffer[18] = (byte) (num >>> 19);
            num = 84131014; buffer[19] = (byte) (num >>> 7);
            num = 1099035394; buffer[20] = (byte) (num >>> 24);
            num = 612074936; buffer[21] = (byte) (num >>> 23);
            num = -86671514; buffer[22] = (byte) (num >>> 19);
            num = -147333786; buffer[23] = (byte) (num >>> 16);
            num = -2112469159; buffer[24] = (byte) (num >>> 8);
            num = 1964665411; buffer[25] = (byte) (num >>> 8);
            num = -1490942405; buffer[26] = (byte) (num >>> 15);
            num = 2007538842; buffer[27] = (byte) (num >>> 13);
            num = -721986743; buffer[28] = (byte) (num >>> 6);
            num = -15332634; buffer[29] = (byte) (num >>> 14);
            num = 1518408655; buffer[30] = (byte) (num >>> 24);
            num = -7965478; buffer[31] = (byte) (num >>> 1);
            num = 1001204403; buffer[32] = (byte) (num >>> 3);
            num = -637474856; buffer[33] = (byte) (num >>> 9);
            return new String(buffer);
        }
        @Override
        public boolean onPreferenceClick(Preference preference) {
            ClipboardManager clipboard = (ClipboardManager) preference.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            final String myBTC = getAddr();
            ClipData clip = ClipData.newPlainText(null, myBTC);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(preference.getContext(), getString(R.string.clipboard) + ": " + myBTC, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

}
