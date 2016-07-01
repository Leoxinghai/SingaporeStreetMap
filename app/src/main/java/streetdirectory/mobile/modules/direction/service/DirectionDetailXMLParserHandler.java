// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.direction.service;

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

// Referenced classes of package streetdirectory.mobile.modules.direction.service:
//            DirectionDetailServiceOutput

public class DirectionDetailXMLParserHandler extends SDDatasetDataXMLHandler
{

    public DirectionDetailXMLParserHandler()
    {
    }

    public void endElement(String s, String s1, String s2)
        throws SAXException
    {
        if(_currentData != null) {
			if(_buffer != null) {
				int j;
				int i = currentLongLatMode;
				j = currentLongLatMode;
				if(s2.equals("x") && i == 0) {
					try
					{
						currentGeoPoint.longitude = Double.parseDouble(_buffer.toString());
					}
					// Misplaced declaration of an exception variable
					catch(Exception ex)
					{
						currentGeoPoint.longitude = 0.0D;
					}
				} else if(s2.equals("y") && j == 0) {
					try {
						currentGeoPoint.latitude = Double.parseDouble(_buffer.toString());
					} catch(Exception ex2) {
						currentGeoPoint.latitude = 0.0D;
					}
				}
			}


			if(s2.equals("dataset")) {
				((DirectionDetailServiceOutput)_currentData).populateData();
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
			} else if(s2.equals("carpark"))
			{
				currentCarpark.populateData();
				((DirectionDetailServiceOutput)_currentData).arrayOfCarpark.add(currentCarpark);
				currentCarpark = null;
				currentHashData = 0;
			} else if(s2.equals("erp"))
			{
				currentERPInfo.populateData();
				currentArrayOfERPInfo.add(currentERPInfo);
				currentERPInfo = null;
				currentHashData = 0;
			} else if(s2.equals("erp_data"))
			{
				((DirectionDetailServiceOutput)_currentData).arrayOfArrayOfERPInfo.add(currentArrayOfERPInfo);
				currentArrayOfERPInfo = null;
			} else if(s2.equals("fare"))
			{
				currentFare.populateData();
				((DirectionDetailServiceOutput)_currentData).arrayOfTaxiFare.add(currentFare);
				currentFare = null;
				currentHashData = 0;
			} else if(s2.equals("road"))
			{
				currentSummary.populateData();
				((DirectionDetailServiceOutput)_currentData).arrayOfSummary.add(currentSummary);
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
				((DirectionDetailServiceOutput)_currentData).arrayOfArrayOfPointDetail.add(currentArrayOfPointDetail);
				currentArrayOfPointDetail = null;
			} else if(s2.equals("route"))
			{
				((DirectionDetailServiceOutput)_currentData).arrayOfRoutes.add(currentRoute);
				currentRoute = null;
				currentLongLatMode = 1;
			} else if(s2.equals("taxi_time"))
			{
				((DirectionDetailServiceOutput)_currentData).taxiSummary.populateData();
				currentHashData = 0;
			} else if(s2.equals("sub")) {
				if(currentStartEnd != 0)
					((DirectionDetailServiceOutput)_currentData).end.populateData();
				else
					((DirectionDetailServiceOutput)_currentData).start.populateData();
				currentStartEnd = currentStartEnd + 1;
			}






			boolean flag = s2.equals("color");
			if(flag) {
				currentLine.color = Color.parseColor(_buffer.toString());
			}



			if(currentHashData == 0)
			{
				((DirectionDetailServiceOutput)_currentData).hashData.put(s2, _buffer.toString());
			}
			if(currentHashData == 1)
			{
				currentCarpark.hashData.put(s2, _buffer.toString());
			}
			if(currentHashData == 2)
			{
				currentERPInfo.hashData.put(s2, _buffer.toString());
			}
			if(currentHashData == 3)
			{
				currentFare.hashData.put(s2, _buffer.toString());
			}
			if(currentHashData == 4)
			{
				currentSummary.hashData.put(s2, _buffer.toString());
			}
			if(currentHashData == 5)
			{
				currentPointDetail.hashData.put(s2, _buffer.toString());
			}
			if(currentHashData == 6)
			{
				((DirectionDetailServiceOutput)_currentData).start.hashData.put(s2, _buffer.toString());
			}
			if(currentHashData == 7)
			{
				((DirectionDetailServiceOutput)_currentData).end.hashData.put(s2, _buffer.toString());
			}
			if(currentHashData == 8)
				((DirectionDetailServiceOutput)_currentData).taxiSummary.hashData.put(s2, _buffer.toString());

		}

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
	        //SDLogger.debug((new StringBuilder()).append("color error = ").append(s.getMessage()).toString());
        }

