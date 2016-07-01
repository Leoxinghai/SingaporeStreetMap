// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.widget;

import android.app.Activity;
import android.content.*;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import com.facebook.internal.*;

public class LikeView extends FrameLayout
{
    public static enum AuxiliaryViewPosition
    {
		BOTTOM ("BOTTOM", 0, "bottom", 0),
		INLINE("INLINE", 1, "inline", 1),
		TOP("TOP", 2, "top", 2);
        static AuxiliaryViewPosition DEFAULT = AuxiliaryViewPosition.INLINE;
        static AuxiliaryViewPosition fromInt(int i)
        {
            AuxiliaryViewPosition aauxiliaryviewposition[] = values();
            int k = aauxiliaryviewposition.length;
            for(int j = 0; j < k; j++)
            {
                AuxiliaryViewPosition auxiliaryviewposition = aauxiliaryviewposition[j];
                if(auxiliaryviewposition.getValue() == i)
                    return auxiliaryviewposition;
            }

            return null;
        }

        private int getValue()
        {
            return intValue;
        }


        public String toString()
        {
            return stringValue;
        }
        String sType;
        int iType;
        String stringValue;
        int intValue;

        private AuxiliaryViewPosition(String s, int i, String s1, int j)
        {
            sType = s;
            iType = i;
            stringValue = s1;
            intValue = j;
        }
    }

    public static enum HorizontalAlignment
    {

		CENTER("CENTER", 0, "center", 0),
		LEFT("LEFT", 1, "left", 1),
		RIGHT("RIGHT", 2, "right", 2);
        static HorizontalAlignment DEFAULT = HorizontalAlignment.CENTER;
        static HorizontalAlignment fromInt(int i)
        {
            HorizontalAlignment ahorizontalalignment[] = values();
            int k = ahorizontalalignment.length;
            for(int j = 0; j < k; j++)
            {
                HorizontalAlignment horizontalalignment = ahorizontalalignment[j];
                if(horizontalalignment.getValue() == i)
                    return horizontalalignment;
            }

            return null;
        }

        private int getValue()
        {
            return intValue;
        }

        public String toString()
        {
            return stringValue;
        }
        String sType;
        int iType;
        String stringValue;
        int intValue;

        private HorizontalAlignment(String s, int i, String s1, int j)
        {
            sType = s;
            iType = i;
            stringValue = s1;
            intValue = j;
        }
    }

    private class LikeActionControllerCreationCallback
        implements com.facebook.internal.LikeActionController.CreationCallback
    {

        public void cancel()
        {
            isCancelled = true;
        }

        public void onComplete(LikeActionController likeactioncontroller)
        {
            if(isCancelled)
            {
                return;
            } else
            {
                associateWithLikeActionController(likeactioncontroller);
                updateLikeStateAndLayout();
                creationCallback = null;
                return;
            }
        }

        private boolean isCancelled;

        private LikeActionControllerCreationCallback()
        {
            super();
        }

    }

    private class LikeControllerBroadcastReceiver extends BroadcastReceiver
    {

        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            boolean flag = true;
            if(bundle != null)
            {
                String s = bundle.getString("com.facebook.sdk.LikeActionController.OBJECT_ID");
                if(Utility.isNullOrEmpty(s) || Utility.areObjectsEqual(objectId, s))
                    flag = true;
                else
                    flag = false;
            }
            if(!flag)
	            return;

            if("com.facebook.sdk.LikeActionController.UPDATED".equals(action))
            {
                updateLikeStateAndLayout();
                return;
            }
            if("com.facebook.sdk.LikeActionController.DID_ERROR".equals(action)) {
				if(onErrorListener == null)
					return;

				onErrorListener.onError(bundle);
				return;
			}

            if("com.facebook.sdk.LikeActionController.DID_RESET".equals(action)) {
				setObjectIdForced(objectId);
				updateLikeStateAndLayout();
				return;
			}
        }


