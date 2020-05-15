// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.fragment;

import android.view.View;
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

public class MessageFragment_ViewBinding implements Unbinder {
  private MessageFragment target;

  @UiThread
  public MessageFragment_ViewBinding(MessageFragment target, View source) {
    this.target = target;

    target.view_pager = Utils.findRequiredViewAsType(source, R.id.view_pager, "field 'view_pager'", ViewPager.class);
    target.zhanghu_message_layout = Utils.findRequiredViewAsType(source, R.id.zhanghu_message_layout, "field 'zhanghu_message_layout'", RelativeLayout.class);
    target.system_message_layout = Utils.findRequiredViewAsType(source, R.id.system_message_layout, "field 'system_message_layout'", RelativeLayout.class);
    target.user_message_layout = Utils.findRequiredViewAsType(source, R.id.user_message_layout, "field 'user_message_layout'", RelativeLayout.class);
    target.lin1 = Utils.findRequiredViewAsType(source, R.id.lin1, "field 'lin1'", LinearLayout.class);
    target.lin2 = Utils.findRequiredViewAsType(source, R.id.lin2, "field 'lin2'", LinearLayout.class);
    target.lin3 = Utils.findRequiredViewAsType(source, R.id.lin3, "field 'lin3'", LinearLayout.class);
    target.tv1 = Utils.findRequiredViewAsType(source, R.id.tv1, "field 'tv1'", TextView.class);
    target.tv2 = Utils.findRequiredViewAsType(source, R.id.tv2, "field 'tv2'", TextView.class);
    target.tv3 = Utils.findRequiredViewAsType(source, R.id.tv3, "field 'tv3'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MessageFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.view_pager = null;
    target.zhanghu_message_layout = null;
    target.system_message_layout = null;
    target.user_message_layout = null;
    target.lin1 = null;
    target.lin2 = null;
    target.lin3 = null;
    target.tv1 = null;
    target.tv2 = null;
    target.tv3 = null;
  }
}
