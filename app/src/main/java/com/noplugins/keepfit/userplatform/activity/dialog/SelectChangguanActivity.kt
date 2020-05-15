package com.noplugins.keepfit.userplatform.activity.dialog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andview.refreshview.XRefreshView
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.changguan.BillActivity
import com.noplugins.keepfit.userplatform.activity.teahcher.PrivateToCgDetailActivity
import com.noplugins.keepfit.userplatform.adapter.ChangguanFindAdapter
import com.noplugins.keepfit.userplatform.adapter.PopUpAdapter
import com.noplugins.keepfit.userplatform.bean.ChangguanBean
import com.noplugins.keepfit.userplatform.bean.OrderBean
import com.noplugins.keepfit.userplatform.callback.MessageEvent
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.BaseUtils
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.ui.pop.SpinnerPopWindow
import com.tencent.mm.opensdk.utils.Log
import kotlinx.android.synthetic.main.dialog_private_select_changguan.*
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.set
import kotlin.collections.toMutableList

class SelectChangguanActivity : Activity() {
    private var orderList: MutableList<OrderBean> = ArrayList()

    var data: ArrayList<ChangguanBean.ChangguanEntity> = ArrayList()
    var adapter: ChangguanFindAdapter? = null

    var currentLat: Double = 0.00
    var currentLon: Double = 0.00

    var isPinfen = false
    var isYongchi = false
    var changguanType = 0

    var isBackType = -1

    private var is_not_more = true

    var page: Int = 1
    var maxPage = -1

