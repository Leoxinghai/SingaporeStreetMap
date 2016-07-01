

package streetdirectory.mobile.core.ui;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ImageViewZoomableTouchListener
    implements android.view.View.OnTouchListener
{

    public ImageViewZoomableTouchListener()
    {
        matrix = new Matrix();
        savedMatrix = new Matrix();
        mode = 0;
        start = new PointF();
        mid = new PointF();
        oldDist = 1.0F;
    }

    private void dumpEvent(MotionEvent motionevent)
    {
        StringBuilder stringbuilder = new StringBuilder();
        int i = motionevent.getAction();
        int k = i & 0xff;
        stringbuilder.append("event ACTION_").append((new String[] {
            "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"
        })[k]);
        if(k == 5 || k == 6)
        {
            stringbuilder.append("(pid ").append(i >> 8);
            stringbuilder.append(")");
        }
        stringbuilder.append("[");
        for(int j = 0; j < motionevent.getPointerCount(); j++)
        {
            stringbuilder.append("#").append(j);
            stringbuilder.append("(pid ").append(motionevent.getPointerId(j));
            stringbuilder.append(")=").append((int)motionevent.getX(j));
            stringbuilder.append(",").append((int)motionevent.getY(j));
            if(j + 1 < motionevent.getPointerCount())
                stringbuilder.append(";");
        }

        stringbuilder.append("]");
        Log.d("Touch", stringbuilder.toString());
    }

    private void midPoint(PointF pointf, MotionEvent motionevent)
    {
        float f = motionevent.getX(0);
        float f1 = motionevent.getX(1);
        float f2 = motionevent.getY(0);
        float f3 = motionevent.getY(1);
        pointf.set((f + f1) / 2.0F, (f2 + f3) / 2.0F);
    }

    private float spacing(MotionEvent motionevent)
    {
        float f = motionevent.getX(0) - motionevent.getX(1);
        float f1 = motionevent.getY(0) - motionevent.getY(1);
        return (float)Math.sqrt(f * f + f1 * f1);
    }

    public boolean onTouch(View view, MotionEvent motionevent)
    {
        view = (ImageView)view;
        dumpEvent(motionevent);
        switch(motionevent.getAction() & 0xff) {
		default:
		case 3:
		case 4:
				break;
		case 0:
				savedMatrix.set(matrix);
				start.set(motionevent.getX(), motionevent.getY());
				Log.d("Touch", "mode=DRAG");
				mode = 1;
				break;
		case 5:
				oldDist = spacing(motionevent);
				Log.d("Touch", (new StringBuilder()).append("oldDist=").append(oldDist).toString());
				if(oldDist > 10F)
				{
					savedMatrix.set(matrix);
					midPoint(mid, motionevent);
					mode = 2;
					Log.d("Touch", "mode=ZOOM");
				}
				break;
		case 1:
		case 6:
				mode = 0;
				Log.d("Touch", "mode=NONE");
				break;
		case 2:
				if(mode == 1)
				{
					matrix.set(savedMatrix);
					matrix.postTranslate(motionevent.getX() - start.x, motionevent.getY() - start.y);
				} else
				if(mode == 2)
				{
					float f = spacing(motionevent);
					Log.d("Touch", (new StringBuilder()).append("newDist=").append(f).toString());
					if(f > 10F)
					{
						matrix.set(savedMatrix);
						f /= oldDist;
						matrix.postScale(f, f, mid.x, mid.y);
					}
				}
				break;
			}
			//view.setImageMatrix(matrix);
			return true;

    }

    static final int DRAG = 1;
    static final int NONE = 0;
    private static final String TAG = "Touch";
    static final int ZOOM = 2;
    Matrix matrix;
    PointF mid;
    int mode;
    float oldDist;
    Matrix savedMatrix;
    PointF start;
}
