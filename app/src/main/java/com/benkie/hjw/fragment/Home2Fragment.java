package com.benkie.hjw.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.HomeSkillAdapter;
import com.benkie.hjw.bean.Category;
import com.benkie.hjw.bean.HomeSkillBean;
import com.benkie.hjw.bean.PopBean;
import com.benkie.hjw.constants.Constants;
import com.benkie.hjw.map.LocationUtils;
import com.benkie.hjw.map.MyMapActivity;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.popwindow.GridVIewPopWindow;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.ui.skill.SkillDetailsActivity;
import com.benkie.hjw.ui.skill.SkillPublicActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by 37636 on 2018/1/18.
 */

public class Home2Fragment extends BaseFragment implements
        PullToRefreshBase.OnRefreshListener2,
        AdapterView.OnItemClickListener {

    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.iv_feiji)
    TextView iv_feiji;
    @BindView(R.id.iv_all_category)
    TextView iv_all_category;
    @BindView(R.id.pullRefreshView)
    PullToRefreshListView pullRefreshView;
    ListView listView;
    HomeSkillAdapter homeSkillAdapter;
    List<Category> categoryList;
    Category category;
    LocationUtils locationUtils;
    private int pageIndex = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        ButterKnife.bind(this, view);
        initView();
        getAllType(false);
        initBroadcastReceiver();
        return view;
    }

    /**
     * 添加技术服务数据
     */
    private void setSkillData(List<HomeSkillBean> skillList) {
        if (homeSkillAdapter != null) {
            if (pageIndex==1){
                homeSkillAdapter.clear();
                homeSkillAdapter = new HomeSkillAdapter(getActivity());
                listView.setAdapter(homeSkillAdapter);
            }
            homeSkillAdapter.addAll(skillList);
            homeSkillAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        tv_location.setText(Constants.address);
        tv_location.setOnClickListener(this);
        iv_feiji.setOnClickListener(this);

        pullRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        pullRefreshView.setOnRefreshListener(this);
        listView = pullRefreshView.getRefreshableView();
        homeSkillAdapter = new HomeSkillAdapter(mActivity);
        listView.setDividerHeight(0);
        listView.setAdapter(homeSkillAdapter);
        listView.setOnItemClickListener(this);
        iv_all_category.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_location:
                Intent intent = new Intent(getContext(), MyMapActivity.class);
                intent.putExtra("Name", "项目所在地");
                getActivity().startActivityFromFragment(this, intent, 1000);
                break;
            case R.id.iv_feiji:
                if (BaseActivity.islogin(getActivity())) {
                    intent = new Intent(getContext(), SkillPublicActivity.class);
                    startActivity(intent);
                } else {
                    BaseActivity.toLogin(getActivity());
                }
                break;
            case R.id.iv_all_category:
                showAllType();
                break;
        }
    }
    /**
     * 全部分类
     */
    private void showAllType() {
        GridVIewPopWindow popWindow = new GridVIewPopWindow(mActivity);
        popWindow.setData(categoryList);
        popWindow.showPopupWindow(iv_all_category);
        popWindow.setPopWindowCheckedListener(new GridVIewPopWindow.PopWindowCheckedListener() {
            @Override
            public void onPopWindowCheckedListener(PopBean popBean) {
                category = (Category) popBean;
                iv_all_category.setText(category.getName());
                pageIndex=1;
                getData(category.getId());
            }
        });
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (category != null){
            pageIndex=1;
            getData(category.getId());
        }

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pageIndex++;
        getData(category.getId());

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HomeSkillBean homeSkillBean = (HomeSkillBean) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent(getActivity(), SkillDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("sid", homeSkillBean.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 获取技术分类列表
     * @param isRefresh 是否刷新所有数据
     */
    private void getAllType(final boolean isRefresh) {
        Call call = Http.links.allSkill();
        Http.http.call(mActivity, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    categoryList = JSON.parseArray(jsObj.getJSONArray("info").toJSONString(), Category.class);
                    categoryList.add(0, new Category(0, "全部"));
                    if (!isRefresh){
                        if (categoryList != null&&categoryList.size()>0) {
                            category = categoryList.get(0);
                            iv_all_category.setText(category.getName());
                            getData(category.getId());
                        }
                    }else {

                    }
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, "" + error);
            }
        });
    }

    /**
     * 获取技术人员列表
     *
     * @param skillTypeId
     */

    private void getData(int skillTypeId) {
        Call call = Http.links.skillUser(skillTypeId, Constants.Latitude, Constants.Longitude,pageIndex);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                pullRefreshView.onRefreshComplete();
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<HomeSkillBean> skillBeanList = JSON.parseArray(jsObj.getString("info"), HomeSkillBean.class);
                    if (skillBeanList != null&&skillBeanList.size()>0)
                        setSkillData(skillBeanList);
                    else {
                        skillBeanList = new ArrayList<>();
                        setSkillData(skillBeanList);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 & resultCode == Activity.RESULT_OK) {
            LatLonPoint latLonPoint = data.getExtras().getParcelable("LatLonPoint");
            String address = data.getExtras().getString("Address");
            tv_location.setText(address);
            Constants.Longitude = latLonPoint.getLongitude();
            Constants.Latitude = latLonPoint.getLatitude();
            homeSkillAdapter.notifyDataSetChanged();
            pageIndex=1;
            getData(category.getId());
        }
    }

    /**
     * 初始化广播
     */
    IntentFilter intentFilter;
    BroadcastReceiver mReceiver;

    private void initBroadcastReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.benkie.skill");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String code = intent.getStringExtra("errCode");
                Log.e("hBroadcastReceiver", "errCode = " + code);
                if (code != null && code.equals("1")) {
                    //当发布项目之后，可能添加了新的服务类型和项目类型，因此要重新获取更新信息
                    getAllType(false);
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
