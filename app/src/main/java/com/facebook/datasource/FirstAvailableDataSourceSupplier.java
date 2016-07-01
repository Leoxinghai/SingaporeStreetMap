// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.datasource;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.*;
import java.util.List;

// Referenced classes of package com.facebook.datasource:
//            DataSource, AbstractDataSource, DataSubscriber

public class FirstAvailableDataSourceSupplier
    implements Supplier
{
    private class FirstAvailableDataSource extends AbstractDataSource
    {

        private boolean clearCurrentDataSource(DataSource datasource)
        {
            boolean flag = false;
            if(!isClosed()) {
				DataSource datasource1 = mCurrentDataSource;
				if(datasource == datasource1) {
					mCurrentDataSource = null;
					flag = true;
				}
			}
            return flag;
        }

        private void closeSafely(DataSource datasource)
        {
            if(datasource != null)
                datasource.close();
        }

        private DataSource getDataSourceWithResult()
        {
            DataSource datasource = mDataSourceWithResult;
            return datasource;
        }

        private Supplier getNextSupplier()
        {
            Object obj;
            if(isClosed() || mIndex >= mDataSourceSuppliers.size()) {
                obj = null;
            } else {
                obj = mDataSourceSuppliers;
                int i = mIndex;
                mIndex = i + 1;
                obj = (Supplier) ((List) (obj)).get(i);
            }
            return ((Supplier) (obj));
        }

        private void maybeSetDataSourceWithResult(DataSource datasource, boolean flag)
        {
            DataSource datasource1 = null;
            if(datasource == mCurrentDataSource && datasource != mDataSourceWithResult) {
                if(mDataSourceWithResult != null && !flag)
                    return;
                datasource1 = mDataSourceWithResult;
                mDataSourceWithResult = datasource;
                closeSafely(datasource1);
                return;

            }
            return;
        }

        private void onDataSourceFailed(DataSource datasource)
        {
            if(clearCurrentDataSource(datasource))
            {
                if(datasource != getDataSourceWithResult())
                    closeSafely(datasource);
                if(!startNextDataSource())
                {
                    setFailure(datasource.getFailureCause());
                    return;
                }
            }
        }

        private void onDataSourceNewResult(DataSource datasource)
        {
            maybeSetDataSourceWithResult(datasource, datasource.isFinished());
            if(datasource == getDataSourceWithResult())
                setResult(null, datasource.isFinished());
        }

        private boolean setCurrentDataSource(DataSource datasource)
        {
            boolean flag = isClosed();
            if(!flag) {
                mCurrentDataSource = datasource;
                flag = true;
            } else
                flag = false;

            return flag;
        }

        private boolean startNextDataSource()
        {
            Object obj = getNextSupplier();
            if(obj != null)
                obj = (DataSource)((Supplier) (obj)).get();
            else
                obj = null;
            if(setCurrentDataSource(((DataSource) (obj))) && obj != null)
            {
                ((DataSource) (obj)).subscribe(new InternalDataSubscriber(), CallerThreadExecutor.getInstance());
                return true;
            } else
            {
                closeSafely(((DataSource) (obj)));
                return false;
            }
        }

        public boolean close()
        {
            if(super.close()) {
                DataSource datasource;
                DataSource datasource1;
                datasource = mCurrentDataSource;
                mCurrentDataSource = null;
                datasource1 = mDataSourceWithResult;
                mDataSourceWithResult = null;
                closeSafely(datasource1);
                closeSafely(datasource);
                return true;
            }
            return false;
        }

        public Object getResult()
        {
            Object obj = getDataSourceWithResult();
            if(obj == null)
                obj = null;
            else
                obj = ((DataSource) (obj)).getResult();
            return obj;

        }

        public boolean hasResult()
        {
            DataSource datasource = getDataSourceWithResult();
            boolean flag = false;
            if(datasource == null) {
                if(!flag) {
                    flag = false;
                } else
                    flag = true;


            } else
                flag = datasource.hasResult();
            return flag;
        }

        private DataSource mCurrentDataSource;
        private DataSource mDataSourceWithResult;
        private int mIndex;



        public FirstAvailableDataSource()
        {
            super();
            mIndex = 0;
            mCurrentDataSource = null;
            mDataSourceWithResult = null;
            if(!startNextDataSource())
                setFailure(new RuntimeException("No data source supplier or supplier returned null."));
        }


        private class InternalDataSubscriber
        implements DataSubscriber
        {

            public void onCancellation(DataSource datasource)
            {
            }

        public void onFailure(DataSource datasource)
        {
            onDataSourceFailed(datasource);
        }

        public void onNewResult(DataSource datasource)
        {
            if(datasource.hasResult())
                onDataSourceNewResult(datasource);
            else
            if(datasource.isFinished())
            {
                onDataSourceFailed(datasource);
                return;
            }
        }

        public void onProgressUpdate(DataSource datasource)
        {
            float f = getProgress();
            setProgress(Math.max(f, datasource.getProgress()));
        }


        private InternalDataSubscriber()
        {
            super();
        }

    }

    }



    private FirstAvailableDataSourceSupplier(List list)
    {
        boolean flag;
        if(!list.isEmpty())
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag, "List of suppliers is empty!");
        mDataSourceSuppliers = list;
    }

    public static FirstAvailableDataSourceSupplier create(List list)
    {
        return new FirstAvailableDataSourceSupplier(list);
    }

    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;
        if(!(obj instanceof FirstAvailableDataSourceSupplier))
        {
            return false;
        } else
        {
            obj = (FirstAvailableDataSourceSupplier)obj;
            return Objects.equal(mDataSourceSuppliers, ((FirstAvailableDataSourceSupplier) (obj)).mDataSourceSuppliers);
        }
    }

    public DataSource get()
    {
        return new FirstAvailableDataSource();
    }


    public int hashCode()
    {
        return mDataSourceSuppliers.hashCode();
    }

    public String toString()
    {
        return Objects.toStringHelper(this).add("list", mDataSourceSuppliers).toString();
    }

    private final List mDataSourceSuppliers;

}
