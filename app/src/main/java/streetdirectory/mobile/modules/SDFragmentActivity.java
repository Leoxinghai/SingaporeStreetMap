

package streetdirectory.mobile.modules;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.GantTools;

public class SDFragmentActivity extends FragmentActivity
{

    public SDFragmentActivity()
    {
    }

    protected void abortAllProcess()
    {
    }

    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.side_menu_a, R.anim.side_menu_b);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        GantTools.startSession(getApplication());
    }

    protected void onDestroy()
    {
        GantTools.stopSession();
        abortAllProcess();
        super.onDestroy();
    }

    protected void onStart()
    {
        super.onStart();
    }

    protected void onStop()
    {
        super.onStop();
    }

    public void startActivity(Intent intent)
    {
        super.startActivity(intent);
        overridePendingTransition(R.anim.side_menu_a, R.anim.side_menu_b);
    }
}
