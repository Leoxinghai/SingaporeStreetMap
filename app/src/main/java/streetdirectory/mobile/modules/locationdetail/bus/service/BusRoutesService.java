// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.bus.service;

import android.os.Handler;
import streetdirectory.mobile.core.service.*;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.bus.service:
//            BusRoutesServiceOutput, BusRoutesServiceInput, BusRouteXMLParserHandler

public class BusRoutesService extends SDHttpService {

    public BusRoutesService(BusRoutesServiceInput busroutesserviceinput) {
        super(busroutesserviceinput, BusRoutesServiceOutput.class);
        initialize();
    }

    private void initialize() {
        _parser = new BusRouteXMLParserHandler() {

            public void onFailed(Exception exception) {
            }

            public void onReceiveData(BusRoutesServiceOutput busroutesserviceoutput) {
                BusRoutesService.this.onReceiveData(busroutesserviceoutput);
            }

            public void onReceiveData(SDDataOutput sddataoutput) {
                onReceiveData((BusRoutesServiceOutput) sddataoutput);
            }

            public void onReceiveError(SDErrorOutput sderroroutput) {
            }
            public void onReceiveTotal(long l) {
                BusRoutesService.this.onReceiveTotal(l);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput) {
            }

        };
    }



        public void onReceiveTotal(long l)
        {
        }

        }
