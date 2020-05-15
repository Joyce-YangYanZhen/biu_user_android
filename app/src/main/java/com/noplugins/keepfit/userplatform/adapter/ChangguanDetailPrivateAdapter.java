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
import com.noplugins.keepfit.userplatform.bean.ChangguanDetailBean;
import com.noplugins.keepfit.userplatform.util.GlideRoundTransform;

import java.util.ArrayList;

public class ChangguanDetailPrivateAdapter extends BaseQuickAdapter<ChangguanDetailBean.TeacherListBean, BaseViewHolder> {

    public ChangguanDetailPrivateAdapter(@Nullable ArrayList<ChangguanDetailBean.TeacherListBean> data) {
        super(R.layout.item_find_changguan_private, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChangguanDetailBean.TeacherListBean item) {

//        ((TextView) helper.getView(R.id.tv_tag)).setText(item);
        ((TextView) helper.getView(R.id.tv_name)).setText(item.getTeacherName());
        ((TextView) helper.getView(R.id.tv_price)).setText("¥"+item.getLowestPrice()+"起");
        ((TextView) helper.getView(R.id.tv_class_time)).setText("累计上课"+item.getServiceDur()+"h");

        helper.addOnClickListener(R.id.ll_item);
        Glide.with(mContext).load(item.getLogoUrl())
                .transform(new CenterCrop(mContext),new GlideRoundTransform(mContext,10))
                .placeholder(R.drawable.logo_gray)
                .into((ImageView)helper.getView(R.id.iv_head));
    }

    @Override
    public int getItemCount() {
        if (this.mData.size() >3){
            return 3;
        }
        return mData.size();
    }
}
