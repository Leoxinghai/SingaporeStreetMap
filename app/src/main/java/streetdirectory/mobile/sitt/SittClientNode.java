

package streetdirectory.mobile.sitt;


// Referenced classes of package streetdirectory.mobile.sitt:
//            SittServerNode, SittSignalNode

public class SittClientNode
{

    public SittClientNode()
    {
    }

    public SittClientNode(SittSignalNode sittsignalnode, SittServerNode sittservernode, double d)
    {
        signalNode = sittsignalnode;
        serverNode = sittservernode;
        distance = d;
    }

    public double distance;
    public SittServerNode serverNode;
    public SittSignalNode signalNode;
}
