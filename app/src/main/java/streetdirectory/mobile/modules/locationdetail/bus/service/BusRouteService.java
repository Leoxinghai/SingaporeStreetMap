

package streetdirectory.mobile.modules.locationdetail.bus.service;

import java.util.ArrayList;
import org.w3c.dom.*;
import streetdirectory.mobile.core.StringTools;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.service.SDXmlServiceW3cOutput;

public class BusRouteService extends SDXmlServiceW3cOutput
{
    public static class Info
    {

        public void populateData(Element element)
        {
        }

        public ArrayList journeys;
        public ArrayList roads;

        public Info()
        {
            journeys = new ArrayList();
            roads = new ArrayList();
        }
    }

    public static class Journey
    {

        public ArrayList datas;

        public Journey()
        {
            datas = new ArrayList();
        }
    }

    public static class JourneyData
    {

        public void populateData(Element element)
        {
            longitude = StringTools.tryParseDouble(element.getElementsByTagName("x").item(0).getNodeValue(), 0.0D);
            latitude = StringTools.tryParseDouble(element.getElementsByTagName("y").item(0).getNodeValue(), 0.0D);
            title = element.getElementsByTagName("title").item(0).getNodeValue();
            desc = element.getElementsByTagName("desc").item(0).getNodeValue();
        }

        public String desc;
        public double latitude;
        public double longitude;
        public String title;

        public JourneyData()
        {
        }
    }

    public static class Line
    {

        public void populateData(Element element)
        {
            if(element != null)
            {
                NodeList nodelist = element.getElementsByTagName("color");
                NodeList nodeList = element.getElementsByTagName("pt");
                for(int i = 0; i < nodelist.getLength(); i++)
                    color = StringTools.tryParseInt(nodelist.item(i).getNodeValue(), 0);

                for(int j = 0; j < nodeList.getLength(); j++)
                {
                    Element element1 = (Element)nodeList.item(j);
                    double d = StringTools.tryParseDouble(element1.getElementsByTagName("x").item(0).getNodeValue(), 0.0D);
                    double d1 = StringTools.tryParseDouble(element1.getElementsByTagName("y").item(0).getNodeValue(), 0.0D);
                    points.add(new GeoPoint(d, d1));
                }

            }
        }

        public int color;
        public ArrayList points;

        public Line()
        {
            points = new ArrayList();
        }
    }

    public static class Road
    {

        public String direction;
        public String end;
        public String start;
        public String title;

        public Road()
        {
        }
    }

    public static class Route
    {

        public void populateData(Element element)
        {
            if(element != null)
            {
                NodeList nodeList = element.getElementsByTagName("lines");
                for(int i = 0; i < nodeList.getLength(); i++)
                {
                    Line line = new Line();
                    line.populateData((Element)nodeList.item(i));
                    lines.add(line);
                }

            }
        }

        public ArrayList lines;

        public Route()
        {
            lines = new ArrayList();
        }
    }


    public BusRouteService()
    {
        routes = new ArrayList();
    }

    public void populateData()
    {
        NodeList nodelist = xml.getElementsByTagName("routes");
        for(int i = 0; i < nodelist.getLength(); i++)
        {
            Route route = new Route();
            route.populateData((Element)nodelist.item(i));
            routes.add(route);
        }

        nodelist = xml.getElementsByTagName("sub");
        start = new JourneyData();
        start.populateData((Element)nodelist.item(0));
        end = new JourneyData();
        end.populateData((Element)nodelist.item(1));
    }

    public JourneyData end;
    public Info info;
    public ArrayList routes;
    public JourneyData start;
}
