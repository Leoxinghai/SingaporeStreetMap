// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.cache.disk;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheErrorLogger;
import com.facebook.cache.common.WriterCallback;
import com.facebook.common.file.*;
import com.facebook.common.internal.CountingOutputStream;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Throwables;
import com.facebook.common.time.Clock;
import com.facebook.common.time.SystemClock;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

// Referenced classes of package com.facebook.cache.disk:
//            DiskStorage

public class DefaultDiskStorage
    implements DiskStorage
{
    private class EntriesCollector
        implements FileTreeVisitor
    {

        public List getEntries()
        {
            return Collections.unmodifiableList(result);
        }

        public void postVisitDirectory(File file)
        {
        }

        public void preVisitDirectory(File file)
        {
        }

        public void visitFile(File file)
        {
            FileInfo fileinfo = getShardFileInfo(file);
            if(fileinfo != null && fileinfo.type == FileType.CONTENT)
                result.add(new EntryImpl(file));
        }

        private final List result;
        private EntriesCollector()
        {
            super();
            result = new ArrayList();
        }

    }

    class EntryImpl
        implements DiskStorage.Entry
    {


        public FileBinaryResource getResource()
        {
            return resource;
        }

        public long getSize()
        {
            if(size < 0L)
                size = resource.size();
            return size;
        }

        public long getTimestamp()
        {
            if(timestamp < 0L)
                timestamp = resource.getFile().lastModified();
            return timestamp;
        }

        private final FileBinaryResource resource;
        private long size;
        private long timestamp;

        private EntryImpl(File file)
        {
            super();
            Preconditions.checkNotNull(file);
            resource = FileBinaryResource.createOrNull(file);
            size = -1L;
            timestamp = -1L;
        }

    }

    private static class FileInfo
    {

        public static FileInfo fromFile(File file)
        {
            int i;
            FileType filetype;

            String filename = file.getName();
            i = filename.lastIndexOf('.');

            for(;i > 0;) {
                if((filetype = FileType.fromExtension(filename.substring(i))) == null)
                    return null;
                String s = filename.substring(0, i);
                filename = s;
                if(!filetype.equals(FileType.TEMP))
                    return new FileInfo(filetype, filename);
                i = s.lastIndexOf('.');
                if(i <= 0)
                    return null;
                filename = s.substring(0, i);
            }
            return null;
        }

        public File createTempFile(File file)
            throws IOException
        {
            return File.createTempFile((new StringBuilder()).append(resourceId).append(".").toString(), ".tmp", file);
        }

        public File toFile(File file)
        {
            return new File(file, (new StringBuilder()).append(resourceId).append(type.extension).toString());
        }

        public String toString()
        {
            return (new StringBuilder()).append(type).append("(").append(resourceId).append(")").toString();
        }

        public final String resourceId;
        public final FileType type;

        private FileInfo(FileType filetype, String s)
        {
            type = filetype;
            resourceId = s;
        }

    }

    private static enum FileType
    {

        CONTENT("CONTENT", 0, ".cnt"),
        TEMP("TEMP", 1, ".tmp");
        public static FileType fromExtension(String s)
        {
            if(".cnt".equals(s))
                return CONTENT;
            if(".tmp".equals(s))
                return TEMP;
            else
                return null;
        }
        public final String extension;
        private String sType;
        private int iType;

        private FileType(String s, int i, String s1)
        {
            sType = s;
            iType = i;
            extension = s1;
        }
    }

    private static class IncompleteFileException extends IOException
    {

        public final long actual;
        public final long expected;

        public IncompleteFileException(long l, long l1)
        {
            super((new StringBuilder()).append("File was not written completely. Expected: ").append(l).append(", found: ").append(l1).toString());
            expected = l;
            actual = l1;
        }
    }

    private class PurgingVisitor
        implements FileTreeVisitor
    {

        private boolean isExpectedFile(File file)
        {
            boolean flag = false;
            FileInfo fileinfo = getShardFileInfo(file);
            if(fileinfo == null)
                return false;
            if(fileinfo.type == FileType.TEMP)
                return isRecentFile(file);
            if(fileinfo.type == FileType.CONTENT)
                flag = true;
            Preconditions.checkState(flag);
            return true;
        }

        private boolean isRecentFile(File file)
        {
            return file.lastModified() > mClock.now() - DefaultDiskStorage.TEMP_FILE_LIFETIME_MS;
        }

        public void postVisitDirectory(File file)
        {
            if(!mRootDirectory.equals(file) && !insideBaseDirectory)
                file.delete();
            if(insideBaseDirectory && file.equals(mVersionDirectory))
                insideBaseDirectory = false;
        }

        public void preVisitDirectory(File file)
        {
            if(!insideBaseDirectory && file.equals(mVersionDirectory))
                insideBaseDirectory = true;
        }

        public void visitFile(File file)
        {
            if(!insideBaseDirectory || !isExpectedFile(file))
                file.delete();
        }

        private boolean insideBaseDirectory;

        private PurgingVisitor()
        {
            super();
        }

    }


    public DefaultDiskStorage(File file, int i, CacheErrorLogger cacheerrorlogger)
    {
        Preconditions.checkNotNull(file);
        mRootDirectory = file;
        mVersionDirectory = new File(mRootDirectory, getVersionSubdirectoryName(i));
        mCacheErrorLogger = cacheerrorlogger;
        recreateDirectoryIfVersionChanges();
    }

    private long doRemove(File file)
    {
        long l;
        if(!file.exists())
        {
            l = 0L;
        } else
        {
            l = file.length();
            if(!file.delete())
                return -1L;
        }
        return l;
    }

    private DiskStorage.DiskDumpInfoEntry dumpCacheEntry(DiskStorage.Entry entry)
        throws IOException
    {
        EntryImpl entryimpl = (EntryImpl)entry;
        String s = "";
        byte abyte0[] = entryimpl.getResource().read();
        String s1 = typeOfBytes(abyte0);
        if(s1.equals("undefined"))
        {
            if(abyte0.length >= 4)
                s = String.format((Locale)null, "0x%02X 0x%02X 0x%02X 0x%02X", new Object[] {
                    Byte.valueOf(abyte0[0]), Byte.valueOf(abyte0[1]), Byte.valueOf(abyte0[2]), Byte.valueOf(abyte0[3])
                });
        }
        return new DiskStorage.DiskDumpInfoEntry(entryimpl.getResource().getFile().getPath(), s1, entryimpl.getSize(), s);
    }

    private FileInfo getShardFileInfo(File file)
    {
        FileInfo fileinfo = FileInfo.fromFile(file);
        if(fileinfo == null)
            return null;
        if(!getSubdirectory(fileinfo.resourceId).equals(file.getParentFile()))
            fileinfo = null;
        return fileinfo;
    }

    private File getSubdirectory(String s)
    {
        int i = Math.abs(s.hashCode() % 100);
        return new File(mVersionDirectory, String.valueOf(i));
    }

    static String getVersionSubdirectoryName(int i)
    {
        return String.format((Locale)null, "%s.ols%d.%d", new Object[] {
            "v2", Integer.valueOf(100), Integer.valueOf(i)
        });
    }

    private void mkdirs(File file, String s)
        throws IOException
    {
        try
        {
            FileUtils.mkdirs(file);
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            mCacheErrorLogger.logError(com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.WRITE_CREATE_DIR, TAG, s, ex);
            throw ex;
        }
    }

    private boolean query(String s, boolean flag)
    {
        File file = getContentFileFor(s);
        boolean flag1 = file.exists();
        if(flag && flag1)
            file.setLastModified(mClock.now());
        return flag1;
    }

    private void recreateDirectoryIfVersionChanges()
    {
        boolean flag = false;
        if(!mRootDirectory.exists()) {
            if(!mVersionDirectory.exists())
            {
                flag = true;
                FileTree.deleteRecursively(mRootDirectory);
            }
        } else {
            flag = true;
        }

        try {
            if (!flag) {
                mCacheErrorLogger.logError(com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.WRITE_CREATE_DIR, TAG, (new StringBuilder()).append("version directory could not be created: ").append(mVersionDirectory).toString(), null);
                return;
            }
            FileUtils.mkdirs(mVersionDirectory);
        } catch(Exception ex) {

        }
        return;
    }

    private String typeOfBytes(byte abyte0[])
    {
        if(abyte0.length >= 2)
        {
            if(abyte0[0] == -1 && abyte0[1] == -40)
                return "jpg";
            if(abyte0[0] == -119 && abyte0[1] == 80)
                return "png";
            if(abyte0[0] == 82 && abyte0[1] == 73)
                return "webp";
            if(abyte0[0] == 71 && abyte0[1] == 73)
                return "gif";
        }
        return "undefined";
    }

    public void clearAll()
    {
        FileTree.deleteContents(mRootDirectory);
    }


    public FileBinaryResource commit(String s, BinaryResource binaryresource, Object obj)
        throws IOException
    {
        File file  = getContentFileFor(s);
        try
        {
            FileUtils.rename(((FileBinaryResource)binaryresource).getFile(), file);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            Throwable thows = ex.getCause();
            CacheErrorLogger.CacheErrorCategory result;
            if(thows == null)
                result = com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.WRITE_RENAME_FILE_OTHER;
            else
            if(thows instanceof com.facebook.common.file.FileUtils.ParentDirNotFoundException)
                result = com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.WRITE_RENAME_FILE_TEMPFILE_PARENT_NOT_FOUND;
            else
            if(thows instanceof FileNotFoundException)
                result = com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.WRITE_RENAME_FILE_TEMPFILE_NOT_FOUND;
            else
                result = com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.WRITE_RENAME_FILE_OTHER;
            mCacheErrorLogger.logError(result, TAG, "commit", ex);
            throw ex;
        }

        if(file.exists())
            file.setLastModified(mClock.now());
        return FileBinaryResource.createOrNull(file);
    }

    public boolean contains(String s, Object obj)
    {
        return query(s, false);
    }

    public FileBinaryResource createTemporary(String s, Object obj)
        throws IOException
    {
        FileInfo temp = new FileInfo(FileType.TEMP, s);
        obj = getSubdirectory(((FileInfo) (temp)).resourceId);
        if(!((File) (obj)).exists())
            mkdirs(((File) (obj)), "createTemporary");
        try
        {
            FileBinaryResource temp2 = FileBinaryResource.createOrNull(temp.createTempFile(((File) (obj))));
            return temp2;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            mCacheErrorLogger.logError(com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.WRITE_CREATE_TEMPFILE, TAG, "createTemporary", ex);
            throw ex;
        }
    }

    File getContentFileFor(String s)
    {
        FileInfo temp = new FileInfo(FileType.CONTENT, s);
        return temp.toFile(getSubdirectory(((FileInfo) (temp)).resourceId));
    }

    public DiskStorage.DiskDumpInfo getDumpInfo()
        throws IOException
    {
        Object obj = getEntries();
        DiskStorage.DiskDumpInfo diskdumpinfo = new DiskStorage.DiskDumpInfo();
        DiskStorage.DiskDumpInfoEntry diskdumpinfoentry;
        for(obj = ((List) (obj)).iterator(); ((Iterator) (obj)).hasNext(); diskdumpinfo.entries.add(diskdumpinfoentry))
        {
            diskdumpinfoentry = dumpCacheEntry((DiskStorage.Entry)((Iterator) (obj)).next());
            String s = diskdumpinfoentry.type;
            if(!diskdumpinfo.typeCounts.containsKey(s))
                diskdumpinfo.typeCounts.put(s, Integer.valueOf(0));
            diskdumpinfo.typeCounts.put(s, Integer.valueOf(((Integer)diskdumpinfo.typeCounts.get(s)).intValue() + 1));
        }

        return diskdumpinfo;
    }


    public List getEntries()
        throws IOException
    {
        EntriesCollector entriescollector = new EntriesCollector();
        FileTree.walkFileTree(mVersionDirectory, entriescollector);
        return entriescollector.getEntries();
    }


    public FileBinaryResource getResource(String s, Object obj)
    {
        File file = getContentFileFor(s);
        if(file.exists())
        {
            file.setLastModified(mClock.now());
            return FileBinaryResource.createOrNull(file);
        } else
        {
            return null;
        }
    }

    public boolean isEnabled()
    {
        return true;
    }

    public void purgeUnexpectedResources()
    {
        FileTree.walkFileTree(mRootDirectory, new PurgingVisitor());
    }

    public long remove(DiskStorage.Entry entry)
    {
        return doRemove(((EntryImpl)entry).getResource().getFile());
    }

    public long remove(String s)
    {
        return doRemove(getContentFileFor(s));
    }

    public boolean touch(String s, Object obj)
    {
        return query(s, true);
    }

    public void updateResource(String s, BinaryResource binaryresource, WriterCallback writercallback, Object obj)
        throws IOException
    {
        File file = ((FileBinaryResource)binaryresource).getFile();
        long l;
        try
        {
            FileOutputStream temp2 = new FileOutputStream(file);
            CountingOutputStream temp3= new CountingOutputStream(temp2);
            writercallback.write(temp3);
            temp3.flush();
            l = temp3.getCount();
            temp2.close();
            if(file.length() != l)
                throw new IncompleteFileException(l, file.length());
            else
                return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            mCacheErrorLogger.logError(com.facebook.cache.common.CacheErrorLogger.CacheErrorCategory.WRITE_UPDATE_FILE_NOT_FOUND, TAG, "updateResource", ex);
            throw ex;
        }
    }

    private static final String CONTENT_FILE_EXTENSION = ".cnt";
    private static final String DEFAULT_DISK_STORAGE_VERSION_PREFIX = "v2";
    private static final int SHARDING_BUCKET_COUNT = 100;
    private static final Class TAG = DefaultDiskStorage.class;
    private static final String TEMP_FILE_EXTENSION = ".tmp";
    static final long TEMP_FILE_LIFETIME_MS;
    private final CacheErrorLogger mCacheErrorLogger;
    private final Clock mClock = SystemClock.get();
    private final File mRootDirectory;
    private final File mVersionDirectory;

    static
    {
        TEMP_FILE_LIFETIME_MS = TimeUnit.MINUTES.toMillis(30L);
    }


}
