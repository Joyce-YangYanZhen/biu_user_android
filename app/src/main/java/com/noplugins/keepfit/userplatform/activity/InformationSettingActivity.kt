package com.noplugins.keepfit.userplatform.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import cn.jiguang.analytics.android.api.CountEvent
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.huantansheng.easyphotos.EasyPhotos
import com.noplugins.keepfit.userplatform.MainActivity
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.base.MyApplication
import com.noplugins.keepfit.userplatform.entity.QiNiuToken
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.GlideEngine
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.ui.ProgressUtil
import com.orhanobut.logger.Logger
import com.qiniu.android.storage.UpCompletionHandler
import com.qiniu.android.storage.UploadManager
import com.qiniu.android.storage.UploadOptions
import com.ycuwq.datepicker.date.HeightPickerDialogFragment
import com.ycuwq.datepicker.time.WeightPickerDialogFragment
import kotlinx.android.synthetic.main.activity_information_setting.*
import okhttp3.RequestBody
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class InformationSettingActivity:BaseActivity() {

    private var sex = -1 //性别

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
        setContentView(R.layout.activity_information_setting)
        /**七牛云**/
        uploadManager = MyApplication.uploadManager
        sdf = SimpleDateFormat("yyyyMMddHHmmss")
        qiniu_key = "icon_" + sdf!!.format(Date())

        Glide.with(this).load(SpUtils.getString(applicationContext,AppConstants.LOGO))
            .placeholder(R.drawable.logo_gray)
            .into(iv_head)
        getToken()
    }

    @SuppressLint("SetTextI18n")
    override fun doBusiness(mContext: Context?) {
        tv_jump.clickWithTrigger {
            //jump to main
            //搜索埋点
            val cEvent = CountEvent("跳过完善个人资料")
            cEvent.addKeyValue(
                "user",
                SpUtils.getString(applicationContext, AppConstants.USER_NAME)
            )
            ///////////////////////////////
            toMain()
        }
        iv_head.clickWithTrigger {
            //获取图片
            EasyPhotos.createAlbum(this@InformationSettingActivity,
                true, GlideEngine.getInstance())
                .setFileProviderAuthority("com.noplugins.keepfit.userplatform.fileprovider")
                .setPuzzleMenu(false)
                .setCount(1)
                .setOriginalMenu(false, true, null)
                .start(102)
        }
        rg_man.clickWithTrigger {
            sex = 1
        }

        rg_woman.clickWithTrigger {
            sex = 0
        }
        tv_height.clickWithTrigger {
            //
            val heightPickerDialogFragment = HeightPickerDialogFragment(100,200,160," cm")
            heightPickerDialogFragment.setOnDateChooseListener {
                tv_height.text = "$it"
            }

            heightPickerDialogFragment.show(supportFragmentManager,"HeightPickerDialogFragment")

        }
        tv_weight.clickWithTrigger {
            val heightPickerDialogFragment = HeightPickerDialogFragment(20,200,60," kg")
            heightPickerDialogFragment.setOnDateChooseListener {
                //专门写了的选择到小数点的体重选择  不要了 呜呜呜呜
                tv_weight.text = "$it"
            }

            heightPickerDialogFragment.show(supportFragmentManager,"WeightPickerDialogFragment")
            //
        }
        login_btn.clickWithTrigger {
//            toNext()

            //搜索埋点
            val cEvent = CountEvent("完善个人资料")
            cEvent.addKeyValue(
                "user",
                SpUtils.getString(applicationContext, AppConstants.USER_NAME)
            )
            ///////////////////////////////
            submit()
        }

    }

    /**
     * 提交个人信息
     */
    private fun submit(){


//        if (edit_user_nicheng.text.toString() == ""){
//            Toast.makeText(applicationContext, "昵称不能为空！", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (sex == -1){
//            Toast.makeText(applicationContext, "请选择性别！", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (tv_height.text.toString() == ""){
//            Toast.makeText(applicationContext, "身高不能为空哦！", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (tv_weight.text.toString() == ""){
//            Toast.makeText(applicationContext, "体重不能为空哦！", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (edit_user_nicheng.text.toString() == "" || tv_height.text.toString() == "" ||
//            tv_weight.text.toString() == "" ||sex == -1 ){
//            toNext()
//            return
//        }

        if (edit_user_nicheng.text.toString() != "" ){
            if (!compileExChar(edit_user_nicheng.text.toString())){
                Toast.makeText(applicationContext,"昵称不能包含特殊字符", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val params = HashMap<String, Any>()
        params["userNum"] = SpUtils.getString(applicationContext,AppConstants.USER_NAME)
        params["qiniuKey"] = icon_net_path
        if (edit_user_nicheng.text.toString()!=""){
            params["nickname"] = edit_user_nicheng.text.toString()
        }

        if (sex!=-1){
            params["sex"] = sex
        }

        if (tv_weight.text.toString() != ""){
            params["weight"] = tv_weight.text.toString().toInt()
        }
        if (tv_height.text.toString() != ""){
            params["stature"] = tv_height.text.toString().toInt()
        }



        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("完善资料", applicationContext)
            .completeData(requestBody,
                ProgressSubscriberNew(Any::class.java, object : GsonSubscriberOnNextListener<Any> {
                    override fun on_post_entity(code: Any, get_message_id: String) {
                        //
//                        Toast.makeText(applicationContext, "ok！", Toast.LENGTH_SHORT).show()
                        toNext()
                    }
                }, object : SubscriberOnNextListener<Bean<Any>> {
                    override fun onNext(result: Bean<Any>) {

                    }

                    override fun onError(error: String) {
                        Logger.e(TAG, "接收验证码报错：$error")
                        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                    }
                }, this, true)
            )
    }

    private fun toNext(){
        val intent = if (SpUtils.getInt(applicationContext,AppConstants.RIGHT) == 1) {
            Intent(
                this@InformationSettingActivity,
                QuanyiConfirmActivity::class.java)
        } else {
            Intent(
                this@InformationSettingActivity,
                MainActivity::class.java)
        }
        startActivity(intent)
        finish()
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
    private fun uploadQiniuColud(){
        /**七牛云**/
        progress_upload = ProgressUtil()
        progress_upload!!.showProgressDialog(this@InformationSettingActivity, "载入中...")
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
                } else {
                    Log.e("qiniu", "Upload Fail")
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                }
                progress_upload!!.dismissProgressDialog()
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
                Glide.with(this@InformationSettingActivity).load(icon_iamge_file).into(iv_head)
            }
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        toMain()
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
                    Log.d("Luban","图片压缩失败，将上传原图")
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

    private fun compileExChar(str:String):Boolean{
        val limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
        val pattern = Pattern.compile(limitEx)
        val m = pattern.matcher(str)
        if( m.find()){
            Toast.makeText(this, "不允许输入特殊符号！", Toast.LENGTH_LONG).show()
            return false
        }
        if (str.length < 2||str.length>16){
            Toast.makeText(this, "昵称长度不能小于2个字符且不能大于16个字符！", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}