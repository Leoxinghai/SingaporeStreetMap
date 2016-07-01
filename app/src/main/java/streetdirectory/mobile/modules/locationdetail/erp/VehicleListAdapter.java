

package streetdirectory.mobile.modules.locationdetail.erp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.Collection;
import java.util.Iterator;
import streetdirectory.mobile.modules.locationdetail.erp.service.ERPVehicleInfo;

public class VehicleListAdapter extends ArrayAdapter
{

    public VehicleListAdapter(Context context)
    {
        super(context, 0x1090008);
        setDropDownViewResource(0x1090003);
    }

    public void addAll(Collection collection)
    {
        if(android.os.Build.VERSION.SDK_INT >= 11)
        {
            super.addAll(collection);
        } else
        {
            Iterator iterator = collection.iterator();
            while(iterator.hasNext())
                add((ERPVehicleInfo)iterator.next());
        }
    }

    public View getDropDownView(int i, View view, ViewGroup viewgroup)
    {
        view = super.getDropDownView(i, view, viewgroup);
        if(view != null && (view instanceof TextView))
            ((TextView)view).setText(((ERPVehicleInfo)getItem(i)).vehicleName);
        return view;
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        view = super.getView(i, view, viewgroup);
        if(view != null && (view instanceof TextView))
            ((TextView)view).setText(((ERPVehicleInfo)getItem(i)).vehicleName);
        return view;
    }
}
