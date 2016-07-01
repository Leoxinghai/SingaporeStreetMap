// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessfinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.xinghai.mycurve.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.core.service.HttpImageService;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderImageService;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderService;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderServiceInput;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderServiceOutput;
import streetdirectory.mobile.modules.businessfindersubdirectory.BusinessFinderSubDirectoryActivity;
import streetdirectory.mobile.service.SDDataOutput;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.businessfinder:
//            BusinessFinderItemGridAdapter

public class BusinessFinderItemFragment extends Fragment
{

    public BusinessFinderItemFragment()
    {
        _imageServices = new ArrayList();
    }

    private void abortAllProcess()
    {
        if(_service != null)
        {
            _service.abort();
            _service = null;
        }
        for(Iterator iterator = (new ArrayList(_imageServices)).iterator(); iterator.hasNext(); ((HttpImageService)iterator.next()).abort());
        _imageServices.clear();
    }

    private void categoryIconClicked(BusinessFinderServiceOutput businessfinderserviceoutput)
    {
        SDLogger.info((new StringBuilder()).append("Button ").append(businessfinderserviceoutput.name).append(" clicked").toString());
        android.support.v4.app.FragmentActivity fragmentactivity = getActivity();
        Intent intent = new Intent(fragmentactivity, BusinessFinderSubDirectoryActivity.class);
        intent.putExtra("countryCode", _countryCode);
        intent.putExtra("stateID", _stateID);
        intent.putExtra("menuName", businessfinderserviceoutput.fullName);
        if(_stateName != null)
            intent.putExtra("detailTitle", (new StringBuilder()).append(_countryName).append(" >> ").append(_stateName).toString());
        else
            intent.putExtra("detailTitle", _countryName);
        intent.putExtra("menuID", _menuID);
        intent.putExtra("categoryID", businessfinderserviceoutput.menuID);
        fragmentactivity.startActivityForResult(intent, 1);
        fragmentactivity.overridePendingTransition(0x10a0000, 0x10a0001);
    }

    class SUBCLASS1 extends BusinessFinderImageService {

        public void onAborted(Exception exception)
        {
            _imageServices.remove(this);
        }

        public void onFailed(Exception exception)
        {
            _imageServices.remove(this);
        }

        public void onSuccess(Bitmap bitmap)
        {
            _imageServices.remove(this);
            _gridAdapter.putImage(businessFinderIcon, bitmap);
            _gridAdapter.notifyDataSetChanged();
        }

        public void onSuccess(Object obj)
        {
            onSuccess((Bitmap)obj);
        }

        final String businessFinderIcon;

        SUBCLASS1(int i, int j, String s1)
        {
            super(s1, i, j);
            businessFinderIcon = s1;
        }
    }

    private void downloadCategoryImage(final String final_s, int i, int j)
    {
        for(Iterator iterator = (new ArrayList(_imageServices)).iterator(); iterator.hasNext();)
            if(((HttpImageService)iterator.next()).tag.equals(final_s))
                return;

        BusinessFinderImageService businessfinderimageservice = new SUBCLASS1(i, j, final_s);
        businessfinderimageservice.tag = final_s;
        _imageServices.add(businessfinderimageservice);
        businessfinderimageservice.executeAsync();
    }

    private void initData()
    {
        _gridAdapter = new BusinessFinderItemGridAdapter(getActivity());
        _gridView.setAdapter(_gridAdapter);
    }

    private void initEvent()
    {
        _gridAdapter.setHandler(new BusinessFinderItemGridAdapter.BusinessFinderItemAdapterHandler() {

            public void onCategoryIconClicked(BusinessFinderServiceOutput businessfinderserviceoutput)
            {
                categoryIconClicked(businessfinderserviceoutput);
            }

            public void onCategoryIconNotFound(BusinessFinderServiceOutput businessfinderserviceoutput, int i, int j)
            {
                downloadCategoryImage(businessfinderserviceoutput.iconURL, i, j);
            }

        });
    }

    static BusinessFinderItemFragment newInstance(String s, String s1, String s2, int i, int j)
    {
        BusinessFinderItemFragment businessfinderitemfragment = new BusinessFinderItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("countryCode", s);
        bundle.putString("countryName", s1);
        bundle.putString("stateName", s2);
        bundle.putInt("stateID", i);
        bundle.putInt("menuID", j);
        businessfinderitemfragment.setArguments(bundle);
        return businessfinderitemfragment;
    }

