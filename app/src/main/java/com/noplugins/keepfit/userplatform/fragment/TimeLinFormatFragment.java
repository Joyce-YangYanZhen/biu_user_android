package com.noplugins.keepfit.userplatform.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.activity.AdviceActivity;
import com.noplugins.keepfit.userplatform.activity.CheckCodeActivity;
import com.noplugins.keepfit.userplatform.adapter.TimeClassAdapter;
import com.noplugins.keepfit.userplatform.entity.CalenderEntity;
import com.noplugins.keepfit.userplatform.entity.DateViewEntity;
import com.noplugins.keepfit.userplatform.global.AppConstants;
import com.noplugins.keepfit.userplatform.util.BaseUtils;
import com.noplugins.keepfit.userplatform.util.ShareUtils;
import com.noplugins.keepfit.userplatform.util.SpUtils;
import com.noplugins.keepfit.userplatform.util.data.DateHelper;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.screen.ScreenUtilsHelper;
import com.noplugins.keepfit.userplatform.util.ui.ViewPagerFragment;
import com.noplugins.keepfit.userplatform.util.ui.erweima.encode.CodeCreator;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.bean.MothEntity;
import com.othershe.calendarview.listener.OnPagerChangeListener;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.utils.CalendarUtil;
import com.othershe.calendarview.weiget.CalendarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import okhttp3.RequestBody;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import rx.Subscription;
import www.linwg.org.lib.LCardView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.umeng.socialize.net.dplus.CommonNetImpl.TAG;


