

package com.facebook.imagepipeline.platform;

import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.EncodedImage;

public interface PlatformDecoder
{

    public abstract CloseableReference decodeFromEncodedImage(EncodedImage encodedimage, android.graphics.Bitmap.Config config);

    public abstract CloseableReference decodeJPEGFromEncodedImage(EncodedImage encodedimage, android.graphics.Bitmap.Config config, int i);
}
