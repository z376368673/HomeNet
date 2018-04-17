package com.benkie.hjw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.bean.TypeBean;
import com.benkie.hjw.utils.Tools;

import java.util.List;

/**
 * Created by 37636 on 2018/1/23.
 */

public class ProductNAdapter extends ArrayAdapter<HomeProductBean> {
    static Context context;

    public ProductNAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_product_n, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeProductBean item = getItem(position);
        holder.initData(item, position);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_img;
        TextView tv_name;
        TextView tv_type;
        TextView tv_address;
        TextView tv_edit;
        TextView tv_fabu;

        void initView(View convertView) {
            iv_img = convertView.findViewById(R.id.iv_img);
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_type = convertView.findViewById(R.id.tv_type);
            tv_address = convertView.findViewById(R.id.tv_address);
            tv_edit = convertView.findViewById(R.id.tv_edit);
            tv_fabu = convertView.findViewById(R.id.tv_fabu);
        }

        void initData(final HomeProductBean item, int p) {
            Picture picture = item.getImgs();
            if (picture != null) {
                Tools.loadImg(context, iv_img, picture.getPicture());
                List<TypeBean> services = picture.getService();
                tv_type.setText(getServiceString(services));
            } else {
                iv_img.setImageResource(R.mipmap.iv_up_img_p);
            }
            tv_name.setText(item.getName());
            tv_address.setText(item.getCity().replace("市",""));
            tv_fabu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Tools.isFastClick())
                    if (adapterInterface!=null)
                        adapterInterface.toPublic(item);
                }
            });
            tv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (adapterInterface!=null)
                        adapterInterface.toEdit(item);
                }
            });
        }

        public String getServiceString(List<TypeBean> services) {
            String str = "";
            for (int i = 0; i < services.size(); i++) {
                str += services.get(i).getName() + "/";
            }
            if (str.length() > 1) {
                str = str.substring(0, str.length() - 1);
            }
            return str;
        }
    }

    public void setAdapterInterface(AdapterInterface adapterInterface) {
        this.adapterInterface = adapterInterface;
    }

    private  AdapterInterface adapterInterface;
    public interface  AdapterInterface{
       public void toEdit(Object obj);
        public  void toPublic(HomeProductBean item);
    }
}
