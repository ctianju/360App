package engine;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Field;

import view.FloatCircleView;
import view.FloatMenuView;

/**
 * 作者：Administrator on 2016/8/13 09:19
 */
public class FloatViewManager {
    private static FloatViewManager instance;
    private Context mContext;
    private final WindowManager mWm;
    private FloatCircleView mFloatCircleView;
    private float mStartX;
    private float mStartY;
    private WindowManager.LayoutParams mParams;
    private float x0;
    private float y0;
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartX = motionEvent.getRawX();
                    mStartY = motionEvent.getRawY();
                    x0 = motionEvent.getRawX();
                    y0 = motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = motionEvent.getRawX();
                    float y = motionEvent.getRawY();
                    float dx = x - mStartX;
                    float dy = y - mStartY;
                    mParams.x += dx;
                    mParams.y += dy;
                    mFloatCircleView.setDragState(true);
                    mWm.updateViewLayout(mFloatCircleView, mParams);
                    mStartX = x;
                    mStartY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    float x1 = motionEvent.getRawX();
                    if (x1 > getScreenWidth() / 2) {
                        mParams.x = getScreenWidth() - mFloatCircleView.width;
                    } else {
                        mParams.x = 0;
                    }
                    mFloatCircleView.setDragState(false);
                    mWm.updateViewLayout(mFloatCircleView, mParams);
                    if (Math.abs(x1 - x0) > 6) {
                        return true;
                    } else {
                        return false;
                    }

            }
            return false;
        }
    };
    private final FloatMenuView mMFloatMenuView;

    public int getScreenWidth() {
        return mWm.getDefaultDisplay().getWidth();
    }


    private FloatViewManager(final Context context) {
        mContext = context;
        mWm = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        mFloatCircleView = new FloatCircleView(context);
        mFloatCircleView.setOnTouchListener(mOnTouchListener);
        mMFloatMenuView = new FloatMenuView(context);
        mFloatCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐藏circleView，显示菜单栏，开启动画，
                mWm.removeView(mFloatCircleView);
                showFloatMenuView();
                mMFloatMenuView.startAnimation();


            }
        });
    }

    private void showFloatMenuView() {
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.width = getScreenWidth();
        mParams.height = getScreenHeight()-getStautsHeight();
        mParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        mParams.x = 0;
        mParams.y = 0;
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mParams.format = PixelFormat.RGBA_8888;
        mWm.addView(mMFloatMenuView, mParams);
    }

    private int getScreenHeight() {
        return mWm.getDefaultDisplay().getHeight();
    }

    private int getStautsHeight() {//获取状态栏的高
        Class<?> c = null;
        try {
            c = Class.forName("con.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            return mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static FloatViewManager getInstance(Context context) {
        if (instance == null) {
            instance = new FloatViewManager(context);
        }
        return instance;
    }

    public void showFloatCircleView() {
        if(mParams == null) {
            mParams = new WindowManager.LayoutParams();

        mParams.width = mFloatCircleView.width;
        mParams.height = mFloatCircleView.height;
        mParams.gravity = Gravity.TOP | Gravity.LEFT;
        mParams.x = 0;
        mParams.y = 0;
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mParams.format = PixelFormat.RGBA_8888;
        }
        mWm.addView(mFloatCircleView, mParams);
    }

    public void hideFloatMenuView() {
        mWm.removeView(mMFloatMenuView);

    }
}
