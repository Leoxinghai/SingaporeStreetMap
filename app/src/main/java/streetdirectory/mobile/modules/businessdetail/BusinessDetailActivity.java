// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessdetail;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.core.ui.ExpandableHeightGridView;
import streetdirectory.mobile.core.ui.UIHelper;
import streetdirectory.mobile.modules.branchlist.BranchListActivity;
import streetdirectory.mobile.modules.businessdetail.service.BusinessBranchService;
import streetdirectory.mobile.modules.businessdetail.service.BusinessBranchServiceInput;
import streetdirectory.mobile.modules.businessdetail.service.BusinessDetailService;
import streetdirectory.mobile.modules.businessdetail.service.BusinessDetailServiceInput;
import streetdirectory.mobile.modules.businessdetail.service.BusinessDetailServiceOutput;
import streetdirectory.mobile.modules.businessdetail.service.DealingWithService;
import streetdirectory.mobile.modules.businessdetail.service.DealingWithServiceInput;
import streetdirectory.mobile.modules.businessdetail.service.DealingWithServiceOutput;
import streetdirectory.mobile.modules.businessdetail.service.SimilarBusinessService;
import streetdirectory.mobile.modules.businessdetail.service.SimilarBusinessServiceInput;
import streetdirectory.mobile.modules.businessdetail.service.SimilarBusinessServiceOutput;
import streetdirectory.mobile.modules.businesslisting.BusinessListingActivity;
import streetdirectory.mobile.modules.businesslisting.offers.OffersListingActivity;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.direction.DirectionActivity;
import streetdirectory.mobile.modules.mappreview.MapPreviewActivity;
import streetdirectory.mobile.modules.photopreview.PhotoPreviewActivity;
import streetdirectory.mobile.modules.photopreview.PhotoThumbnailFragmentAdapter;
import streetdirectory.mobile.modules.photopreview.service.*;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.modules.tips.TipsActivity;
import streetdirectory.mobile.modules.tips.TipsCellViewHolder;
import streetdirectory.mobile.modules.tips.service.*;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.businessdetail:
//            DealingWithAdapter, BusinessOfferWebPageActivity

public class BusinessDetailActivity extends FragmentActivity
{

    public BusinessDetailActivity()
    {
        mDealingWithList = new ArrayList();
        adRequestRetryCount = 0;
    }

    private void abortAllDownload()
    {
        abortDownloadBusinessDetail();
        abortDownloadBusinessSiteBanner();
        abortDownloadSimilarBusiness();
        abortDownloadSimilarBusinessImage();
        abortDownloadBusinessImageList();
        abortDownloadPhotoImage();
        abortDownloadDealingWith();
        abortDownloadOurLocationImage();
        abortDownloadTips();
        abortDownloadFacebookImage();
        abortDownloadBusinessBranch();
    }

    private void abortDownloadBusinessBranch()
    {
        if(mBusinessBranchService != null)
        {
            mBusinessBranchService.abort();
            mBusinessBranchService = null;
        }
    }

    private void abortDownloadBusinessDetail()
    {
        if(mBusinessDetailService != null)
        {
            mBusinessDetailService.abort();
            mBusinessDetailService = null;
        }
    }

    private void abortDownloadBusinessImageList()
    {
        if(mBusinessImageListService != null)
        {
            mBusinessImageListService.abort();
            mBusinessImageListService = null;
        }
    }

    private void abortDownloadBusinessSiteBanner()
    {
        if(mSiteBannerImageService != null)
        {
            mSiteBannerImageService.abort();
            mSiteBannerImageService = null;
        }
    }

    private void abortDownloadDealingWith()
    {
        if(mDealingWithService != null)
        {
            mDealingWithService.abort();
            mDealingWithService = null;
        }
    }

    private void abortDownloadFacebookImage()
    {
        if(mFacebookImageService != null)
        {
            mFacebookImageService.abort();
            mFacebookImageService = null;
        }
    }

    private void abortDownloadOurLocationImage()
    {
        if(mOurLocationImageService != null)
        {
            mOurLocationImageService.abort();
            mOurLocationImageService = null;
        }
    }

    private void abortDownloadPhotoImage()
    {
        if(mPhotoImageService != null)
        {
            mPhotoImageService.abort();
            mPhotoImageService = null;
        }
    }

    private void abortDownloadSimilarBusiness()
    {
        if(mSimilarBusinessService != null)
        {
            mSimilarBusinessService.abort();
            mSimilarBusinessService = null;
        }
    }

    private void abortDownloadSimilarBusinessImage()
    {
        if(mSimilarBusinessImageService != null)
        {
            mSimilarBusinessImageService.abort();
            mSimilarBusinessImageService = null;
        }
    }

    private void abortDownloadTips()
    {
        if(mTipsService != null)
        {
            mTipsService.abort();
            mTipsService = null;
        }
    }

