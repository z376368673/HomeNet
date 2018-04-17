package com.benkie.hjw.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.bean.UserInfo;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class ModifyNickNameActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.ed_nickname)
    EditText ed_nickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nick_name);
        ButterKnife.bind(this);
        headView.setTitle("修改昵称");
        headView.setBtBack(this);
        headView.setBtText("保存");
        headView.setBtClickListener(this);
        String name = getIntent().getStringExtra("NickName");
        if (name!=null)
        ed_nickname.setText(name);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        String nickName = ed_nickname.getText().toString();
        if (TextUtils.isEmpty(nickName.trim())){
            ToastUtil.showInfo(mActivity,"请输入正确的昵称");
            return;
        }
        upItemName(nickName);
    }
    private void upItemName(final String name ) {
        Call call = Http.links.updateUserName(DataHpler.getToken(),name);
        Http.http.call(mActivity,call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                   UserInfo userInfo =  DataHpler.getUserInfo();
                    userInfo.setName(name);
                    DataHpler.setUserInfo(JSON.toJSONString(userInfo));
                    onFail("保存成功");
                    Intent intent = new Intent();
                    intent.putExtra("NickName", name);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    onFail("保存失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

}
