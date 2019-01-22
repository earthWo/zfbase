package whitelife.win.viewlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import  android.support.v7.widget.AppCompatEditText;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;

import whitelife.win.viewlibrary.R;

/**
 * 带icon的，带删除按钮的，带分割线的EditText
 * Created by wuzefeng on 2017/10/10.
 */

public class CleanEditText extends AppCompatEditText implements View.OnClickListener,View.OnFocusChangeListener,TextWatcher {


    /**
     * 左边的icon
     */
    private Drawable mIcon;

    /**
     * 左边的有焦点的icon
     */
    private Drawable mFocusIcon;

    /**
     * 删除图片
     */
    private Drawable mDeleteIcon;

    /**
     * icon的宽
     */
    private int mIconWidth;

    /**
     * icon的高
     */
    private int mIconHeight;

    /**
     * 删除图片的宽
     */
    private int mDeleteIconWidth;


    /**
     * 删除图片的高
     */
    private int mDeleteIconHeight;


    /**
     * 分割线颜色
     */
    private int dividerColor;

    /**
     * 分割线厚度
     */
    private int dividerBonds;


    /**
     * 获得焦点时分割线颜色
     */
    private int focusDividerColor;


    /**
     * 绘制分割线的paint
     */
    private Paint dividerPaint;


    /**
     * 分割线左距
     */
    private int dividerMarginLeft;


    /**
     * 分割线右距
     */
    private int dividerMarginRight;


    /**
     * 文字颜色
     */
    private int textColor;


    /**
     * 获得焦点时文字颜色
     */
    private int focusTextColor;


    /**
     * 背景
     */
    private Drawable background;


    /**
     * 删除按钮的位置
     */
    private Rect deleteRect;


    public CleanEditText(Context context) {
        this(context,null);
    }

