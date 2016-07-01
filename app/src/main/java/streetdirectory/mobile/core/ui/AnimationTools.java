// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core.ui;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AnimationTools
{

    public AnimationTools()
    {
    }

    public static void fadeIn(final View view, int i)
    {
        AlphaAnimation alphaanimation = new AlphaAnimation(0.0F, 1.0F);
        alphaanimation.setDuration(i);
        alphaanimation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {

            public void onAnimationEnd(Animation animation)
            {
            }

            public void onAnimationRepeat(Animation animation)
            {
            }

            public void onAnimationStart(Animation animation)
            {
                view.setVisibility(View.VISIBLE);
            }

        }
);
        view.startAnimation(alphaanimation);
    }

    public static void fadeOut(final View view, int i)
    {
        AlphaAnimation alphaanimation = new AlphaAnimation(1.0F, 0.0F);
        alphaanimation.setDuration(i);
        alphaanimation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {

            public void onAnimationEnd(Animation animation)
            {
                view.setVisibility(View.INVISIBLE);
            }

            public void onAnimationRepeat(Animation animation)
            {
            }

            public void onAnimationStart(Animation animation)
            {
            }
        });
        view.startAnimation(alphaanimation);
    }
}
