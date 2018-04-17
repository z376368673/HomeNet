package com.benkie.hjw.ui.setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.bean.TypeBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.ui.product.ServiceActivity;
import com.benkie.hjw.utils.BitmapUtlis;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.HeadView;
import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;


public class ModifyHeadActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.iv_head)
    ImageView iv_head;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_head);
        ButterKnife.bind(this);
        headView.setTitle("头像");
        headView.setBtBack(this);
        headView.setBtText("保存");
        headView.setBtClickListener(this);
        iv_head.setOnClickListener(this);
        String headImg  = DataHpler.getUserInfo().getImgUrl();
        if (headImg!=null){
            Glide.with(this)
                    .load(headImg)
                    .placeholder(R.drawable.iv_defult_head)
                    .into(iv_head);
        }else {
            iv_head.setImageResource(R.drawable.iv_defult_head);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v==iv_head){
            choseImg();
        }else {
            addItemImg();
        }
    }

    /**
     * 添加图片
     */
    private void addItemImg() {
        if (TextUtils.isEmpty(imgPath)){
            ToastUtil.showInfo(mActivity,"请更换图片");
            return;
        }
        File file = new File(imgPath);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", String.valueOf(DataHpler.getToken()))
                .addFormDataPart("imgPath",file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        List<MultipartBody.Part> parts = builder.build().parts();
        Call call = Http.links.updataUserImg(parts);
        Http.http.call(mActivity,call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    onFail("保存成功,正在更新用户信息...");
                    upDataUserInfo();
                } else {
                    onFail("保存失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    /**
     * 更新用户信息
     */
    private void upDataUserInfo(){
        if (DataHpler.islogin()) {
            Call call = Http.links.getUserInfo(DataHpler.getToken());
            Http.http.call(mActivity, call, true, new Http.JsonCallback() {
                @Override
                public void onResult(String json, String error) {
                    JSONObject jsObj = JSON.parseObject(json);
                    int msg = jsObj.getIntValue("msg");
                    if (msg == 1) {
                        DataHpler.setUserInfo(json);
                        finish();
                    } else {
                        // onFail("获取会员信息失败");
                    }
                }

                @Override
                public void onFail(String error) {
                    ToastUtil.showInfo(mActivity, error);
                }
            });
        }
    }

    String imgPath= "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> imgs = Matisse.obtainResult(data);
            imgPath = Tools.getRealFilePath(this, imgs.get(0));
            imgPath = new BitmapUtlis().compressImageByPath(imgPath);
            Log.e("onActivityResult","imgPath = " +imgPath);
            ToastUtil.showInfo(this,imgPath);
            Glide.with(mActivity)
                    .load(new File(imgPath))
                    .into(iv_head);
        }
    }

    /**
     * 选择图片 先检查是否有相应的权限
     */
    private void choseImg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                Matisse.from(this)
                        .choose(MimeType.allOf())
                        .theme(R.style.Matisse_Dracula)//内置的主题
                        .countable(false)//使用 countable(true) 来显示一个从 1 开始的数字
                        .maxSelectable(1)//限制可选择的最大数目
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choseImg();
            } else {
                ToastUtil.showInfo(mActivity, "权限已拒绝");
            }
        }
    }


//    @Override
//    public void onBackPressed() {
//        if (isModify)
//            BaseDialog.dialogStyle2(mActivity, "您确认放弃添加或编辑此项目吗？", "取消", "放弃", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int tag = (int) view.getTag();
//                    if (tag == 1) {
//                    } else {
//                        finish();
//                    }
//                }
//            });
//        else super.onBackPressed();
//    }

}
