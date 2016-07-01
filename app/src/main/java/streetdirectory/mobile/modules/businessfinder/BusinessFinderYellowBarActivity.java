// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessfinder;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.BitmapTools;
import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderYellowBarService;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderYellowBarServiceInput;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderYellowBarServiceOutput;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.businessfinder:
//            BusinessFinderYellowBarCell

public class BusinessFinderYellowBarActivity extends SDActivity
{

    public BusinessFinderYellowBarActivity()
    {
        _data = new ArrayList();
    }

    private void downloadBusinessFinderCategory()
    {
        _loadingIndicator.setVisibility(View.VISIBLE);
        abortAllProcess();
        _service = new BusinessFinderYellowBarService(new BusinessFinderYellowBarServiceInput(mCountryCode, parentID)) {

            public void onFailed(Exception exception)
            {
                super.onFailed(exception);
                _loadingIndicator.setVisibility(View.INVISIBLE);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                super.onSuccess(sdhttpserviceoutput);
                _loadingIndicator.setVisibility(View.INVISIBLE);
                Iterator iterator = sdhttpserviceoutput.childs.iterator();
                do
                {
                    if(!iterator.hasNext())
                        break;
                    BusinessFinderYellowBarServiceOutput businessfinderyellowbarserviceoutput = (BusinessFinderYellowBarServiceOutput)iterator.next();
                    BusinessFinderYellowBarCell businessfinderyellowbarcell = new BusinessFinderYellowBarCell(businessfinderyellowbarserviceoutput);
                    _data.add(businessfinderyellowbarcell);
                    processBitmap((new StringBuilder()).append("biz_").append(businessfinderyellowbarserviceoutput.id).toString(), businessfinderyellowbarcell);
                    if(mMenuID == businessfinderyellowbarserviceoutput.id)
                    {
                        businessfinderyellowbarcell.cellSelected = true;
                        selectedPosition = _adapter.getPosition(businessfinderyellowbarcell);
                    }
                } while(true);
                _adapter.notifyDataSetChanged();
                _listView.requestFocus();
                _listView.setSelection(selectedPosition);
            }
        };

        _service.executeAsync();
    }

    private void initData()
    {
        Intent intent = getIntent();
        mCountryCode = intent.getStringExtra("countryCode");
        mMenuID = intent.getIntExtra("menuID", 0);
        _adapter = new SanListViewAdapter(this, 0, _data);
        _listView.setAdapter(_adapter);
        downloadBusinessFinderCategory();
    }

    private void initEvent()
    {
        _cancelButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        _listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                BusinessFinderYellowBarServiceOutput cell = ((BusinessFinderYellowBarCell)_data.get(i)).data;
                Intent intent = new Intent();
                intent.putExtra("categoryID", ((BusinessFinderYellowBarServiceOutput) (cell)).categoryID);
                intent.putExtra("menuID", ((BusinessFinderYellowBarServiceOutput) (cell)).id);
                intent.putExtra("categoryName", ((BusinessFinderYellowBarServiceOutput) (cell)).name);
                setResult(-1, intent);
                finish();
            }

        });
    }

    private void initLayout()
    {
        _cancelButton = (Button)findViewById(R.id.CancelButton);
        _listView = (ListView)findViewById(R.id.ListView);
        _loadingIndicator = (ProgressBar)findViewById(R.id.LoadingIndicator);
    }

    private void initialize()
    {
        initLayout();
        initEvent();
        initData();
    }

    private void processBitmap(String s, BusinessFinderYellowBarCell businessfinderyellowbarcell)
    {
        Resources resources = getResources();
        int i = resources.getIdentifier(s, "drawable", getPackageName());
        if(i != 0)
        {
            BitmapFactory.Options temp = new android.graphics.BitmapFactory.Options();
            temp.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(resources, i);
            temp.inSampleSize = BitmapTools.calculateInSampleSize(temp, 38, 38);
            temp.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeResource(resources, i, temp);
        }
        businessfinderyellowbarcell.bmp = bmp;
        _adapter.notifyDataSetChanged();
    }

    protected void abortAllProcess()
    {
        super.abortAllProcess();
        if(_service != null)
        {
            _service.abort();
            _service = null;
        }
    }

    public void finish()
    {
        super.finish();
        abortAllProcess();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_business_finder_sub_directory);
        initialize();
    }

    private static int parentID = 0;
    private SanListViewAdapter _adapter;
    private Button _cancelButton;
    private ArrayList _data;
    private ListView _listView;
    private ProgressBar _loadingIndicator;
    private BusinessFinderYellowBarService _service;
    private Bitmap bmp;
    private String mCountryCode;
    private int mMenuID;
    private int selectedPosition;

    static
    {
        SanListViewItem.addTypeCount(BusinessFinderYellowBarCell.class);
    }







/*
    static int access$402(BusinessFinderYellowBarActivity businessfinderyellowbaractivity, int i)
    {
        businessfinderyellowbaractivity.selectedPosition = i;
        return i;
    }

*/


}
