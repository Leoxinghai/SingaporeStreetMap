// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.datasource;

import com.facebook.common.internal.Supplier;

// Referenced classes of package com.facebook.datasource:
//            SettableDataSource, DataSource

public class DataSources
{

    private DataSources()
    {
    }

    public static Supplier getFailedDataSourceSupplier(final Throwable throwable)
    {
        return new Supplier() {
            public DataSource get()
            {
                return DataSources.immediateFailedDataSource(throwable);
            }
        };
    }

    public static DataSource immediateFailedDataSource(Throwable throwable)
    {
        SettableDataSource settabledatasource = SettableDataSource.create();
        settabledatasource.setFailure(throwable);
        return settabledatasource;
    }
}
