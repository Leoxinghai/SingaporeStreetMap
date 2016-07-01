

package streetdirectory.mobile.modules.businessfinder;

import android.support.v4.app.*;
import java.util.ArrayList;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.businessfinder:
//            BusinessFinderItemFragment

public class BusinessFinderAdapter extends FragmentPagerAdapter
{

    public BusinessFinderAdapter(FragmentManager fragmentmanager)
    {
        this(fragmentmanager, "sg", null);
    }

    public BusinessFinderAdapter(FragmentManager fragmentmanager, String s, String s1)
    {
        this(fragmentmanager, s, s1, null);
    }

    public BusinessFinderAdapter(FragmentManager fragmentmanager, String s, String s1, ArrayList arraylist)
    {
        super(fragmentmanager);
        stateID = 0;
        _menus = new ArrayList();
        viewFragment = null;
        countryCode = s;
        countryName = s1;
        if(arraylist != null)
            _menus = arraylist;
    }

    public void addCategory(BusinessFinderServiceOutput businessfinderserviceoutput)
    {
        _menus.add(businessfinderserviceoutput);
    }

    public int getCount()
    {
        return _menus.size();
    }

    public Fragment getItem(int i)
    {
        BusinessFinderServiceOutput businessfinderserviceoutput = getItemAtIndex(i);
        return BusinessFinderItemFragment.newInstance(countryCode, countryName, stateName, stateID, businessfinderserviceoutput.menuID);
    }

    public BusinessFinderServiceOutput getItemAtIndex(int i)
    {
        return (BusinessFinderServiceOutput)_menus.get(i);
    }

    public int getItemPosition(Object obj)
    {
        return -2;
    }

    private ArrayList _menus;
    public String countryCode;
    public String countryName;
    public int stateID;
    public String stateName;
    BusinessFinderItemFragment viewFragment;
}
