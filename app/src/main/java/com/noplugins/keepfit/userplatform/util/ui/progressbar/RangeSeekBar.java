package com.noplugins.keepfit.userplatform.util.ui.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.util.ui.DensityUtil;

/**
 * @author zlc
 * @created 01/06/2018
 * @desc
 * 自定义游标卡尺
 */
public class RangeSeekBar extends ViewGroup implements ThumbImageView.OnScrollListener{

    private ThumbImageView mLeftIcon;
    private Paint mPbPaint;
    private Paint mRulerPaint;

    private int mRulerMax = 100;

    //刻度尺进度条的高度
    private int mPbHeight;
    //刻度长线的高度
    private float mLineMaxHeight;
    //刻度短线的高度
    private float mLineMinHeight;
    //每一小份刻度的宽度
    private float mPartWidth;
    //图片左右最小最大值
    private int mLeftMin;
    private int mRightMax;
    //刻度的左右间距
    private int mLeftMargin;
    private int mRightMargin;
    private Paint mValuePaint;
    //左右两边图片id
    private static final int LEFT_ICON_ID = 0x166666;
    private static final int RIGHT_ICON_ID = 0x166667;
    //文字大小
    private float mTextSize;
    //进度值的背景图片高度
    private int mValueBitmapHeight;
    //y轴上偏差值 防止高度太紧凑
    private int mOffsetY = dip2px(4);
    //控件的宽度
    private  int  mViewWidth;
    //控件的高度
    private int mViewHeight;

    //大刻度之间的距离
    private int bBigSpaceUnit;

    //左偏移值
    private int mLeftOffset;
    //右偏移值
    private int mRightOffset;
    //view的中间高度
    private int childViewHeight;
    public RangeSeekBar(Context context) {
        this(context,null);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundDrawable(new BitmapDrawable());
        addChildViews(context);
        initPaints();
    }

    private void addChildViews(Context context) {

        mLeftIcon = new ThumbImageView(context);
        mLeftIcon.setImageResource(R.drawable.huadong);

        mLeftIcon.setRangeSeekBar(this);
        mLeftIcon.setText("07:00-08:00");
        mLeftIcon.setId(LEFT_ICON_ID);
        mLeftIcon.setOnScrollListener(this);


        this.addView(mLeftIcon);

        mLeftMargin = 0;
        mRightMargin = 0;

        mValueBitmapHeight = dip2px(23);
        mPbHeight = dip2px(8);
        mLineMaxHeight = dip2px(26);
        mLineMinHeight = dip2px(14);
        mTextSize = dip2px(10);
    }

    public String getTime(){
        Log.d("RangeSeekBar",mLeftIcon.getText());
        return mLeftIcon.getText();
    }

    private void initPaints() {

        //初始化进度条画笔
        mPbPaint = new Paint();
        mPbPaint.setAntiAlias(true);
        mPbPaint.setColor(Color.GRAY);

        //初始化刻度尺画笔
        mRulerPaint = new Paint();
        mRulerPaint.setAntiAlias(true);
        mRulerPaint.setColor(Color.GRAY);
        mRulerPaint.setStrokeWidth(1);
        mRulerPaint.setTextSize(mTextSize);
        mRulerPaint.setTextAlign(Paint.Align.CENTER);

        //初始化刻度值画笔
        mValuePaint = new Paint();
        mValuePaint.setColor(Color.WHITE);
        mValuePaint.setTextAlign(Paint.Align.CENTER);
        mValuePaint.setTextSize(mTextSize);
    }

