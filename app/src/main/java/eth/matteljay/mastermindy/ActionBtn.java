package eth.matteljay.mastermindy;

import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

public class ActionBtn {
    private final MainActivity mainActivity;
    private final PersistVM persistVM;
    private final FloatingActionButton fab;
    private final ViewGroup playField;

    public ActionBtn(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        fab = mainActivity.findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mainActivity, R.color.fab)));
        playField = mainActivity.findViewById(R.id.playField);
        persistVM = ViewModelProviders.of(mainActivity).get(PersistVM.class);
        LinearLayout scrollField = mainActivity.findViewById(R.id.scrollField);
        scrollField.removeAllViews();
    }

    public void drawHistory(ArrayList<Integer> pawn_Rvalues, ArrayList<Integer> hint_Rvalues, boolean store) {
        if(store) {
            persistVM.scrollData.add(pawn_Rvalues);
            persistVM.scrollData.add(hint_Rvalues);
        }

        SizeCappedLinearLayout scrollLine = new SizeCappedLinearLayout(mainActivity);
        scrollLine.setHeightLink(playField);
        scrollLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        scrollLine.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout playBlock = new LinearLayout(mainActivity);
        playBlock.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.75f));
        for(int rval : pawn_Rvalues) {
            playBlock.addView(getFitImageFromR(rval));
        }
        scrollLine.addView(playBlock);

        LinearLayout spacerBlock = new LinearLayout(mainActivity);
        spacerBlock.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.1f));
        scrollLine.addView(spacerBlock);

        LinearLayout hintBlock = new LinearLayout(mainActivity);
        hintBlock.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.25f));
        for(int rval : hint_Rvalues) {
            hintBlock.addView(getFitImageFromR(rval));
        }
        scrollLine.addView(hintBlock);

        // Add custom built line to scrollField
        LinearLayout scrollField = mainActivity.findViewById(R.id.scrollField);
        scrollField.addView(scrollLine, 0);

        // Scroll to top
        ScrollView scroller = mainActivity.findViewById(R.id.scroller);
        scroller.fullScroll(ScrollView.FOCUS_UP);
    }

    private ImageView getFitImageFromR(int rval) {
        ImageView img = new ImageView(mainActivity);
        img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        img.setImageDrawable(ContextCompat.getDrawable(mainActivity, rval));
        img.setAdjustViewBounds(true);
        return img;
    }

    public ArrayList<Integer> getInput() {
        ArrayList<Integer> feeler = new ArrayList<>();
        for(int i = 0; i < playField.getChildCount(); i++) {
            ViewGroup nextChild = (ViewGroup) playField.getChildAt(i);
            if(nextChild.getChildCount() == 1) {
                DraggablePawn pawn = (DraggablePawn) nextChild.getChildAt(0);
                feeler.add(pawn.getResID());
            } else {
                feeler.add(-1);
            }
        }
        return feeler;
    }

    public void clearInputs() {
        for(int i = 0; i < playField.getChildCount(); i++) {
            ViewGroup nextChild = (ViewGroup) playField.getChildAt(i);
            if(nextChild.getChildCount() == 1) {
                View pawn = nextChild.getChildAt(0);
                nextChild.removeView(pawn);
            }
        }
    }

    public void putScrollData() {
        if(persistVM.scrollData == null || persistVM.scrollData.size() == 0) {
            return;
        }
        LinearLayout scrollField = mainActivity.findViewById(R.id.scrollField);
        scrollField.removeAllViewsInLayout(); // overwrite
        for(int i = 0; i < persistVM.scrollData.size(); i += 2) {
            drawHistory(persistVM.scrollData.get(i), persistVM.scrollData.get(i + 1), false);
        }
    }

    public void setFAB(boolean gameFrozen, int turn) {
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mainActivity,
                gameFrozen ? R.color.endfab : R.color.fab)));
        TextView textFab = mainActivity.findViewById(R.id.textFab);
        textFab.setText(mainActivity.getString(R.string.hashnum, turn));
    }

    public void showSecret(ArrayList<Integer> secretRvalues) {
        for(int i = 0; i < playField.getChildCount(); i++) {
            ViewGroup nextChild = (ViewGroup) playField.getChildAt(i);
            if(nextChild.getChildCount() == 1) {
                View pawn = nextChild.getChildAt(0);
                nextChild.removeView(pawn);
            }
            if(secretRvalues.get(i) == null) {
                return;
            }
            nextChild.addView(new DraggablePawn(mainActivity, secretRvalues.get(i), true));
        }
    }
}
