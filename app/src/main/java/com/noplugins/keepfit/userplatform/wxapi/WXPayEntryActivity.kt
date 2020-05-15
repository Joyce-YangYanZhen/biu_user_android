package com.noplugins.keepfit.userplatform.wxapi


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import cn.jiguang.analytics.android.api.CountEvent
import com.alipay.sdk.app.PayTask
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.OederDetailActivity
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.OrderResultBean
import com.noplugins.keepfit.userplatform.bean.WxPayBean
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.PublicPopControl
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.pay.PayResult
import com.noplugins.keepfit.userplatform.util.BaseUtils
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.noplugins.keepfit.userplatform.wxapi.WxSignUtils.genPackageSign
import com.orhanobut.logger.Logger
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.unionpay.UPPayAssistEx
import com.unionpay.uppay.PayActivity
import kotlinx.android.synthetic.main.activity_cg_pay.*
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import java.util.*

class WXPayEntryActivity : BaseActivity(), IWXAPIEventHandler {
    private var type = -1

    private var orderNum: String = ""
    private var price: Double = 0.0

    private var aliPayInfo = ""
    private var wxPayInfo = ""
    private var dialogType = -1

    private var sp: SoundPool? = null//声明一个SoundPool
    private var music: Int = 0//定义一个整型用load（）；来设置suondID

    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {
        setContentView(R.layout.activity_cg_pay)

//        api!!.handleIntent(intent, this)
        if (intent != null) {
            orderNum = intent.getStringExtra("orderNum")
            price = intent.getDoubleExtra("price", 0.0)
            tv_sum_price.text = "¥$price"
        }

        //初始化音效
        //初始化音效
        sp = SoundPool(10, AudioManager.STREAM_SYSTEM, 5)//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp!!.load(this, R.raw.message, 1) //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级


    }

