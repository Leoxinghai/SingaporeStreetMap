// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

// Referenced classes of package com.facebook:
//            FacebookException

public final class NativeAppCallAttachmentStore
    implements NativeAppCallContentProvider.AttachmentDataSource
{
    static interface ProcessAttachment
    {

        public abstract void processAttachment(Object obj, File file)
            throws IOException;
    }


    public NativeAppCallAttachmentStore()
    {
    }

    private void addAttachments(Context context, UUID uuid, Map map, ProcessAttachment processattachment)
    {
        if(map.size() == 0)
            return;

        if(attachmentsDirectory == null)
            cleanupAllAttachments(context);
        ensureAttachmentsDirectoryExists(context);
        ArrayList temp1 = new ArrayList();

        try
        {
            Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext())
            {
                Object obj1 = (java.util.Map.Entry)iterator.next();
                Object obj = (String)((java.util.Map.Entry) (obj1)).getKey();
                obj1 = ((java.util.Map.Entry) (obj1)).getValue();
                obj = getAttachmentFile(uuid, ((String) (obj)), true);
                temp1.add(obj);
                processattachment.processAttachment(obj1, ((File) (obj)));
            }
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            Log.e(TAG, (new StringBuilder()).append("Got unexpected exception:").append(uuid).toString());
            for(Iterator iterator2 = temp1.iterator(); iterator2.hasNext();)
            {
                File file = (File)iterator2.next();
                try
                {
                    file.delete();
                }
                // Misplaced declaration of an exception variable
                catch(Exception ex2) { }
            }

            throw new FacebookException(ex.toString());
        }
    }

    static File getAttachmentsDirectory(Context context)
    {
        if(attachmentsDirectory == null)
            attachmentsDirectory = new File(context.getCacheDir(), "com.facebook.NativeAppCallAttachmentStore.files");

        return attachmentsDirectory;
    }

    public void addAttachmentFilesForCall(Context context, UUID uuid, Map map)
    {
        Validate.notNull(context, "context");
        Validate.notNull(uuid, "callId");
        Validate.containsNoNulls(map.values(), "mediaAttachmentFiles");
        Validate.containsNoNullOrEmpty(map.keySet(), "mediaAttachmentFiles");
        addAttachments(context, uuid, map, new ProcessAttachment() {

            public void processAttachment(File file, File file1)
                throws IOException
            {
                FileOutputStream fileoutputstream;
                fileoutputstream = new FileOutputStream(file1);
                file1 = null;
                FileInputStream fileInputStream = new FileInputStream(file);
                byte buff[] = new byte[1024];
                int i =0;

                try {
                    for (; ; ) {
                        i = fileInputStream.read(buff);
                        if (i <= 0)
                            break;
                        fileoutputstream.write(buff, 0, i);
                    }

                    Utility.closeQuietly(fileoutputstream);
                    Utility.closeQuietly(fileInputStream);
                } catch(IOException iex) {
                    Utility.closeQuietly(fileoutputstream);
                    Utility.closeQuietly(fileInputStream);
                    throw iex;
                }

            }

            public void processAttachment(Object obj, File file)
                throws IOException
            {
                processAttachment((File)obj, file);
            }

        });
    }

    public void addAttachmentsForCall(Context context, UUID uuid, Map map)
    {
        Validate.notNull(context, "context");
        Validate.notNull(uuid, "callId");
        Validate.containsNoNulls(map.values(), "imageAttachments");
        Validate.containsNoNullOrEmpty(map.keySet(), "imageAttachments");
        addAttachments(context, uuid, map, new ProcessAttachment() {

            public void processAttachment(Bitmap bitmap, File file)
                throws IOException
            {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                try {
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    Utility.closeQuietly(fileOutputStream);
                    return;
                } catch(Exception iex) {
                    Utility.closeQuietly(fileOutputStream);
                    throw iex;
                }
            }

            public void processAttachment(Object obj, File file)
                throws IOException
            {
                processAttachment((Bitmap)obj, file);
            }

        });
    }

    void cleanupAllAttachments(Context context)
    {
        Utility.deleteDirectory(getAttachmentsDirectory(context));
    }

    public void cleanupAttachmentsForCall(Context context, UUID uuid)
    {
        Utility.deleteDirectory(getAttachmentsDirectoryForCall(uuid, false));
    }

    File ensureAttachmentsDirectoryExists(Context context)
    {
        File file = getAttachmentsDirectory(context);
        file.mkdirs();
        return file;
    }

    File getAttachmentFile(UUID uuid, String s, boolean flag)
        throws IOException
    {
        File file = getAttachmentsDirectoryForCall(uuid, flag);
        if(file == null)
            return null;
        try
        {
            file = new File(file, URLEncoder.encode(s, "UTF-8"));
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return null;
        }
        return file;
    }

    File getAttachmentsDirectoryForCall(UUID uuid, boolean flag)
    {
        File file = null;
        if(attachmentsDirectory == null)
        {
            uuid = null;
        } else
        {
            file = new File(attachmentsDirectory, uuid.toString());
            if(flag)
            {
                if(!file.exists())
                {
                    file.mkdirs();
                    return file;
                }
            }
        }
        return file;
    }

    public File openAttachment(UUID uuid, String s)
        throws FileNotFoundException
    {
        if(Utility.isNullOrEmpty(s) || uuid == null)
            throw new FileNotFoundException();
        File file = null;
        try
        {
            file = getAttachmentFile(uuid, s, false);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            throw new FileNotFoundException();
        }
        return file;
    }

    static final String ATTACHMENTS_DIR_NAME = "com.facebook.NativeAppCallAttachmentStore.files";
    private static final String TAG = NativeAppCallAttachmentStore.class.getName();
    private static File attachmentsDirectory;

}
