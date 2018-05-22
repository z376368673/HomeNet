package com.benkie.hjw.ui.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.benkie.hjw.utils.ShareUtils;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.HeadView;
import com.benkie.hjw.wxapi.WXPay;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class ProductRecommendPayActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.tv_range)
    TextView tv_range;   //项目分类
    @BindView(R.id.tv_money)
    TextView tv_money;   //支付费用
    @BindView(R.id.tv_date)
    TextView tv_date;   //展示日期
    @BindView(R.id.wx_pay)
    TextView wx_pay;//微信支付
    @BindView(R.id.tv_share)
    TextView tv_share;//分享集赞支付

    @BindView(R.id.tv_explain1)
    TextView tv_explain1;//支付说明文字


    @BindView(R.id.tv_explain)
    TextView tv_explain;//分享集赞支付

    HomeProductBean bean;
    int itemGather = 0;
    int itemPraise=0;
    String name ,typeName;

    int isSkillPraise=0;//是否可以集赞

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_recom_pay);
        ButterKnife.bind(this);
        headView.setTitle("发布推荐");
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
                if (code != null && code.equals("0")) {
                    ToastUtil.showInfo(mActivity, "支付成功");
                    BeseBroadcastReceiver.sendToPublic(mActivity,1);
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
    private void payDialog(){
        String info = String.format(getResources().getString(R.string.pay_success_recommend),typeName);
        BaseDialog.showMag(mActivity, "支付成功", info, "知道了", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        tv_share.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        bean = (HomeProductBean) bundle.getSerializable("Bean");
        if (bean == null) return;
        tv_range.setText("“完结项目”推荐");
        getPayInfo(bean);
    }

    @Override
    public void onClick(View v) {
        if (v == wx_pay && !Tools.isFastClick()) {
            //推荐页面 去微信支付
            WXPay wxPay = new WXPay();
            wxPay.initPay(mActivity);
            wxPay.payStart(bean.getItemId(), WXPay.PAY_PRODUCT_RECOMM);
        } else {
            ShareUtils.shareProducts(this, handler, bean.getItemId(),name);
        }
    }

    private void getPayInfo(final HomeProductBean bean) {
        Call call = Http.links.recommendInfo(bean.getItemId());
        Http.http.call(mActivity, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    name = jsObj.getString("name");
                    typeName = jsObj.getString("typeName");
                    int userItemId = jsObj.getIntValue("userItemId");//项目id
                    itemGather = jsObj.getIntValue("itemGather");//点赞数量
                    itemPraise = jsObj.getIntValue("itemPraise");//需要总数量
                    isSkillPraise   = jsObj.getIntValue("isItemPraise");//是否可以集赞
                    int tag = jsObj.getIntValue("tag");//0.未推荐，1.已推荐，2.集赞中
 //                   String date = jsObj.getString("date");
                    double money = jsObj.getDoubleValue("money");
                    String recommendExplain =jsObj.getString("recommendExplain");
                    tv_explain1.setText(recommendExplain);
//                    tv_date.setText("单次");
                    tv_money.setText(money+"");
                    tv_explain.setText(getResources().getString(R.string.recomm_explain));
                    if (tag == 2) {
                        tv_share.setText(String.format("已集赞 %d/%d 个,继续分享给好友", itemGather,itemPraise));
                        tv_share.setBackgroundResource(R.drawable.shape_button_bg_color_gary);
                    } else if (tag == 1) {
                        wx_pay.setVisibility(View.VISIBLE);
                        tv_share.setVisibility(View.INVISIBLE);
                        tv_explain.setVisibility(View.INVISIBLE);
                        tv_share.setText(String.format("分享集赞%d个免费发布", itemPraise));
                    } else {
                        tv_share.setVisibility(View.VISIBLE);
                        tv_share.setText(String.format("分享集赞%d个免费发布", itemPraise));
                    }
                    if (isSkillPraise==1){
                        tv_share.setVisibility(View.GONE);
                        tv_explain.setVisibility(View.INVISIBLE);
                    }
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                sharePraise();
            }
        }
    };

    /**
     * 分享成功回调此接口 改变按钮状态
     */
    private void sharePraise() {
        Call call = Http.links.shareItemPraise(bean.getItemId());
        Http.http.call(mActivity, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    tv_share.setText(String.format("已集赞 %d/%d 个,继续分享给好友", itemGather,itemPraise));
                    tv_share.setBackgroundResource(R.drawable.shape_button_bg_color_gary);
                    wx_pay.setVisibility(View.VISIBLE);
                    tv_share.setVisibility(View.VISIBLE);
                    ProductRecommendPayActivity.this.setResult(RESULT_OK);
                }
            }
            @Override
            public void onFail(String error) {

            }
        });
    }


}
