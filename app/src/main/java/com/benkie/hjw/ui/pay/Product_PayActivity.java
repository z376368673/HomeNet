package com.benkie.hjw.ui.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.BroadcastReceiver.BeseBroadcastReceiver;
import com.benkie.hjw.R;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.HeadView;
import com.benkie.hjw.wxapi.WXPay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class Product_PayActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.tv_range)
    TextView tv_range;   //项目分类
    @BindView(R.id.tv_explain)
    TextView tv_explain;   //支付说明
    @BindView(R.id.tv_money)
    TextView tv_money;   //支付费用
    @BindView(R.id.tv_unit)
    TextView tv_unit;//单位数
    @BindView(R.id.tv_date)
    TextView tv_date;   //展示日期
    @BindView(R.id.wx_pay)
    TextView wx_pay;//微信支付

    HomeProductBean bean;
    int type = 0;
    String itemName;
    String typeName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_pay);
        ButterKnife.bind(this);
        headView.setTitle("发布项目");
        headView.setBtBack(this);
        initBroadcastReceiver();
        initView();

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
                Log.e("BroadcastReceiver", "errCode: " + code);
                if (code != null && code.equals("0")) {
                    BeseBroadcastReceiver.sendToPublic(Product_PayActivity.this, 1);
                    payDialog();
                } else if (code != null && code.equals("-1")) {
                    ToastUtil.showInfo(mActivity, "支付错误");
                } else if (code != null && code.equals("-2")) {
                    ToastUtil.showInfo(mActivity, "取消支付");
                } else if (code != null && code.equals("-3")) {
                    ToastUtil.showInfo(mActivity, "系统错误");
                }
            }
        };
        registerReceiver(mReceiver, intentFilter);
    }

    private void payDialog() {
        //String info = String.format(getResources().getString(R.string.pay_success_product),itemName,typeName);
        String info = getResources().getString(R.string.pay_success_product);
        BaseDialog.showMag(mActivity, "支付成功", info, "知道了", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
    }

    private void initView() {
        wx_pay.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        bean = (HomeProductBean) bundle.getSerializable("Bean");
        type = bundle.getInt("TYPE", 0);
        if (bean == null) return;
        headView.setTitle(bean.getName());

        getPayInfo(bean);
    }

    @Override
    public void onClick(View v) {
        if (v == wx_pay && !Tools.isFastClick()) {
            //去微信支付
            WXPay wxPay = new WXPay();
            wxPay.initPay(mActivity);
            if (type == 0) {
                wxPay.payStart(bean.getItemId(), WXPay.PAY_PRODUCT_PUBLIC);
            } else {
                wxPay.payStart(bean.getItemId(), WXPay.PAY_PRODUCT_RENEW);
            }

        }
    }

    /**
     * 获取发布支付信息
     *
     * @param bean
     */
    private void getPayInfo(final HomeProductBean bean) {
        Call call;
        if (type == 0) {
            call = Http.links.toPublis(bean.getItemId());
        } else {
            call = Http.links.toRenew(bean.getItemId());
        }
        Http.http.call(mActivity, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {

                    String data = jsObj.getString("date");
                    itemName = jsObj.getString("itemName");
                    typeName = jsObj.getString("typeName");
                    String publishMoney = jsObj.getString("publishMoney");
                    int year = jsObj.getIntValue("year");
                    String unit = " 元/" + Tools.getYMD(year);
                    String publishExplain = jsObj.getString("publishExplain");
                    tv_explain.setText(publishExplain);
                    tv_range.setText(typeName);
                    tv_unit.setText(unit);
                    tv_date.setText(data);
                    tv_money.setText("￥"+publishMoney + "");
                } else if (msg == 2) {
                    onFail("该类型不能发布");
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


}
