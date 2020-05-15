package com.noplugins.keepfit.userplatform.activity.changguan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jiguang.analytics.android.api.CountEvent
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.ms.banner.BannerConfig
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.teahcher.PrivateDetailActivity
import com.noplugins.keepfit.userplatform.activity.team.TeamDetailActivity
import com.noplugins.keepfit.userplatform.adapter.*
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.*
import com.noplugins.keepfit.userplatform.customize.CustomViewHolder
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.BaseUtils
import com.noplugins.keepfit.userplatform.util.ShareUtils
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.TypeUtil
import com.noplugins.keepfit.userplatform.util.data.DateHelper
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.orhanobut.logger.Logger
import com.umeng.socialize.bean.SHARE_MEDIA
import com.ycuwq.datepicker.date.DatePickerDialogFragment
import com.ycuwq.datepicker.time.HourAndMinDialogFragment
import kotlinx.android.synthetic.main.activity_chaungguan_detail.*
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


class ChaungguanDetailActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {

    private var type = -1

    private var days = ""

    private var longitude = ""
    private var latitude = ""
    private var phone = ""
    private var areaNum = ""
    private var areaName = ""
    private var address = ""
    private var nowPrice = 0.0
    private var cgLogo = ""

    private var orderList: MutableList<OrderBean> = ArrayList()

    private var cgStart = ""
    private var cgEnd = ""

    private var cgHightStart = "17:00"
    private var cgHightEnd = "22:00"
    private var normalPrice:Double = 0.0

    private var lowPrice = 0.0
    private var lowTime = ""

    companion object {
        private const val PERMISSION_STORAGE_CODE = 10001
        private const val PERMISSION_STORAGE_MSG = "需要电话权限才能联系客服哦"
    }

    val PERMISSION_STORAGE = arrayOf(Manifest.permission.CALL_PHONE)

    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {
        setContentView(R.layout.activity_chaungguan_detail)

        initAdapter()
        initPrivateAdapter()
        initTimeAdapter()
        initTeamAdapter()

        requestDetails()
    }

