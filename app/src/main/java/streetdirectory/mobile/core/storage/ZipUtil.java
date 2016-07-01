

package streetdirectory.mobile.core.storage;

import android.util.Log;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import streetdirectory.mobile.core.SDLogger;

public class ZipUtil
{
    public static interface OnProgressExtract
    {

        public abstract boolean isAborted();

        public abstract void onFileFinished(String s, long l);

        public abstract void onFileStarted(String s);

        public abstract void onFinished(int i);

        public abstract void onProgress(String s, long l);

        public abstract void onStarted();
    }


    public ZipUtil()
    {
    }

    public static Exception extract(File file, File file1)
    {
        return extract(file, file1, null);
    }

        public static Exception extract(File zipFile,File extractFolder, OnProgressExtract onprogressextract)
        {
                try
                {
                        int BUFFER = 2048;

                        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));


                        extractFolder.mkdir();

                        //Enumeration zipFileEntries = zip.();

                        // Process each entry
                        ZipEntry zipentry;
                        for(;(zipentry = zipInputStream.getNextEntry()) != null;)
                        {
                                String currentEntry = zipentry.getName();

                                File destFile = new File(extractFolder.getPath(), currentEntry);
                                onprogressextract.onFileStarted(((String) (currentEntry)));

                                //destFile = new File(newPath, destFile.getName());
                                File destinationParent = destFile.getParentFile();

                                // create the parent directory structure if needed
                                destinationParent.mkdirs();

                                if (!zipentry.isDirectory())
                                {
                                        BufferedInputStream is = new BufferedInputStream(zipInputStream);
                                        int currentByte;
                                        // establish buffer for writing file
                                        byte data[] = new byte[BUFFER];

                                        // write the current file to disk
                                        FileOutputStream fos = new FileOutputStream(destFile);
                                        BufferedOutputStream dest = new BufferedOutputStream(fos,
                                                BUFFER);

                                        int totalRead = 0;
                                        // read and write until last byte is encountered
                                        while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                                                dest.write(data, 0, currentByte);
                                                totalRead += currentByte;
                                                onprogressextract.onProgress(destFile.getAbsolutePath(), totalRead);
                                        }
                                        dest.flush();
                                        dest.close();
                                        is.close();
                                }


                        }
                        return new Exception("");
                }
                catch (Exception e)
                {
                        SDLogger.printStackTrace(e);
                        return e;
                }

        }


    public static final Exception ABORT_EXCEPTION = new Exception("Zip Aborted");

}
