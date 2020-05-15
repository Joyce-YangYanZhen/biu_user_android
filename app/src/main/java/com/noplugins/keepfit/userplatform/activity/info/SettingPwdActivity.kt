package com.noplugins.keepfit.userplatform.activity.info

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.LoginActivity
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.ActivityCollectorUtil
import com.noplugins.keepfit.userplatform.util.MD5
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.data.PwdCheckUtil
import com.noplugins.keepfit.userplatform.util.data.StringsHelper
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_setting_pwd.*
import okhttp3.RequestBody
import java.util.*

class SettingPwdActivity : BaseActivity() {
    private var messageId = ""
    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {
        setContentView(R.layout.activity_setting_pwd)
        edit_phone.setText(SpUtils.getString(applicationContext,AppConstants.PHONE))
    }

    override fun doBusiness(mContext: Context?) {
        back_btn.clickWithTrigger {
            finish()
        }
        btn_pwd_update.clickWithTrigger {
            if (edit_phone.text.toString() == "") {
                Toast.makeText(applicationContext, "电话号码不能为空！", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (!StringsHelper.isMobileNO(edit_phone.text.toString())) {
                Toast.makeText(applicationContext, "电话号码格式不正确！", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (edit_yzm.text.toString() == "") {
                Toast.makeText(applicationContext, "验证码不能空！", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (edit_new_password.text.toString() == "") {
                Toast.makeText(applicationContext, "密码不能为空！", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (!PwdCheckUtil.isLetterDigit(edit_new_password.text.toString())) {
                Toast.makeText(applicationContext, "密码不符合规则！", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (edit_new_password1.text.toString() == "") {
                Toast.makeText(applicationContext, "确认密码不能为空！", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (edit_new_password1.text.toString() != edit_new_password.text.toString()) {
                Toast.makeText(applicationContext, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            settingPwd()
        }

        tv_send.clickWithTrigger {
            if (edit_phone.text.toString() == "") {
                Toast.makeText(applicationContext, "电话号码不能为空！", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (!StringsHelper.isMobileNO(edit_phone.text.toString())) {
                Toast.makeText(applicationContext, "电话号码格式不正确！", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            tv_send.isEnabled = false//设置不可点击，等待60秒过后可以点击
            timer.start()
            //获取验证码接口
            send()
        }
    }


    private fun settingPwd() {
        val params = HashMap<String, Any>()
        params["userNum"] = SpUtils.getString(applicationContext,AppConstants.USER_NAME)
        params["phone"] = SpUtils.getString(applicationContext,AppConstants.PHONE)
        params["messageId"] = messageId
        params["verifyCode"] = edit_yzm.text.toString()
        params["password"] = edit_new_password.text.toString()

        val subscription = Network.getInstance("修改密码", this)
            .settingPassword(
                params,
                ProgressSubscriber<Any>(
                    "修改密码",
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {
                            Toast.makeText(applicationContext, "设置成功", Toast.LENGTH_SHORT).show()
                            toLogin()
                        }

                        override fun onError(error: String) {
                            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    private fun toLogin() {
        val intent = Intent(this@SettingPwdActivity, LoginActivity::class.java)
        //退出
        SpUtils.putString(applicationContext, AppConstants.TOKEN, "")
        SpUtils.putString(applicationContext, AppConstants.PHONE, "")
        SpUtils.putString(applicationContext, AppConstants.USER_NAME, "")
        startActivity(intent)
        ActivityCollectorUtil.finishAllActivity()

    }
    private fun send() {
        val params = HashMap<String, Any>()
        params["phone"] = edit_phone.text.toString()
        params["sign"] = "${MD5.stringToMD5("MES"+edit_phone.text.toString())}"
        params["time"] = System.currentTimeMillis()
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

    internal var timer: CountDownTimer = object : CountDownTimer(60000, 1000) {
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            tv_send.setTextColor(Color.parseColor("#292C31"))
            tv_send.text = "已发送(${millisUntilFinished / 1000})"

        }

        override fun onFinish() {
            tv_send.setTextColor(Color.parseColor("#75CEE1"))
            tv_send.text = "重新获取"
            tv_send.isEnabled = true
        }
    }

}
