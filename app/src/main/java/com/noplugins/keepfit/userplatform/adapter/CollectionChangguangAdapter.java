package com.noplugins.keepfit.userplatform.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.noplugins.keepfit.userplatform.MainActivity;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.entity.CollectionChangGuanEntity;
import com.noplugins.keepfit.userplatform.util.ui.CustomRoundAngleImageView;

import java.util.List;

public class CollectionChangguangAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<CollectionChangGuanEntity.AreaListBean> list;
    private Activity context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_YOUTANG = 1;


    public CollectionChangguangAdapter(List<CollectionChangGuanEntity.AreaListBean> mlist, Activity mcontext) {
        list = mlist;
        context = mcontext;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {

        YouYangViewHolder youYangViewHolder = new YouYangViewHolder(view, false);
        return youYangViewHolder;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder holder = getViewHolderByViewType(viewType, parent);
        return holder;
    }

    RecyclerView.ViewHolder holder = null;

    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
        View item_view = null;
        if (viewType == EMPTY_VIEW) {
            item_view = LayoutInflater.from(context).inflate(R.layout.changguan_empty_view, parent, false);
            holder = new EmptyViewHolder(item_view, false);
        } else if (viewType == TYPE_YOUTANG) {
            item_view = LayoutInflater.from(context).inflate(R.layout.item_find_changguan, parent, false);
            holder = new YouYangViewHolder(item_view, true);
        }


        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position, boolean isItem) {
        if (view_holder instanceof YouYangViewHolder) {
            YouYangViewHolder holder = (YouYangViewHolder) view_holder;
            CollectionChangGuanEntity.AreaListBean areaListBean = list.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            holder.iv_shoucang.setVisibility(View.VISIBLE);
            holder.pingfen_tv.setText(areaListBean.getFinalGradle() + "分");
            holder.tv_changguan_name.setText(areaListBean.getAreaName());
            holder.tv_changguan_address.setText(areaListBean.getAddress());
            holder.tv_chakan_num.setText(areaListBean.getClickNum() + "");
            holder.tv_juli.setText(areaListBean.getDistance() + "km");
            Glide.with(context).load(areaListBean.getLogo())
                    .placeholder(R.drawable.logo_gray)
                    .into(holder.iv_changguan_logo);

        } else if (view_holder instanceof EmptyViewHolder) {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) view_holder;
            emptyViewHolder.qushouye_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("flag", 0);
                    context.startActivity(intent);
                }
            });
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (list.size() == 0) {
            return EMPTY_VIEW;
        } else {
            return TYPE_YOUTANG;
        }
    }


    @Override
    public int getAdapterItemCount() {
        return list.size() > 0 ? list.size() : 1;
    }


    public void setData(List<CollectionChangGuanEntity.AreaListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public LinearLayout qushouye_btn;

        public EmptyViewHolder(View item_view, boolean isItem) {
            super(item_view);
            if (isItem) {

            } else {
                this.view = item_view;
                qushouye_btn = view.findViewById(R.id.qushouye_btn);
            }
        }
    }


    public class YouYangViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ImageView left_icon, red_icon,iv_shoucang;
        public TextView content_tv, tv_date_time, pingfen_tv,
                tv_changguan_name, tv_changguan_address, tv_chakan_num, tv_juli;
        public CustomRoundAngleImageView iv_changguan_logo;
        public YouYangViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                left_icon = view.findViewById(R.id.left_icon);
                content_tv = view.findViewById(R.id.content_tv);
                red_icon = view.findViewById(R.id.red_icon);
                tv_date_time = view.findViewById(R.id.tv_date_time);
                pingfen_tv = view.findViewById(R.id.pingfen_tv);
                tv_changguan_name = view.findViewById(R.id.tv_changguan_name);
                tv_changguan_address = view.findViewById(R.id.tv_changguan_address);
                tv_chakan_num = view.findViewById(R.id.tv_chakan_num);
                tv_juli = view.findViewById(R.id.tv_juli);
                iv_changguan_logo = view.findViewById(R.id.iv_changguan_logo);
                iv_shoucang = view.findViewById(R.id.iv_shoucang);
            }
        }
    }
}
