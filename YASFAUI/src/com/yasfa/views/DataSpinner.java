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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.UUID;

public class DataSpinner extends YASFAControl {
	public String Name;
    RelativeLayout mmainRelativeLayout;
    String mformName;
    int size = 15;
    Long ID1=-1l;
    Long ID2=-1l;
    Long ID3=-1l;
    public YEditText label;
    public Spinner control1;
    public Spinner control2;
    public Spinner control3;
    public EditText edit1;
    public EditText edit2;
    public EditText edit3;

    public boolean Refresh(int level) {
        ArrayList<com.yasfa.views.DataList.ItemDetails> listitems = new ArrayList<com.yasfa.views.DataList.ItemDetails>();
        com.yasfa.views.DBInterface dbz = new com.yasfa.views.DBInterface();
        dbz.setBaseContext(mcontext);

        LinearLayout bottom = (LinearLayout) this.getChildAt(1);
        EditText E1 = (EditText) bottom.getChildAt(0);
        EditText E2 = (EditText) bottom.getChildAt(1);
        EditText E3 = (EditText) bottom.getChildAt(2);

        LinearLayout test = (LinearLayout) this.getChildAt(0);
        final Spinner control = (Spinner) test.getChildAt(1);
        com.yasfa.views.FormSerialize fz = new com.yasfa.views.FormSerialize();
        fz.setBaseContext(mcontext);

        if (level==0) {
            ArrayList<String> spinnerArray1 = new ArrayList<String>();
            ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, spinnerArray1); //selected item will look like a spinner set from XML
            spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            FormSerialize.Fields cols = fz.FieldList(E1.getText().toString(), true);
            long ID = -1;
            com.yasfa.views.DBInterface.Row row;
            while ((row = dbz.GetShortList(ID, -1, E1.getText().toString(), cols.fieldList,cols.image,cols.image1,cols.sound1,true,false)) != null) {
                int len = row.text.length();
                if (len > size) len = size;
                spinnerArray1.add(row.ID.toString() + "-" + row.text.trim().substring(0, len));
                ID = row.ID;
            }
            control.setAdapter(spinnerArrayAdapter1);
            SetValue(1, ID1);
        }
        final Spinner control1 = (Spinner) test.getChildAt(2);
        if (level==0 || level==1) {

            ArrayList<String> spinnerArray2 = new ArrayList<String>();
            ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, spinnerArray2); //selected item will look like a spinner set from XML
            spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            FormSerialize.Fields cols = fz.FieldList(E2.getText().toString(), true);
            Long ID = -1l;
            DBInterface.Row row;
            while ((row = dbz.GetShortList(ID, ID1, E2.getText().toString(), cols.fieldList,cols.image,cols.image1,cols.sound1,true,false)) != null) {
                int len = row.text.length();
                if (len > size) len = size;
                spinnerArray2.add(row.ID.toString() + "-" + row.text.trim().substring(0, len));
                ID = row.ID;
            }

