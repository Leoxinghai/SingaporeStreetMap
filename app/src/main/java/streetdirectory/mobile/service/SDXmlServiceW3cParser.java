// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.service;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import streetdirectory.mobile.core.service.*;

// Referenced classes of package streetdirectory.mobile.service:
//            SDXmlServiceW3cOutput, SDErrorOutput

public abstract class SDXmlServiceW3cParser
    implements HttpServiceResultHandler
{

    public SDXmlServiceW3cParser(Class class1)
    {
        mBuffer = new StringBuilder();
        mLastLevel = 0;
        mCurrentLevel = 0;
        mOutputClass = class1;
    }

    public void abort()
    {
        mIsCanceled = true;
    }

    protected abstract void onFailed(Exception exception);

    protected abstract void onSuccess(SDXmlServiceW3cOutput sdxmlservicew3coutput);

    public SDXmlServiceW3cOutput parse(InputStream inputstream)
    {
        try
        {
            mIsCanceled = false;
            mOutput = (SDXmlServiceW3cOutput)mOutputClass.newInstance();
            mCurrentData = mOutput;
            DocumentBuilder documentbuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            mOutput.xml = documentbuilder.parse(new InputSource(inputstream));
            mOutput.populateData();
            if(mIsCanceled) {
                onFailed(new SAXParserAbortException());
            } else
                onSuccess(mOutput);
        } catch(Exception ex)
        {
            if(mIsCanceled)
                onFailed(new SAXParserAbortException());
            else
                onFailed(ex);
        }

        return mOutput;
    }

    protected StringBuilder mBuffer;
    protected SDXmlServiceW3cOutput mCurrentData;
    int mCurrentLevel;
    protected boolean mIsCanceled;
    int mLastLevel;
    protected SDXmlServiceW3cOutput mOutput;
    Class mOutputClass;
    protected SDXmlServiceW3cOutput mParentData;
    protected SDErrorOutput mmCurrentError;
}
