
package com.facebook;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import java.util.*;

// Referenced classes of package com.facebook:
//            Session, Request, HttpMethod, Response,
//            FacebookRequestError, FacebookException, AccessTokenSource, AccessToken,
//            SessionState, TokenCachingStrategy

public class TestSession extends Session
{
    private static enum Mode
    {
        PRIVATE("PRIVATE", 0),
        SHARED("SHARED", 1);

        private Mode(String s, int i)
        {
            sType = s;
            iType = i;
        }
        private String sType;
        private int iType;
    }

    public static interface TestAccount
        extends GraphObject
    {

        public abstract String getAccessToken();

        public abstract String getId();

        public abstract String getName();

        public abstract void setName(String s);
    }

    private static interface TestAccountsResponse
        extends GraphObject
    {

        public abstract GraphObjectList getData();
    }

    private static final class TestTokenCachingStrategy extends TokenCachingStrategy
    {

        public void clear()
        {
            bundle = null;
        }

        public Bundle load()
        {
            return bundle;
        }

        public void save(Bundle bundle1)
        {
            bundle = bundle1;
        }

        private Bundle bundle;

        private TestTokenCachingStrategy()
        {
        }

    }


    TestSession(Activity activity, List list, TokenCachingStrategy tokencachingstrategy, String s, Mode mode1)
    {
        super(activity, testApplicationId, tokencachingstrategy);
        Validate.notNull(list, "permissions");
        Validate.notNullOrEmpty(testApplicationId, "testApplicationId");
        Validate.notNullOrEmpty(testApplicationSecret, "testApplicationSecret");
        sessionUniqueUserTag = s;
        mode = mode1;
        requestedPermissions = list;
    }

    public static TestSession createSessionWithPrivateUser(Activity activity, List list)
    {
        return createTestSession(activity, list, Mode.PRIVATE, null);
    }

    public static TestSession createSessionWithSharedUser(Activity activity, List list)
    {
        return createSessionWithSharedUser(activity, list, null);
    }

    public static TestSession createSessionWithSharedUser(Activity activity, List list, String s)
    {
        return createTestSession(activity, list, Mode.SHARED, s);
    }

    private TestAccount createTestAccountAndFinishAuth()
    {
        Bundle bundle = new Bundle();
        bundle.putString("installed", "true");
        bundle.putString("permissions", getPermissionsString());
        bundle.putString("access_token", getAppAccessToken());
        if(mode == Mode.SHARED)
            bundle.putString("name", String.format("Shared %s Testuser", new Object[] {
                getSharedTestAccountIdentifier()
            }));
        Object obj = (new Request(null, String.format("%s/accounts/test-users", new Object[] {
            testApplicationId
        }), bundle, HttpMethod.POST)).executeAndWait();
        FacebookRequestError facebookrequesterror = ((Response) (obj)).getError();
        obj = (TestAccount)((Response) (obj)).getGraphObjectAs(TestSession.TestAccount.class);
        if(facebookrequesterror != null)
        {
            finishAuthOrReauth(null, facebookrequesterror.getException());
            return null;
        }
        if(!$assertionsDisabled && obj == null)
            throw new AssertionError();
        if(mode == Mode.SHARED)
        {
            ((TestAccount) (obj)).setName(bundle.getString("name"));
            storeTestAccount(((TestAccount) (obj)));
        }
        finishAuthWithTestAccount(((TestAccount) (obj)));
        return ((TestAccount) (obj));
    }

    private static TestSession createTestSession(Activity activity, List list, Mode mode1, String s)
    {
        if(Utility.isNullOrEmpty(testApplicationId) || Utility.isNullOrEmpty(testApplicationSecret))
            throw new FacebookException("Must provide app ID and secret");
        List list1 = list;
        if(Utility.isNullOrEmpty(list))
            list1 = Arrays.asList(new String[] {
                "email", "publish_actions"
            });
        TestSession temp = new TestSession(activity, list1, new TestTokenCachingStrategy(), s, mode1);
        return temp;
    }

