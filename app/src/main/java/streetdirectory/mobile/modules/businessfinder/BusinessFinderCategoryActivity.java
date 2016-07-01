// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.io.File;
import java.util.*;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderService;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderServiceInput;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderServiceOutput;
import streetdirectory.mobile.service.SDDataOutput;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.businessfinder:
//            BusinessFinderCategoryAdapter

public class BusinessFinderCategoryActivity extends SDActivity
{

    public BusinessFinderCategoryActivity()
    {
        mMenus = new ArrayList();
    }

    class SUBCLASS1 extends BusinessFinderServiceInput {

        public void onAborted(Exception exception)
        {
            SDLogger.info("Business Finder Aborted");
            _loadingIndicator.setVisibility(View.INVISIBLE);
            _service = null;
        }

        public void onFailed(Exception exception)
        {
            SDLogger.printStackTrace(exception, "Business Finder Failed");
            _loadingIndicator.setVisibility(View.INVISIBLE);
            _service = null;
        }

        public void onReceiveData(BusinessFinderServiceOutput businessfinderserviceoutput)
        {
            businessfinderserviceoutput.generateIcon(_menuID);
        }

        public void onReceiveData(SDDataOutput sddataoutput)
        {
            onReceiveData((BusinessFinderServiceOutput) sddataoutput);
        }


        public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
        {
            SDLogger.info("Business Finder Success");
            Collections.sort(sdhttpserviceoutput.childs, new Comparator() {

                public int compare(Object obj, Object obj1)
                {
                    return compare((BusinessFinderServiceOutput)obj, (BusinessFinderServiceOutput)obj1);
                }

                public int compare(BusinessFinderServiceOutput businessfinderserviceoutput, BusinessFinderServiceOutput businessfinderserviceoutput1)
                {
                    return businessfinderserviceoutput.name.compareToIgnoreCase(businessfinderserviceoutput1.name);
                }

            });

            boolean useLocalFile;
            if(this.getSaveFile().exists())
                useLocalFile = true;
            else
                useLocalFile = false;

            if(!useLocalFile)
                BusinessFinderService.setVersion(countryCode, menuID, SDPreferences.getInstance().getCategoryVersion());
            _loadingIndicator.setVisibility(View.INVISIBLE);
            _service = null;
            _categoryAdapter.setData(sdhttpserviceoutput.childs);
            _categoryAdapter.notifyDataSetInvalidated();
        }

        final String countryCode;
        final int menuID;
		String sType;
		int iType;


        SUBCLASS1( String s, int i)
        {
            super(s,i);
            sType = s;
            iType = i;
            countryCode = s;
            menuID = i;
        }
    }

    private void downloadBusinessFinderMenuItem()
    {
        abortAllProcess();
        final String countryCode = _countryCode;
        int i = _menuID;
        final BusinessFinderServiceInput final_businessfinderserviceinput = new SUBCLASS1(countryCode, i);

        boolean useLocalFile;
        if(final_businessfinderserviceinput.getSaveFile().exists())
            useLocalFile = true;
        else
        useLocalFile = false;

        _loadingIndicator.setVisibility(View.VISIBLE);
        _service.executeAsync();
        if(useLocalFile)
            BusinessFinderService.updateInBackground(_countryCode, _menuID);
    }

    private void initData()
    {
        _categoryAdapter = new BusinessFinderCategoryAdapter(this);
        _listView.setAdapter(_categoryAdapter);
        downloadBusinessFinderMenuItem();
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
                Intent intent = new Intent();
                intent.putExtra("categoryID", _categoryAdapter.getItem(i).menuID);
                intent.putExtra("menuID", _categoryAdapter.getItem(i).categoryID);
                intent.putExtra("categoryName", _categoryAdapter.getItem(i).name);
                intent.putExtra("categoryIcon", _categoryAdapter.getItem(i).icon);
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
        mMenus.add(new BusinessFinderServiceOutput(0, "Genre"));
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
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
        initialize();
    }

    private Button _cancelButton;
    private BusinessFinderCategoryAdapter _categoryAdapter;
    private String _countryCode;
    private ListView _listView;
    private ProgressBar _loadingIndicator;
    private int _menuID;
    private BusinessFinderService _service;
    private ArrayList mMenus;




/*
    static BusinessFinderService access$202(BusinessFinderCategoryActivity businessfindercategoryactivity, BusinessFinderService businessfinderservice)
    {
        businessfindercategoryactivity._service = businessfinderservice;
        return businessfinderservice;
    }

*/

}
