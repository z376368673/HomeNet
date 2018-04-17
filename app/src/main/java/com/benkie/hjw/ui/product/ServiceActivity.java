package com.benkie.hjw.ui.product;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.bean.PopBean;
import com.benkie.hjw.bean.TypeBean;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.LogUtils;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;

import java.util.HashMap;
import java.util.List;

import am.widget.wraplayout.WrapLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

import static com.benkie.hjw.ui.product.AddImgActivity.getSL;


public class ServiceActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.wly_lyt_warp)
    WrapLayout wly_lyt_warp;
    Context context;
    @BindView(R.id.listView1)
    ListView listView1;
    @BindView(R.id.gridview)
    GridView gridview;
    private MyAdadpter myAdadpter;
    private MyAdadpter2 myAdadpter2;

    private static HashMap<String,TypeBean.Tclass> hashMap  = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_service_type);
        ButterKnife.bind(this);
        headView.setTitle("服务分类");
        headView.setBtBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        headView.setBtText("保存");
        headView.setBtClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
        context = this;
        hashMap.clear();
        initView();
        initData();
        getAllServiceList();


    }
    private  void initData(){
        List<TypeBean> list = getSL();
        for (int i = 0; i <getSL().size() ; i++) {
            TypeBean.Tclass tclass = new TypeBean.Tclass();
            tclass.setId(list.get(i).getId());
            tclass.setName(list.get(i).getName());
            hashMap.put(tclass.getName(),tclass);
            addTclass(tclass);
        }
    }

    /**
     * 获取服务类型
     */
    private void getAllServiceList() {
        Call call = Http.links.addAllserviceList();
        Http.http.call(mActivity,call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<TypeBean>  beanList = JSON.parseArray(jsObj.getString("data"), TypeBean.class);
                    if (beanList != null){
                        setData(beanList);
                    }else  onFail("暂无数据");
                }else {
                    onFail("获取数据失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }
    private void initView() {
        listView1.setDividerHeight(0);
        myAdadpter = new MyAdadpter(context);
        listView1.setAdapter(myAdadpter);
        listView1.setOnItemClickListener(listener1);
        myAdadpter2 = new MyAdadpter2(context);
        gridview.setAdapter(myAdadpter2);
        gridview.setOnItemClickListener(listener2);
    }

    public void setData(List<TypeBean> list) {
        myAdadpter.addAll(list);
        myAdadpter.notifyDataSetChanged();
        if (list != null) {
            upAdapter(list.get(0).getTcList(), 0);
        }
    }

    AdapterView.OnItemClickListener listener1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TypeBean typeBean = (TypeBean) adapterView.getAdapter().getItem(i);
            if (typeBean == null) return;
            List<TypeBean.Tclass> tclist = typeBean.getTcList();
            if (tclist == null) return;
            upAdapter(tclist, i);
        }
    };

    private void upAdapter(List<? extends PopBean> list, int p) {
        myAdadpter.selected(p);
        myAdadpter2.clear();
        myAdadpter2.addAll(list);
    }

    public static  HashMap<String, TypeBean.Tclass> getHashMap() {
        return hashMap;
    }

    AdapterView.OnItemClickListener listener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TypeBean.Tclass tclass = (TypeBean.Tclass) adapterView.getAdapter().getItem(i);
            if (tclass == null) return;
            if (!hashMap.containsKey(tclass.getName())){
                hashMap.put(tclass.getName(),tclass);
                isModify = true;
                addTclass(tclass);
            }else {
                ToastUtil.showInfo(mActivity,"您已经添加过了");
            }
        }
    };

    private  void addTclass(final TypeBean.Tclass tclass){
        View view = View.inflate(this, R.layout.view_text, null);
        TextView textView = view.findViewById(R.id.tv_text1);
        textView.setText(tclass.getName());
        //textView.setBackgroundResource(R.drawable.shape_button_bg_color_white_oval);
        view.setPadding(20,5,20,5);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isModify = true;
                hashMap.remove(tclass.getName());
                wly_lyt_warp.removeView(view);
            }
        });
        wly_lyt_warp.addView(view);
    }

    public class MyAdadpter extends ArrayAdapter<PopBean> {
        private int selectePos = -1;

        public MyAdadpter(@NonNull Context context) {
            super(context, 0);
        }

        public void selected(int selectePos) {
            this.selectePos = selectePos;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.pop_windows_item_list_2, null);
            }
            TextView textView1 = convertView.findViewById(R.id.text1);
            TextView textView2 = convertView.findViewById(R.id.text2);
            PopBean item = getItem(position);
            textView1.setText(item.getName());
            textView2.setText(item.getName2());
            textView2.setVisibility(View.GONE);
            textView1.setBackgroundResource(R.color.white);
            if (selectePos == position) {
                textView1.setBackgroundResource(R.color.white_f4);
                textView1.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
                LogUtils.e("position1--->", position);
            } else {
                textView1.setBackgroundResource(R.color.white);
                textView1.setTextColor(ContextCompat.getColor(context, R.color.black_66));
            }
            return convertView;
        }
    }

    public class MyAdadpter2 extends ArrayAdapter<PopBean> {
        private int selectePos = -1;

        public void selected(int selectePos) {
            this.selectePos = selectePos;
            notifyDataSetChanged();
        }

        public MyAdadpter2(@NonNull Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_list_gridview2, null);
            }
            TextView textView1 = convertView.findViewById(R.id.text1);
            //TextView textView2 = convertView.findViewById(R.id.text2);
            PopBean item = getItem(position);
            textView1.setText(item.getName());
           // textView2.setText(item.getName2());
            if (selectePos == position) {
                LogUtils.e("position2--->", position);
                textView1.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
               // textView2.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
            } else {
                textView1.setTextColor(ContextCompat.getColor(context, R.color.black_66));
                //textView2.setTextColor(ContextCompat.getColor(context, R.color.black_66));
            }
            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        if (isModify)
            BaseDialog.dialogStyle2(mActivity, "您的修改还未保存，您确认退出吗？", "保存", "放弃", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    if (tag == 1) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        finish();
                    }
                }
            });
        else super.onBackPressed();
    }
}
