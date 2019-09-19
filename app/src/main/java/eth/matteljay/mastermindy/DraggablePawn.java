package eth.matteljay.mastermindy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.widget.AppCompatImageView;

public class DraggablePawn extends AppCompatImageView implements View.OnClickListener,
        View.OnLongClickListener, View.OnDragListener {

    private static int lastViewRes;
    private boolean removeOriginal;
    private int resID;
    private int bgNorm, bgLight = 0;
    private ViewGroup parent;
    private View bgView = null;
    private MainActivity mainActivity;

    public DraggablePawn(Context context) {
        super(context);
    }

    public DraggablePawn(Context context, int resID, boolean removeOriginal) {
        super(context);
        this.removeOriginal = removeOriginal;
        this.resID = resID;
        mainActivity = (MainActivity) context;
        FrameLayout.LayoutParams frameLayoutPars = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        setLayoutParams(frameLayoutPars);
        setImageResource(resID);
        setOnClickListener(this);
        setOnLongClickListener(this);
        if(!removeOriginal) {
            setOnDragListener(this);
        }
    }

    protected void setBGView(View v) {
        bgView = v;
        bgNorm = ((ColorDrawable) v.getBackground()).getColor();
        float[] hsv = new float[3];
        Color.colorToHSV(bgNorm, hsv);
        hsv[2] *= 1.6f;
        bgLight = Color.HSVToColor(hsv);
    }

    protected static int getLastViewRes() {
        return lastViewRes;
    }

    protected int getResID() {
        return resID;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        parent = (ViewGroup) getParent();
    }

    @Override
    public void onClick(View v) {
        if(mainActivity.isGameFrozen()) {
            return;
        }
        if(removeOriginal) {
            parent.removeView(v);
        } else {
            ViewGroup vg = mainActivity.findViewById(R.id.playField);
            for(int i = 0; i < vg.getChildCount(); i++) {
                ViewGroup nextChild = (ViewGroup) vg.getChildAt(i);
                if(nextChild.getChildCount() == 0) {
                    nextChild.addView(new DraggablePawn(mainActivity, resID, true));
                    break;
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(mainActivity.isGameFrozen()) {
            return true;
        }
        View.DragShadowBuilder theShadow = new View.DragShadowBuilder(v);
        lastViewRes = resID;
        v.startDrag(null, theShadow,null,0);
        if(removeOriginal) {
            parent.removeView(v);
        } else if(bgView != null) {
            bgView.setBackgroundColor(bgLight);
        }
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch(event.getAction()) {
            case DragEvent.ACTION_DRAG_ENDED:
                if(bgView != null) {
                    bgView.setBackgroundColor(bgNorm);
                }
                break;
            default:
                break;
        }
        return true;
    }
}
