// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core.ui.sidemenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.SDApplication;

// Referenced classes of package streetdirectory.mobile.core.ui.sidemenu:
//            SDSideMenuItem, SideMenuLayout

public class SDSideMenuBasicItem extends SDSideMenuItem
{
    class SDSideMenuBasicItemHolder
    {

        public TextView textViewTitle;

        SDSideMenuBasicItemHolder()
        {
            super();
        }
    }


    public SDSideMenuBasicItem(String s, int i, int j)
    {
        Title = "Untitled";
        Title = s;
        IconId = i;
        HighlightedIconId = j;
    }

    protected Object createViewHolder(View view)
    {
        view.getContext().getResources();
        SDSideMenuBasicItemHolder sdsidemenubasicitemholder = new SDSideMenuBasicItemHolder();
        sdsidemenubasicitemholder.textViewTitle = (TextView)view.findViewById(R.id.TitleLabel);
        return sdsidemenubasicitemholder;
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        AlertDialog builder = (new android.app.AlertDialog.Builder(context)).create();
        builder.setTitle(Title);
        builder.show();
    }

    protected int getLayoutId()
    {
        return R.layout.cell_menu_item;
    }

    protected Class getViewHolderClass()
    {
        return SDSideMenuBasicItem.class;
    }

    protected void populateViewHolder(Object obj)
    {
        SDSideMenuBasicItemHolder sdsidemenubasicitemholder = (SDSideMenuBasicItemHolder)obj;
        Object obj1 = SDApplication.getAppContext().getResources();
        sdsidemenubasicitemholder.textViewTitle.setText(Title);
        obj = null;
        if(IconId >= 0 || HighlightedIconId >= 0)
            obj = new StateListDrawable();
        if(HighlightedIconId >= 0)
        {
            android.graphics.drawable.Drawable drawable = ((Resources) (obj1)).getDrawable(HighlightedIconId);
            ((StateListDrawable) (obj)).addState(new int[] {
                0x10100a7
            }, drawable);
        }
        if(IconId >= 0)
        {
            obj1 = ((Resources) (obj1)).getDrawable(IconId);
            ((StateListDrawable) (obj)).addState(new int[0], ((android.graphics.drawable.Drawable) (obj1)));
        }
        sdsidemenubasicitemholder.textViewTitle.setCompoundDrawablesWithIntrinsicBounds(((android.graphics.drawable.Drawable) (obj)), null, null, null);
    }

    public int HighlightedIconId;
    public int IconId;
    public String Title;
}
