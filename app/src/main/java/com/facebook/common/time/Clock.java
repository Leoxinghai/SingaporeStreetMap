

package com.facebook.common.time;


public interface Clock
{

    public abstract long now();

    public static final long MAX_TIME = 0x7fffffffffffffffL;
}
