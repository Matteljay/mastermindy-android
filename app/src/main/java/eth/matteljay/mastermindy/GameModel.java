package eth.matteljay.mastermindy;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.lifecycle.ViewModelProviders;
import mmcore.CodeProcessor;
import mmcore.HintStruct;
import mmcore.StartupGenerator;

public class GameModel {
    private final PersistVM persistVM;
    private final MainActivity mainActivity;
    private CodeProcessor codeProcessor;
    private GameWindow gameWindow;
    private ActionBtn actionBtn;
    private CircularTimers circularTimers = null;
    private HintStruct hint;
    private boolean avoidDuplicates;
    private boolean showHints;
    private boolean screenKeepOn;
    private int maxTurns;
    private int numPawns, assortPawns;
    private int gameTime, turnTime;
    private Toast infoToastMessage;

    public GameModel(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        persistVM = ViewModelProviders.of(mainActivity).get(PersistVM.class);
    }

    public void readSettings() {
        PreferenceManager.setDefaultValues(mainActivity, R.xml.pref_general, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        numPawns = getIntHelper(prefs,"numPawns");
        assortPawns = getIntHelper(prefs,"assortPawns");
        maxTurns = getIntHelper(prefs,"maxTurns");
        gameTime = getIntHelper(prefs,"gameTime");
        turnTime = getIntHelper(prefs,"turnTime");
        avoidDuplicates = prefs.getBoolean("avoidDuplicates", false);
        showHints = prefs.getBoolean("showHints", false);
        screenKeepOn = prefs.getBoolean("screenKeepOn", false);
    }

    public void BuildUI() {
        gameWindow = new GameWindow(mainActivity, assortPawns, numPawns);
        codeProcessor = new CodeProcessor();
        actionBtn = new ActionBtn(mainActivity);
        circularTimers = new CircularTimers(mainActivity, this);
    }

    public void freshGame() {
        persistVM.isGameFrozen = false;
        persistVM.btnInput = new ArrayList<>();
        persistVM.scrollData = new ArrayList<>();
        persistVM.currentTurn = 1;
        persistVM.gameTimeLeft_ms = gameTime * 60000;
        persistVM.turnTimeLeft_ms = turnTime * 1000;
        persistVM.theSecret = codeProcessor.newRandomSecret(numPawns, assortPawns, !avoidDuplicates);
        if(showHints) {
            StartupGenerator startupGenerator = new StartupGenerator(assortPawns, persistVM.theSecret);
            for(ArrayList<Integer> input : startupGenerator) {
                // Get new 'input'
                hint = codeProcessor.computeHint(persistVM.theSecret, input);
                ArrayList<Integer> hint_Rvalues = Translator.getInstance().hintToR(hint);
                actionBtn.drawHistory(Translator.getInstance().arrayListPostoR(input), hint_Rvalues, true);
            }
        }
    }

    public void resumeGame() {
        // Also triggered at start without pauseGame
        if(persistVM.isFirstResume) {
            new SaveGame(mainActivity).Load();
            persistVM.isFirstResume = false;
        }
        gameWindow.restoreInput(persistVM.btnInput);
        actionBtn.putScrollData();
        actionBtn.setFAB(persistVM.isGameFrozen, persistVM.currentTurn);
        circularTimers.resumeGameTimer(gameTime * 60000);
        circularTimers.resumeTurnTimer(turnTime * 1000);
    }

    public void pauseGame() {
        persistVM.btnInput = new ArrayList<>(actionBtn.getInput());
        circularTimers.cancelAll();
    }

    public void processTurnEvent() {
        if(persistVM.isGameFrozen) {
            mainActivity.resetGame();
            return;
        }
        // Check if all fields filled
        ArrayList<Integer> feeler_Rvalues = actionBtn.getInput();
        if(feeler_Rvalues.contains(-1)) {
            showToast(mainActivity.getString(R.string.fill_all_fields));
            return;
        }
        // Get new input
        hint = codeProcessor.computeHint(persistVM.theSecret, Translator.getInstance().arrayListRtoPos(feeler_Rvalues));
        ArrayList<Integer> hint_Rvalues = Translator.getInstance().hintToR(hint);
        actionBtn.drawHistory(feeler_Rvalues, hint_Rvalues, true);
        // Check if you won
        if(hint.numBlacks >= numPawns) {
            endGame(mainActivity.getString(R.string.you_won_game));
        } else {
            actionBtn.clearInputs();
            pushTurn();
        }
    }

    public void pushTurn() {
        if(persistVM.currentTurn >= maxTurns) {
            endGame(mainActivity.getString(R.string.out_of_turns));
            return;
        }
        persistVM.currentTurn++;
        actionBtn.setFAB(false, persistVM.currentTurn);
        persistVM.turnTimeLeft_ms = turnTime * 1000;
        circularTimers.resumeTurnTimer(turnTime * 1000);
    }

    public void endGame(String reason) {
        actionBtn.showSecret(Translator.getInstance().arrayListPostoR(persistVM.theSecret));
        circularTimers.cancelAll();
        persistVM.isGameFrozen = true;
        actionBtn.setFAB(true, persistVM.currentTurn);
        showToast(reason);
    }

    public void showToast(String message) {
        if(infoToastMessage != null) {
            infoToastMessage.cancel();
        }
        infoToastMessage = Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT);
        infoToastMessage.show();
    }

    public boolean isGameFrozen() {
        return persistVM.isGameFrozen;
    }

    public boolean isScreenKeepOn() {
        return screenKeepOn;
    }

    private int getIntHelper(SharedPreferences prefs, String key) {
        int ret;
        String str = prefs.getString(key, "");
        if(str == null || !TextUtils.isDigitsOnly(str)) {
            ret = 0;
        } else {
            ret = Integer.parseInt(str);
        }
        return ret;
    }

}
