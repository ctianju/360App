package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * 作者：Administrator on 2016/8/13 10:55
 */
public class MyProgressView extends View {
    private int width = 200;
    private int heigth = 200;
    private Paint mCirclePaint;
    private Paint mProgressPaint;
    private Paint mTextPaint;
    private Canvas mBitmapCanvas;
    private Bitmap mBitmap;
    private Path mPath = new Path();

    public MyProgressView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.argb(0xff,0x3a,0x8c,0x6c));

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.argb(0x41,0x4e,0xc9,0x63));
        mProgressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(25);

        mBitmap = Bitmap.createBitmap(width,heigth, Bitmap.Config.ARGB_8888);
        mBitmapCanvas = new Canvas(mBitmap);

        final GestureDetector dector = new GestureDetector(new MyGestureDectorListener());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return dector.onTouchEvent(motionEvent);
            }
        });
        setClickable(true);

    }

    class MyGestureDectorListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            startDoubleTapAnimation();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            startSingleTapAnimation();
            currentProgress = progress;
            isSingleTap = true;
            return super.onSingleTapConfirmed(e);
        }
    }

    private int count = 50;
    private void startSingleTapAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               count--;
                if(count>=0){
                   invalidate();
                    handler.postDelayed(this,200);
                }else {
                    handler.removeCallbacks(this);
                    count = 50;
                }
            }
        },200);
    }
    private boolean isSingleTap=false;
    private int currentProgress=0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };
    private void startDoubleTapAnimation() {
        handler.postDelayed(runnable,50);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currentProgress++;
            if(currentProgress<=progress){
                invalidate();
                handler.postDelayed(runnable,50);
            }else {
                handler.removeCallbacks(runnable);
                currentProgress=0;
            }

        }
    };
    public MyProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,heigth);
    }


    private int progress = 50;
    private int maxProgress = 100;
    @Override
    protected void onDraw(Canvas canvas) {
        mBitmapCanvas.drawCircle(width/2,heigth/2,width/2,mCirclePaint);
        mPath.reset();
        float y =(1-(float) currentProgress/maxProgress)*heigth;
        mPath.moveTo(width,y);
        mPath.lineTo(width,heigth);
        mPath.lineTo(0,heigth);
        mPath.lineTo(0,y);

        if(!isSingleTap){
            float d = (1-(float) currentProgress/progress)*10;
            for (int i = 0; i <5 ; i++) {
                mPath.rQuadTo(10,-d,20,0);
                mPath.rQuadTo(10,d,20,0);
            }
        }else {
            float d1 = (float) count/50*10;
            if(count%2 ==0 ){
                for (int i = 0; i <5 ; i++) {
                    mPath.rQuadTo(20,-d1,40,0);
                    mPath.rQuadTo(20,d1,40,0);
                }
            }else{
                for (int i = 0; i <5 ; i++) {
                    mPath.rQuadTo(20,d1,40,0);
                    mPath.rQuadTo(20,-d1,40,0);
                }
            }
        }

        mPath.close();
        mBitmapCanvas.drawPath(mPath,mProgressPaint);
        String text = (int)(((float)currentProgress/maxProgress)*100)+"%";
        float textWidth = mTextPaint.measureText(text);
        Paint.FontMetrics metrics=mTextPaint.getFontMetrics();
        float baseLine = heigth/2-(metrics.ascent+metrics.descent)/2;
        mBitmapCanvas.drawText(text,width/2-textWidth/2,baseLine,mTextPaint);
        canvas.drawBitmap(mBitmap,0,0,null);
    }
}
