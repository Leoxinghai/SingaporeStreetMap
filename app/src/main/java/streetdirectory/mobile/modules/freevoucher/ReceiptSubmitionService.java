

package streetdirectory.mobile.modules.freevoucher;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.squareup.okhttp.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.UUID;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import streetdirectory.mobile.core.MainApplication;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.service.URLFactory;

public class ReceiptSubmitionService
{
    public static interface OnFailedListener
    {

        public abstract void onFailed();
    }

    public static interface OnSuccessListener
    {

        public abstract void onSuccess(boolean flag);
    }


    public ReceiptSubmitionService()
    {
    }

    public static void execute()
    {
        Object obj = UUID.randomUUID().toString();
        obj = (new MultipartBuilder()).type(MultipartBuilder.FORM).addPart(Headers.of(new String[] {
            "Content-Disposition", "form-data; name=\"offer_id\""
        }), RequestBody.create(null, offerId)).addPart(Headers.of(new String[] {
            "Content-Disposition", "form-data; name=\"name\""
        }), RequestBody.create(null, name)).addPart(Headers.of(new String[] {
            "Content-Disposition", "form-data; name=\"address\""
        }), RequestBody.create(null, address)).addPart(Headers.of(new String[] {
            "Content-Disposition", "form-data; name=\"email\""
        }), RequestBody.create(null, email)).addPart(Headers.of(new String[] {
            "Content-Disposition", "form-data; name=\"phone\""
        }), RequestBody.create(null, phone)).addPart(Headers.of(new String[] {
            "Content-Disposition", "form-data; name=\"voucher_id\""
        }), RequestBody.create(null, voucherId)).addPart(Headers.of(new String[] {
            "Content-Disposition", (new StringBuilder()).append("form-data; name=\"photo\"; filename=\"").append(((String) (obj))).append(".jpeg\"").toString()
        }), RequestBody.create(MEDIA_TYPE_JPEG, photo)).build();
        obj = (new com.squareup.okhttp.Request.Builder()).url(URLFactory.createURLReceiptSubmition(countryCode)).post(((RequestBody) (obj))).build();
        client.newCall(((Request) (obj))).enqueue(new Callback() {

                                                      public void onFailure(Request request, IOException ioexception) {
                                                          ReceiptSubmitionService.onFailed(new Exception("Response null"));
                                                      }

                                                      public void onResponse(Response response)
                                                              throws IOException {
                                                          if (response == null) {
                                                              ReceiptSubmitionService.onFailed(new Exception("Response null"));
                                                              return;
                                                          }
                                                          try {
                                                              SDLogger.debug(response.toString());
                                                              if (!response.isSuccessful()) {
                                                                  ReceiptSubmitionService.onFailed(new IOException((new StringBuilder()).append("Unexpected code ").append(response).toString()));
                                                                  return;
                                                              }
                                                              String temp = response.body().string();
                                                              SDLogger.debug(temp);
                                                              ReceiptSubmitionService.parseResult(temp);
                                                              return;
                                                          } catch (Exception ex) {
                                                              ReceiptSubmitionService.onFailed(ex);
                                                              return;
                                                          }
                                                      }

                                                  }
        );
    }

    protected static void onFailed(Exception exception)
    {
        Intent intent = new Intent("redeem_voucher");
        LocalBroadcastManager.getInstance(MainApplication.getAppContext()).sendBroadcast(intent);
        if(onFailedListener != null)
            onFailedListener.onFailed();
        exception.printStackTrace();
    }

    protected static void onSuccess(boolean flag)
    {
        if(!flag)
        {
            Intent intent = new Intent("redeem_voucher");
            LocalBroadcastManager.getInstance(MainApplication.getAppContext()).sendBroadcast(intent);
        }
        if(onSuccessListener != null)
            onSuccessListener.onSuccess(flag);
    }

    private static void parseResult(String s)
    {
        try
        {
            if(s == null)
            {
                onFailed(new Exception("Failed to get result from server"));
                return;
            }
            Object obj1;
            DocumentBuilder documentbuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource inputsource = new InputSource();
            inputsource.setCharacterStream(new StringReader(s));
            obj1 = (Element)documentbuilder.parse(inputsource).getElementsByTagName("data").item(0);
            String s1;
            Object obj;
            s1 = null;
            obj = null;
            Object obj2 = ((Element) (obj1)).getElementsByTagName("result");
            s = s1;
            if(obj2 != null) {
                obj2 = ((Element) ((NodeList) (obj2)).item(0)).getFirstChild();
                s = s1;
                if (obj2 != null)
                    s = ((Node) (obj2)).getNodeValue();
            }
            obj1 = ((Element) (obj1)).getElementsByTagName("img_url");
            s1 = null;
            if(obj1 != null) {

                obj1 = ((Element) ((NodeList) (obj1)).item(0)).getFirstChild();
                s1 = null;
                if (obj1 != null)
                    s1 = ((Node) (obj1)).getNodeValue();
            }
            boolean flag;
            boolean flag1;
            flag1 = false;
            flag = flag1;
            if(s != null) {
                flag = flag1;
                if (s.equals("1")) {
                    flag = flag1;
                    if (s1 != null)
                        flag = true;
                }
                onSuccess(flag);
                return;
            }
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            onFailed(ex);
            return;
        }
        return;
    }

    public static void post(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7,
            byte abyte0[], OnSuccessListener onsuccesslistener, OnFailedListener onfailedlistener)
    {
        offerId = s;
        countryCode = s1;
        name = s2;
        address = s3;
        email = s4;
        photo = abyte0;
        phone = s5;
        voucherId = s6;
        filename = s7;
        onSuccessListener = onsuccesslistener;
        onFailedListener = onfailedlistener;
        execute();
    }

    public static void setOnFailedListener(OnFailedListener onfailedlistener)
    {
        onFailedListener = onfailedlistener;
    }

    public static void setOnSuccessListener(OnSuccessListener onsuccesslistener)
    {
        onSuccessListener = onsuccesslistener;
    }

    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    private static final MediaType MIME_STREAM = MediaType.parse("application/octet-stream");
    private static String address;
    private static final OkHttpClient client = new OkHttpClient();
    private static String countryCode;
    private static String email;
    private static String filename;
    private static String name;
    private static String offerId;
    private static OnFailedListener onFailedListener;
    private static OnSuccessListener onSuccessListener;
    private static String phone;
    private static byte photo[];
    private static String voucherId;


}
