// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CollectionActivity_ViewBinding implements Unbinder {
  private CollectionActivity target;

  @UiThread
  public CollectionActivity_ViewBinding(CollectionActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CollectionActivity_ViewBinding(CollectionActivity target, View source) {
    this.target = target;

    target.view_pager = Utils.findRequiredViewAsType(source, R.id.view_pager, "field 'view_pager'", ViewPager.class);
    target.zhanghu_message_layout = Utils.findRequiredViewAsType(source, R.id.zhanghu_message_layout, "field 'zhanghu_message_layout'", RelativeLayout.class);
    target.system_message_layout = Utils.findRequiredViewAsType(source, R.id.system_message_layout, "field 'system_message_layout'", RelativeLayout.class);
    target.lin1 = Utils.findRequiredViewAsType(source, R.id.lin1, "field 'lin1'", LinearLayout.class);
    target.lin2 = Utils.findRequiredViewAsType(source, R.id.lin2, "field 'lin2'", LinearLayout.class);
    target.tv1 = Utils.findRequiredViewAsType(source, R.id.tv1, "field 'tv1'", TextView.class);
    target.tv2 = Utils.findRequiredViewAsType(source, R.id.tv2, "field 'tv2'", TextView.class);
    target.circle1 = Utils.findRequiredViewAsType(source, R.id.circle1, "field 'circle1'", ImageView.class);
    target.circle2 = Utils.findRequiredViewAsType(source, R.id.circle2, "field 'circle2'", ImageView.class);
    target.back_btn = Utils.findRequiredViewAsType(source, R.id.back_btn, "field 'back_btn'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CollectionActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.view_pager = null;
    target.zhanghu_message_layout = null;
    target.system_message_layout = null;
    target.lin1 = null;
    target.lin2 = null;
    target.tv1 = null;
    target.tv2 = null;
    target.circle1 = null;
    target.circle2 = null;
    target.back_btn = null;
  }
}
