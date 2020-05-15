package com.noplugins.keepfit.userplatform.activity.team

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.PrivateDetailBean
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_team_to_private.*
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class TeamToPrivateActivity : BaseActivity() {

    private var type = -1
    var selectItem = -1
    var logoUrl = ""
    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {
        setContentView(R.layout.activity_team_to_private)
        requestPrivateData()
    }

    override fun doBusiness(mContext: Context?) {
        share_btn.clickWithTrigger {
            takeScreenShot(this)
        }

        back_btn.clickWithTrigger {
            finish()
        }
        iv_collect.clickWithTrigger {
            requestCollect()
        }
     }

    private fun setting(code:PrivateDetailBean){
        if (code.teacherDetail.labelList!= null) {
            val layoutParams =
                ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(30, 0, 30, 30)
            for (i in 0 until code.teacherDetail.labelList .size){
                val paramItemView = layoutInflater.inflate(R.layout.adapter_search_histroy, zf_label,false)
                val keyWordTv = paramItemView.findViewById<TextView>(R.id.tv_content)
                keyWordTv.setPadding(35,5,35,5)
                keyWordTv.text = "# "+code.teacherDetail.labelList[i]
                zf_label.addView(paramItemView, layoutParams)
            }
        }

        if (code.teacherDetail.skillList!= null) {
            val layoutParams1 =
                ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams1.setMargins(30, 0, 30, 30)
            for (i in 0 until code.teacherDetail.skillList.size){
                val paramItemView = layoutInflater.inflate(R.layout.adapter_search_histroy, zf_skill,false)
                val keyWordTv = paramItemView.findViewById<TextView>(R.id.tv_content)
                keyWordTv.setPadding(35,5,35,5)
                keyWordTv.text = code.teacherDetail.skillList[i]
                zf_skill.addView(paramItemView, layoutParams1)
            }
        }



        tv_teacher_name.text = code.teacherDetail.teacherName
//        tv_teacher_sign.text = code.teacherDetail.card
        tv_sum_time.text = "累计服务时长：${code.teacherDetail.serviceDur}小时"
        tv_teacher_pinfen.text = "${code.teacherDetail.finalGrade}分"
        logoUrl = code.teacherDetail.logoUrl
        Glide.with(this).load(logoUrl)
            .placeholder(R.drawable.logo_gray)
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


    }
    private fun requestPrivateData(){

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
        dismiss_tv.clickWithTrigger { m_dialog!!.dismiss() }
        val cut_image = view.findViewById<ImageView>(R.id.cut_image)
        cut_image.setImageBitmap(bitmap_cut)
    }
}
