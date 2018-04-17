/*
package com.benkie.hjw.ui.skill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.SkillEidtAdapter;
import com.benkie.hjw.bean.SkillBean;
import com.benkie.hjw.bean.SkillServiceBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.map.MyMapActivity;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class SkillEditActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.tv_sample)
    TextView tv_sample;

    @BindView(R.id.radio)
    RadioGroup radio;

    @BindView(R.id.ll_addskill)
    LinearLayout ll_addskill;

    @BindView(R.id.pullRefreshView)
    PullToRefreshGridView pullRefreshView;
    GridView gridView;
    SkillEidtAdapter skillAdapter;

    SkillServiceBean skillServiceBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_edit);
        ButterKnife.bind(this);
        headView.setTitle("技术服务");
        headView.setBtBack(this);
        headView.setBtText("保存");
        headView.setBtClickListener(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.right_tv) {
            saveSkillInfo();
        }
    }

    private void initView() {
        skillServiceBean = new SkillServiceBean();
        pullRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullRefreshView.setOnRefreshListener(this);
        gridView = pullRefreshView.getRefreshableView();
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(15);
        gridView.setVerticalSpacing(15);
        gridView.setOnItemClickListener(this);
        skillAdapter = new SkillEidtAdapter(mActivity);
        gridView.setAdapter(skillAdapter);
        gridView.setOnItemClickListener(this);
        gridView.setPadding(10, 0, 10, 0);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio1) {
                    skillServiceBean.setServeType(0);
                } else if (i == R.id.radio2) {
                    skillServiceBean.setServeType(1);
                }
            }
        });
//        skillAdapter.setSkillActionLintener(new SkillEidtAdapter.SkillActionLintener() {
//            @Override
//            public void imgOnclickListener(SkillBean item) {
//                Intent intent = new Intent(mActivity, UpDataSkillImgActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("Title", item);
//                bundle.putInt("Action",1);
//                intent.putExtras(bundle);
//                startActivityForResult(intent,1003);
//            }
//        });
        getData(true);
    }

    public void getData(final boolean isFrist) {
        Call call = Http.links.getUserSkill(DataHpler.getUserInfo().getUserid());
        Http.http.call(this,call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    JSONArray infos = jsObj.getJSONArray("info");
                    if (infos.size() == 0) {
                        skillServiceBean.setId(-1);
                    }else {
                        SkillServiceBean bean = JSON.parseObject(infos.getString(0), SkillServiceBean.class);
                        if (isFrist&&bean!=null) {
                            skillServiceBean = bean;
                            tv_sample.setText(skillServiceBean.getDescribes());
                            tv_address.setText(bean.getAddress());
                            RadioButton radioButton = (RadioButton) radio.getChildAt(bean.getServeType());
                            radioButton.setChecked(true);
                        }
                        if (skillAdapter != null) {
                            List<SkillBean> skillBeans = bean.getSkills();
                            skillAdapter.clear();
                            skillAdapter.addAll(skillBeans);
                            skillAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    onFail("获取数据失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    public void onClickAddress(View view) {
        Intent intent = new Intent(mActivity, MyMapActivity.class);
        intent.putExtra("Name","常住地址");
        startActivityForResult(intent, 1000);
    }

    public void onClickShili(View view) {
        //返回判断是否填写实力
        Intent intent = new Intent(mActivity, MyIntroduceActivity.class);
        if (skillServiceBean!=null&&!TextUtils.isEmpty(skillServiceBean.getDescribes())){
            intent.putExtra("Des",skillServiceBean.getDescribes());
        }
        startActivityForResult(intent, 1001);
    }

    public void onClickSkilli(View view) {
        if (TextUtils.isEmpty(tv_address.getText())) {
            ToastUtil.showInfo(mActivity, "你还没选择地址");
            return;
        }else if (skillServiceBean.getId()<=0){
            ToastUtil.showInfo(mActivity, "请先保存地址与服务信息");
            return;
        }
        Intent intent = new Intent(mActivity, SkillListAllActivity.class);
        startActivityForResult(intent, 1002);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult","requestCode = "+requestCode+" resultCode = "+resultCode);
        if (requestCode == 1000 & resultCode == Activity.RESULT_OK) {
            LatLonPoint latLonPoint = data.getExtras().getParcelable("LatLonPoint");
            String address = data.getExtras().getString("Address");
            String title = data.getExtras().getString("Title");
            tv_address.setText(title);
            if (skillServiceBean != null) {
                skillServiceBean.setAddress(address);
                skillServiceBean.setLng(latLonPoint.getLongitude());
                skillServiceBean.setLat(latLonPoint.getLatitude());
            }
        } else if (requestCode == 1001 & resultCode == Activity.RESULT_OK) {
            String info = data.getExtras().getString("info");
            if (skillServiceBean != null){
                skillServiceBean.setDescribes(info);
                tv_sample.setText(info);
            }
        } else if (requestCode == 1002 & resultCode == Activity.RESULT_OK) {
            SkillBean bean = SkillListAllActivity.getmSkillBean();
            if (bean!=null)
            addSkill(bean);
        } else if (requestCode == 1003 & resultCode == Activity.RESULT_OK) {
           getData(false);
        }
    }
    */
/**
     *添加技能
     *//*

    private void addSkill(final SkillBean skillBean) {
        if(skillServiceBean.getId()>0){
            Call call = Http.links.addSkill(DataHpler.getUserInfo().getUserid(), skillServiceBean.getId(),skillBean.getId());
            Http.http.call(mActivity,call, true, new Http.JsonCallback() {
                @Override
                public void onResult(String json, String error) {
                    JSONObject jsObj = JSON.parseObject(json);
                    int msg = jsObj.getIntValue("msg");
                    if (msg==1){
                        skillAdapter.add(skillBean);
                        skillAdapter.notifyDataSetChanged();
                        //getData(false);
                    }
                }

                @Override
                public void onFail(String error) {
                    ToastUtil.showInfo(mActivity, "保存失败" + error);
                }
            });
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pullRefreshView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pullRefreshView.onRefreshComplete();
    }

    */
/**
     * 保存
     *//*

    private void saveSkillInfo() {
        if (TextUtils.isEmpty(tv_address.getText())) {
            ToastUtil.showInfo(mActivity, "请选择地址");
            return;
        }
        Call call = Http.links.saveSkillInfo(DataHpler.getUserInfo().getUserid(),
                skillServiceBean.getAddress(), skillServiceBean.getDescribes(),
                skillServiceBean.getLng(), skillServiceBean.getLat(), skillServiceBean.getServeType());
        Http.http.call(mActivity,call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    int userSkillId = jsObj.getIntValue("userSkillId ");
                    ToastUtil.showInfo(mActivity, "保存成功");
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
*/
