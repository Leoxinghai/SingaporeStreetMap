// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import streetdirectory.mobile.core.service.*;

// Referenced classes of package streetdirectory.mobile.service:
//            SDDataOutput, SDHttpServiceOutput, SDErrorOutput

public class SDDatasetDataXMLHandler extends DefaultHandler
    implements HttpServiceResultHandler
{

    public SDDatasetDataXMLHandler()
    {
        countryCode = "sg";
        _buffer = new StringBuilder();
    }

    public SDDatasetDataXMLHandler(Class class1)
    {
        countryCode = "sg";
        _buffer = new StringBuilder();
        _dataClass = class1;
    }

    public void abort()
    {
        _isCanceled = true;
    }

    public void characters(char ac[], int i, int j)
        throws SAXException
    {
        try
        {
            if(_buffer != null)
                _buffer.append(ac, i, j);
            return;
        } catch(Exception ex) {
            if(_isCanceled)
                throw new SAXParserAbortException();
            else
                throw new SAXException(ex);
        }
    }

    public void endElement(String s, String s1, String s2)
        throws SAXException
    {
        try {
            if (_currentData == null) {
                if (currentError == null) {
                    _buffer = null;
                    return;
                }
                if (_buffer != null)
                    currentError.hashData.put(s2, _buffer.toString());
                if ("error".equals(s2)) {
                    currentError.populateData();
                    onReceiveError(currentError);
                    currentError = null;
                    throw new SAXParserStopParsingException();
                }
            } else {
                if (_buffer != null)
                    _currentData.hashData.put(s2, _buffer.toString());
                if (s2.equals("data")) {
                    if (!_currentData.hashData.containsKey("country")) {
                        _currentData.hashData.put("country", countryCode);
                        _currentData.countryCode = countryCode;
                    }
                    _currentData.populateData();
                    onReceiveData(_currentData);
                    _output.childs.add(_currentData);
                    _currentData = null;
                }
            }

            _buffer = null;
            return;
        } catch(Exception ex) {
            ex.printStackTrace();
            if (_isCanceled)
                throw new SAXParserAbortException();
            else
                throw new SAXException(ex);
        }
    }

    public void onFailed(Exception exception)
    {
    }

    public void onReceiveData(SDDataOutput sddataoutput)
    {
    }

    public void onReceiveError(SDErrorOutput sderroroutput)
    {
    }

    public void onReceiveTotal(long l)
    {
    }

    public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
    {
    }

    public SDHttpServiceOutput parse(InputStream inputstream)
    {
        try
        {
            _isCanceled = false;
            _output = new SDHttpServiceOutput();
            XMLReader xmlreader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            xmlreader.setContentHandler(this);
            xmlreader.parse(new InputSource(inputstream));
            if(_isCanceled)
                onFailed(new SAXParserAbortException());
            else
                onSuccess(_output);
        } catch(org.xml.sax.SAXException sex) {
        } catch(Exception iex) {
            if(_isCanceled)
                onFailed(new SAXParserAbortException());
            else
                onFailed(iex);
        }

        return _output;
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
            // Misplaced declaration of an exception variable
            catch(Exception ex0) { }
            _buffer = new StringBuilder();
            return;
        }

        flag = s2.equals("data");
        if(flag) {
            try
            {
                _currentData = (SDDataOutput)_dataClass.newInstance();
            }
            catch(Exception ex1)
            {
                if(_isCanceled)
                    throw new SAXParserAbortException();
                else
                    throw new SAXException(s);
            }
            return;
        }

        flag = s2.equals("error");
        if(flag) {
            currentError = new SDErrorOutput();
        }
        throw new SAXException(s);

    }

    protected StringBuilder _buffer;
    protected SDDataOutput _currentData;
    protected Class _dataClass;
    protected boolean _isCanceled;
    protected SDHttpServiceOutput _output;
    public String countryCode;
    protected SDErrorOutput currentError;
}
