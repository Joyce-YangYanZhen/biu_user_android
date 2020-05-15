package com.noplugins.keepfit.userplatform.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.util.net.callback.ErrorCallback;
import com.openxu.utils.LogUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.ql0571.loadmanager.core.LoadManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.*;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import okhttp3.OkHttpClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * Created by shiyujia02 on 2017/8/3.
 */

public class MyApplication extends MultiDexApplication {
    private static final String TAG = MyApplication.class.getSimpleName();
    public static List<Integer> list = new ArrayList<>();

    private static MyApplication _application;//本类的实例
    public static OkHttpClient okHttpClient;

    //qcl用来在主线程中刷新ui
    private static Handler mHandler;
    public static String registrationId;
    public static boolean is_first = true;

    public static String[] lvjings = null;

    //保存List集合
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private static Map<String, Activity> destoryMap = new HashMap<>();

    public static UploadManager uploadManager;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.transparent, android.R.color.darker_gray);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _application = this;

        Context context = getApplicationContext();
        JAnalyticsInterface.init(context);
        // 获取当前包名
        String packageName = context.getPackageName();
// 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
// 初始化Bugly
//        CrashReport.initCrashReport(context, "注册时申请的APPID", false, strategy);
        CrashReport.initCrashReport(getApplicationContext(), "e5856a4862", false);

        //初始化handler
        mHandler = new Handler();
        tbsInit();
        //注册okhttp
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @SuppressLint("BadHostnameVerifier")
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);

        //网络监听（重连）
        LoadManager.beginBuilder()
                .addCallback(new ErrorCallback())
                .commit();

        //初始化友盟分享
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5d9c56903fc1955345000b1e");
        PlatformConfig.setWeixin("wx159898ada9f8208a", "cbfb0a0daffee6d180d631e0d6285535");
        PlatformConfig.setSinaWeibo("3693815547", "299a99fb3055accf9f1185e4879d1f69", "http://sns.whalecloud.com/sina2/callback");//微博
        PlatformConfig.setQQZone("101797759", "b016aca4f940f725d896cbec303d59e2");

        //日志初始化
        Logger.addLogAdapter(new AndroidLogAdapter());

        //初始化七牛云
        Recorder recorder = null;
        String dirPath = "/storage/emulated/0/Download";
        try {
            File f = File.createTempFile("qiniu_xxxx", ".tmp");
            Log.d("qiniu", f.getAbsolutePath().toString());
            dirPath = f.getParent();
            recorder = new FileRecorder(dirPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
                //.recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
        uploadManager = new UploadManager(config);// 重用uploadManager。一般地，只需要创建一个uploadManager对象

        /**极光*/
        JPushInterface.init(this);            // 初始化 JPush
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        if (!rid.isEmpty()) {
            registrationId = rid;
            Log.e("极光registrationId", registrationId);
        } else {
//            Toast.makeText(this, "Get registration fail, JPush init failed!", Toast.LENGTH_SHORT).show();
        }

        /**方法负载过多解决*/
        MultiDex.install(this);


    }

    /**
     * tbs init
     */
    private void tbsInit(){
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("X5", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                Log.d("X5", " onCoreInitFinished ");
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }


    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */

    public static void addDestoryActivity(Activity activity, String activityName) {
        destoryMap.put(activityName, activity);
    }

    /**
     * 销毁指定Activity
     */
    public static void destoryActivity(String activityName) {
        Set<String> keySet = destoryMap.keySet();
        for (String key : keySet) {
            destoryMap.get(key).finish();
        }
    }

    @Override
    public void onTerminate() {


        Log.d(TAG, "onTerminate");
        super.onTerminate();
    }

    /**
     * 在主线程中刷新UI的方法
     **/
    public static void runOnUIThread(Runnable r) {
        MyApplication.getMainHandler().post(r);
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static MyApplication getApplication() {
        return _application;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
