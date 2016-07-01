// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.profile;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.*;
import android.text.style.TextAppearanceSpan;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.facebook.service.FacebookPhotoImageService;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.friend.service.*;
import streetdirectory.mobile.modules.profile.service.PrivacyStatusService;
import streetdirectory.mobile.modules.profile.service.PrivacyStatusServiceInput;
import streetdirectory.mobile.modules.profile.service.PrivacyStatusServiceOutput;
import streetdirectory.mobile.modules.profile.service.UserInfoCountryServiceOutput;
import streetdirectory.mobile.modules.profile.service.UserInfoGeneralServiceOutput;
import streetdirectory.mobile.modules.profile.service.UserInfoService;
import streetdirectory.mobile.modules.profile.service.UserInfoServiceInput;
import streetdirectory.mobile.service.SDDataOutput;
import streetdirectory.mobile.service.SDHttpServiceOutput;

public class ProfileActivity extends SDActivity
{

    public ProfileActivity()
    {
        mShowBusiness = false;
        mShowPlaces = false;
        mShowTips = false;
        mShowRoutes = false;
        mFriendPhotoImageServices = new ArrayList();
    }

    private void abortAllDownload()
    {
        abortDownloadFacebookPhotoImage();
        abortDownloadFriendPhotoImage();
        abortDownloadFriendStatus();
        abortDownloadPrivacyStatus();
        abortDownloadFriendList();
        abortDownloadUserInfo();
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

    private void abortDownloadFriendStatus()
    {
        if(mFriendStatusService != null)
        {
            mFriendStatusService.abort();
            mFriendStatusService = null;
        }
    }

    private void abortDownloadPrivacyStatus()
    {
        if(mPrivacyStatusService != null)
        {
            mPrivacyStatusService.abort();
            mPrivacyStatusService = null;
        }
    }

    private void abortDownloadUserInfo()
    {
        if(mUserInfoService != null)
        {
            mUserInfoService.abort();
            mUserInfoService = null;
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
                onSuccess((Bitmap) obj);
            }

        };
        mFacebookPhotoImageService.executeAsync();
    }

