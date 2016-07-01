

package streetdirectory.mobile.gis;

import java.util.ArrayList;

// Referenced classes of package streetdirectory.mobile.gis:
//            GeoPoint

public class MapZone
{

    public MapZone()
    {
        vertices = new ArrayList();
    }

    public void setBoundary(String s)
    {
        vertices.clear();
        String ss[] = s.split(";");
        if(s != null)
        {
            int j = ss.length;
            for(int i = 0; i < j; i++)
            {
                String as[] = ss[i].split(",");
                if(as != null && as.length > 1)
                {
                    double d = Double.parseDouble(as[0]);
                    double d1 = Double.parseDouble(as[1]);
                    vertices.add(new GeoPoint(d, d1));
                }
            }

        }
    }

    public ArrayList vertices;
}
