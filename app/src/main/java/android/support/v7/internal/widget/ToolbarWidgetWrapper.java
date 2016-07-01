// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v7.internal.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.*;
//import android.support.v7.internal.view.menu.ActionMenuItem;
//import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.view.menu.ActionMenuItem;
//import android.support.v7.widget.ActionMenuPresenter;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.*;
import android.widget.SpinnerAdapter;

// Referenced classes of package android.support.v7.internal.widget:
//            DecorToolbar, TintTypedArray, TintManager, SpinnerCompat, 
//            ScrollingTabContainerView

public class ToolbarWidgetWrapper
    implements DecorToolbar
{

    public ToolbarWidgetWrapper(Toolbar toolbar, boolean flag)
    {
//        this(toolbar, flag, android.support.v7.appcompat.R.string.abc_action_bar_up_description, android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }

    public ToolbarWidgetWrapper(Toolbar toolbar, boolean flag, int i, int j)
    {
        mNavigationMode = 0;
        mDefaultNavigationContentDescription = 0;
        mToolbar = toolbar;
        mTitle = toolbar.getTitle();
        mSubtitle = toolbar.getSubtitle();
        boolean flag1;
        if(mTitle != null)
            flag1 = true;
        else
            flag1 = false;
        mTitleSet = flag1;
        mNavIcon = toolbar.getNavigationIcon();
        if(flag)
        {
            TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(toolbar.getContext(), null, android.support.v7.appcompat.R.styleable.ActionBar, android.support.v7.appcompat.R.attr.actionBarStyle, 0);
            Object obj = tintTypedArray.getText(android.support.v7.appcompat.R.styleable.ActionBar_title);
            if(!TextUtils.isEmpty(((CharSequence) (obj))))
                setTitle(((CharSequence) (obj)));
            obj = tintTypedArray.getText(android.support.v7.appcompat.R.styleable.ActionBar_subtitle);
            if(!TextUtils.isEmpty(((CharSequence) (obj))))
                setSubtitle(((CharSequence) (obj)));
            obj = tintTypedArray.getDrawable(android.support.v7.appcompat.R.styleable.ActionBar_logo);
            if(obj != null)
                setLogo(((Drawable) (obj)));
            obj = tintTypedArray.getDrawable(android.support.v7.appcompat.R.styleable.ActionBar_icon);
            if(mNavIcon == null && obj != null)
                setIcon(((Drawable) (obj)));
            obj = tintTypedArray.getDrawable(android.support.v7.appcompat.R.styleable.ActionBar_homeAsUpIndicator);
            if(obj != null)
                setNavigationIcon(((Drawable) (obj)));
            setDisplayOptions(tintTypedArray.getInt(android.support.v7.appcompat.R.styleable.ActionBar_displayOptions, 0));
            int k = tintTypedArray.getResourceId(android.support.v7.appcompat.R.styleable.ActionBar_customNavigationLayout, 0);
            if(k != 0)
            {
                setCustomView(LayoutInflater.from(mToolbar.getContext()).inflate(k, mToolbar, false));
                setDisplayOptions(mDisplayOpts | 0x10);
            }
            k = tintTypedArray.getLayoutDimension(android.support.v7.appcompat.R.styleable.ActionBar_height, 0);
            if(k > 0)
            {
                android.view.ViewGroup.LayoutParams layoutparams = mToolbar.getLayoutParams();
                layoutparams.height = k;
                mToolbar.setLayoutParams(layoutparams);
            }
            k = tintTypedArray.getDimensionPixelOffset(android.support.v7.appcompat.R.styleable.ActionBar_contentInsetStart, -1);
            int l = tintTypedArray.getDimensionPixelOffset(android.support.v7.appcompat.R.styleable.ActionBar_contentInsetEnd, -1);
            if(k >= 0 || l >= 0)
                mToolbar.setContentInsetsRelative(Math.max(k, 0), Math.max(l, 0));
            k = tintTypedArray.getResourceId(android.support.v7.appcompat.R.styleable.ActionBar_titleTextStyle, 0);
            if(k != 0)
                mToolbar.setTitleTextAppearance(mToolbar.getContext(), k);
            k = tintTypedArray.getResourceId(android.support.v7.appcompat.R.styleable.ActionBar_subtitleTextStyle, 0);
            if(k != 0)
                mToolbar.setSubtitleTextAppearance(mToolbar.getContext(), k);
            k = tintTypedArray.getResourceId(android.support.v7.appcompat.R.styleable.ActionBar_popupTheme, 0);
            if(k != 0)
                mToolbar.setPopupTheme(k);
            tintTypedArray.recycle();
//            mTintManager = tintTypedArray.getTintManager();
        } else
        {
            mDisplayOpts = detectDisplayOptions();
//            mTintManager = TintManager.get(toolbar.getContext());
        }
        setDefaultNavigationContentDescription(i);
        mHomeDescription = mToolbar.getNavigationContentDescription();
//        setDefaultNavigationIcon(mTintManager.getDrawable(j));
        final ActionMenuItem mNavItem = new ActionMenuItem(mToolbar.getContext(), 0, 0x102002c, 0, 0, mTitle);
        mToolbar.setNavigationOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mWindowCallback != null && mMenuPrepared)
                    mWindowCallback.onMenuItemSelected(0, mNavItem);
            }

        });
    }

    private int detectDisplayOptions()
    {
        int i = 11;
        if(mToolbar.getNavigationIcon() != null)
            i = 0xb | 4;
        return i;
    }

    private void ensureSpinner()
    {
        if(mSpinner == null)
        {
            mSpinner = new SpinnerCompat(getContext(), null, android.support.v7.appcompat.R.attr.actionDropDownStyle);
            android.support.v7.widget.Toolbar.LayoutParams layoutparams = new android.support.v7.widget.Toolbar.LayoutParams(-2, -2, 0x800013);
            mSpinner.setLayoutParams(layoutparams);
        }
    }

    private void setTitleInt(CharSequence charsequence)
    {
        mTitle = charsequence;
        if((mDisplayOpts & 8) != 0)
            mToolbar.setTitle(charsequence);
    }

    private void updateHomeAccessibility()
    {
        if((mDisplayOpts & 4) != 0)
        {
            if(!TextUtils.isEmpty(mHomeDescription))
                mToolbar.setNavigationContentDescription(mHomeDescription);
            else
                mToolbar.setNavigationContentDescription(mDefaultNavigationContentDescription);
        }
        return;
    }

    private void updateNavigationIcon()
    {
        if((mDisplayOpts & 4) != 0)
        {
            Toolbar toolbar = mToolbar;
            Drawable drawable;
            if(mNavIcon != null)
                drawable = mNavIcon;
            else
                drawable = mDefaultNavigationIcon;
            toolbar.setNavigationIcon(drawable);
        }
    }

    private void updateToolbarLogo()
    {
        Drawable drawable = null;
        if((mDisplayOpts & 2) != 0)
            if((mDisplayOpts & 1) != 0)
            {
                if(mLogo != null)
                    drawable = mLogo;
                else
                    drawable = mIcon;
            } else
            {
                drawable = mIcon;
            }
        mToolbar.setLogo(drawable);
    }

    public void animateToVisibility(int i)
    {
        if(i == 8)
            ViewCompat.animate(mToolbar).alpha(0.0F).setListener(new ViewPropertyAnimatorListenerAdapter() {

                public void onAnimationCancel(View view)
                {
                    mCanceled = true;
                }

                public void onAnimationEnd(View view)
                {
                    if(!mCanceled)
                        mToolbar.setVisibility(8);
                }
                private boolean mCanceled = false;

            });
        else
        if(i == 0)
        {
            ViewCompat.animate(mToolbar).alpha(1.0F).setListener(new ViewPropertyAnimatorListenerAdapter() {

                public void onAnimationStart(View view)
                {
                    mToolbar.setVisibility(0);
                }

            });
            return;
        }
    }

    public boolean canShowOverflowMenu()
    {
        return mToolbar.canShowOverflowMenu();
    }

    public boolean canSplit()
    {
        return false;
    }

    public void collapseActionView()
    {
        mToolbar.collapseActionView();
    }

    public void dismissPopupMenus()
    {
        mToolbar.dismissPopupMenus();
    }

    public Context getContext()
    {
        return mToolbar.getContext();
    }

    public View getCustomView()
    {
        return mCustomView;
    }

    public int getDisplayOptions()
    {
        return mDisplayOpts;
    }

    public int getDropdownItemCount()
    {
        if(mSpinner != null)
            return mSpinner.getCount();
        else
            return 0;
    }

    public int getDropdownSelectedPosition()
    {
        if(mSpinner != null)
            return mSpinner.getSelectedItemPosition();
        else
            return 0;
    }

    public int getHeight()
    {
        return mToolbar.getHeight();
    }

    public Menu getMenu()
    {
        return mToolbar.getMenu();
    }

    public int getNavigationMode()
    {
        return mNavigationMode;
    }

    public int getPopupTheme()
    {
        return mToolbar.getPopupTheme();
    }

    public CharSequence getSubtitle()
    {
        return mToolbar.getSubtitle();
    }

    public CharSequence getTitle()
    {
        return mToolbar.getTitle();
    }

    public ViewGroup getViewGroup()
    {
        return mToolbar;
    }

    public int getVisibility()
    {
        return mToolbar.getVisibility();
    }

    public boolean hasEmbeddedTabs()
    {
        return mTabView != null;
    }

    public boolean hasExpandedActionView()
    {
        return mToolbar.hasExpandedActionView();
    }

    public boolean hasIcon()
    {
        return mIcon != null;
    }

    public boolean hasLogo()
    {
        return mLogo != null;
    }

    public boolean hideOverflowMenu()
    {
        return mToolbar.hideOverflowMenu();
    }

    public void initIndeterminateProgress()
    {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }

    public void initProgress()
    {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }

    public boolean isOverflowMenuShowPending()
    {
        return mToolbar.isOverflowMenuShowPending();
    }

    public boolean isOverflowMenuShowing()
    {
        return mToolbar.isOverflowMenuShowing();
    }

    public boolean isSplit()
    {
        return false;
    }

    public boolean isTitleTruncated()
    {
        return mToolbar.isTitleTruncated();
    }

    public void restoreHierarchyState(SparseArray sparsearray)
    {
        mToolbar.restoreHierarchyState(sparsearray);
    }

    public void saveHierarchyState(SparseArray sparsearray)
    {
        mToolbar.saveHierarchyState(sparsearray);
    }

    public void setBackgroundDrawable(Drawable drawable)
    {
        mToolbar.setBackgroundDrawable(drawable);
    }

    public void setCollapsible(boolean flag)
    {
        mToolbar.setCollapsible(flag);
    }

    public void setCustomView(View view)
    {
        if(mCustomView != null && (mDisplayOpts & 0x10) != 0)
            mToolbar.removeView(mCustomView);
        mCustomView = view;
        if(view != null && (mDisplayOpts & 0x10) != 0)
            mToolbar.addView(mCustomView);
    }

    public void setDefaultNavigationContentDescription(int i)
    {
        if(i != mDefaultNavigationContentDescription)
        {
            mDefaultNavigationContentDescription = i;
            if(TextUtils.isEmpty(mToolbar.getNavigationContentDescription()))
            {
                setNavigationContentDescription(mDefaultNavigationContentDescription);
                return;
            }
        }
    }

    public void setDefaultNavigationIcon(Drawable drawable)
    {
        if(mDefaultNavigationIcon != drawable)
        {
            mDefaultNavigationIcon = drawable;
            updateNavigationIcon();
        }
    }

    public void setDisplayOptions(int i)
    {
label0:
        {
            int j = mDisplayOpts ^ i;
            mDisplayOpts = i;
            if(j != 0)
            {
                if((j & 4) != 0)
                    if((i & 4) != 0)
                    {
                        updateNavigationIcon();
                        updateHomeAccessibility();
                    } else
                    {
                        mToolbar.setNavigationIcon(null);
                    }
                if((j & 3) != 0)
                    updateToolbarLogo();
                if((j & 8) != 0)
                    if((i & 8) != 0)
                    {
                        mToolbar.setTitle(mTitle);
                        mToolbar.setSubtitle(mSubtitle);
                    } else
                    {
                        mToolbar.setTitle(null);
                        mToolbar.setSubtitle(null);
                    }
                if((j & 0x10) != 0 && mCustomView != null)
                {
                    if((i & 0x10) == 0)
                        break label0;
                    mToolbar.addView(mCustomView);
                }
            }
            return;
        }
        mToolbar.removeView(mCustomView);
    }

    public void setDropdownParams(SpinnerAdapter spinneradapter, AdapterViewCompat.OnItemSelectedListener onitemselectedlistener)
    {
        ensureSpinner();
        mSpinner.setAdapter(spinneradapter);
        mSpinner.setOnItemSelectedListener(onitemselectedlistener);
    }

    public void setDropdownSelectedPosition(int i)
    {
        if(mSpinner == null)
        {
            throw new IllegalStateException("Can't set dropdown selected position without an adapter");
        } else
        {
            mSpinner.setSelection(i);
            return;
        }
    }

    public void setEmbeddedTabView(ScrollingTabContainerView scrollingtabcontainerview)
    {
        if(mTabView != null && mTabView.getParent() == mToolbar)
            mToolbar.removeView(mTabView);
        mTabView = scrollingtabcontainerview;
        if(scrollingtabcontainerview != null && mNavigationMode == 2)
        {
            mToolbar.addView(mTabView, 0);
            android.support.v7.widget.Toolbar.LayoutParams layoutparams = (android.support.v7.widget.Toolbar.LayoutParams)mTabView.getLayoutParams();
            layoutparams.width = -2;
            layoutparams.height = -2;
            layoutparams.gravity = 0x800053;
            scrollingtabcontainerview.setAllowCollapse(true);
        }
    }

    public void setHomeButtonEnabled(boolean flag)
    {
    }

    public void setIcon(int i)
    {
        Drawable drawable;
//        if(i != 0)
//            drawable = mTintManager.getDrawable(i);
//        else
            drawable = null;
        setIcon(drawable);
    }

    public void setIcon(Drawable drawable)
    {
        mIcon = drawable;
        updateToolbarLogo();
    }

    public void setLogo(int i)
    {
        Drawable drawable;
//        if(i != 0)
//            drawable = mTintManager.getDrawable(i);
//        else
            drawable = null;
        setLogo(drawable);
    }

    public void setLogo(Drawable drawable)
    {
        mLogo = drawable;
        updateToolbarLogo();
    }

    /*
    public void setMenu(Menu menu, android.support.v7.internal.view.menu.MenuPresenter.Callback callback)
    {
        if(mActionMenuPresenter == null)
        {
            mActionMenuPresenter = new ActionMenuPresenter(mToolbar.getContext());
            mActionMenuPresenter.setId(android.support.v7.appcompat.R.id.action_menu_presenter);
        }
        mActionMenuPresenter.setCallback(callback);
        mToolbar.setMenu((MenuBuilder)menu, mActionMenuPresenter);
    }

    public void setMenuCallbacks(android.support.v7.internal.view.menu.MenuPresenter.Callback callback, android.support.v7.internal.view.menu.MenuBuilder.Callback callback1)
    {
        mToolbar.setMenuCallbacks(callback, callback1);
    }
    */

    public void setMenuPrepared()
    {
        mMenuPrepared = true;
    }

    public void setNavigationContentDescription(int i)
    {
        Object obj;
        if(i == 0)
            obj = null;
        else
            obj = getContext().getString(i);
        setNavigationContentDescription(((CharSequence) (obj)));
    }

    public void setNavigationContentDescription(CharSequence charsequence)
    {
        mHomeDescription = charsequence;
        updateHomeAccessibility();
    }

    public void setNavigationIcon(int i)
    {
        Drawable drawable;
        //if(i != 0)
//            drawable = mTintManager.getDrawable(i);
//        else
            drawable = null;
        setNavigationIcon(drawable);
    }

    public void setNavigationIcon(Drawable drawable)
    {
        mNavIcon = drawable;
        updateNavigationIcon();
    }

    public void setNavigationMode(int i)
    {
        int j = mNavigationMode;
        if(i == j)
            return;

        switch(j) {
            default:
                break;
            case 1:
                if (mSpinner != null && mSpinner.getParent() == mToolbar)
                    mToolbar.removeView(mSpinner);
                break;
            case 2:
                if (mTabView != null && mTabView.getParent() == mToolbar)
                    mToolbar.removeView(mTabView);
                break;
        }

        mNavigationMode = i;
        switch(i) {
            default:
                throw new IllegalArgumentException((new StringBuilder()).append("Invalid navigation mode ").append(i).toString());

            case 1:
                ensureSpinner();
                mToolbar.addView(mSpinner, 0);
                return;
            case 0:
                return;
            case 2:
                if (mTabView != null) {
                    mToolbar.addView(mTabView, 0);
                    android.support.v7.widget.Toolbar.LayoutParams layoutparams = (android.support.v7.widget.Toolbar.LayoutParams) mTabView.getLayoutParams();
                    layoutparams.width = -2;
                    layoutparams.height = -2;
                    layoutparams.gravity = 0x800053;
                    return;
                }
        }
    }

    public void setSplitToolbar(boolean flag)
    {
        if(flag)
            throw new UnsupportedOperationException("Cannot split an android.widget.Toolbar");
        else
            return;
    }

    public void setSplitView(ViewGroup viewgroup)
    {
    }

    public void setSplitWhenNarrow(boolean flag)
    {
    }

    public void setSubtitle(CharSequence charsequence)
    {
        mSubtitle = charsequence;
        if((mDisplayOpts & 8) != 0)
            mToolbar.setSubtitle(charsequence);
    }

    public void setTitle(CharSequence charsequence)
    {
        mTitleSet = true;
        setTitleInt(charsequence);
    }

    public void setVisibility(int i)
    {
        mToolbar.setVisibility(i);
    }

    public void setWindowCallback(android.view.Window.Callback callback)
    {
        mWindowCallback = callback;
    }

    public void setWindowTitle(CharSequence charsequence)
    {
        if(!mTitleSet)
            setTitleInt(charsequence);
    }

    public boolean showOverflowMenu()
    {
        return mToolbar.showOverflowMenu();
    }

    private static final int AFFECTS_LOGO_MASK = 3;
    private static final String TAG = "ToolbarWidgetWrapper";
//    private ActionMenuPresenter mActionMenuPresenter;
    private View mCustomView;
    private int mDefaultNavigationContentDescription;
    private Drawable mDefaultNavigationIcon;
    private int mDisplayOpts;
    private CharSequence mHomeDescription;
    private Drawable mIcon;
    private Drawable mLogo;
    private boolean mMenuPrepared;
    private Drawable mNavIcon;
    private int mNavigationMode;
    private SpinnerCompat mSpinner;
    private CharSequence mSubtitle;
    private View mTabView;
    //private final TintManager mTintManager;
    private CharSequence mTitle;
    private boolean mTitleSet;
    private Toolbar mToolbar;
    private android.view.Window.Callback mWindowCallback;

}
