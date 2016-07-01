

package streetdirectory.mobile.modules.businessfindersubdirectory.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessFinderSubYellowBarServiceInput extends SDHttpServiceInput
{

    public BusinessFinderSubYellowBarServiceInput(String s, int i)
    {
        super(s);
        menuID = i;
    }

    public String getURL()
    {
        return URLFactory.createURLBusinessFinderYellowBarList(countryCode, menuID);
    }

    private int menuID;
}
