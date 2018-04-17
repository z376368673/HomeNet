package com.benkie.hjw.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.ui.mine.CollectActivity;
import com.benkie.hjw.ui.mine.NewsActivity;
import com.benkie.hjw.ui.mine.RecommendActivity;
import com.benkie.hjw.ui.mine.SettingActivity;
import com.benkie.hjw.ui.product.ProjectActivity;
import com.benkie.hjw.ui.setting.ModifyAccountActivity;
import com.benkie.hjw.ui.skill.SkillPublicActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by 37636 on 2018/1/18.
 */

public class Home3Fragment extends BaseFragment {

    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.tv_project)
    TextView tv_project;
    @BindView(R.id.tv_skill)
    TextView tv_skill;
    @BindView(R.id.tv_collect)
    TextView tv_collect;

    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.tv_tuijian)
    TextView tv_tuijian;
    @BindView(R.id.tv_setting)
    TextView tv_setting;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = View.inflate(getActivity(),R.layout.fragment_home3,null);
        View view = inflater.inflate(R.layout.fragment_home3, container, false);
        ButterKnife.bind(this, view);
        initView();
        setUserInfo();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
    }

    private void setUserInfo() {
        String name = "未登录";
        if (DataHpler.islogin()) {
            name = DataHpler.getUserInfo().getName();
            tv_nickname.setText(name);
            Tools.loadHeadImg(getActivity(), iv_head, DataHpler.getUserInfo().getImgUrl());
        } else {
            tv_nickname.setText(name);
            iv_head.setImageResource(R.mipmap.iv_head);
        }
    }

    public void initView() {
        iv_head.setOnClickListener(this);
        tv_nickname.setOnClickListener(this);
        tv_project.setOnClickListener(this);
        tv_skill.setOnClickListener(this);
        tv_collect.setOnClickListener(this);
        tv_msg.setOnClickListener(this);
        tv_tuijian.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
    }

    /**
     * 更改账户
     *
     * @param view
     */
    public void modifyAccount(View view) {
        Intent intent = new Intent(mActivity, ModifyAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (BaseActivity.islogin(context))
            switch (view.getId()) {
                case R.id.iv_head:
                    modifyAccount(null);
                    break;
                case R.id.tv_nickname:
                    break;
                case R.id.tv_project:
                    startActivity(new Intent(getContext(), ProjectActivity.class));
                    break;
                case R.id.tv_skill:
                    startActivity(new Intent(getContext(), SkillPublicActivity.class));
                    break;
                case R.id.tv_collect:
                    startActivity(new Intent(getContext(), CollectActivity.class));
                    break;
                case R.id.tv_msg:
                    startActivity(new Intent(getContext(), NewsActivity.class));
                    break;
                case R.id.tv_tuijian:
                    startActivity(new Intent(getContext(), RecommendActivity.class));
                    break;
                case R.id.tv_setting:
                    startActivity(new Intent(getContext(), SettingActivity.class));
                    break;
            }
    }

}
