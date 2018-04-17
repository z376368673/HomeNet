package com.benkie.hjw.ui.product;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.ProductPAdapter;
import com.benkie.hjw.bean.Category;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.dialog.Choice1Dialog;
import com.benkie.hjw.fragment.BaseFragment;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.pay.ProductRecommendPayActivity;
import com.benkie.hjw.ui.pay.Product_PayActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.wxapi.WXPay;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by 37636 on 2018/1/18.
 */

public class ProjectPFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.pullRefreshView)
    PullToRefreshGridView pullRefreshView;
    GridView gridView;
    ProductPAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = View.inflate(getActivity(),R.layout.fragment_home3,null);
        View view = inflater.inflate(R.layout.fragment_project_p, container, false);
        ButterKnife.bind(this, view);
        initView();
        initBroadcastReceiver();
        return view;
    }

    /**
     * 初始化广播
     */
    IntentFilter intentFilter;
    BroadcastReceiver mReceiver;

    private void initBroadcastReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.benkie.payresult");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String code = intent.getStringExtra("errCode");
                if (code != null && code.equals("0")) {
                    geteData(false);
                } else if (code != null && code.equals("-1")) {
                    ToastUtil.showInfo(mActivity, "支付错误");
                } else if (code != null && code.equals("-2")) {
                    ToastUtil.showInfo(mActivity, "取消支付");
                } else if (code != null && code.equals("-3")) {
                    ToastUtil.showInfo(mActivity, "系统错误");
                } else if (code != null && code.equals("100")) {
                    ToastUtil.showInfo(mActivity, "取消发布成功");
                    geteData(false);
                }
            }
        };
        getContext().registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null)
            getContext().unregisterReceiver(mReceiver);
    }

    public void initView() {
        pullRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        pullRefreshView.setOnRefreshListener(this);
        gridView = pullRefreshView.getRefreshableView();
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(20);
        gridView.setVerticalSpacing(20);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemClickListener(this);
        adapter = new ProductPAdapter(mActivity);
        adapter.setAdapterInterface(new ProductPAdapter.AdapterInterface() {
            @Override
            public void toRenewMoney(HomeProductBean obj) {
                Intent intent = new Intent(getActivity(), Product_PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", 1);
                bundle.putSerializable("Bean", obj);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void operation(final HomeProductBean item) {

                homeProductBean = item;
                Choice1Dialog choice1Dialog = new Choice1Dialog(getActivity());
                choice1Dialog.setHandler(handler);
                choice1Dialog.show();
            }
        });
        gridView.setAdapter(adapter);
        geteData(true);
    }

    HomeProductBean homeProductBean;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (homeProductBean != null)
                switch (msg.what) {
                    case 1: //取消发布
                        BaseDialog.dialogStyle1(context, "取消后，再次发布则需要重新付费！您确认要取消发布吗？", "取消发布", "点错了", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                noFabu(homeProductBean.getItemId());
                            }
                        });
                        break;
                    case 2://推荐发布
                        if (homeProductBean.getTag() == 1) {
                            ToastUtil.showInfo(getActivity(), "已在推荐中展示");
                            return;
                        }
                        Intent intent = new Intent(getActivity(), ProductRecommendPayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Bean", homeProductBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }

        }
    };

    private void noFabu(int id) {
        Call call = Http.links.unPublisItem(id);
        Http.http.call(context, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    ToastUtil.showInfo(getActivity(), "取消成功");
                    geteData(false);
                } else {
                    onFail("取消失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(context, error);
            }
        });
    }

    private void geteData(boolean isShow) {
        Call call = Http.links.allProduct(DataHpler.getUserInfo().getUserid());
        Http.http.call(mActivity, call, isShow, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                pullRefreshView.onRefreshComplete();
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<HomeProductBean> beanList = JSON.parseArray(jsObj.getString("out"), HomeProductBean.class);
                    if (beanList != null) {
                        adapter.clear();
                        adapter.addAll(beanList);
                        adapter.notifyDataSetChanged();
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HomeProductBean bean = (HomeProductBean) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pid", bean.getItemId());
        bundle.putString("Name", bean.getName());
        bundle.putInt("FormType", 2);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        geteData(false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pullRefreshView.onRefreshComplete();
    }
}
