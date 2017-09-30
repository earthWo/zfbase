package whitelife.win.recyclerviewlibrary.provider;

import whitelife.win.recyclerviewlibrary.adapter.MultiRecyclerViewHolder;

/**
 * 多种item type回调接口
 * Created by wuzefeng on 2017/9/28.
 */

public interface ItemTypeProvider<T> {

    /**
     * 获取item的type
     * @param t
     * @param position
     * @return
     */
    boolean isItemType(T t,int position);


    int getItemLayoutRes();


    void bindViewHolder(T t, int position, MultiRecyclerViewHolder viewHolder);

}
