package com.benkie.hjw.ui.mine;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.GridViewImg1Adapter;
import com.benkie.hjw.adapter.GridViewImgAdapter;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.popwindow.ImagePopWindow;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.ui.ImgActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.HeadView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;


public class ComplaintActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.ll_one)
    LinearLayout ll_one;

    @BindView(R.id.ll_two)
    LinearLayout ll_two;

    @BindView(R.id.radio1)
    RadioGroup radio1;

    @BindView(R.id.radio2)
    RadioGroup radio2;

    @BindView(R.id.ed_text)
    EditText ed_text;

    @BindView(R.id.gridview)
    GridView gridview;

    int userItemId = 0;//项目id
    int reason = 0;//投诉理由（0：违法违规，1：侵权，2：虚假欺诈，3：其他）
    int identity = 0;//投诉人身份（0：我是投诉人，1：我是代理权利人

    GridViewImg1Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        ButterKnife.bind(this);
        headView.setTitle("投诉");
        headView.setBtBack(this);
        initView();
    }

    private void initView() {
        userItemId = getIntent().getIntExtra("pid", 0);
        ll_one.setVisibility(View.VISIBLE);
        ll_two.setVisibility(View.GONE);
        radio1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio1_2) {
                    reason = 1;
                } else if (i == R.id.radio1_3) {
                    reason = 2;
                } else if (i == R.id.radio1_4) {
                    reason = 3;
                } else {
                    reason = 0;
                }
            }
        });
        radio2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio2_2) {
                    identity = 1;
                } else {
                    identity = 0;
                }
            }
        });
        adapter = new GridViewImg1Adapter(mActivity);
        Picture picture = new Picture();
        picture.setId(-1);
        adapter.add(picture);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);

    }

    public void next(View view) {
        String content = ed_text.getText().toString();
        if (TextUtils.isEmpty(content) || content.length() < 10) {
            ToastUtil.showInfo(mActivity, "请填写投诉类容,最少10个字");
            return;
        }
        ll_one.setVisibility(View.GONE);
        ll_two.setVisibility(View.VISIBLE);
    }

    public void sumbit(View view) {
        String content = ed_text.getText().toString();
        if (TextUtils.isEmpty(content) || content.length() < 10) {
            ToastUtil.showInfo(mActivity, "请填写投诉类容,最少10个字");
            return;
        }

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("userInfoId", String.valueOf(DataHpler.getUserInfo().getUserid()));
        builder.addFormDataPart("userItemId", String.valueOf(userItemId));
        builder.addFormDataPart("reason", String.valueOf(reason));
        builder.addFormDataPart("identity", String.valueOf(identity));
        builder.addFormDataPart("content", String.valueOf(content));
        if (imgs != null) {
            for (int i = 0; i < imgs.size(); i++) {
                String imgPath = Tools.getRealFilePath(this, imgs.get(i));
                File file = new File(imgPath);
                Log.e("fileName", file.getName());
                builder.addFormDataPart("imgPath" + i, file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }
        }
        List<MultipartBody.Part> parts = builder.build().parts();

        Call call = Http.links.complaintItem(parts);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    BaseDialog.showMag(mActivity, "提交成功", "我们将在72小时内联系您，并处理您的投诉,感谢您的支持！", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Picture picture = (Picture) adapterView.getAdapter().getItem(i);
        if (picture == null) return;
        if (picture.getId() == -1) {
            choseImg();
            Log.e("TAG", "choseImg");
        } else {

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
                Matisse.from(this)
                        .choose(MimeType.allOf())
                        .theme(R.style.Matisse_Dracula)//内置的主题
                        .countable(true)//使用 countable(true) 来显示一个从 1 开始的数字
                        .maxSelectable(9)//限制可选择的最大数目
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

    List<Uri> imgs;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            imgs = Matisse.obtainResult(data);
            for (int i = 0; i < imgs.size(); i++) {
                String imgPath = Tools.getRealFilePath(this, imgs.get(i));
                Picture picture = new Picture();
                picture.setPicture(imgPath);
                adapter.add(picture);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
