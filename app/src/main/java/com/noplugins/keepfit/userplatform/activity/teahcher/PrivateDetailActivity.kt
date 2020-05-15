package com.noplugins.keepfit.userplatform.activity.teahcher

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
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
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.changguan.BillActivity
import com.noplugins.keepfit.userplatform.activity.dialog.SelectChangguanActivity
import com.noplugins.keepfit.userplatform.adapter.ChangguanDetailTimeAdapter
import com.noplugins.keepfit.userplatform.adapter.PrivateDetailClassAdapter
import com.noplugins.keepfit.userplatform.adapter.PrivateDetailTimeAdapter
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.*
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.BaseUtils
import com.noplugins.keepfit.userplatform.util.ShareUtils
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.data.DateHelper
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.screen.ScreenUtilsHelper
import com.noplugins.keepfit.userplatform.util.ui.erweima.encode.CodeCreator
import com.noplugins.keepfit.userplatform.util.ui.progressbar.RangeSeekBar
import com.orhanobut.logger.Logger
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_private_detail.*
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.w3c.dom.Text
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min

class PrivateDetailActivity : BaseActivity() {
    private var type = -1
    var selectItem = -1
    var logoUrl = ""
    var data: MutableList<PrivateDetailBean.CourseListBean> = ArrayList()
    var adapter: PrivateDetailClassAdapter? = null
    var teacher = ""

    private var orderList: MutableList<OrderBean> = ArrayList()
    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {
        setContentView(R.layout.activity_private_detail)
        initAdapter()
        requestPrivateData()


        if (intent.getIntExtra("from", -1) == 1) {
            tv_next_select.text = "立即购买"
        }
    }

    private fun setting(code: PrivateDetailBean) {
        teacher = code.teacherDetail.teacherNum
        tips.text = code.teacherDetail.tips
        tv_sum_time.text = "${code.teacherDetail.serviceDur}"
        if (code.teacherDetail.labelList!= null) {
            val layoutParams =
                ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            layoutParams.setMargins(30, 0, 30, 30)
            for (i in 0 until code.teacherDetail.labelList.size) {
                val paramItemView = layoutInflater.inflate(R.layout.adapter_search_histroy, zf_label, false)
                val keyWordTv = paramItemView.findViewById<TextView>(R.id.tv_content)
                keyWordTv.setPadding(35, 5, 35, 5)
                keyWordTv.text = "# " + code.teacherDetail.labelList[i]
                zf_label.addView(paramItemView, layoutParams)
            }
        }



        if (code.teacherDetail.skillList!= null) {
            val layoutParams1 =
                ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            layoutParams1.setMargins(30, 0, 30, 30)
            for (i in 0 until code.teacherDetail.skillList.size) {
                val paramItemView = layoutInflater.inflate(R.layout.adapter_search_histroy, zf_skill, false)
                val keyWordTv = paramItemView.findViewById<TextView>(R.id.tv_content)
                keyWordTv.setPadding(35, 5, 35, 5)
                keyWordTv.text = code.teacherDetail.skillList[i]
                zf_skill.addView(paramItemView, layoutParams1)
            }
        }


        tv_teacher_name.text = code.teacherDetail.teacherName
//        tv_teacher_sign.text = code.teacherDetail.card
        if (code.teacherDetail.serviceDur != 0){
            tv_sum_time.text = "累计服务时长：${code.teacherDetail.serviceDur}小时"
        } else {
            tv_sum_time.text = "累计服务时长：0小时"
        }

        tv_teacher_pinfen.text = "${code.teacherDetail.finalGrade}分"
        if (code.teacherDetail.logoUrl != null){
            logoUrl = code.teacherDetail.logoUrl
        }
        Glide.with(this).load(logoUrl)
            .error(R.drawable.logo_gray)
            .into(banner)
        type = code.teacherDetail.collect
        if (type == 1) {
            iv_collect.setImageResource(R.drawable.collect)
        }
        if (code.teacherDetail.sex == 1){
            iv_sex.setImageResource(R.drawable.man_icon)
        } else{
            iv_sex.setImageResource(R.drawable.women_icon)
        }

        if (code.courseList.size > 0){
            data.addAll(code.courseList)
            adapter!!.notifyDataSetChanged()
        }

    }

