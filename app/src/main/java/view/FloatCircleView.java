package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import btw.app360.R;

/**
 * 作者：Administrator on 2016/8/13 09:30
 */
public class FloatCircleView extends View {
    public int width = 150;
    public int height = 150;
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private String text = "50%";
    private Bitmap mSrc;

    public FloatCircleView(Context context) {
        super(context);
        initPAints();
    }
    public FloatCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initPAints() {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.GRAY);
        mCirclePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(25);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);//文字加粗

        mSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.ic);
        mSrc = Bitmap.createScaledBitmap(mSrc,width,height,true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(drag){
            canvas.drawBitmap(mSrc,0,0,null);
        }else {
            canvas.drawCircle(width / 2, height / 2, width / 2, mCirclePaint);
            float textWidth = mTextPaint.measureText(text);
            float x = width / 2 - textWidth / 2;
            Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
            float dy = -(metrics.descent + metrics.ascent);
            float y = height / 2 + dy;
            canvas.drawText(text, x, y, mTextPaint);
        }
    }
    boolean drag = false;
    public void setDragState(boolean b) {
        drag = b ;
        invalidate();
    }
}


