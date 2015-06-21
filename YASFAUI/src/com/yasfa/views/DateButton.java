/*
 * TouchImageView.java
 * By: Edward Ian Hickman
 * Copyright (C) 2015 Edward Ian Hickman
 */

package com.yasfa.views;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class DateButton extends Button{

	public DateButton(final Context context,final EditText control) {
		super(context);
		
        final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        	// onDateSet method
        	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	String date_selected = String.format("%02d",monthOfYear+1)+"/"+String.format("%02d",dayOfMonth)+"/"+String.valueOf(year);
        	control.setText(date_selected);
        	}
        };
        
        this.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Calendar c = Calendar.getInstance();
            	int cyear = c.get(Calendar.YEAR);
            	int cmonth = c.get(Calendar.MONTH);
            	int cday = c.get(Calendar.DAY_OF_MONTH);
            	
            	DatePickerDialog dp = new DatePickerDialog (context,  mDateSetListener,  cyear, cmonth, cday);
            	dp.show();

            }
        });

		// TODO Auto-generated constructor stub
	}

}
