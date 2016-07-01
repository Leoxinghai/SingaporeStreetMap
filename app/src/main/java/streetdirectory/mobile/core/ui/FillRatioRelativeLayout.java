

package streetdirectory.mobile.core.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.xinghai.mycurve.R;

public class FillRatioRelativeLayout extends RelativeLayout
{

    public FillRatioRelativeLayout(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        //mRatio = context.obtainStyledAttributes(attributeset, R.styleable.FillRatio).getFloat(0, 1.0F);
        mRatio = 0.88f;
    }

    protected void onMeasure(int i, int j)
    {
        i = android.view.View.MeasureSpec.getSize(i);
        j = (int)Math.ceil((float)i * mRatio);
        int k = android.view.View.MeasureSpec.makeMeasureSpec(i, 0x80000000);
        int l = android.view.View.MeasureSpec.makeMeasureSpec(j, 0x80000000);
        android.view.ViewGroup.LayoutParams layoutparams = getLayoutParams();
        layoutparams.height = j;
        layoutparams.width = i;
        super.onMeasure(k, l);
    }

    private float mRatio;
}
