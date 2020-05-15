// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.util;

import android.view.View;
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

public class ToolbarControl_ViewBinding implements Unbinder {
  private ToolbarControl target;

  @UiThread
  public ToolbarControl_ViewBinding(ToolbarControl target) {
    this(target, target);
  }

  @UiThread
  public ToolbarControl_ViewBinding(ToolbarControl target, View source) {
    this.target = target;

    target.leftButton = Utils.findRequiredViewAsType(source, R.id.toolbar_left_button1, "field 'leftButton'", ImageView.class);
    target.titleTextView = Utils.findRequiredViewAsType(source, R.id.toolbar_title1, "field 'titleTextView'", TextView.class);
    target.linear_left = Utils.findRequiredViewAsType(source, R.id.linear_left, "field 'linear_left'", LinearLayout.class);
    target.toolbar_right_button_Tex = Utils.findRequiredViewAsType(source, R.id.toolbar_right_button_Tex, "field 'toolbar_right_button_Tex'", TextView.class);
    target.rel_quanju = Utils.findRequiredViewAsType(source, R.id.rel_quanju, "field 'rel_quanju'", RelativeLayout.class);
    target.toolbar_right_button_Image = Utils.findRequiredViewAsType(source, R.id.toolbar_right_button_Image, "field 'toolbar_right_button_Image'", ImageView.class);
    target.linear_right = Utils.findRequiredViewAsType(source, R.id.linear_right, "field 'linear_right'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ToolbarControl target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.leftButton = null;
    target.titleTextView = null;
    target.linear_left = null;
    target.toolbar_right_button_Tex = null;
    target.rel_quanju = null;
    target.toolbar_right_button_Image = null;
    target.linear_right = null;
  }
}
