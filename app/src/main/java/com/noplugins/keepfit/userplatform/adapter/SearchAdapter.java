//package com.noplugins.keepfit.userplatform.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
//import com.bumptech.glide.Glide;
//import com.noplugins.keepfit.userplatform.R;
//import com.noplugins.keepfit.userplatform.activity.changguan.ChaungguanDetailActivity;
//import com.noplugins.keepfit.userplatform.activity.teahcher.PrivateDetailActivity;
//import com.noplugins.keepfit.userplatform.bean.SearchBean;
//
//import java.util.List;
//
//public class SearchAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
//    public static final int TYPE_CHANG = 0;
//    public static final int TYPE_PRIVATE = 1;
//    public static final int TYPE_TEAM = 2;
//
//    public static final int VIEW_TYPE_EMPTY = 4;
//    private View emptyView;
//    private Context context;
//    private List<SearchBean.AreaBean> mChangList;
//    private int type;
//    public SearchAdapter(Context context, List<SearchBean.AreaBean> mChangList) {
//        this.context = context;
//        this.mChangList = mChangList;
//    }
//
//
//
//
//    @Override
//    public RecyclerView.ViewHolder getViewHolder(View view) {
//        Log.d("getViewHolder","getViewHolder:"+type);
//        switch (type){
//            case VIEW_TYPE_EMPTY:
//                EmptyViewHolder holder = new EmptyViewHolder(view,false);
//                return holder;
//            case TYPE_CHANG:
//                ChangHolder changHolder = new ChangHolder(view);
//                return changHolder;
//            case TYPE_PRIVATE:
//                PrivateHolder privateHolder = new PrivateHolder(view);
//                return privateHolder;
//            case TYPE_TEAM:
//                TeamHolder teamHolder = new TeamHolder(view);
//                return teamHolder;
//                default:
//                    EmptyViewHolder holder1 = new EmptyViewHolder(view,false);
//                    return holder1;
//
//        }
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
//
//        RecyclerView.ViewHolder holder = getViewHolderByViewType(viewType, parent);
//        return holder;
//    }
//
//
//
//    RecyclerView.ViewHolder holder = null;
//
//    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
//        View view = null;
//        switch (viewType){
//            case VIEW_TYPE_EMPTY:
//                view = LayoutInflater.from(context).inflate(R.layout.enpty_view, parent, false);
//                view.findViewById(R.id.iv_empty_view).setBackgroundResource(R.drawable.no_list_data);
//                ((TextView)view.findViewById(R.id.tv_empty_tips)).setText("木有搜索到相关内容哇~");
//
//                holder =  new EmptyViewHolder(view,false);
//            case TYPE_CHANG:
//                view = LayoutInflater.from(context).inflate(R.layout.item_search_cg, parent, false);
//                holder = new ChangHolder(view);
//            case TYPE_PRIVATE:
//                view = LayoutInflater.from(context).inflate(R.layout.item_search_private, parent, false);
//                holder = new PrivateHolder(view);
//            case TYPE_TEAM:
//                view = LayoutInflater.from(context).inflate(R.layout.item_search_team, parent, false);
//                holder = new TeamHolder(view);
//        }
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, boolean isItem) {
//        if (holder instanceof ChangHolder) {
//            ChangHolder headHolder = (ChangHolder) holder;
//            headHolder.tvTitle.setText(mChangList.get(position).getAreaName());
//            headHolder.tvAdress.setText(mChangList.get(position).getAddress());
//            headHolder.mJuli.setText(""+mChangList.get(position).getDistance()+"km");
//            headHolder.ll_cg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, ChaungguanDetailActivity.class);
//                    intent.putExtra("areaNum",mChangList.get(position).getAreaNum());
//                    context.startActivity(intent);
////                    if (BaseUtils.isFastClick()){
////
////                    }
//
//                }
//            });
//        }
//        if (holder instanceof PrivateHolder) {
//            PrivateHolder headHolder = (PrivateHolder) holder;
//            Glide.with(context).load("").placeholder(R.mipmap.ic_launcher)
//                    .into(headHolder.iv_logo);
//            headHolder.tvAdress.setText(mChangList.get(position).getTeacherName());
//            headHolder.mJuli.setText(""+mChangList.get(position).getServiceDur());
//            headHolder.ll_private.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, PrivateDetailActivity.class);
//                    intent.putExtra("teacherNum",mChangList.get(position).getTeacherNum());
//                    context.startActivity(intent);
////                    if (BaseUtils.isFastClick()){
////
////                    }
//
//                }
//            });
//        }
//        if (holder instanceof TeamHolder) {
//            TeamHolder headHolder = (TeamHolder) holder;
////            headHolder.tvTitle.setText(mChangList.get(position).getCourseName());
////            headHolder.tvAdress.setText(mChangList.get(position).getCourseNum());
////            headHolder.mJuli.setText(""+mChangList.get(position).getDistance()+"km");
//        }
//    }
//
//
//    @Override
//    public int getAdapterItemViewType(int position) {
//        if (mChangList.size() <= 0){
//            return VIEW_TYPE_EMPTY;
//        } else {
//            if (mChangList.get(0).getType() == 1){
//                type = 1;
//                return TYPE_CHANG;
//            }
//            if (mChangList.get(0).getType() == 2){
//                type = 2;
//                return TYPE_PRIVATE;
//            }
//            if (mChangList.get(0).getType() == 3){
//                type = 3;
//                return TYPE_TEAM;
//            }
//            return VIEW_TYPE_EMPTY;
//        }
//    }
//
//    @Override
//    public int getAdapterItemCount() {
//        return mChangList.size() > 0 ? mChangList.size() : 0;
//    }
//    /**
//     * 场馆 Holder
//     */
//    static class ChangHolder extends RecyclerView.ViewHolder {
//        private TextView tvTitle;
//        private TextView tvAdress;
//        private TextView mJuli;
//        private RelativeLayout ll_cg;
//        public ChangHolder(View itemView) {
//            super(itemView);
//            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
//            tvAdress = (TextView) itemView.findViewById(R.id.tv_address);
//            mJuli = (TextView) itemView.findViewById(R.id.tv_juli);
//            ll_cg = itemView.findViewById(R.id.ll_cg);
//        }
//    }
//    /**
//     * 场馆 Holder
//     */
//    static class PrivateHolder extends RecyclerView.ViewHolder {
//        private ImageView iv_logo;
//        private TextView tvAdress;
//        private TextView mJuli;
//        private RelativeLayout ll_private;
//        public PrivateHolder(View itemView) {
//            super(itemView);
//            iv_logo = (ImageView) itemView.findViewById(R.id.iv_logo);
//            tvAdress = (TextView) itemView.findViewById(R.id.tv_address);
//            mJuli = (TextView) itemView.findViewById(R.id.tv_juli);
//            ll_private = itemView.findViewById(R.id.ll_private);
//        }
//    }
//    /**
//     * 场馆 Holder
//     */
//    static class TeamHolder extends RecyclerView.ViewHolder {
//        private TextView tvTitle;
//        private TextView tvAdress;
//        private TextView mJuli;
//        private RelativeLayout ll_team;
//        public TeamHolder(View itemView) {
//            super(itemView);
//            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
//            tvAdress = (TextView) itemView.findViewById(R.id.tv_address);
//            mJuli = (TextView) itemView.findViewById(R.id.tv_juli);
//            ll_team = itemView.findViewById(R.id.ll_team);
//        }
//    }
//
//    /**
//     * 场馆 Holder
//     */
//    public class EmptyViewHolder extends RecyclerView.ViewHolder {
//        public View view;
//
//        public EmptyViewHolder(View item_view, boolean isItem) {
//            super(item_view);
//            if (isItem) {
//                this.view = item_view;
//            }
//        }
//    }
//
//
//}
//
