/*
 * TouchImageView.java
 * By: Edward Ian Hickman
 * Copyright (C) 2015 Edward Ian Hickman
 */
package com.yasfa.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimeButton extends Button{

	public TimeButton(final Context context, final EditText control) {
		super(context);

        final TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            // onDateSet method
            public void onTimeSet(TimePicker view, int hour, int min) {
                String time_selected = String.format("%02d",hour)+":"+String.format("%02d", min);
                control.setText(time_selected);
            }
        };

        this.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR);
                int min = c.get(Calendar.MINUTE);

                TimePickerDialog dp = new TimePickerDialog(context, min, mTimeSetListener, hour, min, false);
                dp.show();

            }
        });

		// TODO Auto-generated constructor stub
	}

}
