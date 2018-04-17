package com.benkie.hjw.ui.skill;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.SkillAdapter;
import com.benkie.hjw.bean.SkillDetailsBean;
import com.benkie.hjw.bean.SkillBean;
import com.benkie.hjw.constants.Constants;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.popwindow.ImagePopWindow;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.MapDistance;
import com.benkie.hjw.utils.ShareUtils;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.CircleImageView;
import com.benkie.hjw.view.HeadView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class SkillDetailsActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.iv_img)
    CircleImageView iv_img;//iv_zan

    @BindView(R.id.tv_zan_count)
    TextView tv_zan_count;

    @BindView(R.id.iv_zan)
    ImageView iv_zan;//点赞

    @BindView(R.id.iv_collection)
    ImageView iv_collection;//收藏

    @BindView(R.id.iv_phone)
    ImageView iv_phone;//拨打电话

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_distance)
    TextView tv_distance;//距离

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.tv_fuwu)
    TextView tv_fuwu;

    @BindView(R.id.tv_shili)
    TextView tv_shili;

    @BindView(R.id.pullRefreshView)
    PullToRefreshGridView pullRefreshView;
    GridView gridView;
    SkillAdapter skillAdapter;
    int userInfoId = 0;
    int sid = 0;
    int isPraise = 0; //是否点赞
    int pointNumber = 0; //点赞数量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_skill);
        ButterKnife.bind(this);
        headView.setTitle("技术人员");
        headView.setBtBack(this);
        headView.setBtImg(R.mipmap.iv_share);
        headView.setBtClickListener(this);
        Bundle bundle = getIntent().getExtras();
        sid = bundle.getInt("sid", 0);
        initView();
        getSkill();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.right_iv) {
            ShareUtils.shareSkill(this, handler, userInfoId);
        } else if (v == iv_collection) {
            int flag = (int) iv_collection.getTag();
            flag = flag == 0 ? 1 : 0;
            skillCollect(flag);
        } else if (v == iv_phone) {
            toPhone();
        } else if (v == iv_zan) {
            if (isPraise == 0) {
                addPoint();
            } else {
                delPoint();
            }
        }
    }

    private void toPhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                final String tel = (String) iv_phone.getTag();
                BaseDialog.dialogStyle1(mActivity, "是否要拨打Ta的电话？", "拨打", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();               //创建Intent对象
                        intent.setAction(Intent.ACTION_CALL);      //设置动作为拨打电话
                        intent.setData(Uri.parse("tel:" + tel));   // 设置要拨打的电话号码
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toPhone();
            } else {
                ToastUtil.showInfo(mActivity, "权限被拒绝，某些功能将无法使用");
            }
        }
    }

    private void skillCollect(final int flag) {
        if (!DataHpler.islogin()){
            BaseActivity.toLogin(this,true);
            return;
        }
        Call call = Http.links.skillCollect(DataHpler.getUserInfo().getUserid(), sid, flag);
        Http.http.call(mActivity, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    iv_collection.setTag(flag);
                    if (flag == 1) {
                        onFail("收藏成功");
                        iv_collection.setImageResource(R.mipmap.iv_mycollection);
                    } else {
                        onFail("取消收藏");
                        iv_collection.setImageResource(R.mipmap.iv_mycollection_n);
                    }
                } else {
                    onFail("提交失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    private void setViewData(SkillDetailsBean bean) {
        if (bean == null) return;
        userInfoId = bean.getUserInfoId();
        String img = bean.getImgUrl();
        Tools.loadHeadImg(mActivity, iv_img, img);

        String name = bean.getName();
        tv_name.setText(name);
        //根据经纬度 计算距离
        String distance = MapDistance.getDistance(Constants.Latitude + "", Constants.Longitude + "", bean.getLat() + "", bean.getLng() + "");
        tv_distance.setText("距项目地点 " + distance);
        String mobile = bean.getMobile();
        iv_phone.setTag(mobile);
        iv_phone.setOnClickListener(this);

        iv_zan.setOnClickListener(this);
        pointNumber = bean.getPointNumber();
        tv_zan_count.setText(pointNumber + "");
        int flag = bean.getFlag();
        if (flag==1)
            iv_collection.setImageResource(R.mipmap.iv_mycollection);
        iv_collection.setTag(flag);
        iv_collection.setOnClickListener(this);

        tv_address.setText(bean.getAddress());
        tv_fuwu.setText(bean.getServeType() == 0 ? "个人" : "团队");
        tv_shili.setText(bean.getDescribes());

        if (skillAdapter != null) {
            List<SkillBean> skillBeans = bean.getSkills();
            skillAdapter.addAll(skillBeans);
            skillAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        pullRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        pullRefreshView.setOnRefreshListener(this);
        gridView = pullRefreshView.getRefreshableView();
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(15);
        gridView.setVerticalSpacing(15);
        gridView.setOnItemClickListener(this);
        skillAdapter = new SkillAdapter(mActivity);
        skillAdapter.notifyDataSetChanged();
        gridView.setAdapter(skillAdapter);
        gridView.setOnItemClickListener(this);
        gridView.setPadding(10, 0, 10, 0);
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

    public void getSkill() {
        Call call = Http.links.userSkillInfo(DataHpler.getUserInfo().getUserid(), sid);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                isPraise = jsObj.getIntValue("isPraise");
                if (isPraise == 1) {
                    iv_zan.setImageResource(R.mipmap.ic_zan_p);
                } else {
                    iv_zan.setImageResource(R.mipmap.ic_zan_n);
                }
                if (msg == 1) {
                    JSONArray infos = jsObj.getJSONArray("info");
                    if (infos.size() == 0) return;
                    SkillDetailsBean bean = JSON.parseObject(infos.getString(0), SkillDetailsBean.class);
                    setViewData(bean);
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

    /**
     * 点赞
     */
    public void addPoint() {
        if (!DataHpler.islogin()){
            BaseActivity.toLogin(this,true);
            return;
        }
        Call call = Http.links.addPoint(DataHpler.getUserInfo().getUserid(), sid);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                isPraise = jsObj.getIntValue("isPraise");
                if (msg == 1) {
                    ++pointNumber;
                    tv_zan_count.setText(pointNumber + "");
                    iv_zan.setImageResource(R.mipmap.ic_zan_p);
                    isPraise = 1;
                    onFail("点赞成功");
                } else {
                    onFail("点赞失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    /**
     * 取消点赞
     */
    public void delPoint() {
        if (!DataHpler.islogin()){
            BaseActivity.toLogin(this,true);
            return;
        }
        Call call = Http.links.delPoint(DataHpler.getUserInfo().getUserid(), sid);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    --pointNumber;
                    tv_zan_count.setText(pointNumber + "");
                    iv_zan.setImageResource(R.mipmap.ic_zan_n);
                    isPraise = 0;
                    onFail("取消点赞");
                } else {
                    onFail("取消失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }


}
