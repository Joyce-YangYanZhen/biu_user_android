package com.noplugins.keepfit.userplatform.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.MainActivity
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.mvc_demo.MVC_LoginActivity

class SplashActivity : BaseActivity() {
    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {
        if (!isTaskRoot) {
            val intent = intent
            val intentAction = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction == Intent
                    .ACTION_MAIN
            ) {
                finish()
                return
            }
        }
        // 判断是否是第一次开启应用

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable { goWelcome() }, 2000)
        // 如果是第一次启动，则先进入功能引导页




    }

    override fun doBusiness(mContext: Context?) {
    }

    private fun goWelcome(){
        val isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN)
        if (!isFirstOpen) {
            val intent = Intent(this, WelcomeGuideActivity::class.java)
            startActivity(intent)
            finish()
            return
        } else {
            enterHomeActivity()
        }

    }


    private fun enterHomeActivity() {
        val intent = if (SpUtils.getString(applicationContext, AppConstants.TOKEN) == "") {
            Intent(
                this@SplashActivity,
                MVC_LoginActivity::class.java)
        } else {
            Intent(
                this@SplashActivity,
                MainActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
