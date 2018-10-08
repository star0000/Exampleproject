package com.moyu.lianyibuttondemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 墨羽 on 2018/9/12.
 */

public class WaveButton extends View {

    /**
     * 涟漪默认颜色
     */
    private final int DEFAULT_WAVE_COLOR = Color.WHITE;

    /**
     * 进度条默认颜色
     */
    private final int DEFAULT_PROGRESS_COLOR = Color.parseColor("#0277bd");

    /**
     * 涟漪默认数量
     */
    private final int DEFAULT_WAVE_COUNT = 5;

    /**
     * 涟漪默认创建速度
     */
    private final int DEFAULT_SPEED = 600;

    /**
     * 涟漪默认持续时间
     */
    private final int DEFAULT_DURATION_TIME = 3000;

    /**
     * 进度条初始数值
     */
    private float progress = 0;

    /**
     * 涟漪颜色
     */
    private int waveColor = DEFAULT_WAVE_COLOR;

    /**
     * 进度条颜色
     */
    private int progressColor = DEFAULT_PROGRESS_COLOR;

    /**
     * 渐变颜色数组
     */
    private int[] arcColors;

    /**
     * 梯度渐变扫描渲染器
     */
    private SweepGradient sweepGradient;

    /**
     * 控件宽高
     */
    private int widgetWidth;
    private int widgetHeight;

    /**
     * bitmap宽高
     */
    private int bitmapWidth;
    private int bitmapHeight;

    /**
     * 控件宽高1/2
     */
    private int widgetWidthHalf;
    private int widgetHeightHalf;

    /**
     * 进度条半径
     */
    private float progressRadius;

    /**
     * 涟漪最小、最大 半径
     */
    private float mMinRadius;
    private float mMaxRadius;

    /**
     * 涟漪中心的Drawable资源
     */
    private Drawable waveSrc;

    /**
     * bitmap
     */
    private Bitmap mBitmap;

    /**
     * 涟漪画笔
     */
    private Paint wavePaint;

    /**
     * bitmap画笔
     */
    private Paint bitmapPaint;

    /**
     * 进度条画笔
     */
    private Paint progressPaint;

    /**
     * 判断动画是否结束
     */
    private boolean isEnd = false;

    /**
     * 判断动画是否在运行
     */
    private boolean isRunning = false;

    /**
     * 涟漪最后创建时间
     */
    private long mLastCreateTime;

    /**
     * 涟漪集合
     */
    private List<Wave> mWaveList = new ArrayList<>();

    /**
     * 线性插值器（匀速）
     */
    private Interpolator mInterpolator = new LinearInterpolator();

    /**
     * 进度条属性动画
     */
  //  private ValueAnimator progressAnimator;

    /**
     * 矩形
     */
    private RectF rectF = new RectF();


    public WaveButton(Context context) {
        super(context);
        init();

    }

