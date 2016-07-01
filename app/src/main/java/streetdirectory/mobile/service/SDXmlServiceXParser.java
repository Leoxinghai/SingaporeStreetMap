// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.service;

import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import streetdirectory.mobile.core.service.*;

// Referenced classes of package streetdirectory.mobile.service:
//            SDXmlServiceXOutput, SDErrorOutput

public abstract class SDXmlServiceXParser extends DefaultHandler
    implements HttpServiceResultHandler
{

    public SDXmlServiceXParser(Class class1)
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

    public void characters(char ac[], int i, int j)
        throws SAXException
    {
        try
        {
            if(mBuffer != null)
                mBuffer.append(ac, i, j);
            return;
        } catch(Exception ex) {
            if(mIsCanceled)
                throw new SAXParserAbortException();
            else
                throw new SAXException(ex);
        }
    }

    public void endElement(String s, String s1, String s2)
        throws SAXException
    {
        if(mCurrentData != null) {
            mCurrentData.value = mBuffer.toString();
            mParentData = mCurrentData.parent;
            mCurrentData = null;
        } else {
            try
            {
                mParentData = mParentData.parent;
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                ex.printStackTrace();
                if(mIsCanceled)
                    throw new SAXParserAbortException();
                else
                    throw new SAXException(ex);
            }
        }

        mCurrentLevel = mCurrentLevel - 1;
        mBuffer = null;
        return;
    }

    protected abstract void onFailed(Exception exception);

    protected abstract void onSuccess(SDXmlServiceXOutput sdxmlservicexoutput);


    public SDXmlServiceXOutput parse(InputStream inputstream)
    {
        try
        {
            mIsCanceled = false;
            mOutput = (SDXmlServiceXOutput)mOutputClass.newInstance();
            mCurrentData = mOutput;
            XMLReader xmlreader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            xmlreader.setContentHandler(this);
            xmlreader.parse(new InputSource(inputstream));
            if(mIsCanceled) {
                onFailed(new SAXParserAbortException());
            } else
                onSuccess(mOutput);
        }
        catch(Exception  ex)
        {
            if(mIsCanceled)
                onFailed(new SAXParserAbortException());
            else
                onFailed(ex);
        }

        return mOutput;
    }

    public void startElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        int i;
        try
        {
            mCurrentLevel = mCurrentLevel + 1;
            if(mCurrentData != null)
                mParentData = mCurrentData;
            if(mCurrentLevel == 1)
                mCurrentData = mOutput;
            mCurrentData = (SDXmlServiceXOutput)mOutputClass.newInstance();
            if(mParentData != null)
            {
                mCurrentData.parent = mParentData;
                mParentData.childs.add(mCurrentData);
            }
            mCurrentData.name = s2;
            i = 0;
            for(;i < attributes.getLength();) {
                SDXmlServiceXOutput.Attribute attr = new SDXmlServiceXOutput.Attribute();
                attr.name = attributes.getLocalName(i);
                attr.value = attributes.getValue(i);
                mCurrentData.attributes.add(attr);
                i++;
            }
            mBuffer = new StringBuilder();
            return;
        }
        catch(Exception ex)
        {
            if(mIsCanceled)
                throw new SAXParserAbortException();
            else
                throw new SAXException(ex);
        }
    }

    protected StringBuilder mBuffer;
    protected SDXmlServiceXOutput mCurrentData;
    int mCurrentLevel;
    protected boolean mIsCanceled;
    int mLastLevel;
    protected SDXmlServiceXOutput mOutput;
    Class mOutputClass;
    protected SDXmlServiceXOutput mParentData;
    protected SDErrorOutput mmCurrentError;
}
