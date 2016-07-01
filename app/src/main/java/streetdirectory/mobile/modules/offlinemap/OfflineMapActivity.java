// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.offlinemap;

import android.app.AlertDialog;
import android.content.*;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.*;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.map.MapActivity;
import streetdirectory.mobile.modules.offlinemap.service.OfflineMapBackgroundService;
import streetdirectory.mobile.modules.offlinemap.service.OfflineMapBroadcastReceiver;
import streetdirectory.mobile.modules.offlinemap.service.OfflineMapImageService;
import streetdirectory.mobile.modules.offlinemap.service.OfflineMapListService;
import streetdirectory.mobile.modules.offlinemap.service.OfflineMapListServiceInput;
import streetdirectory.mobile.modules.offlinemap.service.OfflineMapListServiceOutput;
import streetdirectory.mobile.modules.offlinemap.service.OfflineMapPreference;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.offlinemap:
//            OfflineMapAdapter, OfflineMapSectionHeader, OfflineMapImagePreviewActivity

public class OfflineMapActivity extends SDActivity
{

    public OfflineMapActivity()
    {
        _imageServices = new ArrayList();
        mReceiver = new OfflineMapBroadcastReceiver() {

            public void onFailedDownload(int i, String s, int j)
            {
                downloadFailed(i, s, j);
            }

            public void onFinishDownload(int i, String s, int j)
            {
                downloadFinish(i, s, j);
            }

            public void onPauseDownload(int i, String s, int j)
            {
                _listAdapter.notifyDataSetChanged();
            }

            public void onStartDownload(int i, String s, int j)
            {
                _listAdapter.notifyDataSetChanged();
            }

        };
    }

    private void abortAllDownload()
    {
        if(_service != null)
        {
            _service.abort();
            _service = null;
        }
        for(Iterator iterator = _imageServices.iterator(); iterator.hasNext(); ((OfflineMapImageService)iterator.next()).abort());
        _imageServices.clear();
    }

    private void checkActiveDownload()
    {
        if(_listAdapter != null && !OfflineMapBackgroundService.isRunning(this))
        {
            SDPreferences sdpreferences = SDPreferences.getInstance();
            int j = _listAdapter.getGroupCount();
            for(int i = 0; i < j; i++)
            {
                Iterator iterator = _listAdapter.getGroup(i).childs.iterator();
                for(;iterator.hasNext();) {
                    OfflineMapListServiceOutput offlinemaplistserviceoutput = (OfflineMapListServiceOutput)iterator.next();
                    if(OfflineMapPreference.getStatus(offlinemaplistserviceoutput.packageID) == 1)
                        OfflineMapPreference.setStatus(sdpreferences, offlinemaplistserviceoutput.packageID, 2);
                }
            }

        }
    }

