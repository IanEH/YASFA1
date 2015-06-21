/*
 * TouchImageView.java
 * By: Edward Ian Hickman
 * Copyright (C) 2015 Edward Ian Hickman
 */

package com.yasfa.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Ian on 22/03/15.
 * Used for labels where editing text is in Edit Mode!
 */

public class YEditText extends EditText {
    InflateView mMainView;
    public YEditText(InflateView context, boolean edit) {
        super(context);
        mMainView = context;
        setPadding(2, 2, 2, 2);
        setShadowLayer(1.5f, -1, 1, Color.GRAY);
        setTextSize(14);
        Edit(edit);
    }

    public void Edit(Boolean edit) {
            if (edit) {
                setBackgroundColor(Color.TRANSPARENT);
                setTextColor(Color.WHITE);
                setClickable(true);
                setEnabled(true);
                setFocusable(true);
                setFocusableInTouchMode(true);
            } else {
                setBackgroundColor(Color.TRANSPARENT);
                setTextColor(Color.WHITE);
                setClickable(false);
                setEnabled(false);
                setFocusable(false);
                setFocusableInTouchMode(false);
            }
    }

    public YEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        if (mMainView == null) return true;
        if (mMainView.editMoveOnly){
            return false;
        } else {
            return true;
        }

    }
}
