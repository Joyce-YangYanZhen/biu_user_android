// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TravelActivity_ViewBinding implements Unbinder {
  private TravelActivity target;

  @UiThread
  public TravelActivity_ViewBinding(TravelActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TravelActivity_ViewBinding(TravelActivity target, View source) {
    this.target = target;

    target.spinner_year = Utils.findRequiredViewAsType(source, R.id.spinner_year, "field 'spinner_year'", TextView.class);
    target.spinner_month = Utils.findRequiredViewAsType(source, R.id.spinner_month, "field 'spinner_month'", TextView.class);
    target.spinner_type = Utils.findRequiredViewAsType(source, R.id.spinner_type, "field 'spinner_type'", TextView.class);
    target.xrefreshview = Utils.findRequiredViewAsType(source, R.id.xrefreshview, "field 'xrefreshview'", SmartRefreshLayout.class);
    target.recycler_view = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recycler_view'", RecyclerView.class);
    target.back_btn = Utils.findRequiredViewAsType(source, R.id.back_btn, "field 'back_btn'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TravelActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.spinner_year = null;
    target.spinner_month = null;
    target.spinner_type = null;
    target.xrefreshview = null;
    target.recycler_view = null;
    target.back_btn = null;
  }
}
