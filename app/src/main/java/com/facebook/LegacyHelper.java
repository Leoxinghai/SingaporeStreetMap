

package com.facebook;

import android.os.Bundle;

// Referenced classes of package com.facebook:
//            Session

public class LegacyHelper
{

    public LegacyHelper()
    {
    }

    public static void extendTokenCompleted(Session session, Bundle bundle)
    {
        session.extendTokenCompleted(bundle);
    }
}
