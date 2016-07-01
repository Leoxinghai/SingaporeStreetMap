

package com.facebook.datasource;


// Referenced classes of package com.facebook.datasource:
//            DataSubscriber, DataSource

public abstract class BaseBooleanSubscriber
    implements DataSubscriber
{

    public BaseBooleanSubscriber()
    {
    }

    public void onCancellation(DataSource datasource)
    {
    }

    public void onFailure(DataSource datasource)
    {
        onFailureImpl(datasource);
        datasource.close();
        return;
    }

    protected abstract void onFailureImpl(DataSource datasource);

    public void onNewResult(DataSource datasource)
    {
        onNewResultImpl(((Boolean)datasource.getResult()).booleanValue());
        datasource.close();
        return;
    }

    protected abstract void onNewResultImpl(boolean flag);

    public void onProgressUpdate(DataSource datasource)
    {
    }
}
