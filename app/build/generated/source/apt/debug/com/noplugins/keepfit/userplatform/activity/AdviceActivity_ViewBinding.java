// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.activity;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AdviceActivity_ViewBinding implements Unbinder {
  private AdviceActivity target;

  @UiThread
  public AdviceActivity_ViewBinding(AdviceActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AdviceActivity_ViewBinding(AdviceActivity target, View source) {
    this.target = target;

    target.back_btn = Utils.findRequiredViewAsType(source, R.id.back_btn, "field 'back_btn'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AdviceActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back_btn = null;
  }
}
