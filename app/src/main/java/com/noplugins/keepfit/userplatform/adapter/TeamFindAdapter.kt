package com.noplugins.keepfit.userplatform.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.bean.TeamBean
import com.noplugins.keepfit.userplatform.util.GlideRoundTransform
import com.noplugins.keepfit.userplatform.util.data.DateHelper
import java.util.*

class TeamFindAdapter(data: ArrayList<TeamBean.CourseListBean>?) :
    BaseQuickAdapter<TeamBean.CourseListBean, BaseViewHolder>(R.layout.item_find_team, data) {


    override fun convert(helper: BaseViewHolder, item: TeamBean.CourseListBean) {

        (helper.getView<View>(R.id.tv_private_name) as TextView).text = item.courseName
       if (null!=item.areaName){
           if (item.areaName.length>6){
               (helper.getView<View>(R.id.tv_class_cg) as TextView).text =item.areaName.substring(0,5)+"..."
           } else {
               (helper.getView<View>(R.id.tv_class_cg) as TextView).text =item.areaName
           }
       }

//        (helper.getView<View>(R.id.tv_class_cg) as TextView).text =item.areaName

        (helper.getView<View>(R.id.tv_juli) as TextView).text = ""+item.distance+"km"
        (helper.getView<View>(R.id.tv_price) as TextView).text = "¥"+item.finalPrice

        if (item.applyNum  == item.maxNum){

            (helper.getView<View>(R.id.tv_private_pinfen) as TextView).text = "满员"
            (helper.getView<View>(R.id.tv_pay) as TextView).setBackgroundResource(R.drawable.shape_btn_bg_gray)
            (helper.getView<View>(R.id.tv_pay) as TextView).isClickable = false
        } else {
            (helper.getView<View>(R.id.tv_pay) as TextView).setBackgroundResource(R.drawable.shape_btn_bg_40)
            (helper.getView<View>(R.id.tv_private_pinfen) as TextView).text = ""+item.applyNum+"/"+item.maxNum
        }

        (helper.getView<View>(R.id.tv_class_teacher) as TextView).text = item.teacherName
        (helper.getView<View>(R.id.tv_class_time) as TextView).text =
            DateHelper.getDateByLong(item.startTime)+"-"+DateHelper.getDateByLong(item.endTime)


        Glide.with(mContext).load(item.imgUrl)
            .transform( CenterCrop(mContext), GlideRoundTransform(mContext,10))
            .placeholder(R.drawable.logo_gray)
            .into(helper.getView<View>(R.id.iv_private_logo) as ImageView)


        helper.addOnClickListener(R.id.tv_pay)
            .addOnClickListener(R.id.ll_team_item)

    }

}
