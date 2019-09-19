package eth.matteljay.mastermindy;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;

public class GameWindow {
    private final MainActivity mainActivity;
    private final TableLayout pawnField;
    private final TableRow playField;
    private final int numPawnFields;
    private final int numPlayFields;

    protected GameWindow(MainActivity mainActivity, int numPawnFields, int numPlayFields) {
        this.mainActivity = mainActivity;
        this.numPawnFields = numPawnFields;
        this.numPlayFields = numPlayFields;
        mainActivity.findViewById(R.id.scroller).setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.history));

        pawnField = mainActivity.findViewById(R.id.pawnField);
        pawnField.removeAllViews();
        buildPawnFields();

        playField = mainActivity.findViewById(R.id.playField);
        playField.removeAllViews();
        buildPlayFields();
    }

    private void buildPawnFields() {
        for(int i = 0; i < numPawnFields; i++) {
            // TableRow(TableLayout)
            TableLayout.LayoutParams tablePars = new TableLayout.LayoutParams(0, 0, 1.0f);
            TableRow tableRow = new TableRow(mainActivity);
            tableRow.setLayoutParams(tablePars);

            // FrameLayout(TableRow)
            TableRow.LayoutParams tableRowPars = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
            FrameLayout frameLayout = new FrameLayout(mainActivity);
            frameLayout.setLayoutParams(tableRowPars);

            // View(FrameLayout)
            FrameLayout.LayoutParams frameLayoutPars = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            View view = new View(mainActivity);
            view.setLayoutParams(frameLayoutPars);
            view.setBackgroundColor(ContextCompat.getColor(mainActivity, (i % 2 == 0) ? R.color.sideOdd : R.color.sideEven));

            // Custom View
            DraggablePawn dp = new DraggablePawn(mainActivity, Translator.getInstance().posToR(i), false);
            dp.setBGView(view);

            frameLayout.addView(view);
            frameLayout.addView(dp);
            tableRow.addView(frameLayout);
            pawnField.addView(tableRow);
        }
    }

    private void buildPlayFields() {
        for(int i = 0; i < numPlayFields; i++) {
            // TableLayout(TableRow)
            TableRow.LayoutParams rowPars = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
            rowPars.gravity = Gravity.CENTER;
            TableLayout tableLayout = new TableLayout(mainActivity);
            tableLayout.setBackgroundColor(ContextCompat.getColor(mainActivity, (i % 2 == 0) ? R.color.topOdd : R.color.topEven));
            tableLayout.setLayoutParams(rowPars);
            tableLayout.setOnDragListener(new PlayFieldDropper());
            playField.addView(tableLayout);
        }
    }

    protected void restoreInput(ArrayList<Integer> pawns) {
        if(pawns == null || pawns.size() == 0) {
            return;
        }
        ViewGroup vg = mainActivity.findViewById(R.id.playField);
        for(int i = 0; i < vg.getChildCount(); i++) {
            ViewGroup nextChild = (ViewGroup) vg.getChildAt(i);
            if(pawns.get(i) != -1) {
                nextChild.removeAllViewsInLayout(); // overwrite
                ImageView img = new DraggablePawn(mainActivity, pawns.get(i), true);
                nextChild.addView(img);
            }
        }
    }

}
