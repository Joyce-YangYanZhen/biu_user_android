package com.noplugins.keepfit.userplatform.activity.teahcher

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.CourseBean
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_private_class.*
import okhttp3.RequestBody
import java.util.*

class PrivateClassActivity : BaseActivity() {
    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {
        setContentView(R.layout.activity_private_class)
        requestClass()
    }

    override fun doBusiness(mContext: Context?) {
        back_btn.clickWithTrigger {
            finish()
        }
     }

    private fun setting(code:CourseBean){
        tv_class_name.text = code.courseName
        tv_class_type.text = code.teacherCourseType
        tv_class_summary.text = code.courseDes
        tv_class_prive.text = "¥${code.finalPrice}/h"
        tv_class_note.text = code.tips
        tv_class_time.text = "1小时"
        tv_fit_man.text = code.suitPerson
    }

    private fun requestClass(){
        val params = HashMap<String, Any>()
        params["courseNum"] = intent.getStringExtra("courseNum")

        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("课程详情", applicationContext)
            .courseDetail(
                requestBody,
                ProgressSubscriberNew(
                    CourseBean::class.java,
                    object : GsonSubscriberOnNextListener<CourseBean> {
                        override fun on_post_entity(code: CourseBean, get_message_id: String) {
                            //
                            setting(code)
                        }
                    },
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            Logger.e("", "获取课程详情失败：$error")
                            Toast.makeText(applicationContext, "获取课程详情失败！", Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }
}
