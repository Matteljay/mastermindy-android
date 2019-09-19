package eth.matteljay.mastermindy;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class SizeCappedLinearLayout extends LinearLayout {
    private View widthLink = null;
    private View heightLink = null;

    public SizeCappedLinearLayout(Context context) {
        super(context);
    }

    public void setWidthLink(View v) {
        widthLink = v;
    }

    public void setHeightLink(View v) {
        heightLink = v;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(widthLink != null) {
            heightMeasureSpec = getLinkedSpec(widthLink.getMeasuredHeight(), widthLink.getHeight());
        }
        if(heightLink != null) {
            heightMeasureSpec = getLinkedSpec(heightLink.getMeasuredHeight(), heightLink.getHeight());
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int getLinkedSpec(int measureDim, int dim) {
        if(dim > 0) {
            return MeasureSpec.makeMeasureSpec(dim, MeasureSpec.AT_MOST);
        } else {
            return MeasureSpec.makeMeasureSpec(measureDim, MeasureSpec.AT_MOST);
        }
    }
}
