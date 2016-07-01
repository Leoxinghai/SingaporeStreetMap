

package streetdirectory.mobile.service;

import java.util.ArrayList;

// Referenced classes of package streetdirectory.mobile.service:
//            SDXmlServiceOutput

public abstract class SDXmlServiceXOutput extends SDXmlServiceOutput
{
    public static class Attribute
    {

        public String name;
        public String value;

        public Attribute()
        {
        }
    }


    public SDXmlServiceXOutput()
    {
    }

    public SDXmlServiceXOutput[] getChildNodes(String s)
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < childs.size(); i++)
        {
            SDXmlServiceXOutput sdxmlservicexoutput = (SDXmlServiceXOutput)childs.get(i);
            if(sdxmlservicexoutput.name.equalsIgnoreCase(s))
                arraylist.add(sdxmlservicexoutput);
        }

        return (SDXmlServiceXOutput[])(SDXmlServiceXOutput[])arraylist.toArray();
    }

    public abstract void populateData();

    public final ArrayList attributes = new ArrayList();
    public final ArrayList childs = new ArrayList();
    public String name;
    public SDXmlServiceXOutput parent;
    public String value;
}
