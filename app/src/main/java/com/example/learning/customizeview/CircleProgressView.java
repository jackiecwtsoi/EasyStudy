package com.example.learning.customizeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * 进度
 *
 * @author ly
 * date 2020/3/3 11:02
 */
public class CircleProgressView extends View {

    private static final int COMPLETE = 360;
    private int okSpeed = 3;

    private Paint paint;
    //进度圈颜色
    private int circleOutsideColor;
    //进度颜色
    private int circleProgressColor;
    private float progressW;
    //进度条圆圈半径
    private float circleProgressR;
    private OnProgressListener onProgressListener;

    private RectF progressRect;
    private float progress;
    private String progressText;
    private Path okPath;
    private int sX, sY;
    private int mX;
    private int eX;
    private int cX, cY;
    private Context context;

    public CircleProgressView(Context context) {

        super(context);
        this.context = context;
        init();
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        circleOutsideColor = 0xffF2F4F5;
        circleProgressColor = 0xff6066DD;
        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true); //设置防抖动
    }

    /**
     * @param progress 已加载进度/总进度
     * @author ly on 2020/3/3 16:41
     */
    public void setProgress(float progress) {
        this.progress = progress * COMPLETE;
        if (this.progress >= COMPLETE) {//表示录制达到最大时长，自动结束
            this.progress = COMPLETE;
        }
        progressText = (int) (progress * 100) + "%";
        invalidate();
    }

    public void reset() {
        setProgress(0);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (circleProgressR == 0 || okPath == null) {
            int w = getWidth();
            progressW = w * 0.061f;
            circleProgressR = (w - progressW) / 2f;

            progressRect = new RectF(0 + progressW / 2, 0 + progressW / 2, w - progressW / 2, w - progressW / 2);

            int okW = (int) ((getWidth() - progressW) * 0.45);
            int okH = (int) ((getHeight() - progressW) * 0.32);

            cX = sX = (getWidth() - okW) / 2;
            cY = sY = getHeight() / 2;
            mX = (int) (sX + 0.39 * okW);
            int mY = (int) (sY + 0.35 * okW);
            eX = getWidth() - (getWidth() - okW) / 2;
            int eY = (getHeight() - okH) / 2;

            okPath = new Path();
            okPath.moveTo(sX, sY);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画进度圈
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(circleOutsideColor);
        paint.setStrokeWidth(progressW);//设置画笔粗细
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, circleProgressR, paint);

        //画进度就是圆弧
        //时钟3点的方向为0度，顺时钟方向为正，-90是圆弧的开始点，即12点位置开始，
        //sweepAngle扫过的角度，调整该值即可实现进度顺时针加载(0-360)
        paint.setColor(circleProgressColor);
        canvas.drawArc(progressRect, -90, progress, false, paint);

        if (progress < COMPLETE) {
            if (!TextUtils.isEmpty(progressText)) {
                // 将坐标原点移到控件中心
                int dx = getWidth() / 2;
                int dy = getHeight() / 2;
                canvas.translate(dx, dy);
                // 绘制居中文字
                paint.setTextSize(sp2px(context,30));
                // 文字宽
                float textWidth = paint.measureText(progressText);
                // 文字baseline在y轴方向的位置
                float baseLineY = Math.abs(paint.ascent() + paint.descent()) / 2;
                paint.setStyle(Paint.Style.FILL);
//            paint.setStrokeWidth(2);
                canvas.drawText(progressText, -textWidth / 2, baseLineY, paint);
            }
        } else {//加载完成，画√
            if (cX < eX) {//来个动画凑合用
                cX += okSpeed;
                if (cX < mX) {
                    cY += okSpeed;
                } else {
                    cY -= okSpeed;
                }
                invalidate();
            } else {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (onProgressListener != null)
                            onProgressListener.complete();
                    }
                }, 1500);
            }
            okPath.lineTo(cX, cY);
            canvas.drawPath(okPath, paint);
        }
    }

    public float getProgress() {
        return progress / COMPLETE;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public interface OnProgressListener {
        void complete();
    }
    public static int sp2px(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scaledDensity + 0.5f);
    }
}