    override fun doBusiness(mContext: Context?) {
        back_btn.setOnClickListener {
            toPayBack()
        }
        cb_product_suggest.setOnClickListener {
            if (cb_product_suggest.isChecked()) {
                type = 1
                cb_fault_feedback.setChecked(false)
                cb_other.setChecked(false)
            } else {
                type = 0
            }
        }
        cb_fault_feedback.setOnClickListener {
            if (cb_fault_feedback.isChecked()) {
                type = 2
                cb_product_suggest.setChecked(false)
                cb_other.setChecked(false)
            } else {
                type = 0
            }
        }
        cb_other.setOnClickListener {
            if (cb_other.isChecked()) {
                type = 3
                cb_fault_feedback.setChecked(false)
                cb_product_suggest.setChecked(false)
            } else {
                type = 0
            }
        }

        tv_pay.setOnClickListener {
            //搜索埋点
            val cEvent3 = CountEvent("支付")
            cEvent3.addKeyValue(
                "user",
                SpUtils.getString(applicationContext, AppConstants.USER_NAME)
            )
            ///////////////////////////////
            if (type == 2) {
                requestAliPayInfo(1)

            } else if (type == 3) {
//                requestAliPayInfo(2)
                testWxRequest()
            } else {
//                unionPayRequest()
//                pay("",PayMode.TEST)
                Toast.makeText(applicationContext, "请选择支付方式", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private var api: IWXAPI? = null


    private val SDK_PAY_FLAG = 1
    private val SDK_PAY_WECHAT = 2

    companion object {
        private val TAG = "MicroMsg.SDKSample.WXPayEntryActivity"
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {}

    @SuppressLint("LongLogTag")
    override fun onResp(resp: BaseResp) {
//        Log.d(TAG, "onPayFinish, errCode = " + resp.errStr)

        val gson = Gson()
        val str = gson.toJson(resp)
        Log.d("GSON", "onPayFinish:$str")

        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle(R.string.app_tip)
//            builder.setMessage(getString(R.string.pay_result_callback_msg, resp.errCode.toString()))
//            builder.show()

            if (resp.errCode == 0) {
                BaseUtils.showProgressDialog(this@WXPayEntryActivity)
                Handler().postDelayed(Runnable {
                    twoYanzhen()
                }, 3000)
            } else {
                toPayError()
            }
        }
    }

    /**
     * 支付结果反馈
     */
    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    Log.d("tag", "msg:" + msg.obj)
                    val payResult = PayResult(msg.obj as String)
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo = payResult.getResult()// 同步返回需要验证的信息
                    val resultStatus = payResult.getResultStatus().toString()
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        BaseUtils.showProgressDialog(this@WXPayEntryActivity)
                        Handler().postDelayed(Runnable {
                            twoYanzhen()
                        }, 3000)
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        toPayError()

                    }
                }
                SDK_PAY_WECHAT -> {
//                    val req = msg.obj as PayReq
//                    api!!.sendReq(req)
                }
            }
        }
    }

    /**
     * 支付宝支付
     */
    private fun zhifubaoPay() {
        val authRunnable = Runnable {
            // 构造AuthTask 对象
            val payTask = PayTask(this@WXPayEntryActivity)
            // 调用支付接口
            val result = payTask.pay(aliPayInfo, true)

            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }

        // 必须异步调用
        val authThread = Thread(authRunnable)
        authThread.start()

    }

    /**
     * 微信支付
     */
    private fun weChatPay(wxPayBean: WxPayBean) {
        api = WXAPIFactory.createWXAPI(this, "wx159898ada9f8208a", false)
        api!!.registerApp("wx159898ada9f8208a")

        val payRunnable = Runnable {
            val req = PayReq()
            req.appId = wxPayBean.appid
            req.partnerId = wxPayBean.mch_id
            req.prepayId = wxPayBean.prepay_id
            req.nonceStr = wxPayBean.noncestr
            req.timeStamp = wxPayBean.timestamp
            req.packageValue = "Sign=WXPay"
            req.sign = wxPayBean.sign

//            val signParams = LinkedHashMap<String, String>()
//            signParams.put("appid", req.appId)
//            signParams.put("noncestr", req.nonceStr)
//            signParams.put("package", req.packageValue)
//            signParams.put("partnerid", req.partnerId)
//            signParams.put("prepayid", req.prepayId)
//            signParams.put("timestamp", req.timeStamp)
//            req.sign = genPackageSign(signParams, "Morgqa72FUBZVDz6TX6v7rD3G2mLrdXZ")
//            Log.d("sign","sign:"+genPackageSign(signParams, "Morgqa72FUBZVDz6TX6v7rD3G2mLrdXZ"))
            api!!.sendReq(req)//发送调起微信的请求
        }
        // 必须异步调用
        val payThread = Thread(payRunnable)
        payThread.start()
    }

    /**
     * 银联支付
     *
     * @param tn      流水号
     * @param payMode 支付模式 FORM：正式  TEST：测试
     */
    private fun pay(tn: String, payMode: PayMode) {
        if (tn != null) {
//            UPPayAssistEx.startPayByJAR(
//                this, PayActivity::class.java, null, null, tn,
//                if (payMode.equals(PayMode.TEST)) "01" else "00"
//            )

            val serverMode = "01"
            UPPayAssistEx.startPay(this, null, null, tn, serverMode)
        } else {
            Log.e("wxPayEntry", "tn is null please check you tn")
        }
    }

    enum class PayMode {
        TEST, FORM;
    }

    private fun unionPayRequest() {
        val params = HashMap<String, Any>()
        subscription = Network.getInstance("银联", applicationContext)
            .unionPay(
                params,
                ProgressSubscriber(
                    "银联",
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            Logger.e("", "生成订单数据失败：$error")
                            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    private fun testWxRequest() {
        //weixinPay
        val params = HashMap<String, Any>()
        params["ordNum"] = orderNum
        params["payType"] = 2
        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("支付宝生成订单数据", applicationContext)
            .orderPay(
                requestBody,
                ProgressSubscriberNew(
                    WxPayBean::class.java,
                    object : GsonSubscriberOnNextListener<WxPayBean> {
                        override fun on_post_entity(code: WxPayBean, get_message_id: String) {
                            //
                            weChatPay(code)
                        }
                    },
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            Logger.e("", "生成订单数据失败：$error")
                            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    private fun requestAliPayInfo(type: Int) {
        val params = HashMap<String, Any>()
        params["ordNum"] = orderNum
        params["payType"] = type
        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("支付宝生成订单数据", applicationContext)
            .orderPay(
                requestBody,
                ProgressSubscriberNew(
                    String::class.java,
                    object : GsonSubscriberOnNextListener<String> {
                        override fun on_post_entity(code: String, get_message_id: String) {
                            //
                            if (type == 1) {
                                aliPayInfo = code
                                zhifubaoPay()
                            }

                        }
                    },
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            Logger.e("", "生成订单数据失败：$error")
                            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )

    }


    private fun showAlert(ctx: Context, info: String) {
        showAlert(ctx, info, null)
    }

    private fun showAlert(ctx: Context, info: String, onDismiss: DialogInterface.OnDismissListener?) {
        androidx.appcompat.app.AlertDialog.Builder(ctx)
            .setMessage(info)
            .setPositiveButton(R.string.confirm, null)
            .setOnDismissListener(onDismiss)
            .show()
    }

    private fun showToast(ctx: Context, msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }

    private fun bundleToString(bundle: Bundle?): String {
        if (bundle == null) {
            return "null"
        }
        val sb = StringBuilder()
        for (key in bundle.keySet()) {
            sb.append(key).append("=>").append(bundle.get(key)).append("\n")
        }
        return sb.toString()
    }


    /**
     * 支付成功
     */
    var m_dialog: Dialog? = null

    private fun toPayOK() {

        PublicPopControl.alert_dialog_center(this) { view, popup ->
            val content = view.findViewById<TextView>(R.id.pop_content)
            val title = view.findViewById<TextView>(R.id.pop_title)
            val cancel = view.findViewById<TextView>(R.id.cancel_tv)
            val sure = view.findViewById<TextView>(R.id.sure_tv)
            cancel.text = "返回首页"
            sure.text = "查看订单"
            title.text = "支付成功！"
            content.setText(R.string.pay_ok)
            view.findViewById<LinearLayout>(R.id.cancel_btn)
                .setOnClickListener {
                    toMain()
                    popup.dismiss()
                }
            view.findViewById<LinearLayout>(R.id.sure_btn)
                .setOnClickListener {
                    this.finish()
                    val intent = Intent(this, OederDetailActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("order_id", orderNum)
                    bundle.putInt("from", 1002)
                    intent.putExtras(bundle)
                    startActivity(intent)

                    popup.dismiss()
                }
        }

    }

    private fun toPayError() {

        PublicPopControl.alert_dialog_center(this) { view, popup ->
            val content = view.findViewById<TextView>(R.id.pop_content)
            val title = view.findViewById<TextView>(R.id.pop_title)
            val cancel = view.findViewById<TextView>(R.id.cancel_tv)
            val sure = view.findViewById<TextView>(R.id.sure_tv)
            cancel.text = "离开界面"
            sure.text = "继续支付"
            title.text = "支付失败！"
            content.setText("小呦提醒您：\n" +" 支付异常！")
            view.findViewById<LinearLayout>(R.id.cancel_btn)
                .setOnClickListener { val intent = Intent(this, OederDetailActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("order_id", orderNum)
                    bundle.putInt("from", 1002)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    this.finish()
                    popup.dismiss()
                }
            view.findViewById<LinearLayout>(R.id.sure_btn)
                .setOnClickListener {

                    popup.dismiss()
                }
        }
    }

    private fun toPayBack() {

        PublicPopControl.alert_dialog_center(this) { view, popup ->
            val content = view.findViewById<TextView>(R.id.pop_content)
            val title = view.findViewById<TextView>(R.id.pop_title)
            val cancel = view.findViewById<TextView>(R.id.cancel_tv)
            val sure = view.findViewById<TextView>(R.id.sure_tv)
            cancel.text = "离开界面"
            sure.text = "继续支付"
            title.text = "确认返回页面？"
            content.setText("小呦提醒您：\n" +
                    "订单会帮您保留30分钟哦，锻炼这件事可是刻不容缓哟～\n" +
                    "逾期将会自动取消订单。")
            view.findViewById<LinearLayout>(R.id.cancel_btn)
                .setOnClickListener {
                    val intent = Intent(this, OederDetailActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("order_id", orderNum)
                    bundle.putInt("from", 1002)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    this.finish()
                    popup.dismiss()
                }
            view.findViewById<LinearLayout>(R.id.sure_btn)
                .setOnClickListener {
                    popup.dismiss()
                }
        }
    }

    override fun onBackPressed() {

        toPayBack()
//        super.onBackPressed()
    }


    /**
     * 二次验证
     */
    private fun twoYanzhen() {
        val params = HashMap<String, Any>()
        params["orderNum"] = orderNum
        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("支付二次验证", this)
            .getFinalPayResult(
                requestBody,
                ProgressSubscriber<OrderResultBean>(
                    "支付二次验证",
                    object : SubscriberOnNextListener<Bean<OrderResultBean>> {
                        override fun onNext(result: Bean<OrderResultBean>) {
                            BaseUtils.dismissProgressDialog()
                            if (result.data.order.status.toInt() == 1) {
                                EventBus.getDefault().post("购买了订单")
                                sp!!.play(music, 1f, 1f, 1, 0, 1f)
                                toPayOK()
                            } else {
                                toPayError()
                            }

                        }

                        override fun onError(error: String) {
                            Log.e(TAG, "支付二次验证：" + error)
                            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                            BaseUtils.dismissProgressDialog()
                        }
                    },
                    this,
                    false
                )
            )
    }

}