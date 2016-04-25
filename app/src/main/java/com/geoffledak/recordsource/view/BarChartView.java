package com.geoffledak.recordsource.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by geoff on 4/21/16.
 */
public class BarChartView extends View {

    String mLeftBarValue;
    String mRightBarValue;

    Paint mLeftBarPaint;
    Paint mRightBarPaint;
    Paint mValueTextPaint;

    Context mContext;


    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mLeftBarPaint = new Paint();
        mRightBarPaint = new Paint();
        mValueTextPaint = new Paint();
        mContext = context;

        mLeftBarValue = "0";
        mRightBarValue = "0";
		
        mLeftBarPaint.setStyle(Paint.Style.FILL);
        mLeftBarPaint.setAntiAlias(true);

        mRightBarPaint.setStyle(Paint.Style.FILL);
        mRightBarPaint.setAntiAlias(true);

        mLeftBarPaint.setColor(Color.parseColor("#8a8a8a"));
        mRightBarPaint.setColor(Color.parseColor("#bd3b2d"));

        mValueTextPaint.setStyle(Paint.Style.FILL);
        mValueTextPaint.setAntiAlias(true);
        mValueTextPaint.setColor(Color.parseColor("#FFFFFF"));
        mValueTextPaint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
        mValueTextPaint.setTypeface( Typeface.create(Typeface.DEFAULT, Typeface.BOLD) );
    }


    @Override
    protected void onDraw(Canvas canvas) {

        float halfBarHeight = getMeasuredHeight() / (float)2.8;
        float horizontalCenter = getMeasuredWidth() / 2;
        float verticalCenter = getMeasuredHeight() / 2;
        float halfBarWidth = halfBarHeight * 8;
        float halfBarDividerWidth = halfBarHeight / (float)18.0;

        float backgroundBarStartX = horizontalCenter - halfBarWidth;
        float backgroundBarStartY = verticalCenter - halfBarHeight;
        float backgroundBarEndX = horizontalCenter + halfBarWidth;
        float backgroundBarEndY = verticalCenter + halfBarHeight;

        float backgroundBarWidth = backgroundBarEndX - backgroundBarStartX;

        float leftValue = Float.parseFloat(mLeftBarValue);
        float rightValue = Float.parseFloat(mRightBarValue);
        float totalValue = leftValue + rightValue;

        float leftValueRatio = leftValue / totalValue;

        // if both values are zero, split the chart in half
        if( leftValue == 0 && rightValue == 0 )
            leftValueRatio = 0.5f;

        float leftBarEndX = (backgroundBarWidth * leftValueRatio) + backgroundBarStartX;


        // canvas.drawRect( backgroundBarStartX, backgroundBarStartY, backgroundBarEndX, backgroundBarEndY, mBackgroundBarPaint );

        // Draw left bar
        canvas.drawRect(backgroundBarStartX, backgroundBarStartY, leftBarEndX - halfBarDividerWidth, backgroundBarEndY, mLeftBarPaint);

        // Draw right bar
        canvas.drawRect(leftBarEndX + halfBarDividerWidth, backgroundBarStartY, backgroundBarEndX, backgroundBarEndY, mRightBarPaint);

        // Draw left text
        mValueTextPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(mLeftBarValue, leftBarEndX - (halfBarDividerWidth * 12), verticalCenter + (halfBarDividerWidth * 6), mValueTextPaint);

        // Draw right text
        mValueTextPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(mRightBarValue, leftBarEndX + (halfBarDividerWidth * 12), verticalCenter + (halfBarDividerWidth * 6), mValueTextPaint);
    }



    public void setStats( String leftValue, String rightValue ) {

        mLeftBarValue = leftValue;
        mRightBarValue = rightValue;
    }


    public void clear() {
        invalidate();
    }

}
