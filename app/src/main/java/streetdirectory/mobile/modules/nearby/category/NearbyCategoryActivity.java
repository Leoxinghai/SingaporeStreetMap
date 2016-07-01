// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.nearby.category;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.core.service.HttpImageService;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.nearby.category.service.NearbyCategoryImageService;
import streetdirectory.mobile.modules.nearby.category.service.NearbyCategoryService;
import streetdirectory.mobile.modules.nearby.category.service.NearbyCategoryServiceInput;
import streetdirectory.mobile.modules.nearby.category.service.NearbyCategoryServiceOutput;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.nearby.category:
//            NearbyCategoryAdapter

public class NearbyCategoryActivity extends SDActivity
{

    public NearbyCategoryActivity()
    {
        _imageServices = new ArrayList();
    }

    private void downloadCategoryImage(final String final_s, int i, int j)
    {
        for(Iterator iterator = (new ArrayList(_imageServices)).iterator(); iterator.hasNext();)
            if(((HttpImageService)iterator.next()).tag.equals(final_s))
                return;

        NearbyCategoryImageService nearbycategoryimageservice = new NearbyCategoryImageService(final_s, i, j) {

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
                _gridAdapter.putImage(final_s, bitmap);
                _gridAdapter.notifyDataSetChanged();
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        nearbycategoryimageservice.tag = final_s;
        _imageServices.add(nearbycategoryimageservice);
        nearbycategoryimageservice.executeAsync();
    }

    private void downloadNearbyCategory()
    {
        abortAllProcess();
        final ArrayList categoryOutput = new ArrayList();
        categoryOutput.add(new NearbyCategoryServiceOutput(1, "Places", 0));
        categoryOutput.add(new NearbyCategoryServiceOutput(2, "Businesses", 0));
        if(_countryCode.equalsIgnoreCase("sg"))
            categoryOutput.add(new NearbyCategoryServiceOutput(1, 93, 0, "Bus Time"));
        final String countryCode = _countryCode;
        final NearbyCategoryServiceInput final_nearbycategoryserviceinput = new NearbyCategoryServiceInput(countryCode);
        final boolean useLocalFile;
        if(final_nearbycategoryserviceinput.getSaveFile().exists())
            useLocalFile = true;
        else
            useLocalFile = false;
        _service = new NearbyCategoryService(countryCode) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Nearby Category Aborted");
                _loadingIndicator.setVisibility(View.INVISIBLE);
                _service = null;
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Nearby Category Failed");
                _loadingIndicator.setVisibility(View.INVISIBLE);
                _service = null;
            }

            public void onReceiveData(NearbyCategoryServiceOutput nearbycategoryserviceoutput)
            {
                SDLogger.info("Nearby Category Received Data");
                categoryOutput.add(nearbycategoryserviceoutput);
            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((NearbyCategoryServiceOutput)sddataoutput);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                SDLogger.info("Nearby Category Success");
                if(!useLocalFile)
                    NearbyCategoryService.setVersion(countryCode, SDPreferences.getInstance().getCategoryVersion());
                _loadingIndicator.setVisibility(View.INVISIBLE);
                _service = null;
                _gridAdapter.setData(categoryOutput);
            }

        };
        _loadingIndicator.setVisibility(View.VISIBLE);
        _service.executeAsync();
        if(useLocalFile)
            NearbyCategoryService.updateInBackground(_countryCode);
    }

    private void initData()
    {
        Intent obj = getIntent();
        _countryCode = ((Intent) (obj)).getStringExtra("countryCode");
        String temp = ((Intent) (obj)).getStringExtra("title");
        if(temp != null)
            _titleLabel.setText(((CharSequence) (temp)));
        _gridAdapter = new NearbyCategoryAdapter(this);
        _gridView.setAdapter(_gridAdapter);
    }

    private void initEvent()
    {
        _cancelButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        _gridAdapter.setHandler(new NearbyCategoryAdapter.NearbyCategoryAdapterHandler() {

            public void onCategoryIconClicked(NearbyCategoryServiceOutput nearbycategoryserviceoutput)
            {
                Intent intent = new Intent();
                intent.putExtra("selectedCategory", (Parcelable)nearbycategoryserviceoutput);
                setResult(-1, intent);
                finish();
            }

            public void onCategoryIconNotFound(NearbyCategoryServiceOutput nearbycategoryserviceoutput, int i, int j)
            {
                downloadCategoryImage(nearbycategoryserviceoutput.icon, i, j);
            }

        });
    }

    private void initLayout()
    {
        _titleLabel = (TextView)findViewById(R.id.TitleLabel);
        _cancelButton = (Button)findViewById(R.id.CancelButton);
        _gridView = (GridView)findViewById(R.id.GridView);
        _loadingIndicator = (ProgressBar)findViewById(R.id.LoadingIndicator);
    }

    public void abortAllProcess()
    {
        if(_service != null)
        {
            _service.abort();
            _service = null;
        }
        for(Iterator iterator = _imageServices.iterator(); iterator.hasNext(); ((NearbyCategoryImageService)iterator.next()).abort());
        _imageServices.clear();
        _gridAdapter.abortAllProcess();
    }

    public void finish()
    {
        super.finish();
        overridePendingTransition(0x10a0000, 0x10a0001);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_nearby_category);
        SDStory.post(URLFactory.createGantNearbyCategory(), SDStory.createDefaultParams());
        initLayout();
        initData();
        initEvent();
        downloadNearbyCategory();
    }

    protected void onDestroy()
    {
        abortAllProcess();
        super.onDestroy();
    }

    private Button _cancelButton;
    private String _countryCode;
    private NearbyCategoryAdapter _gridAdapter;
    private GridView _gridView;
    private ArrayList _imageServices;
    private ProgressBar _loadingIndicator;
    private NearbyCategoryService _service;
    private TextView _titleLabel;




/*
    static NearbyCategoryService access$202(NearbyCategoryActivity nearbycategoryactivity, NearbyCategoryService nearbycategoryservice)
    {
        nearbycategoryactivity._service = nearbycategoryservice;
        return nearbycategoryservice;
    }

*/


}
