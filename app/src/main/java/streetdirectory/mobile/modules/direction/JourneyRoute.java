

package streetdirectory.mobile.modules.direction;

import java.util.ArrayList;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneyLine

public class JourneyRoute
{

    public JourneyRoute()
    {
    }

    public void addNewLines(JourneyLine journeyline)
    {
        if(journeyline != null)
            arrayOfLines.add(journeyline);
    }

    public ArrayList arrayOfLines;
}
