package eth.matteljay.mastermindy;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;

import androidx.lifecycle.ViewModelProviders;

public class SaveGame {
    private final String PERSIST_OBJECT = "persist_object";
    private final MainActivity mainActivity;
    private final PersistVM persistVM;
    private final SharedPreferences prefs;

    public SaveGame(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        persistVM = ViewModelProviders.of(mainActivity).get(PersistVM.class);
        prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
    }

    public void Save() {
        SharedPreferences.Editor editor = prefs.edit();
        String jsonString = new Gson().toJson(persistVM);
        editor.putString(PERSIST_OBJECT, jsonString);
        editor.apply();
    }

    public void Load() {
        String jsonString = prefs.getString(PERSIST_OBJECT, "");
        if(jsonString == null || jsonString.isEmpty()) {
            return;
        }
        PersistVM.deepCopy(persistVM, new Gson().fromJson(jsonString, PersistVM.class));
        wipeSaved();
        Toast.makeText(mainActivity, mainActivity.getString(R.string.game_restored), Toast.LENGTH_SHORT).show();
    }

    private void wipeSaved() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PERSIST_OBJECT, "");
        editor.apply();
    }
}
