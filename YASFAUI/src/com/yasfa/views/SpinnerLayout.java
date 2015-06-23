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

import java.util.UUID;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import android.widget.Spinner;
import android.widget.Button;

public class SpinnerLayout extends YASFAControl {
	public String Name;


    public void EditFocus(boolean Edit) {
        LinearLayout edit = (LinearLayout)((LinearLayout) this).getChildAt(1);
        if (!Edit) {
            edit.setVisibility(View.GONE);
        } else {
            edit.setVisibility(View.VISIBLE);
            for(int j=0;j<((LinearLayout) edit).getChildCount();j++) {
                View control = (View) ((LinearLayout) edit).getChildAt(j);
                control.setClickable(true);
                control.setEnabled(true);
                if (control instanceof Button) {
                    control.setFocusable(false);
                    control.setFocusableInTouchMode(false);
                } else {
                    control.setFocusable(true);
                    control.setFocusableInTouchMode(true);
                }
            }
     }
    }

	public SpinnerLayout(InflateView context) {
		super(context);

	}
	
	public void NewName() {
		Name="S"+UUID.randomUUID().toString().replace("-","");
	}

	public String GetValue() {
		String retval="";
        try {
            LinearLayout test = (LinearLayout) this.getChildAt(0);
            View test1 = test.getChildAt(1);
            if (test1 instanceof Spinner) {
                retval = ((Spinner) test1).getSelectedItem().toString();
            }
        } catch (Exception ex) {}
		return retval;
	}

    public void SetValue(String value) {
        LinearLayout test=(LinearLayout)this.getChildAt(0);
        View test1=test.getChildAt(1);
        if (test1 instanceof Spinner) {
            ArrayAdapter<String> adapter=(ArrayAdapter<String>) ((Spinner)test1).getAdapter();
            int spinnerPostion = adapter.getPosition(value);
            ((Spinner)test1).setSelection(spinnerPostion);
        }
    }

	public void DefaultValue() {
	}

    public void SetSize(int width, int height) {
        try {
            if (height < 20) height = 20;
            if (width < 20) width = 20;

            View child = this.getChildAt(0);

            child = ((LinearLayout) child).getChildAt(1);
            ViewGroup.LayoutParams params = child.getLayoutParams();
            params.height = height;
            params.width = width;

        } catch (Exception ex) {
            String sex=ex.getMessage();
        }
    }

}
