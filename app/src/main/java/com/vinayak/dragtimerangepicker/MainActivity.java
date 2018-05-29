package com.vinayak.dragtimerangepicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    DrawingView dv ;
    private Paint mPaint;
    RecyclerView listView;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawingView(this);
        setContentView(R.layout.activity_main);
        RelativeLayout view=findViewById(R.id.contentView);
        setRecyclerView();
       // RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(100,1000);
       // params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        //dv.setLayoutParams(params);
        view.addView(dv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setShader(new LinearGradient(0, 0, 0, getHeight(), Color.parseColor("#009999"), Color.parseColor("#009999"), Shader.TileMode.MIRROR));
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(90);
        mPaint.setTextSize(10);

    }

    private void setRecyclerView(){
        listView=findViewById(R.id.listView);
        layoutManager=new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(new RecyclerAdapter());
    }

    private float getHeight(){
        return this.getResources().getDisplayMetrics().heightPixels;
    }
    public class DrawingView extends View {

        public int width;
        public  int height;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;
        private float mX, mY;
        private Paint mTextPaint;
        private float mYmax,mYmin,mYStart;
        private static final float TOUCH_TOLERANCE = 10;
        public DrawingView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);


            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(Color.BLACK);
            mTextPaint.setStyle(Paint.Style.STROKE);
            mTextPaint.setStrokeJoin(Paint.Join.MITER);

            mTextPaint.setTextSize(20);
            int mWidth= this.getResources().getDisplayMetrics().widthPixels;
            mX=mWidth/2;
           // mX=50;
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mPath,  mPaint);
            canvas.drawPath( circlePath,  circlePaint);
            canvas.drawPaint(mTextPaint);

        }



        private void touch_start(float x, float y) {
            mYmax=mYmin=mYStart=y;
            mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            mPath.reset();
            mPath.moveTo(mX, y);
            //  mX = x;
            mY = y;
        }

        private void touch_move(float x,  float y) {

            float dy = y - mY;
            if (Math.abs(dy )>= TOUCH_TOLERANCE) {
                if (mYmax==0&&mYmin==0){
                    mYmax=y;
                    mYmin=y;
                    mPath.quadTo(mX, mY, mX, (y + mY)/2);
                }else if (dy < 0) {
                    if (mYmax > y){
                        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                        mPath.reset();
                        mPath.moveTo(mX, mYmin);
                        mPath.quadTo(mX,mYmin,mX,(y + mY)/2);
                        if (mYmax>mYStart) mYmax=y;
                        else mYmax=mYStart;

                    }else {
                        mYmin=y;
                        mPath.quadTo(mX, mY, mX, (y + mY)/2);
                    }
                } else if (dy > 0) {
                    if (mYmin > y) {
                        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                        mPath.reset();
                        mPath.moveTo(mX, mYmax);
                        mPath.quadTo(mX, mYmax, mX, (y + mY) / 2);
                        if (mYmin<mYStart)mYmin=y;
                        else mYmin=mYStart;
                    } else {
                        mPath.quadTo(mX, mY, mX, (y + mY) / 2);
                        mYmax = y;
                    }

                }
                mY = y;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.scrollBy(0,(int) mY);
                    }
                },200);

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw

            mPath.reset();
            mCanvas.drawText("10 - 15",mX-30,mYmax,mTextPaint);
            mCanvas.drawText("10 - 15",mX-30,mYmin,mTextPaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }


}
