

package streetdirectory.mobile.modules.bottombanner.service;

import android.text.Html;
import java.util.HashMap;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;

public class BottomBannerServiceOutput extends LocationBusinessServiceOutput
{

    public BottomBannerServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        description = (String)hashData.get("ab");
        if(description != null)
            description = Html.fromHtml(description).toString();
        categoryName = (String)hashData.get("cn");
        if(categoryName != null)
            categoryName = Html.fromHtml(categoryName).toString();
        bannerType = (String)hashData.get("bnt");
    }

    private static final long serialVersionUID = 0x89d0ffbb4c53c93bL;
    public String bannerType;
    public String categoryName;
    public String description;
}
