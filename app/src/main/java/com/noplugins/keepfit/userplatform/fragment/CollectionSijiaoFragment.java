package com.noplugins.keepfit.userplatform.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.activity.teahcher.PrivateDetailActivity;
import com.noplugins.keepfit.userplatform.adapter.CollectionSijiaoAdapter;
import com.noplugins.keepfit.userplatform.base.BaseFragment;
import com.noplugins.keepfit.userplatform.entity.CollectionSIjiaoEntity;
import com.noplugins.keepfit.userplatform.global.AppConstants;
import com.noplugins.keepfit.userplatform.util.BaseUtils;
import com.noplugins.keepfit.userplatform.util.SpUtils;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import rx.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CollectionSijiaoFragment extends BaseFragment {
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    private View view;
    private LinearLayoutManager layoutManager;
    private CollectionSijiaoAdapter collectionSijiaoAdapter;
    private int page = 1;
    private List<CollectionSIjiaoEntity.TeacherListBean> messageBeans = new ArrayList<>();
    private int maxPage;
    private boolean is_not_more;

    public static CollectionSijiaoFragment newInstance(String title) {
        CollectionSijiaoFragment fragment = new CollectionSijiaoFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_collection_sijiao, container, false);
            ButterKnife.bind(this, view);//绑定黄牛刀
//            initView();
        }
        return view;
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        initMessageDate();
    }

//    @Override
//    protected void onFragmentVisibleChange(boolean isVisible) {
//        super.onFragmentVisibleChange(isVisible);
//        initMessageDate();
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        set_list_resource();
    }

    @Override
    public void onResume() {
        super.onResume();
        initMessageDate();
    }

    private void initView() {
        initMessageDate();
    }

    private void initMessageDate() {
        Map<String, Object> params = new HashMap<>();
        if ("".equals(SpUtils.getString(getActivity(), AppConstants.USER_NAME))) {
        } else {
            String user_id = SpUtils.getString(getActivity(), AppConstants.USER_NAME);
            params.put("userNum", user_id);//用户编号
        }
        params.put("type", "2");//用户编号
        params.put("page", page);
        if ("".equals(SpUtils.getString(getActivity(), AppConstants.LON))) {
        } else {
            String jingdu = SpUtils.getString(getActivity(), AppConstants.LON);
            params.put("longitude", jingdu);
        }
        if ("".equals(SpUtils.getString(getActivity(), AppConstants.LON))) {
        } else {
            String weidu = SpUtils.getString(getActivity(), AppConstants.LAT);
            params.put("latitude", weidu);
        }
        Subscription subscription = Network.getInstance("私教收藏", getActivity())
                .get_collection_sijiao(params,
                        new ProgressSubscriber<>("私教收藏", new SubscriberOnNextListener<Bean<CollectionSIjiaoEntity>>() {
                            @Override
                            public void onNext(Bean<CollectionSIjiaoEntity> result) {

                                if (page == 1){
                                    messageBeans.clear();
                                }
                                messageBeans.addAll(result.getData().getTeacherList());
                                collectionSijiaoAdapter.notifyDataSetChanged();
//                                maxPage = entity.getMaxPage();
//                                if (page == 1) {//表示刷新
//                                    messageBeans.addAll(entity.getList());
//                                    set_list_resource(messageBeans);
//                                } else {
//                                    if (page <= maxPage) {//表示加载还有数据
//                                        is_not_more = false;
//                                        messageBeans.addAll(entity.getList());
//                                        collectionChangguangAdapter.notifyDataSetChanged();
//
//                                    } else {//表示没有更多数据了
//                                        is_not_more = true;
//                                        messageBeans.addAll(entity.getList());
//                                        collectionChangguangAdapter.notifyDataSetChanged();
//                                    }
//                                }

                            }

                            @Override
                            public void onError(String error) {
                            }
                        }, getActivity(), true));
    }


    private void set_list_resource() {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(false);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        collectionSijiaoAdapter = new CollectionSijiaoAdapter(messageBeans, getActivity());
        recycler_view.setAdapter(collectionSijiaoAdapter);
        collectionSijiaoAdapter.setOnItemClickListener(new CollectionSijiaoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (BaseUtils.isFastClick()){
                    Intent intent = new Intent(getActivity(), PrivateDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("teacherNum", messageBeans.get(position).getTeacherNum());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
        // 静默加载模式不能设置footerview
        // 设置静默加载模式
        //xrefreshview.setSilenceLoadMore(true);
        //设置刷新完成以后，headerview固定的时间
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        //xrefreshview.setPullRefreshEnable(true);
        xrefreshview.setPullLoadEnable(true);//关闭加载更多
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        //给recycler_view设置底部加载布局
        if (messageBeans.size() > 9) {
            xrefreshview.enableReleaseToLoadMore(true);
            collectionSijiaoAdapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));//加载更多
            xrefreshview.setLoadComplete(false);//显示底部
        } else {
            xrefreshview.enableReleaseToLoadMore(false);
            xrefreshview.setLoadComplete(true);//隐藏底部
        }
        //设置静默加载时提前加载的item个数
//        xefreshView1.setPreLoadCount(4);

        xrefreshview.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        messageBeans.clear();
                        initMessageDate();
                        xrefreshview.stopRefresh();//刷新停止


                    }
                }, 1000);//2000是刷新的延时，使得有个动画效果
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = page + 1;
                        initMessageDate();
                        //填写加载更多的网络请求，一般page++
//                        //没有更多数据时候
                        if (is_not_more) {
                            xrefreshview.setLoadComplete(true);
                        } else {
                            //刷新完成必须调用此方法停止加载
                            xrefreshview.stopLoadMore(true);
                        }


                    }
                }, 1000);//1000是加载的延时，使得有个动画效果


            }
        });
    }


}
