package com.noplugins.keepfit.userplatform.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.google.gson.Gson;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.entity.TraveListItem;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.ui.pop.CommonPopupWindow;
import okhttp3.RequestBody;
import rx.Subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

public class TravelDailryAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<TraveListItem.SportListBean> list;
    private Activity context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_YOUTANG = 1;
    private TextView yaoqing_number_tv;
    private int select_num;
    private int max_selectnum = 5;

    public TravelDailryAdapter(List<TraveListItem.SportListBean> mlist, Activity mcontext) {
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
            item_view = LayoutInflater.from(context).inflate(R.layout.travel_dailry_empty_view, parent, false);
            holder = new EmptyViewHolder(item_view, false);
        } else if (viewType == TYPE_YOUTANG) {
            item_view = LayoutInflater.from(context).inflate(R.layout.travel_dailry_item, parent, false);
            holder = new YouYangViewHolder(item_view, true);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position, boolean isItem) {
        if (view_holder instanceof YouYangViewHolder) {
            YouYangViewHolder holder = (YouYangViewHolder) view_holder;
            TraveListItem.SportListBean dataBean = list.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });

            if (dataBean.getCourseType() == 1) {//团课
                holder.sijiao.setBackgroundResource(R.drawable.zi_bg);
                holder.class_type.setText("团课服务");
                holder.ll_name.setVisibility(View.VISIBLE);
                holder.teacher_name_tv.setText("团操-" + dataBean.getTeacherName());

            }
            if (dataBean.getCourseType() == 2) {//私教
                holder.sijiao.setBackgroundResource(R.drawable.bg_lv);
                holder.class_type.setText("私教服务");
                holder.ll_name.setVisibility(View.VISIBLE);
                //教练名字
                holder.teacher_name_tv.setText("教练-" + dataBean.getTeacherName());
            }
            if (dataBean.getCourseType() == 3) {
                holder.sijiao.setBackgroundResource(R.drawable.huang_bg);
                holder.class_type.setText("场馆服务");
                holder.ll_name.setVisibility(View.GONE);
                holder.teacher_name_tv.setText("场馆-" + dataBean.getAreaName());

            }
            holder.time_tv.setText(dataBean.getStartTime());
            holder.class_name.setText(dataBean.getCourseName());
            holder.start_end_tv.setText(dataBean.getBiuTime());
            holder.adress_tv.setText(dataBean.getAreaAddress());

            holder.shanchu_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete_pop(holder, dataBean.getSportsNum(), position);
                }
            });

        }
    }

    private void delete_pop(YouYangViewHolder holder, String dataBeanId, int position) {
        CommonPopupWindow popupWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.delete_pop_layout)
                .setBackGroundLevel(0.5f)//0.5f
                .setAnimationStyle(R.style.main_menu_animstyle)
                .setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT)
                .setOutSideTouchable(true).create();
        popupWindow.showAsDropDown(holder.shanchu_btn);

        /**设置逻辑*/
        View view = popupWindow.getContentView();
        LinearLayout cancel_layout = view.findViewById(R.id.cancel_layout);
        LinearLayout sure_layout = view.findViewById(R.id.sure_layout);

        cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        sure_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                delete_daily(dataBeanId, position);

            }
        });


    }

    private void delete_daily(String id, int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("sportsNum", id);
        Gson gson = new Gson();
        String json_params = gson.toJson(params);
        String json = new Gson().toJson(params);//要传递的json
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Log.e(TAG, "删除运动日志：" + json_params);
        Subscription subscription = Network.getInstance("删除运动日志", context)
                .shanchu_yundong_rizhi(requestBody, new ProgressSubscriberNew<>(Object.class, new GsonSubscriberOnNextListener<Object>() {
                    @Override
                    public void on_post_entity(Object entity, String s) {
//                        Log.e("删除运动日志成功", entity + "删除运动日志成功");
                        Toast.makeText(context,"删除成功！",Toast.LENGTH_SHORT).show();
//                        list.remove(position);
//                        notifyDataSetChanged();
                        list.remove(position);//删除数据源,移除集合中当前下标的数据
                        notifyItemRemoved(position);//刷新被删除的地方
                        notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("删除运动日志失败", "删除运动日志失败:" + error);
                    }
                }, context, true));
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


    public void setData(List<TraveListItem.SportListBean> list) {
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
        public ImageView left_icon;
        public TextView title_tv, class_type, time_tv, class_name, start_end_tv, teacher_name_tv, adress_tv;
        public LinearLayout sijiao, shanchu_btn, ll_name;

        public YouYangViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                ll_name = view.findViewById(R.id.ll_name);
                left_icon = view.findViewById(R.id.left_icon);
                title_tv = view.findViewById(R.id.title_tv);
                class_type = view.findViewById(R.id.class_type);
                time_tv = view.findViewById(R.id.time_tv);
                class_name = view.findViewById(R.id.class_name);
                start_end_tv = view.findViewById(R.id.start_end_tv);
                teacher_name_tv = view.findViewById(R.id.teacher_name_tv);
                adress_tv = view.findViewById(R.id.adress_tv);
                sijiao = view.findViewById(R.id.sijiao);
                shanchu_btn = view.findViewById(R.id.shanchu_btn);
            }
        }
    }
}
