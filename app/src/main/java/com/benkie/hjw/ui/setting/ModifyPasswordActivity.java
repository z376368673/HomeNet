package com.benkie.hjw.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.ui.RegisterActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class ModifyPasswordActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.tv_old_pw)
    EditText tv_old_pw;
    @BindView(R.id.tv_new_pw)
    EditText tv_new_pw;
    @BindView(R.id.tv_new_pw1)
    EditText tv_new_pw1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.bind(this);
        headView.setTitle("修改密码");
        headView.setBtBack(this);
    }

    public void forgetPassword(View view){
        ToastUtil.showInfo(mActivity,"忘记密码");
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("type",2);
        startActivityForResult(intent,1000);
    }
    public void confirm(View view){
        String oldPwd = tv_old_pw.getText().toString();
        String loginPwd = tv_new_pw.getText().toString();
        String loginPwd1 = tv_new_pw1.getText().toString();
        if (oldPwd==null||oldPwd.trim().equals("")){
            ToastUtil.showInfo(mActivity,"请填写旧密码");
        }else if (loginPwd==null||loginPwd.trim().equals("")){
            ToastUtil.showInfo(mActivity,"请填写新密码");
        }else if (loginPwd1==null||loginPwd1.trim().equals("")){
            ToastUtil.showInfo(mActivity,"请再次填写新密码");
        }else if (!loginPwd.equals(loginPwd1)){
            ToastUtil.showInfo(mActivity,"两次密码不一致");
        }else {
            updatePwd(oldPwd,loginPwd);
        }

    }


    private void updatePwd(String oldPwd,String loginPwd){
        Call call = Http.links.updatePwd(DataHpler.getToken(),loginPwd,oldPwd);
        Http.http.call(mActivity,call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg==1){
                    ToastUtil.showInfo(mActivity,"修改成功");
                    DataHpler.exit();
                    BaseActivity.toLogin(mActivity);
                    finish();
                }else if(msg==2){
                    ToastUtil.showInfo(mActivity,"密码不对");
                }else if(msg==0){
                    ToastUtil.showInfo(mActivity,"未登陆");
                }else {
                    ToastUtil.showInfo(mActivity,"修改失败");
                }
            }
            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity,""+error);
            }
        });
    }
}
