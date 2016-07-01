// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.tips;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.BitmapMemoryCaching;
import streetdirectory.mobile.modules.tips.service.TipsServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.tips:
//            TipsCellViewHolder

public class TipsAdapter extends BaseAdapter
{
    public static interface OnImageNotFoundListener
    {

        public abstract void onImageNotFound(String s, int i, int j, int k);
    }

    public static interface OnLoadMoreListener
    {

        public abstract void onLoadMoreList();
    }

    public static interface OnReplyClickedListener
    {

        public abstract void onReplyClickedListener(String s, int i, String s1, String s2, String s3, String s4);
    }


    public TipsAdapter(Context context)
    {
        mShowReplyTips = false;
        initialize(context);
    }

    public TipsAdapter(Context context, boolean flag)
    {
        mShowReplyTips = false;
        initialize(context);
        mShowReplyTips = true;
    }

    private void initialize(Context context)
    {
        mContext = context;
        mImages = new BitmapMemoryCaching(mContext);
        mData = new ArrayList();
    }

    public void add(TipsServiceOutput tipsserviceoutput)
    {
        mData.add(tipsserviceoutput);
    }

    public int getCount()
    {
        int j = mData.size();
        if(j > 0)
        {
            int i = j;
            if(j < total)
                i = j + 1;
            return i;
        } else
        {
            return 0;
        }
    }


    public TipsServiceOutput getItem(int i)
    {
        return (TipsServiceOutput)mData.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public int getItemViewType(int i)
    {
        return i >= mData.size() ? 1 : 0;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        int j = getItemViewType(i);
        final TipsServiceOutput data;
        TipsCellViewHolder temp;
        View lview = view;
        if(j == 0)
        {
            String s;
            if(view == null)
            {
                View view1 = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_tips, viewgroup, false);
                final TipsCellViewHolder holder = new TipsCellViewHolder();
                holder.photoView = (ImageView)view1.findViewById(R.id.UserImage);
                holder.titleLabel = (TextView)view1.findViewById(R.id.TitleLabel);
                holder.detailLabel = (TextView)view1.findViewById(R.id.DetailLabel);
                holder.dateLabel = (TextView)view1.findViewById(R.id.DateLabel);
                holder.buttonReply = (Button)view1.findViewById(R.id.button_reply_tips);
                view1.setTag(holder);
                temp = holder;
                view = view1;
                if(mTreeObserver == null)
                {
                    temp = holder;
                    view = view1;
                    if(mImageSize == 0)
                    {
                        mTreeObserver = holder.photoView.getViewTreeObserver();
                        mTreeObserver.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                            public void onGlobalLayout()
                            {
                                holder.photoView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                mTreeObserver = null;
                                mImageSize = holder.photoView.getWidth();
                                notifyDataSetChanged();
                            }
                        });
                        view = view1;
                        temp = holder;
                    }
                }
            } else
            {
                temp = (TipsCellViewHolder)view.getTag();
            }
            data = getItem(i);
            s = data.generateUserPhotoURL(mImageSize, mImageSize);
            if(s != null)
            {
                Bitmap bitmap = (Bitmap)mImages.get(s);
                if(bitmap == null)
                {
                    ((TipsCellViewHolder) (temp)).photoView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.facebook_thumb));
                    if(mImageNotFoundListener != null)
                        mImageNotFoundListener.onImageNotFound(s, i, mImageSize, mImageSize);
                } else
                {
                    ((TipsCellViewHolder) (temp)).photoView.setImageBitmap(bitmap);
                }
            } else
            {
                ((TipsCellViewHolder) (temp)).photoView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.facebook_thumb));
            }
            ((TipsCellViewHolder) (temp)).titleLabel.setText(Html.fromHtml(data.name));
            ((TipsCellViewHolder) (temp)).detailLabel.setText(Html.fromHtml(data.comment));
            ((TipsCellViewHolder) (temp)).dateLabel.setText(Html.fromHtml(data.dateTime));
            if(mShowReplyTips)
            {
                ((TipsCellViewHolder) (temp)).buttonReply.setVisibility(View.VISIBLE);
                ((TipsCellViewHolder) (temp)).buttonReply.setText((new StringBuilder()).append(data.totalReplies).append(" replies").toString());
            }
            ((TipsCellViewHolder) (temp)).buttonReply.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view3)
                {
                    if(mReplyClickedListener != null)
                        mReplyClickedListener.onReplyClickedListener(data.commentID, data.totalReplies, data.uid, data.comment, data.name, data.dateTime);
                }

            });
        } else
        {
            if(j == 1)
            {
                View view2 = view;
                if(view == null)
                    view2 = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_load_more, viewgroup, false);

                lview = view2;
                if(mLoadMoreListener != null)
                {
                    mLoadMoreListener.onLoadMoreList();
                    return view2;
                }
            }
        }
        return lview;
    }

    public int getViewTypeCount()
    {
        return 2;
    }

    public void putImage(String s, Bitmap bitmap)
    {
        if(s != null && bitmap != null)
            mImages.put(s, bitmap);
    }

    public void removeAllData()
    {
        mData.clear();
    }

    public void setOnImageNotFoundListener(OnImageNotFoundListener onimagenotfoundlistener)
    {
        mImageNotFoundListener = onimagenotfoundlistener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onloadmorelistener)
    {
        mLoadMoreListener = onloadmorelistener;
    }

    public void setOnReplyClickedListener(OnReplyClickedListener onreplyclickedlistener)
    {
        mReplyClickedListener = onreplyclickedlistener;
    }

    public static final int LOAD_MORE_CELL = 1;
    public static final int TIPS_CELL = 0;
    private Context mContext;
    private ArrayList mData;
    private OnImageNotFoundListener mImageNotFoundListener;
    private int mImageSize;
    private BitmapMemoryCaching mImages;
    private OnLoadMoreListener mLoadMoreListener;
    private OnReplyClickedListener mReplyClickedListener;
    private boolean mShowReplyTips;
    private ViewTreeObserver mTreeObserver;
    public int total;


/*
    static ViewTreeObserver access$002(TipsAdapter tipsadapter, ViewTreeObserver viewtreeobserver)
    {
        tipsadapter.mTreeObserver = viewtreeobserver;
        return viewtreeobserver;
    }

*/


/*
    static int access$102(TipsAdapter tipsadapter, int i)
    {
        tipsadapter.mImageSize = i;
        return i;
    }

*/

}
