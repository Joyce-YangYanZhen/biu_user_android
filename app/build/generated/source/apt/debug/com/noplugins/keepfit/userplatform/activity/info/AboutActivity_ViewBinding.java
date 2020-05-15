// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.activity.info;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AboutActivity_ViewBinding implements Unbinder {
  private AboutActivity target;

  @UiThread
  public AboutActivity_ViewBinding(AboutActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AboutActivity_ViewBinding(AboutActivity target, View source) {
    this.target = target;

    target.back_btn = Utils.findRequiredViewAsType(source, R.id.back_btn, "field 'back_btn'", ImageView.class);
    target.tv_version = Utils.findRequiredViewAsType(source, R.id.tv_version, "field 'tv_version'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AboutActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back_btn = null;
    target.tv_version = null;
  }
}
