

package streetdirectory.mobile.service.applicationstatus;

import java.text.*;
import java.util.Date;
import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class ApplicationStatusServiceOutput extends SDDataOutput
{

    public ApplicationStatusServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        String s;
        try
        {
            totalNewMessage = Integer.parseInt((String)hashData.get("msg_n"));
        }
        catch(Exception exception)
        {
            totalNewMessage = 0;
        }
        try
        {
            tipsPremiumType = Integer.parseInt((String)hashData.get("tpt"));
        }
        catch(Exception exception1)
        {
            tipsPremiumType = 0;
        }
        if(df == null)
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        s = (String)hashData.get("cutm");
        try
        {
            serverDate = df.parse(s);
            return;
        }
        catch(ParseException parseexception)
        {
            parseexception.printStackTrace();
        }
    }

    private static final long serialVersionUID = 0xc49c7841c0446a6bL;
    private DateFormat df;
    public Date serverDate;
    public int tipsPremiumType;
    public int totalNewMessage;
}