        private LikeControllerBroadcastReceiver()
        {
            super();
        }

    }

    public static interface OnErrorListener
    {

        public abstract void onError(Bundle bundle);
    }

    public static enum Style
    {

        DEFAULT("DEFAULT",0,"default",0),
		STANDARD("STANDARD", 0, "standard", 0),
		BUTTON("BUTTON", 1, "button", 1),
		BOX_COUNT("BOX_COUNT", 2, "box_count", 2);

        static Style fromInt(int i)
        {
            Style astyle[] = values();
            int k = astyle.length;
            for(int j = 0; j < k; j++)
            {
                Style style = astyle[j];
                if(style.getValue() == i)
                    return style;
            }

            return null;
        }

        private int getValue()
        {
            return intValue;
        }

        public String toString()
        {
            return stringValue;
        }


        String sType;
        int iType;
        String stringValue;
        int intValue;

        private Style(String s, int i, String s1, int j)
        {
		sType = s;
		iType = i;
            stringValue = s1;
            intValue = j;
        }
    }


    public LikeView(Context context)
    {
        super(context);
        likeViewStyle = Style.DEFAULT;
        horizontalAlignment = HorizontalAlignment.DEFAULT;
        auxiliaryViewPosition = AuxiliaryViewPosition.DEFAULT;
        foregroundColor = -1;
        initialize(context);
    }

    public LikeView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        likeViewStyle = Style.DEFAULT;
        horizontalAlignment = HorizontalAlignment.DEFAULT;
        auxiliaryViewPosition = AuxiliaryViewPosition.DEFAULT;
        foregroundColor = -1;
        parseAttributes(attributeset);
        initialize(context);
    }

    private void associateWithLikeActionController(LikeActionController likeactioncontroller)
    {
        likeActionController = likeactioncontroller;
        broadcastReceiver = new LikeControllerBroadcastReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("com.facebook.sdk.LikeActionController.UPDATED");
        intentfilter.addAction("com.facebook.sdk.LikeActionController.DID_ERROR");
        intentfilter.addAction("com.facebook.sdk.LikeActionController.DID_RESET");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentfilter);
    }

    private Bundle getAnalyticsParameters()
    {
        Bundle bundle = new Bundle();
        bundle.putString("style", likeViewStyle.toString());
        bundle.putString("auxiliary_position", auxiliaryViewPosition.toString());
        bundle.putString("horizontal_alignment", horizontalAlignment.toString());
        bundle.putString("object_id", Utility.coerceValueIfNullOrEmpty(objectId, ""));
        return bundle;
    }

    public static boolean handleOnActivityResult(Context context, int i, int j, Intent intent)
    {
        return LikeActionController.handleOnActivityResult(context, i, j, intent);
    }

    private void initialize(Context context)
    {
        edgePadding = getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_likeview_edge_padding);
        internalPadding = getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_likeview_internal_padding);
        if(foregroundColor == -1)
            foregroundColor = getResources().getColor(com.facebook.android.R.color.com_facebook_likeview_text_color);
        setBackgroundColor(0);
        containerView = new LinearLayout(context);
        android.widget.FrameLayout.LayoutParams layoutparams = new android.widget.FrameLayout.LayoutParams(-2, -2);
        containerView.setLayoutParams(layoutparams);
        initializeLikeButton(context);
        initializeSocialSentenceView(context);
        initializeLikeCountView(context);
        containerView.addView(likeButton);
        containerView.addView(socialSentenceView);
        containerView.addView(likeBoxCountView);
        addView(containerView);
        setObjectIdForced(objectId);
        updateLikeStateAndLayout();
    }

    private void initializeLikeButton(Context context)
    {
        boolean flag;
        if(likeActionController != null)
            flag = likeActionController.isObjectLiked();
        else
            flag = false;
        likeButton = new LikeButton(context, flag);
        likeButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                toggleLike();
            }

        }
);
        LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-2, -2);
        likeButton.setLayoutParams(layoutParams);
    }

    private void initializeLikeCountView(Context context)
    {
        likeBoxCountView = new LikeBoxCountView(context);
        LinearLayout.LayoutParams layoutParams  = new android.widget.LinearLayout.LayoutParams(-1, -1);
        likeBoxCountView.setLayoutParams(layoutParams);
    }

    private void initializeSocialSentenceView(Context context)
    {
        socialSentenceView = new TextView(context);
        socialSentenceView.setTextSize(0, getResources().getDimension(com.facebook.android.R.dimen.com_facebook_likeview_text_size));
        socialSentenceView.setMaxLines(2);
        socialSentenceView.setTextColor(foregroundColor);
        socialSentenceView.setGravity(17);
        LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-2, -1);
        socialSentenceView.setLayoutParams(layoutParams);
    }

    private void parseAttributes(AttributeSet attributeset)
    {
        TypedArray typedArray;
        if(attributeset != null && getContext() != null)
            if((typedArray = getContext().obtainStyledAttributes(attributeset, com.facebook.android.R.styleable.com_facebook_like_view)) != null)
            {
                objectId = Utility.coerceValueIfNullOrEmpty(typedArray.getString(com.facebook.android.R.styleable.com_facebook_like_view_object_id), null);
                likeViewStyle = Style.fromInt(typedArray.getInt(com.facebook.android.R.styleable.com_facebook_like_view_style, Style.DEFAULT.getValue()));
                if(likeViewStyle == null)
                    throw new IllegalArgumentException("Unsupported value for LikeView 'style'");
                auxiliaryViewPosition = AuxiliaryViewPosition.fromInt(typedArray.getInt(com.facebook.android.R.styleable.com_facebook_like_view_auxiliary_view_position, AuxiliaryViewPosition.DEFAULT.getValue()));
                if(auxiliaryViewPosition == null)
                    throw new IllegalArgumentException("Unsupported value for LikeView 'auxiliary_view_position'");
                horizontalAlignment = HorizontalAlignment.fromInt(typedArray.getInt(com.facebook.android.R.styleable.com_facebook_like_view_horizontal_alignment, HorizontalAlignment.DEFAULT.getValue()));
                if(horizontalAlignment == null)
                {
                    throw new IllegalArgumentException("Unsupported value for LikeView 'horizontal_alignment'");
                } else
                {
                    foregroundColor = typedArray.getColor(com.facebook.android.R.styleable.com_facebook_like_view_foreground_color, -1);
                    typedArray.recycle();
                    return;
                }
            }
    }

    private void setObjectIdForced(String s)
    {
        tearDownObjectAssociations();
        objectId = s;
        if(Utility.isNullOrEmpty(s))
        {
            return;
        } else
        {
            creationCallback = new LikeActionControllerCreationCallback();
            LikeActionController.getControllerForObjectId(getContext(), s, creationCallback);
            return;
        }
    }

    private void tearDownObjectAssociations()
    {
        if(broadcastReceiver != null)
        {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
        if(creationCallback != null)
        {
            creationCallback.cancel();
            creationCallback = null;
        }
        likeActionController = null;
    }

    private void toggleLike()
    {
        if(likeActionController == null)
			return;

        Object obj1 = null;
        Object obj = null;
        if(parentFragment != null) {
			obj = parentFragment.getActivity();
		} else {
			Context context = getContext();
			if(!(context instanceof Activity)) {
				obj = obj1;
				if(context instanceof ContextWrapper)
				{
					context = ((ContextWrapper)context).getBaseContext();
					obj = obj1;
					if(context instanceof Activity)
						obj = (Activity)context;
				}
			} else
				 obj = (Activity)context;
		}
        likeActionController.toggleLike(((Activity) (obj)), parentFragment, getAnalyticsParameters());
        return;
    }

    private void updateBoxCountCaretPosition()
    {

        LikeBoxCountView likeboxcountview;
        switch(auxiliaryViewPosition.ordinal())
        {
        default:
            return;

        case 1: // '\001'
            likeBoxCountView.setCaretPosition(com.facebook.internal.LikeBoxCountView.LikeBoxCountViewCaretPosition.BOTTOM);
            return;

        case 2: // '\002'
            likeBoxCountView.setCaretPosition(com.facebook.internal.LikeBoxCountView.LikeBoxCountViewCaretPosition.TOP);
            return;

        case 3: // '\003'
            likeboxcountview = likeBoxCountView;
            break;
        }
        com.facebook.internal.LikeBoxCountView.LikeBoxCountViewCaretPosition likeboxcountviewcaretposition;
        if(horizontalAlignment == HorizontalAlignment.RIGHT)
            likeboxcountviewcaretposition = com.facebook.internal.LikeBoxCountView.LikeBoxCountViewCaretPosition.RIGHT;
        else
            likeboxcountviewcaretposition = com.facebook.internal.LikeBoxCountView.LikeBoxCountViewCaretPosition.LEFT;
        likeboxcountview.setCaretPosition(likeboxcountviewcaretposition);
    }

    private void updateLayout()
    {
        FrameLayout.LayoutParams layoutParams;
        boolean flag = true;
        layoutParams = (android.widget.FrameLayout.LayoutParams)containerView.getLayoutParams();
        LinearLayout.LayoutParams layoutParams1 = (android.widget.LinearLayout.LayoutParams)likeButton.getLayoutParams();
        int i;
        if(horizontalAlignment == HorizontalAlignment.LEFT)
            i = 3;
        else
        if(horizontalAlignment == HorizontalAlignment.CENTER)
            i = 1;
        else
            i = 5;
        layoutParams.gravity = i | 0x30;
        layoutParams1.gravity = i;
        socialSentenceView.setVisibility(View.INVISIBLE);
        likeBoxCountView.setVisibility(View.INVISIBLE);

        View lView;
        if(likeViewStyle != Style.STANDARD || likeActionController == null || Utility.isNullOrEmpty(likeActionController.getSocialSentence())) {
				if(likeViewStyle != Style.BOX_COUNT || likeActionController == null || Utility.isNullOrEmpty(likeActionController.getLikeCountString()))
					return;
				updateBoxCountCaretPosition();
            lView = likeBoxCountView;
		} else {
            lView = socialSentenceView;
		}

        ((View) (lView)).setVisibility(View.VISIBLE);
        ((android.widget.LinearLayout.LayoutParams)((View) (lView)).getLayoutParams()).gravity = i;

        i = ((flag) ? 1 : 0);
        if(auxiliaryViewPosition == AuxiliaryViewPosition.INLINE)
            i = 0;
        ((LinearLayout) (containerView)).setOrientation(i);
        if(auxiliaryViewPosition == AuxiliaryViewPosition.TOP || auxiliaryViewPosition == AuxiliaryViewPosition.INLINE && horizontalAlignment == HorizontalAlignment.RIGHT)
        {
            containerView.removeView(likeButton);
            containerView.addView(likeButton);
        } else
        {
            containerView.removeView(((View) (lView)));
            containerView.addView(((View) (lView)));
        }
        switch(auxiliaryViewPosition.ordinal()) {
		default:
				return;
		case 1:
				((View) (lView)).setPadding(edgePadding, edgePadding, edgePadding, internalPadding);
				return;
		case 2:
				((View) (lView)).setPadding(edgePadding, internalPadding, edgePadding, edgePadding);
				return;
		case 3:
				if(horizontalAlignment == HorizontalAlignment.RIGHT)
				{
					((View) (lView)).setPadding(edgePadding, edgePadding, internalPadding, edgePadding);
					return;
				} else
				{
					((View) (lView)).setPadding(internalPadding, edgePadding, edgePadding, edgePadding);
					return;
				}
			}
    }

    private void updateLikeStateAndLayout()
    {
        if(likeActionController == null)
        {
            likeButton.setLikeState(false);
            socialSentenceView.setText(null);
            likeBoxCountView.setText(null);
        } else
        {
            likeButton.setLikeState(likeActionController.isObjectLiked());
            socialSentenceView.setText(likeActionController.getSocialSentence());
            likeBoxCountView.setText(likeActionController.getLikeCountString());
        }
        updateLayout();
    }

    public OnErrorListener getOnErrorListener()
    {
        return onErrorListener;
    }

    protected void onDetachedFromWindow()
    {
        setObjectId(null);
        super.onDetachedFromWindow();
    }

    public void setAuxiliaryViewPosition(AuxiliaryViewPosition auxiliaryviewposition)
    {
        if(auxiliaryviewposition == null)
            auxiliaryviewposition = AuxiliaryViewPosition.DEFAULT;
        if(auxiliaryViewPosition != auxiliaryviewposition)
        {
            auxiliaryViewPosition = auxiliaryviewposition;
            updateLayout();
        }
    }

    public void setForegroundColor(int i)
    {
        if(foregroundColor != i)
            socialSentenceView.setTextColor(i);
    }

    public void setFragment(Fragment fragment)
    {
        parentFragment = fragment;
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalalignment)
    {
        if(horizontalalignment == null)
            horizontalalignment = HorizontalAlignment.DEFAULT;
        if(horizontalAlignment != horizontalalignment)
        {
            horizontalAlignment = horizontalalignment;
            updateLayout();
        }
    }

    public void setLikeViewStyle(Style style)
    {
        if(style == null)
            style = Style.DEFAULT;
        if(likeViewStyle != style)
        {
            likeViewStyle = style;
            updateLayout();
        }
    }

    public void setObjectId(String s)
    {
        s = Utility.coerceValueIfNullOrEmpty(s, null);
        if(!Utility.areObjectsEqual(s, objectId))
        {
            setObjectIdForced(s);
            updateLikeStateAndLayout();
        }
    }

    public void setOnErrorListener(OnErrorListener onerrorlistener)
    {
        onErrorListener = onerrorlistener;
    }

    private static final int NO_FOREGROUND_COLOR = -1;
    private AuxiliaryViewPosition auxiliaryViewPosition;
    private BroadcastReceiver broadcastReceiver;
    private LinearLayout containerView;
    private LikeActionControllerCreationCallback creationCallback;
    private int edgePadding;
    private int foregroundColor;
    private HorizontalAlignment horizontalAlignment;
    private int internalPadding;
    private LikeActionController likeActionController;
    private LikeBoxCountView likeBoxCountView;
    private LikeButton likeButton;
    private Style likeViewStyle;
    private String objectId;
    private OnErrorListener onErrorListener;
    private Fragment parentFragment;
    private TextView socialSentenceView;



}
