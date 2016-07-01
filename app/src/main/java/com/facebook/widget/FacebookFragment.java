// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.facebook.*;
import com.facebook.internal.SessionAuthorizationType;
import com.facebook.internal.SessionTracker;
import java.util.Date;
import java.util.List;

class FacebookFragment extends Fragment
{
    private class DefaultSessionStatusCallback
        implements com.facebook.Session.StatusCallback
    {

        public void call(Session session, SessionState sessionstate, Exception exception)
        {
            onSessionStateChange(sessionstate, exception);
        }


        private DefaultSessionStatusCallback()
        {
            super();
        }

    }


    FacebookFragment()
    {
    }

    private void openSession(String s, List list, SessionLoginBehavior sessionloginbehavior, int i, SessionAuthorizationType sessionauthorizationtype)
    {
        Session session;
        if(sessionTracker == null)
            return;
        Session session1 = sessionTracker.getSession();
        session = session1;
        if(session1.getState().isClosed()) {

            session = (new com.facebook.Session.Builder(getActivity())).setApplicationId(s).build();
            Session.setActiveSession(session);
        }
        if(!session.isOpened())
        {
            Session.OpenRequest openRequest = (new com.facebook.Session.OpenRequest(this)).setPermissions(list).setLoginBehavior(sessionloginbehavior).setRequestCode(i);
            if(!SessionAuthorizationType.PUBLISH.equals(sessionauthorizationtype))
                session.openForRead(openRequest);
            session.openForPublish(openRequest);
        }
        return;
    }

    protected final void closeSession()
    {
        if(sessionTracker != null)
        {
            Session session = sessionTracker.getOpenSession();
            if(session != null)
                session.close();
        }
    }

    protected final void closeSessionAndClearTokenInformation()
    {
        if(sessionTracker != null)
        {
            Session session = sessionTracker.getOpenSession();
            if(session != null)
                session.closeAndClearTokenInformation();
        }
    }

    protected final String getAccessToken()
    {
        Object obj = null;
        String s = null;
        if(sessionTracker != null)
        {
            Session session = sessionTracker.getOpenSession();
            s = null;
            if(session != null)
                s = session.getAccessToken();
        }
        return s;
    }

    protected final Date getExpirationDate()
    {
        Object obj = null;
        Date date = null;
        if(sessionTracker != null)
        {
            Session session = sessionTracker.getOpenSession();
            date = null;
            if(session != null)
                date = session.getExpirationDate();
        }
        return date;
    }

    protected final Session getSession()
    {
        if(sessionTracker != null)
            return sessionTracker.getSession();
        else
            return null;
    }

    protected final List getSessionPermissions()
    {
        Object obj = null;
        List list = null;
        if(sessionTracker != null)
        {
            Session session = sessionTracker.getSession();
            list = null;
            if(session != null)
                list = session.getPermissions();
        }
        return list;
    }

    protected final SessionState getSessionState()
    {
        Object obj = null;
        SessionState sessionstate = null;
        if(sessionTracker != null)
        {
            Session session = sessionTracker.getSession();
            sessionstate = null;
            if(session != null)
                sessionstate = session.getState();
        }
        return sessionstate;
    }

    protected final boolean isSessionOpen()
    {
        boolean flag1 = false;
        boolean flag = flag1;
        if(sessionTracker != null)
        {
            flag = flag1;
            if(sessionTracker.getOpenSession() != null)
                flag = true;
        }
        return flag;
    }

    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);
        sessionTracker = new SessionTracker(getActivity(), new DefaultSessionStatusCallback());
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
        sessionTracker.getSession().onActivityResult(getActivity(), i, j, intent);
    }

    public void onDestroy()
    {
        super.onDestroy();
        sessionTracker.stopTracking();
    }

    protected void onSessionStateChange(SessionState sessionstate, Exception exception)
    {
    }

    protected final void openSession()
    {
        openSessionForRead(null, null);
    }

    protected final void openSessionForPublish(String s, List list)
    {
        openSessionForPublish(s, list, SessionLoginBehavior.SSO_WITH_FALLBACK, 64206);
    }

    protected final void openSessionForPublish(String s, List list, SessionLoginBehavior sessionloginbehavior, int i)
    {
        openSession(s, list, sessionloginbehavior, i, SessionAuthorizationType.PUBLISH);
    }

    protected final void openSessionForRead(String s, List list)
    {
        openSessionForRead(s, list, SessionLoginBehavior.SSO_WITH_FALLBACK, 64206);
    }

    protected final void openSessionForRead(String s, List list, SessionLoginBehavior sessionloginbehavior, int i)
    {
        openSession(s, list, sessionloginbehavior, i, SessionAuthorizationType.READ);
    }

    public void setSession(Session session)
    {
        if(sessionTracker != null)
            sessionTracker.setSession(session);
    }

    private SessionTracker sessionTracker;
}
