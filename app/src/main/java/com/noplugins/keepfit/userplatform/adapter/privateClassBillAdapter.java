package com.noplugins.keepfit.userplatform.adapter;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.bean.OrderBean;
import com.noplugins.keepfit.userplatform.util.GlideRoundTransform;

import java.util.List;

public class privateClassBillAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> {
    public privateClassBillAdapter(@Nullable List<OrderBean> data) {
        super(R.layout.item_private_bill,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OrderBean item) {
        Glide.with(mContext).load(item.getLogo())
                .transform( new CenterCrop(mContext), new GlideRoundTransform(mContext,10))
                .placeholder(R.drawable.logo_gray)
                .into((ImageView)helper.getView(R.id.iv_private_logo));
        ((TextView)helper.getView(R.id.tv_private_name)).setText(item.getCoachUserName());
        ((TextView)helper.getView(R.id.tv_class_name)).setText(item.getCourseName());
        ((TextView)helper.getView(R.id.tv_class_price)).setText("¥"+item.getPrice());
        ((TextView)helper.getView(R.id.tv_private_time)).
                setText(item.getStartTime()+"-"+item.getEndTime().split(" ")[1]);


    }

    @Override
    public int getItemCount() {
        return super.getItemCount()-1;
    }
}
