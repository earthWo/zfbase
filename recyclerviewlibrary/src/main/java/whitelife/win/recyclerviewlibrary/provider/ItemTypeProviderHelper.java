package whitelife.win.recyclerviewlibrary.provider;

import java.util.ArrayList;
import java.util.List;

import whitelife.win.recyclerviewlibrary.adapter.MultiRecyclerViewHolder;

/**
 * 多种item type绑定类
 * Created by wuzefeng on 2017/9/28.
 */

public class ItemTypeProviderHelper<T> {

    private List<ItemTypeProvider<T>> itemTypeArray=new ArrayList<>();


    /**
     * 添加provider
     * @param provider
     * @return
     */
    public void addItemType(ItemTypeProvider<T> provider){
        itemTypeArray.add(provider);
    }

    public int getItemType(T t,int position){
        for(int i=0;i<itemTypeArray.size();i++){
            if(itemTypeArray.get(i).isItemType(t,position)){
                return i;
            }
        }
        return -1;
    }


    public int getItemLayoutRes(int type){
        if(itemTypeArray.size()>type){
            return itemTypeArray.get(type).getItemLayoutRes();
        }
        return 0;
    }


    public int getProviderCount(){
        return itemTypeArray.size();
    }

    public int bindViewHolder(T t, int position, MultiRecyclerViewHolder viewHolder){
        int type=getItemType(t,position);
        if(type!=-1){
            if(itemTypeArray.size()>type){
                itemTypeArray.get(type).bindViewHolder(t,position,viewHolder);
            }

        }
        return type;
    }




}
