package eth.matteljay.mastermindy;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;

public class PlayFieldDropper implements View.OnDragListener {
    private int bgNorm, bgLight = 0;

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch(event.getAction()) {
            case DragEvent.ACTION_DRAG_ENTERED:
                if(bgNorm == 0) {
                    bgNorm = ((ColorDrawable) v.getBackground()).getColor();
                    float[] hsv = new float[3];
                    Color.colorToHSV(bgNorm, hsv);
                    hsv[2] *= 1.6f;
                    bgLight = Color.HSVToColor(hsv);
                }
                v.setBackgroundColor(bgLight);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundColor(bgNorm);
                break;
            case DragEvent.ACTION_DROP:
                TableLayout table = (TableLayout) v;
                table.removeAllViews();
                MainActivity activ = (MainActivity) v.getContext();
                ImageView img = new DraggablePawn(activ, DraggablePawn.getLastViewRes(), true);
                table.addView(img);
                table.setBackgroundColor(bgNorm);
                break;
            default:
                break;
        }
        return true;
    }
}
