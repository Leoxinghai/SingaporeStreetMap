

package streetdirectory.mobile.modules.direction.service;

import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import streetdirectory.mobile.core.service.SAXParserAbortException;
import streetdirectory.mobile.core.service.SAXParserStopParsingException;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.direction.service:
//            DirectionOverviewServiceOutput

public class DirectionOverviewXMLParserHandler extends SDDatasetDataXMLHandler
{

    public DirectionOverviewXMLParserHandler()
    {
        super(DirectionOverviewServiceOutput.class);
    }

    public void endElement(String s, String s1, String s2)
        throws SAXException
    {
        if(_currentData == null) {
			if(currentError != null) {

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


        if(_buffer != null) {

			if(s2.equals("total_length")) {
				if(currentMode == 0)
					((DirectionOverviewServiceOutput)_currentData).hashData.put("drive_distance", _buffer.toString());
			} else if(s2.equals("total_time")) {
				if(currentMode == 0) {
					((DirectionOverviewServiceOutput)_currentData).hashData.put("drive_time", _buffer.toString());
				} else if(currentMode == 1) {
					((DirectionOverviewServiceOutput)_currentData).hashData.put("taxi_time", _buffer.toString());
				} else if(currentMode == 2) {
					((DirectionOverviewServiceOutput)_currentData).hashData.put("bus_time", _buffer.toString());
				} else if(currentMode == 3) {
					((DirectionOverviewServiceOutput)_currentData).hashData.put("bustrain_time", _buffer.toString());
				}
			} else if(s2.equals("total_fare")) {
				if(currentMode == 2)
					((DirectionOverviewServiceOutput)_currentData).hashData.put("bus_fare", _buffer.toString());
				else if(currentMode == 3)
					((DirectionOverviewServiceOutput)_currentData).hashData.put("bustrain_fare", _buffer.toString());
			} else if(s2.equals("label")) {
				if(!_buffer.toString().equals("Total Fare"))
					taxiTotalFare = false;
				else
					taxiTotalFare = true;
			} else if(s2.equals("value") && taxiTotalFare) {
				((DirectionOverviewServiceOutput)_currentData).hashData.put("taxi_fare", _buffer.toString());
			}


		}

        if(s2.equals("dataset"))
        {
            ((DirectionOverviewServiceOutput)_currentData).populateData();
            onReceiveData(_currentData);
            _output.childs.add(_currentData);
            _currentData = null;
        }

        _buffer = null;
        return;
    }

    public void startElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        boolean flag = s2.equals("dataset");
		boolean flag1;
		if(flag) {
			try
			{
				long l = Long.parseLong(attributes.getValue("total"));
				_output.total = l;
				onReceiveTotal(l);
			}
			// Misplaced declaration of an exception variable
			catch(Exception ex) { }
			try
			{
				_currentData = new DirectionOverviewServiceOutput();
			}
			// Misplaced declaration of an exception variable
			catch(Exception ex)
			{
				if(_isCanceled)
					throw new SAXParserAbortException();
				else
					throw new SAXException(s);
			}
		} else if(s2.equals("driving"))
				currentMode = 0;
		else if(s2.equals("taxi"))
			currentMode = 1;
		else if(s2.equals("bus"))
			currentMode = 2;
		else if(s2.equals("bustrain"))
			currentMode = 3;

        flag1 = s2.equals("error");
        if(flag1)
	        currentError = new SDErrorOutput();

		_buffer = new StringBuilder();
		return;

    }

    private static final int MODE_BUS = 2;
    private static final int MODE_BUSTRAIN = 3;
    private static final int MODE_DRIVING = 0;
    private static final int MODE_TAXI = 1;
    private int currentMode;
    private boolean taxiTotalFare;
}
