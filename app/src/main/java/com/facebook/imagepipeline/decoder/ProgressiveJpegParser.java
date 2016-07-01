

package com.facebook.imagepipeline.decoder;

import com.facebook.common.internal.*;
import com.facebook.common.util.StreamUtil;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.ByteArrayPool;
import com.facebook.imagepipeline.memory.PooledByteArrayBufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProgressiveJpegParser
{

    public ProgressiveJpegParser(ByteArrayPool bytearraypool)
    {
        mByteArrayPool = (ByteArrayPool)Preconditions.checkNotNull(bytearraypool);
        mBytesParsed = 0;
        mLastByteRead = 0;
        mNextFullScanNumber = 0;
        mBestScanEndOffset = 0;
        mBestScanNumber = 0;
        mParserState = 0;
    }

    private boolean doParseMoreData(InputStream inputstream) {
        int i = mBestScanNumber;
        int j;
        try {
            for (; mParserState != 6;) {
                j = inputstream.read();
                if(j == -1)
                    break;
                mBytesParsed = mBytesParsed + 1;
                switch (mParserState) {
                    default:
                        Preconditions.checkState(false);
                        break;
                    case 0:
                        if (j != 255)
                            mParserState = 6;
                        else
                            mParserState = 1;
                        break;
                    case 1:
                        if (j != 216)
                            mParserState = 6;
                        else
                            mParserState = 2;
                        break;
                    case 2:
                        if (j == 255)
                            mParserState = 3;
                        break;
                    case 3:
                        if (j == 255) {
                            mParserState = 3;
                        } else if (j != 0) {
                            if (j != 218 && j != 217) {
                                if (!doesMarkerStartSegment(j)) {
                                    mParserState = 2;
                                } else {
                                    mParserState = 4;
                                }
                            } else {
                                newScanOrImageEndFound(mBytesParsed - 2);
                                mParserState = 4;
                            }
                        } else {
                            mParserState = 2;
                        }
                        break;
                    case 4:
                        mParserState = 5;
                        break;
                    case 5:
                        int k = ((mLastByteRead << 8) + j) - 2;
                        StreamUtil.skip(inputstream, k);
                        mBytesParsed = mBytesParsed + k;
                        mParserState = 2;
                        break;
                }
                mLastByteRead = j;
            }

        } catch(IOException iex) {
            iex.printStackTrace();
        }
        return mParserState != 6 && mBestScanNumber != i;
    }

    private static boolean doesMarkerStartSegment(int i)
    {
        boolean flag;
        for(flag = true; i == 1 || i >= 208 && i <= 215;)
            return false;

        if(i == 217 || i == 216)
            flag = false;
        return flag;
    }

    private void newScanOrImageEndFound(int i)
    {
        if(mNextFullScanNumber > 0)
            mBestScanEndOffset = i;
        i = mNextFullScanNumber;
        mNextFullScanNumber = i + 1;
        mBestScanNumber = i;
    }

    public int getBestScanEndOffset()
    {
        return mBestScanEndOffset;
    }

    public int getBestScanNumber()
    {
        return mBestScanNumber;
    }

    public boolean isJpeg()
    {
        return mBytesParsed > 1 && mParserState != 6;
    }

    public boolean parseMoreData(EncodedImage encodedimage)
    {
        if(mParserState == 6)
            return false;
        if(encodedimage.getSize() <= mBytesParsed)
            return false;
        PooledByteArrayBufferedInputStream temp = new PooledByteArrayBufferedInputStream(encodedimage.getInputStream(), (byte[])mByteArrayPool.get(16384), mByteArrayPool);
        boolean flag;
        try {
            StreamUtil.skip(temp, mBytesParsed);
            flag = doParseMoreData(temp);
            Closeables.closeQuietly(temp);
            return flag;

        } catch(Exception ex) {
            //Throwables.propagate(((Throwable) (ex)));
            Closeables.closeQuietly(temp);
            return false;
        }

    }

    private static final int BUFFER_SIZE = 16384;
    private static final int NOT_A_JPEG = 6;
    private static final int READ_FIRST_JPEG_BYTE = 0;
    private static final int READ_MARKER_FIRST_BYTE_OR_ENTROPY_DATA = 2;
    private static final int READ_MARKER_SECOND_BYTE = 3;
    private static final int READ_SECOND_JPEG_BYTE = 1;
    private static final int READ_SIZE_FIRST_BYTE = 4;
    private static final int READ_SIZE_SECOND_BYTE = 5;
    private int mBestScanEndOffset;
    private int mBestScanNumber;
    private final ByteArrayPool mByteArrayPool;
    private int mBytesParsed;
    private int mLastByteRead;
    private int mNextFullScanNumber;
    private int mParserState;
}
