/*
 * TouchImageView.java
 * By: Edward Ian Hickman
 * Copyright (C) 2015 Edward Ian Hickman
 */

package com.yasfa.views;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by Ian on 19/03/15.
 */
public class ControlManage {
    public static void make (final InflateView MainView,RelativeLayout mainRelativeLayout) {
        if (MainView.Sp1.getSelectedItem().toString() == "Draw") {
            DrawLayout dataList = new DrawLayout(MainView);
            dataList.NewName();
            mainRelativeLayout.addView(dataList);
            ViewGroup.LayoutParams params = dataList.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 100;
            params.width = 100;
            MainView.SetLayout(dataList, RelativeLayout.ALIGN_PARENT_LEFT,  MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 100, 100,0,0);
        } else if (MainView.Sp1.getSelectedItem().toString() == "PictureList") {
            PictureList dataList= new PictureList(MainView.formName,MainView,mainRelativeLayout,true);
            mainRelativeLayout.addView(dataList);
            ViewGroup.LayoutParams params = dataList.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 200;
            params.width = 200;
            MainView.SetLayout(dataList, RelativeLayout.ALIGN_PARENT_LEFT,  MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 200, 200,0,0);
        } else if (MainView.Sp1.getSelectedItem().toString() == "Record") {
            RecordPlay dataList= new RecordPlay(MainView);
            dataList.NewName();
            mainRelativeLayout.addView(dataList);
            ViewGroup.LayoutParams params = dataList.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 200;
            params.width = 200;
            MainView.SetLayout(dataList, RelativeLayout.ALIGN_PARENT_LEFT, MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 200, 200,0,0);
        } else if (MainView.Sp1.getSelectedItem().toString() == "Camera") {
            CameraLayout dataList= new CameraLayout(MainView);
            dataList.NewName();
            mainRelativeLayout.addView(dataList);
            ViewGroup.LayoutParams params = dataList.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 200;
            params.width = 200;
            MainView.SetLayout(dataList, RelativeLayout.ALIGN_PARENT_LEFT,  MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 200, 200,0,0);
        } else if (MainView.Sp1.getSelectedItem().toString() == "DataList") {
            DataList dataList= new DataList(MainView.formName,MainView,mainRelativeLayout);
            mainRelativeLayout.addView(dataList);
            ViewGroup.LayoutParams params = dataList.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 200;
            params.width = 200;
            MainView.SetLayout(dataList, RelativeLayout.ALIGN_PARENT_LEFT, MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 200, 200,0,0);
        } else if (MainView.Sp1.getSelectedItem().toString() == "EditText") {
            ControlLayout layout = new ControlLayout(MainView);
            layout.NewName();
            layout.setBackgroundColor(MainView.editc);
            layout.setOrientation(LinearLayout.VERTICAL);
            EditText label = new YEditText(MainView,true);
            label.setText("<label>");
            EditText control = new EditText(MainView);
            layout.addView(label);
            layout.addView(control);
            control.setText("");
            control.setTextSize(14);
            control.setGravity(control.getGravity() | Gravity.TOP);

            control.setClickable(false);
            control.setEnabled(false);
            control.setFocusable(false);
            control.setFocusableInTouchMode(false);
            layout.setBackgroundColor(MainView.editc);
            layout.setClickable(false);
            layout.setEnabled(false);
            layout.setFocusable(false);
            layout.setFocusableInTouchMode(false);
            mainRelativeLayout.addView(layout);
            MainView.SetLayout(layout, RelativeLayout.ALIGN_PARENT_LEFT, MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);
        }
        else if (MainView.Sp1.getSelectedItem().toString() == "DataSpinner") {
            DataSpinner top = new DataSpinner(MainView.formName,MainView,mainRelativeLayout,true);
            top.NewName();
            mainRelativeLayout.addView(top);
            MainView.SetLayout(top, RelativeLayout.ALIGN_PARENT_LEFT, MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);
        }
        else if (MainView.Sp1.getSelectedItem().toString() == "Slider") {
            SliderLayout top = new SliderLayout(MainView);
            top.Edit(true);
            mainRelativeLayout.addView(top);
            MainView.SetLayout(top, RelativeLayout.ALIGN_PARENT_LEFT, MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);

        } else if (MainView.Sp1.getSelectedItem().toString() == "Spinner") {
            SpinnerLayout top = new SpinnerLayout(MainView);
            top.NewName();
            top.setOrientation(LinearLayout.VERTICAL);
            top.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            final LinearLayout bits = new LinearLayout(MainView);
            bits.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            bits.setOrientation(LinearLayout.VERTICAL);

            LinearLayout layout = new LinearLayout(MainView);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            layout.setOrientation(LinearLayout.VERTICAL);

            EditText label = new YEditText(MainView,true);
            Spinner control = new Spinner(MainView);
            label.setText("<label>");

            layout.addView(label);
            layout.addView(control);

            top.addView(layout);
            top.addView(bits);


            Button b1 = new Button(MainView);
            b1.setText("Add");
            b1.setVisibility(View.VISIBLE);
            bits.addView(b1);
            bits.setVisibility(View.GONE);

            b1.setClickable(true);
            b1.setEnabled(true);
            b1.setFocusable(true);
            b1.setFocusableInTouchMode(true);


            b1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    EditText label = new EditText(MainView);
                    label.setText("");
                    bits.addView(label);
                }
            });



            ArrayList<String> spinnerArray = new ArrayList<String>();
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainView, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            control.setAdapter(spinnerArrayAdapter);

            control.setClickable(false);
            control.setEnabled(false);
            control.setFocusable(false);
            control.setFocusableInTouchMode(false);

            top.setBackgroundColor(MainView.editc);
            top.setClickable(false);
            top.setEnabled(false);
            top.setFocusable(false);
            top.setFocusableInTouchMode(false);

            mainRelativeLayout.addView(top);
            MainView.SetLayout(top, RelativeLayout.ALIGN_PARENT_LEFT, MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);
        }
        else if (MainView.Sp1.getSelectedItem().toString() == "Picture") {
            PictureLayout test = new PictureLayout(MainView,true);
            mainRelativeLayout.addView(test);
            MainView.SetLayout(test, RelativeLayout.ALIGN_PARENT_LEFT, MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 100, 100,0,0);
        }
        else if (MainView.Sp1.getSelectedItem().toString() == "Label") {
            ControlLayout test = new ControlLayout(MainView);
            test.setBackgroundColor(MainView.editc);
            EditText label = new YEditText(MainView,true);
            label.setText("<label>");
            test.addView(label);
            test.setBackgroundColor(MainView.editc);
            test.setClickable(false);
            test.setEnabled(false);
            test.setFocusable(false);
            test.setFocusableInTouchMode(false);
            mainRelativeLayout.addView(test);
            test.Edit(true);
            test.EditFocus(MainView.editMoveOnly);
            MainView.SetLayout(test, RelativeLayout.ALIGN_PARENT_LEFT, MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);
        }
        else if (MainView.Sp1.getSelectedItem().toString() == "SayIt") {
            SayLayout test = new SayLayout(MainView);
            test.setBackgroundColor(MainView.editc);
            test.setClickable(false);
            test.setEnabled(false);
            test.setFocusable(false);
            test.setFocusableInTouchMode(false);
            mainRelativeLayout.addView(test);
            test.Edit(true);
            test.EditFocus(MainView.editMoveOnly);

            test.label.setText("<SayIt>");
            test.label.setTextSize(16);

            Rect bounds = new Rect();
            Paint textPaint = test.label.getPaint();
            textPaint.getTextBounds(test.label.getText().toString(),0,test.label.getText().toString().length(),bounds);

            MainView.SetLayout(test, RelativeLayout.ALIGN_PARENT_LEFT, MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);
            test.SetSize(-2,35);

        }
        else if (MainView.Sp1.getSelectedItem().toString() == "DatePicker") {
            ControlLayout layout = new ControlLayout(MainView);
            ControlLayout layout1 = new ControlLayout(MainView);
            layout.NewName();
            layout.setBackgroundColor(MainView.editb);
            layout.setBaselineAligned(false);
            layout1.setBaselineAligned(false);

            EditText label = new YEditText(MainView,true);
            label.setText("<label>");
            layout.setOrientation(LinearLayout.VERTICAL);
            final EditText control = new EditText(MainView);
            DateButton b1 = new DateButton(MainView,control);

            layout.addView(label);
            layout.addView(layout1);

            layout1.addView(control);
            layout1.addView(b1);

            control.setText("");
            control.setClickable(false);
            control.setEnabled(false);
            control.setFocusable(false);
            control.setFocusableInTouchMode(false);
            control.setTextSize(14);

            b1.setText("...");
            b1.setClickable(false);
            b1.setEnabled(false);
            b1.setFocusable(false);
            b1.setFocusableInTouchMode(false);

            layout.setBackgroundColor(MainView.editc);
            layout.setClickable(false);
            layout.setEnabled(false);
            layout.setFocusable(false);
            layout.setFocusableInTouchMode(false);
            mainRelativeLayout.addView(layout);
            MainView.SetLayout(layout, RelativeLayout.ALIGN_PARENT_LEFT,  MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);
        }
        else if (MainView.Sp1.getSelectedItem().toString() == "TimePicker") {
            ControlLayout layout = new ControlLayout(MainView);
            ControlLayout layout1 = new ControlLayout(MainView);
            layout.NewName();
            layout.setBackgroundColor(MainView.editb);
            layout.setBaselineAligned(false);
            layout1.setBaselineAligned(false);
            layout.setOrientation(LinearLayout.VERTICAL);

            EditText label = new YEditText(MainView,true);
            label.setText("<label>");

            final EditText control = new EditText(MainView);
            Button b1 = new TimeButton(MainView,control);

            layout.addView(label);
            layout.addView(layout1);

            layout1.addView(control);
            layout1.addView(b1);

            control.setText("");
            control.setClickable(false);
            control.setEnabled(false);
            control.setFocusable(false);
            control.setFocusableInTouchMode(false);
            control.setTextSize(14);

            b1.setText("...");
            b1.setClickable(false);
            b1.setEnabled(false);
            b1.setFocusable(false);
            b1.setFocusableInTouchMode(false);

            layout.setBackgroundColor(MainView.editc);
            layout.setClickable(false);
            layout.setEnabled(false);
            layout.setFocusable(false);
            layout.setFocusableInTouchMode(false);
            mainRelativeLayout.addView(layout);
            MainView.SetLayout(layout, RelativeLayout.ALIGN_PARENT_LEFT, MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);

        }
        else if (MainView.Sp1.getSelectedItem().toString() == "NumberPicker") {
            ControlLayout layout = new ControlLayout(MainView);
            layout.NewName();
            layout.setBackgroundColor(MainView.editb);
            layout.setBaselineAligned(false);
            layout.setOrientation(LinearLayout.VERTICAL);
            EditText label = new YEditText(MainView,true);
            label.setText("<label>");
            final NumberPicker control = new NumberPicker(MainView);
            control.setScaleX(.9f);
            control.setScaleY(.9f);
            control.setPadding(0, 0, 0, 0);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(70,140);
            params.resolveLayoutDirection(LayoutDirection.LTR);
            control.setLayoutParams(params);
            control.setGravity(Gravity.TOP);
            layout.addView(label);
            layout.addView(control);

            control.setMaxValue(10000000);
            control.setMinValue(0);

            control.setValue(0);
            control.setClickable(false);
            control.setEnabled(false);
            control.setFocusable(false);
            control.setFocusableInTouchMode(false);

            layout.setBackgroundColor(MainView.editc);
            layout.setClickable(false);
            layout.setEnabled(false);
            layout.setFocusable(false);
            layout.setFocusableInTouchMode(false);
            mainRelativeLayout.addView(layout);
            MainView.SetLayout(layout, RelativeLayout.ALIGN_PARENT_LEFT, MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);
        }

        else if (MainView.Sp1.getSelectedItem().toString() == "Button") {
            ButtonLayout  btn = new ButtonLayout(MainView,true);
            mainRelativeLayout.addView(btn);
            MainView.SetLayout(btn, RelativeLayout.ALIGN_PARENT_LEFT,  MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);
        }
        else if (MainView.Sp1.getSelectedItem().toString() == "CheckBox") {
            ControlLayout layout = new ControlLayout(MainView);
            layout.NewName();
            layout.setBackgroundColor(MainView.editb);
            layout.setOrientation(LinearLayout.VERTICAL);
            EditText label = new YEditText(MainView,true);
            label.setText("<label>");
            CheckBox control = new CheckBox(MainView);
            layout.addView(label);
            layout.addView(control);
            control.setText("");
            control.setClickable(false);
            control.setEnabled(false);
            control.setFocusable(false);
            control.setFocusableInTouchMode(false);
            layout.setBackgroundColor(MainView.editb);
            layout.setClickable(false);
            layout.setEnabled(false);
            layout.setFocusable(false);
            layout.setFocusableInTouchMode(false);
            mainRelativeLayout.addView(layout);
            MainView.SetLayout(layout, RelativeLayout.ALIGN_PARENT_LEFT,  MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);

        }
        else if (MainView.Sp1.getSelectedItem().toString() == "ToggleButton") {
            ControlLayout layout = new ControlLayout(MainView);
            layout.NewName();
            layout.setBackgroundColor(MainView.editc);
            layout.setOrientation(LinearLayout.VERTICAL);
            EditText label = new YEditText(MainView,true);
            label.setText("<label>");
            ToggleButton control = new ToggleButton(MainView);
            layout.addView(label);
            layout.addView(control);
            control.setText("");
            control.setClickable(false);
            control.setEnabled(false);
            control.setFocusable(false);
            control.setFocusableInTouchMode(false);
            layout.setBackgroundColor(MainView.editc);
            layout.setClickable(false);
            layout.setEnabled(false);
            layout.setFocusable(false);
            layout.setFocusableInTouchMode(false);
            mainRelativeLayout.addView(layout);
            MainView.SetLayout(layout, RelativeLayout.ALIGN_PARENT_LEFT,  MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);

        }
        else if (MainView.Sp1.getSelectedItem().toString() == "RadioGroup") {
            final RadioLayout layout = new RadioLayout(MainView);
            layout.NewName();
            layout.setBackgroundColor(MainView.editc);
            Button b1 = new Button(MainView);
            b1.setText("Add");
            b1.setVisibility(View.VISIBLE);

            layout.addView(b1);
            final RadioGroup group = new RadioGroup(MainView);
            layout.addView(group);
            MainView.SetLayout(layout, RelativeLayout.ALIGN_PARENT_LEFT, MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);
            mainRelativeLayout.addView(layout);

            b1.setClickable(true);
            b1.setEnabled(true);
            b1.setFocusable(true);
            b1.setFocusableInTouchMode(true);

            b1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (v.isFocused()) {
                        v.setEnabled(false);
                        EditText label = new EditText(MainView);
                        group.addView(label);
                        label.setClickable(false);
                        v.setEnabled(true);
                    }
                }
            });
        } else if (MainView.Sp1.getSelectedItem().toString() == "Data") {
            final DataLayout layout = new DataLayout(MainView.formName,MainView,mainRelativeLayout);
            MainView.SetLayout(layout, RelativeLayout.ALIGN_PARENT_LEFT,  MainView.scrollable.vScroll.getScrollX()+5, MainView.scrollable.hScroll.getScrollY()+80, 0, 0,0,0);
            layout.Edit(true);
            mainRelativeLayout.addView(layout);
        }

    }
    public static void Edit (InflateView MainView) {
        try {
            if (MainView.Editing)
                MainView.Editing = false;
            else
                MainView.Editing = true;

            if (MainView.Editing) {
                MainView.Sp1.setVisibility(View.VISIBLE);
                MainView.movebutton.setVisibility(View.VISIBLE);
                MainView.buttonEdit.setVisibility(View.VISIBLE);
                MainView.buttonAdd.setVisibility(View.VISIBLE);
                MainView.buttonDel.setVisibility(View.VISIBLE);
            } else {
                MainView.Sp1.setVisibility(View.GONE);
                MainView.movebutton.setVisibility(View.GONE);
                MainView.buttonEdit.setVisibility(View.GONE);
                MainView.buttonAdd.setVisibility(View.GONE);
                MainView.buttonDel.setVisibility(View.GONE);
            }

            for (int i = 0; i < MainView.mainRelativeLayout.getChildCount(); i++) {
                View child = MainView.mainRelativeLayout.getChildAt(i);
                if (child == MainView.topv) {
                    if (MainView.Editing)
                        MainView.topv.setBackgroundColor(Color.argb(240,255 , 255, 0));
                    else
                        MainView.topv.setBackgroundColor(Color.TRANSPARENT);
                } else if (MainView.Editing) {
                    if (child instanceof SpinnerLayout) {
                        child.setBackgroundColor(MainView.editc);
                        View scontrol = (View) ((LinearLayout) child).getChildAt(0);
                        LinearLayout edit = (LinearLayout) ((LinearLayout) child).getChildAt(1);
                        edit.setVisibility(View.GONE);

                        View label = ((LinearLayout) scontrol).getChildAt(0);
                        label.setClickable(true);
                        label.setEnabled(true);
                        label.setFocusable(true);
                        label.setFocusableInTouchMode(true);

                        Spinner c = (Spinner) ((LinearLayout) scontrol).getChildAt(1);
                        c.setClickable(false);
                        c.setEnabled(false);
                        c.setFocusable(false);
                        c.setFocusableInTouchMode(false);

                        View control = (View) ((LinearLayout) edit).getChildAt(0);
                        control.setVisibility(View.VISIBLE);

                        ArrayAdapter<String> spinnerArrayAdapter = (ArrayAdapter<String>) c.getAdapter();

                        for (int j = 0; j < spinnerArrayAdapter.getCount(); ++j) {
                            String value = (String) spinnerArrayAdapter.getItem(j);
                            EditText label1 = new EditText(MainView);
                            label1.setText(value);
                            edit.addView(label1);

                        }
                    } else if (child instanceof RadioLayout) {
                        child.setBackgroundColor(MainView.editc);
                        View b1 = (View) ((LinearLayout) child).getChildAt(0);
                        b1.setVisibility(View.VISIBLE);
                        b1.setClickable(true);
                        b1.setEnabled(true);
                        b1.setFocusable(true);
                        b1.setFocusableInTouchMode(true);
                        android.widget.RadioGroup radioGroup = (android.widget.RadioGroup) ((LinearLayout) child).getChildAt(1);

                        int j = 0;
                        while (j < radioGroup.getChildCount()) {
                            if (radioGroup.getChildAt(j) instanceof RadioButton) {
                                RadioButton label = (RadioButton) radioGroup.getChildAt(j);
                                if (label.getText().toString().equals("")) {
                                    radioGroup.removeView(label);
                                } else {
                                    final EditText control1 = new EditText(MainView);
                                    radioGroup.addView(control1);
                                    control1.setText(label.getText());
                                    control1.setVisibility(View.VISIBLE);
                                    control1.setClickable(true);
                                    control1.setEnabled(true);
                                    control1.setFocusable(true);
                                    control1.setFocusableInTouchMode(true);
                                    radioGroup.removeView(label);
                                }
                            } else {
                                ++j;
                            }
                        }
                        radioGroup.setEnabled(true);
                        radioGroup.setSaveEnabled(true);
                    } else if (child instanceof YASFAControl) {
                        child.setBackgroundColor(MainView.editd);
                        ((YASFAControl) child).Edit(true);
                    } else {
                        child.setBackgroundColor(MainView.editc);

                    }
                    child.setClickable(false);
                    child.setEnabled(false);
                    child.setFocusable(false);
                    child.setFocusableInTouchMode(false);
                } else {
                    if (child instanceof SpinnerLayout) {
                        child.setBackgroundColor(Color.TRANSPARENT);
                        View scontrol = (View) ((LinearLayout) child).getChildAt(0);
                        View edit = (View) ((LinearLayout) child).getChildAt(1);

                        EditText label = (EditText) ((LinearLayout) scontrol).getChildAt(0);
                        label.setBackgroundColor(Color.TRANSPARENT);
                        label.setTextColor(Color.WHITE);
                        label.setClickable(false);
                        label.setEnabled(false);
                        label.setFocusable(false);
                        label.setFocusableInTouchMode(false);

                        Spinner sp = (Spinner) ((LinearLayout) scontrol).getChildAt(1);
                        sp.setClickable(true);
                        sp.setEnabled(true);
                        sp.setFocusable(false);
                        sp.setFocusableInTouchMode(false);

                        View button = (View) ((LinearLayout) edit).getChildAt(0);
                        button.setVisibility(View.GONE);
                        ArrayList<String> spinnerArray = new ArrayList<String>();

                        int j = 1;
                        while (j < ((LinearLayout) edit).getChildCount()) {
                            EditText control = (EditText) ((LinearLayout) edit).getChildAt(j);
                            if (control.getText().toString().equals("")) {
                                ((LinearLayout) edit).removeView(control);
                            } else {
                                spinnerArray.add(control.getText().toString());
                                control.setVisibility(View.GONE);
                                ((LinearLayout) edit).removeView(control);
                            }
                        }
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainView, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp.setAdapter(spinnerArrayAdapter);
                        edit.setVisibility(View.GONE);
                    } else if (child instanceof RadioLayout) {
                        child.setBackgroundColor(Color.TRANSPARENT);
                        View b1 = (View) ((LinearLayout) child).getChildAt(0);
                        b1.setVisibility(View.GONE);
                        b1.setClickable(false);
                        b1.setEnabled(false);
                        b1.setFocusable(false);
                        b1.setFocusableInTouchMode(false);

                        RadioGroup radioGroup = (RadioGroup) ((LinearLayout) child).getChildAt(1);
                        RadioGroup radioNew =  new RadioGroup(MainView);
                        int j = 0;
                        int q=0;
                        while (j < radioGroup.getChildCount()) {
                            if (radioGroup.getChildAt(j) instanceof EditText) {
                                EditText label = (EditText) radioGroup.getChildAt(j);
                                if (label.getText().toString().equals("")) {
                                    radioGroup.removeView(label);
                                } else {
                                    final RadioButton control1 = new RadioButton(MainView);
                                    control1.setText(label.getText());
                                    control1.setTextColor(Color.WHITE);
                                    control1.setId(++q);
                                    control1.setClickable(true);
                                    control1.setEnabled(true);
                                    control1.setFocusable(false);
                                    control1.setFocusableInTouchMode(false);
                                    radioNew.addView(control1);
                                    radioGroup.removeView(label);
                                }
                            } else {
                                ++j;
                            }
                        }
                        ((RadioLayout) child).removeView(radioGroup);
                        ((RadioLayout) child).addView(radioNew);
                        radioNew.setEnabled(true);
                        radioNew.setSaveEnabled(true);

                    } else if (child instanceof ButtonLayout) {
                        try {
                            child.setBackgroundColor(MainView.editd);
                            ((ButtonLayout) child).Edit(false);
                        } catch (Exception ex) {
                        }
                    } else if (child instanceof YASFAControl) {
                            ((YASFAControl) child).Edit(false);
                    } else
                        child.setBackgroundColor(Color.WHITE);

                    child.setClickable(true);
                    child.setEnabled(true);
                    child.setFocusable(true);
                    child.setFocusableInTouchMode(true);
                    MainView.Selected = null;
                }
            }
        } catch (Exception exn) {
            String ex = exn.getMessage();
        }
    }

}
