

package streetdirectory.mobile.modules.direction;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalServiceOutputV2;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneyPointDetailBus

public class DirectionBusTrainCell extends SanListViewItem
{
    public static class DirectionBusTrainCellViewHolder
    {

        public ImageButton buttonRefresh;
        public Button buttonShowBusArrival;
        public TextView descLabel;
        public ImageView imageViewIcon;
        public RelativeLayout layoutBusArrival;
        public TextView nextBusLabel;
        public TextView subsequentBusLabel;
        public TextView timeLabel;
        public TextView titleLabel;

        public DirectionBusTrainCellViewHolder()
        {
        }
    }


    public DirectionBusTrainCell(JourneyPointDetailBus journeypointdetailbus, Drawable drawable, boolean flag, boolean flag1)
    {
        data = journeypointdetailbus;
        iconDrawable = drawable;
        isShowTime = flag;
        isShowBusButton = flag1;
    }

    protected Object createViewHolder(View view)
    {
        DirectionBusTrainCellViewHolder directionbustraincellviewholder = new DirectionBusTrainCellViewHolder();
        directionbustraincellviewholder.titleLabel = (TextView)view.findViewById(R.id.text_view_address_name);
        directionbustraincellviewholder.descLabel = (TextView)view.findViewById(R.id.text_view_address);
        directionbustraincellviewholder.timeLabel = (TextView)view.findViewById(R.id.text_view_direction_time);
        directionbustraincellviewholder.imageViewIcon = (ImageView)view.findViewById(R.id.image_view_start_icon);
        directionbustraincellviewholder.buttonRefresh = (ImageButton)view.findViewById(R.id.buttonBusArrivalRefresh);
        directionbustraincellviewholder.nextBusLabel = (TextView)view.findViewById(R.id.textViewNextBus);
        directionbustraincellviewholder.subsequentBusLabel = (TextView)view.findViewById(R.id.textViewSubsequentBus);
        directionbustraincellviewholder.buttonShowBusArrival = (Button)view.findViewById(R.id.button1);
        directionbustraincellviewholder.layoutBusArrival = (RelativeLayout)view.findViewById(R.id.layoutBusArrival);
        return directionbustraincellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_direction_bus_train;
    }

    protected void onButtonBusArrivalClicked()
    {
    }

    protected void onButtonRefreshClicked()
    {
    }

    protected void populateViewHolder(Object obj)
    {
            obj = (DirectionBusTrainCellViewHolder)obj;
            ((DirectionBusTrainCellViewHolder) (obj)).titleLabel.setText(data.title);
            ((DirectionBusTrainCellViewHolder) (obj)).descLabel.setText(data.desc);
            if(isShowTime)
            {
                ((DirectionBusTrainCellViewHolder) (obj)).timeLabel.setText(data.time);
                ((DirectionBusTrainCellViewHolder) (obj)).timeLabel.setVisibility(View.VISIBLE);
            } else
            {
                ((DirectionBusTrainCellViewHolder) (obj)).timeLabel.setVisibility(View.INVISIBLE);
            }
            if(iconDrawable != null)
                ((DirectionBusTrainCellViewHolder) (obj)).imageViewIcon.setImageDrawable(iconDrawable);
            ((DirectionBusTrainCellViewHolder) (obj)).buttonShowBusArrival.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view)
                {
                    if(isShowBusArrival)
                        isShowBusArrival = false;
                    else
                        isShowBusArrival = true;
                    onButtonBusArrivalClicked();
                }

            });
            ((DirectionBusTrainCellViewHolder) (obj)).buttonRefresh.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view)
                {
                    onButtonRefreshClicked();
                }

            });
            if(isShowBusButton)
                ((DirectionBusTrainCellViewHolder) (obj)).buttonShowBusArrival.setVisibility(View.VISIBLE);
            else
                ((DirectionBusTrainCellViewHolder) (obj)).buttonShowBusArrival.setVisibility(View.INVISIBLE);
            if(!isShowBusArrival) {
                ((DirectionBusTrainCellViewHolder) (obj)).layoutBusArrival.setVisibility(View.INVISIBLE);
                return;
            }
            ((DirectionBusTrainCellViewHolder) (obj)).layoutBusArrival.setVisibility(View.VISIBLE);
            ((DirectionBusTrainCellViewHolder) (obj)).nextBusLabel.setText("Next Bus: Loading ...");
            ((DirectionBusTrainCellViewHolder) (obj)).subsequentBusLabel.setText("Subsequent Bus: Loading ...");
            if(busInfoData != null)
            {
                if(busInfoData.nextBus == null)
                    ((DirectionBusTrainCellViewHolder) (obj)).nextBusLabel.setText("Next Bus: Loading...");
                else
                if(busInfoData.nextBus.length() > 0)
                    ((DirectionBusTrainCellViewHolder) (obj)).nextBusLabel.setText((new StringBuilder()).append("Next Bus: ").append(busInfoData.nextBus).toString());
                else
                    ((DirectionBusTrainCellViewHolder) (obj)).nextBusLabel.setText("Next Bus: N/A");
                if(busInfoData.subsequentBus != null) {
                    if(busInfoData.subsequentBus.length() > 0)
                    {
                        ((DirectionBusTrainCellViewHolder) (obj)).subsequentBusLabel.setText((new StringBuilder()).append("Subsequent Bus: ").append(busInfoData.subsequentBus).toString());
                        return;
                    } else
                    {
                        ((DirectionBusTrainCellViewHolder) (obj)).subsequentBusLabel.setText("Subsequent Bus: N/A");
                        return;
                    }
                }
                ((DirectionBusTrainCellViewHolder) (obj)).subsequentBusLabel.setText("Subsequent Bus: Loading...");
            }
            return;
    }

    public BusArrivalServiceOutputV2 busInfoData;
    JourneyPointDetailBus data;
    public Drawable iconDrawable;
    public boolean isShowBusArrival;
    public boolean isShowBusButton;
    public boolean isShowTime;
}
