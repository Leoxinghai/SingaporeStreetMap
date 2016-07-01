

package streetdirectory.mobile.modules.locationdetail.bus.service;

import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import streetdirectory.mobile.core.service.SAXParserAbortException;
import streetdirectory.mobile.core.service.SAXParserStopParsingException;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.bus.service:
//            BusArrivalServiceOutput

public class BusArrivalXMLParserHandler extends SDDatasetDataXMLHandler
{

    public BusArrivalXMLParserHandler()
    {
        super(BusArrivalServiceOutput.class);
    }

    public void endElement(String s, String s1, String s2)
        throws SAXException
    {
        if(_currentData == null) {
            if(currentError != null) {
                if (_buffer != null)
                    currentError.hashData.put(s2, _buffer.toString());
                if ("error".equals(s2)) {
                    currentError.populateData();
                    onReceiveError(currentError);
                    currentError = null;
                    throw new SAXParserStopParsingException();
                }
            }
        } else {
            if (_buffer != null) {
                if (s2.equals("t")) {
                    if (currentMode != 0 || totalNextBus != 1) {
                        if (currentMode == 1 && totalSubSequentBus == 1) {
                            ((BusArrivalServiceOutput) _currentData).hashData.put("subsequentbus", _buffer.toString());
                        }
                    } else {
                        ((BusArrivalServiceOutput) _currentData).hashData.put("nextbus", _buffer.toString());
                    }
                }

                if (currentMode == 2 && totalBusNumber == 1)
                    ((BusArrivalServiceOutput) _currentData).hashData.put("service_no", _buffer.toString());
            }

            if (s2.equals("iris")) {
                ((BusArrivalServiceOutput) _currentData).populateData();
                onReceiveData(_currentData);
                _output.childs.add(_currentData);
                _currentData = null;
            }
        }

        _buffer = null;
        return;

    }

    public void startElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        boolean flag = s2.equals("iris");
        try
        {
            if(flag) {
                long l = Long.parseLong(attributes.getValue("total"));
                _output.total = l;
                onReceiveTotal(l);
            } else if(s2.equals("service_no")) {
                currentMode = 2;
                totalBusNumber = totalBusNumber + 1;
            } else  if(s2.equals("nextbus")) {
                currentMode = 0;
                totalNextBus = totalNextBus + 1;
            } else if(s2.equals("subsequentbus")) {
                currentMode = 1;
                totalSubSequentBus = totalSubSequentBus + 1;
            }

            } catch(Exception ex)
            {
                    throw new SAXException(ex);
            }
            if(s2.equals("error"))
            {
                currentError = new SDErrorOutput();
                throw new SAXException(currentError.message);
            }
            _buffer = new StringBuilder();
            return;

    }

    private static final int MODE_BUS_NUMBER = 2;
    private static final int MODE_NEXT_BUS = 0;
    private static final int MODE_SUBSEQUENT_BUS = 1;
    private int currentMode;
    private int totalBusNumber;
    private int totalNextBus;
    private int totalSubSequentBus;
}
