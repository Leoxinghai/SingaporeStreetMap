

package streetdirectory.mobile.modules.freevoucher;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class VoucherListServiceOutput extends SDDataOutput
{

    public VoucherListServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        voucherId = (String)hashData.get("vid");
        voucherName = (String)hashData.get("vnm");
    }

    private static final long serialVersionUID = 0x7bfe4e74734c0fcL;
    public String voucherId;
    public String voucherName;
}
