// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.Action;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.facebook.service.FacebookPhotoImageService;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.friend.FriendListAdapter;
import streetdirectory.mobile.modules.friend.PendingFriendListAdapter;
import streetdirectory.mobile.modules.friend.service.*;
import streetdirectory.mobile.modules.profile.ProfileActivity;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;
import streetdirectory.mobile.service.SDHttpServiceOutput;

public class AccountActivity extends SDActivity
{

    public AccountActivity()
    {
        mFriendPhotoImageServices = new ArrayList();
        mPendingPhotoImageServices = new ArrayList();
        mMemberPhotoImageServices = new ArrayList();
    }

    private void abortAllDownload()
    {
        abortDownloadFacebookPhotoImage();
        abortAllTabDownload();
    }

    private void abortAllTabDownload()
    {
        abortDownloadFriendList();
        abortDownloadFriendPhotoImage();
        abortDownloadRequestFriendList();
        abortDownloadPendingFriendList();
        abortDownloadPendingPhotoImage();
        abortDownloadSuggestedFriendList();
        abortDownloadMemberPhotoImage();
    }

    private void abortDownloadFacebookPhotoImage()
    {
        if(mFacebookPhotoImageService != null)
        {
            mFacebookPhotoImageService.abort();
            mFacebookPhotoImageService = null;
        }
    }

    private void abortDownloadFriendList()
    {
        if(mFriendListService != null)
        {
            mFriendListService.abort();
            mFriendListService = null;
        }
    }

    private void abortDownloadFriendPhotoImage()
    {
        for(Iterator iterator = mFriendPhotoImageServices.iterator(); iterator.hasNext(); ((FacebookPhotoImageService)iterator.next()).abort());
        mFriendPhotoImageServices.clear();
    }

    private void abortDownloadMemberPhotoImage()
    {
        for(Iterator iterator = mMemberPhotoImageServices.iterator(); iterator.hasNext(); ((FacebookPhotoImageService)iterator.next()).abort());
        mMemberPhotoImageServices.clear();
    }

    private void abortDownloadPendingFriendList()
    {
        if(mPendingFriendListService != null)
        {
            mPendingFriendListService.abort();
            mPendingFriendListService = null;
        }
    }

    private void abortDownloadPendingPhotoImage()
    {
        for(Iterator iterator = mPendingPhotoImageServices.iterator(); iterator.hasNext(); ((FacebookPhotoImageService)iterator.next()).abort());
        mPendingPhotoImageServices.clear();
    }

    private void abortDownloadRequestFriendList()
    {
        if(mRequestFriendListService != null)
        {
            mRequestFriendListService.abort();
            mRequestFriendListService = null;
        }
    }

    private void abortDownloadSuggestedFriendList()
    {
        if(mSuggestedFriendListService != null)
        {
            mSuggestedFriendListService.abort();
            mSuggestedFriendListService = null;
        }
    }

    private void downloadFacebookPhotoImage(String s, int i, int j)
    {
        abortDownloadFacebookPhotoImage();
        mFacebookPhotoImageService = new FacebookPhotoImageService(s, i, j) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Facebook Photo Image Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Facebook Photo Image Failed");
                mFacebookPhotoImageService = null;
            }

            public void onSuccess(Bitmap bitmap)
            {
                mFacebookPhotoImageService = null;
                mImagebuttonPhoto.setImageBitmap(bitmap);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        mFacebookPhotoImageService.executeAsync();
    }

    private void downloadFriendList(int i)
    {
        if(mFriendListService == null)
        {
            abortAllTabDownload();
            mFriendListService = new FriendListService(new FriendListServiceInput(mCountryCode, mUid, "", false, i, 16)) {

                public void onAborted(Exception exception)
                {
                    SDLogger.info("Friend List Aborted");
                }

                public void onFailed(Exception exception)
                {
                    SDLogger.printStackTrace(exception, "Friend List Failed");
                    mFriendListService = null;
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mFriendListService = null;
                    mFriendListAdapter.setTotal(sdhttpserviceoutput.total);
                    mFriendListAdapter.addItems(sdhttpserviceoutput.childs);
                    mFriendListAdapter.notifyDataSetChanged();
                }

            };

            mFriendListService.executeAsync();
        }
    }

