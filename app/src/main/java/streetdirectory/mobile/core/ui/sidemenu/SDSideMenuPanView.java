

package streetdirectory.mobile.core.ui.sidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SDSideMenuPanView extends View
{

    public SDSideMenuPanView(Context context)
    {
        super(context);
        mode = 0;
        startX = 0.0F;
        startY = 0.0F;
        translateX = 0.0F;
        translateY = 0.0F;
        previousTranslateX = 0.0F;
        previousTranslateY = 0.0F;
        dragged = false;
        initialize(context);
    }

    public SDSideMenuPanView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mode = 0;
        startX = 0.0F;
        startY = 0.0F;
        translateX = 0.0F;
        translateY = 0.0F;
        previousTranslateX = 0.0F;
        previousTranslateY = 0.0F;
        dragged = false;
        initialize(context);
    }

    protected void initialize(Context context)
    {
    }

    protected void onDragEnd(int i)
    {
    }

    protected void onDragX(int i)
    {
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        switch(motionevent.getAction() & 0xff) {
		default:
				break;
		case 0:
				mode = DRAG;
				startX = motionevent.getX();
				startY = motionevent.getY();
				break;
		case 2:
				translateX = motionevent.getX() - startX;
				translateY = motionevent.getY() - startY;
				if((int)translateX != 0)
					onDragX((int)translateX);
				break;
		case 1:
				mode = NONE;
				dragged = false;
				onDragEnd((int)translateX);
				break;
		}
		return true;
    }

    private static int DRAG = 1;
    private static int NONE = 0;
    private boolean dragged;
    private int mode;
    private float previousTranslateX;
    private float previousTranslateY;
    private float startX;
    private float startY;
    private float translateX;
    private float translateY;

}
