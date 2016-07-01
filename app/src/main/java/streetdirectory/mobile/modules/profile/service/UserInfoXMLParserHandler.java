// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.profile.service;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import streetdirectory.mobile.core.service.SAXParserAbortException;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.profile.service:
//            UserInfoServiceOutput, UserInfoGeneralServiceOutput, UserInfoCountryServiceOutput

public class UserInfoXMLParserHandler extends SDDatasetDataXMLHandler
{

    public UserInfoXMLParserHandler()
    {
        super(UserInfoServiceOutput.class);
        isFoundFirstData = false;
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
        } else if(s2.equals("data")) {
            if (!isFoundFirstData) {
                _currentData = new UserInfoGeneralServiceOutput();
                isFoundFirstData = true;
            } else
                _currentData = new UserInfoCountryServiceOutput();
        } else if(s2.equals("error"))

            {
                currentError = new SDErrorOutput();
            }

            _buffer = new StringBuilder();
            return;
    }
    catch(Exception ex) { }

}

    private boolean isFoundFirstData;
}
