

package streetdirectory.mobile.core.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ExpandableHeightGridView extends GridView
{

    public ExpandableHeightGridView(Context context)
    {
        super(context);
        mExpanded = false;
    }

    public ExpandableHeightGridView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mExpanded = false;
    }

    public ExpandableHeightGridView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mExpanded = false;
    }

    public boolean isExpanded()
    {
        return mExpanded;
    }

    public void onMeasure(int i, int j)
    {
        if(isExpanded())
        {
            super.onMeasure(i, android.view.View.MeasureSpec.makeMeasureSpec(0x1fffffff, 0x80000000));
            getLayoutParams().height = getMeasuredHeight();
            return;
        } else
        {
            super.onMeasure(i, j);
            return;
        }
    }

    public void setExpanded(boolean flag)
    {
        mExpanded = flag;
    }

    private boolean mExpanded;
}
