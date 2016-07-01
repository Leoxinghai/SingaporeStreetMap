package com.facebook.model;


// Referenced classes of package com.facebook.model:
//            GraphObject, GraphLocation

public interface GraphPlace
    extends GraphObject
{

    public abstract String getCategory();

    public abstract String getId();

    public abstract GraphLocation getLocation();

    public abstract String getName();

    public abstract void setCategory(String s);

    public abstract void setId(String s);

    public abstract void setLocation(GraphLocation graphlocation);

    public abstract void setName(String s);
}
