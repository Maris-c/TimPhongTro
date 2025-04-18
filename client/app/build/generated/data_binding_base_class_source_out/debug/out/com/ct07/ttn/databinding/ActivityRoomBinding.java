// Generated by view binder compiler. Do not edit!
package com.ct07.ttn.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.ct07.ttn.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRoomBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final BottomNavigationView bottomNavigationView;

  @NonNull
  public final FrameLayout flFragment;

  @NonNull
  public final FragmentContainerView roomNavHostFragment;

  private ActivityRoomBinding(@NonNull ConstraintLayout rootView,
      @NonNull BottomNavigationView bottomNavigationView, @NonNull FrameLayout flFragment,
      @NonNull FragmentContainerView roomNavHostFragment) {
    this.rootView = rootView;
    this.bottomNavigationView = bottomNavigationView;
    this.flFragment = flFragment;
    this.roomNavHostFragment = roomNavHostFragment;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRoomBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRoomBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_room, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRoomBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bottomNavigationView;
      BottomNavigationView bottomNavigationView = ViewBindings.findChildViewById(rootView, id);
      if (bottomNavigationView == null) {
        break missingId;
      }

      id = R.id.flFragment;
      FrameLayout flFragment = ViewBindings.findChildViewById(rootView, id);
      if (flFragment == null) {
        break missingId;
      }

      id = R.id.roomNavHostFragment;
      FragmentContainerView roomNavHostFragment = ViewBindings.findChildViewById(rootView, id);
      if (roomNavHostFragment == null) {
        break missingId;
      }

      return new ActivityRoomBinding((ConstraintLayout) rootView, bottomNavigationView, flFragment,
          roomNavHostFragment);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
