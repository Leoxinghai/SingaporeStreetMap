

package com.facebook.imagepipeline.producers;

import java.util.Map;

public interface ProducerListener
{

    public abstract void onProducerEvent(String s, String s1, String s2);

    public abstract void onProducerFinishWithCancellation(String s, String s1, Map map);

    public abstract void onProducerFinishWithFailure(String s, String s1, Throwable throwable, Map map);

    public abstract void onProducerFinishWithSuccess(String s, String s1, Map map);

    public abstract void onProducerStart(String s, String s1);

    public abstract boolean requiresExtraMap(String s);
}
