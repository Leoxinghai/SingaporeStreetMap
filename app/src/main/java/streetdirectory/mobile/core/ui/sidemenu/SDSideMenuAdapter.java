

package streetdirectory.mobile.core.ui.sidemenu;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.*;
import streetdirectory.mobile.modules.search.service.SearchServiceOutput;
import streetdirectory.mobile.sd.SDSearchHistoryMenuItem;

// Referenced classes of package streetdirectory.mobile.core.ui.sidemenu:
//            SDSideMenuItem, SideMenuLayout

public class SDSideMenuAdapter extends ArrayAdapter
    implements streetdirectory.mobile.sd.SDSearchHistoryMenuItem.OnRemoveButtonClicked
{

    public SDSideMenuAdapter(Context context, int i, List list, SideMenuLayout sidemenulayout)
    {
        super(context, i, list);
        mContext = context;
        mSideMenuLayout = sidemenulayout;
    }

    public int getItemViewType(int i)
    {
        SDSideMenuItem sdsidemenuitem = null;
        Object obj = (SDSideMenuItem)getItem(i);
        sdsidemenuitem = ((SDSideMenuItem) (obj));

		try {
			i = 0;
			Iterator iterator = SDSideMenuItem.typeClasses.iterator();
			for(;((Iterator) (iterator)).hasNext() || ((Class)((Iterator) (iterator)).next()).isInstance(sdsidemenuitem);) {
				i++;
			}
            return i;

        } catch(Exception exception) {
	        Log.d("ERROR", (new StringBuilder()).append("").append(i).toString());
		}
        return -1;

    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        SDSideMenuItem sdsidemenuitem = (SDSideMenuItem)getItem(i);
        sdsidemenuitem.menuSlideOffset = mSideMenuLayout.getSlideOffset();
        if(sdsidemenuitem instanceof SDSearchHistoryMenuItem)
            ((SDSearchHistoryMenuItem)sdsidemenuitem).setOnRemoveButtonClicked(this);
        return sdsidemenuitem.getView(mContext, view, viewgroup);
    }

    public int getViewTypeCount()
    {
        return SDSideMenuItem.typeCount;
    }

    public void onRemoveButtonClicked(SearchServiceOutput searchserviceoutput)
    {
    }

    private Context mContext;
    private SideMenuLayout mSideMenuLayout;
}
