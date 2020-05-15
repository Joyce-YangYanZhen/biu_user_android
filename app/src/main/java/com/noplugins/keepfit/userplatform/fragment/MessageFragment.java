package com.noplugins.keepfit.userplatform.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.adapter.TabItemAdapter;
import com.noplugins.keepfit.userplatform.base.BaseFragment;
import com.noplugins.keepfit.userplatform.util.MessageEvent;
import com.noplugins.keepfit.userplatform.util.ui.ViewPagerFragment;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MessageFragment extends BaseFragment {

    private View view;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.zhanghu_message_layout)
    RelativeLayout zhanghu_message_layout;
    @BindView(R.id.system_message_layout)
    RelativeLayout system_message_layout;
    @BindView(R.id.user_message_layout)
    RelativeLayout user_message_layout;
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

    private ArrayList<Fragment> mFragments;

    public static MessageFragment newInstance(String title) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.questions_and_answers_fragment, container, false);
            ButterKnife.bind(this, view);//绑定黄牛刀


        }
        return view;
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void upadate(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("jpush_main_enter1")) {//获取消息总数，设置消息总数
            view_pager.setCurrentItem(0);
            setTabTextColorAndImageView(0);
        } else if (messageEvent.getMessage().equals("jpush_main_enter2")) {
            view_pager.setCurrentItem(1);
            setTabTextColorAndImageView(1);
        } else if (messageEvent.getMessage().equals("jpush_main_enter3")) {
            view_pager.setCurrentItem(2);
            setTabTextColorAndImageView(2);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void initView() {
        mFragments = new ArrayList<>();
        mFragments.add(DailyFragment.newInstance("日志消息"));
        mFragments.add(OrderFragment.newInstance("订单消息"));
        mFragments.add(PlatformFragment.newInstance("平台消息"));

        zhanghu_message_layout.setOnClickListener(onClickListener);
        system_message_layout.setOnClickListener(onClickListener);
        user_message_layout.setOnClickListener(onClickListener);


        TabItemAdapter myAdapter = new TabItemAdapter(getChildFragmentManager(), mFragments);// 初始化adapter
        view_pager.setAdapter(myAdapter); // 设置adapter
        setTabTextColorAndImageView(0);// 更改text的颜色还有图片

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
                //tv1.setTextSize(20);
                tv1.setTextColor(getActivity().getResources().getColor(R.color.btn_text_color));
                lin1.setVisibility(View.VISIBLE);
                //tv2.setTextSize(18);
                tv2.setTextColor(getActivity().getResources().getColor(R.color.top_heiziti));
                lin2.setVisibility(View.INVISIBLE);
                //tv3.setTextSize(18);
                tv3.setTextColor(getActivity().getResources().getColor(R.color.top_heiziti));
                lin3.setVisibility(View.INVISIBLE);
                break;
            case 1:
                //tv1.setTextSize(18);
                tv1.setTextColor(getActivity().getResources().getColor(R.color.top_heiziti));
                lin1.setVisibility(View.INVISIBLE);
                //tv2.setTextSize(20);
                tv2.setTextColor(getActivity().getResources().getColor(R.color.btn_text_color));
                lin2.setVisibility(View.VISIBLE);
                //tv3.setTextSize(18);
                tv3.setTextColor(getActivity().getResources().getColor(R.color.top_heiziti));
                lin3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //tv1.setTextSize(18);
                tv1.setTextColor(getActivity().getResources().getColor(R.color.top_heiziti));
                lin1.setVisibility(View.INVISIBLE);
                //tv2.setTextSize(18);
                tv2.setTextColor(getActivity().getResources().getColor(R.color.top_heiziti));
                lin2.setVisibility(View.INVISIBLE);
                //tv3.setTextSize(20);
                tv3.setTextColor(getActivity().getResources().getColor(R.color.btn_text_color));
                lin3.setVisibility(View.VISIBLE);
                break;
        }
    }



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.zhanghu_message_layout:
                    view_pager.setCurrentItem(0);
                    break;
                case R.id.system_message_layout:
                    view_pager.setCurrentItem(1);
                    break;
                case R.id.user_message_layout:
                    view_pager.setCurrentItem(2);
                    break;

            }
        }
    };


}
