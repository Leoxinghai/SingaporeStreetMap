

package streetdirectory.mobile.modules.map;

import android.content.Context;
import android.graphics.*;
import android.opengl.*;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;

import com.xinghai.mycurve.R;

import java.nio.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

// Referenced classes of package streetdirectory.mobile.modules.map:
//            SdMapLayer

public class SdMapView extends GLSurfaceView
{
    private static class Gl
    {

        public static void checkGlError(String s)
        {
            int i = GLES20.glGetError();
            if(i != 0)
            {
                Log.e("SGX", (new StringBuilder()).append(s).append(": glError ").append(i).toString());
                throw new RuntimeException((new StringBuilder()).append(s).append(": glError ").append(i).toString());
            } else
            {
                return;
            }
        }

        public static int genTexture(Context context, int i)
        {
            android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
            options.inScaled = false;
            Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), i, options);
            i = genTexture(((Bitmap) (tmp)));
            tmp.recycle();
            return i;
        }

        public static int genTexture(Bitmap bitmap)
        {
            if(bitmap == null)
            {
                Log.d("sgx", "bitmap could not be decoded.");
                return 0;
            }
            int ai[] = new int[1];
            GLES20.glGenTextures(1, ai, 0);
            if(ai[0] == 0)
            {
                Log.d("sgx", "Could not generate a new OpenGL material object.");
                return 0;
            } else
            {
                GLES20.glBindTexture(3553, ai[0]);
                GLES20.glTexParameteri(3553, 10241, 9728);
                GLES20.glTexParameteri(3553, 10240, 9728);
                GLUtils.texImage2D(3553, 0, bitmap, 0);
                GLES20.glGenerateMipmap(3553);
                return ai[0];
            }
        }

        public static int loadShader(int i, String s)
        {
            i = GLES20.glCreateShader(i);
            GLES20.glShaderSource(i, s);
            GLES20.glCompileShader(i);
            return i;
        }

        public static void removeTexture(int i)
        {
            GLES20.glDeleteTextures(1, new int[] {
                i
            }, 0);
        }

        public static void removeTexture(int ai[])
        {
            GLES20.glDeleteTextures(ai.length, ai, 0);
        }

        public static final String fsCode = "precision mediump float;uniform sampler2D uTextureUnit;varying vec2 vTextureUv;uniform vec4 uColor;void main() { vec4 alpha = texture2D(uTextureUnit, vTextureUv); gl_FragColor = alpha; }";
        public static final String vsCode = "uniform mat4 uMVPMatrix;attribute vec4 aVertexPos;attribute vec2 aTextureData;varying vec2 vTextureUv;uniform vec2 uTextureSize;void main() {vTextureUv = aTextureData;gl_Position = uMVPMatrix * aVertexPos;}";

        private Gl()
        {
        }
    }

    public static interface OnTileRequestListener
    {

        public abstract void onTileRequest(Tile tile);
    }

    private class Renderer
        implements android.opengl.GLSurfaceView.Renderer
    {

        private Tile getTile(int i, int j, int k)
        {
            for(Iterator iterator = tiles.iterator(); iterator.hasNext();)
            {
                Tile tile1 = (Tile)iterator.next();
                if(tile1.state != TileState.DEAD && tile1.layerId == i && tile1.row == j && tile1.col == k)
                    return tile1;
            }

            for(Iterator iterator1 = tiles.iterator(); iterator1.hasNext();)
            {
                Tile tile2 = (Tile)iterator1.next();
                if(tile2.state == TileState.DEAD)
                {
                    tile2.setAlive(i, j, k);
                    return tile2;
                }
            }

            Tile tile = new Tile();
            tile.setAlive(i, j, k);
            tiles.add(tile);
            return tile;
        }

        private void initBuffer()
        {
            ByteBuffer bytebuffer = ByteBuffer.allocateDirect(packed.length * 4);
            bytebuffer.order(ByteOrder.nativeOrder());
            packedBuffer = bytebuffer.asFloatBuffer();
            packedBuffer.put(packed);
            packedBuffer.position(0);
            int ai[] = new int[1];
            GLES20.glGenBuffers(1, ai, 0);
            GLES20.glBindBuffer(34962, ai[0]);
            GLES20.glBufferData(34962, packedBuffer.capacity() * 4, packedBuffer, 35044);
            GLES20.glBindBuffer(34962, 0);
            packedBufferId = ai[0];
        }

        private void renderTile(int i, int j, int k, int l, int i1)
        {
            GLES20.glBindTexture(3553, i1);
            Matrix.setIdentityM(modelMatrix, 0);
            float f1 = toGlWidth((float)k * 0.5F);
            float f = toGlWidth((float)l * 0.5F);
            f1 = (float)((double)f1 * scale);
            f = (float)((double)f * scale);
            Matrix.translateM(modelMatrix, 0, toGlWidth((double)i * scale), toGlWidth((double)j * scale), 0.0F);
            Matrix.scaleM(modelMatrix, 0, f1, f, 1.0F);
            Matrix.multiplyMM(scratch, 0, projectionMatrix, 0, vMatrix, 0);
            Matrix.multiplyMM(scratch2, 0, scratch, 0, modelMatrix, 0);
            GLES20.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, scratch2, 0);
            GLES20.glDrawArrays(5, 0, 4);
            GLES20.glBindBuffer(34962, 0);
        }

        public void onDrawFrame(GL10 gl10)
        {
            GLES20.glClear(16640);
            int i = 1;
            Iterator iterator;
            for(iterator = layers.iterator(); iterator.hasNext();)
            {
                SdMapLayer sdmaplayer = (SdMapLayer)iterator.next();
                if(sdmaplayer.scale >= scale)
                    renderLayer(sdmaplayer, i);
                i++;
            }

        }

        public void onSurfaceChanged(GL10 gl10, int i, int j)
        {
            windowHeight = j;
            windowWidth = i;
            GLES20.glViewport(0, 0, i, j);
            float f = (float)i / (float)j;
            f = (float)j / (float)i;
            windowRatio = f;
            Matrix.orthoM(projectionMatrix, 0, -1F, 1.0F, -f, f, 0.1F, 10F);
        }

        public void onSurfaceCreated(GL10 gl10, EGLConfig eglconfig)
        {
            int i = Gl.loadShader(35633, "uniform mat4 uMVPMatrix;attribute vec4 aVertexPos;attribute vec2 aTextureData;varying vec2 vTextureUv;uniform vec2 uTextureSize;void main() {vTextureUv = aTextureData;gl_Position = uMVPMatrix * aVertexPos;}");
            int j = Gl.loadShader(35632, "precision mediump float;uniform sampler2D uTextureUnit;varying vec2 vTextureUv;uniform vec4 uColor;void main() { vec4 alpha = texture2D(uTextureUnit, vTextureUv); gl_FragColor = alpha; }");
            program = GLES20.glCreateProgram();
            GLES20.glAttachShader(program, i);
            GLES20.glAttachShader(program, j);
            GLES20.glLinkProgram(program);
            uMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
            aVertexPosHandle = GLES20.glGetAttribLocation(program, "aVertexPos");
            aTextureDataHandle = GLES20.glGetAttribLocation(program, "aTextureData");
            uColorHandle = GLES20.glGetUniformLocation(program, "uColor");
            uTextureUnitHandle = GLES20.glGetUniformLocation(program, "uTextureUnit");
            initBuffer();
            backgroundTextureId = Gl.genTexture(mContext, R.drawable.facebook);
            GLES20.glClearColor(0.3F, 0.8F, 0.3F, 1.0F);
            GLES20.glBlendFunc(770, 771);
        }

        public void renderLayer(SdMapLayer sdmaplayer, int i)
        {
            int l = centerX;
            int i1 = centerY;
            double d = scale;
            requestLayer(sdmaplayer, i, l, i1);
            GLES20.glUseProgram(program);
            GLES20.glUniform1i(uTextureUnitHandle, 0);
            GLES20.glActiveTexture(33984);
            Matrix.setLookAtM(vMatrix, 0, 0.0F, 0.0F, 5F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F);
            Matrix.rotateM(vMatrix, 0, yaw, 0.0F, 0.0F, 1.0F);
            Matrix.rotateM(vMatrix, 0, pitch, 1.0F, 0.0F, 0.0F);
            GLES20.glBindBuffer(34962, packedBufferId);
            GLES20.glEnableVertexAttribArray(aVertexPosHandle);
            GLES20.glVertexAttribPointer(aVertexPosHandle, 3, 5126, false, 20, 0);
            GLES20.glBindBuffer(34962, packedBufferId);
            GLES20.glEnableVertexAttribArray(aTextureDataHandle);
            GLES20.glVertexAttribPointer(aTextureDataHandle, 2, 5126, false, 20, 12);
            double d1 = getMeasuredWidth();
            double d2 = getMeasuredHeight();
            sdmaplayer.getTilesRect(l, i1, (int)(d1 / d), (int)(d2 / d), tempRect2);
            tempRect2.inset(-1, -1);
            int k = 0;
            int j = 0;
            Iterator temp = tiles.iterator();
            do
                if(temp.hasNext())
                {
                    Tile tile = (Tile)temp.next();
                    if(tile.state != TileState.DEAD && tempRect2.contains(tile.col, tile.row))
                    {
                        int j1 = tile.x - l;
                        int k1 = tile.y - i1;
                        k++;

                        switch(tile.state.ordinal())
                        {
                        default:
                            tile.kill();
                            j++;
                            break;

                        case 1: // '\001'
                            Log.d("SDGLMAP", (new StringBuilder()).append("render alive x:").append(j1).append(", y:").append(k1).toString());
                            renderTile(j1, k1, tile.width, tile.height, backgroundTextureId);
                            break;

                        case 2: // '\002'
                            Log.d("SDGLMAP", (new StringBuilder()).append("render loading x:").append(j1).append(", y:").append(k1).toString());
                            renderTile(j1, k1, tile.width, tile.height, backgroundTextureId);
                            break;

                        case 3: // '\003'
                            Log.d("SDGLMAP", (new StringBuilder()).append("render loaded x:").append(j1).append(", y:").append(k1).toString());
                            renderTile(j1, k1, tile.width, tile.height, backgroundTextureId);
                            break;

                        case 4: // '\004'
                            Log.d("SDGLMAP", (new StringBuilder()).append("render lit x:").append(j1).append(", y:").append(k1).toString());
                            renderTile(j1, k1, tile.width, tile.height, tile.textureId);
                            break;
                        }
                    } else
                    {
                        tile.kill();
                        j++;
                    }
                } else
                {
                    Log.d("SDGLMAP", (new StringBuilder()).append("render req layer ").append(i).append(", ra=").append(k).append(", rk=").append(j).append(", totals=").append(tiles.size()).toString());
                    return;
                }
            while(true);
        }

        public void requestLayer(SdMapLayer sdmaplayer, int i, int j, int k)
        {
            sdmaplayer.getTilesRect(j, k, (int)((double)(float)getMeasuredWidth() / scale), (int)((double)(float)getMeasuredHeight() / scale), tempRect);
            int l1 = tempRect.right;
            int i2 = tempRect.left;
            int j2 = tempRect.bottom;
            int k2 = tempRect.top;
            j = tempRect.left * sdmaplayer.tileWidth;
            k = tempRect.top * sdmaplayer.tileHeight;
            int i1 = 0;
            for(int l = tempRect.top; l <= tempRect.bottom;)
            {
                int k1 = tempRect.left;
                int j1 = j;
                j = i1;
                for(i1 = k1; i1 <= tempRect.right; i1++)
                {
                    Tile tile = getTile(i, l, i1);
                    tile.x = j1;
                    tile.y = k;
                    tile.width = sdmaplayer.tileWidth;
                    tile.height = sdmaplayer.tileHeight;
                    if(onTileRequestListener != null)
                        onTileRequestListener.onTileRequest(tile);
                    j1 += sdmaplayer.tileWidth;
                    j++;
                }

                j1 = tempRect.left * sdmaplayer.tileWidth;
                k += sdmaplayer.tileHeight;
                l++;
                i1 = j;
                j = j1;
            }

            Log.d("SDGLMAP", (new StringBuilder()).append("render req rl=").append(i1).append(", row=").append(j2 - k2).append(", col=").append(l1 - i2).append(", totals=").append(tiles.size()).toString());
        }

        protected float toGlWidth(double d)
        {
            return (float)d / ((float)windowWidth * 0.5F);
        }

        private int aTextureDataHandle;
        private int aVertexPosHandle;
        private int backgroundTextureId;
        protected float color[] = {
            0.5F, 1.0F, 0.5F, 1.0F
        };
        private final Context mContext;
        protected float modelMatrix[];
        protected float packed[] = {
            -1F, -1F, 0.0F, 0.0F, 1.0F, 1.0F, -1F, 0.0F, 1.0F, 1.0F,
            -1F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F
        };
        protected FloatBuffer packedBuffer;
        protected int packedBufferId;
        protected int program;
        protected float projectionMatrix[];
        protected float scratch[];
        protected float scratch2[];
        private Rect tempRect;
        private Rect tempRect2;
        public ArrayList tiles;
        private int uColorHandle;
        private int uMVPMatrixHandle;
        private int uTextureUnitHandle;
        protected float vMatrix[];
        private int windowHeight;
        private float windowRatio;
        private int windowWidth;

        public Renderer(Context context)
        {
            super();
            windowWidth = 1;
            windowHeight = 1;
            windowRatio = 1.0F;
            tempRect = new Rect();
            tempRect2 = new Rect();
            tiles = new ArrayList();
            projectionMatrix = new float[16];
            modelMatrix = new float[16];
            vMatrix = new float[16];
            scratch2 = new float[16];
            scratch = new float[16];
            packedBuffer = null;
            mContext = context;
        }
    }

    public static class Tile
    {

        public void kill()
        {
            state = TileState.DEAD;
            textureId = -1;
        }

        public int loadBitmap(Bitmap bitmap)
        {
            state = TileState.LOADED;
            textureId = Gl.genTexture(bitmap);
            if(textureId > 0)
                state = TileState.LIT;
            return textureId;
        }

        public void setAlive(int i, int j, int k)
        {
            state = TileState.ALIVE;
            layerId = i;
            row = j;
            col = k;
        }

        public int col;
        public int height;
        public int layerId;
        public int row;
        public TileState state;
        public int textureId;
        public int width;
        public int x;
        public int y;

        public Tile()
        {
            state = TileState.DEAD;
        }
    }

    private static enum TileState
    {


        DEAD("DEAD", 0),
        ALIVE("ALIVE", 1),
        LOADING("LOADING", 2),
        LOADED("LOADED", 3),
        LIT("LIT", 4),
        FAILED("FAILED", 5);

        private String sType;
        private int iType;

        private TileState(String s, int i)
        {
            sType = s;
            iType = i;
        }
    }


    public SdMapView(Context context)
    {
        super(context);
        layers = new ArrayList();
        scale = 1.0D;
        pitch = 0.0F;
        renderer = new Renderer(context);
        init(context);
    }

    public SdMapView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        layers = new ArrayList();
        scale = 1.0D;
        pitch = 0.0F;
        renderer = new Renderer(context);
        init(context);
    }

    private void init(Context context)
    {
        setEGLContextClientVersion(2);
        setRenderer(renderer);
        setRenderMode(0);
    }

    public void setOnTileRequestListener(OnTileRequestListener ontilerequestlistener)
    {
        onTileRequestListener = ontilerequestlistener;
    }

    public int centerX;
    public int centerY;
    public ArrayList layers;
    protected OnTileRequestListener onTileRequestListener;
    public float pitch;
    protected final Renderer renderer;
    public double scale;
    public float yaw;
}
