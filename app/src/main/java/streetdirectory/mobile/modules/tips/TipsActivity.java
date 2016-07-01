// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.tips;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.facebook.Session;
import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.SDStory;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.tips.reply.TipsReplyActivity;
import streetdirectory.mobile.modules.tips.service.LocationBusinessFBLinkService;
import streetdirectory.mobile.modules.tips.service.LocationBusinessFBLinkServiceInput;
import streetdirectory.mobile.modules.tips.service.LocationBusinessFBLinkServiceOutput;
import streetdirectory.mobile.modules.tips.service.PostTipsService;
import streetdirectory.mobile.modules.tips.service.PostTipsServiceInput;
import streetdirectory.mobile.modules.tips.service.PostTipsServiceOutput;
import streetdirectory.mobile.modules.tips.service.TipsService;
import streetdirectory.mobile.modules.tips.service.TipsServiceInput;
import streetdirectory.mobile.modules.tips.service.TipsServiceOutput;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.tips:
//            TipsAdapter

public class TipsActivity extends SDActivity
{

    public TipsActivity()
    {
        mLimit = 10;
        mUID = "";
        mImageSize = 0;
        mImageURL = "";
        mImageLinkURL = "";
        mVenue = "";
    }

    private void abortAllDownload()
    {
        abortDownloadTips();
        abortPostTips();
    }

    private void abortDownloadTips()
    {
        if(mTipsService != null)
        {
            mTipsService.abort();
            mTipsService = null;
        }
    }

    private void abortPostTips()
    {
        if(mPostTipsService != null)
        {
            mPostTipsService.abort();
            mPostTipsService = null;
        }
    }

    private void downloadBusinessTips(final int start, int j)
    {
        if(mTipsService == null)
        {
            abortDownloadTips();
            mTipsService = new TipsService(null) {

                public void onAborted(Exception exception)
                {
                    SDLogger.info("Tips Aborted");
                    mTipsService = null;
                }

                public void onFailed(Exception exception)
                {
                    SDLogger.printStackTrace(exception, "Tips Failed");
                    mTipsService = null;
                    mTipsService = null;
                }

                public void onReceiveTotal(long l)
                {
                    mTipsAdapter.total = (int)l;
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mTipsService = null;
                    if(sdhttpserviceoutput.childs.size() > 0)
                    {
                        if(start == 0)
                            mTipsAdapter.removeAllData();
                        TipsServiceOutput tipsserviceoutput;
                        for(Iterator iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext(); mTipsAdapter.add(tipsserviceoutput))
                            tipsserviceoutput = (TipsServiceOutput)iterator.next();

                        mTipsAdapter.notifyDataSetChanged();
                        SDLogger.debug("success download tips");
                    } else
                    {
                        SDLogger.debug("failed download tips");
                    }
                    mTipsService = null;
                }

            };
            mTipsService.executeAsync();
        }
    }

