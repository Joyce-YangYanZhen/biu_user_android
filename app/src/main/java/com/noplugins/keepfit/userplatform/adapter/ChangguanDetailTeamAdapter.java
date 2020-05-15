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
import com.noplugins.keepfit.userplatform.util.data.DateHelper;

import java.util.ArrayList;

public class ChangguanDetailTeamAdapter extends BaseQuickAdapter<ChangguanDetailBean.CourseListBean, BaseViewHolder> {

    public ChangguanDetailTeamAdapter(@Nullable ArrayList<ChangguanDetailBean.CourseListBean> data) {
        super(R.layout.item_find_changguan_team, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChangguanDetailBean.CourseListBean item) {


        helper.addOnClickListener(R.id.tv_team_pay)
        .addOnClickListener(R.id.rl_team);
        ((TextView) helper.getView(R.id.tv_team_name)).setText(item.getCourseName());
        ((TextView) helper.getView(R.id.tv_team_timeAndTeacher))
                .setText(item.getTeacherName()+"  "
                        + DateHelper.getDateByLong(item.getStartTime())
                        +"-"+DateHelper.getDateByLong(item.getEndTime()));
        ((TextView) helper.getView(R.id.tv_team_price)).setText("¥"+item.getFinalPrice());


        Glide.with(mContext).load(item.getImgUrl())
                .transform( new CenterCrop(mContext),new GlideRoundTransform(mContext,10))
                .error(R.drawable.logo_gray)
                .into((ImageView)helper.getView(R.id.iv_head));
    }

}
