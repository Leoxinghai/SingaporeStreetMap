

package streetdirectory.mobile.sd;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;
import streetdirectory.mobile.modules.search.service.SearchServiceOutput;

public class SDSearchHistoryMenuItem extends SDSideMenuItem
{
    public static interface OnRemoveButtonClicked
    {

        public abstract void onRemoveButtonClicked(SearchServiceOutput searchserviceoutput);
    }

    static class ViewHolder
    {

        public TextView address;
        public TextView category;
        public ImageView remove;
        public TextView title;
        public ViewGroup viewContent;

        ViewHolder()
        {
        }
    }


    public SDSearchHistoryMenuItem(SearchServiceOutput searchserviceoutput)
    {
        data = searchserviceoutput;
    }

    protected Object createViewHolder(View view)
    {
        ViewHolder viewholder = new ViewHolder();
        viewholder.viewContent = (ViewGroup)view.findViewById(R.id.layout_content);
        viewholder.title = (TextView)view.findViewById(R.id.textview_title);
        viewholder.address = (TextView)view.findViewById(R.id.textview_address);
        viewholder.category = (TextView)view.findViewById(R.id.textview_category);
        viewholder.remove = (ImageView)view.findViewById(R.id.button_remove);
        return viewholder;
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_search_history;
    }

    protected Class getViewHolderClass()
    {
        return SDSearchHistoryMenuItem.class;
    }

    protected void populateViewHolder(Object obj)
    {
        ViewHolder viewholder = (ViewHolder)obj;
        ViewGroup.LayoutParams layoutParams = viewholder.viewContent.getLayoutParams();
        layoutParams.width = menuSlideOffset;
        viewholder.viewContent.setLayoutParams(((android.view.ViewGroup.LayoutParams) (layoutParams)));
        viewholder.title.setText(data.venue);
        TextView textview = viewholder.address;
        String lAddress;
        if(data.address != null)
            lAddress = data.address;
        else
            lAddress = "";
        textview.setText(((CharSequence) (lAddress)));
        if(data.type == 1)
        {
            viewholder.category.setTextColor(Color.parseColor("#1FFF1F"));
            viewholder.category.setText("Location");
        } else
        if(data.type == 2)
        {
            viewholder.category.setTextColor(Color.parseColor("#FF8F1E"));
            viewholder.category.setText("Company");
        } else
        {
            viewholder.category.setTextColor(Color.parseColor("#2C2CFB"));
            viewholder.category.setText("Directory");
            viewholder.address.setText("See all listing for directory");
        }
        viewholder.remove.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mOnRemoveButtonClicked != null)
                    mOnRemoveButtonClicked.onRemoveButtonClicked(data);
            }

        });
    }

    public void setOnRemoveButtonClicked(OnRemoveButtonClicked onremovebuttonclicked)
    {
        mOnRemoveButtonClicked = onremovebuttonclicked;
    }

    public SearchServiceOutput data;
    private OnRemoveButtonClicked mOnRemoveButtonClicked;

}
