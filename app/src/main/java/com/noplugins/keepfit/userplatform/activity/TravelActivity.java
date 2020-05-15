package com.noplugins.keepfit.userplatform.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.google.gson.Gson;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.adapter.PopUpAdapter;
import com.noplugins.keepfit.userplatform.adapter.TravelDailryAdapter;
import com.noplugins.keepfit.userplatform.base.BaseActivity;
import com.noplugins.keepfit.userplatform.entity.TraveListItem;
import com.noplugins.keepfit.userplatform.global.AppConstants;
import com.noplugins.keepfit.userplatform.util.BaseUtils;
import com.noplugins.keepfit.userplatform.util.SpUtils;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.ui.pop.SpinnerPopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import lib.demo.spinner.MaterialSpinner;
import okhttp3.RequestBody;
import rx.Subscription;

import java.util.*;

public class TravelActivity extends BaseActivity {

    @BindView(R.id.spinner_year)
    TextView spinner_year;
    @BindView(R.id.spinner_month)
    TextView spinner_month;
    @BindView(R.id.spinner_type)
    TextView spinner_type;
    @BindView(R.id.xrefreshview)
    SmartRefreshLayout xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.back_btn)
    ImageView back_btn;

    private LinearLayoutManager layoutManager;
    private TravelDailryAdapter travelDailryAdapter;
    private int page = 1;
    private boolean is_not_more;
    private int maxPage;
    private List<TraveListItem.SportListBean> messageBeans = new ArrayList<>();

    private String select_year = "";
    private String select_month = "";
    private String select_type = "";

    @Override
    public void initBundle(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_travel);
        ButterKnife.bind(this);
        isShowTitle(false);
    }

    @Override
    public void doBusiness(Context mContext) {
        init_year_date();

        init_month_date();

        init_type_date();
        select_year = Calendar.getInstance().get(Calendar.YEAR)+"年";
        //加载列表数据
//        initMessageDate();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMessageDate();
    }

    private void initMessageDate() {
        Map<String, Object> params = new HashMap<>();
        if ("".equals(SpUtils.getString(this, AppConstants.USER_NAME))) {

        } else {
            String user_id = SpUtils.getString(this, AppConstants.USER_NAME);
            params.put("userNum", user_id);//用户编号
        }
        params.put("page", page);

        if (select_year.length() > 0) {
            params.put("date", select_year.replace("年", ""));
        }
        if (select_month.length() > 0) {
            if (Integer.valueOf(select_month.replace("月", "")) < 10) {
                params.put("month", "0" + select_month.replace("月", ""));
            } else {
                params.put("month", select_month.replace("月", ""));
            }
        }
        if (select_type.length() > 0) {
            if (select_type.equals("场馆")) {
                params.put("courseType", "3");
            } else if (select_type.equals("团课")) {
                params.put("courseType", "1");
            } else if (select_type.equals("私教")) {
                params.put("courseType", "2");
            }
        }

        Gson gson = new Gson();
        String json_params = gson.toJson(params);
        String json = new Gson().toJson(params);//要传递的json
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Log.e(TAG, "运动日志列表参数：" + json_params);
        Subscription subscription = Network.getInstance("运动日志列表", this)
                .yundong_rizhi_liebiao(requestBody, new ProgressSubscriberNew<>(TraveListItem.class, new GsonSubscriberOnNextListener<TraveListItem>() {
                    @Override
                    public void on_post_entity(TraveListItem entity, String s) {
                        if (page == 1){
                            messageBeans.clear();
                        }
                        Log.e("运动日志列表成功", entity + "运动日志列表成功" + entity.getSportList().size());
                        messageBeans.addAll(entity.getSportList());
                        set_list_resource(messageBeans);

                        maxPage = entity.getMaxPage();
                        if (maxPage <= page){
                            xrefreshview.setEnableLoadMore(false);
                        }
//                        if (page == 1) {//表示刷新
//                            messageBeans.addAll(entity.getList());
//                            set_list_resource(messageBeans);
//                        } else {
//                            if (page <= maxPage) {//表示加载还有数据
//                                is_not_more = false;
//                                messageBeans.addAll(entity.getList());
//                                travelDailryAdapter.notifyDataSetChanged();
//
//                            } else {//表示没有更多数据了
//                                is_not_more = true;
//                                messageBeans.addAll(entity.getList());
//                                travelDailryAdapter.notifyDataSetChanged();
//                            }
//                        }


                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("运动日志列表失败", "运动日志列表失败:" + error);
                    }
                }, this, false));


    }


    private void set_list_resource(final List<TraveListItem.SportListBean> dates) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(false);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        travelDailryAdapter = new TravelDailryAdapter(dates, this);
        recycler_view.setAdapter(travelDailryAdapter);
        travelDailryAdapter.setOnItemClickListener(new TravelDailryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (BaseUtils.isFastClick()){
                    Intent intent = new Intent(TravelActivity.this, DailyTravelDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("detail_id", dates.get(position).getSportsNum() + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });


        xrefreshview.setEnableLoadMore(true);
        xrefreshview.setEnableRefresh(true);
        xrefreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        dates.clear();
                        initMessageDate();
                        xrefreshview.finishRefresh();
                        xrefreshview.setEnableRefresh(true);
                    }
                }, 1000);
            }
        });

        xrefreshview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = page + 1;
                        initMessageDate();
                        xrefreshview.finishLoadMore();
                    }
                }, 1000);//1000是加载的延时，使得有个动画效果
            }
        });
    }

    private void showPopwindow(SpinnerPopWindow<String> pop, TextView view){
        pop.setWidth(view.getWidth());
        pop.showAsDropDown(view);

    }
    private  SpinnerPopWindow<String> popWindowYear;
    private  SpinnerPopWindow<String> popWindowMonth;
    private  SpinnerPopWindow<String> popWindowType;
    private void init_type_date() {
        List<String> typeArrays = Arrays.asList(getResources().getStringArray(R.array.type_date));

        popWindowType = new SpinnerPopWindow<String>(this, typeArrays, new PopUpAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object object, int position) {
                select_type = typeArrays.get(position);
                spinner_type.setText(typeArrays.get(position));
                //填写刷新数据的
                page = 1;
                initMessageDate();
                popWindowType.dismiss();
            }
        });

        spinner_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopwindow(popWindowType,spinner_type);
            }
        });
    }

    private void init_month_date() {
        List<String> typeArrays = Arrays.asList(getResources().getStringArray(R.array.month_date));

        popWindowMonth = new SpinnerPopWindow<String>(this, typeArrays, new PopUpAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object object, int position) {
                if (position == 0){
                    select_month = "";
                } else {
                    select_month = typeArrays.get(position);
                }

                spinner_month.setText(typeArrays.get(position));
                //填写刷新数据的
                page = 1;

                initMessageDate();
                popWindowMonth.dismiss();
            }
        });
        spinner_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopwindow(popWindowMonth,spinner_month);
            }
        });
    }

    private void init_year_date() {

        List<String> typeArrays = Arrays.asList(getResources().getStringArray(R.array.year_date));

        popWindowYear = new SpinnerPopWindow<String>(this, typeArrays, new PopUpAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object object, int position) {
                select_year = typeArrays.get(position);
                spinner_year.setText(typeArrays.get(position));
                //填写刷新数据的
                page = 1;
                initMessageDate();
                popWindowYear.dismiss();
            }
        });
        spinner_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopwindow(popWindowYear,spinner_year);
            }
        });
    }


}
