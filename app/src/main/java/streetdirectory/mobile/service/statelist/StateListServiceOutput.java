// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.service.statelist;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class StateListServiceOutput extends SDDataOutput
{

    public StateListServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        try
        {
            stateID = Integer.parseInt((String)hashData.get("stid"));
        }
        catch(Exception exception)
        {
            stateID = 0;
        }
        stateName = (String)hashData.get("stnm");
        try
        {
            isDefaultState = "1".equals(hashData.get("df"));
            return;
        }
        catch(Exception exception1)
        {
            isDefaultState = false;
        }
    }

    public static final streetdirectory.mobile.service.SDOutput.Creator CREATOR = new streetdirectory.mobile.service.SDOutput.Creator(StateListServiceOutput.class);
    private static final long serialVersionUID = 0xc21546007fe43a65L;
    public boolean isDefaultState;
    public int stateID;
    public String stateName;

}
