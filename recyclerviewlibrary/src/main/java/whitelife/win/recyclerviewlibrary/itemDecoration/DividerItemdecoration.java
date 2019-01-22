package whitelife.win.recyclerviewlibrary.itemDecoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wuzefeng on 2017/9/28.
 */

public class DividerItemdecoration extends RecyclerView.ItemDecoration {

    private int direction;

    private int width;

    private int color;

    private Paint paint;


    public DividerItemdecoration(int direction, int width, int color) {
        this.direction = direction;
        this.width = width;
        this.color = color;
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStrokeWidth(width);
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        paint.setStrokeWidth(width);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if(direction== LinearLayoutManager.HORIZONTAL){
            drawHorizontalDivider(c,parent);
        }else{
            drawVerticalDivider(c,parent);
        }
    }


    private void drawVerticalDivider(Canvas c, RecyclerView parent){
        int left=parent.getLeft()-parent.getPaddingLeft();
        int right=parent.getRight()-parent.getPaddingRight();

        for(int i=0;i<parent.getChildCount();i++){
            View child=parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int top=parent.getChildAt(i).getBottom()+params.bottomMargin;

            c.drawLine(left,top,right,top,paint);
        }


    }


    private void drawHorizontalDivider(Canvas c, RecyclerView parent){

        int top=parent.getTop()-parent.getPaddingTop();
        int bottom=parent.getBottom()-parent.getPaddingBottom();

        for(int i=0;i<parent.getChildCount();i++){
            View child=parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left=parent.getChildAt(i).getRight()+params.rightMargin;

            c.drawLine(left,top,left,bottom,paint);
        }


    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(direction==LinearLayoutManager.HORIZONTAL){
            outRect.set(0,0,width,0);
        }else{
            outRect.set(0,0,0,width);
        }
    }
}
