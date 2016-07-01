

package com.facebook.model;

import java.util.List;
import org.json.JSONArray;

public interface GraphObjectList
    extends List
{

    public abstract GraphObjectList castToListOf(Class class1);

    public abstract JSONArray getInnerJSONArray();
}