    private void deleteDownload(final int packageID, final String packageName, final int parentID)
    {

        android.content.DialogInterface.OnClickListener onclicklistener = new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                Iterator iterator = null;
                switch(i)
                {
                default:
                    return;

                case -1:
                    iterator = _listAdapter.datas.iterator();
                    break;
                }
                do
                {
                    if(!iterator.hasNext())
                        break;
                    OfflineMapSectionHeader offlinemapsectionheader = (OfflineMapSectionHeader)iterator.next();
                    if(offlinemapsectionheader.packageID != parentID)
                        continue;
                    SDStory.post(URLFactory.createGantOfflineMapDeleteDownload(offlinemapsectionheader.name, packageName), SDStory.createDefaultParams());
                    break;
                } while(true);
                startService(packageID, packageName, parentID, 2);
            }

        };
        (new android.app.AlertDialog.Builder(this)).setTitle((new StringBuilder()).append("Delete ").append(packageName).append(" Map").toString()).setMessage("You can always download or update the offline maps again.").setPositiveButton("Delete Now", onclicklistener).setNegativeButton("Cancel", onclicklistener).show();
    }

    private void downloadFailed(final int packageID, final String packageName, final int parentID)
    {
        android.content.DialogInterface.OnClickListener onclicklistener = new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                switch(i)
                {
                default:
                    return;

                case -1:
                    resumeDownload(packageID, packageName, parentID);
                    break;
                }
            }

        };
        (new android.app.AlertDialog.Builder(this)).setTitle((new StringBuilder()).append(packageName).append(" Map Download Interrupted").toString()).setMessage("Do you want to retry?").setPositiveButton("Try Again", onclicklistener).setNegativeButton("Later", onclicklistener).show();
    }

    private void downloadFinish(int i, String s, int j)
    {
        android.content.DialogInterface.OnClickListener onclicklistener = new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int k)
            {
                switch(k)
                {
                default:
                    return;

                case -1:
                    finish();
                    break;
                }
                startActivity(new Intent(OfflineMapActivity.this, MapActivity.class));
            }
        };
        Iterator iterator = _listAdapter.datas.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            OfflineMapSectionHeader offlinemapsectionheader = (OfflineMapSectionHeader)iterator.next();
            if(offlinemapsectionheader.packageID != j)
                continue;
            SDStory.post(URLFactory.createGantOfflineMapSuccessDownload(offlinemapsectionheader.name, s), SDStory.createDefaultParams());
            break;
        } while(true);
        (new android.app.AlertDialog.Builder(this)).setTitle((new StringBuilder()).append(s).append(" Map Download Successful").toString()).setMessage((new StringBuilder()).append("Congratulations, Your Streetdirectory.com App is now loaded with ").append(s).append(" Map").toString()).setPositiveButton("Go to Offline map", onclicklistener).setNegativeButton("Later", onclicklistener).show();
    }

    private void downloadImage(final String final_s, int i, int j)
    {
        for(Iterator iterator = _imageServices.iterator(); iterator.hasNext();)
            if(((OfflineMapImageService)iterator.next()).tag.equals(final_s))
                return;

        OfflineMapImageService offlinemapimageservice = new OfflineMapImageService(final_s, i, j) {

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
                _listAdapter.putImage(final_s, bitmap);
                _listAdapter.notifyDataSetChanged();
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }
        };
        offlinemapimageservice.tag = final_s;
        _imageServices.add(offlinemapimageservice);
        offlinemapimageservice.executeAsync();
    }

    private void downloadListPackage()
    {
        abortAllDownload();
        _tryAgainLayout.setVisibility(View.INVISIBLE);
        _listAdapter.clear();
        _service = new OfflineMapListService() {

            public void onAborted(Exception exception)
            {
                SDLogger.info("OfflineMap List Aborted");
                _service = null;
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "OfflineMap List Failed");
                _service = null;
                _loadingIndicator.setVisibility(View.INVISIBLE);
                _listView.setVisibility(View.INVISIBLE);
                _tryAgainLayout.setVisibility(View.VISIBLE);
            }

            public void onReceiveData(OfflineMapListServiceOutput offlinemaplistserviceoutput)
            {
            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((OfflineMapListServiceOutput)sddataoutput);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                Iterator iterator = sdhttpserviceoutput.childs.iterator();
                for(;iterator.hasNext();) {
                    OfflineMapListServiceOutput offlinemaplistserviceoutput1 = (OfflineMapListServiceOutput)iterator.next();
                    if(offlinemaplistserviceoutput1.parentID == 0)
                        _listAdapter.addSectionHeader(offlinemaplistserviceoutput1);
                }
                Iterator iterator2 = sdhttpserviceoutput.childs.iterator();
                for(;iterator2.hasNext();) {
                    OfflineMapListServiceOutput offlinemaplistserviceoutput = (OfflineMapListServiceOutput)iterator2.next();
                    if(offlinemaplistserviceoutput.parentID != 0)
                        _listAdapter.addChild(offlinemaplistserviceoutput.parentID, offlinemaplistserviceoutput);
                }
                SDLogger.info("OfflineMap List Success");
                _service = null;
                checkActiveDownload();
                _loadingIndicator.setVisibility(View.INVISIBLE);
                _listAdapter.notifyDataSetChanged();
                saveData(_listAdapter.datas);
                if(_listAdapter.getGroupCount() >= 2)
                {
                    _listView.expandGroup(0);
                    _listView.expandGroup(1);
                }
                startTimer();
            }

        };
        _loadingIndicator.setVisibility(View.VISIBLE);
        _service.executeAsync();
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void pauseDownload(int i, String s, int j)
    {
        startService(i, s, j, 1);
    }

    private void refresh()
    {
        OfflineMapListServiceInput.deleteAllCache();
        OfflineMapImageService.deleteAllCache();
        downloadListPackage();
    }

    private void resumeDownload(int i, String s, int j)
    {
        startService(i, s, j, 0);
    }

    private void saveData(ArrayList arraylist)
    {
        OfflineMapSectionHeader offlinemapsectionheader;
        ArrayList arraylist1;
        Iterator iterator;
        for(iterator = arraylist.iterator(); iterator.hasNext(); OfflineMapPreference.setPackageList(offlinemapsectionheader.packageID, arraylist1))
        {
            offlinemapsectionheader = (OfflineMapSectionHeader)iterator.next();
            arraylist1 = new ArrayList();
            for(Iterator iterator2 = offlinemapsectionheader.childs.iterator(); iterator2.hasNext(); arraylist1.add(Integer.valueOf(((OfflineMapListServiceOutput)iterator2.next()).packageID)));
        }

    }

    private void showExitConfirmDialog()
    {
        (new android.app.AlertDialog.Builder(this)).setTitle("Confirmation").setMessage("Are you sure want to exit ?").setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                finish();
            }

        }).setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                dialoginterface.cancel();
            }

        }).create().show();
    }

    private void startDownload(final int packageID, final String packageName, final int parentID)
    {

        android.content.DialogInterface.OnClickListener onclicklistener = new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                Iterator iterator;
                switch(i)
                {
                default:
                    return;

                case -1:
                    iterator = _listAdapter.datas.iterator();
                    break;
                }
                do
                {
                    if(!iterator.hasNext())
                        break;
                    OfflineMapSectionHeader offlinemapsectionheader = (OfflineMapSectionHeader)iterator.next();
                    if(offlinemapsectionheader.packageID != parentID)
                        continue;
                    SDStory.post(URLFactory.createGantOfflineMapStartDownload(offlinemapsectionheader.name, packageName), SDStory.createDefaultParams());
                    break;
                } while(true);
                resumeDownload(packageID, packageName, parentID);
            }

        }
