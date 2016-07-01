

package streetdirectory.mobile.core.service;

import org.xml.sax.SAXException;

public class SAXParserAbortException extends SAXException
{

    public SAXParserAbortException()
    {
    }

    public String toString()
    {
        return "SAXParserAbortException";
    }

    private static final long serialVersionUID = 0x2eefcc56d3502f4L;
}
