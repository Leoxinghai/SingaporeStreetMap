

package streetdirectory.mobile.core.service;


public interface HttpServiceListener
{

    public abstract void onAborted(Exception exception);

    public abstract void onFailed(Exception exception);

    public abstract void onProgress(int i);

    public abstract void onSuccess(Object obj);
}
