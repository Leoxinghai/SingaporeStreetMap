// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.gis.maps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.facebook.Request;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.squareup.okhttp.*;
import com.squareup.okhttp.Callback;
import com.squareup.picasso.*;
import java.io.File;
import java.io.IOException;
import streetdirectory.mobile.SDApplication;
import streetdirectory.mobile.core.SDLogger;

// Referenced classes of package streetdirectory.mobile.gis.maps:
//            MapTools

public class SdMapTileService
{
    public static interface MapTileServiceListener
    {

        public abstract void onFailure(Exception exception);

        public abstract void onSuccess(Bitmap bitmap, String s);
    }


    public SdMapTileService()
    {
    }

    public static void downloadMapTile(String s, MapTileServiceListener maptileservicelistener)
    {
        getFromNetworkUsingFresco(s, maptileservicelistener);
    }

    private static boolean getFromCache(final String url, final MapTileServiceListener maptileservicelistener)
    {
        File file = new File(MapTools.getMapCacheStoragePath(Uri.parse(url).getPath()));
        if(((File) (file)).exists())
        {
            final Bitmap bitmap = BitmapFactory.decodeFile(((File) (file)).getAbsolutePath());
            if(bitmap != null)
            {
                mHandler.post(new Runnable() {

                    public void run()
                    {
                        SDLogger.debug((new StringBuilder()).append("[RR] Map tile is got from cache : ").append(url).toString());
                        if(maptileservicelistener != null)
                            maptileservicelistener.onSuccess(bitmap, url);
                    }

                }
);
                return true;
            }
        }
        return false;
    }

    private static void getFromNetwork(final String url, final MapTileServiceListener maptileservicelistener)
    {
        (new OkHttpClient()).newCall((new com.squareup.okhttp.Request.Builder()).url(url).build()).enqueue(new Callback() {

            public void onFailure(Request request, IOException ioexception)
            {
                SdMapTileService.onNetworkFailed(ioexception, maptileservicelistener);
            }

         @Override
         public void onFailure(com.squareup.okhttp.Request request, IOException e) {

         }

         public void onResponse(Response response)
                throws IOException
            {
                byte temp[] = response.body().bytes();
                SdMapTileService.onNetworkSuccess(BitmapFactory.decodeByteArray(temp, 0, temp.length), url, maptileservicelistener);
            }

        }
);
    }

    private static void getFromNetworkUsingFresco(final String url, final MapTileServiceListener maptileservicelistener)
    {
        com.facebook.imagepipeline.request.ImageRequest imagerequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setProgressiveRenderingEnabled(true).build();
        mImagePipeline.fetchDecodedImage(imagerequest, SDApplication.getAppContext()).subscribe(new BaseBitmapDataSubscriber() {

            public void onFailureImpl(DataSource datasource)
            {
                SdMapTileService.onNetworkFailed(new Exception((new StringBuilder()).append("Failed to get image from network ").append(url).toString()), maptileservicelistener);
            }

            public void onNewResultImpl(Bitmap bitmap)
            {
                SdMapTileService.onNetworkSuccess(bitmap, url, maptileservicelistener);
            }

        }, CallerThreadExecutor.getInstance());
    }

    private static void getFromNetworkUsingPicasso(final String url, final MapTileServiceListener maptileservicelistener)
    {
        Picasso.with(SDApplication.getAppContext()).load(url).into(new Target() {

            public void onBitmapFailed(Drawable drawable)
            {
                SdMapTileService.onNetworkFailed(new Exception((new StringBuilder()).append("Failed to get image from network ").append(url).toString()), maptileservicelistener);
            }

            public void onBitmapLoaded(Bitmap bitmap, com.squareup.picasso.Picasso.LoadedFrom loadedfrom)
            {
                SdMapTileService.onNetworkSuccess(bitmap, url, maptileservicelistener);
            }

            public void onPrepareLoad(Drawable drawable)
            {
            }

        });
    }

    private static void onNetworkFailed(final Exception exception, final MapTileServiceListener maptileservicelistener)
    {
        mHandler.post(new Runnable() {

            public void run()
            {
                if(maptileservicelistener != null)
                    maptileservicelistener.onFailure(exception);
            }

        }
);
    }

    private static void onNetworkSuccess(final Bitmap bitmap, final String url, final MapTileServiceListener maptileservicelistener)
    {
        if(bitmap != null)
        {
            MapTools.saveBitmapToCache(bitmap, Uri.parse(url).getPath());
            SDLogger.debug((new StringBuilder()).append("[RR] Map tile is got from network : ").append(url).toString());
        }
        mHandler.post(new Runnable() {

            public void run()
            {
                    if(maptileservicelistener != null)
                    {
                        if(bitmap == null)
                            maptileservicelistener.onFailure(new NullPointerException("Bitmap null"));
                        else
                            maptileservicelistener.onSuccess(bitmap, url);
                    }
                    return;
            }

        });
    }

    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static ImagePipeline mImagePipeline = Fresco.getImagePipeline();



}
