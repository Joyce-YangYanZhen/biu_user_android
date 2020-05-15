// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DailyTravelDetailActivity_ViewBinding implements Unbinder {
  private DailyTravelDetailActivity target;

  @UiThread
  public DailyTravelDetailActivity_ViewBinding(DailyTravelDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DailyTravelDetailActivity_ViewBinding(DailyTravelDetailActivity target, View source) {
    this.target = target;

    target.back_btn = Utils.findRequiredViewAsType(source, R.id.back_btn, "field 'back_btn'", ImageView.class);
    target.update_xinqing_btn = Utils.findRequiredViewAsType(source, R.id.update_xinqing_btn, "field 'update_xinqing_btn'", ImageView.class);
    target.yundonghou_img = Utils.findRequiredViewAsType(source, R.id.yundonghou_img, "field 'yundonghou_img'", ImageView.class);
    target.yundonghou_layout = Utils.findRequiredViewAsType(source, R.id.yundonghou_layout, "field 'yundonghou_layout'", RelativeLayout.class);
    target.update_tice_btn = Utils.findRequiredViewAsType(source, R.id.update_tice_btn, "field 'update_tice_btn'", ImageView.class);
    target.shengao_edit = Utils.findRequiredViewAsType(source, R.id.shengao_edit, "field 'shengao_edit'", EditText.class);
    target.tizhong_edit = Utils.findRequiredViewAsType(source, R.id.tizhong_edit, "field 'tizhong_edit'", EditText.class);
    target.tizhi_edit = Utils.findRequiredViewAsType(source, R.id.tizhi_edit, "field 'tizhi_edit'", EditText.class);
    target.bmi_edit = Utils.findRequiredViewAsType(source, R.id.bmi_edit, "field 'bmi_edit'", EditText.class);
    target.yaotunbi_edit = Utils.findRequiredViewAsType(source, R.id.yaotunbi_edit, "field 'yaotunbi_edit'", EditText.class);
    target.daixie_edit = Utils.findRequiredViewAsType(source, R.id.daixie_edit, "field 'daixie_edit'", EditText.class);
    target.grid_view = Utils.findRequiredViewAsType(source, R.id.grid_view, "field 'grid_view'", GridView.class);
    target.shanchu_btn = Utils.findRequiredViewAsType(source, R.id.shanchu_btn, "field 'shanchu_btn'", ImageView.class);
    target.save_btn = Utils.findRequiredViewAsType(source, R.id.save_btn, "field 'save_btn'", TextView.class);
    target.class_name = Utils.findRequiredViewAsType(source, R.id.class_name, "field 'class_name'", TextView.class);
    target.class_time = Utils.findRequiredViewAsType(source, R.id.class_time, "field 'class_time'", TextView.class);
    target.address_tv = Utils.findRequiredViewAsType(source, R.id.address_tv, "field 'address_tv'", TextView.class);
    target.ruchang_time = Utils.findRequiredViewAsType(source, R.id.ruchang_time, "field 'ruchang_time'", TextView.class);
    target.lichang_time = Utils.findRequiredViewAsType(source, R.id.lichang_time, "field 'lichang_time'", TextView.class);
    target.ruhang_biaoqing = Utils.findRequiredViewAsType(source, R.id.ruhang_biaoqing, "field 'ruhang_biaoqing'", ImageView.class);
    target.yundong_before_layout = Utils.findRequiredViewAsType(source, R.id.yundong_before_layout, "field 'yundong_before_layout'", RelativeLayout.class);
    target.class_content = Utils.findRequiredViewAsType(source, R.id.class_content, "field 'class_content'", TextView.class);
    target.yundongqian_tv = Utils.findRequiredViewAsType(source, R.id.yundongqian_tv, "field 'yundongqian_tv'", TextView.class);
    target.yundonghou_tv = Utils.findRequiredViewAsType(source, R.id.yundonghou_tv, "field 'yundonghou_tv'", TextView.class);
    target.lin4 = Utils.findRequiredViewAsType(source, R.id.lin4, "field 'lin4'", LinearLayout.class);
    target.lin5 = Utils.findRequiredViewAsType(source, R.id.lin5, "field 'lin5'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DailyTravelDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back_btn = null;
    target.update_xinqing_btn = null;
    target.yundonghou_img = null;
    target.yundonghou_layout = null;
    target.update_tice_btn = null;
    target.shengao_edit = null;
    target.tizhong_edit = null;
    target.tizhi_edit = null;
    target.bmi_edit = null;
    target.yaotunbi_edit = null;
    target.daixie_edit = null;
    target.grid_view = null;
    target.shanchu_btn = null;
    target.save_btn = null;
    target.class_name = null;
    target.class_time = null;
    target.address_tv = null;
    target.ruchang_time = null;
    target.lichang_time = null;
    target.ruhang_biaoqing = null;
    target.yundong_before_layout = null;
    target.class_content = null;
    target.yundongqian_tv = null;
    target.yundonghou_tv = null;
    target.lin4 = null;
    target.lin5 = null;
  }
}
