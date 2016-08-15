package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import btw.app360.R;
import engine.FloatViewManager;

/**
 * 作者：Administrator on 2016/8/13 14:33
 */
public class FloatMenuView extends LinearLayout {

    private final LinearLayout mLl;
    private final TranslateAnimation mAnimation;

    public FloatMenuView(final Context context) {
        super(context);
        View root = View.inflate(getContext(), R.layout.float_menu_view, null);
        addView(root);
        mLl = (LinearLayout) root.findViewById(R.id.ll);
        mAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0);
        mAnimation.setDuration(500);
        mAnimation.setFillAfter(true);
        mLl.setAnimation(mAnimation);
        root.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                FloatViewManager manager = FloatViewManager.getInstance(context);
                manager.hideFloatMenuView();
                manager.showFloatCircleView();
                return false;
            }
        });
    }

    public void startAnimation() {
        mAnimation.start();
    }


}
