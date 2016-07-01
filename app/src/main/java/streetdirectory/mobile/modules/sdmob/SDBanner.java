

package streetdirectory.mobile.modules.sdmob;

import java.util.Date;

public class SDBanner
{

    public SDBanner(SdMobHelper.SdMobUnit sdmobunit)
    {
        unit = sdmobunit;
        bannerType = sdmobunit.type;
    }

    public int bannerType;
    public Date endTime;
    public Date startTime;
    public SdMobHelper.SdMobUnit unit;
}
