package com.noplugins.keepfit.userplatform.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.lxj.xpopup.core.BasePopupView;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.adapter.ItemOrderAdapter2;
import com.noplugins.keepfit.userplatform.base.BaseActivity;
import com.noplugins.keepfit.userplatform.callback.PopViewCallBack;
import com.noplugins.keepfit.userplatform.entity.OrderDetailEntity;
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
import java.util.Map;

public class OederDetailActivity extends BaseActivity {
    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.cancel_btn)
    LinearLayout cancel_btn;
    @BindView(R.id.ll_pay)
    LinearLayout ll_pay;
    @BindView(R.id.run_time)
    TimeRunTextView run_time;

    @BindView(R.id.listview)
    MyListView listview;
    @BindView(R.id.changguan_name)
    TextView changguan_name;
    @BindView(R.id.changguan_btn)
    LinearLayout changguan_btn;
    @BindView(R.id.zhufu_status)
    TextView zhufu_status;
    @BindView(R.id.gongji_price)
    TextView gongji_price;
    @BindView(R.id.quanyi_price)
    TextView quanyi_price;
    @BindView(R.id.gongji_tv2)
    TextView gongji_tv2;
    @BindView(R.id.zhifu_type)
    TextView zhifu_type;
    @BindView(R.id.order_number)
    TextView order_number;
    @BindView(R.id.tv_huodong_price)
    TextView tv_huodong_price;
    @BindView(R.id.order_create_time)
    TextView order_create_time;
    @BindView(R.id.yizhifu_layout)
    RelativeLayout yizhifu_layout;
    @BindView(R.id.yizhifu_layout2)
    LinearLayout yizhifu_layout2;
    @BindView(R.id.huodong_layout)
    LinearLayout huodong_layout;

    private String order_id = "";

    private int pay = -1;

    private double price = 0.0;
    private String orderItemNum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initBundle(Bundle parms) {
        if (null != parms) {
            order_id = parms.getString("order_id");
            Log.e("传过来的order_id", "" + order_id);
            pay = parms.getInt("from", -1);
        }
    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_oeder_detail);
        ButterKnife.bind(this);
        isShowTitle(false);


    }

    private void cancel_pop() {

        PublicPopControl.alert_call_phone_dialog_center(this, new PopViewCallBack() {
            @Override
            public void return_view(View view, BasePopupView popup) {
                TextView title = view.findViewById(R.id.to_phone);
                title.setText("确认取消订单？");
                view.findViewById(R.id.cancel_btn)
                        .setOnClickListener(v -> popup.dismiss());
                view.findViewById(R.id.sure_btn)
                        .setOnClickListener(v -> {
                            popup.dismiss();
                            cancel_order();
                        });
            }
        });

    }

    private void cancel_order() {
        Map<String, Object> params = new HashMap<>();
//        params.put("ordNum",order_id);
        params.put("ordItemNum", order_number.getText());
        Subscription subscription = Network.getInstance("取消订单", this)
                .cancel_order(params,
                        new ProgressSubscriber<>("取消订单", new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                                Toast.makeText(getApplicationContext(), "订单取消成功", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post("update_status");
                                finish();

                            }

                            @Override
                            public void onError(String error) {
                                Log.d("onError", error);
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }, this, true));

    }

    private void delete_pop() {
        CommonPopupWindow popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.cancel_pop_layout)
                .setBackGroundLevel(0.5f)//0.5f
                .setAnimationStyle(R.style.main_menu_animstyle)
                .setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT)
                .setOutSideTouchable(true).create();
        popupWindow.showAsDropDown(cancel_btn);

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
        //删除订单
        sure_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();

                delete_order();

            }
        });


    }

    private void delete_order() {
        Map<String, Object> params = new HashMap<>();
        params.put("ordNum", order_id);
        Subscription subscription = Network.getInstance("删除订单", this)
                .cancel_order(params,
                        new ProgressSubscriber<>("删除订单", new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                                Toast.makeText(getApplicationContext(), "订单删除成功", Toast.LENGTH_SHORT).show();
                                finish();

                            }

                            @Override
                            public void onError(String error) {

                            }
                        }, this, true));
    }


    @Override
    public void doBusiness(Context mContext) {
        initDate();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseUtils.isFastClick()) {
                    if (pay == 1002) {
                        Intent intent = new Intent(OederDetailActivity.this, OrderManageActivity.class);
                        intent.putExtra("pay", 1001);
                        startActivity(intent);
                    }
                    finish();

                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_pop();
            }
        });

        ll_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseUtils.isFastClick()) {
                    //去支付
                    Intent intent = new Intent(OederDetailActivity.this, WXPayEntryActivity.class);
                    intent.putExtra("orderNum", order_id);
                    intent.putExtra("price", price);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void initDate() {
        Map<String, Object> params = new HashMap<>();
        params.put("custOrderNum", order_id);
        Subscription subscription = Network.getInstance("订单详情", this)
                .order_detail(params,
                        new ProgressSubscriber<>("订单详情", new SubscriberOnNextListener<Bean<OrderDetailEntity>>() {
                            @Override
                            public void onNext(Bean<OrderDetailEntity> result) {
                                double sum = 0.0;
//                                price = result.getData().getOrderList()
//                                        .get(0).get(0).getTotalPrice();
                                set_date(result.getData());

                            }

                            @Override
                            public void onError(String error) {

                            }
                        }, this, true));
    }

    private void set_date(OrderDetailEntity data) {
        if (data.getOrderList().get(0).get(0).getPayType() == 1){
            zhifu_type.setText("支付宝");
        }
        if (data.getOrderList().get(0).get(0).getPayType() == 2){
            zhifu_type.setText("微信");
        }

        OrderDetailEntity.OrderListBean orderListBean = data.getOrderList().get(0).get(0);

        if (orderListBean.getCourseType() == 2) {//2表示为私教

            cancel_btn.setVisibility(View.GONE);
            for (OrderDetailEntity.OrderListBean order :
                    data.getOrderList().get(0)) {
                //当status == 0 则表示有未支付的订单
                if (order.getStatus() == 0) {
                    ll_pay.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (orderListBean.getCancelButton() == 1){//为1表示可取消
                cancel_btn.setVisibility(View.VISIBLE);
            } else {
                cancel_btn.setVisibility(View.GONE);
            }
            if (orderListBean.getStatus() == 3 || orderListBean.getStatus() == 2
                    || orderListBean.getStatus() == -1) {
//                cancel_btn.setVisibility(View.GONE);
                ll_pay.setVisibility(View.GONE);
            } else if (orderListBean.getStatus() == 0) {
                ll_pay.setVisibility(View.VISIBLE);
            }

        }
        if (orderListBean.getAreaName().length() > 20){
            changguan_name.setText(orderListBean.getAreaName().substring(0,20)+"...");
        } else {
            changguan_name.setText(orderListBean.getAreaName());
        }

        if (data.getCoupon().size()>0){
            huodong_layout.setVisibility(View.VISIBLE);
            double zhekou = Double.parseDouble(data.getCoupon().get(0).getPriceDiscount());

            price = orderListBean.getTotalPrice()-zhekou;
            tv_huodong_price.setText("-¥"+zhekou);
        } else {
            price = orderListBean.getTotalPrice();
        }

        price = Double.parseDouble(data.getTotalPrice());
        zhufu_status.setText(orderListBean.getFinalStatus());

        gongji_price.setText("¥" + (data.getTotalPrice()));

        if (orderListBean.getStatus() == 1) {//已经支付
            yizhifu_layout.setVisibility(View.VISIBLE);
            yizhifu_layout2.setVisibility(View.VISIBLE);
            ll_pay.setVisibility(View.GONE);
        } else {
            yizhifu_layout.setVisibility(View.GONE);
            yizhifu_layout2.setVisibility(View.VISIBLE);
        }
        if (data.getOrderList().get(0).size() > 1) {
            zhufu_status.setVisibility(View.INVISIBLE);
        } else {
            zhufu_status.setVisibility(View.VISIBLE);
        }
        ItemOrderAdapter2 itemOrderAdapter = new ItemOrderAdapter2(data.getOrderList().get(0), this);
        listview.setAdapter(itemOrderAdapter);
        order_create_time.setText(orderListBean.getCreateDate());
        order_number.setText(orderListBean.getCustOrderItemNum());


        if (orderListBean.getTimeDur() > 0) {
            run_time.initTime();
            //run_time.startTime(14870);
            run_time.startTime(orderListBean.getTimeDur() + 6000);
            run_time.setTimeViewListener(new TimeRunTextView.OnTimeViewListener() {
                @Override
                public void onTimeStart() {
                    Log.e("mcy", "开始倒计时啦");
                }

                @Override
                public void onTimeEnd() {
                    Log.e("mcy", "时间停止了");
                    cancel_btn.setVisibility(View.GONE);
                    ll_pay.setVisibility(View.GONE);
                }

            });
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        run_time.stopTime();
    }

    @Override
    public void onBackPressed() {
        if (BaseUtils.isFastClick()) {
            if (pay == 1002) {
                Intent intent = new Intent(OederDetailActivity.this, OrderManageActivity.class);
                intent.putExtra("pay", 1001);
                startActivity(intent);
            }
            finish();
        }
    }
}
