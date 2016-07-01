

package streetdirectory.mobile.modules.location.service;

import java.util.HashMap;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;

public class CurrentLocationServiceOutput extends LocationBusinessServiceOutput
{

    public CurrentLocationServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        try
        {
            distance = Double.parseDouble((String)hashData.get("d"));
            return;
        }
        catch(Exception exception)
        {
            distance = 0.0D;
        }
    }

    private static final long serialVersionUID = 0xae746e917e372b3bL;
    public double distance;
}
