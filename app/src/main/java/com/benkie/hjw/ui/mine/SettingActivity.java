package com.benkie.hjw.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.ui.setting.AboutOurActivity;
import com.benkie.hjw.ui.setting.FeedbackActivity;
import com.benkie.hjw.ui.setting.ModifyAccountActivity;
import com.benkie.hjw.ui.setting.ModifyPasswordActivity;
import com.benkie.hjw.ui.setting.UseHelpActivity;
import com.benkie.hjw.ui.setting.UserHelp_WebActivity;
import com.benkie.hjw.utils.CacheUtil;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.tv_cache)
    TextView tv_cache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        headView.setBtBack(this);
        headView.setTitle("设置");
        try {
            String cache = CacheUtil.getTotalCacheSize(this);
            tv_cache.setText(cache);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改密码
     * @param view
     */
    public void modifyPassword(View view){
        Intent intent = new Intent(this, ModifyPasswordActivity.class);
        startActivity(intent);
    }

    /**
     * 更改账户
     * @param view
     */
    public void modifyAccount(View view){
        Intent intent = new Intent(this, ModifyAccountActivity.class);
        startActivity(intent);
    }

    /**
     * 使用帮助
     * @param view
     */
    public void useHelp(View view){
        Intent intent = new Intent(this, UserHelp_WebActivity.class);
        startActivity(intent);
    }

    /**
     * 清理缓存
     * @param view
     */
    public void clearCache(View view){
        CacheUtil.clearAllCache(this);
        tv_cache.setText("0K");
        ToastUtil.showInfo(mActivity,"缓存已清空");
    }
    /**
     * 用户反馈
     * @param view
     */
    public void feedback(View view){
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
    }

    /**
     * 关于我们
     * @param view
     */
    public void aboutOurs(View view){
        Intent intent = new Intent(this, AboutOurActivity.class);
        startActivity(intent);
    }

    /**
     * 退出
     * @param view
     */
    public void exit(View view){
        BaseDialog.dialogStyle1(mActivity, "您确认要退出此账户?",
                "退出", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataHpler.exit();
                BaseActivity.toLogin(mActivity);
                finish();
            }
        });
    }
}
