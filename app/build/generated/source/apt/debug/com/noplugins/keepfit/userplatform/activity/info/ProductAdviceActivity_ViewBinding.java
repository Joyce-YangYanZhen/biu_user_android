// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.activity.info;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProductAdviceActivity_ViewBinding implements Unbinder {
  private ProductAdviceActivity target;

  @UiThread
  public ProductAdviceActivity_ViewBinding(ProductAdviceActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ProductAdviceActivity_ViewBinding(ProductAdviceActivity target, View source) {
    this.target = target;

    target.back_btn = Utils.findRequiredViewAsType(source, R.id.back_btn, "field 'back_btn'", ImageView.class);
    target.cb_product_suggest = Utils.findRequiredViewAsType(source, R.id.cb_product_suggest, "field 'cb_product_suggest'", CheckBox.class);
    target.cb_fault_feedback = Utils.findRequiredViewAsType(source, R.id.cb_fault_feedback, "field 'cb_fault_feedback'", CheckBox.class);
    target.cb_other = Utils.findRequiredViewAsType(source, R.id.cb_other, "field 'cb_other'", CheckBox.class);
    target.edit_content = Utils.findRequiredViewAsType(source, R.id.edit_content, "field 'edit_content'", EditText.class);
    target.add_class_teacher_btn = Utils.findRequiredViewAsType(source, R.id.add_class_teacher_btn, "field 'add_class_teacher_btn'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductAdviceActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back_btn = null;
    target.cb_product_suggest = null;
    target.cb_fault_feedback = null;
    target.cb_other = null;
    target.edit_content = null;
    target.add_class_teacher_btn = null;
  }
}
