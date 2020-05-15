package com.noplugins.keepfit.userplatform.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.bean.ChangguanBean;
import com.noplugins.keepfit.userplatform.util.GlideRoundTransform;

import java.util.ArrayList;

public class ChangguanFindAdapter extends BaseQuickAdapter<ChangguanBean.ChangguanEntity,BaseViewHolder> {
    private int type;
    public ChangguanFindAdapter(@Nullable ArrayList<ChangguanBean.ChangguanEntity> data) {
        super(R.layout.item_find_changguan, data);
//        this.context = context;
    }

    public ChangguanFindAdapter(@Nullable ArrayList<ChangguanBean.ChangguanEntity> data,int type) {
        super(R.layout.item_find_changguan, data);
//        this.context = context;
        this.type = type;
    }



    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChangguanBean.ChangguanEntity item) {

        helper.addOnClickListener(R.id.tv_select)
        .addOnClickListener(R.id.ll_cg_item);

//        if (item.getAreaName().length()<=14){
//            ((TextView)helper.getView(R.id.tv_changguan_name)).setText(item.getAreaName());
//        } else {
//            ((TextView)helper.getView(R.id.tv_changguan_name)).setText(item.getAreaName().substring(0,13)+"...");
//
//        }
        ((TextView)helper.getView(R.id.tv_changguan_name)).setText(item.getAreaName());
        ((TextView)helper.getView(R.id.tv_juli)).setText(""+item.getDistance()+"km");
        ((TextView)helper.getView(R.id.tv_changguan_address)).setText(item.getAddress());

        ((TextView)helper.getView(R.id.tv_price)).setText("¥"+item.getNormalPrice());
        ((TextView)helper.getView(R.id.tv_chakan_num)).setText(""+item.getClickNum());
        ((TextView)helper.getView(R.id.pingfen_tv)).setText(""+item.getFinalGradle()+"分");

        helper.getView(R.id.iv_zuji).setVisibility(View.GONE);

        if (type == 2){
            //私教跳转的列表
            ((TextView)helper.getView(R.id.tv_select)).setVisibility(View.VISIBLE);
            helper.getView(R.id.ll_price).setVisibility(View.INVISIBLE);
            if (item.getAddress().length() > 10){
                ((TextView)helper.getView(R.id.tv_changguan_address)).setText(item.getAddress().substring(0,9)+"...");
            } else {
                ((TextView)helper.getView(R.id.tv_changguan_address)).setText(item.getAddress());
            }

        } else {
            //首页的列表
            helper.getView(R.id.ll_price).setVisibility(View.VISIBLE);
        }

        if (item.getCollect() == 1){
            helper.getView(R.id.iv_shoucang).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_shoucang).setVisibility(View.GONE);
        }




        Glide.with(mContext).load(item.getLogo())
                .transform( new CenterCrop(mContext),new GlideRoundTransform(mContext,10))
//                .placeholder(R.mipmap.loading)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.logo_gray)
                .into((ImageView)helper.getView(R.id.iv_changguan_logo));
    }

}
