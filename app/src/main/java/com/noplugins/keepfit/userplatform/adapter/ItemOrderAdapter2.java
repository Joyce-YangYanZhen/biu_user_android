package com.noplugins.keepfit.userplatform.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.lxj.xpopup.core.BasePopupView;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.callback.PopViewCallBack;
import com.noplugins.keepfit.userplatform.entity.OrderDetailEntity;
import com.noplugins.keepfit.userplatform.global.PublicPopControl;
import com.noplugins.keepfit.userplatform.util.BaseUtils;
import com.noplugins.keepfit.userplatform.util.MessageEvent;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.ui.pop.CommonPopupWindow;
import org.greenrobot.eventbus.EventBus;
import rx.Subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemOrderAdapter2 extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    private List<OrderDetailEntity.OrderListBean> list;

    public ItemOrderAdapter2(List<OrderDetailEntity.OrderListBean> mlist, Context context) {
        this.context = context;
        this.list = mlist;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<OrderDetailEntity.OrderListBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list.size()> 1){
            for (OrderDetailEntity.OrderListBean bean:list) {
                if (bean.getCourseType() == 3){
                    list.remove(bean);
                    return list.size();
                }
            }
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewHolder holder;
        if (convertView == null) {
            holder = new viewHolder();
            convertView = inflater.inflate(R.layout.order_item, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.item_dismiss = (TextView) convertView.findViewById(R.id.item_dismiss);
            holder.class_type = (TextView) convertView.findViewById(R.id.class_type);
            holder.teacher_name = (TextView) convertView.findViewById(R.id.teacher_name);
            holder.cource_name = (TextView) convertView.findViewById(R.id.cource_name);
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.zhifu_status = (TextView) convertView.findViewById(R.id.zhifu_status);
            holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        OrderDetailEntity.OrderListBean orderListBean = list.get(position);
        holder.zhifu_status.setText(orderListBean.getFinalStatus());


//        if (orderListBean.getStatus() == -1) {//支付失败
//            holder.item_dismiss.setVisibility(View.GONE);
//
//        } else if (orderListBean.getStatus() == 0) {//未支付
//            if (orderListBean.getCourseType() == 2){
//                holder.item_dismiss.setVisibility(View.VISIBLE);
//            } else {
//                holder.item_dismiss.setVisibility(View.GONE);
//            }
//
//        } else if (orderListBean.getStatus() == 1) {//已经支付
//            if (orderListBean.getCourseType() == 2){
//                holder.item_dismiss.setVisibility(View.VISIBLE);
//            } else {
//                holder.item_dismiss.setVisibility(View.GONE);
//            }
//
//        } else if (orderListBean.getStatus() == 2) {//取消已经支付
//            holder.item_dismiss.setVisibility(View.GONE);
//        }else if (orderListBean.getStatus() == 3) {//取消支付
//            holder.item_dismiss.setVisibility(View.GONE);
//        }

        if (orderListBean.getCourseType() == 1) {//团课
            holder.item_dismiss.setVisibility(View.GONE);
            holder.class_type.setText("团操课程");
        } else if (orderListBean.getCourseType() == 2) {//私教
            holder.class_type.setText("私教课程");
            if (orderListBean.getCancelButton() == 1){
                holder.item_dismiss.setVisibility(View.VISIBLE);
            } else {
                holder.item_dismiss.setVisibility(View.GONE);
            }
        } else if (orderListBean.getCourseType() == 3) {//健身
            holder.item_dismiss.setVisibility(View.GONE);
            holder.class_type.setText("健身服务");

        }

        holder.teacher_name.setText(orderListBean.getCoachUserName());
        holder.cource_name.setText(orderListBean.getCourseName());
        holder.time_tv.setText(BaseUtils.strSubEnd3(orderListBean.getStartTime()) +"-"+BaseUtils.strSubEnd3(orderListBean.getEndTime().split(" ")[1]));
        holder.price_tv.setText("¥"+orderListBean.getFinalPrice());
        Glide.with(context).load(orderListBean.getUrl())
                .placeholder(R.drawable.logo_gray)
                .into(holder.img);
        holder.item_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_pop(holder.item_dismiss, orderListBean.getCustOrderItemNum(), position);

            }
        });
        return convertView;
    }

    private void cancel_pop(TextView quxiao_btn1, String custOrderNum, int position) {
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

    private void cancel_order(String custOrderItemNum, int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("ordItemNum", custOrderItemNum);
        Subscription subscription = Network.getInstance("取消订单", context)
                .cancel_order(params,
                        new ProgressSubscriber<>("取消订单", new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                                Toast.makeText(context, "订单取消成功", Toast.LENGTH_SHORT).show();
//                                MessageEvent messageEvent = new MessageEvent("update_status");
                                EventBus.getDefault().post("update_status");
                                ((Activity)context).finish();
                            }

                            @Override
                            public void onError(String error) {

                            }
                        }, context, true));

    }


    private class viewHolder {
        private TextView tv_name, class_type, teacher_name, cource_name, time_tv, zhifu_status, price_tv;
        private TextView item_dismiss;
        private ImageView img;
    }
}
