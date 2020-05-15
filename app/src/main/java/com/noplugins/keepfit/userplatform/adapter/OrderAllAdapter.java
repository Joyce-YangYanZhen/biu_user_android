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
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.lxj.xpopup.core.BasePopupView;
import com.noplugins.keepfit.userplatform.MainActivity;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.activity.OederDetailActivity;
import com.noplugins.keepfit.userplatform.activity.teahcher.PrivateToCgDetailActivity;
import com.noplugins.keepfit.userplatform.callback.PopViewCallBack;
import com.noplugins.keepfit.userplatform.entity.OrderEntity;
import com.noplugins.keepfit.userplatform.global.PublicPopControl;
import com.noplugins.keepfit.userplatform.util.BaseUtils;
import com.noplugins.keepfit.userplatform.util.MessageEvent;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.ui.MyListView;
import com.noplugins.keepfit.userplatform.util.ui.TimeRunTextView;
import com.noplugins.keepfit.userplatform.util.ui.pop.CommonPopupWindow;
import com.noplugins.keepfit.userplatform.wxapi.WXPayEntryActivity;
import org.greenrobot.eventbus.EventBus;
import rx.Subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAllAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<List<OrderEntity.OrderListBean>> list;
    private Activity context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_YOUTANG = 1;
    private TextView yaoqing_number_tv;
    private int select_num;
    private int max_selectnum = 5;
    private LinearLayoutManager layoutManager;

    public OrderAllAdapter(List<List<OrderEntity.OrderListBean>> mlist, Activity mcontext, LinearLayoutManager mlayoutManager) {
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        Log.d("OrderAllLog11", "------333----:" + position);
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });

            holder.time_tv.setText(orderListBean.getCreateDate());
            holder.changuan_tv.setText(orderListBean.getAreaName());
            Log.d("OrderAllLog", "----------" + orderListBean.getTotalPrice());
            holder.gongji_tv.setText("¥" + orderListBean.getTotalPrice());
            holder.zhufu_status.setText(orderListBean.getFinalStatus());



            if (orderListBean.getCourseType() == 2) {//sijiao
                holder.quxiao_btn1.setVisibility(View.INVISIBLE);
                holder.daifukuan_layout.setVisibility(View.GONE);
                for (OrderEntity.OrderListBean order :
                        orderListBeans) {
                    if (order.getStatus() == 0) {
                        holder.daifukuan_layout.setVisibility(View.VISIBLE);
                        holder.ll_pay.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if (orderListBean.getCancelButton() == 1){//为1表示可取消
                    holder.quxiao_btn1.setVisibility(View.VISIBLE);
                } else {
                    holder.quxiao_btn1.setVisibility(View.GONE);
                }
//                holder.quxiao_btn1.setVisibility(View.VISIBLE);
                if (orderListBean.getStatus() == 3) {
                    holder.ll_pay.setVisibility(View.GONE);
                } else if (orderListBean.getStatus() == 0) {
                    holder.ll_pay.setVisibility(View.VISIBLE);
                }
            }

            if (orderListBean.getStatus() == -1) {//支付失败
                ItemOrderAdapter itemOrderAdapter = new ItemOrderAdapter(orderListBeans, context);
                holder.listview.setAdapter(itemOrderAdapter);

//                holder.gongji_layout.setVisibility(View.GONE);
//                holder.quanyi_hgongji_layout.setVisibility(View.VISIBLE);
//                holder.quanyi_layout.setVisibility(View.GONE);
                holder.daifukuan_layout.setVisibility(View.GONE);
                holder.chognxin_buy_layout.setVisibility(View.VISIBLE);

                if (orderListBeans.size() > 1) {
                    holder.zhufu_status.setVisibility(View.INVISIBLE);
                } else {
                    holder.zhufu_status.setVisibility(View.VISIBLE);
                }

            } else if (orderListBean.getStatus() == 0) {//未支付
                ItemOrderAdapter itemOrderAdapter = new ItemOrderAdapter(orderListBeans, context);
                holder.listview.setAdapter(itemOrderAdapter);

                holder.gongji_layout.setVisibility(View.VISIBLE);
//                holder.quanyi_hgongji_layout.setVisibility(View.GONE);
//                holder.daifukuan_layout.setVisibility(View.VISIBLE);
                holder.chognxin_buy_layout.setVisibility(View.GONE);

                if (orderListBeans.size() > 1) {
                    holder.zhufu_status.setVisibility(View.INVISIBLE);
                } else {
                    holder.zhufu_status.setVisibility(View.VISIBLE);
                }
            } else if (orderListBean.getStatus() == 1) {//已经支付


                ItemOrderAdapter itemOrderAdapter = new ItemOrderAdapter(orderListBeans, context);
                holder.listview.setAdapter(itemOrderAdapter);
//                holder.gongji_layout.setVisibility(View.GONE);
                if (orderListBean.getCourseType() != 2) {
//                    holder.quxiao_btn1.setVisibility(View.VISIBLE);
                }
//                holder.quanyi_hgongji_layout.setVisibility(View.VISIBLE);
                holder.ll_pay.setVisibility(View.GONE);
                holder.chognxin_buy_layout.setVisibility(View.GONE);

                if (orderListBeans.size() > 1) {
                    holder.zhufu_status.setVisibility(View.INVISIBLE);
                } else {
                    holder.zhufu_status.setVisibility(View.VISIBLE);
                }
            } else if (orderListBean.getStatus() == 2) {//取消支付

                ItemOrderAdapter itemOrderAdapter = new ItemOrderAdapter(orderListBeans, context);
                holder.listview.setAdapter(itemOrderAdapter);
//                holder.gongji_layout.setVisibility(View.GONE);
//                holder.quanyi_hgongji_layout.setVisibility(View.VISIBLE);
//                holder.daifukuan_layout.setVisibility(View.GONE);
                holder.chognxin_buy_layout.setVisibility(View.GONE);

                if (orderListBeans.size() > 1) {
                    holder.zhufu_status.setVisibility(View.INVISIBLE);
                } else {
                    holder.zhufu_status.setVisibility(View.VISIBLE);
                }
            } else if (orderListBean.getStatus() == 3) {//第一条订单为 取消已支付
                ItemOrderAdapter itemOrderAdapter = new ItemOrderAdapter(orderListBeans, context);
                holder.listview.setAdapter(itemOrderAdapter);
//                holder.gongji_layout.setVisibility(View.GONE);
//                holder.quanyi_hgongji_layout.setVisibility(View.VISIBLE);
//                holder.daifukuan_layout.setVisibility(View.GONE);
                holder.chognxin_buy_layout.setVisibility(View.GONE);

                if (orderListBeans.size() > 1) {
                    holder.zhufu_status.setVisibility(View.INVISIBLE);
                } else {
                    holder.zhufu_status.setVisibility(View.VISIBLE);
                }
            }


//            holder.changguan_detail_tv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (BaseUtils.isFastClick()){
//                        Intent intent = new Intent(context, PrivateToCgDetailActivity.class);
//                        intent.putExtra("areaNum", orderListBeans.get(position).getAreaNum());
//                        intent.putExtra("is_from_order_list", "true");
//                        context.startActivity(intent);
//                    }
//
//                }
//            });

            //倒计时的逻辑
            if (orderListBean.getTimeDur() > 0) {
                Log.e("剩余时间", "剩余时间" + orderListBean.getTimeDur() + 6000);
                holder.run_time.initTime();
                holder.run_time.startTime(orderListBean.getTimeDur()+ 6000);
                holder.run_time.setTimeViewListener(new TimeRunTextView.OnTimeViewListener() {
                    @Override
                    public void onTimeStart() {
                        Log.e("mcy", "开始倒计时啦");
                    }

                    @Override
                    public void onTimeEnd() {
                        Log.e("mcy", "时间停止了");
                        //倒计时完成后通知订单首页刷新列表
                        MessageEvent messageEvent = new MessageEvent("update_status_djs");
                        EventBus.getDefault().post(messageEvent);
                    }

                });
            }
            holder.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (BaseUtils.isFastClick()) {
                        Log.d("OrderAllLog11", "----------:" + position);
                        Log.d("OrderAllLog11", "----size------:" + orderListBeans.size());
                        Intent intent = new Intent(context, OederDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("order_id", orderListBeans.get(i).getCustOrderNum());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                }
            });

            holder.quxiao_btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BaseUtils.isFastClick()) {
                        cancel_pop(holder.quxiao_btn1, orderListBean.getCustOrderItemNum(), position);

                    }

                }
            });

            holder.ll_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BaseUtils.isFastClick()) {
                        //去支付
                        Intent intent = new Intent(context, WXPayEntryActivity.class);
                        intent.putExtra("orderNum", orderListBeans.get(position).getCustOrderNum());
                        intent.putExtra("price", orderListBeans.get(position).getTotalPrice());
                        context.startActivity(intent);
                        context.finish();
                    }

                }
            });
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

    private void cancel_pop(LinearLayout quxiao_btn1, String custOrderNum, int position) {

        PublicPopControl.alert_call_phone_dialog_center(context, new PopViewCallBack() {
            @Override
            public void return_view(View view, BasePopupView popup) {
                TextView title = view.findViewById(R.id.to_phone);
                title.setText("确认取消订单？");
                view.findViewById(R.id.cancel_btn)
                        .setOnClickListener(v -> popup.dismiss());
                view.findViewById(R.id.sure_btn)
                        .setOnClickListener(v -> {
                            popup.dismiss();
                            cancel_order(custOrderNum, position);
                        });
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
                                Log.d("onError", error);
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                        }, context, true));

    }


    /**
     * 当view被销毁的时候
     *
     * @param holder
     */
    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof YouYangViewHolder) {
            YouYangViewHolder vholder = (YouYangViewHolder) holder;
            vholder.run_time.stopTime();
        }
        super.onViewRecycled(holder);
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
        LinearLayout gongji_layout, quanyi_hgongji_layout, daifukuan_layout, ll_pay,
                chognxin_buy_layout, changguan_detail_tv, quxiao_btn1,huodong_layout;
        TextView zhufu_status, time_tv, changuan_tv, gongji_tv,tv_huodong_price;
        RelativeLayout quanyi_layout;
        TimeRunTextView run_time;

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
                run_time = view.findViewById(R.id.run_time);
                time_tv = view.findViewById(R.id.time_tv);
                changuan_tv = view.findViewById(R.id.changuan_tv);
                gongji_tv = view.findViewById(R.id.gongji_tv);
                changguan_detail_tv = view.findViewById(R.id.changguan_detail_tv);
                quxiao_btn1 = view.findViewById(R.id.quxiao_btn1);
                ll_pay = view.findViewById(R.id.ll_pay);
                huodong_layout = view.findViewById(R.id.huodong_layout);
                tv_huodong_price = view.findViewById(R.id.tv_huodong_price);
            }
        }

    }
}
