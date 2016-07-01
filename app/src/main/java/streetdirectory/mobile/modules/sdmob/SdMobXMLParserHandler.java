// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.sdmob;

import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import streetdirectory.mobile.core.service.SAXParserAbortException;
import streetdirectory.mobile.core.service.SAXParserStopParsingException;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.sdmob:
//            SdMobServiceOutput

public class SdMobXMLParserHandler extends SDDatasetDataXMLHandler
{

    public SdMobXMLParserHandler()
    {
    }

    public void endElement(String s, String s1, String s2)
        throws SAXException
    {
        try
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
                if (s2.equals("type")) {
                    if (currentMode == 0) {
                        ((SdMobServiceOutput) _currentData).hashData.put("small_banner_type", _buffer.toString());
                    } else if (currentMode == 1) {
                        ((SdMobServiceOutput) _currentData).hashData.put("full_banner_type", _buffer.toString());
                    }
                } else if (s2.equals("dataset")) {
                    ((SdMobServiceOutput) _currentData).populateData();
                    onReceiveData(_currentData);
                    _output.childs.add(_currentData);
                    _currentData = null;
                } else if (s2.equals("start")) {
                    if (currentMode == 0) {
                        ((SdMobServiceOutput) _currentData).hashData.put("small_banner_start", _buffer.toString());
                    } else if (currentMode == 1) {
                        ((SdMobServiceOutput) _currentData).hashData.put("full_banner_start", _buffer.toString());
                    }
                } else if (s2.equals("end")) {
                    if (currentMode == 0) {
                        ((SdMobServiceOutput) _currentData).hashData.put("small_banner_end", _buffer.toString());
                    } else if (currentMode == 1) {
                        ((SdMobServiceOutput) _currentData).hashData.put("full_banner_end", _buffer.toString());
                    }
                } else if (s2.equals("admob_droid_id")) {

                    if (currentMode == 0) {
                        ((SdMobServiceOutput) _currentData).hashData.put("small_banner_admob_id", _buffer.toString());

                    } else if (currentMode == 1) {
                        ((SdMobServiceOutput) _currentData).hashData.put("full_banner_admob_id", _buffer.toString());
                    }
                } else if (s2.equals("offer_id")) {

                    if (currentMode == 0) {
                        ((SdMobServiceOutput) _currentData).hashData.put("small_banner_offer_id", _buffer.toString());
                    } else if (currentMode == 1) {
                        ((SdMobServiceOutput) _currentData).hashData.put("full_banner_offer_id", _buffer.toString());
                    }
                } else if (s2.equals("html")) {
                    if (currentMode == 1)
                        ((SdMobServiceOutput) _currentData).hashData.put("full_banner_offer_html", _buffer.toString());
                } else if (s2.equals("lid")) {
                    if (currentMode == 1)
                        ((SdMobServiceOutput) _currentData).hashData.put("full_banner_offer_lid", _buffer.toString());
                } else if (s2.equals("bid") && currentMode == 1) {
                    ((SdMobServiceOutput) _currentData).hashData.put("full_banner_offer_bid", _buffer.toString());
                }
            }
        }
        _buffer = null;
        return;




        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void startElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        byte byte0 = 0;
        boolean flag;
        int i = s2.hashCode();
        try
        {

            switch(i) {
            default:
                byte0 = -1;
                break;
            case 1443214456:
                if (!s2.equals("dataset"))
                    byte0 = -1;
                break;
            case 403997028:
                if (!s2.equals("small_banner"))
                    byte0 = -1;
                else
                    byte0 = 1;
                break;
            case 1497158948:
                if (!(flag = s2.equals("full_banner")))
                    byte0 = -1;
                else
                    byte0 = 2;
                    break;
        }


        switch(byte0) {
            default:
                break;
            case 0:
                _currentData = new SdMobServiceOutput();
                break;
            case 1:
                currentMode = 0;
                break;
            case 2:
                currentMode = 1;
                break;
        }
            _buffer = new StringBuilder();
            return;

        } catch(Exception sex) {
            if (_isCanceled)
                throw new SAXParserAbortException();
            throw new SAXException(s);
        }

    }

    private static final int FULL_BANNER = 1;
    private static final int SMALL_BANNER = 0;
    private int currentMode;
}
