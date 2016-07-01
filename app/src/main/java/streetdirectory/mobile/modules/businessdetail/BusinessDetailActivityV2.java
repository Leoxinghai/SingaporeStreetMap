// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessdetail;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.*;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.core.ui.*;
import streetdirectory.mobile.modules.branchlist.BranchListActivity;
import streetdirectory.mobile.modules.branchlist.BusinessBranchListAdapter;
import streetdirectory.mobile.modules.businessdetail.service.BusinessBranchService;
import streetdirectory.mobile.modules.businessdetail.service.BusinessBranchServiceInput;
import streetdirectory.mobile.modules.businessdetail.service.BusinessBranchServiceOutput;
import streetdirectory.mobile.modules.businessdetail.service.BusinessDetailService;
import streetdirectory.mobile.modules.businessdetail.service.BusinessDetailServiceInput;
import streetdirectory.mobile.modules.businessdetail.service.BusinessDetailServiceOutput;
import streetdirectory.mobile.modules.businessdetail.service.ClaimOfferService;
import streetdirectory.mobile.modules.businessdetail.service.ClaimOfferServiceOutput;
import streetdirectory.mobile.modules.businessdetail.service.DealingWithService;
import streetdirectory.mobile.modules.businessdetail.service.DealingWithServiceInput;
import streetdirectory.mobile.modules.businessdetail.service.DealingWithServiceOutput;
import streetdirectory.mobile.modules.businessdetail.service.SimilarBusinessService;
import streetdirectory.mobile.modules.businessdetail.service.SimilarBusinessServiceInput;
import streetdirectory.mobile.modules.businessdetail.service.SimilarBusinessServiceOutput;
import streetdirectory.mobile.modules.businesslisting.BusinessListingActivity;
import streetdirectory.mobile.modules.businesslisting.offers.OffersListingActivity;
import streetdirectory.mobile.modules.businesslisting.service.MoreOffersService;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.direction.DirectionActivity;
import streetdirectory.mobile.modules.freevoucher.FreeVoucherActivity;
import streetdirectory.mobile.modules.freevoucher.FreeVoucherActivityV2;
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
//            DealingWithAdapter, MoreOffersAdapter, BusinessOfferWebPageActivity, MoreOffersServiceOutput

public class BusinessDetailActivityV2 extends FragmentActivity
{

