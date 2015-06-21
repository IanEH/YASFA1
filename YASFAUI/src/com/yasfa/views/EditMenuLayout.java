/*
 * TouchImageView.java
 * By: Edward Ian Hickman
 * Copyright (C) 2015 Edward Ian Hickman
 */


package com.yasfa.views;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class EditMenuLayout extends LinearLayout {
    public void EditFocus(boolean Edit) {
        View button=((LinearLayout) this).getChildAt(1);
        View label=((LinearLayout) this).getChildAt(0);
        View spinner=((LinearLayout) this).getChildAt(2);
        View action=((LinearLayout) this).getChildAt(3);
        if (Edit) {
            spinner.setVisibility(View.VISIBLE);
            action.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        } else {
            spinner.setVisibility(View.GONE);
            action.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }
    }
	public EditMenuLayout(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
	}
}
