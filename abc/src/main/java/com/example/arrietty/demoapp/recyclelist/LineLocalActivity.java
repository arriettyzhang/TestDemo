package com.example.arrietty.demoapp.recyclelist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arrietty.demoapp.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by arrietty on 2017/12/6.
 */


public class LineLocalActivity extends AppCompatActivity {

    private static final String TAG = LineLocalActivity.class.getSimpleName();
    private static RecyclerView recyclerview;
    private CoordinatorLayout coordinatorLayout;
    private MyAdapter mAdapter;
    private ArrayList<BeanMode> arrList = new ArrayList<>();
    private int[] drawbleIdArr= {R.drawable.home_choose_genie, R.drawable.home_choose_genie_pro, R.drawable.home_choose_geniebt,
    R.drawable.home_choose_lumoscolor, R.drawable.home_choose_lumoscolor_add, R.drawable.home_choose_lumosstrip,
    R.drawable.home_choose_lumostunable, R.drawable.home_choose_lumoswhite, R.drawable.home_choose_plug,
    R.drawable.home_choose_robovac, R.drawable.home_choose_robovac_t2107, R.drawable.home_choose_smartplug,
    R.drawable.home_choose_smartplug_mini, R.drawable.home_choose_switch};
    //布局管理器
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem ;
    private int page=1;
    //拖住删除帮助类
    private ItemTouchHelper itemTouchHelper;
    private int screenwidth;
    //下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
        setListener();

        page =1;
        new GetData().execute("http://gank.io/api/data/福利/10/" + page);

