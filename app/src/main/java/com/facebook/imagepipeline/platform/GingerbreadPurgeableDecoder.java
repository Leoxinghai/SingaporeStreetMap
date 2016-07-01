// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.MemoryFile;
import com.facebook.common.internal.*;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.streams.LimitedInputStream;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.memory.PooledByteBufferInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;

// Referenced classes of package com.facebook.imagepipeline.platform:
//            DalvikPurgeableDecoder

public class GingerbreadPurgeableDecoder extends DalvikPurgeableDecoder
{

    public GingerbreadPurgeableDecoder()
    {
    }

    private static MemoryFile copyToMemoryFile(CloseableReference closeablereference, int i, byte abyte0[])
        throws IOException
    {
        Object obj;
        Object obj1;
        Object obj2;
        Object obj3;
        LimitedInputStream limitedinputstream;
        MemoryFile memoryfile;
        int j;
        if(abyte0 == null)
            j = 0;
        else
            j = abyte0.length;

		try {
			memoryfile = new MemoryFile(null, i + j);
			memoryfile.allowPurging(false);
			limitedinputstream = null;
			obj3 = null;
			obj = null;
			obj2 = null;
			obj1 = new PooledByteBufferInputStream((PooledByteBuffer)closeablereference.get());
			limitedinputstream = new LimitedInputStream(((java.io.InputStream) (obj1)), i);
			obj2 = memoryfile.getOutputStream();
			obj = obj2;
			ByteStreams.copy(limitedinputstream, ((java.io.OutputStream) (obj2)));
			if(abyte0 == null) {

				abyte0 = ((byte []) (obj1));
				obj1 = obj3;
			}

			obj = obj2;
			memoryfile.writeBytes(abyte0, 0, i, abyte0.length);
			CloseableReference.closeSafely(closeablereference);
			Closeables.closeQuietly(((java.io.InputStream) (obj1)));
			Closeables.closeQuietly(limitedinputstream);
			Closeables.close(((java.io.Closeable) (obj2)), true);
			return memoryfile;
		} catch(Exception ex) {
            ex.printStackTrace();
            return null;
		}

    }

    private Method getFileDescriptorMethod()
    {
        Method method = sGetFileDescriptorMethod;
        try {
            if (method == null) {
                sGetFileDescriptorMethod = MemoryFile.class.getDeclaredMethod("getFileDescriptor", new Class[0]);
            }
            method = sGetFileDescriptorMethod;
            return method;
        } catch(NoSuchMethodException nex) {
            nex.printStackTrace();
            return null;
        }

    }

    private FileDescriptor getMemoryFileDescriptor(MemoryFile memoryfile)
    {
        try
        {
            FileDescriptor fileDescriptor = (FileDescriptor)getFileDescriptorMethod().invoke(memoryfile, new Object[0]);
            return fileDescriptor;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return null;
        }
    }

    protected Bitmap decodeByteArrayAsPurgeable(CloseableReference closeablereference, android.graphics.BitmapFactory.Options options)
    {
        return decodeFileDescriptorAsPurgeable(closeablereference, ((PooledByteBuffer)closeablereference.get()).size(), null, options);
    }

    protected Bitmap decodeFileDescriptorAsPurgeable(CloseableReference closeablereference, int i, byte abyte0[], android.graphics.BitmapFactory.Options options)
    {
        CloseableReference closeablereference1;
        CloseableReference closeablereference2;
        closeablereference2 = null;
        closeablereference1 = null;
        try {
            MemoryFile memoryFile = copyToMemoryFile(closeablereference, i, abyte0);
            Bitmap bitmap = (Bitmap) Preconditions.checkNotNull(BitmapFactory.decodeFileDescriptor(getMemoryFileDescriptor(memoryFile), null, options), "BitmapFactory returned null");
            if (memoryFile != null)
                memoryFile.close();
            return bitmap;
        } catch(IOException iex) {
            iex.printStackTrace();
            return null;
        }
    }

    public CloseableReference decodeFromEncodedImage(EncodedImage encodedimage, android.graphics.Bitmap.Config config)
    {
        return super.decodeFromEncodedImage(encodedimage, config);
    }

    protected Bitmap decodeJPEGByteArrayAsPurgeable(CloseableReference closeablereference, int i, android.graphics.BitmapFactory.Options options)
    {
        byte abyte0[];
        if(endsWithEOI(closeablereference, i))
            abyte0 = null;
        else
            abyte0 = EOI;
        return decodeFileDescriptorAsPurgeable(closeablereference, i, abyte0, options);
    }

    public CloseableReference decodeJPEGFromEncodedImage(EncodedImage encodedimage, android.graphics.Bitmap.Config config, int i)
    {
        return super.decodeJPEGFromEncodedImage(encodedimage, config, i);
    }

    public CloseableReference pinBitmap(Bitmap bitmap)
    {
        return super.pinBitmap(bitmap);
    }

    private static Method sGetFileDescriptorMethod;
}