    private void deleteTestAccount(String s, String s1)
    {
        Object obj = new Bundle();
        ((Bundle) (obj)).putString("access_token", s1);
        obj = (new Request(null, s, ((Bundle) (obj)), HttpMethod.DELETE)).executeAndWait();
        FacebookRequestError temp = ((Response) (obj)).getError();
        obj = ((Response) (obj)).getGraphObject();
        if(temp != null)
            Log.w("FacebookSDK.TestSession", String.format("Could not delete test account %s: %s", new Object[] {
                s, temp.getException().toString()
            }));
        else
        if(((GraphObject) (obj)).getProperty("FACEBOOK_NON_JSON_RESULT") == Boolean.valueOf(false) || ((GraphObject) (obj)).getProperty("success") == Boolean.valueOf(false))
        {
            Log.w("FacebookSDK.TestSession", String.format("Could not delete test account %s: unknown reason", new Object[] {
                s
            }));
            return;
        }
    }

    private void findOrCreateSharedTestAccount()
    {
        TestAccount testaccount = findTestAccountMatchingIdentifier(getSharedTestAccountIdentifier());
        if(testaccount != null)
        {
            finishAuthWithTestAccount(testaccount);
            return;
        } else
        {
            createTestAccountAndFinishAuth();
            return;
        }
    }

    private static TestAccount findTestAccountMatchingIdentifier(String s)
    {
        Iterator iterator;
        retrieveTestAccountsForAppIfNeeded();
        iterator = appTestAccounts.values().iterator();

        TestAccount testaccount = null;
        for(;iterator.hasNext();) {
            boolean flag;
            testaccount = (TestAccount) iterator.next();
            flag = testaccount.getName().contains(s);
            if (flag)
                return testaccount;

        }

        return null;
    }

    private void finishAuthWithTestAccount(TestAccount testaccount)
    {
        testAccountId = testaccount.getId();
        testAccountUserName = testaccount.getName();
        finishAuthOrReauth(AccessToken.createFromString(testaccount.getAccessToken(), requestedPermissions, AccessTokenSource.TEST_USER), null);
    }

    static final String getAppAccessToken()
    {
        return (new StringBuilder()).append(testApplicationId).append("|").append(testApplicationSecret).toString();
    }

    private String getPermissionsString()
    {
        return TextUtils.join(",", requestedPermissions);
    }

    private String getSharedTestAccountIdentifier()
    {
        long l1 = getPermissionsString().hashCode();
        long l;
        if(sessionUniqueUserTag != null)
            l = (long)sessionUniqueUserTag.hashCode() & 0xffffffffL;
        else
            l = 0L;
        return validNameStringFromInteger(l1 & 0xffffffffL ^ l);
    }

    public static String getTestApplicationId()
    {
        String s = testApplicationId;
        return s;
    }

    public static String getTestApplicationSecret()
    {
        String s = testApplicationSecret;
        return s;
    }

    private static void populateTestAccounts(Collection collection, GraphObject graphobject)
    {
        TestAccount testaccount;
        for(Iterator iterator = collection.iterator(); iterator.hasNext(); storeTestAccount(testaccount))
        {
            testaccount = (TestAccount)iterator.next();
            testaccount.setName(((GraphUser)graphobject.getPropertyAs(testaccount.getId(), GraphUser.class)).getName());
        }
    }

    private static void retrieveTestAccountsForAppIfNeeded()
    {
        Object obj = appTestAccounts;
        if(obj != null)
            return;

        appTestAccounts = new HashMap();
        Request.setDefaultBatchApplicationId(testApplicationId);
        Bundle bundle = new Bundle();
        ((Bundle) (bundle)).putString("access_token", getAppAccessToken());
        Request request = new Request(null, "app/accounts/test-users", ((Bundle) (obj)), null);
        ((Request) (request)).setBatchEntryName("testUsers");
        ((Request) (request)).setBatchEntryOmitResultOnSuccess(false);
        Object bundle2 = new Bundle();
        ((Bundle) (bundle2)).putString("access_token", getAppAccessToken());
        ((Bundle) (bundle2)).putString("ids", "{result=testUsers:$.data.*.id}");
        ((Bundle) (bundle2)).putString("fields", "name");
        Request request2 = new Request(null, "", ((Bundle) (bundle2)), null);
        ((Request) (request2)).setBatchEntryDependsOn("testUsers");
        obj = Request.executeBatchAndWait(new Request[] {
                request, request2
        });
        if(obj != null) {
            if (((List) (obj)).size() == 2) {
                populateTestAccounts(((TestAccountsResponse) ((Response) ((List) (obj)).get(0)).getGraphObjectAs(TestSession.TestAccount.class)).getData(), ((Response) ((List) (obj)).get(1)).getGraphObject());
                return;

            }
        }
        throw new FacebookException("Unexpected number of results from TestUsers batch query");
    }

