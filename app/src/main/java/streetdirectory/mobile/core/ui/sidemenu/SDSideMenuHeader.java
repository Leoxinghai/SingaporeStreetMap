// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core.ui.sidemenu;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xinghai.mycurve.R;

// Referenced classes of package streetdirectory.mobile.core.ui.sidemenu:
//            SDSideMenuItem, SideMenuLayout

public class SDSideMenuHeader extends SDSideMenuItem
{
    class SDSideMenuHeaderHolder
    {

        public TextView textViewTitle;

        SDSideMenuHeaderHolder()
        {
            super();
        }
    }


    public SDSideMenuHeader(String s)
    {
        Text1 = "Untitles";
        Text1 = s;
    }

    protected Object createViewHolder(View view)
    {
        SDSideMenuHeaderHolder sdsidemenuheaderholder = new SDSideMenuHeaderHolder();
        sdsidemenuheaderholder.textViewTitle = (TextView)view.findViewById(R.id.TitleLabel);
        return sdsidemenuheaderholder;
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_menu_header;
    }

    protected Class getViewHolderClass()
    {
        return SDSideMenuHeader.class;
    }

    protected void populateViewHolder(Object obj)
    {
        ((SDSideMenuHeaderHolder)obj).textViewTitle.setText(Text1);
    }

    public String Text1;
}
