// Generated by view binder compiler. Do not edit!
package com.example.arielscupid.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager.widget.ViewPager;
import com.example.arielscupid.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityChatBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TabItem chat;

  @NonNull
  public final ViewPager fragmentcontainer;

  @NonNull
  public final TabLayout include;

  @NonNull
  public final TextView myapptext;

  @NonNull
  public final Toolbar toolbar;

  private ActivityChatBinding(@NonNull RelativeLayout rootView, @NonNull TabItem chat,
      @NonNull ViewPager fragmentcontainer, @NonNull TabLayout include, @NonNull TextView myapptext,
      @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.chat = chat;
    this.fragmentcontainer = fragmentcontainer;
    this.include = include;
    this.myapptext = myapptext;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityChatBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityChatBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_chat, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityChatBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.chat;
      TabItem chat = ViewBindings.findChildViewById(rootView, id);
      if (chat == null) {
        break missingId;
      }

      id = R.id.fragmentcontainer;
      ViewPager fragmentcontainer = ViewBindings.findChildViewById(rootView, id);
      if (fragmentcontainer == null) {
        break missingId;
      }

      id = R.id.include;
      TabLayout include = ViewBindings.findChildViewById(rootView, id);
      if (include == null) {
        break missingId;
      }

      id = R.id.myapptext;
      TextView myapptext = ViewBindings.findChildViewById(rootView, id);
      if (myapptext == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new ActivityChatBinding((RelativeLayout) rootView, chat, fragmentcontainer, include,
          myapptext, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
