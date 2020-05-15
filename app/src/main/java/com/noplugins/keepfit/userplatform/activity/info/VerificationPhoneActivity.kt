package com.noplugins.keepfit.userplatform.activity.info

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.LoginActivity
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.jpush.TagAliasOperatorHelper
import com.noplugins.keepfit.userplatform.util.ActivityCollectorUtil
import com.noplugins.keepfit.userplatform.util.BaseUtils
import com.noplugins.keepfit.userplatform.util.MD5
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_verification_phone.*
import okhttp3.RequestBody
import java.util.*

class VerificationPhoneActivity : BaseActivity() {
    var messageId = ""
    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {
        setContentView(R.layout.activity_verification_phone)
//        tv_now_phone.text = SpUtils.getString(applicationContext, AppConstants.PHONE)
        tv_phone.text = intent.getStringExtra("newPhone")
    }

    override fun doBusiness(mContext: Context?) {
        back_btn.setOnClickListener {
            finish()
        }
        tv_send.setOnClickListener {
            tv_send.isEnabled = false//设置不可点击，等待60秒过后可以点击
            timer.start()
            //获取验证码接口
            send()
        }
        btn_ToLogin.setOnClickListener {
           if (BaseUtils.isFastClick()){
               updatePhone()
           }
        }
    }

    private fun send() {
        val params = HashMap<String, Any>()
        params["phone"] = tv_phone.text.toString()
        params["sign"] = "${MD5.stringToMD5("MES"+tv_phone.text.toString())}"
        params["time"] = System.currentTimeMillis()
        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)
        Network.getInstance("接收验证码", this)
            .get_yanzhengma(
                params, ProgressSubscriber("接收验证码", object : SubscriberOnNextListener<Bean<String>> {
                    override fun onNext(code: Bean<String>) {
                        Toast.makeText(applicationContext, "发送成功", Toast.LENGTH_SHORT).show()
                        messageId = code.data
                    }

                    override fun onError(error: String?) {
                        Logger.e(TAG, "接收验证码报错：$error")
                        Toast.makeText(applicationContext, "接收验证码失败！", Toast.LENGTH_SHORT).show()
                    }

                }, this, true)
            )
    }
    private fun updatePhone() {
        if (tv_send.text.toString() == "") {
            Toast.makeText(applicationContext, "验证码不能为空！", Toast.LENGTH_SHORT).show()
            return
        }
        val params = HashMap<String, Any>()
        params["phone"] = tv_phone.text.toString()
        params["messageId"] = messageId
        params["verifyCode"] = edit_yzm.text.toString()
        params["userNum"] = SpUtils.getString(applicationContext,AppConstants.USER_NAME)
        subscription = Network.getInstance("修改手机号", this)
            .modificationPhone(
                params,
                ProgressSubscriber<Any>(
                    "修改手机号",
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {
                            toLogin()
                        }

                        override fun onError(error: String) {
                            Log.e(TAG, "修改失败：" +error)
                            Toast.makeText(applicationContext,error, Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }


    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        //退出
        SpUtils.putString(applicationContext, AppConstants.TOKEN, "")
        SpUtils.putString(applicationContext, AppConstants.PHONE, "")
        SpUtils.putString(applicationContext, AppConstants.USER_NAME, "")
        SpUtils.putInt(applicationContext,AppConstants.PROMOTIONS,-1)
        SpUtils.putString(applicationContext,AppConstants.IS_SET_ALIAS,"")

        val tagAliasBean = TagAliasOperatorHelper.TagAliasBean()
        tagAliasBean.alias = ""
        TagAliasOperatorHelper.getInstance().handleAction(
            applicationContext,
            TagAliasOperatorHelper.sequence, tagAliasBean
        )
        startActivity(intent)
        ActivityCollectorUtil.finishAllActivity()

    }
    internal var timer: CountDownTimer = object : CountDownTimer(60000, 1000) {
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            tv_send.setTextColor(Color.parseColor("#292C31"))
            tv_send.text = "已发送(${millisUntilFinished / 1000})"

        }

        override fun onFinish() {
            tv_send.setTextColor(Color.parseColor("#FFBA02"))
            tv_send.text = "重新获取"
            tv_send.isEnabled = true
        }
    }
}
