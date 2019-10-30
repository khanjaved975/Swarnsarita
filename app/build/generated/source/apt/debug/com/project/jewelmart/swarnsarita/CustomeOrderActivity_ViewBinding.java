// Generated code from Butter Knife. Do not modify!
package com.project.jewelmart.swarnsarita;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CustomeOrderActivity_ViewBinding implements Unbinder {
  private CustomeOrderActivity target;

  @UiThread
  public CustomeOrderActivity_ViewBinding(CustomeOrderActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CustomeOrderActivity_ViewBinding(CustomeOrderActivity target, View source) {
    this.target = target;

    target.fab = Utils.findRequiredViewAsType(source, R.id.fab_image, "field 'fab'", FloatingActionButton.class);
    target.btn_addorder = Utils.findRequiredViewAsType(source, R.id.btn_order, "field 'btn_addorder'", Button.class);
    target.et_grosswt = Utils.findRequiredViewAsType(source, R.id.et_grosswt, "field 'et_grosswt'", MaterialEditText.class);
    target.image = Utils.findRequiredViewAsType(source, R.id.image, "field 'image'", ImageView.class);
    target.et_netwt = Utils.findRequiredViewAsType(source, R.id.et_netwt, "field 'et_netwt'", MaterialEditText.class);
    target.et_size = Utils.findRequiredViewAsType(source, R.id.et_size, "field 'et_size'", MaterialEditText.class);
    target.et_length = Utils.findRequiredViewAsType(source, R.id.et_length, "field 'et_length'", MaterialEditText.class);
    target.et_remarks = Utils.findRequiredViewAsType(source, R.id.et_remarks, "field 'et_remarks'", MaterialEditText.class);
    target.et_color = Utils.findRequiredViewAsType(source, R.id.et_color, "field 'et_color'", MaterialEditText.class);
    target.et_date = Utils.findRequiredViewAsType(source, R.id.et_date, "field 'et_date'", MaterialEditText.class);
    target.melting_spinner = Utils.findRequiredViewAsType(source, R.id.melting_spinner, "field 'melting_spinner'", Spinner.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CustomeOrderActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.fab = null;
    target.btn_addorder = null;
    target.et_grosswt = null;
    target.image = null;
    target.et_netwt = null;
    target.et_size = null;
    target.et_length = null;
    target.et_remarks = null;
    target.et_color = null;
    target.et_date = null;
    target.melting_spinner = null;
  }
}
