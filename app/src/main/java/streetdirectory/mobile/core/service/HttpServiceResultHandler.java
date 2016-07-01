

package streetdirectory.mobile.core.service;

import java.io.InputStream;

public interface HttpServiceResultHandler
{

    public abstract void abort();

    public abstract Object parse(InputStream inputstream);
}
