package whitelife.win.recyclerviewlibrary.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;
import java.util.ArrayList;
import java.util.List;
import whitelife.win.recyclerviewlibrary.R;

/**
 * 侧滑可以看item下一层的 RecyclerView
 *
 * @author wuzefeng
 * @date 16/9/22
 */

public class SwipeRecyclerView extends RecyclerView {


    /**
     * 计算速度
     */
    private VelocityTracker velocityTracker;


    private int touchSlop;//滑动最小距离


    private static int INVALID_POSITION=-1;

    private int position=INVALID_POSITION;


    private View scrollView; //滑动的view


    private boolean isScroll; //是的正在滑动


    private final int DEFAULT_MAX_SCROLL_LENGTH=200;  //默认最大滑动距离

    private int maxScrollLength=DEFAULT_MAX_SCROLL_LENGTH; //最大滑动距离

    private int lastX; //上一个触点的x值

    private Scroller scroller;

    private final int DEFAULT_SCROLL_TIME=200;  //默认滑动时间

    private int scrollTime=DEFAULT_SCROLL_TIME;  //滑动时间

    private ScrollType scrollType;

    private boolean swipeClosesAllItemsWhenListMoves = false;  //list滑动时是否全部关闭

    private boolean onlyOneOpenedWhenSwipe = true; //是否只能打开一个

    private FrameLayout.LayoutParams layoutParams;

    private List<String>openItemPosition;  //打开的item 序号

    private int touchPosition;  // 滑动的序号

    private int firstVisibleItem;

    private int lastVisibleItem;

    private int frontLayoutId; // 前面的layout id

    private LayoutManager layoutManager;

    private int viewTop;

    private enum ScrollType{
        SCROLL_CLOSE,
        SCROLL_OPEN
    }

    /**
     * 固定的position
     */
    private List<Integer>stationaryPositions;

    public SwipeRecyclerView(Context context) {
        this(context,null);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.SwipeRecyclerView, defStyleAttr, 0);

        maxScrollLength=a.getDimensionPixelOffset(R.styleable.SwipeRecyclerView_maxScrollLength,DEFAULT_MAX_SCROLL_LENGTH);

        scrollTime=a.getInteger(R.styleable.SwipeRecyclerView_scrollTime,DEFAULT_SCROLL_TIME);

        swipeClosesAllItemsWhenListMoves=a.getBoolean(R.styleable.SwipeRecyclerView_swipeClosesAllItemsWhenListMoves,true);

        onlyOneOpenedWhenSwipe=a.getBoolean(R.styleable.SwipeRecyclerView_onlyOneOpenedWhenSwipe,true);

        frontLayoutId=a.getResourceId(R.styleable.SwipeRecyclerView_front,0);