    private void downloadBusinessBranch()
    {
        abortDownloadBusinessBranch();
        mBusinessBranchService = new BusinessBranchService(new BusinessBranchServiceInput(mData.countryCode, mData.companyID, 0, 0, mData.promoId)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Business Branch Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Business Branch Failed");
                mBusinessBranchService = null;
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mBusinessBranchService = null;
                int i = sdhttpserviceoutput.childs.size();
                if(i > 1)
                {
                    mButtonBranch.setText((new StringBuilder()).append("See all ").append(i).append(" branches").toString());
                    mLayoutBranches.setVisibility(View.VISIBLE);
                }
            }
        };
        mBusinessBranchService.executeAsync();
    }

    private void downloadBusinessDetail()
    {
        abortDownloadBusinessDetail();
        mBusinessDetailService = new BusinessDetailService(new BusinessDetailServiceInput(mData.countryCode, SDStoryStats.SDuId, mData.companyID, mData.locationID, mData.promoId)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Business Detail Aborted");
                mProgressIndicatorBusiness.setVisibility(View.INVISIBLE);
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Business Detail Failed");
                mBusinessDetailService = null;
                mProgressIndicatorBusiness.setVisibility(View.INVISIBLE);
                populateVisibleView();
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mBusinessDetailService = null;
                mProgressIndicatorBusiness.setVisibility(View.INVISIBLE);
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    java.util.Map.Entry entry;
                    Iterator iterator;
                    for(iterator = ((BusinessDetailServiceOutput)sdhttpserviceoutput.childs.get(0)).hashData.entrySet().iterator(); iterator.hasNext(); mData.hashData.put(entry.getKey(), entry.getValue()))
                        entry = (java.util.Map.Entry)iterator.next();

                    mData.populateData();
                    populateVisibleView();
                }
            }

        };
        mProgressIndicatorBusiness.setVisibility(View.VISIBLE);
        mBusinessDetailService.executeAsync();
    }

    private void downloadBusinessImageList()
    {
        abortDownloadBusinessImageList();
        mBusinessImageListService = new BusinessImageListService(new BusinessImageListServiceInput(mData.countryCode, mData.companyID, mData.locationID)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Business Image List Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Business Image List Failed");
                mBusinessImageListService = null;
                mLayoutPhoto.setVisibility(View.VISIBLE);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                int i = 0;
                int j;

                mBusinessImageListService = null;
                if(sdhttpserviceoutput.childs.size() <= 0) {
                    mLayoutPhoto.setVisibility(View.INVISIBLE);
                    return;
                }
                j = mButtonBusinessPhoto.getWidth();
                int k = mButtonBusinessPhoto.getHeight();
                if(j != 0)
                {
                    i = k;
                }
                if( j == 0 || k == 0) {
                    j = mButtonBusinessPhoto.getLayoutParams().width;
                    i = mButtonBusinessPhoto.getLayoutParams().height;
                }

                downloadPhotoImage((ImageListServiceOutput)sdhttpserviceoutput.childs.get(0), j, i);
                mPhotoAdapter.setData(sdhttpserviceoutput.childs);
                mPhotoAdapter.notifyDataSetChanged();
                mLayoutPhoto.setVisibility(View.VISIBLE);
                return;
            }

        };
        mBusinessImageListService.executeAsync();
    }

    private void downloadBusinessSiteBanner()
    {
        abortDownloadBusinessSiteBanner();
        int i = mImageviewSiteBanner.getLayoutParams().width;
        int j = mImageviewSiteBanner.getLayoutParams().height;
        mSiteBannerImageService = new SDHttpImageService(mData.generateSiteBannerURL(i, j)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Business Site Banner Aborted");
                mProgressIndicatorSiteBanner.setVisibility(View.INVISIBLE);
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Business Site Banner Failed");
                mSiteBannerImageService = null;
                mProgressIndicatorSiteBanner.setVisibility(View.INVISIBLE);
            }

            public void onSuccess(Bitmap bitmap)
            {
                mSiteBannerImageService = null;
                mProgressIndicatorSiteBanner.setVisibility(View.INVISIBLE);
                mImageviewSiteBanner.setImageBitmap(bitmap);
            }

        };
        mProgressIndicatorSiteBanner.setVisibility(View.VISIBLE);
        mSiteBannerImageService.executeAsync();
    }

    private void downloadDealingWith()
    {
        abortDownloadDealingWith();
        mDealingWithService = new DealingWithService(new DealingWithServiceInput(mData.countryCode, mData.companyID, mData.locationID)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Dealing With Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Dealing With Failed");
                mDealingWithService = null;
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mDealingWithService = null;
                mDealingWithList = sdhttpserviceoutput.childs;
                int i = mDealingWithList.size();
                if(i > 0)
                {
                    if(i > 4 && !getResources().getBoolean(R.bool.isTablet))
                    {
                        mButtonShowMoreDealing.setVisibility(View.VISIBLE);
                        mDealingWithAdapter.add(mDealingWithList.subList(0, 4));
                    } else
                    {
                        mButtonShowMoreDealing.setVisibility(View.INVISIBLE);
                        mDealingWithAdapter.add(mDealingWithList);
                    }
                    mLayoutDealingWith.setVisibility(View.VISIBLE);
                }
            }

        };
        mDealingWithService.executeAsync();
    }

    private void downloadFacebookImage(String s, int i, int j)
    {
        abortDownloadFacebookImage();
        mFacebookImageService = new SDFacebookImageService(s, i, j) {

            public void onAborted(Exception exception)
            {
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Facebook Image Failed");
                mFacebookImageService = null;
            }

            public void onSuccess(Bitmap bitmap)
            {
                mFacebookImageService = null;
                mTipsCellViewHolder.photoView.setImageBitmap(bitmap);
            }

        };
        mFacebookImageService.executeAsync();
    }

    private void downloadOurLocationImage(String s)
    {
        abortDownloadOurLocationImage();
        mOurLocationImageService = new SDHttpImageService(s) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Our Location Image Aborted");
                mProgressIndicatorOurLocation.setVisibility(View.INVISIBLE);
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Our Location Image Failed");
                mImagebuttonOurLocation.setImageResource(R.drawable.ic_map_no_location);
                mOurLocationImageService = null;
                mProgressIndicatorOurLocation.setVisibility(View.INVISIBLE);
            }

            public void onSuccess(Bitmap bitmap)
            {
                mImagebuttonOurLocation.setImageBitmap(bitmap);
                mOurLocationImageService = null;
                mProgressIndicatorOurLocation.setVisibility(View.INVISIBLE);
                mButtonOurLocationDirection.setVisibility(View.VISIBLE);
            }

        };
        mProgressIndicatorOurLocation.setVisibility(View.VISIBLE);
        mOurLocationImageService.executeAsync();
    }

    private void downloadPhotoImage(ImageListServiceOutput imagelistserviceoutput, int i, int j)
    {
        abortDownloadPhotoImage();
        mPhotoImageService = new SDHttpImageService(imagelistserviceoutput.generateImageURL(i, j, 1)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Photo Image Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Photo Image Failed");
                mPhotoImageService = null;
            }

            public void onSuccess(Bitmap bitmap)
            {
                mPhotoImageService = null;
                mButtonBusinessPhoto.setImageBitmap(bitmap);
            }

        };
        mPhotoImageService.executeAsync();
    }

    private void downloadSimilarBusiness()
    {
        abortDownloadSimilarBusiness();
        mSimilarBusinessService = new SimilarBusinessService(new SimilarBusinessServiceInput(mData.countryCode, mData.stateID, mData.companyID, 1)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Similar Business Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Similar Business Failed");
                mSimilarBusinessService = null;
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput) obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mSimilarBusinessService = null;
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    mSimilarBusiness = (SimilarBusinessServiceOutput)sdhttpserviceoutput.childs.get(0);
                    int i;
                    int j;
                    if(mSimilarBusiness.adsLabel != null && mSimilarBusiness.adsLabel.length() > 0)
                    {
                        mTextviewSimilarBusinessTitle.setText(getBoldStringFromAdMessage(mSimilarBusiness.adsLabel));
                        mTextviewSimilarBusinessDetail.setText(mSimilarBusiness.venue);
                    } else
                    {
                        mTextviewSimilarBusinessTitle.setText(mSimilarBusiness.venue);
                    }
                    if(mSimilarBusiness.phoneNumber != null && mSimilarBusiness.phoneNumber.length() > 0)
                        mImageButtonSimilarBusinessCall.setVisibility(View.VISIBLE);
                    else
                        mImageButtonSimilarBusinessCall.setVisibility(View.INVISIBLE);
                    mLayoutSimilarBusiness.setVisibility(View.VISIBLE);
                    i = mImageButtonSimilarBusinessPhoto.getLayoutParams().width;
                    j = mImageButtonSimilarBusinessPhoto.getLayoutParams().height;
                    downloadSimilarBusinessImage(mSimilarBusiness.generateSiteBannerURL(i, j));
                }
            }

        };

        mSimilarBusinessService.executeAsync();
    }

    private void downloadSimilarBusinessImage(String s)
    {
        abortDownloadSimilarBusinessImage();
        mSimilarBusinessImageService = new SDHttpImageService(s) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Similar Business Image Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Similar Business Image Failed");
                mSimilarBusinessImageService = null;
            }

            public void onSuccess(Bitmap bitmap)
            {
                mSimilarBusinessImageService = null;
                mImageButtonSimilarBusinessPhoto.setImageBitmap(bitmap);
            }

        };
        mSimilarBusinessImageService.executeAsync();
    }

    private void downloadTips()
    {
        abortDownloadTips();
        mTipsService = new TipsService(new TipsServiceInput(mData.countryCode, 2, (new StringBuilder()).append(mData.companyID).append("_").append(mData.locationID).toString(), 0, 1)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Tips Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Tips Failed");
                mTipsService = null;
            }

            public void onReceiveTotal(long l)
            {
                super.onReceiveTotal(l);
                mButtonMoreReviews.setText((new StringBuilder()).append("View all ").append(l).append(" reviews").toString());
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
                    mReviews = (TipsServiceOutput)sdhttpserviceoutput.childs.get(0);
                    mTipsCellViewHolder.titleLabel.setText(Html.fromHtml(mReviews.name));
                    mTipsCellViewHolder.detailLabel.setText(Html.fromHtml(mReviews.comment));
                    mTipsCellViewHolder.dateLabel.setText(mReviews.dateTime);
                    int i = mTipsCellViewHolder.photoView.getLayoutParams().width;
                    downloadFacebookImage(mReviews.generateUserPhotoURL(i, i), i, i);
                    mLayoutNoReviews.setVisibility(View.INVISIBLE);
                    mLayoutReviews.setVisibility(View.VISIBLE);
                    mButtonTips.setVisibility(View.VISIBLE);
                    return;
                } else
                {
                    mLayoutNoReviews.setVisibility(View.INVISIBLE);
                    mLayoutReviews.setVisibility(View.INVISIBLE);
                    mButtonTips.setVisibility(View.INVISIBLE);
                    return;
                }
            }

        };
        mTipsService.executeAsync();
    }

    private String getBoldStringFromAdMessage(String s)
    {
        Matcher matcher = Pattern.compile("^(<(b|B)>).*</(b|B)>").matcher(s);
        if(matcher.find())
        {
            s = matcher.group(0);
            s = s.substring(3, s.length() - 4);
        }
        return s;
    }

    private void initData()
    {
        LocationBusinessServiceOutput locationbusinessserviceoutput = (LocationBusinessServiceOutput)getIntent().getParcelableExtra("data");
        if(locationbusinessserviceoutput != null)
        {
            mData = new BusinessDetailServiceOutput(locationbusinessserviceoutput.hashData);
            if(mData.isPremium)
                SDStory.post(URLFactory.createGantBusinessCategoryPremium((new StringBuilder()).append(mData.companyID).append("").toString(), mData.venue), SDStory.createDefaultParams());
            else
                SDStory.post(URLFactory.createGantBusinessCategoryFree((new StringBuilder()).append(mData.companyID).append("").toString(), mData.venue), SDStory.createDefaultParams());
            mTextviewTitle.setText(mData.venue);
            mTextviewDetail.setText(mData.address);
            mTextviewPhone.setText(mData.phoneNumber);
            if(mData.longitude == 0.0D && mData.latitude == 0.0D)
                mButtonMap.setEnabled(false);
        }
    }

    private void initEvent()
    {
        mTextViewSeeOffer.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mData != null)
                {
                    Intent intent = new Intent(BusinessDetailActivity.this, BusinessOfferWebPageActivity.class);
                    intent.putExtra("offer_url", mData.offer);
                    intent.putExtra("offer_title", mData.venue);
                    intent.putExtra("data", (Parcelable)mData);
                    startActivity(intent);
                }
            }

        });
        mButtonOurLocationDirection.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(BusinessDetailActivity.this, DirectionActivity.class);
                intent.putExtra("endData", (Parcelable)mData);
                intent.putExtra("menu_visible", false);
                startActivity(intent);
            }
        });
        mLayoutSimilarBusiness.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mSimilarBusiness != null)
                {
                    Intent intent = new Intent(BusinessDetailActivity.this, BusinessDetailActivity.class);
                    intent.putExtra("data",(Parcelable) mSimilarBusiness);
                    startActivity(intent);
                }
            }
        });

        mImageButtonSimilarBusinessCall.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mSimilarBusiness == null || mSimilarBusiness.phoneNumber == null || mSimilarBusiness.phoneNumber.length() <= 0) {
                    //Log.e("", "Call failed", view);
                    return;
                }
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setData(Uri.parse((new StringBuilder()).append("tel:").append(mSimilarBusiness.phoneNumber).toString()));
                HashMap hashmap = SDStory.createDefaultParams();
                hashmap.put("c_id", Integer.toString(mSimilarBusiness.companyID));
                hashmap.put("c_pid", mSimilarBusiness.phoneNumber);
                hashmap.put("opg", "similar_business");
                SDStory.post(URLFactory.createGantBusinessCall((new StringBuilder()).append(mSimilarBusiness.companyID).append("").toString()), hashmap);
                startActivity(intent);
                return;
            }

        });

        mImageviewSiteBanner.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mData != null && mData.offer != null && mData.offer.length() > 0)
                {
                    Intent intent = new Intent(BusinessDetailActivity.this, BusinessOfferWebPageActivity.class);
                    intent.putExtra("offer_url", mData.offer);
                    intent.putExtra("offer_title", mData.venue);
                    intent.putExtra("data", (Parcelable)mData);
                    startActivity(intent);
                }
            }

        });
        mButtonMoreReviews.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                startActivityTips();
            }
        });

        mBackButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mShareButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });

        mButtonDirection.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(BusinessDetailActivity.this, DirectionActivity.class);
                intent.putExtra("endData", (Parcelable)mData);
                intent.putExtra("menu_visible", false);
                startActivity(intent);
            }

        });
        mButtonTips.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                startActivityTips();
            }

        });

        mButtonMap.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                startActivityMapPreview();
            }
        });

        mImagebuttonOurLocation.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                startActivityMapPreview();
            }

        });

        mWebviewAdLabel.setWebViewClient(new WebViewClient() {

                 public void onPageFinished(WebView webview, String s) {
                     mLayoutAdLabel.setVisibility(View.VISIBLE);
                 }

                 public boolean shouldOverrideUrlLoading(WebView webview, String s) {
                     if (!s.startsWith("mailto:")) {
                         Intent intent = new Intent(BusinessDetailActivity.this,BusinessOfferWebPageActivity.class);
                         intent.putExtra("offer_url", s);
                         intent.putExtra("offer_title", mData.venue);
                         intent.putExtra("data", (Parcelable)mData);
                         startActivity(intent);
                     }
                     return true;
                 }

             }
        );
        mWebviewAdOperatingHours.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView webview, String s)
            {
                mLayoutAdOperatingHours.setVisibility(View.VISIBLE);
            }

        });

        mWebviewAdPromotion.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView webview, String s)
            {
                mLayoutAdPromotion.setVisibility(View.VISIBLE);
            }
        });

        mWebviewAboutUs.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView webview, String s)
            {
                mLayoutAboutUs.setVisibility(View.VISIBLE);
            }
        });

        mPhotoAdapter.setOnImageClickedListener(new streetdirectory.mobile.modules.photopreview.PhotoThumbnailFragmentAdapter.OnImageClickedListener() {

            public void onAddImageClicked(int i)
            {
                startActivityPhotoPreview(i);
            }

            public void onImageClicked(ImageListServiceOutput imagelistserviceoutput, int i)
            {
                startActivityPhotoPreview(i);
            }

        });

        mGridDealingWith.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                return motionevent.getAction() == 2;
            }

        });

        mButtonShowMoreDealing.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mDealingWithAdapter.clear();
                mDealingWithAdapter.add(mDealingWithList);
                mDealingWithAdapter.notifyDataSetChanged();
                mButtonShowMoreDealing.setVisibility(View.INVISIBLE);
            }

        });

        mDealingWithAdapter.setOnDealingWithClickedListener(new DealingWithAdapter.OnDealingWithClickedListener() {

            public void onClicked(DealingWithServiceOutput dealingwithserviceoutput, int i)
            {
                startActivityDealingWith(dealingwithserviceoutput);
            }

        });

        mButtonBranch.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(BusinessDetailActivity.this, BranchListActivity.class);
                intent.putExtra("countryCode", mData.countryCode);
                intent.putExtra("companyID", mData.companyID);
                startActivity(intent);
            }

        });
        mButtonBusinessPhoto.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                startActivityPhotoPreview(0);
            }

        });
        mButtonMoreReviews.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                startActivityTips();
            }
        });
        mLayoutReviews.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                startActivityTips();
            }
        });

        mLayoutPhone.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                try
                {
                    Intent intent = new Intent("android.intent.action.DIAL");
                    String s = mTextviewPhone.getText().toString();
                    intent.setData(Uri.parse((new StringBuilder()).append("tel:").append(s).toString()));
                    HashMap hashmap = SDStory.createDefaultParams();
                    hashmap.put("c_id", Integer.toString(mData.companyID));
                    hashmap.put("c_pid", s);
                    hashmap.put("opg", "business_detail");
                    SDStory.post(URLFactory.createGantBusinessCall((new StringBuilder()).append(mData.companyID).append("").toString()), hashmap);
                    startActivity(intent);
                    return;
                }
                // Misplaced declaration of an exception variable
                catch(Exception  ex)
                {
                    Log.e("", "Call failed", ex);
                }
            }
        });
    }

    private void initLayout()
    {
        mBackButton = (Button)findViewById(R.id.BackButton);
        mShareButton = (Button)findViewById(R.id.ShareButton);
        mButtonBusinessPhoto = (ImageButton)findViewById(R.id.button_business_photo);
        mTextviewTitle = (TextView)findViewById(R.id.textview_title);
        mTextviewDetail = (TextView)findViewById(R.id.textview_detail);
        mButtonDirection = (Button)findViewById(R.id.button_direction);
        mButtonMap = (Button)findViewById(R.id.button_map);
        mButtonTips = (Button)findViewById(R.id.button_tips);
        mTextviewPhone = (TextView)findViewById(R.id.textview_phone);
        mLayoutSimilarBusiness = findViewById(R.id.layout_similar_business);
        mImageButtonSimilarBusinessPhoto = (ImageButton)mLayoutSimilarBusiness.findViewById(R.id.BusinessImageButton);
        mTextviewSimilarBusinessTitle = (TextView)mLayoutSimilarBusiness.findViewById(R.id.TitleLabel);
        mTextviewSimilarBusinessDetail = (TextView)mLayoutSimilarBusiness.findViewById(R.id.DetailLabel);
        mImageButtonSimilarBusinessCall = (ImageButton)mLayoutSimilarBusiness.findViewById(R.id.imageButtonCall);
        mLayoutSiteBanner = (RelativeLayout)findViewById(R.id.layout_site_banner);
        mImageviewSiteBanner = (ImageView)findViewById(R.id.imageview_site_banner);
        mProgressIndicatorSiteBanner = (ProgressBar)findViewById(R.id.progress_indicator_site_banner);
        mLayoutPhoto = (LinearLayout)findViewById(R.id.layout_photo);
        mViewpagerPhoto = (ViewPager)findViewById(R.id.viewpager_photo);
        mPhotoAdapter = new PhotoThumbnailFragmentAdapter(getSupportFragmentManager(), this);
        mViewpagerPhoto.setAdapter(mPhotoAdapter);
        mLayoutAboutUs = (LinearLayout)findViewById(R.id.layout_about_us);
        mWebviewAboutUs = (WebView)findViewById(R.id.webView_about_us);
        mLayoutAdLabel = (LinearLayout)findViewById(R.id.layout_ad_label);
        mWebviewAdLabel = (WebView)findViewById(R.id.webView_ad_label);
        mTextViewOfferTitle = (TextView)findViewById(R.id.textview_title_offers);
        mLayoutAdOperatingHours = (LinearLayout)findViewById(R.id.layout_ad_operating_hours);
        mWebviewAdOperatingHours = (WebView)findViewById(R.id.webView_ad_operating_hours);
        mLayoutAdPromotion = (LinearLayout)findViewById(R.id.layout_ad_promotion);
        mWebviewAdPromotion = (WebView)findViewById(R.id.webView_ad_promotion);
        mLayoutDealingWith = (LinearLayout)findViewById(R.id.layout_dealing_with);
        mGridDealingWith = (ExpandableHeightGridView)findViewById(R.id.grid_dealing_with);
        mGridDealingWith.setExpanded(true);
        mDealingWithAdapter = new DealingWithAdapter(this);
        mGridDealingWith.setAdapter(mDealingWithAdapter);
        mButtonShowMoreDealing = (Button)findViewById(R.id.button_show_more_dealing);
        mLayoutOurLocation = (LinearLayout)findViewById(R.id.layout_our_location);
        mImagebuttonOurLocation = (ImageView)findViewById(R.id.imagebutton_our_location);
        mProgressIndicatorOurLocation = (ProgressBar)findViewById(R.id.progress_indicator_our_location);
        mLayoutReviews = (LinearLayout)findViewById(R.id.layout_reviews);
        mLayoutTipsCell = findViewById(R.id.layout_tips_cell);
        mTipsCellViewHolder = new TipsCellViewHolder();
        mTipsCellViewHolder.photoView = (ImageView)mLayoutTipsCell.findViewById(R.id.UserImage);
        mTipsCellViewHolder.titleLabel = (TextView)mLayoutTipsCell.findViewById(R.id.TitleLabel);
        mTipsCellViewHolder.detailLabel = (TextView)mLayoutTipsCell.findViewById(R.id.DetailLabel);
        mTipsCellViewHolder.dateLabel = (TextView)mLayoutTipsCell.findViewById(R.id.DateLabel);
        mButtonMoreReviews = (Button)findViewById(R.id.button_more_reviews);
        mLayoutNoReviews = (LinearLayout)findViewById(R.id.layout_no_reviews);
        mLayoutBranches = (LinearLayout)findViewById(R.id.layout_branches);
        mButtonBranch = (Button)findViewById(R.id.button_branch);
        mProgressIndicatorBusiness = (ProgressBar)findViewById(R.id.progress_indicator_business);
        mLayoutPhone = (LinearLayout)findViewById(R.id.layout_button_phone);
        mTextViewSeeOffer = (TextView)findViewById(R.id.textViewSeeOffer);
        mButtonOurLocationDirection = (Button)findViewById(R.id.buttonDirections);
        mSdSmallBanner = (RelativeLayout)findViewById(R.id.view_sdmob);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void loadSmallBanner()
    {
        Object obj = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_main_map));