    private void downloadFriendPhotoImage(final String final_s, int i, int j)
    {
        for(Iterator iterator = mFriendPhotoImageServices.iterator(); iterator.hasNext();)
            if(((String)((FacebookPhotoImageService)iterator.next()).tag).equals(final_s))
                return;

        FacebookPhotoImageService facebookphotoimageservice = new FacebookPhotoImageService(final_s, i, j) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Friend Photo Image Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Friend Photo Image Failed");
                mFriendPhotoImageServices.remove(this);
            }

            public void onSuccess(Bitmap bitmap)
            {
                mFriendPhotoImageServices.remove(this);
                mFriendListAdapter.putImage(final_s, bitmap);
                mFriendListAdapter.notifyDataSetChanged();
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }


        }
;
        facebookphotoimageservice.tag = final_s;
        mFriendPhotoImageServices.add(facebookphotoimageservice);
        facebookphotoimageservice.executeAsync();
    }

    private void downloadMemberPhotoImage(final String final_s, int i, int j)
    {
        for(Iterator iterator = mMemberPhotoImageServices.iterator(); iterator.hasNext();)
            if(((String)((FacebookPhotoImageService)iterator.next()).tag).equals(final_s))
                return;

        FacebookPhotoImageService facebookphotoimageservice = new FacebookPhotoImageService(final_s,i, j) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Member Photo Image Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Member Photo Image Failed");
                mMemberPhotoImageServices.remove(this);
            }

            public void onSuccess(Bitmap bitmap)
            {
                mMemberPhotoImageServices.remove(this);
                mMemberListAdapter.putImage(final_s, bitmap);
                mMemberListAdapter.notifyDataSetChanged();
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        facebookphotoimageservice.tag = final_s;
        mMemberPhotoImageServices.add(facebookphotoimageservice);
        facebookphotoimageservice.executeAsync();
    }

    private void downloadPendingFriendList()
    {
        mPendingFriendListService = new PendingFriendListService(new PendingFriendListServiceInput(mCountryCode, mUid)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Pending Friend List Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Pending Friend List Failed");
                mPendingFriendListService = null;
                mIndicatorLoading.setVisibility(View.INVISIBLE);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mPendingFriendListService = null;
                mIndicatorLoading.setVisibility(View.INVISIBLE);
                mPendingListAdapter.addPendingItems(sdhttpserviceoutput.childs);
                mPendingListAdapter.notifyDataSetChanged();
                int j = mPendingListAdapter.getGroupCount();
                for(int i = 0; i < j; i++)
                    mListviewFriends.expandGroup(i);

            }
        };
        mPendingFriendListService.executeAsync();
    }

    private void downloadPendingList()
    {
        abortAllProcess();
        int i = mPendingListAdapter.getRequestDataSize();
        int j = mPendingListAdapter.getPendingDataSize();
        if(i == 0 && j == 0)
            mIndicatorLoading.setVisibility(View.VISIBLE);
        if(i == 0)
            downloadRequestFriendList();
        if(j == 0)
            downloadPendingFriendList();
    }

    private void downloadPendingPhotoImage(final String final_s, int i, int j)
    {
        for(Iterator iterator = mPendingPhotoImageServices.iterator(); iterator.hasNext();)
            if(((String)((FacebookPhotoImageService)iterator.next()).tag).equals(final_s))
                return;

        FacebookPhotoImageService facebookphotoimageservice = new FacebookPhotoImageService(final_s, i, j) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Pending Photo Image Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Pending Photo Image Failed");
                mPendingPhotoImageServices.remove(this);
            }

            public void onSuccess(Bitmap bitmap)
            {
                mPendingPhotoImageServices.remove(this);
                mPendingListAdapter.putImage(final_s, bitmap);
                mPendingListAdapter.notifyDataSetChanged();
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        facebookphotoimageservice.tag = final_s;
        mPendingPhotoImageServices.add(facebookphotoimageservice);
        facebookphotoimageservice.executeAsync();
    }

    private void downloadRequestFriendList()
    {
        mRequestFriendListService = new RequestFriendListService(new RequestFriendListServiceInput(mCountryCode, mUid)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Request Friend List Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Request Friend List Failed");
                mRequestFriendListService = null;
                mIndicatorLoading.setVisibility(View.INVISIBLE);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mRequestFriendListService = null;
                mIndicatorLoading.setVisibility(View.INVISIBLE);
                mPendingListAdapter.addRequestItems(sdhttpserviceoutput.childs);
                mPendingListAdapter.notifyDataSetChanged();
                int j = mPendingListAdapter.getGroupCount();
                for(int i = 0; i < j; i++)
                    mListviewFriends.expandGroup(i);

            }

        };

        mRequestFriendListService.executeAsync();
    }

    private void downloadSuggestedFriendList(int i, String s)
    {
        if(mSuggestedFriendListService == null)
        {
            abortAllTabDownload();
            mSuggestedFriendListService = new SuggestedFriendListService(new SuggestedFriendListServiceInput(mCountryCode, mUid, s, i, 16)) {

                public void onAborted(Exception exception)
                {
                    SDLogger.info("Suggested Friend List Aborted");
                }

                public void onFailed(Exception exception)
                {
                    SDLogger.printStackTrace(exception, "Suggested Friend List Failed");
                    mSuggestedFriendListService = null;
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mSuggestedFriendListService = null;
                    mMemberListAdapter.setTotal(sdhttpserviceoutput.total);
                    mMemberListAdapter.addItems(sdhttpserviceoutput.childs);
                    mMemberListAdapter.notifyDataSetChanged();
                }

            };

            mSuggestedFriendListService.executeAsync();
        }
    }

    private void hideMemberSearchBar()
    {
        mImagebuttonSearch.setPressed(false);
        mLayoutSearch.setVisibility(View.INVISIBLE);
    }

    private void initData()
    {
        Intent intent = getIntent();
        mUid = intent.getStringExtra("uid");
        mUsername = intent.getStringExtra("username");
        mCountryCode = intent.getStringExtra("countryCode");
        mTextviewProfileName.setText(mUsername);
        mFriendListAdapter = new FriendListAdapter(this);
        mFriendListAdapter.setFriendListAdapterListener(new streetdirectory.mobile.modules.friend.FriendListAdapter.FriendListAdapterListener() {

            public void onLoadMore(int i) {
                downloadFriendList(i);
            }

            public void onPhotoClicked(FriendListServiceOutput friendlistserviceoutput) {
                startActivityProfile(friendlistserviceoutput.userID, friendlistserviceoutput.userName);
            }

            public void onPhotoNotFound(FriendListServiceOutput friendlistserviceoutput, int i, int j) {
                downloadFriendPhotoImage(friendlistserviceoutput.userID, i, j);
            }

        });

        mGridviewFriends.setAdapter(mFriendListAdapter);
        mPendingListAdapter = new PendingFriendListAdapter(this);
        mPendingListAdapter.setPendingFriendListAdapterListener(new streetdirectory.mobile.modules.friend.PendingFriendListAdapter.PendingFriendListAdapterListener() {

            public void onConfirmButtonClicked(FriendListServiceOutput friendlistserviceoutput) {
            }

            public void onPhotoNotFound(FriendListServiceOutput friendlistserviceoutput, int i, int j) {
                downloadPendingPhotoImage(friendlistserviceoutput.userID, i, j);
            }

            public void onRejectButtonClicked(FriendListServiceOutput friendlistserviceoutput) {
            }

        });

        mListviewFriends.setAdapter(mPendingListAdapter);
        mMemberListAdapter = new FriendListAdapter(this);
        mMemberListAdapter.setFriendListAdapterListener(new streetdirectory.mobile.modules.friend.FriendListAdapter.FriendListAdapterListener() {

            public void onLoadMore(int i) {
                downloadSuggestedFriendList(i, mEdittextSearch.getText().toString());
            }

            public void onPhotoClicked(FriendListServiceOutput friendlistserviceoutput) {
                startActivityProfile(friendlistserviceoutput.userID, friendlistserviceoutput.userName);
            }

            public void onPhotoNotFound(FriendListServiceOutput friendlistserviceoutput, int i, int j) {
                downloadMemberPhotoImage(friendlistserviceoutput.userID, i, j);
            }

        });
    }

    private void initEvent()
    {
        mSideMenu.setOnSlideOpen(new Action() {

            public void execute()
            {
                mSideMenuBlocker.setVisibility(View.VISIBLE);
            }

        });
        mSideMenu.setOnSlideClose(new Action() {

            public void execute()
            {
                mSideMenuBlocker.setVisibility(View.INVISIBLE);
            }

        });

        mSideMenuBlocker.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mSideMenu.touchExecutor(motionevent);
                return true;
            }

        });
        mImagebuttonMenu.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mSideMenu.touchExecutor(motionevent);
                return false;
            }

        });
        mImagebuttonMenu.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mSideMenu.getIsMenuOpen())
                {
                    mSideMenu.slideClose();
                    return;
                } else
                {
                    mSideMenu.slideOpen();
                    return;
                }
            }

        });
        mImagebuttonPhoto.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }
        });
        mButtonPrivacy.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }
        });

        mImagebuttonPhoto.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout()
            {
                mImagebuttonPhoto.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                downloadFacebookPhotoImage(mUid, mImagebuttonPhoto.getWidth(), mImagebuttonPhoto.getHeight());
            }

        });

        mButtonInbox.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });

        mButtonAll.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                if(!mButtonAll.isPressed())
                    setAllButtonPressed();
                return true;
            }

        });

        mButtonPending.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                if(!mButtonPending.isPressed())
                    setPendingButtonPressed();
                return true;
            }

        });

        mButtonMembers.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                if(!mButtonMembers.isPressed())
                    setMemberButtonPressed();
                return true;
            }

        });
        mImagebuttonSearch.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                if(motionevent.getAction() == 1)
                {
                    if(mImagebuttonSearch.isPressed()) {
                        hideMemberSearchBar();
                        return true;

                    }
                    showMemberSearchBar();
                }
                return true;
            }
        });

        mLayoutSearch.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout()
            {
                mLayoutSearch.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mSearchBarHeight = mLayoutSearch.getHeight();
            }

        });
        mListviewFriends.setOnChildClickListener(new android.widget.ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView expandablelistview, View view, int i, int j, long l)
            {
                FriendListServiceOutput friendListServiceOutput = mPendingListAdapter.getChild(i, j);
                startActivityProfile(((FriendListServiceOutput) (friendListServiceOutput)).userID, ((FriendListServiceOutput) (friendListServiceOutput)).userName);
                return false;
            }

        });

        mButtonSearch.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                abortAllTabDownload();
                mMemberListAdapter.clearData();
                mMemberListAdapter.notifyDataSetChanged();
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEdittextSearch.getWindowToken(), 0);
                downloadSuggestedFriendList(0, mEdittextSearch.getText().toString());
            }

        });
    }

    private void initLayout()
    {
        mSideMenu = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mButtonPrivacy = (Button)findViewById(R.id.button_privacy);
        mImagebuttonPhoto = (ImageButton)findViewById(R.id.imagebutton_photo);
        mTextviewProfileName = (TextView)findViewById(R.id.textview_profile_name);
        mButtonInbox = (Button)findViewById(R.id.button_inbox);
        mButtonInboxNotification = (Button)findViewById(R.id.button_inbox_notification);
        mButtonAll = (Button)findViewById(R.id.button_all);
        mButtonPending = (Button)findViewById(R.id.button_pending);
        mButtonMembers = (Button)findViewById(R.id.button_members);
        mImagebuttonSearch = (ImageButton)findViewById(R.id.imagebutton_member_search);
        mLayoutSearch = (RelativeLayout)findViewById(R.id.layout_search);
        mEdittextSearch = (EditText)findViewById(R.id.edittext_search);
        mButtonSearch = (Button)findViewById(R.id.button_search);
        mGridviewFriends = (GridView)findViewById(R.id.gridview_friends);
        mListviewFriends = (ExpandableListView)findViewById(R.id.listview_friends);
        mIndicatorLoading = (ProgressBar)findViewById(R.id.indicator_loading);
        mSideMenuBlocker = findViewById(R.id.side_menu_blocker);
        mImagebuttonMenu = (ImageButton)findViewById(R.id.imagebutton_menu);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void setAllButtonPressed()
    {
        mSelectedMenu = mButtonAll;
        mButtonAll.setPressed(true);
        mButtonPending.setPressed(false);
        mButtonMembers.setPressed(false);
        setSearchButtonEnabled(false);
        mEdittextSearch.setText("");
        mMemberListAdapter.clearData();
        mGridviewFriends.setAdapter(mFriendListAdapter);
        mGridviewFriends.setVisibility(View.VISIBLE);
        mListviewFriends.setVisibility(View.INVISIBLE);
        mIndicatorLoading.setVisibility(View.INVISIBLE);
        int i = mFriendListAdapter.getChildSize();
        if(i == 0)
            downloadFriendList(i);
    }

    private void setMemberButtonPressed()
    {
        mSelectedMenu = mButtonMembers;
        mButtonAll.setPressed(false);
        mButtonPending.setPressed(false);
        mButtonMembers.setPressed(true);
        setSearchButtonEnabled(true);
        mGridviewFriends.setAdapter(mMemberListAdapter);
        mGridviewFriends.setVisibility(View.VISIBLE);
        mListviewFriends.setVisibility(View.INVISIBLE);
        mIndicatorLoading.setVisibility(View.INVISIBLE);
        int i = mMemberListAdapter.getChildSize();
        if(i == 0)
            downloadSuggestedFriendList(i, mEdittextSearch.getText().toString());
    }

    private void setPendingButtonPressed()
    {
        mSelectedMenu = mButtonPending;
        mButtonAll.setPressed(false);
        mButtonPending.setPressed(true);
        mButtonMembers.setPressed(false);
        setSearchButtonEnabled(false);
        mEdittextSearch.setText("");
        mMemberListAdapter.clearData();
        mGridviewFriends.setVisibility(View.INVISIBLE);
        mListviewFriends.setVisibility(View.VISIBLE);
        mIndicatorLoading.setVisibility(View.INVISIBLE);
        downloadPendingList();
    }

    private void setSearchButtonEnabled(boolean flag)
    {
        mImagebuttonSearch.setPressed(false);
        mLayoutSearch.setVisibility(View.INVISIBLE);
        if(flag)
        {
            mImagebuttonSearch.setAlpha(255);
            mImagebuttonSearch.getBackground().setAlpha(255);
        } else
        {
            mImagebuttonSearch.setAlpha(80);
            mImagebuttonSearch.getBackground().setAlpha(120);
        }
        mImagebuttonSearch.setClickable(flag);
        mImagebuttonSearch.setFocusable(flag);
    }

    private void showMemberSearchBar()
    {
        mImagebuttonSearch.setPressed(true);
        mLayoutSearch.setVisibility(View.VISIBLE);
    }

    private void startActivityProfile(String s, String s1)
    {
        abortDownloadFriendPhotoImage();
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("countryCode", mCountryCode);
        intent.putExtra("uid", s);
        intent.putExtra("personalUid", mUid);
        intent.putExtra("username", s1);
        startActivity(intent);
    }

    protected void abortAllProcess()
    {
        abortAllDownload();
        super.abortAllProcess();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_account);
        initialize();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(mSideMenu.getIsMenuOpen())
            mSideMenu.slideClose();
        else
            mSideMenu.slideOpen();
        return false;
    }

    protected void onResume()
    {
        super.onResume();
        if(mFriendListAdapter.getChildSize() > 0)
            mFriendListAdapter.notifyDataSetChanged();
        if(mSelectedMenu == null)
            mSelectedMenu = mButtonAll;
        if(mSelectedMenu == mButtonAll)
        {
            setAllButtonPressed();
        } else
        {
            if(mSelectedMenu == mButtonPending)
            {
                setPendingButtonPressed();
                return;
            }
            if(mSelectedMenu == mButtonMembers)
            {
                setMemberButtonPressed();
                return;
            }
        }
    }

    private Button mButtonAll;
    private Button mButtonInbox;
    private Button mButtonInboxNotification;
    private Button mButtonMembers;
    private Button mButtonPending;
    private Button mButtonPrivacy;
    private Button mButtonSearch;
    private String mCountryCode;
    private EditText mEdittextSearch;
    private FacebookPhotoImageService mFacebookPhotoImageService;
    private FriendListAdapter mFriendListAdapter;
    private FriendListService mFriendListService;
    private ArrayList mFriendPhotoImageServices;
    private GridView mGridviewFriends;
    private ImageButton mImagebuttonMenu;
    private ImageButton mImagebuttonPhoto;
    private ImageButton mImagebuttonSearch;
    private ProgressBar mIndicatorLoading;
    private RelativeLayout mLayoutSearch;
    private ExpandableListView mListviewFriends;
    private FriendListAdapter mMemberListAdapter;
    private ArrayList mMemberPhotoImageServices;
    private PendingFriendListService mPendingFriendListService;
    private PendingFriendListAdapter mPendingListAdapter;
    private ArrayList mPendingPhotoImageServices;
    private RequestFriendListService mRequestFriendListService;
    private int mSearchBarHeight;
    private Button mSelectedMenu;
    private SDMapSideMenuLayout mSideMenu;
    private View mSideMenuBlocker;
    private SuggestedFriendListService mSuggestedFriendListService;
    private TextView mTextviewProfileName;
    private String mUid;
    private String mUsername;

















