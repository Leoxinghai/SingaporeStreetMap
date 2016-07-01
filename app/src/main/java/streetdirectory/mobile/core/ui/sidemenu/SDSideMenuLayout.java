// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core.ui.sidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;

// Referenced classes of package streetdirectory.mobile.core.ui.sidemenu:
//            SideMenuLayout, SDSideMenuAdapter, SDSideMenuProvider, SDSideMenuItem

public class SDSideMenuLayout extends SideMenuLayout
{

    public SDSideMenuLayout(Context context)
    {
        super(context);
    }

    public SDSideMenuLayout(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public void notifyDataSetChanged()
    {
        adapter.notifyDataSetChanged();
    }

    protected void populateBackView(Context context)
    {
        final ListView listView = new ListView(context);
        android.widget.FrameLayout.LayoutParams layoutparams = new android.widget.FrameLayout.LayoutParams(-1, -1);
        layoutparams.gravity = 51;
        listView.setLayoutParams(layoutparams);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        adapter = new SDSideMenuAdapter(context, 0, SDSideMenuProvider.items, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                ((SDSideMenuItem)listView.getItemAtPosition(i)).execute(getContext(), SDSideMenuLayout.this);
            }

        });
        backview.addView(listView);
    }

    private SDSideMenuAdapter adapter;
}
