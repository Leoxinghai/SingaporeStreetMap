

package streetdirectory.mobile.gis.maps;

import android.content.Context;
import android.hardware.*;
import android.util.Log;

public class Compass
    implements SensorEventListener
{

    public Compass(Context context)
    {
        mSensorManager = null;
        gsensor = null;
        msensor = null;
        mIsEnabled = false;
        mGData = new float[3];
        mMData = new float[3];
        mR = new float[16];
        mI = new float[16];
        mOrientation = new float[3];
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        gsensor = mSensorManager.getDefaultSensor(1);
        msensor = mSensorManager.getDefaultSensor(2);
    }

    public void disable()
    {
        mSensorManager.unregisterListener(this);
        mIsEnabled = false;
    }

    public void enable()
    {
        mSensorManager.registerListener(this, gsensor, 1);
        mSensorManager.registerListener(this, msensor, 1);
        mIsEnabled = true;
    }

    public float getPitch()
    {
        System.out.println("getPitch." + mOrientation[1] * 57.29578F);
        return mOrientation[1] * 57.29578F;
    }

    public float getRoll()
    {
        return mOrientation[2] * 57.29578F;
    }

    public float getYaw()
    {
        System.out.println("getYaw." + mOrientation[0] * 57.29578F);
        return mOrientation[0] * 57.29578F;
    }

    public boolean isEnabled()
    {
        return mIsEnabled;
    }

    public void onAccuracyChanged(Sensor sensor, int i)
    {
    }

    public void onSensorChanged(SensorEvent sensorevent)
    {
        int i = sensorevent.sensor.getType();
        float af[];
        if(i == 1)
        {
            af = mGData;
        } else if(i == 2) {
            af = mMData;
        } else
            return;
        for(i = 0; i < 3; i++)
            af[i] = sensorevent.values[i];

        SensorManager.getRotationMatrix(mR, mI, mGData, mMData);
        SensorManager.getOrientation(mR, mOrientation);
        float f = SensorManager.getInclination(mI);

        mCount++;
        if(mCount > 50)
        {
            mCount = 0;
            System.out.println("Compass.SensorChanged."+(new StringBuilder()).append("yaw: ").append((int)(mOrientation[0] * 57.29578F)).append("  pitch: ").append((int)(mOrientation[1] * 57.29578F)).append("  roll: ").append((int)(mOrientation[2] * 57.29578F)).append("  incl: ").append((int)(f * 57.29578F)).toString());

            Log.d("Compass", (new StringBuilder()).append("yaw: ").append((int)(mOrientation[0] * 57.29578F)).append("  pitch: ").append((int)(mOrientation[1] * 57.29578F)).append("  roll: ").append((int)(mOrientation[2] * 57.29578F)).append("  incl: ").append((int)(f * 57.29578F)).toString());
        }
    }

    Sensor gsensor;
    private int mCount;
    private float mGData[];
    private float mI[];
    private boolean mIsEnabled;
    private float mMData[];
    private float mOrientation[];
    private float mR[];
    private SensorManager mSensorManager;
    Sensor msensor;
    final float rad2deg = 57.29578F;
}
