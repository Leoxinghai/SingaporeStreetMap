

package com.facebook.common.internal;

import com.android.internal.util.Predicate;

public class AndroidPredicates
{

    private AndroidPredicates()
    {
    }

    public static Predicate False()
    {
        return new Predicate() {

            public boolean apply(Object obj)
            {
                return false;
            }

        }
;
    }

    public static Predicate True()
    {
        return new Predicate() {

            public boolean apply(Object obj)
            {
                return true;
            }

        }
;
    }
}
