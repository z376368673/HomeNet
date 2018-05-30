package com.benkie.hjw.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.CategoryAdapter;
import com.benkie.hjw.adapter.HomeProductAdapter;
import com.benkie.hjw.bean.Category;
import com.benkie.hjw.bean.Channel;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.bean.PopBean;
import com.benkie.hjw.bean.TypeBean;
import com.benkie.hjw.constants.Constants;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.popwindow.List2PopWindow;
import com.benkie.hjw.popwindow.ListPopWindow;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.ui.product.CategoryActivity;
import com.benkie.hjw.ui.product.ProductDetailsActivity;
import com.benkie.hjw.ui.product.ProjectActivity;
import com.benkie.hjw.ui.product.SearchActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.CheckTextView2;
import com.benkie.hjw.view.HorizontalListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DoublePicker;
import retrofit2.Call;

/**
 * Created by 37636 on 2018/1/18.
 */

public class Home1Fragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.tv_search)
    TextView tv_search;
    @BindView(R.id.iv_feiji)
    TextView iv_feiji;
    @BindView(R.id.horizontalListView)
    HorizontalListView horizontalListView;
    @BindView(R.id.iv_add_category)
    ImageView iv_add_category;
    @BindView(R.id.pullRefreshView)
    PullToRefreshGridView pullRefreshView;
    GridView gridView;

    @BindView(R.id.ll_search)
    LinearLayout ll_search;

    @BindView(R.id.ct_1)
    CheckTextView2 ct_1;
    @BindView(R.id.ct_2)
    CheckTextView2 ct_2;
    @BindView(R.id.ct_3)
    CheckTextView2 ct_3;

    CategoryAdapter categoryAdapter;
    HomeProductAdapter productAdapter;
    private int categoryId = 0;
    private int serviceId = 0;
    private String city = "";
    private String name = "";
    private String starData = "";
    private String endData = "";
    private String timestamp = String.valueOf(System.currentTimeMillis());
    private int pageIndex = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home1, container, false);
        ButterKnife.bind(this, view);
        initBroadcastReceiver();
        initView();
        getMyLikes();

        return view;
    }

    private void setProductData(List<HomeProductBean> list) {
        if (productAdapter != null) {
            if (pageIndex==1) {//如果是下拉刷新 则要清除数据 如果不是下拉刷新 则加载新数据
                productAdapter.clear();
            }
            productAdapter.addAll(list);
            productAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        pullRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        pullRefreshView.setOnRefreshListener(this);
        gridView = pullRefreshView.getRefreshableView();
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(20);
        gridView.setVerticalSpacing(20);
        gridView.setOnItemClickListener(this);
        productAdapter = new HomeProductAdapter(getActivity());
        gridView.setAdapter(productAdapter);

        categoryAdapter = new CategoryAdapter(mActivity);
        horizontalListView.setOnItemClickListener(horItemClickListener);
        horizontalListView.setAdapter(categoryAdapter);
        horizontalListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = (Category) adapterView.getAdapter().getItem(i);
                if (category.getId() == -1) return;
                pageIndex = 1;
                categoryAdapter.setCheck(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ct_1.setTitle("全部区域");
        ct_2.setTitle("完工日期");
        ct_3.setTitle("服务内容");
        ct_1.setOnClickListener(this);
        ct_2.setOnClickListener(this);
        ct_3.setOnClickListener(this);

        tv_search.setOnClickListener(this);
        iv_feiji.setOnClickListener(this);
        iv_add_category.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_feiji:
                if (BaseActivity.islogin(getActivity())) {
                    intent = new Intent(getContext(), ProjectActivity.class);
                    startActivity(intent);
                } else {
                    BaseActivity.toLogin(getActivity());
                }
                break;
            case R.id.tv_search:
                intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ct_1:
                showChoiceCity();
                //                intent = new Intent(getContext(), CityPickerActivity.class);
//                getActivity().startActivityFromFragment(this, intent, 1001);
                break;
            case R.id.ct_2:
                ct_1.setChecked(false);
                ct_2.setChecked(true);
                ct_3.setChecked(false);
                showChoiceDate();
                break;
            case R.id.ct_3:
                ct_1.setChecked(false);
                ct_2.setChecked(false);
                ct_3.setChecked(true);
                showChoiceService();
                break;
            case R.id.iv_add_category:
                intent = new Intent(getContext(), CategoryActivity.class);
                getActivity().startActivityFromFragment(this, intent, 1002);
                break;
        }
    }

    private void showChoiceCity() {
        List<HotCity> hotCities = new ArrayList<>();
        hotCities.add(new HotCity("全部区域", "全部区域", "000000000"));
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("海外地区", "海外地区", "101210101"));

        CityPicker.getInstance()
                .setFragmentManager(getFragmentManager())
                .enableAnimation(true)
                .setLocatedCity(new LocatedCity(Constants.city, Constants.province, ""))
                .setHotCities(hotCities)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        if (data==null)return;
                        city = data.getName();
                        ct_1.setTitle(city);
                        if (city.equals("全部区域")){
                            city = "";
                            ct_1.setTitleColor(false);
                        }else {
                            ct_1.setTitleColor(true);
                        }
                        ct_1.setChecked(false);
                        pageIndex=1;
                        getAllData();
                    }

                    @Override
                    public void onLocate() {
                        Log.e("onLocate", "onLocate");
                    }
                })
                .show();
    }

    /**
     * 获取所有得完成项目
     */
    private void getAllData() {
        Call call;
        if (categoryId == 0) {//推荐内容 没有删选条件
            call = Http.links.allOutProduct(0, "", "", "", "", 0, pageIndex);
        } else {
            //非推荐内容
            call = Http.links.allOutProduct(categoryId, city, name, starData, endData, serviceId, pageIndex);
        }
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                pullRefreshView.onRefreshComplete();
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<HomeProductBean> beanList = JSON.parseArray(jsObj.getString("data"), HomeProductBean.class);
                    if (beanList != null && beanList.size() > 0) {
                        setProductData(beanList);
                    } else {
                        beanList = new ArrayList<>();
                        setProductData(beanList);
                        onFail("没有更多数据了");
                    }
                } else {
                    onFail("获取数据失败");
                }
            }

            @Override
            public void onFail(String error) {
                pullRefreshView.onRefreshComplete();
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    /**
     * 获取服务类型
     */
    private void getAllServiceList() {
        Call call = Http.links.homeServiceList(categoryId, starData, endData, city);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<TypeBean> beanList = JSON.parseArray(jsObj.getString("data"), TypeBean.class);
                    if (beanList != null) {
                        /*** 添加全部选项 ***/
                        TypeBean typeBean = new TypeBean(0, "全部");
                        List<TypeBean.Tclass> tclasses = new ArrayList<>();
                        tclasses.add(0, new TypeBean.Tclass(0, "全部", ""));
                        typeBean.setTcList(tclasses);
                        beanList.add(0, typeBean);

                        List2PopWindow popWindow = new List2PopWindow(mActivity);
                        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                ct_3.setChecked(false);
                            }
                        });
                        popWindow.setData(beanList);
                        popWindow.setPopWindowCheckedListener(new List2PopWindow.PopWindowCheckedListener() {
                            @Override
                            public void onPopWindowCheckedListener(PopBean popBean) {
                                ct_3.setTitle(popBean.getName());
                                serviceId = popBean.getId();
                                if (serviceId==0){
                                    ct_3.setTitleColor(false);
                                }else {
                                    ct_3.setTitleColor(true);
                                }
                                pageIndex=1;
                                getAllData();
                            }
                        });
                        popWindow.showPopupWindow(ct_3);
                    } else onFail("暂无数据");
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

    private void showChoiceService() {
        getAllServiceList();
    }

    /**
     * 选择时间
     */
    private void showChoiceDate() {
        ListPopWindow popWindow = new ListPopWindow(mActivity);
        List<PopBean> popBeanList = new ArrayList<>();
        int startYear = 2010;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        while (startYear <=year) {
            if (startYear >= year) startYear = year;
            popBeanList.add(new Category(6, startYear + " 年"));
            startYear++;
        }
        Collections.reverse(popBeanList);
        popBeanList.add(0,new Category(0, "全部"));
        popWindow.setData(popBeanList);
        popWindow.showPopupWindow(ct_2);
        popWindow.setPopWindowCheckedListener(new ListPopWindow.PopWindowCheckedListener() {
            @Override
            public void onPopWindowCheckedListener(PopBean popBean) {
                String data = popBean.getName();
                ct_2.setTitle(data);
                if (data.contains(" ")) {
                    data = data.substring(0, data.indexOf(" "));
                    starData = data + "-01-01";
                    endData = data + "-12-31";
                    ct_2.setTitleColor(true);
                } else {
                    starData = "";
                    endData = "";
                    ct_2.setTitleColor(false);
                }
                pageIndex=1;
                getAllData();

            }
        });
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ct_2.setChecked(false);
            }
        });
    }

    /**
     * 导航菜单点击
     */
    AdapterView.OnItemClickListener horItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Category category = (Category) adapterView.getAdapter().getItem(i);
            if (category.getId() == -1) return;
            if (i == 0) {
                ll_search.setVisibility(View.GONE);
//                serviceId=0;
//                city = "";
//                name = "";
//                starData = "";
//                endData = "";
//                ct_1.setTitle("全部区域");
//                ct_2.setTitle("完工日期");
//                ct_3.setTitle("服务内容");
            } else {
                ll_search.setVisibility(View.VISIBLE);
            }
            horizontalListView.setSelection(i);
            categoryId = category.getId();
            timestamp = String.valueOf(System.currentTimeMillis());
            getAllData();
        }
    };
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pageIndex = 1;
        getAllData();

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pageIndex++;
        getAllData();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HomeProductBean bean = (HomeProductBean) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pid", bean.getItemId());
        bundle.putInt("FormType", 0);
        bundle.putString("Name", bean.getName());
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    public void setCategoryData(List<Category> categoryLists) {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(0, "推荐"));
//        List<Channel> channels = JSON.parseArray(DataHpler.getLikeType(), Channel.class);
//        if (channels != null)
//            for (int i = 0; i < channels.size(); i++) {
//                Channel channel = channels.get(i);
//                categoryList.add(new Category(channel.getId(), channel.getTitle()));
//            }
        categoryList.addAll(categoryLists);
        categoryList.add(new Category(-1, "   "));
        categoryAdapter.clear();
        categoryAdapter.addAll(categoryList);
        categoryAdapter.setCheck(categoryList.get(0));
        ll_search.setVisibility(View.GONE);
        categoryId = categoryList.get(0).getId();
        pageIndex=1;
        getAllData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == 1002 && data != null) {
            getMyLikes();
        }
    }
    /**
     * 获取我喜欢的分类
     */
    private void getMyLikes(){
        Call call = Http.links.userItemCategory(DataHpler.getUserInfo().getUserid());
        Http.http.call(mActivity,call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<Category> otherList = JSON.parseArray(jsObj.getString("data"), Category.class);
                    setCategoryData(otherList);
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
     * 初始化广播
     */
    IntentFilter intentFilter;
    BroadcastReceiver mReceiver;

    private void initBroadcastReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.benkie.public");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String code = intent.getStringExtra("errCode");
                Log.e("hBroadcastReceiver", "errCode = " + code);
                if (code != null && code.equals("1")) {
                    //当发布项目之后，可能添加了新的服务类型和项目类型，因此要重新获取更新信息
                    getAllData();
                } else if (code != null && code.equals("2")) {
                    getMyLikes();
                }
            }
        };
        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null)
            getContext().unregisterReceiver(mReceiver);
    }
}
