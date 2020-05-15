package com.noplugins.keepfit.userplatform.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import cn.jiguang.analytics.android.api.CountEvent
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.PasswordBean
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.data.PwdCheckUtil
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_password_setting.*
import okhttp3.RequestBody
import java.util.*

class PasswordSettingActivity : BaseActivity() {
    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {
        setContentView(R.layout.activity_password_setting)
        SpUtils.putBoolean(applicationContext, AppConstants.FIRST_PASSWPRD, true)
    }

    override fun doBusiness(mContext: Context?) {
        login_btn.clickWithTrigger {
            submit()
        }

        tv_jump.clickWithTrigger {
            //搜索埋点
            val cEvent = CountEvent("跳过密码设置")
            cEvent.addKeyValue(
                "user",
                SpUtils.getString(applicationContext, AppConstants.USER_NAME)
            )
            ///////////////////////////////
            toNextActivity()
        }
        iv_delete_edit.clickWithTrigger {
            edit_password.setText("")
        }
    }

    private fun submit() {
        if (edit_password.text.toString() == "") {
            Toast.makeText(applicationContext, "密码不能为空！", Toast.LENGTH_SHORT).show()
            return
        }
        if (!PwdCheckUtil.isLetterDigit(edit_password.text.toString())) {
            Toast.makeText(applicationContext, "密码不符合规则！", Toast.LENGTH_SHORT).show()
            return
        }
        if (edit_password_2.text.toString() == "") {
            Toast.makeText(applicationContext, "确认密码不能为空！", Toast.LENGTH_SHORT).show()
            return
        }
        if (edit_password_2.text.toString() != edit_password.text.toString()) {
            Toast.makeText(applicationContext, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show()
            return
        }
        //搜索埋点
        val cEvent = CountEvent("设置密码")
        cEvent.addKeyValue(
            "user",
            SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        )
        ///////////////////////////////
        send()
        //
    }

    private fun send() {
        val params = HashMap<String, String>()
        params["phone"] = SpUtils.getString(applicationContext, AppConstants.PHONE)
        params["password"] = edit_password.text.toString()
        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("设置密码", applicationContext)
            .setPassword(
                requestBody,
                ProgressSubscriberNew(PasswordBean::class.java, object : GsonSubscriberOnNextListener<PasswordBean> {
                    override fun on_post_entity(code: PasswordBean, get_message_id: String) {
                        toNextActivity()
                    }
                }, object : SubscriberOnNextListener<Bean<Any>> {
                    override fun onNext(result: Bean<Any>) {

                    }

                    override fun onError(error: String) {
                        Logger.e(TAG, "密码设置失败：$error")
                        Toast.makeText(applicationContext, "密码设置失败！", Toast.LENGTH_SHORT).show()
                    }
                }, this, true)
            )
    }


    override fun onBackPressed() {
//        super.onBackPressed()
        toNextActivity()

    }
    private fun toNextActivity() {

        val intent = Intent(
            this@PasswordSettingActivity,
            InformationSettingActivity::class.java
        )
        startActivity(intent)
        finish()
    }
}
