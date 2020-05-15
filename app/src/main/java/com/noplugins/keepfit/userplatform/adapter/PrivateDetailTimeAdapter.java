package com.noplugins.keepfit.userplatform.adapter;

import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.bean.TeacherTimeBean;

import java.util.ArrayList;

public class PrivateDetailTimeAdapter extends BaseQuickAdapter<TeacherTimeBean.MyBean, BaseViewHolder> {
    public PrivateDetailTimeAdapter(@Nullable ArrayList<TeacherTimeBean.MyBean> data) {

        super(R.layout.item_find_private_features, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, TeacherTimeBean.MyBean item) {

        ((TextView) helper.getView(R.id.tv_tag)).setText(item.getTimeDec());
        if (item.getDeleted() == 1){
            ((TextView) helper.getView(R.id.tv_tag)).setClickable(false);
            ((TextView) helper.getView(R.id.tv_tag)).setBackgroundResource(R.drawable.shape_btn_bg);
        } else {
            if (getData().size()> 0 &&getData().get(helper.getLayoutPosition()).isClicks()){
                ((TextView) helper.getView(R.id.tv_tag)).setBackgroundResource(R.drawable.shape_btn_bg);
            } else {
                ((TextView) helper.getView(R.id.tv_tag)).setBackgroundResource(R.drawable.shape_btn_bg_gray);
            }
            Log.d("Add","is::"+item.isClicks());
        }
        helper.addOnClickListener(R.id.tv_tag);
//        Glide.with(context).load(item.getAreaLogo()).into((ImageView)helper.getView(R.id.iv_coupon_logo));
    }

}
