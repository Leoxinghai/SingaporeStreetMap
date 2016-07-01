

package com.facebook.drawee.components;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class DraweeEventTracker
{
    public static enum Event
    {
        ON_SET_HIERARCHY("ON_SET_HIERARCHY", 0),
        ON_CLEAR_HIERARCHY("ON_CLEAR_HIERARCHY", 1),
        ON_SET_CONTROLLER("ON_SET_CONTROLLER", 2),
        ON_CLEAR_OLD_CONTROLLER("ON_CLEAR_OLD_CONTROLLER", 3),
        ON_CLEAR_CONTROLLER("ON_CLEAR_CONTROLLER", 4),
        ON_INIT_CONTROLLER("ON_INIT_CONTROLLER", 5),
        ON_ATTACH_CONTROLLER("ON_ATTACH_CONTROLLER", 6),
        ON_DETACH_CONTROLLER("ON_DETACH_CONTROLLER", 7),
        ON_RELEASE_CONTROLLER("ON_RELEASE_CONTROLLER", 8),
        ON_DATASOURCE_SUBMIT("ON_DATASOURCE_SUBMIT", 9),
        ON_DATASOURCE_RESULT("ON_DATASOURCE_RESULT", 10),
        ON_DATASOURCE_RESULT_INT("ON_DATASOURCE_RESULT_INT", 11),
        ON_DATASOURCE_FAILURE("ON_DATASOURCE_FAILURE", 12),
        ON_DATASOURCE_FAILURE_INT("ON_DATASOURCE_FAILURE_INT", 13),
        ON_HOLDER_ATTACH("ON_HOLDER_ATTACH", 14),
        ON_HOLDER_DETACH("ON_HOLDER_DETACH", 15),
        ON_DRAWABLE_SHOW("ON_DRAWABLE_SHOW", 16),
        ON_DRAWABLE_HIDE("ON_DRAWABLE_HIDE", 17),
        ON_ACTIVITY_START("ON_ACTIVITY_START", 18),
        ON_ACTIVITY_STOP("ON_ACTIVITY_STOP", 19);

        String sType;
        int iType;
        private Event(String s, int i)
        {
            sType = s;
            iType = i;

        }
    }


    public DraweeEventTracker()
    {
    }

    public void recordEvent(Event event)
    {
        if(mEventQueue.size() + 1 > 20)
            mEventQueue.poll();
        mEventQueue.add(event);
    }

    public String toString()
    {
        return mEventQueue.toString();
    }

    private static final int MAX_EVENTS_TO_TRACK = 20;
    private final Queue mEventQueue = new ArrayBlockingQueue(20);
}
