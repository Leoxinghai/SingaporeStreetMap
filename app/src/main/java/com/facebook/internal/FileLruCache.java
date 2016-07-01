// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.internal;

import android.content.Context;
import com.facebook.LoggingBehavior;
import com.facebook.Settings;
import java.io.*;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import org.json.*;

// Referenced classes of package com.facebook.internal:
//            Utility, Logger

public final class FileLruCache
{
    private static class BufferFile
    {

        static void deleteAll(File file)
        {
            File files[] = file.listFiles(excludeNonBufferFiles());
            if(file != null)
            {
                int j = files.length;
                for(int i = 0; i < j; i++)
                    files[i].delete();

            }
        }

        static FilenameFilter excludeBufferFiles()
        {
            return filterExcludeBufferFiles;
        }

        static FilenameFilter excludeNonBufferFiles()
        {
            return filterExcludeNonBufferFiles;
        }

        static File newFile(File file)
        {
            return new File(file, (new StringBuilder()).append("buffer").append(Long.valueOf(FileLruCache.bufferIndex.incrementAndGet()).toString()).toString());
        }

        private static final String FILE_NAME_PREFIX = "buffer";
        private static final FilenameFilter filterExcludeBufferFiles = new FilenameFilter() {

            public boolean accept(File file, String s)
            {
                return !s.startsWith("buffer");
            }

        };
        private static final FilenameFilter filterExcludeNonBufferFiles = new FilenameFilter() {

            public boolean accept(File file, String s)
            {
                return s.startsWith("buffer");
            }

        };


        private BufferFile()
        {
        }
    }

    private static class CloseCallbackOutputStream extends OutputStream
    {

        public void close()
            throws IOException
        {
            innerStream.close();
            callback.onClose();
            return;
        }

        public void flush()
            throws IOException
        {
            innerStream.flush();
        }

        public void write(int i)
            throws IOException
        {
            innerStream.write(i);
        }

        public void write(byte abyte0[])
            throws IOException
        {
            innerStream.write(abyte0);
        }

        public void write(byte abyte0[], int i, int j)
            throws IOException
        {
            innerStream.write(abyte0, i, j);
        }

        final StreamCloseCallback callback;
        final OutputStream innerStream;

        CloseCallbackOutputStream(OutputStream outputstream, StreamCloseCallback streamclosecallback)
        {
            innerStream = outputstream;
            callback = streamclosecallback;
        }
    }

    private static final class CopyingInputStream extends InputStream
    {

        public int available()
            throws IOException
        {
            return input.available();
        }

        public void close()
            throws IOException
        {
            input.close();
            output.close();
            return;
        }

        public void mark(int i)
        {
            throw new UnsupportedOperationException();
        }

        public boolean markSupported()
        {
            return false;
        }

        public int read()
            throws IOException
        {
            int i = input.read();
            if(i >= 0)
                output.write(i);
            return i;
        }

        public int read(byte abyte0[])
            throws IOException
        {
            int i = input.read(abyte0);
            if(i > 0)
                output.write(abyte0, 0, i);
            return i;
        }

        public int read(byte abyte0[], int i, int j)
            throws IOException
        {
            j = input.read(abyte0, i, j);
            if(j > 0)
                output.write(abyte0, i, j);
            return j;
        }

        public void reset()
        {
            throw new UnsupportedOperationException();
        }

        public long skip(long l)
            throws IOException
        {
            byte abyte0[] = new byte[1024];
            long l1 = 0L;
            int i;
            for(;l1 < l;)
            {
                i = read(abyte0, 0, (int)Math.min(l - l1, abyte0.length));
                if(i < 0)
                    return l1;
                l1 += i;
            }
            return l1;
        }

        final InputStream input;
        final OutputStream output;

        CopyingInputStream(InputStream inputstream, OutputStream outputstream)
        {
            input = inputstream;
            output = outputstream;
        }
    }

    public static final class Limits
    {

        int getByteCount()
        {
            return byteCount;
        }

        int getFileCount()
        {
            return fileCount;
        }

        void setByteCount(int i)
        {
            if(i < 0)
            {
                throw new InvalidParameterException("Cache byte-count limit must be >= 0");
            } else
            {
                byteCount = i;
                return;
            }
        }

        void setFileCount(int i)
        {
            if(i < 0)
            {
                throw new InvalidParameterException("Cache file count limit must be >= 0");
            } else
            {
                fileCount = i;
                return;
            }
        }

        private int byteCount;
        private int fileCount;

        public Limits()
        {
            fileCount = 1024;
            byteCount = 0x100000;
        }
    }

