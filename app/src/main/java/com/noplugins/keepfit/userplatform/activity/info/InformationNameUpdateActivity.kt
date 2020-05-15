package com.noplugins.keepfit.userplatform.activity.info

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_information_name_update.*
import okhttp3.RequestBody
import java.util.*
import java.util.regex.Pattern

class InformationNameUpdateActivity : BaseActivity() {
    override fun initBundle(parms: Bundle?) {

    }
    var name = ""
    override fun initView() {
        setContentView(R.layout.activity_information_name_update)
       if (null!= intent.getStringExtra("nickname")){
           name = intent.getStringExtra("nickname")
       }
        edit_name.setText(name)
     }

    override fun doBusiness(mContext: Context?) {
        btn_ok.setOnClickListener {
            if (edit_name.text.toString() != "" && edit_name.text.toString()!=name){
                if (compileExChar(edit_name.text.toString())){
                    updateInfo()
                }
            } else {
                Toast.makeText(applicationContext,"昵称不能重复且不能为空", Toast.LENGTH_SHORT).show()
            }
        }
        back_btn.setOnClickListener {
            finish()
        }
    }

    private fun updateInfo(){
        val params = HashMap<String, Any>()
        params["userNum"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        params["nickname"] = edit_name.text.toString()



        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("修改资料", applicationContext)
            .setPersonalData(
                requestBody,
                ProgressSubscriber<Any>(
                    "获取个人信息",
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                            finish()
                        }

                        override fun onError(error: String) {
                            Log.e(TAG, "获取个人信息：" +error)
                            Toast.makeText(applicationContext,error, Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    private fun compileExChar(str:String):Boolean{
        val limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
        val pattern = Pattern.compile(limitEx)
        val m = pattern.matcher(str)
        if( m.find()){
            Toast.makeText(this, "不允许输入特殊符号！", Toast.LENGTH_LONG).show()
            return false
        }
        if (str.length < 2){
            Toast.makeText(this, "昵称长度不能小于2个字符！", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}
