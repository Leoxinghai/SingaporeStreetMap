// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.sitt;

import android.content.*;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import java.io.File;
import java.util.*;
import streetdirectory.mobile.core.FileTools;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.core.storage.InternalStorage;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.sitt:
//            SittClientNode, SittSignalNode, SittSsidInfoListServiceInput, SittSsidInfoListService,
//            SittServerNode, SittSsidInfoListServiceOutput

public class SittManager
{
    public static abstract class ClientNodeUpdateListener
    {

        public abstract void onUpdate(List list);

        public ClientNodeUpdateListener()
        {
        }
    }

    public static enum MODE
    {

        CONTINOUS("CONTINOUS", 0),
        STEP("STEP", 1);
        private String sType;
        private int iType;
        private MODE(String s, int i)
        {
            sType = s;
            iType = i;

        }
    }

    private static class WifiReceiver extends BroadcastReceiver
    {

        public void onReceive(Context context, Intent intent)
        {
            if(wifiManager != null)
                if(sittManager == null);
        }

        public SittManager sittManager;
        public WifiManager wifiManager;

        private WifiReceiver()
        {
            wifiManager = null;
            sittManager = null;
        }

    }


    public SittManager()
    {
        signalNodes = new ArrayList();
        clientNodes = new ArrayList();
        serverNodes = new ArrayList();
        wifiReceiver = new WifiReceiver();
        isRunning = false;
        mode = MODE.STEP;
        clientNodeUpdateListeners = new ArrayList();
    }

    private void addClientNode(SittClientNode sittclientnode)
    {
        if(sittclientnode == null)
            return;
        for(Iterator iterator = clientNodes.iterator(); iterator.hasNext();)
        {
            SittClientNode sittclientnode1 = (SittClientNode)iterator.next();
            if(sittclientnode1.signalNode.bssid.equalsIgnoreCase(sittclientnode.signalNode.bssid))
            {
                sittclientnode1.signalNode = sittclientnode.signalNode;
                sittclientnode1.serverNode = sittclientnode.serverNode;
                sittclientnode1.distance = sittclientnode.distance;
                return;
            }
        }

        try
        {
            clientNodes.add(sittclientnode);
            return;
        } catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void addSignalNode(SittSignalNode sittsignalnode)
    {
        if(sittsignalnode == null || sittsignalnode.bssid == null)
            return;
        for(Iterator iterator = signalNodes.iterator(); iterator.hasNext();)
        {
            LinkedList linkedlist1 = (LinkedList)iterator.next();
            SittSignalNode sittsignalnode1 = (SittSignalNode)linkedlist1.peek();
            if(sittsignalnode1 != null && sittsignalnode1.bssid.equalsIgnoreCase(sittsignalnode.bssid))
            {
                if(linkedlist1.size() >= 5)
                    if(mode == MODE.STEP)
                        linkedlist1.clear();
                    else
                        linkedlist1.poll();
                linkedlist1.add(sittsignalnode);
                return;
            }
        }

        LinkedList linkedlist = new LinkedList();
        linkedlist.add(sittsignalnode);
        signalNodes.add(linkedlist);
    }

    private void cleanUpClientNode()
    {
        Iterator iterator = clientNodes.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            SittClientNode sittclientnode = (SittClientNode)iterator.next();
            if(getServerNode(sittclientnode.signalNode.bssid) == null)
                clientNodes.remove(sittclientnode);
        } while(true);
    }

    private SittClientNode createClientNode(List list, SittServerNode sittservernode, int i)
    {
        SittSignalNode sittsignalnode = enhance(list, i);
        SittClientNode temp = null;
        if(sittsignalnode != null)
            temp = new SittClientNode(sittsignalnode, sittservernode, getDistance(sittsignalnode.frequency, sittsignalnode.level));
        return temp;
    }

    class SUBCLASS1 extends SittSsidInfoListService {

        public void onFailed(Exception exception)
        {
            super.onFailed(exception);
            sittService = null;
        }

        public void onSuccess(Object obj)
        {
            onSuccess((SDHttpServiceOutput)obj);
        }

        public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
        {
            super.onSuccess(sdhttpserviceoutput);
            Iterator iterator;

            for(iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext();)
            {
                Object obj = (SittSsidInfoListServiceOutput)iterator.next();
                if(downloadedTimeInMiliSecond == 0L)
                {
                    serverNodes.addAll(((SittSsidInfoListServiceOutput) (obj)).listServerNode);
                } else
                {
                    obj = ((SittSsidInfoListServiceOutput) (obj)).listServerNode.iterator();
                    while(((Iterator) (obj)).hasNext())
                    {
                        SittServerNode sittservernode = (SittServerNode)((Iterator) (obj)).next();
                        updateOrInsertServerNode(sittservernode);
                    }
                }
            }

            sittService = null;
            FileTools.serialize(InternalStorage.getStorageFile("list_server_node.txt").getPath(), serverNodes);
            android.content.SharedPreferences.Editor temp = SDPreferences.getInstance().createEditor();
            temp.putLong("sitt_server_downloaded_time", (new Date()).getTime());
            temp.commit();
        }

