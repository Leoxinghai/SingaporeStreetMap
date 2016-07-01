// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.settings.sendfeedback;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import streetdirectory.mobile.core.Action;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.map.MapActivity;
import streetdirectory.mobile.modules.settings.sendfeedback.service.SendFeedbackService;
import streetdirectory.mobile.modules.settings.sendfeedback.service.SendFeedbackServiceInput;
import streetdirectory.mobile.modules.settings.sendfeedback.service.SendFeedbackServiceOutput;
import streetdirectory.mobile.modules.tips.TipsActivity;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;
import streetdirectory.mobile.service.SDHttpServiceOutput;

public class SendFeedbackActivity extends SDActivity
{

    public SendFeedbackActivity()
    {
    }

    private String capitalize(String s)
    {
        String s1;
        if(s == null || s.length() == 0)
        {
            s1 = "";
        } else
        {
            char c = s.charAt(0);
            s1 = s;
            if(!Character.isUpperCase(c))
                return (new StringBuilder()).append(Character.toUpperCase(c)).append(s.substring(1)).toString();
        }
        return s1;
    }

    private void dismissKeyboard()
    {
        View view = getCurrentFocus();
        if(view != null)
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private String getAndroidVersion()
    {
        StringBuilder stringbuilder;
        Field afield[];
        int i;
        int l;
        stringbuilder = new StringBuilder();
        stringbuilder.append("Android ").append(android.os.Build.VERSION.RELEASE);
        afield = Build.VERSION_CODES.class.getFields();
        l = afield.length;
        i = 0;

        Field field;
        String s;
        int j;
        try {
            for (; i < l; ) {
                field = afield[i];
                s = field.getName();
                j = -1;
                int k = field.getInt(new Object());
                j = k;
                if (j == android.os.Build.VERSION.SDK_INT) {
                    stringbuilder.append(" ").append(s).append(" ");
                    stringbuilder.append("sdk=").append(j);
                }
                i++;
            }
        } catch(Exception ex) {

        }

        return stringbuilder.toString();
    }

    private String getAppVersion()
    {
        Object obj = getPackageManager();
        try
        {
            obj = ((PackageManager) (obj)).getPackageInfo(getPackageName(), 0);
            obj = (new StringBuilder()).append(getString(R.string.app_name)).append(" ").append(((PackageInfo) (obj)).versionName).append(" (").append(((PackageInfo) (obj)).versionCode).append(")").toString();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
        return ((String) (obj));
    }

    private String getDeviceName()
    {
        String s = Build.MANUFACTURER;
        String s1 = Build.MODEL;
        if(s1.startsWith(s))
            return capitalize(s1);
        else
            return (new StringBuilder()).append(capitalize(s)).append(" ").append(s1).toString();
    }

    private void setFormEditable(boolean flag)
    {
        mEditTextEmail.setEnabled(flag);
        mEditTextFeedback.setEnabled(flag);
        mButtonSubmit.setEnabled(flag);
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

        }
).create().show();
    }

    private void showResultDialog(boolean flag)
    {
        android.app.AlertDialog.Builder builder = (new android.app.AlertDialog.Builder(this)).setTitle("Send Feedback");
        String s;
        if(flag)
            s = "Feedback posted successfully. Thank You.";
        else
            s = "Failed to post feedback. Please try again later.";
        builder.setMessage(s).setNegativeButton("OK", null).show();
    }

