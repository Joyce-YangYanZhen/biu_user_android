package com.noplugins.keepfit.userplatform.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.bean.PrivateBean
import com.noplugins.keepfit.userplatform.util.GlideRoundTransform
import com.noplugins.keepfit.userplatform.util.ui.ZFlowLayout
import java.util.*

class PrivateFindAdapter(data: ArrayList<PrivateBean.GenTeacherListBean>?) :
    BaseQuickAdapter<PrivateBean.GenTeacherListBean, BaseViewHolder>(R.layout.item_find_private, data) {


    override fun convert(helper: BaseViewHolder, item: PrivateBean.GenTeacherListBean) {

        (helper.getView<View>(R.id.tv_private_name) as TextView).text = item.teacherName


        (helper.getView<View>(R.id.tv_price) as TextView).text = "¥" + item.lowestPrice
        (helper.getView<View>(R.id.tv_juli) as TextView).text = "${item.distance}km"
        (helper.getView<View>(R.id.tv_private_pinfen) as TextView).text = "${item.finalGrade}分"
        (helper.getView<View>(R.id.tv_private_time) as TextView).text = "累计服务时长：" + item.serviceDur + "h"

        if (item.collect == 1) {
            helper.getView<View>(R.id.iv_shoucang).visibility = View.VISIBLE
        } else {
            helper.getView<View>(R.id.iv_shoucang).visibility = View.GONE
        }

        Glide.with(mContext).load(item.logoUrl)
            .transform(CenterCrop(mContext), GlideRoundTransform(mContext, 10))
            .placeholder(R.drawable.logo_gray)
            .into(helper.getView<View>(R.id.iv_private_logo) as ImageView)

        val skill = (helper.getView<ZFlowLayout>(R.id.fl_private_skill))


        val layoutParams =
            ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(10, 5, 10, 5)
        skill.removeAllViews()
        if (item.skillList != null) {
            val arr = item.skillList
            for (i in 0 until arr.size) {
                val paramItemView =
                    (mContext as Activity).layoutInflater.inflate(R.layout.adapter_search_histroy, skill, false)
                val keyWordTv = paramItemView.findViewById<TextView>(R.id.tv_content)
                keyWordTv.setPadding(15, 5, 15, 5)
                keyWordTv.text = arr[i]
                skill.addView(paramItemView, layoutParams)
            }
        }


    }

}