        final long downloadedTimeInMiliSecond;
        SUBCLASS1(long l)
        {
            super(null);
            downloadedTimeInMiliSecond = l;
        }
    };

    private void downloadSittSsidInfo(List list)
    {
        if(sittService != null)
        {
            return;
        } else
        {
            long l = SDPreferences.getInstance().getLongForKey("sitt_server_downloaded_time", 0L);
            sittService = new SUBCLASS1(l);
            sittService.executeAsync();
            return;
        }
    }

    private SittSignalNode enhance(List list, int i)
    {
        if(list == null || list.size() < 1)
            return null;

        SittSignalNode sittsignalnode;
        double d;


        Iterator iterator;
        int j;
        if(i > 5)
        {
            j = 5;
        } else
        {
            j = i;
            if(i < 1)
                j = 1;
        }
        d = 0.0D;
        sittsignalnode = new SittSignalNode();
        SittSignalNode temp = null;
        iterator = list.iterator();
        while(iterator.hasNext())
        {
            SittSignalNode sittsignalnode1 = (SittSignalNode)iterator.next();
            sittsignalnode = SittSignalNode.newNode(sittsignalnode1);
            double d1 = Math.abs(Math.log10(getDistance(sittsignalnode1.frequency, sittsignalnode1.level)));
            if(sittsignalnode1.frequency > 4000)
            {
                if(sittsignalnode1.level < -80D)
                    sittsignalnode.level = ((20D * Math.log10(sittsignalnode1.frequency) + 22D * Math.log10(d1) + (double)((j - 1) * 3 + 6)) - 16D) * -1D;
                else
                    sittsignalnode.level = ((20D * Math.log10(sittsignalnode1.frequency) + 22D * Math.log10(d1) + (double)((j - 1) * 3 + 6)) - 22D) * -1D;
            } else
            if(sittsignalnode1.level < -60D)
                sittsignalnode.level = ((20D * Math.log10(sittsignalnode1.frequency) + 22D * Math.log10(d1) + (double)((j - 1) * 3 + 6)) - 24D) * -1D;
            else
                sittsignalnode.level = ((20D * Math.log10(sittsignalnode1.frequency) + 22D * Math.log10(d1) + (double)((j - 1) * 3 + 6)) - 28D) * -1D;
            d += sittsignalnode.level;
        }
        sittsignalnode.level = d / (double)list.size();
        if(sittsignalnode.frequency < 4000 && sittsignalnode.level >= -30D)
            return temp;
        temp = sittsignalnode;
        if(sittsignalnode.frequency >= 4000) {
            temp = sittsignalnode;
            if (sittsignalnode.level >= -40D)
                return null;
        }
        return temp;
    }

    public static SittClientNode getClientNode(List list, String s)
    {
        Iterator iterator = list.iterator();
        SittClientNode sittclientnode;
        boolean flag;
        for(;iterator.hasNext();) {
            sittclientnode = (SittClientNode)iterator.next();
            flag = sittclientnode.signalNode.bssid.equalsIgnoreCase(s);
            if(flag)
                return sittclientnode;
        }

        return null;
    }

    private double getDistance(double d, double d1)
    {
        if(d > 4000D)
            return Math.pow(10D, (d1 + 45.610390000000002D) / -27.82D);
        else
            return Math.pow(10D, (d1 + 31.559999999999999D) / -28.093900000000001D);
    }

    public static SittManager getInstance()
    {
        if(staticInstance == null)
            staticInstance = new SittManager();
        return staticInstance;
    }

    private SittServerNode getServerNode(String s)
    {
        Iterator iterator = serverNodes.iterator();
        SittServerNode sittservernode;
        boolean flag;
        for(;iterator.hasNext();) {
            sittservernode = (SittServerNode)iterator.next();
            flag = sittservernode.bssid.equalsIgnoreCase(s);
            if(flag)
                return sittservernode;
        }

        return null;
    }

    private boolean isDownloadIntervalElapsed()
    {
        long l = SDPreferences.getInstance().getLongForKey("sitt_server_downloaded_time", 0L);
        return (new Date()).getTime() - l >= 0x2932e00L;
    }

    public static boolean isQualifiedForRecord(SittClientNode sittclientnode, SittClientNode sittclientnode1)
    {
        int i;
        int j;
        try
        {
            i = regionLevel(sittclientnode.distance);
            j = regionLevel(sittclientnode1.distance);
            if(i == j || j > 5)
                return false;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return true;
        }
        return true;
    }

