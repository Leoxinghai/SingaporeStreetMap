// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.closing;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.SDActivity;

public class ClosingActivity extends SDActivity
{

    public ClosingActivity()
    {
        mSecondsToGo = 1;
    }

    protected void onCreate(Bundle bundle)
    {
        onCreate(bundle);
        setContentView(R.layout.activity_closing);
        mTextViewMessage = (TextView)findViewById(R.id.text_view_message);
        mTextViewSecond = (TextView)findViewById(R.id.text_view_second);
        mButtonClose = (AppCompatButton)findViewById(R.id.button_close);
        String temp = (new StringBuilder()).append("You are leaving\n").append(getString(R.string.app_name)).toString();
        mTextViewMessage.setText(temp);
        mButtonClose.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        (new CountDownTimer(1400L, 1000L) {

            public void onFinish()
            {
                finish();
            }

            public void onTick(long l) {
                mTextViewSecond.setText(String.valueOf(mSecondsToGo));
//                int i =
// JavaClassFileOutputException: get_constant: invalid tag
            }

        }).start();
    }

    private AppCompatButton mButtonClose;
    private int mSecondsToGo;
    private TextView mTextViewMessage;
    private TextView mTextViewSecond;


}
