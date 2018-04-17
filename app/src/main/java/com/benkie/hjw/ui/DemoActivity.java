package com.benkie.hjw.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.benkie.hjw.R;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.view.HeadView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DemoActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.headView)
    HeadView headView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        headView.setTitle("Demo");
        headView.setBtBack(this);
    }


}
