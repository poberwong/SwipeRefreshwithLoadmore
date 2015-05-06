package com.bob.loadmore;

import com.bob.loadmore.widgets.LoadMoreListView;
import com.bob.loadmore.widgets.LoadMoreListView.OnLoadMoreListener;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * 分析开源项目
 */
public class MainActivity extends Activity implements OnRefreshListener, OnLoadMoreListener {

    private SwipeRefreshLayout swipeLayout;  //系统带的下拉刷新控件
    private LoadMoreListView listView;  //具有上拉加载的ListView
    private MyAdapter adapter; //数据适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);//下拉组件的事件监听

        // set style for swipeRefreshLayout
        swipeLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_blue_bright, android.R.color.holo_orange_light);
        listView = (LoadMoreListView) this.findViewById(R.id.listview);
        listView.setOnLoadMoreListener(this);//为listView添加加载监听
        adapter = new MyAdapter();
        listView.setAdapter(adapter);//设置数据适配器
    }

    @Override
    public void onRefresh() {//下拉监听
        new AsyncTask<Void, Void, Void>() {//创建一个异步任务

            @Override
            protected Void doInBackground(Void... params) {//在子线程中的任务逻辑处理功能
                try {
                    Thread.sleep(3 * 1000); //sleep 3 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {//任务处理结束后的UI更新方法，此时运行在主线程中
                adapter.count = 15;
                adapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
                listView.setCanLoadMore(adapter.count < 45);
                super.onPostExecute(result);
            }

        }.execute();
    }

    @Override
    public void onLoadMore() {//上拉监听

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3 * 1000);    //sleep 3 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                adapter.count += 15;
                adapter.notifyDataSetChanged();
                listView.setCanLoadMore(adapter.count < 45);
                listView.onLoadMoreComplete();
                super.onPostExecute(result);
            }

        }.execute();
    }


    private class MyAdapter extends BaseAdapter {
        public int count = 5;

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;//灵感来自《第一行代码》
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_1, null);
                holder.textV = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textV.setText("This is " + (position + 1) + " line.");
            return convertView;
        }

        private class ViewHolder {
            TextView textV;
        }

    }

}
