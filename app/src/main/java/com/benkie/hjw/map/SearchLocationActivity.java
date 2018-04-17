package com.benkie.hjw.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.benkie.hjw.R;
import com.benkie.hjw.ui.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 37636 on 2018/1/22.
 */

public class SearchLocationActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_keyword)
    EditText tv_keyword;
    @BindView(R.id.tv_save)
    TextView tv_save;
    @BindView(R.id.listView)
    ListView listView;

    MapAdapter mapAdapter;

    private PoiSearch.Query query;//搜索
    private static MapBean mapBean;

    public static  MapBean getMapBean() {
        return mapBean;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        ButterKnife.bind(this);
        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        mapAdapter = new MapAdapter(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        listView.setAdapter(mapAdapter);
        mapBean = null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mapBean = (MapBean) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("LatLonPoint", mapBean.getPoiItem().getLatLonPoint());
                String address = mapBean.getPoiItem().getProvinceName()+mapBean.getPoiItem().getCityName() +mapBean.getPoiItem().getAdName()+ mapBean.getPoiItem().getSnippet();
                bundle.putString("Address", address);
                bundle.putString("City", mapBean.getPoiItem().getCityName());
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        tv_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length()>=2)search(charSequence.toString());
                    else if (charSequence.length()>=0){
                        mapAdapter.clear();
                        mapAdapter.notifyDataSetChanged();
                    }else {

                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                String key = tv_keyword.getText().toString();
                if (key != null && !TextUtils.isEmpty(key)) {
                    search(key);
                }
                break;
        }
    }

    /**
     * 周边搜索
     *
     * @param keyWord 搜索关键字
     */
    private void search(String keyWord) {
        query = new PoiSearch.Query(keyWord, "", "");
//				//keyWord表示搜索字符串，
//				//第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//				//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(50);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码


        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
        mapAdapter.clear();
        mapAdapter.addAll(MapBean.addAll(poiItems));
        mapAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
