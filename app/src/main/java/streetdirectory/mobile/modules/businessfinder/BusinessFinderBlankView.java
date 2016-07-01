

package streetdirectory.mobile.modules.businessfinder;

import android.content.Context;
import android.view.View;

public class BusinessFinderBlankView
    implements android.widget.TabHost.TabContentFactory
{

    public BusinessFinderBlankView(Context context)
    {
        _context = context;
    }

    public View createTabContent(String s)
    {
        View view  = new View(_context);
        view.setMinimumWidth(0);
        view.setMinimumHeight(0);
        return view;
    }

    private Context _context;
}
