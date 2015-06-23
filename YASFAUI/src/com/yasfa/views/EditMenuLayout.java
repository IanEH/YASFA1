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
