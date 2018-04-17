package com.benkie.hjw.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.benkie.hjw.R;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.HeadView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImgActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;
    @BindView(R.id.iv_img)
    ImageView iv_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        ButterKnife.bind(this);
        headView.setTitle("");
        headView.setBtBack(this);

        Bundle bundle = getIntent().getExtras();
        String img = bundle.getString("Img");
        Tools.loadImg(mActivity, iv_img, img);

    }


}
