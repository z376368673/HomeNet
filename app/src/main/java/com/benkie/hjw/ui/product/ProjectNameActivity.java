package com.benkie.hjw.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProjectNameActivity extends BaseActivity {
    @BindView(R.id.headView)
    HeadView headView;

    @BindView(R.id.ed_name)
    EditText ed_name;
    @BindView(R.id.tv_count)
    TextView tv_count;
    private boolean isModify = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_name);
        ButterKnife.bind(this);
        headView.setTitle("项目名称");
        headView.setBtBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        headView.setBtText("保存");
        String name = getIntent().getStringExtra("Name");
        if (name != null)
            ed_name.setText(name);
        headView.setBtClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickName = ed_name.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("Name", nickName);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isModify = true;
                if (charSequence.length() > 10) {
                    ToastUtil.showInfo(mActivity, "最多输入10个字符");
                }
                tv_count.setText(charSequence.length() + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isModify)
            BaseDialog.dialogStyle2(mActivity, "您的修改还未保存，您确认退出吗？", "保存", "放弃", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    if (tag == 1) {
                        String nickName = ed_name.getText().toString();
                        Intent intent = new Intent();
                        intent.putExtra("Name", nickName);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        finish();
                    }
                }
            });
        else super.onBackPressed();
    }
}
