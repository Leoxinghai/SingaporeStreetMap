// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.util.*;
import org.json.*;

// Referenced classes of package com.facebook:
//            TokenCachingStrategy, LoggingBehavior

public class SharedPreferencesTokenCachingStrategy extends TokenCachingStrategy
{

    public SharedPreferencesTokenCachingStrategy(Context context)
    {
        this(context, null);
    }

    public SharedPreferencesTokenCachingStrategy(Context context, String s)
    {
        Validate.notNull(context, "context");
        String s1 = s;
        if(Utility.isNullOrEmpty(s))
            s1 = "com.facebook.SharedPreferencesTokenCachingStrategy.DEFAULT_KEY";
        cacheKey = s1;
        Context temp = context.getApplicationContext();
        if(temp == null)
            temp = context;
        cache = temp.getSharedPreferences(cacheKey, 0);
    }

    private void deserializeKey(String s, Bundle bundle)
        throws JSONException
    {
        Object obj;
        Object obj1;
        obj = new JSONObject(cache.getString(s, "{}"));
        obj1 = ((JSONObject) (obj)).getString("valueType");
        if(((String) (obj1)).equals("bool")) {
            bundle.putBoolean(s, ((JSONObject) (obj)).getBoolean("value"));
            return;
        }

        if(((String) (obj1)).equals("bool[]"))
        {
            obj = ((JSONObject) (obj)).getJSONArray("value");
            boolean temp1[] = new boolean[((JSONArray) (obj)).length()];
            for(int i = 0; i < temp1.length; i++)
                temp1[i] = ((JSONArray) (obj)).getBoolean(i);

            bundle.putBooleanArray(s, ((boolean []) (temp1)));
            return;
        }
        if(((String) (obj1)).equals("byte"))
        {
            bundle.putByte(s, (byte)((JSONObject) (obj)).getInt("value"));
            return;
        }
        if(((String) (obj1)).equals("byte[]"))
        {
            obj = ((JSONObject) (obj)).getJSONArray("value");
            byte temp2[] = new byte[((JSONArray) (obj)).length()];
            for(int j = 0; j < temp2.length; j++)
                temp2[j] = (byte)((JSONArray) (obj)).getInt(j);

            bundle.putByteArray(s, ((byte []) (temp2)));
            return;
        }
        if(((String) (obj1)).equals("short"))
        {
            bundle.putShort(s, (short)((JSONObject) (obj)).getInt("value"));
            return;
        }
        if(((String) (obj1)).equals("short[]"))
        {
            obj = ((JSONObject) (obj)).getJSONArray("value");
            short temp3[] = new short[((JSONArray) (obj)).length()];
            for(int k = 0; k < temp3.length; k++)
                temp3[k] = (short)((JSONArray) (obj)).getInt(k);

            bundle.putShortArray(s, ((short []) (obj1)));
            return;
        }
        if(((String) (obj1)).equals("int"))
        {
            bundle.putInt(s, ((JSONObject) (obj)).getInt("value"));
            return;
        }
        if(((String) (obj1)).equals("int[]"))
        {
            obj = ((JSONObject) (obj)).getJSONArray("value");
            int temp4[] = new int[((JSONArray) (obj)).length()];
            for(int l = 0; l < temp4.length; l++)
                temp4[l] = ((JSONArray) (obj)).getInt(l);

            bundle.putIntArray(s, ((int []) (temp4)));
            return;
        }
        if(((String) (obj1)).equals("long"))
        {
            bundle.putLong(s, ((JSONObject) (obj)).getLong("value"));
            return;
        }
        if(((String) (obj1)).equals("long[]"))
        {
            obj = ((JSONObject) (obj)).getJSONArray("value");
            long temp5[] = new long[((JSONArray) (obj)).length()];
            for(int i1 = 0; i1 < temp5.length; i1++)
                temp5[i1] = ((JSONArray) (obj)).getLong(i1);

            bundle.putLongArray(s, ((long []) (temp5)));
            return;
        }
        if(((String) (obj1)).equals("float"))
        {
            bundle.putFloat(s, (float)((JSONObject) (obj)).getDouble("value"));
            return;
        }
        if(((String) (obj1)).equals("float[]"))
        {
            obj = ((JSONObject) (obj)).getJSONArray("value");
            float temp6[] = new float[((JSONArray) (obj)).length()];
            for(int j1 = 0; j1 < temp6.length; j1++)
                temp6[j1] = (float)((JSONArray) (obj)).getDouble(j1);

            bundle.putFloatArray(s, ((float []) (temp6)));
            return;
        }
        if(((String) (obj1)).equals("double"))
        {
            bundle.putDouble(s, ((JSONObject) (obj)).getDouble("value"));
            return;
        }
        if(((String) (obj1)).equals("double[]"))
        {
            obj = ((JSONObject) (obj)).getJSONArray("value");
            double temp7[] = new double[((JSONArray) (obj)).length()];
            for(int k1 = 0; k1 < temp7.length; k1++)
                temp7[k1] = ((JSONArray) (obj)).getDouble(k1);

            bundle.putDoubleArray(s, ((double []) (temp7)));
            return;
        }
        if(((String) (obj1)).equals("char")) {
            obj = ((JSONObject) (obj)).getString("value");
            if (obj != null && ((String) (obj)).length() == 1) {
                bundle.putChar(s, ((String) (obj)).charAt(0));
                return;
            }
        } else {
            if (((String) (obj1)).equals("char[]")) {
                obj = ((JSONObject) (obj)).getJSONArray("value");
                char temp8[] = new char[((JSONArray) (obj)).length()];
                for (int l1 = 0; l1 < temp8.length; l1++) {
                    String s1 = ((JSONArray) (obj)).getString(l1);
                    if (s1 != null && s1.length() == 1)
                        temp8[l1] = s1.charAt(0);
                }

                bundle.putCharArray(s, ((char[]) (temp8)));
                return;
            }
            if (((String) (obj1)).equals("string")) {
                bundle.putString(s, ((JSONObject) (obj)).getString("value"));
                return;
            }
            if (((String) (obj1)).equals("stringList")) {
                obj1 = ((JSONObject) (obj)).getJSONArray("value");
                int j2 = ((JSONArray) (obj1)).length();
                ArrayList arraylist = new ArrayList(j2);
                int i2 = 0;
                while (i2 < j2) {
                    obj = ((JSONArray) (obj1)).get(i2);
                    if (obj == JSONObject.NULL)
                        obj = null;
                    else
                        obj = (String) obj;
                    arraylist.add(i2, obj);
                    i2++;
                }
                bundle.putStringArrayList(s, arraylist);
                return;
            }
            if (((String) (obj1)).equals("enum"))
                try {
                    //bundle.putSerializable(s, Enum.valueOf(Class.forName(((JSONObject) (obj)).getString("enumType")), ((JSONObject) (obj)).getString("value")));
                    //bundle.putSerializable(s, Class.forName(((JSONObject) (obj)).getString("enumType")), ((JSONObject) (obj)).getString("value"));
                    return;
                }
                catch (Exception ex) {
                    return;
                }
        }
    }

