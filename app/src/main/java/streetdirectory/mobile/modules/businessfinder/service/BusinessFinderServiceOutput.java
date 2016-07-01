

package streetdirectory.mobile.modules.businessfinder.service;

import android.text.Html;
import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class BusinessFinderServiceOutput extends SDDataOutput
{

    public BusinessFinderServiceOutput()
    {
    }

    public BusinessFinderServiceOutput(int i, String s)
    {
        hashData.put("mid", (new StringBuilder()).append(i).append("").toString());
        hashData.put("cn", s);
        populateData();
    }

    public void generateIcon(int i)
    {
        icon = (new StringBuilder()).append("bf_").append(i).append("_").append(menuID).toString();
    }

    public void generateIconURL(int i)
    {
        iconURL = (new StringBuilder()).append(i).append("-").append(menuID).append(".png").toString();
    }

    public void populateData()
    {
        super.populateData();
        try
        {
            menuID = Integer.parseInt((String)hashData.get("mid"));
        }
        catch(Exception exception)
        {
            menuID = 0;
        }
        try
        {
            menuParentID = Integer.parseInt((String)hashData.get("mpid"));
        }
        catch(Exception exception1)
        {
            menuParentID = 0;
        }
        try
        {
            categoryID = Integer.parseInt((String)hashData.get("cat"));
        }
        catch(Exception exception2)
        {
            categoryID = 0;
        }
        fullName = (String)hashData.get("cn");
        if(fullName != null)
            fullName = Html.fromHtml(fullName).toString();
        name = (String)hashData.get("cns");
        if(name != null)
            name = Html.fromHtml(name).toString();
        else
            name = fullName;
        try
        {
            type = Integer.parseInt((String)hashData.get("t"));
            return;
        }
        catch(Exception exception3)
        {
            type = 1;
        }
    }

    private static final long serialVersionUID = 0x4aba94ff51db9fdbL;
    public int categoryID;
    public String fullName;
    public String icon;
    public String iconURL;
    public int menuID;
    public int menuParentID;
    public String name;
    public int type;
}
