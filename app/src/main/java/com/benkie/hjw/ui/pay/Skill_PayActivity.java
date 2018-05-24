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
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ShareUtils;
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

public class Skill_PayActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.tv_money)
    TextView tv_money;   //支付费用
    @BindView(R.id.tv_unit)
    TextView tv_unit;   //单位
    @BindView(R.id.tv_date)
    TextView tv_date;   //展示日期
    @BindView(R.id.wx_pay)
    TextView wx_pay;//微信支付
    @BindView(R.id.tv_share)
    TextView tv_share;//分享集赞支付

    @BindView(R.id.tv_explain1)
    TextView tv_explain1;//说明

    @BindView(R.id.tv_explain)
    TextView tv_explain;//分享集赞支付
    int status = 0;//0.未发布，1.会员，2.集赞中
    int gatherNumber = 0;//已集赞数量
    int skillPraise = 0; //总需数量
    int type = 0; //续费或者开通的标识
    int isSkillPraise = 0;//是否可以集赞 1关闭

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_pay);
        ButterKnife.bind(this);
        headView.setTitle("发布技能");
        headView.setBtBack(this);
        initBroadcastReceiver();
        initView();
        type = getIntent().getIntExtra("type", 0);
        getPayInfo();
//        getInfo();
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
                    BeseBroadcastReceiver.sendToPublic(mActivity, 1);
                    payDialog();
                } else if (code != null && code.equals("-1")) {
                    ToastUtil.showInfo(mActivity, "支付错误");
                } else if (code != null && code.equals("-2")) {
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
        if (status == 0) {
            String info = getResources().getString(R.string.pay_success_skill);
            BaseDialog.showMag(mActivity, "支付成功", info, "知道了", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Skill_PayActivity.this.setResult(RESULT_OK);
                    finish();
                }
            });
        }
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
//        PayProductActivity
    }

    @Override
    public void onClick(View v) {

        if (v == wx_pay && !Tools.isFastClick()) {
            //去微信支付
            WXPay wxPay = new WXPay();
            wxPay.initPay(mActivity);
            if (status == 1) {
                wxPay.payStart(DataHpler.getUserInfo().getUserid(), WXPay.PAY_SKILL_RENEW);
            } else {
                wxPay.payStart(DataHpler.getUserInfo().getUserid(), WXPay.PAY_SKILL_OPEN);
            }
        } else if (v == tv_share) {
            ShareUtils.shareSkills(this, handler, DataHpler.getUserInfo().getUserid());
        }
    }


    private void getPayInfo() {
        Call call;
        if (type == 1) {
            call = Http.links.renewSkill(DataHpler.getUserInfo().getUserid());
        } else {
            call = Http.links.openSkill(DataHpler.getUserInfo().getUserid());
        }
        Http.http.call(mActivity, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    //已集赞数量
                    gatherNumber = jsObj.getIntValue("gatherNumber");
                    //购买时长 单位天
                    int year = jsObj.getIntValue("year");
                    //所需集赞总数
                    skillPraise = jsObj.getIntValue("skillPraise");
                    isSkillPraise = jsObj.getIntValue("isSkillPraise");//是否可以集赞

                    if (type==0){
                        String publishExplain = jsObj.getString("publishExplain");//
                        tv_explain1.setText(publishExplain);
                    }else {
                        String renewExplain = jsObj.getString("renewExplain");//
                        tv_explain1.setText(renewExplain);
                    }

                    //支付金额
                    double publishMoney = jsObj.getDoubleValue("publishMoney");
                    status = jsObj.getIntValue("status");//0.未发布，1.会员，2.集赞中
                    String date = jsObj.getString("date");//0.未发布，1.会员，2.集赞中
                    //备注
                    tv_explain.setText(getResources().getString(R.string.skill_explain));

                    tv_unit.setText(" 元 /" + Tools.getYMD(year));
                    tv_date.setText(date);
                    tv_money.setText("￥" + publishMoney);

//                    if (status == 0) {
//                        tv_share.setVisibility(View.VISIBLE);
//                        wx_pay.setVisibility(View.VISIBLE);
//                    } else if (status == 1) {
//                        tv_share.setVisibility(View.GONE);
//                        wx_pay.setVisibility(View.VISIBLE);
//                        tv_explain.setVisibility(View.GONE);
//                    } else {
//                        tv_share.setText(String.format("已集赞 %d/%d 个,继续分享给好友", gatherNumber, skillPraise));
//                        tv_share.setBackgroundResource(R.drawable.shape_button_bg_color_gary);
//                        wx_pay.setVisibility(View.VISIBLE);
//                        tv_share.setVisibility(View.VISIBLE);
//                    }
//                    if (isSkillPraise==1){
//                        tv_share.setVisibility(View.GONE);
//                        tv_explain.setVisibility(View.GONE);
//                    }

                    if (status==0&&isSkillPraise==0){
                        // 非会员   允许集赞
                        tv_share.setVisibility(View.VISIBLE);
                        wx_pay.setVisibility(View.VISIBLE);
                        tv_share.setText(String.format("分享集赞%d个开通年会员", skillPraise));
                    }else if (status==0&&isSkillPraise==1){
                        // 非会员   不允许集赞
                        tv_share.setVisibility(View.GONE);
                        tv_explain.setVisibility(View.GONE);
                    }else if (status==1&&isSkillPraise==0){
                        // 会员   允许集赞
                        tv_share.setVisibility(View.GONE);
                        tv_explain.setVisibility(View.GONE);
                    }else if (status==1&&isSkillPraise==1){
                        // 会员   不允许集赞
                        tv_share.setVisibility(View.GONE);
                        tv_explain.setVisibility(View.GONE);
                    }else if (status==2&&isSkillPraise==0){
                        // 集赞中  允许集赞
                        tv_share.setVisibility(View.VISIBLE);
                        tv_explain.setVisibility(View.VISIBLE);
                        tv_share.setText(String.format("已集赞 %d/%d 个,继续分享给好友", gatherNumber, skillPraise));
                        tv_share.setBackgroundResource(R.drawable.shape_button_bg_color_gary);
                    }else if (status==2&&isSkillPraise==1){
                        // 集赞中  不允许集赞
                        tv_share.setVisibility(View.GONE);
                        tv_explain.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity,error);
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
        Call call = Http.links.shareSkillPraise(DataHpler.getUserInfo().getUserid());
        Http.http.call(mActivity, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    tv_share.setText(String.format("已集赞 %d/%d 个,继续分享给好友", gatherNumber, skillPraise));
                    tv_share.setBackgroundResource(R.drawable.shape_button_bg_color_gary);
                    wx_pay.setVisibility(View.VISIBLE);
                    tv_share.setVisibility(View.VISIBLE);
                    Skill_PayActivity.this.setResult(RESULT_OK);
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

}
