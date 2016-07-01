// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.erp.service;

import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import streetdirectory.mobile.core.service.SAXParserAbortException;
import streetdirectory.mobile.core.service.SAXParserStopParsingException;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.erp.service:
//            ERPDetailServiceOutput, ERPVehicleInfo, ERPTimeRateInfo

public class ERPDetailXMLParserHandler extends SDDatasetDataXMLHandler
{

    public ERPDetailXMLParserHandler()
    {
        super(ERPDetailServiceOutput.class);
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

			if(s2.equals("name")) {
				currentERPVehicleInfo.vehicleName = _buffer.toString();
			} else if(s2.equals("start_time")) {
				currentTimeRateInfo.startTime = _buffer.toString();
			} else if(s2.equals("end_time")) {
				currentTimeRateInfo.endTime = _buffer.toString();
			} else if(s2.equals("amount")) {
				currentTimeRateInfo.amount = _buffer.toString();
			}
		}


        if(s2.equals("sub")) {
			currentArrayOfTimeRateInfo.add(currentTimeRateInfo);
			currentTimeRateInfo = null;
		} else if(s2.equals("weekdays")) {
			currentERPVehicleInfo.arrayOfWeekdaysInfo.addAll(currentArrayOfTimeRateInfo);
			currentArrayOfTimeRateInfo = null;
		} else if(s2.equals("saturday")) {
			currentERPVehicleInfo.arrayOfWeekendInfo.addAll(currentArrayOfTimeRateInfo);
			currentArrayOfTimeRateInfo = null;
		} else if(s2.equals("vehicle_type_1")) {
			((ERPDetailServiceOutput)_currentData).addNewVehicleInfo(currentERPVehicleInfo);
			currentERPVehicleInfo = null;
		} else if(s2.equals("vehicle_type_2")) {
			((ERPDetailServiceOutput)_currentData).addNewVehicleInfo(currentERPVehicleInfo);
			currentERPVehicleInfo = null;
		} else if(s2.equals("vehicle_type_3")) {
			((ERPDetailServiceOutput)_currentData).addNewVehicleInfo(currentERPVehicleInfo);
			currentERPVehicleInfo = null;
		} else if(s2.equals("vehicle_type_4")) {
			((ERPDetailServiceOutput)_currentData).addNewVehicleInfo(currentERPVehicleInfo);
			currentERPVehicleInfo = null;
		} else if(s2.equals("vehicle_type_5")) {
			((ERPDetailServiceOutput)_currentData).addNewVehicleInfo(currentERPVehicleInfo);
			currentERPVehicleInfo = null;
		} else if(s2.equals("vehicle_type_6")) {
			((ERPDetailServiceOutput)_currentData).addNewVehicleInfo(currentERPVehicleInfo);
			currentERPVehicleInfo = null;
		} else if(s2.equals("dataset")) {
			((ERPDetailServiceOutput)_currentData).populateData();
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
        try
        {
            if(s2.equals("dataset")) {
                long l = Long.parseLong(attributes.getValue("total"));
                _output.total = l;
                onReceiveTotal(l);
                _buffer = new StringBuilder();
                return;
            } else if(s2.equals("vehicle_type_1")) {
                currentERPVehicleInfo = new ERPVehicleInfo();
            } else if(s2.equals("vehicle_type_2")) {
                currentERPVehicleInfo = new ERPVehicleInfo();
            } else if(s2.equals("vehicle_type_3")) {
                currentERPVehicleInfo = new ERPVehicleInfo();
            } else if(s2.equals("vehicle_type_4")) {
                currentERPVehicleInfo = new ERPVehicleInfo();
            } else if(s2.equals("vehicle_type_5")) {
                currentERPVehicleInfo = new ERPVehicleInfo();
            } else if(s2.equals("vehicle_type_6")) {
                currentERPVehicleInfo = new ERPVehicleInfo();
            } else if(s2.equals("weekdays")) {
                currentERPVehicleInfo.arrayOfWeekdaysInfo = new ArrayList();
                currentArrayOfTimeRateInfo = new ArrayList();
            } else if(s2.equals("saturday")) {
                currentERPVehicleInfo.arrayOfWeekendInfo = new ArrayList();
                currentArrayOfTimeRateInfo = new ArrayList();
            } else if(s2.equals("sub")) {
                currentTimeRateInfo = new ERPTimeRateInfo();
            } else if(s2.equals("detail")) {
                ((ERPDetailServiceOutput)_currentData).arrayOfVehicleInfo = new ArrayList();
            } else if(s2.equals("error"))
                currentError = new SDErrorOutput();
        }
        catch(Exception ex) { }

    }

    private ArrayList currentArrayOfTimeRateInfo;
    private ERPVehicleInfo currentERPVehicleInfo;
    private ERPTimeRateInfo currentTimeRateInfo;
}