    private void serializeKey(String s, Bundle bundle, android.content.SharedPreferences.Editor editor)
        throws JSONException
    {
        Object obj1 = bundle.get(s);

        JSONArray jsonarray;
        String s1;
        int i;
        int j;
        jsonarray = new JSONArray();
        if(obj1 == null)
            return;

        Object obj;
        JSONObject jsonobject;
        String tempkey = null;
        obj = null;
        jsonobject = new JSONObject();
        if(obj1 instanceof Byte) {
            tempkey = "byte";
            jsonobject.put("value", ((Byte) obj1).intValue());
        } else if(obj1 instanceof Short)
        {
            tempkey = "short";
            jsonobject.put("value", ((Short)obj1).intValue());
        } else if(obj1 instanceof Integer)
        {
            tempkey = "int";
            jsonobject.put("value", ((Integer)obj1).intValue());
        } else if(obj1 instanceof Long)
        {
            tempkey = "long";
            jsonobject.put("value", ((Long)obj1).longValue());
        } else if(obj1 instanceof Float)
        {
            tempkey = "float";
            jsonobject.put("value", ((Float)obj1).doubleValue());
        } else if(obj1 instanceof Double)
        {
            tempkey = "double";
            jsonobject.put("value", ((Double)obj1).doubleValue());
        } else if(obj1 instanceof Boolean)
        {
            tempkey = "bool";
            jsonobject.put("value", ((Boolean)obj1).booleanValue());
        } else if(obj1 instanceof Character)
        {
            tempkey = "char";
            jsonobject.put("value", obj1.toString());
        } else if(obj1 instanceof String)
        {
            tempkey = "string";
            jsonobject.put("value", (String)obj1);
        } else if(obj1 instanceof Enum) {
            tempkey = "enum";
            jsonobject.put("value", obj1.toString());
            jsonobject.put("enumType", obj1.getClass().getName());
        } else if(obj1 instanceof byte[])
        {
            tempkey = "byte[]";
            obj1 = (byte[]) (byte[]) obj1;
            j = ((byte[])obj1).length;
            i = 0;
            obj = jsonarray;

            for (; i < j; ) {
                jsonarray.put(((byte[])obj1)[i]);
                i++;
            }
        } else if((obj1 instanceof short[]))
        {
            tempkey = "short[]";
            obj1 = (short[]) (short[]) obj1;
            j = ((short[])obj1).length;
            i = 0;
            for (; i < j; ) {
                jsonarray.put(((short[])obj1)[i]);
                i++;
            }
        } else if(obj1 instanceof int[]) {
            tempkey = "int[]";
            obj1 = (int[]) (int[]) obj1;
            j = ((int[])obj1).length;
            i = 0;
            for (; i < j; ) {
                jsonarray.put(((int[])obj1)[i]);
                i++;
            }
        } else if(obj1 instanceof long[]) {
            tempkey = "long[]";
            obj1 = (long[]) (long[]) obj1;
            j = ((long[])obj1).length;
            i = 0;
            for (; i < j; ) {
                jsonarray.put(((long[])obj1)[i]);
                i++;
            }
        } else if(obj1 instanceof float[]) {
            tempkey = "float[]";
            obj1 = (float[]) (float[]) obj1;
            j = ((float[])obj1).length;
            i = 0;
            for (; i < j; ) {
                jsonarray.put(((float[])obj1)[i]);
                i++;
            }
        } else if(obj1 instanceof double[]) {
            tempkey = "double[]";
            obj1 = (double[]) (double[]) obj1;
            j = ((double[])obj1).length;
            i = 0;
            for (; i < j; ) {
                jsonarray.put(((double[])obj1)[i]);
                i++;
            }
        } else if(obj1 instanceof boolean[]) {
            tempkey = "bool[]";
            obj1 = (boolean[]) (boolean[]) obj1;
            j = ((boolean[])obj1).length;
            i = 0;
            for (; i < j; ) {
                jsonarray.put(((boolean[])obj1)[i]);
                i++;
            }
        } else if(obj1 instanceof char[]) {
            tempkey = "char[]";
            obj1 = (char[]) (char[]) obj1;
            j = ((char[])obj1).length;
            i = 0;
            for (; i < j; ) {
                jsonarray.put(String.valueOf(((char[])obj1)[i]));
                i++;
            }
        } else if(obj1 instanceof List) {
            tempkey = "stringList";
            obj1 = ((List) obj1).iterator();
            for (; ((Iterator) (obj1)).hasNext(); ) {
                obj = (String) ((Iterator) (obj1)).next();
                bundle = ((Bundle) (obj));
                if (obj == null)
                    bundle = ((Bundle) (JSONObject.NULL));
                jsonarray.put(bundle);
            }
        }

        obj = null;
        if(tempkey != null)
        {
            jsonobject.put("valueType", tempkey);
            if(obj != null)
                jsonobject.putOpt("value", obj);
            editor.putString(s, jsonobject.toString());
            return;
        }
    }

