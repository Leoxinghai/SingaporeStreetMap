

package android.support.v7.internal.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.ListPopupWindow;
import android.util.*;
import android.view.*;
import android.widget.*;

// Referenced classes of package android.support.v7.internal.widget:
//            AbsSpinnerCompat, TintTypedArray, TintManager, ViewUtils

class SpinnerCompat extends AbsSpinnerCompat
    implements android.content.DialogInterface.OnClickListener
{
    private class DialogPopup
        implements SpinnerPopup, android.content.DialogInterface.OnClickListener
    {

        public void dismiss()
        {
            if(mPopup != null)
            {
                mPopup.dismiss();
                mPopup = null;
            }
        }

        public Drawable getBackground()
        {
            return null;
        }

        public CharSequence getHintText()
        {
            return mPrompt;
        }

        public int getHorizontalOffset()
        {
            return 0;
        }

        public int getVerticalOffset()
        {
            return 0;
        }

        public boolean isShowing()
        {
            if(mPopup != null)
                return mPopup.isShowing();
            else
                return false;
        }

        public void onClick(DialogInterface dialoginterface, int i)
        {
            setSelection(i);
            if(mOnItemClickListener != null)
                performItemClick(null, i, mListAdapter.getItemId(i));
            dismiss();
        }

        public void setAdapter(ListAdapter listadapter)
        {
            mListAdapter = listadapter;
        }

        public void setBackgroundDrawable(Drawable drawable)
        {
            Log.e("Spinner", "Cannot set popup background for MODE_DIALOG, ignoring");
        }

        public void setHorizontalOffset(int i)
        {
            Log.e("Spinner", "Cannot set horizontal offset for MODE_DIALOG, ignoring");
        }

        public void setPromptText(CharSequence charsequence)
        {
            mPrompt = charsequence;
        }

        public void setVerticalOffset(int i)
        {
            Log.e("Spinner", "Cannot set vertical offset for MODE_DIALOG, ignoring");
        }

        public void show()
        {
            if(mListAdapter == null)
                return;
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            if(mPrompt != null)
                builder.setTitle(mPrompt);
            mPopup = builder.setSingleChoiceItems(mListAdapter, getSelectedItemPosition(), this).create();
            mPopup.show();
        }

        private ListAdapter mListAdapter;
        private AlertDialog mPopup;
        private CharSequence mPrompt;

        private DialogPopup()
        {
            super();
        }

    }

    private static class DropDownAdapter
        implements ListAdapter, SpinnerAdapter
    {

        public boolean areAllItemsEnabled()
        {
            ListAdapter listadapter = mListAdapter;
            if(listadapter != null)
                return listadapter.areAllItemsEnabled();
            else
                return true;
        }

        public int getCount()
        {
            if(mAdapter == null)
                return 0;
            else
                return mAdapter.getCount();
        }

        public View getDropDownView(int i, View view, ViewGroup viewgroup)
        {
            if(mAdapter == null)
                return null;
            else
                return mAdapter.getDropDownView(i, view, viewgroup);
        }

        public Object getItem(int i)
        {
            if(mAdapter == null)
                return null;
            else
                return mAdapter.getItem(i);
        }

        public long getItemId(int i)
        {
            if(mAdapter == null)
                return -1L;
            else
                return mAdapter.getItemId(i);
        }

        public int getItemViewType(int i)
        {
            return 0;
        }

        public View getView(int i, View view, ViewGroup viewgroup)
        {
            return getDropDownView(i, view, viewgroup);
        }

        public int getViewTypeCount()
        {
            return 1;
        }

        public boolean hasStableIds()
        {
            return mAdapter != null && mAdapter.hasStableIds();
        }

        public boolean isEmpty()
        {
            return getCount() == 0;
        }

        public boolean isEnabled(int i)
        {
            ListAdapter listadapter = mListAdapter;
            if(listadapter != null)
                return listadapter.isEnabled(i);
            else
                return true;
        }

        public void registerDataSetObserver(DataSetObserver datasetobserver)
        {
            if(mAdapter != null)
                mAdapter.registerDataSetObserver(datasetobserver);
        }

        public void unregisterDataSetObserver(DataSetObserver datasetobserver)
        {
            if(mAdapter != null)
                mAdapter.unregisterDataSetObserver(datasetobserver);
        }

        private SpinnerAdapter mAdapter;
        private ListAdapter mListAdapter;

        public DropDownAdapter(SpinnerAdapter spinneradapter)
        {
            mAdapter = spinneradapter;
            if(spinneradapter instanceof ListAdapter)
                mListAdapter = (ListAdapter)spinneradapter;
        }
    }

    private class DropdownPopup extends ListPopupWindow
        implements SpinnerPopup
    {

        void computeContentWidth()
        {
            Drawable drawable = getBackground();
            int i = 0;
            int i1;
            int j1;
            int k1;
            int j;
            int k;
            int l;
            if(drawable != null)
            {
                drawable.getPadding(mTempRect);
                if(ViewUtils.isLayoutRtl(SpinnerCompat.this))
                    i = mTempRect.right;
                else
                    i = -mTempRect.left;
            } else
            {
                Rect rect = mTempRect;
                mTempRect.right = 0;
                rect.left = 0;
            }
            i1 = getPaddingLeft();
            j1 = getPaddingRight();
            k1 = getWidth();
            if(mDropDownWidth == -2)
            {
                k = measureContentWidth((SpinnerAdapter)mAdapter, getBackground());
                l = getContext().getResources().getDisplayMetrics().widthPixels - mTempRect.left - mTempRect.right;
                j = k;
                if(k > l)
                    j = l;
                setContentWidth(Math.max(j, k1 - i1 - j1));
            } else
            if(mDropDownWidth == -1)
                setContentWidth(k1 - i1 - j1);
            else
                setContentWidth(mDropDownWidth);
            if(ViewUtils.isLayoutRtl(SpinnerCompat.this))
                i += k1 - j1 - getWidth();
            else
                i += i1;
            setHorizontalOffset(i);
        }

        public CharSequence getHintText()
        {
            return mHintText;
        }

        public void setAdapter(ListAdapter listadapter)
        {
            super.setAdapter(listadapter);
            mAdapter = listadapter;
        }

        public void setPromptText(CharSequence charsequence)
        {
            mHintText = charsequence;
        }

        public void show(int i, int j)
        {
            boolean flag = isShowing();
            computeContentWidth();
            setInputMethodMode(2);
            super.show();
            getListView().setChoiceMode(1);
            setSelection(getSelectedItemPosition());
            ViewTreeObserver viewtreeobserver;
            if(!flag)
                if((viewtreeobserver = getViewTreeObserver()) != null)
                {
                    final android.view.ViewTreeObserver.OnGlobalLayoutListener ongloballayoutlistener = new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                        public void onGlobalLayout()
                        {
                            computeContentWidth();
                            show();
                        }

                    };
                    viewtreeobserver.addOnGlobalLayoutListener(ongloballayoutlistener);
                    setOnDismissListener(new android.widget.PopupWindow.OnDismissListener() {

                        public void onDismiss()
                        {
                            ViewTreeObserver viewtreeobserver = getViewTreeObserver();
                            if(viewtreeobserver != null)
                                viewtreeobserver.removeGlobalOnLayoutListener(ongloballayoutlistener);
                        }

                    });
                    return;
                }
        }

        private ListAdapter mAdapter;
        private CharSequence mHintText;



        public DropdownPopup(Context context, AttributeSet attributeset, int i)
        {
            super(context, attributeset, i);
            setAnchorView(SpinnerCompat.this);
            setModal(true);
            setPromptPosition(0);
            setOnItemClickListener(new _cls1());
        }

        class _cls1
                implements android.widget.AdapterView.OnItemClickListener
        {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                setSelection(i);
                if(mOnItemClickListener != null)
                    performItemClick(i);
                dismiss();
            }

        }

    }

    static class SavedState extends AbsSpinnerCompat.SavedState
    {

        public void writeToParcel(Parcel parcel, int i)
        {
            super.writeToParcel(parcel, i);
            if(showDropdown)
                i = 1;
            else
                i = 0;
            parcel.writeByte((byte)i);
        }

        public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

            public SavedState createFromParcel(Parcel parcel)
            {
                return new SavedState(parcel);
            }
            public SavedState[] newArray(int i)
            {
                return new SavedState[i];
            }
        };

        boolean showDropdown;


        private SavedState(Parcel parcel)
        {
            super(parcel);
            boolean flag;
            if(parcel.readByte() != 0)
                flag = true;
            else
                flag = false;
            showDropdown = flag;
        }


        SavedState(Parcelable parcelable)
        {
            super(parcelable);
        }
    }

    private static interface SpinnerPopup
    {

        public abstract void dismiss();

        public abstract Drawable getBackground();

        public abstract CharSequence getHintText();

        public abstract int getHorizontalOffset();

        public abstract int getVerticalOffset();

        public abstract boolean isShowing();

        public abstract void setAdapter(ListAdapter listadapter);

        public abstract void setBackgroundDrawable(Drawable drawable);

        public abstract void setHorizontalOffset(int i);

        public abstract void setPromptText(CharSequence charsequence);

        public abstract void setVerticalOffset(int i);

        public abstract void show();
    }


    SpinnerCompat(Context context)
    {
        this(context, ((AttributeSet) (null)));
    }

    SpinnerCompat(Context context, int i)
    {
        this(context, null, android.support.v7.appcompat.R.attr.spinnerStyle, i);
    }

    SpinnerCompat(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, android.support.v7.appcompat.R.attr.spinnerStyle);
    }

    SpinnerCompat(Context context, AttributeSet attributeset, int i)
    {
        this(context, attributeset, i, -1);
    }

    SpinnerCompat(Context context, AttributeSet attributeset, int i, int j)
    {
        super(context, attributeset, i);
        TintTypedArray tinttypedarray;
        int k;
        mTempRect = new Rect();
        tinttypedarray = TintTypedArray.obtainStyledAttributes(context, attributeset, android.support.v7.appcompat.R.styleable.Spinner, i, 0);
        /*
        if(tinttypedarray.hasValue(android.support.v7.appcompat.R.styleable.Spinner_android_background))
            setBackgroundDrawable(tinttypedarray.getDrawable(android.support.v7.appcompat.R.styleable.Spinner_android_background));
        k = j;
        if(j == -1)
            k = tinttypedarray.getInt(android.support.v7.appcompat.R.styleable.Spinner_spinnerMode, 0);

        switch(k) {
            default:
                break;
            case 0:
                mPopup = new DialogPopup();
                break;
            case 1:
                final DropdownPopup dropdownPopup = new DropdownPopup();
                mDropDownWidth = tinttypedarray.getLayoutDimension(android.support.v7.appcompat.R.styleable.Spinner_android_dropDownWidth, -2);
                dropdownPopup.setBackgroundDrawable(tinttypedarray.getDrawable(android.support.v7.appcompat.R.styleable.Spinner_android_popupBackground));
                mForwardingListener = new android.support.v7.widget.ListPopupWindow.ForwardingListener(context) {

                    public ListPopupWindow getPopup() {
                        return dropdownPopup;
                    }


                    public boolean onForwardingStarted() {
                        if (!mPopup.isShowing())
                            mPopup.show();
                        return true;
                    }

                }
                break;
        }
        mGravity = tinttypedarray.getInt(android.support.v7.appcompat.R.styleable.Spinner_android_gravity, 17);
        mPopup.setPromptText(tinttypedarray.getString(android.support.v7.appcompat.R.styleable.Spinner_prompt));
        mDisableChildrenWhenDisabled = tinttypedarray.getBoolean(android.support.v7.appcompat.R.styleable.Spinner_disableChildrenWhenDisabled, false);
        tinttypedarray.recycle();
        if (mTempAdapter != null) {
            mPopup.setAdapter(mTempAdapter);
            mTempAdapter = null;
        }
        */
        mTintManager = tinttypedarray.getTintManager();
        return;
    }

    private View makeView(int i, boolean flag)
    {
        if(!mDataChanged)
        {
            View view = mRecycler.get(i);
            if(view != null)
            {
                setUpChild(view, flag);
                return view;
            }
        }
        View view1 = mAdapter.getView(i, null, this);
        setUpChild(view1, flag);
        return view1;
    }

    private void setUpChild(View view, boolean flag)
    {
        android.view.ViewGroup.LayoutParams layoutparams1 = view.getLayoutParams();
        android.view.ViewGroup.LayoutParams layoutparams = layoutparams1;
        if(layoutparams1 == null)
            layoutparams = generateDefaultLayoutParams();
        if(flag)
            addViewInLayout(view, 0, layoutparams);
        view.setSelected(hasFocus());
        if(mDisableChildrenWhenDisabled)
            view.setEnabled(isEnabled());
        int i = ViewGroup.getChildMeasureSpec(mHeightMeasureSpec, mSpinnerPadding.top + mSpinnerPadding.bottom, layoutparams.height);
        view.measure(ViewGroup.getChildMeasureSpec(mWidthMeasureSpec, mSpinnerPadding.left + mSpinnerPadding.right, layoutparams.width), i);
        i = mSpinnerPadding.top + (getMeasuredHeight() - mSpinnerPadding.bottom - mSpinnerPadding.top - view.getMeasuredHeight()) / 2;
        int j = view.getMeasuredHeight();
        view.layout(0, i, 0 + view.getMeasuredWidth(), i + j);
    }

    public int getBaseline()
    {
        Object obj;
        byte byte0;
        byte0 = -1;
        obj = null;
        View view;
        view = null;
        if(getChildCount() <= 0) {
            if(mAdapter != null)
            {
                if(mAdapter.getCount() > 0)
                {
                    view = makeView(0, false);
                    mRecycler.put(0, view);
                }
            }
        } else
            view = getChildAt(0);

        int i = byte0;
        if(view != null)
        {
            int j = view.getBaseline();
            i = byte0;
            if(j >= 0)
                i = view.getTop() + j;
        }
        return i;
    }

    public int getDropDownHorizontalOffset()
    {
        return mPopup.getHorizontalOffset();
    }

    public int getDropDownVerticalOffset()
    {
        return mPopup.getVerticalOffset();
    }

    public int getDropDownWidth()
    {
        return mDropDownWidth;
    }

    public Drawable getPopupBackground()
    {
        return mPopup.getBackground();
    }

    public CharSequence getPrompt()
    {
        return mPopup.getHintText();
    }

    void layout(int i, boolean flag) {
        int j;
        int k;
        j = mSpinnerPadding.left;
        k = getRight() - getLeft() - mSpinnerPadding.left - mSpinnerPadding.right;
        if (mDataChanged)
            handleDataChanged();
        if (mItemCount == 0) {
            resetList();
            return;
        }
        if (mNextSelectedPosition >= 0)
            setSelectedPositionInt(mNextSelectedPosition);
        recycleAllViews();
        removeAllViewsInLayout();
        mFirstPosition = mSelectedPosition;
        View view;
        int l;
        int i1;
        if (mAdapter != null) {
            view = makeView(mSelectedPosition, true);
            l = view.getMeasuredWidth();
            i = j;
            i1 = ViewCompat.getLayoutDirection(this);
            switch (GravityCompat.getAbsoluteGravity(mGravity, i1) & 7) {
                default:
                    break;
                case 1:
                    i = (k / 2 + j) - l / 2;
                    break;
                case 5:
                    i = (j + k) - l;
                    break;
            }
            view.offsetLeftAndRight(i);
        }

        mRecycler.clear();
        invalidate();
        checkSelectionChanged();
        mDataChanged = false;
        mNeedSync = false;
        setNextSelectedPositionInt(mSelectedPosition);
        return;
    }

    int measureContentWidth(SpinnerAdapter spinneradapter, Drawable drawable)
    {
        int j;
        if(spinneradapter == null)
        {
            j = 0;
        } else
        {
            int i = 0;
            View view = null;
            int k = 0;
            int j1 = android.view.View.MeasureSpec.makeMeasureSpec(0, 0);
            int k1 = android.view.View.MeasureSpec.makeMeasureSpec(0, 0);
            j = Math.max(0, getSelectedItemPosition());
            int l1 = Math.min(spinneradapter.getCount(), j + 15);
            for(j = Math.max(0, j - (15 - (l1 - j))); j < l1;)
            {
                int i1 = spinneradapter.getItemViewType(j);
                int l = k;
                if(i1 != k)
                {
                    l = i1;
                    view = null;
                }
                view = spinneradapter.getView(j, view, this);
                if(view.getLayoutParams() == null)
                    view.setLayoutParams(new android.view.ViewGroup.LayoutParams(-2, -2));
                view.measure(j1, k1);
                i = Math.max(i, view.getMeasuredWidth());
                j++;
                k = l;
            }

            j = i;
            if(drawable != null)
            {
                drawable.getPadding(mTempRect);
                return i + (mTempRect.left + mTempRect.right);
            }
        }
        return j;
    }

    public void onClick(DialogInterface dialoginterface, int i)
    {
        setSelection(i);
        dialoginterface.dismiss();
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if(mPopup != null && mPopup.isShowing())
            mPopup.dismiss();
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l)
    {
        super.onLayout(flag, i, j, k, l);
        mInLayout = true;
        layout(0, false);
        mInLayout = false;
    }

    protected void onMeasure(int i, int j)
    {
        super.onMeasure(i, j);
        if(mPopup != null && android.view.View.MeasureSpec.getMode(i) == 0x80000000)
            setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), measureContentWidth(getAdapter(), getBackground())), android.view.View.MeasureSpec.getSize(i)), getMeasuredHeight());
    }

    public void onRestoreInstanceState(Parcelable parcelable)
    {
        super.onRestoreInstanceState(((SavedState)parcelable).getSuperState());
        SavedState savedState = (SavedState)parcelable;
        if(savedState.showDropdown)
        {
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if(viewTreeObserver != null)
                viewTreeObserver.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                    public void onGlobalLayout()
                    {
                        if(!mPopup.isShowing())
                            mPopup.show();
                        ViewTreeObserver viewtreeobserver = getViewTreeObserver();
                        if(viewtreeobserver != null)
                            viewtreeobserver.removeGlobalOnLayoutListener(this);
                    }

                });
        }
    }

    public Parcelable onSaveInstanceState()
    {
        SavedState savedstate = new SavedState(super.onSaveInstanceState());
        boolean flag;
        if(mPopup != null && mPopup.isShowing())
            flag = true;
        else
            flag = false;
        savedstate.showDropdown = flag;
        return savedstate;
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        if(mForwardingListener != null && mForwardingListener.onTouch(this, motionevent))
            return true;
        else
            return super.onTouchEvent(motionevent);
    }

    public boolean performClick()
    {
        boolean flag1 = super.performClick();
        boolean flag = flag1;
        if(!flag1)
        {
            boolean flag2 = true;
            flag = flag2;
            if(!mPopup.isShowing())
            {
                mPopup.show();
                flag = flag2;
            }
        }
        return flag;
    }

    public void setAdapter(Adapter adapter)
    {
        setAdapter((SpinnerAdapter)adapter);
    }

    public void setAdapter(SpinnerAdapter spinneradapter)
    {
        super.setAdapter(spinneradapter);
        mRecycler.clear();
        if(getContext().getApplicationInfo().targetSdkVersion >= 21 && spinneradapter != null && spinneradapter.getViewTypeCount() != 1)
            throw new IllegalArgumentException("Spinner adapter view type count must be 1");
        if(mPopup != null)
        {
            mPopup.setAdapter(new DropDownAdapter(spinneradapter));
            return;
        } else
        {
            mTempAdapter = new DropDownAdapter(spinneradapter);
            return;
        }
    }

    public void setDropDownHorizontalOffset(int i)
    {
        mPopup.setHorizontalOffset(i);
    }

    public void setDropDownVerticalOffset(int i)
    {
        mPopup.setVerticalOffset(i);
    }

    public void setDropDownWidth(int i)
    {
        if(!(mPopup instanceof DropdownPopup))
        {
            Log.e("Spinner", "Cannot set dropdown width for MODE_DIALOG, ignoring");
            return;
        } else
        {
            mDropDownWidth = i;
            return;
        }
    }

    public void setEnabled(boolean flag)
    {
        super.setEnabled(flag);
        if(mDisableChildrenWhenDisabled)
        {
            int j = getChildCount();
            for(int i = 0; i < j; i++)
                getChildAt(i).setEnabled(flag);

        }
    }

    public void setGravity(int i)
    {
        if(mGravity != i)
        {
            int j = i;
            if((i & 7) == 0)
                j = i | 0x800003;
            mGravity = j;
            requestLayout();
        }
    }

    public void setOnItemClickListener(AdapterViewCompat.OnItemClickListener onitemclicklistener)
    {
        throw new RuntimeException("setOnItemClickListener cannot be used with a spinner.");
    }

    void setOnItemClickListenerInt(AdapterViewCompat.OnItemClickListener onitemclicklistener)
    {
        super.setOnItemClickListener(onitemclicklistener);
    }

    public void setPopupBackgroundDrawable(Drawable drawable)
    {
        if(!(mPopup instanceof DropdownPopup))
        {
            Log.e("Spinner", "setPopupBackgroundDrawable: incompatible spinner mode; ignoring...");
            return;
        } else
        {
            ((DropdownPopup)mPopup).setBackgroundDrawable(drawable);
            return;
        }
    }

    public void setPopupBackgroundResource(int i)
    {
        setPopupBackgroundDrawable(mTintManager.getDrawable(i));
    }

    public void setPrompt(CharSequence charsequence)
    {
        mPopup.setPromptText(charsequence);
    }

    public void setPromptId(int i)
    {
        setPrompt(getContext().getText(i));
    }

    private static final int MAX_ITEMS_MEASURED = 15;
    public static final int MODE_DIALOG = 0;
    public static final int MODE_DROPDOWN = 1;
    private static final int MODE_THEME = -1;
    private static final String TAG = "Spinner";
    private boolean mDisableChildrenWhenDisabled;
    int mDropDownWidth;
    private android.support.v7.widget.ListPopupWindow.ForwardingListener mForwardingListener;
    private int mGravity;
    private SpinnerPopup mPopup;
    private DropDownAdapter mTempAdapter;
    private Rect mTempRect;
    private final TintManager mTintManager;

}
