package com.noplugins.keepfit.userplatform

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.media.SoundPool
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.allenliu.versionchecklib.callback.OnCancelListener
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder
import com.allenliu.versionchecklib.v2.builder.UIData
import com.allenliu.versionchecklib.v2.callback.CustomDownloadingDialogListener
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.adapter.ContentPagerAdapterMy
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.base.MyApplication
import com.noplugins.keepfit.userplatform.bean.PromotionsBean
import com.noplugins.keepfit.userplatform.entity.MaxMessageEntity
import com.noplugins.keepfit.userplatform.entity.VersionEntity
import com.noplugins.keepfit.userplatform.fragment.FindFragment
import com.noplugins.keepfit.userplatform.fragment.MessageFragment
import com.noplugins.keepfit.userplatform.fragment.MineFragment
import com.noplugins.keepfit.userplatform.fragment.TimeLinFormatFragment
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.jpush.TagAliasOperatorHelper
import com.noplugins.keepfit.userplatform.util.GlideRoundTransform
import com.noplugins.keepfit.userplatform.util.MessageEvent
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.VersionUtils
import com.noplugins.keepfit.userplatform.util.data.SharedPreferencesHelper
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.ui.BaseDialog
import com.noplugins.keepfit.userplatform.util.ui.pop.CommonPopupWindow
import com.noplugins.keepfit.userplatform.util.ui.progress.CustomHorizontalProgresWithNum
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import kotlin.system.exitProcess

class MainActivity : BaseActivity(), View.OnClickListener, EasyPermissions.RationaleCallbacks {


    internal var bottom_iamge_views: MutableList<ImageView> = ArrayList()
    private var sp: SoundPool? = null//声明一个SoundPool
    private var music: Int = 0//定义一个整型用load（）；来设置suondID
    private val tabFragments = ArrayList<Fragment>()
    private lateinit var builder: DownloadBuilder
    private var is_qiangzhi_update = false
    private var update_url = ""

    //声明AMapLocationClient类对象
    private var mLocationClient: AMapLocationClient? = null
    //声明AMapLocationClientOption对象
    var mLocationOption: AMapLocationClientOption? = null

