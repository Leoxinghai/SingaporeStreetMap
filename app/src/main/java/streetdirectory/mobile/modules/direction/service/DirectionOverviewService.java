// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.direction.service;

import android.os.Handler;
import streetdirectory.mobile.core.service.*;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.direction.service:
//            DirectionOverviewServiceOutput, DirectionOverviewServiceInput, DirectionOverviewXMLParserHandler

public class DirectionOverviewService extends SDHttpService
{

    public DirectionOverviewService(DirectionOverviewServiceInput directionoverviewserviceinput)
    {
        super(directionoverviewserviceinput, DirectionOverviewServiceOutput.class);
        initialize();
    }

    private void initialize()
    {
        _parser = new DirectionOverviewXMLParserHandler() {

            public void onFailed(Exception exception)
            {

            }

            public void onReceiveData(DirectionOverviewServiceOutput directionoverviewserviceoutput)
            {

            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((DirectionOverviewServiceOutput)sddataoutput);
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
        };
    }

    public void onReceiveTotal(long l)
    {
    }

    /*

    class _cls1
        implements Runnable
    {

        public void run()
        {
            onReceiveTotal(total);
        }

    }


    class _cls2
        implements Runnable
    {

        public void run()
        {
            onReceiveData(data);
        }

    }


    class _cls3
        implements Runnable
    {

        public void run()
        {
                onFailed(new SDDatasetErrorFoundException(error));
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
