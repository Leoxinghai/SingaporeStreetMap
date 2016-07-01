

package streetdirectory.mobile.modules.businessfinder.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessFinderYellowBarServiceInput extends SDHttpServiceInput
{

    public BusinessFinderYellowBarServiceInput(String s, int i)
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