            control1.setAdapter(spinnerArrayAdapter2);
            SetValue(2, ID2);
        }

        final Spinner control2 = (Spinner) test.getChildAt(3);

        if (level==0 || level==2) {
             ArrayList<String> spinnerArray3 = new ArrayList<String>();
            ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, spinnerArray3); //selected item will look like a spinner set from XML
            spinnerArrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            RelativeLayout mainRelativeLayout2 = new RelativeLayout(mcontext);

            FormSerialize.Fields cols = fz.FieldList(E3.getText().toString(), true);
            Long ID = -1l;
            DBInterface.Row row;
            while ((row = dbz.GetShortList(ID, ID2, E3.getText().toString(), cols.fieldList,cols.image,cols.image1,cols.sound1,true,false)) != null) {
                int len = row.text.length();
                if (len > size) len = size;
                spinnerArray3.add(row.ID.toString() + "-" + row.text.trim().substring(0, len));
                ID = row.ID;
            }
            control2.setAdapter(spinnerArrayAdapter3);
            SetValue(3, ID3);
        }

        control.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        String selected = control.getSelectedItem().toString();
                        int p = selected.indexOf("-");
                        if (p > 0) {

                            String pr = selected.substring(0, p);
                            ID1 = Long.parseLong(selected.substring(0, p));
                            ID2 = -1l;
                            ID3 = -1l;
                            Refresh(1);
                            Refresh(2);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        control1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        String selected = control1.getSelectedItem().toString();
                        int p = selected.indexOf("-");
                        if (p > 0) {
                            String pr = selected.substring(0, p);
                            ID2 = Long.parseLong(selected.substring(0, p));
                            ID3 = -1l;
                            Refresh(2);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        control2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        String selected = control2.getSelectedItem().toString();
                        int p = selected.indexOf("-");
                        if (p > 0) {
                            String pr = selected.substring(0, p);
                            ID3 = Long.parseLong(selected.substring(0, p));
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        return true;
    }

    public void SetValue(int place,Long ID) {
        LinearLayout test=(LinearLayout)this.getChildAt(0);
        View test1=test.getChildAt(place);
        if (test1 instanceof Spinner) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) ((Spinner) test1).getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).toString().startsWith(ID + "-")) {
                    ((Spinner) test1).setSelection(i);
                    break;
                }
            }
        }
    }





	public DataSpinner (final String formName,final InflateView MainView,final RelativeLayout mainRelativeLayout,boolean edit) {
            super(MainView);
            mmainRelativeLayout=mainRelativeLayout;
            mformName=formName;

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setBackgroundColor(InflateView.editc);

        final LinearLayout bits = new LinearLayout(MainView);
        bits.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        bits.setOrientation(LinearLayout.VERTICAL);
        edit1 = new EditText(MainView);
        bits.addView(edit1);
        edit2 = new EditText(MainView);
        bits.addView(edit2);
        edit3 = new EditText(MainView);
        bits.addView(edit3);

        LinearLayout layout = new LinearLayout(MainView);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        label = new YEditText(MainView,edit);
        label.setText("<label>");
        layout.addView(label);

        control1 = new Spinner(MainView);
        layout.addView(control1);
        control2 = new Spinner(MainView);
        layout.addView(control2);
        control3 = new Spinner(MainView);
        layout.addView(control3);

        addView(layout);
        addView(bits);

        Edit(edit);
    }

    public void EditFocus (boolean edit) {
        label.Edit(edit);
        if (edit) {
            setBackgroundColor(InflateView.editc);
            edit1.setVisibility(View.VISIBLE);
            edit2.setVisibility(View.VISIBLE);
            edit3.setVisibility(View.VISIBLE);
            control1.setVisibility(View.GONE);
            control2.setVisibility(View.GONE);
            control3.setVisibility(View.GONE);
        } else {
            setBackgroundColor(InflateView.editc);
            edit1.setVisibility(View.GONE);
            edit2.setVisibility(View.GONE);
            edit3.setVisibility(View.GONE);
            control1.setVisibility(View.GONE);
            control2.setVisibility(View.GONE);
            control3.setVisibility(View.GONE);
            if (!edit1.getText().toString().equals("")) control1.setVisibility(View.VISIBLE);
            if (!edit2.getText().toString().equals("")) control2.setVisibility(View.VISIBLE);
            if (!edit3.getText().toString().equals("")) control3.setVisibility(View.VISIBLE);
        }
    }

    public void Edit (boolean edit) {
        label.Edit(edit);
        if (edit) {
            setBackgroundColor(InflateView.editc);
            edit1.setVisibility(View.VISIBLE);
            edit2.setVisibility(View.VISIBLE);
            edit3.setVisibility(View.VISIBLE);
            control1.setVisibility(View.GONE);
            control2.setVisibility(View.GONE);
            control3.setVisibility(View.GONE);
        } else {
            setBackgroundColor(Color.TRANSPARENT);
            edit1.setVisibility(View.GONE);
            edit2.setVisibility(View.GONE);
            edit3.setVisibility(View.GONE);
            control1.setVisibility(View.GONE);
            control2.setVisibility(View.GONE);
            control3.setVisibility(View.GONE);
            if (!edit1.getText().toString().equals("")) control1.setVisibility(View.VISIBLE);
            if (!edit2.getText().toString().equals("")) control2.setVisibility(View.VISIBLE);
            if (!edit3.getText().toString().equals("")) control3.setVisibility(View.VISIBLE);
        }
    }

    public void NewName() {
		Name="S"+UUID.randomUUID().toString().replace("-","");
	}

	public String GetValue() {
		String retval="";
        try {
            LinearLayout test = (LinearLayout) this.getChildAt(0);
            View test1 = test.getChildAt(1);
            if (test1 instanceof Spinner) {
                String selected = ((Spinner) test1).getSelectedItem().toString();
                int p = selected.indexOf("-");
                String pr = selected.substring(0, p);
                retval=retval+selected.substring(0, p)+"-";
            }
        } catch (Exception ex) {}
        try {
            LinearLayout test = (LinearLayout) this.getChildAt(0);
            View test1 = test.getChildAt(2);
            if (test1 instanceof Spinner) {
                String selected = ((Spinner) test1).getSelectedItem().toString();
                int p = selected.indexOf("-");
                String pr = selected.substring(0, p);
                retval=retval+selected.substring(0, p)+"-";
            }
        } catch (Exception ex) {}
        try {
            LinearLayout test = (LinearLayout) this.getChildAt(0);
            View test1 = test.getChildAt(3);
            if (test1 instanceof Spinner) {
                String selected = ((Spinner) test1).getSelectedItem().toString();
                int p = selected.indexOf("-");
                String pr = selected.substring(0, p);
                retval=retval+selected.substring(0, p)+"-";
            }
        } catch (Exception ex) {}
		return retval;
	}
	
	public void SetValue(String value) {
        try {
            LinearLayout test = (LinearLayout) this.getChildAt(0);
            String[] vl = value.split("-");

            for (int i = 1; i <= vl.length; ++i) {
                View test1 = test.getChildAt(i);
                if (test1 instanceof Spinner) {
                    SetValue(i, Long.parseLong(vl[i - 1]));
                }

            }
        } catch (Exception ex) {}

	}
	
	public void DefaultValue() {
	}

    public void SetSize(int width, int height) {
            if (height < 20) height = 20;
            if (width < 20) width = 20;
            try {
                LinearLayout test = (LinearLayout) this.getChildAt(0);
                View test1 = test.getChildAt(1);
                if (test1 instanceof Spinner) {
                    ViewGroup.LayoutParams params = test1.getLayoutParams();
                    params.width = width;
                }
            } catch (Exception ex) {}
            try {
                LinearLayout test = (LinearLayout) this.getChildAt(0);
                View test1 = test.getChildAt(2);
                if (test1 instanceof Spinner) {
                    ViewGroup.LayoutParams params = test1.getLayoutParams();
                    params.width = width;
                }
            } catch (Exception ex) {}
            try {
                LinearLayout test = (LinearLayout) this.getChildAt(0);
                View test1 = test.getChildAt(3);
                if (test1 instanceof Spinner) {
                    ViewGroup.LayoutParams params = test1.getLayoutParams();
                    params.width = width;
                }
            } catch (Exception ex) {}
    }
}
