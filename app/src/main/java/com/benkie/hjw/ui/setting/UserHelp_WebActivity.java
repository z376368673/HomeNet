package com.benkie.hjw.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.bean.UserInfo;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class UserHelp_WebActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_use_help);
        ButterKnife.bind(this);
        headView.setTitle("使用帮助");
        headView.setBtBack(this);

        AgentWeb mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
                //.defaultProgressBarColor() // 使用默认进度条颜色
                .createAgentWeb()//
                .ready()
                .go("http://www.3huanju.com/yetdwell/problem/list.do");

    }


}
