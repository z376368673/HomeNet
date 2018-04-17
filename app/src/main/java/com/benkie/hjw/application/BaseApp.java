package com.benkie.hjw.application;

import android.app.Application;
import android.util.Log;

import com.benkie.hjw.net.Http;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.SocializeConstants;

import cn.jpush.android.api.JPushInterface;

public class BaseApp extends Application {

    private static BaseApp application;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        application = this;
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(application);
        Http http = Http.getIntens(this);
        umengShare();
        umengCount();

        // 初始化 JPush
        JPushInterface.init(this);     		// 初始化 JPush
        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
    }

    public static BaseApp getInstance() {
        // TODO Auto-generated method stub
        return application;
    }

    private void umengShare() {
        Config.DEBUG = true;
        PlatformConfig.setWeixin("wx490ea6e24c44ebbd", "5efbdae1e96784d06115d276a7827bbb");
        PlatformConfig.setQQZone("1106472106", "5efbdae1e96784d06115d276a7827bbb");
        UMShareAPI.get(this);
       Log.e("友盟SDK_VERSION",SocializeConstants.SDK_VERSION+"");

    }

    /**
     * 友盟统计
     */
    private void umengCount() {
        // TODO Auto-generated method stub
//		MobclickAgent.setDebugMode(true);
//		MobclickAgent.openActivityDurationTrack(false);
//		MobclickAgent.setScenarioType(this, EScenarioType.E_UM_NORMAL);

    }

}
