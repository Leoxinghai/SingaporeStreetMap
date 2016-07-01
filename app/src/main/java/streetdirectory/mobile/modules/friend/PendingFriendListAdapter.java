// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.friend;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Collection;
import streetdirectory.mobile.core.BitmapMemoryCaching;
import streetdirectory.mobile.modules.friend.service.FriendListServiceOutput;
import streetdirectory.mobile.modules.friend.service.PendingFriendListServiceOutput;
import streetdirectory.mobile.modules.friend.service.RequestFriendListServiceOutput;

public class PendingFriendListAdapter extends BaseExpandableListAdapter
{
    public static interface PendingFriendListAdapterListener
    {

        public abstract void onConfirmButtonClicked(FriendListServiceOutput friendlistserviceoutput);

        public abstract void onPhotoNotFound(FriendListServiceOutput friendlistserviceoutput, int i, int j);

        public abstract void onRejectButtonClicked(FriendListServiceOutput friendlistserviceoutput);
    }

    public static class PendingFriendListItemViewHolder
    {

        public LinearLayout buttonLayout;
        public ImageView confirm;
        public ImageView photo;
        public ImageView reject;
        public TextView title;

        public PendingFriendListItemViewHolder()
        {
        }
    }


    public PendingFriendListAdapter(Context context)
    {
        mRequestData = new ArrayList();
        mPendingData = new ArrayList();
        mContext = context;
        mImages = new BitmapMemoryCaching(context);
    }

    public void addPendingItems(Collection collection)
    {
        mPendingData.addAll(collection);
    }

    public void addRequestItems(Collection collection)
    {
        mRequestData.addAll(collection);
    }


    public FriendListServiceOutput getChild(int i, int j)
    {
        if(i == 0 && j < mRequestData.size())
            return (FriendListServiceOutput)mRequestData.get(j);
        else
            return (FriendListServiceOutput)mPendingData.get(j);
    }

    public long getChildId(int i, int j)
    {
        return (long)j;
    }