    private var selectSum = 0
    private fun initAdapter() {
        rv_private.layoutManager = LinearLayoutManager(this)
        adapter = PrivateDetailClassAdapter(data)
        rv_private.adapter = adapter

        adapter!!.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.cb_class_select -> {
                    val time = adapter.getViewByPosition(rv_private,position,R.id.tv_select_time) as TextView
                    boxSelect(view, position,time)
                }
                R.id.iv_class_time -> {
                    val a = adapter.getViewByPosition(rv_private, position, R.id.cb_class_select) as CheckBox
                    val time = adapter.getViewByPosition(rv_private,position,R.id.tv_select_time) as TextView

                    selectItem = position
                    selectTime(a,time)
                    Log.d("private", "2222")
                }
                R.id.tv_class_look -> {
                    Log.d("private", "3333")
                    if (BaseUtils.isFastClick()) {
                        val intent = Intent(this, PrivateClassActivity::class.java)
                        intent.putExtra("courseNum", data[position].courseNum)
                        startActivity(intent)
                    }

                }
            }
        }
    }

    private fun boxSelect(view: View, position: Int,time:TextView) {
        Log.d("private", "1111")
        if ((view as CheckBox).isChecked) {
            selectSum++
        } else {
            //当取消点击checkbox 时  对应的selectList中的数据应该删除
            time.visibility = View.GONE
            selectSum--
            var i = -1
            if (selectList.size > 0) {
                selectList.forEach {
                    if (it.selectItem == position){
                        i = 1
                        return@forEach
                    }
                }
            }
            if (i ==1) selectList.removeAt(position)

        }
        tv_private_all.text = "已选（${selectSum}）"
    }

    override fun doBusiness(mContext: Context?) {
        share_btn.setOnClickListener {
           if (BaseUtils.isFastClick()){
               takeScreenShot(this)
           }
        }

        back_btn.clickWithTrigger {
            finish()
        }
        iv_collect.clickWithTrigger {
            requestCollect()
        }
        tv_next_select.clickWithTrigger {


            if (selectList.size < 1 || selectList.size != selectSum) {
                Toast.makeText(applicationContext, "请先选择课程与上课时间！", Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }

//            if (!dateListPanduan(selectList)){
//                return@clickWithTrigger
//            }
            //搜索埋点
            val cEvent3 = CountEvent("私教选择场馆")
            cEvent3.addKeyValue(
                "user",
                SpUtils.getString(applicationContext, AppConstants.USER_NAME)
            )
            ///////////////////////////////
            if (intent.getIntExtra("from", -1) == 1) {
                val order = intent.getSerializableExtra("order") as OrderBean
                orderList.clear()
                //从场馆直接选择私教 则直接跳转到购买
                val intent = Intent(this, BillActivity::class.java)
                for (i in 0 until selectList.size) {
                    val orderBean = OrderBean()
                    orderBean.custUserNum = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
                    orderBean.courseType = 2
                    orderBean.startTime = selectList[i].selectDay + " " + selectList[i].startTime
                    orderBean.endTime = selectList[i].selectDay + " " + selectList[i].endTime
//                    orderBean.teacherTime = selectList[i].selectType
                    orderBean.areaNum = order.areaNum
                    orderBean.areaName = order.areaName
                    orderBean.address = order.address
                    orderBean.courseNum = data[selectList[i].selectItem].courseNum
                    orderBean.coachUserNum = data[selectList[i].selectItem].genTeacherNum
                    orderBean.courseName = data[selectList[i].selectItem].courseName
                    orderBean.coachUserName = tv_teacher_name.text.toString()
                    orderBean.price = data[selectList[i].selectItem].finalPrice
                    orderBean.logo = logoUrl
                    orderList.add(orderBean)
                }
                orderList.add(order)
                val gson = Gson()
                Log.d("OrderBean","json:${gson.toJson(orderList)}")
                intent.putExtra("list", orderList as Serializable)
                intent.putExtra("type", 2)
                startActivity(intent)
            } else {
                orderList.clear()
                //跳转到选择场馆
                //需要携带订单对象List
                //每一堂选择的课程是一个订单对象
                val intent = Intent(this, SelectChangguanActivity::class.java)
                for (i in 0 until selectList.size) {
                    val orderBean = OrderBean()
                    orderBean.custUserNum = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
                    orderBean.courseType = 2
                    orderBean.startTime = selectList[i].selectDay + " " + selectList[i].startTime
                    orderBean.endTime = selectList[i].selectDay + " " + selectList[i].endTime
//                    orderBean.teacherTime = selectList[i].selectType

                    orderBean.courseNum = data[selectList[i].selectItem].courseNum
                    orderBean.coachUserNum = data[selectList[i].selectItem].genTeacherNum
                    orderBean.courseName = data[selectList[i].selectItem].courseName
                    orderBean.coachUserName = tv_teacher_name.text.toString()
                    orderBean.price = data[selectList[i].selectItem].finalPrice
                    orderBean.logo = logoUrl
                    orderList.add(orderBean)
                }
                intent.putExtra("list", orderList as Serializable)
                intent.putExtra("teacherNum",orderList[0].coachUserNum)
                startActivity(intent)
            }


        }
    }

    private fun requestPrivateData() {

        val params = HashMap<String, Any>()
        params["teacherNum"] = intent.getStringExtra("teacherNum")
        params["userNum"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)

        subscription = Network.getInstance("获取详情", applicationContext)
            .findteacherDetail(
                params,
                ProgressSubscriber(
                    "获取详情", object : SubscriberOnNextListener<Bean<PrivateDetailBean>> {
                        override fun onNext(t: Bean<PrivateDetailBean>?) {
                            setting(t!!.data)
                        }

                        override fun onError(error: String?) {
                            Toast.makeText(applicationContext,error,Toast.LENGTH_SHORT).show()
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
        params["teacherNum"] = intent.getStringExtra("teacherNum")
        params["userNum"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        params["type"] = type
        subscription = Network.getInstance("收藏教练", applicationContext)
            .userCollectTeacher(
                params,
                ProgressSubscriber("收藏教练",
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {
                            EventBus.getDefault().post("收藏了私教")
                            if (type == 1) {
                                iv_collect.setImageResource(R.drawable.collect)
                                Toast.makeText(applicationContext, "收藏成功！", Toast.LENGTH_SHORT).show()
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
                            Logger.e("", "收藏教练失败：$error")
                            Toast.makeText(applicationContext, "收藏教练失败！", Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
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
            ShareUtils.share(this@PrivateDetailActivity, bitmap, SHARE_MEDIA.WEIXIN)
        }
        pengyouquan.setOnClickListener {
            val bitmap = ShareUtils.createViewBitmap(llBitmap)
            ShareUtils.share(this@PrivateDetailActivity, bitmap, SHARE_MEDIA.WEIXIN_CIRCLE)
        }
        qq.setOnClickListener {
//            val bitmap = ShareUtils.createViewBitmap(llBitmap)
//            ShareUtils.share(this@PrivateDetailActivity, bitmap, SHARE_MEDIA.QQ)
            requestPermissions(llBitmap)
        }
        weibo.setOnClickListener {
            val bitmap = ShareUtils.createViewBitmap(llBitmap)
            ShareUtils.share(this@PrivateDetailActivity, bitmap, SHARE_MEDIA.SINA)
        }
    }

    private val PERMISSION_REQUEST_STOREAGE = 1001
    private fun requestPermissions(layout: LinearLayout) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permission =
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    //Log
                    Log.d("判断一下","进来了，没有权限")
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_STOREAGE
                    )
                } else {
                    Log.d("判断一下","进来了，已经有了")
                    val bitmap = ShareUtils.createViewBitmap(layout)
                    ShareUtils.share(this, bitmap, SHARE_MEDIA.QQ)
                }
            } else {
                Log.d("判断一下","没有进来")
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
     * 选择健身时间
     * tips：单日选择的时间段不能重复
     * 用单独一个list记录选择的时间天数及时间段
     */

    var timeAdapter: ChangguanDetailTimeAdapter? = null
    var timeData: ArrayList<TimeWeekBean> = ArrayList()
    var timePartAdapter: PrivateDetailTimeAdapter? = null
    var timePartData: ArrayList<TeacherTimeBean.MyBean> = ArrayList()

    var selectList: MutableList<ClassTImeSelectBean> = ArrayList()

    private fun selectTime(box: CheckBox,time:TextView) {

        var selectDay = ""
//        var selectType = -1
        var startTime = ""
        var endTime = ""

        val factory = LayoutInflater.from(this)
        val view = factory.inflate(R.layout.dialog_to_select_time, null)
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

        val time1 = view.findViewById<RecyclerView>(R.id.rv_date_select)
//        val rv_team_list = view.findViewById<RecyclerView>(R.id.rv_team_list)
//
        val horizontal_scale = view.findViewById<RangeSeekBar>(R.id.horizontal_scale)
        val layoutManager = LinearLayoutManager(this)
        //调整RecyclerView的排列方向
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        time1.layoutManager = layoutManager

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
        timeAdapter = ChangguanDetailTimeAdapter(applicationContext, timeData)
        selectDay = timeData[0].nowDate
        time1.adapter = timeAdapter
//        requestTeacherTime("${Calendar.YEAR}-${Calendar.MONTH + 1}-${Calendar.DAY_OF_MONTH}")
        timeAdapter!!.setmOnItemClickListener { view, obj, position ->
            Log.d("FFFFFFF", "FFFFF:$obj")
            selectDay = obj as String
//            requestTeacherTime(selectDay)
        }

        m_dialog!!.show()


        val dismiss_tv = view.findViewById<TextView>(R.id.dismiss_tv)
        dismiss_tv.clickWithTrigger { m_dialog!!.dismiss() }
        val ok_tv = view.findViewById<TextView>(R.id.ok_tv)
        ok_tv.clickWithTrigger {
            startTime = horizontal_scale.time.split("-")[0]
            endTime = horizontal_scale.time.split("-")[1]
            val calendar = Calendar.getInstance()

            val nowdate = Date()
            val myString = "$selectDay $startTime"
            Log.d("tag_date",myString)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val d = sdf.parse(myString)
            val flag = d.before(nowdate)
            if (flag){
                Toast.makeText(applicationContext,"选择时间不能小于当前时间",Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }

            if (!checkOverlap(selectDay,startTime,endTime,selectList)){
                Toast.makeText(applicationContext,"选择的时间不能重叠",Toast.LENGTH_SHORT).show()
                return@clickWithTrigger
            }

            panduan(teacher,selectDay,selectDay,startTime,endTime,box,time)

        }
    }


    private fun requestTeacherTime(date: String) {
        timePartData.clear()
        val params = HashMap<String, Any>()
        params["teacherNum"] = intent.getStringExtra("teacherNum")
        params["date"] = date

        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("获取教练时间段", applicationContext)
            .findTeacherTime(
                requestBody,
                ProgressSubscriberNew(
                    TeacherTimeBean::class.java,
                    object : GsonSubscriberOnNextListener<TeacherTimeBean> {
                        override fun on_post_entity(code: TeacherTimeBean, get_message_id: String) {
                            timePartData.addAll(code.data)
                            timePartAdapter!!.notifyDataSetChanged()
                        }
                    },
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            type = if (type == 1) {
                                2
                            } else {
                                1
                            }
                            Logger.e("", "获取教练时间段失败：$error")
                            Toast.makeText(applicationContext, "获取教练时间段失败！", Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    /**
     * 判断当前选择的时间段 是否在教练的上课时间范围内
     */
    private fun panduan(teacher:String,begDate:String,endDate:String,begTime:String,endTime:String,
                        box:CheckBox,time:TextView){

        val params = HashMap<String, Any>()
        params["teacherNum"] =teacher
        params["begDate"] =begDate
        params["endDate"] =endDate
        params["begTime"] =begTime+":00"
        params["endTime"] =endTime+":00"
        subscription = Network.getInstance("获取教练时间段", applicationContext)
            .findTeacherBusyTime(params,ProgressSubscriber("获取教练时间段",
                object :SubscriberOnNextListener<Bean<Any>>{
                    override fun onNext(t: Bean<Any>?) {
                        time.text = "$begDate  $begTime -$endTime "
                        time.visibility = View.VISIBLE
                        if (t!!.code == 0){
                            if (selectList.size > 0) {
                                for (i in 0 until selectList.size) {
                                    if (selectList[i].selectItem == selectItem) {
                                        selectList[i].selectDay = begDate
//                        selectList[i].selectType = selectType
                                        selectList[i].startTime = begTime
                                        selectList[i].endTime = endTime
                                        m_dialog!!.dismiss()
                                        return
                                    }
                                }

                            }

                            val cls = ClassTImeSelectBean()
                            cls.selectDay = begDate
//            cls.selectType = selectType
                            cls.selectItem = selectItem
                            cls.startTime = begTime
                            cls.endTime = endTime
                            selectList.add(cls)
                            if (!box.isChecked) {
                                box.setChecked(true)
                                boxSelect(box, selectItem,time)
                            }
                            m_dialog!!.dismiss()
                        }
                    }

                    override fun onError(error: String?) {
                        Toast.makeText(applicationContext,error,Toast.LENGTH_SHORT).show()
                    }

                },
                this,true))

    }



    private fun checkOverlap(selectDay:String,startTime:String,endTime: String,
                             periods:List<ClassTImeSelectBean>):Boolean {
        var flag = true
        val startHour:Int = startTime.split(":")[0].toInt()
        val startMinute:Int= startTime.split(":")[1].toInt()
        val endHour:Int= endTime.split(":")[0].toInt()
        val endMinute:Int= endTime.split(":")[1].toInt()

        run outside@ {
            periods.forEach continuing@ {
                if (selectItem == it.selectItem){
                    return@continuing
                }
                if (selectDay != it.selectDay){
                    return@continuing
                }
                val hour1 = it.startTime.split(":")[0].toInt()
                val minute1 = it.startTime.split(":")[1].toInt()
                val hour2 = it.endTime.split(":")[0].toInt()
                val minute2 = it.endTime.split(":")[1].toInt()
                if (startHour * 60 + startMinute < endHour * 60 + endMinute) {
                    if ((hour1 * 60 + minute1 >= endHour * 60 + endMinute) ||
                        startHour * 60 + startMinute >= hour2 * 60 + minute2) {
                        flag = true
                    } else {
                        flag = false
                        return@outside
                    }
                } else {
                    if ((hour1 * 60 + minute1 <= endHour * 60 + endMinute) &&
                        startHour * 60 + startMinute >= hour2 * 60 + minute2) {
                        flag = true
                    } else {
                        flag = false
                        return@outside
                    }
                }
            }
        }

        return flag
    }
}
