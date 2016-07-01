

package streetdirectory.mobile.core.ui;

import android.content.Context;
import android.view.*;
import java.util.ArrayList;

// Referenced classes of package streetdirectory.mobile.core.ui:
//            SanListViewAdapter

public abstract class SanListViewItem
{

    public SanListViewItem()
    {
        childs = new ArrayList();
        isExpanded = false;
    }

    public static void addTypeCount(Class class1)
    {
        if(!typeClasses.contains(class1))
        {
            typeCount++;
            typeClasses.add(class1);
        }
    }

    public void addChild(SanListViewItem sanlistviewitem)
    {
        childs.add(sanlistviewitem);
    }

    protected abstract Object createViewHolder(View view);

    public abstract void execute(Context context, SanListViewAdapter sanlistviewadapter);

    protected abstract int getLayoutId();

    public View getView(Context context, View view, ViewGroup viewgroup)
    {
        View view1 = view;
        if(view == null)
        {
            view1 = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(getLayoutId(), viewgroup, false);
            view1.setTag(createViewHolder(view1));
        }
        populateViewHolder(view1.getTag());
        return view1;
    }

    protected abstract void populateViewHolder(Object obj);

    public static int scrollIndex;
    public static int scrollTop;
    public static ArrayList typeClasses = new ArrayList();
    public static int typeCount = 0;
    public ArrayList childs;
    public boolean isExpanded;

}
