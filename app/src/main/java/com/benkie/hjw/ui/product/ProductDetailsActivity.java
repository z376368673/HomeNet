package com.benkie.hjw.ui.product;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.ChannelAdapter;
import com.benkie.hjw.application.BaseApp;
import com.benkie.hjw.bean.Channel;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.bean.ProducDetailstBean;
import com.benkie.hjw.bean.SkillBean;
import com.benkie.hjw.bean.TypeBean;
import com.benkie.hjw.constants.Constants;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.listener.ItemDragHelperCallBack;
import com.benkie.hjw.listener.OnChannelDragListener;
import com.benkie.hjw.listener.OnChannelListener;
import com.benkie.hjw.map.LocationUtils;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.popwindow.ImagePopWindow;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.ui.mine.ComplaintActivity;
import com.benkie.hjw.utils.LogUtils;
import com.benkie.hjw.utils.ShareUtils;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.CircleImageView;
import com.benkie.hjw.view.HeadView;
import com.benkie.hjw.view.LabelTextView;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import am.widget.wraplayout.WrapLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

import static com.benkie.hjw.adapter.CategoryAdapter.context;
import static com.benkie.hjw.bean.Channel.TYPE_MY_CHANNEL;

/**
 * Created by 37636 on 2018/1/27.
 */

