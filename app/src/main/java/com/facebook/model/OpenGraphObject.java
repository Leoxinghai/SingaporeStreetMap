// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.model;

import java.util.*;

// Referenced classes of package com.facebook.model:
//            GraphObject, GraphObjectList

public interface OpenGraphObject
    extends GraphObject
{
    public static final class Factory
    {

        public static OpenGraphObject createForPost(Class class1, String s)
        {
            return createForPost(class1, s, null, null, null, null);
        }

        public static OpenGraphObject createForPost(Class class1, String s, String s1, String s2, String s3, String s4)
        {
            OpenGraphObject temp = (OpenGraphObject)GraphObject.Factory.create(class1);
            if(s != null)
                temp.setType(s);
            if(s1 != null)
                temp.setTitle(s1);
            if(s2 != null)
                temp.setImageUrls(Arrays.asList(new String[] {
                    s2
                }));
            if(s3 != null)
                temp.setUrl(s3);
            if(s4 != null)
                temp.setDescription(s4);
            temp.setCreateObject(true);
            temp.setData(GraphObject.Factory.create());
            return temp;
        }

        public static OpenGraphObject createForPost(String s)
        {
            return createForPost(OpenGraphObject.class, s);
        }

        public Factory()
        {
        }
    }


    public abstract GraphObject getApplication();

    public abstract GraphObjectList getAudio();

    public abstract boolean getCreateObject();

    public abstract Date getCreatedTime();

    public abstract GraphObject getData();

    public abstract String getDescription();

    public abstract String getDeterminer();

    public abstract String getId();

    public abstract GraphObjectList getImage();

    public abstract boolean getIsScraped();

    public abstract String getPostActionId();

    public abstract List getSeeAlso();

    public abstract String getSiteName();

    public abstract String getTitle();

    public abstract String getType();

    public abstract Date getUpdatedTime();

    public abstract String getUrl();

    public abstract GraphObjectList getVideo();

    public abstract void setApplication(GraphObject graphobject);

    public abstract void setAudio(GraphObjectList graphobjectlist);

    public abstract void setCreateObject(boolean flag);

    public abstract void setCreatedTime(Date date);

    public abstract void setData(GraphObject graphobject);

    public abstract void setDescription(String s);

    public abstract void setDeterminer(String s);

    public abstract void setId(String s);

    public abstract void setImage(GraphObjectList graphobjectlist);

    public abstract void setImageUrls(List list);

    public abstract void setIsScraped(boolean flag);

    public abstract void setPostActionId(String s);

    public abstract void setSeeAlso(List list);

    public abstract void setSiteName(String s);

    public abstract void setTitle(String s);

    public abstract void setType(String s);

    public abstract void setUpdatedTime(Date date);

    public abstract void setUrl(String s);

    public abstract void setVideo(GraphObjectList graphobjectlist);
}
