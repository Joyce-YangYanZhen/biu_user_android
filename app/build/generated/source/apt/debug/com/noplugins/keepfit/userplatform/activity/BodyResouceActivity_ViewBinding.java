// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.util.ui.LineView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BodyResouceActivity_ViewBinding implements Unbinder {
  private BodyResouceActivity target;

  @UiThread
  public BodyResouceActivity_ViewBinding(BodyResouceActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public BodyResouceActivity_ViewBinding(BodyResouceActivity target, View source) {
    this.target = target;

    target.base_information_edit = Utils.findRequiredViewAsType(source, R.id.base_information_edit, "field 'base_information_edit'", ImageView.class);
    target.shengao_edit = Utils.findRequiredViewAsType(source, R.id.shengao_edit, "field 'shengao_edit'", EditText.class);
    target.tizhong_edit = Utils.findRequiredViewAsType(source, R.id.tizhong_edit, "field 'tizhong_edit'", EditText.class);
    target.update_jinjie_btn = Utils.findRequiredViewAsType(source, R.id.update_jinjie_btn, "field 'update_jinjie_btn'", ImageView.class);
    target.tizhilv_edit = Utils.findRequiredViewAsType(source, R.id.tizhilv_edit, "field 'tizhilv_edit'", EditText.class);
    target.yaotunbi_edit = Utils.findRequiredViewAsType(source, R.id.yaotunbi_edit, "field 'yaotunbi_edit'", EditText.class);
    target.jichudaxie_edit = Utils.findRequiredViewAsType(source, R.id.jichudaxie_edit, "field 'jichudaxie_edit'", EditText.class);
    target.bmi_edit = Utils.findRequiredViewAsType(source, R.id.bmi_edit, "field 'bmi_edit'", EditText.class);
    target.save_btn = Utils.findRequiredViewAsType(source, R.id.save_btn, "field 'save_btn'", TextView.class);
    target.save_jinjie_btn = Utils.findRequiredViewAsType(source, R.id.save_jinjie_btn, "field 'save_jinjie_btn'", TextView.class);
    target.tizhong_chart = Utils.findRequiredViewAsType(source, R.id.tizhong_chart, "field 'tizhong_chart'", LineView.class);
    target.back_btn = Utils.findRequiredViewAsType(source, R.id.back_btn, "field 'back_btn'", ImageView.class);
    target.tizhilv_chart = Utils.findRequiredViewAsType(source, R.id.tizhilv_chart, "field 'tizhilv_chart'", LineView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BodyResouceActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.base_information_edit = null;
    target.shengao_edit = null;
    target.tizhong_edit = null;
    target.update_jinjie_btn = null;
    target.tizhilv_edit = null;
    target.yaotunbi_edit = null;
    target.jichudaxie_edit = null;
    target.bmi_edit = null;
    target.save_btn = null;
    target.save_jinjie_btn = null;
    target.tizhong_chart = null;
    target.back_btn = null;
    target.tizhilv_chart = null;
  }
}