    private val mPerms = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)

    companion object {

        private const val PERMISSIONS = 100//请求码
    }

    override fun initBundle(parms: Bundle?) {

    }

    override fun initView() {
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        requestPermission()

        bottom_iamge_views.add(home_img)
        bottom_iamge_views.add(shipu_img)
        bottom_iamge_views.add(movie_img)
        bottom_iamge_views.add(mine_img)

        btn_home.setOnClickListener(this)
        btn_shipu.setOnClickListener(this)
        btn_movie.setOnClickListener(this)
        btn_mine.setOnClickListener(this)


    }

    private fun update_app() {
        val params: MutableMap<String, Any> =
            HashMap()
        params["type"] = "cust"
        params["code"] = VersionUtils.getAppVersionCode(applicationContext)
        params["phoneType"] = "2"
        val subscription = Network.getInstance("升级版本", this)
            .update_version(
                params,
                ProgressSubscriber("升级版本", object : SubscriberOnNextListener<Bean<VersionEntity>> {
                    override fun onNext(result: Bean<VersionEntity>) {
                        update_url = result.data.url
                        //是否需要强制升级1强制升级 2不升级 3可升级可不升级
                        if (result.data.up == 1) {
                            is_qiangzhi_update = true
                            update_app_pop()
                        } else if (result.data.up == 3) {
                            update_app_pop()
                            is_qiangzhi_update = false
                        } else {
                            requestPromotions()
                        }
                    }

                    override fun onError(error: String?) {
                        requestPromotions()
                    }

                }, this, false)
            )
    }

    private fun update_app_pop() {
        builder = AllenVersionChecker
            .getInstance()
            .downloadOnly(crateUIData())
        builder.setCustomVersionDialogListener(createCustomDialogTwo()) //设置更新弹窗样式
        builder.setCustomDownloadingDialogListener(createCustomDownloadingDialog()) //设置下载样式
        builder.setForceRedownload(true) //强制重新下载apk（无论本地是否缓存）
        builder.setShowNotification(true) //显示下载通知栏
        builder.setShowDownloadingDialog(true) //显示下载中对话框
        builder.setShowDownloadFailDialog(true) //显示下载失败对话框
        builder.setDownloadAPKPath(Environment.getExternalStorageDirectory().toString() + "/noplugins/apkpath/") //自定义下载路径
        builder.setOnCancelListener(OnCancelListener {
            if (is_qiangzhi_update) {
                Toast.makeText(this@MainActivity, "已关闭更新", Toast.LENGTH_SHORT).show()
                val intent = Intent()
                intent.action = "android.intent.action.MAIN"
                intent.addCategory("android.intent.category.HOME")
                startActivity(intent)
                finish()
            } else {
                requestPromotions()
                Toast.makeText(this@MainActivity, "已关闭更新", Toast.LENGTH_SHORT).show()
            }
        })
        builder.executeMission(this)
    }

    /**
     * 自定义下载中对话框，下载中会连续回调此方法 updateUI
     * 务必用库传回来的context 实例化你的dialog
     *
     * @return
     */
    private fun createCustomDownloadingDialog(): CustomDownloadingDialogListener? {
        return object : CustomDownloadingDialogListener {
            override fun getCustomDownloadingDialog(
                context: Context,
                progress: Int,
                versionBundle: UIData
            ): Dialog {
                return BaseDialog(context, R.style.BaseDialog, R.layout.custom_download_layout)
            }

            override fun updateUI(dialog: Dialog, progress: Int, versionBundle: UIData) {
                val pb: CustomHorizontalProgresWithNum = dialog.findViewById(R.id.pb)
                pb.setProgress(progress)
                pb.setMax(100)
            }
        }
    }

    /**
     * 更新弹窗样式
     *
     * @return
     */
    private fun createCustomDialogTwo(): CustomVersionDialogListener? {
        return CustomVersionDialogListener { context: Context?, versionBundle: UIData ->
            val baseDialog = BaseDialog(context, R.style.BaseDialog, R.layout.shengji_pop_layout)
            val textView: TextView = baseDialog.findViewById(R.id.tv_msg)
            textView.text = versionBundle.content
            baseDialog.setCanceledOnTouchOutside(false)
            baseDialog
        }
    }

    /**
     * @return
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData 内部是一个Bundle
     */
    private fun crateUIData(): UIData? {
        val uiData = UIData.create()
        uiData.title = getString(R.string.update_title)
        uiData.downloadUrl = update_url
        if (is_qiangzhi_update) {
            uiData.content = getString(R.string.updatecontent2)
        } else {
            uiData.content = getString(R.string.updatecontent)
        }
        return uiData
    }

    public override fun onStart() {
        super.onStart()
        if (mLocationClient != null) {
            mLocationClient!!.startLocation() // 启动定位
        }
    }

    public override fun onPause() {
        super.onPause()
        if (mLocationClient != null) {
            mLocationClient!!.stopLocation()//停止定位
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        if (mLocationClient != null) {
            mLocationClient!!.onDestroy()//销毁定位客户端。
        }

    }


    override fun doBusiness(mContext: Context?) {
        //初始化页面
        tabFragments.add(FindFragment.findInstance("第一页"))
        tabFragments.add(TimeLinFormatFragment.newInstance("第二页"))
        tabFragments.add(MessageFragment.newInstance("第三页"))
        tabFragments.add(MineFragment.newInstance("第四页"))
        //初始化viewpager
        val contentAdapter = ContentPagerAdapterMy(supportFragmentManager, tabFragments)
        viewpager_content.setScroll(false)
        viewpager_content.setAdapter(contentAdapter)
        viewpager_content.setCurrentItem(0)

        MyApplication.addDestoryActivity(this, "MainActivity")

        //获取消息总数，设置消息总数
        get_message_all()

        loginSuccess()


        //请求活动的logo
//        requestPromotions()
    }


    /**
     * 判断是否调用活动接口
     */
    private fun requestPromotions() {
        val nowDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        //todo 还需要一个判断条件 当前时间已经不在活动日期内了，则直接 return

        if (SpUtils.getInt(applicationContext, AppConstants.PROMOTIONS, -1) == nowDay) {
            //表示活动界面 当天已经弹出过了
            return
        }
        //调用接口

        val params: MutableMap<String, Any> = HashMap()
        params["user_num"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        val subscription = Network.getInstance("优惠活动", this)
            .getIndexCoupon(
                params,
                ProgressSubscriber("优惠活动", object : SubscriberOnNextListener<Bean<PromotionsBean>> {
                    override fun onNext(result: Bean<PromotionsBean>) {
                        //将当天日期保存到sp中
                        SpUtils.putInt(applicationContext, AppConstants.PROMOTIONS, nowDay)
                        if (result.data.hasCoupon == 1) {
                            promotionsDialog(result.data.url)
                        }
                    }

                    override fun onError(error: String?) {

                    }

                }, this, false)
            )

    }

    /**
     * 设置活动的dialog
     */
    private fun promotionsDialog(logoUrl: String) {
        val popupWindow = CommonPopupWindow.Builder(this)
            .setView(R.layout.dialog_promotions)
            .setBackGroundLevel(0.5f)//0.5f
            .setAnimationStyle(R.style.main_menu_animstyle)
            .setWidthAndHeight(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            .setOutSideTouchable(true).create()
        popupWindow.showAsDropDown(viewpager_content)

        /**设置逻辑 */
        val view = popupWindow.contentView

        val logo = view.findViewById<ImageView>(R.id.iv_promotions_logo)
        val close = view.findViewById<ImageView>(R.id.iv_close)
        Glide.with(this).load(logoUrl)
            .transform(CenterCrop(this), GlideRoundTransform(this, 10))
            .into(logo)

        logo.setOnClickListener {
            popupWindow.dismiss()
        }
        close.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    private fun loginSuccess() {
        //如果没有缓存的别名，重新获取
        if ("" == SpUtils.getString(this, AppConstants.IS_SET_ALIAS, "")) {
            //设置别名
            val tagAliasBean = TagAliasOperatorHelper.TagAliasBean()
            TagAliasOperatorHelper.sequence++
            //设置用户编号为别名
//            if ("" == SpUtils.getString(applicationContext, AppConstants.USER_NAME)) {
//                tagAliasBean.alias = "null_user_id"
//            } else {
//                val user_id = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
//                tagAliasBean.alias = user_id
//            }
            tagAliasBean.alias = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
            tagAliasBean.isAliasAction = true
            tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET
            TagAliasOperatorHelper.getInstance().handleAction(
                applicationContext,
                TagAliasOperatorHelper.sequence, tagAliasBean
            )
            Log.e("设置的alias", "" + SharedPreferencesHelper.get(this, AppConstants.IS_SET_ALIAS, "").toString())

        } else {
            Log.e("已缓存alias", "" + SharedPreferencesHelper.get(this, AppConstants.IS_SET_ALIAS, "").toString())
        }

    }

    override fun onResume() {
        super.onResume()
        //更新app
        update_app()
        if (null != intent.extras) {
            val parms = intent.extras
            if (parms!!.getString("jpush_enter") == "jpush_enter1") {

                viewpager_content.currentItem = 2
                xianshi_three()
                //设置跳转到消息tab1
                val messageEvent = MessageEvent("jpush_main_enter1")
                EventBus.getDefault().postSticky(messageEvent)

            } else if (parms.getString("jpush_enter") == "jpush_enter2") {
                viewpager_content.currentItem = 2

                xianshi_three()
                //设置跳转到消息tab2
                val messageEvent = MessageEvent("jpush_main_enter2")
                EventBus.getDefault().postSticky(messageEvent)

            } else if (parms.getString("jpush_enter") == "jpush_enter3") {

                viewpager_content.currentItem = 2
                xianshi_three()

                //设置跳转到消息tab3
                val messageEvent = MessageEvent("jpush_main_enter3")
                EventBus.getDefault().postSticky(messageEvent)

            }
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btn_home -> {
                viewpager_content.currentItem = 0
                xianshi_one()
                home_name.setTextColor(resources.getColor(R.color.btn_text_color))
                shipu_name.setTextColor(resources.getColor(R.color.contents_text))
                movie_name.setTextColor(resources.getColor(R.color.contents_text))
                mine_name.setTextColor(resources.getColor(R.color.contents_text))
            }
            R.id.btn_shipu -> {
                viewpager_content.currentItem = 1
                xianshi_two()
                home_name.setTextColor(resources.getColor(R.color.contents_text))
                shipu_name.setTextColor(resources.getColor(R.color.btn_text_color))
                movie_name.setTextColor(resources.getColor(R.color.contents_text))
                mine_name.setTextColor(resources.getColor(R.color.contents_text))
            }
            R.id.btn_movie -> {
                viewpager_content.currentItem = 2
                xianshi_three()
                home_name.setTextColor(resources.getColor(R.color.contents_text))
                shipu_name.setTextColor(resources.getColor(R.color.contents_text))
                movie_name.setTextColor(resources.getColor(R.color.btn_text_color))
                mine_name.setTextColor(resources.getColor(R.color.contents_text))
            }
            R.id.btn_mine -> {
                viewpager_content.currentItem = 3
                xianshi_four()
                home_name.setTextColor(resources.getColor(R.color.contents_text))
                shipu_name.setTextColor(resources.getColor(R.color.contents_text))
                movie_name.setTextColor(resources.getColor(R.color.contents_text))
                mine_name.setTextColor(resources.getColor(R.color.btn_text_color))
            }
        }
    }

    private fun xianshi_one() {
        bottom_iamge_views.get(0).setImageResource(R.drawable.icon_home_on)
        bottom_iamge_views.get(1).setImageResource(R.drawable.icon_discover_off)
        bottom_iamge_views.get(2).setImageResource(R.drawable.icon_issue_off)
        bottom_iamge_views.get(3).setImageResource(R.drawable.icon_user_off)


    }

    private fun xianshi_two() {
        bottom_iamge_views.get(0).setImageResource(R.drawable.icon_home_off)
        bottom_iamge_views.get(1).setImageResource(R.drawable.icon_discover_on)
        bottom_iamge_views.get(2).setImageResource(R.drawable.icon_issue_off)
        bottom_iamge_views.get(3).setImageResource(R.drawable.icon_user_off)
    }

    private fun xianshi_three() {
        bottom_iamge_views.get(0).setImageResource(R.drawable.icon_home_off)
        bottom_iamge_views.get(1).setImageResource(R.drawable.icon_discover_off)
        bottom_iamge_views.get(2).setImageResource(R.drawable.icon_issue_on)
        bottom_iamge_views.get(3).setImageResource(R.drawable.icon_user_off)
    }

    private fun xianshi_four() {
        bottom_iamge_views.get(0).setImageResource(R.drawable.icon_home_off)
        bottom_iamge_views.get(1).setImageResource(R.drawable.icon_discover_off)
        bottom_iamge_views.get(2).setImageResource(R.drawable.icon_issue_off)
        bottom_iamge_views.get(3).setImageResource(R.drawable.icon_user_on)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun upadate(messageEvent: MessageEvent) {
        if (messageEvent.message == "update_message_num") {//获取消息总数，设置消息总数
            Log.e("九分裤都是", "kfsld")
            get_message_all()
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(messageEvent: String) {
        if ("消息已读" == messageEvent || "购买了订单" == messageEvent) {
            get_message_all()
        }
    }


    private fun get_message_all() {
        val params = HashMap<String, String>()
        if ("" == SpUtils.getString(this, AppConstants.USER_NAME)) {

        } else {
            val user_id = SpUtils.getString(this!!, AppConstants.USER_NAME)
            params["userNum"] = user_id//用户编号
        }
        val gson = Gson()
        val json_params = gson.toJson(params)
        val json = Gson().toJson(params)//要传递的json
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)
        Log.e(TAG, "获取消息总数参数：$json_params")
        subscription = Network.getInstance("获取消息总数", applicationContext)
            .get_message_all(
                requestBody,
                ProgressSubscriberNew(
                    MaxMessageEntity::class.java,
                    object : GsonSubscriberOnNextListener<MaxMessageEntity> {
                        override fun on_post_entity(maxMessageEntity: MaxMessageEntity, get_message_id: String) {
                            Log.e(TAG, "获取消息总数成功：" + maxMessageEntity.getNoRead())
                            //设置消息总数
                            if (maxMessageEntity.getNoRead() > 0) {
                                message_view.visibility = View.VISIBLE
                                if (maxMessageEntity.getNoRead() > 99) {
                                    message_num_tv.text = "99+"
                                } else {
                                    message_num_tv.text = maxMessageEntity.getNoRead().toString()
                                }
                            } else {
                                message_view.visibility = View.GONE
                            }

                        }
                    },
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            Logger.e(TAG, "获取获取场馆消息总数报错：$error")
                            //Toast.makeText(getApplicationContext(), "获取审核状态失败！", Toast.LENGTH_SHORT).show();
                        }
                    },
                    this,
                    true
                )
            )
    }

    //退出时的时间
    private var mExitTime: Long = 0

    override fun onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(applicationContext, "再按一次退出", Toast.LENGTH_SHORT).show()
            mExitTime = System.currentTimeMillis()
        } else {
            finish()
            exitProcess(0)
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val mFlag = intent!!.getIntExtra("flag", 0)
//        if (mFlag == 0) { // 我的
//
//        }
        checkMoudle(mFlag)
    }

    // 跳转到对应的fragment
    fun checkMoudle(position: Int) {
        when (position) {
            0 -> {
                viewpager_content.currentItem = 0
                xianshi_one()
                home_name.setTextColor(resources.getColor(R.color.btn_text_color))
                shipu_name.setTextColor(resources.getColor(R.color.contents_text))
                movie_name.setTextColor(resources.getColor(R.color.contents_text))
                mine_name.setTextColor(resources.getColor(R.color.contents_text))
            }
            1 -> {
                viewpager_content.currentItem = 1
                xianshi_two()
                home_name.setTextColor(resources.getColor(R.color.contents_text))
                shipu_name.setTextColor(resources.getColor(R.color.btn_text_color))
                movie_name.setTextColor(resources.getColor(R.color.contents_text))
                mine_name.setTextColor(resources.getColor(R.color.contents_text))
            }
            2 -> {
                bottom_iamge_views.get(0).setImageResource(R.drawable.icon_home_off)
                bottom_iamge_views.get(1).setImageResource(R.drawable.icon_discover_off)
                bottom_iamge_views.get(2).setImageResource(R.drawable.icon_issue_on)
                bottom_iamge_views.get(3).setImageResource(R.drawable.icon_user_off)
            }
            3 -> {
                viewpager_content.currentItem = 3
                xianshi_four()
                home_name.setTextColor(resources.getColor(R.color.contents_text))
                shipu_name.setTextColor(resources.getColor(R.color.contents_text))
                movie_name.setTextColor(resources.getColor(R.color.contents_text))
                mine_name.setTextColor(resources.getColor(R.color.btn_text_color))
            }
        }
    }


    private fun initGaode() {
//        if (SpUtils.getString(applicationContext, AppConstants.LOCATION, "") == "") {
//            SpUtils.putString(applicationContext, AppConstants.LOCATION, "上海市")
////            return
//        }
        //
        Log.d("MainActivity", "怎么又在申请权限了")
        //初始化定位
        mLocationClient = AMapLocationClient(this)
        //设置定位回调监听
        mLocationClient!!.setLocationListener(mLocationListener)
        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()

        mLocationOption!!.isOnceLocation = true
        //        mLocationOption.setOnceLocationLatest(true);
        // 同时使用网络定位和GPS定位,优先返回最高精度的定位结果,以及对应的地址描述信息
        mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。默认连续定位 切最低时间间隔为1000ms
        //        mLocationOption.setInterval(3500);
        //给定位客户端对象设置定位参数
        mLocationClient!!.setLocationOption(mLocationOption)
        //启动定位
        mLocationClient!!.startLocation()
    }

    /**
     * 定位回调监听器
     */
    private var mLocationListener: AMapLocationListener = AMapLocationListener { amapLocation ->
        if (amapLocation != null) {
            Log.i("MainActivity", "首页调用了")
            if (amapLocation.errorCode == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.locationType//获取当前定位结果来源，如网络定位结果，详见定位类型表
                val currentLat = amapLocation.latitude//获取纬度
                val currentLon = amapLocation.longitude//获取经度
                //                        latLonPoint = new LatLonPoint(currentLat, currentLon);  // latlng形式的
                /*currentLatLng = new LatLng(currentLat, currentLon);*/   //latlng形式的
                Log.i("currentLocation", "currentLat : $currentLat currentLon : $currentLon")
                amapLocation.accuracy//获取精度信息


                val city = amapLocation.adCode.toString().substring(0, 4) + "00"
                val province = amapLocation.adCode.toString().substring(0, 2) + "0000"
//                val district = amapLocation.adCode
                SpUtils.putString(applicationContext, AppConstants.PROVINCE, province)
                SpUtils.putString(applicationContext, AppConstants.CITY, city)
                SpUtils.putString(applicationContext, AppConstants.DISTRICT, "")
                val messageEvent = com.noplugins.keepfit.userplatform.callback.MessageEvent(currentLat, currentLon)
                SpUtils.putString(applicationContext, AppConstants.LOCATION, amapLocation.city)
                SpUtils.putString(applicationContext, AppConstants.LAT, "" + currentLat)
                SpUtils.putString(applicationContext, AppConstants.LON, "" + currentLon)
                messageEvent.address = amapLocation.city
                EventBus.getDefault().post(messageEvent)
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e(
                    "AmapError", "location Error, ErrCode:"
                            + amapLocation.errorCode + ", errInfo:"
                            + amapLocation.errorInfo
                )
                Toast.makeText(applicationContext,"缺少定位权限或没有开启定位服务哦～",Toast.LENGTH_SHORT)
                    .show()

                errorDw()


            }
        }
        mLocationClient!!.onDestroy()
    }

    @AfterPermissionGranted(PERMISSIONS)
    private fun requestPermission() {

        initGaode()
        Log.d("MainActivity", "requestPermission")
        if (EasyPermissions.hasPermissions(applicationContext, *mPerms)) {
            //Log.d(TAG, "onClick: 获取读写内存权限,Camera权限和wifi权限");
            initGaode()
            Log.d("MainActivity", "requestPermission")
        } else {
            EasyPermissions.requestPermissions(this, "获取读写内存权限,Camera权限和wifi权限", PERMISSIONS, *mPerms)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            PERMISSIONS -> if (grantResults[0] == PERMISSION_GRANTED && grantResults.size > 0) {  //有权限
                // 获取到权限，作相应处理
                initGaode()
                Log.d("MainActivity", "onRequestPermissionsResult")
            } else {
                //                    showGPSContacts()
            }
            else -> {
            }
        }
        Log.i("permission", "quan xian fan kui")
        //如果用户取消，permissions可能为null.

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
//        super.onPermissionsGranted(requestCode, perms)
        Log.i("permission", "onPermissionsGranted")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
//        super.onPermissionsDenied(requestCode, perms)
        Log.i("permission", "onPermissionsDenied")
    }

    override fun onRationaleDenied(requestCode: Int) {
        Log.i("permission", "onRationaleDenied")}

    override fun onRationaleAccepted(requestCode: Int) {
        Log.i("permission", "onRationaleAccepted")}

    /**
     * 权限错误情况下
     */
    private fun errorDw(){


        //默认上海市 市人民政府 121.473701,31.230416
        if (SpUtils.getString(applicationContext, AppConstants.LAT) == "") {
            SpUtils.putString(applicationContext, AppConstants.LAT, "121.473701")
        }

        if (SpUtils.getString(applicationContext, AppConstants.LON) == "") {
            SpUtils.putString(applicationContext, AppConstants.LON, "31.230416")
        }
        val messageEvent = com.noplugins.keepfit.userplatform.callback.MessageEvent()
        if (SpUtils.getString(applicationContext,AppConstants.LOCATION) == ""){
            messageEvent.address = "上海市"
            SpUtils.putString(applicationContext, AppConstants.PROVINCE, "310000")
            SpUtils.putString(applicationContext, AppConstants.CITY, "")
            SpUtils.putString(applicationContext, AppConstants.DISTRICT, "")
        } else {
            messageEvent.address = SpUtils.getString(applicationContext,AppConstants.LOCATION)
        }

        EventBus.getDefault().post(messageEvent)
    }
}
