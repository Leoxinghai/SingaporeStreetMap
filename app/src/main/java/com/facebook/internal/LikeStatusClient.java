

package com.facebook.internal;

import android.content.Context;
import android.os.Bundle;

// Referenced classes of package com.facebook.internal:
//            PlatformServiceClient

final class LikeStatusClient extends PlatformServiceClient
{

    LikeStatusClient(Context context, String s, String s1)
    {
        super(context, 0x10006, 0x10007, 0x13353c9, s);
        objectId = s1;
    }

    protected void populateRequestBundle(Bundle bundle)
    {
        bundle.putString("com.facebook.platform.extra.OBJECT_ID", objectId);
    }

    private String objectId;
}
