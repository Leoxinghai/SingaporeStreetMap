

package streetdirectory.mobile.modules;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.GantTools;
import streetdirectory.mobile.gis.gps.LocationService;

public class SDActivity extends AppCompatActivity
{

    public SDActivity()
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

    protected void onPause()
    {
        isLocationEnable = LocationService.getInstance().isEnable;
        LocationService.getInstance().disable();
        super.onPause();
    }

    protected void onResume()
    {
        if(isLocationEnable)
            LocationService.getInstance().enable();
        isLocationEnable = false;
        super.onResume();
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

    private static boolean isLocationEnable = false;

}