    public CleanEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public CleanEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attr(context,attrs);
    }


    private void attr(Context context, AttributeSet attrs){


        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.cleanEditText);

        //有左边的icon
        if(a.hasValue(R.styleable.cleanEditText_icon)){
            mIcon=a.getDrawable(R.styleable.cleanEditText_icon);
        }
        //有左边的focusIcon
        if(a.hasValue(R.styleable.cleanEditText_focusIcon)){
            mFocusIcon=a.getDrawable(R.styleable.cleanEditText_focusIcon);
        }
        //有右边的icon
        if(a.hasValue(R.styleable.cleanEditText_deleteIcon)){
            mDeleteIcon=a.getDrawable(R.styleable.cleanEditText_deleteIcon);
        }

        //如果有icon，则获取icon的尺寸
        if(mIcon!=null&&a.hasValue(R.styleable.cleanEditText_iconWidth)){
            mIconWidth= (int) a.getDimension(R.styleable.cleanEditText_iconWidth,0);
        }

        if(mIcon!=null&&a.hasValue(R.styleable.cleanEditText_iconHeight)){
            mIconHeight= (int) a.getDimension(R.styleable.cleanEditText_iconHeight,0);
        }


        //如果有删除按钮，则获取按钮的尺寸
        if(mDeleteIcon!=null&&a.hasValue(R.styleable.cleanEditText_deleteIconWidth)){
            mDeleteIconWidth= (int) a.getDimension(R.styleable.cleanEditText_deleteIconWidth,0);
        }

        if(mDeleteIcon!=null&&a.hasValue(R.styleable.cleanEditText_deleteIconHeight)){
            mDeleteIconHeight= (int) a.getDimension(R.styleable.cleanEditText_deleteIconHeight,0);
        }


        //设置icon和删除按钮的尺寸
        if(mIcon!=null){
            mIcon.setBounds(0, 0, mIconWidth, mIconHeight);
        }
        if(mFocusIcon!=null){
            mFocusIcon.setBounds(0, 0, mIconWidth, mIconHeight);
        }
        if(mDeleteIcon!=null){
            mDeleteIcon.setBounds(0, 0, mDeleteIconWidth, mDeleteIconHeight);
            deleteRect=new Rect();
            point=new Point();
        }

        //设置icon和删除按钮
        setDrawables();
        dividerPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

        //设置分割线颜色
        if(a.hasValue(R.styleable.cleanEditText_dividerColor)){
            dividerColor=  a.getColor(R.styleable.cleanEditText_dividerColor,0);
            dividerPaint.setColor(dividerColor);
        }

        //设置分割线宽度
        if(a.hasValue(R.styleable.cleanEditText_dividerBonds)){
            dividerBonds= (int) a.getDimension(R.styleable.cleanEditText_dividerBonds,0);
            dividerPaint.setStrokeWidth(dividerBonds);
        }

        //设置获取焦点的分割线颜色
        if(a.hasValue(R.styleable.cleanEditText_focusDividerColor)){
            focusDividerColor=  a.getColor(R.styleable.cleanEditText_focusDividerColor,0);
        }

        //设置分割线的左右距
        if(a.hasValue(R.styleable.cleanEditText_dividerMarginLeft)){
            dividerMarginLeft= (int) a.getDimension(R.styleable.cleanEditText_dividerMarginLeft,0);
        }

        if(a.hasValue(R.styleable.cleanEditText_dividerMarginRight)){
            dividerMarginRight= (int) a.getDimension(R.styleable.cleanEditText_dividerMarginRight,0);
        }

        //获取当前的颜色
        textColor=getCurrentTextColor();

        //获取焦点时的文字颜色
        if(a.hasValue(R.styleable.cleanEditText_focusTextColor)){
            focusTextColor=a.getColor(R.styleable.cleanEditText_focusTextColor,0);
        }

        //获取设置的背景
        if(a.hasValue(R.styleable.cleanEditText_background)){
            background=a.getDrawable(R.styleable.cleanEditText_background);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(background);
        }else{
            this.setBackgroundDrawable(background);
        }

        //获取设置光标
        int cursorRes = a.getResourceId(R.styleable.cleanEditText_cursor, 0);
        if(cursorRes!=0) {
            try {
                // 2. 通过反射 获取光标属性
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                // 3. 传入资源ID
                f.set(this, cursorRes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算出删除按钮的位置
        if(deleteRect!=null){
            deleteRect.set(getMeasuredWidth()-getPaddingRight()-mDeleteIconWidth,(getMeasuredHeight()-mDeleteIconHeight)/2,
                    getMeasuredWidth()-getPaddingRight(),(getMeasuredHeight()+mDeleteIconHeight)/2);
        }
    }

    /**
     * 添加各种监听
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnClickListener(this);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(dividerMarginLeft,getMeasuredHeight()-dividerBonds/2,getMeasuredWidth()-dividerMarginRight,getMeasuredHeight()-dividerBonds/2,dividerPaint);
    }

    private Point point;

    /**
     * 设置触点的位置
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(point!=null){
            point.set((int)event.getX(),(int)event.getY());
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        //判断点击的是否是删除
        // 点击的是删除
        if(point!=null&&deleteRect!=null&&deleteRect.contains(point.x,point.y)){
            setText(null);
        }
    }

    /**
     * 不显示时清除drawable
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(background!=null){
            background.setCallback(null);
            background=null;
        }


        if(mIcon!=null){
            mIcon.setCallback(null);
            mIcon=null;
        }
        if(mFocusIcon!=null){
            mFocusIcon.setCallback(null);
            mFocusIcon=null;
        }
        if(mDeleteIcon!=null){
            mDeleteIcon.setCallback(null);
            mDeleteIcon=null;
        }

        removeTextChangedListener(this);
    }

    /**
     * 获得焦点时改变分割线和文字的颜色
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            if(focusTextColor!=0){
                setTextColor(focusTextColor);
            }
            if(focusDividerColor!=0){
                dividerPaint.setColor(focusDividerColor);
            }
        }else{
            if(textColor!=0){
                setTextColor(textColor);
            }
            if(dividerColor!=0){
                dividerPaint.setColor(dividerColor);
            }
        }
        setDrawables();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * 文字改变时改变删除按钮的显示情况
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
        setDrawables();
    }


    private void setDrawables(){
        setCompoundDrawables(mFocusIcon!=null&&isFocused()?mFocusIcon:mIcon,null,getText().toString().length()>0&&isFocused()?mDeleteIcon:null,null);
    }



}
