package com.benkie.hjw.wxapi;
//com.wevalue.wxapi

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.benkie.hjw.constants.Constants;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.LogUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivit";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("errCode", String.valueOf(resp.errCode));
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            //回调中errCode值列表：
            //名称  描述 解决方案
            // 0 成功 展示成功页面
            // -1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
            // -2 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
            LogUtils.e("wxdebug", String.valueOf(resp.errCode));
            Intent intent = new Intent();
            intent.setAction("com.benkie.payresult");
            intent.putExtra("errCode", String.valueOf(resp.errCode));
            sendBroadcast(intent);
            finish();
        }
    }


}