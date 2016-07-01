

package streetdirectory.mobile.modules.tips;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.tips.service.TipsServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.tips:
//            TipsCell

public class TipsWithImageCell extends SanListViewItem
{
    public static class TipsCellWithImageViewHolder
    {

        public TextView commentLabel;
        public ImageButton iconButton;
        public TextView nameLabel;
        public ImageView photoView;
        public TextView scoreLabel;

        public TipsCellWithImageViewHolder()
        {
        }
    }


    public TipsWithImageCell(TipsServiceOutput tipsserviceoutput)
    {
        data = tipsserviceoutput;
    }

    protected Object createViewHolder(View view)
    {
        TipsCellWithImageViewHolder tipscellwithimageviewholder = new TipsCellWithImageViewHolder();
        tipscellwithimageviewholder.photoView = (ImageView)view.findViewById(R.id.image_view_photo);
        tipscellwithimageviewholder.iconButton = (ImageButton)view.findViewById(R.id.button_icon);
        tipscellwithimageviewholder.nameLabel = (TextView)view.findViewById(R.id.text_view_name);
        tipscellwithimageviewholder.commentLabel = (TextView)view.findViewById(R.id.text_view_comment);
        tipscellwithimageviewholder.scoreLabel = (TextView)view.findViewById(R.id.text_view_score);
        return tipscellwithimageviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_tips_with_img;
    }

    protected void populateViewHolder(Object obj)
    {
        obj = (TipsCellWithImageViewHolder)obj;
        if(data.name != null)
            ((TipsCellWithImageViewHolder) (obj)).nameLabel.setText(Html.fromHtml(data.name));
        if(data.comment != null)
            ((TipsCellWithImageViewHolder) (obj)).commentLabel.setText(Html.fromHtml(data.comment));
        if(data.score != 0)
            ((TipsCellWithImageViewHolder) (obj)).scoreLabel.setText(Html.fromHtml((new StringBuilder()).append("(Earn ").append(data.score).append(" point)").toString()));
        ((TipsCellWithImageViewHolder) (obj)).photoView.setImageBitmap(userImage);
        ((TipsCellWithImageViewHolder) (obj)).iconButton.setImageBitmap(tipsImage);
    }

    TipsServiceOutput data;
    public Bitmap tipsImage;
    public Bitmap userImage;

    static
    {
        SanListViewItem.addTypeCount(TipsCell.class);
    }
}
