package com.noplugins.keepfit.userplatform.adapter;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.bean.PrivateDetailBean;

import java.util.List;

public class PrivateDetailClassAdapter extends BaseQuickAdapter<PrivateDetailBean.CourseListBean, BaseViewHolder> {

    public PrivateDetailClassAdapter(@Nullable List<PrivateDetailBean.CourseListBean> data) {
        super(R.layout.item_private_class,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PrivateDetailBean.CourseListBean item) {

        helper.addOnClickListener(R.id.cb_class_select)
                .addOnClickListener(R.id.iv_class_time)
                .addOnClickListener(R.id.tv_class_look);
        ((TextView)helper.getView(R.id.tv_class_name)).setText(item.getCourseName()+" - ¥"+item.getFinalPrice()+"/h");


    }
}
