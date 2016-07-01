

package streetdirectory.mobile.modules.settings.sendfeedback.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class SendFeedbackServiceInput extends SDHttpServiceInput
{

    public SendFeedbackServiceInput()
    {
    }

    public SendFeedbackServiceInput(String s, String s1, String s2, String s3, String s4)
    {
        super(s);
        message = s1;
        note = s2;
        uid = s3;
        email = s4;
    }

    public String getURL()
    {
        return URLFactory.createURLYourFeedback(countryCode, message, note, uid, email);
    }

    public String email;
    public String message;
    public String note;
    public String uid;
}
