

package streetdirectory.mobile.core.service;

import org.xml.sax.SAXException;

public class SAXParserStopParsingException extends SAXException
{

    public SAXParserStopParsingException()
    {
    }

    public String toString()
    {
        return "SAXParserStopParsingException";
    }

    private static final long serialVersionUID = 0x4a852c36e55ec180L;
}
