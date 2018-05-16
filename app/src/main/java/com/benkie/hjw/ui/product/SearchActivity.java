package com.benkie.hjw.ui.product;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.GridTextAdapter;
import com.benkie.hjw.bean.Category;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.bean.PopBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.db.HistorySqliteOpenHelper;
import com.benkie.hjw.db.ProductSqliteOpenHelper;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.popwindow.BaseListPopWindow;
import com.benkie.hjw.popwindow.ListPopWindow;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import am.widget.wraplayout.WrapLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class SearchActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.ed_search)
    EditText ed_search;

    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.iv_delect)
    ImageView iv_delect;

    @BindView(R.id.wly_lyt_warp)
    WrapLayout wly_lyt_warp;

    String key = null;
    BaseListPopWindow popWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        ininPop();
        setHistoryData();
    }

    private void initView() {
        tv_search.setOnClickListener(this);
        //取消智能提示
        //ed_search.addTextChangedListener(this);
        iv_back.setOnClickListener(this);
        iv_delect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == tv_search) {
            String key = ed_search.getText().toString();
            search(key);
        } else if (v == iv_back) {
            finish();
        } else if (v == iv_delect) {
            HistorySqliteOpenHelper.getNewHelpe(this).delectAll();
            setHistoryData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHistoryData();
    }

    /**
     * 获取所有得完成项目
     */
    private void getAllData(String key) {
        Call call = Http.links.searchAllItemList(0, "", key, "", "", 0, String.valueOf(System.currentTimeMillis()));
        Http.http.call(mActivity, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<HomeProductBean> beanList = JSON.parseArray(jsObj.getString("data"), HomeProductBean.class);
                    if (popWindow != null) {
                        if (beanList == null || beanList.isEmpty()) {
                            popWindow.dismiss();
                            return;
                        }
                        popWindow.setData(beanList);
                        if (!popWindow.isShowing()) {
                            popWindow.showPopupWindow(tv_search);
                        } else {
                            popWindow.update();
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


    /**
     * 获取历史记录
     */
    private void setHistoryData() {
        List<String> list = HistorySqliteOpenHelper.getNewHelpe(this).getAll();
        wly_lyt_warp.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            TextView view = new TextView(mActivity);
            final String str = list.get(i);
            view.setText(str);
            view.setTextColor(Color.parseColor("#333333"));
            view.setTextSize(16);
            view.setBackgroundResource(R.drawable.shape_button_bg_color_white_e4);
            view.setPadding(20, 10, 20, 10);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    key = str;
                    ed_search.setText(key);
                }
            });
            wly_lyt_warp.addView(view);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }


    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        key = ed_search.getText().toString();
        if (TextUtils.isEmpty(key)) {
            if (popWindow.isShowing()) {
                popWindow.dismiss();
            }
        }else {
            getAllData(key);
        }
        //List<HomeProductBean> list = ProductSqliteOpenHelper.getNewHelpe(this).getAotu(key);

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void ininPop() {
        popWindow = new BaseListPopWindow<HomeProductBean>(this) {
            @Override
            public View getViews(int position, HomeProductBean productBean, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(mActivity).inflate(R.layout.pop_windows_item_list_3, null);
                TextView textView1 = view.findViewById(R.id.text1);
                TextView textView2 = view.findViewById(R.id.text2);
                TextView textView3 = view.findViewById(R.id.text3);
                if (productBean != null) {
                    textView1.setText(productBean.getName());
                    textView2.setText(productBean.getCity());
                    textView3.setText(productBean.getFinishDate());
                }
                return view;
            }
        };
        popWindow.setFocusable(false);
        popWindow.setPopWindowCheckedListener(new BaseListPopWindow.PopWindowCheckedListener() {
            @Override
            public void onPopWindowCheckedListener(Object object) {
                HomeProductBean productBean = (HomeProductBean) object;
                //toDetails(productBean);
                String key = productBean.getName();
                if (key.length() > 1) {
                    //一个字不保存
                    HistorySqliteOpenHelper.getNewHelpe(mActivity).setData(key);
                }
                Intent intent = new Intent(mActivity, SearchResultActivity.class);
                intent.putExtra("KEY", key);
                intent.putExtra("CITY", productBean.getCity());
                intent.putExtra("DATE", productBean.getFinishDate());
                startActivity(intent);
            }
        });
    }


    /**
     * 去项目详情
     *
     * @param productBean
     */
    private void toDetails(HomeProductBean productBean) {
        if (productBean.getName().length() > 1) {
            //一个字不保存
            HistorySqliteOpenHelper.getNewHelpe(this).setData(productBean.getName());
        }
        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pid", productBean.getItemId());
        bundle.putInt("FormType", 0);
        bundle.putString("Name", productBean.getName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void search(String key) {
        if (key.length() > 1) {
            //一个字不保存
            HistorySqliteOpenHelper.getNewHelpe(this).setData(key);
        }
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("KEY", key);
        startActivity(intent);
    }
}
