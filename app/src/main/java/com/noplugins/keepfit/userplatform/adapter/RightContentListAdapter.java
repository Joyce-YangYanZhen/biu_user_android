package com.noplugins.keepfit.userplatform.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.bean.TaskListEntity;
import com.noplugins.keepfit.userplatform.util.ViewUtil;
import com.noplugins.keepfit.userplatform.viewholder.RightListViewHolder;
import me.zhouzhuo.zzsecondarylinkage.adapter.RightMenuBaseListAdapter;

import java.util.List;


/**
 * Created by zz on 2016/8/20.
 */
public class RightContentListAdapter extends RightMenuBaseListAdapter<RightListViewHolder, TaskListEntity.LeftMenuEntity.TaskListDataEntity> {

    public RightContentListAdapter(Context ctx, List<TaskListEntity.LeftMenuEntity.TaskListDataEntity> list) {
        super(ctx, list);
    }

    @Override
    public RightListViewHolder getViewHolder() {
        return new RightListViewHolder();
    }

    @Override
    public void bindView(RightListViewHolder rightListViewHolder, View itemView) {
        ViewUtil.scaleContentView((ViewGroup) itemView.findViewById(R.id.root));
        rightListViewHolder.ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
        rightListViewHolder.tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
        rightListViewHolder.tvMacName = (TextView) itemView.findViewById(R.id.mac_name);
        rightListViewHolder.tvTaskNum = (TextView) itemView.findViewById(R.id.tv_task_number);
        rightListViewHolder.tvTaskId = (TextView) itemView.findViewById(R.id.tv_task_id);
        rightListViewHolder.tvStartTime = (TextView) itemView.findViewById(R.id.tv_start_time);
    }

    @Override
    public void bindData(RightListViewHolder rightListViewHolder, int position) {
//        Glide.with(context).load(Constants.BASE_URL + getItem(position).getPicUrl()).into(rightListViewHolder.ivPic);
        rightListViewHolder.tvProductName.setText(getItem(position).getProductName());
        rightListViewHolder.tvMacName.setText(getItem(position).getMachineName());
        rightListViewHolder.tvTaskNum.setText(getItem(position).getTaskNo());
        rightListViewHolder.tvTaskId.setText(getItem(position).getTaskId());
        rightListViewHolder.tvStartTime.setText(getItem(position).getStartTime());
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item_content;
    }
}