    private void notifyClientNodesUpdate()
    {
        ArrayList arraylist = new ArrayList();
        try
        {
            SittClientNode sittclientnode;
            for(Iterator iterator = clientNodes.iterator(); iterator.hasNext(); arraylist.add(new SittClientNode(sittclientnode.signalNode, sittclientnode.serverNode, sittclientnode.distance)))
                sittclientnode = (SittClientNode)iterator.next();

        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        for(Iterator iterator1 = clientNodeUpdateListeners.iterator(); iterator1.hasNext();)
        {
            ClientNodeUpdateListener clientnodeupdatelistener = (ClientNodeUpdateListener)iterator1.next();
            try
            {
                clientnodeupdatelistener.onUpdate(arraylist);
            }
            catch(Exception exception1)
            {
                exception1.printStackTrace();
            }
        }

    }

    private void processClientNodes()
    {
        Iterator iterator = signalNodes.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            LinkedList linkedlist = (LinkedList)iterator.next();
            if(linkedlist.size() >= 5)
            {
                SittServerNode sittservernode = getServerNode(((SittSignalNode)linkedlist.peek()).bssid);
                if(sittservernode != null)
                    addClientNode(createClientNode(linkedlist, sittservernode, 5));
            }
        } while(true);
    }

    private void processScanResults(List list)
    {
        if(isDownloadIntervalElapsed())
            downloadSittSsidInfo(list);
        Iterator iterator;
        for(iterator = list.iterator(); iterator.hasNext(); addSignalNode(new SittSignalNode((ScanResult)iterator.next())));
        processClientNodes();
        cleanUpClientNode();
        if(clientNodes.size() > 0)
            notifyClientNodesUpdate();
    }

    public static int regionLevel(double d)
    {
        if(d < 5D)
            return 1;
        if(d < 10D)
            return 2;
        if(d < 15D)
            return 3;
        if(d < 20D)
            return 4;
        return d >= 50D ? 6 : 5;
    }

    public void addClientNodeUpdateListener(ClientNodeUpdateListener clientnodeupdatelistener)
    {
        clientNodeUpdateListeners.add(clientnodeupdatelistener);
    }

    protected boolean isBssidInServerNode(String s)
    {
        for(Iterator iterator = serverNodes.iterator(); iterator.hasNext();)
            if(((SittServerNode)iterator.next()).bssid.equalsIgnoreCase(s))
                return true;

        return false;
    }

    public void removeClientNodeUpdateListener(ClientNodeUpdateListener clientnodeupdatelistener)
    {
        clientNodeUpdateListeners.remove(clientnodeupdatelistener);
    }

    public void start(Context context)
    {
        Object obj = FileTools.deserialize(InternalStorage.getStorageFile("list_server_node.txt").getPath());
        if(obj != null)
            serverNodes = (ArrayList)obj;
        if(isRunning)
            stop();
        isRunning = true;
        currentContext = context;
        wifiManager = (WifiManager)currentContext.getSystemService(Context.WIFI_SERVICE);
        wifiReceiver.wifiManager = wifiManager;
        wifiReceiver.sittManager = this;
        currentContext.registerReceiver(wifiReceiver, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
        wifiManager.startScan();
    }

    public void stop()
    {
        currentContext.unregisterReceiver(wifiReceiver);
        isRunning = false;
    }

    protected void updateOrInsertServerNode(SittServerNode sittservernode)
    {
        boolean flag = true;
        Iterator iterator = serverNodes.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            SittServerNode sittservernode1 = (SittServerNode)iterator.next();
            if(sittservernode1.bssid.equalsIgnoreCase(sittservernode.bssid))
            {
                serverNodes.set(serverNodes.indexOf(sittservernode1), sittservernode);
                flag = false;
            }
        } while(true);
        if(flag)
            serverNodes.add(sittservernode);
    }

    private static final double A2400 = -31.559999999999999D;
    private static final double A5200 = -45.610390000000002D;
    private static final long DOWNLOAD_INTERVAL = 0x2932e00L;
    private static final int FLOORLIMIT = 5;
    private static final String LIST_SERVER_NODE_FILENAME = "list_server_node.txt";
    private static final int SCAN_SAMPLE_SIZE = 5;
    private static final double calibratedN2400 = -2.8093900000000001D;
    private static final double calibratedN5200 = -2.782D;
    public static SittManager staticInstance;
    public ArrayList clientNodeUpdateListeners;
    protected ArrayList clientNodes;
    protected Context currentContext;
    protected boolean isRunning;
    public MODE mode;
    protected ArrayList serverNodes;
    protected ArrayList signalNodes;
    private SittSsidInfoListService sittService;
    protected WifiManager wifiManager;
    protected WifiReceiver wifiReceiver;


/*
    static SittSsidInfoListService access$102(SittManager sittmanager, SittSsidInfoListService sittssidinfolistservice)
    {
        sittmanager.sittService = sittssidinfolistservice;
        return sittssidinfolistservice;
    }

*/
}