    private void downloadFriendList()
    {
        abortDownloadFriendList();
        mFriendListService = new FriendListService(new FriendListServiceInput(mCountryCode, mUid, "", true, 0, 4)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Friend List Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Friend List Failed");
                mFriendListService = null;
                setContentFriendLocked();
                if(!mShowBusiness && !mShowPlaces && !mShowTips && !mShowRoutes)
                    mIndicatorLoading.setVisibility(View.INVISIBLE);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mFriendListService = null;
                mFriendLists = sdhttpserviceoutput.childs;
                setContentFriend(mFriendLists);
                if(!mShowBusiness && !mShowPlaces && !mShowTips && !mShowRoutes)
                    mIndicatorLoading.setVisibility(View.INVISIBLE);
            }

        };
        mFriendListService.executeAsync();
    }

    private void downloadFriendPhotoImage(final String final_s, int i, int j, ImageButton imagebutton)
    {
        final ImageButton button = imagebutton;
        FacebookPhotoImageService temp = new FacebookPhotoImageService(final_s,i, j) {

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
                button.setImageBitmap(bitmap);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };

        mFriendPhotoImageServices.add(temp);
        temp.executeAsync();
    }

    private void downloadFriendStatus()
    {
        abortDownloadFriendStatus();
        mFriendStatusService = new FriendStatusService(new FriendStatusServiceInput(mCountryCode, mPersonalUid, mUid)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Friend Status Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Friend Status Failed");
                mFriendStatusService = null;
                setContent(null, null);
            }

            public void onReceiveData(FriendStatusServiceOutput friendstatusserviceoutput)
            {
                mFriendStatus = friendstatusserviceoutput;
                setHeaderButton(friendstatusserviceoutput);
            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((FriendStatusServiceOutput) sddataoutput);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput) obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mFriendStatusService = null;
                if(mFriendStatus != null)
                {
                    downloadPrivacyStatus();
                    return;
                } else
                {
                    setContent(null, null);
                    return;
                }
            }

        };
        mFriendStatusService.executeAsync();
    }

    private void downloadPrivacyStatus()
    {
        abortDownloadPrivacyStatus();
        mPrivacyStatusService = new PrivacyStatusService(new PrivacyStatusServiceInput(mCountryCode, mUid)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Privacy Status Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Privacy Status Failed");
                mPrivacyStatusService = null;
                setContent(null, null);
            }

            public void onReceiveData(PrivacyStatusServiceOutput privacystatusserviceoutput)
            {
                setContent(privacystatusserviceoutput, mFriendStatus);
            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((PrivacyStatusServiceOutput) sddataoutput);
            }


            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mPrivacyStatusService = null;
                if(sdhttpserviceoutput.childs.size() <= 0)
                    setContent(null, null);
            }
        };
        mPrivacyStatusService.executeAsync();
    }

    private void downloadUserInfo()
    {
        abortDownloadUserInfo();
        mUserInfoService = new UserInfoService(new UserInfoServiceInput(mCountryCode, mUid)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("User Info Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "User Info Failed");
                mUserInfoService = null;
                mIndicatorLoading.setVisibility(View.INVISIBLE);
            }

            public void onReceiveCountryData(UserInfoCountryServiceOutput userinfocountryserviceoutput)
            {
                setContentInfo(userinfocountryserviceoutput);
            }

            public void onReceiveGeneralData(UserInfoGeneralServiceOutput userinfogeneralserviceoutput)
            {
                mLayoutInfoList.removeAllViews();
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mUserInfoService = null;
                mIndicatorLoading.setVisibility(View.INVISIBLE);
            }

        };
        mUserInfoService.executeAsync();
    }

    private Spannable formatString(String s, String s1)
    {
        SpannableString temp = new SpannableString((new StringBuilder()).append(s).append("\n").append(s1).toString());
        temp.setSpan(new TextAppearanceSpan(this, R.style.ProfileButtonItemBigText), 0, s.length(), 33);
        return temp;
    }

    private void initData()
    {
        Object obj = getIntent();
        mUid = ((Intent) (obj)).getStringExtra("uid");
        mUsername = ((Intent) (obj)).getStringExtra("username");
        mPersonalUid = ((Intent) (obj)).getStringExtra("personalUid");
        mCountryCode = ((Intent) (obj)).getStringExtra("countryCode");
        obj = Html.fromHtml(mUsername);
        mTextviewTitle.setText(((CharSequence) (obj)));
        mTextviewProfileName.setText(((CharSequence) (obj)));
    }

    private void initEvent()
    {
        mButtonBack.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mImagebuttonPhoto.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout()
            {
                mImagebuttonPhoto.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                downloadFacebookPhotoImage(mUid, mImagebuttonPhoto.getWidth(), mImagebuttonPhoto.getHeight());
            }

        });
        mImagebuttonPhoto.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
        mButtonAddFriend.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
        mButtonSendMessage.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
        mButtonConfirm.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
        mButtonReject.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
        mButtonViewAll.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
        mButtonFriendLocked.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                showOKDialog("Private", (new StringBuilder()).append("Sorry, ").append(mUsername).append(" only share some information publicly").toString());
            }

        });
    }

    private void initLayout()
    {
        mButtonBack = (Button)findViewById(R.id.button_back);
        mTextviewTitle = (TextView)findViewById(R.id.textview_title);
        mImagebuttonPhoto = (ImageButton)findViewById(R.id.imagebutton_photo);
        mTextviewProfileName = (TextView)findViewById(R.id.textview_profile_name);
        mButtonAddFriend = (Button)findViewById(R.id.button_add_friend);
        mButtonSendMessage = (Button)findViewById(R.id.button_send_message);
        mImageviewRequestSent = (ImageView)findViewById(R.id.imageview_request_sent);
        mLayoutConfirmReject = (LinearLayout)findViewById(R.id.layout_confirm_reject);
        mButtonConfirm = (Button)findViewById(R.id.button_confirm);
        mButtonReject = (Button)findViewById(R.id.button_reject);
        mLayoutContent = (RelativeLayout)findViewById(R.id.layout_content);
        mTextviewNoInformation = (TextView)findViewById(R.id.textview_no_information);
        mLayoutProfile = (LinearLayout)findViewById(R.id.layout_profile);
        mLayoutFriendList = (LinearLayout)findViewById(R.id.layout_friend_list);
        mButtonViewAll = (ImageButton)findViewById(R.id.button_view_all);
        mButtonFriendLocked = (ImageButton)findViewById(R.id.button_friend_locked);
        mLayoutInfoList = (LinearLayout)findViewById(R.id.layout_info_list);
        mIndicatorLoading = (ProgressBar)findViewById(R.id.indicator_loading);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void onBusinessClicked()
    {
        if(mShowBusiness)
        {
            return;
        } else
        {
            showOKDialog("Private", (new StringBuilder()).append("Sorry, ").append(mUsername).append(" only share some information publicly").toString());
            return;
        }
    }

    private void onPlacesClicked()
    {
        if(mShowPlaces)
        {
            return;
        } else
        {
            showOKDialog("Private", (new StringBuilder()).append("Sorry, ").append(mUsername).append(" only share some information publicly").toString());
            return;
        }
    }

    private void onRoutesClicked()
    {
        if(mShowRoutes)
        {
            return;
        } else
        {
            showOKDialog("Private", (new StringBuilder()).append("Sorry, ").append(mUsername).append(" only share some information publicly").toString());
            return;
        }
    }

    private void onTipsClicked()
    {
        if(mShowTips)
        {
            return;
        } else
        {
            showOKDialog("Private", (new StringBuilder()).append("Sorry, ").append(mUsername).append(" only share some information publicly").toString());
            return;
        }
    }

    private void setContent(PrivacyStatusServiceOutput privacystatusserviceoutput, FriendStatusServiceOutput friendstatusserviceoutput)
    {
        boolean flag;
        boolean flag1;
        boolean flag2;
        flag2 = true;
        flag1 = true;
        flag = flag2;
        if(privacystatusserviceoutput != null && friendstatusserviceoutput != null) {
                mShowBusiness = false;
                mShowPlaces = false;
                mShowTips = false;
                mShowRoutes = false;
                boolean flag3;
                if (friendstatusserviceoutput.friendStatus == 1)
                    flag2 = true;
                else
                    flag2 = false;
                if (mPersonalUid != null)
                    flag3 = true;
                else
                    flag3 = false;

            if (privacystatusserviceoutput.likeBusiness != 1 && (!flag3 || privacystatusserviceoutput.likeBusiness != 2)) {
                flag = flag1;
                if (!flag2 || privacystatusserviceoutput.likeBusiness != 3) {

                } else {
                    flag = false;
                    mShowBusiness = true;
                }
            } else {
                flag = false;
                mShowBusiness = true;
            }

            if (privacystatusserviceoutput.favoritePlace != 1 && (!flag3 || privacystatusserviceoutput.favoritePlace != 2)) {
                flag1 = flag;
                if (flag2 && privacystatusserviceoutput.favoritePlace == 3) {
                    flag1 = false;
                    mShowPlaces = true;
                }
            } else {
                flag1 = false;
                mShowPlaces = true;
            }

            if (privacystatusserviceoutput.shouts != 1 && (!flag3 || privacystatusserviceoutput.shouts != 2)) {
                flag = flag1;
                if (!flag2 || privacystatusserviceoutput.shouts != 3) {
                    flag = false;
                    mShowTips = true;
                }
            } else {
                flag = false;
                mShowTips = true;
            }
            if (privacystatusserviceoutput.favoriteRoute != 1 && (!flag3 || privacystatusserviceoutput.favoriteRoute != 2)) {
                flag1 = flag;
                if (!flag2 || privacystatusserviceoutput.favoriteRoute != 3) {

                } else {
                    flag1 = false;
                    mShowRoutes = true;
                }
            } else {
                flag1 = false;
                mShowRoutes = true;
            }


            if (privacystatusserviceoutput.friends != 1 && (!flag3 || privacystatusserviceoutput.friends != 2) && (!flag2 || privacystatusserviceoutput.friends != 3)) {
                if (!mShowBusiness && !mShowPlaces && !mShowTips && !mShowRoutes) {
                    flag2 = flag1;
                }
                setContentFriendLocked();
                flag2 = flag1;
            } else {
                flag2 = false;
                downloadFriendList();
            }

            if (mShowBusiness || mShowPlaces || mShowTips || mShowRoutes)
                downloadUserInfo();
            flag = flag2;
            if (privacystatusserviceoutput.friends == 4) {
                flag = flag2;
                if (privacystatusserviceoutput.friends == 4) {
                    flag = flag2;
                    if (privacystatusserviceoutput.friends == 4) {
                        flag = flag2;
                        if (privacystatusserviceoutput.friends == 4) {
                            setContentNoInformationNotFriend();
                            flag = false;
                        }
                    }
                }
            }
        }

        if(flag)
            setContentNoInformation();
        return;
    }

    private void setContentFriend(ArrayList arraylist)
    {
        if(arraylist.size() > 0)
        {
            mLayoutFriendList.setVisibility(View.VISIBLE);
            mButtonViewAll.setVisibility(View.VISIBLE);
            mButtonFriendLocked.setVisibility(View.INVISIBLE);
            mLayoutFriendList.removeAllViews();
            LayoutInflater layoutinflater = getLayoutInflater();
            Iterator iterator;
            for(iterator = arraylist.iterator(); iterator.hasNext(); )

            {
                final FriendListServiceOutput item;
                final ImageButton button;
                item = (FriendListServiceOutput)iterator.next();
                button = (ImageButton)layoutinflater.inflate(R.layout.view_profile_item, mLayoutFriendList, false);
                mLayoutFriendList.addView(button);
                button.setOnClickListener(new android.view.View.OnClickListener() {

                    public void onClick(View view) {
                        startActivityProfile(mCountryCode, item.userID, item.userName, mPersonalUid);
                    }
                });

                button.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                    public void onGlobalLayout() {
                        button.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        downloadFriendPhotoImage(item.userID, button.getWidth(), button.getHeight(), button);
                    }

                });
            }

            mLayoutProfile.setVisibility(View.VISIBLE);
            return;
        } else
        {
            setContentFriendLocked();
            return;
        }
    }

    private void setContentFriendLocked()
    {
        mLayoutFriendList.setVisibility(View.INVISIBLE);
        mButtonViewAll.setVisibility(View.INVISIBLE);
        mButtonFriendLocked.setVisibility(View.VISIBLE);
        mLayoutProfile.setVisibility(View.VISIBLE);
    }

    private void setContentInfo(UserInfoCountryServiceOutput userinfocountryserviceoutput)
    {
        View view = getLayoutInflater().inflate(R.layout.layout_profile_activities, mLayoutInfoList, false);
        Object obj = (TextView)view.findViewById(R.id.textview_title_activities);
        Button button = (Button)view.findViewById(R.id.button_business);
        Button button1 = (Button)view.findViewById(R.id.button_places);
        Button button2 = (Button)view.findViewById(R.id.button_tips);
        Button button3 = (Button)view.findViewById(R.id.button_routes);
        ((TextView) (obj)).setText((new StringBuilder()).append(getResources().getString(R.string.profile_activities_title)).append(" ").append(userinfocountryserviceoutput.countryName).toString());
        obj = getResources();
        if(mShowBusiness)
        {
            button.setCompoundDrawables(null, null, null, null);
            button.setText(formatString((new StringBuilder()).append(userinfocountryserviceoutput.totalLikeBusiness).append("").toString(), ((Resources) (obj)).getText(R.string.profile_activities_business).toString()));
        } else
        {
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_like_locked, 0, 0);
            button.setText(((Resources) (obj)).getText(R.string.profile_activities_business));
        }
        button.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view1)
            {
                onBusinessClicked();
            }

        });
        if(mShowPlaces)
        {
            button1.setCompoundDrawables(null, null, null, null);
            button1.setText(formatString((new StringBuilder()).append(userinfocountryserviceoutput.totalFavoritePlace).append("").toString(), ((Resources) (obj)).getText(R.string.profile_activities_places).toString()));
        } else
        {
            button1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_fav_locked, 0, 0);
            button1.setText(((Resources) (obj)).getText(R.string.profile_activities_places));
        }
        if(mShowTips)
        {
            button2.setCompoundDrawables(null, null, null, null);
            button2.setText(formatString((new StringBuilder()).append(userinfocountryserviceoutput.totalTips).append("").toString(), ((Resources) (obj)).getText(R.string.profile_activities_tips).toString()));
        } else
        {
            button2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_tips_locked, 0, 0);
            button2.setText(((Resources) (obj)).getText(R.string.profile_activities_tips));
        }
        if(mShowRoutes)
        {
            button3.setCompoundDrawables(null, null, null, null);
            button3.setText(formatString((new StringBuilder()).append(userinfocountryserviceoutput.totalFavoriteRoute).append("").toString(), ((Resources) (obj)).getText(R.string.profile_activities_routes).toString()));
        } else
        {
            button3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_routes_locked, 0, 0);
            button3.setText(((Resources) (obj)).getText(R.string.profile_activities_routes));
        }
        mLayoutInfoList.addView(view);
    }

    private void setContentNoInformation()
    {
        mLayoutContent.setBackgroundResource(R.drawable.pattern_location_business_header);
        mTextviewNoInformation.setText((new StringBuilder()).append(getResources().getString(R.string.profile_add_friend_text)).append(" ").append(mUsername).toString());
        mTextviewNoInformation.setVisibility(View.VISIBLE);
        mIndicatorLoading.setVisibility(View.INVISIBLE);
    }

    private void setContentNoInformationNotFriend()
    {
        mLayoutContent.setBackgroundResource(R.drawable.pattern_location_business_header);
        mTextviewNoInformation.setText((new StringBuilder()).append(mUsername).append(" does not share information with everyone").toString());
        mTextviewNoInformation.setVisibility(View.VISIBLE);
        mIndicatorLoading.setVisibility(View.INVISIBLE);
    }

    private void setHeaderButton(FriendStatusServiceOutput friendstatusserviceoutput)
    {
        mButtonAddFriend.setVisibility(View.INVISIBLE);
        mButtonSendMessage.setVisibility(View.INVISIBLE);
        mImageviewRequestSent.setVisibility(View.INVISIBLE);
        mLayoutConfirmReject.setVisibility(View.INVISIBLE);
        switch(friendstatusserviceoutput.friendStatus)
        {
        default:
            return;

        case 0: // '\0'
            mButtonAddFriend.setVisibility(View.VISIBLE);
            return;

        case 1: // '\001'
            mButtonSendMessage.setVisibility(View.VISIBLE);
            return;

        case 2: // '\002'
            mImageviewRequestSent.setVisibility(View.VISIBLE);
            return;

        case 3: // '\003'
            mLayoutConfirmReject.setVisibility(View.VISIBLE);
            break;
        }
    }

    private void showOKDialog(String s, String s1)
    {
        (new android.app.AlertDialog.Builder(this)).setTitle(s).setMessage(s1).setPositiveButton("OK", null).show();
    }

    private void startActivityProfile(String s, String s1, String s2, String s3)
    {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("countryCode", s);
        intent.putExtra("uid", s1);
        intent.putExtra("personalUid", s3);
        intent.putExtra("username", s2);
        startActivity(intent);
        finish();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_profile);
        initialize();
        mIndicatorLoading.setVisibility(View.VISIBLE);
        downloadFriendStatus();
    }

    protected void onDestroy()
    {
        abortAllDownload();
        super.onDestroy();
    }

    private Button mButtonAddFriend;
    private Button mButtonBack;
    private Button mButtonConfirm;
    private ImageButton mButtonFriendLocked;
    private Button mButtonReject;
    private Button mButtonSendMessage;
    private ImageButton mButtonViewAll;
    private String mCountryCode;
    private FacebookPhotoImageService mFacebookPhotoImageService;
    private FriendListService mFriendListService;
    private ArrayList mFriendLists;
    private ArrayList mFriendPhotoImageServices;
    private FriendStatusServiceOutput mFriendStatus;
    private FriendStatusService mFriendStatusService;
    private ImageButton mImagebuttonPhoto;
    private ImageView mImageviewRequestSent;
    private ProgressBar mIndicatorLoading;
    private LinearLayout mLayoutConfirmReject;
    private RelativeLayout mLayoutContent;
    private LinearLayout mLayoutFriendList;
    private LinearLayout mLayoutInfoList;
    private LinearLayout mLayoutProfile;
    private String mPersonalUid;
    private PrivacyStatusService mPrivacyStatusService;
    private boolean mShowBusiness;
    private boolean mShowPlaces;
    private boolean mShowRoutes;
    private boolean mShowTips;
    private TextView mTextviewNoInformation;
    private TextView mTextviewProfileName;
    private TextView mTextviewTitle;
    private String mUid;
    private UserInfoService mUserInfoService;
    private String mUsername;




