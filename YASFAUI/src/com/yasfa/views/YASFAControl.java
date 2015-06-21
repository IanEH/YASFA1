/*
 * TouchImageView.java
 * By: Edward Ian Hickman
 * Copyright (C) 2015 Edward Ian Hickman
 */

package com.yasfa.views;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
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
