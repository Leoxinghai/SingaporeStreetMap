// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.sdmob;

import android.os.Handler;
import streetdirectory.mobile.core.service.*;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.sdmob:
//            SdMobServiceOutput, SdMobServiceInput, SdMobXMLParserHandler

public class SdMobService extends SDHttpService
{

    public SdMobService(SdMobServiceInput sdmobserviceinput)
    {
        super(sdmobserviceinput, SdMobServiceOutput.class);
        initialize();
    }

    private void initialize()
    {
        _parser = new SdMobXMLParserHandler() {

            public void onFailed(Exception exception)
            {

            }

            public void onReceiveData(SdMobServiceOutput sdmobserviceoutput)
            {

            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((SdMobServiceOutput)sddataoutput);
            }

            public void onReceiveError(SDErrorOutput sderroroutput)
            {

            }

            public void onReceiveTotal(long l)
            {

            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {

            }

        }
;
    }




























/*
    class _cls1
        implements Runnable
    {

        public void run()
        {
            onReceiveTotal(total);
        }

        final long total;


            {
                total = J.this;
                Object();
            }
    }


    class _cls2
        implements Runnable
    {

        public void run()
        {
            onReceiveData(data);
        }

        final SdMobServiceOutput data;


            {
                data = SdMobServiceOutput.this;
                Object();
            }
    }


    class _cls3
        implements Runnable
    {

        public void run()
        {
            if( != null)
                .onFailed(new SDDatasetErrorFoundException(error));
        }

        final SDErrorOutput error;


            {
                error = SDErrorOutput.this;
                Object();
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
*/
}
