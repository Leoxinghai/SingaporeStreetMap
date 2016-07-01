

package streetdirectory.mobile.modules.freevoucher;

import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.freevoucher:
//            VoucherListServiceOutput

public class VoucherListService extends SDHttpService
{

    public VoucherListService(String s)
    {
        super(new SDHttpServiceInput() {

            public String getURL()
            {
                return URLFactory.createURLVoucherList(countryCode);
            }

        }, VoucherListServiceOutput.class);
    }
}
