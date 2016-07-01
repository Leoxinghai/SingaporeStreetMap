// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.service;

import android.os.*;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

// Referenced classes of package streetdirectory.mobile.service:
//            SDDataOutput

public class SDOutput
    implements Serializable, Parcelable
{
    public static class Creator
        implements android.os.Parcelable.Creator
    {

        public SDOutput createFromParcel(Parcel parcel)
        {
            SDOutput sdoutput;
            try
            {
                sdoutput = (SDOutput)tClass.newInstance();
                sdoutput.readParcel(parcel);
            } catch(Exception ex)
            {
                throw new RuntimeException(ex);
            }
            return sdoutput;
        }


        public SDOutput[] newArray(int i)
        {
            return (SDOutput[])(SDOutput[])Array.newInstance(tClass, i);
        }

        private Class tClass;

        public Creator(Class class1)
        {
            tClass = class1;
        }
    }


    public SDOutput()
    {
        hashData = new HashMap();
    }

    public SDOutput(HashMap hashmap)
    {
        hashData = hashmap;
        populateData();
    }

    public int describeContents()
    {
        return 0;
    }

    public void populateData()
    {
    }

    public void readParcel(Parcel parcel)
    {
        Bundle temp = parcel.readBundle();
        hashData = new HashMap();
        String s;
        for(Iterator iterator = temp.keySet().iterator(); iterator.hasNext(); hashData.put(s, temp.getString(s)))
            s = (String)iterator.next();

        populateData();
    }

    public void writeToParcel(Parcel parcel, int i)
    {
        Bundle bundle = new Bundle();
        java.util.Map.Entry entry;
        for(Iterator iterator = hashData.entrySet().iterator(); iterator.hasNext(); bundle.putString((String)entry.getKey(), (String)entry.getValue()))
            entry = (java.util.Map.Entry)iterator.next();

        parcel.writeBundle(bundle);
    }

    public static final Creator CREATOR = new Creator(SDDataOutput.class);
    private static final long serialVersionUID = 0x67132aa47d1a5a1eL;
    public HashMap hashData;

}
