

package com.facebook.common.internal;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

// Referenced classes of package com.facebook.common.internal:
//            Preconditions

public final class Throwables
{

    private Throwables()
    {
    }

    public static List getCausalChain(Throwable throwable)
    {
        Preconditions.checkNotNull(throwable);
        ArrayList arraylist = new ArrayList(4);
        for(; throwable != null; throwable = throwable.getCause())
            arraylist.add(throwable);

        return Collections.unmodifiableList(arraylist);
    }

    public static Throwable getRootCause(Throwable throwable)
    {
        do
        {
            Throwable throwable1 = throwable.getCause();
            if(throwable1 != null)
                throwable = throwable1;
            else
                return throwable;
        } while(true);
    }

    public static String getStackTraceAsString(Throwable throwable)
    {
        StringWriter stringwriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringwriter));
        return stringwriter.toString();
    }

    public static RuntimeException propagate(Throwable throwable) throws Throwable
    {
        propagateIfPossible((Throwable)Preconditions.checkNotNull(throwable));
        throw new RuntimeException(throwable);
    }

    public static void propagateIfInstanceOf(Throwable throwable, Class class1)
        throws Throwable
    {
        if(throwable != null && class1.isInstance(throwable))
            throw (Throwable)class1.cast(throwable);
        else
            return;
    }

    public static void propagateIfPossible(Throwable throwable) throws Throwable
    {
        propagateIfInstanceOf(throwable, Error.class);
        propagateIfInstanceOf(throwable, RuntimeException.class);
    }

    public static void propagateIfPossible(Throwable throwable, Class class1)
        throws Throwable
    {
        propagateIfInstanceOf(throwable, class1);
        propagateIfPossible(throwable);
    }

    public static void propagateIfPossible(Throwable throwable, Class class1, Class class2)
        throws Throwable, Throwable
    {
        Preconditions.checkNotNull(class2);
        propagateIfInstanceOf(throwable, class1);
        propagateIfPossible(throwable, class2);
    }
}
