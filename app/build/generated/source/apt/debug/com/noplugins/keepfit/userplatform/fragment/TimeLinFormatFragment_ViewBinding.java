// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import com.othershe.calendarview.weiget.CalendarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import java.lang.IllegalStateException;
import java.lang.Override;
import www.linwg.org.lib.LCardView;

public class TimeLinFormatFragment_ViewBinding implements Unbinder {
  private TimeLinFormatFragment target;

  @UiThread
  public TimeLinFormatFragment_ViewBinding(TimeLinFormatFragment target, View source) {
    this.target = target;

    target.calendar = Utils.findRequiredViewAsType(source, R.id.calendar, "field 'calendar'", CalendarView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.tv_now_select = Utils.findRequiredViewAsType(source, R.id.tv_now_select, "field 'tv_now_select'", TextView.class);
    target.tv_now_week = Utils.findRequiredViewAsType(source, R.id.tv_now_week, "field 'tv_now_week'", TextView.class);
    target.last_btn = Utils.findRequiredViewAsType(source, R.id.last_btn, "field 'last_btn'", LinearLayout.class);
    target.next_btn = Utils.findRequiredViewAsType(source, R.id.next_btn, "field 'next_btn'", LinearLayout.class);
    target.recycler_view = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recycler_view'", RecyclerView.class);
    target.share_btn = Utils.findRequiredViewAsType(source, R.id.share_btn, "field 'share_btn'", ImageView.class);
    target.clander_layout = Utils.findRequiredViewAsType(source, R.id.clander_layout, "field 'clander_layout'", LCardView.class);
    target.title_layout = Utils.findRequiredViewAsType(source, R.id.title_layout, "field 'title_layout'", LinearLayout.class);
    target.left_img = Utils.findRequiredViewAsType(source, R.id.left_img, "field 'left_img'", ImageView.class);
    target.right_img = Utils.findRequiredViewAsType(source, R.id.right_img, "field 'right_img'", ImageView.class);
    target.xiala_img = Utils.findRequiredViewAsType(source, R.id.xiala_img, "field 'xiala_img'", ImageView.class);
    target.refreshLayout = Utils.findRequiredViewAsType(source, R.id.refreshLayout, "field 'refreshLayout'", SmartRefreshLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TimeLinFormatFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.calendar = null;
    target.title = null;
    target.tv_now_select = null;
    target.tv_now_week = null;
    target.last_btn = null;
    target.next_btn = null;
    target.recycler_view = null;
    target.share_btn = null;
    target.clander_layout = null;
    target.title_layout = null;
    target.left_img = null;
    target.right_img = null;
    target.xiala_img = null;
    target.refreshLayout = null;
  }
}
