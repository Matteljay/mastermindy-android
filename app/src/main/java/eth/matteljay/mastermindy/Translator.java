package eth.matteljay.mastermindy;

import java.util.ArrayList;
import java.util.Arrays;

import mmcore.HintStruct;

public class Translator {
    private static Translator instance = null;
    private final ArrayList<Integer> pawnColorAL;

    public static Translator getInstance() {
        if(instance == null) {
            instance = new Translator();
        }
        return instance;
    }

    private Translator() {
        pawnColorAL = new ArrayList<>(Arrays.asList(
                R.drawable.pawn_red, R.drawable.pawn_green, R.drawable.pawn_blue,
                R.drawable.pawn_fuchsia, R.drawable.pawn_yellow, R.drawable.pawn_aqua,
                R.drawable.pawn_maroon, R.drawable.pawn_lime, R.drawable.pawn_navy,
                R.drawable.pawn_purple, R.drawable.pawn_olive, R.drawable.pawn_teal));
    }

    public int posToR(int pos) {
        return pawnColorAL.get(pos);
    }

    public int RtoPos(int R) {
        return pawnColorAL.indexOf(R);
    }

    public ArrayList<Integer> arrayListRtoPos(ArrayList<Integer> valuesR) {
        ArrayList<Integer> arrayListPos = new ArrayList<>();
        for(int valR : valuesR) {
            arrayListPos.add(RtoPos(valR));
        }
        return arrayListPos;
    }

    public ArrayList<Integer> arrayListPostoR(ArrayList<Integer> valuesPos) {
        ArrayList<Integer> arrayListR = new ArrayList<>();
        for(int valPos : valuesPos) {
            arrayListR.add(posToR(valPos));
        }
        return arrayListR;
    }

    public ArrayList<Integer> hintToR(HintStruct hint) {
        ArrayList<Integer> arrayListHints = new ArrayList<>();
        for(int i = 0; i < hint.numBlacks; i++) {
            arrayListHints.add(R.drawable.hint_black);
        }
        for(int i = 0; i < hint.numWhites; i++) {
            arrayListHints.add(R.drawable.hint_white);
        }
        for(int i = 0; i < hint.numBlanks; i++) {
            arrayListHints.add(R.drawable.hint_blank);
        }
        return arrayListHints;
    }

}
