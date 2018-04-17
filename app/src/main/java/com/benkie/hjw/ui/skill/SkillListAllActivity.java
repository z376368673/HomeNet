package com.benkie.hjw.ui.skill;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.bean.SkillBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.DisplayUtil;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import am.widget.wraplayout.WrapLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class SkillListAllActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.wly_lyt_warp)
    WrapLayout wly_lyt_warp;
    private static SkillBean mSkillBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_all);
        ButterKnife.bind(this);
        headView.setTitle("选择技能");
        headView.setBtBack(this);
        headView.setBtText("保存");
        headView.setBtClickListener(this);
        mSkillBean = new SkillBean();
        getData();
    }

    TextView lastView = null;

    private void setData(final List<SkillBean> list) {
        int v = DisplayUtil.dip2px(mActivity, 11);
        int h = DisplayUtil.dip2px(mActivity, 14);
        for (int i = 0; i < list.size(); i++) {
            final SkillBean skillBean = list.get(i);
            final TextView textView = new TextView(mActivity);
            textView.setTextColor(getResources().getColor(R.color.black_33));
            textView.setTextSize(15);
            textView.setBackgroundResource(R.drawable.shape_button_bg_color_white_e4);
            textView.setText(list.get(i).getName());
            textView.setPadding(v, h, v, h);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lastView != null) {
                        lastView.setBackgroundResource(R.drawable.shape_button_bg_color_white_e4);
                        lastView.setTextColor(getResources().getColor(R.color.black_33));
                    }
                    textView.setBackgroundResource(R.drawable.shape_button_bg_color_main);
                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setTag(1);
                    mSkillBean = skillBean;
                    lastView = textView;
                }
            });
            wly_lyt_warp.addView(textView);
        }
        final TextView textView = new TextView(mActivity);
        textView.setTextColor(getResources().getColor(R.color.colorMain));
        textView.setTextSize(15);
        textView.setBackgroundResource(R.drawable.shape_button_bg_color_main_frame);
        textView.setTag(0);
        textView.setText("没有我想要的");

        textView.setPadding(v, h, v, h);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回判断是否填写实力
                Intent intent = new Intent(mActivity, SkillFeedbackkActivity.class);
                startActivityForResult(intent, 1002);
            }
        });
        wly_lyt_warp.addView(textView);
    }

    public static SkillBean getmSkillBean() {
        return mSkillBean;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.right_tv) {
            String str = "";
            //ToastUtil.showInfo(this, "选择技能:" + str);
            setResult(RESULT_OK);
            finish();
        }
    }


    /**
     * 获取技能列表
     */
    public void getData() {
        Call call = Http.links.addAllSkill();
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<SkillBean> list = JSON.parseArray(jsObj.getJSONArray("info").toJSONString(), SkillBean.class);
                    if (list != null)
                        setData(list);
                } else {

                }
            }

            @Override
            public void onFail(String error) {

            }
        });
    }
}
