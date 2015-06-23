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
