// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import java.util.*;

// Referenced classes of package com.facebook.widget:
//            PickerFragment, GraphObjectAdapter, GraphObjectPagingLoader, SimpleGraphObjectCursor

public class FriendPickerFragment extends PickerFragment
{
    public static enum FriendPickerType
    {

		FRIENDS("FRIENDS", 0, "/friends", true),
		TAGGABLE_FRIENDS("TAGGABLE_FRIENDS", 1, "/taggable_friends", false),
		INVITABLE_FRIENDS("INVITABLE_FRIENDS", 2, "/invitable_friends", false);

        String getRequestPath()
        {
            return requestPath;
        }

        boolean isCacheable()
        {
            return requestIsCacheable;
        }

        private final boolean requestIsCacheable;
        private final String requestPath;
		String sType;
		int iType;

        private FriendPickerType(String s, int i, String s1, boolean flag)
        {
		sType = s;
		iType = i;
            requestPath = s1;
            requestIsCacheable = flag;
        }
    }

    private class ImmediateLoadingStrategy extends PickerFragment.LoadingStrategy
    {

        private void followNextLink()
        {
            displayActivityCircle();
            loader.followNextLink();
        }

        protected boolean canSkipRoundTripIfCached()
        {
            return friendPickerType.isCacheable();
        }

        protected void onLoadFinished(GraphObjectPagingLoader graphobjectpagingloader, SimpleGraphObjectCursor simplegraphobjectcursor)
        {
            super.onLoadFinished(graphobjectpagingloader, simplegraphobjectcursor);
            if(simplegraphobjectcursor != null && !graphobjectpagingloader.isLoading())
            {
                if(simplegraphobjectcursor.areMoreObjectsAvailable())
                {
                    followNextLink();
                    return;
                }
                hideActivityCircle();
                if(simplegraphobjectcursor.isFromCache())
                {
                    long l;
                    if(simplegraphobjectcursor.getCount() == 0)
                        l = 2000L;
                    else
                        l = 0L;
                    graphobjectpagingloader.refreshOriginalRequest(l);
                    return;
                }
            }
        }


        private ImmediateLoadingStrategy()
        {
            super();
        }

    }


    public FriendPickerFragment()
    {
        this(null);
    }

    public FriendPickerFragment(Bundle bundle)
    {
        super(GraphUser.class, com.facebook.android.R.layout.com_facebook_friendpickerfragment, bundle);
        multiSelect = true;
        friendPickerType = FriendPickerType.FRIENDS;
        preSelectedFriendIds = new ArrayList();
        setFriendPickerSettingsFromBundle(bundle);
    }

    private Request createRequest(String s, Set set, Session session)
    {
        Request request = Request.newGraphPathRequest(session, (new StringBuilder()).append(s).append(friendPickerType.getRequestPath()).toString(), null);
        set = new HashSet(set);
        set.addAll(Arrays.asList(new String[] {
            "id", "name"
        }));
        String temp = adapter.getPictureFieldSpecifier();
        if(temp != null)
            set.add(temp);
        Bundle bundle = request.getParameters();
        bundle.putString("fields", TextUtils.join(",", set));
        request.setParameters(bundle);
        return request;
    }

    private void setFriendPickerSettingsFromBundle(Bundle bundle)
    {
        if(bundle == null)
            return;
        if(bundle.containsKey("com.facebook.widget.FriendPickerFragment.UserId"))
            setUserId(bundle.getString("com.facebook.widget.FriendPickerFragment.UserId"));
        setMultiSelect(bundle.getBoolean("com.facebook.widget.FriendPickerFragment.MultiSelect", multiSelect));
        if(!bundle.containsKey("com.facebook.widget.FriendPickerFragment.FriendPickerType"))
            return;
        friendPickerType = FriendPickerType.valueOf(bundle.getString("com.facebook.widget.FriendPickerFragment.FriendPickerType"));
        return;
    }

    PickerFragment.PickerFragmentAdapter createAdapter()
    {
        PickerFragment.PickerFragmentAdapter pickerfragmentadapter = new PickerFragment.PickerFragmentAdapter(getActivity()) {

            protected int getDefaultPicture()
            {
                return com.facebook.android.R.drawable.com_facebook_profile_default_icon;
            }

            protected int getGraphObjectRowLayoutId(GraphObject graphobject)
            {
                return getGraphObjectRowLayoutId((GraphUser)graphobject);
            }

            protected int getGraphObjectRowLayoutId(GraphUser graphuser)
            {
                return com.facebook.android.R.layout.com_facebook_picker_list_row;
            }


        };

        pickerfragmentadapter.setShowCheckbox(true);
        pickerfragmentadapter.setShowPicture(getShowPictures());
        pickerfragmentadapter.setSortFields(Arrays.asList(new String[] {
            "name"
        }));
        pickerfragmentadapter.setGroupByField("name");
        return pickerfragmentadapter;
    }

