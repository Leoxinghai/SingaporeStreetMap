// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v7.internal.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Adapter;
import android.widget.AdapterView;

public abstract class AdapterViewCompat extends ViewGroup
{
    public static class AdapterContextMenuInfo
        implements android.view.ContextMenu.ContextMenuInfo
    {

        public long id;
        public int position;
        public View targetView;

        public AdapterContextMenuInfo(View view, int i, long l)
        {
            targetView = view;
            position = i;
            id = l;
        }
    }

    class AdapterDataSetObserver extends DataSetObserver
    {

        public void clearSavedState()
        {
            mInstanceState = null;
        }

        public void onChanged()
        {
            mDataChanged = true;
            mOldItemCount = mItemCount;
            mItemCount = getAdapter().getCount();
            if(getAdapter().hasStableIds() && mInstanceState != null && mOldItemCount == 0 && mItemCount > 0)
            {
                onRestoreInstanceState(mInstanceState);
                mInstanceState = null;
            } else
            {
                rememberSyncState();
            }
            checkFocus();
            requestLayout();
        }

        public void onInvalidated()
        {
            mDataChanged = true;
            if(getAdapter().hasStableIds())
                mInstanceState = onSaveInstanceState();
            mOldItemCount = mItemCount;
            mItemCount = 0;
            mSelectedPosition = -1;
            mSelectedRowId = 0x8000000000000000L;
            mNextSelectedPosition = -1;
            mNextSelectedRowId = 0x8000000000000000L;
            mNeedSync = false;
            checkFocus();
            requestLayout();
        }

        private Parcelable mInstanceState;

        AdapterDataSetObserver()
        {
            super();
            mInstanceState = null;
        }
    }

    public static interface OnItemClickListener
    {

        public abstract void onItemClick(AdapterViewCompat adapterviewcompat, View view, int i, long l);
    }

    class OnItemClickListenerWrapper
        implements android.widget.AdapterView.OnItemClickListener
    {

        public void onItemClick(AdapterView adapterview, View view, int i, long l)
        {
            mWrappedListener.onItemClick(AdapterViewCompat.this, view, i, l);
        }

        private final OnItemClickListener mWrappedListener;

        public OnItemClickListenerWrapper(OnItemClickListener onitemclicklistener)
        {
            super();
            mWrappedListener = onitemclicklistener;
        }
    }

    public static interface OnItemLongClickListener
    {

        public abstract boolean onItemLongClick(AdapterViewCompat adapterviewcompat, View view, int i, long l);
    }

    public static interface OnItemSelectedListener
    {

        public abstract void onItemSelected(AdapterViewCompat adapterviewcompat, View view, int i, long l);

        public abstract void onNothingSelected(AdapterViewCompat adapterviewcompat);
    }

    private class SelectionNotifier
        implements Runnable
    {

        public void run()
        {
            if(mDataChanged)
            {
                if(getAdapter() != null)
                    post(this);
                return;
            } else
            {
                fireOnSelected();
                return;
            }
        }

        private SelectionNotifier()
        {
            super();
        }

    }


    AdapterViewCompat(Context context)
    {
        super(context);
        mFirstPosition = 0;
        mSyncRowId = 0x8000000000000000L;
        mNeedSync = false;
        mInLayout = false;
        mNextSelectedPosition = -1;
        mNextSelectedRowId = 0x8000000000000000L;
        mSelectedPosition = -1;
        mSelectedRowId = 0x8000000000000000L;
        mOldSelectedPosition = -1;
        mOldSelectedRowId = 0x8000000000000000L;
        mBlockLayoutRequests = false;
    }

    AdapterViewCompat(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mFirstPosition = 0;
        mSyncRowId = 0x8000000000000000L;
        mNeedSync = false;
        mInLayout = false;
        mNextSelectedPosition = -1;
        mNextSelectedRowId = 0x8000000000000000L;
        mSelectedPosition = -1;
        mSelectedRowId = 0x8000000000000000L;
        mOldSelectedPosition = -1;
        mOldSelectedRowId = 0x8000000000000000L;
        mBlockLayoutRequests = false;
    }

    AdapterViewCompat(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mFirstPosition = 0;
        mSyncRowId = 0x8000000000000000L;
        mNeedSync = false;
        mInLayout = false;
        mNextSelectedPosition = -1;
        mNextSelectedRowId = 0x8000000000000000L;
        mSelectedPosition = -1;
        mSelectedRowId = 0x8000000000000000L;
        mOldSelectedPosition = -1;
        mOldSelectedRowId = 0x8000000000000000L;
        mBlockLayoutRequests = false;
    }