;
        (new android.app.AlertDialog.Builder(this)).setTitle((new StringBuilder()).append("Download ").append(packageName).append(" Map").toString()).setMessage("Offline maps are stored to your phone for faster access. You still need Internet connection for searching and navigation.").setPositiveButton("Download Now", onclicklistener).setNegativeButton("Cancel", onclicklistener).show();
    }

    private void startService(int i, String s, int j, int k)
    {
        Intent intent = new Intent(this, OfflineMapBackgroundService.class);
        intent.putExtra("mode", k);
        intent.putExtra("packageID", i);
        intent.putExtra("parentID", j);
        intent.putExtra("packageName", s);
        startService(intent);
    }

    private void startTimer()
    {
        stopTimer();
        mTimer = new Timer();
        TimerTask timertask = new TimerTask() {

            public void run()
            {
                runOnUiThread(new Runnable() {

                    public void run()
                    {
                        _listAdapter.notifyDataSetChanged();
                    }

                });
            }
        };
        mTimer.schedule(timertask, 2000L, 2000L);
    }

    private void stopBackgroundService()
    {
    }

    private void stopTimer()
    {
        if(mTimer != null)
        {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void updateDownload(final int packageID, final String packageName, final int parentID)
    {
        android.content.DialogInterface.OnClickListener onclicklistener = new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                Iterator iterator = null;
                switch(i)
                {
                default:
                    return;

                case -1:
                    iterator = _listAdapter.datas.iterator();
                    break;
                }
                do
                {
                    if(!iterator.hasNext())
                        break;
                    OfflineMapSectionHeader offlinemapsectionheader = (OfflineMapSectionHeader)iterator.next();
                    if(offlinemapsectionheader.packageID != parentID)
                        continue;
                    SDStory.post(URLFactory.createGantOfflineMapUpdateDownload(offlinemapsectionheader.name, packageName), SDStory.createDefaultParams());
                    break;
                } while(true);
                resumeDownload(packageID, packageName, parentID);
            }

        };
        (new android.app.AlertDialog.Builder(this)).setTitle((new StringBuilder()).append("Update ").append(packageName).append(" Map").toString()).setMessage("Updating the maps monthly will get you the most updated maps.").setPositiveButton("Update Now", onclicklistener).setNegativeButton("Cancel", onclicklistener).show();
    }

    protected void abortAllProcess()
    {
        stopTimer();
        abortAllDownload();
        super.abortAllProcess();
    }

    protected void initData()
    {
        if(_listView != null)
        {
            _listAdapter = new OfflineMapAdapter(this);
            _listAdapter.setListener(new OfflineMapAdapter.OfflineMapAdapterListener() {

                public void onDeleteButtonClicked(int i, String s, int j)
                {
                    deleteDownload(i, s, j);
                }

                public void onDownloadButtonClicked(int i, String s, int j)
                {
                    startDownload(i, s, j);
                }

                public void onImageClicked(Bitmap bitmap, OfflineMapListServiceOutput offlinemaplistserviceoutput)
                {
                    Intent intent = new Intent(OfflineMapActivity.this, OfflineMapImagePreviewActivity.class);
                    intent.putExtra("title", (new StringBuilder()).append(offlinemaplistserviceoutput.name).append(" Map").toString());
                    intent.putExtra("detail", offlinemaplistserviceoutput.desc);
                    intent.putExtra("thumbnail", bitmap);
                    intent.putExtra("imageID", offlinemaplistserviceoutput.imageID);
                    startActivity(intent);
                    overridePendingTransition(0x10a0000, 0x10a0001);
                }

                public void onImageNotFound(String s, int i, int j)
                {
                    downloadImage(s, i, j);
                }

                public void onPauseButtonClicked(int i, String s, int j)
                {
                    pauseDownload(i, s, j);
                }

                public void onResumeButtonClicked(int i, String s, int j)
                {
                    resumeDownload(i, s, j);
                }

                public void onUpdateButtonClicked(int i, String s, int j)
                {
                    updateDownload(i, s, j);
                }

                public void onUpdateNotAvailableButtonClicked(int i, String s, int j)
                {
                    (new android.app.AlertDialog.Builder(OfflineMapActivity.this)).setTitle((new StringBuilder()).append(s).append(" Map").toString()).setMessage("Your current version is the most updated.").setPositiveButton("OK", null).show();
                }

            });
            _listView.setAdapter(_listAdapter);
        }
    }

    protected void initEvent()
    {
        _refreshButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                refresh();
            }

        });
        _tryAgainButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                refresh();
            }

        });
        mMenuLayout.setOnSlideOpen(new Action() {

            public void execute()
            {
                mSideMenuBlocker.setVisibility(View.VISIBLE);
            }

        });
        mMenuLayout.setOnSlideClose(new Action() {

            public void execute()
            {
                mSideMenuBlocker.setVisibility(View.INVISIBLE);
            }

        });
        mSideMenuBlocker.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mMenuLayout.touchExecutor(motionevent);
                return true;
            }

        });
        _menuButton.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mMenuLayout.touchExecutor(motionevent);
                return false;
            }

        });
        _menuButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mMenuLayout.getIsMenuOpen())
                {
                    mMenuLayout.slideClose();
                    return;
                } else
                {
                    mMenuLayout.slideOpen();
                    return;
                }
            }

        });
        _backButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
    }

    protected void initLayout()
    {
        _menuButton = (ImageButton)findViewById(R.id.MenuButton);
        _refreshButton = (Button)findViewById(R.id.RefreshButton);
        _backButton = (Button)findViewById(R.id.BackButton);
        _tryAgainLayout = (LinearLayout)findViewById(R.id.TryAgainLayout);
        _tryAgainButton = (Button)findViewById(R.id.TryAgainButton);
        _listView = (ExpandableListView)findViewById(R.id.ListView);
        _loadingIndicator = (ProgressBar)findViewById(R.id.LoadingIndicator);
        mMenuLayout = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mSideMenuBlocker = findViewById(R.id.side_menu_blocker);
        mSearchListView = (ListView)mMenuLayout.findViewById(R.id.SearchListView);
        mMenuListView = (ListView)mMenuLayout.findViewById(R.id.MenuListView);
        mSearchField = (EditText)mMenuLayout.findViewById(R.id.MenuSearchField);
        mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        isfromDailyOfferAlert = getIntent().getBooleanExtra("fromDailyOfferAlert", false);
        if(isfromDailyOfferAlert)
        {
            _backButton.setVisibility(View.VISIBLE);
            _menuButton.setVisibility(View.INVISIBLE);
            return;
        } else
        {
            _backButton.setVisibility(View.INVISIBLE);
            _menuButton.setVisibility(View.VISIBLE);
            return;
        }
    }

    public void onBackPressed()
    {
        if(isfromDailyOfferAlert)
        {
            finish();
            return;
        }
        if(mMenuLayout.getIsMenuOpen())
        {
            if(mSearchListView.getVisibility() == View.VISIBLE)
            {
                mMenuListView.setVisibility(View.VISIBLE);
                mSearchListView.setVisibility(View.INVISIBLE);
                mgr.hideSoftInputFromWindow(mSearchField.getWindowToken(), 0);
                mMenuLayout.requestFocus();
                mMenuLayout.slideOpen(77);
                return;
            } else
            {
                Intent intent = new Intent(this, MapActivity.class);
                intent.setFlags(0x4000000);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
                return;
            }
        } else
        {
            mMenuLayout.slideOpen();
            return;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_offline_map);
        SDStory.post(URLFactory.createGantOfflineMapDownloadPage(), SDStory.createDefaultParams());
        initialize();
        downloadListPackage();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(mMenuLayout.getIsMenuOpen())
            mMenuLayout.slideClose();
        else
            mMenuLayout.slideOpen();
        return false;
    }

    protected void onDestroy()
    {
        stopBackgroundService();
        super.onDestroy();
    }

    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    protected void onResume()
    {
        super.onResume();
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("streetdirectory.mobile.modules.offlinemap.start");
        intentfilter.addAction("streetdirectory.mobile.modules.offlinemap.pause");
        intentfilter.addAction("streetdirectory.mobile.modules.offlinemap.failed");
        intentfilter.addAction("streetdirectory.mobile.modules.offlinemap.finish");
        registerReceiver(mReceiver, intentfilter);
        checkActiveDownload();
    }

    private Button _backButton;
    private ArrayList _imageServices;
    private OfflineMapAdapter _listAdapter;
    private ExpandableListView _listView;
    private ProgressBar _loadingIndicator;
    private ImageButton _menuButton;
    private Button _refreshButton;
    private OfflineMapListService _service;
    private Button _tryAgainButton;
    private LinearLayout _tryAgainLayout;
    private boolean isfromDailyOfferAlert;
    private SDMapSideMenuLayout mMenuLayout;
    private ListView mMenuListView;
    private OfflineMapBroadcastReceiver mReceiver;
    private EditText mSearchField;
    private ListView mSearchListView;
    private View mSideMenuBlocker;
    private Timer mTimer;
    private InputMethodManager mgr;






/*
    static OfflineMapListService access$1202(OfflineMapActivity offlinemapactivity, OfflineMapListService offlinemaplistservice)
    {
        offlinemapactivity._service = offlinemaplistservice;
        return offlinemaplistservice;
    }

*/
















}
