

package streetdirectory.mobile.modules.locationdetail.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.direction.JourneyPointDetail;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalServiceOutputV2;

public class BusRouteCell extends SanListViewItem
{
    private static class BusRouteCellViewHolder
    {

        ImageButton buttonBusArrivalRefresh;
        RelativeLayout cellLayout;
        ImageView imageViewMap;
        ImageView imageViewNextBusFeature;
        ImageView imageViewNextBusLoad;
        ImageView imageViewSubsequentBusFeature;
        ImageView imageViewSubsequentBusLoad;
        ProgressBar progressBarLoading;
        TableRow tableRowBusArrival;
        TableRow tableRowBusArrivalNotes;
        TextView textViewAddress;
        TextView textViewBusStop;
        TextView textViewNextBus;
        TextView textViewSubsequentBus;
        ToggleButton toggleButtonBusArrival;

        private BusRouteCellViewHolder()
        {
        }

    }


    public BusRouteCell(JourneyPointDetail journeypointdetail)
    {
        infoVisible = false;
        refreshEnabled = true;
        cellSelected = false;
        timer = new CountDownTimer(30000L, 1000L) {

            public void onFinish()
            {
                refreshEnabled = true;
            }

            public void onTick(long l)
            {
            }

        };
        data = journeypointdetail;
        checkedListener = new android.widget.CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton compoundbutton, boolean flag)
            {
                onToggleInfoClicked();
                infoVisible = flag;
            }

        };
    }

    private int getBusLoadImageResource(int i)
    {
        if(i == 1)
            return R.drawable.bus_seated;
        return i != 2 ? R.drawable.bus_full : R.drawable.bus_standing;
    }

    protected Object createViewHolder(View view)
    {
        BusRouteCellViewHolder busroutecellviewholder = new BusRouteCellViewHolder();
        busroutecellviewholder.imageViewMap = (ImageView)view.findViewById(R.id.imageViewMap);
        busroutecellviewholder.cellLayout = (RelativeLayout)view.findViewById(R.id.cell_bus_route);
        busroutecellviewholder.progressBarLoading = (ProgressBar)view.findViewById(R.id.progressBarLoading);
        busroutecellviewholder.textViewBusStop = (TextView)view.findViewById(R.id.textViewBusStop);
        busroutecellviewholder.textViewAddress = (TextView)view.findViewById(R.id.textViewAddress);
        busroutecellviewholder.toggleButtonBusArrival = (ToggleButton)view.findViewById(R.id.toggleButtonBusArrival);
        busroutecellviewholder.tableRowBusArrival = (TableRow)view.findViewById(R.id.tableRowBusArrival);
        busroutecellviewholder.buttonBusArrivalRefresh = (ImageButton)view.findViewById(R.id.buttonBusArrivalRefresh);
        busroutecellviewholder.textViewNextBus = (TextView)view.findViewById(R.id.textViewNextBus);
        busroutecellviewholder.imageViewNextBusFeature = (ImageView)view.findViewById(R.id.imageViewNextBusFeature);
        busroutecellviewholder.imageViewNextBusLoad = (ImageView)view.findViewById(R.id.imageViewNextBusLoad);
        busroutecellviewholder.textViewSubsequentBus = (TextView)view.findViewById(R.id.textViewSubsequentBus);
        busroutecellviewholder.imageViewSubsequentBusFeature = (ImageView)view.findViewById(R.id.imageViewSubsequentBusFeature);
        busroutecellviewholder.imageViewSubsequentBusLoad = (ImageView)view.findViewById(R.id.imageViewSubsequentBusLoad);
        busroutecellviewholder.tableRowBusArrivalNotes = (TableRow)view.findViewById(R.id.tableRowBusArrivalNotes);
        return busroutecellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.bus_routes_item_layout;
    }

    protected void onButtonRefreshClicked()
    {
    }

    protected void onToggleInfoClicked()
    {
    }

    protected void populateViewHolder(final Object holder)
    {
        BusRouteCellViewHolder temp = (BusRouteCellViewHolder)holder;
        if(cellSelected)
            ((BusRouteCellViewHolder) (temp)).cellLayout.setBackgroundResource(R.drawable.selector_color_cell_location_business_premium_layout);
        else
            ((BusRouteCellViewHolder) (temp)).cellLayout.setBackgroundResource(R.drawable.selector_color_cell_location_business_layout);
        ((BusRouteCellViewHolder) (temp)).textViewBusStop.setText(data.title);
        ((BusRouteCellViewHolder) (temp)).textViewAddress.setText(data.desc);
        if(mapImage != null)
        {
            ((BusRouteCellViewHolder) (temp)).imageViewMap.setImageBitmap(mapImage);
            ((BusRouteCellViewHolder) (temp)).progressBarLoading.setVisibility(View.INVISIBLE);
        }
        ((BusRouteCellViewHolder) (temp)).toggleButtonBusArrival.setOnCheckedChangeListener(null);
        ((BusRouteCellViewHolder) (temp)).toggleButtonBusArrival.setChecked(infoVisible);
        if(infoVisible)
        {
            ((BusRouteCellViewHolder) (temp)).tableRowBusArrival.setVisibility(View.VISIBLE);
            ((BusRouteCellViewHolder) (temp)).tableRowBusArrivalNotes.setVisibility(View.VISIBLE);
            ((BusRouteCellViewHolder) (temp)).textViewNextBus.setText("Next Bus: Loading ...");
            ((BusRouteCellViewHolder) (temp)).textViewSubsequentBus.setText("Subsequent Bus: Loading ...");
            if(busInfoData != null)
            {
                if(busInfoData.nextBus == null)
                    ((BusRouteCellViewHolder) (temp)).textViewNextBus.setText("Next Bus: Loading...");
                else
                if(busInfoData.nextBus.length() > 0)
                    ((BusRouteCellViewHolder) (temp)).textViewNextBus.setText((new StringBuilder()).append("Next Bus: ").append(busInfoData.nextBus).toString());
                else
                    ((BusRouteCellViewHolder) (temp)).textViewNextBus.setText("Next Bus: N/A");
                if(busInfoData.subsequentBus == null)
                    ((BusRouteCellViewHolder) (temp)).textViewSubsequentBus.setText("Subsequent Bus: Loading...");
                else
                if(busInfoData.subsequentBus.length() > 0)
                    ((BusRouteCellViewHolder) (temp)).textViewSubsequentBus.setText((new StringBuilder()).append("Subsequent Bus: ").append(busInfoData.subsequentBus).toString());
                else
                    ((BusRouteCellViewHolder) (temp)).textViewSubsequentBus.setText("Subsequent Bus: N/A");
                if(busInfoData.nextBusFeature == 1)
                    ((BusRouteCellViewHolder) (temp)).imageViewNextBusFeature.setVisibility(View.VISIBLE);
                else
                    ((BusRouteCellViewHolder) (temp)).imageViewNextBusFeature.setVisibility(View.INVISIBLE);
                if(busInfoData.nextBusLoad == 0)
                {
                    ((BusRouteCellViewHolder) (temp)).imageViewNextBusLoad.setVisibility(View.INVISIBLE);
                } else
                {
                    ((BusRouteCellViewHolder) (temp)).imageViewNextBusLoad.setImageResource(getBusLoadImageResource(busInfoData.nextBusLoad));
                    ((BusRouteCellViewHolder) (temp)).imageViewNextBusLoad.setVisibility(View.VISIBLE);
                }
                if(busInfoData.subsequentBusFeature == 1)
                    ((BusRouteCellViewHolder) (temp)).imageViewSubsequentBusFeature.setVisibility(View.VISIBLE);
                else
                    ((BusRouteCellViewHolder) (temp)).imageViewSubsequentBusFeature.setVisibility(View.INVISIBLE);
                if(busInfoData.subsequentBusLoad == 0)
                {
                    ((BusRouteCellViewHolder) (temp)).imageViewSubsequentBusLoad.setVisibility(View.INVISIBLE);
                } else
                {
                    ((BusRouteCellViewHolder) (temp)).imageViewSubsequentBusLoad.setImageResource(getBusLoadImageResource(busInfoData.subsequentBusLoad));
                    ((BusRouteCellViewHolder) (temp)).imageViewSubsequentBusLoad.setVisibility(View.VISIBLE);
                }
            }
        } else
        {
            ((BusRouteCellViewHolder) (holder)).tableRowBusArrival.setVisibility(View.INVISIBLE);
            ((BusRouteCellViewHolder) (holder)).tableRowBusArrivalNotes.setVisibility(View.INVISIBLE);
        }
        ((BusRouteCellViewHolder) (holder)).toggleButtonBusArrival.setOnCheckedChangeListener(checkedListener);
        final BusRouteCellViewHolder holder0;
        {
            holder0 = temp;
        }

        ((BusRouteCellViewHolder) (holder)).buttonBusArrivalRefresh.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(refreshEnabled)
                {
                    refreshEnabled = false;
                    timer.start();
                    holder0.textViewNextBus.setText("Next Bus: Loading ...");
                    holder0.textViewSubsequentBus.setText("Subsequent Bus: Loading ...");
                    onButtonRefreshClicked();
                }
            }

        }
);
    }

    private static int selectedBackgroundColor = Color.rgb(51, 153, 255);
    public BusArrivalServiceOutputV2 busInfoData;
    public boolean cellSelected;
    private android.widget.CompoundButton.OnCheckedChangeListener checkedListener;
    JourneyPointDetail data;
    private boolean infoVisible;
    protected Bitmap mapImage;
    public boolean refreshEnabled;
    private CountDownTimer timer;



/*
    static boolean access$002(BusRouteCell busroutecell, boolean flag)
    {
        busroutecell.infoVisible = flag;
        return flag;
    }

*/

}