    private void fireOnSelected()
    {
        if(mOnItemSelectedListener == null)
            return;
        int i = getSelectedItemPosition();
        if(i >= 0)
        {
            View view = getSelectedView();
            mOnItemSelectedListener.onItemSelected(this, view, i, getAdapter().getItemId(i));
            return;
        } else
        {
            mOnItemSelectedListener.onNothingSelected(this);
            return;
        }
    }

    private void updateEmptyStatus(boolean flag)
    {
        if(isInFilterMode())
            flag = false;
        if(flag)
        {
            if(mEmptyView != null)
            {
                mEmptyView.setVisibility(0);
                setVisibility(8);
            } else
            {
                setVisibility(0);
            }
            if(mDataChanged)
                onLayout(false, getLeft(), getTop(), getRight(), getBottom());
            return;
        }
        if(mEmptyView != null)
            mEmptyView.setVisibility(8);
        setVisibility(0);
    }

    public void addView(View view)
    {
        throw new UnsupportedOperationException("addView(View) is not supported in AdapterView");
    }

    public void addView(View view, int i)
    {
        throw new UnsupportedOperationException("addView(View, int) is not supported in AdapterView");
    }

    public void addView(View view, int i, android.view.ViewGroup.LayoutParams layoutparams)
    {
        throw new UnsupportedOperationException("addView(View, int, LayoutParams) is not supported in AdapterView");
    }

    public void addView(View view, android.view.ViewGroup.LayoutParams layoutparams)
    {
        throw new UnsupportedOperationException("addView(View, LayoutParams) is not supported in AdapterView");
    }

    protected boolean canAnimate()
    {
        return super.canAnimate() && mItemCount > 0;
    }

    void checkFocus()
    {
            boolean flag2 = false;
            Adapter adapter = getAdapter();
            boolean flag;
            boolean flag1;
            if(adapter == null || adapter.getCount() == 0)
                flag = true;
            else
                flag = false;
            if(!flag || isInFilterMode())
                flag = true;
            else
                flag = false;
            if(flag && mDesiredFocusableInTouchModeState)
                flag1 = true;
            else
                flag1 = false;
            super.setFocusableInTouchMode(flag1);
            if(flag && mDesiredFocusableState)
                flag1 = true;
            else
                flag1 = false;
            super.setFocusable(flag1);
            if(mEmptyView == null)
                return;
            if(adapter != null)
            {
                flag1 = flag2;
                if(adapter.isEmpty())
                    flag1 = true;
            } else
                flag1 = true;
            updateEmptyStatus(flag1);
    }