//        ((SmallBanner) (obj)).setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener() );
        obj = ((SmallBanner) (obj)).getView(this);
        mSdSmallBanner.removeAllViews();
        mSdSmallBanner.addView(((View) (obj)), new RelativeLayout.LayoutParams(-1, -1));
    }

    private void populateVisibleView()
    {
        mTextviewTitle.setText(mData.venue);
        mTextviewDetail.setText(mData.address);
        mTextviewPhone.setText(mData.phoneNumber);
        if(mData.longitude != 0.0D && mData.latitude != 0.0D)
            mButtonMap.setEnabled(true);
        if(mData.siteBanner != null)
        {
            mLayoutSiteBanner.setVisibility(View.VISIBLE);
            downloadBusinessSiteBanner();
        }
        downloadBusinessImageList();
        Object obj = null;
        String s = null;
        if(mData.adsLabel != null)
        {
            s = null;
            if(mData.adsLabel.length() > 0)
                s = (new StringBuilder()).append("<font face=\"HelveticaNeue\" size=\"2\">").append(mData.adsLabel).append("</font>").toString();
        }
        if(s != null && s.length() > 0)
            mWebviewAdLabel.loadData(s, "text/html", null);
        if(mData.adOperatingHours != null && mData.adOperatingHours.length() > 0)
            mWebviewAdOperatingHours.loadData(mData.adOperatingHours, "text/html", null);
        if(mData.adPromotion != null && mData.adPromotion.length() > 0)
            mWebviewAdPromotion.loadData(mData.adPromotion, "text/html", null);
        mWebviewAboutUs.loadUrl(URLFactory.createURLBusinessAboutUs(mData.countryCode, mData.companyID, SDPreferences.getInstance().getAPIVersion()));
        downloadDealingWith();
        mLayoutOurLocation.setVisibility(View.VISIBLE);
        if(mData.longitude != 0.0D && mData.latitude != 0.0D)
        {
            android.view.ViewGroup.LayoutParams layoutparams = mImagebuttonOurLocation.getLayoutParams();
            int i = (int)((float)UIHelper.getScreenDimension().widthPixels - UIHelper.toPixel(16F));
            int j = layoutparams.height;
            downloadOurLocationImage(URLFactory.createURLMapImage(mData.longitude, mData.latitude, i, j, 13));
        } else
        {
            mImagebuttonOurLocation.setImageResource(R.drawable.ic_map_no_location);
        }
        if(mData.offer != null && mData.offer.length() > 0)
            mTextViewSeeOffer.setVisibility(View.VISIBLE);
        downloadTips();
        downloadBusinessBranch();
    }

    private void startActivityDealingWith(DealingWithServiceOutput dealingwithserviceoutput)
    {
        Intent intent = new Intent(this, BusinessListingActivity.class);
        if(dealingwithserviceoutput.categoryID == 11342)
            intent = new Intent(this, OffersListingActivity.class);
        intent.putExtra("countryCode", dealingwithserviceoutput.countryCode);
        intent.putExtra("categoryID", dealingwithserviceoutput.categoryID);
        intent.putExtra("categoryName", dealingwithserviceoutput.name);
        intent.putExtra("type", 2);
        intent.putExtra("longitude", SDBlackboard.currentLongitude);
        intent.putExtra("latitude", SDBlackboard.currentLatitude);
        startActivity(intent);
    }

    private void startActivityMapPreview()
    {
        Intent intent = new Intent(this, MapPreviewActivity.class);
        intent.putExtra("longitude", mData.longitude);
        intent.putExtra("latitude", mData.latitude);
        intent.putExtra("pinLongitude", mData.longitude);
        intent.putExtra("pinLatitude", mData.latitude);
        intent.putExtra("pinTitle", mData.venue);
        startActivity(intent);
    }

    private void startActivityPhotoPreview(int i)
    {
        Intent intent = new Intent(this, PhotoPreviewActivity.class);
        intent.putExtra("companyID", mData.companyID);
        intent.putExtra("locationID", mData.locationID);
        intent.putExtra("type", 2);
        intent.putExtra("countryCode", mData.countryCode);
        intent.putExtra("position", i);
        startActivity(intent);
    }

    private void startActivityTips()
    {
        Intent intent = new Intent(this, TipsActivity.class);
        intent.putExtra("countryCode", mData.countryCode);
        intent.putExtra("companyID", mData.companyID);
        intent.putExtra("locationID", mData.locationID);
        intent.putExtra("img", mData.imageURL);
        intent.putExtra("venue", mData.venue);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    protected void onCreate(Bundle bundle)
    {
        onCreate(bundle);
        setContentView(R.layout.activity_business_detail);
        initialize();
        if(mData != null)
            downloadBusinessDetail();
        loadSmallBanner();
    }

    protected void onDestroy()
    {
        abortAllDownload();
        onDestroy();
    }

    private int adRequestRetryCount;
    private Button mBackButton;
    private BusinessBranchService mBusinessBranchService;
    private BusinessDetailService mBusinessDetailService;
    private BusinessImageListService mBusinessImageListService;
    private Button mButtonBranch;
    private ImageButton mButtonBusinessPhoto;
    private Button mButtonDirection;
    private Button mButtonMap;
    private Button mButtonMoreReviews;
    private Button mButtonOurLocationDirection;
    private Button mButtonShowMoreDealing;
    private Button mButtonTips;
    private BusinessDetailServiceOutput mData;
    private DealingWithAdapter mDealingWithAdapter;
    private ArrayList mDealingWithList;
    private DealingWithService mDealingWithService;
    private SDHttpImageService mFacebookImageService;
    private ExpandableHeightGridView mGridDealingWith;
    private ImageButton mImageButtonSimilarBusinessCall;
    private ImageButton mImageButtonSimilarBusinessPhoto;
    private ImageView mImagebuttonOurLocation;
    private ImageView mImageviewSiteBanner;
    private LinearLayout mLayoutAboutUs;
    private LinearLayout mLayoutAdLabel;
    private LinearLayout mLayoutAdOperatingHours;
    private LinearLayout mLayoutAdPromotion;
    private LinearLayout mLayoutBranches;
    private LinearLayout mLayoutDealingWith;
    private LinearLayout mLayoutNoReviews;
    private LinearLayout mLayoutOurLocation;
    private LinearLayout mLayoutPhone;
    private LinearLayout mLayoutPhoto;
    private LinearLayout mLayoutReviews;
    private View mLayoutSimilarBusiness;
    private RelativeLayout mLayoutSiteBanner;
    private View mLayoutTipsCell;
    private SDHttpImageService mOurLocationImageService;
    private Bitmap mPhoto;
    private PhotoThumbnailFragmentAdapter mPhotoAdapter;
    private SDHttpImageService mPhotoImageService;
    private ProgressBar mProgressIndicatorBusiness;
    private ProgressBar mProgressIndicatorOurLocation;
    private ProgressBar mProgressIndicatorSiteBanner;
    private TipsServiceOutput mReviews;
    private RelativeLayout mSdSmallBanner;
    private Button mShareButton;
    private SimilarBusinessServiceOutput mSimilarBusiness;
    private SDHttpImageService mSimilarBusinessImageService;
    private SimilarBusinessService mSimilarBusinessService;
    private SDHttpImageService mSiteBannerImageService;
    private TextView mTextViewOfferTitle;
    private TextView mTextViewSeeOffer;
    private TextView mTextviewDetail;
    private TextView mTextviewPhone;
    private TextView mTextviewSimilarBusinessDetail;
    private TextView mTextviewSimilarBusinessTitle;
    private TextView mTextviewTitle;
    private TipsCellViewHolder mTipsCellViewHolder;
    private TipsService mTipsService;
    private ViewPager mViewpagerPhoto;
    private WebView mWebviewAboutUs;
    private WebView mWebviewAdLabel;
    private WebView mWebviewAdOperatingHours;
    private WebView mWebviewAdPromotion;





/*
    static ArrayList access$1002(BusinessDetailActivity businessdetailactivity, ArrayList arraylist)
    {
        businessdetailactivity.mDealingWithList = arraylist;
        return arraylist;
    }

*/


/*
    static SimilarBusinessServiceOutput access$102(BusinessDetailActivity businessdetailactivity, SimilarBusinessServiceOutput similarbusinessserviceoutput)
    {
        businessdetailactivity.mSimilarBusiness = similarbusinessserviceoutput;
        return similarbusinessserviceoutput;
    }

*/





/*
    static BusinessDetailService access$1402(BusinessDetailActivity businessdetailactivity, BusinessDetailService businessdetailservice)
    {
        businessdetailactivity.mBusinessDetailService = businessdetailservice;
        return businessdetailservice;
    }

*/




/*
    static SimilarBusinessService access$1702(BusinessDetailActivity businessdetailactivity, SimilarBusinessService similarbusinessservice)
    {
        businessdetailactivity.mSimilarBusinessService = similarbusinessservice;
        return similarbusinessservice;
    }

*/










/*
    static SDHttpImageService access$2502(BusinessDetailActivity businessdetailactivity, SDHttpImageService sdhttpimageservice)
    {
        businessdetailactivity.mSimilarBusinessImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/


/*
    static SDHttpImageService access$2602(BusinessDetailActivity businessdetailactivity, SDHttpImageService sdhttpimageservice)
    {
        businessdetailactivity.mSiteBannerImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/




/*
    static BusinessImageListService access$2902(BusinessDetailActivity businessdetailactivity, BusinessImageListService businessimagelistservice)
    {
        businessdetailactivity.mBusinessImageListService = businessimagelistservice;
        return businessimagelistservice;
    }

*/







/*
    static SDHttpImageService access$3402(BusinessDetailActivity businessdetailactivity, SDHttpImageService sdhttpimageservice)
    {
        businessdetailactivity.mPhotoImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/


/*
    static DealingWithService access$3502(BusinessDetailActivity businessdetailactivity, DealingWithService dealingwithservice)
    {
        businessdetailactivity.mDealingWithService = dealingwithservice;
        return dealingwithservice;
    }

*/




/*
    static SDHttpImageService access$3802(BusinessDetailActivity businessdetailactivity, SDHttpImageService sdhttpimageservice)
    {
        businessdetailactivity.mOurLocationImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/





/*
    static TipsService access$4102(BusinessDetailActivity businessdetailactivity, TipsService tipsservice)
    {
        businessdetailactivity.mTipsService = tipsservice;
        return tipsservice;
    }

*/



/*
    static TipsServiceOutput access$4202(BusinessDetailActivity businessdetailactivity, TipsServiceOutput tipsserviceoutput)
    {
        businessdetailactivity.mReviews = tipsserviceoutput;
        return tipsserviceoutput;
    }

*/








/*
    static SDHttpImageService access$4902(BusinessDetailActivity businessdetailactivity, SDHttpImageService sdhttpimageservice)
    {
        businessdetailactivity.mFacebookImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/



/*
    static BusinessBranchService access$5002(BusinessDetailActivity businessdetailactivity, BusinessBranchService businessbranchservice)
    {
        businessdetailactivity.mBusinessBranchService = businessbranchservice;
        return businessbranchservice;
    }

*/





/*
    static int access$5302(BusinessDetailActivity businessdetailactivity, int i)
    {
        businessdetailactivity.adRequestRetryCount = i;
        return i;
    }

*/


/*
    static int access$5308(BusinessDetailActivity businessdetailactivity)
    {
        int i = businessdetailactivity.adRequestRetryCount;
        businessdetailactivity.adRequestRetryCount = i + 1;
        return i;
    }

*/





}
