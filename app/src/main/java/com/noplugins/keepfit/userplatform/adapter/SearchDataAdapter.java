package com.noplugins.keepfit.userplatform.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.activity.changguan.ChaungguanDetailActivity;
import com.noplugins.keepfit.userplatform.activity.teahcher.PrivateDetailActivity;
import com.noplugins.keepfit.userplatform.activity.team.TeamDetailActivity;
import com.noplugins.keepfit.userplatform.bean.SearchBean;

import java.util.List;


public class SearchDataAdapter extends BaseMultiItemQuickAdapter<SearchBean.AreaBean, BaseViewHolder> {


    private Context context;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SearchDataAdapter(List<SearchBean.AreaBean> data) {
        super(data);
        addItemType(1,R.layout.item_search_cg);
        addItemType(2,R.layout.item_search_private);
        addItemType(3,R.layout.item_search_team);
        context = mContext;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchBean.AreaBean item) {
        switch (helper.getItemViewType()){
            case 1:
                helper.setText(R.id.tv_title,item.getAreaName());
                helper.setText(R.id.tv_address,item.getAddress());
                helper.setText(R.id.tv_juli,""+item.getDistance()+"km");
                helper.addOnClickListener(R.id.ll_cg);

                break;
            case 2:
                Glide.with(mContext).load(item.getLogoUrl()).placeholder(R.mipmap.ic_launcher)
                        .placeholder(R.drawable.logo_gray)
                        .into((ImageView) helper.getView(R.id.iv_logo));
                helper.setText(R.id.tv_address,item.getTeacherName());
                helper.setText(R.id.tv_juli,"累计服务时长："+item.getServiceDur()+"h");
                helper.addOnClickListener(R.id.ll_private);

                break;
            case 3:
                helper.setText(R.id.tv_title,item.getCourseName());
                helper.setText(R.id.tv_address,item.getAreaName());
                helper.setText(R.id.tv_juli,""+item.getDistance()+"km");
                helper.addOnClickListener(R.id.ll_team);
                break;
        }
    }




}

