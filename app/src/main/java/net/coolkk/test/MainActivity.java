package net.coolkk.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseDraggableModule;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class  MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //定义RecyclerView
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //创建数据
        List<MainAdapterData> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MainAdapterData mainAdapterData = new MainAdapterData();
            mainAdapterData.setTitle(String.valueOf(i));
            mainAdapterData.setInfo(i + "s");
            list.add(mainAdapterData);
        }
        //创建适配器
        final MainAdapter adapter = new MainAdapter(R.layout.recyclerview_main, list);
        //创建适配器.动画
        adapter.setAnimationEnable(true);//打开动画
        adapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInLeft);//设置动画类型
        adapter.setAnimationFirstOnly(false);//设置始终显示动画
        //创建适配器.头部
        //adapter.setHeaderWithEmptyEnable(true);//同时显示头部与空布局
        adapter.setHeaderView(View.inflate(this, R.layout.recyclerview_header, null));//Adapter的头部
        //创建适配器.组件事件
        adapter.setOnItemClickListener(new OnItemClickListener() {//监听条目的点击事件
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(MainActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {//监听条目的长按事件
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(MainActivity.this, "onItemLongClick" + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //创建适配器.子组件事件
        adapter.addChildClickViewIds(R.id.button);//注册条目子组件的点击事件
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {//监听条目子组件的点击事件
                if (view.getId() == R.id.button) {
                    Toast.makeText(MainActivity.this, "onItemChildClick" + position, Toast.LENGTH_SHORT).show();
                    //获取其他子控件，如有设置头部则position应加头部布局数量
                    TextView textView = (TextView) adapter.getViewByPosition(position + 1, R.id.textView);
                    Objects.requireNonNull(textView).setText("Clicked Me");
                }
            }
        });
        adapter.addChildLongClickViewIds(R.id.button);//注册条目子组件的长按事件
        adapter.setOnItemChildLongClickListener(new OnItemChildLongClickListener() {//监听条目子组件的长按事件
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.button) {
                    Toast.makeText(MainActivity.this, "onItemChildLongClick" + position, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        //创建适配器.上拉加载
        BaseLoadMoreModule loadMore = adapter.getLoadMoreModule();
        assert loadMore != null;
        loadMore.setEnableLoadMore(true);//打开上拉加载
        loadMore.setAutoLoadMore(true);//自动加载
        loadMore.setPreLoadNumber(1);//设置滑动到倒数第几个条目时自动加载，默认为1
        loadMore.setEnableLoadMoreIfNotFullPage(true);//当数据不满一页时继续自动加载
        //loadMore.setLoadMoreView(new BaseLoadMoreView)//设置自定义加载布局
        loadMore.setOnLoadMoreListener(new OnLoadMoreListener() {//设置加载更多监听事件
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {//延迟以提升用户体验
                    @Override
                    public void run() {
                        List<MainAdapterData> lists = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            MainAdapterData mainAdapterData = new MainAdapterData();
                            mainAdapterData.setTitle(String.valueOf(i));
                            mainAdapterData.setInfo(i + "s");
                            lists.add(mainAdapterData);
                        }
                        adapter.addData(lists);
                        adapter.getLoadMoreModule().loadMoreComplete();
                        //adapter.loadMoreEnd()//加载完毕
                        //adapter.loadMoreFail()//加载失败
                    }
                }, 500);
            }
        });
        //创建适配器.侧滑删除
        BaseDraggableModule draggableModule = adapter.getDraggableModule();
        assert draggableModule != null;
        draggableModule.setSwipeEnabled(true);//启动侧滑删除
        draggableModule.getItemTouchHelperCallback().setSwipeMoveFlags(ItemTouchHelper.START);//侧滑删除方向
        draggableModule.setOnItemSwipeListener(new OnItemSwipeListener() {//侧滑删除监听
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
            }
            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            }
            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Toast.makeText(MainActivity.this, "onItemSwipe" + viewHolder.getLayoutPosition(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {//滑动时的背景
                canvas.drawColor(Color.parseColor("#00DDB6"));
            }
        });
        //创建适配器.拖拽
        draggableModule.setDragEnabled(true);//启动拖拽
        draggableModule.getItemTouchHelperCallback().setDragMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);//拖拽方向
        draggableModule.setOnItemDragListener(new OnItemDragListener() {//拖拽监听
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Toast.makeText(MainActivity.this, "onItemDragBefore" + viewHolder.getLayoutPosition(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
            }
            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                Toast.makeText(MainActivity.this, "onItemDragAfter" + viewHolder.getLayoutPosition(), Toast.LENGTH_SHORT).show();
            }
        });
        //设置RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //创建适配器.空布局
        //在setAdapter后使用 且 new MainAdapter第二个参数为null
        adapter.setEmptyView(R.layout.recyclerview_empty);
    }
}
