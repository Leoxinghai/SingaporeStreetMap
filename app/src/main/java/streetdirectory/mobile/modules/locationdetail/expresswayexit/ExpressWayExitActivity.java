

package streetdirectory.mobile.modules.locationdetail.expresswayexit;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.SDActivity;

public class ExpressWayExitActivity extends SDActivity
{

    public ExpressWayExitActivity()
    {
    }

    private void initData()
    {
    }

    private void initEvent()
    {
        mBackButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
    }

    private void initLayout()
    {
        mMenuBar = (RelativeLayout)findViewById(R.id.MenuBar);
        mBackButton = (Button)findViewById(R.id.BackButton);
        mTitleBar = (TextView)findViewById(R.id.TitleBar);
        mShareButton = (Button)findViewById(R.id.ShareButton);
        mLayoutHeader = (RelativeLayout)findViewById(R.id.layout_header);
        mButtonBusinessPhoto = (ImageButton)findViewById(R.id.button_business_photo);
        mTextviewTitle = (TextView)findViewById(R.id.textview_title);
        mTextviewDetail = (TextView)findViewById(R.id.textview_detail);
        mLayoutHeaderButton = (LinearLayout)findViewById(R.id.layout_header_button);
        mButtonDirection = (Button)findViewById(R.id.button_direction);
        mButtonMap = (Button)findViewById(R.id.button_map);
        mButtonTips = (Button)findViewById(R.id.button_tips);
        mTextviewPhotoTitle = (TextView)findViewById(R.id.textview_photo_title);
        mViewpagerPhoto = (ViewPager)findViewById(R.id.viewpager_photo);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    protected void onCreate(Bundle bundle)
    {
        onCreate(bundle);
        setContentView(R.layout.activity_location_detail_expressway_exit);
        initialize();
    }

    private Button mBackButton;
    private ImageButton mButtonBusinessPhoto;
    private Button mButtonDirection;
    private Button mButtonMap;
    private Button mButtonTips;
    private RelativeLayout mLayoutHeader;
    private LinearLayout mLayoutHeaderButton;
    private RelativeLayout mMenuBar;
    private Button mShareButton;
    private TextView mTextviewDetail;
    private TextView mTextviewPhotoTitle;
    private TextView mTextviewTitle;
    private TextView mTitleBar;
    private ViewPager mViewpagerPhoto;
}
