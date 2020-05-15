package com.noplugins.keepfit.userplatform.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.bean.TaskListEntity;
import com.noplugins.keepfit.userplatform.util.ViewUtil;
import com.noplugins.keepfit.userplatform.viewholder.LeftListViewHolder;
import me.zhouzhuo.zzsecondarylinkage.adapter.LeftMenuBaseListAdapter;

import java.util.List;

/**
 * Created by zz on 2016/8/19.
 */
public class LeftMenuListAdapter extends LeftMenuBaseListAdapter<LeftListViewHolder, TaskListEntity.LeftMenuEntity> {

    public LeftMenuListAdapter(Context ctx, List<TaskListEntity.LeftMenuEntity> list) {
        super(ctx, list);
    }

    @Override
    public LeftListViewHolder getViewHolder() {
        return new LeftListViewHolder();
    }

    @Override
    public void bindView(LeftListViewHolder leftListViewHolder, View itemView) {
        ViewUtil.scaleContentView((ViewGroup) itemView.findViewById(R.id.root));
        leftListViewHolder.tvMacName = (TextView) itemView.findViewById(R.id.tv_menu);
        leftListViewHolder.tvMacId = (TextView) itemView.findViewById(R.id.tv_id);

    }

    @Override
    public void bindData(LeftListViewHolder leftListViewHolder, int position) {
        leftListViewHolder.tvMacName.setText(list.get(position).getMacName());
        leftListViewHolder.tvMacId.setText(list.get(position).getMacId());
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item_menu;
    }

    //9-patch drawable
    @Override
    public int getIndicatorResId() {
        return R.color.white;
    }
}