    public BusinessDetailActivityV2()
    {
        mDealingWithList = new ArrayList();
        isOffer = false;
        adRequestRetryCount = 0;
        redeemVoucherReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent)
            {
                AlertDialog.Builder temp = new AlertDialog.Builder(BusinessDetailActivityV2.this);
                temp.setMessage("Upload Receipt Failed");
                temp.setNegativeButton("Retry", new android.content.DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        Intent intent = new Intent(BusinessDetailActivityV2.this, FreeVoucherActivity.class);
                        intent.putExtra("country_code", mData.countryCode);
                        intent.putExtra("company_name", mData.venue);
                        intent.putExtra("company_ID", mData.companyID);
                        intent.putExtra("offer_id", mData.promoId);
                        startActivityForResult(intent, 0);
                    }

                });

                temp.setPositiveButton("Cancel", new android.content.DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        dialoginterface.dismiss();
                    }

                });
                temp.create().show();
            }

        };
    }

    private void abortAllDownload()
    {
        abortDownloadBusinessDetail();
        abortDownloadSimilarBusiness();
        abortDownloadSimilarBusinessImage();
        abortDownloadBusinessImageList();
        abortDownloadDealingWith();
        abortDownloadOurLocationImage();
        abortDownloadTips();
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

    private void abortDownloadOfferImage()
    {
        if(mOfferImageService != null)
        {
            mOfferImageService.abort();
            mOfferImageService = null;
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
                    mBusinessBranchesAdapter.appendData(sdhttpserviceoutput);
                    i = 0;
                    Iterator iterator;
                    iterator = sdhttpserviceoutput.childs.iterator();
                    do
                    {
                        if(!iterator.hasNext())
                            break;
                        if(((BusinessBranchServiceOutput)iterator.next()).isOfferAvailable)
                            i++;
                    } while(true);
                    int j;
                    if(i > 1)
                        mTextViewBranchTitle.setText((new StringBuilder()).append("Offer available at ").append(i).append(" Branches").toString());
                    else
                    if(i == 1)
                        mTextViewBranchTitle.setText("Offer available at 1 Branch");
                    else
                        mTextViewBranchTitle.setText("Branches");
                    j = 0;
                    for(i = 0; i < mBusinessBranchesAdapter.getCount(); i++)
                    {
                        View temp3 = mBusinessBranchesAdapter.getView(i, null, mListViewBranches);
                        temp3.measure(0, 0);
                        j += temp3.getMeasuredHeight();
                    }

                    ViewGroup.LayoutParams temp2 = mListViewBranches.getLayoutParams();
                    temp2.height = mListViewBranches.getDividerHeight() * (mBusinessBranchesAdapter.getCount() - 1) + j;
                    mListViewBranches.setLayoutParams(temp2);
                    mListViewBranches.requestLayout();
                    mListViewBranches.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView adapterview, View view, int i, long l)
                        {
                            SDStory.post(URLFactory.createGantOfferDetailBranch(mData.promoId, mData.companyID), SDStory.createDefaultParams());
                            Intent intent = new Intent(BusinessDetailActivityV2.this, BranchListActivity.class);
                            intent.putExtra("countryCode", mData.countryCode);
                            intent.putExtra("companyID", mData.companyID);
                            intent.putExtra("offerID", mData.promoId);
                            startActivity(intent);
                        }

                    });
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
                if(j == 0 || k == 0) {
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

    private void downloadMoreOffers()
    {
        moreOffersService = new MoreOffersService(mData.companyID, mData.countryCode) {

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "More Offers Failed");
                mLayoutMoreOffers.setVisibility(View.INVISIBLE);
                moreOffersService = null;
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mMoreOffersAdapter.setData(sdhttpserviceoutput);
                moreOffersService = null;
            }

        };

        moreOffersService.executeAsync();
    }

    private void downloadOfferImage(String s)
    {
        abortDownloadOfferImage();
        mOfferImageService = new SDHttpImageService(s) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Offer Image Aborted");
                mProgressIndicatorOfferImage.setVisibility(View.INVISIBLE);
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Offer Image Failed");
                mImageViewOffer = null;
                mProgressIndicatorOfferImage.setVisibility(View.INVISIBLE);
            }

            public void onSuccess(Bitmap bitmap)
            {
                mImageViewOffer.setImageBitmap(bitmap);
                mOfferImageService = null;
                mProgressIndicatorOfferImage.setVisibility(View.INVISIBLE);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        mProgressIndicatorOfferImage.setVisibility(View.VISIBLE);
        mOfferImageService.executeAsync();
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
                mButtonMoreReviews.setText((new StringBuilder()).append("Read Reviews (").append(l).append(")").toString());
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mTipsService = null;
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    mLayoutReviews.setVisibility(View.VISIBLE);
                    return;
                } else
                {
                    mLayoutNoReviews.setVisibility(View.INVISIBLE);
                    mLayoutReviews.setVisibility(View.INVISIBLE);
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
            mTextViewOfferCompanyName.setText(mData.venue);
            mTextViewCompanyLocationAddress.setText(mData.address);
            if(mData.phoneNumber == null || mData.phoneNumber.length() <= 0) {
                mTextviewPhone.setText("No Number");
                return;
            }
            mTextviewPhone.setText(mData.phoneNumber);
        }
        return;
    }

    private void initEvent()
    {
        mButtonOkTerms.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mViewTermsDialog.setVisibility(View.INVISIBLE);
                mWebViewTermsDescription.loadUrl("about:blank");
            }

        });
        mButtonCallOffer.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                SDStory.post(URLFactory.createGantOfferDetailCall(mData.promoId, mData.companyID), SDStory.createDefaultParams());
                String s1;
                Intent intent = new Intent("android.intent.action.DIAL");
                s1 = mTextviewPhone.getText().toString();
                if(s1.equalsIgnoreCase("no number"))
                    return;
                try
                {
                    intent.setData(Uri.parse((new StringBuilder()).append("tel:").append(s1).toString()));
                    HashMap hashmap = SDStory.createDefaultParams();
                    hashmap.put("c_id", Integer.toString(mData.companyID));
                    hashmap.put("c_pid", s1);
                    hashmap.put("opg", "business_detail");
                    SDStory.post(URLFactory.createGantBusinessCall((new StringBuilder()).append(mData.companyID).append("").toString()), hashmap);
                    startActivity(intent);
                    return;
                }
                catch(Exception ex)
                {
                    Log.e("", "Call failed", ex);
                }
                return;
            }

        });
        mLayoutGetThisOffer.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                SDStory.post(URLFactory.createGantOfferDetailGetThisOffer(mData.promoId, mData.companyID), SDStory.createDefaultParams());
                mViewGetThisOffer.setVisibility(View.VISIBLE);
            }
        });

        String s = getString(R.string.business_detail_grab_offer_checkbox_description);
        SpannableString spannablestring = new SpannableString(s);
        spannablestring.setSpan(new ClickableSpan() {

            public void onClick(View view)
            {
                showGetOfferTermsDialog();
            }

        }, s.indexOf("Terms"), s.length(), 33);
        mTextViewGetOfferTerms.setText(spannablestring);
        mTextViewGetOfferTerms.setMovementMethod(LinkMovementMethod.getInstance());
        mBackButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mButtonGetOfferClose.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mViewGetThisOffer.setVisibility(View.INVISIBLE);
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEditTextGetOfferEmail.getWindowToken(), 0);
            }

        });
        mImageViewRedeemVoucher.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                SDStory.post(URLFactory.createGantOfferDetailRedeemVoucher(mData.promoId, mData.companyID), SDStory.createDefaultParams());
                if(mData.redeemVoucherTemplate == 1)
                {
                    Intent intent = new Intent(BusinessDetailActivityV2.this, FreeVoucherActivity.class);
                    intent.putExtra("country_code", mData.countryCode);
                    intent.putExtra("company_name", mData.venue);
                    intent.putExtra("company_ID", mData.companyID);
                    intent.putExtra("offer_id", mData.promoId);
                    startActivityForResult(intent, 0);
                    return;
                } else
                {
                    Intent intent = new Intent(BusinessDetailActivityV2.this, FreeVoucherActivityV2.class);
                    intent.putExtra("country_code", mData.countryCode);
                    intent.putExtra("company_name", mData.venue);
                    intent.putExtra("company_ID", mData.companyID);
                    intent.putExtra("offer_id", mData.promoId);
                    startActivity(intent);
                    return;
                }
            }
        });
        mButtonGetOfferSubmit.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                validateAndSubmit();
            }

        });
        mImageViewOffer.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                SDStory.post(URLFactory.createGantOfferDetailOfferBanner(mData.promoId, mData.companyID), SDStory.createDefaultParams());
                mScrollView.scrollTo(0, mLayoutOfferImage.getBottom());
            }
        });
        mTextViewShowDirection.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view) {
                startActivityDirection();
            }

        });
        mButtonOurLocationDirection.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view) {
                startActivityDirection();
            }

        });
        mLayoutSimilarBusiness.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view) {
                if (mSimilarBusiness != null) {
                    Intent intent = new Intent(BusinessDetailActivityV2.this, BusinessDetailActivityV2.class);
                    intent.putExtra("data", (Parcelable)mSimilarBusiness);
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
                Intent intent = new Intent(BusinessDetailActivityV2.this, DirectionActivity.class);
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

            public void onPageFinished(WebView webview, String s1) {
                mLayoutAdLabel.setVisibility(View.VISIBLE);
            }

            public boolean shouldOverrideUrlLoading(WebView webview, String s1) {
                if (!s1.startsWith("mailto:")) {
                    Intent intent = new Intent(BusinessDetailActivityV2.this, BusinessOfferWebPageActivity.class);
                    intent.putExtra("offer_url", s1);
                    intent.putExtra("offer_title", mData.venue);
                    intent.putExtra("data", (Parcelable)mData);
                    startActivity(intent);
                }
                return true;
            }

        });

        mWebviewAdOperatingHours.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView webview, String s1)
            {
                mLayoutAdOperatingHours.setVisibility(View.VISIBLE);
            }

        });
        mWebviewAdPromotion.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView webview, String s1)
            {
                mLayoutAdPromotion.setVisibility(View.VISIBLE);
            }

        });
        mWebviewAboutUs.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView webview, String s1)
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
                SDStory.post(URLFactory.createGantOfferDetailBranch(mData.promoId, mData.companyID), SDStory.createDefaultParams());
                Intent intent = new Intent(BusinessDetailActivityV2.this, BranchListActivity.class);
                intent.putExtra("countryCode", mData.countryCode);
                intent.putExtra("companyID", mData.companyID);
                intent.putExtra("offerID", mData.promoId);
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
                    String s1 = mTextviewPhone.getText().toString();
                    intent.setData(Uri.parse((new StringBuilder()).append("tel:").append(s1).toString()));
                    HashMap hashmap = SDStory.createDefaultParams();
                    hashmap.put("c_id", Integer.toString(mData.companyID));
                    hashmap.put("c_pid", s1);
                    hashmap.put("opg", "business_detail");
                    SDStory.post(URLFactory.createGantBusinessCall((new StringBuilder()).append(mData.companyID).append("").toString()), hashmap);
                    startActivity(intent);
                    return;
                }
                // Misplaced declaration of an exception variable
                catch(Exception ex)
                {
                    Log.e("", "Call failed", ex);
                }
            }

        });
    }

    private void initLayout()
    {
        mTextViewTitleBar = (TextView)findViewById(R.id.TitleBar);
        mScrollView = (ScrollView)findViewById(R.id.scrollView_content);
        mLayoutHeader = (RelativeLayout)findViewById(R.id.layout_header);
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
        mPhotoAdapter.scale = 2.0F;
        mViewpagerPhoto.setAdapter(mPhotoAdapter);
        mLayoutAboutUs = (LinearLayout)findViewById(R.id.layout_about_us);
        mTextViewAboutUs = (TextView)findViewById(R.id.textView_about_us);
        mWebviewAboutUs = (WebView)findViewById(R.id.webView_about_us);
        mLayoutAdLabel = (LinearLayout)findViewById(R.id.layout_ad_label);
        mWebviewAdLabel = (WebView)findViewById(R.id.webView_ad_label);
        mLayoutAdOperatingHours = (LinearLayout)findViewById(R.id.layout_ad_operating_hours);
        mWebviewAdOperatingHours = (WebView)findViewById(R.id.webView_ad_operating_hours);
        mLayoutAdPromotion = (LinearLayout)findViewById(R.id.layout_ad_promotion);
        mWebviewAdPromotion = (WebView)findViewById(R.id.webView_ad_promotion);
        mLayoutDealingWith = (LinearLayout)findViewById(R.id.layout_dealing_with);
        mGridDealingWith = (ExpandableHeightGridView)findViewById(R.id.grid_dealing_with);
        mGridDealingWith.setExpanded(true);
        mDealingWithAdapter = new DealingWithAdapter(this);
        mGridDealingWith.setAdapter(mDealingWithAdapter);
        mButtonShowMoreDealing = (TextView)findViewById(R.id.button_show_more_dealing);
        mTextViewOurLocationTitle = (TextView)findViewById(R.id.textView_our_location_title);
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
        mTextViewBranchTitle = (TextView)findViewById(R.id.textView_branch_title);
        mButtonBranch = (TextView)findViewById(R.id.button_branch);
        mListViewBranches = (ListView)findViewById(R.id.listView_branches);
        mBusinessBranchesAdapter = new BusinessBranchListAdapter(this);
        mListViewBranches.setAdapter(mBusinessBranchesAdapter);
        mProgressIndicatorBusiness = (ProgressBar)findViewById(R.id.progress_indicator_business);
        mLayoutPhone = (LinearLayout)findViewById(R.id.layout_button_phone);
        mButtonOurLocationDirection = (Button)findViewById(R.id.buttonDirections);
        mLayoutOfferHeader = (RelativeLayout)findViewById(R.id.layout_header_offer);
        mTextViewOfferCompanyName = (TextView)findViewById(R.id.textview_title_offer);
        mTextViewCompanyLocationAddress = (TextView)findViewById(R.id.textview_our_location_address);
        mLayoutOfferImage = (RelativeLayout)findViewById(R.id.layout_offer_image);
        mImageViewOffer = (FillRatioImageView)findViewById(R.id.imageView_offer);
        mProgressIndicatorOfferImage = (ProgressBar)findViewById(R.id.progress_indicator_offer_image);
        mLayoutGetThisOffer = (LinearLayout)findViewById(R.id.layout_GetThisOffer);
        mTextViewOfferValidDate = (TextView)findViewById(R.id.textView_offer_valid_date);
        mButtonShareOffer = (Button)findViewById(R.id.buttonShareOffer);
        mViewSeparator = findViewById(R.id.separator_view);
        mLayoutTotalViews = (LinearLayout)findViewById(R.id.layout_total_view);
        mTextViewTotalView = (TextView)findViewById(R.id.textView_total_view);
        mButtonCallOffer = (Button)findViewById(R.id.buttonCallOffer);
        mImageViewRedeemVoucher = (FillRatioImageView)findViewById(R.id.imageViewRedeemVoucher);
        mViewGetThisOffer = findViewById(R.id.layout_get_this_offer);
        mTextViewGetOfferDescription = (TextView)findViewById(R.id.textView_claim_offer_description);
        mTextViewGetOfferAddress = (TextView)findViewById(R.id.textView_claim_offer_address);
        mTextViewShowDirection = (TextView)findViewById(R.id.textView_show_direction);
        mTextViewGetOfferSubmitDescription = (TextView)findViewById(R.id.textView_claim_offer_title_desc);
        mTextViewPhoneCode = (TextView)findViewById(R.id.textViewPhoneCodeLabel);
        mEditTextGetOfferEmail = (EditText)findViewById(R.id.editTextEmail);
        mCheckBoxGetOfferTerms = (CheckBox)findViewById(R.id.checkBox_terms);
        mTextViewGetOfferTerms = (TextView)findViewById(R.id.textView_terms);
        mButtonGetOfferSubmit = (ImageButton)findViewById(R.id.buttonSend);
        mButtonGetOfferClose = (Button)findViewById(R.id.buttonCloseEmailOffer);
        mTextViewOfferTerms = (TextView)findViewById(R.id.textView_offer_terms);
        mViewTermsDialog = findViewById(R.id.layout_terms_dialog);
        mWebViewTermsDescription = (WebView)findViewById(R.id.webView_terms_description);
        mButtonOkTerms = (Button)findViewById(R.id.button_ok_terms);
        mProgressBarTerms = (ProgressBar)findViewById(R.id.progressBar_terms);
        mLayoutMoreOffers = (LinearLayout)findViewById(R.id.layout_more_offers);
        mTextViewMoreOffers = (TextView)findViewById(R.id.textView_more_offers);
        mRecyclerViewOffers = (RecyclerView)findViewById(R.id.recycler_view_offer);
        mMoreOffersAdapter = new MoreOffersAdapter(this);
        mRecyclerViewOffers.setAdapter(mMoreOffersAdapter);
        mRecyclerViewOffers.setHasFixedSize(true);
        mRecyclerViewOffers.setLayoutManager(new LinearLayoutManager(this, 0, false));
        mSdSmallBanner = (RelativeLayout)findViewById(R.id.view_sdmob);
    }

    private void initOfferAboutUs()
    {
        mTextViewAboutUs.setText("About Offer");
        mWebviewAboutUs.getSettings().setJavaScriptEnabled(true);
        mWebviewAboutUs.getSettings().setLoadWithOverviewMode(true);
        mWebviewAboutUs.getSettings().setDefaultTextEncodingName("utf-8");
        (new AsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected String doInBackground(Void avoid[]) {
                String result = null;
                DefaultHttpClient temp = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(mData.offerAbout);
                StringBuilder stringbuilder;
                try {
                    InputStream temp2 = temp.execute(((org.apache.http.client.methods.HttpUriRequest) (httpget))).getEntity().getContent();
                    BufferedReader temp3 = new BufferedReader(new InputStreamReader(temp2, "utf-8"), 8);
                    stringbuilder = new StringBuilder();

                    for (; ; ) {
                        String s = ((BufferedReader) (temp3)).readLine();
                        if (s == null)
                            break;
                        stringbuilder.append(s);
                    }

                    result = stringbuilder.toString();
                    temp2.close();
                } catch(Exception ex)
                {
                    ex.printStackTrace();
                    return "";
                }

                return ((String) (result));
            }

            protected void onPostExecute(Object obj)
            {
                onPostExecute((String)obj);
            }

            protected void onPostExecute(String s)
            {
                boolean flag = false;
                String s1;
                flag = true;
                if(s.toLowerCase().contains("see terms and conditions for more details.")) {
                    s1 = s.replaceAll((new StringBuilder()).append("(?i)").append("see terms and conditions for more details.").toString(), "");
                } else if(s.toLowerCase().contains("see terms &amp; conditions for more details."))
                {
                    flag = true;
                    s1 = s.replaceAll((new StringBuilder()).append("(?i)").append("see terms &amp; conditions for more details.").toString(), "");
                } else
                if(s.toLowerCase().contains("see terms &amp; conditions for more details"))
                {
                    flag = true;
                    s1 = s.replaceAll((new StringBuilder()).append("(?i)").append("see terms &amp; conditions for more details").toString(), "");
                } else
                if(s.toLowerCase().contains("see terms &amp; conditions for more info."))
                {
                    flag = true;
                    s1 = s.replaceAll((new StringBuilder()).append("(?i)").append("see terms &amp; conditions for more info.").toString(), "");
                } else
                {
                    s1 = s;
                    if(s.toLowerCase().contains("see terms &amp; conditions for more information."))
                    {
                        flag = true;
                        s1 = s.replaceAll((new StringBuilder()).append("(?i)").append("see terms &amp; conditions for more information.").toString(), "");
                    }
                }

                mWebviewAboutUs.loadData(s1, "text/html; charset=UTF-8", null);
                if(flag)
                {
                    mTextViewOfferTerms.setOnClickListener(new android.view.View.OnClickListener() {

                        public void onClick(View view)
                        {
                            showOfferTermsDialog();
                        }

                    });
                    mTextViewOfferTerms.setVisibility(View.VISIBLE);
                }
                return;
            }

        }).execute(new Void[0]);

    }

    private void initViewWithOffer()
    {
        isOffer = true;
        int i;
        int j;
        if(mData.totalOffer > 1)
        {
            mMoreOffersAdapter.setSelectedOfferId(mData.promoId);
            mMoreOffersAdapter.setOnImageClickedListener(new MoreOffersAdapter.OnImageClickedListener() {

                public void onImageClicked(MoreOffersServiceOutput moreoffersserviceoutput)
                {
                    Intent intent = new Intent(BusinessDetailActivityV2.this, BusinessDetailActivityV2.class);
                    mData.hashData.put("prm_id", moreoffersserviceoutput.offerId);
                    intent.putExtra("data", (Parcelable)mData);
                    startActivity(intent);
                }

            });
            downloadMoreOffers();
            mLayoutMoreOffers.setVisibility(View.VISIBLE);
            mTextViewMoreOffers.setText("More offers from ".concat(mData.venue));
        }
        if(mData.isSubmitMethodSms)
        {
            mTextViewGetOfferSubmitDescription.setText(getString(R.string.business_detail_edittext_offer_phone_label));
            mTextViewPhoneCode.setVisibility(View.VISIBLE);
            mEditTextGetOfferEmail.setHint(getString(R.string.business_detail_edittext_offer_phone));
            mEditTextGetOfferEmail.setInputType(3);
            mEditTextGetOfferEmail.setFilters(new InputFilter[] {
                new android.text.InputFilter.LengthFilter(11)
            });
        }
        SDStory.post(URLFactory.createGantOfferDetailPage(mData.promoId, mData.companyID), SDStory.createDefaultParams());
        mTextViewTitleBar.setText(getString(R.string.offers_listing_title));
        downloadOfferImage(mData.offerImage);
        mLayoutOfferImage.setVisibility(View.VISIBLE);
        mLayoutGetThisOffer.setVisibility(View.VISIBLE);
        mTextViewOfferValidDate.setVisibility(View.VISIBLE);
        if(mData.offerValidDate != null)
        {
            Object obj = new SimpleDateFormat("dd MMMMM yyyy");
            Object obj1 = new SimpleDateFormat("d MMM yyyy");
            try
            {
                obj = ((DateFormat) (obj1)).format(((DateFormat) (obj)).parse(mData.offerValidDate));
            }
            catch(ParseException parseexception)
            {
                parseexception.printStackTrace();
            }
            mTextViewOfferValidDate.setText((new StringBuilder()).append("(Valid till ").append(((String) (obj))).append(")").toString());
        }
        mViewSeparator.setVisibility(View.VISIBLE);
        mTextViewOurLocationTitle.setText(mData.venue);
        initOfferAboutUs();
        mLayoutTotalViews.setVisibility(View.VISIBLE);
        mTextViewTotalView.setText((new StringBuilder()).append(mData.offerViewCount).append(" Views").toString());
        if(mData.phoneNumber != null && mData.phoneNumber.length() > 0)
        {
            mButtonCallOffer.setText((new StringBuilder()).append("Call ").append(mData.phoneNumber).toString());
            mButtonCallOffer.setVisibility(View.VISIBLE);
        }
        if(mData.isPaid)
        {
            mImageViewRedeemVoucher.setVisibility(View.VISIBLE);
            i = Math.round(TypedValue.applyDimension(1, 10F, getResources().getDisplayMetrics()));
            j = Math.round(TypedValue.applyDimension(1, 62F, getResources().getDisplayMetrics()));
            LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-1, -2);
            ((android.widget.LinearLayout.LayoutParams) (layoutParams)).setMargins(i, i, i, j);
            mButtonCallOffer.setLayoutParams(((android.view.ViewGroup.LayoutParams) (layoutParams)));
        }
        String lAddress;
        lAddress = mData.address;

        if(mData.distance != null)
            lAddress = (new StringBuilder()).append(((String) (lAddress))).append(" (").append(mData.distance).append(" away)").toString();
        mTextViewGetOfferAddress.setText(((CharSequence) (lAddress)));
        mTextViewGetOfferDescription.setText((new StringBuilder()).append("Show this offer to the staff at ").append(mData.venue).toString());
        if(mData.linkOffer != null && mData.linkOffer.length() > 0)
        {
            mButtonShareOffer.setVisibility(View.VISIBLE);
            mButtonShareOffer.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view)
                {
                    SDStory.post(URLFactory.createGantOfferDetailShareOffer(mData.promoId, mData.companyID), SDStory.createDefaultParams());
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", "Offer");
                    intent.putExtra("android.intent.extra.TEXT", mData.linkOffer);
                    startActivity(Intent.createChooser(intent, "Share this Offer"));
                }

            });
        }
    }

    private void initViewWithoutWithoutOffer()
    {
        String s = getIntent().getStringExtra("branch_name");
        TextView textview = mTextviewTitle;
        if(s == null)
            s = mData.venue;
        textview.setText(s);
        mTextviewDetail.setText(mData.address);
        if(mData.phoneNumber != null && mData.phoneNumber.length() > 0)
            mTextviewPhone.setText(mData.phoneNumber);
        else
            mTextviewPhone.setText("No Number");
        mLayoutPhone.setVisibility(View.VISIBLE);
        mLayoutOfferHeader.setVisibility(View.INVISIBLE);
        mLayoutHeader.setVisibility(View.VISIBLE);
        if(mData.siteBanner != null)
        {
            mLayoutSiteBanner.setVisibility(View.VISIBLE);
            downloadBusinessSiteBanner();
        }
        textview = null;

        if(mData.adsLabel != null)
        {
            if(mData.adsLabel.length() > 0)
                s = (new StringBuilder()).append("<font face=\"HelveticaNeue\" size=\"2\">").append(mData.adsLabel).append("</font>").toString();
        }
        if(s != null && s.length() > 0)
            mWebviewAdLabel.loadData(s, "text/html", null);
        mWebviewAboutUs.loadUrl(URLFactory.createURLBusinessAboutUs(mData.countryCode, mData.companyID, SDPreferences.getInstance().getAPIVersion()));
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void loadSmallBanner()
    {
        SdMobHelper sdmobhelper = SdMobHelper.getInstance(this);
        Object obj;
        if(mData.offer != null && mData.offer.length() > 0)
            obj = SdMob.ad_bnr_offer_profile;
        else
            obj = SdMob.ad_bnr_business_profile;
        obj = SmallBanner.getBannerFromSdMobUnit(this, sdmobhelper.getSdMobUnit(((streetdirectory.mobile.modules.sdmob.SdMobHelper.SdMobUnit) (obj))));
//        ((SmallBanner) (obj)).setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener() );
        obj = ((SmallBanner) (obj)).getView(this);
        mSdSmallBanner.removeAllViews();
        mSdSmallBanner.addView(((View) (obj)), new FrameLayout.LayoutParams(-1, -1));
    }

    private void populateVisibleView()
    {
        boolean flag = true;
        mTextViewOfferCompanyName.setText(mData.venue);
        mTextViewCompanyLocationAddress.setText(mData.address);
        if(mData.phoneNumber != null && mData.phoneNumber.length() > 0)
            mTextviewPhone.setText(mData.phoneNumber);
        else
            mTextviewPhone.setText("No Number");
        if(mData.longitude != 0.0D && mData.latitude != 0.0D)
            mButtonMap.setEnabled(true);
        if(mData.offer != null && mData.offer.length() > 0)
            initViewWithOffer();
        else
            initViewWithoutWithoutOffer();
        downloadBusinessImageList();
        if(mData.adOperatingHours != null && mData.adOperatingHours.length() > 0)
            mWebviewAdOperatingHours.loadData(mData.adOperatingHours, "text/html", null);
        if(mData.adPromotion != null && mData.adPromotion.length() > 0)
            mWebviewAdPromotion.loadData(mData.adPromotion, "text/html", null);
        downloadDealingWith();
        mLayoutOurLocation.setVisibility(View.VISIBLE);
        if(mData.longitude != 0.0D && mData.latitude != 0.0D)
        {
            android.view.ViewGroup.LayoutParams layoutparams = mImagebuttonOurLocation.getLayoutParams();
            int i = (int)((float)UIHelper.getScreenDimension().widthPixels - UIHelper.toPixel(16F));
            int j = layoutparams.height;
            if(mData.offer == null || mData.offer.length() <= 0)
                flag = false;
            downloadOurLocationImage(URLFactory.createURLMapImage(flag, mData.longitude, mData.latitude, i, j, 13));
        } else
        {
            mImagebuttonOurLocation.setImageResource(R.drawable.ic_map_no_location);
        }
        downloadTips();
        downloadBusinessBranch();
    }

    private void showDialog(String s, String s1)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        if(s != null)
            builder.setTitle(s);
        builder.setMessage(s1).setCancelable(false).setPositiveButton("OK", null);
        builder.show().show();
    }

    private void showGetOfferTermsDialog()
    {
        mViewTermsDialog.setVisibility(View.VISIBLE);
        mProgressBarTerms.setVisibility(View.INVISIBLE);
        String s = getString(R.string.business_detail_get_offer_terms);
        mWebViewTermsDescription.loadData(s.replace("%company%", mData.venue), "text/html", null);
    }

    private void showOfferTermsDialog()
    {
        mProgressBarTerms.setVisibility(View.VISIBLE);
        mViewTermsDialog.setVisibility(View.VISIBLE);
        mWebViewTermsDescription.getSettings().setLoadWithOverviewMode(true);
        mWebViewTermsDescription.getSettings().setDefaultTextEncodingName("utf-8");
        mWebViewTermsDescription.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView webview, String s)
            {
                mProgressBarTerms.setVisibility(View.INVISIBLE);
            }

        });
        mWebViewTermsDescription.loadUrl(mData.offerTermsAndCondition);
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

    private void startActivityDirection()
    {
        if(isOffer)
            SDStory.post(URLFactory.createGantOfferDetailDirection(mData.promoId, mData.companyID), SDStory.createDefaultParams());
        Intent intent = new Intent(this, DirectionActivity.class);
        intent.putExtra("endData", (Parcelable)mData);
        intent.putExtra("menu_visible", false);
        startActivity(intent);
    }

    private void startActivityMapPreview()
    {
        if(isOffer)
            SDStory.post(URLFactory.createGantOfferDetailMap(mData.promoId, mData.companyID), SDStory.createDefaultParams());
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

    private boolean validateAndSubmit()
    {
        if(!mCheckBoxGetOfferTerms.isChecked())
        {
            showDialog(null, "You have not agree to the Terms");
            return false;
        }
        String s = mEditTextGetOfferEmail.getText().toString();
        if(!mData.isSubmitMethodSms && !s.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
        {
            showDialog(null, "Please enter a valid Email Address");
            return false;
        } else
        {
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEditTextGetOfferEmail.getWindowToken(), 0);
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
            claimOfferService = new ClaimOfferService(mData.companyID, mData.locationID, mData.promoId, s, mData.isSubmitMethodSms) {

                public void onFailed(Exception exception)
                {
                    mViewGetThisOffer.setVisibility(View.INVISIBLE);
                    onFailed(exception);
                    showDialog(null, getString(R.string.get_this_offer_email_fail_message));
                }


                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mViewGetThisOffer.setVisibility(View.INVISIBLE);
                    mProgressDialog.hide();
                    String temp = ((ClaimOfferServiceOutput)sdhttpserviceoutput.childs.get(0)).result;
                    if(mData.isSubmitMethodSms)
                    {
                        if(temp.equals("1"))
                            showDialog(null, getString(R.string.get_this_offer_phone_success_message));
                        else
                        if(temp.equals("phone_exist"))
                            showDialog(null, getString(R.string.get_this_offer_phone_exist_message));
                        else
                            showDialog(null, getString(R.string.get_this_offer_email_fail_message));
                        mEditTextGetOfferEmail.setText("");
                        return;
                    }
                    if(temp.equals("1"))
                        showDialog(null, getString(R.string.get_this_offer_email_success_message));
                    else
                    if(temp.equals("email_exist"))
                        showDialog(null, getString(R.string.get_this_offer_email_exist_message));
                    else
                        showDialog(null, getString(R.string.get_this_offer_email_fail_message));
                    mEditTextGetOfferEmail.setText("");
                }

            };
            claimOfferService.executeAsync();
            return true;
        }
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_business_detail_v2);
        LocalBroadcastManager.getInstance(this).registerReceiver(redeemVoucherReceiver, new IntentFilter("redeem_voucher"));
        initialize();
        if(mData != null)
            downloadBusinessDetail();
        loadSmallBanner();
    }

    protected void onDestroy()
    {
        abortAllDownload();
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(redeemVoucherReceiver);
    }

    protected void onPause()
    {
        super.onPause();
    }

    private int adRequestRetryCount;
    private ClaimOfferService claimOfferService;
    private boolean isOffer;
    private Button mBackButton;
    private BusinessBranchService mBusinessBranchService;
    private BusinessBranchListAdapter mBusinessBranchesAdapter;
    private BusinessDetailService mBusinessDetailService;
    private BusinessImageListService mBusinessImageListService;
    private TextView mButtonBranch;
    private ImageButton mButtonBusinessPhoto;
    private Button mButtonCallOffer;
    private Button mButtonDirection;
    private Button mButtonGetOfferClose;
    private ImageButton mButtonGetOfferSubmit;
    private Button mButtonMap;
    private Button mButtonMoreReviews;
    private Button mButtonOkTerms;
    private Button mButtonOurLocationDirection;
    private Button mButtonShareOffer;
    private TextView mButtonShowMoreDealing;
    private Button mButtonTips;
    private CheckBox mCheckBoxGetOfferTerms;
    private BusinessDetailServiceOutput mData;
    private DealingWithAdapter mDealingWithAdapter;
    private ArrayList mDealingWithList;
    private DealingWithService mDealingWithService;
    private EditText mEditTextGetOfferEmail;
    private ExpandableHeightGridView mGridDealingWith;
    private ImageButton mImageButtonSimilarBusinessCall;
    private ImageButton mImageButtonSimilarBusinessPhoto;
    private FillRatioImageView mImageViewOffer;
    private FillRatioImageView mImageViewRedeemVoucher;
    private ImageView mImagebuttonOurLocation;
    private ImageView mImageviewSiteBanner;
    private LinearLayout mLayoutAboutUs;
    private LinearLayout mLayoutAdLabel;
    private LinearLayout mLayoutAdOperatingHours;
    private LinearLayout mLayoutAdPromotion;
    private LinearLayout mLayoutBranches;
    private LinearLayout mLayoutDealingWith;
    private LinearLayout mLayoutGetThisOffer;
    private RelativeLayout mLayoutHeader;
    private LinearLayout mLayoutMoreOffers;
    private LinearLayout mLayoutNoReviews;
    private RelativeLayout mLayoutOfferHeader;
    private RelativeLayout mLayoutOfferImage;
    private LinearLayout mLayoutOurLocation;
    private LinearLayout mLayoutPhone;
    private LinearLayout mLayoutPhoto;
    private LinearLayout mLayoutReviews;
    private View mLayoutSimilarBusiness;
    private RelativeLayout mLayoutSiteBanner;
    private View mLayoutTipsCell;
    private LinearLayout mLayoutTotalViews;
    private ListView mListViewBranches;
    private MoreOffersAdapter mMoreOffersAdapter;
    private SDHttpImageService mOfferImageService;
    private SDHttpImageService mOurLocationImageService;
    private PhotoThumbnailFragmentAdapter mPhotoAdapter;
    private SDHttpImageService mPhotoImageService;
    private ProgressBar mProgressBarTerms;
    private ProgressDialog mProgressDialog;
    private ProgressBar mProgressIndicatorBusiness;
    private ProgressBar mProgressIndicatorOfferImage;
    private ProgressBar mProgressIndicatorOurLocation;
    private ProgressBar mProgressIndicatorSiteBanner;
    private RecyclerView mRecyclerViewOffers;
    private TipsServiceOutput mReviews;
    private ScrollView mScrollView;
    private RelativeLayout mSdSmallBanner;
    private Button mShareButton;
    private SimilarBusinessServiceOutput mSimilarBusiness;
    private SDHttpImageService mSimilarBusinessImageService;
    private SimilarBusinessService mSimilarBusinessService;
    private SDHttpImageService mSiteBannerImageService;
    private TextView mTextViewAboutUs;
    private TextView mTextViewBranchTitle;
    private TextView mTextViewCompanyLocationAddress;
    private TextView mTextViewGetOfferAddress;
    private TextView mTextViewGetOfferDescription;
    private TextView mTextViewGetOfferSubmitDescription;
    private TextView mTextViewGetOfferTerms;
    private TextView mTextViewMoreOffers;
    private TextView mTextViewOfferCompanyName;
    private TextView mTextViewOfferTerms;
    private TextView mTextViewOfferValidDate;
    private TextView mTextViewOurLocationTitle;
    private TextView mTextViewPhoneCode;
    private TextView mTextViewShowDirection;
    private TextView mTextViewTitleBar;
    private TextView mTextViewTotalView;
    private TextView mTextviewDetail;
    private TextView mTextviewPhone;
    private TextView mTextviewSimilarBusinessDetail;
    private TextView mTextviewSimilarBusinessTitle;
    private TextView mTextviewTitle;
    private TipsCellViewHolder mTipsCellViewHolder;
    private TipsService mTipsService;
    private View mViewGetThisOffer;
    private View mViewSeparator;
    private View mViewTermsDialog;
    private ViewPager mViewpagerPhoto;
    private WebView mWebViewTermsDescription;
    private WebView mWebviewAboutUs;
    private WebView mWebviewAdLabel;
    private WebView mWebviewAdOperatingHours;
    private WebView mWebviewAdPromotion;
    private MoreOffersService moreOffersService;
    private BroadcastReceiver redeemVoucherReceiver;






