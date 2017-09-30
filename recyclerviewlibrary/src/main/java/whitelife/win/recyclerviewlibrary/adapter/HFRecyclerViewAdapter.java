package whitelife.win.recyclerviewlibrary.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuzefeng on 16/8/10.
 */

public class HFRecyclerViewAdapter extends RecyclerView.Adapter {

    protected RecyclerView recyclerView;

    protected List<View> headerViewList=new ArrayList<>();

    protected List<View> footerViewList=new ArrayList<>();

    protected RecyclerView.Adapter adapter;

    protected static final int HEADER_FOOTER_TYPE=10000;

    protected Context context;

    protected View.OnClickListener headerClickListener;

    protected View.OnClickListener footerClickListener;

    private List<Integer>headerIds=new ArrayList<>();

    private List<Integer>footerIds=new ArrayList<>();


    public HFRecyclerViewAdapter(Context context) {
        this.context=context;
    }

    public HFRecyclerViewAdapter addHeaderView(View view){
        if(view!=null&&headerViewList!=null){
            headerViewList.add(view);
            if(headerClickListener!=null){
                view.setOnClickListener(headerClickListener);
            }
        }
        return this;
    }


    public HFRecyclerViewAdapter addFooterView(View view){
        if(view!=null&&footerViewList!=null){
            footerViewList.add(view);
            if(footerClickListener!=null){
                view.setOnClickListener(footerClickListener);
            }
        }
        return this;
    }


    public void setInnerAdapter(RecyclerView.Adapter adapter){
        if(adapter!=null){
            this.adapter=adapter;
        }else{
            throw new NullPointerException("adapter 不能为空");
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView re) {
        super.onAttachedToRecyclerView(re);
        this.recyclerView=re;
        final RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
        if(layoutManager!=null&&layoutManager instanceof GridLayoutManager){
            ((GridLayoutManager)layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(adapter!=null) {
                        return isHeaderOrFooter(position)?((GridLayoutManager)layoutManager).getSpanCount():1;
                    }
                    return 1;
                }
            });
        }

        if(headerIds.size()>0){
            for(int id:headerIds){
                addHeaderView(LayoutInflater.from(context).inflate(id,recyclerView,false));
            }
        }
        if(footerIds.size()>0){
            for(int id:footerIds){
                addFooterView(LayoutInflater.from(context).inflate(id,recyclerView,false));
            }
        }
    }

    public HFRecyclerViewAdapter addHeaderView(@LayoutRes int id){
        if(recyclerView!=null){
            addHeaderView(LayoutInflater.from(context).inflate(id,recyclerView,false));
        }else{
            headerIds.add(id);
        }
        return this;
    }

    public HFRecyclerViewAdapter addFooterView(@LayoutRes int id){
        if(recyclerView!=null) {
            addFooterView(LayoutInflater.from(context).inflate(id, recyclerView, false));
        }else{
            footerIds.add(id);
        }
        return this;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType<HEADER_FOOTER_TYPE+headerViewList.size()&&viewType>=HEADER_FOOTER_TYPE){
            return new Holder(headerViewList.get(viewType-HEADER_FOOTER_TYPE));
        }else if(viewType>=headerViewList.size()+getInnerAdapterItemCount()){
            return new Holder(footerViewList.get(viewType-headerViewList.size()-getInnerAdapterItemCount()-HEADER_FOOTER_TYPE));
        }else{
            return adapter.onCreateViewHolder(parent,viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(!(holder instanceof Holder)&&adapter!=null){
            adapter.onBindViewHolder(holder,position-headerViewList.size());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position<headerViewList.size()||position>=headerViewList.size()+getInnerAdapterItemCount()){
            return HEADER_FOOTER_TYPE+position;
        }else{
            return adapter.getItemViewType(position-headerViewList.size());
        }
    }

    @Override
    public int getItemCount() {
        return adapter==null?0:headerViewList.size()+getInnerAdapterItemCount()+footerViewList.size();
    }



    private int getInnerAdapterItemCount(){
        return adapter==null?0:adapter.getItemCount();
    }


    private class Holder extends RecyclerView.ViewHolder{


        public Holder(View itemView) {
            super(itemView);
        }

    }


    public boolean isHeaderOrFooter(int position){
        return position<headerViewList.size()||position>=headerViewList.size()+getInnerAdapterItemCount();
    }


    public void setHeaderClickListener(View.OnClickListener headerClickListener) {
        this.headerClickListener = headerClickListener;
        for(View v:headerViewList){
            v.setOnClickListener(headerClickListener);
        }
    }

    public void setFooterClickListener(View.OnClickListener footerClickListener) {
        this.footerClickListener = footerClickListener;
        for(View v:footerViewList){
            v.setOnClickListener(footerClickListener);
        }
    }
}
