package whitelife.win.testproject;

import android.Manifest;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
import whitelife.win.library.database.RealmHelper;
import whitelife.win.library.database.RealmInterface;
import whitelife.win.library.util.LogUtils;
import whitelife.win.permissionlibrary.PermissionCallback;
import whitelife.win.permissionlibrary.PermissionManager;
import whitelife.win.recyclerviewlibrary.adapter.HFRecyclerViewAdapter;
import whitelife.win.recyclerviewlibrary.adapter.LoadMoreAdapter;
import whitelife.win.recyclerviewlibrary.adapter.MultiRecyclerViewAdapter;
import whitelife.win.recyclerviewlibrary.adapter.MultiRecyclerViewHolder;
import whitelife.win.recyclerviewlibrary.callback.ItemTouchCallback;
import whitelife.win.recyclerviewlibrary.itemDecoration.DividerItemDecoration;
import whitelife.win.recyclerviewlibrary.provider.ItemTypeProvider;
import whitelife.win.testproject.bean.User;


public class MainActivity extends AppCompatActivity {

    List<String>dataList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testSwipe();
    }


    private void testSwipe(){
        for(int i=0;i<300;i++){
            dataList.add("第"+i+"个");
        }
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.sr_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final MultiRecyclerViewAdapter<String>innerAdapter=new MultiRecyclerViewAdapter<>(this,dataList) ;
        innerAdapter.addItemType(provider1);
        HFRecyclerViewAdapter adapter=new HFRecyclerViewAdapter(this);
        adapter.addHeaderView(R.layout.item_header).addFooterView(R.layout.item_footer);
        adapter.setInnerAdapter(innerAdapter);
        recyclerView.setAdapter(innerAdapter);
        innerAdapter.setItemClickListener(new MultiRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position, View view, MultiRecyclerViewHolder holder) {
                Toast.makeText(MainActivity.this,""+position,Toast.LENGTH_SHORT).show();
            }
        });
        ItemTouchHelper.Callback callback=new ItemTouchCallback(innerAdapter);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }


    private void testRecycler(){
        for(int i=0;i<3;i++){
            dataList.add("第"+i+"个");
        }

        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.sr_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final MultiRecyclerViewAdapter<String>innerAdapter=new MultiRecyclerViewAdapter<>(this,dataList) ;
        innerAdapter.addItemType(provider1).addItemType(provider2);
        recyclerView.addItemDecoration(new DividerItemDecoration(LinearLayoutManager.VERTICAL,10, Color.BLACK));

        final LoadMoreAdapter adapter=new LoadMoreAdapter(this);
        adapter.setLoadMoreView(R.layout.layout_load_more);
        adapter.setLoadMoreListener(new LoadMoreAdapter.LoadMoreListener() {
            @Override
            public void loadMore(View loadMore) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=1;i<31;i++){
                            dataList.add("第"+(dataList.size()+1)+"个");
                        }
                        innerAdapter.setDataList(dataList);
                        adapter.loadMoreFinish();
                        if(dataList.size()>200){
                            adapter.setLoadMoreEnable(false);
                        }
                        adapter.notifyDataSetChanged();
                    }
                },3000);
                ((TextView)loadMore.findViewById(R.id.tv_more)).setText("正在加载更多");
            }

            @Override
            public void loadMoreFinish(View loadMore) {
                ((TextView)loadMore.findViewById(R.id.tv_more)).setText("加载更多");
            }

            @Override
            public void loadMoreDisable(View loadMore) {
                ((TextView)loadMore.findViewById(R.id.tv_more)).setText("没有更多");
            }
        });

        adapter.setInnerAdapter(innerAdapter);
        recyclerView.setAdapter(adapter);
        adapter.addHeaderView(R.layout.item_header);
        adapter.addFooterView(R.layout.item_footer);
        innerAdapter.setItemClickListener(new MultiRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position, View view, MultiRecyclerViewHolder holder) {
                Log.d("点击",position+"");
            }
        });
        innerAdapter.setLongClickListener(new MultiRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void itemLongClick(int position, View view, MultiRecyclerViewHolder holder) {
                Log.d("长按",position+"");
            }
        });
    }



    private ItemTypeProvider<String>provider1=new ItemTypeProvider<String>() {
        @Override
        public boolean isItemType(String s, int position) {
            return true;
        }

        @Override
        public int getItemLayoutRes() {
            return R.layout.item_item;
        }

        @Override
        public void bindViewHolder(String s, int position, MultiRecyclerViewHolder viewHolder) {
            viewHolder.bindTextViewRes(R.id.tv_item,s);
        }
    };

    private ItemTypeProvider<String>provider2=new ItemTypeProvider<String>() {
        @Override
        public boolean isItemType(String s, int position) {
            return position>=3;
        }

        @Override
        public int getItemLayoutRes() {
            return R.layout.item_2;
        }

        @Override
        public void bindViewHolder(String s, int position, MultiRecyclerViewHolder viewHolder) {
            viewHolder.bindTextViewRes(R.id.tv_item,s);
        }
    };






    private void checkPermission(){
        PermissionManager.getInstance(this)
                .addPermission(Manifest.permission.CAMERA)
                .checkPermission(new PermissionCallback() {
                    @Override
                    public void granted(String permission) {
                        LogUtils.d("申请成功",permission);
                    }

                    @Override
                    public void denied(String permission) {
                        LogUtils.d("申请失败",permission);
                    }

                    @Override
                    public void cancel(String permission) {
                        LogUtils.d("申请取消",permission);
                    }

                    @Override
                    public void finish() {

                    }
                });
    }


    private void addTest(){
        User user=new User();
        user.setUid((int) (System.currentTimeMillis()&1000));
        user.setName("a11");

        User user1=new User();
        user1.setUid((int) (System.currentTimeMillis()&10000));
        user1.setName("a11");


        List<User>list=new ArrayList<>();
        list.add(user);
        list.add(user1);

        list.add(user);

        list.add(user1);

        new RealmHelper().create(RealmInterface.class).copyToRealm(list);
    }


    private void deleteTest(){
        RealmResults<User> users=Realm.getDefaultInstance().where(User.class).findAll();

        int count=users.size();


        new RealmHelper().create(RealmInterface.class).deleteFromRealm(users.get(0),users.get(1));

    }


    private void modify(){
        RealmResults<User> users=new RealmHelper().create(RealmInterface.class).
                findEquals(User.class,new String[]{"uid"},new Object[]{264});
        Log.d("用户数",users.get(0).getSex()+"");



        new RealmHelper().create(RealmInterface.class).modify(User.class,users.get(0),
                new String[]{"setSex"},new Class[]{String.class},new String[]{"男"});
        users=new RealmHelper().create(RealmInterface.class).
                findEquals(User.class,new String[]{"uid"},new Object[]{264});
        Log.d("用户数",users.get(0).getSex()+"");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void finish() {
        super.finish();



    }
}
