// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.erp.service;

import android.os.Handler;
import streetdirectory.mobile.core.service.*;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.erp.service:
//            ERPDetailServiceOutput, ERPDetailServiceInput, ERPDetailXMLParserHandler

public class ERPDetailService extends SDHttpService
{

    public ERPDetailService(ERPDetailServiceInput erpdetailserviceinput)
    {
        super(erpdetailserviceinput, ERPDetailServiceOutput.class);
        initialize();
    }

    private void initialize()
    {
        _parser = new ERPDetailXMLParserHandler() {

            public void onFailed(Exception exception)
            {

            }

            public void onReceiveData(ERPDetailServiceOutput erpdetailserviceoutput)
            {

            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((ERPDetailServiceOutput)sddataoutput);
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

        final long total;


            {
                total = J.this;
                super();
            }
    }


    class _cls2
        implements Runnable
    {

        public void run()
        {
            onReceiveData(data);
        }

        final ERPDetailServiceOutput data;


		{
			data = ERPDetailServiceOutput.this;
			super();
		}
    }

    class _cls3
        implements Runnable
    {

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
