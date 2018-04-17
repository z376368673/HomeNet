package com.benkie.hjw.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.bean.SkillBean;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.ui.skill.UpDataSkillImgActivity;
import com.benkie.hjw.ui.skill.UpskillImgActivity;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;

import java.util.List;

/**
 * Created by 37636 on 2018/1/23.
 */

public class SkillAdapter extends ArrayAdapter<SkillBean> {
    static Context context;

    public SkillAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_skill, null);
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
        TextView tv_isCertificate;

        void initView(View convertView) {
            iv_img = convertView.findViewById(R.id.iv_img);
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_isCertificate = convertView.findViewById(R.id.tv_isCertificate);
        }

        void initData(final SkillBean item, int p) {
            List<Picture> imgs = item.getImgs();
            String imgUrl;
            if (imgs != null)
                imgUrl = imgs.size() > 0 ? imgs.get(0).getPicture() : "";
            else imgUrl = "";
            Tools.loadImg(context, iv_img, imgUrl, R.mipmap.iv_no_img);

            tv_name.setText(item.getName());
            if (item.getCertificate() == 1)
                tv_isCertificate.setVisibility(View.VISIBLE);
            else
                tv_isCertificate.setVisibility(View.INVISIBLE);
            iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getImgs() == null || item.getImgs().size() == 0) {
                        ToastUtil.showInfo(context, "此技能暂无图片");
                    } else {
                        Intent intent = new Intent(context, UpskillImgActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Title", item);
                        bundle.putInt("Action", 0);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }
            });
        }


    }
}
