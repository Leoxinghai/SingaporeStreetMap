// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.core;

import android.text.Html;
import java.util.HashMap;
import streetdirectory.mobile.service.URLFactory;

// Referenced classes of package streetdirectory.mobile.modules.core:
//            LocationBusinessServiceOutput

public class LocationBusinessTipsServiceOutput extends LocationBusinessServiceOutput
{

    public LocationBusinessTipsServiceOutput()
    {
    }

    public LocationBusinessTipsServiceOutput(HashMap hashmap)
    {
        super(hashmap);
    }

    public String generateUserPhotoURL(int i, int j)
    {
        String s = null;
        if(tipsUserImageURL != null && tipsUserImageURL.length() > 2)
        {
            s = URLFactory.createURLResizeImage(tipsUserImageURL, i, j);
        } else
        {
            if(tipsUserID != null)
            {
                if(tipsUserID.length() > 2)
                    return URLFactory.createURLFacebookPhoto(tipsUserID, i, j);
            }
        }
        return s;
    }

    public void populateData()
    {
        populateData();
        try
        {
            tipsUserID = (String)hashData.get("t_uid");
            tipsUserName = (String)hashData.get("t_nm");
            if(tipsUserName != null)
                tipsUserName = Html.fromHtml(tipsUserName).toString();
            tipsText = (String)hashData.get("t_tx");
            if(tipsText != null)
                tipsText = Html.fromHtml(tipsText).toString();
            tipsImageURL = (String)hashData.get("t_img");
            tipsUserImageURL = (String)hashData.get("t_uimg");
            tipsTime = (String)hashData.get("t_tm");
            tipsTimeOrigin = (String)hashData.get("t_tm_org");
            return;
        }
        catch(Exception exception)
        {
            return;
        }
    }

    public static final streetdirectory.mobile.service.SDOutput.Creator CREATOR = new Creator(LocationBusinessTipsServiceOutput.class);
    private static final long serialVersionUID = 0xbfe5feb87cee99adL;
    public String tipsImageURL;
    public String tipsText;
    public String tipsTime;
    public String tipsTimeOrigin;
    public String tipsUserID;
    public String tipsUserImageURL;
    public String tipsUserName;

}
