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
import streetdirectory.mobile.service.SDHttpServiceOutput;

public class FriendListAdapter extends BaseAdapter
{
    public static interface FriendListAdapterListener
    {

        public abstract void onLoadMore(int i);

        public abstract void onPhotoClicked(FriendListServiceOutput friendlistserviceoutput);

        public abstract void onPhotoNotFound(FriendListServiceOutput friendlistserviceoutput, int i, int j);
    }

    private static class FriendListGridViewHolder
    {

        public ImageButton photo;
        public TextView title;

        private FriendListGridViewHolder()
        {
        }

    }


    public FriendListAdapter(Context context)
    {
        mData = new SDHttpServiceOutput();
        mContext = context;
        mImages = new BitmapMemoryCaching(mContext);
    }

    public void addItem(FriendListServiceOutput friendlistserviceoutput)
    {
        mData.childs.add(friendlistserviceoutput);
    }

    public void addItems(Collection collection)
    {
        mData.childs.addAll(collection);
    }

    public void clearData()
    {
        mData.total = 0L;
        mData.childs.clear();
    }

    public int getChildSize()
    {
        return mData.childs.size();
    }

    public int getCount()
    {
        int j = mData.childs.size();
        if(j > 0)
        {
            int i = j;
            if((long)j < mData.total)
                i = j + 1;
            return i;
        } else
        {
            return 16;
        }
    }

    public FriendListServiceOutput getItem(int i)
    {
        if(i < mData.childs.size())
            return (FriendListServiceOutput)mData.childs.get(i);
        else
            return null;
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, final ViewGroup holder)
    {
        FriendListServiceOutput friendlistserviceoutput;
        Bitmap bitmap;
        final FriendListGridViewHolder temp;
        if(view == null)
        {
            view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.grid_friend_list, holder, false);
            temp = new FriendListGridViewHolder();
            temp.photo = (ImageButton)view.findViewById(R.id.imagebutton_photo);
            temp.title = (TextView)view.findViewById(R.id.textview_title);
            view.setTag(holder);


            if(mTreeObserver == null && mImageSize == 0)
            {
                mTreeObserver = ((FriendListGridViewHolder) (temp)).photo.getViewTreeObserver();
                mTreeObserver.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                    public void onGlobalLayout()
                    {
                        temp.photo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        mImageSize = temp.photo.getWidth();
                        mTreeObserver = null;
                        notifyDataSetChanged();
                    }
                });

            }
            ((FriendListGridViewHolder) (temp)).photo.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view1)
                {
                    if(mFriendListAdapterListener != null)
                    {
                        FriendListServiceOutput temp2 = (FriendListServiceOutput)view1.getTag();
                        if(view1 != null)
                            mFriendListAdapterListener.onPhotoClicked(temp2);
                    }
                }

            });
        } else
        {
            temp = (FriendListGridViewHolder)view.getTag();
        }
        if(mData.childs.size() <= 0) {
            ((FriendListGridViewHolder) (temp)).photo.setImageResource(R.drawable.facebook_thumb);
            ((FriendListGridViewHolder) (temp)).photo.setTag(null);
            ((FriendListGridViewHolder) (temp)).title.setText("");
            return view;
        } else {
            for (;i < mData.childs.size();) {
                friendlistserviceoutput = getItem(i);
                bitmap = (Bitmap) mImages.get(friendlistserviceoutput.userID);
                if (bitmap == null) {
                    if (mImageSize != 0 && mFriendListAdapterListener != null) {
                        ((FriendListGridViewHolder) (temp)).photo.setImageResource(R.drawable.facebook_thumb);
                        mFriendListAdapterListener.onPhotoNotFound(friendlistserviceoutput, mImageSize, mImageSize);
                    }
                } else
                    ((FriendListGridViewHolder) (temp)).photo.setImageBitmap(bitmap);

            }
            ((FriendListGridViewHolder) (temp)).photo.setImageResource(R.drawable.facebook_thumb);
            ((FriendListGridViewHolder) (temp)).photo.setTag(null);
            ((FriendListGridViewHolder) (temp)).title.setText("Loading...");
            if(mFriendListAdapterListener == null)
                return view;
            mFriendListAdapterListener.onLoadMore(mData.childs.size());
            return view;

        }
        //((FriendListGridViewHolder) (temp)).photo.setTag(friendlistserviceoutput);
        //((FriendListGridViewHolder) (temp)).title.setText(friendlistserviceoutput.userName);
        //return view;
    }

    public void putImage(String s, Bitmap bitmap)
    {
        if(s != null && bitmap != null)
            mImages.put(s, bitmap);
    }

    public void removeAllData()
    {
        mData.childs.clear();
    }

    public void setFriendListAdapterListener(FriendListAdapterListener friendlistadapterlistener)
    {
        mFriendListAdapterListener = friendlistadapterlistener;
    }

    public void setTotal(long l)
    {
        mData.total = l;
    }

    public static final int DUMMY_CHILD_COUNT = 16;
    private Context mContext;
    private SDHttpServiceOutput mData;
    private FriendListAdapterListener mFriendListAdapterListener;
    private int mImageSize;
    private BitmapMemoryCaching mImages;
    private ViewTreeObserver mTreeObserver;


/*
    static int access$102(FriendListAdapter friendlistadapter, int i)
    {
        friendlistadapter.mImageSize = i;
        return i;
    }

*/


/*
    static ViewTreeObserver access$202(FriendListAdapter friendlistadapter, ViewTreeObserver viewtreeobserver)
    {
        friendlistadapter.mTreeObserver = viewtreeobserver;
        return viewtreeobserver;
    }

*/

}
