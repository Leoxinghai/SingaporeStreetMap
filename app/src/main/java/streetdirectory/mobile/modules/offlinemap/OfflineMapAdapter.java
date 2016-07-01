// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.offlinemap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.text.SimpleDateFormat;
import java.util.*;
import streetdirectory.mobile.core.BitmapMemoryCaching;
import streetdirectory.mobile.core.MathTools;
import streetdirectory.mobile.modules.offlinemap.service.OfflineMapListServiceOutput;
import streetdirectory.mobile.modules.offlinemap.service.OfflineMapPreference;

// Referenced classes of package streetdirectory.mobile.modules.offlinemap:
//            OfflineMapItemCellViewHolder, OfflineMapSectionHeader, OfflineMapHeaderCellViewHolder

public class OfflineMapAdapter extends BaseExpandableListAdapter
{
    public static interface OfflineMapAdapterListener
    {

        public abstract void onDeleteButtonClicked(int i, String s, int j);

        public abstract void onDownloadButtonClicked(int i, String s, int j);

        public abstract void onImageClicked(Bitmap bitmap, OfflineMapListServiceOutput offlinemaplistserviceoutput);

        public abstract void onImageNotFound(String s, int i, int j);

        public abstract void onPauseButtonClicked(int i, String s, int j);

        public abstract void onResumeButtonClicked(int i, String s, int j);

        public abstract void onUpdateButtonClicked(int i, String s, int j);

        public abstract void onUpdateNotAvailableButtonClicked(int i, String s, int j);
    }


    public OfflineMapAdapter(Context context)
    {
        datas = new ArrayList();
        mContext = context;
        images = new BitmapMemoryCaching(context);
        mHeaderFlagSize = Math.round(TypedValue.applyDimension(1, 21F, mContext.getResources().getDisplayMetrics()));
        mMapImageSize = Math.round(TypedValue.applyDimension(1, 100F, mContext.getResources().getDisplayMetrics()));
    }

