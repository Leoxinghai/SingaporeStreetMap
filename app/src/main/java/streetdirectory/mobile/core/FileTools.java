

package streetdirectory.mobile.core;

import java.io.*;

public class FileTools
{

    public FileTools()
    {
    }

    public static Object deserialize(String s)
    {
        String s1 = null;
        String s2 = null;
        String s3 = null;
        String s4 = null;
        ObjectInputStream objectinputstream;
        try
        {
            objectinputstream = new ObjectInputStream(new FileInputStream(s));
            s = ((String) (objectinputstream.readObject()));
            s1 = s;
            s2 = s;
            s3 = s;
            s4 = s;
            objectinputstream.close();
            return s;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            ex.printStackTrace();
            return s1;
        }
    }

    public static Exception serialize(String s, Object obj)
    {
        try
        {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(s));
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
}
