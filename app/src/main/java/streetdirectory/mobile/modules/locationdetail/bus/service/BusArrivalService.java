// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.bus.service;

import android.os.Handler;
import streetdirectory.mobile.core.service.*;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.bus.service:
//            BusArrivalServiceOutput, BusArrivalServiceInput, BusArrivalXMLParserHandler

public class BusArrivalService extends SDHttpService
{

    public BusArrivalService(BusArrivalServiceInput busarrivalserviceinput)
    {
        super(busarrivalserviceinput, BusArrivalServiceOutput.class);
        initialize();
    }

    private void initialize()
    {
        _parser = new BusArrivalXMLParserHandler() {

            public void onFailed(Exception exception) {
            }

            public void onReceiveData(BusArrivalServiceOutput busarrivalserviceoutput) {
            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((BusArrivalServiceOutput)sddataoutput);
            }

            public void onReceiveError(SDErrorOutput sderroroutput) {
            }

            public void onReceiveTotal(long l) {
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput) {
            }
        };
    }

    /*
    class _cls1
        implements Runnable
    {
        public void run()
        {
            onReceiveTotal(total);
        }

        class _cls2
                implements Runnable
        {

            public void run()
            {
                onReceiveData(data);
            }

            final BusArrivalServiceOutput data;


            {
                data = BusArrivalServiceOutput.this;
                super();
            }
        }


        class _cls3
                implements Runnable
        {

            public void run()
            {
                onFailed(new SDDatasetErrorFoundException(error));
            }

            final SDErrorOutput error;


            {
                error = SDErrorOutput.this;
                super();
            }
        }


        class _cls4
                implements Runnable
        {

            public void run()
            {

            }


        }

        class _cls5
                implements Runnable
        {

            public void run()
            {

            }

        }

    }
*/


}
