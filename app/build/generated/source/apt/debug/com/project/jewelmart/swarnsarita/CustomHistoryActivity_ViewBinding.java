// Generated code from Butter Knife. Do not modify!
package com.project.jewelmart.swarnsarita;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CustomHistoryActivity_ViewBinding implements Unbinder {
  private CustomHistoryActivity target;

  @UiThread
  public CustomHistoryActivity_ViewBinding(CustomHistoryActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CustomHistoryActivity_ViewBinding(CustomHistoryActivity target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CustomHistoryActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
  }
}
