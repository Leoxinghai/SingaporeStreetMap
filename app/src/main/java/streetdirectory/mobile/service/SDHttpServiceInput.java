

package streetdirectory.mobile.service;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.core.service.HttpConnectionInput;

public abstract class SDHttpServiceInput extends HttpConnectionInput
{

    public SDHttpServiceInput()
    {
        this("sg");
    }

    public SDHttpServiceInput(String s)
    {
        apiVersion = SDPreferences.getInstance().getAPIVersion();
        countryCode = s;
    }

    public Header[] getHeaders()
    {
        Object obj = super.getHeaders();
        if(obj == null)
            obj = new ArrayList();
        else
            obj = new ArrayList(Arrays.asList(((Object []) (obj))));
        ((ArrayList) (obj)).add(new BasicHeader("Referer", "http://www.streetdirectory.com/"));
        return (Header[])((ArrayList) (obj)).toArray(new Header[0]);
    }

    public abstract String getURL();

    public int apiVersion;
    public String countryCode;
}
