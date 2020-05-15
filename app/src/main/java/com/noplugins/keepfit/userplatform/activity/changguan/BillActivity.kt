package com.noplugins.keepfit.userplatform.activity.changguan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jiguang.analytics.android.api.CountEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.dialog.SelectChangguanActivity
import com.noplugins.keepfit.userplatform.adapter.privateClassBillAdapter
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.CouponListBean
import com.noplugins.keepfit.userplatform.bean.OrderBean
import com.noplugins.keepfit.userplatform.bean.OrderReult
import com.noplugins.keepfit.userplatform.entity.CouponEntity
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.ActivityCollectorUtil
import com.noplugins.keepfit.userplatform.util.BaseUtils
import com.noplugins.keepfit.userplatform.util.GlideRoundTransform
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.noplugins.keepfit.userplatform.wxapi.WXPayEntryActivity
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_cg_bill.*
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.set


class BillActivity : BaseActivity() {
    var nowType = -1
    var datas: List<OrderBean> = ArrayList()
    var coupon_num = ""
    var sumOrderPrice = 0.0
    override fun initBundle(parms: Bundle?) {

    }

    override fun initView() {
        setContentLayout(R.layout.activity_cg_bill)
        datas = intent.getSerializableExtra("list") as MutableList<OrderBean>
        nowType = intent.getIntExtra("type", -1)
        nowType()

        if (nowType == 1) {
            initCg(datas[0])
        }
        if (nowType == 2) {
            initPrivate(datas)
        }
        if (nowType == 3) {
            initTeam(datas[0])
        }

        matchUserCoupon()


    }

    private fun initCg(orderBean: OrderBean) {
//        tv_cg_name.text = orderBean.areaName

        if (orderBean.areaName.length > 20){
            tv_cg_name.text =orderBean.areaName.substring(0,20)+"..."
        } else{
            tv_cg_name.text =orderBean.areaName
        }
        tv_prive.text = "¥${orderBean.price}"
        val str = orderBean.endTime.split(" ")[1]
        Log.d("bill", str)

        tv_time.text = "${orderBean.startTime}-$str"
        tv_location.text = orderBean.address
//        tv_sum_price.text = "¥${orderBean.price}"
        Glide.with(this).load(intent.getStringExtra("cgLogo"))
            .transform(CenterCrop(this), GlideRoundTransform(this, 10))
            .placeholder(R.drawable.logo_gray)
            .into(iv_logo)
        sumOrderPrice = orderBean.price
    }

    private fun initPrivate(list: List<OrderBean>) {
        rv_private_class.layoutManager = LinearLayoutManager(this)
        val adapter = privateClassBillAdapter(list)
        rv_private_class.adapter = adapter
        // -----分割-----
        tv_cg_location.text = list[list.size - 1].address
        tv_cg_price.text = "¥${list[list.size - 1].oldPrice}"
//        tv_cg_price.paint.flags = STRIKE_THRU_TEXT_FLAG or ANTI_ALIAS_FLAG
        if (list[list.size - 1].areaName.length > 12){
            tv_private_cg_name.text =list[list.size - 1].areaName.substring(0,12)+"..."
        } else{
            tv_private_cg_name.text =list[list.size - 1].areaName
        }

//        Glide.with(this).load(intent.getStringExtra("privateLogo")).into(iv_private_logo)
        Glide.with(this).load(list[list.size - 1].logo)
            .transform(CenterCrop(this), GlideRoundTransform(this, 10))
            .placeholder(R.drawable.logo_gray)
            .into(iv_cg_logo)

        var sumPrice = BigDecimal(0)
        tv_cg_discounted_price.text = "-¥${list[list.size - 1].oldPrice}"
        for (i in 0 until list.size - 1) {
            sumPrice += BigDecimal.valueOf(list[i].price)
        }

        sumOrderPrice = sumPrice.toDouble()
//        tv_sum_price.text = "¥${sumPrice.toString()}"
        fl_teacher.visibility = View.VISIBLE
    }

