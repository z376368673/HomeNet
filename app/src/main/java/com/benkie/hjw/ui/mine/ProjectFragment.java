package com.benkie.hjw.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.CategoryAdapter;
import com.benkie.hjw.adapter.CollectionProductAdapter;
import com.benkie.hjw.bean.Category;
import com.benkie.hjw.bean.Channel;
import com.benkie.hjw.bean.CollectProductBean;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.bean.PopBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.fragment.BaseFragment;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.popwindow.GridVIewPopWindow;
import com.benkie.hjw.ui.product.ProductDetailsActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HorizontalListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by 37636 on 2018/1/18.
 */

public class ProjectFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.iv_no)
    ImageView iv_no;

    @BindView(R.id.horizontalListView)
    HorizontalListView horizontalListView;

    @BindView(R.id.pullRefreshView)
    PullToRefreshGridView pullRefreshView;
    GridView gridView;
    CollectionProductAdapter adapter;
    CollectProductBean productBean;
    Fragment fragment;
    CategoryAdapter categoryAdapter;
    Category category;
    List<Category> cList;
    int pageIndex = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = View.inflate(getActivity(),R.layout.fragment_home3,null);
        View view = inflater.inflate(R.layout.fragment_project_collection, container, false);
        ButterKnife.bind(this, view);
        fragment = this;
        initView();
        getCollectionType();
       // getCategoryList();
        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }
    private  List<Category> getAlltype(){
        List<Category> categoryList = new ArrayList<>();
        categoryList.addAll(cList);
        return categoryList;
    }

    public void initView() {
        cList = new ArrayList<>();
        pullRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        pullRefreshView.setOnRefreshListener(this);
        gridView = pullRefreshView.getRefreshableView();
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(20);
        gridView.setVerticalSpacing(20);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemClickListener(this);
        gridView.setPadding(0, 0, 00, 0);
        adapter = new CollectionProductAdapter(mActivity) {
            @Override
            public void delCollection(CollectProductBean item) {
                delColle(item);
            }
        };
        gridView.setAdapter(adapter);
        categoryAdapter = new CategoryAdapter(mActivity);
        horizontalListView.setOnItemClickListener(horItemClickListener);
        horizontalListView.setAdapter(categoryAdapter);
        horizontalListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               Category category = (Category) adapterView.getAdapter().getItem(i);
                categoryAdapter.setCheck(category);
                geteItemCollection(category.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void getCategoryList() {
        List<Category> categoryList =getAlltype();
        categoryAdapter.clear();
        categoryAdapter.addAll(categoryList);
        category = categoryList.get(0);
        categoryAdapter.setCheck(category);
        geteItemCollection(category.getId());
    }
    /**
     * 导航菜单点击
     */
    AdapterView.OnItemClickListener horItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Category category = (Category) adapterView.getAdapter().getItem(i);
            horizontalListView.setSelection(i);
            geteItemCollection( category.getId());
        }
    };
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        productBean = (CollectProductBean) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pid", productBean.getUid());
        bundle.putString("Name",productBean.getName());
        bundle.putInt("FormType", 0);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (category!=null)
            geteItemCollection(category.getId());
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pullRefreshView.onRefreshComplete();
    }

    /**
     * 取消收藏
     *
     * @param productBean
     */
    private void delColle(final CollectProductBean productBean) {
        Call call = Http.links.itemCollect(DataHpler.getUserInfo().getUserid(),productBean.getUid(),0);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    adapter.remove(productBean);
                    adapter.notifyDataSetChanged();
                } else {
                    onFail("删除失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    /**
     * 收藏项目分类
     */
    private void getCollectionType(){
        Call call = Http.links.itemCollectionType(DataHpler.getUserInfo().getUserid());
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<Category> beanList = JSON.parseArray(jsObj.getString("type"), Category.class);
                    if (beanList != null&&beanList.size()>0) {
                        pullRefreshView.setVisibility(View.VISIBLE);
                        horizontalListView.setVisibility(View.VISIBLE);
                        iv_no.setVisibility(View.GONE);
                        cList =beanList;
                        getCategoryList();
                    }else {
                        pullRefreshView.setVisibility(View.GONE);
                        horizontalListView.setVisibility(View.GONE);
                        iv_no.setVisibility(View.VISIBLE);
                    }
                } else {
                    onFail("获取数据失败");
                }
                pullRefreshView.onRefreshComplete();
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
                pullRefreshView.onRefreshComplete();
            }
        });
    }

    private void geteItemCollection(int categoryId) {
        Call call = Http.links.myItemCollection(DataHpler.getUserInfo().getUserid(),
                categoryId,pageIndex);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<CollectProductBean> beanList = JSON.parseArray(jsObj.getString("item"), CollectProductBean.class);
                    if (beanList != null&&beanList.size()>0) {
                        pullRefreshView.setVisibility(View.VISIBLE);
                        iv_no.setVisibility(View.GONE);
                        adapter.clear();
                        adapter.addAll(beanList);
                        adapter.notifyDataSetChanged();
                    }else {
                        pullRefreshView.setVisibility(View.GONE);
                        iv_no.setVisibility(View.VISIBLE);
                    }
                } else {
                    onFail("获取数据失败");
                }
                pullRefreshView.onRefreshComplete();
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
                pullRefreshView.onRefreshComplete();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (category!=null)
            geteItemCollection(category.getId());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && data != null&&requestCode == 1000) {
//            //添加项目文件返回 刷新界面
//        }
//        else if (resultCode == Activity.RESULT_OK && data != null&&requestCode == 1001){
//            geteData();
//        }else if (resultCode == Activity.RESULT_OK && data != null&&requestCode == 1002){
//            geteData();
//        }
    }

}