public class TimeLinFormatFragment extends ViewPagerFragment {
    @BindView(R.id.calendar)
    CalendarView calendar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_now_select)
    TextView tv_now_select;
    @BindView(R.id.tv_now_week)
    TextView tv_now_week;
    @BindView(R.id.last_btn)
    LinearLayout last_btn;
    @BindView(R.id.next_btn)
    LinearLayout next_btn;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.share_btn)
    ImageView share_btn;
    @BindView(R.id.clander_layout)
    LCardView clander_layout;
    @BindView(R.id.title_layout)
    LinearLayout title_layout;
    @BindView(R.id.left_img)
    ImageView left_img;
    @BindView(R.id.right_img)
    ImageView right_img;
    @BindView(R.id.xiala_img)
    ImageView xiala_img;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private View view;
    private int[] cDate = CalendarUtil.getCurrentDate();
    List<MothEntity.DataBean> dataBeans = new ArrayList<>();
    private TimeClassAdapter timeClassAdapter;
    private String select_date_str = "";

    List<DateViewEntity.CourseListBean> dates = new ArrayList<>();

    public static TimeLinFormatFragment newInstance(String title) {
        TimeLinFormatFragment fragment = new TimeLinFormatFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_time_formart, container, false);
            ButterKnife.bind(this, view);//绑定黄牛刀
            EventBus.getDefault().register(this);
            initView();
        }
        return view;
    }


    /**
     * eventBus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String data) {
        if (data.equals("update_biaoqin")) {
            get_date_class_resource(select_date_str);
        }
        if (data.equals("购买了订单")) {
            get_calender_resource();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void initView() {
        //获取网络数据
        get_net_resource();

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseUtils.isFastClick()) {
                    //截屏分享
                     takeScreenShot(getActivity());
//                    Intent intent = new Intent(getActivity(), AdviceActivity.class);
//                    startActivity(intent);
                }
            }
        });

        title_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clander_layout.getVisibility() == View.VISIBLE) {
                    xiala_img.animate().rotation(180);
                    clander_layout.setVisibility(View.GONE);
                    last_btn.setVisibility(View.INVISIBLE);
                    next_btn.setVisibility(View.INVISIBLE);
                } else {
                    xiala_img.animate().rotation(0);
                    clander_layout.setVisibility(View.VISIBLE);
                    last_btn.setVisibility(View.VISIBLE);
                    next_btn.setVisibility(View.VISIBLE);
                }
            }
        });
        //禁用加载
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //获取网络数据
                get_net_resource();
            }
        });

    }



    private void get_net_resource() {
        //获取日历
        get_calender_resource();
        //获取课程
        select_date_str = DateHelper.get_date(cDate[0], cDate[1], cDate[2]);
        tv_now_select.setText(select_date_str);
        tv_now_week.setText(DateHelper.getWeek(select_date_str));
        get_date_class_resource(select_date_str);
    }

    private void get_calender_resource() {
        Map<String, String> params = new HashMap<>();
        params.put("nowYear", "" + cDate[0]);
        if ("".equals(SpUtils.getString(getActivity(), AppConstants.USER_NAME))) {
        } else {
            String user_id = SpUtils.getString(getActivity(), AppConstants.USER_NAME);
            params.put("userNum", user_id);//用户编号
        }
        Gson gson = new Gson();
        String json_params = gson.toJson(params);
        String json = new Gson().toJson(params);//要传递的json
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Log.e("获取日历", "获取日历参数：" + json_params);
        Subscription subscription = Network.getInstance("获取日历", getActivity())
                .get_rili(requestBody,
                        new ProgressSubscriberNew<>(CalenderEntity.class, new GsonSubscriberOnNextListener<CalenderEntity>() {
                            @Override
                            public void on_post_entity(CalenderEntity s, String get_message_id) {
                                Log.e(TAG, "获取日历成功：");
                                init_clander_view(s.getData());
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e(TAG, "获取日历报错：" + error);
                            }
                        }, getActivity(), false));
    }

    /**
     * 截取屏幕
     *
     * @param activity
     */
    public void takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        //获取actiobBar的高度
        float actionBarHeight = activity.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize})
                .getDimension(0, 0);
        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(bitmap, 0, statusBarHeight + (int) actionBarHeight, width, height
                - statusBarHeight - (int) actionBarHeight);
        view.destroyDrawingCache();
        //将bitmap存入本地文件
        //获取系统缓存文件
        File cacheFile = new File(getActivity().getCacheDir(), "ScreenCut");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(cacheFile);
            b.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            if (cacheFile.exists()) {
                Bitmap bitmap_cut = BitmapFactory.decodeFile(getActivity().getCacheDir() + "//ScreenCut");
                //在这里弹出popwindow
                select_more_popwindow(bitmap_cut);
            } else {
                Toast.makeText(getActivity(), "加载图片失败", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void select_more_popwindow(Bitmap bitmap_cut) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        View view = factory.inflate(R.layout.jiepin_share_detail, null);
        Dialog m_dialog = new Dialog(getActivity(), R.style.transparentFrameWindowStyle2);
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
        TextView dismiss_tv = view.findViewById(R.id.dismiss_tv);
        dismiss_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_dialog.dismiss();
            }
        });
        ImageView cut_image = view.findViewById(R.id.cut_image);
        cut_image.setImageBitmap(bitmap_cut);

        LinearLayout weixin = view.findViewById(R.id.ll_wx);
        LinearLayout pengyouquan = view.findViewById(R.id.ll_pyq);
        LinearLayout qq = view.findViewById(R.id.ll_qq);
        LinearLayout weibo = view.findViewById(R.id.ll_wb);
        LinearLayout llBitmap = view.findViewById(R.id.ll_bitmap);
        ImageView erweima = view.findViewById(R.id.iv_erweima);
        initCodeErweima(erweima);

        weixin.setOnClickListener(v -> {
                    if (BaseUtils.isFastClick()) {
                        Bitmap bitmap = ShareUtils.createViewBitmap(llBitmap);
                        ShareUtils.share(getActivity(), bitmap, SHARE_MEDIA.WEIXIN);
                    }
                }
        );
        pengyouquan.setOnClickListener(v -> {
                    if (BaseUtils.isFastClick()) {
                        Bitmap bitmap = ShareUtils.createViewBitmap(llBitmap);
                        ShareUtils.share(getActivity(), bitmap, SHARE_MEDIA.WEIXIN_CIRCLE);
                    }
                }
        );
        qq.setOnClickListener(v -> {
                    if (BaseUtils.isFastClick()) {
//                        Bitmap bitmap = ShareUtils.createViewBitmap(llBitmap);
//                        ShareUtils.share(getActivity(), bitmap, SHARE_MEDIA.QQ);
                        requestPermissions(llBitmap);
                    }

                }
        );
        weibo.setOnClickListener(v -> {
                    if (BaseUtils.isFastClick()) {
                        Bitmap bitmap = ShareUtils.createViewBitmap(llBitmap);
                        ShareUtils.share(getActivity(), bitmap, SHARE_MEDIA.SINA);
                    }
                }
        );
    }



    private int PERMISSION_REQUEST_STOREAGE = 1001;
    private void requestPermissions(LinearLayout layout) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,}, PERMISSION_REQUEST_STOREAGE);
                } else {
                    Bitmap bitmap = ShareUtils.createViewBitmap(layout);
                    ShareUtils.share(getActivity(), bitmap, SHARE_MEDIA.QQ);
                }
            } else {
                Bitmap bitmap = ShareUtils.createViewBitmap(layout);
                ShareUtils.share(getActivity(), bitmap, SHARE_MEDIA.QQ);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            if (requestCode == PERMISSION_REQUEST_STOREAGE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void initCodeErweima(ImageView view) {
        //Bitmap logo = BitmapFactory.decodeResource(getResources(),R.mipmap.changguan);
        Glide.with(getActivity())
                .load(R.drawable.cust)
                .centerCrop()
                .into(view);

    }

    public void get_date_class_resource(String selectdate) {
        Map<String, String> params = new HashMap<>();
        if ("".equals(SpUtils.getString(getActivity(), AppConstants.USER_NAME))) {

        } else {
            String user_id = SpUtils.getString(getActivity(), AppConstants.USER_NAME);
            params.put("userNum", user_id);//用户编号
        }
        params.put("nowDay", selectdate);

        Gson gson = new Gson();
        String json_params = gson.toJson(params);
        String json = new Gson().toJson(params);//要传递的json
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Log.e("获取课程", "获取课程参数：" + json_params);
        Subscription subscription = Network.getInstance("获取课程", getActivity())
                .get_class_resource_date(requestBody,
                        new ProgressSubscriberNew<>(DateViewEntity.class, new GsonSubscriberOnNextListener<DateViewEntity>() {
                            @Override
                            public void on_post_entity(DateViewEntity dateViewEntity, String get_message_id) {
                                Log.e(TAG, "获取课程成功：" + dateViewEntity.getCourseList());
                                if (dates.size() > 0) {
                                    dates.clear();
                                }
                                dates.addAll(dateViewEntity.getCourseList());
                                //设置上拉刷新下拉加载
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                recycler_view.setLayoutManager(layoutManager);
                                timeClassAdapter = new TimeClassAdapter(dates, getActivity(), TimeLinFormatFragment.this);
                                recycler_view.setAdapter(timeClassAdapter);
                                refreshLayout.finishRefresh(true);

                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e(TAG, "获取课程报错：" + error);
                                refreshLayout.finishRefresh(true);
                                Toast.makeText(getActivity(), "获取课程失败！", Toast.LENGTH_SHORT).show();
                            }
                        }, getActivity(), false));

    }


    private void init_clander_view(List<String> strings) {
        title.setText(cDate[0] + "年" + cDate[1] + "月");
        calendar.setStartEndDate("2019.01", "2025.12")
                .setDisableStartEndDate("2019.01.01", "2025.12.30")
                .setInitDate(cDate[0] + "." + cDate[1])
                .setSingleDate(cDate[0] + "." + cDate[1] + "." + cDate[2])
                .init(strings);
        calendar.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean date) {
                //date_tv.setText(DateHelper.get_date2(date.getSolar()[1],date.getSolar()[2]));
                //日视角刷新数据
                select_date_str = DateHelper.get_date(date.getSolar()[0], date.getSolar()[1], date.getSolar()[2]);
                Log.e("选择的日期", select_date_str);
                tv_now_select.setText(select_date_str);
                tv_now_week.setText(DateHelper.getWeek(select_date_str));
                get_date_class_resource(select_date_str);
            }
        });
        calendar.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                title.setText(date[0] + "年" + date[1] + "月");
            }
        });
        last_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.lastMonth();

            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.nextMonth();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppConstants.is_check_code_back = true) {
            get_net_resource();
            AppConstants.is_check_code_back = false;
        }


    }

    @Override
    public void fetchData() {

    }
}
