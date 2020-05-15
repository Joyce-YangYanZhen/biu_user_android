// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.util.ui.MyListView;
import com.noplugins.keepfit.userplatform.util.ui.TimeRunTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OederDetailActivity_ViewBinding implements Unbinder {
  private OederDetailActivity target;

  @UiThread
  public OederDetailActivity_ViewBinding(OederDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public OederDetailActivity_ViewBinding(OederDetailActivity target, View source) {
    this.target = target;

    target.back_btn = Utils.findRequiredViewAsType(source, R.id.back_btn, "field 'back_btn'", ImageView.class);
    target.cancel_btn = Utils.findRequiredViewAsType(source, R.id.cancel_btn, "field 'cancel_btn'", LinearLayout.class);
    target.ll_pay = Utils.findRequiredViewAsType(source, R.id.ll_pay, "field 'll_pay'", LinearLayout.class);
    target.run_time = Utils.findRequiredViewAsType(source, R.id.run_time, "field 'run_time'", TimeRunTextView.class);
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", MyListView.class);
    target.changguan_name = Utils.findRequiredViewAsType(source, R.id.changguan_name, "field 'changguan_name'", TextView.class);
    target.changguan_btn = Utils.findRequiredViewAsType(source, R.id.changguan_btn, "field 'changguan_btn'", LinearLayout.class);
    target.zhufu_status = Utils.findRequiredViewAsType(source, R.id.zhufu_status, "field 'zhufu_status'", TextView.class);
    target.gongji_price = Utils.findRequiredViewAsType(source, R.id.gongji_price, "field 'gongji_price'", TextView.class);
    target.quanyi_price = Utils.findRequiredViewAsType(source, R.id.quanyi_price, "field 'quanyi_price'", TextView.class);
    target.gongji_tv2 = Utils.findRequiredViewAsType(source, R.id.gongji_tv2, "field 'gongji_tv2'", TextView.class);
    target.zhifu_type = Utils.findRequiredViewAsType(source, R.id.zhifu_type, "field 'zhifu_type'", TextView.class);
    target.order_number = Utils.findRequiredViewAsType(source, R.id.order_number, "field 'order_number'", TextView.class);
    target.tv_huodong_price = Utils.findRequiredViewAsType(source, R.id.tv_huodong_price, "field 'tv_huodong_price'", TextView.class);
    target.order_create_time = Utils.findRequiredViewAsType(source, R.id.order_create_time, "field 'order_create_time'", TextView.class);
    target.yizhifu_layout = Utils.findRequiredViewAsType(source, R.id.yizhifu_layout, "field 'yizhifu_layout'", RelativeLayout.class);
    target.yizhifu_layout2 = Utils.findRequiredViewAsType(source, R.id.yizhifu_layout2, "field 'yizhifu_layout2'", LinearLayout.class);
    target.huodong_layout = Utils.findRequiredViewAsType(source, R.id.huodong_layout, "field 'huodong_layout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    OederDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back_btn = null;
    target.cancel_btn = null;
    target.ll_pay = null;
    target.run_time = null;
    target.listview = null;
    target.changguan_name = null;
    target.changguan_btn = null;
    target.zhufu_status = null;
    target.gongji_price = null;
    target.quanyi_price = null;
    target.gongji_tv2 = null;
    target.zhifu_type = null;
    target.order_number = null;
    target.tv_huodong_price = null;
    target.order_create_time = null;
    target.yizhifu_layout = null;
    target.yizhifu_layout2 = null;
    target.huodong_layout = null;
  }
}
