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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ButtonLayout extends YASFAControl {
    InflateView MainView;

    EditText label;
    Spinner spinner;
    EditText action;
    FButton button;
    Button setimage;

    public String GetValue() {
        return button.getText().toString();
    }
    public String SayText() {
        return GetValue()/*+" Button"*/;
    }

    public byte[] GetByteValue() {
        Drawable img = button.getBackground();
        if (img != null) {
            try {
                Bitmap bitmap = ((BitmapDrawable) img).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                return stream.toByteArray();
            } catch (Exception ex) {
            }
        }
        return new byte[1];
    }

    public void loadImage() {
        loadit ld = new loadit();
        Dialog dg = MainView.CreateDialog(MainView.DIALOG_LOAD_FILE, ".png", ld);
    }

    class loadit extends LoadAFile
    {
        @Override
        public void LoadFile (String file){
            getBitmapFromFile(file);
            button.setTextSize(0);
            SetSize(53, 45);
        }
    }

    public void getBitmapFromFile(String src) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(src, options);
            BitmapDrawable bmd = new BitmapDrawable(bitmap);
            button.setBackground(bmd);
        } catch (Exception e) {
            // Log exception
        }
    }



    public void SetValue(byte[] ItemBytes) {
        if (ItemBytes != null && ItemBytes.length > 1) {
           try {
               Bitmap bitmapOrg = BitmapFactory.decodeByteArray(ItemBytes, 0, ItemBytes.length);
               BitmapDrawable bmd = new BitmapDrawable(bitmapOrg);
               button.setTextSize(0);
               button.setBackground(bmd);
           } catch (Exception ex) {
             String exp=ex.getMessage();
           }
        } else {
            DefaultValue();
        }
    }

    public void SetSize(int width, int height) {
        ViewGroup.LayoutParams params = button.getLayoutParams();
        if (height<20) height=20;
        if (width<20) width=20;
        params.height = height;
        params.width = width;
    }

    public void DefaultValue() {

    }


    public void Edit (boolean edit) {
       if (edit) {
           setBackgroundColor(MainView.editc);
           spinner.setVisibility(View.GONE);
           action.setVisibility(View.GONE);
           setimage.setVisibility(View.GONE);
           label.setVisibility(View.GONE);
           button.setVisibility(View.VISIBLE);
           button.setEnabled(false);
       } else {
           setBackgroundColor(Color.TRANSPARENT);
           spinner.setVisibility(View.GONE);
           action.setVisibility(View.GONE);
           setimage.setVisibility(View.GONE);
           label.setVisibility(View.GONE);
           button.setVisibility(View.VISIBLE);
           button.setEnabled(true);

       }

    }

    public void EditFocus(boolean Edit) {
        if (Edit) {
            spinner.setVisibility(View.VISIBLE);
            action.setVisibility(View.VISIBLE);
            setimage.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
            button.setEnabled(false);
        } else {
            spinner.setVisibility(View.GONE);
            action.setVisibility(View.GONE);
            setimage.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            button.setEnabled(false);
        }
    }

	public ButtonLayout(InflateView context,boolean edit) {
			super(context);
            setOrientation(LinearLayout.VERTICAL);
            MainView=context;


        label = new EditText(MainView);
        spinner = new Spinner(MainView);
        action = new EditText(MainView);
        button = new FButton(MainView,"");
        setimage = new Button(MainView);

        addView(label);
        addView(button);
        addView(spinner);
        addView(action);
        addView(setimage);

        setimage.setText("Set Image");
        setimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadImage();
            }
        });

        label.setText("<button>");
        label.setClickable(true);
        label.setEnabled(true);
        label.setFocusable(true);
        label.setFocusableInTouchMode(true);

        action.setText("<Action>");
        action.setSingleLine();
        action.setWidth(100);
        action.setClickable(true);
        action.setEnabled(true);
        action.setFocusable(true);
        action.setFocusableInTouchMode(true);

        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Load");
        spinnerArray.add("LoadN");
        spinnerArray.add("Close");
        spinnerArray.add("Save");
        spinnerArray.add("Get");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainView, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        label.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    button.setText(s);
            }
        });

        button.setText("<button>");

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (spinner.getSelectedItem().toString().equals("Load")) {
                    if (MainView.RowID == -1 && MainView.ParentRowID != -1) { // No Parent so no  chldren!
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainView);
                        builder.setMessage("No Parent!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                        return;
                    }
                    Intent myIntent = new Intent(MainView, InflateView.class);
                    String form = "";
                    if (action.getText().toString().equals("")) {
                        form = "main";
                    } else {
                        form = action.getText().toString();
                    }
                    myIntent.putExtra("formName", form); //Optional parameters
                    myIntent.putExtra("ParentRowID", MainView.RowID); //Optional parameters
                    myIntent.putExtra("ParentformName", MainView.formName); //Optional parameters
                    String ParentText = "";
                    if (MainView.RowID != -1) {
                        DBInterface dbz = new DBInterface();
                        dbz.setBaseContext(MainView);
                        long ID = -1;

                        DBInterface.Row row;

                        if ((row = dbz.GetList(MainView.RowID, -1, MainView.formName, MainView.mainRelativeLayout, "", false)) != null) {
                            ParentText = row.text;
                        }

                    }
                    myIntent.putExtra("ParentRow", ParentText); //Optional parameters


                    MainView.startActivity(myIntent);
                } else if (spinner.getSelectedItem().toString().equals("LoadN")) {

                    Intent myIntent = new Intent(MainView, InflateView.class);
                    String form = "";
                    if (action.getText().toString().equals("")) {
                        form = "main";
                    } else {
                        form = action.getText().toString();
                    }
                    myIntent.putExtra("formName", form); //Optional parameters
                    myIntent.putExtra("ParentRowID", -1); //Optional parameters
                    myIntent.putExtra("ParentformName", MainView.formName); //Optional parameters
                    myIntent.putExtra("ParentRow", ""); //Optional parameters


                    MainView.startActivity(myIntent);
                } else if (spinner.getSelectedItem().toString().equals("Close")) {
                    MainView.finish();
                } else if (spinner.getSelectedItem().toString().equals("Save")) {
                    DBInterface dbz = new DBInterface();
                    dbz.setBaseContext(MainView);
                    MainView.RowID = dbz.Save(MainView.RowID,MainView.ParentRowID, MainView.formName, MainView.mainRelativeLayout);
                } else if (spinner.getSelectedItem().toString().equals("Get")) {
                    DBInterface dbz = new DBInterface();
                    dbz.setBaseContext(MainView);
                    if (action.getText().toString().equals("F")) {
                        MainView.RowID = dbz.Get(MainView.RowID,MainView.ParentRowID, MainView.formName, MainView.mainRelativeLayout, DBInterface.Direction.First);
                    } else if (action.getText().toString().equals("L")) {
                        MainView.RowID = dbz.Get(MainView.RowID,MainView.ParentRowID, MainView.formName, MainView.mainRelativeLayout, DBInterface.Direction.Last);
                    } else if (action.getText().toString().equals("P")) {
                        MainView.RowID = dbz.Get(MainView.RowID,MainView.ParentRowID, MainView.formName, MainView.mainRelativeLayout, DBInterface.Direction.Prev);
                    } else if (action.getText().toString().equals("N")) {
                        MainView.RowID = dbz.Get(MainView.RowID,MainView.ParentRowID, MainView.formName, MainView.mainRelativeLayout, DBInterface.Direction.New);
                    } else {
                        MainView.RowID = dbz.Get(MainView.RowID,MainView.ParentRowID, MainView.formName, MainView.mainRelativeLayout, DBInterface.Direction.Next);
                    }
                }
            }
        });


        setClickable(false);
        setEnabled(false);
        setFocusable(false);
        setFocusableInTouchMode(false);
        Edit(edit);

    }
}
