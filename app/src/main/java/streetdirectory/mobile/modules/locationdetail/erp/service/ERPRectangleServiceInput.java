

package streetdirectory.mobile.modules.locationdetail.erp.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class ERPRectangleServiceInput extends SDHttpServiceInput
{

    public ERPRectangleServiceInput()
    {
    }

    public ERPRectangleServiceInput(double d, double d1, double d2, double d3)
    {
        top = d;
        left = d1;
        bottom = d2;
        right = d3;
    }

    public String getURL()
    {
        return URLFactory.createURLERPRectangle(top, left, bottom, right);
    }

    public double bottom;
    public double left;
    public double right;
    public double top;
}
