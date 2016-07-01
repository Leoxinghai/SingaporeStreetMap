// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.message;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import streetdirectory.mobile.modules.message.service.MessageListServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.message:
//            MessageCellViewHolder

public class MessageInboxAdapter extends BaseAdapter
{
    public static interface OnLoadMoreListener
    {

        public abstract void onLoadMoreList();
    }


    public MessageInboxAdapter(Context context)
    {
        initialize(context);
    }

    private void initialize(Context context)
    {
        mContext = context;
        mData = new ArrayList();
    }

    public void add(MessageListServiceOutput messagelistserviceoutput)
    {
        mData.add(messagelistserviceoutput);
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


    public MessageListServiceOutput getItem(int i)
    {
        return (MessageListServiceOutput)mData.get(i);
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
        Object obj;
        java.util.Date date;
        MessageCellViewHolder temp;

        if(j != 0) {
            obj = view;
            if(j == 1)
            {
                View view1 = view;
                if(view == null)
                    view1 = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_load_more, viewgroup, false);
                obj = view1;
                if(mLoadMoreListener != null)
                {
                    mLoadMoreListener.onLoadMoreList();
                    return view1;
                }
            }
            return ((View) (obj));
        }

        if(view == null)
        {
            view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_message, viewgroup, false);
            temp = new MessageCellViewHolder();
            temp.labelSender = (TextView)view.findViewById(R.id.text_view_sender);
            temp.labelSubject = (TextView)view.findViewById(R.id.text_view_subject);
            temp.labelDetail = (TextView)view.findViewById(R.id.text_view_detail);
            temp.labelDate = (TextView)view.findViewById(R.id.text_view_date);
            view.setTag(temp);
        } else
        {
            temp = (MessageCellViewHolder)view.getTag();
        }
        obj = view;
        for(;i < mData.size();) {
            obj = getItem(i);
            if (mDateFormat == null)
                mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = ((MessageListServiceOutput) (obj)).dateTime;
            ((MessageCellViewHolder) (temp)).labelSender.setText(((MessageListServiceOutput) (obj)).friendName);
            ((MessageCellViewHolder) (temp)).labelSubject.setText(((MessageListServiceOutput) (obj)).subject);
            ((MessageCellViewHolder) (temp)).labelDetail.setText(((MessageListServiceOutput) (obj)).messagePreview);
            if (date == null) {
                ((MessageCellViewHolder) (temp)).labelDate.setText("");
                return view;
            } else {
                ((MessageCellViewHolder) (temp)).labelDate.setText(mDateFormat.format(date));
                obj = view;
            }
        }

        return ((View) (obj));

    }

    public int getViewTypeCount()
    {
        return 2;
    }

    public void removeAllData()
    {
        mData.clear();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onloadmorelistener)
    {
        mLoadMoreListener = onloadmorelistener;
    }

    public static final int LOAD_MORE_CELL = 1;
    public static final int MESSAGE_CELL = 0;
    private Context mContext;
    private ArrayList mData;
    private SimpleDateFormat mDateFormat;
    private OnLoadMoreListener mLoadMoreListener;
    public int total;
}
