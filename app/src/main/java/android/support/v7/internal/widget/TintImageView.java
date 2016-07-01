// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v7.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

// Referenced classes of package android.support.v7.internal.widget:
//            TintTypedArray, TintManager

public class TintImageView extends ImageView
{

    public TintImageView(Context context)
    {
        this(context, null);
    }

    public TintImageView(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public TintImageView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), attributeset, TINT_ATTRS, i, 0);
        if(tintTypedArray.length() > 0)
        {
            if(tintTypedArray.hasValue(0))
                setBackgroundDrawable(tintTypedArray.getDrawable(0));
            if(tintTypedArray.hasValue(1))
                setImageDrawable(tintTypedArray.getDrawable(1));
        }
        tintTypedArray.recycle();
        mTintManager = tintTypedArray.getTintManager();
    }

    public void setImageResource(int i)
    {
        setImageDrawable(mTintManager.getDrawable(i));
    }

    private static final int TINT_ATTRS[] = {
        0x10100d4, 0x1010119
    };
    private final TintManager mTintManager;

}
