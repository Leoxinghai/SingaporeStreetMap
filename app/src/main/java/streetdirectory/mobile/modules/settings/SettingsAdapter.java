// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.settings;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.SDLogger;

// Referenced classes of package streetdirectory.mobile.modules.settings:
//            SettingsTableData, SettingsHeaderTableData, SettingsFooterTableData, AutoLockTableData

public class SettingsAdapter extends BaseExpandableListAdapter
{
    public static class SettingsItemViewHolder
    {

        public TextView detailLabel;
        public ImageView icon;
        public ImageView imageArrow;
        public ProgressBar progressFB;
        public ImageView separator;
        public TextView titleLabel;

        public SettingsItemViewHolder()
        {
        }
    }

    public static class ViewHolder
    {

        public ViewHolder()
        {
        }
    }


    public SettingsAdapter(Context context)
    {
        mContext = context;
        if(mSectionArray == null)
            mSectionArray = new ArrayList();
    }

    public void addSectionArray(ArrayList arraylist)
    {
        mSectionArray.add(arraylist);
    }


    public SettingsTableData getChild(int i, int j)
    {
        return (SettingsTableData)((ArrayList)mSectionArray.get(i)).get(j);
    }

    public long getChildId(int i, int j)
    {
        return (long)j;
    }

    public View getChildView(int i, int j, boolean flag, View view, ViewGroup viewgroup)
    {
        boolean flag3 = true;
        View lView = null;
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(((ArrayList)mSectionArray.get(i)).get(j) instanceof SettingsHeaderTableData)
        {
            view = layoutInflater.inflate(R.layout.cell_rounded_header, null);
        } else
        {
            if(((ArrayList)mSectionArray.get(i)).get(j) instanceof SettingsFooterTableData)
                return layoutInflater.inflate(R.layout.cell_rounded_footer, null);
            if(((ArrayList)mSectionArray.get(i)).get(j) instanceof AutoLockTableData)
                return layoutInflater.inflate(R.layout.cell_auto_lock, null);
            lView = layoutInflater.inflate(R.layout.cell_settings, null);
            SettingsItemViewHolder settingsitemviewholder = new SettingsItemViewHolder();
            settingsitemviewholder.icon = (ImageView)lView.findViewById(R.id.image_view_icon);
            settingsitemviewholder.titleLabel = (TextView)lView.findViewById(R.id.text_view_title_label);
            settingsitemviewholder.detailLabel = (TextView)lView.findViewById(R.id.text_view_detail_label);
            settingsitemviewholder.separator = (ImageView)lView.findViewById(R.id.image_view_separator);
            settingsitemviewholder.progressFB = (ProgressBar)lView.findViewById(R.id.progressbar_fb);
            SettingsTableData settingstabledata = getChild(i, j);
            android.graphics.Bitmap bitmap = ((SettingsTableData)((ArrayList)mSectionArray.get(i)).get(j)).mImageIcon;

            if(settingsitemviewholder != null)
            {
                boolean flag1;
                boolean flag2;
                if(settingsitemviewholder.icon != null)
                    if(bitmap != null)
                        settingsitemviewholder.icon.setImageBitmap(bitmap);
                    else
                        settingsitemviewholder.icon.setVisibility(View.INVISIBLE);
                if(settingsitemviewholder.titleLabel != null)
                {
                    if(settingstabledata.mTitle != null)
                        flag1 = true;
                    else
                        flag1 = false;
                    if(!"".equals(settingstabledata.mTitle))
                        flag2 = true;
                    else
                        flag2 = false;
                    if(flag1 & flag2)
                        settingsitemviewholder.titleLabel.setText(settingstabledata.mTitle);
                    else
                        settingsitemviewholder.titleLabel.setVisibility(View.INVISIBLE);
                }
                if(settingsitemviewholder.detailLabel != null)
                {
                    if(settingstabledata.mDetail != null)
                        flag1 = true;
                    else
                        flag1 = false;
                    if(!"".equals(settingstabledata.mDetail))
                        flag2 = flag3;
                    else
                        flag2 = false;
                    if(flag1 & flag2)
                        settingsitemviewholder.detailLabel.setText(settingstabledata.mDetail);
                    else
                        settingsitemviewholder.detailLabel.setVisibility(View.INVISIBLE);
                }
                view = viewgroup;
                if(settingsitemviewholder.separator != null)
                    if(j < getChildrenCount(i) - 2)
                    {
                        settingsitemviewholder.separator.setVisibility(View.VISIBLE);
                        return viewgroup;
                    } else
                    {
                        settingsitemviewholder.separator.setVisibility(View.INVISIBLE);
                        return viewgroup;
                    }
            }
        }
        return lView;
    }

    public int getChildrenCount(int i)
    {
        SDLogger.debug((new StringBuilder()).append("children count").append(((ArrayList)mSectionArray.get(i)).size()).toString());
        return ((ArrayList)mSectionArray.get(i)).size();
    }

    public Object getGroup(int i)
    {
        return mSectionArray.get(i);
    }

    public int getGroupCount()
    {
        SDLogger.debug((new StringBuilder()).append("group count").append(mSectionArray.size()).toString());
        return mSectionArray.size();
    }

    public long getGroupId(int i)
    {
        return (long)i;
    }

    public View getGroupView(int i, boolean flag, View view, ViewGroup viewgroup)
    {
        return new View(mContext);
    }

    public boolean hasStableIds()
    {
        return true;
    }

    public boolean isChildSelectable(int i, int j)
    {
        return true;
    }

    private Context mContext;
    private ArrayList mSectionArray;
}