    public static void setTestApplicationId(String s)
    {
        if(testApplicationId != null && !testApplicationId.equals(s))
            throw new FacebookException("Can't have more than one test application ID");
    }

    public static void setTestApplicationSecret(String s)
    {
        if(testApplicationSecret != null && !testApplicationSecret.equals(s))
            throw new FacebookException("Can't have more than one test application secret");
    }

    private static void storeTestAccount(TestAccount testaccount)
    {
        appTestAccounts.put(testaccount.getId(), testaccount);
        return;
    }

    private String validNameStringFromInteger(long l)
    {
        String s = Long.toString(l);
        StringBuilder stringbuilder = new StringBuilder("Perm");
        int j = 0;
        char ac[] = s.toCharArray();
        int k = ac.length;
        for(int i = 0; i < k;)
        {
            char c1 = ac[i];
            char c = c1;
            if(c1 == j)
                c = (char)(c1 + 10);
            stringbuilder.append((char)((c + 97) - 48));
            i++;
            j = c;
        }

        return stringbuilder.toString();
    }

    void authorize(Session.AuthorizationRequest authorizationrequest)
    {
        if(mode == Mode.PRIVATE)
        {
            createTestAccountAndFinishAuth();
            return;
        } else
        {
            findOrCreateSharedTestAccount();
            return;
        }
    }

    void extendAccessToken()
    {
        wasAskedToExtendAccessToken = true;
        super.extendAccessToken();
    }

    void fakeTokenRefreshAttempt()
    {
        setCurrentTokenRefreshRequest(new Session.TokenRefreshRequest());
    }

    void forceExtendAccessToken(boolean flag)
    {
        AccessToken accesstoken = getTokenInfo();
        setTokenInfo(new AccessToken(accesstoken.getToken(), new Date(), accesstoken.getPermissions(), accesstoken.getDeclinedPermissions(), AccessTokenSource.TEST_USER, new Date(0L)));
        setLastAttemptedTokenExtendDate(new Date(0L));
    }

    public final String getTestUserId()
    {
        return testAccountId;
    }

    public final String getTestUserName()
    {
        return testAccountUserName;
    }

    boolean getWasAskedToExtendAccessToken()
    {
        return wasAskedToExtendAccessToken;
    }

    void postStateChange(SessionState sessionstate, SessionState sessionstate1, Exception exception)
    {
        String s = testAccountId;
        super.postStateChange(sessionstate, sessionstate1, exception);
        if(sessionstate1.isClosed() && s != null && mode == Mode.PRIVATE)
            deleteTestAccount(s, getAppAccessToken());
    }

    boolean shouldExtendAccessToken()
    {
        boolean flag = super.shouldExtendAccessToken();
        wasAskedToExtendAccessToken = false;
        return flag;
    }

    public final String toString()
    {
        String s = super.toString();
        return (new StringBuilder()).append("{TestSession").append(" testUserId:").append(testAccountId).append(" ").append(s).append("}").toString();
    }

    static final boolean $assertionsDisabled;
    private static final String LOG_TAG = "FacebookSDK.TestSession";
    private static Map appTestAccounts;
    private static final long serialVersionUID = 1L;
    private static String testApplicationId;
    private static String testApplicationSecret;
    private final Mode mode;
    private final List requestedPermissions;
    private final String sessionUniqueUserTag;
    private String testAccountId;
    private String testAccountUserName;
    private boolean wasAskedToExtendAccessToken;

    static
    {
        boolean flag;
        if(!TestSession.class.desiredAssertionStatus())
            flag = true;
        else
            flag = false;
        $assertionsDisabled = flag;
    }
}
