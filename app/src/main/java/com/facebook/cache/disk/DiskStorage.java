

package com.facebook.cache.disk;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.cache.common.WriterCallback;
import java.io.IOException;
import java.util.*;

public interface DiskStorage
{
    public static class DiskDumpInfo
    {

        public List entries;
        public Map typeCounts;

        public DiskDumpInfo()
        {
            entries = new ArrayList();
            typeCounts = new HashMap();
        }
    }

    public static class DiskDumpInfoEntry
    {

        public final String firstBits;
        public final String path;
        public final float size;
        public final String type;

        protected DiskDumpInfoEntry(String s, String s1, float f, String s2)
        {
            path = s;
            type = s1;
            size = f;
            firstBits = s2;
        }
    }

    public static interface Entry
    {

        public abstract BinaryResource getResource();

        public abstract long getSize();

        public abstract long getTimestamp();
    }


    public abstract void clearAll()
        throws IOException;

    public abstract BinaryResource commit(String s, BinaryResource binaryresource, Object obj)
        throws IOException;

    public abstract boolean contains(String s, Object obj)
        throws IOException;

    public abstract BinaryResource createTemporary(String s, Object obj)
        throws IOException;

    public abstract DiskDumpInfo getDumpInfo()
        throws IOException;

    public abstract Collection getEntries()
        throws IOException;

    public abstract BinaryResource getResource(String s, Object obj)
        throws IOException;

    public abstract boolean isEnabled();

    public abstract void purgeUnexpectedResources();

    public abstract long remove(Entry entry)
        throws IOException;

    public abstract long remove(String s)
        throws IOException;

    public abstract boolean touch(String s, Object obj)
        throws IOException;

    public abstract void updateResource(String s, BinaryResource binaryresource, WriterCallback writercallback, Object obj)
        throws IOException;
}
