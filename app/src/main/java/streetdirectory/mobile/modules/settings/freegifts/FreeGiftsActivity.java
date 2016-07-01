

package streetdirectory.mobile.modules.settings.freegifts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.SDActivity;

public class FreeGiftsActivity extends SDActivity
{

    public FreeGiftsActivity()
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_free_gift);
        mBackButton = (Button)findViewById(R.id.BackButton);
        mBackButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
    }

    private Button mBackButton;
}
