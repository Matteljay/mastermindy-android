package eth.matteljay.mastermindy;

import java.util.ArrayList;

import androidx.lifecycle.ViewModel;

public class PersistVM extends ViewModel {
    public boolean isGameFrozen = false;
    public int currentTurn;
    public long gameTimeLeft_ms;
    public long turnTimeLeft_ms;
    public boolean isFirstResume = true;
    public ArrayList<Integer> theSecret = null;
    public ArrayList<Integer> btnInput = null;
    public ArrayList<ArrayList<Integer>> scrollData = null;

    public static void deepCopy(PersistVM dst, PersistVM src) {
        dst.isGameFrozen = src.isGameFrozen;
        dst.currentTurn = src.currentTurn;
        dst.gameTimeLeft_ms = src.gameTimeLeft_ms;
        dst.turnTimeLeft_ms = src.turnTimeLeft_ms;
        dst.theSecret = new ArrayList<>(src.theSecret);
        dst.btnInput = new ArrayList<>(src.btnInput);
        dst.scrollData = new ArrayList<>();
        for(ArrayList<Integer> scrollLine : src.scrollData) {
            ArrayList<Integer> newLine = new ArrayList<>(scrollLine);
            dst.scrollData.add(newLine);
        }
    }
}