    var teacherNum = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_private_select_changguan)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.BOTTOM)

        EventBus.getDefault().register(this)
        if (null != intent.getSerializableExtra("list")){
            orderList = intent.getSerializableExtra("list") as MutableList<OrderBean>
        }

        if (null != intent.getStringExtra("teacherNum")){
            teacherNum = intent.getStringExtra("teacherNum")!!
        }

        var json = Gson().toJson(orderList)
        Log.d("json", json)

        initview()

        currentLat = SpUtils.getString(this, AppConstants.LAT).toDouble()
        currentLon = SpUtils.getString(this, AppConstants.LON).toDouble()

        requestChuangguan()

    }

    /**
     * eventBus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(data: String) {
        if (data == "关闭选择场馆"){
            this.finish()
        }
    }
    private fun showPopwindow(pop: SpinnerPopWindow<String>, view: TextView) {
        pop.width = view.width
        pop.showAsDropDown(view)

    }

    private var popWindow: SpinnerPopWindow<String>? = null
    private fun initview() {

        val listClass = resources.getStringArray(R.array.identify_types).toMutableList()
        popWindow = SpinnerPopWindow(this,
            listClass,
            PopUpAdapter.OnItemClickListener { _, _, position ->
                main_occupation.text = listClass[position]
                changguanType = if (position == listClass.size -1){
                    -1
                } else {
                    position + 1
                }
//                isDialog = true
                page = 1
                requestChuangguan()
                popWindow!!.dismiss()
            })
        main_occupation.setOnClickListener {
            showPopwindow(popWindow!!,main_occupation)

        }

        tv_pingfen.clickWithTrigger {
            isPinfen = if (!isPinfen) {
                tv_pingfen.setTextColor(resources.getColor(R.color.btn_text_color))
                true
            } else {
                tv_pingfen.setTextColor(resources.getColor(R.color.color_6D7278))
                false
            }

            requestChuangguan()
        }
        iv_to_top.clickWithTrigger {
            if (data.size > 0) {
                rv_list.smoothScrollToPosition(0)
            }
        }

        tv_yongchi.clickWithTrigger {
            isYongchi = if (!isYongchi) {
                tv_yongchi.setTextColor(resources.getColor(R.color.btn_text_color))
                true
            } else {
                tv_yongchi.setTextColor(resources.getColor(R.color.color_6D7278))
                false
            }

            requestChuangguan()
        }

        iv_back.clickWithTrigger {
            finish()
        }
        rv_list.layoutManager = LinearLayoutManager(this)
        adapter = ChangguanFindAdapter(data, 2)
        rv_list.adapter = adapter
        val view = LayoutInflater.from(this).inflate(R.layout.enpty_view, rv_list, false)
        val txet = view.findViewById<TextView>(R.id.tv_empty_tips)
        val iv = view.findViewById<ImageView>(R.id.iv_empty_view)
        iv.setImageResource(R.drawable.no_list_data)
        txet.text = "木有搜索到相关内容哇～"
        adapter!!.emptyView = view
        adapter!!.setOnItemChildClickListener { adapter, view, position ->
            if (!BaseUtils.isFastClick()){
                return@setOnItemChildClickListener
            }
            for (i in 0 until orderList.size) {
                orderList[i].areaNum = data[position].areaNum
                orderList[i].areaName = data[position].areaName
                orderList[i].address = data[position].address
            }

            val orderBean = OrderBean()
            orderBean.areaNum = data[position].areaNum
            orderBean.areaName = data[position].areaName
            orderBean.address = data[position].address
            orderBean.courseType = 3
            orderBean.logo = data[position].logo
            orderBean.price = 0.0
            orderBean.oldPrice = data[position].normalPrice
            orderBean.custUserNum = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
            orderList.add(orderBean)
            when (view.id) {
                R.id.tv_select -> {

                    val intent = Intent(this, BillActivity::class.java)

                    intent.putExtra("list", orderList as Serializable)
                    intent.putExtra("type", 2)
                    startActivityForResult(intent, 1)
                }
                R.id.ll_cg_item -> {
                    Log.d("ABCD", "奇了怪了")
                    val intent = Intent(this,
                        PrivateToCgDetailActivity::class.java)
                    intent.putExtra("areaNum",data[position].areaNum)
                    intent.putExtra("list", orderList as Serializable)
                    intent.putExtra("type", 2)
                    startActivityForResult(intent, 1)
                }
            }
//
//
        }



        refresh_layout.setEnableLoadMore(true)
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setOnRefreshListener {
            //下拉刷新
            page = 1
            //填写刷新数据的网络请求，一般page=1，List集合清空操作
//            data.clear()
            requestChuangguan()
            refresh_layout.finishRefresh(1000/*,false*/)
        }
        refresh_layout.setOnLoadMoreListener {
            //上拉加载
            page += 1
            requestChuangguan()
            refresh_layout.finishRefresh(1000/*,false*/)
            refresh_layout.finishLoadMore(2000/*,false*/)
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (isBackType == 1) {
            orderList.removeAt(orderList.size - 1)
        }
    }
    var isFirst = 0
    override fun onResume() {
        super.onResume()
        if (isFirst ==1){
            requestChuangguan()
        }
    }

    override fun onPause() {
        isFirst = 1
        super.onPause()

    }
    /**
     * 请求场馆数据
     */
    private fun requestChuangguan() {
        val params = HashMap<String, Any>()
        params["longitude"] = currentLon
        params["latitude"] = currentLat
        params["page"] = page

        params["userNum"] = SpUtils.getString(this, AppConstants.USER_NAME)
        params["genTeacherNum"] =teacherNum
        if (isPinfen) {
            params["grade"] = 1
        }
        if (isYongchi) {
            params["haveSwim"] = 1
        }
        if (changguanType > 0) {
            params["type"] = changguanType
        }


//        if (SpUtils.getString(applicationContext,AppConstants.PROVINCE,"") != ""){
//            params["province"] = SpUtils.getString(applicationContext,AppConstants.PROVINCE)
//        }
//        if (SpUtils.getString(applicationContext,AppConstants.CITY,"") != ""){
//            params["city"] = SpUtils.getString(applicationContext,AppConstants.CITY)
//        }
//        if (SpUtils.getString(applicationContext,AppConstants.DISTRICT,"") != ""){
//            params["district"] = SpUtils.getString(applicationContext,AppConstants.DISTRICT)
//        }
        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)


        val subscription = Network.getInstance("获取场馆列表", this)
            .findArea(params,
                ProgressSubscriber("",object : SubscriberOnNextListener<Bean<ChangguanBean>> {
                        override fun onNext(result: Bean<ChangguanBean>) {
                            tv_cg_num.text = "共${result.data.totalNum}家场馆"
                            maxPage = result.data.maxPage
//                            if (page == maxPage) {
//                                refresh_layout.setEnableLoadMore(false)
//                            } else {
//                                refresh_layout.setEnableLoadMore(true)
//                            }
                            if (page == 1) {
                                update(result.data)
                            } else {
                                add(result.data)
                            }
                        }

                        override fun onError(error: String) {
                            Toast.makeText(this@SelectChangguanActivity, "获取场馆列表失败！", Toast.LENGTH_SHORT).show()
                        }
                    }, this, true
                )
            )
    }

    private fun update(bean: ChangguanBean) {
        data.clear()
        data.addAll(bean.areaList)
        adapter!!.notifyDataSetChanged()

    }

    private fun add(bean: ChangguanBean) {
        data.addAll(bean.areaList)
        adapter!!.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == -1) {
                    isBackType = 1
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