    public void clear()
    {
        cache.edit().clear().apply();
    }

    public Bundle load()
    {
        Bundle bundle;
        Iterator iterator;
        bundle = new Bundle();
        Object obj = null;
        try {
            iterator = cache.getAll().keySet().iterator();

            //obj = bundle;
            for (; iterator.hasNext(); ) {
                obj = (String) iterator.next();
                deserializeKey(((String) (obj)), bundle);
            }
        } catch(JSONException jsonexception) {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, (new StringBuilder()).append("Error reading cached value for key: '").append(((String) (obj))).append("' -- ").append(jsonexception).toString());
            obj = null;
        }
        return ((Bundle) (obj));
    }

    public void save(Bundle bundle)
    {
        Validate.notNull(bundle, "bundle");
        android.content.SharedPreferences.Editor editor = cache.edit();
        Iterator iterator = bundle.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            try
            {
                serializeKey(s, bundle, editor);
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                Logger.log(LoggingBehavior.CACHE, 5, TAG, (new StringBuilder()).append("Error processing value for key: '").append(s).append("' -- ").append(bundle).toString());
                return;
            }
        } while(true);
        editor.apply();
    }

    private static final String DEFAULT_CACHE_KEY = "com.facebook.SharedPreferencesTokenCachingStrategy.DEFAULT_KEY";
    private static final String JSON_VALUE = "value";
    private static final String JSON_VALUE_ENUM_TYPE = "enumType";
    private static final String JSON_VALUE_TYPE = "valueType";
    private static final String TAG = SharedPreferencesTokenCachingStrategy.class.getSimpleName();
    private static final String TYPE_BOOLEAN = "bool";
    private static final String TYPE_BOOLEAN_ARRAY = "bool[]";
    private static final String TYPE_BYTE = "byte";
    private static final String TYPE_BYTE_ARRAY = "byte[]";
    private static final String TYPE_CHAR = "char";
    private static final String TYPE_CHAR_ARRAY = "char[]";
    private static final String TYPE_DOUBLE = "double";
    private static final String TYPE_DOUBLE_ARRAY = "double[]";
    private static final String TYPE_ENUM = "enum";
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_FLOAT_ARRAY = "float[]";
    private static final String TYPE_INTEGER = "int";
    private static final String TYPE_INTEGER_ARRAY = "int[]";
    private static final String TYPE_LONG = "long";
    private static final String TYPE_LONG_ARRAY = "long[]";
    private static final String TYPE_SHORT = "short";
    private static final String TYPE_SHORT_ARRAY = "short[]";
    private static final String TYPE_STRING = "string";
    private static final String TYPE_STRING_LIST = "stringList";
    private SharedPreferences cache;
    private String cacheKey;

}
