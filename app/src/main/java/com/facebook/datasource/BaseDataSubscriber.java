

package com.facebook.datasource;


// Referenced classes of package com.facebook.datasource:
//            DataSubscriber, DataSource

public abstract class BaseDataSubscriber
    implements DataSubscriber
{

    public BaseDataSubscriber()
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
        onNewResultImpl(datasource);
        if(datasource.isFinished())
            datasource.close();
        return;
    }

    protected abstract void onNewResultImpl(DataSource datasource);

    public void onProgressUpdate(DataSource datasource)
    {
    }
}
