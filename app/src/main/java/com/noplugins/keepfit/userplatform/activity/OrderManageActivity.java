package com.noplugins.keepfit.userplatform.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.noplugins.keepfit.userplatform.MainActivity;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.adapter.TabItemAdapter;
import com.noplugins.keepfit.userplatform.base.BaseActivity;
import com.noplugins.keepfit.userplatform.fragment.HavePayFragment;
import com.noplugins.keepfit.userplatform.fragment.OrderAllFragment;
import com.noplugins.keepfit.userplatform.fragment.WaitPayFragment;
import com.noplugins.keepfit.userplatform.util.BaseUtils;

import java.util.ArrayList;

public class OrderManageActivity extends BaseActivity {
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
    @BindView(R.id.circle1)
    ImageView circle1;
    @BindView(R.id.circle2)
    ImageView circle2;
    @BindView(R.id.circle3)
    ImageView circle3;
    @BindView(R.id.back_btn)
    ImageView back_btn;

    private ArrayList<Fragment> mFragments;

    @Override
    public void initBundle(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_order_manage);
        ButterKnife.bind(this);
        isShowTitle(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void doBusiness(Context mContext) {
        mFragments = new ArrayList<>();
        mFragments.add(OrderAllFragment.newInstance("全部"));
        mFragments.add(WaitPayFragment.newInstance("待支付"));
        mFragments.add(HavePayFragment.newInstance("已支付"));

        zhanghu_message_layout.setOnClickListener(onClickListener);
        system_message_layout.setOnClickListener(onClickListener);
        user_message_layout.setOnClickListener(onClickListener);


        TabItemAdapter myAdapter = new TabItemAdapter(getSupportFragmentManager(), mFragments);// 初始化adapter
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
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseUtils.isFastClick()){
                    if (getIntent().getIntExtra("pay",-1) == 1001){
                        Intent intent = new Intent(OrderManageActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();

                }
            }
        });

    }

    private void setTabTextColorAndImageView(int position) {
        switch (position) {
            case 0:
                //tv1.setTextSize(20);
                tv1.setTextColor(getResources().getColor(R.color.btn_text_color));
                lin1.setVisibility(View.VISIBLE);
                //tv2.setTextSize(18);
                tv2.setTextColor(getResources().getColor(R.color.top_heiziti));
                lin2.setVisibility(View.INVISIBLE);
                //tv3.setTextSize(18);
                tv3.setTextColor(getResources().getColor(R.color.top_heiziti));
                lin3.setVisibility(View.INVISIBLE);
                break;
            case 1:
                //tv1.setTextSize(18);
                tv1.setTextColor(getResources().getColor(R.color.top_heiziti));
                lin1.setVisibility(View.INVISIBLE);
                //tv2.setTextSize(20);
                tv2.setTextColor(getResources().getColor(R.color.btn_text_color));
                lin2.setVisibility(View.VISIBLE);
                //tv3.setTextSize(18);
                tv3.setTextColor(getResources().getColor(R.color.top_heiziti));
                lin3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //tv1.setTextSize(18);
                tv1.setTextColor(getResources().getColor(R.color.top_heiziti));
                lin1.setVisibility(View.INVISIBLE);
                //tv2.setTextSize(18);
                tv2.setTextColor(getResources().getColor(R.color.top_heiziti));
                lin2.setVisibility(View.INVISIBLE);
                //tv3.setTextSize(20);
                tv3.setTextColor(getResources().getColor(R.color.btn_text_color));
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


    @Override
    public void onBackPressed() {
        if (BaseUtils.isFastClick()){
            if (getIntent().getIntExtra("pay",-1) == 1001){
                Intent intent = new Intent(OrderManageActivity.this, MainActivity.class);
                startActivity(intent);
            }
            finish();

        }
    }
}
