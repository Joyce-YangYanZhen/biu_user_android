package com.noplugins.keepfit.userplatform.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.entity.MessageDateEntity;

import java.util.List;

public class OrderAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<MessageDateEntity.ListBean> list;
    private Activity context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_YOUTANG = 1;

    public OrderAdapter(List<MessageDateEntity.ListBean> mlist, Activity mcontext) {
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
            item_view = LayoutInflater.from(context).inflate(R.layout.message_empty_view, parent, false);
            holder = new EmptyViewHolder(item_view, false);
        } else if (viewType == TYPE_YOUTANG) {
            item_view = LayoutInflater.from(context).inflate(R.layout.order_message_item, parent, false);
            holder = new YouYangViewHolder(item_view, true);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position, boolean isItem) {
        if (view_holder instanceof YouYangViewHolder) {
            YouYangViewHolder holder = (YouYangViewHolder) view_holder;
            MessageDateEntity.ListBean messageBean = list.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });

            if (messageBean.getReadStatus() == 1) {//已读
                holder.red_icon.setVisibility(View.INVISIBLE);
            } else {//未读
                holder.red_icon.setVisibility(View.VISIBLE);
            }

            if (messageBean.getType() == 1) {
                Glide.with(context).load(R.drawable.yugou_icon).into(holder.left_icon);
            } else if (messageBean.getType() == 2) {
                Glide.with(context).load(R.drawable.cancel_icon).into(holder.left_icon);
            } else if (messageBean.getType() == 3) {
                Glide.with(context).load(R.drawable.daizhifu_icon).into(holder.left_icon);
            }

            holder.content_tv.setText(messageBean.getMessageCon());
            holder.tv_date_time.setText(messageBean.getCreateDate());

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


    public void setData(List<MessageDateEntity.ListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public EmptyViewHolder(View item_view, boolean isItem) {
            super(item_view);
            if (isItem) {
                this.view = item_view;
            }
        }
    }


    public class YouYangViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ImageView left_icon, red_icon;
        public TextView content_tv, tv_date_time;

        public YouYangViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                left_icon = view.findViewById(R.id.left_icon);
                content_tv = view.findViewById(R.id.content_tv);
                red_icon = view.findViewById(R.id.red_icon);
                tv_date_time = view.findViewById(R.id.tv_date_time);

            }
        }
    }


}