    private void initHolderEvent(final OfflineMapItemCellViewHolder holder)
    {
        holder.iconButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mListener != null)
                {
                    OfflineMapListServiceOutput temp = getChild(holder.groupPosition, holder.childPosition);
                    Bitmap bitmap = (Bitmap)images.get(((OfflineMapListServiceOutput) (temp)).thumbID);
                    if(bitmap != null)
                        mListener.onImageClicked(bitmap, temp);
                }
            }

        });
        holder.downloadButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mListener != null)
                    mListener.onDownloadButtonClicked(holder.packageID, holder.packageName, holder.parentID);
            }

        });
        holder.resumeButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mListener != null)
                    mListener.onResumeButtonClicked(holder.packageID, holder.packageName, holder.parentID);
            }

        });
        holder.updateButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mListener != null)
                    mListener.onUpdateButtonClicked(holder.packageID, holder.packageName, holder.parentID);
            }

        });
        holder.updateNotAvailableButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mListener != null)
                    mListener.onUpdateNotAvailableButtonClicked(holder.packageID, holder.packageName, holder.parentID);
            }

        });
        holder.pauseButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mListener != null)
                    mListener.onPauseButtonClicked(holder.packageID, holder.packageName, holder.parentID);
            }

        });
        holder.deleteButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mListener != null)
                    mListener.onDeleteButtonClicked(holder.packageID, holder.packageName, holder.parentID);
            }

        });
    }

    public void addChild(int i, OfflineMapListServiceOutput offlinemaplistserviceoutput)
    {
        Iterator iterator = datas.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            OfflineMapSectionHeader offlinemapsectionheader = (OfflineMapSectionHeader)iterator.next();
            if(offlinemapsectionheader.packageID != i)
                continue;
            offlinemapsectionheader.childs.add(offlinemaplistserviceoutput);
            break;
        } while(true);
    }

    public void addChilds(int i, ArrayList arraylist)
    {
        Iterator iterator = datas.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            OfflineMapSectionHeader offlinemapsectionheader = (OfflineMapSectionHeader)iterator.next();
            if(offlinemapsectionheader.packageID != i)
                continue;
            offlinemapsectionheader.childs = arraylist;
            break;
        } while(true);
    }

    public void addSectionHeader(OfflineMapListServiceOutput offlinemaplistserviceoutput)
    {
        OfflineMapSectionHeader temp = new OfflineMapSectionHeader(offlinemaplistserviceoutput.packageID, offlinemaplistserviceoutput.name, offlinemaplistserviceoutput.thumbID);
        datas.add(temp);
    }

    public void addSectionHeader(OfflineMapListServiceOutput offlinemaplistserviceoutput, ArrayList arraylist)
    {
        OfflineMapSectionHeader temp = new OfflineMapSectionHeader(offlinemaplistserviceoutput.packageID, offlinemaplistserviceoutput.name, offlinemaplistserviceoutput.thumbID, arraylist);
        datas.add(temp);
    }

    public void clear()
    {
        datas.clear();
        images.evictAll();
        notifyDataSetChanged();
    }


    public OfflineMapListServiceOutput getChild(int i, int j)
    {
        return (OfflineMapListServiceOutput)((OfflineMapSectionHeader)datas.get(i)).childs.get(j);
    }

    public long getChildId(int i, int j)
    {
        return (long)getChild(i, j).packageID;
    }

    public View getChildView(int i, int j, boolean flag, View view, ViewGroup viewgroup)
    {
        OfflineMapListServiceOutput offlinemaplistserviceoutput;
        Object obj;
        OfflineMapItemCellViewHolder temp;
        if(view == null)
        {
            view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_offline_map_item, null);
            temp = new OfflineMapItemCellViewHolder(view);
            initHolderEvent(temp);
            view.setTag(viewgroup);
        } else
        {
            temp = (OfflineMapItemCellViewHolder)view.getTag();
        }
        offlinemaplistserviceoutput = getChild(i, j);
        temp.packageID = offlinemaplistserviceoutput.packageID;
        temp.parentID = offlinemaplistserviceoutput.parentID;
        temp.packageName = offlinemaplistserviceoutput.name;
        temp.groupPosition = i;
        temp.childPosition = j;
        obj = MathTools.sizeToString(offlinemaplistserviceoutput.totalSize);
        ((OfflineMapItemCellViewHolder) (temp)).titleLabel.setText((new StringBuilder()).append(offlinemaplistserviceoutput.name).append(" (").append(((String) (obj))).append(")").toString());
        i = OfflineMapPreference.getStatus(offlinemaplistserviceoutput.packageID);
        if(i == 0)
        {
            ((OfflineMapItemCellViewHolder) (temp)).detailLabel.setVisibility(View.VISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).progressLayout.setVisibility(View.INVISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).downloadButton.setVisibility(View.VISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).buttonLayout.setVisibility(View.INVISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).deleteButton.setVisibility(View.INVISIBLE);
            double d = offlinemaplistserviceoutput.totalSize / 102400D;
            ((OfflineMapItemCellViewHolder) (temp)).detailLabel.setText((new StringBuilder()).append("Estimated time: ").append(MathTools.timeToString(d * 15D)).toString());
        } else
        if(i == 1 || i == 2)
        {
            ((OfflineMapItemCellViewHolder) (temp)).detailLabel.setVisibility(View.INVISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).progressLayout.setVisibility(View.VISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).downloadButton.setVisibility(View.INVISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).buttonLayout.setVisibility(View.VISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).deleteButton.setVisibility(View.VISIBLE);
            double d1;
            double d2;
            float f;
            if(i == 1)
            {
                ((OfflineMapItemCellViewHolder) (temp)).resumeButton.setVisibility(View.INVISIBLE);
                ((OfflineMapItemCellViewHolder) (temp)).pauseButton.setVisibility(View.VISIBLE);
                ((OfflineMapItemCellViewHolder) (temp)).statusLabel.setText("Downloading...");
            } else
            {
                ((OfflineMapItemCellViewHolder) (temp)).resumeButton.setVisibility(View.VISIBLE);
                ((OfflineMapItemCellViewHolder) (temp)).pauseButton.setVisibility(View.INVISIBLE);
                ((OfflineMapItemCellViewHolder) (temp)).statusLabel.setText("Paused");
            }
            ((OfflineMapItemCellViewHolder) (temp)).updateButton.setVisibility(View.INVISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).updateNotAvailableButton.setVisibility(View.INVISIBLE);
            f = OfflineMapPreference.getProgress(offlinemaplistserviceoutput.packageID);
            ((OfflineMapItemCellViewHolder) (temp)).progressBar.setProgress((int)Math.floor(100F * f));
            d1 = f;
            d2 = offlinemaplistserviceoutput.totalSize;
            ((OfflineMapItemCellViewHolder) (temp)).progressLabel.setText((new StringBuilder()).append(MathTools.sizeToString(d1 * d2)).append(" of ").append(((String) (obj))).toString());
        } else
        if(i == 3)
        {
            ((OfflineMapItemCellViewHolder) (temp)).detailLabel.setVisibility(View.VISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).progressLayout.setVisibility(View.INVISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).downloadButton.setVisibility(View.INVISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).buttonLayout.setVisibility(View.VISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).deleteButton.setVisibility(View.VISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).resumeButton.setVisibility(View.INVISIBLE);
            ((OfflineMapItemCellViewHolder) (temp)).pauseButton.setVisibility(View.INVISIBLE);
            obj = OfflineMapPreference.getCompletionDate(offlinemaplistserviceoutput.packageID);
            if(obj != null)
            {
                if((new Date()).getTime() - ((Date) (obj)).getTime() > 0x48190800L)
                {
                    ((OfflineMapItemCellViewHolder) (temp)).updateButton.setVisibility(View.VISIBLE);
                    ((OfflineMapItemCellViewHolder) (temp)).updateNotAvailableButton.setVisibility(View.INVISIBLE);
                } else
                {
                    ((OfflineMapItemCellViewHolder) (temp)).updateButton.setVisibility(View.INVISIBLE);
                    ((OfflineMapItemCellViewHolder) (temp)).updateNotAvailableButton.setVisibility(View.VISIBLE);
                }
                obj = (new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)).format(((Date) (obj)));
                ((OfflineMapItemCellViewHolder) (temp)).detailLabel.setText((new StringBuilder()).append("Dowloaded: ").append(((String) (obj))).toString());
            } else
            {
                ((OfflineMapItemCellViewHolder) (temp)).updateButton.setVisibility(View.VISIBLE);
                ((OfflineMapItemCellViewHolder) (temp)).updateNotAvailableButton.setVisibility(View.INVISIBLE);
                ((OfflineMapItemCellViewHolder) (temp)).detailLabel.setText("Dowloaded: ");
            }
        }
        obj = (Bitmap)images.get(offlinemaplistserviceoutput.thumbID);
        if(obj == null)
        {
            ((OfflineMapItemCellViewHolder) (temp)).iconButton.setImageResource(R.drawable.offline_map_no_thumbnail);
            if(mListener != null)
                mListener.onImageNotFound(offlinemaplistserviceoutput.thumbID, mMapImageSize, mMapImageSize);
            return view;
        } else
        {
            ((OfflineMapItemCellViewHolder) (temp)).iconButton.setImageBitmap(((Bitmap) (obj)));
            return view;
        }
    }

    public int getChildrenCount(int i)
    {
        return ((OfflineMapSectionHeader)datas.get(i)).childs.size();
    }


    public OfflineMapSectionHeader getGroup(int i)
    {
        return (OfflineMapSectionHeader)datas.get(i);
    }

    public int getGroupCount()
    {
        return datas.size();
    }

    public long getGroupId(int i)
    {
        return (long)getGroup(i).packageID;
    }

    public View getGroupView(int i, boolean flag, View view, ViewGroup viewgroup)
    {
        OfflineMapSectionHeader offlinemapsectionheader;
        Bitmap bitmap;
        OfflineMapHeaderCellViewHolder temp;
        if(view == null)
        {
            view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_offline_map_header, null);
            temp = new OfflineMapHeaderCellViewHolder();
            temp.iconImage = (ImageView)view.findViewById(R.id.IconImage);
            temp.titleLabel = (TextView)view.findViewById(R.id.TitleLabel);
            view.setTag(viewgroup);
        } else
        {
            temp = (OfflineMapHeaderCellViewHolder)view.getTag();
        }
        offlinemapsectionheader = getGroup(i);
        ((OfflineMapHeaderCellViewHolder) (temp)).titleLabel.setText(offlinemapsectionheader.name);
        bitmap = (Bitmap)images.get(offlinemapsectionheader.thumbID);
        if(bitmap == null)
        {
            ((OfflineMapHeaderCellViewHolder) (temp)).iconImage.setImageResource(R.drawable.offline_map_noflag);
            if(mListener != null)
                mListener.onImageNotFound(offlinemapsectionheader.thumbID, mHeaderFlagSize, mHeaderFlagSize);
            return view;
        } else
        {
            ((OfflineMapHeaderCellViewHolder) (temp)).iconImage.setImageBitmap(bitmap);
            return view;
        }
    }

    public boolean hasStableIds()
    {
        return true;
    }

    public boolean isChildSelectable(int i, int j)
    {
        return true;
    }

    public void putImage(String s, Bitmap bitmap)
    {
        if(s != null && bitmap != null)
            images.put(s, bitmap);
    }

    public void setListener(OfflineMapAdapterListener offlinemapadapterlistener)
    {
        mListener = offlinemapadapterlistener;
    }

    public static final int HEADER_FLAG_SIZE_DP = 21;
    public static final int MAP_IMAGE_SIZE_DP = 100;
    public ArrayList datas;
    public BitmapMemoryCaching images;
    private Context mContext;
    private int mHeaderFlagSize;
    private OfflineMapAdapterListener mListener;
    private int mMapImageSize;

}
