

package streetdirectory.mobile.service;

import streetdirectory.mobile.core.service.HttpService;

// Referenced classes of package streetdirectory.mobile.service:
//            SDXmlServiceW3cOutput, SDHttpServiceInput

public abstract class SDXmlService extends HttpService
{

    public SDXmlService(Class class1)
    {
        setInput(new SDHttpServiceInput() {

            public String getURL()
            {
                return createUrl();
            }
        });

        if(!class1.isAssignableFrom(SDXmlServiceW3cOutput.class));
    }

    protected abstract String createUrl();
}
