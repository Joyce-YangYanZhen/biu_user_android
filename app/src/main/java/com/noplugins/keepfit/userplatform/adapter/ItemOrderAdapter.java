package com.noplugins.keepfit.userplatform.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lxj.xpopup.core.BasePopupView;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.callback.PopViewCallBack;
import com.noplugins.keepfit.userplatform.entity.OrderEntity;
import com.noplugins.keepfit.userplatform.global.PublicPopControl;
import com.noplugins.keepfit.userplatform.util.BaseUtils;
import com.noplugins.keepfit.userplatform.util.GlideRoundTransform;
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

public class ItemOrderAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    private List<OrderEntity.OrderListBean> list;

    public ItemOrderAdapter(List<OrderEntity.OrderListBean> mlist, Context context) {
        this.context = context;
        this.list = mlist;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<OrderEntity.OrderListBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        //当购买的课程为私教课时，对应的订单不显示场馆单号
        if (list.size()> 1){
            for (OrderEntity.OrderListBean bean:list) {
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
        final ItemOrderAdapter.viewHolder holder;
        if (convertView == null) {
            holder = new ItemOrderAdapter.viewHolder();
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
            holder.rl_item = convertView.findViewById(R.id.rl_item);
            convertView.setTag(holder);
        } else {
            holder = (ItemOrderAdapter.viewHolder) convertView.getTag();
        }

        OrderEntity.OrderListBean orderListBean = list.get(position);
        holder.zhifu_status.setText(orderListBean.getFinalStatus());

        if (orderListBean.getCourseType() == 1) {//团课
            holder.item_dismiss.setVisibility(View.GONE);
            holder.class_type.setText("团操课程");
        } else if (orderListBean.getCourseType() == 2) {//私教
            holder.class_type.setText("私教课程");
//            holder.item_dismiss.setVisibility(View.VISIBLE);
            if (orderListBean.getCancelButton() == 1){
                holder.item_dismiss.setVisibility(View.VISIBLE);
            } else {
                holder.item_dismiss.setVisibility(View.GONE);
            }
        } else if (orderListBean.getCourseType() == 3) {//健身
            holder.item_dismiss.setVisibility(View.GONE);
            holder.class_type.setText("健身服务");

        }
//        if (orderListBean.getStatus() == -1) {//支付失败
//            holder.item_dismiss.setVisibility(View.GONE);
//
//        } else if (orderListBean.getStatus() == 0) {//未支付
//            if (orderListBean.getCourseType() == 2){//代表私教
//                holder.item_dismiss.setVisibility(View.VISIBLE);
//            } else {
//                holder.item_dismiss.setVisibility(View.GONE);
//            }
//
//        } else if (orderListBean.getStatus() == 1) {//已经支付
//            holder.item_dismiss.setVisibility(View.VISIBLE);
//
//
//        } else if (orderListBean.getStatus() == 2) {//取消已经支付
//            holder.item_dismiss.setVisibility(View.GONE);
//
//        }
//        else if (orderListBean.getStatus() == 3) {//取消已经支付
//            holder.item_dismiss.setVisibility(View.GONE);
//
//        }


        holder.teacher_name.setText(orderListBean.getCoachUserName());
        holder.cource_name.setText(orderListBean.getCourseName());
        if (orderListBean.getStartTime()!=null && orderListBean.getEndTime()!= null){
            holder.time_tv.setText(BaseUtils.strSubEnd3(orderListBean.getStartTime()) +"-"+BaseUtils.strSubEnd3(orderListBean.getEndTime().split(" ")[1]));
        }

        holder.price_tv.setText("¥" + orderListBean.getFinalPrice());
        Glide.with(context)
                .load(orderListBean.getUrl())
                .transform(new CenterCrop(context),
                        new GlideRoundTransform(context, 10))
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

    private class viewHolder {
        private TextView tv_name, class_type, teacher_name, cource_name, time_tv, zhifu_status, price_tv;
        private TextView item_dismiss;
        private ImageView img;
        private RelativeLayout rl_item;
    }
}
