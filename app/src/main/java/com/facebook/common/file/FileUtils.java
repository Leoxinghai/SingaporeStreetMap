

package com.facebook.common.file;

import com.facebook.common.internal.Preconditions;
import java.io.*;

public class FileUtils
{
    public static class CreateDirectoryException extends IOException
    {

        public CreateDirectoryException(String s)
        {
            super(s);
        }

        public CreateDirectoryException(String s, Throwable throwable)
        {
            super(s);
            initCause(throwable);
        }
    }

    public static class FileDeleteException extends IOException
    {

        public FileDeleteException(String s)
        {
            super(s);
        }
    }

    public static class ParentDirNotFoundException extends FileNotFoundException
    {

        public ParentDirNotFoundException(String s)
        {
            super(s);
        }
    }

    public static class RenameException extends IOException
    {

        public RenameException(String s)
        {
            super(s);
        }

        public RenameException(String s, Throwable throwable)
        {
            super(s);
            initCause(throwable);
        }
    }


    public FileUtils()
    {
    }

    public static void mkdirs(File file)
        throws CreateDirectoryException
    {
        if(!file.exists()) {
            if(!file.mkdirs() && !file.isDirectory())
                throw new CreateDirectoryException(file.getAbsolutePath());
        } else {
            if (!file.isDirectory()) {
                if(!file.delete())
                    throw new CreateDirectoryException(file.getAbsolutePath(), new FileDeleteException(file.getAbsolutePath()));
            }
        }
        return;
    }

    public static void rename(File file, File file1)
        throws RenameException
    {
        Object obj;
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(file1);
        file1.delete();
        if(file.renameTo(file1))
            return;
        obj = null;
        if(!file1.exists()) {
            if(!file.getParentFile().exists())
                obj = new ParentDirNotFoundException(file.getAbsolutePath());
            else
            if(!file.exists())
                obj = new FileNotFoundException(file.getAbsolutePath());
        } else {
            obj = new FileDeleteException(file1.getAbsolutePath());
        }

        throw new RenameException((new StringBuilder()).append("Unknown error renaming ").append(file.getAbsolutePath()).append(" to ").append(file1.getAbsolutePath()).toString(), ((Throwable) (obj)));
    }
}
