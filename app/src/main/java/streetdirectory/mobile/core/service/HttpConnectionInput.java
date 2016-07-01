

package streetdirectory.mobile.core.service;

import java.io.File;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;

import streetdirectory.mobile.core.storage.CacheStorage;

public abstract class HttpConnectionInput
{

    public HttpConnectionInput()
    {
        forceUpdate = false;
    }

    public AuthScope getAuthScope()
    {
        return null;
    }

    public Credentials getCredentials()
    {
        return null;
    }

    public Header[] getHeaders()
    {
        return null;
    }

    public String getMethodType()
    {
        return "get";
    }

    public HttpEntity getRequestEntity()
    {
        return null;
    }

    public File getSaveFile()
    {
        return null;
        /*
        String fileName ="temp";
        if(fileName != null)
            return CacheStorage.getStorageFile((new StringBuilder()).append("images/photo/thumb/").append(fileName).toString());
        else
            return null;
        */
    }

    public abstract String getURL();

    public boolean forceUpdate;
}
