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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.widget.CheckBox;

public class ControlLayout extends YASFAControl {
    public String Name;


    public ControlLayout(InflateView context) {
        super(context);

    }

    public void NewName() {
        Name = "A" + UUID.randomUUID().toString().replace("-", "");
    }

    public void SetSize(int width, int height) {
        try {
            if (height < 20) height = 20;
            if (width < 20) width = 20;

            View child = this.getChildAt(1);
            if (!(child instanceof NumberPicker) && (child instanceof LinearLayout)) {
                ViewGroup.LayoutParams params = child.getLayoutParams();
                params.height = height;
                params.width = width;
                View child1 = ((LinearLayout) child).getChildAt(0);
                params = child1.getLayoutParams();
                params.height = height;
                params.width = width - 32;
                View child2 = ((LinearLayout) child).getChildAt(1);
                params = child2.getLayoutParams();
                params.height = height;
                params.width = 30;
            } else {
                ViewGroup.LayoutParams params = child.getLayoutParams();
                params.height = height;
                params.width = width;
            }
        } catch (Exception ex) { // its a label!


            String sex = ex.getMessage();
        }
    }

    public boolean DoSay(int xpos, int ypos) {
        View np = this.getChildAt(1);
        if (np instanceof NumberPicker) {
            int count = ((NumberPicker) np).getChildCount();
            for (int i = 0; i < count; i++) {
                final View child = ((NumberPicker) np).getChildAt(i);
                if (child instanceof ImageButton) {
                    if (ypos >= child.getY() && ypos <= child.getY() + child.getHeight()-10) {
                        return false;
                    }
                }

            }
        }
        return true;
    }


    public String GetValue() {
        String retval = "";
        View child = this.getChildAt(1);
        if (child instanceof EditText) {
            retval = ((EditText) child).getText().toString();
        } else if (child instanceof NumberPicker) {
            ((NumberPicker) child).clearFocus();
            retval = new Integer(((NumberPicker) child).getValue()).toString();
        } else if (child instanceof ToggleButton) {
            if (((ToggleButton) child).isChecked())
                retval = "True";
            else
                retval = "False";
        } else if (child instanceof CheckBox) {
            if (((CheckBox) child).isChecked())
                retval = "True";
            else
                retval = "False";
        } else if (child instanceof LinearLayout) {
            child = ((LinearLayout) child).getChildAt(0);
            retval = ((EditText) child).getText().toString();
        }
        return retval;
    }

    public void SetValue(String value) {
        String retval = "";
        View child = this.getChildAt(1);
        if (child instanceof EditText) {
            ((EditText) child).setText(value);

        } else if (child instanceof CheckBox) {
            if (value == null) {
                ((CheckBox) child).setChecked(false);
            }
            if (value.equals("True"))
                ((CheckBox) child).setChecked(true);
            else
                ((CheckBox) child).setChecked(false);
        } else if (child instanceof NumberPicker) {
            if (value == null || value.equals(""))
                ((NumberPicker) child).setValue(0);
            else
                ((NumberPicker) child).setValue(Integer.parseInt(value));
        } else if (child instanceof ToggleButton) {
            if (value == null) {
                ((ToggleButton) child).setChecked(false);
            }
            if (value.equals("True"))
                ((ToggleButton) child).setChecked(true);
            else
                ((ToggleButton) child).setChecked(false);
        } else if (child instanceof LinearLayout) {
            child = ((LinearLayout) child).getChildAt(0);
            ((EditText) child).setText(value);
        }
    }

    public void DefaultValue() {
        View child = this.getChildAt(1);
        if (child instanceof EditText) {
            ((EditText) child).setText("");
        } else if (child instanceof CheckBox) {
            ((CheckBox) child).setChecked(false);
        } else if (child instanceof NumberPicker) {
            ((NumberPicker) child).setValue(0);
        } else if (child instanceof ToggleButton) {
            ((ToggleButton) child).setChecked(false);
        }
    }

    public String getDefaultValue() {
        return "";
    }

    public String getDefaultIntValue() {
        return "0";
    }

    public void EditFocus(boolean edit) {
        EditText label = (EditText) ((ControlLayout) this).getChildAt(0);
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
        EditText label=(EditText)((ControlLayout) this).getChildAt(0);
        if (label==null) { // Brok so fix it!
            label=new EditText(mcontext);
            addView(label);
        }
        RelativeLayout.LayoutParams LayoutParameters = (RelativeLayout.LayoutParams) this.getLayoutParams();
        if (label.getText().toString().trim().equals("") && LayoutParameters.topMargin <1){
            //setOrientation(LinearLayout.HORIZONTAL);
            label.setVisibility(View.GONE);
        } else{
            //setOrientation(LinearLayout.VERTICAL);

            label.setVisibility(View.VISIBLE);
        }
        if (edit) {
            //	label.setBackground((new EditText(mcontext)).getBackground());
            label.setTextColor(Color.WHITE);
            label.setClickable(true);
            label.setEnabled(true);
            label.setFocusable(true);
            label.setFocusableInTouchMode(true);
        } else {
            //if (label.getText().equals("")) {
            //    label.setVisibility(View.VISIBLE);
            //}
            this.setBackgroundColor(Color.TRANSPARENT);
            label.setBackgroundColor(Color.TRANSPARENT);
            label.setTextColor(Color.WHITE);
            label.setClickable(false);
            label.setEnabled(false);
            label.setFocusable(false);
            label.setFocusableInTouchMode(false);
        }
        for(int j=1;j<((ControlLayout) this).getChildCount();j++){
            View control=(View)((ControlLayout) this).getChildAt(j);
            if (control instanceof LinearLayout) {
                for(int j1=0;j1<((LinearLayout) control).getChildCount();j1++) {
                    View control1 = ((LinearLayout) control).getChildAt(j1);
                    if (edit) {
                        control1.setClickable(false);
                        control1.setEnabled(false);
                        control1.setFocusable(false);
                        control1.setFocusableInTouchMode(false);
                    } else {
                        control1.setClickable(true);
                        control1.setEnabled(true);
                        if (control1 instanceof Button) {
                            control1.setFocusable(false);
                            control1.setFocusableInTouchMode(false);
                        } else {
                            control1.setFocusable(true);
                            control1.setFocusableInTouchMode(true);
                        }
                    }
                }
            }

            if (edit) {
                control.setClickable(false);
                control.setEnabled(false);
                control.setFocusable(false);
                control.setFocusableInTouchMode(false);
            } else {
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
}
