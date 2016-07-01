

package streetdirectory.mobile.facebook.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class FacebookSendDataServiceInput extends SDHttpServiceInput
{

    public FacebookSendDataServiceInput()
    {
    }

    public FacebookSendDataServiceInput(String s, String s1, String s2, String s3, String s4, String s5, String s6,
            String s7, String s8, String s9, String s10, String s11, String s12)
    {
        super(s);
        uid = s1;
        name = s2;
        sex = s3;
        religion = s4;
        picSquareURL = s5;
        profileURL = s6;
        location = s7;
        postal = s8;
        email = s9;
        birthday = s10;
        country = s11;
        insertTime = s12;
    }

    public String getURL()
    {
        return URLFactory.createURLFacebookSendData(uid, name, sex, religion, picSquareURL, profileURL, location, postal, email, birthday, country, insertTime, apiVersion);
    }

    public String birthday;
    public String country;
    public String email;
    public String insertTime;
    public String location;
    public String name;
    public String picSquareURL;
    public String postal;
    public String profileURL;
    public String religion;
    public String sex;
    public String uid;
}