/*
    static SimilarBusinessServiceOutput access$1102(BusinessDetailActivityV2 businessdetailactivityv2, SimilarBusinessServiceOutput similarbusinessserviceoutput)
    {
        businessdetailactivityv2.mSimilarBusiness = similarbusinessserviceoutput;
        return similarbusinessserviceoutput;
    }

*/












/*
    static ArrayList access$2002(BusinessDetailActivityV2 businessdetailactivityv2, ArrayList arraylist)
    {
        businessdetailactivityv2.mDealingWithList = arraylist;
        return arraylist;
    }

*/







/*
    static BusinessDetailService access$2602(BusinessDetailActivityV2 businessdetailactivityv2, BusinessDetailService businessdetailservice)
    {
        businessdetailactivityv2.mBusinessDetailService = businessdetailservice;
        return businessdetailservice;
    }

*/




/*
    static SimilarBusinessService access$2902(BusinessDetailActivityV2 businessdetailactivityv2, SimilarBusinessService similarbusinessservice)
    {
        businessdetailactivityv2.mSimilarBusinessService = similarbusinessservice;
        return similarbusinessservice;
    }

*/










/*
    static SDHttpImageService access$3702(BusinessDetailActivityV2 businessdetailactivityv2, SDHttpImageService sdhttpimageservice)
    {
        businessdetailactivityv2.mSimilarBusinessImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/


/*
    static SDHttpImageService access$3802(BusinessDetailActivityV2 businessdetailactivityv2, SDHttpImageService sdhttpimageservice)
    {
        businessdetailactivityv2.mSiteBannerImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/





/*
    static BusinessImageListService access$4102(BusinessDetailActivityV2 businessdetailactivityv2, BusinessImageListService businessimagelistservice)
    {
        businessdetailactivityv2.mBusinessImageListService = businessimagelistservice;
        return businessimagelistservice;
    }

*/






