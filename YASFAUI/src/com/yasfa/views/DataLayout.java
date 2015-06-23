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

import com.yasfa.views.DBInterface.Direction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DataLayout extends LinearLayout {
    final EditText row;
    InflateView mMainView;
	public DataLayout(final String formName,final InflateView MainView,final RelativeLayout mainRelativeLayout) {
		super(MainView);
        mMainView=MainView;
		setBaselineAligned(false);
		row  = new EditText(MainView);
		row.setText("");
        row.setEnabled(false);
        row.setBackgroundColor(Color.GRAY);
		row.setVisibility(View.VISIBLE);


		Button first = new Button(MainView);
		first.setText("<<");
		first.setVisibility(View.VISIBLE);
		addView(first);
		first.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DBInterface dbz = new DBInterface();
            	dbz.setBaseContext(MainView);
            	if (MainView.RowID!=-1) {
            		dbz.Save(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout);
            	}
            	MainView.RowID = dbz.Get(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout,Direction.First);
            	row.setText(MainView.RowID.toString());
            }
		});
            	
		Button prev = new Button(MainView);
		prev.setText("<");
		prev.setVisibility(View.VISIBLE);
		addView(prev);
		prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DBInterface dbz = new DBInterface();
            	dbz.setBaseContext(MainView);
            	if (MainView.RowID!=-1) {
            		dbz.Save(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout);
            	}
            	MainView.RowID = dbz.Get(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout,Direction.Prev);
            	row.setText(MainView.RowID.toString());
            }
		});

		addView(row);
		
		Button next = new Button(MainView);
		next.setText(">");
		next.setVisibility(View.VISIBLE);
		addView(next);
		next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DBInterface dbz = new DBInterface();
            	dbz.setBaseContext(MainView);
            	if (MainView.RowID!=-1) {
            		dbz.Save(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout);
            	}
            	MainView.RowID = dbz.Get(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout,Direction.Next);
            	row.setText(MainView.RowID.toString());
            }
		});


		Button last = new Button(MainView);
		last.setText(">>");
		last.setVisibility(View.VISIBLE);
		addView(last);
		last.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DBInterface dbz = new DBInterface();
            	dbz.setBaseContext(MainView);
            	if (MainView.RowID!=-1) {
            		dbz.Save(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout);
            	}
            	MainView.RowID = dbz.Get(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout,Direction.Last);
            	row.setText(MainView.RowID.toString());
            }
		});

		Button Save = new Button(MainView);
		Save.setText("Save");
		Save.setVisibility(View.VISIBLE);
		addView(Save);
		Save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DBInterface dbz = new DBInterface();
            	dbz.setBaseContext(MainView);
            	MainView.RowID = dbz.Save(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout);
            	row.setText(MainView.RowID.toString());
            }
		});

		Button newi = new Button(MainView);
		newi.setText("New");
		newi.setVisibility(View.VISIBLE);
		addView(newi);
		newi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DBInterface dbz = new DBInterface();
            	dbz.setBaseContext(MainView);
            	MainView.RowID = dbz.Get(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout,Direction.New);
            	row.setText(MainView.RowID.toString());
            }
		});

		Button del = new Button(MainView);
        del.setText("Del");
		del.setVisibility(View.VISIBLE);
		addView(del);
		del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
           		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
           		    @Override
           		    public void onClick(DialogInterface dialog, int which) {
           		        switch (which){
           		        case DialogInterface.BUTTON_POSITIVE:
           	            	DBInterface dbz = new DBInterface();
           	            	dbz.setBaseContext(MainView);
           	            	MainView.RowID = dbz.Delete(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout);
           	            	row.setText(MainView.RowID.toString());
           		            break;

           		        case DialogInterface.BUTTON_NEGATIVE:
           		            //No button clicked
           		            break;
           		        }
           		    }
           		};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainView);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
		});
	}

    public void Refresh(String sel) {
        row.setText(mMainView.RowID.toString());
    }

    public void Edit(boolean Edit) {
        if (Edit) {
            this.setBackgroundColor(InflateView.edite);
        } else {
            setBackgroundColor(Color.TRANSPARENT);
        }
        for(int j=0;j<this.getChildCount();j++){
            View control=(View)this.getChildAt(j);
            if (Edit) {
                control.setClickable(false);
                control.setEnabled(false);
                control.setFocusable(false);
                control.setFocusableInTouchMode(false);
            } else {
                control.setClickable(true);
                control.setEnabled(true);
                control.setFocusable(false);
                control.setFocusableInTouchMode(false);
            }
        }
    }
}
