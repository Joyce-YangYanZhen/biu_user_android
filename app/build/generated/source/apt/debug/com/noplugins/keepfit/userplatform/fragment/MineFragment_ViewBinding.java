// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.fragment;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MineFragment_ViewBinding implements Unbinder {
  private MineFragment target;

  @UiThread
  public MineFragment_ViewBinding(MineFragment target, View source) {
    this.target = target;

    target.grid_view = Utils.findRequiredViewAsType(source, R.id.grid_view, "field 'grid_view'", GridView.class);
    target.touxiang_image = Utils.findRequiredViewAsType(source, R.id.touxiang_image, "field 'touxiang_image'", CircleImageView.class);
    target.user_name = Utils.findRequiredViewAsType(source, R.id.user_name, "field 'user_name'", TextView.class);
    target.sex_img = Utils.findRequiredViewAsType(source, R.id.sex_img, "field 'sex_img'", ImageView.class);
    target.qianming_tv = Utils.findRequiredViewAsType(source, R.id.qianming_tv, "field 'qianming_tv'", TextView.class);
    target.call_kefu = Utils.findRequiredViewAsType(source, R.id.call_kefu, "field 'call_kefu'", LinearLayout.class);
    target.travel_daily_btn = Utils.findRequiredViewAsType(source, R.id.travel_daily_btn, "field 'travel_daily_btn'", LinearLayout.class);
    target.body_resouce = Utils.findRequiredViewAsType(source, R.id.body_resouce, "field 'body_resouce'", LinearLayout.class);
    target.order_manage_btn = Utils.findRequiredViewAsType(source, R.id.order_manage_btn, "field 'order_manage_btn'", LinearLayout.class);
    target.mine_collection = Utils.findRequiredViewAsType(source, R.id.mine_collection, "field 'mine_collection'", LinearLayout.class);
    target.advise_btn = Utils.findRequiredViewAsType(source, R.id.advise_btn, "field 'advise_btn'", LinearLayout.class);
    target.ll_info = Utils.findRequiredViewAsType(source, R.id.ll_info, "field 'll_info'", LinearLayout.class);
    target.btn_setting = Utils.findRequiredViewAsType(source, R.id.btn_setting, "field 'btn_setting'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MineFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.grid_view = null;
    target.touxiang_image = null;
    target.user_name = null;
    target.sex_img = null;
    target.qianming_tv = null;
    target.call_kefu = null;
    target.travel_daily_btn = null;
    target.body_resouce = null;
    target.order_manage_btn = null;
    target.mine_collection = null;
    target.advise_btn = null;
    target.ll_info = null;
    target.btn_setting = null;
  }
}
