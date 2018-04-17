package com.benkie.hjw.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.GridTextAdapter;
import com.benkie.hjw.bean.Category;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class ProjectTypeActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.gridview)
    GridView gridview;
    GridTextAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_type);
        ButterKnife.bind(this);
        headView.setTitle("项目分类");
        headView.setBtBack(this);
        adapter = new GridTextAdapter(mActivity);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = (Category) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent();
                intent.putExtra("TypeName",category.getName());
                intent.putExtra("TypeId",category.getId());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        getData();
    }
    private void getData(){
        Call call = Http.links.addItemType();
        Http.http.call(mActivity,call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg==1){
                    List<Category> list =  JSON.parseArray(jsObj.getString("data"),Category.class);
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                }else {
                    onFail("获取数据失败");
                }
            }
            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity,error);
            }
        });
    }

}
