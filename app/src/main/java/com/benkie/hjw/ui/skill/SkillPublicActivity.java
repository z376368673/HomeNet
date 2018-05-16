package com.benkie.hjw.ui.skill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.BroadcastReceiver.BeseBroadcastReceiver;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.SkillEidtAdapter;
import com.benkie.hjw.bean.SkillBean;
import com.benkie.hjw.bean.SkillServiceBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.AgreementActivity;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.ui.pay.Skill_PayActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class SkillPublicActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.ll_open)
    LinearLayout ll_open;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_xufei)
    TextView tv_xufei;

    @BindView(R.id.ll_zan)
    LinearLayout ll_zan;
    @BindView(R.id.tv_zan_lack)
    TextView tv_zan_lack;
    @BindView(R.id.tv_zan_count)
    TextView tv_zan_count;
    @BindView(R.id.tv_wxpay)
    TextView tv_wxpay;

    @BindView(R.id.tv_skill_info)
    TextView tv_skill_info;

    @BindView(R.id.pullRefreshView)
    PullToRefreshGridView pullRefreshView;
    GridView gridView;
    SkillEidtAdapter skillAdapter;

    SkillServiceBean sBean;
    boolean isEdit = false;
    int status = 0;//是否可以发布技能  0 非会员 1 会员, 2 集赞中 ，3 过期会员
    int isData = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_public);
        ButterKnife.bind(this);
        headView.setTitle("技能发布");
        headView.setBtBack(this);
        headView.setBtClickListener(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (tv_xufei == v) {
            Intent intent = new Intent(this, Skill_PayActivity.class);
            intent.putExtra("type",1);
            startActivity(intent);
        } else if (tv_wxpay == v) {
            Intent intent = new Intent(this, Skill_PayActivity.class);
            intent.putExtra("type",0);
            startActivity(intent);
        }
    }

    private void initView() {
        tv_xufei.setOnClickListener(this);
        tv_wxpay.setOnClickListener(this);

        sBean = new SkillServiceBean();
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
        skillAdapter.setSkillActionLintener(new SkillEidtAdapter.SkillActionLintener() {
            @Override
            public void imgOnclickListener(SkillBean item) {
                Intent intent = new Intent(mActivity, UpskillImgActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Title", item);
                bundle.putInt("Action", 1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1003);
            }

            @Override
            public void fabuListener(SkillBean item) {
                if (status == 1) {
                    fabuSkill(item);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing())
                            BeseBroadcastReceiver.sendToSkill(SkillPublicActivity.this,1);
                        }
                    },1000);
                }else {
                    //跳转到集赞支付界面，开通会员
                    Intent intent = new Intent(mActivity, Skill_PayActivity.class);
                    startActivityForResult(intent,1004);
                    return;
                }
            }

            @Override
            public void checkedChange(SkillBean item) {
                modifyCertificate(item);
            }
        });
        getData(true);

    }
    /**
     * 修改证书
     *
     * @param item
     */
    private void modifyCertificate(final SkillBean item) {
        final int certificate = item.getCertificate() == 0 ? 1 : 0; //取反状态提交
        Call call = Http.links.updateSkill(item.getId(), item.getStatus(), certificate);
        Http.http.call(mActivity, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    item.setCertificate(certificate);
                    //skillAdapter.notifyDataSetChanged();
                    BeseBroadcastReceiver.sendToSkill(SkillPublicActivity.this,1);
                } else {
                    ToastUtil.showInfo(mActivity, "数据错误");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });


    }

    /**
     * 发布技能
     *
     * @param item
     */
    private void fabuSkill(final SkillBean item) {
        final int status = item.getStatus() == 0 ? 1 : 0;//取反状态提交
        Call call = Http.links.updateSkill(item.getId(), status, item.getCertificate());
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    int s = item.getStatus() == 0 ? 1 : 0;
                    item.setStatus(status);
                    if (s == 1) {
                        ToastUtil.showInfo(mActivity, "发布成功");
                        item.setStatus(1);
                    } else {
                        ToastUtil.showInfo(mActivity, "发布已取消");
                        item.setStatus(0);
                    }
                    skillAdapter.notifyDataSetChanged();

                } else {
                    ToastUtil.showInfo(mActivity, "数据错误");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    /**
     * 获取此界面信息
     *
     * @param iShow
     */
    public void getData(final boolean iShow) {
        Call call = Http.links.myUserSkill(DataHpler.getUserInfo().getUserid());
        Http.http.call(this, call, iShow, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                isData = jsObj.getIntValue("isData");
                int count = jsObj.getIntValue("count");
                int days = jsObj.getIntValue("days");
                int skillPraise = jsObj.getIntValue("skillPraise");
                status = jsObj.getIntValue("status");
                tv_date.setText(String.format(getResources().getString(R.string.member_rest), days>=0?days:0));
                tv_zan_count.setText(String.format(getResources().getString(R.string.zan_count), count));
                tv_zan_lack.setText(String.format(getResources().getString(R.string.zan_lack), skillPraise - count));
                if (days > 1) {
                    tv_xufei.setVisibility(View.INVISIBLE);
                } else {
                    tv_xufei.setVisibility(View.VISIBLE);
                }
                if (isData == 1) {
                    tv_skill_info.setHint(" ");
                    isEdit = true;
                    JSONArray infos = jsObj.getJSONArray("info");
                    if (infos.size() > 0) {
                        sBean = JSON.parseObject(infos.getString(0), SkillServiceBean.class);
                        List<SkillBean> skillBeans = sBean.getSkills();
                        if (skillBeans == null) return;
                        skillAdapter.clear();
                        skillAdapter.addAll(skillBeans);
                        skillAdapter.notifyDataSetChanged();
                    }
                    setStatus(status);
                } else {
                    tv_skill_info.setHint("(未填写)");
                    isEdit = false;
                    ll_open.setVisibility(View.GONE);
                    ll_zan.setVisibility(View.GONE);
                    //onFail("暂无信息");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    /**
     * 根据会员状态判断显示内容
     * @param status
     */
    public void setStatus(int status) {
        if (status == 0) {
            ll_open.setVisibility(View.GONE);
            ll_zan.setVisibility(View.GONE);
        } else if (status == 1) {
            //本地保存是否是会员
            ll_open.setVisibility(View.VISIBLE);
            ll_zan.setVisibility(View.GONE);
        } else if(status == 2){
            ll_open.setVisibility(View.GONE);
            ll_zan.setVisibility(View.VISIBLE);
        }else {
            ll_open.setVisibility(View.VISIBLE);
            ll_zan.setVisibility(View.GONE);
        }
    }

    /**
     * 添加技能
     *
     * @param view
     */
    public void onClickSkilli(View view) {
        if (isData==1){
            Intent intent = new Intent(mActivity, SkillListAllActivity.class);
            startActivityForResult(intent, 1002);
        }else {
            ToastUtil.showInfo(mActivity,"请您先填写基本资料");
        }

    }

    public void onClickToInfo(View view) {
        Intent intent = new Intent(mActivity, SkillInfoActivity.class);
        intent.putExtra("IsEdit", isEdit);
        startActivityForResult(intent, 1001);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", "requestCode = " + requestCode + " resultCode = " + resultCode);
        if (requestCode == 1002 & resultCode == Activity.RESULT_OK) {
            SkillBean bean = SkillListAllActivity.getmSkillBean();
            if (bean != null)
                addSkill(bean);
        } else if (requestCode == 1003 & resultCode == Activity.RESULT_OK) {
            getData(false);
        } else if (requestCode == 1001 & resultCode == Activity.RESULT_OK) {
            getData(false);
        }else if (requestCode == 1004 & resultCode == Activity.RESULT_OK) {
            getData(true);
        }
    }

    /**
     * 添加技能
     */
    private void addSkill(final SkillBean skillBean) {
        if (sBean.getId() > 0) {
            int uid = DataHpler.getUserInfo().getUserid();
            int sid = sBean.getId();
            int tid = skillBean.getId();
            Call call = Http.links.addSkill(uid, sid, tid);
            Http.http.call(mActivity, call, true, new Http.JsonCallback() {
                @Override
                public void onResult(String json, String error) {
                    JSONObject jsObj = JSON.parseObject(json);
                    int msg = jsObj.getIntValue("msg");

                    if (msg == 1) {
//                        skillAdapter.add(skillBean);
//                        skillAdapter.notifyDataSetChanged();
                        getData(false);
                    }else if (msg==2){

                        onFail("此技能已存在,不能重复添加");
                    }else {
                        error = jsObj.getString("error");
                        onFail(error);
                    }
                }

                @Override
                public void onFail(String error) {
                    ToastUtil.showInfo(mActivity, error);
                }
            });
        } else {
            ToastUtil.showInfo(mActivity, "服务器异常");
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


}
