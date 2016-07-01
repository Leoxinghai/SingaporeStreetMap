

package streetdirectory.mobile.modules.offlinemap;

import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

public class OfflineMapItemCellViewHolder
{

    public OfflineMapItemCellViewHolder()
    {
    }

    public OfflineMapItemCellViewHolder(View view)
    {
        initLayout(view);
    }

    public void initLayout(View view)
    {
        iconButton = (ImageButton)view.findViewById(R.id.IconButton);
        titleLabel = (TextView)view.findViewById(R.id.TitleLabel);
        detailLabel = (TextView)view.findViewById(R.id.DetailLabel);
        progressLayout = (RelativeLayout)view.findViewById(R.id.ProgressLayout);
        progressBar = (ProgressBar)view.findViewById(R.id.ProgressBar);
        statusLabel = (TextView)view.findViewById(R.id.StatusLabel);
        progressLabel = (TextView)view.findViewById(R.id.ProgressLabel);
        downloadButton = (Button)view.findViewById(R.id.DownloadButton);
        buttonLayout = (RelativeLayout)view.findViewById(R.id.ButtonLayout);
        resumeButton = (Button)view.findViewById(R.id.ResumeButton);
        pauseButton = (Button)view.findViewById(R.id.PauseButton);
        deleteButton = (Button)view.findViewById(R.id.DeleteButton);
        updateNotAvailableButton = (Button)view.findViewById(R.id.UpdateNotAvailableButton);
        updateButton = (Button)view.findViewById(R.id.UpdateButton);
    }

    public RelativeLayout buttonLayout;
    public int childPosition;
    public Button deleteButton;
    public TextView detailLabel;
    public Button downloadButton;
    public int groupPosition;
    public ImageButton iconButton;
    public int packageID;
    public String packageName;
    public int parentID;
    public Button pauseButton;
    public ProgressBar progressBar;
    public TextView progressLabel;
    public RelativeLayout progressLayout;
    public Button resumeButton;
    public TextView statusLabel;
    public TextView titleLabel;
    public Button updateButton;
    public Button updateNotAvailableButton;
}
