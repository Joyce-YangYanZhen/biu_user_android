package com.noplugins.keepfit.userplatform.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.adapter.HavePayAdapter;
import com.noplugins.keepfit.userplatform.base.BaseFragment;
import com.noplugins.keepfit.userplatform.entity.OrderEntity;
import com.noplugins.keepfit.userplatform.global.AppConstants;
import com.noplugins.keepfit.userplatform.util.SpUtils;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import rx.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HavePayFragment extends BaseFragment {


    @BindView(R.id.xrefreshview)
    SmartRefreshLayout xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    private View view;
    private LinearLayoutManager layoutManager;
    private HavePayAdapter havePayAdapter;
    private int page = 1;
    private List<List<OrderEntity.OrderListBean>> messageBeans = new ArrayList<>();
    private int maxPage;
    private boolean is_not_more;

    public static HavePayFragment newInstance(String title) {
        HavePayFragment fragment = new HavePayFragment();
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
            view = inflater.inflate(R.layout.fragment_have_pay, container, false);
            ButterKnife.bind(this, view);//绑定黄牛刀
            EventBus.getDefault().register(this);
        }
        return view;
    }

    @Override
    protected void onFragmentFirstVisible() {
        set_list_resource();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
//        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upadate(String messageEvent) {
        //这里进行网络请求会导致多次重复请求
        if (messageEvent.equals("update_status")) {//获取消息总数，设置消息总数
            page = 1;
//            messageBeans.clear();
            initMessageDate();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        set_list_resource();
//        initView();
    }

    private void initMessageDate() {
        Map<String, Object> params = new HashMap<>();
        if ("".equals(SpUtils.getString(getActivity(), AppConstants.USER_NAME))) {

        } else {
            String user_id = SpUtils.getString(getActivity(), AppConstants.USER_NAME);
            params.put("userNum", user_id);//用户编号
        }
        params.put("status", "1");
        params.put("page", page);
        Subscription subscription = Network.getInstance("已支付订单列表", getActivity())
                .get_order_list(params,
                        new ProgressSubscriber<>("已支付订单列表", new SubscriberOnNextListener<Bean<OrderEntity>>() {
                            @Override
                            public void onNext(Bean<OrderEntity> result) {
                                if (page == 1) {
                                    messageBeans.clear();
                                    messageBeans.addAll(result.getData().getOrderList());
                                    havePayAdapter.notifyDataSetChanged();
                                } else  {
                                    messageBeans.addAll(result.getData().getOrderList());
                                    havePayAdapter.notifyDataSetChanged();
                                }

                                maxPage = result.getData().getMaxPage();
                                if (page >= maxPage) {
                                    xrefreshview.setEnableLoadMore(false);
                                }

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("PayStatus", "已支付列表异常:" + error);
                            }
                        }, getActivity(), false));
    }

    private void initView() {
        initMessageDate();//获取消息列表
    }

    private void set_list_resource() {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(false);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        havePayAdapter = new HavePayAdapter(messageBeans, getActivity(), layoutManager);
        recycler_view.setAdapter(havePayAdapter);
        xrefreshview.setEnableLoadMore(true);
        xrefreshview.setEnableRefresh(true);
        xrefreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                initMessageDate();
                xrefreshview.setEnableLoadMore(true);
                xrefreshview.finishRefresh();
            }
        });

        xrefreshview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initMessageDate();
                xrefreshview.finishLoadMore();
            }
        });
    }

}
