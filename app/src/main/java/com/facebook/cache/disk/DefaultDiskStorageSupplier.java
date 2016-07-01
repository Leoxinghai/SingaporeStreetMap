// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.cache.disk;

import com.facebook.cache.common.CacheErrorLogger;
import com.facebook.common.file.FileTree;
import com.facebook.common.file.FileUtils;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.common.logging.FLog;
import java.io.File;
import java.io.IOException;

// Referenced classes of package com.facebook.cache.disk:
//            DiskStorageSupplier, DefaultDiskStorage, DiskStorage

public class DefaultDiskStorageSupplier
    implements DiskStorageSupplier
{
    static class State
    {

        public final File rootDirectory;
        public final DiskStorage storage;

        State(File file, DiskStorage diskstorage)
        {
            storage = diskstorage;
            rootDirectory = file;
        }
    }


    public DefaultDiskStorageSupplier(int i, Supplier supplier, String s, CacheErrorLogger cacheerrorlogger)
    {
        mVersion = i;
        mCacheErrorLogger = cacheerrorlogger;
        mBaseDirectoryPathSupplier = supplier;
        mBaseDirectoryName = s;
        mCurrentState = new State(null, null);
    }

    private void createStorage()
        throws IOException
    {
        File file = new File((File)mBaseDirectoryPathSupplier.get(), mBaseDirectoryName);
        createRootDirectoryIfNecessary(file);
        mCurrentState = new State(file, new DefaultDiskStorage(file, mVersion, mCacheErrorLogger));
    }

    private boolean shouldCreateNewStorage()
    {
        State state = mCurrentState;
        return state.storage == null || state.rootDirectory == null || !state.rootDirectory.exists();
    }

    void createRootDirectoryIfNecessary(File file)
        throws IOException
    {
        try
        {
            FileUtils.mkdirs(file);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            mCacheErrorLogger.logError(com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.WRITE_CREATE_DIR, TAG, "createRootDirectoryIfNecessary", ex);
            throw ex;
        }
        FLog.d(TAG, "Created cache directory %s", file.getAbsolutePath());
    }

    void deleteOldStorageIfNecessary()
    {
        if(mCurrentState.storage != null && mCurrentState.rootDirectory != null)
            FileTree.deleteRecursively(mCurrentState.rootDirectory);
    }

    public DiskStorage get()
        throws IOException
    {
        DiskStorage diskstorage;
        if(shouldCreateNewStorage())
        {
            deleteOldStorageIfNecessary();
            createStorage();
        }
        diskstorage = (DiskStorage)Preconditions.checkNotNull(mCurrentState.storage);
        return diskstorage;
    }

    private static final Class TAG = DefaultDiskStorageSupplier.class;
    private final String mBaseDirectoryName;
    private final Supplier mBaseDirectoryPathSupplier;
    private final CacheErrorLogger mCacheErrorLogger;
    State mCurrentState;
    private final int mVersion;

}
