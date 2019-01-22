package whitelife.win.viewlibrary.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

/**
 * 控制view在两个位置间做曲线运动的popwindow
 * Created by wuzefeng on 2017/10/11.
 */

public class MoveAnimateView extends PopupWindow implements Animator.AnimatorListener {

    private Context context;

    /**
     * 动画是否开启
     */
    private boolean animateStart;

    /**
     * 动画时间
     */

    private static int DEFAULT_ANIMATE_TIME=1000;
    private int animateTime=DEFAULT_ANIMATE_TIME;

    /**
     * x轴动画
     */
    private ValueAnimator xAnimator;

    /**
     * y轴动画
     */
    private ValueAnimator yAnimator;

    /**
     * 整体动画
     */
    private AnimatorSet animationSet;

    /**
     * 需要进行动画的view
     */
    private View animatorView;

    public MoveAnimateView(Context context) {
        super(context);
        this.context = context;
        init();
    }


    private void init(){
        //设置整个view，宽高为整个屏幕
        FrameLayout frameLayout=new FrameLayout(context);
        setContentView(frameLayout);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        setWidth(dm.widthPixels);
        setHeight(dm.heightPixels);
        //设置背景为透明
        setBackgroundDrawable(new ColorDrawable());
        //添加动画监听
        animationSet=new AnimatorSet();
        animationSet.addListener(this);
        setAnimationStyle(0);

    }

    /**
     * 动画起始点
     */
    private Point startPoint;

    /**
     * 动画结束点
     */
    private Point endPoint;

    /**
     * 动画运动轨迹的控制点
     */
    private Point thirdPoint;

    /**
     * 开始动画
     * @param startPoint 起始点
     * @param endPoint 初始点
     * @param animatorView 动画view
     * @param root 整体显示的view
     */
    public void startAnimate(Point startPoint, Point endPoint, View animatorView,View root){
        if(!animateStart&&startPoint!=null&&endPoint!=null&&animatorView!=null){
            this.startPoint=startPoint;
            this.endPoint=endPoint;
            this.animatorView=animatorView;
            startAnim();
            showAsDropDown(root);
        }
    }


    /**
     * 开始动画
     * @param startView 起始位置的view
     * @param endView 结束位置的view
     * @param animatorView 动画view
     * @param root 整体显示的view
     */
    public void startAnimate(View startView, View endView, View animatorView,View root){
        if(!animateStart&&startView!=null&&endView!=null&&animatorView!=null){
            int[] startPosition = new int[2];
            startView.getLocationInWindow(startPosition);
            if(startPoint==null){
                startPoint=new Point(startPosition[0],startPosition[1]);
            }else{
                startPoint.set(startPosition[0],startPosition[1]);
            }
            int[] endPosition = new int[2];
            endView.getLocationInWindow(endPosition);
            if(endPoint==null){
                endPoint=new Point(endPosition[0],endPosition[1]);
            }else{
                endPoint.set(endPosition[0],endPosition[1]);
            }
            startAnimate(startPoint,endPoint,animatorView,root);
        }
    }


    /**
     * 开始动画
     */
    private void startAnim(){
        createThirdPoint();
        //初始化动画
        xAnimator= ObjectAnimator.ofFloat(animatorView,"translationX",startPoint.x,endPoint.x);
        yAnimator=  ObjectAnimator.ofFloat(animatorView,"translationY",startPoint.y,endPoint.y);
        xAnimator.setDuration(animateTime);
        yAnimator.setDuration(animateTime);
        xAnimator.setEvaluator(xEvaluator);
        yAnimator.setEvaluator(yEvaluator);
        animationSet.play(xAnimator).with(yAnimator);
        animationSet.start();
    }


    /**
     * x位置变化
     */
    private TypeEvaluator<Float>xEvaluator=new TypeEvaluator<Float>() {
        @Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {
            float x= (float) ((1.0-fraction)*(1.0-fraction)*startPoint.x*1.0+2*fraction*(1.0-fraction)*thirdPoint.x*1.0+fraction*fraction*endPoint.x*1.0);
            return x;
        }
    };

    /**
     * y位置变化
     */
    private TypeEvaluator<Float>yEvaluator=new TypeEvaluator<Float>() {
        @Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {
            float y= (float) ((1.0-fraction)*(1.0-fraction)*startPoint.y*1.0+2*fraction*(1.0-fraction)*thirdPoint.y*1.0+fraction*fraction*endPoint.y*1.0);
            return y;
        }
    };

    /**
     * 创建控制点位置
     */
    private void createThirdPoint(){
        if(thirdPoint==null)
            thirdPoint=new Point();

        thirdPoint.x=endPoint.x;
        thirdPoint.y= startPoint.y;
    }


    @Override
    public void onAnimationStart(Animator animation) {
        animateStart=true;
        ((ViewGroup)getContentView()).addView(animatorView);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        animateStart=false;
        ((ViewGroup)getContentView()).removeAllViews();
        dismiss();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public int getAnimateTime() {
        return animateTime;
    }

    public void setAnimateTime(int animateTime) {
        this.animateTime = animateTime;
    }
}