    override fun doBusiness(mContext: Context?) {
        val calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = if (calendar.get(Calendar.MONTH) > 8) {
            "${calendar.get(Calendar.MONTH) + 1}"
        } else {
            "0${calendar.get(Calendar.MONTH) + 1}"
        }
        var day = if (calendar.get(Calendar.DAY_OF_MONTH) > 9) {
            "${calendar.get(Calendar.DAY_OF_MONTH)}"
        } else {
            "0${calendar.get(Calendar.DAY_OF_MONTH)}"
        }
        calendar.add(Calendar.HOUR, 1)
        var startH =
            if (calendar.get(Calendar.HOUR_OF_DAY) > 9) {
                "${calendar.get(Calendar.HOUR_OF_DAY)}"
            } else {
                "0${calendar.get(Calendar.HOUR_OF_DAY)}"
            }
        var startM = if (calendar.get(Calendar.MINUTE) > 9) {
            "${calendar.get(Calendar.MINUTE)}"
        } else {
            "0${calendar.get(Calendar.MINUTE)}"
        }
        calendar.add(Calendar.HOUR, 1)
        var endH = if (calendar.get(Calendar.HOUR_OF_DAY) > 9) {
            "${calendar.get(Calendar.HOUR_OF_DAY)}"
        } else {
            "0${calendar.get(Calendar.HOUR_OF_DAY)}"
        }
        var endM = if (calendar.get(Calendar.MINUTE) > 9) {
            "${calendar.get(Calendar.MINUTE)}"
        } else {
            "0${calendar.get(Calendar.MINUTE)}"
        }

//        requestTimePrice("$startH:$startM", "$endH:$endM")

        tv_hour.text = "$startH:$startM - $endH:$endM"



        tv_day.text = "$year-$month-$day"

        share_btn.clickWithTrigger(2000) {
            takeScreenShot(this)
        }

        back_btn.clickWithTrigger {
            finish()
        }

        tv_hour.clickWithTrigger {
            val hour = HourAndMinDialogFragment()

            if (startH != "") {
                hour.setSelectedDate(startH.toInt(), startM.toInt(), endH.toInt(), endM.toInt())
            } else {

            }

            hour.setOnDateChooseListener { startHour, startMinute, endHour, endMinute ->

                if ("$year-$month-$day" == tv_day.text){
                    if (startHour<Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
                        Toast.makeText(applicationContext,"开始时间不能早于当前时间",
                            Toast.LENGTH_SHORT).show()
                        return@setOnDateChooseListener
                    }
                    if (startHour==Calendar.getInstance().get(Calendar.HOUR_OF_DAY) &&
                        startMinute < Calendar.getInstance().get(Calendar.MINUTE) ){
                        Toast.makeText(applicationContext,"开始时间不能早于当前时间",
                            Toast.LENGTH_SHORT).show()
                        return@setOnDateChooseListener
                    }
                }


                if (endHour < startHour) {
                    Toast.makeText(applicationContext,"开始时间不能大于结束时间",
                        Toast.LENGTH_SHORT).show()
                    return@setOnDateChooseListener
                }
                if (endHour == startHour && endMinute <= startMinute) {
                    Toast.makeText(applicationContext,"开始时间不能大于结束时间",
                        Toast.LENGTH_SHORT).show()
                    return@setOnDateChooseListener
                }


                if (startHour< start.split(":")[0].toInt()){
                    Toast.makeText(applicationContext,"请根据场馆的营业时间选择健身时间",
                        Toast.LENGTH_SHORT).show()
                    return@setOnDateChooseListener
                }
                if (startHour== start.split(":")[0].toInt() &&
                    startMinute<start.split(":")[1].toInt()){
                    Toast.makeText(applicationContext,"请根据场馆的营业时间选择健身时间",
                        Toast.LENGTH_SHORT).show()
                    return@setOnDateChooseListener
                }

                if (endHour> end.split(":")[0].toInt()){
                    Toast.makeText(applicationContext,"请根据场馆的营业时间选择健身时间",
                        Toast.LENGTH_SHORT).show()
                    return@setOnDateChooseListener
                }
                if (endHour== end.split(":")[0].toInt() &&
                    endMinute>end.split(":")[1].toInt()){
                    Toast.makeText(applicationContext,"请根据场馆的营业时间选择健身时间",
                        Toast.LENGTH_SHORT).show()
                    return@setOnDateChooseListener
                }



                startH = if (startHour > 9) {
                    "$startHour"
                } else {
                    "0$startHour"
                }
                startM = if (startMinute > 9) {
                    "$startMinute"
                } else {
                    "0$startMinute"
                }
                endH = if (endHour > 9) {
                    "$endHour"
                } else {
                    "0$endHour"
                }
                endM = if (endMinute > 9) {
                    "$endMinute"
                } else {
                    "0$endMinute"
                }
                tv_hour.setText("$startH:$startM - $endH:$endM")

                requestTimePrice("$startH:$startM", "$endH:$endM")
            }

            hour.show(supportFragmentManager, "HourAndMinDialogFragment")
        }

        tv_day.clickWithTrigger {
            val _day1 = DatePickerDialogFragment()
            _day1.setOnDateChooseListener { _year, _month, _day ->
                //当前
//                year = _year
                if (_year<year){
                    Toast.makeText(applicationContext,"不能选择以前的时间",Toast.LENGTH_SHORT)
                        .show()
                    return@setOnDateChooseListener
                }
                if (_year == year && _month<month.toInt()){
                    Toast.makeText(applicationContext,"不能选择以前的时间",Toast.LENGTH_SHORT)
                        .show()
                    return@setOnDateChooseListener
                }
                if (_year == year && _month==month.toInt() && _day < day.toInt()){
                    Toast.makeText(applicationContext,"不能选择以前的时间",Toast.LENGTH_SHORT)
                        .show()
                    return@setOnDateChooseListener
                }
//                year = _year
                val monthNow = if (_month > 9) {
                    "$_month"
                } else {
                    "0$_month"
                }

                val dayNow = if (_day > 9) {
                    "$_day"
                } else {
                    "0$_day"
                }

                tv_day.setText("$_year-$monthNow-$dayNow")
            }
            _day1.show(supportFragmentManager, "DatePickerDialogFragment")
        }

        lin_iv_collection.clickWithTrigger {
            requestCollect()
        }

        tv_location.clickWithTrigger {
            toMap()
        }

        tv_toPhone.clickWithTrigger {
            toPhone()
        }

        tv_private_all.clickWithTrigger {
            val intent = Intent(this, ChangguanPrivateListActivity::class.java)
            val orderBean = OrderBean()
            orderBean.address = tv_location.text.toString()
            orderBean.areaNum = areaNum
            orderBean.oldPrice = nowPrice
            orderBean.custUserNum = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
            orderBean.areaName = tv_chuangguan_name.text.toString()
            orderBean.courseType = 3
            orderBean.startTime = "${tv_day.text.toString()} ${tv_hour.text.split(" - ")[0]}"
            orderBean.endTime = "${tv_day.text.toString()} ${tv_hour.text.split(" - ")[1]}"

            intent.putExtra("areaNum", areaNum)
            intent.putExtra("order", orderBean as Serializable)
            startActivity(intent)
        }

        tv_pay.clickWithTrigger {
            //搜索埋点
            val cEvent3 = CountEvent("场馆立即购买")
            cEvent3.addKeyValue(
                "user",
                SpUtils.getString(applicationContext, AppConstants.USER_NAME)
            )


            if (startH.toInt()< start.split(":")[0].toInt()){
                Toast.makeText(applicationContext,"超出营业时间",
                    Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (startH.toInt()== start.split(":")[0].toInt() &&
                startM.toInt()<start.split(":")[1].toInt()){
                Toast.makeText(applicationContext,"超出营业时间",
                    Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }

            if (endH.toInt()> end.split(":")[0].toInt()){
                Toast.makeText(applicationContext,"超出营业时间",
                    Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (endH.toInt()== end.split(":")[0].toInt() &&
                endM.toInt()>end.split(":")[1].toInt()){
                Toast.makeText(applicationContext,"超出营业时间",
                    Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (endH.toInt()< start.split(":")[0].toInt()){
                Toast.makeText(applicationContext,"超出营业时间",
                    Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (endH.toInt()== start.split(":")[0].toInt() &&
                endM.toInt()<start.split(":")[1].toInt()){
                Toast.makeText(applicationContext,"超出营业时间",
                    Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }

            if (startH.toInt()> end.split(":")[0].toInt()){
                Toast.makeText(applicationContext,"超出营业时间",
                    Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            if (startH.toInt()== end.split(":")[0].toInt() &&
                startM.toInt()<end.split(":")[1].toInt()){
                Toast.makeText(applicationContext,"超出营业时间",
                    Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            ///////////////////////////////
            orderList.clear()
            val intent = Intent(this, BillActivity::class.java)
            //
            val orderBean = OrderBean()
            orderBean.address = tv_location.text.toString()
            orderBean.areaNum = areaNum
            orderBean.price = nowPrice
            orderBean.custUserNum = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
            orderBean.areaName = tv_chuangguan_name.text.toString()
            orderBean.courseType = 3
            orderBean.startTime = "${tv_day.text.toString()} ${tv_hour.text.split(" - ")[0]}"
            orderBean.endTime = "${tv_day.text.toString()} ${tv_hour.text.split(" - ")[1]}"
            orderList.add(orderBean)
            intent.putExtra("list", orderList as Serializable)
            intent.putExtra("type", 1)
            intent.putExtra("cgLogo", cgLogo)
            startActivity(intent)
        }
//
//        iv_yiwen.clickWithTrigger {
//            toTips()
//        }

        fl_cg_price_tips.setOnClickListener {
            if (BaseUtils.isFastClick()){
                toTips()
            }

        }
    }

    var data: ArrayList<String> = ArrayList()
    var adapter: ChangguanDetailFeaturesAdapter? = null
    var privateData: ArrayList<ChangguanDetailBean.TeacherListBean> = ArrayList()
    var privateAdapter: ChangguanDetailPrivateAdapter? = null
    var timeData: ArrayList<TimeWeekBean> = ArrayList()
    var timeAdapter: ChangguanDetailTimeAdapter? = null
    var teamData: ArrayList<ChangguanDetailBean.CourseListBean> = ArrayList()
    var teamAdapter: ChangguanDetailTeamAdapter? = null


    private fun initAdapter() {
        rv_features.layoutManager = GridLayoutManager(this, 4)
        adapter = ChangguanDetailFeaturesAdapter(data)
        rv_features.adapter = adapter
    }

    private fun initPrivateAdapter() {
        val layoutManager = LinearLayoutManager(this)
        //调整RecyclerView的排列方向
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rv_private.layoutManager = layoutManager

        privateAdapter = ChangguanDetailPrivateAdapter(privateData)
        rv_private.adapter = privateAdapter
        privateAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            if (BaseUtils.isFastClick()) {
                val intent = Intent(
                    this,
                    PrivateDetailActivity::class.java
                )
                val orderBean = OrderBean()
                orderBean.address = tv_location.text.toString()
                orderBean.areaNum = areaNum
                orderBean.oldPrice = nowPrice
                orderBean.custUserNum = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
                orderBean.areaName = tv_chuangguan_name.text.toString()
                orderBean.courseType = 3
//                orderBean.startTime = "${tv_day.text.toString()} ${tv_hour.text.split(" - ")[0]}"
//                orderBean.endTime = "${tv_day.text.toString()} ${tv_hour.text.split(" - ")[1]}"
                orderBean.logo = cgLogo
                intent.putExtra("teacherNum", privateData[position].teacherNum)
                intent.putExtra("from", 1)
                intent.putExtra("order", orderBean as Serializable)
                intent.putExtra("cgLogo", cgLogo)
                startActivity(intent)
            }

        }


    }

    private fun initTimeAdapter() {
        val layoutManager = LinearLayoutManager(this)
        //调整RecyclerView的排列方向
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rv_date_select.layoutManager = layoutManager

        for (i in 0..13) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, +i)
            val time = TimeWeekBean()
            time.time = "${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.DATE)}"
            time.week = DateHelper.getWeek(calendar.get(Calendar.DAY_OF_WEEK))
            val month = if (calendar.get(Calendar.MONTH) + 1 > 9) {
                "${calendar.get(Calendar.MONTH) + 1}"
            } else {
                "0${calendar.get(Calendar.MONTH) + 1}"
            }

            val day = if (calendar.get(Calendar.DATE) > 9) {
                calendar.get(Calendar.DATE)
            } else {
                "0${calendar.get(Calendar.DATE)}"
            }

            time.nowDate = "${calendar.get(Calendar.YEAR)}-$month-$day"

            timeData.add(time)
        }
        timeAdapter = ChangguanDetailTimeAdapter(this, timeData)
        rv_date_select.adapter = timeAdapter

        timeAdapter!!.setmOnItemClickListener { view, obj, position ->
            Log.d("FFFFFFF", "FFFFF:$obj")
            days = obj as String

            requestTeam()
        }

    }

    /**
     * 场馆选择团课
     */
    private fun initTeamAdapter() {
        val layoutManager = LinearLayoutManager(this)
        //调整RecyclerView的排列方向
        rv_team_list.layoutManager = layoutManager

        teamAdapter = ChangguanDetailTeamAdapter(teamData)
        rv_team_list.adapter = teamAdapter
        val view = LayoutInflater.from(this).inflate(R.layout.enpty_view, rv_team_list, false)

        teamAdapter!!.emptyView = view
        teamAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_team_pay -> {
                    if (BaseUtils.isFastClick()) {
                        //搜索埋点
                        val cEvent3 = CountEvent("场馆购买团课")
                        cEvent3.addKeyValue(
                            "user",
                            SpUtils.getString(applicationContext, AppConstants.USER_NAME)
                        )
                        ///////////////////////////////
                        orderList.clear()
                        val intent = Intent(this, BillActivity::class.java)
                        //
                        val orderBean = OrderBean()
                        orderBean.areaNum = teamData[position].gymAreaNum
                        orderBean.areaName = tv_chuangguan_name.text.toString()
                        orderBean.address = tv_location.text.toString()
                        orderBean.custUserNum = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
                        orderBean.courseType = 1
                        orderBean.startTime = DateHelper.getDayDateByLong(teamData[position].startTime)
                        orderBean.endTime = DateHelper.getDayDateByLong(teamData[position].endTime)
                        orderBean.courseNum = teamData[position].courseNum
                        orderBean.coachUserNum = teamData[position].genTeacherNum
                        orderBean.courseName = teamData[position].courseName
                        orderBean.coachUserName = teamData[position].teacherName
                        orderBean.price = teamData[position].finalPrice
                        orderBean.logo = teamData[position].imgUrl as String?
                        orderBean.difficulty = difficultyToStr(teamData[position].difficulty)
                        orderList.add(orderBean)
                        intent.putExtra("list", orderList as Serializable)
                        intent.putExtra("type", 3)
                        intent.putExtra("cgLogo", cgLogo)
                        startActivity(intent)
                    }

                }
                R.id.rl_team -> {
                    orderList.clear()
                    val orderBean = OrderBean()
                    orderBean.areaNum = teamData[position].gymAreaNum
                    orderBean.areaName = tv_chuangguan_name.text.toString()
                    orderBean.address = tv_location.text.toString()
                    orderBean.custUserNum = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
                    orderBean.courseType = 1
                    orderBean.startTime = days + " " + DateHelper.getDateByLong(teamData[position].startTime)
                    orderBean.endTime = days + " " + DateHelper.getDateByLong(teamData[position].endTime)
                    orderBean.courseNum = teamData[position].courseNum
                    orderBean.coachUserNum = teamData[position].genTeacherNum
                    orderBean.courseName = teamData[position].courseName
                    orderBean.coachUserName = teamData[position].teacherName
                    orderBean.price = teamData[position].finalPrice
                    orderBean.logo = teamData[position].imgUrl
                    orderBean.difficulty = difficultyToStr(teamData[position].difficulty)
                    orderList.add(orderBean)
                    val intent = Intent(
                        this,
                        TeamDetailActivity::class.java
                    )
                    intent.putExtra("list", orderList as Serializable)
                    intent.putExtra("courseNum", teamData[position].courseNum)
                    startActivity(intent)

                }

            }
        }

    }

    private fun difficultyToStr(id: Int): String {
        var str = ""
        when (id) {
            1 -> {
                str = "容易"
            }
            2 -> {
                str = "中等"
            }
            3 -> {
                str = "难"
            }
        }
        return str
    }

    private fun requestDetails() {
        val params = HashMap<String, Any>()
        params["areaNum"] = intent.getStringExtra("areaNum")
        params["userNum"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        subscription = Network.getInstance("获取场馆详情", applicationContext)
            .findAreaDetails(
                params,
                ProgressSubscriber("",object : SubscriberOnNextListener<Bean<ChangguanDetailBean>> {
                        override fun onNext(result: Bean<ChangguanDetailBean>) {
                            setting(result.data)
                        }

                        override fun onError(error: String) {
                            Logger.e("", "获取详情失败：$error")
                            Toast.makeText(applicationContext, "获取详情失败！", Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    private fun requestCollect() {
        type = if (type == 1) {
            2
        } else {
            1
        }
        val params = HashMap<String, Any>()
        params["areaNum"] = intent.getStringExtra("areaNum")
        params["userNum"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        params["type"] = type
        subscription = Network.getInstance("收藏场馆", applicationContext)
            .userCollect(
                params,
                ProgressSubscriber(
                    "",
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {
                            EventBus.getDefault().post("场馆收藏了")
                            if (type == 1) {
                                iv_collect.setImageResource(R.drawable.collect)
                                Toast.makeText(applicationContext, "收藏场馆成功！", Toast.LENGTH_SHORT).show()
                            } else {
                                iv_collect.setImageResource(R.drawable.xin_2)
                                Toast.makeText(applicationContext, "已取消收藏！", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onError(error: String) {
                            type = if (type == 1) {
                                2
                            } else {
                                1
                            }
                            Logger.e("", "收藏场馆失败：$error")
                            Toast.makeText(applicationContext, "收藏场馆失败！", Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    false
                )
            )
    }

    private fun requestTeam() {
        val params = HashMap<String, Any>()
        params["areaNum"] = intent.getStringExtra("areaNum")
        params["days"] = days

        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("获取团课", applicationContext)
            .findCourse(
                requestBody,
                ProgressSubscriberNew(
                    ChangguanTeamBean::class.java,
                    object : GsonSubscriberOnNextListener<ChangguanTeamBean> {
                        override fun on_post_entity(code: ChangguanTeamBean, get_message_id: String) {
                            teamData.clear()
                            teamData.addAll(code.data)
                            teamAdapter!!.notifyDataSetChanged()
                        }
                    },
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            Logger.e("", "获取团课失败：$error")
                            Toast.makeText(applicationContext, "获取团课失败！", Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    private fun requestTimePrice(start: String, end: String) {
        val params = HashMap<String, Any>()
        params["areaNum"] = intent.getStringExtra("areaNum")
        params["startTime"] = start
        params["endTime"] = end

        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("获取时间段价格", applicationContext)
            .findPriceArea(
                requestBody,
                ProgressSubscriberNew(
                    TimePriceBean::class.java,
                    object : GsonSubscriberOnNextListener<TimePriceBean> {
                        override fun on_post_entity(code: TimePriceBean, get_message_id: String) {
                            //
                            nowPrice = code.finalPrice
                            tv_price.setText("¥${code.finalPrice}")
                        }
                    },
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            Logger.e("", "获取时间段价格失败：$error")
                            Toast.makeText(applicationContext, "获取时间段价格失败！", Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }


    var start = ""
    var end = ""
    private fun setting(code: ChangguanDetailBean) {
        //简单使用
        banner
            .setBannerStyle(BannerConfig.NUM_INDICATOR)
            .setPages(code.picUrl, CustomViewHolder())
            .start()

        type = code.areaDetail.collect
        if (type == 1) {
            iv_collect.setImageResource(R.drawable.collect)
        }
        areaNum = code.areaDetail.areaNum
        tv_chuangguan_name.text = code.areaDetail.areaName
        tv_type_size.text = "${TypeUtil.cgTypeToStr(code.areaDetail.type)} | ${code.areaDetail.area}平方米"
        start = BaseUtils.strSubEnd3(code.areaDetail.businessStart)
        end = BaseUtils.strSubEnd3(code.areaDetail.businessEnd)
        tv_work_time.text = "营业时间 $start-$end"
        tv_fenshu.text = "${code.areaDetail.finalGradle}分"
        tv_location.text = "${code.areaDetail.address}"

        if (code.nowPrice<0){
            nowPrice = 0.0
            tv_price.text = "未营业"
        } else{
            nowPrice = code.nowPrice
            tv_price.text = "¥$nowPrice"
        }

        if (code.areaDetail.logo != null){
            cgLogo = code.areaDetail.logo
        }



        val arrList = code.areaDetail.facility.split(",").toList()
        data.addAll(arrList)
        adapter!!.notifyDataSetChanged()

        privateData.addAll(code.teacherList)
        privateAdapter!!.notifyDataSetChanged()
        if (privateData.size == 0) {
            rl_private.visibility = View.GONE
        } else {
            rl_private.visibility = View.VISIBLE
            tv_private_all.visibility = if (privateData.size <= 3) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        teamData.addAll(code.courseList)
        teamAdapter!!.notifyDataSetChanged()

        longitude = code.areaDetail.longitude.toString()
        latitude = code.areaDetail.latitude.toString()
        phone = code.areaDetail.phone

        normalPrice = code.areaTime.gymArea.normalPrice
        if (code.areaTime.highTimeList.isNotEmpty()){
            priceData = code.areaTime.highTimeList
        }

        if (code.areaTime.lowPrice!=null){
            lowPrice = code.areaTime.lowPrice.price
            lowTime = BaseUtils.strSubEnd3(code.areaTime.lowPrice.startTime)+"-"+BaseUtils.strSubEnd3(code.areaTime.lowPrice.endTime)

            tv_low_prices_time.text = lowTime
            tv_low_prices.text = "¥$lowPrice"
        }
    }


    /**
     * 截取屏幕
     *
     * @param activity
     */
    fun takeScreenShot(activity: Activity?) {
        // View是你需要截图的View
        val view = activity!!.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bitmap = view.drawingCache
        // 获取状态栏高度
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top
        //获取actiobBar的高度
        val actionBarHeight = activity.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
            .getDimension(0, 0f)
        // 获取屏幕长和高
        val width = activity.windowManager.defaultDisplay.width
        val height = activity.windowManager.defaultDisplay
            .height
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        val b = Bitmap.createBitmap(
            bitmap, 0, statusBarHeight + actionBarHeight.toInt(), width, height
                    - statusBarHeight - actionBarHeight.toInt()
        )
        view.destroyDrawingCache()
        //将bitmap存入本地文件
        //获取系统缓存文件
        val cacheFile = File(activity.getCacheDir(), "ScreenCut")
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(cacheFile)
            b.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            if (cacheFile.exists()) {
                val bitmap_cut = BitmapFactory.decodeFile("${activity.getCacheDir()}//ScreenCut")
                //在这里弹出popwindow
                select_more_popwindow(bitmap_cut)
            } else {
                Toast.makeText(this, "加载图片失败", Toast.LENGTH_SHORT).show()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                out!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


    var m_dialog: Dialog? = null
    private fun select_more_popwindow(bitmap_cut: Bitmap) {
        val factory = LayoutInflater.from(this)
        val view = factory.inflate(R.layout.jiepin_share_detail, null)
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
        val dismiss_tv = view.findViewById<TextView>(R.id.dismiss_tv)
        dismiss_tv.setOnClickListener { m_dialog!!.dismiss() }
        val cut_image = view.findViewById<ImageView>(R.id.cut_image)
        cut_image.setImageBitmap(bitmap_cut)

        val weixin = view.findViewById<LinearLayout>(R.id.ll_wx)
        val pengyouquan = view.findViewById<LinearLayout>(R.id.ll_pyq)
        val qq = view.findViewById<LinearLayout>(R.id.ll_qq)
        val weibo = view.findViewById<LinearLayout>(R.id.ll_wb)
        val llBitmap = view.findViewById<LinearLayout>(R.id.ll_bitmap)
        val erweima = view.findViewById<ImageView>(R.id.iv_erweima)
        initCodeErweima(erweima)

        weixin.setOnClickListener {
            Log.d("tag", "aaaaaaa")
            val bitmap = ShareUtils.createViewBitmap(llBitmap)
            ShareUtils.share(this@ChaungguanDetailActivity, bitmap, SHARE_MEDIA.WEIXIN)
        }
        pengyouquan.setOnClickListener {
            val bitmap = ShareUtils.createViewBitmap(llBitmap)
            ShareUtils.share(this@ChaungguanDetailActivity, bitmap, SHARE_MEDIA.WEIXIN_CIRCLE)
        }
        qq.setOnClickListener {
//            val bitmap = ShareUtils.createViewBitmap(llBitmap)
//            ShareUtils.share(this@ChaungguanDetailActivity, bitmap, SHARE_MEDIA.QQ)
            requestPermissions(llBitmap)
        }
        weibo.setOnClickListener {
            val bitmap = ShareUtils.createViewBitmap(llBitmap)
            ShareUtils.share(this@ChaungguanDetailActivity, bitmap, SHARE_MEDIA.SINA)
        }
    }

    private val PERMISSION_REQUEST_STOREAGE = 1001
    private fun requestPermissions(layout: LinearLayout) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permission =
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_STOREAGE
                    )
                } else {
                    val bitmap = ShareUtils.createViewBitmap(layout)
                    ShareUtils.share(this, bitmap, SHARE_MEDIA.QQ)
                }
            } else {
                val bitmap = ShareUtils.createViewBitmap(layout)
                ShareUtils.share(this, bitmap, SHARE_MEDIA.QQ)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initCodeErweima(view: ImageView) {
        //Bitmap logo = BitmapFactory.decodeResource(getResources(),R.mipmap.changguan);
        Glide.with(this)
            .load(R.drawable.cust)
            .centerCrop()
            .into(view)

    }

    /**
     * 跳转到地图
     */
    private fun toMap() {
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
        val dismiss_tv = view.findViewById<TextView>(R.id.dismiss_tv)
        dismiss_tv.clickWithTrigger { m_dialog!!.dismiss() }

        val iv_baidu = view.findViewById<LinearLayout>(R.id.iv_baidu)
        val iv_gaode = view.findViewById<LinearLayout>(R.id.iv_gaode)
        val iv_qq = view.findViewById<LinearLayout>(R.id.iv_qq)
        iv_baidu.clickWithTrigger {
            if (!BaseUtils.isAvilible(this, "com.baidu.BaiduMap")) {
                Toast.makeText(this, "请先安装百度地图客户端", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }

            val intent = Intent()
            intent.data = Uri.parse("baidumap://map/direction?destination=${tv_location.text}&mode=driving")
//            intent.data = Uri.parse("baidumap://map/navi?query=${tv_location.text}&src=$packageName")
            startActivity(intent)
        }

        iv_gaode.clickWithTrigger {
            if (!BaseUtils.isAvilible(this, "com.autonavi.minimap")) {
                Toast.makeText(this, "请先安装高德地图客户端", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }

            val intent = Intent()
            val stringBuffer = StringBuffer("androidamap://route?sourceApplication=").append(packageName)

            stringBuffer
                .append("&dlat=").append(latitude)
                .append("&dlon=").append(longitude)
                .append("&dname=").append(tv_location.text)
                .append("&dev=").append(0)
                .append("&t=").append(0)
            intent.data = Uri.parse(stringBuffer.toString())
            startActivity(intent)
        }

        iv_qq.clickWithTrigger {
            if (!BaseUtils.isAvilible(this, "com.tencent.map")) {
                Toast.makeText(this, "请先安装腾讯地图客户端", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }
            val stringBuffer = StringBuffer("qqmap://map/routeplan?type=drive")
                .append("&tocoord=$latitude").append(",")
                .append(longitude).append("&to=${tv_location.text}")
            intent.data = Uri.parse(stringBuffer.toString())
            startActivity(intent)
        }

    }

    /**
     * 拨打电话
     */
    private fun toPhone() {
        val factory = LayoutInflater.from(this)
        val view = factory.inflate(R.layout.dialog_to_phone, null)
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

        val callPhone = view.findViewById<TextView>(R.id.tv_phone)
        callPhone.text = "确认拨打  $phone"
        val dismiss_tv = view.findViewById<TextView>(R.id.dismiss_tv)
        dismiss_tv.clickWithTrigger { m_dialog!!.dismiss() }

        val phone_tv = view.findViewById<TextView>(R.id.phone_tv)
        phone_tv.clickWithTrigger {
            initSimple()
            m_dialog!!.dismiss()
        }
    }

    /**
     * 价格提示
     */
    private var priceData:MutableList<ChangguanDetailBean.AreaTimeBean.HighTimeListBean> = ArrayList()
    private fun toTips() {
        val factory = LayoutInflater.from(this)
        val view = factory.inflate(R.layout.dialog_to_tips_v11, null)
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

        val cgTime = view.findViewById<TextView>(R.id.tv_cg_time)
        val cgPrice= view.findViewById<TextView>(R.id.tv_cg_price)
        val rlBg= view.findViewById<RelativeLayout>(R.id.rl_bg)
        rlBg.setOnClickListener {
            m_dialog!!.dismiss()
        }
        cgPrice.text = "¥$normalPrice"
        cgTime.text = "$start-$end"
        val ivBack = view.findViewById<ImageView>(R.id.iv_cha)
        ivBack.setOnClickListener {
            m_dialog!!.dismiss()
        }

        val llHighTime = view.findViewById<LinearLayout>(R.id.ll_high_time)
        if (priceData.size == 0){
            llHighTime.visibility = View.GONE
        } else {
            llHighTime.visibility = View.VISIBLE
            val rvHighPrice = view.findViewById<RecyclerView>(R.id.rv_high_price)

            val layoutManager = LinearLayoutManager(this)
            rvHighPrice.layoutManager = layoutManager
            rvHighPrice.setHasFixedSize(true)
            rvHighPrice.setNestedScrollingEnabled(false)
            val adapter = CgDetailTipsAdapter(priceData)
            rvHighPrice.adapter = adapter
        }


    }

    fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        return EasyPermissions.hasPermissions(context!!, *permissions)
    }

    @AfterPermissionGranted(PERMISSION_STORAGE_CODE)
    fun initSimple() {
        if (hasStoragePermission(this)) {
            //有权限
            callPhone(phone)
        } else {
            //申请权限
            EasyPermissions.requestPermissions(
                this,
                PERMISSION_STORAGE_MSG,
                PERMISSION_STORAGE_CODE,
                *PERMISSION_STORAGE
            )
        }
    }

    /**
     * 是否有电话权限
     *
     * @param context
     * @return
     */
    fun hasStoragePermission(context: Context?): Boolean {
        return hasPermissions(context, *PERMISSION_STORAGE)
    }


    @SuppressLint("MissingPermission")
    fun callPhone(phoneNum: String) {
        val _intent = Intent(Intent.ACTION_CALL)
        val data = Uri.parse("tel:$phoneNum")
        _intent.data = data
        startActivity(_intent)
    }

    /**
     * 权限设置页面回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 拒绝权限
     *
     * @param requestCode
     * @param perms
     */
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                .setTitle("提醒")
                .setRationale("需要电话权限才能联系客服哦")
                .build()
                .show()
        }
    }

}
