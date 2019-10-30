// Generated code from Butter Knife. Do not modify!
package com.project.jewelmart.swarnsarita;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.jaredrummler.materialspinner.MaterialSpinner;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProductDetailActivity_ViewBinding implements Unbinder {
  private ProductDetailActivity target;

  @UiThread
  public ProductDetailActivity_ViewBinding(ProductDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ProductDetailActivity_ViewBinding(ProductDetailActivity target, View source) {
    this.target = target;

    target.et_review = Utils.findRequiredViewAsType(source, R.id.review, "field 'et_review'", EditText.class);
    target.melting_spinner = Utils.findRequiredViewAsType(source, R.id.melting_spinner, "field 'melting_spinner'", MaterialSpinner.class);
    target.top_leyer = Utils.findRequiredViewAsType(source, R.id.top_leyer, "field 'top_leyer'", LinearLayout.class);
    target.labour_layout = Utils.findRequiredViewAsType(source, R.id.labour_layout, "field 'labour_layout'", LinearLayout.class);
    target.charges_layout = Utils.findRequiredViewAsType(source, R.id.charges_layout, "field 'charges_layout'", LinearLayout.class);
    target.color_layout = Utils.findRequiredViewAsType(source, R.id.color_layout, "field 'color_layout'", LinearLayout.class);
    target.polish_layout = Utils.findRequiredViewAsType(source, R.id.polish_layout, "field 'polish_layout'", LinearLayout.class);
    target.tone_layout1 = Utils.findRequiredViewAsType(source, R.id.tone_layout, "field 'tone_layout1'", LinearLayout.class);
    target.tone_spinner = Utils.findRequiredViewAsType(source, R.id.tone_spinner, "field 'tone_spinner'", MaterialSpinner.class);
    target.color_spinner = Utils.findRequiredViewAsType(source, R.id.color_spinner, "field 'color_spinner'", MaterialSpinner.class);
    target.polish_spinner = Utils.findRequiredViewAsType(source, R.id.polish_spinner, "field 'polish_spinner'", MaterialSpinner.class);
    target.card_wishlist = Utils.findRequiredViewAsType(source, R.id.card_wishlist, "field 'card_wishlist'", CardView.class);
    target.card_cart = Utils.findRequiredViewAsType(source, R.id.card_cart, "field 'card_cart'", CardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.et_review = null;
    target.melting_spinner = null;
    target.top_leyer = null;
    target.labour_layout = null;
    target.charges_layout = null;
    target.color_layout = null;
    target.polish_layout = null;
    target.tone_layout1 = null;
    target.tone_spinner = null;
    target.color_spinner = null;
    target.polish_spinner = null;
    target.card_wishlist = null;
    target.card_cart = null;
  }
}