        _buffer = null;
        return;
    }

    public void startElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        boolean flag = s2.equals("dataset");

		try
		{
		if(flag) {
				long l = Long.parseLong(attributes.getValue("total"));
				_output.total = l;
				onReceiveTotal(l);
				_currentData = new DirectionDetailServiceOutput();
        } else if(s2.equals("lines")) {
            currentLine = new JourneyLine();
        } else if(s2.equals("pt")) {
            currentLongLatMode = 0;
            currentGeoPoint = new GeoPoint();
        } else if(s2.equals("start_end")) {
            currentLongLatMode = 1;
            currentGeoPoint = new GeoPoint();
        } else if(s2.equals("pts")) {
            currentLine.arrayOfPoints = new ArrayList();
        } else if(s2.equals("carparks")) {
            ((DirectionDetailServiceOutput)_currentData).arrayOfCarpark = new ArrayList();
        } else if(s2.equals("carpark")) {
            currentHashData = 1;
            currentCarpark = new Carpark();
        } else if(s2.equals("erp_datas")) {
            ((DirectionDetailServiceOutput)_currentData).arrayOfArrayOfERPInfo = new ArrayList();
        } else if(s2.equals("erp_data")) {
            currentArrayOfERPInfo = new ArrayList();
        } else if(s2.equals("erp")) {
            currentHashData = 2;
            currentERPInfo = new JourneyERPInfo();
        } else if(s2.equals("taxi_fare")) {
            ((DirectionDetailServiceOutput)_currentData).arrayOfTaxiFare = new ArrayList();
        } else if(s2.equals("fare")) {
            currentHashData = 3;
            currentFare = new JourneyFare();
        } else if(s2.equals("roads")) {
            ((DirectionDetailServiceOutput)_currentData).arrayOfSummary = new ArrayList();
        } else if(s2.equals("road")) {
            currentHashData = 4;
            currentSummary = (JourneySummary)JourneySummary.class.newInstance();
        } else if(s2.equals("journey")) {
            ((DirectionDetailServiceOutput)_currentData).arrayOfArrayOfPointDetail = new ArrayList();
        } else if(s2.equals("datas")) {
            currentArrayOfPointDetail = new ArrayList();
        } else if(s2.equals("data")) {
            currentHashData = 5;
            currentPointDetail = (JourneyPointDetail)JourneyPointDetail.class.newInstance();
        } else if(s2.equals("route")) {
            currentRoute = new JourneyRoute();
            currentRoute.arrayOfLines = new ArrayList();
        } else if(s2.equals("routes")) {
            ((DirectionDetailServiceOutput)_currentData).arrayOfRoutes = new ArrayList();
        } else if(s2.equals("start_end")) {
            currentStartEnd = 0;
        } else if(s2.equals("taxi_time")) {
            currentHashData = 8;
            ((DirectionDetailServiceOutput)_currentData).taxiSummary = new JourneyTaxiSummary();
        } else if(s2.equals("sub")) {
            if(currentStartEnd != 0) {
				currentHashData = 7;
				((DirectionDetailServiceOutput)_currentData).end = new JourneyPointDetail();
			} else {
				currentHashData = 6;
				((DirectionDetailServiceOutput)_currentData).start = new JourneyPointDetail();
			}
        } else if(s2.equals("error")) {
			currentError = new SDErrorOutput();
	        throw new SAXException(s);
		}
		}
		catch(Exception ex)
		{
			throw new SAXException(s);
		}
		_buffer = new StringBuilder();
		return;

    }

    private static final int GENERAL_LONGLAT = 1;
    private static final int HASH_DATA_CARPARK = 1;
    private static final int HASH_DATA_END = 7;
    private static final int HASH_DATA_ERP = 2;
    private static final int HASH_DATA_FARE = 3;
    private static final int HASH_DATA_MAIN = 0;
    private static final int HASH_DATA_POINT_DETAIL = 5;
    private static final int HASH_DATA_START = 6;
    private static final int HASH_DATA_SUMMARY = 4;
    private static final int HASH_DATA_TAXI_SUMMARY = 8;
    private static final int LINE_POINT_LONGLAT = 0;
    private ArrayList currentArrayOfERPInfo;
    private ArrayList currentArrayOfPointDetail;
    private Carpark currentCarpark;
    private JourneyERPInfo currentERPInfo;
    private JourneyFare currentFare;
    private GeoPoint currentGeoPoint;
    private int currentHashData;
    private JourneyLine currentLine;
    private int currentLongLatMode;
    private JourneyPointDetail currentPointDetail;
    private JourneyRoute currentRoute;
    private int currentStartEnd;
    private JourneySummary currentSummary;
}
