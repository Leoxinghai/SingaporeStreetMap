// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.offlinemap.service;

import java.util.*;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.gis.maps.configs.*;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.service.SDDataOutput;

public class OfflineMapPackageDetailServiceOutput extends SDDataOutput
{

    public OfflineMapPackageDetailServiceOutput()
    {
    }

    public static MapPresetLevelMap getMapPresetLevelMap(String s)
    {
        return getMapPresetLevelMap(s, 0);
    }

    public static MapPresetLevelMap getMapPresetLevelMap(String s, int i) {
        if (SDBlackboard.preset == null)
            return null;

        int j = SDBlackboard.preset.levels.size();
        try {
            for (; i < j; ) {
                Iterator iterator = ((MapPresetLevel) SDBlackboard.preset.levels.get(i)).maps.iterator();
                MapPresetLevelMap mappresetlevelmap;
                boolean flag;
                for (; iterator.hasNext(); ) {
                    mappresetlevelmap = (MapPresetLevelMap) iterator.next();
                    flag = mappresetlevelmap.connectionString.equalsIgnoreCase(s);
                    if (flag)
                        return mappresetlevelmap;
                }
                i++;
            }
        } catch (Exception ex) {
            SDLogger.printStackTrace(ex, "OfflineMapPackageDetailServiceOutput populate maxCol maxRow");
        }
        return null;
    }

    public void populateData()
    {
        super.populateData();
        try
        {
            level = Integer.parseInt((String)hashData.get("level"));
        }
        catch(Exception exception)
        {
            level = 0;
        }
        connectionString = (String)hashData.get("connection_string");
        filemode = (String)hashData.get("filemode");
        try
        {
            size = Double.parseDouble((String)hashData.get("size"));
        }
        catch(Exception exception1)
        {
            size = 0.0D;
        }
        try
        {
            minRow = Integer.parseInt((String)hashData.get("min_row"));
        }
        catch(Exception exception2)
        {
            minRow = 0;
        }
        try
        {
            maxRow = Integer.parseInt((String)hashData.get("max_row"));
        }
        catch(Exception exception3)
        {
            maxRow = -1;
        }
        try
        {
            minCol = Integer.parseInt((String)hashData.get("min_col"));
        }
        catch(Exception exception4)
        {
            minCol = 0;
        }
        try
        {
            maxCol = Integer.parseInt((String)hashData.get("max_col"));
        }
        catch(Exception exception5)
        {
            maxCol = -1;
        }
        if(maxRow == -1 || maxCol == -1)
        {
            MapfileScale mapfilescale = getMapPresetLevelMap(connectionString).scale;
            if(mapfilescale != null)
            {
                if(maxRow == -1)
                {
                    hashData.put("max_row", (new StringBuilder()).append(mapfilescale.maxRow).append("").toString());
                    maxRow = mapfilescale.maxRow;
                }
                if(maxCol == -1)
                {
                    hashData.put("max_col", (new StringBuilder()).append(mapfilescale.maxCol).append("").toString());
                    maxCol = mapfilescale.maxCol;
                }
            }
        }
        totalTiles = ((maxCol - minCol) + 1) * ((maxRow - minRow) + 1);
    }

    public static final streetdirectory.mobile.service.SDOutput.Creator CREATOR = new streetdirectory.mobile.service.SDOutput.Creator(OfflineMapPackageDetailServiceOutput.class);
    private static final long serialVersionUID = 0xa41e5e6680ecd4c7L;
    public String connectionString;
    public String filemode;
    public int level;
    public int maxCol;
    public int maxRow;
    public int minCol;
    public int minRow;
    public double size;
    public int totalTiles;

}
