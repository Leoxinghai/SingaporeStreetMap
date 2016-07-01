// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.tips.reply;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.facebook.model.GraphUser;
import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.facebook.FacebookManagerHandler;
import streetdirectory.mobile.facebook.SDFacebookManager;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.tips.reply.service.PostReplyTipsService;
import streetdirectory.mobile.modules.tips.reply.service.PostReplyTipsServiceInput;
import streetdirectory.mobile.modules.tips.reply.service.PostReplyTipsServiceOutput;
import streetdirectory.mobile.modules.tips.reply.service.TipsReplyService;
import streetdirectory.mobile.modules.tips.reply.service.TipsReplyServiceInput;
import streetdirectory.mobile.modules.tips.reply.service.TipsReplyServiceOutput;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.tips.reply:
//            TipsReplyAdapter

public class TipsReplyActivity extends SDActivity
{

    public TipsReplyActivity()
    {
        mLimit = 2;
        mTotal = 0;
        mImageSize = 0;
        mUID = "";
    }

    private void abortAllDownload()
    {
        abortDownloadTips();
        abortPostReplyTips();
    }

    private void abortDownloadTips()
    {
        if(mTipsReplyService != null)
        {
            mTipsReplyService.abort();
            mTipsReplyService = null;
        }
    }

    private void abortPostReplyTips()
    {
        if(mPostReplyTipsService != null)
        {
            mPostReplyTipsService.abort();
            mPostReplyTipsService = null;
        }
    }

    private void downloadImage(final String imageID, int i, int j)
    {
        for(Iterator iterator = mImageServices.iterator(); iterator.hasNext();)
            if(((SDHttpImageService)iterator.next()).tag.equals(imageID))
                return;

        SDHttpImageService sdhttpimageservice = new SDHttpImageService(imageID, i, j) {

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
                mTipsAdapter.putImage(imageID, bitmap);
                mTipsAdapter.notifyDataSetChanged();
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        sdhttpimageservice.tag = imageID;
        mImageServices.add(sdhttpimageservice);
        sdhttpimageservice.executeAsync();
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

    private void downloadTips(final int start, int j)
    {
        if(mTipsReplyService == null)
        {
            abortDownloadTips();
            final ProgressDialog progressdialog = new ProgressDialog(this);
            if(start == 0)
            {
                progressdialog.setMessage("Loading...");
                progressdialog.setCancelable(true);
                progressdialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

                    public void onCancel(DialogInterface dialoginterface)
                    {
                        if(mTipsReplyService != null)
                            mTipsReplyService.abort();
                    }

                });
                progressdialog.show();
            }
            mTipsReplyService = new TipsReplyService(null) {

                public void onAborted(Exception exception)
                {
                    SDLogger.info("Tips Aborted");
                    mTipsReplyService = null;
                }

                public void onFailed(Exception exception)
                {
                    SDLogger.printStackTrace(exception, "Tips Failed");
                    mTipsReplyService = null;
                    progressdialog.dismiss();
                    mTipsReplyService = null;
                }

                public void onReceiveTotal(long l)
                {
                    mTipsAdapter.total = (int)l;
                    mTotal = (int)l;
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mTipsReplyService = null;
                    if(sdhttpserviceoutput.childs.size() <= 0) {
						SDLogger.debug("failed download tips");
						if(start == 0)
                            progressdialog.dismiss();
					} else {
						mTipsAdapter.removeAllData();
						TipsReplyServiceOutput tipsreplyserviceoutput;
						for(Iterator iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext(); mTipsAdapter.add(tipsreplyserviceoutput))
							tipsreplyserviceoutput = (TipsReplyServiceOutput)iterator.next();

						mTipsAdapter.notifyDataSetChanged();
						SDLogger.debug("success download tips");
						if(start == 0)
                            progressdialog.dismiss();
					}
                    mTipsReplyService = null;
                    return;
                }

            };
            mTipsReplyService.executeAsync();
        }
    }

