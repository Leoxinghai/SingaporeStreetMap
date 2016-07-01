

package com.facebook.datasource;


// Referenced classes of package com.facebook.datasource:
//            DataSource

public interface DataSubscriber
{

    public abstract void onCancellation(DataSource datasource);

    public abstract void onFailure(DataSource datasource);

    public abstract void onNewResult(DataSource datasource);

    public abstract void onProgressUpdate(DataSource datasource);
}
