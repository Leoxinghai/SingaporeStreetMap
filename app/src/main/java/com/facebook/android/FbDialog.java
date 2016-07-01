

package com.facebook.android;

import android.content.Context;
import android.os.Bundle;
import com.facebook.*;
import com.facebook.widget.WebDialog;

// Referenced classes of package com.facebook.android:
//            DialogError, FacebookError

public class FbDialog extends WebDialog
{

    public FbDialog(Context context, String s, Bundle bundle, Facebook.DialogListener dialoglistener)
    {
        super(context, s, bundle, 0x1030010, null);
        setDialogListener(dialoglistener);
    }

    public FbDialog(Context context, String s, Bundle bundle, Facebook.DialogListener dialoglistener, int i)
    {
        super(context, s, bundle, i, null);
        setDialogListener(dialoglistener);
    }

    public FbDialog(Context context, String s, Facebook.DialogListener dialoglistener)
    {
        this(context, s, dialoglistener, 0x1030010);
    }

    public FbDialog(Context context, String s, Facebook.DialogListener dialoglistener, int i)
    {
        super(context, s, i);
        setDialogListener(dialoglistener);
    }

    private void callDialogListener(Bundle bundle, FacebookException facebookexception)
    {
        if(mListener == null)
            return;
        if(bundle != null)
        {
            mListener.onComplete(bundle);
            return;
        }
        if(facebookexception instanceof FacebookDialogException)
        {
            FacebookDialogException ex = (FacebookDialogException)facebookexception;
            mListener.onError(new DialogError(ex.getMessage(), ex.getErrorCode(), ex.getFailingUrl()));
            return;
        }
        if(facebookexception instanceof FacebookOperationCanceledException)
        {
            mListener.onCancel();
            return;
        } else
        {
            mListener.onFacebookError(new FacebookError(facebookexception.getMessage()));
            return;
        }
    }

    private void setDialogListener(Facebook.DialogListener dialoglistener)
    {
        mListener = dialoglistener;
        setOnCompleteListener(new com.facebook.widget.WebDialog.OnCompleteListener() {

            public void onComplete(Bundle bundle, FacebookException facebookexception)
            {
                callDialogListener(bundle, facebookexception);
            }
        });
    }

    private Facebook.DialogListener mListener;

}
