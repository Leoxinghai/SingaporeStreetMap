

package streetdirectory.mobile.modules.direction;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class JourneyFare extends SDDataOutput
{

    public JourneyFare()
    {
    }

    public void populateData()
    {
        label = (String)hashData.get("label");
        value = (String)hashData.get("value");
        unit = (String)hashData.get("unit");
    }

    private static final long serialVersionUID = 0xa603114606e0e820L;
    public String label;
    public String unit;
    public String value;
}