/*
    static int access$2202(AccountActivity accountactivity, int i)
    {
        accountactivity.mSearchBarHeight = i;
        return i;
    }

*/





/*
    static FacebookPhotoImageService access$2602(AccountActivity accountactivity, FacebookPhotoImageService facebookphotoimageservice)
    {
        accountactivity.mFacebookPhotoImageService = facebookphotoimageservice;
        return facebookphotoimageservice;
    }

*/


/*
    static FriendListService access$2702(AccountActivity accountactivity, FriendListService friendlistservice)
    {
        accountactivity.mFriendListService = friendlistservice;
        return friendlistservice;
    }

*/





/*
    static RequestFriendListService access$3002(AccountActivity accountactivity, RequestFriendListService requestfriendlistservice)
    {
        accountactivity.mRequestFriendListService = requestfriendlistservice;
        return requestfriendlistservice;
    }

*/




/*
    static PendingFriendListService access$3302(AccountActivity accountactivity, PendingFriendListService pendingfriendlistservice)
    {
        accountactivity.mPendingFriendListService = pendingfriendlistservice;
        return pendingfriendlistservice;
    }

*/



/*
    static SuggestedFriendListService access$3502(AccountActivity accountactivity, SuggestedFriendListService suggestedfriendlistservice)
    {
        accountactivity.mSuggestedFriendListService = suggestedfriendlistservice;
        return suggestedfriendlistservice;
    }

*/







}