    private void downloadImage(final String final_s, int i, int j)
    {
        for(Iterator iterator = mImageServices.iterator(); iterator.hasNext();)
            if(((SDHttpImageService)iterator.next()).tag.equals(final_s))
                return;

        SDHttpImageService sdhttpimageservice = new SDHttpImageService(final_s, i, j) {

            public void onAborted(Exception exception)
            {
                mImageServices.remove(this);
            }

            public void onFailed(Exception exception)
            {
                mImageServices.remove(this);
            }

            public void onSuccess(Bitmap bitmap)
            {
                mImageServices.remove(this);
                mTipsAdapter.putImage(final_s, bitmap);
                mTipsAdapter.notifyDataSetChanged();
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        sdhttpimageservice.tag = final_s;
        mImageServices.add(sdhttpimageservice);
        sdhttpimageservice.executeAsync();
    }

    private void downloadLocationTips(final int start, int j)
    {
        if(mTipsService == null)
        {
            abortDownloadTips();
            mTipsService = new TipsService(null) {

                public void onAborted(Exception exception)
                {
                    SDLogger.info("Tips Aborted");
                    mTipsService = null;
                }

                public void onFailed(Exception exception)
                {
                    SDLogger.printStackTrace(exception, "Tips Failed");
                    mTipsService = null;
                    mTipsService = null;
                }

                public void onReceiveTotal(long l)
                {
                    mTipsAdapter.total = (int)l;
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mTipsService = null;
                    if(sdhttpserviceoutput.childs.size() > 0)
                    {
                        if(start == 0)
                            mTipsAdapter.removeAllData();
                        TipsServiceOutput tipsserviceoutput;
                        for(Iterator iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext(); mTipsAdapter.add(tipsserviceoutput))
                            tipsserviceoutput = (TipsServiceOutput)iterator.next();

                        mTipsAdapter.notifyDataSetChanged();
                        SDLogger.debug("success download tips");
                    } else
                    {
                        SDLogger.debug("failed download tips");
                    }
                    mTipsService = null;
                }

            };
            mTipsService.executeAsync();
        }
    }

    private void downloadProfileImage(String s, int i, int j)
    {
        if(mProfileImageService == null)
        {
            SDHttpImageService sdHttpImageService = new SDHttpImageService(s, i, j) {

                public void onAborted(Exception exception)
                {
                    mProfileImageService = null;
                }

                public void onFailed(Exception exception)
                {
                    mProfileImageService = null;
                }

                public void onSuccess(Bitmap bitmap)
                {
                    mProfileImageService = null;
                    mImageViewProfilePicture.setImageBitmap(bitmap);
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((Bitmap)obj);
                }

            };
            mProfileImageService = sdHttpImageService;
            sdHttpImageService.executeAsync();
        }
    }

    private void initData()
    {
        Intent intent = getIntent();
        mCountryCode = intent.getStringExtra("countryCode");
        mType = intent.getIntExtra("type", 2);
        mPlaceID = intent.getIntExtra("placeID", 0);
        mAddressID = intent.getIntExtra("addressID", 0);
        mCompanyID = intent.getIntExtra("companyID", 0);
        mLocationID = intent.getIntExtra("locationID", 0);
        mImageLinkURL = intent.getStringExtra("img");
        mVenue = intent.getStringExtra("venue");
        SDStory.post(URLFactory.createGantBuildingDirectoryTips(mVenue), SDStory.createDefaultParams());
        mImageServices = new ArrayList();
    }

    private void initEvent()
    {
        mButtonBack.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        }
);
        mButtonSubmit.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                postTips();
            }

        });
    }

    private void initFacebook(Bundle bundle)
    {
    }

    private void initLayout()
    {
        mButtonBack = (Button)findViewById(R.id.BackButton);
        mListViewTips = (ListView)findViewById(R.id.listView1);
        initListView();
    }

    private void initListView()
    {
        if(mListViewTips != null)
        {
            View view = getLayoutInflater().inflate(R.layout.layout_post_tips_header, null);
            mTipsAdapter = new TipsAdapter(this, true);
            mTipsAdapter.setOnImageNotFoundListener(new TipsAdapter.OnImageNotFoundListener() {

                public void onImageNotFound(String s, int i, int j, int k)
                {
                    downloadImage(s, j, k);
                }

            });
            mTipsAdapter.setOnLoadMoreListener(new TipsAdapter.OnLoadMoreListener() {

                public void onLoadMoreList()
                {
                    if(mType == 2)
                    {
                        downloadBusinessTips(mTipsAdapter.getCount() - 1, mLimit);
                        return;
                    } else
                    {
                        downloadLocationTips(mTipsAdapter.getCount() - 1, mLimit);
                        return;
                    }
                }

            });
            mTipsAdapter.setOnReplyClickedListener(new TipsAdapter.OnReplyClickedListener() {

                public void onReplyClickedListener(String s, int i, String s1, String s2, String s3, String s4)
                {
                    Intent intent = new Intent(TipsActivity.this, TipsReplyActivity.class);
                    intent.putExtra("countryCode", mCountryCode);
                    intent.putExtra("commentID", s);
                    intent.putExtra("totalReplies", i);
                    intent.putExtra("uid", s1);
                    intent.putExtra("comment", s2);
                    intent.putExtra("name", s3);
                    intent.putExtra("datetime", s4);
                    intent.putExtra("companyID", mCompanyID);
                    intent.putExtra("locationID", mLocationID);
                    intent.putExtra("placeID", mPlaceID);
                    intent.putExtra("addressID", mAddressID);
                    intent.putExtra("type", mType);
                    startActivity(intent);
                }

            });
            mListViewTips.setAdapter(mTipsAdapter);
            mImageViewProfilePicture = (ImageView)view.findViewById(R.id.image_view_profile);
            if(mTreeObserver == null && mImageSize == 0 && mUID != null)
            {
                mTreeObserver = mImageViewProfilePicture.getViewTreeObserver();
                mTreeObserver.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                    public void onGlobalLayout()
                    {
                        mImageViewProfilePicture.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        mTreeObserver = null;
                        mImageSize = mImageViewProfilePicture.getWidth();
                        downloadProfileImage(URLFactory.createURLFacebookPhoto(mUID, mImageSize, mImageSize), mImageSize, mImageSize);
                    }

                });
            }
            mEditTextComment = (EditText)view.findViewById(R.id.edit_text_comment);
            mButtonAddPhoto = (Button)view.findViewById(R.id.button_add_photo);
            mToogleButtonShare = (ToggleButton)view.findViewById(R.id.toogle_button_share);
            mButtonSubmit = (Button)view.findViewById(R.id.button_submit);
            mTextViewUsername = (TextView)view.findViewById(R.id.text_view_username);
        }
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void postTips()
    {
        if(mUID != null && !mUID.equals("") && mPostTipsService == null)
        {
            abortPostTips();
            final ProgressDialog progressdialog = new ProgressDialog(this);
            progressdialog.setMessage("Loading...");
            progressdialog.setCancelable(true);
            progressdialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

                public void onCancel(DialogInterface dialoginterface)
                {
                    if(mTipsService != null)
                        mTipsService.abort();
                }

            });
            progressdialog.show();
            mPostTipsService = new PostTipsService(null) {

                public void onAborted(Exception exception)
                {
                    SDLogger.info("Post Tips Aborted");
                    mPostTipsService = null;
                }

                public void onFailed(Exception exception)
                {
                    Toast.makeText(TipsActivity.this, "Failed to post tips", 1).show();
                    SDLogger.printStackTrace(exception, "Post Tips Failed");
                    mPostTipsService = null;
                    progressdialog.dismiss();
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mPostTipsService = null;
                    if(sdhttpserviceoutput.childs.size() > 0)
                    {
                        if(((PostTipsServiceOutput)sdhttpserviceoutput.childs.get(0)).result)
                            Toast.makeText(TipsActivity.this, "Tips posted successfully", 1).show();
                        else
                            Toast.makeText(TipsActivity.this, "Failed to post tips", 1).show();
                        progressdialog.dismiss();
                        if(mType == 2)
                            downloadBusinessTips(0, mLimit);
                        else
                            downloadLocationTips(0, mLimit);
                    } else
                    {
                        Toast.makeText(TipsActivity.this, "Failed to post tips", 1).show();
                        progressdialog.dismiss();
                    }
                    mPostTipsService = null;
                }

            };
            mPostTipsService.executeAsync();
        }
    }

    private void postTipsToFacebook()
    {
        if(mFBLinkService == null)
        {
            final ProgressDialog progressdialog = new ProgressDialog(this);
            progressdialog.setMessage("Loading...");
            progressdialog.setCancelable(true);
            progressdialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

                public void onCancel(DialogInterface dialoginterface)
                {
                    if(mFBLinkService != null)
                        mFBLinkService.abort();
                }

            });
            progressdialog.show();
            mFBLinkService = new LocationBusinessFBLinkService(null) {

                public void onAborted(Exception exception)
                {
                    SDLogger.info("Download link Tips Aborted");
                    mFBLinkService = null;
                }

                public void onFailed(Exception exception)
                {
                    mFBLinkService = null;
                    progressdialog.dismiss();
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mFBLinkService = null;
                    if(sdhttpserviceoutput.childs.size() > 0)
                    {
                        LocationBusinessFBLinkServiceOutput locationBusinessFBLinkServiceOutput = (LocationBusinessFBLinkServiceOutput)sdhttpserviceoutput.childs.get(0);
                        progressdialog.dismiss();
                    } else
                    {
                        progressdialog.dismiss();
                    }
                    mFBLinkService = null;
                }

            };
            mFBLinkService.executeAsync();
        }
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
        Session.getActiveSession().onActivityResult(this, i, j, intent);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mSavedInstanceState = bundle;
        setContentView(R.layout.activity_tips);
        initialize();
    }

    protected void onDestroy()
    {
        abortAllDownload();
        super.onDestroy();
    }

    protected void onResume()
    {
        super.onResume();
        initFacebook(mSavedInstanceState);
        if(false)
            mUID = null;
        if(mType == 2)
        {
            downloadBusinessTips(0, mLimit);
            return;
        } else
        {
            downloadLocationTips(0, mLimit);
            return;
        }
    }

    public void onStop()
    {
        super.onStop();
    }

    private int mAddressID;
    private Button mButtonAddPhoto;
    private Button mButtonBack;
    private Button mButtonSubmit;
    private int mCompanyID;
    private String mCountryCode;
    private EditText mEditTextComment;
    private LocationBusinessFBLinkService mFBLinkService;
    private String mImageLinkURL;
    private ArrayList mImageServices;
    private int mImageSize;
    private String mImageURL;
    private ImageView mImageViewProfilePicture;
    private int mLimit;
    private ListView mListViewTips;
    private int mLocationID;
    private int mPlaceID;
    private PostTipsService mPostTipsService;
    private SDHttpImageService mProfileImageService;
    private Bundle mSavedInstanceState;
    private TextView mTextViewUsername;
    private TipsAdapter mTipsAdapter;
    private TipsService mTipsService;
    private ToggleButton mToogleButtonShare;
    private ViewTreeObserver mTreeObserver;
    private int mType;
    private String mUID;
    private String mVenue;







/*
    static ViewTreeObserver access$1302(TipsActivity tipsactivity, ViewTreeObserver viewtreeobserver)
    {
        tipsactivity.mTreeObserver = viewtreeobserver;
        return viewtreeobserver;
    }

*/



/*
    static int access$1402(TipsActivity tipsactivity, int i)
    {
        tipsactivity.mImageSize = i;
        return i;
    }

*/





/*
    static TipsService access$1702(TipsActivity tipsactivity, TipsService tipsservice)
    {
        tipsactivity.mTipsService = tipsservice;
        return tipsservice;
    }

*/



/*
    static SDHttpImageService access$1902(TipsActivity tipsactivity, SDHttpImageService sdhttpimageservice)
    {
        tipsactivity.mProfileImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/



/*
    static PostTipsService access$2002(TipsActivity tipsactivity, PostTipsService posttipsservice)
    {
        tipsactivity.mPostTipsService = posttipsservice;
        return posttipsservice;
    }

*/



/*
    static LocationBusinessFBLinkService access$2102(TipsActivity tipsactivity, LocationBusinessFBLinkService locationbusinessfblinkservice)
    {
        tipsactivity.mFBLinkService = locationbusinessfblinkservice;
        return locationbusinessfblinkservice;
    }

*/







}
