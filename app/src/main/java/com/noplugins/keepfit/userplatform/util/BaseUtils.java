package com.noplugins.keepfit.userplatform.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressHUD;

import java.util.ArrayList;
import java.util.List;

public class BaseUtils {
    public static ProgressHUD mProgressHUD;
    /*
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     *
     * @param packageName：应用包名
     *
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static void showProgressDialog(Context mContext) {
        if (mProgressHUD == null) {
            mProgressHUD = new ProgressHUD(mContext);
        }
        mProgressHUD = ProgressHUD.show(mContext, "加载中", false, new DialogInterface.OnCancelListener(){

            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });

    }
    public static void dismissProgressDialog() {
        if (mProgressHUD != null) {
            mProgressHUD.dismiss();
            mProgressHUD = null;
        }
    }

    public static String strSubEnd3(String str){

        String string = str.substring(0, str.length()-3);
        return string;
    }
}
