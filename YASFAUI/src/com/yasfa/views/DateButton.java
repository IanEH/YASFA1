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
