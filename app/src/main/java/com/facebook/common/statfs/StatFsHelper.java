// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.common.statfs;

import android.os.*;
import com.facebook.common.internal.Throwables;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StatFsHelper
{
    public static enum StorageType
    {
        INTERNAL("INTERNAL", 0),
        EXTERNAL("EXTERNAL", 1);

        private StorageType(String s, int i)
        {
            sType = s;
            iType = i;
        }
        private String sType;
        private int iType;
    }


    protected StatFsHelper()
    {
        mInternalStatFs = null;
        mExternalStatFs = null;
        mInitialized = false;
    }

    protected static StatFs createStatFs(String s)
    {
        return new StatFs(s);
    }

    private void ensureInitialized()
    {
        if(mInitialized) {
            lock.unlock();
            return;
        }
        lock.lock();
        if(!mInitialized)
        {
            mInternalPath = Environment.getDataDirectory();
            mExternalPath = Environment.getExternalStorageDirectory();
            updateStats();
            mInitialized = true;
        }
        lock.unlock();
        return;
    }

    public static StatFsHelper getInstance()
    {
        StatFsHelper statfshelper;
        if(sStatsFsHelper == null)
            sStatsFsHelper = new StatFsHelper();
        statfshelper = sStatsFsHelper;
        return statfshelper;
    }

    private void maybeUpdateStats()
    {
        if(!lock.tryLock()) {
            lock.unlock();
            return;
        }
        if(SystemClock.elapsedRealtime() - mLastRestatTime > RESTAT_INTERVAL_MS)
            updateStats();
        lock.unlock();
        return;
    }

    private void updateStats()
    {
        mInternalStatFs = updateStatsHelper(mInternalStatFs, mInternalPath);
        mExternalStatFs = updateStatsHelper(mExternalStatFs, mExternalPath);
        mLastRestatTime = SystemClock.elapsedRealtime();
    }

    private StatFs updateStatsHelper(StatFs statfs, File file)
    {
        if(file == null || !file.exists())
            return null;
        if(statfs == null)
        {
            try
            {
                statfs = createStatFs(file.getAbsolutePath());
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                statfs = null;
            }
        }
        statfs.restat(file.getAbsolutePath());
        return statfs;
    }

    public long getAvailableStorageSpace(StorageType storagetype)
    {
        ensureInitialized();
        maybeUpdateStats();
        StatFs temp;
        if(storagetype == StorageType.INTERNAL)
            temp = mInternalStatFs;
        else
            temp = mExternalStatFs;
        if(storagetype != null)
            return (long)temp.getBlockSize() * (long)temp.getAvailableBlocks();
        else
            return 0L;
    }

    public void resetStats()
    {
        if(!lock.tryLock()) {
            lock.unlock();
            return;
        }
        ensureInitialized();
        updateStats();
        lock.unlock();
        return;
    }

    public boolean testLowDiskSpace(StorageType storagetype, long l)
    {
        ensureInitialized();
        long l1 = getAvailableStorageSpace(storagetype);
        return l1 <= 0L || l1 < l;
    }

    private static final long RESTAT_INTERVAL_MS;
    private static StatFsHelper sStatsFsHelper;
    private final Lock lock = new ReentrantLock();
    private File mExternalPath;
    private StatFs mExternalStatFs;
    private boolean mInitialized;
    private File mInternalPath;
    private StatFs mInternalStatFs;
    private long mLastRestatTime;

    static
    {
        RESTAT_INTERVAL_MS = TimeUnit.MINUTES.toMillis(2L);
    }
}
