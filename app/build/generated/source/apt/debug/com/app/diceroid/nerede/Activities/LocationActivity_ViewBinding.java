// Generated code from Butter Knife. Do not modify!
package com.app.diceroid.nerede.Activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.app.diceroid.nerede.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LocationActivity_ViewBinding implements Unbinder {
  private LocationActivity target;

  @UiThread
  public LocationActivity_ViewBinding(LocationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LocationActivity_ViewBinding(LocationActivity target, View source) {
    this.target = target;

    target.findRoute = Utils.findRequiredViewAsType(source, R.id.buttonFindRoute, "field 'findRoute'", Button.class);
    target.location_info_text = Utils.findRequiredViewAsType(source, R.id.location_info_text, "field 'location_info_text'", TextView.class);
    target.destination_place_text = Utils.findRequiredViewAsType(source, R.id.destination_place_text, "field 'destination_place_text'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LocationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.findRoute = null;
    target.location_info_text = null;
    target.destination_place_text = null;
  }
}
