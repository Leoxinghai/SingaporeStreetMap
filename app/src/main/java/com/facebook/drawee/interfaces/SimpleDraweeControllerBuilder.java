

package com.facebook.drawee.interfaces;

import android.net.Uri;

// Referenced classes of package com.facebook.drawee.interfaces:
//            DraweeController

public interface SimpleDraweeControllerBuilder
{

    public abstract DraweeController build();

    public abstract SimpleDraweeControllerBuilder setCallerContext(Object obj);

    public abstract SimpleDraweeControllerBuilder setOldController(DraweeController draweecontroller);

    public abstract SimpleDraweeControllerBuilder setUri(Uri uri);

    public abstract SimpleDraweeControllerBuilder setUri(String s);
}
