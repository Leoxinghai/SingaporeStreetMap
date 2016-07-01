

package com.facebook;


public enum SessionLoginBehavior
{
    SSO_WITH_FALLBACK("SSO_WITH_FALLBACK", 0, true, true),
    SSO_ONLY("SSO_ONLY", 1, true, false),
    SUPPRESS_SSO("SUPPRESS_SSO", 2, false, true);

    private SessionLoginBehavior(String s, int i, boolean flag, boolean flag1)
    {
        allowsKatanaAuth = flag;
        allowsWebViewAuth = flag1;
        sType = s;
        iType = i;
    }

    boolean allowsKatanaAuth()
    {
        return allowsKatanaAuth;
    }

    boolean allowsWebViewAuth()
    {
        return allowsWebViewAuth;
    }

    private final boolean allowsKatanaAuth;
    private final boolean allowsWebViewAuth;
    private String sType;
    private int iType;

}
