

package streetdirectory.mobile.modules.offlinemap;

import java.util.ArrayList;

public class OfflineMapSectionHeader
{

    public OfflineMapSectionHeader(int i, String s, String s1)
    {
        this(i, s, s1, new ArrayList());
    }

    public OfflineMapSectionHeader(int i, String s, String s1, ArrayList arraylist)
    {
        packageID = i;
        name = s;
        thumbID = s1;
        childs = arraylist;
    }

    public ArrayList childs;
    public String name;
    public int packageID;
    public String thumbID;
}