/*
    static FacebookPhotoImageService access$1002(ProfileActivity profileactivity, FacebookPhotoImageService facebookphotoimageservice)
    {
        profileactivity.mFacebookPhotoImageService = facebookphotoimageservice;
        return facebookphotoimageservice;
    }

*/



/*
    static FriendStatusService access$1202(ProfileActivity profileactivity, FriendStatusService friendstatusservice)
    {
        profileactivity.mFriendStatusService = friendstatusservice;
        return friendstatusservice;
    }

*/



/*
    static FriendStatusServiceOutput access$1302(ProfileActivity profileactivity, FriendStatusServiceOutput friendstatusserviceoutput)
    {
        profileactivity.mFriendStatus = friendstatusserviceoutput;
        return friendstatusserviceoutput;
    }

*/





/*
    static PrivacyStatusService access$1702(ProfileActivity profileactivity, PrivacyStatusService privacystatusservice)
    {
        profileactivity.mPrivacyStatusService = privacystatusservice;
        return privacystatusservice;
    }

*/


/*
    static FriendListService access$1802(ProfileActivity profileactivity, FriendListService friendlistservice)
    {
        profileactivity.mFriendListService = friendlistservice;
        return friendlistservice;
    }

*/



/*
    static ArrayList access$1902(ProfileActivity profileactivity, ArrayList arraylist)
    {
        profileactivity.mFriendLists = arraylist;
        return arraylist;
    }

*/










/*
    static UserInfoService access$2702(ProfileActivity profileactivity, UserInfoService userinfoservice)
    {
        profileactivity.mUserInfoService = userinfoservice;
        return userinfoservice;
    }

*/









}
