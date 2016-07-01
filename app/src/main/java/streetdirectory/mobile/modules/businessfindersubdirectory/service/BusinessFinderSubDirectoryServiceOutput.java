

package streetdirectory.mobile.modules.businessfindersubdirectory.service;

import android.text.Html;
import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class BusinessFinderSubDirectoryServiceOutput extends SDDataOutput
{

    public BusinessFinderSubDirectoryServiceOutput()
    {
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
        try
        {
            totalBusiness = Integer.parseInt((String)hashData.get("tbiz"));
        }
        catch(Exception exception3)
        {
            totalBusiness = 0;
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
        catch(Exception exception4)
        {
            type = 1;
        }
    }

    private static final long serialVersionUID = 0x3a8a3690e28e4935L;
    public int categoryID;
    public String fullName;
    public int menuID;
    public int menuParentID;
    public String name;
    public int totalBusiness;
    public int type;
}