    void checkSelectionChanged()
    {
        if(mSelectedPosition != mOldSelectedPosition || mSelectedRowId != mOldSelectedRowId)
        {
            selectionChanged();
            mOldSelectedPosition = mSelectedPosition;
            mOldSelectedRowId = mSelectedRowId;
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityevent)
    {
        View view = getSelectedView();
        return view != null && view.getVisibility() == 0 && view.dispatchPopulateAccessibilityEvent(accessibilityevent);
    }

    protected void dispatchRestoreInstanceState(SparseArray sparsearray)
    {
        dispatchThawSelfOnly(sparsearray);
    }

    protected void dispatchSaveInstanceState(SparseArray sparsearray)
    {
        dispatchFreezeSelfOnly(sparsearray);
    }

    int findSyncPosition()
    {
        int i1 = mItemCount;
        int l = -1;
        if(i1 == 0)
            return l;

        Adapter adapter;
        int i;
        boolean flag;
        int j;
        int k;
        long l1;
        long l2;
        boolean flag1;

        l1 = mSyncRowId;
        i = mSyncPosition;
        if(l1 == 0x8000000000000000L)
            return -1;
        i = Math.min(i1 - 1, Math.max(0, i));
        l2 = SystemClock.uptimeMillis();
        j = i;
        k = i;
        flag = false;
        adapter = getAdapter();
        if(adapter == null)
            return -1;


        for(;SystemClock.uptimeMillis() <= l2 + 100L;) {
            l = i;
            if (adapter.getItemId(i) == l1)
                return l;

            if (k == i1 - 1)
                l = 1;
            else
                l = 0;
            if (j == 0)
                flag1 = true;
            else
                flag1 = false;
            if (l == 0 || !flag1) {
                if (flag1 || flag && l == 0) {
                    k++;
                    i = k;
                    flag = false;
                } else if (l != 0 || !flag && !flag1) {
                    j--;
                    i = j;
                    flag = true;
                }
            } else {
                break;
            }
        }

        return -1;
    }

    public abstract Adapter getAdapter();

    public int getCount()
    {
        return mItemCount;
    }

    public View getEmptyView()
    {
        return mEmptyView;
    }

    public int getFirstVisiblePosition()
    {
        return mFirstPosition;
    }

    public Object getItemAtPosition(int i)
    {
        Adapter adapter = getAdapter();
        if(adapter == null || i < 0)
            return null;
        else
            return adapter.getItem(i);
    }

    public long getItemIdAtPosition(int i)
    {
        Adapter adapter = getAdapter();
        if(adapter == null || i < 0)
            return 0x8000000000000000L;
        else
            return adapter.getItemId(i);
    }

    public int getLastVisiblePosition()
    {
        return (mFirstPosition + getChildCount()) - 1;
    }

    public final OnItemClickListener getOnItemClickListener()
    {
        return mOnItemClickListener;
    }

    public final OnItemLongClickListener getOnItemLongClickListener()
    {
        return mOnItemLongClickListener;
    }

    public final OnItemSelectedListener getOnItemSelectedListener()
    {
        return mOnItemSelectedListener;
    }

    public int getPositionForView(View view) {
        View view1;
        boolean flag;
        for(;;) {
            view1 = (View) view.getParent();
            flag = view1.equals(this);
            if (flag) {
                int j = getChildCount();
                int i = 0;
                while (i < j) {
                    if (getChildAt(i).equals(view))
                        return mFirstPosition + i;
                    i++;
                }
                break;
            } else
                view = view1;
        }
        return -1;
    }

    public Object getSelectedItem()
    {
        Adapter adapter = getAdapter();
        int i = getSelectedItemPosition();
        if(adapter != null && adapter.getCount() > 0 && i >= 0)
            return adapter.getItem(i);
        else
            return null;
    }

    public long getSelectedItemId()
    {
        return mNextSelectedRowId;
    }

    public int getSelectedItemPosition()
    {
        return mNextSelectedPosition;
    }

    public abstract View getSelectedView();

    void handleDataChanged()
    {
        int k = mItemCount;
        int i = 0;
        boolean flag1 = false;
        if(k > 0)
        {
            boolean flag = flag1;
            if(mNeedSync)
            {
                mNeedSync = false;
                i = findSyncPosition();
                flag = flag1;
                if(i >= 0)
                {
                    flag = flag1;
                    if(lookForSelectablePosition(i, true) == i)
                    {
                        setNextSelectedPositionInt(i);
                        flag = true;
                    }
                }
            }
            i = ((flag) ? 1 : 0);
            if(!flag)
            {
                int j = getSelectedItemPosition();
                i = j;
                if(j >= k)
                    i = k - 1;
                j = i;
                if(i < 0)
                    j = 0;
                i = lookForSelectablePosition(j, true);
                k = i;
                if(i < 0)
                    k = lookForSelectablePosition(j, false);
                i = ((flag) ? 1 : 0);
                if(k >= 0)
                {
                    setNextSelectedPositionInt(k);
                    checkSelectionChanged();
                    i = 1;
                }
            }
        }
        if(i == 0)
        {
            mSelectedPosition = -1;
            mSelectedRowId = 0x8000000000000000L;
            mNextSelectedPosition = -1;
            mNextSelectedRowId = 0x8000000000000000L;
            mNeedSync = false;
            checkSelectionChanged();
        }
    }

    boolean isInFilterMode()
    {
        return false;
    }

    int lookForSelectablePosition(int i, boolean flag)
    {
        return i;
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        removeCallbacks(mSelectionNotifier);
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l)
    {
        mLayoutHeight = getHeight();
    }

    public boolean performItemClick(View view, int i, long l)
    {
        boolean flag = false;
        if(mOnItemClickListener != null)
        {
            playSoundEffect(0);
            if(view != null)
                view.sendAccessibilityEvent(1);
            mOnItemClickListener.onItemClick(this, view, i, l);
            flag = true;
        }
        return flag;
    }

    void rememberSyncState()
    {
            if(getChildCount() > 0)
            {
                mNeedSync = true;
                mSyncHeight = mLayoutHeight;
                if(mSelectedPosition < 0) {
                    View view1 = getChildAt(0);
                    Adapter adapter = getAdapter();
                    if(mFirstPosition >= 0 && mFirstPosition < adapter.getCount())
                        mSyncRowId = adapter.getItemId(mFirstPosition);
                    else
                        mSyncRowId = -1L;
                    mSyncPosition = mFirstPosition;
                    if(view1 != null)
                        mSpecificTop = view1.getTop();
                    mSyncMode = 1;

                } else {
                    View view = getChildAt(mSelectedPosition - mFirstPosition);
                    mSyncRowId = mNextSelectedRowId;
                    mSyncPosition = mNextSelectedPosition;
                    if (view != null)
                        mSpecificTop = view.getTop();
                    mSyncMode = 0;
                }
            }
            return;
    }

    public void removeAllViews()
    {
        throw new UnsupportedOperationException("removeAllViews() is not supported in AdapterView");
    }

    public void removeView(View view)
    {
        throw new UnsupportedOperationException("removeView(View) is not supported in AdapterView");
    }

    public void removeViewAt(int i)
    {
        throw new UnsupportedOperationException("removeViewAt(int) is not supported in AdapterView");
    }

    void selectionChanged()
    {
        if(mOnItemSelectedListener != null)
            if(mInLayout || mBlockLayoutRequests)
            {
                if(mSelectionNotifier == null)
                    mSelectionNotifier = new SelectionNotifier();
                post(mSelectionNotifier);
            } else
            {
                fireOnSelected();
            }
        if(mSelectedPosition != -1 && isShown() && !isInTouchMode())
            sendAccessibilityEvent(4);
    }

    public abstract void setAdapter(Adapter adapter);

    public void setEmptyView(View view)
    {
        mEmptyView = view;
        Adapter adapter = getAdapter();
        boolean flag;
        if(adapter == null || adapter.isEmpty())
            flag = true;
        else
            flag = false;
        updateEmptyStatus(flag);
    }

    public void setFocusable(boolean flag)
    {
        boolean flag2 = true;
        Adapter adapter = getAdapter();
        boolean flag1;
        if(adapter == null || adapter.getCount() == 0)
            flag1 = true;
        else
            flag1 = false;
        mDesiredFocusableState = flag;
        if(!flag)
            mDesiredFocusableInTouchModeState = false;
        if(!flag)
            flag = false;
        else {
            flag = flag2;
            if (flag1) {
                if (!isInFilterMode())
                    flag = false;
                else
                    flag = flag2;
            }
        }

        super.setFocusable(flag);
        return;
    }

    public void setFocusableInTouchMode(boolean flag)
    {
        boolean flag2 = true;
        Adapter adapter = getAdapter();
        boolean flag1;
        if(adapter == null || adapter.getCount() == 0)
            flag1 = true;
        else
            flag1 = false;
        mDesiredFocusableInTouchModeState = flag;
        if(flag)
            mDesiredFocusableState = true;
        if(!flag)
            flag = false;
        else {
            flag = flag2;
            if (flag1) {
                if (!isInFilterMode())
                    flag = false;
                else
                    flag = flag2;
            }
        }

        super.setFocusableInTouchMode(flag);
        return;
    }

    void setNextSelectedPositionInt(int i)
    {
        mNextSelectedPosition = i;
        mNextSelectedRowId = getItemIdAtPosition(i);
        if(mNeedSync && mSyncMode == 0 && i >= 0)
        {
            mSyncPosition = i;
            mSyncRowId = mNextSelectedRowId;
        }
    }

    public void setOnClickListener(android.view.View.OnClickListener onclicklistener)
    {
        throw new RuntimeException("Don't call setOnClickListener for an AdapterView. You probably want setOnItemClickListener instead");
    }

    public void setOnItemClickListener(OnItemClickListener onitemclicklistener)
    {
        mOnItemClickListener = onitemclicklistener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onitemlongclicklistener)
    {
        if(!isLongClickable())
            setLongClickable(true);
        mOnItemLongClickListener = onitemlongclicklistener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onitemselectedlistener)
    {
        mOnItemSelectedListener = onitemselectedlistener;
    }

    void setSelectedPositionInt(int i)
    {
        mSelectedPosition = i;
        mSelectedRowId = getItemIdAtPosition(i);
    }

    public abstract void setSelection(int i);

    public static final int INVALID_POSITION = -1;
    public static final long INVALID_ROW_ID = 0x8000000000000000L;
    static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -2;
    static final int ITEM_VIEW_TYPE_IGNORE = -1;
    static final int SYNC_FIRST_POSITION = 1;
    static final int SYNC_MAX_DURATION_MILLIS = 100;
    static final int SYNC_SELECTED_POSITION = 0;
    boolean mBlockLayoutRequests;
    boolean mDataChanged;
    private boolean mDesiredFocusableInTouchModeState;
    private boolean mDesiredFocusableState;
    private View mEmptyView;
    int mFirstPosition;
    boolean mInLayout;
    int mItemCount;
    private int mLayoutHeight;
    boolean mNeedSync;
    int mNextSelectedPosition;
    long mNextSelectedRowId;
    int mOldItemCount;
    int mOldSelectedPosition;
    long mOldSelectedRowId;
    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;
    OnItemSelectedListener mOnItemSelectedListener;
    int mSelectedPosition;
    long mSelectedRowId;
    private SelectionNotifier mSelectionNotifier;
    int mSpecificTop;
    long mSyncHeight;
    int mSyncMode;
    int mSyncPosition;
    long mSyncRowId;



}
