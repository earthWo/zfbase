package whitelife.win.recyclerviewlibrary.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by wuzefeng on 2017/9/28.
 */

public class LoadMoreAdapter extends HFRecyclerViewAdapter {

    private boolean hasLoadMore;

    private View loadMoreView;

    private boolean loading=false;

    private boolean loadMoreEnable=true;

    private LoadMoreListener loadMoreListener;

    private int loadMoreId;

    public LoadMoreAdapter(Context context) {
        super(context);
    }

    public LoadMoreAdapter(Context context,@LayoutRes int res) {
        super(context);
        setLoadMoreView(res);
    }

    public LoadMoreAdapter(Context context,View loadMoreView) {
        super(context);
        setLoadMoreView(loadMoreView);
    }

    public void setLoadMoreView(@LayoutRes int id){
        loadMoreId=id;
        if(recyclerView!=null){
            setLoadMoreView(LayoutInflater.from(context).inflate(id,recyclerView,false));
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView re) {
        super.onAttachedToRecyclerView(re);
        if(loadMoreId!=0&&loadMoreView==null){
            setLoadMoreView(LayoutInflater.from(context).inflate(loadMoreId,recyclerView,false));
        }
        re.addOnScrollListener(scrollListener);
    }

    public void setLoadMoreView(View view){
        loadMoreView=view;
        if(hasLoadMore){
            footerViewList.set(footerViewList.size(),loadMoreView);
        }else{
            footerViewList.add(loadMoreView);
        }
        hasLoadMore();
    }

    private void hasLoadMore(){
        if(loadMoreView!=null){
            hasLoadMore=true;
        }else{
            hasLoadMore=false;
        }
    }

    /**
     * 重写addFooterView方法，如果有loadmore时添加的footer在其之前
     * @param view
     */
    @Override
    public LoadMoreAdapter addFooterView(View view){
        if(view!=null&&footerViewList!=null){
            if(hasLoadMore){
                footerViewList.add(footerViewList.size()-1,view);
            }else{
                footerViewList.add(view);
            }
        }
        return this;
    }


    private RecyclerView.OnScrollListener scrollListener=new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if(!loading&&loadMoreEnable&&loadMoreListener!=null){
                RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
                if(layoutManager instanceof LinearLayoutManager){
                    //滚到最后面
                    if(((LinearLayoutManager)layoutManager).findLastVisibleItemPosition()==recyclerView.getAdapter().getItemCount()-1){
                        loadMoreListener.loadMore(loadMoreView);
                        loading=true;
                    }
                }
            }
        }
    };

    public boolean isLoadMoreEnable() {
        return loadMoreEnable;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
        if(!loadMoreEnable&&loadMoreListener!=null){
            loadMoreListener.loadMoreDisable(loadMoreView);
        }
    }

    public void loadMoreFinish(){
        loading=false;
       if(loadMoreListener!=null){
           loadMoreListener.loadMoreFinish(loadMoreView);
       }
    }


    public LoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public interface LoadMoreListener{

        void loadMore(View loadMore);

        void loadMoreFinish(View loadMore);

        void loadMoreDisable(View loadMore);

    }



}
