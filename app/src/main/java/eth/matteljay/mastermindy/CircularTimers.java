package eth.matteljay.mastermindy;

import android.os.CountDownTimer;
import android.widget.ProgressBar;

import androidx.lifecycle.ViewModelProviders;

public class CircularTimers {
    private final long TIMER_RESOLUTION = 100;
    private final MainActivity mainActivity;
    private final GameModel gameModel;
    private final PersistVM persistVM;
    private final ProgressBar gameProgressBar;
    private final ProgressBar turnProgressBar;
    private CountDownTimer gameCountDownTimer;
    private CountDownTimer turnCountDownTimer;

    public CircularTimers(MainActivity mainActivity, GameModel gameModel) {
        this.mainActivity = mainActivity;
        this.gameModel = gameModel;
        persistVM = ViewModelProviders.of(mainActivity).get(PersistVM.class);
        gameProgressBar = mainActivity.findViewById(R.id.gameProgressBar);
        turnProgressBar = mainActivity.findViewById(R.id.turnProgressBar);
    }

    public void resumeGameTimer(final long totalDuration) {
        if(totalDuration < 100) {
            gameProgressBar.setProgress(0);
            return;
        }
        if(gameCountDownTimer != null) {
            gameCountDownTimer.cancel();
        }
        gameProgressBar.setProgress((int)(1000 * persistVM.gameTimeLeft_ms/totalDuration));
        if(gameModel.isGameFrozen()) {
            return;
        }
        gameCountDownTimer = new CountDownTimer(persistVM.gameTimeLeft_ms, TIMER_RESOLUTION) {
            @Override
            public void onTick(long msecLeft) {
                gameProgressBar.setProgress((int)(1000 * msecLeft/totalDuration));
                persistVM.gameTimeLeft_ms = msecLeft;
            }
            @Override
            public void onFinish() {
                gameProgressBar.setProgress(0);
                persistVM.gameTimeLeft_ms = 0;
                gameModel.endGame(mainActivity.getString(R.string.game_timeout));
            }
        }.start();
    }

    public void resumeTurnTimer(final long totalDuration) {
        if(totalDuration < 100) {
            turnProgressBar.setProgress(0);
            return;
        }
        if(turnCountDownTimer != null) {
            turnCountDownTimer.cancel();
        }
        turnProgressBar.setProgress((int)(1000 * persistVM.turnTimeLeft_ms/totalDuration));
        if(gameModel.isGameFrozen()) {
            return;
        }
        turnCountDownTimer = new CountDownTimer(persistVM.turnTimeLeft_ms, TIMER_RESOLUTION) {
            @Override
            public void onTick(long msecLeft) {
                turnProgressBar.setProgress((int)(1000 * msecLeft/totalDuration));
                persistVM.turnTimeLeft_ms = msecLeft;
            }
            @Override
            public void onFinish() {
                turnProgressBar.setProgress(0);
                persistVM.turnTimeLeft_ms = 0;
                gameModel.pushTurn();
            }
        }.start();
    }

    public void cancelAll() {
        if(gameCountDownTimer != null) {
            gameCountDownTimer.cancel();
            gameCountDownTimer = null;
        }
        if(turnCountDownTimer != null) {
            turnCountDownTimer.cancel();
            turnCountDownTimer = null;
        }
    }
}
