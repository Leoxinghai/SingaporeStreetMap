// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.nearby.category.service;

import android.graphics.Bitmap;
import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class NearbyCategoryServiceOutput extends SDDataOutput
{

    public NearbyCategoryServiceOutput()
    {
        type = 1;
        categoryID = 0;
        cellSelected = false;
    }

    public NearbyCategoryServiceOutput(int i, int j, int k, String s)
    {
        type = 1;
        categoryID = 0;
        cellSelected = false;
        categoryType = k;
        hashData.put("t", (new StringBuilder()).append(i).append("").toString());
        hashData.put("cat", (new StringBuilder()).append(j).append("").toString());
        hashData.put("cn", s);
        populateData();
    }

    public NearbyCategoryServiceOutput(int i, String s, int j)
    {
        this(i, j, 1, s);
    }

    public void populateData()
    {
        super.populateData();
        try
        {
            type = Integer.parseInt((String)hashData.get("t"));
        }
        catch(Exception exception)
        {
            type = 1;
        }
        try
        {
            categoryID = Integer.parseInt((String)hashData.get("cat"));
        }
        catch(Exception exception1)
        {
            categoryID = 0;
        }
        fullName = (String)hashData.get("cn");
        name = (String)hashData.get("cns");
        if(name == null)
            name = fullName;
        icon = (String)hashData.get("ico");
        if(categoryID == 93)
        {
            categoryType = 0;
            return;
        } else
        {
            categoryType = 1;
            return;
        }
    }

    public static final streetdirectory.mobile.service.SDOutput.Creator CREATOR = new streetdirectory.mobile.service.SDOutput.Creator(NearbyCategoryServiceOutput.class);
    private static final long serialVersionUID = 0x1cfc8e5f96258980L;
    public int categoryID;
    public int categoryType;
    public boolean cellSelected;
    public String fullName;
    public String icon;
    public Bitmap iconBitmap;
    public String name;
    public int type;

}