public class ProductDetailsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private int FormType = 0; // 0 从首页进来 1 从未发布的进来  2 从已经发布的进来
    @BindView(R.id.headView)
    HeadView headView;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.wly_lyt_warp)
    WrapLayout wly_lyt_warp;

    @BindView(R.id.iv_head)
    CircleImageView iv_head;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.ll_sc)
    LinearLayout ll_sc;
    @BindView(R.id.ll_edit)
    LinearLayout ll_edit;
    @BindView(R.id.ll_warning)
    LinearLayout ll_warning;

    @BindView(R.id.ll_zan)
    LinearLayout ll_zan; //此功能已取消不用 不再显示了，可以删除

    @BindView(R.id.iv_collection)
    ImageView iv_collection;//收藏

    @BindView(R.id.iv_phone)
    ImageView iv_phone;//拨打电话

    @BindView(R.id.tv_edit)
    TextView tv_edit;//编辑按钮
    @BindView(R.id.tv_add)
    TextView tv_add;//编辑按钮

    @BindView(R.id.tv_info)
    TextView tv_info;//项目描述

    @BindView(R.id.tv_address)
    TextView tv_address;//项目地址

    @BindView(R.id.tv_date)
    TextView tv_date;//项目完工日期

    @BindView(R.id.tv_zan_count)
    TextView tv_zan_count;//集赞数量
    @BindView(R.id.tv_zan_lack)
    TextView tv_zan_lack;//缺少数量


    WorksPagerAdapter adapter;

    int pid = 0;
    int pos = 0;
    private int pointNumber = 0;
    private int itemPraise = 0;
    private String name = "项目详情";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);

        headView.setBtBack(this);
        headView.setBtImg(R.mipmap.iv_share);
        headView.setBtClickListener(this);
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("Name", "项目名称");
        headView.setTitle(name);
        pid = bundle.getInt("pid");
        FormType = bundle.getInt("FormType");
        initView();
        geteData();
    }

    public void complain(View view) {
        if (BaseActivity.islogin(mActivity)) {
            Intent intent = new Intent(mActivity, ComplaintActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);
        } else {
            BaseActivity.toLogin(context);
        }

    }

    private void initView() {
        tv_edit.setOnClickListener(this);
        tv_add.setOnClickListener(this);

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
            ShareUtils.shareProduct(this, handler, pid,name);
        } else if (v == iv_collection) {
            int flag = (int) iv_collection.getTag();
            flag = flag == 0 ? 1 : 0;
            itemCollect(flag);
        } else if (v == iv_phone) {
            toPhone();
        } else if (v == tv_edit) {
            upDataImg();
        } else if (v == tv_add) {
            addImg();
        }
    }

    private void toPhone() {
        if (!DataHpler.islogin()){
            BaseActivity.toLogin(this,true);
            return;
        }
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

    /**
     * 收藏
     *
     * @param flag
     */
    private void itemCollect(final int flag) {
        if (!DataHpler.islogin()){
            BaseActivity.toLogin(this,true);
            return;
        }
        Call call = Http.links.itemCollect(DataHpler.getUserInfo().getUserid(), pid, flag);
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

    /**
     * 去编辑图片
     */
    private void upDataImg() {
        if (adapter == null) return;
        Intent intent = new Intent(mActivity, AddImgActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pid", pid);
        bundle.putBoolean("isEdit", true);
        bundle.putSerializable("Bean", adapter.getList().get(pos));
        intent.putExtras(bundle);
        startActivityForResult(intent, 1001);
    }

    private void addImg() {
        Intent intent = new Intent(mActivity, AddImgActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pid", pid);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1002);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1001 || requestCode == 1002) {
            //修改项目图片返回 刷新界面
            geteData();
        }
    }

    private void geteData() {
        Call call = Http.links.productInfo(DataHpler.getUserInfo().getUserid(), pid);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                itemPraise = jsObj.getIntValue("itemPraise");
                if (msg == 1) {
                    JSONArray jsArr = jsObj.getJSONArray("info");
                    if (jsArr != null && jsArr.size() > 0) {
                        ProducDetailstBean detailstBean = JSON.parseObject(jsArr.getString(0), ProducDetailstBean.class);
                        if (detailstBean != null) {
                            setProductData(detailstBean);
                            List<Picture> list = detailstBean.getImgs();
                            if (list != null)
                                initViewPager(list);
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

    private void setProductData(ProducDetailstBean bean) {
        Tools.loadHeadImg(mActivity, iv_head, bean.getImgUrl());
        tv_name.setText(bean.getUname());
        tv_address.setText("项目所在地：" +bean.getCity()+bean.getAddress());
        tv_date.setText("完工日期：" + bean.getFinishDate());
        int flag = bean.getFlag();
        iv_collection.setTag(flag);
        iv_collection.setOnClickListener(this);
        iv_phone.setTag(bean.getMobile());
        iv_phone.setOnClickListener(this);
        pointNumber = bean.getItemPoint();

        if (flag == 0)
            iv_collection.setImageResource(R.mipmap.iv_mycollection_n);
        else
            iv_collection.setImageResource(R.mipmap.iv_mycollection);


        if (FormType == 0) {
            ll_edit.setVisibility(View.GONE);
            ll_sc.setVisibility(View.VISIBLE);
            ll_zan.setVisibility(View.GONE);
            ll_warning.setVisibility(View.VISIBLE);
        } else if (FormType == 1) {
            ll_edit.setVisibility(View.VISIBLE);
            ll_sc.setVisibility(View.GONE);
            ll_zan.setVisibility(View.GONE);
            ll_warning.setVisibility(View.GONE);
        } else if (FormType == 2) {
            ll_edit.setVisibility(View.GONE);
            ll_sc.setVisibility(View.GONE);
            ll_zan.setVisibility(View.GONE);
            ll_warning.setVisibility(View.GONE);
            //非推荐 显示此信息
            if (bean.getTag()==1){
                ll_zan.setVisibility(View.GONE);
            }
            tv_zan_count.setText(bean.getItemGather() +"");
            tv_zan_lack.setText(itemPraise -bean.getItemGather()+"");
        }
    }

    private void tab(int position) {
        if (adapter == null) return;
        if (adapter.getList().size() == 0) {
            tv_info.setText("没有图片描述");
            // indicator.setText(String.format(Locale.CHINA, "%d/%d", 0, 0));
        } else {
            Picture picture = adapter.getList().get(position);
            tv_info.setText(picture.getDescribes());
            List<TypeBean> service = picture.getService();
            wly_lyt_warp.removeAllViews();
            for (int i = 0; i < service.size(); i++) {
                TypeBean bean = service.get(i);
                TextView textView = new TextView(mActivity);
                textView.setText(bean.getName());
                textView.setTextSize(15);
                textView.setTextColor(getResources().getColor(R.color.colorMain));
                textView.setPadding(0, 10, 20, 10);
                wly_lyt_warp.addView(textView);
            }
//            indicator.setText(String.format(Locale.CHINA, "%d/%d", position + 1, sizeColorList.size()));
        }
    }

    private void initViewPager(List<Picture> list) {
        viewPager.addOnPageChangeListener(this);
        adapter = new WorksPagerAdapter(list);
        viewPager.setAdapter(adapter);
        tab(0);
    }

    private class WorksPagerAdapter extends PagerAdapter {
        List<Picture> list;
        ImagePopWindow popWindow = new ImagePopWindow(mActivity);

        public WorksPagerAdapter(List<Picture> list) {
            this.list = list;
            popWindow.setData(list);
        }

        public List<Picture> getList() {
            return list;
        }

        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = View.inflate(mActivity, R.layout.item_pruduct_pager, null);
            Picture picture = list.get(position);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_img);
            TextView tv1 = (TextView) view.findViewById(R.id.text1);
            TextView tv2 = (TextView) view.findViewById(R.id.text2);
            Tools.loadImg(mActivity, iv, picture.getPicture());
            tv1.setText((position + 1) + " / " + list.size());
            if (picture.getType() == 2) {
                tv2.setText("实景图");
            } else {
                tv2.setText("效果图");
            }
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popWindow.showPopupWindow(headView);
                    popWindow.setCurrentItem(position);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        pos = position;
        tab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
