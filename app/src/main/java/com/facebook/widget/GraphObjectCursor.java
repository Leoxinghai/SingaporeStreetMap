

package com.facebook.widget;

import com.facebook.model.GraphObject;

interface GraphObjectCursor
{

    public abstract boolean areMoreObjectsAvailable();

    public abstract void close();

    public abstract int getCount();

    public abstract GraphObject getGraphObject();

    public abstract int getPosition();

    public abstract boolean isAfterLast();

    public abstract boolean isBeforeFirst();

    public abstract boolean isClosed();

    public abstract boolean isFirst();

    public abstract boolean isFromCache();

    public abstract boolean isLast();

    public abstract boolean move(int i);

    public abstract boolean moveToFirst();

    public abstract boolean moveToLast();

    public abstract boolean moveToNext();

    public abstract boolean moveToPosition(int i);

    public abstract boolean moveToPrevious();
}
