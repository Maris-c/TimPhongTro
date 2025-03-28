// Generated by view binder compiler. Do not edit!
package com.ct07.ttn.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.ct07.ttn.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentHeadlinesBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText btnSearch;

  @NonNull
  public final ItemErrorBinding itemHeadlinesError;

  @NonNull
  public final ProgressBar paginationProgressBar;

  @NonNull
  public final RecyclerView recyclerHeadlines;

  private FragmentHeadlinesBinding(@NonNull LinearLayout rootView, @NonNull EditText btnSearch,
      @NonNull ItemErrorBinding itemHeadlinesError, @NonNull ProgressBar paginationProgressBar,
      @NonNull RecyclerView recyclerHeadlines) {
    this.rootView = rootView;
    this.btnSearch = btnSearch;
    this.itemHeadlinesError = itemHeadlinesError;
    this.paginationProgressBar = paginationProgressBar;
    this.recyclerHeadlines = recyclerHeadlines;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentHeadlinesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentHeadlinesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_headlines, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentHeadlinesBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnSearch;
      EditText btnSearch = ViewBindings.findChildViewById(rootView, id);
      if (btnSearch == null) {
        break missingId;
      }

      id = R.id.itemHeadlinesError;
      View itemHeadlinesError = ViewBindings.findChildViewById(rootView, id);
      if (itemHeadlinesError == null) {
        break missingId;
      }
      ItemErrorBinding binding_itemHeadlinesError = ItemErrorBinding.bind(itemHeadlinesError);

      id = R.id.paginationProgressBar;
      ProgressBar paginationProgressBar = ViewBindings.findChildViewById(rootView, id);
      if (paginationProgressBar == null) {
        break missingId;
      }

      id = R.id.recyclerHeadlines;
      RecyclerView recyclerHeadlines = ViewBindings.findChildViewById(rootView, id);
      if (recyclerHeadlines == null) {
        break missingId;
      }

      return new FragmentHeadlinesBinding((LinearLayout) rootView, btnSearch,
          binding_itemHeadlinesError, paginationProgressBar, recyclerHeadlines);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
