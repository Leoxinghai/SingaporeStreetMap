

package com.facebook.common.internal;


// Referenced classes of package com.facebook.common.internal:
//            Supplier

public class Suppliers
{

    public Suppliers()
    {
    }

    public static Supplier of(final Object obj)
    {

        return new Supplier() {
            public Object get()
            {
                return obj;
            }
        };
    }
}
