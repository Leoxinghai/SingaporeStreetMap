

package streetdirectory.mobile.sitt;

import java.io.Serializable;
import java.util.*;

public class SittServerNode
    implements Serializable
{
    public static class CompanyInfo
    {

        public void addNewOfferInfo(OfferInfo offerinfo)
        {
            if(offerinfo != null)
                listOfferInfo.add(offerinfo);
        }

        public void populateData()
        {
            companyId = (String)hashData.get("business_id");
        }

        public String companyId;
        public HashMap hashData;
        public ArrayList listOfferInfo;

        public CompanyInfo()
        {
            listOfferInfo = new ArrayList();
            hashData = new HashMap();
        }
    }

    public static class OfferInfo
    {

        public void populateData()
        {
            offerId = (String)hashData.get("offers_id");
            businessId = (String)hashData.get("business_id");
            branchId = (String)hashData.get("branch_id");
            outlet = (String)hashData.get("outlet");
            branchList = (String)hashData.get("branch_list");
        }

        public String branchId;
        public String branchList;
        public String businessId;
        public HashMap hashData;
        public String offerId;
        public String outlet;

        public OfferInfo()
        {
            hashData = new HashMap();
        }
    }


    public SittServerNode()
    {
    }

    public SittServerNode(String s, String s1, int i)
    {
        ssid = s;
        bssid = s1;
        floor = i;
    }

    public void addNewCompanyInfo(CompanyInfo companyinfo)
    {
        if(companyinfo == null);
    }

    public String getAddress()
    {
        return address;
    }

    public String getBssid()
    {
        return bssid;
    }

    public String getCompanyId()
    {
        return companyId;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }

    public int getFloor()
    {
        return floor;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public String getName()
    {
        return name;
    }

    public String getOffersId()
    {
        return offersId;
    }

    public String getSsid()
    {
        return ssid;
    }

    public String getWifiId()
    {
        return wifiId;
    }

    public void setAddress(String s)
    {
        address = s;
    }

    public void setBssid(String s)
    {
        bssid = s;
    }

    public void setCompanyId(String s)
    {
        companyId = s;
    }

    public void setCreatedTime(Date date)
    {
        createdTime = date;
    }

    public void setFloor(int i)
    {
        floor = i;
    }

    public void setLatitude(double d)
    {
        latitude = d;
    }

    public void setLongitude(double d)
    {
        longitude = d;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setOffersId(String s)
    {
        offersId = s;
    }

    public void setSsid(String s)
    {
        ssid = s;
    }

    public void setWifiId(String s)
    {
        wifiId = s;
    }

    private static final long serialVersionUID = 0xe1f3a787f3ab30f8L;
    public String address;
    public String bssid;
    public String companyId;
    public Date createdTime;
    public int floor;
    public double latitude;
    public double longitude;
    private String name;
    public String offersId;
    public String ssid;
    public String wifiId;
}
