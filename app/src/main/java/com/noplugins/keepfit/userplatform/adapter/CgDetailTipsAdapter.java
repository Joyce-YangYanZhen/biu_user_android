package com.noplugins.keepfit.userplatform.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.bean.ChangguanDetailBean;
import com.noplugins.keepfit.userplatform.util.BaseUtils;

import java.util.List;

public class CgDetailTipsAdapter extends BaseQuickAdapter<ChangguanDetailBean.AreaTimeBean.HighTimeListBean, BaseViewHolder> {

    public CgDetailTipsAdapter(@Nullable List<ChangguanDetailBean.AreaTimeBean.HighTimeListBean> data) {
        super(R.layout.item_cg_detail_price,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChangguanDetailBean.AreaTimeBean.HighTimeListBean item) {
        helper.setText(R.id.tv_high_time, BaseUtils.strSubEnd3(item.getHighTimeStart())+"-"+BaseUtils.strSubEnd3(item.getHighTimeEnd()));
        helper.setText(R.id.tv_high_price,"¥"+item.getFinalHighPrice());
    }
}
