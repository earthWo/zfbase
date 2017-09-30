package whitelife.win.viewlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import whitelife.win.viewlibrary.R;


/**
 * 一个旋转的imageview
 * Created by wuzefeng on 2017/9/26.
 */

public class RotateImageView extends android.support.v7.widget.AppCompatImageView {
    public RotateImageView(Context context) {
        this(context,null);
    }

    public RotateImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RotateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.rotateImageView);
        if(a.hasValue(R.styleable.rotateImageView_speed)){
            float sp=a.getFloat(R.styleable.rotateImageView_speed,0);
            setSpeed(sp);
        }
        a.recycle();
        runTask();
    }

    private int mWidth;

    private int mHeight;

    private int mTime=20;

    private int rotate;

    private int iRotate=10;

    private boolean rotateAble=true;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth=mHeight=MeasureSpec.makeMeasureSpec(widthMeasureSpec,MeasureSpec.UNSPECIFIED);
        setMeasuredDimension(mWidth,mHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(rotate,mWidth/2,mWidth/2);
        super.onDraw(canvas);
    }


    private void runTask(){
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(rotateAble){
                    rotate+=iRotate;
                    invalidate();
                    runTask();
                }
            }
        },mTime);
    }


    /**
     * 设置速度
     * @param speed 转/S
     */
    public void setSpeed(float speed){
        if(speed>0) {
            iRotate = (int) (speed * 360 / (1000 * 1.0f / mTime));
        }
    }

    public void stopRotate(){
        rotateAble=false;
    }

    public void startRotate(){
        if(!rotateAble){
            rotateAble=true;
            runTask();
        }
    }

}
