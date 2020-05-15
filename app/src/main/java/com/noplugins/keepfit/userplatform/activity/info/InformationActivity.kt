package com.noplugins.keepfit.userplatform.activity.info

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.gson.Gson
import com.huantansheng.easyphotos.EasyPhotos
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.base.MyApplication
import com.noplugins.keepfit.userplatform.bean.InformationBean
import com.noplugins.keepfit.userplatform.entity.QiNiuToken
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.GlideEngine
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.ui.ProgressUtil
import com.qiniu.android.storage.UpCompletionHandler
import com.qiniu.android.storage.UploadManager
import com.qiniu.android.storage.UploadOptions
import com.ycuwq.datepicker.date.DatePickerDialogFragment
import kotlinx.android.synthetic.main.activity_information.*
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class InformationActivity : BaseActivity() {

    /**
     * 七牛云
     */
    //指定upToken, 强烈建议从服务端提供get请求获取
    private var uptoken = "xxxxxxxxx:xxxxxxx:xxxxxxxxxx"
    private var sdf: SimpleDateFormat? = null
    private var qiniu_key: String? = null
    private var uploadManager: UploadManager? = null

    private var icon_image_path: String? = null

    private var progress_upload: ProgressUtil? = null

    private var icon_net_path = ""//需要有一个默认的头像
    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {
        setContentView(R.layout.activity_information)
//        Glide.with(this).load(SpUtils.getString(applicationContext,AppConstants.LOGO))
//            .placeholder(R.drawable.logo_gray)
//            .into(iv_logo)
        /**七牛云**/
        uploadManager = MyApplication.uploadManager
        sdf = SimpleDateFormat("yyyyMMddHHmmss")
        qiniu_key = "icon_" + sdf!!.format(Date())
        getToken()
        getInfoData()
    }

    override fun doBusiness(mContext: Context?) {

        back_btn.clickWithTrigger {
            finish()
        }

        iv_logo.clickWithTrigger {
            logoDialog()
        }
        rl_user_name.clickWithTrigger {
            val intent = Intent(this, InformationNameUpdateActivity::class.java)
            intent.putExtra("nickname", name)
            startActivity(intent)
        }
        rl_sex.clickWithTrigger {
            sexDialog()
        }

        rl_birthday.clickWithTrigger {
            val _day = DatePickerDialogFragment()
            _day.setOnDateChooseListener { _year, _month, _day ->
                var year = _year
                var month = if (_month > 9) {
                    "$_month"
                } else {
                    "0$_month"
                }

                var day = if (_day > 9) {
                    "$_day"
                } else {
                    "0$_day"
                }
                tv_birthday.setText("$_year-$month-$day")

                if (tv_birthday.text.toString() != birthday) {
                    updateInfo()
                }
            }
            _day.show(supportFragmentManager, "DatePickerDialogFragment")
        }
    }

    var m_dialog: Dialog? = null
    private fun sexDialog() {
        val factory = LayoutInflater.from(this)
        val view = factory.inflate(R.layout.dialog_sex_select, null)
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
        val tv_man = view.findViewById<TextView>(R.id.tv_man)
        val tv_woman = view.findViewById<TextView>(R.id.tv_woman)
        val cancel_tv = view.findViewById<TextView>(R.id.cancel_tv)
        val sure_tv = view.findViewById<TextView>(R.id.sure_tv)

        if (sex == 1) {
            tv_man.setTextColor(Color.parseColor("#76CEE1"))
            tv_woman.setTextColor(Color.parseColor("#929292"))
        } else {
            tv_woman.setTextColor(Color.parseColor("#76CEE1"))
            tv_man.setTextColor(Color.parseColor("#929292"))
        }
        cancel_tv.clickWithTrigger {
            m_dialog!!.dismiss()
        }
        sure_tv.clickWithTrigger {
            m_dialog!!.dismiss()
        }
        tv_man.clickWithTrigger {

            if (sex == 0) {
                sex = 1
                tv_sex.text = "男"
                tv_man.setTextColor(Color.parseColor("#76CEE1"))
                tv_woman.setTextColor(Color.parseColor("#929292"))
                updateInfo()
            }


            m_dialog!!.dismiss()

        }

        tv_woman.clickWithTrigger {
            if (sex == 1) {
                tv_sex.text = "女"
                sex = 0
                tv_woman.setTextColor(Color.parseColor("#76CEE1"))
                tv_man.setTextColor(Color.parseColor("#929292"))
                updateInfo()
            }
            m_dialog!!.dismiss()

        }
    }

    private fun logoDialog() {
        val factory = LayoutInflater.from(this)
        val view = factory.inflate(R.layout.dialog_logo_select, null)
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
        val cancel_tv = view.findViewById<TextView>(R.id.cancel_tv)
        val sure_tv = view.findViewById<TextView>(R.id.sure_tv)
        cancel_tv.clickWithTrigger {
            m_dialog!!.dismiss()
        }
        sure_tv.clickWithTrigger {
            m_dialog!!.dismiss()
        }

        val tv_paizhao = view.findViewById<TextView>(R.id.tv_paizhao)
        tv_paizhao.clickWithTrigger {
            EasyPhotos.createCamera(this)
                .setFileProviderAuthority("com.noplugins.keepfit.userplatform.fileprovider")
                .setPuzzleMenu(false)
                .setOriginalMenu(false, true, null)
                .start(102)
            m_dialog!!.dismiss()
        }
        val tv_xiangce = view.findViewById<TextView>(R.id.tv_xiangce)
        tv_xiangce.clickWithTrigger {
            m_dialog!!.dismiss()
            //获取图片
            EasyPhotos.createAlbum(
                this,
                true, GlideEngine.getInstance()
            )
                .setFileProviderAuthority("com.noplugins.keepfit.userplatform.fileprovider")
                .setPuzzleMenu(false)
                .setCount(1)
                .setOriginalMenu(false, true, null)
                .start(102)

        }


    }


    private fun getInfoData() {
        val params = HashMap<String, Any>()
        params["userNum"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("获取个人信息", this)
            .personalData(
                requestBody,
                ProgressSubscriber<InformationBean>(
                    "获取个人信息",
                    object : SubscriberOnNextListener<Bean<InformationBean>> {
                        override fun onNext(result: Bean<InformationBean>) {
                            setting(result.data)
                        }

                        override fun onError(error: String) {
                            Log.e(TAG, "获取个人信息：" + error)
                            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    var name = ""
    var sex = -1
    var birthday = ""

    private fun setting(info: InformationBean) {
        Glide.with(this).load(info.logo)
            .asBitmap()
            .centerCrop()
            .override(100, 100)
            .dontAnimate()
            .skipMemoryCache(true)
            .into(object : BitmapImageViewTarget(iv_logo) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource)
                    circularBitmapDrawable.setCircular(true)
                    iv_logo.setImageDrawable(circularBitmapDrawable)
                }
            })

        if (info.nickname != null) {
            if (info.nickname.length > 11) {
                tv_user_name.text = info.nickname.substring(0, 10) + "..."
            } else {
                tv_user_name.setText(info.nickname)
            }
        }

        name = info.nickname
        sex = info.sex
        tv_sex.text = if (info.sex == 1) {
            "男"
        } else {
            "女"
        }
        if (info.birthday != null) {
            birthday = info.birthday
            tv_birthday.text = info.birthday
        }

    }

    private fun updateInfo() {
        val params = HashMap<String, Any>()
        params["userNum"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        if (icon_net_path != "") {
            params["logo"] = icon_net_path
        }

        if (sex != -1) {
            params["sex"] = sex
        }
        if (tv_birthday.text != "" && birthday != tv_birthday.text.toString().trim()) {
            params["birthday"] = tv_birthday.text
        }


        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("修改资料", applicationContext)
            .setPersonalData(
                requestBody,
                ProgressSubscriber<Any>(
                    "修改资料",
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {
                            EventBus.getDefault().post("修改了个人信息")
                            if (icon_net_path!=""){
                                Glide.with(this@InformationActivity).load(icon_image_path)
                                    .asBitmap()
                                    .centerCrop()
                                    .override(100, 100)
                                    .dontAnimate()
                                    .skipMemoryCache(true)
                                    .into(object : BitmapImageViewTarget(iv_logo) {
                                        override fun setResource(resource: Bitmap?) {
                                            val circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(getResources(), resource)
                                            circularBitmapDrawable.setCircular(true)
                                            iv_logo.setImageDrawable(circularBitmapDrawable)
                                        }
                                    })
                            }
                            icon_net_path = ""
//                            SpUtils.putString(applicationContext, AppConstants.LOGO, code.logo)
                            Toast.makeText(applicationContext, "修改成功", Toast.LENGTH_SHORT).show()
//                            getInfoData()
                        }

                        override fun onError(error: String) {
                            Log.e(TAG, "获取个人信息：" + error)
                            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    /**
     * 获取token
     */
    private fun getToken() {
        subscription = Network.getInstance("七牛云token", this)
            .get_qiniu_token(
                HashMap(),
                ProgressSubscriberNew(QiNiuToken::class.java, object : GsonSubscriberOnNextListener<QiNiuToken> {
                    override fun on_post_entity(qiNiuToken: QiNiuToken, s: String) {
                        Log.e("获取到的token", "获取到的token" + qiNiuToken.getToken())
                        uptoken = qiNiuToken.getToken()

                    }
                }, object : SubscriberOnNextListener<Bean<Any>> {
                    override fun onNext(result: Bean<Any>) {}

                    override fun onError(error: String) {
                        Log.e("获取到的token失败", error)
                    }
                }, this, false)
            )
    }

    /**
     * 上传到七牛云
     */
    private fun uploadQiniuColud() {
        /**七牛云**/
        progress_upload = ProgressUtil()
//        progress_upload!!.showProgressDialog(this@InformationActivity, "载入中...")
        //上传icon
        uploadManager!!.put(
            icon_image_path, qiniu_key, uptoken,
            UpCompletionHandler { key, info, response ->
                //res包含hash、key等信息，具体字段取决于上传策略的设置
                if (info.isOK) {
                    Log.e("qiniu", "Upload Success")
                    icon_net_path = key
                    Log.e("打印key：", icon_net_path)
                    //测试资料上传的
                    //getUrlTest(icon_net_path);
                    val headpicPath = "http://upload.qiniup.com/$key"
                    Log.e("返回的地址", headpicPath)
                    progress_upload!!.dismissProgressDialog()

                    updateInfo()
                } else {
                    Log.e("qiniu", "Upload Fail")
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                }
                //Log.e("qiniu", key + ",\r\n " + info.path + ",\r\n " + response);
            }, UploadOptions(null, "test-type", true, null, null)
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {

        } else {
            //添加icon,上传icon
            if (data == null) return
            val resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS)
            if (resultPaths!!.size > 0) {
//                icon_image_path = resultPaths[0]
                val icon_iamge_file = File(resultPaths[0])
                withLs(icon_iamge_file)

            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        getInfoData()
    }

    private fun withLs(photos: File) {
        Luban.with(this)
            .load(photos)
            .ignoreBy(100)
            .setTargetDir(getPath())
            .setFocusAlpha(false)
            .setCompressListener(object : OnCompressListener {
                override fun onSuccess(file: File?) {

                    icon_image_path = file!!.path
                    uploadQiniuColud()
                }

                override fun onError(e: Throwable?) {
                    Log.d("Luban", "图片压缩失败，将上传原图")
                    icon_image_path = photos.path
                    uploadQiniuColud()
                }

                override fun onStart() {
                }

            })
            .launch()
    }

    private fun getPath(): String {
        val path = "${Environment.getExternalStorageDirectory()}/Luban/image/"
        val file = File(path)
        if (file.mkdirs()) {
            return path
        }
        return path
    }

}
