package com.example.marvin.magiceigthball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by marvin on 1/24/17.
 */

public class EightBall extends View {

    private Paint mPaintCircle;
    private Paint mPaintOval;
    private Paint mPaintTriangle;
    private Rect mRect;
    private RectF mRectF;
    private RectF mInnerRectF;
    private Path mTriPath;


    public EightBall(Context context, AttributeSet attrs) {
        super(context, attrs);


        mPaintTriangle = new Paint();
        mPaintTriangle.setColor(Color.BLUE);
        mPaintTriangle.setStyle(Paint.Style.FILL);
        mPaintTriangle.setAntiAlias(true);


        mPaintOval = new Paint();
        mPaintOval.setColor(Color.BLACK);
        mPaintOval.setAntiAlias(true);

        mRect = new Rect();
        mRectF = new RectF();
        mInnerRectF = new RectF();

        mTriPath = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getDrawingRect(mRect);

        mRectF.set(mRect);
        // Apply padding.
        mRectF.left += getPaddingLeft();
        mRectF.right -= getPaddingRight();
        mRectF.top += getPaddingTop();
        mRectF.bottom -= getPaddingBottom();

        // Make it square.
        if (mRectF.height() > mRectF.width()) {
            float center = mRectF.centerY();
            mRectF.top = center - mRectF.width() / 2;
            mRectF.bottom = center + mRectF.width() / 2;
        } else {
            float center = mRectF.centerX();
            mRectF.left = center - mRectF.height() / 2;
            mRectF.right = center + mRectF.height() / 2;
        }


        mInnerRectF.set(mRectF.left+mRectF.width()*.15f,
                        mRectF.top+mRectF.height()*.15f,
                        mRectF.right-mRectF.width()*.15f,
                        mRectF.bottom-mRectF.height()*.15f);

        float border =  mRectF.width()*0.025f;

        if(mPaintCircle == null)
        {
            mPaintCircle = new Paint();
            mPaintCircle.setColor(Color.BLACK);
            mPaintCircle.setAntiAlias(true);
            mPaintCircle.setShader(new RadialGradient(mRectF.width() * .7f, 0.0f, mRectF.height() * 4.0f, Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));
        }

        canvas.drawCircle(mRectF.centerX(),
                mRectF.centerY(),
                mRectF.width() * 0.5f - border,
                mPaintCircle);






        canvas.drawOval(mInnerRectF,mPaintOval);

        mTriPath.moveTo(mInnerRectF.left+mInnerRectF.width()*.1f,mInnerRectF.top+mInnerRectF.height()*.2f);
        mTriPath.lineTo(mInnerRectF.right-mInnerRectF.width()*.1f,mInnerRectF.top+mInnerRectF.height()*.2f);
        mTriPath.lineTo(mInnerRectF.left+mInnerRectF.width()*.5f,mInnerRectF.bottom);
        mTriPath.lineTo(mInnerRectF.left+mInnerRectF.width()*.1f,mInnerRectF.top+mInnerRectF.height()*.2f);
        mTriPath.close();

        canvas.drawPath(mTriPath,mPaintTriangle);



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }



}
