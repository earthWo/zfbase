package whitelife.win.recyclerviewlibrary.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by wuzefeng on 2017/8/18.
 */

public class ItemTouchCallback extends ItemTouchHelper.Callback {

    private SwipeAdapterInterface adapter;

    public ItemTouchCallback(SwipeAdapterInterface adapter) {
        this.adapter=adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags= ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags= ItemTouchHelper.LEFT;
        return  makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.moveData(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.deleteDate(viewHolder.getAdapterPosition());
    }
}