    private void downloadTipsProfileImage(String s, int i, int j)
    {
        if(mTipsProfileImageService == null)
        {
            SDHttpImageService sdHttpImageService = new SDHttpImageService(s, i, j) {

                public void onAborted(Exception exception)
                {
                    mTipsProfileImageService = null;
                }

                public void onFailed(Exception exception)
                {
                    mTipsProfileImageService = null;
                }

                public void onSuccess(Bitmap bitmap)
                {
                    mTipsProfileImageService = null;
                    mImageViewTipsProfilePicture.setImageBitmap(bitmap);
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((Bitmap)obj);
                }

            };
            mTipsProfileImageService = sdHttpImageService;
            sdHttpImageService.executeAsync();
        }
    }

    private void initData()
    {
        Intent intent = getIntent();
        mCountryCode = intent.getStringExtra("countryCode");
        mTipsCommentID = intent.getStringExtra("commentID");
        mTotal = intent.getIntExtra("totalReplies", 0);
        mTipsUID = intent.getStringExtra("uid");
        mTipsComment = intent.getStringExtra("comment");
        mTipsName = intent.getStringExtra("name");
        mTipsDatetime = intent.getStringExtra("datetime");
        mCompanyID = intent.getIntExtra("companyID", 0);
        mLocationID = intent.getIntExtra("locationID", 0);
        mPlaceID = intent.getIntExtra("placeID", 0);
        mAddressID = intent.getIntExtra("addressID", 0);
        mType = intent.getIntExtra("type", 2);
        mTextViewTipsUsername.setText(mTipsName);
        mTextViewComment.setText(mTipsComment);
        mTextViewDatetime.setText(mTipsDatetime);
        mImageServices = new ArrayList();
        if(mTreeObserver == null && mImageSize == 0 && mTipsUID != null)
        {
            mTreeObserver = mImageViewTipsProfilePicture.getViewTreeObserver();
            mTreeObserver.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                public void onGlobalLayout()
                {
                    mImageViewTipsProfilePicture.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    mTreeObserver = null;
                    mImageSize = mImageViewTipsProfilePicture.getWidth();
                    downloadTipsProfileImage(URLFactory.createURLFacebookPhoto(mTipsUID, mImageSize, mImageSize), mImageSize, mImageSize);
                }

            });
        }
        if(mProfilePictureTreeObserver == null && mUID != null)
        {
            mProfilePictureTreeObserver = mImageViewProfilePicture.getViewTreeObserver();
            mProfilePictureTreeObserver.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                public void onGlobalLayout()
                {
                    mImageViewProfilePicture.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    mProfilePictureTreeObserver = null;
                    int i = mImageViewProfilePicture.getWidth();
                    downloadProfileImage(URLFactory.createURLFacebookPhoto(mUID, i, i), i, i);
                }

            });
        }
    }

    private void initEvent()
    {
        mButtonBack.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mButtonPostReply.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                postReplyTips();
            }

        });
    }

    private void initFacebook(Bundle bundle)
    {
        mFacebookHandler = new FacebookManagerHandler() {

            public void onUserDataDidLoad(GraphUser graphuser)
            {
                if(SDFacebookManager.getInstance().getContext() == TipsReplyActivity.this)
                {
                    mTextViewUsername.setText(graphuser.getName());
                    mUID = graphuser.getId();
                    SDFacebookManager.getInstance().getUserImage();
                    postReplyTips();
                }
            }

            public void onUserDidLogin(int i)
            {
                SDLogger.debug("berhasil login dari tips reply :D");
            }

            public void onUserDidReceiveImage(Bitmap bitmap)
            {
                if(SDFacebookManager.getInstance().getContext() == TipsReplyActivity.this)
                {
                    super.onUserDidReceiveImage(bitmap);
                    mImageViewProfilePicture.setImageBitmap(bitmap);
                }
            }

        };
        SDFacebookManager.getInstance().addCallback(mFacebookHandler);
    }

    private void initLayout()
    {
        mButtonBack = (Button)findViewById(R.id.BackButton);
        mListViewTips = (ListView)findViewById(R.id.listView1);
        mImageViewProfilePicture = (ImageView)findViewById(R.id.image_view_user_photo);
        mTextViewUsername = (TextView)findViewById(R.id.text_view_username);
        mEditTextReplyComment = (EditText)findViewById(R.id.edit_text_comment);
        mButtonPostReply = (Button)findViewById(R.id.button_post_reply);
        initListView();
    }

    private void initListView()
    {
        if(mListViewTips != null)
        {
            View view = getLayoutInflater().inflate(R.layout.cell_tips, null);
            mListViewTips.addHeaderView(view);
            mTipsAdapter = new TipsReplyAdapter(this);
            mTipsAdapter.setOnImageNotFoundListener(new TipsReplyAdapter.OnImageNotFoundListener() {

                public void onImageNotFound(String s, int i, int j, int k)
                {
                    downloadImage(s, j, k);
                }

            });
            mTipsAdapter.setOnLoadMoreListener(new TipsReplyAdapter.OnLoadMoreListener() {

                public void onLoadMoreList()
                {
                    downloadTips(0, mTotal);
                }

            });
            mListViewTips.setAdapter(mTipsAdapter);
            mImageViewTipsProfilePicture = (ImageView)view.findViewById(R.id.UserImage);
            mTextViewTipsUsername = (TextView)view.findViewById(R.id.TitleLabel);
            mTextViewComment = (TextView)view.findViewById(R.id.DetailLabel);
            mTextViewDatetime = (TextView)view.findViewById(R.id.DateLabel);
        }
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void postReplyTips()
    {
        if(mUID != null && !mUID.equals(""))
        {
            if(mPostReplyTipsService == null)
            {
                abortPostReplyTips();
                final ProgressDialog progressdialog = new ProgressDialog(this);
                progressdialog.setMessage("Loading...");
                progressdialog.setCancelable(true);
                progressdialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

                    public void onCancel(DialogInterface dialoginterface)
                    {
                        if(mPostReplyTipsService != null)
                            mPostReplyTipsService.abort();
                    }

                });
                progressdialog.show();
                mPostReplyTipsService = new PostReplyTipsService(null) {

                    public void onAborted(Exception exception)
                    {
                        SDLogger.info("Post Tips Aborted");
                        mPostReplyTipsService = null;
                    }

                    public void onFailed(Exception exception)
                    {
                        Toast.makeText(TipsReplyActivity.this, "Failed to post reply", 1).show();
                        SDLogger.printStackTrace(exception, "Post Tips Failed");
                        mPostReplyTipsService = null;
                        progressdialog.dismiss();
                    }

                    public void onSuccess(Object obj)
                    {
                        onSuccess((SDHttpServiceOutput)obj);
                    }

                    public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                    {
                        int i = 0;
                        mPostReplyTipsService = null;
                        if(sdhttpserviceoutput.childs.size() > 0)
                        {
                            if(((PostReplyTipsServiceOutput)sdhttpserviceoutput.childs.get(0)).result)
                            {
                                Toast.makeText(TipsReplyActivity.this, "Reply tips posted successfully", 1).show();
                                mEditTextReplyComment.setText("");
                            } else
                            {
                                Toast.makeText(TipsReplyActivity.this, "Failed to post reply", 1).show();
                            }
                            progressdialog.dismiss();
                            if((mTotal - mLimit) + 1 > 0)
                                i = (mTotal - mLimit) + 1;
                            downloadTips(i, mLimit);
                        } else
                        {
                            Toast.makeText(TipsReplyActivity.this, "Failed to post reply", 1).show();
                            progressdialog.dismiss();
                        }
                        mPostReplyTipsService = null;
                    }

                };
                mPostReplyTipsService.executeAsync();
            }
            return;
        }
        if(SDFacebookManager.getInstance().userID != null && !SDFacebookManager.getInstance().userID.equals(""))
        {
            mUID = SDFacebookManager.getInstance().userID;
            return;
        }
        if(!SDFacebookManager.getInstance().hasLoginFacebook(this))
        {
            SDFacebookManager.getInstance().login(this, mSavedInstanceState);
            return;
        } else
        {
            SDFacebookManager.getInstance().restoreSession(this);
            return;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_tips_reply);
        initialize();
        int i;
        if(mTotal - mLimit <= 0)
            i = 0;
        else
            i = mTotal - mLimit;
        downloadTips(i, mLimit);
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
        String s = SDFacebookManager.getInstance().getUserID(this);
        if(s != null)
            mUID = s;
    }

    protected void onStop()
    {
        super.onStop();
        SDFacebookManager.getInstance().removeCallback(mFacebookHandler);
    }

    private int mAddressID;
    private Button mButtonBack;
    private Button mButtonPostReply;
    private int mCompanyID;
    private String mCountryCode;
    private EditText mEditTextReplyComment;
    private FacebookManagerHandler mFacebookHandler;
    private ArrayList mImageServices;
    private int mImageSize;
    private ImageView mImageViewProfilePicture;
    private ImageView mImageViewTipsProfilePicture;
    private int mLimit;
    private ListView mListViewTips;
    private int mLocationID;
    private int mPlaceID;
    private PostReplyTipsService mPostReplyTipsService;
    private SDHttpImageService mProfileImageService;
    private ViewTreeObserver mProfilePictureTreeObserver;
    private Bundle mSavedInstanceState;
    private TextView mTextViewComment;
    private TextView mTextViewDatetime;
    private TextView mTextViewTipsUsername;
    private TextView mTextViewUsername;
    private TipsReplyAdapter mTipsAdapter;
    private String mTipsComment;
    private String mTipsCommentID;
    private String mTipsDatetime;
    private String mTipsName;
    private SDHttpImageService mTipsProfileImageService;
    private TipsReplyService mTipsReplyService;
    private String mTipsUID;
    private int mTotal;
    private ViewTreeObserver mTreeObserver;
    private int mType;
    private String mUID;





