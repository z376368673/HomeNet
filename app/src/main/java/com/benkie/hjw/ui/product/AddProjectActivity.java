package com.benkie.hjw.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;
import retrofit2.Call;


public class AddProjectActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_type)
    TextView tv_type;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.tv_save)
    TextView tv_save;

    String city = "";
    String address = "";
    int typeId = 0;
    boolean isEdit = false;
    HomeProductBean productBean;
    private boolean isModify = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        ButterKnife.bind(this);
        headView.setTitle("添加项目");
        headView.setBtBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            isEdit = bundle.getBoolean("isEdit");
        if (isEdit) {
            headView.setTitle("编辑项目");
            productBean = (HomeProductBean) bundle.getSerializable("Bean");
            city = productBean.getCity();
            address = productBean.getAddress();
            tv_address.setText(city);
            tv_name.setText(productBean.getName());
            tv_date.setText(productBean.getFinishDate());
            tv_type.setText(productBean.getTypeName());
            typeId = productBean.getItemTypeId();
            tv_type.setTag(typeId);
        }

        tv_address.setOnClickListener(this);
        tv_name.setOnClickListener(this);
        tv_type.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (tv_address == v) {
//            Intent intent = new Intent(mActivity, SearchLocationActivity.class);
//            startActivityForResult(intent, 1001);
            Intent intent = new Intent(mActivity, ProductAddressActivity.class);
            if (isEdit) {
                intent.putExtra("city", productBean.getCity());
                intent.putExtra("address", productBean.getAddress());
            }
            startActivityForResult(intent, 1001);
        } else if (tv_name == v) {
            Intent intent = new Intent(mActivity, ProjectNameActivity.class);
            intent.putExtra("Name", tv_name.getText().toString());
            startActivityForResult(intent, 1002);
        } else if (tv_type == v) {
            Intent intent = new Intent(mActivity, ProjectTypeActivity.class);
            startActivityForResult(intent, 1003);
        } else if (tv_date == v) {
            DatePicker datePicker = new DatePicker(mActivity, DatePicker.YEAR_MONTH);
            Calendar cal = Calendar.getInstance();
            final int mYear = cal.get(Calendar.YEAR);
            final int mMonth = cal.get(Calendar.MONTH)+1;
            datePicker.setRange(2010, mYear);//年份范围
            datePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                @Override
                public void onDatePicked(String year, String month) {
                    isModify = true;
                    try {
                        if (!TextUtils.isEmpty(month)) {
                            int month1 = Integer.parseInt(month);
                            if (year.equals(mYear+"")&&month1 > mMonth) {
                                ToastUtil.showInfo(mActivity, "不能超过当前时间");
                            } else {
                                tv_date.setText(year + "-" + month);
                            }
                        }

                    } catch (Exception e) {
                        tv_date.setText(year + "-" + month);
                    }
                }
            });
            datePicker.show();
        } else if (tv_save == v) {
            save();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 & resultCode == RESULT_OK) {
            isModify = true;
            //LatLonPoint latLonPoint = data.getExtras().getParcelable("LatLonPoint");
            address = data.getExtras().getString("Address");
            city = data.getExtras().getString("City");
            tv_address.setText(city);
        } else if (requestCode == 1002 & resultCode == RESULT_OK) {
            isModify = true;
            String name = data.getStringExtra("Name");
            tv_name.setText(name);
        } else if (requestCode == 1003 & resultCode == RESULT_OK) {
            isModify = true;
            String type = data.getStringExtra("TypeName");
            int id = data.getIntExtra("TypeId", 0);
            tv_type.setText(type);
            tv_type.setTag(id);
        }
    }

    private void save() {
        String name = tv_name.getText().toString();
//        String address = tv_address.getText().toString();
        String date = tv_date.getText().toString();
        String type = tv_type.getText().toString();
        if (address == null || address.length() < 1) {
            ToastUtil.showInfo(mActivity, "请填写地址");
            return;
        } else if (name == null || name.length() < 1) {
            ToastUtil.showInfo(mActivity, "请填写项目名称");
            return;
        } else if (type == null || type.length() < 1) {
            ToastUtil.showInfo(mActivity, "请选择项目类型");
            return;
        } else if (date == null || date.length() < 1) {
            ToastUtil.showInfo(mActivity, "请选择完工日期");
            return;
        }
        typeId = (int) tv_type.getTag();
        Call call;
        if (isEdit) {
            call = Http.links.updateItem(productBean.getItemId(), name, city, address, date + "-01", typeId);

        } else {
            call = Http.links.addItem(DataHpler.getUserInfo().getUserid(), name, city, address, date + "-01", typeId);
        }

        Http.http.call(mActivity, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                int itemId = jsObj.getIntValue("itemId");
                if (msg == 1) {
                    if (!isEdit)
                        isGoToAddImg(itemId);
                    else {
                        //编辑项目完成后
                        setResult(RESULT_OK);
                        finish();
                    }
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

    private void isGoToAddImg(final int itemId) {
        BaseDialog.dialogStyle2(mActivity,
                "项目文件夹添加成功！现在可以去添加项目图片",
                "去添加",
                "以后添加",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.dialog_confirm) {
                            //去添加图片
                            Intent intent = new Intent(mActivity, AddImgActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("pid", itemId);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } else {
                            //以后添加图片
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (isModify)
            BaseDialog.dialogStyle2(mActivity, "您确认放弃添加或编辑此项目吗？", "取消", "放弃", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    if (tag == 1) {
                    } else {
                        finish();
                    }
                }
            });
        else super.onBackPressed();
    }
}