/*
    static SDHttpImageService access$4602(BusinessDetailActivityV2 businessdetailactivityv2, SDHttpImageService sdhttpimageservice)
    {
        businessdetailactivityv2.mPhotoImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/



/*
    static FillRatioImageView access$4702(BusinessDetailActivityV2 businessdetailactivityv2, FillRatioImageView fillratioimageview)
    {
        businessdetailactivityv2.mImageViewOffer = fillratioimageview;
        return fillratioimageview;
    }

*/


/*
    static SDHttpImageService access$4802(BusinessDetailActivityV2 businessdetailactivityv2, SDHttpImageService sdhttpimageservice)
    {
        businessdetailactivityv2.mOfferImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/




/*
    static DealingWithService access$5002(BusinessDetailActivityV2 businessdetailactivityv2, DealingWithService dealingwithservice)
    {
        businessdetailactivityv2.mDealingWithService = dealingwithservice;
        return dealingwithservice;
    }

*/




/*
    static SDHttpImageService access$5302(BusinessDetailActivityV2 businessdetailactivityv2, SDHttpImageService sdhttpimageservice)
    {
        businessdetailactivityv2.mOurLocationImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/




/*
    static TipsService access$5602(BusinessDetailActivityV2 businessdetailactivityv2, TipsService tipsservice)
    {
        businessdetailactivityv2.mTipsService = tipsservice;
        return tipsservice;
    }

*/






/*
    static BusinessBranchService access$6002(BusinessDetailActivityV2 businessdetailactivityv2, BusinessBranchService businessbranchservice)
    {
        businessdetailactivityv2.mBusinessBranchService = businessbranchservice;
        return businessbranchservice;
    }

*/








/*
    static MoreOffersService access$6702(BusinessDetailActivityV2 businessdetailactivityv2, MoreOffersService moreoffersservice)
    {
        businessdetailactivityv2.moreOffersService = moreoffersservice;
        return moreoffersservice;
    }

*/








/*
    static int access$7202(BusinessDetailActivityV2 businessdetailactivityv2, int i)
    {
        businessdetailactivityv2.adRequestRetryCount = i;
        return i;
    }

*/


/*
    static int access$7208(BusinessDetailActivityV2 businessdetailactivityv2)
    {
        int i = businessdetailactivityv2.adRequestRetryCount;
        businessdetailactivityv2.adRequestRetryCount = i + 1;
        return i;
    }

*/



}
