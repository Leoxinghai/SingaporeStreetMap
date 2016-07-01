// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.offlinemap.service;

import java.text.SimpleDateFormat;
import java.util.*;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.service.SDDataOutput;

public class OfflineMapListServiceOutput extends SDDataOutput
{

    public OfflineMapListServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        String s;
        try
        {
            packageID = Integer.parseInt((String)hashData.get("id"));
        }
        catch(Exception exception)
        {
            packageID = 0;
        }
        name = (String)hashData.get("name");
        desc = (String)hashData.get("description");
        thumbID = (String)hashData.get("thumb");
        imageID = (String)hashData.get("img");
        s = (String)hashData.get("date");
        if(s != null)
            try
            {
                date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)).parse(s);
            }
            catch(Exception exception1)
            {
                SDLogger.printStackTrace(exception1, "OfflineMapListServiceOutput parse date failed");
            }
        try
        {
            totalSize = Double.parseDouble((String)hashData.get("total_size"));
        }
        catch(Exception exception2)
        {
            totalSize = 0.0D;
        }
        try
        {
            totalTile = Integer.parseInt((String)hashData.get("total_tile"));
        }
        catch(Exception exception3)
        {
            totalTile = 0;
        }
        try
        {
            parentID = Integer.parseInt((String)hashData.get("parent"));
            return;
        }
        catch(Exception exception4)
        {
            parentID = 0;
        }
    }

    public static final streetdirectory.mobile.service.SDOutput.Creator CREATOR = new streetdirectory.mobile.service.SDOutput.Creator(OfflineMapListServiceOutput.class);
    private static final long serialVersionUID = 0xded100404731d85cL;
    public Date date;
    public String desc;
    public String imageID;
    public String name;
    public int packageID;
    public int parentID;
    public String thumbID;
    public double totalSize;
    public int totalTile;

}
