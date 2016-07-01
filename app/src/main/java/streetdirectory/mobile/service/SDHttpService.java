

package streetdirectory.mobile.service;

import android.os.Handler;
import streetdirectory.mobile.core.service.*;

// Referenced classes of package streetdirectory.mobile.service:
//            SDHttpServiceInput, SDDatasetDataXMLHandler, SDDataOutput, SDDatasetErrorFoundException,
//            SDErrorOutput, SDHttpServiceOutput

public abstract class SDHttpService extends HttpService
{

    public SDHttpService(Class class1)
    {
        initialize(class1);
    }

    public SDHttpService(SDHttpServiceInput sdhttpserviceinput, Class class1)
    {
        super(sdhttpserviceinput);
        initialize(class1);
    }

    static class SUBCLASS1 extends SDDatasetDataXMLHandler {

        public void onFailed(Exception exception)
        {
            if(!(exception instanceof HttpConnectionAbortException) && !(exception instanceof SAXParserAbortException)) {
                mService.onFailed(exception);
                return;
            }

            if( mService != null)
            {
                mService.onAborted(exception);
                return;
            }
            return;
        }

        public void onReceiveData(SDDataOutput sddataoutput)
        {
            mService.onReceiveData(sddataoutput);
            return;
        }

        public void onReceiveError(SDErrorOutput sderroroutput)
        {
            mService.onFailed(new SDDatasetErrorFoundException(sderroroutput));
            return;
        }

        public void onReceiveTotal(long l)
        {
            mService.onReceiveTotal(l);
            return;
        }

        public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
        {
            if( mService != null)
            {
                mService.onSuccess(sdhttpserviceoutput);
                return;
            }
        }

        final SDHttpService mService;

        SUBCLASS1(Class class1, SDHttpService lservice)
        {
            super(class1);
            mService = lservice;
        }
    };

    private void initialize(Class class1)
    {
        _parser = new SUBCLASS1(class1, this);
        if((_input instanceof SDHttpServiceInput) && ((SDHttpServiceInput)_input).countryCode != null)
            ((SDDatasetDataXMLHandler)_parser).countryCode = ((SDHttpServiceInput)_input).countryCode;
    }

    public void onReceiveData(SDDataOutput sddataoutput)
    {
    }

    public void onReceiveTotal(long l)
    {
    }

}
