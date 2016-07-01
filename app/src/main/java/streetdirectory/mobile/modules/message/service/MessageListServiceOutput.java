

package streetdirectory.mobile.modules.message.service;

import java.text.SimpleDateFormat;
import java.util.*;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.service.SDDataOutput;

public class MessageListServiceOutput extends SDDataOutput
{

    public MessageListServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        messageID = (String)hashData.get("mid");
        subject = (String)hashData.get("sbj");
        messagePreview = (String)hashData.get("smsg");
        friendID = (String)hashData.get("fid");
        friendName = (String)hashData.get("fname");
        type = (String)hashData.get("t");
        if(format == null)
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getDefault());
        try
        {
            dateTime = format.parse((String)hashData.get("tm"));
        }
        catch(Exception exception)
        {
            SDLogger.printStackTrace(exception, "ServiceOutput parse date failed");
        }
        if("1".equals(hashData.get("sts")))
        {
            status = true;
            return;
        } else
        {
            status = false;
            return;
        }
    }

    private static final long serialVersionUID = 0x70b914404c9f8574L;
    public Date dateTime;
    private SimpleDateFormat format;
    public String friendID;
    public String friendName;
    public String messageID;
    public String messagePreview;
    public boolean status;
    public String subject;
    public String type;
}
