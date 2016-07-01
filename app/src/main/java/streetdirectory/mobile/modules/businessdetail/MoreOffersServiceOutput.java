// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessdetail;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class MoreOffersServiceOutput extends SDDataOutput
{

    public MoreOffersServiceOutput()
    {
    }

    public MoreOffersServiceOutput(HashMap hashmap)
    {
        super(hashmap);
    }

    public void populateData()
    {
        super.populateData();
        offerId = (String)hashData.get("offer_id");
        offerImage = (String)hashData.get("offer_img");
    }

    public static final streetdirectory.mobile.service.SDOutput.Creator CREATOR = new streetdirectory.mobile.service.SDOutput.Creator(MoreOffersServiceOutput.class);
    private static final long serialVersionUID = 0x6cd29e641f6806f8L;
    public String offerId;
    public String offerImage;

}
