package com.benkie.hjw.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.application.BaseApp;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.db.ProductSqliteOpenHelper;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.version.NetUtil;
import com.benkie.hjw.version.UpdataVersion;
import com.benkie.hjw.version.UpdateManager;
import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;

public class WelcomeActivity extends BaseActivity {
    boolean isFenghao =false; //是否被封号
    @BindView(R.id.welcomImg)
    ImageView welcomImg;
    boolean isValidityToken = true;//token是否有效
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

//        UpdataVersion updataVersion = new UpdataVersion(this) {
//            @Override
//            public void isUpdateVersion(boolean isUp) {
//                if (!isUp)
//                getUserInfo();
//            }
//        };
//        if (NetUtil.isConnected(this)){
//            UpdataVersion.URL= Http.BASE_URL+"yetdwell/refreshInfo/newInfo.do";
//            updataVersion.getVersion();
//        }else {
//            ToastUtil.showInfo(this,"您尚未连接网络,即将自动退出...");
//            handler.sendEmptyMessageDelayed(1,2000);
//        }



       getUserInfo();
    }



    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                finish();
            }else if (msg.what==1){

            }
        }
    };
    private void toNext() {
        if (DataHpler.getFirstShow("firstIn")) {
            Intent intent = new Intent(this, Welcome2Activity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getUserInfo() {
        /** 倒计时三秒 **/
        CountDownTimer timer = new CountDownTimer(4700, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // (millisUntilFinished / 1000) + "秒后可重发";
                int  d = (int) (millisUntilFinished/1000);
                Log.e("onTick",d+"");
                switch (d){
                    case 3:
                        Glide.with(mActivity).load(R.mipmap.welcome1)
                                //.animate(R.anim.animtion)
                                //.crossFade(1000)
                                .into(welcomImg);
                        break;
                    case 2:
                        break;
                    case 1:
                        Glide.with(mActivity).load(R.mipmap.welcome2)
                                //.crossFade(1000)
                                //.animate(R.anim.animtion)
                                .into(welcomImg);
                        break;
                }
            }

            @Override
            public void onFinish() {
                if (isFenghao||!isValidityToken){
                    DataHpler.exit();
                    BaseActivity.toLogin(mActivity);
                    finish();
                }else {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
        //upDataProduct();
        upDataUserInfo();

    }

    /**
     * 更新用户信息
     */
    private void upDataUserInfo(){
        if (DataHpler.islogin()) {
            Call call = Http.links.getUserInfo(DataHpler.getToken());
            Http.http.call(mActivity, call, false, new Http.JsonCallback() {
                @Override
                public void onResult(String json, String error) {
                    JSONObject jsObj = JSON.parseObject(json);
                    int msg = jsObj.getIntValue("msg");
                    if (msg==1){
                        isValidityToken =true;
                        DataHpler.setUserInfo(json);
                        setTagAndAlias();
                    }else if (msg == 2){
                        isValidityToken =false;
                        isFenghao = true;
                    }else {
                        isValidityToken =false;
                    }
                }

                @Override
                public void onFail(String error) {
                    ToastUtil.showInfo(mActivity, error);
                }
            });
        }
    }

    /**
     * JPush设置标签与别名
     */
    private void setTagAndAlias() {
        /**
         *这里设置了别名，在这里获取的用户登录的信息
         *并且此时已经获取了用户的userId,然后就可以用用户的userId来设置别名了
         **/
        //false状态为未设置标签与别名成功
        //if (UserUtils.getTagAlias(getHoldingActivity()) == false) {
        Set<String> tags = new HashSet<String>();
        //这里可以设置你要推送的人，一般是用户uid 不为空在设置进去 可同时添加多个
        tags.add(DataHpler.getUserInfo().getMobile());//设置tag
        //上下文、别名【Sting行】、标签【Set型】、回调
        JPushInterface.setTags(this,0,tags);
        JPushInterface.setAlias(this,0,DataHpler.getUserInfo().getMobile());
        // }
    }

    /**
     * 获取所有得完成项目，更新本地数据库
     */
    private void upDataProduct() {
        Call call = Http.links.searchAllItemList(0,"","","2000-01-01","2100-01-01",0,String.valueOf(System.currentTimeMillis()));
        Http.http.call(mActivity,call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    final List<HomeProductBean> beanList = JSON.parseArray(jsObj.getString("data"), HomeProductBean.class);
                    if (beanList != null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ProductSqliteOpenHelper.getNewHelpe(WelcomeActivity.this).setProducts(beanList);
                            }
                        }).start();
                    }else  onFail("暂无数据");
                }else {
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
