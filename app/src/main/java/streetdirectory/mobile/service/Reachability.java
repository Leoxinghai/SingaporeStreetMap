

package streetdirectory.mobile.service;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import streetdirectory.mobile.core.MathTools;
import com.xinghai.mycurve.R;

public class Reachability
{

    public Reachability()
    {
    }

    static class SUBCLASS1 implements android.view.View.OnClickListener {

        public void onClick(View view1)
        {
            ((ViewGroup)v2.getParent()).removeView(v2);
        }

        final View v2;

        SUBCLASS1(View view)
        {
            super();
            v2 = view;
        }
    }

    public static View createNoInternetAccess(Context context)
    {
        View view = View.inflate(context, R.layout.layout_location_warning, null);
        TextView textview = (TextView)view.findViewById(R.id.textViewNotifTitle);
        TextView textview1 = (TextView)view.findViewById(R.id.textViewNotifDesc);
        ImageButton imagebutton = (ImageButton)view.findViewById(R.id.buttonClose);
        Button button = (Button)view.findViewById(R.id.buttonGPSSwitch);
        textview.setText("No internet connection");
        textview1.setText("Please check your network and connection settings.");
        imagebutton.setOnClickListener(new SUBCLASS1(view));
        button.setVisibility(View.INVISIBLE);

        view.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, MathTools.dpToPixel(64F, (Activity)context), 10));
        return view;
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager temp = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return temp.getActiveNetworkInfo() != null && temp.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
