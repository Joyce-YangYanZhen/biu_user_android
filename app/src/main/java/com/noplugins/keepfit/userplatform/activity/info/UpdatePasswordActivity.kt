package com.noplugins.keepfit.userplatform.activity.info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.LoginActivity
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.jpush.TagAliasOperatorHelper
import com.noplugins.keepfit.userplatform.util.ActivityCollectorUtil
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.data.PwdCheckUtil
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_update_password.*
import okhttp3.RequestBody

import java.util.HashMap

class UpdatePasswordActivity : BaseActivity() {

    override fun initBundle(parms: Bundle?) {

    }

    override fun initView() {
        setContentLayout(R.layout.activity_update_password)
        ButterKnife.bind(this)
        isShowTitle(false)
    }


    private fun updatePassword() {

        if (TextUtils.isEmpty(edit_old_password!!.text.toString())) {
            Toast.makeText(this, "旧密码不能为空！", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(edit_new_password1!!.text.toString())) {
            Toast.makeText(this, "新密码不能为空！", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(edit_password2!!.text.toString())) {
            Toast.makeText(this, "确认密码不能为空！", Toast.LENGTH_SHORT).show()
            return
        }
        if (!PwdCheckUtil.isLetterDigit(edit_new_password1!!.text.toString())) {
            Toast.makeText(this, "请输入6-18位同时包含字母 数字的密码！", Toast.LENGTH_SHORT).show()
            return
        }
        if (edit_password2!!.text.toString() == edit_new_password1!!.text.toString()) {
            Toast.makeText(this, "两次密码输入不一致哦！", Toast.LENGTH_SHORT).show()
            return
        }
        val params = HashMap<String, String>()
        params["oldPassword"] = edit_old_password!!.text.toString()
        params["userNum"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        params["password"] = edit_new_password1!!.text.toString()
        val gson = Gson()
        val json_params = gson.toJson(params)
        Log.e(TAG, "修改密码的参数：$json_params")
        val json = Gson().toJson(params)//要传递的json
        val requestBody = RequestBody.create(null, json)

        subscription = Network.getInstance("修改密码", applicationContext)

            .update_my_password(
                requestBody,
                ProgressSubscriberNew(Any::class.java, GsonSubscriberOnNextListener { s, message_id ->
                    Toast.makeText(applicationContext, "修改成功", Toast.LENGTH_SHORT).show()
                    toLogin()
                }, object : SubscriberOnNextListener<Bean<Any>> {
                    override fun onNext(objectBean: Bean<Any>) {

                    }

                    override fun onError(error: String) {
                        Log.e(TAG, "修改失败：$error")
                        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                    }
                }, this, true)
            )
    }

    override fun doBusiness(mContext: Context?) {
        back_btn!!.setOnClickListener { finish() }
        login_btn!!.setOnClickListener { updatePassword() }
    }

    private fun toLogin() {
        val intent = Intent(this@UpdatePasswordActivity, LoginActivity::class.java)
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
}
