

package streetdirectory.mobile.core.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ApplicationPreferences
{

    public ApplicationPreferences(Context context)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean contain(String s)
    {
        if(s != null)
            return preferences.contains(s);
        else
            return false;
    }

    public android.content.SharedPreferences.Editor createEditor()
    {
        return preferences.edit();
    }

    public boolean getBooleanForKey(String s, boolean flag)
    {
        return preferences.getBoolean(s, flag);
    }

    public float getFloatForKey(String s, float f)
    {
        return preferences.getFloat(s, f);
    }

    public int getIntForKey(String s, int i)
    {
        return preferences.getInt(s, i);
    }

    public long getLongForKey(String s, long l)
    {
        return preferences.getLong(s, l);
    }

    public String getStringForKey(String s, String s1)
    {
        return preferences.getString(s, s1);
    }

    public void removeValueForKey(String s)
    {
        if(s != null)
        {
            android.content.SharedPreferences.Editor editor = preferences.edit();
            editor.remove(s);
            editor.commit();
        }
    }

    public void setValueForKey(String s, float f)
    {
        if(s != null)
        {
            android.content.SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat(s, f);
            editor.commit();
        }
    }

    public void setValueForKey(String s, int i)
    {
        if(s != null)
        {
            android.content.SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(s, i);
            editor.commit();
        }
    }

    public void setValueForKey(String s, long l)
    {
        if(s != null)
        {
            android.content.SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(s, l);
            editor.commit();
        }
    }

    public void setValueForKey(String s, String s1)
    {
        if(s != null && s1 != null)
        {
            android.content.SharedPreferences.Editor editor = preferences.edit();
            editor.putString(s, s1);
            editor.commit();
        }
    }

    public void setValueForKey(String s, boolean flag)
    {
        if(s != null)
        {
            android.content.SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(s, flag);
            editor.commit();
        }
    }

    private static SharedPreferences preferences;
}
