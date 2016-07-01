// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.settings;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.facebook.*;
import com.facebook.model.GraphUser;
import com.xinghai.mycurve.R;

import streetdirectory.mobile.service.SDHttpImageServicePool;
import streetdirectory.mobile.service.URLFactory;

// Referenced classes of package streetdirectory.mobile.modules.settings:
//            SettingsTableData, SettingsAdapter

public class FacebookTableData extends SettingsTableData
{

    public FacebookTableData(Context context, SettingsAdapter settingsadapter)
    {
        super(context);
        imagePool = new SDHttpImageServicePool();
        adapter = null;
        adapter = settingsadapter;
        initFacebook();
    }

    private void initFacebook()
    {
        Session session = Session.getActiveSession();
        if(session != null)
        {
            if(session.isOpened())
                requestGraphUser(session);
            return;
        } else
        {
            setDefaultValue();
            return;
        }
    }

    private void requestGraphUser(Session session)
    {
        Request.executeMeRequestAsync(session, new com.facebook.Request.GraphUserCallback() {

            public void onCompleted(GraphUser graphuser, Response response)
            {
                if(graphuser != null)
                {
                    mTitle = graphuser.getName();
                    mDetail = "Tap to logout";
                    String temp = URLFactory.createURLFacebookPhoto(graphuser.getId(), 48, 48);
                    imagePool.queueRequest(temp, 48, 48, new streetdirectory.mobile.service.SDHttpImageServicePool.OnBitmapReceivedListener() {

                        public void bitmapReceived(String s, Bitmap bitmap)
                        {
                            mImageIcon = bitmap;
                            adapter.notifyDataSetChanged();
                        }

                    });
                }
                adapter.notifyDataSetChanged();
            }

        });
    }

    private void setDefaultValue()
    {
        mImageIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_account);
        mTitle = "Login to My Account";
        mDetail = "Save favorites and win prizes";
    }

    public void execute()
    {
        Session session = Session.getActiveSession();
        if(session != null && !session.isClosed()) {
			if(Session.getActiveSession() != null && Session.getActiveSession().isOpened())
			{
				android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
				builder.setTitle("Logout");
				builder.setMessage("Logout from your streetdirectory account?");
				builder.setCancelable(false);
				builder.setPositiveButton("Logout Now", new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialoginterface, int i)
					{
						if(Session.getActiveSession() != null)
						{
							Session.getActiveSession().closeAndClearTokenInformation();
							setDefaultValue();
							adapter.notifyDataSetChanged();
						}
					}

				});
				builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialoginterface, int i)
					{
						dialoginterface.cancel();
						adapter.notifyDataSetChanged();
					}

				});
				builder.create();
				builder.show();
			}
		} else {
				Session.openActiveSession((Activity)mContext, true, new com.facebook.Session.StatusCallback() {

					public void call(Session session1, SessionState sessionstate, Exception exception)
					{
						requestGraphUser(session1);
					}

				}
		);
		}
        super.execute();
        return;
    }

    SettingsAdapter adapter;
    SDHttpImageServicePool imagePool;


}
