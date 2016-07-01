

package com.facebook.common.util;


public enum TriState
{
    YES("YES", 0),
    NO("NO", 1),
    UNSET("UNSET", 2);

    private TriState(String s, int i)
    {

    }

    public static TriState fromDbValue(int i)
    {
        switch(i)
        {
        default:
            return UNSET;

        case 1: // '\001'
            return YES;

        case 2: // '\002'
            return NO;
        }
    }

    public static TriState valueOf(Boolean boolean1)
    {
        if(boolean1 != null)
            return valueOf(boolean1.booleanValue());
        else
            return UNSET;
    }


    public static TriState valueOf(boolean flag)
    {
        if(flag)
            return YES;
        else
            return NO;
    }


    public boolean asBoolean()
    {
        switch(ordinal())
        {
        default:
            throw new IllegalStateException((new StringBuilder()).append("Unrecognized TriState value: ").append(this).toString());

        case 1: // '\001'
            return true;

        case 2: // '\002'
            return false;

        case 3: // '\003'
            throw new IllegalStateException("No boolean equivalent for UNSET");
        }
    }

    public boolean asBoolean(boolean flag)
    {
        switch(ordinal())
        {
        default:
            throw new IllegalStateException((new StringBuilder()).append("Unrecognized TriState value: ").append(this).toString());

        case 1: // '\001'
            flag = true;
            // fall through

        case 3: // '\003'
            return flag;

        case 2: // '\002'
            return false;
        }
    }

    public Boolean asBooleanObject()
    {
        switch(ordinal())
        {
        default:
            throw new IllegalStateException((new StringBuilder()).append("Unrecognized TriState value: ").append(this).toString());

        case 1: // '\001'
            return Boolean.TRUE;

        case 2: // '\002'
            return Boolean.FALSE;

        case 3: // '\003'
            return null;
        }
    }

    public int getDbValue()
    {
        switch(ordinal())
        {
        default:
            return 3;

        case 1: // '\001'
            return 1;

        case 2: // '\002'
            return 2;
        }
    }

    public boolean isSet()
    {
        return this != UNSET;
    }


}
