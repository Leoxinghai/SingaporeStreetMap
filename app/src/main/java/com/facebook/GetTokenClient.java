

package com.facebook;

import android.content.Context;
import android.os.Bundle;
import com.facebook.internal.PlatformServiceClient;

final class GetTokenClient extends PlatformServiceClient
{

    GetTokenClient(Context context, String s)
    {
        super(context, 0x10000, 0x10001, 0x133060d, s);
    }

    protected void populateRequestBundle(Bundle bundle)
    {
    }
}
