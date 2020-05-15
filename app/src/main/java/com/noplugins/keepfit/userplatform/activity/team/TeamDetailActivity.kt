package com.noplugins.keepfit.userplatform.activity.team

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
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import cn.jiguang.analytics.android.api.CountEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.gson.Gson
import com.ms.banner.BannerConfig
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.changguan.BillActivity
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.OrderBean
import com.noplugins.keepfit.userplatform.bean.TeamDetailBean
import com.noplugins.keepfit.userplatform.customize.CustomViewHolder
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.BaseUtils
import com.noplugins.keepfit.userplatform.util.GlideRoundTransform
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
import com.orhanobut.logger.Logger
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_team_detail.*
import okhttp3.RequestBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.set

class TeamDetailActivity : BaseActivity(),EasyPermissions.PermissionCallbacks {

    private var type = -1

    private var days = ""

    private var longitude = ""
    private var latitude = ""
    private var phone = ""
    private var areaNum = ""
    private var nowPrice = 0.0
    private var cgLogo = ""

    private var startTime = ""
    private var endTime = ""
    private var teacherNum = ""

    private var courseNum = ""

    private var orderList:MutableList<OrderBean> = ArrayList()
    companion object {

        private const val PERMISSION_STORAGE_CODE = 10001
        private const val PERMISSION_STORAGE_MSG = "需要电话权限才能联系客服哦"
    }

    val PERMISSION_STORAGE = arrayOf(Manifest.permission.CALL_PHONE)

    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {

        setContentView(R.layout.activity_team_detail)
        if (intent.getSerializableExtra("list")!=null){
            orderList = intent.getSerializableExtra("list") as MutableList<OrderBean>
        }
        if (null!= intent.getStringExtra("courseNum")){
            courseNum = intent.getStringExtra("courseNum")
        }
        requestDetails()
    }

    override fun doBusiness(mContext: Context?) {

        share_btn.clickWithTrigger(2000) {
            takeScreenShot(this)
        }

        back_btn.clickWithTrigger {
            finish()
        }


        tv_location.clickWithTrigger {
            toMap()
        }

        tv_toPhone.clickWithTrigger {
            toPhone()
        }
        pay_btn.clickWithTrigger {
            //搜索埋点
            val cEvent3 = CountEvent("团课购买")
            cEvent3.addKeyValue(
                "user",
                SpUtils.getString(applicationContext, AppConstants.USER_NAME)
            )
            ///////////////////////////////
            val intent = Intent(
                this,
                BillActivity::class.java
            )

            intent.putExtra("list", orderList as Serializable)
            intent.putExtra("type",3)
            startActivity(intent)
        }

        iv_private_logo.clickWithTrigger {
            val intent = Intent(
                this,
                TeamToPrivateActivity::class.java
            )
            intent.putExtra("teacherNum",teacherNum)
            startActivity(intent)

        }


    }