        //获取屏幕宽度
        WindowManager wm = (WindowManager) LineLocalActivity.this
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenwidth =outMetrics.widthPixels;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arrList.clear();
    }

    private void getDate(){
        if(arrList ==null){
            arrList = new ArrayList<>();
        }
        for(int i =0;i<14;i++){
            BeanMode bm = new BeanMode();
            bm.setDrawableId(drawbleIdArr[i]);
            bm.setText("第"+ ((page - 1)*14 + i)+"张图");
            arrList.add(bm);
        }
    }

    private void initView(){
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.line_coordinatorLayout);

        recyclerview=(RecyclerView)findViewById(R.id.line_recycler);
        mLayoutManager=new LinearLayoutManager(this);

        recyclerview.setLayoutManager(mLayoutManager);

        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.line_swipe_refresh) ;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark,R.color.colorAccent);
        swipeRefreshLayout.setProgressViewOffset(false, 0,  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    private void setListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                page=1;
                Log.v(TAG, "swipeRefreshLayout onRefresh page " + page);
                arrList.clear();
                new GetData().execute("http://gank.io/api/data/福利/10/1");
            }
        });

        itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override//用于设置拖拽和滑动的方向
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                Log.v(TAG,"getMovementFlags");
                int dragFlags=0,swipeFlags=0;
                if(recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
                    dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
                }else if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
                    dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN;
                    //设置侧滑方向为从左到右和从右到左都可以
                    swipeFlags = ItemTouchHelper.START|ItemTouchHelper.END;
                }
                return makeMovementFlags(dragFlags,swipeFlags);
            }
            //长摁item拖拽时会回调这个方法
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Log.v(TAG," 长摁item拖拽时会回调这个方法 onMove");
                int from=viewHolder.getAdapterPosition();
                int to=target.getAdapterPosition();
                Collections.swap(arrList,from,to);
                mAdapter.notifyItemMoved(from,to);
                return true;
            }
            //这里处理滑动删除
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.v(TAG," 这里处理滑动删除 onSwiped");
                mAdapter.removeItem(viewHolder.getAdapterPosition());

            }
            //当item拖拽开始时调用
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                Log.v(TAG," 当item拖拽开始时调用 onSelectedChanged  actionState " +actionState);
                Log.v(TAG, "viewHolder " + viewHolder);
                if(viewHolder!=null){
                    Log.v(TAG, "viewHolder.itemView =  " + viewHolder.itemView);
                }
                //横向
                if(viewHolder!=null && viewHolder.itemView!=null && actionState==ItemTouchHelper.ACTION_STATE_SWIPE){
                    viewHolder.itemView.setBackgroundColor(0xffff0000);//Color.LTGRAY
                }

                //长按拖拽
                if(actionState==ItemTouchHelper.ACTION_STATE_DRAG){
                    Log.v(TAG," 当item拖拽开始时调用 onSelectedChanged  11111 state=  drag");
                    viewHolder.itemView.setBackgroundColor(0xff00ff00);//Color.LTGRAY
                }
            }
            //当item拖拽完成时调用
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                Log.v(TAG," 当item拖拽完成时调用 clearView  ");
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
            }
            //当item视图变化时调用
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                viewHolder.itemView.setAlpha(1- Math.abs(dX)/screenwidth);
                Log.v(TAG," 当item视图变化时调用  onChildDraw");

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        //recyclerview滚动监听
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.v(TAG," 滚动监听  onScrollStateChanged  newState " + newState);
//                0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                //               滑动状态停止并且剩余两个item时自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem +2>=mLayoutManager.getItemCount()) {
                    new GetData().execute("http://gank.io/api/data/福利/10/"+(++page));
                    Log.v(TAG," 滚动监听  onScrollStateChanged  status = 00000000000000 ");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //                获取加载的最后一个可见视图在适配器的位置。
                Log.v(TAG," onScrolled  lastVisibleItem =" +lastVisibleItem);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            }

        });
    }


    private class GetData extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v(TAG, "GetData onPreExecute");

            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            Log.v(TAG, "GetData doInBackground params[0]" +params[0]);
            return MyOkhttp.get(params[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v(TAG, "GetData onPostExecute  result " +result);
            if(!TextUtils.isEmpty(result)){

                JSONObject jsonObject;
                Gson gson=new Gson();
                String jsonData=null;

                try {
                    jsonObject = new JSONObject(result);
                    jsonData = jsonObject.getString("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                if(meizis==null||meizis.size()==0){
//                    meizis= gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {}.getType());
//                    Meizi pages=new Meizi();
//                    pages.setPage(page);
//                    meizis.add(pages);
//                }else{
//                    List<Meizi> more= gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {}.getType());
//                    meizis.addAll(more);
//                    Meizi pages=new Meizi();
//                    pages.setPage(page);
//                    meizis.add(pages);
//                }


                if(mAdapter==null){
                    recyclerview.setAdapter(mAdapter = new MyAdapter());
                    itemTouchHelper.attachToRecyclerView(recyclerview);
                }else{
                    mAdapter.notifyDataSetChanged();
                }
            }else{
                getDate();
                if(mAdapter==null){
                    recyclerview.setAdapter(mAdapter = new MyAdapter());
                    itemTouchHelper.attachToRecyclerView(recyclerview);
                }else{
                    mAdapter.notifyDataSetChanged();
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener {



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(
                    LineLocalActivity.this).inflate(R.layout.line_meizi_item, parent,
                    false);
            MyViewHolder holder = new MyViewHolder(view);

            view.setOnClickListener(this);

            return holder;

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.tv.setText(arrList.get(position).getText());
            holder.iv.setImageResource(arrList.get(position).getDrawableId());

        }

        @Override
        public int getItemCount()
        {
            return arrList.size();
        }


        @Override
        public void onClick(View v) {
            //点击事件的回调可以在这里添加

            int position=recyclerview.getChildAdapterPosition(v);
            SnackbarUtil.ShortSnackbar(coordinatorLayout,"点击第"+position+"个",SnackbarUtil.Info).show();
        }


        class MyViewHolder extends RecyclerView.ViewHolder
        {
            private ImageView iv;
            private TextView tv;
            public MyViewHolder(View view)
            {
                super(view);
                iv = (ImageView) view.findViewById(R.id.line_item_iv);
                tv=(TextView) view.findViewById(R.id.line_item_tv);
            }
        }

        public void addItem(BeanMode bm, int position) {
            arrList.add(position, bm);
            notifyItemInserted(position);
            recyclerview.scrollToPosition(position);
        }

        public void removeItem(final int position) {
            final BeanMode removed=arrList.get(position);
            arrList.remove(position);
            notifyItemRemoved(position);
            SnackbarUtil.ShortSnackbar(coordinatorLayout,"你删除了第"+position+"个item",SnackbarUtil.Warning).setAction("撤销", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItem(removed,position);
                    SnackbarUtil.ShortSnackbar(coordinatorLayout,"撤销了删除第"+position+"个item",SnackbarUtil.Confirm).show();
                }
            }).setActionTextColor(Color.WHITE).show();
        }

    }

}

