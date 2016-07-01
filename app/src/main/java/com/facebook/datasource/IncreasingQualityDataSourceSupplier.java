// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.datasource;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.facebook.datasource:
//            DataSource, AbstractDataSource, DataSubscriber

public class IncreasingQualityDataSourceSupplier
    implements Supplier
{
    private class IncreasingQualityDataSource extends AbstractDataSource
    {

        private void closeSafely(DataSource datasource)
        {
            if(datasource != null)
                datasource.close();
        }

        private DataSource getAndClearDataSource(int i)
        {
            Object obj = null;
            DataSource datasource = null;
            if(mDataSources != null) {
                if (i < mDataSources.size())
                    datasource = (DataSource) mDataSources.set(i, null);
            }
            return datasource;
        }

        private DataSource getDataSource(int i)
        {
            DataSource datasource;
            if(mDataSources == null || i >= mDataSources.size())
	            datasource = null;
	        else
            	datasource = (DataSource)mDataSources.get(i);
            return datasource;
        }

        private DataSource getDataSourceWithResult()
        {
            DataSource datasource = getDataSource(mIndexOfDataSourceWithResult);

            return datasource;
        }

        private void maybeSetIndexOfDataSourceWithResult(int i, DataSource datasource, boolean flag)
        {
            int k;
            int l;
            k = mIndexOfDataSourceWithResult;
            l = mIndexOfDataSourceWithResult;
            if(datasource == getDataSource(i) && i != mIndexOfDataSourceWithResult) {
                if(getDataSourceWithResult() == null)
                    return;
                int j;
                j = l;
                if(flag && i < mIndexOfDataSourceWithResult) {
                    j = i;
                    mIndexOfDataSourceWithResult = i;
                }

                for(i = k; i > j; i--)
                    closeSafely(getAndClearDataSource(i));

            }
            return;


        }

        private void onDataSourceFailed(int i, DataSource datasource)
        {
            closeSafely(tryGetAndClearDataSource(i, datasource));
            if(i == 0)
                setFailure(datasource.getFailureCause());
        }

        private void onDataSourceNewResult(int i, DataSource datasource)
        {
            maybeSetIndexOfDataSourceWithResult(i, datasource, datasource.isFinished());
            if(datasource == getDataSourceWithResult())
            {
                boolean flag;
                if(i == 0 && datasource.isFinished())
                    flag = true;
                else
                    flag = false;
                setResult(null, flag);
            }
        }

        private DataSource tryGetAndClearDataSource(int i, DataSource datasource)
        {
            DataSource datasource1 = getDataSourceWithResult();
            if(datasource != datasource1) {
				datasource1 = datasource;
				if(datasource == getDataSource(i))
					datasource1 = getAndClearDataSource(i);
			} else
            	datasource1 = null;

            return datasource1;
        }

        public boolean close()
        {
            if(super.close()) {
                ArrayList arraylist;
                arraylist = mDataSources;
                mDataSources = null;

                if(arraylist != null)
                {
                    for(int i = 0; i < arraylist.size(); i++)
                        closeSafely((DataSource)arraylist.get(i));

                }
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
            if(datasource != null) {
	            flag = datasource.hasResult();
            	if(flag)
		            flag = true;
			}

            return flag;
        }

        private ArrayList mDataSources;
        private int mIndexOfDataSourceWithResult;



        public IncreasingQualityDataSource()
        {
            super();
            int j = mDataSourceSuppliers.size();
            mIndexOfDataSourceWithResult = j;
            mDataSources = new ArrayList(j);
            int i = 0;
            for(;i < j;)
            {
                DataSource datasource = (DataSource)((Supplier)mDataSourceSuppliers.get(i)).get();
                mDataSources.add(datasource);
                datasource.subscribe(new InternalDataSubscriber(i), CallerThreadExecutor.getInstance());
                if(datasource.hasResult())
                    return;
                i++;
            }
        }

        private class InternalDataSubscriber
        implements DataSubscriber
        {

            public void onCancellation(DataSource datasource)
            {
            }

        public void onFailure(DataSource datasource)
        {
            onDataSourceFailed(mIndex, datasource);
        }

        public void onNewResult(DataSource datasource)
        {
            if(datasource.hasResult())
                onDataSourceNewResult(mIndex, datasource);
            else
            if(datasource.isFinished())
            {
                onDataSourceFailed(mIndex, datasource);
                return;
            }
        }

        public void onProgressUpdate(DataSource datasource)
        {
            if(mIndex == 0)
                setProgress(datasource.getProgress());
        }

        private int mIndex;

        public InternalDataSubscriber(int i)
        {
            super();
            mIndex = i;
        }
    }

    }



    private IncreasingQualityDataSourceSupplier(List list)
    {
        boolean flag;
        if(!list.isEmpty())
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag, "List of suppliers is empty!");
        mDataSourceSuppliers = list;
    }

    public static IncreasingQualityDataSourceSupplier create(List list)
    {
        return new IncreasingQualityDataSourceSupplier(list);
    }

    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;
        if(!(obj instanceof IncreasingQualityDataSourceSupplier))
        {
            return false;
        } else
        {
            obj = (IncreasingQualityDataSourceSupplier)obj;
            return Objects.equal(mDataSourceSuppliers, ((IncreasingQualityDataSourceSupplier) (obj)).mDataSourceSuppliers);
        }
    }

    public DataSource get()
    {
        return new IncreasingQualityDataSource();
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