    private fun requestDetails() {
        val params = HashMap<String, Any>()
        params["courseNum"] = courseNum
        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("团课详情", applicationContext)
            .findCourseListDetail(
                params,ProgressSubscriber("",
                    object :SubscriberOnNextListener<Bean<TeamDetailBean>>{
                        override fun onNext(t: Bean<TeamDetailBean>) {
                            setting(t.data)
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

    private fun setting(code: TeamDetailBean) {
        //简单使用
        banner
            .setBannerStyle(BannerConfig.NUM_INDICATOR)
            .setPages(code.course.urls, CustomViewHolder())
            .start()
        teacherNum = code.teacher.teacherNum
        areaNum = code.course.gymAreaNum
        tv_team_name.text = code.course.courseName
        tv_cg_name.text = code.course.areaName
        startTime =  DateHelper.getDateByLong(code.course.startTime)
        endTime =  DateHelper.getDateByLong(code.course.endTime)
        tv_work_time.text = "课程时间 $startTime-$endTime"

        tv_location.text = "${code.course.address}"
        nowPrice = code.course.finalPrice
        tv_class_price.text = "¥$nowPrice"
        cgLogo = code.course.imgUrl
        tv_class_duction.text = code.course.courseDes
        tv_class_mans.text = code.course.suitPerson
        tv_class_tips.text = code.course.tips

        if (code.course.fullPerson == 1){
            pay_btn.isEnabled = false
            pay_btn.setBackgroundResource(R.drawable.shape_btn_bg_gray_40)
        }


        if (code.teacher.labelList!=null){
            val layoutParams =
                ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(30, 0, 30, 30)
            for (i in 0 until code.teacher.labelList.size){
                val paramItemView = layoutInflater.inflate(R.layout.adapter_search_histroy, fl_private_skill,false)
                val keyWordTv = paramItemView.findViewById<TextView>(R.id.tv_content)
                keyWordTv.setPadding(35,5,35,5)
                keyWordTv.text = "# "+code.teacher.labelList[i]
                fl_private_skill.addView(paramItemView, layoutParams)
            }
        }


//        longitude = code.course.longitude.toString()
//        latitude = code.course.latitude.toString()
//
        phone = code.course.phone

        tv_private_time.text = "累计服务时长：${code.teacher.serviceDur}小时"
        tv_fenshu.text = "${code.teacher.finalGrade}分"
        tv_private_name.text = code.teacher.teacherName
        tv_private_name.text = code.teacher.teacherName
        Glide.with(this).load(code.teacher.logoUrl)
            .transform( CenterCrop(this), GlideRoundTransform(this,10))
            .placeholder(R.drawable.logo_gray)
            .into(iv_private_logo)

        if (intent.getSerializableExtra("list")==null){
            val orderBean = OrderBean()
            orderBean.areaNum = code.course.gymAreaNum
            orderBean.areaName = code.course.areaName
            orderBean.address = code.course.address
            orderBean.custUserNum = SpUtils.getString(this, AppConstants.USER_NAME)
            orderBean.courseType = 1
            orderBean.startTime = DateHelper.getDayDateByLong(code.course.startTime)
            orderBean.endTime = DateHelper.getDayDateByLong(code.course.endTime)
            orderBean.courseNum = code.course.courseNum
            orderBean.coachUserNum = code.course.genTeacherNum
            orderBean.courseName = code.course.courseName
            orderBean.coachUserName = code.teacher.teacherName
            orderBean.price = code.course.finalPrice
            orderBean.logo = code.course.imgUrl
            orderBean.difficulty = difficultyToStr(code.course.difficulty)
            orderList.add(orderBean)
        }


    }

    private fun difficultyToStr(id:Int):String {
        var str = ""
        when(id){
            1->{str = "容易"}
            2->{str = "中等"}
            3->{str = "难"}
        }
        return str
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
            ShareUtils.share(this@TeamDetailActivity, bitmap, SHARE_MEDIA.WEIXIN)
        }
        pengyouquan.setOnClickListener {
            val bitmap = ShareUtils.createViewBitmap(llBitmap)
            ShareUtils.share(this@TeamDetailActivity, bitmap, SHARE_MEDIA.WEIXIN_CIRCLE)
        }
        qq.setOnClickListener {
//            val bitmap = ShareUtils.createViewBitmap(llBitmap)
//            ShareUtils.share(this@TeamDetailActivity, bitmap, SHARE_MEDIA.QQ)
            requestPermissions(llBitmap)
        }
        weibo.setOnClickListener {
            val bitmap = ShareUtils.createViewBitmap(llBitmap)
            ShareUtils.share(this@TeamDetailActivity, bitmap, SHARE_MEDIA.SINA)
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

//        val bitmap = CodeCreator.createQRImage(
//            AppConstants.WEB,
//            ScreenUtilsHelper.dip2px(this, 60f),
//            ScreenUtilsHelper.dip2px(this, 60f),
//            null
//        )
//        //        erweima_code.setImageBitmap(bitmap);
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val bytes = baos.toByteArray()
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
            val stringBuffer =StringBuffer("qqmap://map/routeplan?type=drive")
                .append("&tocoord=$latitude").append(",")
                .append(longitude).append("&to=${tv_location.text}")
            intent.data = Uri.parse(stringBuffer.toString())
            startActivity(intent)
        }

    }

    /**
     * 拨打电话
     */
    private fun toPhone(){
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

        val dismiss_tv = view.findViewById<TextView>(R.id.dismiss_tv)
        val phone1 = view.findViewById<TextView>(R.id.tv_phone)
        phone1.text = "确认拨打 $phone ?"
        dismiss_tv.clickWithTrigger { m_dialog!!.dismiss() }

        val phone_tv = view.findViewById<TextView>(R.id.phone_tv)
        phone_tv.clickWithTrigger {
            initSimple()
            m_dialog!!.dismiss()
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
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data)
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
