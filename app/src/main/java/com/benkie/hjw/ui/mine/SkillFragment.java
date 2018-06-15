package com.benkie.hjw.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.CollectinoSkillAdapter;
import com.benkie.hjw.adapter.HomeProductAdapter;
import com.benkie.hjw.adapter.HomeSkillAdapter;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.bean.HomeSkillBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.fragment.BaseFragment;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.product.ProductDetailsActivity;
import com.benkie.hjw.ui.skill.SkillDetailsActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by 37636 on 2018/1/18.
 */

public class SkillFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.iv_no)
    ImageView iv_no;

    @BindView(R.id.pullRefreshView)
    PullToRefreshListView pullRefreshView;
    ListView listView;
    CollectinoSkillAdapter adapter;
    Fragment fragment;
    int pageIndex = 1;
    //String timestamp = System.currentTimeMillis() + "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = View.inflate(getActivity(),R.layout.fragment_home3,null);
        View view = inflater.inflate(R.layout.fragment_skill_collection, container, false);
        ButterKnife.bind(this, view);
        initView();
        fragment = this;
        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

    }

    public void initView() {
//        pullRefreshView.setVisibility(View.GONE);
//        iv_no.setVisibility(View.VISIBLE);
        pullRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        pullRefreshView.setOnRefreshListener(this);
        listView = pullRefreshView.getRefreshableView();
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(this);
        adapter = new CollectinoSkillAdapter(mActivity);
        listView.setAdapter(adapter);
        geteData();
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

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //timestamp = System.currentTimeMillis() + "";
        pageIndex=1;
        geteData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pageIndex++;
        geteData();

    }

    private void geteData() {
        Call call = Http.links.skillCollection(DataHpler.getUserInfo().getUserid(), pageIndex);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<HomeSkillBean> skillBeanList = JSON.parseArray(jsObj.getString("skill"), HomeSkillBean.class);
                    if (skillBeanList != null && skillBeanList.size() > 0) {
                        pullRefreshView.setVisibility(View.VISIBLE);
                        iv_no.setVisibility(View.GONE);
                        setSkillData(skillBeanList);
                    } else {
                        if (pageIndex == 1) {
                            pullRefreshView.setVisibility(View.GONE);
                            iv_no.setVisibility(View.VISIBLE);
                        } else {
                            onFail("没有更多数据了");
                        }
                    }

                } else {
                    onFail("获取数据失败");
                }
                pullRefreshView.onRefreshComplete();
            }

            @Override
            public void onFail(String error) {
                pullRefreshView.onRefreshComplete();
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    /**
     * 添加技术服务数据
     */
    private void setSkillData(List<HomeSkillBean> skillList) {
        if (adapter==null){
            adapter= new CollectinoSkillAdapter(getActivity());
            listView.setAdapter(adapter);
        }
        if (pageIndex == 1) {
            adapter.clear();
        }
        adapter.addAll(skillList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        geteData();
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
