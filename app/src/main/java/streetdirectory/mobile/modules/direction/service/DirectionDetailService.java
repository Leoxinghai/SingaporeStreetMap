// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.direction.service;

import android.os.Handler;
import streetdirectory.mobile.core.service.*;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.direction.service:
//            DirectionDetailServiceOutput, DirectionDetailServiceInput, DirectionDetailXMLParserHandler

public class DirectionDetailService extends SDHttpService
{

    public DirectionDetailService(DirectionDetailServiceInput directiondetailserviceinput)
    {
        super(directiondetailserviceinput, DirectionDetailServiceOutput.class);
        initialize();
    }

    private void initialize()
    {
        _parser = new DirectionDetailXMLParserHandler() {

            public void onFailed(Exception exception)
            {

            }


            public void onReceiveData(final DirectionDetailServiceOutput directiondetailserviceoutput)
            {
                    DirectionDetailService.this.onReceiveData(directiondetailserviceoutput);
            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((DirectionDetailServiceOutput)sddataoutput);
            }

            public void onReceiveError(final SDErrorOutput sderroroutput)
            {
                    onFailed(new SDDatasetErrorFoundException(sderroroutput));
            }

            public void onReceiveTotal(final long l)
            {
                    DirectionDetailService.this.onReceiveTotal(l);
                    return;
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {

            }

        };
    }

    public void onReceiveTotal(long l)
    {
    }


}