    PickerFragment.LoadingStrategy createLoadingStrategy()
    {
        return new ImmediateLoadingStrategy();
    }

    PickerFragment.SelectionStrategy createSelectionStrategy()
    {
        if(multiSelect)
            return new PickerFragment.MultiSelectionStrategy();
        else
            return new PickerFragment.SingleSelectionStrategy();
    }

    String getDefaultTitleText()
    {
        return getString(com.facebook.android.R.string.com_facebook_choose_friends);
    }

    public boolean getMultiSelect()
    {
        return multiSelect;
    }

    Request getRequestForLoadData(Session session)
    {
        if(adapter == null)
            throw new FacebookException("Can't issue requests until Fragment has been created.");
        String s;
        if(userId != null)
            s = userId;
        else
            s = "me";
        return createRequest(s, extraFields, session);
    }

    public List getSelection()
    {
        return getSelectedGraphObjects();
    }

    public String getUserId()
    {
        return userId;
    }

    public void loadData(boolean flag)
    {
        super.loadData(flag);
        setSelectedGraphObjects(preSelectedFriendIds);
    }

    void logAppEvents(boolean flag)
    {
        AppEventsLogger appeventslogger = AppEventsLogger.newLogger(getActivity(), getSession());
        Bundle bundle = new Bundle();
        String s;
        if(flag)
            s = "Completed";
        else
            s = "Unknown";
        bundle.putString("fb_dialog_outcome", s);
        bundle.putInt("num_friends_picked", getSelection().size());
        appeventslogger.logSdkEvent("fb_friend_picker_usage", null, bundle);
    }

    public void onInflate(Activity activity, AttributeSet attributeset, Bundle bundle)
    {
        super.onInflate(activity, attributeset, bundle);
        TypedArray typedArray = activity.obtainStyledAttributes(attributeset, com.facebook.android.R.styleable.com_facebook_friend_picker_fragment);
        setMultiSelect(typedArray.getBoolean(com.facebook.android.R.styleable.com_facebook_friend_picker_fragment_multi_select, multiSelect));
        typedArray.recycle();
    }

    void saveSettingsToBundle(Bundle bundle)
    {
        super.saveSettingsToBundle(bundle);
        bundle.putString("com.facebook.widget.FriendPickerFragment.UserId", userId);
        bundle.putBoolean("com.facebook.widget.FriendPickerFragment.MultiSelect", multiSelect);
    }

    public void setFriendPickerType(FriendPickerType friendpickertype)
    {
        friendPickerType = friendpickertype;
    }

    public void setMultiSelect(boolean flag)
    {
        if(multiSelect != flag)
        {
            multiSelect = flag;
            setSelectionStrategy(createSelectionStrategy());
        }
    }

    public void setSelection(List list)
    {
        ArrayList arraylist = new ArrayList();
        for(Iterator iterator  = list.iterator(); iterator.hasNext(); arraylist.add(((GraphUser)iterator.next()).getId()));
        setSelectionByIds(arraylist);
    }

    public void setSelection(GraphUser agraphuser[])
    {
        setSelection(Arrays.asList(agraphuser));
    }

    public void setSelectionByIds(List list)
    {
        preSelectedFriendIds.addAll(list);
    }

    public void setSelectionByIds(String as[])
    {
        setSelectionByIds(Arrays.asList(as));
    }

    public void setSettingsFromBundle(Bundle bundle)
    {
        super.setSettingsFromBundle(bundle);
        setFriendPickerSettingsFromBundle(bundle);
    }

    public void setUserId(String s)
    {
        userId = s;
    }

    public static final String FRIEND_PICKER_TYPE_KEY = "com.facebook.widget.FriendPickerFragment.FriendPickerType";
    private static final String ID = "id";
    public static final String MULTI_SELECT_BUNDLE_KEY = "com.facebook.widget.FriendPickerFragment.MultiSelect";
    private static final String NAME = "name";
    public static final String USER_ID_BUNDLE_KEY = "com.facebook.widget.FriendPickerFragment.UserId";
    private FriendPickerType friendPickerType;
    private boolean multiSelect;
    private List preSelectedFriendIds;
    private String userId;

}