    public int dip2px(float dpValue){
        return DensityUtil.dp2px(dpValue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量所有子控件
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        //得到当前控件的测量模式和测量大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //wrap_content
        int width = widthSize;
        int height = dip2px(120);

        width = widthMode == MeasureSpec.AT_MOST ? width : widthSize;
        height = heightMode == MeasureSpec.AT_MOST ? height : heightSize;

        setMeasuredDimension(width,height);

        this.mViewWidth = width;
        this.mViewHeight = height;
        mLeftMin = mLeftMargin;
        mRightMax = width - mRightMargin;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for (int i = 0; i < getChildCount(); i++) {

            View childView = getChildAt(i);
            int cWidth = childView.getMeasuredWidth();
            mLeftOffset = cWidth/2;
            int cHeight = childView.getMeasuredHeight();
            mRightOffset = mLeftOffset;
            childViewHeight = (cHeight-50)/2;
            int left = 0;
            //左右可以拖动的图片距上面的高度=进度条的高度 + 最长刻度线的高度 + 刻度值字体大小的高度
            // + 拖动显示刻度值背景图片的高度 + 偏移量
            int top = (int) (mPbHeight + mLineMaxHeight + mTextSize
                    + mValueBitmapHeight + mOffsetY);
            int right = 0;
            //距底部的高度 = top + 图片的高度 - 距离底部的padding值
            int bottom = top + cHeight - getPaddingBottom();
            if(i == 0){
                left = mLeftMargin + getPaddingLeft();
                right = left + cWidth;
            }else if(i == 1){
                left = (mViewWidth - mRightMargin - cWidth - getPaddingRight());
                right = left + cWidth;
            }
            childView.layout(left,top,right,bottom);
        }

        int leftWidth = mLeftIcon.getMeasuredWidth();

        mPartWidth = (mViewWidth - (mLeftMargin + leftWidth / 2)
                - (mRightMargin)) /  (float) mRulerMax;

        bBigSpaceUnit = (mViewWidth-(mLeftOffset-2)*2)/8;

        mLeftIcon.setLeftRightLimit(0,mViewWidth - bBigSpaceUnit/2);

        mLeftIcon.setCenterX(mLeftMargin + leftWidth / 2);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawProgressBar(canvas);

        if(mLeftIcon.isMoving()){
            drawRulerValue(canvas,mLeftIcon);
        }

        drawRuler(canvas);
    }

    private void drawRulerValue(Canvas canvas,ThumbImageView imageView) {
//        int centerX = imageView.getCenterX();
//        Bitmap valueBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rod_place_icon);
//        int top = 0;
//        canvas.drawBitmap(valueBitmap,centerX - valueBitmap.getWidth() / 2,top,mValuePaint);
//
////        int bottom = mViewHeight - childViewHeight;
//        canvas.drawText(String.valueOf(getValue(imageView)),
//                centerX,top + valueBitmap.getHeight() / 2 + dip2px(3),mValuePaint);
        mLeftIcon.setText(value2Time(getValue(imageView)));
        mLeftIcon.invalidate();
    }

    private String value2Time(double value){
        int hour = 7+(int) Math.floor(value / (100/16.0));
        String startH = "";
        String endH = "";
        if (hour<=9){
            startH = "0"+hour;
        } else {
            startH = ""+hour;
        }
        if (hour+1 <=9){
            endH = "0"+(hour+1);
        }
        else {
            endH = ""+(hour+1);
        }
        Log.d("value2Time","fenzhong:"+hour);

        Log.d("value2Time","value:"+value / (100.0/16.0));
        Log.d("value2Time","value:"+value / (100/16));
//        int minute = (int) ((value/10.0 - Math.floor(value/10.0))*60);
        int minute = (int) ((value / (100.0/16.0) - Math.floor(value / (100/16.0)))*60)/5 * 5;

        String allMinute;
        if (minute<10){
            allMinute="0"+minute;
        } else {
            allMinute = ""+minute;
        }
        Log.d("value2Time","minute:"+minute);
        return startH+":"+allMinute+"-"+endH+":"+allMinute;
    }

    private  double getValue(ThumbImageView imageView){

        Log.d("getValue","i:"+100*(imageView.getCenterX()-mLeftOffset)/(mViewWidth-mLeftOffset*2));


        //17个刻度，
         double d= 100 * (imageView.getCenterX() - mLeftMin - imageView.getWidth() / 2.0)
                / (mRightMax - mLeftMin - imageView.getWidth());

        return (double) Math.round(d * 100) / 100;
    }



    //画进度条
    private void drawProgressBar(Canvas canvas) {

        //绘制所有时间背景
        mPbPaint.setColor(getResources().getColor(R.color.color_40b2dfe9));
        canvas.drawRect(0, mViewHeight/3, mViewWidth, mViewHeight/3+60, mPbPaint);


        //游标卡尺拨动过程中 实时进度条的绘制
        mPbPaint.setColor(getResources().getColor(R.color.color_b2dfe9));
        Rect rect = new Rect(mLeftIcon.getCenterX(),mViewHeight/3,
                mLeftIcon.getCenterX()+bBigSpaceUnit/2,mViewHeight/3+60);
        canvas.drawRect(rect, mPbPaint);
    }

    //画刻度尺和刻度值
    private void drawRuler(Canvas canvas) {


        //画9个大刻度

        for (int i = 0;i<9;i++){

            //画时间
            Rect rect = new Rect();
            int hour = 7+i*2;
            String str;
            if (hour<10){
                str = "0"+hour+":00";
            } else {
                str = hour+":00";
            }
            mRulerPaint.setColor(getResources().getColor(R.color.black));
            mRulerPaint.setTextSize(DensityUtil.dp2px(8));
            mRulerPaint.getTextBounds(str, 0, str.length(), rect);
            int w = rect.width();
            int h = rect.height();
            if (i == 0||i%2 ==0){
                canvas.drawText(str,mLeftOffset + i * bBigSpaceUnit,mViewHeight/3-h,mRulerPaint);
            } else {
                canvas.drawText(str,mLeftOffset + i * bBigSpaceUnit,mViewHeight/3+60+h+20,mRulerPaint);
            }


            mRulerPaint.setColor(getResources().getColor(R.color.color_75CEE1));
            //画大刻度线
            mRulerPaint.setStrokeWidth(4);
            canvas.drawLine(mLeftOffset + i * bBigSpaceUnit, mViewHeight/3+(60-50),
                    mLeftOffset + i * bBigSpaceUnit, mViewHeight/3+(60-10), mRulerPaint);


            if (i == 8){
                continue;
            }
            //画中刻度线
            mRulerPaint.setStrokeWidth(4);
            canvas.drawLine(mLeftOffset + i * bBigSpaceUnit+bBigSpaceUnit/2, mViewHeight/3+(60-45),
                    mLeftOffset + i * bBigSpaceUnit+bBigSpaceUnit/2, mViewHeight/3+(60-15), mRulerPaint);

        }

    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    public void onValue(View v,int value) {

        switch (v.getId()){
            case LEFT_ICON_ID:
                Log.e("left onValue",value+"");
                break;
            case RIGHT_ICON_ID:
                Log.e("right onValue",value+"");
                break;
        }
//        mLeftIcon.setText(""+value);
        this.invalidate();
    }
}