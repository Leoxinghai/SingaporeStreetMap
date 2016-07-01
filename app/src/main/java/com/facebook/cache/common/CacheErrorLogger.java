

package com.facebook.cache.common;


public interface CacheErrorLogger
{

    public static enum CacheErrorCategory
    {
        READ_DECODE("READ_DECODE", 0),
        READ_FILE("READ_FILE", 1),
        READ_FILE_NOT_FOUND("READ_FILE_NOT_FOUND", 2),
        READ_INVALID_ENTRY("READ_INVALID_ENTRY", 3),
        WRITE_ENCODE("WRITE_ENCODE", 4),
        WRITE_CREATE_TEMPFILE("WRITE_CREATE_TEMPFILE", 5),
        WRITE_UPDATE_FILE_NOT_FOUND("WRITE_UPDATE_FILE_NOT_FOUND", 6),
        WRITE_RENAME_FILE_TEMPFILE_NOT_FOUND("WRITE_RENAME_FILE_TEMPFILE_NOT_FOUND", 7),
        WRITE_RENAME_FILE_TEMPFILE_PARENT_NOT_FOUND("WRITE_RENAME_FILE_TEMPFILE_PARENT_NOT_FOUND", 8),
        WRITE_RENAME_FILE_OTHER("WRITE_RENAME_FILE_OTHER", 9),
        WRITE_CREATE_DIR("WRITE_CREATE_DIR", 10),
        WRITE_CALLBACK_ERROR("WRITE_CALLBACK_ERROR", 11),
        WRITE_INVALID_ENTRY("WRITE_INVALID_ENTRY", 12),
        DELETE_FILE("DELETE_FILE", 13),
        EVICTION("EVICTION", 14),
        GENERIC_IO("GENERIC_IO", 15),
        OTHER("OTHER", 16);

        String sType;
        int iType;
        private CacheErrorCategory(String s, int i)
        {
            sType = s;
            iType = i;
        }
    };

    public abstract void logError(CacheErrorCategory cacheerrorcategory, Class class1, String s, Throwable throwable);
}
