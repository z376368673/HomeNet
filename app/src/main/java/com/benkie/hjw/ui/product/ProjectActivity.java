package com.benkie.hjw.ui.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.view.HeadView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectActivity extends BaseActivity {
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
        setContentView(R.layout.activity_project);
        ButterKnife.bind(this);
        headView.setTitle("项目发布");
        headView.setBtBack(this);
        tv_public_n.setOnClickListener(this);
        tv_public_p.setOnClickListener(this);
        initFragment();
        selectTab(0);
        if(DataHpler.getFirstShow("productMsg"))
        BaseDialog.showMag(mActivity, "项目发布说明",
                "完结项目适合施工方展示完工后的项目，通过发布完成后的项目，" +
                        "可以让更多需求方看到，即能扩展业务，又能欣赏更多同行的优秀作品，" +
                        "可在未发布里建立自己的暂存项目，记录我做过的项目作品，" +
                        "未发布跟已发布的项目都可以分享给好友，本次使用说明只出现这一次，" +
                        "如不清楚，下次请在“设置”-“使用帮助”中查看。", "我知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DataHpler.setFirstShow("productMsg");
                    }
                });
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
        ProjectNFragment projectNFragment = new ProjectNFragment();
        ProjectPFragment projectPFragment = new ProjectPFragment();
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
