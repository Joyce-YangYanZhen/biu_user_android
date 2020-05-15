package com.noplugins.keepfit.userplatform.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.base.BaseActivity;
import com.noplugins.keepfit.userplatform.bean.OrderBean;
import com.noplugins.keepfit.userplatform.entity.CheckInEntity;
import com.noplugins.keepfit.userplatform.entity.DateViewEntity;
import com.noplugins.keepfit.userplatform.global.AppConstants;
import com.noplugins.keepfit.userplatform.util.MessageEvent;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.screen.ScreenUtilsHelper;
import com.noplugins.keepfit.userplatform.util.ui.StepView;
import com.noplugins.keepfit.userplatform.util.ui.erweima.encode.CodeCreator;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import rx.Subscription;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CheckCodeActivity extends BaseActivity {
    @BindView(R.id.sv)
    StepView stepView;
    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.erweima_code)
    ImageView erweima_code;
    @BindView(R.id.order_number)
    TextView order_number;
    @BindView(R.id.xiangmu_name)
    TextView xiangmu_name;
    @BindView(R.id.teacher_name)
    TextView teacher_name;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @BindView(R.id.address_tv)
    TextView address_tv;
    @BindView(R.id.room_tv)
    TextView room_tv;
    @BindView(R.id.lin1)
    RelativeLayout lin1;
    @BindView(R.id.lin2)
    RelativeLayout lin2;
    @BindView(R.id.rl_room)
    RelativeLayout rl_room;

    Bitmap bitmap = null;
    private String oder_number = "";
    private DateViewEntity.CourseListBean orderBean;
    private int select_biaoqing = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initBundle(Bundle parms) {
        if (null != parms) {
            oder_number = parms.getString("order_number");
            orderBean = (DateViewEntity.CourseListBean) parms.getSerializable("order");
            Log.e("传过来的orderid", oder_number);
        }
    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_check_code);
        ButterKnife.bind(this);
        isShowTitle(false);
        EventBus.getDefault().register(this);

        initStepview();

        //生成订单二维码
        init_code_erweima();

        setiing();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upadate(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("show_checkin_pop")) {
            ruchang_popwindow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init_code_erweima() {
        //Bitmap logo = BitmapFactory.decodeResource(getResources(),R.mipmap.changguan);
        String user = oder_number + ":1";
        bitmap = CodeCreator.createQRImage(user, ScreenUtilsHelper.dip2px(this, 60), ScreenUtilsHelper.dip2px(this, 60), null);
//        erweima_code.setImageBitmap(bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        Glide.with(this)
                .load(bytes)
                .centerCrop()
                //.placeholder(R.drawable.logo_gray)
                .into(erweima_code);

    }

    private void initStepview() {
        String[] titles = new String[]{"已下单", "入场", "离场"};
        //设置进度标题
        stepView.setTitles(titles);
        //设置后退
        int step = stepView.getCurrentStep();
        stepView.setCurrentStep((step + 1) % stepView.getStepNum());

      /*//设置前进
        int step = stepView.getCurrentStep();
        //设置进度
        stepView.setCurrentStep(Math.max((step - 1) % stepView.getStepNum(), 0));*/

    }


    private void setiing() {
        if (orderBean.getCourseType() == 3) {
            lin1.setVisibility(View.GONE);
            lin2.setVisibility(View.GONE);
            rl_room.setVisibility(View.GONE);
        } else {
            lin1.setVisibility(View.VISIBLE);
            lin2.setVisibility(View.VISIBLE);
            rl_room.setVisibility(View.VISIBLE);
            room_tv.setText(orderBean.getPlaceName());
        }
        order_number.setText(orderBean.getCustOrderItemNum());
        xiangmu_name.setText(orderBean.getCourseName());
        teacher_name.setText(orderBean.getCoachUserName());
        time_tv.setText(orderBean.getStartTime());
        address_tv.setText(orderBean.getAddress());
    }

    @Override
    public void doBusiness(Context mContext) {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstants.is_check_code_back = true;
                finish();
            }
        });

//        erweima_code.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ruchang_popwindow();
//            }
//        });


        initDate();
    }

    private void initDate() {
        Map<String, Object> params = new HashMap<>();
        params.put("orderItemNum", oder_number);
        Subscription subscription = Network.getInstance("获取checkin数据", this)
                .get_check_in_date(params,
                        new ProgressSubscriber<>("获取checkin数据", new SubscriberOnNextListener<Bean<CheckInEntity>>() {
                            @Override
                            public void onNext(Bean<CheckInEntity> result) {
                                set_date_resource(result.getData());
                            }

                            @Override
                            public void onError(String error) {

                            }
                        }, this, true));
    }

    private void set_date_resource(CheckInEntity data) {
        order_number.setText(data.getCustOrderItemNum());
        xiangmu_name.setText(data.getCourseName());
        teacher_name.setText(data.getCoachUserName());
        time_tv.setText(data.getStartTime());
        address_tv.setText(data.getAddress());
    }


    private void ruchang_popwindow() {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.ruchang_pop, null);
        Dialog m_dialog = new Dialog(this, R.style.transparentFrameWindowStyle2);
        m_dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        Window window = m_dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
        // 设置显示位置
        m_dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        m_dialog.setCanceledOnTouchOutside(true);
        m_dialog.show();
        /**操作*/
        TextView cancel_tv = view.findViewById(R.id.cancel_tv);
        TextView sure_tv = view.findViewById(R.id.sure_tv);
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_dialog.dismiss();
            }
        });
        sure_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_dialog.dismiss();
                if (select_biaoqing > 0) {
                    set_biaoqing_value();
                }
            }
        });

        ImageView img_set = view.findViewById(R.id.img_set);
        ImageView xiaolian1 = view.findViewById(R.id.xiaolian1);
        ImageView xiaolian2 = view.findViewById(R.id.xiaolian2);
        ImageView xiaolian3 = view.findViewById(R.id.xiaolian3);
        ImageView xiaolian4 = view.findViewById(R.id.xiaolian4);
        ImageView xiaolian5 = view.findViewById(R.id.xiaolian5);
        xiaolian1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_set.setImageResource(R.drawable.xiaolian1);
                select_biaoqing = 1;
            }
        });
        xiaolian2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_set.setImageResource(R.drawable.xiaolian2);
                select_biaoqing = 2;
            }
        });
        xiaolian3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_set.setImageResource(R.drawable.xiaolian3);
                select_biaoqing = 3;
            }
        });
        xiaolian4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_set.setImageResource(R.drawable.xiaolian4);
                select_biaoqing = 4;
            }
        });
        xiaolian5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_set.setImageResource(R.drawable.xiaolian5);
                select_biaoqing = 5;
            }
        });

    }

    private void set_biaoqing_value() {
        Map<String, Object> params = new HashMap<>();
        params.put("ordNum", oder_number);
        params.put("beforeFace", select_biaoqing);

        Subscription subscription = Network.getInstance("入场添加标签", this)
                .beforeSportFace(params,
                        new ProgressSubscriber<>("入场添加标签", new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                                MessageEvent messageEvent = new MessageEvent("update_biaoqin");//首页刷新数据
                                EventBus.getDefault().postSticky(messageEvent);

                                Toast.makeText(CheckCodeActivity.this, "记录成功啦~", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String error) {

                            }
                        }, this, true));
    }

}
