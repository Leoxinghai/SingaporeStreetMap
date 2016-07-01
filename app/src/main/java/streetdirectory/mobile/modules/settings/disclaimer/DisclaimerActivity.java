// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.settings.disclaimer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.SDActivity;

public class DisclaimerActivity extends SDActivity
{

    public DisclaimerActivity()
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_disclaimer);
        mBackButton = (Button)findViewById(R.id.BackButton);
        mBackButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mTextViewLink = (TextView)findViewById(R.id.textView3);
        mTextViewLink.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Object obj = mTextViewLink.getText().toString();
                view = ((View) (obj));
                String temp = null;
                if(!((String) (obj)).startsWith("http://"))
                    temp = (new StringBuilder()).append("http://").append(((String) (obj))).toString();
                obj = new Intent("android.intent.action.VIEW");
                ((Intent) (obj)).setData(Uri.parse(temp));
                startActivity(((Intent) (obj)));
            }

        });
    }

    private Button mBackButton;
    private TextView mTextViewLink;

}
