package com.noplugins.keepfit.userplatform.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.noplugins.keepfit.userplatform.MainActivity;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.activity.OederDetailActivity;
import com.noplugins.keepfit.userplatform.activity.teahcher.PrivateToCgDetailActivity;
import com.noplugins.keepfit.userplatform.entity.OrderEntity;
import com.noplugins.keepfit.userplatform.util.BaseUtils;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.ui.MyListView;
import com.noplugins.keepfit.userplatform.util.ui.pop.CommonPopupWindow;
import org.greenrobot.eventbus.EventBus;
import rx.Subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HavePayAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<List<OrderEntity.OrderListBean>> list;
    private Activity context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_YOUTANG = 1;
    private TextView yaoqing_number_tv;
    private int select_num;
    private int max_selectnum = 5;
    private LinearLayoutManager layoutManager;

    public HavePayAdapter(List<List<OrderEntity.OrderListBean>> mlist, Activity mcontext, LinearLayoutManager mlayoutManager) {
        list = mlist;
        context = mcontext;
        layoutManager = mlayoutManager;

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
            item_view = LayoutInflater.from(context).inflate(R.layout.daywhatch_empty_view, parent, false);
            holder = new EmptyViewHolder(item_view, false);
        } else if (viewType == TYPE_YOUTANG) {
            item_view = LayoutInflater.from(context).inflate(R.layout.order_all_item, parent, false);
            holder = new YouYangViewHolder(item_view, true);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position, boolean isItem) {
        if (view_holder instanceof YouYangViewHolder) {
            YouYangViewHolder holder = (YouYangViewHolder) view_holder;
            List<OrderEntity.OrderListBean> orderListBeans = list.get(position);
            OrderEntity.OrderListBean orderListBean = orderListBeans.get(0);

            if (orderListBean.getCouponList().size() > 0){
                //优惠金额
                holder.huodong_layout.setVisibility(View.VISIBLE);
                holder.tv_huodong_price.setText("-¥"+orderListBean.getCouponList().get(0).getPriceDiscount());
            } else {
                holder.huodong_layout.setVisibility(View.GONE);
            }
            holder.time_tv.setText(orderListBean.getCreateDate());
            holder.changuan_tv.setText(orderListBean.getAreaName());
            holder.gongji_tv.setText("¥"+orderListBean.getTotalPrice());
            holder.zhufu_status.setText(orderListBean.getFinalStatus());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });


            ItemOrderAdapter itemOrderAdapter = new ItemOrderAdapter(orderListBeans, context);
            holder.listview.setAdapter(itemOrderAdapter);
//            holder.gongji_layout.setVisibility(View.GONE);

            if (orderListBean.getCancelButton() == 1){
                holder.quxiao_btn1.setVisibility(View.VISIBLE);
                if (orderListBean.getCourseType() != 2){
                    holder.quxiao_btn1.setVisibility(View.VISIBLE);
                } else {
                    holder.quxiao_btn1.setVisibility(View.GONE);
                }
            } else {
                holder.quxiao_btn1.setVisibility(View.GONE);
            }
//                holder.quanyi_hgongji_layout.setVisibility(View.VISIBLE);
//            holder.daifukuan_layout.setVisibility(View.GONE);
            holder.chognxin_buy_layout.setVisibility(View.GONE);

            if (orderListBeans.size() > 1) {
                holder.zhufu_status.setVisibility(View.INVISIBLE);
            } else {
                holder.zhufu_status.setVisibility(View.VISIBLE);
            }
            holder.changguan_detail_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BaseUtils.isFastClick()){
//                        Intent intent = new Intent(context, PrivateToCgDetailActivity.class);
//                        intent.putExtra("areaNum", orderListBeans.get(position).getAreaNum());
//                        intent.putExtra("is_from_order_list", "true");
//                        context.startActivity(intent);
                    }

                }
            });

            holder.quxiao_btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BaseUtils.isFastClick()){
                        cancel_pop(holder.quxiao_btn1, orderListBean.getCustOrderItemNum(), position);

                    }

                }
            });

            holder.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (BaseUtils.isFastClick()){
                        Intent intent = new Intent(context, OederDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("order_id", orderListBeans.get(i).getCustOrderNum());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                }
            });
        }
        else if (view_holder instanceof EmptyViewHolder) {
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

    private void cancel_pop(LinearLayout quxiao_btn1, String custOrderNum, int position) {
        CommonPopupWindow popupWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.delete_order_pop)
                .setBackGroundLevel(0.5f)//0.5f
                .setAnimationStyle(R.style.main_menu_animstyle)
                .setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT)
                .setOutSideTouchable(true).create();
        popupWindow.showAsDropDown(quxiao_btn1);

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
        //取消订单
        sure_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                cancel_order(custOrderNum, position);
            }
        });

    }

    private void cancel_order(String custOrderNum, int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("ordItemNum", custOrderNum);
        Subscription subscription = Network.getInstance("取消订单", context)
                .cancel_order(params,
                        new ProgressSubscriber<>("取消订单", new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                                Toast.makeText(context, "订单取消成功", Toast.LENGTH_SHORT).show();
//                                MessageEvent messageEvent = new MessageEvent("update_status");
                                EventBus.getDefault().post("update_status");
                            }

                            @Override
                            public void onError(String error) {
                                Log.d("onError",error);
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
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


    public void setData(List<List<OrderEntity.OrderListBean>> list) {
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
        MyListView listview;
        LinearLayout gongji_layout, quanyi_hgongji_layout, daifukuan_layout,
                chognxin_buy_layout, changguan_detail_tv,quxiao_btn1,huodong_layout;
        TextView zhufu_status, time_tv, changuan_tv, gongji_tv,tv_huodong_price;
        RelativeLayout quanyi_layout;

        public YouYangViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                listview = view.findViewById(R.id.listview);
                gongji_layout = view.findViewById(R.id.gongji_layout);
                quanyi_hgongji_layout = view.findViewById(R.id.quanyi_hgongji_layout);
                daifukuan_layout = view.findViewById(R.id.daifukuan_layout);
                zhufu_status = view.findViewById(R.id.zhufu_status);
                chognxin_buy_layout = view.findViewById(R.id.chognxin_buy_layout);
                quanyi_layout = view.findViewById(R.id.quanyi_layout);
                changguan_detail_tv = view.findViewById(R.id.changguan_detail_tv);
                time_tv = view.findViewById(R.id.time_tv);
                changuan_tv = view.findViewById(R.id.changuan_tv);
                gongji_tv = view.findViewById(R.id.gongji_tv);
                quxiao_btn1 = view.findViewById(R.id.quxiao_btn1);
                huodong_layout = view.findViewById(R.id.huodong_layout);
                tv_huodong_price = view.findViewById(R.id.tv_huodong_price);
            }
        }
    }
}
