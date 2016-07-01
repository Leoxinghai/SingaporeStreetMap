// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.bus.service;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.service.SAXParserAbortException;
import streetdirectory.mobile.core.service.SAXParserStopParsingException;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.modules.direction.*;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.bus.service:
//            BusRoutesServiceOutput, BusRouteSummary

public class BusRouteXMLParserHandler extends SDDatasetDataXMLHandler
{

    public BusRouteXMLParserHandler()
    {
    }

    public void endElement(String s, String s1, String s2)
        throws SAXException
    {
        if(_currentData == null) {
			if(currentError != null)
			{
				if(_buffer != null)
					currentError.hashData.put(s2, _buffer.toString());
				if("error".equals(s2))
				{
					currentError.populateData();
					onReceiveError(currentError);
					currentError = null;
					throw new SAXParserStopParsingException();
				}
			}
			_buffer = null;
			return;
		}


		int j;
        if(_buffer != null) {
			int i = currentLongLatMode;
			if(s2.equals("x") && i == 0) {
				try
				{
					currentGeoPoint.longitude = Double.parseDouble(_buffer.toString());
				}
				catch(Exception ex)
				{
					currentGeoPoint.longitude = 0.0D;
					throw new SAXParserAbortException();
				}
			}  else {
				j = currentLongLatMode;
				if(s2.equals("y") && j == 0) {
					currentGeoPoint.latitude = Double.parseDouble(_buffer.toString());
				} else {
					currentGeoPoint.latitude = 0.0D;
				}
			}

		}


        if(s2.equals("dataset")) {
			((BusRoutesServiceOutput)_currentData).populateData();
			onReceiveData(_currentData);
			_output.childs.add(_currentData);
			_currentData = null;
		} else if(s2.equals("pt"))
        {
            currentLine.addNewPoint(currentGeoPoint);
            currentGeoPoint = null;
        } else if(s2.equals("lines"))
        {
            if(currentLine != null)
            {
                currentRoute.addNewLines(currentLine);
                currentLine = null;
            }
        } else if(s2.equals("road"))
        {
            currentSummary.populateData();
            ((BusRoutesServiceOutput)_currentData).arrayOfSummary.add(currentSummary);
            currentSummary = null;
            currentHashData = 0;
        } else if(s2.equals("data"))
        {
            currentPointDetail.populateData();
            currentArrayOfPointDetail.add(currentPointDetail);
            currentPointDetail = null;
            currentHashData = 0;
        } else if(s2.equals("datas"))
        {
            ((BusRoutesServiceOutput)_currentData).arrayOfArrayOfPointDetail.add(currentArrayOfPointDetail);
            currentArrayOfPointDetail = null;
        } else if(s2.equals("route"))
        {
            ((BusRoutesServiceOutput)_currentData).arrayOfRoutes.add(currentRoute);
            currentRoute = null;
            currentLongLatMode = 1;
        } else if(s2.equals("start_end"))
        {
            currentLongLatMode = 1;
        } else if(s2.equals("sub")) {
			if(currentStartEnd != 0)
				((BusRoutesServiceOutput)_currentData).end.populateData();
			else
				((BusRoutesServiceOutput)_currentData).start.populateData();

			currentStartEnd = currentStartEnd + 1;
		}



        if( s2.equals("color")) {
            currentLine.color = Color.parseColor(_buffer.toString());
        } else {
            //SDLogger.debug((new StringBuilder()).append("color error = ").append(s.getMessage()).toString());
        }

        if(currentHashData == 0)
        {
            ((BusRoutesServiceOutput)_currentData).hashData.put(s2, _buffer.toString());
        } else if(currentHashData == 4)
        {
            currentSummary.hashData.put(s2, _buffer.toString());
        } else if(currentHashData == 5)
        {
            currentPointDetail.hashData.put(s2, _buffer.toString());
        } else if(currentHashData == 6)
        {
            ((BusRoutesServiceOutput)_currentData).start.hashData.put(s2, _buffer.toString());
        } else if(currentHashData == 7)
            ((BusRoutesServiceOutput)_currentData).end.hashData.put(s2, _buffer.toString());


        _buffer = null;
        return;

    }

    public void startElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        try
        {
        boolean flag = s2.equals("dataset");
		if(flag) {
				long l = Long.parseLong(attributes.getValue("total"));
				_output.total = l;
				onReceiveTotal(l);
				_currentData = new BusRoutesServiceOutput();
            _buffer = new StringBuilder();
            return;
        } else  if(s2.equals("lines")) {
            currentLine = new JourneyLine();
        } else if(s2.equals("pt")) {
            currentLongLatMode = 0;
            currentGeoPoint = new GeoPoint();
        } else if(s2.equals("pts")) {
            currentLine.arrayOfPoints = new ArrayList();
        } else if(s2.equals("roads")) {
            ((BusRoutesServiceOutput)_currentData).arrayOfSummary = new ArrayList();
        } else if(s2.equals("road")) {
            currentHashData = 4;
            currentSummary = (BusRouteSummary)BusRouteSummary.class.newInstance();
        } else if(s2.equals("journey")) {
            ((BusRoutesServiceOutput)_currentData).arrayOfArrayOfPointDetail = new ArrayList();
        } else if(s2.equals("datas")) {
            currentArrayOfPointDetail = new ArrayList();
        } else if(s2.equals("data")) {
            currentHashData = 5;
            currentPointDetail = (JourneyPointDetail)JourneyPointDetail.class.newInstance();
        } else if(s2.equals("route")) {
            currentRoute = new JourneyRoute();
            currentRoute.arrayOfLines = new ArrayList();
        } else if(s2.equals("routes")) {
            ((BusRoutesServiceOutput)_currentData).arrayOfRoutes = new ArrayList();
        } else if(s2.equals("start_end")) {
            currentStartEnd = 0;
        } else if(s2.equals("sub")) {
            if(currentStartEnd != 0) {
				currentHashData = 7;
				((BusRoutesServiceOutput)_currentData).end = new JourneyPointDetail();
			} else {
				currentHashData = 6;
				((BusRoutesServiceOutput)_currentData).start = new JourneyPointDetail();
			}
        } else if(s2.equals("error")) {
			currentError = new SDErrorOutput();
	        throw new SAXException(s);
		}
        }
        catch(Exception ex) { }

    }

    private static final int GENERAL_LONGLAT = 1;
    private static final int HASH_DATA_END = 7;
    private static final int HASH_DATA_MAIN = 0;
    private static final int HASH_DATA_POINT_DETAIL = 5;
    private static final int HASH_DATA_START = 6;
    private static final int HASH_DATA_SUMMARY = 4;
    private static final int LINE_POINT_LONGLAT = 0;
    private ArrayList currentArrayOfPointDetail;
    private GeoPoint currentGeoPoint;
    private int currentHashData;
    private JourneyLine currentLine;
    private int currentLongLatMode;
    private JourneyPointDetail currentPointDetail;
    private JourneyRoute currentRoute;
    private int currentStartEnd;
    private BusRouteSummary currentSummary;
}
