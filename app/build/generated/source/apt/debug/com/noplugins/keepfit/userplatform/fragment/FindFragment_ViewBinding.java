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

public class FindFragment_ViewBinding implements Unbinder {
  private FindFragment target;

  @UiThread
  public FindFragment_ViewBinding(FindFragment target, View source) {
    this.target = target;

    target.view_pager = Utils.findRequiredViewAsType(source, R.id.view_pager, "field 'view_pager'", ViewPager.class);
    target.rl_private = Utils.findRequiredViewAsType(source, R.id.rl_private, "field 'rl_private'", RelativeLayout.class);
    target.rl_changguan = Utils.findRequiredViewAsType(source, R.id.rl_changguan, "field 'rl_changguan'", RelativeLayout.class);
    target.rl_team = Utils.findRequiredViewAsType(source, R.id.rl_team, "field 'rl_team'", RelativeLayout.class);
    target.lin1 = Utils.findRequiredViewAsType(source, R.id.lin1, "field 'lin1'", LinearLayout.class);
    target.lin2 = Utils.findRequiredViewAsType(source, R.id.lin2, "field 'lin2'", LinearLayout.class);
    target.lin3 = Utils.findRequiredViewAsType(source, R.id.lin3, "field 'lin3'", LinearLayout.class);
    target.tv1 = Utils.findRequiredViewAsType(source, R.id.tv1, "field 'tv1'", TextView.class);
    target.tv2 = Utils.findRequiredViewAsType(source, R.id.tv2, "field 'tv2'", TextView.class);
    target.tv3 = Utils.findRequiredViewAsType(source, R.id.tv3, "field 'tv3'", TextView.class);
    target.tv_location = Utils.findRequiredViewAsType(source, R.id.tv_location, "field 'tv_location'", TextView.class);
    target.ll_search = Utils.findRequiredViewAsType(source, R.id.ll_search, "field 'll_search'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FindFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.view_pager = null;
    target.rl_private = null;
    target.rl_changguan = null;
    target.rl_team = null;
    target.lin1 = null;
    target.lin2 = null;
    target.lin3 = null;
    target.tv1 = null;
    target.tv2 = null;
    target.tv3 = null;
    target.tv_location = null;
    target.ll_search = null;
  }
}