    public WaveButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 取出布局中自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveButton);
        waveSrc = typedArray.getDrawable(R.styleable.WaveButton_waveSrc);
        // xml中没有指定默认图片
        if (waveSrc == null) {
            waveSrc = getResources().getDrawable(R.mipmap.ic_launcher_round);
        }
        // 如果没有指定取默认值
        waveColor = typedArray.getColor(R.styleable.WaveButton_waveColor, DEFAULT_WAVE_COLOR);
        progressColor = typedArray.getColor(R.styleable.WaveButton_progressColor, DEFAULT_PROGRESS_COLOR);
        arcColors = new int[]{Color.WHITE, progressColor, Color.WHITE};
        // 资源回收
        typedArray.recycle();
        init();

    }

    public WaveButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }


    /**
     * 初始化
     */
    private void init() {
        // 涟漪画笔
        wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint.setColor(waveColor);
        // bitmap画笔
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 进度条画笔
//        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        progressPaint.setDither(true);
//        progressPaint.setStyle(Paint.Style.STROKE);
//        progressPaint.setStrokeCap(Paint.Cap.ROUND);
//        progressPaint.setColor(progressColor);
//        progressPaint.setStrokeWidth(12);

        // 获取屏幕大小
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;

        // 创建bitmap
        BitmapDrawable drawable = (BitmapDrawable) waveSrc;
        // 指定bitmap宽高
        mBitmap = Bitmap.createScaledBitmap(drawable.getBitmap(),
                screenHeight / 8, screenHeight / 8, true);
        // 获取bitmap宽高
        bitmapWidth = mBitmap.getWidth();
        bitmapHeight = mBitmap.getHeight();
        // 进度条半径
      //  progressRadius = (bitmapWidth + 40) / 2;

        // 水波纹最小半径icon * 0.8
        // 水波纹最大半径icon * 2
        mMinRadius = (float) (bitmapWidth * 0.8 / 2);
        mMaxRadius = bitmapWidth;

        // 硬件加速
        setLayerType(LAYER_TYPE_HARDWARE, null);
        // 进度条的属性动画
//        progressAnimator = ObjectAnimator.ofFloat(this, "progress", 0, 360);
//        progressAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                stop();
//                isEnd = true;
//                setLayerType(LAYER_TYPE_NONE, null);
//            }
//        });
//        progressAnimator.setDuration(5000);
//        progressAnimator.start();
        start();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 控件宽高
        widgetWidth = getWidth();
        widgetHeight = getHeight();
        // 控件宽高的 1/2
        widgetWidthHalf = widgetWidth / 2;
        widgetHeightHalf = widgetHeight / 2;

        // cx：中心点x坐标； cy：中心点y坐标；
        // colors：颜色数组至少要两种颜色不可为null；
        // positions：可以是null。 颜色数组中每个对应颜色的相对位置，
        // 从0开始到1.0结束。如果这些值不是单调的，则绘图可能会产生意想不到的结果。
        // 如果位置为NULL，则颜色自动均匀分布。
        sweepGradient = new SweepGradient(widgetWidth / 2,
                widgetHeight / 2, arcColors, null);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 开启硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, wavePaint);
        setLayerType(View.LAYER_TYPE_SOFTWARE, bitmapPaint);
        setLayerType(View.LAYER_TYPE_SOFTWARE, progressPaint);

        // 涟漪的迭代器
        Iterator<Wave> iterator = mWaveList.iterator();
        while (iterator.hasNext()) {
            Wave wave = iterator.next();
            if (System.currentTimeMillis() - wave.mCreateTime < DEFAULT_DURATION_TIME) {
                wavePaint.setAlpha(wave.getAlpha());
                canvas.drawCircle(widgetWidthHalf, widgetHeightHalf, wave.getCurrentRadius(), wavePaint);
            } else {
                iterator.remove();
            }
        }

        canvas.drawBitmap(mBitmap, (widgetWidth - bitmapWidth) / 2,
                (widgetHeight - bitmapHeight) / 2, bitmapPaint);

        // 画布旋转， 默认0是时钟3点钟位置
        canvas.rotate(-90, widgetWidthHalf, widgetHeightHalf);

        // 矩形设置l t r b 确认大小和位置
        rectF.set(widgetWidthHalf - progressRadius, widgetHeightHalf - progressRadius,
                widgetWidthHalf + progressRadius, widgetHeightHalf + progressRadius);

        // 设置着色器
       // progressPaint.setShader(sweepGradient);

        // 第一个参数圆弧的形状和轮廓，第二个参数开始的角度，
        // 第三个参数圆弧顺时针扫过的角度， 第四个参数是否经过圆心
      //  canvas.drawArc(rectF, 0, progress, false, progressPaint);

        // 关闭硬件加速
        setLayerType(LAYER_TYPE_NONE, bitmapPaint);
      //  setLayerType(LAYER_TYPE_NONE, progressPaint);
        setLayerType(LAYER_TYPE_NONE, wavePaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // xml 没有指定大小， 默认为bitmap宽度的两倍
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width, height;
        // View的宽高是bitmap宽度的两倍
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = bitmapWidth * 2;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = bitmapHeight * 2;
        }
        setMeasuredDimension(width, height);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // bitmap点击范围
        int bitmapMargin = (widgetWidth - bitmapWidth) / 2;
        switch (event.getAction()) {
            // 点击暂停涟漪和进度条， 再点击继续播放
            case MotionEvent.ACTION_DOWN:
                if (!isEnd)
                    // 判断点击位置是bitmap范围内
                    if (event.getX() >= bitmapMargin && event.getX() <= bitmapMargin + bitmapWidth
                            && event.getY() >= bitmapMargin && event.getY() <= bitmapMargin + bitmapHeight)
                        // 判断是否正在运行， 运行就暂停， 暂停就继续
                        if (isRunning()) {
                            // 涟漪暂停
                            stop();
                            // 进度条动画暂停(注意pause()这个方法在4.4才有)
                          //  progressAnimator.pause();
                        } else {
                            // 涟漪继续
                            start();
                            // 进度条动画继续(注意resume这个方法在4.4才有)
                          //  progressAnimator.resume();
                        }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * 创建涟漪
     */
    private void createWave() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < DEFAULT_SPEED) {
            return;
        }
        Wave wave = new Wave();
        mWaveList.add(wave);
        invalidate();
        mLastCreateTime = currentTime;
    }


    /**
     * 控制涟漪透明度、 持续时间、 大小
     */
    private class Wave {
        private long mCreateTime;

        public Wave() {
            this.mCreateTime = System.currentTimeMillis();
        }

        public int getAlpha() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / DEFAULT_DURATION_TIME;
            return (int) ((1.0f - mInterpolator.getInterpolation(percent)) * 255);
        }

        public float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / DEFAULT_DURATION_TIME;
            return mMinRadius + mInterpolator.getInterpolation(percent) * (mMaxRadius - mMinRadius);
        }
    }


    /**
     * 600毫秒创建一个涟漪
     */
    private Runnable mCreateWave = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                createWave();
                postDelayed(mCreateWave, DEFAULT_SPEED);
            }
        }
    };


    /**
     * 开始
     */
    public void start() {
        if (!isRunning) {
            isRunning = true;
            mCreateWave.run();
        }
    }


    /**
     * 暂停
     */
    public void stop() {
        isRunning = false;
        mWaveList.clear();
    }

    public boolean isRunning() {
        return isRunning;
    }


    public void setRunning(boolean running) {
        isRunning = running;
    }


//    public float getProgress() {
//        return progress;
//    }
//
//
//    public void setProgress(float progress) {
//        this.progress = progress;
//        invalidate();
//    }
//
//
//    public int getProgressColor() {
//        return progressColor;
//    }
//
//
//    public void setProgressColor(int progressColor) {
//        this.progressColor = progressColor;
//        invalidate();
//    }

}