    public void onBackPressed()
    {
        if(mSideMenuLayout.getIsMenuOpen())
        {
            if(mSearchListView.getVisibility() == 0)
            {
                mMenuListView.setVisibility(View.VISIBLE);
                mSearchListView.setVisibility(View.INVISIBLE);
                mgr.hideSoftInputFromWindow(mSearchField.getWindowToken(), 0);
                mSideMenuLayout.requestFocus();
                mSideMenuLayout.slideOpen(77);
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
            mSideMenuLayout.slideOpen();
            return;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_send_feedback);
        mSideMenuLayout = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mSideMenuBlocker = findViewById(R.id.side_menu_blocker);
        mMenuButton = (ImageButton)findViewById(R.id.MenuButton);
        mSearchListView = (ListView)mSideMenuLayout.findViewById(R.id.SearchListView);
        mMenuListView = (ListView)mSideMenuLayout.findViewById(R.id.MenuListView);
        mSearchField = (EditText)mSideMenuLayout.findViewById(R.id.MenuSearchField);
        mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditTextFeedback = (EditText)findViewById(R.id.editText1);
        mEditTextEmail = (EditText)findViewById(R.id.editText2);
        mButtonSubmit = (AppCompatButton)findViewById(R.id.button1);
        mButtonViewAll = (Button)findViewById(R.id.button2);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        mScrollView = (ScrollView)findViewById(R.id.scrollView);
        mButtonSubmit.setEnabled(false);
        mSideMenuLayout.setOnSlideOpen(new Action() {

            public void execute()
            {
                mSideMenuBlocker.setVisibility(View.VISIBLE);
                dismissKeyboard();
            }

        }
);
        mSideMenuLayout.setOnSlideClose(new Action() {

            public void execute()
            {
                mSideMenuBlocker.setVisibility(View.INVISIBLE);
            }

        });
        mSideMenuBlocker.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mSideMenuLayout.touchExecutor(motionevent);
                return true;
            }

        });
        mMenuButton.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mSideMenuLayout.touchExecutor(motionevent);
                return false;
            }

        });
        mMenuButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mSideMenuLayout.getIsMenuOpen())
                {
                    mSideMenuLayout.slideClose();
                    return;
                } else
                {
                    mSideMenuLayout.slideOpen();
                    dismissKeyboard();
                    return;
                }
            }

        });
        mEditTextFeedback.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable)
            {

                boolean flag;
                if(mEditTextFeedback.getText().toString().length() > 0)
                    flag = true;
                else
                    flag = false;
                mButtonSubmit.setEnabled(flag);
            }

            public void beforeTextChanged(CharSequence charsequence, int i, int j, int k)
            {
            }

            public void onTextChanged(CharSequence charsequence, int i, int j, int k)
            {
            }

        });
        mButtonSubmit.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mProgressBar.setVisibility(View.VISIBLE);
                setFormEditable(false);
                String temp = mEditTextFeedback.getText().toString();
                String s = mEditTextEmail.getText().toString();
                String s1 = (new StringBuilder()).append(getDeviceName()).append(", ").append(getAppVersion()).append(", ").append(getAndroidVersion()).toString();
                (new SendFeedbackService(new SendFeedbackServiceInput(SDBlackboard.currentCountryCode, temp, s1, "", s)) {

                    public void onFailed(Exception exception)
                    {
                        super.onFailed(exception);
                        setFormEditable(true);
                        mProgressBar.setVisibility(View.INVISIBLE);
                        showResultDialog(false);
                    }

                    public void onSuccess(Object obj)
                    {
                        onSuccess((SDHttpServiceOutput)obj);
                    }

                    public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                    {
                        super.onSuccess(sdhttpserviceoutput);
                        setFormEditable(true);
                        mProgressBar.setVisibility(View.INVISIBLE);
                        SendFeedbackActivity sendfeedbackactivity = SendFeedbackActivity.this;
                        boolean flag;
                        if(sdhttpserviceoutput.childs.size() > 0 && ((SendFeedbackServiceOutput)sdhttpserviceoutput.childs.get(0)).result)
                            flag = true;
                        else
                            flag = false;
                        sendfeedbackactivity.showResultDialog(flag);
                    }

                }
).executeAsync();
            }

        });
        mButtonViewAll.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(SendFeedbackActivity.this, TipsActivity.class);
                intent.putExtra("countryCode", SDBlackboard.currentCountryCode);
                intent.putExtra("companyID", 0x1ad15);
                intent.putExtra("locationID", 0x2a17f);
                intent.putExtra("venue", "Streetdirectory Pte Ltd");
                intent.putExtra("type", 2);
                startActivity(intent);
            }

        });
        mScrollView.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                if(motionevent.getAction() == 2)
                {
                    dismissKeyboard();
                    return true;
                } else
                {
                    return false;
                }
            }

        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(mSideMenuLayout.getIsMenuOpen())
            mSideMenuLayout.slideClose();
        else
            mSideMenuLayout.slideOpen();
        return false;
    }

    protected void onPause()
    {
        super.onPause();
        dismissKeyboard();
    }

    private AppCompatButton mButtonSubmit;
    private Button mButtonViewAll;
    private EditText mEditTextEmail;
    private EditText mEditTextFeedback;
    private ImageButton mMenuButton;
    private ListView mMenuListView;
    private ProgressBar mProgressBar;
    private ScrollView mScrollView;
    private EditText mSearchField;
    private ListView mSearchListView;
    private View mSideMenuBlocker;
    private SDMapSideMenuLayout mSideMenuLayout;
    private InputMethodManager mgr;


}
