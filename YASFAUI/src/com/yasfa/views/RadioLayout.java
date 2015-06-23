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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class RadioLayout  extends LinearLayout {
    public String Name;
    Context mcontext;

    public RadioLayout(Context context) {
    super(context);
        mcontext=context;
			// TODO Auto-generated constructor stub
	}

    public void NewName() {
        Name="S"+ UUID.randomUUID().toString().replace("-","");
    }

    public String GetValue() {
        String retval = "";
        try {
            View RadioGroup = getChildAt(1);
            for (int i = 0; i < ((LinearLayout) RadioGroup).getChildCount(); ++i) {
                if (((LinearLayout) RadioGroup).getChildAt(i) instanceof RadioButton) {
                    RadioButton item = (RadioButton) ((LinearLayout) RadioGroup).getChildAt(i);
                    item.setClickable(true);
                    item.setEnabled(true);
                    if (item.isChecked()) {
                        retval = item.getText().toString();
                        break;
                    }
                }
            }
        } catch (Exception ex){
            String sex=ex.getMessage();
        }
        return retval;
    }

    public void SetValue(String Value) {
        RadioGroup radioGroup = (RadioGroup)getChildAt(1);
        radioGroup.clearCheck();
        try {
         for (int i=0;i<radioGroup.getChildCount();++i){
                if (radioGroup.getChildAt(i) instanceof RadioButton) {
                    RadioButton item= (RadioButton)radioGroup.getChildAt(i);
                    item.setClickable(true);
                    item.setEnabled(true);

                    if (item.getText().toString().equals(Value)) {
                        item.setChecked(true);
                        break;
                    }
                }
            }
    } catch (Exception ex){
        String sex=ex.getMessage();
    }

}

    public void DefaultValue() {
        RadioGroup radioGroup = (RadioGroup)getChildAt(1);
        radioGroup.clearCheck();
    }


}
