

package streetdirectory.mobile.core.ui;

import android.content.Context;
import android.view.View;

import com.xinghai.mycurve.R;

// Referenced classes of package streetdirectory.mobile.core.ui:
//            SanListViewItem, SanListViewAdapter

public class LoadMoreCell extends SanListViewItem
{
    public static interface OnLoadMoreListener
    {

        public abstract void onLoadMoreList();
    }


    public LoadMoreCell()
    {
    }

    protected Object createViewHolder(View view)
    {
        if(loadMoreListener != null)
            loadMoreListener.onLoadMoreList();
        return null;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_load_more;
    }

    protected void populateViewHolder(Object obj)
    {
    }

    public OnLoadMoreListener loadMoreListener;
}
