package com.benkie.hjw.ui.product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.benkie.hjw.R;
import com.benkie.hjw.constants.Constants;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.map.SearchLocationActivity;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocatedCity;
//import com.zaaach.citypicker.CityPickerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductAddressActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.headView)
    HeadView headView;


    @BindView(R.id.ll_city)
    LinearLayout ll_city;

    @BindView(R.id.tv_city)
    TextView tv_city;

    @BindView(R.id.tv_address)
    EditText tv_address;

    @BindView(R.id.tv_save)
    TextView tv_save;

    String city = "";
    String address = "";
    private boolean isModify = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_addess);
        ButterKnife.bind(this);
        headView.setTitle("项目所在地址");
        headView.setBtBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ll_city.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        city = getIntent().getStringExtra("city");
        address = getIntent().getStringExtra("address");
        tv_city.setText(city);
        tv_address.setText(address);

        tv_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isModify = true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (ll_city == v) {
            showChoiceCity();
        } else if (tv_save == v) {
            saveAddress();
        }
    }

    private void saveAddress() {
        city = tv_city.getText().toString().trim();
        address = tv_address.getText().toString().trim();
        if (TextUtils.isEmpty(city)) {
            ToastUtil.showInfo(mActivity, "请选择项目区域");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtil.showInfo(mActivity, "请填写详细地址");
        }
        Intent intent = new Intent();
        intent.putExtra("City", city);
        intent.putExtra("Address", address);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void showChoiceCity() {
        List<HotCity> hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));
        CityPicker.getInstance()
                .setFragmentManager(getSupportFragmentManager())
                .enableAnimation(true)
                .setLocatedCity(new LocatedCity(Constants.city, Constants.province, ""))
                .setHotCities(hotCities)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        isModify = true;
                        city = data.getName();
                        tv_city.setText(city);
                    }

                    @Override
                    public void onLocate() {
                        Log.e("onLocate", "onLocate");
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        if (isModify)
            BaseDialog.dialogStyle2(mActivity, "您确认放弃编辑吗？", "取消", "放弃", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    if (tag == 1) {
                        //saveAddress();
                    } else {
                        finish();
                    }
                }
            });
        else super.onBackPressed();
    }
}
