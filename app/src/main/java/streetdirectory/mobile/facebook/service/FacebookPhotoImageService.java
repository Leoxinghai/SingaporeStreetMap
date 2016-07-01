// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.facebook.service;

import streetdirectory.mobile.core.service.HttpImageConnectionInput;
import streetdirectory.mobile.core.service.HttpImageService;
import streetdirectory.mobile.service.URLFactory;

public class FacebookPhotoImageService extends HttpImageService
{

    public FacebookPhotoImageService(final String uid, final int width, final int height)
    {
        super(new HttpImageConnectionInput() {

            public String getURL()
            {
                return URLFactory.createURLFacebookPhotoImage(uid, width, height);
            }

        });
    }
}