/*
    static String access$102(TipsReplyActivity tipsreplyactivity, String s)
    {
        tipsreplyactivity.mUID = s;
        return s;
    }

*/




/*
    static int access$1202(TipsReplyActivity tipsreplyactivity, int i)
    {
        tipsreplyactivity.mTotal = i;
        return i;
    }

*/




/*
    static TipsReplyService access$1402(TipsReplyActivity tipsreplyactivity, TipsReplyService tipsreplyservice)
    {
        tipsreplyactivity.mTipsReplyService = tipsreplyservice;
        return tipsreplyservice;
    }

*/




/*
    static SDHttpImageService access$1702(TipsReplyActivity tipsreplyactivity, SDHttpImageService sdhttpimageservice)
    {
        tipsreplyactivity.mTipsProfileImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/


/*
    static SDHttpImageService access$1802(TipsReplyActivity tipsreplyactivity, SDHttpImageService sdhttpimageservice)
    {
        tipsreplyactivity.mProfileImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/



/*
    static PostReplyTipsService access$1902(TipsReplyActivity tipsreplyactivity, PostReplyTipsService postreplytipsservice)
    {
        tipsreplyactivity.mPostReplyTipsService = postreplytipsservice;
        return postreplytipsservice;
    }

*/







/*
    static ViewTreeObserver access$502(TipsReplyActivity tipsreplyactivity, ViewTreeObserver viewtreeobserver)
    {
        tipsreplyactivity.mTreeObserver = viewtreeobserver;
        return viewtreeobserver;
    }

*/



/*
    static int access$602(TipsReplyActivity tipsreplyactivity, int i)
    {
        tipsreplyactivity.mImageSize = i;
        return i;
    }

*/




/*
    static ViewTreeObserver access$902(TipsReplyActivity tipsreplyactivity, ViewTreeObserver viewtreeobserver)
    {
        tipsreplyactivity.mProfilePictureTreeObserver = viewtreeobserver;
        return viewtreeobserver;
    }

*/
}
