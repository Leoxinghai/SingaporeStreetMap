

package streetdirectory.mobile.core.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import java.util.*;

// Referenced classes of package streetdirectory.mobile.core.ui:
//            SanListViewItem

public class SanListViewAdapter extends ArrayAdapter
{

    public SanListViewAdapter(Context context, int i, List list)
    {
        super(context, i, list);
        mContext = context;
        items = list;
    }

    public static android.widget.AdapterView.OnItemClickListener createOnItemClickListener()
    {
        return new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                SanListViewAdapter sanListViewAdapter = (SanListViewAdapter)adapterview.getAdapter();
                ((SanListViewItem)((SanListViewAdapter) (sanListViewAdapter)).items.get(i)).execute(adapterview.getContext(), sanListViewAdapter);
            }

        }
;
    }

    public int getItemViewType(int i)
    {
        SanListViewItem sanlistviewitem = (SanListViewItem)items.get(i);
        i = 0;
        Iterator iterator = SanListViewItem.typeClasses.iterator();
        do
        {
            if(!iterator.hasNext() || ((Class)iterator.next()).isInstance(sanlistviewitem))
                return i;
            i++;
        } while(true);
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        return ((SanListViewItem)getItem(i)).getView(mContext, view, viewgroup);
    }

    public int getViewTypeCount()
    {
        if(SanListViewItem.typeCount < 1)
            return 1;
        else
            return SanListViewItem.typeCount;
    }

    List items;
    Context mContext;
}
