

package streetdirectory.mobile.core.ui.sidemenu;

import android.content.Context;
import android.view.*;
import java.util.ArrayList;

// Referenced classes of package streetdirectory.mobile.core.ui.sidemenu:
//            SideMenuLayout

public abstract class SDSideMenuItem
{

    public SDSideMenuItem()
    {
        menuSlideOffset = 0;
    }

    public static void addTypeCount(Class class1)
    {
        typeCount++;
        typeClasses.add(class1);
    }

    protected abstract Object createViewHolder(View view);

    public abstract void execute(Context context, SideMenuLayout sidemenulayout);

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

    protected abstract Class getViewHolderClass();

    protected abstract void populateViewHolder(Object obj);

    public static ArrayList typeClasses = new ArrayList();
    public static int typeCount = 0;
    public int menuSlideOffset;

}
