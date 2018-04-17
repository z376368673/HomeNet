package com.benkie.hjw.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.constants.Constants;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.utils.ToastUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import retrofit2.Call;

/**
 * Created by 37636 on 2018/3/10.
 */

public class WXPay {
    public static String PAY_SKILL_OPEN = "skill_open"; //开通技术服务年费
    public static String PAY_SKILL_RENEW = "skill_renew";//技术服务续费
    public static String PAY_PRODUCT_PUBLIC = "product_public";//发布项目费用
    public static String PAY_PRODUCT_RENEW = "product_renew";//项目续费
    public static String PAY_PRODUCT_RECOMM = "product_recomm";//推荐项目费用

    private IWXAPI api;
    Activity activity;

    public void initPay(Activity activity) {
        this.activity = activity;
        api = WXAPIFactory.createWXAPI(activity, null);
        api.registerApp(Constants.APP_ID);
    }

    public void payStart(int id,String payType) {
        if (payType==PAY_SKILL_OPEN){
            wxpayByOpen(id);
        }else if (payType==PAY_SKILL_RENEW){
            wxpayByRenew(id);
        }else if (payType==PAY_PRODUCT_PUBLIC){
            wxpayByProduct(id);
        }else if (payType==PAY_PRODUCT_RENEW){
            wxpayByProductRenew(id);
        }else if (payType==PAY_PRODUCT_RECOMM){
            wxpayByRecomm(id);
        }
    }

    /***
     * 解析json 发送支付请求
     * @param json
     */
     private void payFormJson(String json){
         if(json.length()<3){
             Intent intent = new Intent();
             intent.setAction("com.benkie.payresult");
             intent.putExtra("errCode", String.valueOf(-3));
             activity.sendBroadcast(intent);
             return;
         }
         JSONObject jsObj = JSON.parseObject(json);
         PayReq request = new PayReq();
         request.appId = jsObj.getString("appid");
         request.partnerId = jsObj.getString("partnerid");
         request.prepayId = jsObj.getString("prepayid");
         request.nonceStr = jsObj.getString("noncestr");
         request.timeStamp = jsObj.getString("timestamp");
         request.packageValue = jsObj.getString("packageValue");
         request.sign = jsObj.getString("sign");
         api.sendReq(request);
     }
    /**
     * 技术服务续费
     */
    private void wxpayByRenew(int id) {
        Call call = Http.links.wxpayByRenew(id);
        Http.http.call(activity, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                payFormJson(json);
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(activity, error);
            }
        });
    }

    /**
     * 开通技术服务年费
     */
    private void wxpayByOpen(int id) {
        Call call = Http.links.wxpayByOpen(id);
        Http.http.call(activity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                payFormJson(json);
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(activity, error);
            }
        });
    }

    /**
     * 发布项目支付
     */
    private void wxpayByProduct(int id) {
        Call call = Http.links.wxpayByProduct(id);
        Http.http.call(activity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                payFormJson(json);
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(activity, error);
            }
        });
    }
    /**
     * 项目续费支付
     */
    private void wxpayByProductRenew(int id) {
        Call call = Http.links.wxpayByProductRenew(id);
        Http.http.call(activity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                payFormJson(json);
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(activity, error);
            }
        });
    }
    /**
     * 项目到推荐支付
     */
    private void wxpayByRecomm(int id) {
        Call call = Http.links.wxpayByProductRecomm(id);
        Http.http.call(activity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                payFormJson(json);
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(activity, error);
            }
        });
    }
}
