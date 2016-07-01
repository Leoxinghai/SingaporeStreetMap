

package streetdirectory.mobile.sitt;

import java.util.ArrayList;
import streetdirectory.mobile.service.SDDataOutput;

// Referenced classes of package streetdirectory.mobile.sitt:
//            SittServerNode

public class SittSsidInfoListServiceOutput extends SDDataOutput
{

    public SittSsidInfoListServiceOutput()
    {
        listServerNode = new ArrayList();
    }

    public void addNewServerNode(SittServerNode sittservernode)
    {
        if(sittservernode != null)
            listServerNode.add(sittservernode);
    }

    public void populateData()
    {
        populateData();
    }

    private static final long serialVersionUID = 0x574855e09b8cdf12L;
    public ArrayList listServerNode;
}
