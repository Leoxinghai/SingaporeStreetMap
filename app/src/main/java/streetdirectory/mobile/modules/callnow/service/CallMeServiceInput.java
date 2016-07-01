

package streetdirectory.mobile.modules.callnow.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class CallMeServiceInput extends SDHttpServiceInput
{

    public CallMeServiceInput()
    {
    }

    public CallMeServiceInput(String s, int i, int j, String s1, int k)
    {
        super(s);
        locationID = j;
        companyID = i;
        phoneArea = s1;
        phoneNumber = k;
    }

    public String getURL()
    {
        return URLFactory.createURLCallMeRequest(countryCode, companyID, locationID, phoneArea, phoneNumber, apiVersion);
    }

    public int companyID;
    public int locationID;
    public String phoneArea;
    public int phoneNumber;
}
