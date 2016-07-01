

package com.facebook.common.internal;

import java.util.ArrayList;
import java.util.List;

public class ImmutableList extends ArrayList
{

    private ImmutableList(List list)
    {
        super(list);
    }

    public static ImmutableList copyOf(List list)
    {
        return new ImmutableList(list);
    }
}
