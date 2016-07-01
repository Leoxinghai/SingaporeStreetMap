// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.sitt;

import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import streetdirectory.mobile.core.service.SAXParserAbortException;
import streetdirectory.mobile.core.service.SAXParserStopParsingException;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.sitt:
//            SittServerNode, SittSsidInfoListServiceOutput

public class SittSsidInfoXMLParserHandler extends SDDatasetDataXMLHandler
{
    private static enum CurrentMode
    {
		COMPANY("COMPANY", 0),
		OFFER("OFFER", 1);
		String sType;
		int iType;
        private CurrentMode(String s, int i) {
			sType = s;
			iType = i;
        }
    }


    public SittSsidInfoXMLParserHandler()
    {
    }

    public void endElement(String s, String s1, String s2)
        throws SAXException
    {
        if(_currentData == null)
        	return;

			try
			{

        if(_buffer != null) {
			boolean flag = s2.equals("floor");
			if(flag)  {
				currentSittServerNode.floor = Integer.parseInt(_buffer.toString());
			} else if(s2.equals("wifi_id"))
			{
				currentSittServerNode.wifiId = _buffer.toString();
			} else if(s2.equals("wifi_name"))
			{
				currentSittServerNode.ssid = _buffer.toString();
			} else if(s2.equals("bssid"))
			{
				currentSittServerNode.bssid = _buffer.toString();
			} else if(s2.equals("address"))
			{
				currentSittServerNode.address = _buffer.toString();
			} else if(currentMode == CurrentMode.COMPANY)
			{
				currentCompanyInfo2.hashData.put(s2, _buffer.toString());
			} else if(currentMode == CurrentMode.OFFER) {
				currentOfferInfo2.hashData.put(s2, _buffer.toString());
			}
		}

		if(s2.equals("dataset")) {
			((SittSsidInfoListServiceOutput)_currentData).populateData();
			onReceiveData(_currentData);
			_output.childs.add(_currentData);
			_currentData = null;
		} else if(s2.equals("data"))
        {
            ((SittSsidInfoListServiceOutput)_currentData).addNewServerNode(currentSittServerNode);
            currentSittServerNode = null;
        } else if(s2.equals("sub")) {
			if(currentMode == CurrentMode.COMPANY)
			{
				currentCompanyInfo2.populateData();
				currentSittServerNode.addNewCompanyInfo(currentCompanyInfo2);
				currentCompanyInfo2 = null;
			} else if(currentMode == CurrentMode.OFFER)
			{
				currentOfferInfo2.populateData();
				currentCompanyInfo2.addNewOfferInfo(currentOfferInfo2);
				currentOfferInfo2 = null;
			}
		} else if(s2.equals("list_offers")) {
            currentMode = CurrentMode.COMPANY;
		}

		}
		catch(Exception ex)
		{
			currentSittServerNode.floor = 0;
			ex.printStackTrace();
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
        }

        _buffer = null;
        return;
    }

    public void startElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        boolean flag = s2.equals("dataset");
		if(flag) {
			try
			{
				long l = Long.parseLong(attributes.getValue("total"));
				_output.total = l;
				onReceiveTotal(l);
			}
			catch(Exception ex) { }
			try
			{
				_currentData = new SittSsidInfoListServiceOutput();
			}
			catch(Exception ex)
			{
				throw new SAXException(ex);
			}
			_buffer = new StringBuilder();
			return;
		} else if(s2.equals("data")) {
            currentSittServerNode = new SittServerNode();
        } else if(s2.equals("list_company")) {
            currentMode = CurrentMode.COMPANY;
        } else if(s2.equals("list_offers"))
        {
            currentMode = CurrentMode.OFFER;
        } else if(s2.equals("sub")) {
			if(currentMode == CurrentMode.COMPANY)
			{
				currentCompanyInfo2 = new SittServerNode.CompanyInfo();

			}
			if(currentMode == CurrentMode.OFFER)
				currentOfferInfo2 = new SittServerNode.OfferInfo();
		}
    }

    private SittServerNode.CompanyInfo currentCompanyInfo2;
    private CurrentMode currentMode;
    private SittServerNode.OfferInfo currentOfferInfo2;
    private SittServerNode currentSittServerNode;
}
