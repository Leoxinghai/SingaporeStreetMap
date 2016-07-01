// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v7.internal.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;

// Referenced classes of package android.support.v7.internal.widget:
//            ActivityChooserModel, TintTypedArray

public class ActivityChooserView extends ViewGroup
    implements ActivityChooserModel.ActivityChooserModelClient
{
    private class ActivityChooserViewAdapter extends BaseAdapter
    {

        public int getActivityCount()
        {
            return mDataModel.getActivityCount();
        }

        public int getCount()
        {
            int j = mDataModel.getActivityCount();
            int i = j;
            if(!mShowDefaultActivity)
            {
                i = j;
                if(mDataModel.getDefaultActivity() != null)
                    i = j - 1;
            }
            j = Math.min(i, mMaxActivityCount);
            i = j;
            if(mShowFooterView)
                i = j + 1;
            return i;
        }

        public ActivityChooserModel getDataModel()
        {
            return mDataModel;
        }

        public ResolveInfo getDefaultActivity()
        {
            return mDataModel.getDefaultActivity();
        }

        public int getHistorySize()
        {
            return mDataModel.getHistorySize();
        }

        public Object getItem(int i)
        {
            int j;
            switch(getItemViewType(i))
            {
            default:
                throw new IllegalArgumentException();

            case 1: // '\001'
                return null;

            case 0: // '\0'
                j = i;
                break;
            }
            if(!mShowDefaultActivity)
            {
                j = i;
                if(mDataModel.getDefaultActivity() != null)
                    j = i + 1;
            }
            return mDataModel.getActivity(j);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        public int getItemViewType(int i)
        {
            return !mShowFooterView || i != getCount() - 1 ? 0 : 1;
        }

        public boolean getShowDefaultActivity()
        {
            return mShowDefaultActivity;
        }

        public View getView(int i, View view, ViewGroup viewgroup)
        {
            switch(getItemViewType(i)) {
                default:
                    throw new IllegalArgumentException();
                case 1:
                    View view1 = view;
                    if (view == null || view.getId() != 1) {
                        view1 = LayoutInflater.from(getContext()).inflate(android.support.v7.appcompat.R.layout.abc_activity_chooser_view_list_item, viewgroup, false);
                        view1.setId(1);
                        ((TextView) view1.findViewById(android.support.v7.appcompat.R.id.title)).setText(getContext().getString(android.support.v7.appcompat.R.string.abc_activity_chooser_view_see_all));
                    }
                    return view1;
                case 0:
                    view1 = view;
                    if (view == null || view.getId() != android.support.v7.appcompat.R.id.list_item)
                        view1 = LayoutInflater.from(getContext()).inflate(android.support.v7.appcompat.R.layout.abc_activity_chooser_view_list_item, viewgroup, false);

                    PackageManager packageManager = getContext().getPackageManager();
                    ImageView imageView = (ImageView) view1.findViewById(android.support.v7.appcompat.R.id.icon);
                    ResolveInfo resolveinfo = (ResolveInfo) getItem(i);
                    imageView.setImageDrawable(resolveinfo.loadIcon(packageManager));
                    ((TextView) view1.findViewById(android.support.v7.appcompat.R.id.title)).setText(resolveinfo.loadLabel(packageManager));
                    if (mShowDefaultActivity && i == 0 && mHighlightDefaultActivity)
                        ViewCompat.setActivated(view1, true);
                    else
                        ViewCompat.setActivated(view1, false);
                    return view1;
            }
        }

        public int getViewTypeCount()
        {
            return 3;
        }

        public int measureContentWidth()
        {
            int k = mMaxActivityCount;
            mMaxActivityCount = 0x7fffffff;
            int j = 0;
            View view = null;
            int l = android.view.View.MeasureSpec.makeMeasureSpec(0, 0);
            int i1 = android.view.View.MeasureSpec.makeMeasureSpec(0, 0);
            int j1 = getCount();
            for(int i = 0; i < j1; i++)
            {
                view = getView(i, view, null);
                view.measure(l, i1);
                j = Math.max(j, view.getMeasuredWidth());
            }

            mMaxActivityCount = k;
            return j;
        }

        public void setDataModel(ActivityChooserModel activitychoosermodel)
        {
            ActivityChooserModel activitychoosermodel1 = mAdapter.getDataModel();
            if(activitychoosermodel1 != null && isShown())
                activitychoosermodel1.unregisterObserver(mModelDataSetOberver);
            mDataModel = activitychoosermodel;
            if(activitychoosermodel != null && isShown())
                activitychoosermodel.registerObserver(mModelDataSetOberver);
            notifyDataSetChanged();
        }

        public void setMaxActivityCount(int i)
        {
            if(mMaxActivityCount != i)
            {
                mMaxActivityCount = i;
                notifyDataSetChanged();
            }
        }

        public void setShowDefaultActivity(boolean flag, boolean flag1)
        {
            if(mShowDefaultActivity != flag || mHighlightDefaultActivity != flag1)
            {
                mShowDefaultActivity = flag;
                mHighlightDefaultActivity = flag1;
                notifyDataSetChanged();
            }
        }

        public void setShowFooterView(boolean flag)
        {
            if(mShowFooterView != flag)
            {
                mShowFooterView = flag;
                notifyDataSetChanged();
            }
        }

        private static final int ITEM_VIEW_TYPE_ACTIVITY = 0;
        private static final int ITEM_VIEW_TYPE_COUNT = 3;
        private static final int ITEM_VIEW_TYPE_FOOTER = 1;
        public static final int MAX_ACTIVITY_COUNT_DEFAULT = 4;
        public static final int MAX_ACTIVITY_COUNT_UNLIMITED = 0x7fffffff;
        private ActivityChooserModel mDataModel;
        private boolean mHighlightDefaultActivity;
        private int mMaxActivityCount;
        private boolean mShowDefaultActivity;
        private boolean mShowFooterView;

        private ActivityChooserViewAdapter()
        {
            super();
            mMaxActivityCount = 4;
        }

    }

    private class Callbacks
        implements android.widget.AdapterView.OnItemClickListener, android.view.View.OnClickListener, android.view.View.OnLongClickListener, android.widget.PopupWindow.OnDismissListener
    {

        private void notifyOnDismissListener()
        {
            if(mOnDismissListener != null)
                mOnDismissListener.onDismiss();
        }

        public void onClick(View view)
        {
            if(view == mDefaultActivityButton)
            {
                dismissPopup();
                ResolveInfo resolveInfo = mAdapter.getDefaultActivity();
                int i = mAdapter.getDataModel().getActivityIndex(resolveInfo);
                Intent intent = mAdapter.getDataModel().chooseActivity(i);
                if(intent != null)
                {
                    intent.addFlags(0x80000);
                    getContext().startActivity(intent);
                }
                return;
            }
            if(view == mExpandActivityOverflowButton)
            {
                mIsSelectingDefaultActivity = false;
                showPopupUnchecked(mInitialActivityCount);
                return;
            } else
            {
                throw new IllegalArgumentException();
            }
        }

        public void onDismiss()
        {
            notifyOnDismissListener();
            if(mProvider != null)
                mProvider.subUiVisibilityChanged(false);
        }

        public void onItemClick(AdapterView adapterview, View view, int i, long l)
        {
            switch(((ActivityChooserViewAdapter)adapterview.getAdapter()).getItemViewType(i)) {
                default:
                    throw new IllegalArgumentException();
                case 1:
                    showPopupUnchecked(0x7fffffff);
                    return;
                case 0:
                    dismissPopup();
                    if (!mIsSelectingDefaultActivity) {

                        if (!mAdapter.getShowDefaultActivity())
                            i++;
                        Intent intent = mAdapter.getDataModel().chooseActivity(i);
                        if (intent != null) {
                            intent.addFlags(0x80000);
                            getContext().startActivity(intent);
                            return;
                        }
                        return;

                    }
                    if (i > 0) {
                        mAdapter.getDataModel().setDefaultActivity(i);
                        return;
                    }
                    return;
            }
        }

        public boolean onLongClick(View view)
        {
            if(view == mDefaultActivityButton)
            {
                if(mAdapter.getCount() > 0)
                {
                    mIsSelectingDefaultActivity = true;
                    showPopupUnchecked(mInitialActivityCount);
                }
                return true;
            } else
            {
                throw new IllegalArgumentException();
            }
        }


        private Callbacks()
        {
            super();
        }

    }

    public static class InnerLayout extends LinearLayoutCompat
    {

        private static final int TINT_ATTRS[] = {
            0x10100d4
        };


        public InnerLayout(Context context, AttributeSet attributeset)
        {
            super(context, attributeset);
            TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(context, attributeset, TINT_ATTRS);
            setBackgroundDrawable(tintTypedArray.getDrawable(0));
            tintTypedArray.recycle();
        }
    }


    public ActivityChooserView(Context context)
    {
        this(context, null);
    }

    public ActivityChooserView(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public ActivityChooserView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mModelDataSetOberver = new DataSetObserver() {

            public void onChanged()
            {
                super.onChanged();
                mAdapter.notifyDataSetChanged();
            }

            public void onInvalidated()
            {
                super.onInvalidated();
                mAdapter.notifyDataSetInvalidated();
            }

        };

        mOnGlobalLayoutListener = new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout()
            {
                if(isShowingPopup())
                    if(!isShown())
                    {
                        getListPopupWindow().dismiss();
                    } else
                    {
                        getListPopupWindow().show();
                        if(mProvider != null)
                        {
                            mProvider.subUiVisibilityChanged(true);
                            return;
                        }
                    }
            }

        };

        mInitialActivityCount = 4;
        Object obj = context.obtainStyledAttributes(attributeset, android.support.v7.appcompat.R.styleable.ActivityChooserView, i, 0);
        mInitialActivityCount = ((TypedArray) (obj)).getInt(android.support.v7.appcompat.R.styleable.ActivityChooserView_initialActivityCount, 4);
        Drawable drawable = ((TypedArray) (obj)).getDrawable(android.support.v7.appcompat.R.styleable.ActivityChooserView_expandActivityOverflowButtonDrawable);
        ((TypedArray) (obj)).recycle();
        LayoutInflater.from(getContext()).inflate(android.support.v7.appcompat.R.layout.abc_activity_chooser_view, this, true);
        mCallbacks = new Callbacks();
        mActivityChooserContent = (LinearLayoutCompat)findViewById(android.support.v7.appcompat.R.id.activity_chooser_view_content);
        mActivityChooserContentBackground = mActivityChooserContent.getBackground();
        mDefaultActivityButton = (FrameLayout)findViewById(android.support.v7.appcompat.R.id.default_activity_button);
        mDefaultActivityButton.setOnClickListener(mCallbacks);
        mDefaultActivityButton.setOnLongClickListener(mCallbacks);
        mDefaultActivityButtonImage = (ImageView)mDefaultActivityButton.findViewById(android.support.v7.appcompat.R.id.image);
        obj = (FrameLayout)findViewById(android.support.v7.appcompat.R.id.expand_activities_button);
        ((FrameLayout) (obj)).setOnClickListener(mCallbacks);
        ((FrameLayout) (obj)).setOnTouchListener(new android.support.v7.widget.ListPopupWindow.ForwardingListener(((View) (obj))) {

            public ListPopupWindow getPopup()
            {
                return getListPopupWindow();
            }

            protected boolean onForwardingStarted()
            {
                showPopup();
                return true;
            }

            protected boolean onForwardingStopped()
            {
                dismissPopup();
                return true;
            }

        });
        mExpandActivityOverflowButton = ((FrameLayout) (obj));
        mExpandActivityOverflowButtonImage = (ImageView)((FrameLayout) (obj)).findViewById(android.support.v7.appcompat.R.id.image);
        mExpandActivityOverflowButtonImage.setImageDrawable(drawable);
        mAdapter = new ActivityChooserViewAdapter();
        mAdapter.registerDataSetObserver(new DataSetObserver() {

            public void onChanged()
            {
                super.onChanged();
                updateAppearance();
            }

        });
        Resources resources = context.getResources();
        //mListPopupMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2, context.getDimensionPixelSize(android.support.v7.appcompat.R.dimen.abc_config_prefDialogWidth));
        mListPopupMaxWidth = resources.getDisplayMetrics().widthPixels / 2;
    }

    private ListPopupWindow getListPopupWindow()
    {
        if(mListPopupWindow == null)
        {
            mListPopupWindow = new ListPopupWindow(getContext());
            mListPopupWindow.setAdapter(mAdapter);
            mListPopupWindow.setAnchorView(this);
            mListPopupWindow.setModal(true);
            mListPopupWindow.setOnItemClickListener(mCallbacks);
            mListPopupWindow.setOnDismissListener(mCallbacks);
        }
        return mListPopupWindow;
    }

    private void showPopupUnchecked(int i)
    {
        if(mAdapter.getDataModel() == null)
            throw new IllegalStateException("No data model. Did you call #setDataModel?");
        getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        ListPopupWindow listpopupwindow;
        int j;
        int k;
        boolean flag;
        if(mDefaultActivityButton.getVisibility() == 0)
            flag = true;
        else
            flag = false;
        k = mAdapter.getActivityCount();
        if(flag)
            j = 1;
        else
            j = 0;
        if(i != 0x7fffffff && k > i + j)
        {
            mAdapter.setShowFooterView(true);
            mAdapter.setMaxActivityCount(i - 1);
        } else
        {
            mAdapter.setShowFooterView(false);
            mAdapter.setMaxActivityCount(i);
        }
        listpopupwindow = getListPopupWindow();
        if(!listpopupwindow.isShowing())
        {
            if(mIsSelectingDefaultActivity || !flag)
                mAdapter.setShowDefaultActivity(true, flag);
            else
                mAdapter.setShowDefaultActivity(false, false);
            listpopupwindow.setContentWidth(Math.min(mAdapter.measureContentWidth(), mListPopupMaxWidth));
            listpopupwindow.show();
            if(mProvider != null)
                mProvider.subUiVisibilityChanged(true);
            listpopupwindow.getListView().setContentDescription(getContext().getString(android.support.v7.appcompat.R.string.abc_activitychooserview_choose_application));
        }
    }

    private void updateAppearance()
    {
        int i;
        int j;
        if(mAdapter.getCount() > 0)
            mExpandActivityOverflowButton.setEnabled(true);
        else
            mExpandActivityOverflowButton.setEnabled(false);
        i = mAdapter.getActivityCount();
        j = mAdapter.getHistorySize();
        if(i == 1 || i > 1 && j > 0)
        {
            mDefaultActivityButton.setVisibility(0);
            Object obj = mAdapter.getDefaultActivity();
            android.content.pm.PackageManager packagemanager = getContext().getPackageManager();
            mDefaultActivityButtonImage.setImageDrawable(((ResolveInfo) (obj)).loadIcon(packagemanager));
            if(mDefaultActionButtonContentDescription != 0)
            {
                obj = ((ResolveInfo) (obj)).loadLabel(packagemanager);
                obj = getContext().getString(mDefaultActionButtonContentDescription, new Object[] {
                    obj
                });
                mDefaultActivityButton.setContentDescription(((CharSequence) (obj)));
            }
        } else
        {
            mDefaultActivityButton.setVisibility(8);
        }
        if(mDefaultActivityButton.getVisibility() == 0)
        {
            mActivityChooserContent.setBackgroundDrawable(mActivityChooserContentBackground);
            return;
        } else
        {
            mActivityChooserContent.setBackgroundDrawable(null);
            return;
        }
    }

    public boolean dismissPopup()
    {
        if(isShowingPopup())
        {
            getListPopupWindow().dismiss();
            ViewTreeObserver viewtreeobserver = getViewTreeObserver();
            if(viewtreeobserver.isAlive())
                viewtreeobserver.removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
        }
        return true;
    }

    public ActivityChooserModel getDataModel()
    {
        return mAdapter.getDataModel();
    }

    public boolean isShowingPopup()
    {
        return getListPopupWindow().isShowing();
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        ActivityChooserModel activitychoosermodel = mAdapter.getDataModel();
        if(activitychoosermodel != null)
            activitychoosermodel.registerObserver(mModelDataSetOberver);
        mIsAttachedToWindow = true;
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        Object obj = mAdapter.getDataModel();
        if(obj != null)
            ((ActivityChooserModel) (obj)).unregisterObserver(mModelDataSetOberver);
        obj = getViewTreeObserver();
        if(((ViewTreeObserver) (obj)).isAlive())
            ((ViewTreeObserver) (obj)).removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
        if(isShowingPopup())
            dismissPopup();
        mIsAttachedToWindow = false;
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l)
    {
        mActivityChooserContent.layout(0, 0, k - i, l - j);
        if(!isShowingPopup())
            dismissPopup();
    }

    protected void onMeasure(int i, int j)
    {
        LinearLayoutCompat linearlayoutcompat = mActivityChooserContent;
        int k = j;
        if(mDefaultActivityButton.getVisibility() != 0)
            k = android.view.View.MeasureSpec.makeMeasureSpec(android.view.View.MeasureSpec.getSize(j), 0x40000000);
        measureChild(linearlayoutcompat, i, k);
        setMeasuredDimension(linearlayoutcompat.getMeasuredWidth(), linearlayoutcompat.getMeasuredHeight());
    }

    public void setActivityChooserModel(ActivityChooserModel activitychoosermodel)
    {
        mAdapter.setDataModel(activitychoosermodel);
        if(isShowingPopup())
        {
            dismissPopup();
            showPopup();
        }
    }

    public void setDefaultActionButtonContentDescription(int i)
    {
        mDefaultActionButtonContentDescription = i;
    }

    public void setExpandActivityOverflowButtonContentDescription(int i)
    {
        String s = getContext().getString(i);
        mExpandActivityOverflowButtonImage.setContentDescription(s);
    }

    public void setExpandActivityOverflowButtonDrawable(Drawable drawable)
    {
        mExpandActivityOverflowButtonImage.setImageDrawable(drawable);
    }

    public void setInitialActivityCount(int i)
    {
        mInitialActivityCount = i;
    }

    public void setOnDismissListener(android.widget.PopupWindow.OnDismissListener ondismisslistener)
    {
        mOnDismissListener = ondismisslistener;
    }

    public void setProvider(ActionProvider actionprovider)
    {
        mProvider = actionprovider;
    }

    public boolean showPopup()
    {
        if(isShowingPopup() || !mIsAttachedToWindow)
        {
            return false;
        } else
        {
            mIsSelectingDefaultActivity = false;
            showPopupUnchecked(mInitialActivityCount);
            return true;
        }
    }

    private static final String LOG_TAG = "ActivityChooserView";
    private final LinearLayoutCompat mActivityChooserContent;
    private final Drawable mActivityChooserContentBackground;
    private final ActivityChooserViewAdapter mAdapter;
    private final Callbacks mCallbacks;
    private int mDefaultActionButtonContentDescription;
    private final FrameLayout mDefaultActivityButton;
    private final ImageView mDefaultActivityButtonImage;
    private final FrameLayout mExpandActivityOverflowButton;
    private final ImageView mExpandActivityOverflowButtonImage;
    private int mInitialActivityCount;
    private boolean mIsAttachedToWindow;
    private boolean mIsSelectingDefaultActivity;
    private final int mListPopupMaxWidth;
    private ListPopupWindow mListPopupWindow;
    private final DataSetObserver mModelDataSetOberver;
    private android.widget.PopupWindow.OnDismissListener mOnDismissListener;
    private final android.view.ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    ActionProvider mProvider;


}
