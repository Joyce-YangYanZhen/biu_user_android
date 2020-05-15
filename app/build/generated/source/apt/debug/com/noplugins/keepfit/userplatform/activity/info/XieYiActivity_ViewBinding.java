// Generated code from Butter Knife. Do not modify!
package com.noplugins.keepfit.userplatform.activity.info;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.noplugins.keepfit.userplatform.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XieYiActivity_ViewBinding implements Unbinder {
  private XieYiActivity target;

  @UiThread
  public XieYiActivity_ViewBinding(XieYiActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XieYiActivity_ViewBinding(XieYiActivity target, View source) {
    this.target = target;

    target.back_btn = Utils.findRequiredViewAsType(source, R.id.back_btn, "field 'back_btn'", ImageView.class);
    target.webView = Utils.findRequiredViewAsType(source, R.id.webView, "field 'webView'", WebView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    XieYiActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back_btn = null;
    target.webView = null;
  }
}
