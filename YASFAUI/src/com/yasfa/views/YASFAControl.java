
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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import java.util.UUID;

/**
 * Created by Ian on 16/05/15.
 */
public class YASFAControl extends LinearLayout {
    public String Name;
    protected InflateView  mcontext;

    public void NewName() {
        Name ="S"+ UUID.randomUUID().toString().replace("-","");
    }

    public YASFAControl(InflateView context) {
        super(context);
        mcontext=context;
    }

    public void SetSize(int width,int height) {
    }

    public boolean DoSay (int x,int y) {
        return true;
    }

    public String SayText() {
        try {
            if (((ControlLayout) this).getChildCount() < 2) {
                return GetLabel();
            }
        } catch (Exception ex) {}
        return /*GetLabel()+" "+*/GetValue();
    }

    public String GetLabel() {
        try {
            return ((YEditText) ((ControlLayout) this).getChildAt(0)).getText().toString();
        } catch (Exception ex) {
            return "";
        }
    }


    public byte [] GetByteValue()
    {
        return null;
    }

    public void SetValue(byte [] itemBytes) {
    }

    public void DefaultValue() {
    }


    public void EditFocus (boolean edit) {

    }

    public void Edit (boolean edit) {
    }


    public String GetValue() {

        return "";
    }

    public void SetValue(String value) {

    }

    @Override
    protected void finalize () throws Throwable {
        super.finalize();

    }
    public void Destroy() {
    }

    private void SetLayout(View item, int centerInParent, int marginLeft, int marginTop, int marginRight, int marginBottom) {
    }
}
