// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.datasource;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Preconditions;
import com.facebook.datasource.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

public class ListDataSource extends AbstractDataSource
{
    private class InternalDataSubscriber
        implements DataSubscriber
    {

        private boolean tryFinish()
        {
            boolean flag = true;
            boolean flag1 = mFinished;
            if(!flag1)
                mFinished = true;
            else
                flag = false;

            return flag;
        }

        public void onCancellation(DataSource datasource)
        {
            onDataSourceCancelled();
        }

        public void onFailure(DataSource datasource)
        {
            onDataSourceFailed(datasource);
        }

        public void onNewResult(DataSource datasource)
        {
            if(datasource.isFinished() && tryFinish())
                onDataSourceFinished();
        }

        public void onProgressUpdate(DataSource datasource)
        {
            onDataSourceProgress();
        }

        boolean mFinished;

        private InternalDataSubscriber()
        {
            mFinished = false;
        }

    }


    protected ListDataSource(DataSource adatasource[])
    {
        mDataSources = adatasource;
        mFinishedDataSources = 0;
    }

    public static ListDataSource create(DataSource adatasource[])
    {
        Preconditions.checkNotNull(adatasource);
        ListDataSource listdatasource;
        int j;
        boolean flag;
        if(adatasource.length > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        listdatasource = new ListDataSource(adatasource);
        j = adatasource.length;
        for(int i = 0; i < j; i++)
        {
            DataSource datasource = adatasource[i];
            listdatasource.getClass();
            datasource.subscribe(listdatasource. new InternalDataSubscriber(), CallerThreadExecutor.getInstance());
        }

        return listdatasource;
    }

    private boolean increaseAndCheckIfLast()
    {
        int i;
        int j;
        i = mFinishedDataSources + 1;
        mFinishedDataSources = i;
        j = mDataSources.length;
        boolean flag;
        if(i == j)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private void onDataSourceCancelled()
    {
        setFailure(new CancellationException());
    }

    private void onDataSourceFailed(DataSource datasource)
    {
        setFailure(datasource.getFailureCause());
    }

    private void onDataSourceFinished()
    {
        if(increaseAndCheckIfLast())
            setResult(null, true);
    }

    private void onDataSourceProgress()
    {
        float f = 0.0F;
        DataSource adatasource[] = mDataSources;
        int j = adatasource.length;
        for(int i = 0; i < j; i++)
            f += adatasource[i].getProgress();

        setProgress(f / (float)mDataSources.length);
    }

    public boolean close()
    {
        if(!super.close())
            return false;
        DataSource adatasource[] = mDataSources;
        int j = adatasource.length;
        for(int i = 0; i < j; i++)
            adatasource[i].close();

        return true;
    }

    public List getResult()
    {
        boolean flag = hasResult();
        Object obj = null;
        if(flag) {
            ArrayList arraylist;
            DataSource adatasource[];
            int j;
            arraylist = new ArrayList(mDataSources.length);
            adatasource = mDataSources;
            j = adatasource.length;
            int i = 0;
            obj = arraylist;

            for(;i < j;) {
                arraylist.add(adatasource[i].getResult());
                i++;
            }
        }

        return ((List) (obj));
    }

    public boolean hasResult()
    {
        boolean flag = true;
        if(isClosed()) {
            flag = false;
        } else {
            int i;
            int j;
            i = mFinishedDataSources;
            j = mDataSources.length;
            if (i != j)
                flag = false;
        }

        return flag;
    }

    private final DataSource mDataSources[];
    private int mFinishedDataSources;




}
