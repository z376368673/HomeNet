/*
package com.benkie.hjw.ui.skill;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.SkillAdapter;
import com.benkie.hjw.bean.SkillBean;
import com.benkie.hjw.bean.SkillServiceBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.HeadView;
import com.benkie.hjw.wxapi.WXPay;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class SkillActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.tv_xufei)
    TextView tv_xufei;

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.tv_fuwu)
    TextView tv_fuwu;

    @BindView(R.id.tv_shili)
    TextView tv_shili;

    @BindView(R.id.pullRefreshView)
    PullToRefreshGridView pullRefreshView;
    GridView gridView;
    SkillAdapter skillAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);
        ButterKnife.bind(this);
        headView.setTitle("技术服务");
        headView.setBtBack(this);
        headView.setBtText("编辑");
        headView.setBtClickListener(this);
        initBroadcastReceiver();
        initView();
        getSkill();
    }
    */
/**
     * 初始化广播
     *//*

    IntentFilter intentFilter;
    BroadcastReceiver mReceiver;
    private void initBroadcastReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.benkie.payresult");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String code = intent.getStringExtra("errCode");
                if (code!=null&&code.equals("0")){
                    ToastUtil.showInfo(mActivity,"支付成功");
                    getSkill();
                }else  if (code!=null&&code.equals("-1")){
                    ToastUtil.showInfo(mActivity,"支付错误");
                }else  if (code!=null&&code.equals("-2")){
                    ToastUtil.showInfo(mActivity,"取消支付");
                }else  if (code!=null&&code.equals("-3")){
                    ToastUtil.showInfo(mActivity,"系统错误");
                }
            }
        };
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.right_tv) {
            Intent intent = new Intent(mActivity, SkillEditActivity.class);
            startActivityForResult(intent, 1000);
        }else  if (v == tv_xufei&& !Tools.isFastClick()) {
            WXPay wxPay = new WXPay();
            wxPay.initPay(mActivity);
            wxPay.payStart(DataHpler.getUserInfo().getUserid(),WXPay.PAY_SKILL_RENEW);
        }
    }

    private void initView() {
        pullRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullRefreshView.setOnRefreshListener(this);
        gridView = pullRefreshView.getRefreshableView();
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(15);
        gridView.setVerticalSpacing(15);
        gridView.setOnItemClickListener(this);
        skillAdapter = new SkillAdapter(mActivity);
        skillAdapter.notifyDataSetChanged();
        gridView.setAdapter(skillAdapter);
        gridView.setOnItemClickListener(this);
        gridView.setPadding(10, 0, 10, 0);
        tv_xufei.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 & resultCode == Activity.RESULT_OK) {
            //从编辑界面回来 更新修后的信息
            getSkill();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        getSkill();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pullRefreshView.onRefreshComplete();
    }

    public void getSkill() {
        Call call = Http.links.getUserSkill(DataHpler.getUserInfo().getUserid());
        Http.http.call(mActivity,call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                int day = jsObj.getIntValue("day");
                if (msg == 1) {
                    tv_date.setText("技能展示剩余时间 "+day+" 天");
                    JSONArray infos = jsObj.getJSONArray("info");
                    if (infos.size() == 0) {
                        Intent intent = new Intent(mActivity, SkillEditActivity.class);
                        startActivityForResult(intent, 1000);
                    }else {
                        SkillServiceBean bean = JSON.parseObject(infos.getString(0), SkillServiceBean.class);
                        tv_address.setText(bean.getAddress());
                        tv_fuwu.setText(bean.getServeType() == 0 ? "个人" : "团队");
                        tv_shili.setText(bean.getDescribes());
                        if (skillAdapter != null) {
                            List<SkillBean> skillBeans = bean.getSkills();
                            skillAdapter.clear();
                            skillAdapter.addAll(skillBeans);
                            skillAdapter.notifyDataSetChanged();
                        }
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

}
*/
