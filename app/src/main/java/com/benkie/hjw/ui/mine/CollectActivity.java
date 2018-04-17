package com.benkie.hjw.ui.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.ui.product.ProjectNFragment;
import com.benkie.hjw.ui.product.ProjectPFragment;
import com.benkie.hjw.view.HeadView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CollectActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.tv_public_n)
    TextView tv_public_n;
    @BindView(R.id.v_n)
    View v_n;

    @BindView(R.id.tv_public_p)
    TextView tv_public_p;
    @BindView(R.id.v_p)
    View v_p;

    List<Fragment> fragmentList = new ArrayList<Fragment>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        headView.setTitle("我的收藏");
        headView.setBtBack(this);
        tv_public_n.setOnClickListener(this);
        tv_public_p.setOnClickListener(this);
        initFragment();
        selectTab(0);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v==tv_public_n){
            selectTab(0);
        }else if (v==tv_public_p){
            selectTab(1);
        }
    }

    private void initFragment() {
        ProjectFragment projectNFragment = new ProjectFragment();
        SkillFragment projectPFragment = new SkillFragment();
        fragmentList.add(projectNFragment);
        fragmentList.add(projectPFragment);
    }
    private void selectTab(int indexOfChild) {
        Fragment fragment;
        if (indexOfChild==0){
            tv_public_n.setTextColor(getResources().getColor(R.color.colorMain));
            v_n.setBackgroundColor(getResources().getColor(R.color.colorMain));
            tv_public_p.setTextColor(getResources().getColor(R.color.black_66));
            v_p.setBackgroundColor(getResources().getColor(R.color.white));
            fragment = fragmentList.get(0);
        }else {
            tv_public_n.setTextColor(getResources().getColor(R.color.black_66));
            v_n.setBackgroundColor(getResources().getColor(R.color.white));
            tv_public_p.setTextColor(getResources().getColor(R.color.colorMain));
            v_p.setBackgroundColor(getResources().getColor(R.color.colorMain));
            fragment = fragmentList.get(1);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

}
