// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

public class SdGestureDetector
{
    public static class RotationGestureDetector
    {

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

        public float getAngle()
        {
            return mAngle;
        }

        public boolean onTouchEvent(MotionEvent motionevent)
        {
            switch(motionevent.getActionMasked()) {
			default:
			case 4:
						return true;
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
							float f = motionevent.getX(motionevent.findPointerIndex(ptrID1));
							float f1 = motionevent.getY(motionevent.findPointerIndex(ptrID1));
							float f2 = motionevent.getX(motionevent.findPointerIndex(ptrID2));
							float f3 = motionevent.getY(motionevent.findPointerIndex(ptrID2));
							f1 = angleBetweenLines(fX, fY, sX, sY, f2, f3, f, f1);
							f = f1;
							if(mTempAngle != 0.0F)
								f = f1 + mTempAngle;
							mAngle = f;
							if(mListener != null)
								mListener.onRotation(this);
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
			case 3:
						ptrID1 = -1;
						ptrID2 = -1;
						mAngle = mTempAngle;
						break;
			}
			return true;
        }

        private static final int INVALID_POINTER_ID = -1;
        private float fX;
        private float fY;
        private float mAngle;
        private OnRotationGestureListener mListener;
        private float mTempAngle;
        private int ptrID1;
        private int ptrID2;
        private float sX;
        private float sY;

        public RotationGestureDetector(OnRotationGestureListener onrotationgesturelistener)
        {
            mListener = onrotationgesturelistener;
            ptrID1 = -1;
            ptrID2 = -1;
        }

        public static interface OnRotationGestureListener
        {

            public abstract void onRotation(RotationGestureDetector rotationgesturedetector);
        }

    }


    public static interface SdGestureDetectorListener
    {

        public abstract void onMove(SdGestureDetector sdgesturedetector);

        public abstract void onRotate(SdGestureDetector sdgesturedetector);

        public abstract void onScale(SdGestureDetector sdgesturedetector);
    }

    public static class Zoomer
    {

        public void abortAnimation()
        {
            mFinished = true;
            mCurrentZoom = mEndZoom;
        }

        public boolean computeZoom()
        {
            if(mFinished)
                return false;
            long l = SystemClock.elapsedRealtime() - mStartRTC;
            if(l >= (long)mAnimationDurationMillis)
            {
                mFinished = true;
                mCurrentZoom = mEndZoom;
                return false;
            } else
            {
                float f = ((float)l * 1.0F) / (float)mAnimationDurationMillis;
                mCurrentZoom = mEndZoom * mInterpolator.getInterpolation(f);
                return true;
            }
        }

        public void forceFinished(boolean flag)
        {
            mFinished = flag;
        }

        public float getCurrZoom()
        {
            return mCurrentZoom;
        }

        public void startZoom(float f)
        {
            mStartRTC = SystemClock.elapsedRealtime();
            mEndZoom = f;
            mFinished = false;
            mCurrentZoom = 1.0F;
        }

        private int mAnimationDurationMillis;
        private float mCurrentZoom;
        private float mEndZoom;
        private boolean mFinished;
        private Interpolator mInterpolator;
        private long mStartRTC;

        public Zoomer(Context context)
        {
            mFinished = true;
            mInterpolator = new DecelerateInterpolator();
            mAnimationDurationMillis = context.getResources().getInteger(0x10e0000);
        }
    }


    public SdGestureDetector(Context context, SdGestureDetectorListener sdgesturedetectorlistener)
    {
        mListener = sdgesturedetectorlistener;
        mScroller = new OverScroller(context);
        mZoomer = new Zoomer(context);
        mRotationDetector = new RotationGestureDetector(new RotationGestureDetector.OnRotationGestureListener() {

            public void onRotation(RotationGestureDetector rotationgesturedetector)
            {
                mAngle = rotationgesturedetector.getAngle();
                if(mListener != null)
                    mListener.onRotate(SdGestureDetector.this);
            }

        });
        mScaleDetector = new ScaleGestureDetector(context, new android.view.ScaleGestureDetector.OnScaleGestureListener() {

            public boolean onScale(ScaleGestureDetector scalegesturedetector)
            {
                float f = Math.max(1.0F, Math.min(mScale * scalegesturedetector.getScaleFactor(), 13F));
                if(f != mScale && mListener != null)
                {
                    mScale = f;
                    mListener.onScale(SdGestureDetector.this);
                }
                return false;
            }

            public boolean onScaleBegin(ScaleGestureDetector scalegesturedetector)
            {
                return true;
            }

            public void onScaleEnd(ScaleGestureDetector scalegesturedetector)
            {
            }

        }
);
        mGestureDetectorCompat = new GestureDetectorCompat(context, new android.view.GestureDetector.SimpleOnGestureListener() {

            public boolean onDoubleTap(MotionEvent motionevent)
            {
                mZoomer.forceFinished(true);
                mZoomer.startZoom(2.0F);
                if(mListener != null)
                    mListener.onScale(SdGestureDetector.this);
                return true;
            }

            public boolean onDown(MotionEvent motionevent)
            {
                mScroller.forceFinished(true);
                return true;
            }

            public boolean onFling(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
            {
                fling((int)(-f), (int)(-f1));
                if(mListener != null)
                    mListener.onMove(SdGestureDetector.this);
                return true;
            }

            public boolean onScroll(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
            {
                mX = mX + f;
                mY = mY + f1;
                if(mListener != null)
                    mListener.onMove(SdGestureDetector.this);
                return true;
            }

        });
    }

    private void fling(int i, int j)
    {
        mScroller.forceFinished(true);
        mScroller.fling((int)mX, (int)mY, i, j, 0, 1000, 0, 1000);
    }

    public float getAngle()
    {
        return mAngle;
    }

    public float getCurrentX()
    {
        return mX;
    }

    public float getCurrentY()
    {
        return mY;
    }

    public float getScale()
    {
        return mScale;
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        mRotationDetector.onTouchEvent(motionevent);
        mScaleDetector.onTouchEvent(motionevent);
        mGestureDetectorCompat.onTouchEvent(motionevent);
        return true;
    }

    private static final int MAX_ZOOM = 13;
    private static final int MIN_ZOOM = 1;
    private float mAngle;
    private GestureDetectorCompat mGestureDetectorCompat;
    private final SdGestureDetectorListener mListener;
    private RotationGestureDetector mRotationDetector;
    private float mScale;
    private ScaleGestureDetector mScaleDetector;
    private OverScroller mScroller;
    private float mX;
    private float mY;
    private Zoomer mZoomer;


/*
    static float access$002(SdGestureDetector sdgesturedetector, float f)
    {
        sdgesturedetector.mAngle = f;
        return f;
    }

*/




/*
    static float access$202(SdGestureDetector sdgesturedetector, float f)
    {
        sdgesturedetector.mScale = f;
        return f;
    }

*/





/*
    static float access$502(SdGestureDetector sdgesturedetector, float f)
    {
        sdgesturedetector.mX = f;
        return f;
    }

*/



/*
    static float access$602(SdGestureDetector sdgesturedetector, float f)
    {
        sdgesturedetector.mY = f;
        return f;
    }

*/

}
