

package streetdirectory.mobile.gis.maps;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.Scroller;

public class RotatableView extends View
    implements android.view.GestureDetector.OnGestureListener, android.view.GestureDetector.OnDoubleTapListener, android.view.ScaleGestureDetector.OnScaleGestureListener
{
    public static interface GestureListener
    {

        public abstract boolean onDoubleTap(MotionEvent motionevent);

        public abstract boolean onDoubleTapEvent(MotionEvent motionevent);

        public abstract boolean onFling(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1);

        public abstract void onLongPress(MotionEvent motionevent);

        public abstract void onRotate(float f, float f1, float f2, float f3, float f4, float f5, float f6,
                float f7, float f8);

        public abstract boolean onScale(float f, float f1, float f2);

        public abstract boolean onScaleBegin(float f);

        public abstract void onScaleEnd(float f);

        public abstract boolean onScroll(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1);

        public abstract boolean onSingleTapConfirmed(MotionEvent motionevent);

        public abstract boolean onSingleTapUp(MotionEvent motionevent);
    }


    public RotatableView(Context context)
    {
        super(context);
        mScaleFactor = 1.0F;
        init();
    }

    public RotatableView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mScaleFactor = 1.0F;
        init();
    }

    public RotatableView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mScaleFactor = 1.0F;
        init();
    }

    public RotatableView(Context context, AttributeSet attributeset, int i, int j)
    {
        super(context, attributeset, i, j);
        mScaleFactor = 1.0F;
        init();
    }

    private float angleBetweenLines(float f, float f1, float f2, float f3, float f4, float f5, float f6,
            float f7)
    {
        f1 = (float)Math.toDegrees((float)Math.atan2(f1 - f3, f - f2) - (float)Math.atan2(f5 - f7, f4 - f6)) % 360F;
        f = f1;
        if(f1 < -180F)
            f = f1 + 360F;
        f1 = f;
        if(f > 180F)
            f1 = f - 360F;
        return f1;
    }

    private void init()
    {
        mGestureDetector = new GestureDetectorCompat(getContext(), this);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), this);
        mGestureDetector.setOnDoubleTapListener(this);
        mScroller = new Scroller(getContext());
        ptrID1 = -1;
        ptrID2 = -1;
    }

    public boolean onDoubleTap(MotionEvent motionevent)
    {
        Log.d("RotatableView", "onDoubleTap");
        if(mListener != null)
            mListener.onDoubleTap(motionevent);
        return true;
    }

    public boolean onDoubleTapEvent(MotionEvent motionevent)
    {
        Log.d("RotatableView", "onDoubleTapEvent");
        if(mListener != null)
            mListener.onDoubleTapEvent(motionevent);
        return false;
    }

    public boolean onDown(MotionEvent motionevent)
    {
        Log.d("RotatableView", "onDown");
        return false;
    }

    public boolean onFling(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
    {
		float f2;
		float f3;
		Log.d("RotatableView", (new StringBuilder()).append("onFling() called with: e1 = [").append(motionevent.getY()).append(", ").append(motionevent.getY()).append("], e2 = [").append(motionevent1.getY()).append(", ").append(motionevent1.getY()).append("], ").append("velocityX = [").append(f).append("], velocityY = [").append(f1).append("]").toString());
		int i = (int)(motionevent1.getX() - motionevent.getX());
		int j = (int)(motionevent1.getY() - motionevent.getY());
		mScroller.fling((int)motionevent1.getX(), (int)motionevent1.getY(), (int)f, (int)f1, i, getWidth(), j, getHeight());
		if(mListener == null)
	        return true;
		if(motionevent.getX() - motionevent1.getX() <= 120F || Math.abs(f) <= 200F)
		{
			f3 = f;
			f2 = f1;
		} else {
			f2 = f1 / 3F;
			f3 = (float)((double)f / 1.5D);
		}

		if(motionevent.getY() - motionevent1.getY() <= 120F || Math.abs(f2) <= 200F)
		{
			f1 = f3;
			f = f2;
		} else {
			f1 = f3 / 3F;
			f = (float)((double)f2 / 1.5D);
		}

		mListener.onFling(motionevent, motionevent1, f1, f);
        return true;
    }

    public void onLongPress(MotionEvent motionevent)
    {
        if(mListener != null)
            mListener.onLongPress(motionevent);
    }

    public void onRotate(float f, float f1, float f2, float f3, float f4, float f5, float f6,
            float f7, float f8)
    {
        Log.d("RotatableView", (new StringBuilder()).append("onRotate ").append(f).append(", ").append(f1).append(", ").append(f2).append(", ").append(f3).append(", ").append(f2).append(", ").append(f5).append(", ").append(f6).append(", ").append(f7).append(", ").append(f8).toString());
        if(mListener != null)
            mListener.onRotate(f, f1, f2, f3, f4, f5, f6, f7, f8);
    }

    public boolean onScale(ScaleGestureDetector scalegesturedetector)
    {
        Log.d("RotatableView", (new StringBuilder()).append("onScale() called with: detector = [").append(scalegesturedetector.getScaleFactor()).append("]").toString());
        float f = Math.max(1.0F, Math.min(mScaleFactor * scalegesturedetector.getScaleFactor(), 13F));
        if(f != mScaleFactor && mListener != null)
        {
            mScaleFactor = f;
            mListener.onScale(f, scalegesturedetector.getFocusX(), scalegesturedetector.getFocusY());
        }
        return true;
    }

    public boolean onScaleBegin(ScaleGestureDetector scalegesturedetector)
    {
        Log.d("RotatableView", "onScaleBegin");
        if(mListener != null)
            mListener.onScaleBegin(mScaleFactor);
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector scalegesturedetector)
    {
        Log.d("RotatableView", "onScaleEnd");
        if(mListener != null)
            mListener.onScaleEnd(mScaleFactor);
    }

    public boolean onScroll(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
    {
        Log.d("RotatableView", (new StringBuilder()).append("onScroll ").append(f).append(", ").append(f1).toString());
        if(mListener != null)
            mListener.onScroll(motionevent, motionevent1, f, f1);
        return true;
    }

    public void onShowPress(MotionEvent motionevent)
    {
    }

    public boolean onSingleTapConfirmed(MotionEvent motionevent)
    {
        Log.d("RotatableView", "onSingleTapConfirmed");
        if(mListener != null)
            mListener.onSingleTapConfirmed(motionevent);
        return true;
    }

    public boolean onSingleTapUp(MotionEvent motionevent)
    {
        Log.d("RotatableView", "onSingleTapUp");
        if(mListener != null)
            mListener.onSingleTapUp(motionevent);
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        float f;
        float f1;
        float f2;
        float f3;
        float f4;
        float f5;
        boolean flag;

        switch(motionevent.getActionMasked()) {
default:
case 4:
        break; /* Loop/switch isn't completed */
case 3:
		ptrID1 = -1;
		ptrID2 = -1;
		mAngle = mTempAngle;
		  break;

case 0:
        ptrID1 = motionevent.getPointerId(motionevent.getActionIndex());
          break;
case 5:
        ptrID2 = motionevent.getPointerId(motionevent.getActionIndex());
        sX = motionevent.getX(motionevent.findPointerIndex(ptrID1));
        sY = motionevent.getY(motionevent.findPointerIndex(ptrID1));
        fX = motionevent.getX(motionevent.findPointerIndex(ptrID2));
        fY = motionevent.getY(motionevent.findPointerIndex(ptrID2));
          break;
case 2:
        if(ptrID1 != -1 && ptrID2 != -1)
        {
            f2 = motionevent.getX(motionevent.findPointerIndex(ptrID1));
            f3 = motionevent.getY(motionevent.findPointerIndex(ptrID1));
            f4 = motionevent.getX(motionevent.findPointerIndex(ptrID2));
            f5 = motionevent.getY(motionevent.findPointerIndex(ptrID2));
            f1 = angleBetweenLines(fX, fY, sX, sY, f4, f5, f2, f3);
            Log.d("RotatableView", (new StringBuilder()).append("onTouchEvent: angle = ").append(f1).toString());
            Log.d("RotatableView", (new StringBuilder()).append("onTouchEvent: last angle = ").append(mTempAngle).toString());
            f = f1;
            if(mAngle != 0.0F)
                f = f1 + mAngle;
            Log.d("RotatableView", (new StringBuilder()).append("onTouchEvent: passed angle = ").append(f).toString());
            mTempAngle = f;
            onRotate(f, sX, sY, fX, fY, f4, f5, f2, f3);
        }
          break;
case 1:
        ptrID1 = -1;
        mAngle = mTempAngle;
	break;
case 6:
        ptrID2 = -1;
        mAngle = mTempAngle;
        break;
	}
        boolean flag1 = mScaleGestureDetector.onTouchEvent(motionevent);
        if(mGestureDetector.onTouchEvent(motionevent) || flag1)
            flag = true;
        else
            flag = false;
        return flag || super.onTouchEvent(motionevent);


    }

    public void setGestureListener(GestureListener gesturelistener)
    {
        mListener = gesturelistener;
    }

    public void setScaleFactor(float f)
    {
        mScaleFactor = f;
    }

    private static final int INVALID_POINTER_ID = -1;
    private static final int MAX_ZOOM = 13;
    private static final int MIN_ZOOM = 1;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final String TAG = "RotatableView";
    private float fX;
    private float fY;
    private float mAngle;
    private GestureDetectorCompat mGestureDetector;
    private GestureListener mListener;
    private float mScaleFactor;
    private ScaleGestureDetector mScaleGestureDetector;
    private Scroller mScroller;
    private float mTempAngle;
    private int ptrID1;
    private int ptrID2;
    private float sX;
    private float sY;
}
