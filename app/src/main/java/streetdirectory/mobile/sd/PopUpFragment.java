

package streetdirectory.mobile.sd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.Button;
import android.widget.CheckBox;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.SDPreferences;

public class PopUpFragment extends Fragment
{

    public PopUpFragment()
    {
    }

    private void initEvent()
    {
        buttonClose.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                android.content.SharedPreferences.Editor temp = SDPreferences.getInstance().createEditor();
                if(checkBoxDontShow.isChecked())
                {
                    temp.putInt("DONT_SHOW", 1);
                    return;
                } else
                {
                    temp.putInt("DONT_SHOW", 0);
                    return;
                }
            }

        }
);
    }

    private void initLayout(View view)
    {
        buttonClose = (Button)view.findViewById(R.id.buttonOk);
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        View lview = layoutinflater.inflate(R.layout.layout_popup_message, viewgroup);
        initLayout(lview);
        initEvent();
        return lview;
    }

    private Button buttonClose;
    private CheckBox checkBoxDontShow;

}
