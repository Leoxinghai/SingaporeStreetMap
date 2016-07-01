// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import streetdirectory.mobile.core.BitmapTools;

// Referenced classes of package streetdirectory.mobile.core.service:
//            HttpService, HttpConnection, HttpConnectionAbortException, ImageCorruptException,
//            HttpImageConnectionInput

public class HttpImageService extends HttpService
{

    public HttpImageService(HttpImageConnectionInput httpimageconnectioninput)
    {
        super(httpimageconnectioninput);
        _connection.showProgress = true;
    }

    protected Bitmap parse(final InputStream inputStream)
    {
        final Bitmap bitmap = resize(inputStream);
        System.out.println("HttpImageService.parse."+bitmap);
        if(bitmap != null)
            if(_currentThread != null)
            {
                _currentThread.post(new Runnable() {

                    public void run()
                    {

                        onSuccess(bitmap);
                    }

                });
                return bitmap;
            } else
            {
                onSuccess(bitmap);
                return bitmap;
            }
        if(_isCanceled)
            if(_currentThread != null)
            {
                _currentThread.post(new Runnable() {

                    public void run()
                    {
                        onAborted(new HttpConnectionAbortException());
                    }

                });
                return bitmap;
            } else
            {
                onAborted(new HttpConnectionAbortException());
                return bitmap;
            }
        if(_currentThread != null)
        {
            _currentThread.post(new Runnable() {

                public void run()
                {
                    onFailed(new ImageCorruptException());
                }

            });
            return bitmap;
        } else
        {
            onFailed(new ImageCorruptException());
            return bitmap;
        }
    }


    protected Bitmap resize(InputStream inputstream)
    {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeStream(inputstream);
        try {
            inputstream.close();
            HttpImageConnectionInput temp = (HttpImageConnectionInput) _input;
            if (((HttpImageConnectionInput) (temp)).resizeOption == 0 || ((HttpImageConnectionInput) (temp)).requestWidth <= 0 || ((HttpImageConnectionInput) (temp)).requestHeight <= 0 || bitmap.getWidth() == ((HttpImageConnectionInput) (temp)).requestWidth && bitmap.getHeight() == ((HttpImageConnectionInput) (temp)).requestHeight) {

                if (((HttpImageConnectionInput) (temp)).resizeOption == 1) {
                    bitmap = BitmapTools.scale(bitmap, ((HttpImageConnectionInput) (temp)).requestWidth, ((HttpImageConnectionInput) (temp)).requestHeight);
                    bitmap.recycle();
                    return bitmap;
                }

            }
            if (((HttpImageConnectionInput) (temp)).resizeOption == 2) {
                bitmap = BitmapTools.scaleProportional(bitmap, ((HttpImageConnectionInput) (temp)).requestWidth, ((HttpImageConnectionInput) (temp)).requestHeight);
                bitmap.recycle();
                return bitmap;
            }

            if (((HttpImageConnectionInput) (temp)).resizeOption == 3) {
                bitmap = BitmapTools.scaleToFill(bitmap, ((HttpImageConnectionInput) (temp)).requestWidth, ((HttpImageConnectionInput) (temp)).requestHeight, 1);
                bitmap.recycle();
                return bitmap;
            }

            return bitmap;


        } catch(IOException iex) {
            iex.printStackTrace();
            return null;
        }
    }
}
