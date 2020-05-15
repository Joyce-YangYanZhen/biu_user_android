package com.noplugins.keepfit.userplatform.fragment;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.analytics.android.api.CountEvent;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.activity.SearchActivity;
import com.noplugins.keepfit.userplatform.activity.dialog.SelectLocationActivity;
import com.noplugins.keepfit.userplatform.adapter.TabItemAdapter;
import com.noplugins.keepfit.userplatform.callback.MessageEvent;
import com.noplugins.keepfit.userplatform.fragment.find.ChangguanFragment;
import com.noplugins.keepfit.userplatform.fragment.find.PrivateFragment;
import com.noplugins.keepfit.userplatform.fragment.find.TeamFragment;
import com.noplugins.keepfit.userplatform.global.AppConstants;
import com.noplugins.keepfit.userplatform.util.BaseUtils;
import com.noplugins.keepfit.userplatform.util.SpUtils;
import com.noplugins.keepfit.userplatform.util.ui.ViewPagerFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
 * 首页
 */
public class FindFragment extends ViewPagerFragment {

    private View view;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.rl_private)
    RelativeLayout rl_private;
    @BindView(R.id.rl_changguan)
    RelativeLayout rl_changguan;
    @BindView(R.id.rl_team)
    RelativeLayout rl_team;
    @BindView(R.id.lin1)
    LinearLayout lin1;
    @BindView(R.id.lin2)
    LinearLayout lin2;
    @BindView(R.id.lin3)
    LinearLayout lin3;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;

    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;


    private ArrayList<Fragment> mFragments;

    public static FindFragment findInstance(String title) {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_find, container, false);
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
    public void onEvent(MessageEvent data) {
        Log.d("aaa", "qqq:" + data.getAddress());
        if (!data.getAddress().equals("")) {
            tv_location.setText(data.getAddress());
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "跳转到地址", Toast.LENGTH_SHORT).show();
                if (BaseUtils.isFastClick()) {
                    //定位埋点
                    CountEvent cEvent = new CountEvent("跳转到地址");
                    cEvent.addKeyValue("user",
                            SpUtils.getString(getActivity(), AppConstants.USER_NAME));
                    ///////////////////////////////
                    Intent intent = new Intent(getActivity(), SelectLocationActivity.class);
                    intent.putExtra("address", tv_location.getText().toString());
                    startActivity(intent);
                }
            }
        });

        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseUtils.isFastClick()) {
                    //搜索埋点
                    CountEvent cEvent = new CountEvent("跳转到搜索");
                    cEvent.addKeyValue("user",
                            SpUtils.getString(getActivity(), AppConstants.USER_NAME));
                    ///////////////////////////////
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                }
//                Toast.makeText(getContext(),"跳转到搜索",Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initView() {
        mFragments = new ArrayList<>();
        mFragments.add(PrivateFragment.Companion.newInstance("私教"));
        mFragments.add(ChangguanFragment.Companion.newInstance("场馆"));
        mFragments.add(TeamFragment.Companion.newInstance("团课"));

        rl_private.setOnClickListener(onClickListener);
        rl_changguan.setOnClickListener(onClickListener);
        rl_team.setOnClickListener(onClickListener);


        TabItemAdapter myAdapter = new TabItemAdapter(getChildFragmentManager(), mFragments);// 初始化adapter
        view_pager.setAdapter(myAdapter); // 设置adapter
        view_pager.setCurrentItem(1);
        setTabTextColorAndImageView(1);// 更改text的颜色还有图片
        view_pager.setOffscreenPageLimit(2);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPs) {
            }

            @Override
            public void onPageSelected(int position) {
                setTabTextColorAndImageView(position);// 更改text的颜色还有图片
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void setTabTextColorAndImageView(int position) {
        switch (position) {
            case 0:
                tv1.setTextColor(getActivity().getResources().getColor(R.color.btn_text_color));
                lin1.setVisibility(View.VISIBLE);
                tv2.setTextColor(getActivity().getResources().getColor(R.color.top_heiziti));
                lin2.setVisibility(View.GONE);
                tv3.setTextColor(getActivity().getResources().getColor(R.color.top_heiziti));
                lin3.setVisibility(View.GONE);
                break;
            case 1:
                tv1.setTextColor(getActivity().getResources().getColor(R.color.top_heiziti));
                lin1.setVisibility(View.GONE);
                tv2.setTextColor(getActivity().getResources().getColor(R.color.btn_text_color));
                lin2.setVisibility(View.VISIBLE);
                tv3.setTextColor(getActivity().getResources().getColor(R.color.top_heiziti));
                lin3.setVisibility(View.GONE);
                break;
            case 2:
                tv1.setTextColor(getActivity().getResources().getColor(R.color.top_heiziti));
                lin1.setVisibility(View.GONE);
                tv2.setTextColor(getActivity().getResources().getColor(R.color.top_heiziti));
                lin2.setVisibility(View.GONE);
                tv3.setTextColor(getActivity().getResources().getColor(R.color.btn_text_color));
                lin3.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void fetchData() {

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_private:
                    //搜索埋点
                    CountEvent cEvent = new CountEvent("私教Tab");
                    cEvent.addKeyValue("user",
                            SpUtils.getString(getActivity(), AppConstants.USER_NAME));
                    ///////////////////////////////
                    view_pager.setCurrentItem(0);
                    break;
                case R.id.rl_changguan:
                    //搜索埋点
                    CountEvent cEvent1 = new CountEvent("场馆Tab");
                    cEvent1.addKeyValue("user",
                            SpUtils.getString(getActivity(), AppConstants.USER_NAME));
                    ///////////////////////////////
                    view_pager.setCurrentItem(1);
                    break;
                case R.id.rl_team:
                    //搜索埋点
                    CountEvent cEvent3 = new CountEvent("团课Tab");
                    cEvent3.addKeyValue("user",
                            SpUtils.getString(getActivity(), AppConstants.USER_NAME));
                    ///////////////////////////////
                    view_pager.setCurrentItem(2);

                    break;
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();

    }


}
