

package streetdirectory.mobile.core;

import android.graphics.drawable.Drawable;
import android.view.View;

public class Compatibility
{

    public Compatibility()
    {
    }

    public static void setBackgroundDrawable(View view, Drawable drawable)
    {
        if(android.os.Build.VERSION.SDK_INT < 16)
        {
            view.setBackgroundDrawable(drawable);
            return;
        } else
        {
            view.setBackground(drawable);
            return;
        }
    }
}
