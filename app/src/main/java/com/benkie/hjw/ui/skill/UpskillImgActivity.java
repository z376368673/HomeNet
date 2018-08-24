package com.benkie.hjw.ui.skill;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.GridViewImgAdapter;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.bean.SkillBean;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.popwindow.ImagePopWindow;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.BitmapUtlis;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.HeadView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class UpskillImgActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private int Action = 0; //  0：查看 没有操作;   1：编辑图片（删除 和 添加）

    @BindView(R.id.headView)
    HeadView headView;
    @BindView(R.id.tv_tip)
    TextView tv_tip;
    @BindView(R.id.gridview)
    GridView gridview;
    GridViewImgAdapter adapter;
    List<Picture> pictureList;
    SkillBean skillBean;
    List<Uri> imgs;
    List<String> imgPathList;
    ImagePopWindow popWindow;
    int maxPage = 2; //最多只能上传5张图片
    private boolean isModify = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_skill_img);
        ButterKnife.bind(this);
        imgPathList = new ArrayList<>();
        skillBean = (SkillBean) getIntent().getSerializableExtra("Title");
        headView.setTitle(skillBean.getName());
        Action = getIntent().getExtras().getInt("Action");
        adapter = new GridViewImgAdapter(mActivity);
        popWindow = new ImagePopWindow(this);
        headView.setBtBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Action == 1)
                    onBackPressed();
                else finish();
            }
        });
        if (Action == 1) {
            headView.setBtClickListener(this);
            Picture picture = new Picture();
            picture.setId(-1);
            adapter.add(picture);
            adapter.setEdit(true);
            tv_tip.setVisibility(View.VISIBLE);
        } else {
            adapter.setEdit(false);
            tv_tip.setVisibility(View.GONE);
        }
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);
        if (skillBean != null) {
            pictureList = skillBean.getImgs();
            if (pictureList != null) {
                adapter.addAll(pictureList);
                popWindow.setData(pictureList);
            }

        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (Action == 1) {
            addSkillImg();
        }
    }

    private void addSkillImg() {
        if (imgPathList.size() <= 0) {
            finish();
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("skillInfoId", String.valueOf(skillBean.getId()));
        for (int i = 0; i < imgPathList.size(); i++) {
            //String imgPath = Tools.getRealFilePath(this, imgs.get(i));
            File file = new File(imgPathList.get(i));
            Log.e("fileName", file.getName());
            builder.addFormDataPart("imgPath" + i, file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        Call call = Http.links.addSkillImg(parts);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    onFail("上传成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    onFail("上传失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Picture picture = (Picture) adapterView.getAdapter().getItem(i);
        if (picture == null) return;
        if (picture.getId() == -1) {
            if (adapterView.getAdapter().getCount() > maxPage) {
                ToastUtil.showInfo(mActivity, "最多只能上传"+maxPage+"张照片");
            } else {
                choseImg();
                Log.e("TAG", "choseImg");
            }
        } else {
            popWindow.showPopupWindow(headView);
            popWindow.setCurrentItem(i-1);
//            Intent intent = new Intent(mActivity, ImgActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("Img", picture.getPicture());
//            intent.putExtras(bundle);
//            startActivity(intent);
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
               int max =  maxPage-adapter.getCount()+1;
                Matisse.from(this)
                        .choose(MimeType.allOf())
                        .theme(R.style.Matisse_Dracula)//内置的主题
                        .countable(true)//使用 countable(true) 来显示一个从 1 开始的数字
                        .maxSelectable(max)//限制可选择的最大数目
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
                Log.e("TAG", "forResult");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            isModify = true;
            imgs = Matisse.obtainResult(data);
            BitmapUtlis bitmapUtlis = new BitmapUtlis();
            for (int i = 0; i < imgs.size(); i++) {
                String imgPath = Tools.getRealFilePath(this, imgs.get(i));
                imgPath = bitmapUtlis.compressImageByPath(imgPath);
                Picture picture = new Picture();
                picture.setPicture(imgPath);
                adapter.add(picture);
                imgPathList.add(imgPath);
                if (adapter.getCount() > maxPage) {
                    break;
                }
            }
            adapter.notifyDataSetChanged();
            headView.setBtText("保存");
            isModify = true;
            //adapter.setEdit(true);
//            addSkillImg();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        if (isModify)
            BaseDialog.dialogStyle2(mActivity, "您的修改还未保存，您确认退出吗？", "保存", "放弃", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    if (tag == 1) {
                        addSkillImg();
                    } else {
                        finish();
                    }
                }
            });
        else super.onBackPressed();
    }
}
