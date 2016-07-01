

package streetdirectory.mobile.modules.settings.freemaps;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.SDActivity;

public class FreeMapsActivity extends SDActivity
{

    public FreeMapsActivity()
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_free_maps);
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
