package com.benkie.hjw.ui.product;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.HomeProductAdapter;
import com.benkie.hjw.adapter.ProductNAdapter;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.dialog.ChoiceDialog;
import com.benkie.hjw.fragment.BaseFragment;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.pay.Product_PayActivity;
import com.benkie.hjw.utils.ShareUtils;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.wxapi.WXPay;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by 37636 on 2018/1/18.
 */

public class ProjectNFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.ll_add)
    LinearLayout ll_add;
    @BindView(R.id.pullRefreshView)
    PullToRefreshGridView pullRefreshView;
    GridView gridView;
    ProductNAdapter adapter;
    HomeProductBean productBean;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = View.inflate(getActivity(),R.layout.fragment_home3,null);
        View view = inflater.inflate(R.layout.fragment_project_n, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public void addProject() {
        Intent intent = new Intent(getContext(), AddProjectActivity.class);
        getActivity().startActivityFromFragment(this, intent, 1000);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == ll_add) {
            addProject();
        }
    }
    public void initView() {
        ll_add.setOnClickListener(this);
        pullRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        pullRefreshView.setOnRefreshListener(this);
        gridView = pullRefreshView.getRefreshableView();
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(20);
        gridView.setVerticalSpacing(20);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemClickListener(this);
        adapter = new ProductNAdapter(mActivity);
        gridView.setAdapter(adapter);
        adapter.setAdapterInterface(new ProductNAdapter.AdapterInterface() {
            @Override
            public void toPublic(final HomeProductBean bean) {
                if (bean.getImgs()==null||bean.getImgs().getId()==0){
                    ToastUtil.showInfo(getActivity(),"你还未添加项目图片");
                    return;
                }
                BaseDialog.dialogStyle1(getActivity(), "发布后项目信息将不可更改!", "发布", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Product_PayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("TYPE",0);
                        bundle.putSerializable("Bean", bean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void toEdit(Object object) {
                productBean = (HomeProductBean) object;
                ChoiceDialog choiceDialog = new ChoiceDialog(mActivity);
                choiceDialog.setHandler(handler);
                choiceDialog.show();
            }
        });
        geteData(true);
    }



    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1: //编辑
                    toEditPro();
                    break;
                case 2://删除
                    delProduct(productBean);
                    break;
                case 3://添加图片
                    toAddImg();
                    break;
                case 4://分享
                    toShare();
                    break;
            }

        }
    };


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        productBean = (HomeProductBean) adapterView.getAdapter().getItem(i);
        //如果图片为空 则去添加图片
        if (productBean.getImgs()==null){
            toAddImg();
        }else {
            Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("pid", productBean.getItemId());
            bundle.putString("Name",productBean.getName());
            bundle.putInt("FormType", 1);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pullRefreshView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        geteData(false);
    }

    /**
     * 去分享
     */
    private void toShare() {
        ShareUtils.shareProduct(mActivity,handler,productBean.getItemId());
    }
    /**
     * 删除项目
     * @param productBean
     */
    private void delProduct(final HomeProductBean productBean) {
        Call call = Http.links.delItem(productBean.getItemId());
        Http.http.call(mActivity,call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    adapter.remove(productBean);
                    adapter.notifyDataSetChanged();
                } else {
                    onFail("删除失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    private void geteData(boolean isShow) {
        Call call = Http.links.allProduct(DataHpler.getUserInfo().getUserid());
        Http.http.call(mActivity,call, isShow, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                pullRefreshView.onRefreshComplete();
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                   List<HomeProductBean> beanList =  JSON.parseArray(jsObj.getString("in"), HomeProductBean.class);
                    if (beanList!=null){
                        adapter.clear();
                        adapter.addAll(beanList);
                        adapter.notifyDataSetChanged();
                    }
                } else {

                    onFail("获取数据失败");
                }
            }

            @Override
            public void onFail(String error) {
                pullRefreshView.onRefreshComplete();
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        geteData(false);
    }

    /**
     * 去编辑项目
     */
    private void toEditPro(){
        Intent intent = new Intent(mActivity,AddProjectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isEdit",true);
        bundle.putSerializable("Bean",  productBean);
        intent.putExtras(bundle);
        getActivity().startActivityFromFragment(this,intent,1001);
    }

    /**
     * 去添加图片
     */
    private void toAddImg(){
        Intent intent = new Intent(mActivity,AddImgActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pid", productBean.getItemId());
        intent.putExtras(bundle);
        getActivity().startActivityFromFragment(this,intent,1002);
    }

}
