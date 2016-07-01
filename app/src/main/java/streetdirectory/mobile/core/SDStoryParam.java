

package streetdirectory.mobile.core;

import java.util.HashMap;

public class SDStoryParam
{

    public SDStoryParam()
    {
        content = new HashMap();
    }

    public static SDStoryParam create(String s, String s1)
    {
        SDStoryParam sdstoryparam = new SDStoryParam();
        sdstoryparam.add(s, s1);
        return sdstoryparam;
    }

    public static HashMap getDefaultParam()
    {
        return null;
    }

    public SDStoryParam add(String s, String s1)
    {
        content.put(s, s1);
        return this;
    }

    public HashMap build()
    {
        return content;
    }

    HashMap content;
}