    private static final class ModifiedFile
        implements Comparable
    {

        public int compareTo(ModifiedFile modifiedfile)
        {
            if(getModified() < modifiedfile.getModified())
                return -1;
            if(getModified() > modifiedfile.getModified())
                return 1;
            else
                return getFile().compareTo(modifiedfile.getFile());
        }

        public int compareTo(Object obj)
        {
            return compareTo((ModifiedFile)obj);
        }

        public boolean equals(Object obj)
        {
            return (obj instanceof ModifiedFile) && compareTo((ModifiedFile)obj) == 0;
        }

        File getFile()
        {
            return file;
        }

        long getModified()
        {
            return modified;
        }

        public int hashCode()
        {
            return (file.hashCode() + 1073) * 37 + (int)(modified % 0x7fffffffL);
        }

        private static final int HASH_MULTIPLIER = 37;
        private static final int HASH_SEED = 29;
        private final File file;
        private final long modified;

        ModifiedFile(File file1)
        {
            file = file1;
            modified = file1.lastModified();
        }
    }

    private static interface StreamCloseCallback
    {

        public abstract void onClose();
    }

    private static final class StreamHeader
    {

        static JSONObject readHeader(InputStream inputstream)
            throws IOException
        {
            if(inputstream.read() != 0)
                return null;
            int k = 0;
            for(int i = 0; i < 3; i++)
            {
                int i1 = inputstream.read();
                if(i1 == -1)
                {
                    Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, "readHeader: stream.read returned -1 while reading header size");
                    return null;
                }
                k = (k << 8) + (i1 & 0xff);
            }

            byte abyte0[] = new byte[k];
            int l;
            for(int j = 0; j < abyte0.length; j += l)
            {
                l = inputstream.read(abyte0, j, abyte0.length - j);
                if(l < 1)
                {
                    Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, (new StringBuilder()).append("readHeader: stream.read stopped at ").append(Integer.valueOf(j)).append(" when expected ").append(abyte0.length).toString());
                    return null;
                }
            }

