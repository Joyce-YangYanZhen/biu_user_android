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
import com.noplugins.keepfit.userplatform.entity.CollectionSIjiaoEntity;
import com.noplugins.keepfit.userplatform.util.ui.CustomRoundAngleImageView;
import com.noplugins.keepfit.userplatform.util.ui.ZFlowLayout;

import java.util.List;

public class CollectionSijiaoAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<CollectionSIjiaoEntity.TeacherListBean> list;
    private Activity context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_YOUTANG = 1;


    public CollectionSijiaoAdapter(List<CollectionSIjiaoEntity.TeacherListBean> mlist, Activity mcontext) {
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
            item_view = LayoutInflater.from(context).inflate(R.layout.item_find_private, parent, false);
            holder = new YouYangViewHolder(item_view, true);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position, boolean isItem) {
        if (view_holder instanceof YouYangViewHolder) {
            YouYangViewHolder holder = (YouYangViewHolder) view_holder;
            CollectionSIjiaoEntity.TeacherListBean teacherListBean = list.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            holder.iv_shoucang.setVisibility(View.VISIBLE);
            holder.tv_private_pinfen.setText(teacherListBean.getFinalGrade() + "分");
            holder.tv_private_name.setText(teacherListBean.getTeacherName());
            holder.tv_private_time.setText("累计服务时长: " + teacherListBean.getServiceDur());
            holder.tv_price.setText(teacherListBean.getLowestPrice() + "");
            Glide.with(context).load(teacherListBean.getLogoUrl())
                    .placeholder(R.drawable.logo_gray)
                    .into(holder.iv_private_logo);

            ViewGroup.LayoutParams layoutParams =
                    new ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(5, 5, 5, 5);
            holder.fl_private_skill.removeAllViews();
            if (teacherListBean.getSkillList()!= null) {
                for (int i =0;i<teacherListBean.getSkillList().size();i++) {
                    View paramItemView =
                            context.getLayoutInflater().inflate(R.layout.adapter_search_histroy,
                                    holder.fl_private_skill, false);
                    TextView keyWordTv = paramItemView.findViewById(R.id.tv_content);
                            keyWordTv.setPadding(5, 5, 5, 5);
                    keyWordTv.setText(teacherListBean.getSkillList().get(i));
                    holder.fl_private_skill.addView(paramItemView, layoutParams);
                }
            }

        } else if (view_holder instanceof CollectionSijiaoAdapter.EmptyViewHolder) {
            CollectionSijiaoAdapter.EmptyViewHolder emptyViewHolder = (CollectionSijiaoAdapter.EmptyViewHolder) view_holder;
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


    public void setData(List<CollectionSIjiaoEntity.TeacherListBean> list) {
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
        public TextView content_tv, tv_date_time, tv_private_pinfen, tv_private_name, tv_private_time, tv_price;
        public CustomRoundAngleImageView iv_private_logo;
        public ZFlowLayout fl_private_skill;

        public YouYangViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                left_icon = view.findViewById(R.id.left_icon);
                content_tv = view.findViewById(R.id.content_tv);
                red_icon = view.findViewById(R.id.red_icon);
                tv_date_time = view.findViewById(R.id.tv_date_time);
                tv_date_time = view.findViewById(R.id.tv_date_time);
                tv_private_pinfen = view.findViewById(R.id.tv_private_pinfen);
                tv_private_name = view.findViewById(R.id.tv_private_name);
                iv_private_logo = view.findViewById(R.id.iv_private_logo);
                tv_private_time = view.findViewById(R.id.tv_private_time);
                tv_price = view.findViewById(R.id.tv_price);
                iv_shoucang = view.findViewById(R.id.iv_shoucang);
                fl_private_skill = view.findViewById(R.id.fl_private_skill);
            }
        }
    }
}
