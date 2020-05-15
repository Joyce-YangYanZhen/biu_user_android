package com.noplugins.keepfit.userplatform.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.bean.QuanyiBean;

import java.util.ArrayList;

public class QuanyiAdapter extends BaseQuickAdapter<QuanyiBean.DataBean,BaseViewHolder> {
    private Context context;
    public QuanyiAdapter(@Nullable ArrayList<QuanyiBean.DataBean> data) {
        super(R.layout.item_coupon, data);
//        this.context = context;
    }



    @Override
    protected void convert(@NonNull BaseViewHolder helper, QuanyiBean.DataBean item) {

        helper.addOnClickListener(R.id.tv_confirm);


        if (item.getCoachServiceTimes() > 0){
            ((TextView)helper.getView(R.id.tv_coupon_name)).setText("私人教练服务卷");//名字
            if (item.getMemberRight() == -1 || item.getMemberRight() == 2){
                ((TextView)helper.getView(R.id.tv_confirm)).setText("确认");
                helper.getView(R.id.tv_confirm).setClickable(true);
            }
             else {
                ((TextView)helper.getView(R.id.tv_confirm)).setText("已确认");
                helper.getView(R.id.tv_confirm).setBackgroundResource(R.drawable.shape_btn_bg_longgray);
                helper.getView(R.id.tv_confirm).setClickable(false);
            }
            ((TextView)helper.getView(R.id.tv_time)).setText(""+item.getCoachServiceTimes());
            ((TextView)helper.getView(R.id.tv_time_tips)).setText("剩余次数");

        } else {
            ((TextView)helper.getView(R.id.tv_coupon_name)).setText("会籍服务时间卷");//名字
            if (item.getMemberRight() == -1 ||item.getMemberRight() == 1){
                ((TextView)helper.getView(R.id.tv_confirm)).setText("确认");
                helper.getView(R.id.tv_confirm).setClickable(true);
            }
            else {
                ((TextView)helper.getView(R.id.tv_confirm)).setText("已确认");
                helper.getView(R.id.tv_confirm).setBackgroundResource(R.drawable.shape_btn_bg_longgray);
                helper.getView(R.id.tv_confirm).setClickable(false);
            }


            ((TextView)helper.getView(R.id.tv_time)).setText(item.getEnddate());
            ((TextView)helper.getView(R.id.tv_time_tips)).setText("有效时间");
        }




        ((TextView)helper.getView(R.id.tv_changguan_name)).setText(item.getAreaName());




        if (!"".equals(item.getAreaLogo())){
            Glide.with(context).load("")
                    .placeholder(R.drawable.logo_gray)
                    .into((ImageView)helper.getView(R.id.iv_coupon_logo));
        }
     }

}
