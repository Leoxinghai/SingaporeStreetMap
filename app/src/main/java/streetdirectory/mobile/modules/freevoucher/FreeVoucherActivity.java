// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.freevoucher;

import android.app.AlertDialog;
import android.content.*;
import android.graphics.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.webkit.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import streetdirectory.mobile.service.URLFactory;

// Referenced classes of package streetdirectory.mobile.modules.freevoucher:
//            VoucherListService, ReceiptSubmitionService, VoucherListServiceOutput

public class FreeVoucherActivity extends SDActivity
{

    public FreeVoucherActivity()
    {
        voucherList = new ArrayList();
        isTermsVisible = false;
    }

    private File createImageFile()
        throws IOException
    {
        Object obj = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)).format(new Date());
        obj = File.createTempFile((new StringBuilder()).append("SD_").append(((String) (obj))).append("_").toString(), ".png", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        mCurrentPhotoFile = ((File) (obj));
        return ((File) (obj));
    }

    private void dispatchTakePictureIntent()
    {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = null;
        try {
            if (intent.resolveActivity(getPackageManager()) == null) {
                return;
            } else {
                File file1 = createImageFile();
                file = file1;
            }

            if (file != null) {
                intent.putExtra("output", Uri.fromFile(file));
                startActivityForResult(intent, 1);
            }
        } catch(IOException ioex) {
            ioex.printStackTrace();
        }
    }

    private void initData()
    {
        Intent intent = getIntent();
        mCountryCode = intent.getStringExtra("country_code");
        companyName = intent.getStringExtra("company_name");
        mOfferId = intent.getStringExtra("offer_id");
        companyID = intent.getIntExtra("company_ID", 0);
        SDStory.post(URLFactory.createGantRedeemVoucherPage(mOfferId, companyID), SDStory.createDefaultParams());
        if(mCountryCode == null)
            mCountryCode = "sg";
        if(companyName != null)
            mTextViewCompanyName.setText(companyName);
        mSpinnerVoucher.setEnabled(false);
        mEditTextName.setText(SDPreferences.getInstance().getRedeemVoucherName());
        mEditTextAddress.setText(SDPreferences.getInstance().getRedeemVoucherAddress());
        mEditTextPhone.setText(SDPreferences.getInstance().getRedeemVoucherPhoneNumber());
    }

    private void initEvent()
    {
        loadTerms();
        mLayoutTerms.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                FreeVoucherActivity temp = FreeVoucherActivity.this;
                boolean flag;
                if(!isTermsVisible)
                    flag = true;
                else
                    flag = false;
                temp.isTermsVisible = flag;
                if(isTermsVisible)
                {
                    rotateArrow(180);
                    mWebViewTerms.setVisibility(View.VISIBLE);
                    mScrollViewMain.post(new Runnable() {

                        public void run()
                        {
                            mScrollViewMain.smoothScrollTo(0, mButtonSubmit.getBottom());
                        }

                    });
                    SDStory.post(URLFactory.createGantRedeemVoucherTAC(mOfferId, companyID), SDStory.createDefaultParams());
                    return;
                } else
                {
                    rotateArrow(360);
                    mScrollViewMain.post(new Runnable() {

                        public void run()
                        {
                            mScrollViewMain.fullScroll(33);
                            mWebViewTerms.setVisibility(View.INVISIBLE);
                        }

                    });
                    return;
                }
            }

        });
        mImageBUttonClose.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mButtonUploadPhoto.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                showUploadPhotoOptionDialog();
            }

        });
        mButtonSubmit.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                String temp = mEditTextName.getText().toString();
                String s = mEditTextAddress.getText().toString();
                String s1 = mEditTextPhone.getText().toString();
                if(isFieldValid(temp, s1, ""))
                {
                    SDStory.post(URLFactory.createGantRedeemVoucherSubmit(mOfferId, companyID), SDStory.createDefaultParams());
                    postData(temp, s, "", s1);
                    setResult(-1, null);
                    finish();
                }
            }

        });
        mLayoutReceiptUploaded.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                photoBitmap = null;
                mButtonUploadPhoto.setVisibility(View.VISIBLE);
                mLayoutReceiptUploaded.setVisibility(View.INVISIBLE);
            }

        });
        mLayoutVoucherSelected.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mSpinnerVoucher.setSelection(0);
                mLayoutVoucherSelected.setVisibility(View.INVISIBLE);
                mLayoutSpinner.setVisibility(View.VISIBLE);
            }

        });
    }

    private void initView()
    {
        mScrollViewMain = (ScrollView)findViewById(R.id.scrollView_main);
        mTextViewCompanyName = (TextView)findViewById(R.id.textview_company_name);
        mButtonUploadPhoto = (Button)findViewById(R.id.button_upload_photo);
        mButtonSubmit = (Button)findViewById(R.id.SubmitButton);
        mImageBUttonClose = (ImageView)findViewById(R.id.imageButton_close);
        mSpinnerVoucher = (Spinner)findViewById(R.id.spinner_voucher);
        mEditTextName = (EditText)findViewById(R.id.edittext_name);
        mEditTextAddress = (EditText)findViewById(R.id.edittext_address);
        mEditTextPhone = (EditText)findViewById(R.id.edittext_phone);
        mLayoutTerms = (RelativeLayout)findViewById(R.id.layout_terms);
        mTextViewTerms = (TextView)findViewById(R.id.textView_terms);
        imageViewArrow = (ImageView)findViewById(R.id.imageView_arrow);
        mLayoutSpinner = findViewById(R.id.layoutSpinner);
        mLayoutReceiptUploaded = findViewById(R.id.layoutReceiptUploaded);
        mTextViewReceiptUploaded = (TextView)findViewById(R.id.textViewReceiptUploaded);
        mLayoutVoucherSelected = findViewById(R.id.layoutVoucherSelected);
        mTextViewSelectedVoucher = (TextView)findViewById(R.id.textViewSelectedVoucher);
        mWebViewTerms = (WebView)findViewById(R.id.webView_terms);
        mViewTermsDialog = findViewById(R.id.layout_terms_dialog);
        mWebViewTermsDescription = (WebView)findViewById(R.id.webView_terms_description);
        mButtonOkTerms = (Button)findViewById(R.id.button_ok_terms);
        mProgressBarTerms = (ProgressBar)findViewById(R.id.progressBar_terms);
    }

    private void initVoucherList()
    {
        voucherListService = new VoucherListService(mCountryCode) {

            public void onFailed(Exception exception)
            {
                super.onFailed(exception);
                SDLogger.debug(exception.getMessage());
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                super.onSuccess(sdhttpserviceoutput);
                voucherList = sdhttpserviceoutput.childs;
                spinnerData = new String[sdhttpserviceoutput.childs.size() + 1];
                spinnerData[0] = getString(R.string.free_voucher_available_voucher);
                for(int i = 0; i < voucherList.size(); i++)
                    spinnerData[i + 1] = ((VoucherListServiceOutput)voucherList.get(i)).voucherName;

                ArrayAdapter temp = new ArrayAdapter(FreeVoucherActivity.this, R.layout.cell_spinner_voucher_item, spinnerData);
                mSpinnerVoucher.setAdapter(temp);
                mSpinnerVoucher.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView adapterview, View view, int i, long l)
                    {
                        if(i != 0)
                        {
                            voucherId = ((VoucherListServiceOutput)voucherList.get(i - 1)).voucherId;
                            SDStory.post(URLFactory.createGantRedeemVoucherVoucherList(mOfferId, companyID, voucherId), SDStory.createDefaultParams());
                            mTextViewSelectedVoucher.setText(spinnerData[i]);
                            mLayoutSpinner.setVisibility(View.INVISIBLE);
                            mLayoutVoucherSelected.setVisibility(View.VISIBLE);
                        }
                    }

                    public void onNothingSelected(AdapterView adapterview)
                    {
                    }

                });
                mSpinnerVoucher.setEnabled(true);
            }

        };
        voucherListService.executeAsync();
    }

    private boolean isFieldValid(String s, String s1, String s2)
    {
        boolean flag = true;
        if(photoBitmap == null)
        {
            flag = false;
            AlertDialog.Builder temp = new android.app.AlertDialog.Builder(this);
            temp.setMessage("Please upload the photo");
            temp.setPositiveButton("Upload Now", new android.content.DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialoginterface, int i)
                {
                    showUploadPhotoOptionDialog();
                }

            });
            temp.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialoginterface, int i)
                {
                    dialoginterface.dismiss();
                }

            });
            temp.create().show();
        }
        if(voucherList == null || voucherList.size() == 0 || mSpinnerVoucher.getSelectedItemPosition() == 0)
        {
            mSpinnerVoucher.setBackgroundResource(R.drawable.shape_rectangel_white_red_border);
            flag = false;
            AlertDialog.Builder temp3 = new android.app.AlertDialog.Builder(this);
            temp3.setMessage("Please choose voucher");
            temp3.setNegativeButton("OK", new android.content.DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialoginterface, int i)
                {
                    dialoginterface.dismiss();
                }

            });
            temp3.create().show();
        } else
        {
            mSpinnerVoucher.setBackgroundColor(Color.parseColor("#EAEAEA"));
        }
        if(s1.trim().length() == 0)
        {
            mEditTextPhone.setBackgroundResource(R.drawable.shape_rectangel_white_red_border);
            flag = false;
        } else
        {
            mEditTextPhone.setBackgroundResource(R.drawable.shape_rectangel_white_grey_border);
        }
        if(s.trim().length() == 0)
        {
            mEditTextName.setBackgroundResource(R.drawable.shape_rectangel_white_red_border);
            return false;
        } else
        {
            mEditTextName.setBackgroundResource(R.drawable.shape_rectangel_white_grey_border);
            return flag;
        }
    }

    private void loadTerms()
    {
        mWebViewTerms.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView webview, String s) {
                mProgressBarTerms.setVisibility(View.INVISIBLE);
            }

        });
        mWebViewTerms.getSettings().setLoadWithOverviewMode(true);
        mWebViewTerms.getSettings().setDefaultTextEncodingName("utf-8");
        mWebViewTerms.loadUrl(URLFactory.createURLVoucherTermsAndConditions());
    }

    private void pickImageFromLibrary()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        startActivityForResult(intent, 0);
    }

    private void postData(String s, String s1, String s2, String s3)
    {
        ReceiptSubmitionService.OnSuccessListener onsuccesslistener = new ReceiptSubmitionService.OnSuccessListener() {

            public void onSuccess(boolean flag)
            {
            }

        };
        ReceiptSubmitionService.OnFailedListener onfailedlistener = new ReceiptSubmitionService.OnFailedListener() {

            public void onFailed()
            {
            }

        };
        ReceiptSubmitionService.post(mOfferId, mCountryCode, s, s1, s2, s3, voucherId, mCurrentPhotoFile.getName(), photoBitmap, onsuccesslistener, onfailedlistener);
    }

    private void rotateArrow(int i)
    {
        RotateAnimation rotateanimation = new RotateAnimation(i - 180, i, 1, 0.5F, 1, 0.5F);
        rotateanimation.setDuration(300L);
        rotateanimation.setFillAfter(true);
        imageViewArrow.startAnimation(rotateanimation);
    }

    private Bitmap scaleBitmap(Bitmap bitmap)
    {
        if(bitmap.getWidth() <= bitmap.getHeight()) {
            if(bitmap.getHeight() < 1024)
                return bitmap;
            float f1 = (float)mReceiptBitmap.getHeight() / (float)mReceiptBitmap.getWidth();
            bitmap = BitmapTools.scale(mReceiptBitmap, (int)(1024F / f1), (int)1024F);
            return bitmap;
        }

        if(bitmap.getWidth() >= 1024) {
            float f = (float)mReceiptBitmap.getWidth() / (float)mReceiptBitmap.getHeight();
            bitmap = BitmapTools.scale(mReceiptBitmap, (int)1024F, (int)(1024F / f));
            return bitmap;
        }
        return bitmap;
    }

    private void showTermsDialog()
    {
        mViewTermsDialog.setVisibility(View.VISIBLE);
        mWebViewTermsDescription.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView webview, String s)
            {
                mProgressBarTerms.setVisibility(View.INVISIBLE);
            }

        });
        mWebViewTermsDescription.getSettings().setLoadWithOverviewMode(true);
        mWebViewTermsDescription.getSettings().setDefaultTextEncodingName("utf-8");
        mWebViewTermsDescription.loadUrl(URLFactory.createURLVoucherTermsAndConditions());
        mButtonOkTerms.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view) {
                mViewTermsDialog.setVisibility(View.INVISIBLE);
            }

        });
    }

    private void showUploadPhotoOptionDialog()
    {
        SDStory.post(URLFactory.createGantRedeemVoucherTakePhoto(mOfferId, companyID), SDStory.createDefaultParams());
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog.Builder builder1 = builder.setTitle("How would you like to upload your receipt?");
        android.content.DialogInterface.OnClickListener onclicklistener = new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                switch(i)
                {
                default:
                    return;

                case 0: // '\0'
                    dispatchTakePictureIntent();
                    return;

                case 1: // '\001'
                    pickImageFromLibrary();
                    break;
                }
            }

        };
        builder1.setItems(new String[]{
                "Take photo", "Choose from library", "Cancel"
        }, onclicklistener);
        builder.show();
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
        if(j != -1)
            return;

        if(mReceiptBitmap != null)
        {
            mReceiptBitmap.recycle();
            mReceiptBitmap = null;
        }
        if(i != 1) {
        if(i == 0)
            try
            {
                mCurrentPhotoFile = new File(intent.getData().toString());
                InputStream is = getContentResolver().openInputStream(intent.getData());
                mReceiptBitmap = BitmapFactory.decodeStream(is);
                is.close();
            }
            // Misplaced declaration of an exception variable
            catch(IOException ioex)
            {
                ioex.printStackTrace();
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        } else
            mReceiptBitmap = BitmapFactory.decodeFile(mCurrentPhotoFile.getAbsolutePath());

        Bitmap tmp = scaleBitmap(mReceiptBitmap);
        if(mReceiptBitmap != tmp)
        {
            mReceiptBitmap.recycle();
            mReceiptBitmap = null;
        }
        mReceiptBitmap = tmp;
        ByteArrayOutputStream temp2 = new ByteArrayOutputStream();
        mReceiptBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, temp2);
        SDLogger.info((new StringBuilder()).append("Image compression result ").append((float)temp2.size() / 1024F).append(" Kb").toString());
        photoBitmap = temp2.toByteArray();
        mButtonUploadPhoto.setVisibility(View.INVISIBLE);
        mLayoutReceiptUploaded.setVisibility(View.VISIBLE);
        return;
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_free_voucher);
        initView();
        initData();
        initEvent();
        initVoucherList();
    }

    protected void onPause()
    {
        android.content.SharedPreferences.Editor editor = SDPreferences.getInstance().createEditor();
        editor.putString("redeem_voucher_name", mEditTextName.getText().toString());
        editor.putString("redeem_voucher_address", mEditTextAddress.getText().toString());
        editor.putString("redeem_voucher_phone_number", mEditTextPhone.getText().toString());
        editor.commit();
        super.onPause();
    }

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_LIBRARY = 0;
    private int companyID;
    private String companyName;
    private ImageView imageViewArrow;
    private boolean isTermsVisible;
    private Button mButtonOkTerms;
    private Button mButtonSubmit;
    private Button mButtonUploadPhoto;
    private String mCountryCode;
    private File mCurrentPhotoFile;
    private EditText mEditTextAddress;
    private EditText mEditTextName;
    private EditText mEditTextPhone;
    private ImageView mImageBUttonClose;
    private View mLayoutReceiptUploaded;
    private View mLayoutSpinner;
    private RelativeLayout mLayoutTerms;
    private View mLayoutVoucherSelected;
    private String mOfferId;
    private ProgressBar mProgressBarTerms;
    private Bitmap mReceiptBitmap;
    private ScrollView mScrollViewMain;
    private Spinner mSpinnerVoucher;
    private TextView mTextViewCompanyName;
    private TextView mTextViewReceiptUploaded;
    private TextView mTextViewSelectedVoucher;
    private TextView mTextViewTerms;
    private View mViewTermsDialog;
    private WebView mWebViewTerms;
    private WebView mWebViewTermsDescription;
    private byte photoBitmap[];
    private ArrayAdapter spinnerAdapter;
    String spinnerData[];
    private ReceiptSubmitionService submitionService;
    private String voucherId;
    private ArrayList voucherList;
    private VoucherListService voucherListService;



/*
    static boolean access$002(FreeVoucherActivity freevoucheractivity, boolean flag)
    {
        freevoucheractivity.isTermsVisible = flag;
        return flag;
    }

*/






/*
    static byte[] access$1302(FreeVoucherActivity freevoucheractivity, byte abyte0[])
    {
        freevoucheractivity.photoBitmap = abyte0;
        return abyte0;
    }

*/








/*
    static ArrayList access$1902(FreeVoucherActivity freevoucheractivity, ArrayList arraylist)
    {
        freevoucheractivity.voucherList = arraylist;
        return arraylist;
    }

*/




/*
    static String access$2002(FreeVoucherActivity freevoucheractivity, String s)
    {
        freevoucheractivity.voucherId = s;
        return s;
    }

*/












}
