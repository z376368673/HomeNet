package com.benkie.hjw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.bean.SkillBean;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.skill.UpDataSkillImgActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;

import java.util.List;

import retrofit2.Call;

/**
 * Created by 37636 on 2018/1/23.
 */

public class SkillEidtAdapter extends ArrayAdapter<SkillBean> {
    static Context context;
    public SkillEidtAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_skill_edit, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SkillBean item = getItem(position);
        holder.initData(item, position);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_img;
        TextView tv_name;
        RadioGroup radio;
        RadioButton radio1;
        RadioButton radio2;
        TextView tv_fabu;
        ImageView iv_delect;

        void initView(View convertView) {
            iv_img = convertView.findViewById(R.id.iv_img);
            tv_name = convertView.findViewById(R.id.tv_name);
            radio = convertView.findViewById(R.id.radio);
            radio1 = convertView.findViewById(R.id.radio1);
            radio2 = convertView.findViewById(R.id.radio2);
            tv_fabu = convertView.findViewById(R.id.tv_fabu);
            iv_delect = convertView.findViewById(R.id.iv_delect);
        }

        void initData(final SkillBean item, int p) {
            List<Picture> imgs = item.getImgs();
            String imgUrl;
            if (imgs != null)
                imgUrl = imgs.size() > 0 ? imgs.get(0).getPicture() : "";
            else imgUrl = "";
            Tools.loadImg(context, iv_img, imgUrl, R.mipmap.iv_up_img_s);
            iv_delect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delect(item);
                    notifyDataSetChanged();
                }
            });
            if (item.getCertificate() == 1)
                radio2.setChecked(true);
            else radio1.setChecked(true);
            tv_name.setText(item.getName());

            radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (skillActionLintener != null)
                        skillActionLintener.checkedChange(item);

                }
            });
            iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (skillActionLintener != null)
                        skillActionLintener.imgOnclickListener(item);
                }
            });
            if (item.getStatus() == 0) {
                tv_fabu.setText("发布技能");
                tv_fabu.setBackgroundResource(R.drawable.shape_button_bg_color_main);
                tv_fabu.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                tv_fabu.setText("取消发布");
                tv_fabu.setBackgroundResource(R.drawable.shape_button_bg_color_white);
                tv_fabu.setTextColor(context.getResources().getColor(R.color.black_66));
            }
            tv_fabu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (skillActionLintener != null)
                        skillActionLintener.fabuListener(item);
                }
            });
        }
    }

    private void delSkill(final SkillBean item) {
        Call call = Http.links.delSkill(item.getId());
        Http.http.call(context, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    remove(item);
                } else {
                    ToastUtil.showInfo(context, "删除失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(context, "删除失败");
            }
        });
    }

    private void delect(final SkillBean item) {
        BaseDialog.dialogStyle1(context, "您确认删除此技能", "删除", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getImgs() == null)
                    ToastUtil.showInfo(context, "请保存后再删除...");
                else
                    delSkill(item);
            }
        });
    }


    private void fabuSkill(final SkillBean item, final int type) {
        int status = item.getStatus();
        int certificate = item.getCertificate();
        if (type == 1) {
            status = item.getStatus() == 0 ? 1 : 0;
        } else {
            certificate = item.getCertificate() == 0 ? 1 : 0;
        }
        Call call = Http.links.updateSkill(item.getId(), status, certificate);
        Http.http.call(context, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    if (type == 1) {
                        int s = item.getStatus() == 0 ? 1 : 0;
                        if (s == 1) {
                            ToastUtil.showInfo(context, "发布成功");
                            item.setStatus(1);
                        } else {
                            ToastUtil.showInfo(context, "取消发布");
                            item.setStatus(0);
                        }
                        notifyDataSetChanged();

                    } else {
                        ToastUtil.showInfo(context, "修改成功");
                        int c = item.getCertificate() == 0 ? 1 : 0;
                        item.setCertificate(c);

                    }
                } else {
                    ToastUtil.showInfo(context, "数据错误");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(context, error);
            }
        });
    }

    public void setSkillActionLintener(SkillActionLintener skillActionLintener) {
        this.skillActionLintener = skillActionLintener;
    }

    SkillActionLintener skillActionLintener;

    public interface SkillActionLintener {

        void imgOnclickListener(SkillBean item);
        void  fabuListener(SkillBean item);
        void  checkedChange(SkillBean item);
    }
}