            try
            {
                JSONTokener temp = new JSONTokener(new String(abyte0));
                Object temp2 = temp.nextValue();
                if(temp2 instanceof JSONObject) {
                    temp2 = (JSONObject)temp2;
                }
                return (JSONObject)temp2;
            } catch(Exception ex)
            {
                Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, (new StringBuilder()).append("readHeader: expected JSONObject, got ").append(inputstream.getClass().getCanonicalName()).toString());
                //return null;
                throw new IOException(ex.getMessage());
            }

        }

        static void writeHeader(OutputStream outputstream, JSONObject jsonobject)
            throws IOException
        {
            byte temp[] = jsonobject.toString().getBytes();
            outputstream.write(0);
            outputstream.write(temp.length >> 16 & 0xff);
            outputstream.write(temp.length >> 8 & 0xff);
            outputstream.write(temp.length >> 0 & 0xff);
            outputstream.write(temp);
        }

        private static final int HEADER_VERSION = 0;

        private StreamHeader()
        {
        }
    }


    public FileLruCache(Context context, String s, Limits limits1)
    {
        lastClearCacheTime = new AtomicLong(0L);
        tag = s;
        limits = limits1;
        directory = new File(context.getCacheDir(), s);
        if(directory.mkdirs() || directory.isDirectory())
            BufferFile.deleteAll(directory);
    }

    private void postTrim()
    {
        synchronized(lock)
        {
            if(!isTrimPending)
            {
                isTrimPending = true;
                Settings.getExecutor().execute(new Runnable() {

                    public void run()
                    {
                        trim();
                    }

                });

            }
        }
        return;
    }

    private void renameToTargetAndTrim(String s, File file)
    {
        if(!file.renameTo(new File(directory, Utility.md5hash(s))))
            file.delete();
        postTrim();
    }

    private void trim()
    {
        synchronized(lock)
        {
            isTrimPending = false;
            isTrimInProgress = true;
        }
        Logger.log(LoggingBehavior.CACHE, TAG, "trim started");
        PriorityQueue obj = new PriorityQueue();
        long l1;
        long l2;
        l2 = 0L;
        l1 = 0L;
        File afile[] = directory.listFiles(BufferFile.excludeBufferFiles());
        long l;
        long l3;
        l = l1;
        l3 = l2;
        if(afile != null) {
            int j = afile.length;
            int i = 0;
            File file1;
            l = l1;
            l3 = l2;
            for (; i < j; ) {
                file1 = afile[i];
                ModifiedFile modifiedfile = new ModifiedFile(file1);
                ((PriorityQueue) (obj)).add(modifiedfile);
                Logger.log(LoggingBehavior.CACHE, TAG, (new StringBuilder()).append("  trim considering time=").append(Long.valueOf(modifiedfile.getModified())).append(" name=").append(modifiedfile.getFile().getName()).toString());
                l = file1.length();
                l2 += l;
                l1++;
                i++;
            }
        }

        File file;
        if(l3 > (long)limits.getByteCount() || l > (long)limits.getFileCount()) {
            file = ((ModifiedFile) ((PriorityQueue) (obj)).remove()).getFile();
            Logger.log(LoggingBehavior.CACHE, TAG, (new StringBuilder()).append("  trim removing ").append(file.getName()).toString());
            l3 -= file.length();
            l--;
            file.delete();
        }

        synchronized(lock)
        {
            isTrimInProgress = false;
            lock.notifyAll();
        }
        return;
    }

    public void clearCache()
    {
        final File filesToDelete[] = directory.listFiles(BufferFile.excludeBufferFiles());
        lastClearCacheTime.set(System.currentTimeMillis());
        if(filesToDelete != null)
            Settings.getExecutor().execute(new Runnable() {

                public void run()
                {
                    File afile[] = filesToDelete;
                    int j = afile.length;
                    for(int i = 0; i < j; i++)
                        afile[i].delete();

                }
            });
    }

    public InputStream get(String s)
        throws IOException
    {
        return get(s, null);
    }

    public InputStream get(String s, String s1)
        throws IOException
    {
        File file;
        Object obj;
        JSONObject jsonobject;
        file = new File(directory, Utility.md5hash(s));
        try
        {
            obj = new FileInputStream(file);
            obj = new BufferedInputStream(((InputStream) (obj)), 8192);
            jsonobject = StreamHeader.readHeader(((InputStream) (obj)));
            if(jsonobject == null)
            {
                if(true)
                    ((BufferedInputStream) (obj)).close();
                return null;
            }
            String s2 = jsonobject.optString("key");
            if(s2 != null && !s2.equals(s)) {
                ((BufferedInputStream) (obj)).close();
                return null;
            }

            s = jsonobject.optString("tag", null);
            if(s1 != null && !s1.equals(s)) {
                ((BufferedInputStream) (obj)).close();
                return null;
            }

            long l = (new Date()).getTime();
            Logger.log(LoggingBehavior.CACHE, TAG, (new StringBuilder()).append("Setting lastModified to ").append(Long.valueOf(l)).append(" for ").append(file.getName()).toString());
            file.setLastModified(l);
            if(false)
                ((BufferedInputStream) (obj)).close();
            return ((InputStream) (obj));
        }
        catch(Exception ex)
        {
            return null;
        }

    }

    public InputStream interceptAndPut(String s, InputStream inputstream)
        throws IOException
    {
        return new CopyingInputStream(inputstream, openPutStream(s));
    }

    OutputStream openPutStream(String s)
        throws IOException
    {
        return openPutStream(s, null);
    }

    public OutputStream openPutStream(String s, String s1)
        throws IOException
    {
        Object obj;
        File file  = BufferFile.newFile(directory);
        ((File) (file)).delete();
        if(!((File) (file)).createNewFile())
            throw new IOException((new StringBuilder()).append("Could not create file at ").append(((File) (file)).getAbsolutePath()).toString());
        Object obj1;
        try
        {
            obj1 = new FileOutputStream(file);

            final File buffer = file;
            final long bufferFileCreateTime = file.lastModified();
            final String key = s;

            obj = new BufferedOutputStream(new CloseCallbackOutputStream(((OutputStream) (obj1)), new StreamCloseCallback() {

                public void onClose()
                {
                    if(bufferFileCreateTime < lastClearCacheTime.get())
                    {
                        buffer.delete();
                        return;
                    } else
                    {
                        renameToTargetAndTrim(key, buffer);
                        return;
                    }
                }


            }
            ), 8192);
            obj1 = new JSONObject();
            ((JSONObject) (obj1)).put("key", s);
            if(!Utility.isNullOrEmpty(s1))
                ((JSONObject) (obj1)).put("tag", s1);
            StreamHeader.writeHeader(((OutputStream) (obj)), ((JSONObject) (obj1)));
            if(false)
                ((BufferedOutputStream) (obj)).close();
            ((BufferedOutputStream) (obj)).close();

            return ((OutputStream) (obj));
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, (new StringBuilder()).append("Error creating buffer output stream: ").append(s).toString());
            throw new IOException(ex.getMessage());
        }
    }

    long sizeInBytesForTest() {
        Object obj = lock;
        long l = 0L;
        boolean flag;
        if (isTrimPending) {
            long l1;
            File files[] = directory.listFiles();
            l1 = l;
            if (files != null) {
                int j = files.length;
                int i = 0;
                do {
                    l1 = l;
                    if (i >= j)
                        break;
                    l += files[i].length();
                    i++;
                } while (true);
            }
        } else {
            flag = isTrimInProgress;
            if (!flag)
                return 0L;
            try {
                lock.wait();
            } catch (InterruptedException interruptedexception) {
            }
        }
        return l;
    }

    public String toString()
    {
        return (new StringBuilder()).append("{FileLruCache: tag:").append(tag).append(" file:").append(directory.getName()).append("}").toString();
    }

    private static final String HEADER_CACHEKEY_KEY = "key";
    private static final String HEADER_CACHE_CONTENT_TAG_KEY = "tag";
    static final String TAG = FileLruCache.class.getSimpleName();
    private static final AtomicLong bufferIndex = new AtomicLong();
    private final File directory;
    private boolean isTrimInProgress;
    private boolean isTrimPending;
    private AtomicLong lastClearCacheTime;
    private final Limits limits;
    private final Object lock = new Object();
    private final String tag;

}
