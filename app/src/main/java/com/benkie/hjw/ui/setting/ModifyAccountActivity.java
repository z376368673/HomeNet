package com.benkie.hjw.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.CircleImageView;
import com.benkie.hjw.view.HeadView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class ModifyAccountActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;


    @BindView(R.id.cv_head)
    CircleImageView cv_head;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_phone)
    TextView tv_phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account);
        ButterKnife.bind(this);
        headView.setTitle("更改账户");
        headView.setBtBack(this);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        tv_nickname.setText(DataHpler.getUserInfo().getName());
        tv_phone.setText(DataHpler.getUserInfo().getMobile());
       String headImg  = DataHpler.getUserInfo().getImgUrl();
       if (headImg!=null){
           Tools.loadHeadImg(mActivity,cv_head,headImg);
       }else {
           cv_head.setImageResource(R.mipmap.iv_head);
       }
    }
    private void initView() {
        cv_head.setOnClickListener(this);
        tv_nickname.setOnClickListener(this);
        tv_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_nickname) {
            modifyNickName(tv_nickname);
        } else if (v == tv_phone) {
            modifyPhone(tv_phone);
        } else if (v == cv_head) {
            modifyHead(cv_head);
        }
    }

    /**
     * 更改手机号
     *
     * @param view
     */
    public void modifyPhone(View view) {
        Intent intent = new Intent(this, ModifyPhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent,2);
        startActivity(intent);
    }

    /**
     * 更改昵称
     *
     * @param view
     */
    public void modifyNickName(View view) {
        Intent intent = new Intent(this, ModifyNickNameActivity.class);
        intent.putExtra("NickName", tv_nickname.getText().toString());
        startActivityForResult(intent, 1);
    }

    /**
     * 更改头像
     *
     * @param view
     */
    public void modifyHead(View view) {
        Intent intent = new Intent(this, ModifyHeadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent,3);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String name = data.getStringExtra("NickName");
                tv_nickname.setText(name);
            }
        }else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            initData();
        }else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            initData();
        }
    }

}
