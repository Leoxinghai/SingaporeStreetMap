

package streetdirectory.mobile.service;

import org.w3c.dom.Document;

// Referenced classes of package streetdirectory.mobile.service:
//            SDXmlServiceOutput

public abstract class SDXmlServiceW3cOutput extends SDXmlServiceOutput
{

    public SDXmlServiceW3cOutput()
    {
    }

    public abstract void populateData();

    public Document xml;
}
