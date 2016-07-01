

package streetdirectory.mobile.core;

import java.util.HashMap;

// Referenced classes of package streetdirectory.mobile.core:
//            ISDStoryProvider, GantTools

public class SDStoryGAProvider
    implements ISDStoryProvider
{

    public SDStoryGAProvider()
    {
    }

    public void post(String s, HashMap hashmap)
    {
        GantTools.trackPageView(s);
    }
}