        init();
    }

    /**
     * 初始化
     */
    private void init(){
        stationaryPositions=new ArrayList<>();
        touchSlop= ViewConfiguration.get(getContext()).getScaledTouchSlop();
        openItemPosition=new ArrayList<>();
        super.addOnScrollListener(scrollListener);
    }

    /**
     * 获取整个View的top位置
     */
    private void getViewTop(){
        int[] position = new int[2];
        this.getLocationOnScreen(position);
        viewTop=position[1];
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        super.setLayoutManager(layoutManager);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //如果没有font id或者动画没有结束，则不做处理
        if(scroller!=null||frontLayoutId==0){
            return super.dispatchTouchEvent(ev);
        }else {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //按下时计算当前按的position
                    touchPosition = pointToPosition(x, y);
                    if (touchPosition != INVALID_POSITION && isPositionCanScroll(touchPosition)) {
                        position = touchPosition -getFirstVisiblePosition();
                        //将当前按下的view设置为scroll view
                        scrollView = getChildAt(position).findViewById(frontLayoutId);
                        if (scrollView != null) layoutParams = (FrameLayout.LayoutParams) scrollView.getLayoutParams();
                    }
                    lastX = x;
                    break;
                case MotionEvent.ACTION_MOVE:
                    //position可滑动到情况
                    if (touchPosition != INVALID_POSITION && isPositionCanScroll(touchPosition)) {
                        //计算速度
                        obtainVelocity(ev);
                        //判断是否
                        boolean canScroll = canScroll();
                        if (!isScroll) {
                            isScroll = canScroll;
                        }
                        //可以移动,事件不下发
                        if (canScroll) {
                            if (swipeClosesAllItemsWhenListMoves) {
                                closeOtherOpenedItems(touchPosition);
                            }
                            onTouchEvent(ev);
                            return super.dispatchTouchEvent(ev);
                        } else {
                            //不可移动，且滑动关闭，则将所有的滑开的全部全部
                            if (swipeClosesAllItemsWhenListMoves) {
                                closeOtherOpenedItems(-1);
                                openItemPosition.clear();
                            }
                            return super.dispatchTouchEvent(ev);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(!isScroll&&openItemPosition.size()==0){
                        return super.dispatchTouchEvent(ev);
                    }
                    onTouchEvent(ev);
                    if(isScroll){
                        isScroll=false;
                        return true;
                    }
                    isScroll=false;
                    break;

            }
            return super.dispatchTouchEvent(ev);
        }
    }


    /**
     * 根据速度的方向来判断是否可以侧滑
     * @return
     */
    private boolean canScroll(){
        velocityTracker.computeCurrentVelocity(1000);
        float xv = velocityTracker.getXVelocity();
        float yv = velocityTracker.getYVelocity();
        if(Math.abs(xv)>Math.abs(yv)*5&& Math.abs(xv) >touchSlop){
            return true;
        }
        return false;
    }

    /**
     * 获取根据x,y的位置获取滑动的position
     * @param x
     * @param y
     * @return
     */
    private int pointToPosition(int x, int y) {
        if(layoutManager==null){
            return INVALID_POSITION;
        }else{
            for(int i=0;i<getChildCount();i++){
                Rect rect=new Rect();
                getChildAt(i).getGlobalVisibleRect(rect);
                rect.right=Math.abs(rect.right);
                if(viewTop==0) {
                    getViewTop();
                }
                rect.top=rect.top-viewTop;
                rect.bottom=rect.bottom-viewTop;
                if(rect.contains(x,y)){
                    return getFirstVisiblePosition()+i;
                }
            }
        }

        return INVALID_POSITION;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x= (int) ev.getX();
        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                if(scrollView!=null&&layoutParams!=null&&isScroll){
                    //滑动状态下滑动item的上层view
                    int scrollX=lastX-x;
                    if(layoutParams.rightMargin+scrollX<0){
                        layoutParams.setMargins(0,0,0,0);
                        scrollView.setLayoutParams(layoutParams);
                    }else if(layoutParams.rightMargin+scrollX>maxScrollLength){
                        layoutParams.setMargins(-maxScrollLength,0,maxScrollLength,0);
                        scrollView.setLayoutParams(layoutParams);
                    }else{
                        layoutParams.setMargins(-layoutParams.rightMargin-scrollX,0,layoutParams.rightMargin+scrollX,0);
                        scrollView.setLayoutParams(layoutParams);
                    }
                }
                lastX=x;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //如果时滑开的状态，将滑动结束
                if(isScroll) {
                    startScroll();
                    releaseVelocity();
                    lastX = 0;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    private OnScrollListener scrollListener=new OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(getFirstVisiblePosition()!=firstVisibleItem||getFirstVisiblePosition()+getChildCount()-1!=lastVisibleItem){
                for (int i = 0; i < getChildCount(); i++) {
                    View rootView = getChildAt(i);
                    int p = getFirstVisiblePosition() + i;
                    if (rootView != null) {
                        View scrollView = rootView.findViewById(frontLayoutId);
                        if (scrollView!=null&&openItemPosition.contains(p + "")) {
                            openItem(scrollView);
                        } else if (scrollView!=null&&touchPosition != p && ((FrameLayout.LayoutParams) scrollView.getLayoutParams()).rightMargin >0) {
                            closeItem(scrollView);
                        }
                    }
                }
            }

            firstVisibleItem=getFirstVisiblePosition();
            lastVisibleItem=firstVisibleItem+getChildCount()-1;

        }
    };

    public List<Integer> getStationaryPositions() {
        return stationaryPositions;
    }

    public void setStationaryPositions(List<Integer> stationaryPositions) {
        this.stationaryPositions = stationaryPositions;
    }

    public void addStationaryPosition(int position){
        if(!this.stationaryPositions.contains(position)){
            this.stationaryPositions.add(position);
        }
    }

    private boolean isPositionCanScroll(int position){
        return !this.stationaryPositions.contains(position);
    }

    /**
     * 关闭全部item 除了第p个
     * @param p
     */
    private void closeOtherOpenedItems(int p){
        ArrayList<String>opens= (ArrayList<String>) ((ArrayList<String>) openItemPosition).clone();
        for(final String openItem:opens){
            final int position=Integer.parseInt(openItem);
            if(position!=p) {
                openItemPosition.remove(openItem);
                ValueAnimator animator = ValueAnimator.ofInt(maxScrollLength, 0);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if(getChildCount()>position-getFirstVisiblePosition()&&position-getFirstVisiblePosition()>-1) {
                            View view = getChildAt(position - getFirstVisiblePosition()).findViewById(frontLayoutId);
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                            layoutParams.setMargins(-(Integer) animation.getAnimatedValue(), 0, (Integer) animation.getAnimatedValue(), 0);
                            view.setLayoutParams(layoutParams);
                        }
                    }
                });
                animator.start();
            }
        }
    }

    public int getMaxScrollLength() {
        return maxScrollLength;
    }

    public void setMaxScrollLength(int maxScrollLength) {
        this.maxScrollLength = maxScrollLength;
    }

    public boolean isSwipeClosesAllItemsWhenListMoves() {
        return swipeClosesAllItemsWhenListMoves;
    }

    public void setSwipeClosesAllItemsWhenListMoves(boolean swipeClosesAllItemsWhenListMoves) {
        this.swipeClosesAllItemsWhenListMoves = swipeClosesAllItemsWhenListMoves;
    }

    public boolean isOnlyOneOpenedWhenSwipe() {
        return onlyOneOpenedWhenSwipe;
    }

    public void setOnlyOneOpenedWhenSwipe(boolean onlyOneOpenedWhenSwipe) {
        this.onlyOneOpenedWhenSwipe = onlyOneOpenedWhenSwipe;
    }

    public int getFrontLayoutId() {
        return frontLayoutId;
    }

    public void setFrontLayoutId(int frontLayoutId) {
        this.frontLayoutId = frontLayoutId;
    }

    public int getFirstVisiblePosition() {
        if(layoutManager!=null&&layoutManager instanceof LinearLayoutManager){
            return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }else if(layoutManager!=null&&layoutManager instanceof GridLayoutManager){
            return ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        return 0;
    }

    /**
     * 关闭全部第p个item
     * @param p
     */
    private void closeOpenedItemByPosition(int p){
        for(final String openItem:openItemPosition){
            final int position=Integer.parseInt(openItem);
            if(position==p) {
                ValueAnimator animator = ValueAnimator.ofInt(maxScrollLength, 0);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if(getChildCount()>position-getFirstVisiblePosition()&&position-getFirstVisiblePosition()>-1) {
                            View view = getChildAt(position - getFirstVisiblePosition()).findViewById(frontLayoutId);
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                            layoutParams.setMargins(-(Integer) animation.getAnimatedValue(), 0, (Integer) animation.getAnimatedValue(), 0);
                            view.setLayoutParams(layoutParams);
                        }
                    }
                });
                animator.start();
            }

        }
    }

    /**
     * 直接关闭
     * @param view
     */
    private void closeItem(View view){
        if(view!=null){
            FrameLayout.LayoutParams layoutParams= (FrameLayout.LayoutParams) view.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
            view.setLayoutParams(layoutParams);
        }
    }

    /**
     * 直接打开
     * @param view
     */
    private void openItem(View view){
        if(view!=null){
            FrameLayout.LayoutParams layoutParams= (FrameLayout.LayoutParams) view.getLayoutParams();
            layoutParams.setMargins(-maxScrollLength,0,maxScrollLength,0);
            view.setLayoutParams(layoutParams);
        }
    }


    /**
     * 开始滑动
     */
    private void startScroll(){

        if(scrollView!=null&&layoutParams!=null&&scroller==null){

            if(scroller==null)scroller=new Scroller(getContext());

            if(layoutParams.rightMargin<maxScrollLength/2){// 未到一半
                scrollType= ScrollType.SCROLL_CLOSE;

                int time= (int) ((maxScrollLength/2-layoutParams.rightMargin)*1.0f/maxScrollLength*scrollTime);

                scroller.startScroll(layoutParams.rightMargin,0,-layoutParams.rightMargin,0,time);

                if(openItemPosition.contains(touchPosition+"")) openItemPosition.remove(touchPosition+"");

            }else{//到了一半
                scrollType= ScrollType.SCROLL_OPEN;

                int time= (int) ((maxScrollLength-layoutParams.rightMargin)*1.0f/maxScrollLength*scrollTime);

                scroller.startScroll(layoutParams.rightMargin,0,maxScrollLength-layoutParams.rightMargin,0,time);

                if(!openItemPosition.contains(touchPosition)) {
                    openItemPosition.add(touchPosition + "");
                }

            }

            invalidate();
        }
    }

    /**
     * 关闭所有的view
     */
    public void closeAllOpenedItems(){
        position=INVALID_POSITION;
        touchPosition=INVALID_POSITION;
        closeOtherOpenedItems(INVALID_POSITION);
    }


    @Override
    public void computeScroll() {
        if (scroller != null && scroller.computeScrollOffset() && scrollView != null&&layoutParams!=null) {//打开或者返回
            int x = scroller.getCurrX();
            layoutParams.setMargins(-x,0,x,0);
            scrollView.setLayoutParams(layoutParams);
            invalidate();
        }else if(scroller != null && !scroller.computeScrollOffset() && scrollView != null&&layoutParams!=null){//滑动结束
            //打开结束后如果只能打开一个item,关闭其他item
            if(scroller!=null&&scrollType== ScrollType.SCROLL_OPEN&&!swipeClosesAllItemsWhenListMoves&&onlyOneOpenedWhenSwipe){
                closeOtherOpenedItems(touchPosition);
                openItemPosition.clear();
                openItemPosition.add(touchPosition+"");
            }
            invalidate();
            scroller=null;
        }
    }

    /**
     * 获取触点的速度
     * @param event
     */
    private void obtainVelocity(MotionEvent event){
        if(velocityTracker==null){
            velocityTracker=VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
    }


    /**
     * 释放速度
     */
    private void releaseVelocity(){
        if(velocityTracker!=null){
            velocityTracker.clear();
            velocityTracker.recycle();
            velocityTracker=null;
        }
    }




}
