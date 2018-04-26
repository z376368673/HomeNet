package com.benkie.hjw.ui.product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.HomeProductAdapter;
import com.benkie.hjw.bean.Category;
import com.benkie.hjw.bean.Channel;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.bean.PopBean;
import com.benkie.hjw.bean.TypeBean;
import com.benkie.hjw.constants.Constants;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.popwindow.GridVIewPopWindow;
import com.benkie.hjw.popwindow.List2PopWindow;
import com.benkie.hjw.popwindow.ListPopWindow;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.CheckTextView2;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocatedCity;
//import com.zaaach.citypicker.CityPickerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by 37636 on 2018/1/18.
 */

public class SearchResultActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.ed_search)
    TextView ed_search;
    @BindView(R.id.pullRefreshView)
    PullToRefreshGridView pullRefreshView;
    GridView gridView;

    @BindView(R.id.ct_1)
    CheckTextView2 ct_1;
    @BindView(R.id.ct_2)
    CheckTextView2 ct_2;
    @BindView(R.id.ct_3)
    CheckTextView2 ct_3;

    HomeProductAdapter productAdapter;


    private int categoryId = 0;
    private int serviceId = 0;
    private String city ="";
    private String name ="";
    private String starData = "";
    private String endData = "";
    private String timestamp = String.valueOf(System.currentTimeMillis());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        initView();
        getAllType();
        getAllData();

    }

    private void initView() {
        name = getIntent().getStringExtra("KEY");
        ed_search.setText(name);
        ed_search.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        pullRefreshView.setMode(PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY);
        pullRefreshView.setOnRefreshListener(this);
        gridView = pullRefreshView.getRefreshableView();
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(20);
        gridView.setVerticalSpacing(20);
        productAdapter = new HomeProductAdapter(this);
        gridView.setAdapter(productAdapter);
        gridView.setOnItemClickListener(this);
        ct_1.setTitle("全部区域");
        ct_2.setTitle("项目分类");
        ct_3.setTitle("服务内容");
        ct_1.setOnClickListener(this);
        ct_2.setOnClickListener(this);
        ct_3.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ed_search:
                finish();
                break;
            case R.id.ct_1:
                showChoiceCity();
                break;
            case R.id.ct_2:
                ct_1.setChecked(false);
                ct_2.setChecked(true);
                ct_3.setChecked(false);
                showType();
                break;
            case R.id.ct_3:
                ct_1.setChecked(false);
                ct_2.setChecked(false);
                ct_3.setChecked(true);
                showChoiceService();
                break;
        }
    }
    private void showChoiceCity() {
        List<HotCity> hotCities = new ArrayList<>();
        hotCities.add(new HotCity("全国", "全国", "000000000"));
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));
        CityPicker.getInstance()
                .setFragmentManager(getSupportFragmentManager())
                .enableAnimation(true)
                .setLocatedCity(new LocatedCity( Constants.city, Constants.province,""))
                .setHotCities(hotCities)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        if (data==null)return;
                        city = data.getName();
                        ct_1.setTitle(city);
                        if (city.equals("全国"))
                            city="";
                        getAllData();
                    }

                    @Override
                    public void onLocate() {
                        Log.e("onLocate","onLocate");
                    }
                })
                .show();
    }

    private void showChoiceService() {
        getAllServiceList();

    }

    /**
     * 选择项目分类
     */
    GridVIewPopWindow popWindowType;
    private void showType() {
        if (popWindowType!=null)
            popWindowType.showPopupWindow(ct_2);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HomeProductBean productBean  = (HomeProductBean) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pid", productBean.getItemId());
        bundle.putInt("FormType", 0);
        bundle.putString("Name", productBean.getName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getAllType() {
        Call call = Http.links.allProductType();
        Http.http.call(mActivity,call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<Category> otherList = JSON.parseArray(jsObj.getString("data"), Category.class);
                    otherList.add(0,new Category(0,"全部"));
                    popWindowType = new GridVIewPopWindow(mActivity);
                    popWindowType.setGravity(Gravity.CENTER);
                    popWindowType.setData(otherList);
                    popWindowType.setPopWindowCheckedListener(new GridVIewPopWindow.PopWindowCheckedListener() {
                        @Override
                        public void onPopWindowCheckedListener(PopBean popBean) {
                            ct_2.setTitle(popBean.getName());
                            categoryId = popBean.getId();
                            getAllData();
                        }
                    });
                    popWindowType.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            ct_2.setChecked(false);
                        }
                    });
                } else {
                    onFail("获取数据失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }
    /**
     * 获取服务类型
     */
    private void getAllServiceList() {
        Call call = Http.links.allserviceList(categoryId,city,name);
        Http.http.call(mActivity,call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<TypeBean>  beanList = JSON.parseArray(jsObj.getString("data"), TypeBean.class);
                    if (beanList != null){

                        /*** 添加全部选项 ***/
                        TypeBean typeBean = new TypeBean(0, "全部");
                        List<TypeBean.Tclass> tclasses = new ArrayList<>();
                        tclasses.add(0, new TypeBean.Tclass(0, "全部", ""));
                        typeBean.setTcList(tclasses);
                        beanList.add(0, typeBean);

                        List2PopWindow popWindowService = new List2PopWindow(mActivity);
                        popWindowService.setData(beanList);
                        popWindowService.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                ct_3.setChecked(false);
                            }
                        });
                        popWindowService.setPopWindowCheckedListener(new List2PopWindow.PopWindowCheckedListener() {
                            @Override
                            public void onPopWindowCheckedListener(PopBean popBean) {
                                ct_3.setTitle(popBean.getName());
                                serviceId = popBean.getId();
                                getAllData();
                            }
                        });
                        popWindowService.showPopupWindow(ct_3);
                    }else  onFail("暂无数据");
                }else {
                    onFail("获取数据失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    /**
     * 获取所有得完成项目
     */
    private void getAllData() {
        Call call = Http.links.searchAllItemList(categoryId,city,name,starData,endData,serviceId,timestamp);
        Http.http.call(mActivity,call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<HomeProductBean> beanList = JSON.parseArray(jsObj.getString("data"), HomeProductBean.class);
                    if (beanList != null){
                        setProductData(beanList);
                    }else  onFail("暂无数据");
                }else {
                    onFail("获取数据失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    private void setProductData(List<HomeProductBean> list) {
        if (productAdapter != null) {
            productAdapter.clear();
            productAdapter.addAll(list);
            productAdapter.notifyDataSetChanged();
        }
    }
}
