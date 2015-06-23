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
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.UUID;

public class SliderLayout extends YASFAControl {
	public String Name;
    SeekBar sBar;
    TextView mvalue;
    TextView mmin;
    TextView mmax;
    RelativeLayout mbits;
    int minval=0;
    int maxval=100;
    int place=0;

    public void EditFocus(boolean Edit) {
      /* not implemented!  LinearLayout edit = (LinearLayout)((LinearLayout) this).getChildAt(1);
        if (!Edit) {
            edit.setVisibility(View.GONE);
        } else {
            edit.setVisibility(View.VISIBLE);
            for(int j=0;j<((LinearLayout) edit).getChildCount();j++) {
                View control = (View) ((LinearLayout) edit).getChildAt(j);
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
     }*/
    }

    public void SetSize(int width,int height) {
        android.view.ViewGroup.LayoutParams params;
        params = sBar.getLayoutParams();
        params.width = width;
        params = mbits.getLayoutParams();
        params.width = width;
        RelativeLayout.LayoutParams LayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutParameters.setMargins((width/2)-60,0,0,0);
        mvalue.setLayoutParams(LayoutParameters);
        params = mvalue.getLayoutParams();
        params.height = 18;
        params.width = 100;
        RelativeLayout.LayoutParams LayoutParameters1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutParameters1.setMargins(0,0,0,0);
        mmin.setLayoutParams(LayoutParameters1);
        params = mmin.getLayoutParams();
        params.height = 18;
        params.width = 20;
        RelativeLayout.LayoutParams LayoutParameters2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutParameters2.setMargins(width-55,0,0,0);
        mmax.setLayoutParams(LayoutParameters2);
        params = mmax.getLayoutParams();
        params.height = 18;
        params.width = 100;
    }

    public SliderLayout(final InflateView context) {
		super(context);

        NewName();
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mbits = new RelativeLayout(context);
        mbits.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.TRANSPARENT);

        EditText label = new YEditText((InflateView) context,false);
        label.setText("<label>");
        addView(label);

        mmin = new EditText(context);
        mmin.setPadding(2,2,2,2);
        mmin.setTextColor(Color.WHITE);
        mmin.setBackgroundColor(Color.BLACK);
        mmin.setEnabled(false);
        mmin.setTextSize(14);
        mmin.setText("0");
        mmin.setGravity(Gravity.CENTER_HORIZONTAL);
        mbits.addView(mmin);

        mvalue = new EditText(context);
        mvalue.setPadding(2,2,2,2);
        mvalue.setTextColor(Color.WHITE);
        mvalue.setBackgroundColor(Color.BLACK);
        mvalue.setEnabled(false);
        mvalue.setTextSize(14);
        mvalue.setText("");
        mvalue.setGravity(Gravity.CENTER_HORIZONTAL);
        mbits.addView(mvalue);

        mmax = new EditText(context);
        mmax.setPadding(2,2,2,2);
        mmax.setTextColor(Color.WHITE);
        mmax.setBackgroundColor(Color.BLACK);
        mmax.setEnabled(false);
        mmax.setTextSize(14);
        mmax.setText("100");
        mmax.setGravity(Gravity.CENTER_HORIZONTAL);
        mbits.addView(mmax);


        mbits.setBackgroundColor(Color.BLACK);
        layout.setBackgroundColor(Color.BLACK);
        sBar = new SeekBar(mcontext);
        SeekBar.OnSeekBarChangeListener abc = new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    int p = sBar.getProgress();
                    mvalue.setText(String.valueOf(p));
                } catch (Exception ex) {}
                //
            }
        };
        sBar.setOnSeekBarChangeListener(abc);
        LayoutParams sBarLayParams=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

        layout.addView(sBar,sBarLayParams);
        SetSize(200,100);
        addView(mbits);
        addView(layout);
        sBar.setMax(100);
	}
	
	public void NewName() {
		Name="S"+UUID.randomUUID().toString().replace("-","");
	}

	public String GetValue() {
        place=sBar.getProgress();
		return String.valueOf(place);
	}

    public void SetValue(String value) {
        place=0;
        try {
            place=Integer.parseInt(value);
            sBar.setProgress(place);
        } catch (Exception ex) {
            sBar.setProgress(0);
        }
    }

	public void DefaultValue() {
        sBar.setProgress(0);
	}

    public void Edit (boolean edit) {
        EditText label=(EditText)((SliderLayout) this).getChildAt(0);
        if (label==null) { // Brok so fix it!
            label=new EditText(mcontext);
            addView(label);
        }
/*        RelativeLayout.LayoutParams LayoutParameters = (RelativeLayout.LayoutParams) this.getLayoutParams();
        if (label.getText().toString().trim().equals("") && LayoutParameters.topMargin <1){
            //setOrientation(LinearLayout.HORIZONTAL);
            label.setVisibility(View.GONE);
        } else{
            label.setVisibility(View.VISIBLE);
        }
*/
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
    }

}
