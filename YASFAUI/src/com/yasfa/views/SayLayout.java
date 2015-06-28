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

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import java.util.UUID;

public class SayLayout extends YASFAControl {
	public String Name;
    public EditText label;
    public InflateView MainLayout;

	public SayLayout(InflateView context) {
		super(context);

        MainLayout = context;
        label = new YEditText(context,true);
        //label.setTextColor(Color.BLUE);
        this.addView(label);
    }

	public void NewName() {
		Name="";
	}

    public void SetSize(int width, int height) {
        try {
            if (height < 20) height = 20;
            if (width < 20) width = 20;

            ViewGroup.LayoutParams params = label.getLayoutParams();
            Rect bounds = new Rect();

            if (height>20) {
                int size = height - ((height+20)/6);
                if (size<14) size=14;
                label.setTextSize(size);
            } else {
                label.setTextSize(14);
            }
            label.setPadding(5,-(int)(height/2.5),5,-(height/5));

            params.height = height;
            params.width = -2;//bounds.width()+10;

        } catch (Exception ex1) {
        }
    }



    public String GetValue() {
      	return "";
	}
	public void SetValue(String value) {
    }

    public void DefaultValue() {
    }

    public String SayText() {
        try {
            return label.getText().toString();
        } catch (Exception ex) {}
        return "";
    }

    public void EditFocus(boolean edit) {
        if(label==null)
        { // Brok so fix it!
            label = new EditText(mcontext);
            addView(label);
        }

        if (edit) {
            label.setClickable(true);
            label.setEnabled(true);
            label.setFocusable(true);
            label.setFocusableInTouchMode(true);
        }
        else
        {
            label.setClickable(false);
            label.setEnabled(false);
            label.setFocusable(false);
            label.setFocusableInTouchMode(false);
        }
    }

    @SuppressLint("NewApi")
    public void Edit (boolean edit) {
         if (label==null) { // Brock so fix it!
            label=new EditText(mcontext);
            addView(label);
        }
        if (edit) {
            //	label.setBackground((new EditText(mcontext)).getBackground());
            label.setBackgroundColor(Color.TRANSPARENT);
            label.setTextColor(Color.WHITE);
            label.setClickable(true);
            label.setEnabled(true);
            label.setFocusable(true);
            label.setFocusableInTouchMode(true);
        } else {
            //if (label.getText().equals("")) {
            //    label.setVisibility(View.VISIBLE);
            //}
            int i = label.getText().length();
            int c = Color.argb(40, 255, 0, 0);
            if (i>10)
                c = Color.argb(40, 255, 255, 0);
            else if (i>8)
                c = Color.argb(40, 255, 100, 255);
            else if (i>7)
                c = Color.argb(40, 255, 100, 100);
            else if (i>6)
                c = Color.argb(40, 255, 255, 255);
            else if (i>5)
                c = Color.argb(40, 0, 255, 0);
            else if (i>4)
                c = Color.argb(40, 0, 0, 255);
            else if (i>3)
                c = Color.argb(40, 0, 255, 255);
            else if (i>2)
                c = Color.argb(40, 100, 100, 255);

            this.setBackgroundColor(Color.TRANSPARENT);
            label.setBackgroundColor(c);
            label.setTextColor(Color.WHITE);
            label.setClickable(false);
            label.setEnabled(false);
            label.setFocusable(false);
            label.setFocusableInTouchMode(false);
        }
    }
}
