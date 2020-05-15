package com.noplugins.keepfit.userplatform.activity.dialog

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.geocoder.*
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.adapter.Address1Adapter
import com.noplugins.keepfit.userplatform.adapter.Address2Adapter
import com.noplugins.keepfit.userplatform.adapter.Address3Adapter
import com.noplugins.keepfit.userplatform.bean.AddressBean
import com.noplugins.keepfit.userplatform.bean.TaskListEntity
import com.noplugins.keepfit.userplatform.callback.MessageEvent
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.dialog_select_location.*
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class SelectLocationActivity : Activity(), AMapLocationListener {

    var message:MessageEvent?=null
    private var province = ""
    private var city = ""
    private var district = ""
    private var selectType = 0
    override fun onLocationChanged(amapLocation: AMapLocation?) {
        if (amapLocation != null) {
            if (amapLocation.errorCode == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.locationType//获取当前定位结果来源，如网络定位结果，详见定位类型表
                val currentLat = amapLocation.latitude//获取纬度
                val currentLon = amapLocation.longitude//获取经度
                //                        latLonPoint = new LatLonPoint(currentLat, currentLon);  // latlng形式的
                /*currentLatLng = new LatLng(currentLat, currentLon);*/   //latlng形式的
                Log.i("currentLocation", "currentLat : $currentLat currentLon : $currentLon")
                amapLocation.accuracy//获取精度信息

//                selectAddress = amapLocation.district

                tv_nowLoc.text = amapLocation.district

                val code = amapLocation.adCode.toString().substring(0,4)+"00"
                province = amapLocation.adCode.toString().substring(0,2)+"0000"
                city = code
                district = amapLocation.adCode

                message = MessageEvent()
                message!!.currentLon = currentLon
                message!!.currentLat = currentLat
                message!!.address = amapLocation.district


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e(
                    "AmapError", "location Error, ErrCode:"
                            + amapLocation.errorCode + ", errInfo:"
                            + amapLocation.errorInfo
                )




            }
        }
    }

    private var entities: MutableList<TaskListEntity.LeftMenuEntity>? = null

    //声明AMapLocationClient类对象
    internal var mLocationClient: AMapLocationClient? = null
    //声明AMapLocationClientOption对象
    var mLocationOption: AMapLocationClientOption? = null

    private val mPerms = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)

    private var selectAddress = ""
    private var selectText = ""

    /**
     * 定位回调监听器
     */
//    var mLocationListener: AMapLocationListener = AMapLocationListener { amapLocation ->
//
//
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_select_location)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.BOTTOM)
//        ViewUtil.scaleContentView(findViewById<View>(R.id.act_main_root_layout) as ViewGroup)
        entities = ArrayList()
        initSelect()
