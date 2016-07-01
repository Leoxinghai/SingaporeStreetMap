

package streetdirectory.mobile.core.service;


// Referenced classes of package streetdirectory.mobile.core.service:
//            HttpConnectionInput

public abstract class HttpImageConnectionInput extends HttpConnectionInput
{

    public HttpImageConnectionInput()
    {
        resizeOption = 0;
        cropOption = 0;
    }

    public HttpImageConnectionInput(int i, int j)
    {
        this(i, j, 0, 0);
    }

    public HttpImageConnectionInput(int i, int j, int k)
    {
        this(i, j, k, 0);
    }

    public HttpImageConnectionInput(int i, int j, int k, int l)
    {
        resizeOption = 0;
        cropOption = 0;
        requestWidth = i;
        requestHeight = j;
        resizeOption = k;
        cropOption = l;
    }

    public static final int FILL_CONTENT = 3;
    public static final int NO_RESIZE = 0;
    public static final int PROPORTIONAL = 2;
    public static final int STRETCH = 1;
    public int cropOption;
    public int requestHeight;
    public int requestWidth;
    public int resizeOption;
}
