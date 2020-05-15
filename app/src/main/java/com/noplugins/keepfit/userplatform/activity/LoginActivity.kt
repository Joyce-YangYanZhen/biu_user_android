package com.noplugins.keepfit.userplatform.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.text.*
import android.text.style.StyleSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import cn.jiguang.analytics.android.api.CountEvent
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.MainActivity
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.LoginBean
import com.noplugins.keepfit.userplatform.entity.CheckInEntity
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.BaseUtils
import com.noplugins.keepfit.userplatform.util.MD5
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.data.StringsHelper
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.screen.KeyboardUtils
import com.noplugins.keepfit.userplatform.util.ui.pop.CommonPopupWindow
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.RequestBody
import java.util.*

class LoginActivity : BaseActivity() {
    override fun initBundle(parms: Bundle?) {

    }

    override fun initView() {
        setContentView(R.layout.activity_login)
        val content = "登录注册即代表您已同意哔呦《用户协议和隐私政策》"
        val mSpannableString = SpannableString(content)
        val styleSpan_B = StyleSpan(Typeface.BOLD)
        mSpannableString.setSpan(styleSpan_B, content.length - 11, content.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        tv_user_protocol.setText(mSpannableString)

    }

    override fun doBusiness(mContext: Context?) {
        init()
    }

    private var nowType = 0 //默认是验证码登陆
    private var messageId = ""
    private fun init() {
        tv_register.setOnClickListener {
            //为验证码登陆 时：
            if (nowType == 1) {
                nowType = 0
                tv_register.text = "密码登陆"
                tv_send.visibility = View.VISIBLE
                edit_password.hint = "请输入验证码"
                edit_password.setText("")
                edit_password.filters = arrayOf<InputFilter>(object : InputFilter.LengthFilter(6) {

                })
                edit_password.inputType = InputType.TYPE_CLASS_NUMBER

            } else {
                //为密码登陆 时：
                nowType = 1
                edit_password.setText("")
                tv_register.text = "验证码登陆"
                tv_send.visibility = View.GONE
                edit_password.hint = "请输入密码"
                edit_password.filters = arrayOf<InputFilter>(object : InputFilter.LengthFilter(16) {

                })
                edit_password.inputType = 0x00000081
            }
        }

        tv_send.clickWithTrigger {
            if (edit_phone_number.text.toString() == "") {
                Toast.makeText(applicationContext, "电话号码不能为空！", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (!StringsHelper.isMobileNO(edit_phone_number.text.toString())) {
                Toast.makeText(applicationContext, "电话号码格式不正确！", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            tv_send.isEnabled = false//设置不可点击，等待60秒过后可以点击
            timer.start()
            //获取验证码接口
            send()
        }

        tv_user_protocol.setOnClickListener {
            if (BaseUtils.isFastClick()) {
                xieyi_pop()
            }
        }
        xieyi_check_btn.setOnClickListener {
            if (BaseUtils.isFastClick()) {
                if (xieyi_check_btn.isChecked) {
                    xieyi_pop()
                }
            }
        }

        iv_delete_edit.clickWithTrigger {
            edit_phone_number.setText("")
        }
        login_btn.clickWithTrigger {
            login()
        }

        tv_user_protocol.setOnTouchListener(View.OnTouchListener { v, event ->
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val v = window.peekDecorView()
            val isBoolean = inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0)
            edit_password.clearFocus()
            edit_phone_number.clearFocus()
            isBoolean
        })
        xieyi_check_btn.setOnTouchListener(View.OnTouchListener { v, event ->
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val v = window.peekDecorView()
            val isBoolean = inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0)
            edit_password.clearFocus()
            edit_phone_number.clearFocus()
            isBoolean
        })

    }

    private fun send() {
        val params = HashMap<String, Any>()
        params["phone"] = edit_phone_number.text.toString()
        params["sign"] = "${MD5.stringToMD5("MES" + edit_phone_number.text.toString())}"
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
                        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                    }

                }, this, true)
            )


    }

    private fun login() {
        if (edit_phone_number.text.toString() == "") {
            Toast.makeText(applicationContext, "电话号码不能为空！", Toast.LENGTH_SHORT).show()
            return
        }
        if (!StringsHelper.isMobileNO(edit_phone_number.text.toString())) {
            Toast.makeText(applicationContext, "电话号码格式不正确！", Toast.LENGTH_SHORT).show()
            return
        }
        if (edit_password.text.toString() == "" && nowType == 0) {
            Toast.makeText(applicationContext, "验证码不能为空！", Toast.LENGTH_SHORT).show()
            return
        }
        if (edit_password.text.toString() == "" && nowType == 1) {
            Toast.makeText(applicationContext, "密码不能为空！", Toast.LENGTH_SHORT).show()
            return
        }
        if (!xieyi_check_btn.isChecked) {
            Toast.makeText(applicationContext, "请先勾选用户协议！", Toast.LENGTH_SHORT).show()
            return
        }

        if (nowType == 0) {
            codeLogin()
        } else {
            //搜索埋点
            val cEvent = CountEvent("密码登陆")
            cEvent.addKeyValue(
                "user", "pwd"
            )
            passwordLogin()
        }


    }

    private fun codeLogin() {
        val params = HashMap<String, Any>()
        params["messageId"] = messageId
        params["code"] = edit_password.text.toString()
        params["phone"] = edit_phone_number.text.toString()
        Network.getInstance("验证验证码", this)
            .check_yanzhengma(
                params, ProgressSubscriber("验证验证码", object : SubscriberOnNextListener<Bean<LoginBean>> {
                    override fun onNext(code: Bean<LoginBean>) {
                        SpUtils.putString(applicationContext, AppConstants.PHONE, edit_phone_number.text.toString())
                        SpUtils.putString(applicationContext, AppConstants.TOKEN, code.data.token)
                        SpUtils.putString(applicationContext, AppConstants.USER_NAME, code.data.userNum)
                        SpUtils.putInt(applicationContext, AppConstants.RIGHT, code.data.right)
                        SpUtils.putString(applicationContext, AppConstants.LOGO, code.data.logo)
                        if (code.data.firstLoad == 1) {
                            val intent = Intent(this@LoginActivity, PasswordSettingActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(
                                this@LoginActivity,
                                MainActivity::class.java
                            )
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onError(error: String?) {
                        Logger.e(TAG, "接收验证码报错：$error")
                        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                    }

                }, this, true)
            )


    }

    private fun passwordLogin() {
        val password_login_params = HashMap<String, Any>()
        password_login_params["password"] = edit_password.text.toString()
        password_login_params["phone"] = edit_phone_number.text.toString()
        Network.getInstance("登录", this)
            .login(
                password_login_params, ProgressSubscriber("登录", object : SubscriberOnNextListener<Bean<LoginBean>> {
                    override fun onNext(result: Bean<LoginBean>) {
                        SpUtils.putString(applicationContext, AppConstants.PHONE, edit_phone_number.text.toString())
                        SpUtils.putString(applicationContext, AppConstants.TOKEN, result.data.token)
                        SpUtils.putString(applicationContext, AppConstants.USER_NAME, result.data.userNum)
                        SpUtils.putInt(applicationContext, AppConstants.RIGHT, result.data.right)
                        SpUtils.putString(applicationContext, AppConstants.LOGO, result.data.logo)
                        toMain()
                    }

                    override fun onError(error: String) {
                        Logger.e(TAG, "密码登陆报错：$error")
                        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                    }
                }, this, true)
            )

    }


    private fun xieyi_pop() {
        val popupWindow = CommonPopupWindow.Builder(this)
            .setView(R.layout.xieyi_pop_layout)
            .setBackGroundLevel(1F)//0.5f
            .setAnimationStyle(R.style.main_menu_animstyle)
            .setWidthAndHeight(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            .setOutSideTouchable(true).create()
        popupWindow.showAsDropDown(rel_content)

        /**设置逻辑 */
        val view = popupWindow.getContentView()
        val agree_btn = view.findViewById<TextView>(R.id.agree_btn)
        agree_btn.setOnClickListener(View.OnClickListener {
            popupWindow.dismiss()
            xieyi_check_btn.isChecked = true
        })
        val no_agree_btn = view.findViewById<TextView>(R.id.no_agree_btn)
        no_agree_btn.setOnClickListener(View.OnClickListener {
            xieyi_check_btn.isChecked = false
            popupWindow.dismiss()
        })
        val webView = view.findViewById<WebView>(R.id.webView)
        //自适应屏幕
        val webSettings = webView.getSettings()
        webSettings.setUseWideViewPort(true)//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true)
        webView.loadUrl("http://www.noplugins.com/doc/yonghu_xieyi.html")
    }

    internal var timer: CountDownTimer = object : CountDownTimer(60000, 1000) {
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            tv_send.setTextColor(Color.parseColor("#292C31"))
            tv_send.text = "已发送(${millisUntilFinished / 1000})"

        }

        override fun onFinish() {
            tv_send.setTextColor(Color.parseColor("#76CEE1"))
            tv_send.text = "重新获取"
            tv_send.isEnabled = true
        }
    }

    private var exitTime: Long = 0 //必须是long型
    override fun onBackPressed() {
        println(System.currentTimeMillis())
        if (System.currentTimeMillis() - exitTime < 2000) {
            finish()
        } else {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show()
            exitTime = System.currentTimeMillis()
        }
    }


}