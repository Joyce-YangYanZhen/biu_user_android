package com.noplugins.keepfit.userplatform.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.adapter.QuanyiAdapter
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.QuanyiBean
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_quanyi_confirm.*
import okhttp3.RequestBody
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class QuanyiConfirmActivity:BaseActivity(){

    private var quanyiAdapter:QuanyiAdapter ?= null
    private var data:ArrayList<QuanyiBean.DataBean> ?= null

    private var name:String = ""
    override fun initBundle(parms: Bundle?) {
     }

    override fun initView() {
        setContentView(R.layout.activity_quanyi_confirm)
        SpUtils.putBoolean(applicationContext, AppConstants.FIRST_QUANYI, true)
        rv_view.layoutManager = LinearLayoutManager(this)

        data = ArrayList()
        quanyiAdapter = QuanyiAdapter(data)
        quanyiAdapter?.setOnItemChildClickListener(object :BaseQuickAdapter.OnItemChildClickListener{
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

                when(view?.id){
                    R.id.tv_confirm -> {
                        Log.d("????","????")
//                        Toast.makeText(applicationContext,"????",Toast.LENGTH_LONG).show()
                        //confirmQuanyi()
                        if (name == ""){
                            onCreateNameDialog(data!!.get(position).memberNum,position)
                        } else {
                            confirmQuanyi(data!!.get(position).memberNum,position)
                        }
                    }
                }
                false
            }

        })
        rv_view.adapter = quanyiAdapter

        getData()

    }

    var m_dialog: Dialog? = null
    private fun onCreateNameDialog(memberNum:String,position:Int) {
        val factory = LayoutInflater.from(this)
        val view = factory.inflate(R.layout.dialog_to_map, null)
        m_dialog = Dialog(this, R.style.transparentFrameWindowStyle2)
        m_dialog!!.setContentView(
            view,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT)
        )
        val window = m_dialog!!.window
        // 设置显示动画
        window!!.setWindowAnimations(R.style.main_menu_animstyle)
        val wl = window.attributes
        wl.x = 0
        wl.y = 0
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT
        // 设置显示位置
        m_dialog!!.onWindowAttributesChanged(wl)
        // 设置点击外围解散
        m_dialog!!.setCanceledOnTouchOutside(true)
        m_dialog!!.show()
        /**操作 */
        //val userInput = view.findViewById<EditText>(R.id.changename_edit)
        val dismiss_tv = view.findViewById<TextView>(R.id.dismiss_tv)
        dismiss_tv.clickWithTrigger { m_dialog!!.dismiss() }
        val phone_tv = view.findViewById<TextView>(R.id.phone_tv)
//        phone_tv.clickWithTrigger {
//            if (userInput.text.toString() == ""){
//                Toast.makeText(applicationContext, "姓名不能为空！", Toast.LENGTH_SHORT).show()
//                return@clickWithTrigger
//            }
//            name = userInput.text.toString()
//            confirmQuanyi(data!!.get(position).memberNum,position)
//            m_dialog!!.dismiss()
//        }
    }

    override fun doBusiness(mContext: Context?) {
        tv_jump.setOnClickListener {
            toMain()
        }
     }

    /**
     * 获取权益列表
     */
    private fun getData(){
        data?.clear()
        val params = HashMap<String, Any>()
        params["phone"] = SpUtils.getString(applicationContext,AppConstants.PHONE)

        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("获取列表", applicationContext)
            .equityAffirm(requestBody,
                ProgressSubscriberNew(QuanyiBean::class.java, object : GsonSubscriberOnNextListener<QuanyiBean> {
                    override fun on_post_entity(code: QuanyiBean, get_message_id: String) {
                        //
                        if (code.data.size > 0){
                            data?.addAll(code.data)
                            quanyiAdapter?.notifyDataSetChanged()
                        }

                    }
                }, object : SubscriberOnNextListener<Bean<Any>> {
                    override fun onNext(result: Bean<Any>) {

                    }

                    override fun onError(error: String) {
                        Logger.e(TAG, "接收验证码报错：$error")
                        Toast.makeText(applicationContext, "获取权益失败！", Toast.LENGTH_SHORT).show()
                    }
                }, this, true)
            )

    }

    private fun confirmQuanyi(memberNum:String,position:Int){
        var memberRights:Int = -1
        val params = HashMap<String, Any>()
        params["memberName"] = name//用户姓名
        params["memberNum"] = memberNum
        val select = if (data!![position].coachServiceTimes > 0){
            1
        } else {
            0
        }
        for(i in 0..(data!!.size-1)){
            //选择的场馆编号
            if (i == position){
                continue
            }
            if (data!![i].memberNum == memberNum){
                memberRights = if (select == 1){

                    if (data!![i].memberRight == 2){
                        3
                    } else{
                        1
                    }
                } else {
                    if (data!![i].memberRight == 1){
                        3
                    } else{
                        2
                    }
                }
            }
        }

        params["memberRights"] = memberRights

        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("权益确认", applicationContext)
            .verifyRights(requestBody,
                ProgressSubscriberNew(Any::class.java, object : GsonSubscriberOnNextListener<Any> {
                    override fun on_post_entity(code: Any, get_message_id: String) {
                        //
                        update(position,select,memberRights)
                    }
                }, object : SubscriberOnNextListener<Bean<Any>> {
                    override fun onNext(result: Bean<Any>) {

                    }

                    override fun onError(error: String) {
                        Logger.e(TAG, "接收验证码报错：$error")
                        Toast.makeText(applicationContext, "权益确认失败！", Toast.LENGTH_SHORT).show()
                    }
                }, this, true)
            )

    }


    private fun update(position:Int,select:Int,memberRights:Int){
        if (select == 1){
            data!![position].memberRight = 1
        } else {
            data!![position].memberRight = 2
        }
        val view = quanyiAdapter?.getViewByPosition(position,R.id.tv_confirm) as TextView
        view.text = "已确认"
        view.setBackgroundColor(Color.GRAY)
        view.isClickable = false
        quanyiAdapter?.notifyItemChanged(position)
    }

}
