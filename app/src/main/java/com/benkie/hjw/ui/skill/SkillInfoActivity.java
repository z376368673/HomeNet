package com.benkie.hjw.ui.skill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.benkie.hjw.BroadcastReceiver.BeseBroadcastReceiver;
import com.benkie.hjw.R;
import com.benkie.hjw.bean.SkillBean;
import com.benkie.hjw.bean.SkillServiceBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.map.MyMapActivity;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.ui.skill.MyIntroduceActivity;
import com.benkie.hjw.ui.skill.SkillListAllActivity;
import com.benkie.hjw.utils.CheckUtil;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class SkillInfoActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.tv_sample)
    TextView tv_sample;

    @BindView(R.id.radio)
    RadioGroup radio;

    private String address = "";
    private String describes = "";
    private int serveType = 0;
    private double lat = 0.00;
    private double lng = 0.00;

    private boolean isEdit = false;

    private boolean isModify = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skillinfo);
        ButterKnife.bind(this);
        headView.setTitle("个人信息");
        headView.setBtText("保存");
        headView.setBtBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        headView.setBtClickListener(this);
        isEdit = getIntent().getBooleanExtra("IsEdit", false);
        initView();
    }

    @Override
    public void onBackPressed() {
        if (isModify)
            BaseDialog.dialogStyle2(mActivity, "您的修改还未保存，您确认退出吗？", "保存", "放弃", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    if (tag == 1) {
                        saveSkillInfo();
                    } else {
                        finish();
                    }
                }
            });
        else  super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void initView() {
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                isModify= true;
                if (i == R.id.radio1) {
                    serveType = 0;
                    // skillServiceBean.setServeType(0);
                } else if (i == R.id.radio2) {
                    // skillServiceBean.setServeType(1);
                    serveType = 1;
                }
            }
        });
        getUserSkillInfo(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.right_tv) {
            saveSkillInfo();
        }
    }

    /**
     * 选择地址
     *
     * @param view
     */
    public void onClickAddress(View view) {
        Intent intent = new Intent(mActivity, MyMapActivity.class);
        intent.putExtra("Name", "常住地址");
        startActivityForResult(intent, 1000);
    }

    /**
     * 我的实力
     *
     * @param view
     */
    public void onClickShili(View view) {
        //返回判断是否填写实力
        Intent intent = new Intent(mActivity, MyIntroduceActivity.class);
        if (!TextUtils.isEmpty(describes)) {
            intent.putExtra("Des", describes);
        }
        startActivityForResult(intent, 1001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", "requestCode = " + requestCode + " resultCode = " + resultCode);
        if (requestCode == 1000 & resultCode == Activity.RESULT_OK) {
            //选择地址
            isModify= true;
            LatLonPoint latLonPoint = data.getExtras().getParcelable("LatLonPoint");
            address = data.getExtras().getString("Address");
            String title = data.getExtras().getString("Title");
            tv_address.setText(title);
            lat = latLonPoint.getLatitude();
            lng = latLonPoint.getLongitude();
        } else if (requestCode == 1001 & resultCode == Activity.RESULT_OK) {
            isModify= true;
            //我的实力
            describes = data.getExtras().getString("info");
            tv_sample.setText(describes);
        }
    }

    /**
     * 获取用户信息
     * private String address="";
     * private String describes="";
     * private int serveType = 0;
     * private double lat=0.00;
     * private double lng=0.00;
     *
     * @param isShow
     */
    public void getUserSkillInfo(final boolean isShow) {
        Call call = Http.links.mySkillInfo(DataHpler.getUserInfo().getUserid());
        Http.http.call(this, call, isShow, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    address = jsObj.getString("address");
                    describes = jsObj.getString("describes");
                    serveType = jsObj.getIntValue("serveType");
                    lat = jsObj.getDoubleValue("lat");
                    lng = jsObj.getDoubleValue("lng");
                    RadioButton radioButton = (RadioButton) radio.getChildAt(serveType);
                    radioButton.setChecked(true);
                    tv_sample.setText(describes);
                    tv_address.setText(address);
                } else {
                   // onFail("暂无信息");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    /**
     * 保存 或者 修改
     */
    private void saveSkillInfo() {
        if (TextUtils.isEmpty(tv_address.getText())) {
            ToastUtil.showInfo(mActivity, "请选择地址");
            return;
        }
        int des_len =   2;
        boolean[] itemChecks = {
                CheckUtil.isText(address,2),
                CheckUtil.isText(describes,des_len)
        };
        String[] itemTips = {
                "您还没选择地址呢",
                "我的实力不能为空，并且不少于"+des_len+"字"
        };
        int i = CheckUtil.checkAll(itemChecks);
        if (i < itemChecks.length) {
            Toast.makeText(this, itemTips[i], Toast.LENGTH_SHORT).show();
            return;
        }
        Call call;
        if (isEdit) {
            call = Http.links.edtiUserSkill(DataHpler.getUserInfo().getUserid(), address, describes, lng, lat, serveType);
        } else {
            call = Http.links.saveUserSkill(DataHpler.getUserInfo().getUserid(), address, describes, lng, lat, serveType);
        }

        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    if (isEdit){
                        BeseBroadcastReceiver.sendToSkill(SkillInfoActivity.this,1);
                        ToastUtil.showInfo(mActivity, "修改成功");
                    }else {
                        ToastUtil.showInfo(mActivity, "保存成功");
                    }
                    setResult(RESULT_OK);
                    finish();
                } else {
                    ToastUtil.showInfo(mActivity, "保存失败");
                }
            }
            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, "保存失败" + error);
            }
        });
    }

}
