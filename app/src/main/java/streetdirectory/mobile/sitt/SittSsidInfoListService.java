// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.sitt;

import android.os.Handler;
import streetdirectory.mobile.core.service.*;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.sitt:
//            SittSsidInfoListServiceOutput, SittSsidInfoListServiceInput, SittSsidInfoXMLParserHandler

public class SittSsidInfoListService extends SDHttpService
{

    public SittSsidInfoListService(SittSsidInfoListServiceInput sittssidinfolistserviceinput)
    {
        super(sittssidinfolistserviceinput, SittSsidInfoListServiceOutput.class);
        initialize();
    }

    private void initialize()
    {
        _parser = new SittSsidInfoXMLParserHandler() {

            public void onFailed(Exception exception) {
                SittSsidInfoListService.this.onFailed(exception);
            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((SittSsidInfoListServiceOutput)sddataoutput);
            }

            public void onReceiveData(SittSsidInfoListServiceOutput sittssidinfolistserviceoutput)
            {
                    SittSsidInfoListService.this.onReceiveData(sittssidinfolistserviceoutput);
            }

            public void onReceiveError(SDErrorOutput sderroroutput)
            {
                SittSsidInfoListService.this.onFailed(new SDDatasetErrorFoundException(sderroroutput));
            }

            public void onReceiveTotal(long l)
            {
                    SittSsidInfoListService.this.onReceiveTotal(l);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                SittSsidInfoListService.this.onSuccess(sdhttpserviceoutput);
            }
        };
    }

    public void onReceiveTotal(long l)
    {
    }

    // Unreferenced inner class streetdirectory/mobile/sitt/SittSsidInfoListService$1$6

/* anonymous class */
}