    class SUBCLASS2 extends BusinessFinderService {

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
            onReceiveData((BusinessFinderServiceOutput)sddataoutput);
        }

        public void onSuccess(Object obj)
        {
            onSuccess((SDHttpServiceOutput)obj);
        }

        public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
        {
            SDLogger.info("Business Finder Success");
            if(!useLocalFile)
                BusinessFinderService.setVersion(countryCode, menuID, SDPreferences.getInstance().getCategoryVersion());
            _loadingIndicator.setVisibility(View.INVISIBLE);
            _service = null;
            _gridAdapter.setData(sdhttpserviceoutput.childs);
            _gridAdapter.notifyDataSetInvalidated();
        }

        final String countryCode;
        final int menuID;
        final boolean useLocalFile;

        SUBCLASS2(BusinessFinderServiceInput serviceinput, String s,int i, boolean flag)
        {
            super(serviceinput);
            useLocalFile = flag;
            countryCode = s;
            menuID = i;
        }
    }

    public void downloadBusinessFinderMenuItem()
    {
        abortAllProcess();
        final String countryCode = _countryCode;
        int i = _menuID;
        final BusinessFinderServiceInput serviceinput = new BusinessFinderServiceInput(countryCode, i);
        final boolean useLocalFile;
        if(serviceinput.getSaveFile().exists())
            useLocalFile = true;
        else
            useLocalFile = false;
        _service = new SUBCLASS2(serviceinput, countryCode,i, useLocalFile);
        _loadingIndicator.setVisibility(View.VISIBLE);
        _service.executeAsync();
        if(useLocalFile)
            BusinessFinderService.updateInBackground(_countryCode, _menuID);
    }

    public String getCountryCode()
    {
        return _countryCode;
    }

    public String getCountryName()
    {
        return _countryName;
    }

    public int getStateID()
    {
        return _stateID;
    }

    public String getStateName()
    {
        return _stateName;
    }

    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);
    }

    public void onCreate(Bundle bundle)
    {
        boolean flag = true;
        super.onCreate(bundle);
        int i;
        String temp;
        if(getArguments() != null)
            temp = getArguments().getString("countryCode");
        else
            temp = "";
        _countryCode = temp;
        if(getArguments() != null)
            temp = getArguments().getString("countryName");
        else
            temp = "";
        _countryName = temp;
        if(getArguments() != null)
            temp = getArguments().getString("stateName");
        else
            temp = "";
        _stateName = temp;
        if(getArguments() != null)
            i = getArguments().getInt("stateID");
        else
            i = 1;
        _stateID = i;
        i = ((flag) ? 1 : 0);
        if(getArguments() != null)
            i = getArguments().getInt("menuID");
        _menuID = i;
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        View temp = layoutinflater.inflate(R.layout.fragment_business_finder_item, viewgroup, false);
        _gridView = (GridView)temp.findViewById(R.id.GridView);
        _loadingIndicator = (ProgressBar)temp.findViewById(R.id.LoadingIndicator);
        initData();
        initEvent();
        downloadBusinessFinderMenuItem();
        return temp;
    }

    public void onDestroy()
    {
        abortAllProcess();
        super.onDestroy();
    }

    public void onDestroyView()
    {
        abortAllProcess();
        super.onDestroyView();
    }

    public void refresh()
    {
        _gridAdapter.clear();
        downloadBusinessFinderMenuItem();
    }

    public static final int BUSINESS_CATEGORY_SELECTED = 1;
    private String _countryCode;
    private String _countryName;
    private BusinessFinderItemGridAdapter _gridAdapter;
    private GridView _gridView;
    private ArrayList _imageServices;
    private ProgressBar _loadingIndicator;
    private int _menuID;
    private BusinessFinderService _service;
    private int _stateID;
    private String _stateName;





/*
    static BusinessFinderService access$302(BusinessFinderItemFragment businessfinderitemfragment, BusinessFinderService businessfinderservice)
    {
        businessfinderitemfragment._service = businessfinderservice;
        return businessfinderservice;
    }

*/



}
