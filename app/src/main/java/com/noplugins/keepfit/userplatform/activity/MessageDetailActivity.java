package com.noplugins.keepfit.userplatform.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.base.BaseActivity;
import com.noplugins.keepfit.userplatform.entity.MessageDateEntity;

public class MessageDetailActivity extends BaseActivity {
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.message_title)
    TextView message_title;
    @BindView(R.id.content_tv)
    TextView content_tv;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @BindView(R.id.back_btn)
    ImageView back_btn;

    private MessageDateEntity.ListBean message_item = new MessageDateEntity.ListBean();

    @Override
    public void initBundle(Bundle parms) {
        if (null != parms) {
            message_item = parms.getParcelable("message_item");
        }
    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        isShowTitle(false);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void doBusiness(Context mContext) {

        //设置数据
        Glide.with(this).load(R.drawable.message_detail_icon).into(icon);

        content_tv.setText(message_item.getMessageCon());

        time_tv.setText(message_item.getCreateDate());
    }
}
