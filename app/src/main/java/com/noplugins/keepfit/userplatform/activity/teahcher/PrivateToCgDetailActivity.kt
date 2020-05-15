package com.noplugins.keepfit.userplatform.activity.teahcher

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.ms.banner.BannerConfig
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.changguan.BillActivity
import com.noplugins.keepfit.userplatform.adapter.ChangguanDetailFeaturesAdapter
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.ChangguanDetailBean
import com.noplugins.keepfit.userplatform.bean.OrderBean
import com.noplugins.keepfit.userplatform.customize.CustomViewHolder
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.BaseUtils
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.TypeUtil
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_private_to_cg_detail.*
import okhttp3.RequestBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.set
import kotlin.collections.toList

class PrivateToCgDetailActivity : BaseActivity() {
    private var days = ""

    private var longitude = ""
    private var latitude = ""
    private var phone = ""
    private var areaNum = ""
    private var cgLogo = ""
    private var type = -1
    var data: ArrayList<String> = ArrayList()

    var datas: List<OrderBean> = ArrayList()
    var adapter: ChangguanDetailFeaturesAdapter? = null

    companion object {

        private const val PERMISSION_STORAGE_CODE = 10001
        private const val PERMISSION_STORAGE_MSG = "需要电话权限才能联系客服哦"
    }

    val PERMISSION_STORAGE = arrayOf(Manifest.permission.CALL_PHONE)

    override fun initBundle(parms: Bundle?) {

    }

    override fun initView() {
        setContentView(R.layout.activity_private_to_cg_detail)
        if (null != intent.getSerializableExtra("list")) {
            datas = intent.getSerializableExtra("list") as MutableList<OrderBean>
        }

        if (null != intent.getStringExtra("areaNum")){
            areaNum = intent.getStringExtra("areaNum")
        }
        initAdapter()
        requestDetails()

        if (null != intent.getStringExtra("is_from_order_list")) {
            tv_next_select.setVisibility(View.GONE)
        }

    }

    override fun doBusiness(mContext: Context?) {
        share_btn.clickWithTrigger {
            takeScreenShot(this)
        }

        back_btn.clickWithTrigger {
            back_btn.setOnClickListener {
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        iv_collect.clickWithTrigger {
            requestCollect()
        }

        tv_location.clickWithTrigger {
            toMap()
        }

        tv_toPhone.clickWithTrigger {
            toPhone()
        }

        tv_next_select.clickWithTrigger {
            val intent = Intent(this, BillActivity::class.java)
            intent.putExtra("list", datas as Serializable)
            intent.putExtra("type", 2)
            startActivity(intent)
        }
    }


    override fun onBackPressed() {
//        super.onBackPressed()
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun initAdapter() {
        rv_features.layoutManager = GridLayoutManager(this, 4)
        adapter = ChangguanDetailFeaturesAdapter(data)
        rv_features.adapter = adapter
    }

    private fun requestDetails() {
        val params = HashMap<String, Any>()
        params["areaNum"] = areaNum
        params["userNum"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        subscription = Network.getInstance("获取详情", applicationContext)
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
                ProgressSubscriber("收藏场馆",
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {
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
                            Logger.e("", "收藏场馆失败：$error")
                            Toast.makeText(applicationContext, "收藏场馆失败！", Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    false
                )
            )
    }

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
        tv_work_time.text = "营业时间 ${code.areaDetail.businessStart}-${code.areaDetail.businessEnd}"
        tv_fenshu.text = "${code.areaDetail.finalGradle}分"
        tv_location.text = "${code.areaDetail.address}"
        cgLogo = code.areaDetail.logo


        val arrList = code.areaDetail.facility.split(",").toList()
        data.addAll(arrList)
        adapter!!.notifyDataSetChanged()

        longitude = code.areaDetail.longitude.toString()
        latitude = code.areaDetail.latitude.toString()
        phone = code.areaDetail.phone


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
        dismiss_tv.clickWithTrigger { m_dialog!!.dismiss() }
        val cut_image = view.findViewById<ImageView>(R.id.cut_image)
        cut_image.setImageBitmap(bitmap_cut)
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
        dismiss_tv.setOnClickListener { m_dialog!!.dismiss() }

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

        val dismiss_tv = view.findViewById<TextView>(R.id.dismiss_tv)
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