    public View getChildView(int i, int j, boolean flag, View view, final ViewGroup holder)
    {
        FriendListServiceOutput friendlistserviceoutput;
        Bitmap bitmap;
        PendingFriendListItemViewHolder temp;
        if(view == null)
        {
            view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_account_pending_item, null);
            temp = new PendingFriendListItemViewHolder();
            temp.photo = (ImageView)view.findViewById(R.id.imageview_photo);
            if(mTreeObserver == null && mImageSize == 0)
            {
                mTreeObserver = ((PendingFriendListItemViewHolder) (temp)).photo.getViewTreeObserver();
                final PendingFriendListItemViewHolder holder0 = temp;
                mTreeObserver.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                    public void onGlobalLayout()
                    {
                        holder0.photo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        mImageSize = holder0.photo.getWidth();
                        mTreeObserver = null;
                        notifyDataSetChanged();
                    }

                });
            }
            temp.title = (TextView)view.findViewById(R.id.textview_title);
            temp.buttonLayout = (LinearLayout)view.findViewById(R.id.layout_confirm_reject);
            temp.confirm = (ImageView)view.findViewById(R.id.button_confirm);
            ((PendingFriendListItemViewHolder) (temp)).confirm.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view1)
                {
                    if(mPendingFriendListAdapterListener != null)
                    {
                        FriendListServiceOutput temp;
                        temp = (FriendListServiceOutput)view1.getTag();
                        mPendingFriendListAdapterListener.onConfirmButtonClicked(temp);
                    }
                }

            });
            temp.reject = (ImageView)view.findViewById(R.id.button_reject);
            ((PendingFriendListItemViewHolder) (temp)).reject.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view1)
                {
                    if(mPendingFriendListAdapterListener != null)
                    {
                        FriendListServiceOutput friendListServiceOutput = (FriendListServiceOutput)view1.getTag();
                        mPendingFriendListAdapterListener.onRejectButtonClicked(friendListServiceOutput);
                    }
                }

            });
            view.setTag(holder);
        } else
        {
            temp = (PendingFriendListItemViewHolder)view.getTag();
        }
        friendlistserviceoutput = getChild(i, j);
        bitmap = (Bitmap)mImages.get(friendlistserviceoutput.userID);
        if(bitmap != null)
            ((PendingFriendListItemViewHolder) (temp)).photo.setImageBitmap(bitmap);
        else
        if(mImageSize != 0 && mPendingFriendListAdapterListener != null)
        {
            ((PendingFriendListItemViewHolder) (temp)).photo.setImageResource(R.drawable.facebook_thumb);
            mPendingFriendListAdapterListener.onPhotoNotFound(friendlistserviceoutput, mImageSize, mImageSize);
        }
        ((PendingFriendListItemViewHolder) (temp)).confirm.setTag(friendlistserviceoutput);
        ((PendingFriendListItemViewHolder) (temp)).reject.setTag(friendlistserviceoutput);
        ((PendingFriendListItemViewHolder) (temp)).title.setText(friendlistserviceoutput.userName);
        if(friendlistserviceoutput instanceof RequestFriendListServiceOutput)
        {
            ((PendingFriendListItemViewHolder) (temp)).buttonLayout.setVisibility(View.VISIBLE);
            return view;
        } else
        {
            ((PendingFriendListItemViewHolder) (temp)).buttonLayout.setVisibility(View.INVISIBLE);
            return view;
        }
    }

    public int getChildrenCount(int i)
    {
        if(i == 0 && mRequestData.size() > 0)
            return mRequestData.size();
        else
            return mPendingData.size();
    }


    public String getGroup(int i)
    {
        if(i == 0 && mRequestData.size() > 0)
            return "You have been invited";
        else
            return "You have added these people";
    }

    public int getGroupCount()
    {
        int i = 0;
        if(mRequestData.size() > 0)
            i = 0 + 1;
        int j = i;
        if(mPendingData.size() > 0)
            j = i + 1;
        return j;
    }

    public long getGroupId(int i)
    {
        return (long)i;
    }

    public View getGroupView(int i, boolean flag, View view, ViewGroup viewgroup)
    {
        if(view == null)
        {
            view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_account_pending_header, null);
            View temp = (TextView)view.findViewById(R.id.textview_title);
            view.setTag(viewgroup);
        } else
        {
            TextView temp2 = (TextView)view.getTag();
            temp2.setText(getGroup(i));
        }
        return view;
    }

    public int getPendingDataSize()
    {
        return mPendingData.size();
    }

    public int getRequestDataSize()
    {
        return mRequestData.size();
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
            mImages.put(s, bitmap);
    }

    public void removeAllData()
    {
        mRequestData.clear();
        mPendingData.clear();
    }

    public void removePendingItem(PendingFriendListServiceOutput pendingfriendlistserviceoutput)
    {
        mPendingData.remove(pendingfriendlistserviceoutput);
    }

    public void removeRequestItem(RequestFriendListServiceOutput requestfriendlistserviceoutput)
    {
        mRequestData.remove(requestfriendlistserviceoutput);
    }

    public void setPendingFriendListAdapterListener(PendingFriendListAdapterListener pendingfriendlistadapterlistener)
    {
        mPendingFriendListAdapterListener = pendingfriendlistadapterlistener;
    }

    public static final int PENDING_GROUP = 1;
    public static final String PENDING_GROUP_TITLE = "You have added these people";
    public static final int REQUEST_GROUP = 0;
    public static final String REQUEST_GROUP_TITLE = "You have been invited";
    private Context mContext;
    private int mImageSize;
    private BitmapMemoryCaching mImages;
    private ArrayList mPendingData;
    private PendingFriendListAdapterListener mPendingFriendListAdapterListener;
    private ArrayList mRequestData;
    private ViewTreeObserver mTreeObserver;


/*
    static int access$002(PendingFriendListAdapter pendingfriendlistadapter, int i)
    {
        pendingfriendlistadapter.mImageSize = i;
        return i;
    }

*/


/*
    static ViewTreeObserver access$102(PendingFriendListAdapter pendingfriendlistadapter, ViewTreeObserver viewtreeobserver)
    {
        pendingfriendlistadapter.mTreeObserver = viewtreeobserver;
        return viewtreeobserver;
    }

*/

}
