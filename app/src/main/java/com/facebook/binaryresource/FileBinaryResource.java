

package com.facebook.binaryresource;

import com.facebook.common.internal.Files;
import com.facebook.common.internal.Preconditions;
import java.io.*;

// Referenced classes of package com.facebook.binaryresource:
//            BinaryResource

public class FileBinaryResource
    implements BinaryResource
{

    private FileBinaryResource(File file)
    {
        mFile = (File)Preconditions.checkNotNull(file);
    }

    public static FileBinaryResource createOrNull(File file)
    {
        if(file != null)
            return new FileBinaryResource(file);
        else
            return null;
    }

    public boolean equals(Object obj)
    {
        if(obj == null || !(obj instanceof FileBinaryResource))
        {
            return false;
        } else
        {
            obj = (FileBinaryResource)obj;
            return mFile.equals(((FileBinaryResource) (obj)).mFile);
        }
    }

    public File getFile()
    {
        return mFile;
    }

    public int hashCode()
    {
        return mFile.hashCode();
    }

    public InputStream openStream()
        throws IOException
    {
        return new FileInputStream(mFile);
    }

    public byte[] read()
        throws IOException
    {
        return Files.toByteArray(mFile);
    }

    public long size()
    {
        return mFile.length();
    }

    private final File mFile;
}
