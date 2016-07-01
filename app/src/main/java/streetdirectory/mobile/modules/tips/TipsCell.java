

package streetdirectory.mobile.modules.tips;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.tips.service.TipsServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.tips:
//            TipsCellViewHolder

public class TipsCell extends SanListViewItem
{

    public TipsCell(TipsServiceOutput tipsserviceoutput)
    {
        data = tipsserviceoutput;
    }

    protected Object createViewHolder(View view)
    {
        TipsCellViewHolder tipscellviewholder = new TipsCellViewHolder();
        tipscellviewholder.photoView = (ImageView)view.findViewById(R.id.UserImage);
        tipscellviewholder.titleLabel = (TextView)view.findViewById(R.id.TitleLabel);
        tipscellviewholder.detailLabel = (TextView)view.findViewById(R.id.DetailLabel);
        tipscellviewholder.dateLabel = (TextView)view.findViewById(R.id.DateLabel);
        tipscellviewholder.buttonReply = (Button)view.findViewById(R.id.button_reply_tips);
        return tipscellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_tips;
    }

    protected void populateViewHolder(Object obj)
    {
        obj = (TipsCellViewHolder)obj;
        if(data.name != null)
            ((TipsCellViewHolder) (obj)).titleLabel.setText(Html.fromHtml(data.name));
        if(data.comment != null)
            ((TipsCellViewHolder) (obj)).detailLabel.setText(Html.fromHtml(data.comment));
    }

    TipsServiceOutput data;

    static
    {
        SanListViewItem.addTypeCount(TipsCell.class);
    }
}
