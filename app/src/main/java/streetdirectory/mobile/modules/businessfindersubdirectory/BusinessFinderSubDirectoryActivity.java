// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessfindersubdirectory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.businessfindersubdirectory.service.BusinessFinderSubDirectoryService;
import streetdirectory.mobile.modules.businessfindersubdirectory.service.BusinessFinderSubDirectoryServiceInput;
import streetdirectory.mobile.modules.businessfindersubdirectory.service.BusinessFinderSubDirectoryServiceOutput;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.businessfindersubdirectory:
//            BusinessFinderSubDirectoryAdapter

public class BusinessFinderSubDirectoryActivity extends SDActivity
{

    public BusinessFinderSubDirectoryActivity()
    {
    }

    private void downloadSubDirectory()
    {
        abortAllProcess();
        _adapter.clear();
        _service = new BusinessFinderSubDirectoryService(new BusinessFinderSubDirectoryServiceInput(_countryCode, _menuID, _categoryID)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Sub Directory Aborted");
                _loadingIndicator.setVisibility(View.INVISIBLE);
                _service = null;
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Sub Directory Failed");
                _loadingIndicator.setVisibility(View.INVISIBLE);
                _service = null;
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                SDLogger.info("Sub Directory Success");
                _loadingIndicator.setVisibility(View.INVISIBLE);
                _service = null;
                _adapter.setData(sdhttpserviceoutput);
            }

        };
        _loadingIndicator.setVisibility(View.VISIBLE);
        _service.executeAsync();
    }

    private void initData()
    {
        Intent intent = getIntent();
        _countryCode = intent.getStringExtra("countryCode");
        _menuName = intent.getStringExtra("menuName");
        if(_menuName != null && _titleLabel != null)
            _titleLabel.setText(_menuName);
        String s = intent.getStringExtra("detailTitle");
        if(s != null)
            _detailLabel.setText(s);
        _menuID = intent.getIntExtra("menuID", 0);
        _categoryID = intent.getIntExtra("categoryID", 0);
        _adapter = new BusinessFinderSubDirectoryAdapter(this);
        if(_listView != null)
            _listView.setAdapter(_adapter);
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
                SDLogger.info((new StringBuilder()).append(i).append(" clicked").toString());
                BusinessFinderSubDirectoryServiceOutput cell = _adapter.getItem(i);
                Intent intent = new Intent();
                intent.putExtra("type", ((BusinessFinderSubDirectoryServiceOutput) (cell)).type);
                intent.putExtra("menuID", _menuID);
                intent.putExtra("menuName", _menuName);
                intent.putExtra("parentID", _categoryID);
                intent.putExtra("categoryID", ((BusinessFinderSubDirectoryServiceOutput) (cell)).categoryID);
                intent.putExtra("categoryName", ((BusinessFinderSubDirectoryServiceOutput) (cell)).fullName);
                setResult(-1, intent);
                finish();
            }

        });
    }

    private void initLayout()
    {
        _titleLabel = (TextView)findViewById(R.id.TitleLabel);
        _detailLabel = (TextView)findViewById(R.id.DetailLabel);
        _cancelButton = (Button)findViewById(R.id.CancelButton);
        _listView = (ListView)findViewById(R.id.ListView);
        _loadingIndicator = (ProgressBar)findViewById(R.id.LoadingIndicator);
    }

    public void abortAllProcess()
    {
        if(_service != null)
        {
            _service.abort();
            _service = null;
        }
    }

    public void finish()
    {
        super.finish();
        overridePendingTransition(0x10a0000, 0x10a0001);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_business_finder_sub_directory);
        initLayout();
        initData();
        initEvent();
        downloadSubDirectory();
    }

    protected void onDestroy()
    {
        abortAllProcess();
        super.onDestroy();
    }

    private BusinessFinderSubDirectoryAdapter _adapter;
    private Button _cancelButton;
    private int _categoryID;
    private String _countryCode;
    private TextView _detailLabel;
    private ListView _listView;
    private ProgressBar _loadingIndicator;
    private int _menuID;
    private String _menuName;
    private BusinessFinderSubDirectoryService _service;
    private TextView _titleLabel;







/*
    static BusinessFinderSubDirectoryService access$502(BusinessFinderSubDirectoryActivity businessfindersubdirectoryactivity, BusinessFinderSubDirectoryService businessfindersubdirectoryservice)
    {
        businessfindersubdirectoryactivity._service = businessfindersubdirectoryservice;
        return businessfindersubdirectoryservice;
    }

*/
}
