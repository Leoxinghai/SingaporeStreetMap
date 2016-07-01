
package streetdirectory.mobile.gis.maps;

import android.content.Context;
import android.graphics.Bitmap;
import java.util.ArrayList;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.service.SDHttpImageService;


public class MapTile
{

    public MapTile()
    {
        isReady = false;
        isDownloading = false;
        reqPool = new ArrayList();
        workPool = new ArrayList();
    }

    public void beginRead(String s)
    {
        id = new MapTileImageService((new StringBuilder()).append(s).append("?v=").append(SDPreferences.getInstance().getMapVersion()).toString()) {

            public void onAborted(Exception exception)
            {
                id = null;
                recycle();
                isReady = true;
                System.out.println("MapTile.onAborted."+row+":"+col+":"+exception);
            }

            public void onFailed(Exception exception)
            {
                id = null;
                isReady = true;
                System.out.println("MapTile.onFailed."+row+":"+col +":"+exception);
            }

            public void onSuccess(Bitmap bitmap)
            {
                id = null;
                isReady = true;
                recycle();
                image = bitmap;
                onTileReady(image);
                System.out.println("MapTile.onSuccess.ready."+row+":"+col);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        isDownloading = true;
        id.executeAsync();
    }

    public void buildBound()
    {
        bound = new RectangleF64(x, y, x + (float)width, y + (float)height);
        dest = new RectangleF64((double)x - l.topLeftMap.x, (double)y - l.topLeftMap.y, x + (float)width, y + (float)height);
    }

    public void cancel()
    {
        isReady = true;
        if(id != null)
        {
            id.abort();
            id = null;
        }
    }

    public void onTileReady(Bitmap bitmap)
    {
    }

    public void recycle()
    {
        if(image != null)
            image = null;
    }

    public RectangleF64 bound;
    public int col;
    protected Context context;
    public RectangleF64 dest;
    public int height;
    SDHttpImageService id;
    protected Bitmap image;
    public boolean isDownloading;
    public boolean isReady;
    public MapViewLevel l;
    public int level;
    public ArrayList reqPool;
    public int row;
    public RectangleF64 src;
    public int width;
    public ArrayList workPool;
    public float x;
    public float y;
}
