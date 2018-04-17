package com.benkie.hjw.ui.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;

import com.benkie.hjw.R;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ShareUtils;
import com.benkie.hjw.view.HeadView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reommend);
        ButterKnife.bind(this);
        headView.setTitle("推荐好友");
        headView.setBtBack(this);
        headView.setBtImg(R.mipmap.iv_share);
        headView.setBtClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.right_iv) {
            ShareUtils.shareRecomment(this,handler, 0);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

}
