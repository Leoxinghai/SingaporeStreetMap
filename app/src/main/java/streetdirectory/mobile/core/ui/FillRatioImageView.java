

package streetdirectory.mobile.core.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FillRatioImageView extends ImageView
{

    public FillRatioImageView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    protected void onMeasure(int i, int j)
    {
        Drawable drawable = getDrawable();
        if(drawable != null)
        {
            i = android.view.View.MeasureSpec.getSize(i);
            setMeasuredDimension(i, (int)Math.ceil((double)i * ((double)drawable.getIntrinsicHeight() / (double)drawable.getIntrinsicWidth())));
            return;
        } else
        {
            super.onMeasure(i, j);
            return;
        }
    }
}
