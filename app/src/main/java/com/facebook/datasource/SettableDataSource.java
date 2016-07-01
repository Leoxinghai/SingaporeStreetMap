

package com.facebook.datasource;

import com.facebook.common.internal.Preconditions;

// Referenced classes of package com.facebook.datasource:
//            AbstractDataSource

public class SettableDataSource extends AbstractDataSource
{

    private SettableDataSource()
    {
    }

    public static SettableDataSource create()
    {
        return new SettableDataSource();
    }

    public boolean setFailure(Throwable throwable)
    {
        return super.setFailure((Throwable)Preconditions.checkNotNull(throwable));
    }

    public boolean setProgress(float f)
    {
        return super.setProgress(f);
    }

    public boolean setResult(Object obj)
    {
        return super.setResult(Preconditions.checkNotNull(obj), true);
    }

    public boolean setResult(Object obj, boolean flag)
    {
        return super.setResult(Preconditions.checkNotNull(obj), flag);
    }
}
