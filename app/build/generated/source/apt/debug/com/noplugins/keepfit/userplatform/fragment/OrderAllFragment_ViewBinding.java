// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.fragment;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrderAllFragment_ViewBinding implements Unbinder {
  private OrderAllFragment target;

  @UiThread
  public OrderAllFragment_ViewBinding(OrderAllFragment target, View source) {
    this.target = target;

    target.xrefreshview = Utils.findRequiredViewAsType(source, R.id.xrefreshview, "field 'xrefreshview'", SmartRefreshLayout.class);
    target.recycler_view = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recycler_view'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    OrderAllFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.xrefreshview = null;
    target.recycler_view = null;
  }
}
