package whitelife.win.recyclerviewlibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import whitelife.win.recyclerviewlibrary.callback.SwipeAdapterInterface;
import whitelife.win.recyclerviewlibrary.provider.ItemTypeProvider;
import whitelife.win.recyclerviewlibrary.provider.ItemTypeProviderHelper;

/**
 * 自定义RecyclerView adapter，实现多种item type，实现点击事件和长按事件
 *
 * 如果不适用多种item type，请重写getItemLayoutRes()、bindViewHolder()方法
 * 如果View中的child需要绑定相应的事件，请重写bindItemView方法。
 * 可以重写dataEquals方法，自定义data的equal条件
 *
 *
 * Created by wuzefeng on 2017/9/28.
 */

public class MultiRecyclerViewAdapter<T> extends RecyclerView.Adapter<MultiRecyclerViewHolder> implements SwipeAdapterInterface{


    private List<T> mDataList;

    private ItemTypeProviderHelper<T>itemTypeProviderHelper;

    private Context context;

    private OnItemClickListener clickListener;

    private OnItemLongClickListener longClickListener;

    private DataUpdateListener dataUpdateListener;


    public MultiRecyclerViewAdapter(Context context,List<T> mDataList) {
        this.mDataList = mDataList;
        this.context=context;
        this.itemTypeProviderHelper=new ItemTypeProviderHelper<>();
    }

    @Override
    public MultiRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int res;
        if(viewType==-1){
            res=getItemLayoutRes();
        }else{
            res=itemTypeProviderHelper.getItemLayoutRes(viewType);
        }

        MultiRecyclerViewHolder viewHolder=new MultiRecyclerViewHolder(MultiRecyclerViewHolder.inflateView(context,res,parent));
        bindItemView(viewHolder.getRootView(),viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MultiRecyclerViewHolder holder, int position) {
        bindViews(position,holder);
        if(itemTypeProviderHelper.bindViewHolder(mDataList.get(position),position,holder)==-1){
            bindViewHolder(mDataList.get(position),position,holder);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(itemTypeProviderHelper.getProviderCount()>0){
            return itemTypeProviderHelper.getItemType(mDataList.get(position),position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mDataList!=null?mDataList.size():0;
    }


    /**
     * 如果没有添加ItemTypeProvider，需要重写该方法
     * @param t
     * @param position
     * @param holder
     */
    public void bindViewHolder(T t,int position,MultiRecyclerViewHolder holder){}

    /**
     * 如果item没有多种情况，必须重写该方法
     * @return
     */
    public int getItemLayoutRes(){
        return 0;
    }



    /**
     * 设置数据
     * @param dataList
     */
    public void setDataList(List<T>dataList){
        this.mDataList=dataList;
        notifyDataSetChanged();
    }


    /**
     * 添加数据
     * @param dataList
     */
    public void addDataList(List<T>dataList){
        if( this.mDataList!=null){
            this.mDataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public MultiRecyclerViewAdapter<T> addItemType(ItemTypeProvider<T> provider){
        itemTypeProviderHelper.addItemType(provider);
        return this;
    }


    /**
     * 使用equals直接比较，可以重写自定义
     * @param t
     * @param r
     * @return
     */
    public boolean dataEquals(T t,T r){
        return t.equals(r);
    }


    public void bindItemView(View view,int type){}

    /**
     * 添加数据
     * @param data
     */
    public void addDataList(T data){
        if( this.mDataList!=null){
            this.mDataList.add(data);
            notifyDataSetChanged();
        }
    }


    /**
     * 更新数据
     * @param data
     */
    public void updateData(T data){
        if(data!=null&&mDataList!=null){
            for(int i=0;i<mDataList.size();i++){
                if(data.equals(mDataList.get(i))){
                    mDataList.set(i,data);
                    notifyItemChanged(i);
                }
            }
        }
    }

    private void bindViews(final int position, final MultiRecyclerViewHolder viewHolder){
        viewHolder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener!=null){
                    clickListener.itemClick(position, viewHolder.getRootView(),viewHolder);
                }
            }
        });

        viewHolder.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(longClickListener!=null){
                    longClickListener.itemLongClick(position, viewHolder.getRootView(),viewHolder);
                }
                return false;
            }
        });
    }


    /**
     * 更新数据
     * @param dataList
     */
    public void updateData(List<T> dataList){
        if(dataList!=null&&mDataList!=null){
            for(T data:dataList){
                for(int i=0;i<mDataList.size();i++){
                    if(data.equals(mDataList.get(i))){
                        mDataList.set(i,data);
                        notifyItemChanged(i);
                    }
                }
            }
        }
    }

    /**
     * 更新数据
     * @param position
     */
    public void updateData(int position){
        if(mDataList!=null&&mDataList.size()>position){
            notifyItemChanged(position);
        }
    }

    /**
     * 删除数据
     * @param position
     */
    public void deleteData(int position){
        if(mDataList!=null&&mDataList.size()>position){
            mDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 删除数据
     * @param data
     */
    public void deleteData(T data){
        if(data!=null&&mDataList!=null){
            for(int i=0;i<mDataList.size();i++){
                if(dataEquals(data,mDataList.get(i))){
                    mDataList.remove(i);
                    notifyItemRemoved(i);
                }
            }
        }
    }

    /**
     * 删除数据
     * @param dataList
     */
    public void deleteData(List<T> dataList){
        if(dataList!=null&&mDataList!=null){
            Stack<Integer>stack=new Stack<>();
            for(T data:dataList){
                for(int i=0;i<mDataList.size();i++){
                    if(dataEquals(data,mDataList.get(i))){
                        stack.add(i);
                    }
                }
            }

            while(stack.iterator().hasNext()){
                int position=stack.pop();
                mDataList.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public List<T> getmDataList() {
        return mDataList;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public OnItemClickListener getItemClickListener() {
        return clickListener;
    }

    public void setItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public OnItemLongClickListener getLongClickListener() {
        return longClickListener;
    }

    public void setLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public DataUpdateListener getDataUpdateListener() {
        return dataUpdateListener;
    }

    public void setDataUpdateListener(DataUpdateListener dataUpdateListener) {
        this.dataUpdateListener = dataUpdateListener;
    }

    /**
     * 清除所有的数据
     */
    public void clear(){
        if(mDataList!=null){
            mDataList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void moveData(int from, int to) {
        if(mDataList!=null&&mDataList.size()>from&&mDataList.size()>to){
            Collections.swap(mDataList, from, to);
            notifyItemMoved(from, to);
        }
    }

    @Override
    public void deleteDate(int position) {
        if(mDataList!=null&&mDataList.size()>position){
            mDataList.remove(position);
            notifyItemRemoved(position);
        }
    }


    /**
     * 点击事件
     */
    public interface OnItemClickListener{

        void itemClick(int position,View view,MultiRecyclerViewHolder holder);

    }

    /**
     * 长按事件
     */
    public interface OnItemLongClickListener{

        void itemLongClick(int position,View view,MultiRecyclerViewHolder holder);

    }

    public interface DataUpdateListener{

        void dataDelete(int position);

        void dataSwipe(int from,int to);
    }


}
