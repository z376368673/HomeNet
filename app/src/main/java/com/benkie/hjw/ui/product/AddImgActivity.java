package com.benkie.hjw.ui.product;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.bean.SkillDetailsBean;
import com.benkie.hjw.bean.TypeBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.dialog.LoadingDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.BitmapUtlis;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.HeadView;
import com.bumptech.glide.Glide;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;


public class AddImgActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.rl_add)
    RelativeLayout rl_add;

    @BindView(R.id.iv_img)
    ImageView iv_img;

    @BindView(R.id.tv_chose)
    TextView tv_chose;

    @BindView(R.id.radio)
    RadioGroup radio;

    @BindView(R.id.radio2)
    RadioButton radio2;
    @BindView(R.id.radio3)
    RadioButton radio3;

    @BindView(R.id.tv_fuwu)
    TextView tv_fuwu;
    @BindView(R.id.tv_miaoshu)
    TextView tv_miaoshu;
    @BindView(R.id.tv_save)
    TextView tv_save;
    @BindView(R.id.tv_del)
    TextView tv_del;

    int pid = 0;
    Picture picture;
    int ImgType = 2; //照片类型（1.效果图，2.实景图
    String fuwuId = ""; //服务id 例如：1,2,3
    String describe = ""; //描述
    String imgPath = "";
    private static boolean isEdit;
    private static List<TypeBean> SList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_img);
        ButterKnife.bind(this);
        headView.setTitle("添加项目图片");
        headView.setBtBack(this);
        initView();
        getQiNiuToken();
    }

    private void initView() {
        //因为这个集合是静态变量，只要不退出app 里面的值不消失，所以需要初始化位空
        SList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        pid = bundle.getInt("pid");
        isEdit = bundle.getBoolean("isEdit");
        if (isEdit) {
            headView.setTitle("编辑项目图片");
            picture = (Picture) bundle.getSerializable("Bean");
            describe = picture.getDescribes();
            pid = picture.getId();
            tv_del.setVisibility(View.VISIBLE);
            Tools.loadImg(mActivity, iv_img, picture.getPicture());
           if (picture.getType() == 1) {
                radio2.setChecked(true);
                ImgType = 1;
            } else {
                radio3.setChecked(true);
                ImgType = 2;
            }
            SList = picture.getService();
            fuwuId = getServiceId(SList);
            tv_fuwu.setText(getServiceString(SList));
            tv_miaoshu.setText(picture.getDescribes());
            tv_chose.setVisibility(View.VISIBLE);
        }else {
            tv_chose.setVisibility(View.INVISIBLE);
        }
        tv_chose.setOnClickListener(this);
        rl_add.setOnClickListener(this);
        tv_fuwu.setOnClickListener(this);
        tv_miaoshu.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        tv_del.setOnClickListener(this);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                isModify = true;
                if (i == R.id.radio2) {
                    ImgType = 1;
                } else {
                    ImgType = 2;
                }
            }
        });
    }

    public static List<TypeBean> getSL() {
        if (SList == null)
            return SList = new ArrayList<>();
        else
            return SList;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (rl_add == v || tv_chose == v) {
            choseImg();
        } else if (tv_fuwu == v) {
            Intent intent = new Intent(mActivity, ServiceActivity.class);
            Bundle bundle = new Bundle();
            //bundle.putSerializable("SL",picture.getService());
            intent.putExtras(bundle);
            startActivityForResult(intent, 1001);
        } else if (tv_miaoshu == v) {
            Intent intent = new Intent(mActivity, ProductDescribeActivity.class);
            intent.putExtra("Des", describe);
            startActivityForResult(intent, 1002);
        } else if (tv_save == v) {
            if (isEdit) {
                if (!TextUtils.isEmpty(imgPath)) {
                    uploadImg2QiNiu(new File(imgPath));
                }else {
                    upItemImg("");
                }
            } else {
                if (imgPath.equals("")) {
                    ToastUtil.showInfo(mActivity, "您还没添加图片！");
                    return;
                } else if (fuwuId.equals("")) {
                    ToastUtil.showInfo(mActivity, "您还没选择服务类容！");
                    return;
                }
                uploadImg2QiNiu(new File(imgPath));
            }

        } else if (tv_del == v) {
            delItemImg();
        }
    }

    private void delItemImg() {
        Call call = Http.links.delItemImg(picture.getId());
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    onFail("删除成功");
                    setResult(RESULT_OK);
                    finish();
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
                        .thumbnailScale(0.5f)//缩放比例 相册图片的显示
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

    public String getServiceString(List<TypeBean> services) {
        String str = "";
        if (services == null) return str;
        for (int i = 0; i < services.size(); i++) {
            str += services.get(i).getName() + "/";
        }
        if (str.length() > 1) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public String getServiceId(List<TypeBean> services) {
        String str = "";
        if (services == null) return str;
        for (int i = 0; i < services.size(); i++) {
            str += services.get(i).getId() + ",";
        }
        if (str.length() > 1) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            isModify = true;
            List<Uri> imgs = Matisse.obtainResult(data);
            imgPath = Tools.getRealFilePath(this, imgs.get(0));
            //imgPath = new BitmapUtlis().compressImageByPath(imgPath);
            Glide.with(mActivity)
                    .load(new File(imgPath))
                    .into(iv_img);
        } else if (requestCode == 1001 && resultCode == RESULT_OK) {
            isModify = true;
            HashMap<String, TypeBean.Tclass> hashMap = ServiceActivity.getHashMap();
            SList = new ArrayList<>();
            for (Map.Entry<String, TypeBean.Tclass> entry : hashMap.entrySet()) {
                TypeBean.Tclass tclass = entry.getValue();
                TypeBean typeBean = new TypeBean();
                typeBean.setId(tclass.getId());
                typeBean.setName(tclass.getName());
                SList.add(typeBean);
            }
            String fuwu = getServiceString(SList);
            fuwuId = getServiceId(SList);
            tv_fuwu.setText(fuwu);
            Log.e("fuwuId", fuwuId);
        } else if (requestCode == 1002 && resultCode == RESULT_OK && data != null) {
            isModify = true;
            describe = data.getStringExtra("Des");
            if (!TextUtils.isEmpty(describe))
                tv_miaoshu.setText(describe);
        }
    }

    private void upItemImg(String fileName) {
        if (picture == null) return;
        File file = new File(imgPath);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        builder.addFormDataPart("itemId", String.valueOf(pid));
        builder.addFormDataPart("itemImgId", String.valueOf(picture.getId()));
        builder.addFormDataPart("type", String.valueOf(ImgType));
        builder.addFormDataPart("describe", String.valueOf(describe));
        if (!fuwuId.equals(""))
            builder.addFormDataPart("twoserviceids", String.valueOf(fuwuId));
        builder.addFormDataPart("picture", String.valueOf(fileName));
            //builder.addFormDataPart("imgPath", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        List<MultipartBody.Part> parts = builder.build().parts();

        Call call = Http.links.updateItemImg(parts);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    onFail("保存成功");
                    setResult(RESULT_OK);
                    finish();
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
     * 添加图片
     */
    private void addItemImg(String fileName) {
        File file = new File(imgPath);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("itemId", String.valueOf(pid))
                .addFormDataPart("type", String.valueOf(ImgType))
                .addFormDataPart("describe", String.valueOf(describe))
                .addFormDataPart("twoserviceids", String.valueOf(fuwuId))
                .addFormDataPart("picture", String.valueOf(fileName));

                //.addFormDataPart("imgPath", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
//        builder.addPart(new Http.FileProgressRequestBody(file, new Http.ProgressListener() {
//            @Override
//            public void transferred(long total,long size) {
//                Log.e("TAG", "transferred: "+size);
//            }
//        }));
        List<MultipartBody.Part> parts = builder.build().parts();
        Call call = Http.links.addItemImg(parts);
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    onFail("保存成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    error = jsObj.getString("errorInfo");
                    onFail("保存失败"+error);
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    private  String qiNiuToken;
    public void getQiNiuToken() {
        Call call = Http.links.getQiNiuToken();
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                qiNiuToken = jsObj.getString("upToken");
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    Configuration config = new Configuration.Builder()
                .zone(FixedZone.zone2)
                .build();
    LoadingDialog dialog ;
    private void uploadImg2QiNiu(File file) {

        dialog = new LoadingDialog(this);
        if (!dialog.isShowing()) dialog.show();
        UploadManager uploadManager = new UploadManager(config);
        // 设置图片名字
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "ico_" + sdf.format(new Date());
        String picPath = file.getAbsolutePath().toString();
        final String imgType = picPath.substring(picPath.lastIndexOf("."));
        Log.i("uploadImg2QiNiu", "picPath: " + picPath);
        //Auth.create(AccessKey, SecretKey).uploadToken("zhongshan-avatar")，这句就是生成token
        uploadManager.put(picPath, key, qiNiuToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, org.json.JSONObject response) {
                dialog.dismiss();
                // info.error中包含了错误信息，可打印调试
                Log.i("uploadImg2QiNiu", "ResponseInfo==" + info);
                Log.i("uploadImg2QiNiu", "response==" + response.toString());
                // 上传成功后将key值上传到自己的服务器
                if (info.isOK()) {
                    Log.i("uploadImg2QiNiu", "complete: " + key+imgType);
                    if (isEdit) {
                        upItemImg(key);
                    } else {
                       addItemImg(key);
                    }
                }
                //uploadpictoQianMo(headpicPath, picPath);
            }

        }, new UploadOptions(null, null, false,
                new UpProgressHandler(){
                    public void progress(String key, double percent){
                        dialog.setProgress(percent*100);
                        Log.i("uploadImg2QiNiu", key + ": " + percent);
                    }
                }, null));
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