//        requestPermission()

        if (intent.getStringExtra("address") != null){
            tv_nowLoc.text = (intent.getStringExtra("address"))
        }

        tv_nowLoc.setOnClickListener {
            if (message!=null){
                SpUtils.putString(this@SelectLocationActivity, AppConstants.LOCATION, message!!.address)
                SpUtils.putString(this@SelectLocationActivity, AppConstants.LAT, "" + message!!.currentLat)
                SpUtils.putString(this@SelectLocationActivity, AppConstants.LON, "" + message!!.currentLon)

                SpUtils.putString(applicationContext, AppConstants.PROVINCE, province)
                SpUtils.putString(applicationContext, AppConstants.CITY, city)
                SpUtils.putString(applicationContext, AppConstants.DISTRICT, district)
                EventBus.getDefault().post(message)
            }
            finish()
        }
        tv_re_locate.clickWithTrigger {
            requestPermission()
        }
        share_btn.clickWithTrigger(2000) {
            //关闭当前activity 并发送地址

            //默认上海市 市人民政府 121.473701,31.230416
//            val messageEvent = MessageEvent(31.230416, 121.473701)
//

            if (selectText != ""){
                getLatlon(selectText)
            } else {
                if (message!=null){
                    SpUtils.putString(this@SelectLocationActivity, AppConstants.LOCATION, message!!.address)
                    SpUtils.putString(this@SelectLocationActivity, AppConstants.LAT, "" + message!!.currentLat)
                    SpUtils.putString(this@SelectLocationActivity, AppConstants.LON, "" + message!!.currentLon)

                    SpUtils.putString(applicationContext, AppConstants.PROVINCE, province)
                    SpUtils.putString(applicationContext, AppConstants.CITY, city)
                    SpUtils.putString(applicationContext, AppConstants.DISTRICT, district)
                    EventBus.getDefault().post(message)
                }

                finish()
            }

        }
        back_btn.clickWithTrigger {
            finish()
        }


    }
    var provineAdapter: Address1Adapter?=null
    var cityAdapter: Address2Adapter?=null
    var areaAdapter: Address3Adapter?=null
    private var bean1:ArrayList<AddressBean.Province> = ArrayList()
    private var bean2:ArrayList<AddressBean.City> = ArrayList()
    private var bean3:ArrayList<AddressBean.Area> = ArrayList()
    private fun initSelect() {
        //调整RecyclerView的排列方向
        rv_province.layoutManager  = LinearLayoutManager(this)
        provineAdapter = Address1Adapter(bean1)
        rv_province.adapter = provineAdapter
        provineAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            for (i in 0 until bean1.size) {
                bean1.get(i).setClicks(false)
            }
//                isClicks.set(helper.getLayoutPosition(), true);
            bean1.get(position).setClicks(true)
            provineAdapter!!.notifyDataSetChanged()
            requestAddress(2,bean1[position].prvnccd)
            selectAddress = (view as TextView).text.toString()
            selectText = (view as TextView).text.toString()
            selectType = 1
            city = ""
            district = ""
        }

        rv_city.layoutManager  = LinearLayoutManager(this)
        cityAdapter = Address2Adapter( bean2)
        rv_city.adapter = cityAdapter

        cityAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            for (i in 0 until bean2.size) {
                bean2.get(i).setClicks(false)
            }
//                isClicks.set(helper.getLayoutPosition(), true);
            bean2.get(position).setClicks(true)
            cityAdapter!!.notifyDataSetChanged()
            requestAddress(3,bean2[position].citycd)
            selectAddress = (view as TextView).text.toString()
            selectText += (view as TextView).text.toString()
            selectType =2
            district = ""
        }

        rv_area.layoutManager  = LinearLayoutManager(this)
        areaAdapter = Address3Adapter( bean3)
        rv_area.adapter = areaAdapter

        areaAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            //
            for (i in 0 until bean3.size) {
                bean3.get(i).setClicks(false)
            }
//                isClicks.set(helper.getLayoutPosition(), true);
            bean3.get(position).setClicks(true)
            areaAdapter!!.notifyDataSetChanged()
            selectAddress = (view as TextView).text.toString()
            selectText += (view as TextView).text.toString()
            selectType = 3
        }
        requestAddress(0,"")


    }
