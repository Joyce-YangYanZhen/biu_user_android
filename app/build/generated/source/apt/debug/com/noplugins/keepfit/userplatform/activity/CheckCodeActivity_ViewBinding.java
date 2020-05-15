// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.util.ui.StepView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CheckCodeActivity_ViewBinding implements Unbinder {
  private CheckCodeActivity target;

  @UiThread
  public CheckCodeActivity_ViewBinding(CheckCodeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CheckCodeActivity_ViewBinding(CheckCodeActivity target, View source) {
    this.target = target;

    target.stepView = Utils.findRequiredViewAsType(source, R.id.sv, "field 'stepView'", StepView.class);
    target.back_btn = Utils.findRequiredViewAsType(source, R.id.back_btn, "field 'back_btn'", ImageView.class);
    target.erweima_code = Utils.findRequiredViewAsType(source, R.id.erweima_code, "field 'erweima_code'", ImageView.class);
    target.order_number = Utils.findRequiredViewAsType(source, R.id.order_number, "field 'order_number'", TextView.class);
    target.xiangmu_name = Utils.findRequiredViewAsType(source, R.id.xiangmu_name, "field 'xiangmu_name'", TextView.class);
    target.teacher_name = Utils.findRequiredViewAsType(source, R.id.teacher_name, "field 'teacher_name'", TextView.class);
    target.time_tv = Utils.findRequiredViewAsType(source, R.id.time_tv, "field 'time_tv'", TextView.class);
    target.address_tv = Utils.findRequiredViewAsType(source, R.id.address_tv, "field 'address_tv'", TextView.class);
    target.room_tv = Utils.findRequiredViewAsType(source, R.id.room_tv, "field 'room_tv'", TextView.class);
    target.lin1 = Utils.findRequiredViewAsType(source, R.id.lin1, "field 'lin1'", RelativeLayout.class);
    target.lin2 = Utils.findRequiredViewAsType(source, R.id.lin2, "field 'lin2'", RelativeLayout.class);
    target.rl_room = Utils.findRequiredViewAsType(source, R.id.rl_room, "field 'rl_room'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CheckCodeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.stepView = null;
    target.back_btn = null;
    target.erweima_code = null;
    target.order_number = null;
    target.xiangmu_name = null;
    target.teacher_name = null;
    target.time_tv = null;
    target.address_tv = null;
    target.room_tv = null;
    target.lin1 = null;
    target.lin2 = null;
    target.rl_room = null;
  }
}