    private fun initTeam(orderBean: OrderBean) {
//        tv_team_cg_name.text = orderBean.areaName
        if (orderBean.areaName!=null){
            if (orderBean.areaName.length > 20){
                tv_team_cg_name.text =orderBean.areaName.substring(0,20)+"..."
            } else{
                tv_team_cg_name.text =orderBean.areaName
            }
        }
        tv_team_difficult.text = orderBean.difficulty
        tv_team_class.text = orderBean.courseName
        tv_team_price.text = "¥" + orderBean.price
        tv_team_location.text = orderBean.address
        tv_team_time.text = orderBean.startTime + "-" + orderBean.endTime.split(" ")[1]
        Glide.with(this).load(orderBean.logo)
            .transform(CenterCrop(this), GlideRoundTransform(this, 10))
            .placeholder(R.drawable.logo_gray)
            .into(iv_team_logo)

//        tv_sum_price.text = "¥${orderBean.price}"
        sumOrderPrice = orderBean.price
    }

    override fun doBusiness(mContext: Context?) {
        tv_confirm.setOnClickListener {
            if (BaseUtils.isFastClick()) {
                //搜索埋点
                val cEvent3 = CountEvent("确认订单")
                cEvent3.addKeyValue(
                    "user",
                    SpUtils.getString(applicationContext, AppConstants.USER_NAME)
                )
                ///////////////////////////////
                requestOrder()
            }

        }
        back_btn.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    private fun nowType() {
        if (nowType == 1) {
            rl_changguan.visibility = View.VISIBLE
            return
        }
        if (nowType == 2) {
            rl_private.visibility = View.VISIBLE
            return
        }
        if (nowType == 3) {
            rl_team.visibility = View.VISIBLE
            return
        }
    }

    private fun toPay(orderNum: String, price: Double) {
        val intent = Intent(this@BillActivity, WXPayEntryActivity::class.java)
        intent.putExtra("orderNum", orderNum)
        intent.putExtra("price", price)
        EventBus.getDefault().post("关闭选择场馆")
        startActivity(intent)
        this.finish()

    }

    /**
     * 匹配用户最优优惠券
     */
    private fun matchUserCoupon() {
        val params = HashMap<String, Any>()
        params["user_num"] = SpUtils.getString(applicationContext,AppConstants.USER_NAME)
        params["price"] = sumOrderPrice
        subscription = Network.getInstance("匹配用户最优优惠券", applicationContext)
            .matchUserCoupon(
                params,
                ProgressSubscriber(
                    "匹配用户最优优惠券",
                    object : SubscriberOnNextListener<Bean<CouponListBean>> {
                        override fun onNext(t: Bean<CouponListBean>) {
                            //
                            if (t.data.coupon.size > 0){
                                fl_promotions.visibility = View.VISIBLE
                                tv_discounted_price.text = "-¥${t.data.coupon[0].priceDiscount}"
                                coupon_num = t.data.coupon[0].userCouponNum
                                tv_sum_price.text = "¥${t.data.totalPrice}"
                            } else{
                                tv_sum_price.text = "¥$sumOrderPrice"
                            }
                        }

                        override fun onError(error: String?) {
//                            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                            tv_sum_price.text = "¥$sumOrderPrice"
                        }

                    },
                    this, false)
            )
    }

    private fun requestOrder() {
        val list:MutableList<CouponEntity> = ArrayList()
        val params = HashMap<String, Any>()
        params["orderList"] = datas
        //1.1 新增参数
        if (coupon_num!=""){
            list.add(CouponEntity(coupon_num))
        }
        params["coupon"] = list
//
        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)
        Log.e("生成订单参数", json)
        subscription = Network.getInstance("生成订单", applicationContext)
            .confirmOrder(
                params,
                ProgressSubscriber(
                    "生成订单",
                    object : SubscriberOnNextListener<Bean<OrderReult>> {
                        override fun onNext(t: Bean<OrderReult>) {
                            toPay(t.data.ordNum, t.data.totalPrice)
                        }

                        override fun onError(error: String?) {
                            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                        }

                    },
                    this,
                    true
                )
            )
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