//
//    private fun change(type:Int, adapter: Address1Adapter, nowBean:AddressBean){
//
//        adapter.notifyDataSetChanged()
//    }

    private fun requestAddress(type:Int,number:String){
        val params = HashMap<String, Any>()
        if(type == 0){
            params["province"] = "1"
        }
        if(type == 2){
            params["prvnccd"] = number
        }
       if (type == 3){
           params["citycd"] = number
       }

        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        val subscription = Network.getInstance("获取数据", applicationContext)
            .findAllCity(
                requestBody,
                ProgressSubscriberNew(
                    AddressBean::class.java,
                    object : GsonSubscriberOnNextListener<AddressBean> {
                        override fun on_post_entity(code: AddressBean, get_message_id: String) {
                            if (type ==0){
                                bean1.addAll(code.province)
                                provineAdapter!!.notifyDataSetChanged()
                            }
                            if (type == 2){
                                bean2.clear()
                                bean3.clear()
                                bean2.addAll(code.city)
                                cityAdapter!!.notifyDataSetChanged()
                                areaAdapter!!.notifyDataSetChanged()
                            }

                            if (type == 3){
                                bean3.clear()
                                bean3.addAll(code.area)
                                areaAdapter!!.notifyDataSetChanged()
                            }
                        }
                    },
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            Logger.e("", "获取数据失败：$error")
                            Toast.makeText(applicationContext, "获取数据失败！", Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    private fun initGaode() {
        //初始化定位
        mLocationClient = AMapLocationClient(this)
        //设置定位回调监听
        mLocationClient!!.setLocationListener(this)
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

    @AfterPermissionGranted(PERMISSIONS)
    private fun requestPermission() {
        if (EasyPermissions.hasPermissions(this, *mPerms)) {
            //Log.d(TAG, "onClick: 获取读写内存权限,Camera权限和wifi权限");
            initGaode()

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
            } else {
                //                    showGPSContacts();
            }
            else -> {
            }
        }
        Log.i("permission", "quan xian fan kui")
        //如果用户取消，permissions可能为null.

    }

    companion object {

        private const val PERMISSIONS = 100//请求码
    }

    private fun getLatlon(address:String){
        val geocodeSearch= GeocodeSearch(this)
        geocodeSearch.setOnGeocodeSearchListener(object :GeocodeSearch.OnGeocodeSearchListener{
            override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
            }

            override fun onGeocodeSearched(geocodeResult: GeocodeResult?, i: Int) {
                if (i == 1000){
//                    Log.d("TAG_GAODE",Gson().toJson(geocodeResult))
                    if (geocodeResult?.geocodeAddressList != null &&
                        geocodeResult.geocodeAddressList.size>0){
                        val geocodeAddress:GeocodeAddress = geocodeResult.getGeocodeAddressList().get(0)
                        val latitude = geocodeAddress.latLonPoint.latitude//纬度
                        val longititude = geocodeAddress.latLonPoint.longitude//经度
                        val adcode = geocodeAddress.adcode//区域编码
                        Log.d("adcode","adcode:"+adcode)

                        val messageEvent = MessageEvent()
                        messageEvent.currentLon = longititude
                        messageEvent.currentLat = latitude
                        messageEvent.address = selectAddress
                        SpUtils.putString(this@SelectLocationActivity, AppConstants.LOCATION, selectAddress)
                        SpUtils.putString(this@SelectLocationActivity, AppConstants.LAT, "" + latitude)
                        SpUtils.putString(this@SelectLocationActivity, AppConstants.LON, "" + longititude)

                        when(selectType){
                            1->{
                                province = adcode
                                SpUtils.putString(applicationContext, AppConstants.PROVINCE, province)
                            }
                            2->{
                                province = adcode.toString().substring(0,2)+"0000"
                                //4个直辖市 特殊处理
                                city = if (selectAddress =="上海市" || selectAddress =="北京市"
                                    || selectAddress =="天津市" || selectAddress =="重庆市"){
                                    adcode.toString().substring(0,2)+"0100"
                                } else{
                                    adcode
                                }
                                SpUtils.putString(applicationContext, AppConstants.PROVINCE, province)
                                SpUtils.putString(applicationContext, AppConstants.CITY, city)
                            }
                            3->{
                                province = adcode.toString().substring(0,2)+"0000"
                                city = adcode.toString().substring(0,4)+"00"
                                district = adcode
                            }
                        }

                        SpUtils.putString(applicationContext, AppConstants.PROVINCE, province)
                        SpUtils.putString(applicationContext, AppConstants.CITY, city)
                        SpUtils.putString(applicationContext, AppConstants.DISTRICT, district)


//                        EventBus.getDefault().post(message)
                        EventBus.getDefault().post(messageEvent)
                        finish()

                    } else {
                        Toast.makeText(applicationContext,"你选择的地址暂无地图数据哦",Toast.LENGTH_SHORT).show()
                    }
                } else{
                    //地址输入错误
                    Toast.makeText(applicationContext,"你选择的地址有误哦",Toast.LENGTH_SHORT).show()
                }
            }

        })

        val geocodeQuery = GeocodeQuery(address.trim(),"")
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery)
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
}
