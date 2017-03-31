package com.weather.weather.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.weather.weather.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by ilyas on 31.03.2017.
 */

public class GraphTemp extends View {
    private String minTemp;
    private String middleTemp;
    private String highTemp;
    private boolean isTextDraw;
    private Paint mPaint = new Paint(ANTI_ALIAS_FLAG);
    private Paint mPaintCircle = new Paint();
    private Paint mPaintText = new Paint();

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
        isTextDraw = true;
        invalidate();
    }

    public String getMiddleTemp() {
        return middleTemp;
    }

    public void setMiddleTemp(String middleTemp) {
        this.middleTemp = middleTemp;
    }

    public String getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(String highTemp) {
        this.highTemp = highTemp;
    }

    public GraphTemp(Context context) {
        super(context);
    }

    public GraphTemp(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // настройка кисти
        mPaint.setStyle(Paint.Style.STROKE);
        mPaintCircle.setStyle(Paint.Style.FILL);

        mPaintText.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setFlags(Paint.ANTI_ALIAS_FLAG);

        mPaintText.setColor(Color.WHITE);
        mPaint.setColor(Color.WHITE);
        mPaintCircle.setColor(Color.WHITE);

        mPaint.setStrokeWidth(10);
        mPaintText.setTextSize(25);

        canvas.drawLine(getPixel(30), getPixel(120), getWidth() - getPixel(30), getPixel(60), mPaint);
        //minTemp
        canvas.drawCircle(getPixel(30), getPixel(120), 25, mPaintCircle);
        canvas.drawText(getContext().getString(R.string.today_min_temp), getPixel(20), getPixel(145), mPaintText);
        //middleTemp
        canvas.drawCircle(((getWidth()) / 2), getPixel(90), 25, mPaintCircle);
        canvas.drawText(getContext().getString(R.string.today_middle_temp), ((getWidth() / 2) - getPixel(10)), getPixel(115), mPaintText);
        //highTemp
        canvas.drawCircle(getWidth() - getPixel(30), getPixel(60), 25, mPaintCircle);
        canvas.drawText(getContext().getString(R.string.today_high_temp), getWidth() - getPixel(40), getPixel(85), mPaintText);

        if (isTextDraw) {
            mPaintText.setTextSize(40);
            canvas.drawText(minTemp, getPixel(20), getPixel(95), mPaintText);
            canvas.drawText(middleTemp, ((getWidth() / 2) - getPixel(10)), getPixel(65), mPaintText);
            canvas.drawText(highTemp, getWidth() - getPixel(40), getPixel(35), mPaintText);
        }
    }

    private int getPixel(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }
}
