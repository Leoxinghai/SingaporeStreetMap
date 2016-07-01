// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.tips.reply;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.BitmapMemoryCaching;
import streetdirectory.mobile.modules.tips.TipsCellViewHolder;
import streetdirectory.mobile.modules.tips.reply.service.TipsReplyServiceOutput;

public class TipsReplyAdapter extends BaseAdapter
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

        public abstract void onReplyClickedListener();
    }


    public TipsReplyAdapter(Context context)
    {
        initialize(context);
    }

    private void initialize(Context context)
    {
        mContext = context;
        mImages = new BitmapMemoryCaching(mContext);
        mData = new ArrayList();
    }

    public void add(TipsReplyServiceOutput tipsreplyserviceoutput)
    {
        mData.add(tipsreplyserviceoutput);
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


    public TipsReplyServiceOutput getItem(int i)
    {
        return (TipsReplyServiceOutput)mData.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public int getItemViewType(int i)
    {
        return i != 0 || mData.size() >= total ? 0 : 1;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        int j = getItemViewType(i);
        Object obj;
        TipsCellViewHolder temp;

        if(j == 0)
        {
            TipsReplyServiceOutput tipsreplyserviceoutput;
            if(view == null)
            {
                View view1 = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_tips, viewgroup, false);
                final TipsCellViewHolder holder = new TipsCellViewHolder();
                holder.photoView = (ImageView)view1.findViewById(R.id.UserImage);
                holder.titleLabel = (TextView)view1.findViewById(R.id.TitleLabel);
                holder.detailLabel = (TextView)view1.findViewById(R.id.DetailLabel);
                holder.dateLabel = (TextView)view1.findViewById(R.id.DateLabel);
                holder.buttonReply = (Button)view1.findViewById(R.id.button_reply_tips);
                holder.buttonReply.setOnClickListener(new android.view.View.OnClickListener() {

                    public void onClick(View view3)
                    {
                        if(mReplyClickedListener != null)
                            mReplyClickedListener.onReplyClickedListener();
                    }

                }
);
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
            if(mData.size() < total)
                j = i - 1;
            else
                j = i;
            tipsreplyserviceoutput = getItem(j);
            obj = tipsreplyserviceoutput.generateUserPhotoURL(mImageSize, mImageSize);
            if(obj != null)
            {
                Bitmap bitmap = (Bitmap)mImages.get(obj);
                if(bitmap == null)
                {
                    ((TipsCellViewHolder) (temp)).photoView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.facebook_thumb));
                    if(mImageNotFoundListener != null)
                        mImageNotFoundListener.onImageNotFound(((String) (obj)), i, mImageSize, mImageSize);
                } else
                {
                    ((TipsCellViewHolder) (temp)).photoView.setImageBitmap(bitmap);
                }
            } else
            {
                ((TipsCellViewHolder) (temp)).photoView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.facebook_thumb));
            }
            if(tipsreplyserviceoutput.name != null)
                ((TipsCellViewHolder) (temp)).titleLabel.setText(Html.fromHtml(tipsreplyserviceoutput.name));
            if(tipsreplyserviceoutput.comment != null)
                ((TipsCellViewHolder) (temp)).detailLabel.setText(Html.fromHtml(tipsreplyserviceoutput.comment));
            obj = view;
            if(tipsreplyserviceoutput.dateTime != null)
            {
                ((TipsCellViewHolder) (temp)).dateLabel.setText(Html.fromHtml(tipsreplyserviceoutput.dateTime));
                obj = view;
            }
        } else
        {
            obj = view;
            if(j == 1)
            {
                View view2 = view;
                if(view == null)
                    view2 = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_load_all_replies, viewgroup, false);
                view2.setOnClickListener(new android.view.View.OnClickListener() {

                    public void onClick(View view3)
                    {
                        if(mLoadMoreListener != null)
                            mLoadMoreListener.onLoadMoreList();
                    }

                });
                return view2;
            }
        }
        return ((View) (obj));
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
    private ViewTreeObserver mTreeObserver;
    public int total;



/*
    static ViewTreeObserver access$102(TipsReplyAdapter tipsreplyadapter, ViewTreeObserver viewtreeobserver)
    {
        tipsreplyadapter.mTreeObserver = viewtreeobserver;
        return viewtreeobserver;
    }

*/


/*
    static int access$202(TipsReplyAdapter tipsreplyadapter, int i)
    {
        tipsreplyadapter.mImageSize = i;
        return i;
    }

*/

}
