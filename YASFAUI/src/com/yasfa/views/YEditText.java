/*
    Copyright (C) {2015}  (Edward Ian Hickman}

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
