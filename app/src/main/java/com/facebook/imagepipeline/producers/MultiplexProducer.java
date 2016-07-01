// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import android.util.Pair;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Sets;
import com.facebook.imagepipeline.common.Priority;
import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerContext, Consumer, BaseProducerContext,
//            BaseProducerContextCallbacks, BaseConsumer

public abstract class MultiplexProducer
    implements Producer
{
    class Multiplexer
    {

        private void addCallbacks(final Pair pair, ProducerContext producercontext)
        {
            producercontext.addCallbacks(new BaseProducerContextCallbacks() {

                public void onCancellationRequested() {
                    Object obj;
                    Object obj1;
                    Object obj2;
                    Object obj3;
                    obj = null;
                    obj1 = null;
                    obj2 = null;
                    obj3 = null;
                    Multiplexer multiplexer = Multiplexer.this;
                    boolean flag = mConsumerContextPairs.remove(pair);
                    BaseProducerContext baseproducercontext;
                    List list;
                    List list1;
                    List list2;
                    baseproducercontext = null;
                    list = null;
                    list1 = null;
                    list2 = null;
                    if (flag) {
                        if (!mConsumerContextPairs.isEmpty()) {
                            list1 = updateIsPrefetch();
                            list2 = updatePriority();
                            list = updateIsIntermediateResultExpected();
                            baseproducercontext = null;
                        } else {
                            baseproducercontext = mMultiplexProducerContext;
                            list2 = null;
                            list1 = null;
                            list = null;
                        }
                    }

                    BaseProducerContext.callOnIsPrefetchChanged(list1);
                    BaseProducerContext.callOnPriorityChanged(list2);
                    BaseProducerContext.callOnIsIntermediateResultExpectedChanged(list);
                    if (baseproducercontext != null)
                        baseproducercontext.cancel();
                    if (flag)
                        ((Consumer) pair.first).onCancellation();
                    return;
                }

                public void onIsIntermediateResultExpectedChanged() {
                    BaseProducerContext.callOnIsIntermediateResultExpectedChanged(updateIsIntermediateResultExpected());
                }

                public void onIsPrefetchChanged() {
                    BaseProducerContext.callOnIsPrefetchChanged(updateIsPrefetch());
                }

                public void onPriorityChanged() {
                    BaseProducerContext.callOnPriorityChanged(updatePriority());
                }
            });
        }

        private void closeSafely(Closeable closeable)
        {
            if(closeable == null)
                return;
            try {
                closeable.close();
                return;
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        private boolean computeIsIntermediateResultExpected()
        {
            Iterator iterator = mConsumerContextPairs.iterator();
            boolean flag = false;
            for(;iterator.hasNext();) {
				flag = ((ProducerContext)((Pair)iterator.next()).second).isIntermediateResultExpected();
				if(flag) {
					flag = true;
					break;
				}
			}
            return flag;
        }

        private boolean computeIsPrefetch()
        {
            Iterator iterator = mConsumerContextPairs.iterator();
            boolean flag = true;
            for(;iterator.hasNext();) {
				flag = ((ProducerContext)((Pair)iterator.next()).second).isPrefetch();
				if(!flag) {
                    flag = false;
                    break;
                }
			}
            return flag;
        }

        private Priority computePriority()
        {
            Priority priority;
            priority = Priority.LOW;
            for(Iterator iterator = mConsumerContextPairs.iterator(); iterator.hasNext();)
                priority = Priority.getHigherPriority(priority, ((ProducerContext)((Pair)iterator.next()).second).getPriority());


            return priority;
        }

        private void startInputProducerIfHasAttachedConsumers()
        {
            boolean flag1 = true;
            Object obj;
            Exception exception;
            ForwardingConsumer forwardingconsumer;
            boolean flag;
            if(mMultiplexProducerContext == null)
                flag = true;
            else
                flag = false;
            Preconditions.checkArgument(flag);
            if(mForwardingConsumer == null)
                flag = flag1;
            else
                flag = false;
            Preconditions.checkArgument(flag);
            if(!mConsumerContextPairs.isEmpty()) {
                obj = (ProducerContext)((Pair)mConsumerContextPairs.iterator().next()).second;
                mMultiplexProducerContext = new BaseProducerContext(((ProducerContext) (obj)).getImageRequest(), ((ProducerContext) (obj)).getId(), ((ProducerContext) (obj)).getListener(), ((ProducerContext) (obj)).getCallerContext(), ((ProducerContext) (obj)).getLowestPermittedRequestLevel(), computeIsPrefetch(), computeIsIntermediateResultExpected(), computePriority());
                mForwardingConsumer = new ForwardingConsumer();
                obj = mMultiplexProducerContext;
                forwardingconsumer = mForwardingConsumer;

                mInputProducer.produceResults(forwardingconsumer, ((ProducerContext) (obj)));
                return;

            }
            removeMultiplexer(mKey, this);

            return;
        }

        private List updateIsIntermediateResultExpected()
        {
            Object obj = mMultiplexProducerContext;
            if(obj != null) {
	            obj = mMultiplexProducerContext.setIsIntermediateResultExpectedNoCallbacks(computeIsIntermediateResultExpected());
			} else
            	obj = null;


            return ((List) (obj));
        }

        private List updateIsPrefetch()
        {
            Object obj = mMultiplexProducerContext;
            if(obj != null) {
				obj = mMultiplexProducerContext.setIsPrefetchNoCallbacks(computeIsPrefetch());
			} else
            	obj = null;

            return ((List) (obj));
        }

        private List updatePriority()
        {
            Object obj = mMultiplexProducerContext;
            if(obj != null) {
				obj = mMultiplexProducerContext.setPriorityNoCallbacks(computePriority());
			} else
            	obj = null;

            return ((List) (obj));
        }

        public boolean addNewConsumer(Consumer consumer, ProducerContext producercontext)
        {
            Pair pair = Pair.create(consumer, producercontext);
            if(getExistingMultiplexer(mKey) != this)
				return false;
            float f;
            Object obj;
            Closeable closeable;
            List list;
            List list1;
            mConsumerContextPairs.add(pair);
            obj = updateIsPrefetch();
            list = updatePriority();
            list1 = updateIsIntermediateResultExpected();
            closeable = mLastIntermediateResult;
            f = mLastProgress;

            BaseProducerContext.callOnIsPrefetchChanged(((List) (obj)));
            BaseProducerContext.callOnPriorityChanged(list);
            BaseProducerContext.callOnIsIntermediateResultExpectedChanged(list1);
            if(closeable == mLastIntermediateResult) {
				obj = closeable;
				if(closeable != null)
					obj = cloneOrNull(closeable);
			} else
            	obj = null;


            if(obj != null) {
				if(f > 0.0F) {
					consumer.onProgressUpdate(f);
					consumer.onNewResult(obj, false);
					closeSafely(((Closeable) (obj)));
					addCallbacks(pair, producercontext);
					return true;
				}
			}
			return false;

        }

        public void onCancelled(ForwardingConsumer forwardingconsumer)
        {
            if(mForwardingConsumer != forwardingconsumer)
	            return;
            mForwardingConsumer = null;
            mMultiplexProducerContext = null;
            closeSafely(mLastIntermediateResult);
            mLastIntermediateResult = null;

            startInputProducerIfHasAttachedConsumers();
            return;
        }

        public void onFailure(ForwardingConsumer forwardingconsumer, Throwable throwable)
        {
            if(mForwardingConsumer != forwardingconsumer)
	            return;
            Iterator iterator;
            iterator = mConsumerContextPairs.iterator();
            mConsumerContextPairs.clear();
            removeMultiplexer(mKey, this);
            closeSafely(mLastIntermediateResult);
            mLastIntermediateResult = null;


            Pair temp;
            for(;iterator.hasNext();) {
                temp = (Pair) iterator.next();
                synchronized (temp) {
                    ((Consumer) temp.first).onFailure(throwable);
                }
            }

        }

        public void onNextResult(ForwardingConsumer forwardingconsumer, Closeable closeable, boolean flag)
        {
            if(mForwardingConsumer != forwardingconsumer)
	            return;
            Iterator iterator;
            closeSafely(mLastIntermediateResult);
            mLastIntermediateResult = null;
            iterator = mConsumerContextPairs.iterator();
            if(flag) {
				mConsumerContextPairs.clear();
				removeMultiplexer(mKey, this);
			} else
            	mLastIntermediateResult = cloneOrNull(closeable);

            Pair temp;
            for(;iterator.hasNext();) {
                temp = (Pair) iterator.next();
                synchronized (temp) {
                    ((Consumer) temp.first).onNewResult(closeable, flag);
                }
            }
        }

        public void onProgressUpdate(ForwardingConsumer forwardingconsumer, float f)
        {
            if(mForwardingConsumer != forwardingconsumer)
	            return;
            Iterator iterator;
            mLastProgress = f;
            iterator = mConsumerContextPairs.iterator();


            Pair temp;
            for(;iterator.hasNext();) {
                temp = (Pair) iterator.next();
                synchronized (temp) {
                    ((Consumer) temp.first).onProgressUpdate(f);
                }
            }

        }

        private final CopyOnWriteArraySet mConsumerContextPairs = Sets.newCopyOnWriteArraySet();
        private ForwardingConsumer mForwardingConsumer;
        private final Object mKey;
        private Closeable mLastIntermediateResult;
        private float mLastProgress;
        private BaseProducerContext mMultiplexProducerContext;
        public Multiplexer(Object obj)
        {
            super();
            mKey = obj;
        }

        private class ForwardingConsumer extends BaseConsumer
        {

            protected void onCancellationImpl()
            {
                onCancelled(this);
            }

            protected void onFailureImpl(Throwable throwable)
            {
                onFailure( throwable);
            }

            protected void onNewResultImpl(Closeable closeable, boolean flag)
            {
                onNextResult(this, closeable, flag);
            }

            protected void onNewResultImpl(Object obj, boolean flag)
            {
                onNewResultImpl((Closeable)obj, flag);
            }

            protected void onProgressUpdateImpl(float f)
            {
                onProgressUpdate( f);
            }


            private ForwardingConsumer()
            {
                super();
            }

        }

    }



    protected MultiplexProducer(Producer producer)
    {
        mInputProducer = producer;
    }

    private Multiplexer createAndPutNewMultiplexer(Object obj)
    {
        Multiplexer multiplexer;
        multiplexer = new Multiplexer(obj);
        mMultiplexers.put(obj, multiplexer);
        return multiplexer;
    }

    private Multiplexer getExistingMultiplexer(Object obj)
    {
        obj = (Multiplexer)mMultiplexers.get(obj);
        return ((Multiplexer) (obj));
    }

    private void removeMultiplexer(Object obj, Multiplexer multiplexer)
    {
        if(mMultiplexers.get(obj) == multiplexer)
            mMultiplexers.remove(obj);
        return;
    }

    protected abstract Closeable cloneOrNull(Closeable closeable);

    protected abstract Object getKey(ProducerContext producercontext);

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
        Object obj = getKey(producercontext);
        boolean flag = false;
        Multiplexer multiplexer = null;

        for(;;) {
			Multiplexer multiplexer1 = getExistingMultiplexer(obj);
			multiplexer = multiplexer1;
			if(multiplexer1 == null)
				multiplexer = createAndPutNewMultiplexer(obj);
			flag = true;
			if(multiplexer.addNewConsumer(consumer, producercontext))
				break;
		}

        if(flag)
            multiplexer.startInputProducerIfHasAttachedConsumers();
        return;
    }

    private final Producer mInputProducer;
    final Map mMultiplexers = new HashMap();



}
