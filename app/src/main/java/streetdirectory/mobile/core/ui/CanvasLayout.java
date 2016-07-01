

package streetdirectory.mobile.core.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CanvasLayout extends ViewGroup
{

    public CanvasLayout(Context context)
    {
        this(context, null);
    }

    public CanvasLayout(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public CanvasLayout(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        setClipChildren(false);
        setClipToPadding(false);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutparams)
    {
        return layoutparams instanceof android.widget.RelativeLayout.LayoutParams;
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeset)
    {
        return new android.widget.RelativeLayout.LayoutParams(getContext(), attributeset);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutparams)
    {
        return new android.widget.RelativeLayout.LayoutParams(layoutparams);
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l)
    {
        j = getChildCount();
        for(i = 0; i < j; i++)
        {
            View view = getChildAt(i);
            android.widget.RelativeLayout.LayoutParams layoutparams = (android.widget.RelativeLayout.LayoutParams)view.getLayoutParams();
            view.layout(layoutparams.leftMargin, layoutparams.topMargin, layoutparams.leftMargin + layoutparams.width, layoutparams.topMargin + layoutparams.height);
        }

    }

    protected void onMeasure(int i, int j)
    {
        super.onMeasure(i, j);
        for(i = 0; i < getChildCount(); i++)
        {
            View view = getChildAt(i);
            j = view.getLayoutParams().width;
            int k = view.getLayoutParams().height;
            view.measure(android.view.View.MeasureSpec.makeMeasureSpec(j, 0x40000000), android.view.View.MeasureSpec.makeMeasureSpec(k, 0x40000000));
        }

    }
}
