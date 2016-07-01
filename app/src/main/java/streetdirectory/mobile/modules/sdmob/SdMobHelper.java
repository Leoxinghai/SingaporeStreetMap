

package streetdirectory.mobile.modules.sdmob;

import android.content.Context;
import android.content.SharedPreferences;
import java.lang.annotation.Annotation;
import streetdirectory.mobile.core.StringTools;

public class SdMobHelper
{
    static interface SdMobBannerSize
        extends Annotation
    {
    }

    static interface SdMobType
        extends Annotation
    {
    }

    public static class SdMobUnit
    {

        public static SdMobUnit parse(String s)
        {
            SdMobUnit temp = null;
            try
            {
                String ss[] = s.split(";");
                temp = new SdMobUnit(StringTools.tryParseInt(ss[0], 1), ss[1], ss[2]);
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                return null;
            }
            return temp;
        }

        public String packedString()
        {
            return (new StringBuilder()).append(type).append(";").append(name).append(";").append(id).toString();
        }

        public String id;
        public String name;
        public int type;

        public SdMobUnit(int i, String s, String s1)
        {
            type = i;
            name = s;
            id = s1;
        }
    }


    public SdMobHelper(Context context)
    {
        mPreferences = context.getSharedPreferences("sdmob", 0);
    }

    public static SdMobHelper getInstance(Context context)
    {
        if(mInstance == null)
            mInstance = new SdMobHelper(context);

        return mInstance;
    }

    private void putIntToPreferences(String s, int i)
    {
        mEditor = mPreferences.edit();
        mEditor.putInt(s, i);
        mEditor.apply();
    }

    private void putStringToPreferences(String s, String s1)
    {
        mEditor = mPreferences.edit();
        mEditor.putString(s, s1);
        mEditor.apply();
    }

    public SdMobUnit getSdMobUnit(SdMobUnit sdmobunit)
    {
        SdMobUnit sdmobunit2 = SdMobUnit.parse(mPreferences.getString(sdmobunit.name, ""));
        SdMobUnit sdmobunit1 = sdmobunit2;
        if(sdmobunit2 == null)
            sdmobunit1 = sdmobunit;
        return sdmobunit1;
    }

    public void saveSdMobUnit(SdMobUnit sdmobunit)
    {
        putStringToPreferences(sdmobunit.name, sdmobunit.packedString());
    }

    public static final int ADMOB = 1;
    public static final int FACEBOOK_AD = 2;
    public static final String FIRST_ACTIVITY_BROADCAST_CODE = "first_activity_started";
    public static final String SDMOB_BROADCAST_CODE = "sdmob_broadcast";
    private static SdMobHelper mInstance;
    private android.content.SharedPreferences.Editor mEditor;
    private final SharedPreferences mPreferences;
}
