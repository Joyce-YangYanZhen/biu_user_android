// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.activity;

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

public class MessageDetailActivity_ViewBinding implements Unbinder {
  private MessageDetailActivity target;

  @UiThread
  public MessageDetailActivity_ViewBinding(MessageDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MessageDetailActivity_ViewBinding(MessageDetailActivity target, View source) {
    this.target = target;

    target.icon = Utils.findRequiredViewAsType(source, R.id.icon, "field 'icon'", ImageView.class);
    target.message_title = Utils.findRequiredViewAsType(source, R.id.message_title, "field 'message_title'", TextView.class);
    target.content_tv = Utils.findRequiredViewAsType(source, R.id.content_tv, "field 'content_tv'", TextView.class);
    target.time_tv = Utils.findRequiredViewAsType(source, R.id.time_tv, "field 'time_tv'", TextView.class);
    target.back_btn = Utils.findRequiredViewAsType(source, R.id.back_btn, "field 'back_btn'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MessageDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.icon = null;
    target.message_title = null;
    target.content_tv = null;
    target.time_tv = null;
    target.back_btn = null;
  }
}
