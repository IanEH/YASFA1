/*
 * TouchImageView.java
 * By: Edward Ian Hickman
 * Copyright (C) 2015 Edward Ian Hickman
 */

package com.yasfa.views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Ian on 05/04/15.
 */
public class FButton extends Button {
    String mtoast;
    Context mcontext;
    public FButton(Context context,String toast) {
        super(context);mtoast=toast;mcontext=context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!mtoast.equals("")) {
                Toast.makeText(mcontext, mtoast,Toast.LENGTH_SHORT).show();
            }
            setAlpha(0.4f);
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            setAlpha(1);
        }
        return super.onTouchEvent(event);
    }

}
