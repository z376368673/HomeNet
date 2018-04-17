package com.benkie.hjw.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.benkie.hjw.R;
import com.benkie.hjw.constants.Constants;
import com.benkie.hjw.fragment.Home1Fragment;
import com.benkie.hjw.fragment.Home3Fragment;
import com.benkie.hjw.fragment.Home2Fragment;
import com.benkie.hjw.map.LocationUtils;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.TabItemVIew;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;
import retrofit2.Call;

public class MainActivity extends BaseActivity implements OnTabItemSelectedListener {
    PageNavigationView tabStrip;
    private long millis;

    List<Fragment> fragmentList = new ArrayList<>(4);
    FragmentManager fragmentManager;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int index = intent.getIntExtra("index", 1);
            Fragment fragment = null;
            if (index == 0) {
                fragment = new Home1Fragment();
            } else if (index == 1) {
                fragment = new Home2Fragment();
            } else if (index == 2) {
                fragment = new Home3Fragment();
            }
            Fragment remove = fragmentList.get(index);
            fragmentList.set(index, fragment);
            fragmentManager.beginTransaction().add(R.id.frameLayout, fragment).commitAllowingStateLoss();
            fragmentManager.beginTransaction().remove(remove).commitAllowingStateLoss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLocation();
        tabStrip = (PageNavigationView) findViewById(R.id.tabStrip);
        new NormalItemView(this);
        registerReceiver(receiver, new IntentFilter("refreshFragment"));
        NavigationController navigationController = tabStrip.custom()
                .addItem(newItem(R.mipmap.home_1_n, R.mipmap.home_1_p, "完结项目"))
                .addItem(newItem(R.mipmap.home_2_n, R.mipmap.home_2_p, "技术服务"))
                .addItem(newItem(R.mipmap.home_3_n, R.mipmap.home_3_p, "我的"))
                .build();
        navigationController.addTabItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        Home1Fragment homeFragment = new Home1Fragment();
        Home2Fragment zzbsFragment = new Home2Fragment();
        Home3Fragment technicalsFragment = new Home3Fragment();
        fragmentList.add(homeFragment);
        fragmentList.add(zzbsFragment);
        fragmentList.add(technicalsFragment);
        fragmentManager.beginTransaction().add(R.id.frameLayout, homeFragment).commitAllowingStateLoss();

    }


    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        //创建一个Itemwable, int checkedDrawable, String text){
        TabItemVIew normalItemView = new TabItemVIew(this);
        normalItemView.initialize(drawable, checkedDrawable, text);
        normalItemView.setTextDefaultColor(ContextCompat.getColor(this, R.color.gray_8a));
        normalItemView.setTextCheckedColor(ContextCompat.getColor(this, R.color.colorMain));
        // normalItemView.getmTitle().setTextSize(14);
        return normalItemView;
    }

    @Override
    public void onSelected(int index, int old) {
        Fragment from = fragmentList.get(old);
        Fragment to = fragmentList.get(index);
        FragmentTransaction toTransaction = fragmentManager.beginTransaction();
        if (to.isAdded()) {
            toTransaction.show(to);
        } else {
            toTransaction.add(R.id.frameLayout, to);
        }
        toTransaction.commitAllowingStateLoss();
        FragmentTransaction fromTransaction = fragmentManager.beginTransaction();
        if (from.isAdded()) {
            fromTransaction.hide(from);
        }
        fromTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onRepeat(int index) {

    }

    /**
     * 初始化定位功能
     */
    private void initLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 1);
            } else {
                final LocationUtils locationUtils = new LocationUtils(this);
                locationUtils.startLocation();
                locationUtils.setLocationListener(new LocationUtils.LocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation1) {
                        Constants.address = aMapLocation1.getAddress();
                        Constants.Longitude = aMapLocation1.getLongitude();
                        Constants.Latitude = aMapLocation1.getLatitude();
                        Constants.city = aMapLocation1.getCity();
                        Constants.province = aMapLocation1.getProvince();
                        locationUtils.stopLocation();
                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocation();
            } else {
                ToastUtil.showInfo(mActivity, "权限被拒绝，某些功能将无法使用");
            }
        }
    }


    @Override
    public void onBackPressed() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - millis <= 1500) {
            super.onBackPressed();
        } else {
            millis = currentTimeMillis;
            Toast.makeText(mActivity, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


}
