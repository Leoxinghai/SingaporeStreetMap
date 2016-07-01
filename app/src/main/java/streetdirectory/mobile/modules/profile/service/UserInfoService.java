// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.profile.service;

import android.os.Handler;
import streetdirectory.mobile.core.service.*;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.profile.service:
//            UserInfoServiceInput, UserInfoCountryServiceOutput, UserInfoGeneralServiceOutput, UserInfoXMLParserHandler,
//            UserInfoServiceOutput

public class UserInfoService extends HttpService
{

    public UserInfoService(UserInfoServiceInput userinfoserviceinput)
    {
        super(userinfoserviceinput);
        initialize();
    }

    private void initialize()
    {
        _parser = new UserInfoXMLParserHandler() {

            public void onFailed(Exception exception) {

            }

            public void onReceiveData(UserInfoServiceOutput userinfoserviceoutput) {

            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((UserInfoServiceOutput)sddataoutput);
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

    public void onReceiveCountryData(UserInfoCountryServiceOutput userinfocountryserviceoutput)
    {
    }

    public void onReceiveGeneralData(UserInfoGeneralServiceOutput userinfogeneralserviceoutput)
    {
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
            if(data instanceof UserInfoGeneralServiceOutput)
                onReceiveGeneralData((UserInfoGeneralServiceOutput)data);
            else
            if(data instanceof UserInfoCountryServiceOutput)
            {
                onReceiveCountryData((UserInfoCountryServiceOutput)data);
                return;
            }
        }

        final UserInfoServiceOutput data;


            {
                data = UserInfoServiceOutput.this;
                super();
            }
    }

    class _cls3
        implements Runnable
    {

        public void run()
        {
            if(_fld0.SDDatasetErrorFoundException != null)
                _fld0.SDDatasetErrorFoundException.onFailed(new SDDatasetErrorFoundException(error));
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
            if(onSuccess != null)
                onSuccess.onSuccess(output);
        }

        final SDHttpServiceOutput output;


            {
                output = SDHttpServiceOutput.this;
                super();
            }
    }

    class _cls5
        implements Runnable
    {

        public void run()
        {
            if(onAborted != null)
                onAborted.onAborted(exception);
        }

        final Exception exception;


            {
                exception = Exception.this;
                super();
            }
    }


    class _cls6
        implements Runnable {

        public void run() {

        }
    }
    */

}
