

package streetdirectory.mobile.modules.search.service;

import java.util.HashMap;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;

public class SearchServiceOutput extends LocationBusinessServiceOutput
{

    public SearchServiceOutput()
    {
    }

    public SearchServiceOutput(HashMap hashmap)
    {

        super(hashmap);
    }

    public void populateData()
    {
        populateData();
        if(categoryID != -1 && categoryID != 0)
            return;
        categoryID = Integer.parseInt((String)hashData.get("id"));
        hashData.put("cat", (new StringBuilder()).append(categoryID).append("").toString());
        return;
    }

    private static final long serialVersionUID = 0x93085adbc1299e81L;
}
