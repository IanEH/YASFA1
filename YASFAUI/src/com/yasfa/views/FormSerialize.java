/*
 * TouchImageView.java
 * By: Edward Ian Hickman
 * Copyright (C) 2015 Edward Ian Hickman
 */

package com.yasfa.views;
import java.util.ArrayList;

import com.yasfa.views.DBInterface.Direction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;import android.widget.ScrollView;
import android.widget.HorizontalScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.view.Display;
import android.graphics.Point;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

@SuppressLint("NewApi")
public class FormSerialize  {
	static SQLiteDatabase DB = null;
    Context ctx;
	public void openDB() {
        try {
        	if (DB==null) {
                DB = ctx.openOrCreateDatabase("YASFA.db", ctx.MODE_PRIVATE, null);
                DB.execSQL("CREATE TABLE IF NOT EXISTS YASFAFormV1  (ID INTEGER PRIMARY KEY,Name VARCHAR,Type VARCHAR,LockCode VARCHAR,Rotation INTEGER)");
                DB.execSQL("CREATE TABLE IF NOT EXISTS YASFAObjectV1 (ID INTEGER PRIMARY KEY,FormID INTEGER,Name VARCHAR,Type VARCHAR, x INTEGER,y INTEGER,width INTEGER,height INTEGER,Picture BLOB)");
                DB.execSQL("CREATE TABLE IF NOT EXISTS YASFAControlV1 (ID INTEGER PRIMARY KEY,FormID INTEGER,ObjectID INTEGER,Name VARCHAR,Type VARCHAR, x INTEGER,y INTEGER,width INTEGER,height INTEGER)");
        	}
        } catch (Exception e) {
        	int i=0;
        }
        try { // rEMOVE THIS
            DB.execSQL("ALTER TABLE  YASFAFormV1 ADD COLUMN LockCode VARCHAR");
        } catch (Exception e1) {
            int i2=0;
        }
        try { // rEMOVE THIS
            DB.execSQL("ALTER TABLE  YASFAFormV1 ADD COLUMN Rotation INTEGER");
        } catch (Exception e1) {
            int i2=0;
        }
        try { // rEMOVE THIS
            DB.execSQL("UPDATE YASFAFormV1 SET Rotation = 0 WHERE Rotation is NULL");
        } catch (Exception e1) {
            int i2=0;
        }
        try { // rEMOVE THIS
            DB.execSQL("ALTER TABLE  YASFAObjectV1 ADD COLUMN Picture BLOB");
        } catch (Exception e1) {
            int i2=0;
        }

    }

	public void closeDB() { 
        try {
            DB.close();
            DB = null;
        } catch (Exception e) {
        }
 	}

    @SuppressLint("NewApi")
    public void SaveLock(String Name,String LockCode) {
        try {

            if (Name.equals("")) return;
            openDB();
            Cursor c = DB.rawQuery("SELECT * FROM YASFAFormV1 WHERE Name like  '" + Name + "'", null);
            c.moveToFirst();
            int FormID = c.getInt(c.getColumnIndex("ID"));
            DB.execSQL("UPDATE YASFAFormV1 Set LockCode = '" + LockCode + "' WHERE ID = '" + FormID + "'");
            closeDB();
        } catch (Exception ex) {
        }
    }

    public int getRotation() {

        Display display = ((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Integer rotation = display.getRotation();
        switch(rotation) {
            case Surface.ROTATION_0:
                rotation = 0;
                break;
            case Surface.ROTATION_90:
                rotation = 1;
                break;
            case Surface.ROTATION_180:
                rotation = 0;
                break;
            case Surface.ROTATION_270:
                rotation = 1;
                break;
            default:
                rotation = 0;
                break;
        }
        return rotation;
    }

    @SuppressLint("NewApi")
    public void SyncFlip(String Name) {
        if (Name.equals("")) return;
        openDB();
        Integer rotation = getRotation();
        Integer hrotation = 0;
        if (rotation == 0) hrotation=1;

        Cursor c = DB.rawQuery("SELECT * FROM YASFAFormV1 WHERE Name like  '" + Name + "' AND Rotation ='" + rotation + "'", null);
        if (!c.moveToFirst()) {
            return;
        }
        Cursor c1 = DB.rawQuery("SELECT * FROM YASFAFormV1 WHERE Name like  '" + Name + "' AND Rotation ='" + hrotation + "'", null);
        if (!c1.moveToFirst()) {
            return;
        }
        int Form1ID = c.getInt(c.getColumnIndex("ID"));
        int Form2ID = c1.getInt(c1.getColumnIndex("ID"));
        Cursor c2 = DB.rawQuery("SELECT ID,FormID,Name,Type,x,y,width,height,Picture FROM YASFAObjectV1 WHERE FormID =  '"+Form1ID+"' AND Name not like ''" , null);
        if (c2.moveToFirst()) {
            do {
                String nName = c2.getString(c2.getColumnIndex("Name"));
                Cursor c3 = DB.rawQuery("SELECT ID,FormID,Name,Type,x,y,width,height,Picture FROM YASFAObjectV1 WHERE FormID =  '"+Form2ID+"' AND Name like '"+nName+"'" , null);
                String nType = c2.getString(c2.getColumnIndex("Type"));
                if (!c3.moveToFirst() && !nType.equals("PictureLayout")) {
                    Integer ObjectID = c2.getInt(c2.getColumnIndex("ID"));
                    Integer nLeft = c2.getInt(c2.getColumnIndex("x"));
                    Integer nTop = c2.getInt(c2.getColumnIndex("y"));
                    Integer nWidth = c2.getInt(c2.getColumnIndex("width"));
                    Integer nHeight = c2.getInt(c2.getColumnIndex("height"));
                    DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('" + Form2ID + "','"+nName+"','"+nType+"','" + nLeft + "','" + nTop + "','"+nWidth+"','"+nHeight+"')");

                    String query = "SELECT ID from YASFAObjectV1 order by ID DESC limit 1";
                    Cursor c5 = DB.rawQuery(query,null);
                    long NewObjectID = 0;
                    if (c5 != null && c5.moveToFirst()) {
                        NewObjectID = c5.getLong(0);
                    }

                    Cursor c4 = DB.rawQuery("SELECT ID,FormID,ObjectID,Name,Type,x,y,width,height FROM YASFAControlV1 WHERE ObjectID =  '" + ObjectID + "'", null);
                    if (c4.moveToFirst()) {
                        do {
                            String nnName = c4.getString(c4.getColumnIndex("Name"));
                            String nnType = c4.getString(c4.getColumnIndex("Type"));
                            Integer nnLeft = c4.getInt(c4.getColumnIndex("x"));
                            Integer nnTop = c4.getInt(c4.getColumnIndex("y"));
                            Integer nnWidth = c4.getInt(c4.getColumnIndex("width"));
                            Integer nnHeight = c4.getInt(c4.getColumnIndex("height"));
                            DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+Form2ID+"','"+NewObjectID+"','"+nnName+"','"+nnType+"','"+nnLeft+"','"+nnTop+"','"+nnWidth+"','"+nnHeight+"')");
                        } while (c4.moveToNext());
                    }
                }
            } while (c2.moveToNext());
        }
        closeDB();
    }

    @SuppressLint("NewApi")
    public void SaveForm(String Name,RelativeLayout mainRelativeLayout,InflateView MainView,String LockCode) {
        try {

            if (Name.equals("")) return;
            openDB();
            Integer rotation = getRotation();

            Cursor c = DB.rawQuery("SELECT * FROM YASFAFormV1 WHERE Name like  '"+Name+"' AND Rotation ='"+rotation+"'" , null);
            if (!c.moveToFirst()) {
                DB.execSQL("INSERT INTO YASFAFormV1  (Name,Type,Rotation) VALUES ('"+Name+"','Form','"+rotation+"')");
                c = DB.rawQuery("SELECT * FROM YASFAFormV1 WHERE Name like  '"+Name+"' AND Rotation ='"+rotation+"'" , null);
                c.moveToFirst();
            }
            int FormID = c.getInt(c.getColumnIndex("ID"));
            DB.execSQL("UPDATE YASFAFormV1 Set LockCode = '"+LockCode+"' WHERE  Name like '"+Name+"'");

            DB.execSQL("DELETE FROM YASFAObjectV1 WHERE FormID = '"+FormID+"'");
               DB.execSQL("DELETE FROM YASFAControlV1 WHERE FormID = '"+FormID+"'");

			   
			   for(int i=0;i<mainRelativeLayout.getChildCount();i++){
	      	        View child=mainRelativeLayout.getChildAt(i);
                   if (child instanceof DrawLayout) {
                       DrawLayout.Draw list = ((DrawLayout)child).mdraw;
                       android.view.ViewGroup.LayoutParams params = list.getLayoutParams();
                       // Changes the height and width to the specified *pixels*
                       DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','"+((DrawLayout)child).Name+"','DrawLayout','" + child.getLeft() + "','" + child.getTop() + "','"+params.width+"','"+(params.height)+"')");
                   }
                   else if (child instanceof RecordPlay) {
                       RelativeLayout lbg = (RelativeLayout)((RecordPlay) child).getChildAt(0);
                       RecordPlay.VisualizerView lbg1 = (RecordPlay.VisualizerView)lbg.getChildAt(0);
                       android.view.ViewGroup.LayoutParams params = lbg1.getLayoutParams();
                       // Changes the height and width to the specified *pixels*
                       DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ((RecordPlay) child).Name + "','RecordPlay','" + child.getLeft() + "','" + child.getTop() + "','" + params.width + "','" + (params.height+36) + "')");
                   }
                   else if (child instanceof CameraLayout) {
                       RelativeLayout rl = (RelativeLayout)((CameraLayout)child).getChildAt(0);
                       ImageView list = (ImageView)rl.getChildAt(0);
                       android.view.ViewGroup.LayoutParams params = list.getLayoutParams();
                       // Changes the height and width to the specified *pixels*
                       DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ((CameraLayout) child).Name + "','CameraLayout','" + child.getLeft() + "','" + child.getTop() + "','" + params.width + "','" + (params.height) + "')");
                   }
                   else if (child instanceof PictureLayout) {
                       Button list = (Button)((PictureLayout)child).getChildAt(0);
                       android.view.ViewGroup.LayoutParams params = list.getLayoutParams();
                       // Changes the height and width to the specified *pixels*
                       SQLiteStatement insertStmt = DB.compileStatement("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height,Picture) VALUES ('" + FormID + "','"+((PictureLayout)child).Name+"','PictureLayout','" + child.getLeft() + "','" + child.getTop() + "','"+params.width+"','"+(params.height)+"',?)");
                       insertStmt.clearBindings();
                       insertStmt.bindBlob(1, ((PictureLayout) child).GetByteValue());
                       insertStmt.executeInsert();
                   }
                   else if (child instanceof SliderLayout) {
                       View list = (View)((SliderLayout)child).getChildAt(1);
                       android.view.ViewGroup.LayoutParams params = list.getLayoutParams();
                       // Changes the height and width to the specified *pixels*
                       DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','"+((SliderLayout)child).Name+"','SliderLayout','" + child.getLeft() + "','" + child.getTop() + "','"+params.width+"','"+(params.height)+"')");
                       String query = "SELECT ID from YASFAObjectV1 order by ID DESC limit 1";
                       Cursor c1 = DB.rawQuery(query,null);
                       long ObjectID = 0;
                       if (c1 != null && c1.moveToFirst()) {
                           ObjectID = c1.getLong(0);
                       }

                       EditText label = (EditText) ((SliderLayout)child).getChildAt(0);
                       DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"','"+label.getText()+"','Label','"+label.getLeft()+"','"+label.getTop()+"','0','0')");
                   }
                   else if (child instanceof DataList) {
                       LinearLayout lbg = (LinearLayout) ((DataList) child).getChildAt(0);
                       LinearLayout lbg2 = (LinearLayout) lbg.getChildAt(0);
                       ListView list = (ListView) lbg2.getChildAt(0);
                       android.view.ViewGroup.LayoutParams params = list.getLayoutParams();
                       // Changes the height and width to the specified *pixels*
                       // Changes the height and width to the specified *pixels*
                       int offset = 50;
                       if (!MainView.ParentRow.equals("")) {
                           offset += 25;
                       }
                       DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','','DataList','" + child.getLeft() + "','" + child.getTop() + "','" + params.width + "','" + (params.height + offset) + "')");
                   } else if (child instanceof PictureList) {
                           LinearLayout lbg = (LinearLayout)((PictureList)child).getChildAt(0);
                           LinearLayout lbg2 = (LinearLayout)lbg.getChildAt(0);
                           android.view.ViewGroup.LayoutParams params = lbg2.getLayoutParams();
                           DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','','PictureList','" + child.getLeft() + "','" + child.getTop() + "','"+params.width+"','"+(params.height)+"')");
                       String query = "SELECT ID from YASFAObjectV1 order by ID DESC limit 1";
                       Cursor c1 = DB.rawQuery(query,null);
                       long ObjectID = 0;
                       if (c1 != null && c1.moveToFirst()) {
                           ObjectID = c1.getLong(0);
                       }
                       PictureList dp = (PictureList) child;
                       DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"','"+dp.edit1.getText()+"','EditText','0','0','0','0')");
                   } else if (child instanceof MMLayout) {
                           DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','','MMLayout','"+child.getLeft()+"','"+child.getTop()+"','0','0')");
                   } else if (child instanceof DataSpinner) {
                       DataSpinner dp = (DataSpinner) child;
                       android.view.ViewGroup.LayoutParams params = dp.control1.getLayoutParams();

                       DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+((DataSpinner)child).Name+"','DataSpinner','"+child.getLeft()+"','"+child.getTop()+ "','"+params.width+"','"+(params.height)+"')");
                       String query = "SELECT ID from YASFAObjectV1 order by ID DESC limit 1";
                       Cursor c1 = DB.rawQuery(query,null);
                       long ObjectID = 0;
                       if (c1 != null && c1.moveToFirst()) {
                           ObjectID = c1.getLong(0);
                       }

                       LinearLayout bits =(LinearLayout)((LinearLayout) child).getChildAt(0);
                       EditText label=(EditText)bits.getChildAt(0);

                       SQLiteStatement insertStmt = DB.compileStatement("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"',?,'Label','"+label.getLeft()+"','"+label.getTop()+"','0','0')");
                       insertStmt.clearBindings();
                       insertStmt.bindString(1, label.getText().toString());
                       insertStmt.executeInsert();

                       DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"','"+dp.edit1.getText()+"','EditText','0','0','0','0')");
                       DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"','"+dp.edit2.getText()+"','EditText','0','0','0','0')");
                       DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"','"+dp.edit3.getText()+"','EditText','0','0','0','0')");

                   } else if (child instanceof SpinnerLayout) {
                       LinearLayout bits =(LinearLayout)((LinearLayout) child).getChildAt(0);
                       EditText label=(EditText)bits.getChildAt(0);
                       Spinner spinner =(Spinner)bits.getChildAt(1);
                       android.view.ViewGroup.LayoutParams params = spinner.getLayoutParams();

                       DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+((SpinnerLayout)child).Name+"','SpinnerLayout','"+child.getLeft()+"','"+child.getTop()+ "','"+params.width+"','"+(params.height)+"')");
                       String query = "SELECT ID from YASFAObjectV1 order by ID DESC limit 1";
                       Cursor c1 = DB.rawQuery(query,null);
                       long ObjectID = 0;
                       if (c1 != null && c1.moveToFirst()) {
                           ObjectID = c1.getLong(0);
                       }

                       SQLiteStatement insertStmt = DB.compileStatement("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"',?,'Label','"+label.getLeft()+"','"+label.getTop()+"','0','0')");
                       insertStmt.clearBindings();
                       insertStmt.bindString(1, label.getText().toString());
                       insertStmt.executeInsert();

                       ArrayAdapter<String> spinnerArrayAdapter = (ArrayAdapter<String>) spinner.getAdapter();
                       for (int j=0;j<spinnerArrayAdapter.getCount();j++) {
                           DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"','"+spinnerArrayAdapter.getItem(j)+"','SpinnerValue','0','0','0','0')");
                       }
          		} else if (child instanceof RadioLayout) {
                    DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','"+((RadioLayout)child).Name+"','RadioLayout','" + child.getLeft() + "','" + child.getTop()+"','0','0')");
          			String query = "SELECT ID from YASFAObjectV1 order by ID DESC limit 1";
          			Cursor c1 = DB.rawQuery(query,null);
          			long ObjectID = 0;
          			if (c1 != null && c1.moveToFirst()) {
          				ObjectID = c1.getLong(0); 
          			}
  					RadioGroup RadioGroup=(RadioGroup)((LinearLayout) child).getChildAt(1);
          			for(int j=0;j<RadioGroup.getChildCount();j++){
          				View control=RadioGroup.getChildAt(j);
          				if (control instanceof RadioButton) {
          					DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"','"+((RadioButton)control).getText()+"','RadioButton','0','0','0','0')");
          				}
          			}
          		} else if (child instanceof DataLayout) {
  		            DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','','DataLayout','"+child.getLeft()+"','"+child.getTop()+"','0','0')");
          		} else if (child instanceof ButtonLayout) {
                     Button btn = (Button)((ButtonLayout)child).getChildAt(1);
                     android.view.ViewGroup.LayoutParams params = btn.getLayoutParams();

                     SQLiteStatement insertStmtm = DB.compileStatement("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height,Picture) VALUES ('" + FormID + "','','ButtonLayout','" + child.getLeft() + "','" + child.getTop() + "','"+params.width+"','"+(params.height)+"',?)");
                       insertStmtm.clearBindings();
                       insertStmtm.bindBlob(1, ((ButtonLayout) child).GetByteValue());
                       insertStmtm.executeInsert();


          			String query = "SELECT ID from YASFAObjectV1 order by ID DESC limit 1";
          			Cursor c1 = DB.rawQuery(query,null);
          			long ObjectID = 0;
          			if (c1 != null && c1.moveToFirst()) {
          				ObjectID = c1.getLong(0); 
          			}
  		            EditText label=(EditText)((ButtonLayout) child).label;

                       SQLiteStatement insertStmt = DB.compileStatement("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"',?,'Spinner','"+label.getLeft()+"','"+label.getTop()+"','0','0')");
                       insertStmt.clearBindings();
                       insertStmt.bindString(1, label.getText().toString());
                       insertStmt.executeInsert();

          			Button button=(Button)((ButtonLayout) child).button;
                       SQLiteStatement insertStmt1 = DB.compileStatement("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"',?,'Button','" + button.getLeft() + "','" + button.getTop()+"','0','0')");
                       insertStmt1.clearBindings();
                       insertStmt1.bindString(1, button.getText().toString());
                       insertStmt1.executeInsert();

  		            Spinner spinner=(Spinner)((ButtonLayout) child).spinner;
  		            String val="";
  		            if (spinner.getSelectedItem() !=null)val=spinner.getSelectedItem().toString().replace("'", "''");
  		            DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"','"+val+"','Spinner','"+spinner.getLeft()+"','"+spinner.getTop()+"','0','0')");

  		            EditText extra=(EditText)((ButtonLayout) child).action;
  		            DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('"+FormID+"','"+ObjectID+"','"+extra.getText().toString().replace("'", "")+"','Spinner','"+extra.getLeft()+"','"+extra.getTop()+"','0','0')");
                   } else if (child instanceof SayLayout) {
                       try {
                           View ctl;
                           android.view.ViewGroup.LayoutParams params;
                           if (((SayLayout) child).getChildAt(0)==null) {
                               params = new android.view.ViewGroup.LayoutParams(0, 0);
                           } else {
                               ctl = (View) ((SayLayout) child).getChildAt(0);
                               params = ctl.getLayoutParams();
                           }

                           if (((SayLayout) child).getChildCount() < 2) {
                               ((SayLayout)child).Name="null";
                           }

                           DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ((SayLayout) child).Name + "','SayLayout','" + child.getLeft() + "','" + child.getTop() + "','" + params.width + "','" + (params.height) + "')");
                           String query = "SELECT ID from YASFAObjectV1 order by ID DESC limit 1";
                           Cursor c1 = DB.rawQuery(query, null);
                           long ObjectID = 0;
                           if (c1 != null && c1.moveToFirst()) {
                               ObjectID = c1.getLong(0);
                           }
                           EditText label = (EditText) ((LinearLayout) child).getChildAt(0);

                           SQLiteStatement insertStmt = DB.compileStatement("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "',?,'Label','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                           insertStmt.clearBindings();
                           insertStmt.bindString(1, label.getText().toString());
                           insertStmt.executeInsert();

                       } catch (Exception ex) {}
                   }

                else if (child instanceof ControlLayout) {
                       try {
                           View ctl;
                           android.view.ViewGroup.LayoutParams params;
                           if (((ControlLayout) child).getChildAt(0)==null) {
                               params = new android.view.ViewGroup.LayoutParams(0, 0);
                           } else if (((ControlLayout) child).getChildAt(1)==null) {
                               ctl = (View) ((ControlLayout) child).getChildAt(0);
                               params = ctl.getLayoutParams();
                           } else{
                               ctl = (View) ((ControlLayout) child).getChildAt(1);
                               params = ctl.getLayoutParams();
                           }

                           if (((ControlLayout) child).getChildCount() < 2) {
                               ((ControlLayout)child).Name="null";
                           } else {
                           }

                           DB.execSQL("INSERT INTO YASFAObjectV1  (FormID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ((ControlLayout) child).Name + "','ControlLayout','" + child.getLeft() + "','" + child.getTop() + "','" + params.width + "','" + (params.height) + "')");
                           String query = "SELECT ID from YASFAObjectV1 order by ID DESC limit 1";
                           Cursor c1 = DB.rawQuery(query, null);
                           long ObjectID = 0;
                           if (c1 != null && c1.moveToFirst()) {
                               ObjectID = c1.getLong(0);
                           }
                           EditText label = (EditText) ((LinearLayout) child).getChildAt(0);

                           SQLiteStatement insertStmt = DB.compileStatement("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "',?,'Label','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                           insertStmt.clearBindings();
                           insertStmt.bindString(1, label.getText().toString());
                           insertStmt.executeInsert();


                           for (int j = 1; j < ((LinearLayout) child).getChildCount(); j++) {
                               View control = (View) ((LinearLayout) child).getChildAt(j);
                               if (control instanceof NumberPicker) {
                                   DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultIntValue() + "','NumberPicker','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                               } else if (control instanceof EditText) {
                                   DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultValue() + "','EditText','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                               } else if (control instanceof CheckBox) {
                                   DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultValue() + "','CheckBox','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                               } else if (control instanceof ToggleButton) {
                                   DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultValue() + "','ToggleButton','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                               } else if (control instanceof DateButton) {
                                   DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultValue() + "','DateButton','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                               } else if (control instanceof Button) {
                                   DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultValue() + "','Button','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                               } else if (control instanceof LinearLayout) {
                                   DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultIntValue() + "','LinearLayout','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                                   for (int j1 = 0; j1 < ((LinearLayout) control).getChildCount(); j1++) {
                                       View control1 = (View) ((LinearLayout) control).getChildAt(j1);
                                       if (control1 instanceof NumberPicker) {
                                           DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultIntValue() + "','NumberPicker','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                                       } else if (control1 instanceof EditText) {
                                           DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultValue() + "','EditText','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                                       } else if (control1 instanceof CheckBox) {
                                           DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultValue() + "','CheckBox','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                                       } else if (control1 instanceof ToggleButton) {
                                           DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultValue() + "','ToggleButton','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                                       } else if (control1 instanceof DateButton) {
                                           DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultValue() + "','DateButton','...','" + label.getTop() + "','0','0')");
                                       } else if (control1 instanceof TimeButton) {
                                           DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultValue() + "','TimeButton','...','" + label.getTop() + "','0','0')");
                                       } else if (control1 instanceof Button) {
                                           DB.execSQL("INSERT INTO YASFAControlV1  (FormID,ObjectID,Name,Type,x,y,width,height) VALUES ('" + FormID + "','" + ObjectID + "','" + ((ControlLayout) child).getDefaultValue() + "','Button','" + label.getLeft() + "','" + label.getTop() + "','0','0')");
                                       }
                                   }
                               }
                           }
                       } catch (Exception ex) {}
                   }
               }
			closeDB();
		    } catch (Exception e) {
		    	int i=0;
	        }
	 	}

    @SuppressLint("NewApi")
    public RelativeLayout LoadForm(final String Name,final RelativeLayout mainRelativeLayout,final InflateView MainView) {
        try {
            if (Name.equals("")) return mainRelativeLayout;
            openDB();
            Integer rotation = getRotation();

            Cursor c = DB.rawQuery("SELECT * FROM YASFAFormV1 WHERE Name like  '"+Name+"' AND Rotation = '"+rotation+"'" , null);
            if (!c.moveToFirst()) {
                c = c = DB.rawQuery("SELECT * FROM YASFAFormV1 WHERE Name like  '"+Name+"' AND Rotation = '0'" , null);
            }

            if (!c.moveToFirst()) {
                return mainRelativeLayout;
            }
            int FormID = c.getInt(c.getColumnIndex("ID"));
            MainView.LockCode  = c.getString(c.getColumnIndex("LockCode"));


            Cursor c1 = DB.rawQuery("SELECT ID,FormID,Name,Type,x,y,width,height,Picture FROM YASFAObjectV1 WHERE FormID =  '"+FormID+"'" , null);

            if (c1.moveToFirst()) {
                do {
                    int ObjectID = c1.getInt(c.getColumnIndex("ID"));
                    if (c1.getString(c1.getColumnIndex("Type")).equals("DrawLayout")) {
                        DrawLayout dataList = new DrawLayout(MainView);
                        mainRelativeLayout.addView(dataList);
                        LayoutParams params = dataList.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        params.height = c1.getInt(c1.getColumnIndex("height"));
                        params.width = c1.getInt(c1.getColumnIndex("width"));
                        dataList.Name = 	c1.getString(c1.getColumnIndex("Name"));
                        dataList.SetSize(c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                        SetLayout(dataList, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("SliderLayout")) {
                        SliderLayout dataList = new SliderLayout(MainView);
                        mainRelativeLayout.addView(dataList);
                        LayoutParams params = dataList.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        params.height = c1.getInt(c1.getColumnIndex("height"));
                        params.width = c1.getInt(c1.getColumnIndex("width"));
                        dataList.Name = 	c1.getString(c1.getColumnIndex("Name"));
                        dataList.SetSize(c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                        SetLayout(dataList, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                        EditText label = (EditText) dataList.getChildAt(0);
                        Cursor c2 = DB.rawQuery("SELECT ID,FormID,ObjectID,Name,Type,x,y,width,height FROM YASFAControlV1 WHERE ObjectID =  '"+ObjectID+"'" , null);
                        if (c2.moveToFirst()) {
                            EditText TextField = null;
                            do {
                                if (c2.getString(c2.getColumnIndex("Type")).equals("Label")) {
                                    label.setText(c2.getString(c2.getColumnIndex("Name")));
                                }
                            } while (c2.moveToNext());
                        }
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("RecordPlay")) {
                        RecordPlay dataList = new RecordPlay(MainView);
                        mainRelativeLayout.addView(dataList);
                        LayoutParams params = dataList.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        params.height = c1.getInt(c1.getColumnIndex("height"));
                        params.width = c1.getInt(c1.getColumnIndex("width"));
                        dataList.Name = 	c1.getString(c1.getColumnIndex("Name"));
                        dataList.SetSize(c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                        SetLayout(dataList, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("CameraLayout")) {
                        CameraLayout dataList = new CameraLayout(MainView);
                        mainRelativeLayout.addView(dataList);
                        LayoutParams params = dataList.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        params.height = c1.getInt(c1.getColumnIndex("height"));
                        params.width = c1.getInt(c1.getColumnIndex("width"));
                        dataList.Name = 	c1.getString(c1.getColumnIndex("Name"));
                        dataList.SetSize(c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                        SetLayout(dataList, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("PictureLayout")) {
                        PictureLayout dataList = new PictureLayout(MainView,false);
                        mainRelativeLayout.addView(dataList);
                        LayoutParams params = dataList.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        params.height = c1.getInt(c1.getColumnIndex("height"));
                        params.width = c1.getInt(c1.getColumnIndex("width"));
                        dataList.Name = 	c1.getString(c1.getColumnIndex("Name"));
                        dataList.SetSize(c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                        dataList.SetValue(c1.getBlob(c1.getColumnIndex("Picture")));
                        SetLayout(dataList, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("DataList")) {
                        DataList dataList= new DataList(Name,MainView,mainRelativeLayout);
                        mainRelativeLayout.addView(dataList);
                        LayoutParams params = dataList.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        params.height = c1.getInt(c1.getColumnIndex("height"));
                        params.width = c1.getInt(c1.getColumnIndex("width"));
                        dataList.SetSize(c1.getInt(c1.getColumnIndex("width")),c1.getInt(c1.getColumnIndex("height")));
                        SetLayout(dataList, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), c1.getInt(c1.getColumnIndex("width")),c1.getInt(c1.getColumnIndex("height")));
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("PictureList")) {
                        PictureList pictureList= new PictureList(Name,MainView,mainRelativeLayout,false);
                        mainRelativeLayout.addView(pictureList);
                        LayoutParams params = pictureList.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        params.height = c1.getInt(c1.getColumnIndex("height"));
                        params.width = c1.getInt(c1.getColumnIndex("width"));
                        pictureList.SetSize(c1.getInt(c1.getColumnIndex("width")),c1.getInt(c1.getColumnIndex("height")));
                        SetLayout(pictureList, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), c1.getInt(c1.getColumnIndex("width")),c1.getInt(c1.getColumnIndex("height")));
                        int ic=0;
                        Cursor c2 = DB.rawQuery("SELECT ID,FormID,ObjectID,Name,Type,x,y,width,height FROM YASFAControlV1 WHERE ObjectID =  '" + ObjectID + "'", null);
                        if (c2.moveToFirst()) {
                            EditText TextField = null;
                            do {
                                if (c2.getString(c2.getColumnIndex("Type")).equals("EditText")) {
                                    if (ic == 0) {
                                        pictureList.edit1.setText(c2.getString(c2.getColumnIndex("Name")));
                                    }

                                    ++ic;
                                }
                            } while (c2.moveToNext());
                        }
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("MMLayout")) {
                        MainView.menux=c1.getInt(c1.getColumnIndex("x"));MainView.menuy=c1.getInt(c1.getColumnIndex("y"));
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("DataLayout")) {
                        final DataLayout layout = new DataLayout(Name,MainView,mainRelativeLayout);
                        mainRelativeLayout.addView(layout);
                        SetLayout(layout, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), 400, 0);

                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("DataSpinner")) {
                        try {
                            DataSpinner ds = new DataSpinner(Name, MainView, mainRelativeLayout,false);
                            LayoutParams params = ds.getLayoutParams();
                            // Changes the height and width to the specified *pixels*
                            params.height = c1.getInt(c1.getColumnIndex("height"));
                            params.width = c1.getInt(c1.getColumnIndex("width"));
                            ds.Name = 	c1.getString(c1.getColumnIndex("Name"));
                            //ds.SetSize(c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));

                            SetLayout(ds, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), 400, 0);
                            mainRelativeLayout.addView(ds);
                            int ic=0;
                            Cursor c2 = DB.rawQuery("SELECT ID,FormID,ObjectID,Name,Type,x,y,width,height FROM YASFAControlV1 WHERE ObjectID =  '" + ObjectID + "'", null);
                            if (c2.moveToFirst()) {
                                EditText TextField = null;
                                do {
                                    if (c2.getString(c2.getColumnIndex("Type")).equals("Label")) {
                                        ds.label.setText(c2.getString(c2.getColumnIndex("Name")));
                                    } else if (c2.getString(c2.getColumnIndex("Type")).equals("EditText")) {
                                        if (ic == 0) {
                                            ds.edit1.setText(c2.getString(c2.getColumnIndex("Name")));
                                        }
                                        if (ic == 1) {
                                            ds.edit2.setText(c2.getString(c2.getColumnIndex("Name")));
                                        }
                                        if (ic == 2) {
                                            ds.edit3.setText(c2.getString(c2.getColumnIndex("Name")));}
                                        ++ic;
                                    }
                                } while (c2.moveToNext());
                            }
                            ds.Refresh(0);
                            openDB(); // cHEAT!!

                            if (c1.getInt(c1.getColumnIndex("width"))>20)
                                ds.SetSize(c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));

                            ds.Edit(false);
                        } catch (Exception ex) {
                            int i=1;

                        }
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("SpinnerLayout")) {
                        SpinnerLayout ll = new SpinnerLayout(MainView);
                        ll.setOrientation(LinearLayout.VERTICAL);

                        ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                        ll.Name = 	c1.getString(c1.getColumnIndex("Name"));
                        SetLayout(ll, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), 0, 0);

                        ll.setOrientation(LinearLayout.VERTICAL);

                        final LinearLayout bits = new LinearLayout(MainView);
                        bits.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                        bits.setOrientation(LinearLayout.VERTICAL);

                        LinearLayout test = new LinearLayout(MainView);
                        test.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

                        EditText label = new YEditText(MainView,false);
                        Spinner control = new Spinner(MainView);

                        test.setOrientation(LinearLayout.VERTICAL);
                        test.addView(label);
                        test.addView(control);

                        ll.addView(test);
                        ll.addView(bits);


                        Button b1 = new Button(MainView);
                        b1.setText("Add");
                        b1.setVisibility(View.VISIBLE);
                        bits.addView(b1);
                        bits.setVisibility(View.GONE);

                        b1.setClickable(false);
                        b1.setEnabled(false);
                        b1.setFocusable(false);
                        b1.setFocusableInTouchMode(false);

                        b1.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                EditText label = new EditText(MainView);
                                label.setText("");
                                bits.addView(label);
                            }
                        });

                        ArrayList<String> spinnerArray = new ArrayList<String>();
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainView, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        control.setClickable(true);
                        control.setEnabled(true);
                        control.setFocusable(false);
                        control.setFocusableInTouchMode(false);

                        ll.setBackgroundColor(Color.TRANSPARENT);
                        ll.setClickable(false);
                        ll.setEnabled(false);
                        ll.setFocusable(false);
                        ll.setFocusableInTouchMode(false);

                        mainRelativeLayout.addView(ll);

                        Cursor c2 = DB.rawQuery("SELECT ID,FormID,ObjectID,Name,Type,x,y,width,height FROM YASFAControlV1 WHERE ObjectID =  '"+ObjectID+"'" , null);
                        if (c2.moveToFirst()) {
                            EditText TextField = null;
                            do {
                                if (c2.getString(c2.getColumnIndex("Type")).equals("Label")) {
                                    label.setText(c2.getString(c2.getColumnIndex("Name")));
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("SpinnerValue")) {
                                    spinnerArray.add(c2.getString(c2.getColumnIndex("Name")));

                                }
                            } while(c2.moveToNext());
                        }
                        control.setAdapter(spinnerArrayAdapter);
                        if (c1.getInt(c1.getColumnIndex("width"))>0 && c1.getInt(c1.getColumnIndex("height"))>0)
                        ll.SetSize(c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("RadioLayout")) {
                        RadioLayout ll= new RadioLayout(MainView);
                        mainRelativeLayout.addView(ll);
                        ll.Name = 	c1.getString(c1.getColumnIndex("Name"));
                        SetLayout(ll, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), 0, 0);
                        Button b1 = new Button(MainView);
                        b1.setText("Add");
                        b1.setVisibility(View.GONE);
                        b1.setClickable(false);
                        b1.setEnabled(false);
                        b1.setFocusable(false);
                        b1.setFocusableInTouchMode(false);
                        ll.addView(b1);


                        final RadioGroup radioGroup=new RadioGroup(MainView);
                        radioGroup.setEnabled(true);
                        ll.addView(radioGroup);
                        b1.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                EditText label = new EditText(MainView);
                                radioGroup.addView(label);
                                label.setClickable(false);
                            }
                        });


                        Cursor c2 = DB.rawQuery("SELECT ID,FormID,ObjectID,Name,Type,x,y,width,height FROM YASFAControlV1 WHERE ObjectID =  '"+ObjectID+"'" , null);
                        if (c2.moveToFirst()) {
                            int q=0;
                            do {
                                if (c2.getString(c2.getColumnIndex("Type")).equals("RadioButton")) {
                                    RadioButton label = new RadioButton(MainView);
                                    label.setText(c2.getString(c2.getColumnIndex("Name")));
                                    label.setTextColor(Color.WHITE);
                                    label.setClickable(true);
                                    label.setEnabled(true);
                                    label.setFocusable(false);
                                    label.setFocusableInTouchMode(false);
                                    label.setId(q++);
                                    radioGroup.addView(label);
                                }
                            } while(c2.moveToNext());
                        }
                        radioGroup.setEnabled(true);
                        radioGroup.setSaveEnabled(true);
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("ButtonLayout")) {
                        try {
                            ButtonLayout bl = new ButtonLayout(MainView,false);
                            mainRelativeLayout.addView(bl);
                            SetLayout(bl, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), 400, 0);
                            Cursor c2 = DB.rawQuery("SELECT ID,FormID,ObjectID,Name,Type,x,y,width,height FROM YASFAControlV1 WHERE ObjectID =  '" + ObjectID + "'", null);
                            c2.moveToFirst();
                            try {
                                bl.label.setText(c2.getString(c2.getColumnIndex("Name")));
                            } catch (Exception ex1) {
                            }
                            c2.moveToNext();
                            try {
                                bl.button.setText(c2.getString(c2.getColumnIndex("Name")));
                            } catch (Exception ex1) {
                            }
                            try {
                                byte [] bts = c1.getBlob(c1.getColumnIndex("Picture"));
                                if (bts.length>1) {
                                    bl.SetValue(bts);
                                }
                            } catch (Exception ex1) {
                            }
                            if (c1.getInt(c1.getColumnIndex("width"))>20 &&  c1.getInt(c1.getColumnIndex("height"))>20)
                                bl.SetSize(c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                            c2.moveToNext();
                            try {
                                if (!c2.getString(c2.getColumnIndex("Name")).equals(null)) {

                                    int spinnerPostion = ((ArrayAdapter<String>)bl.spinner.getAdapter()).getPosition(c2.getString(c2.getColumnIndex("Name")));
                                    bl.spinner.setSelection(spinnerPostion);
                                    spinnerPostion = 0;
                                }
                            } catch (Exception ex1) {
                            }
                            c2.moveToNext();
                            bl.action.setVisibility(View.GONE);
                            try {
                                bl.action.setText(c2.getString(c2.getColumnIndex("Name")));
                            } catch (Exception ex1) {
                            }
                        } catch (Exception ex1) {}

                    }
                    else if (c1.getString(c1.getColumnIndex("Type")).equals("SayLayout")) {
                        SayLayout cl = new SayLayout(MainView);
                        cl.setBaselineAligned(false);
                        cl.setOrientation(LinearLayout.VERTICAL);
                        cl.setBackgroundColor(Color.TRANSPARENT);
                        cl.Name = c1.getString(c1.getColumnIndex("Name"));
                        mainRelativeLayout.addView(cl);
                        SetLayout(cl, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), 0, 0);

                        Cursor c2 = DB.rawQuery("SELECT ID,FormID,ObjectID,Name,Type,x,y,width,height FROM YASFAControlV1 WHERE ObjectID =  '"+ObjectID+"'" , null);

                        if (c2.moveToFirst()) {
                            cl.label.setText(c2.getString(c2.getColumnIndex("Name")));
                        }
                        cl.Edit(false);
                        cl.SetSize(c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));
                    }
                    else if (c1.getString(c1.getColumnIndex("Type")).equals("ControlLayout")) {
                        ControlLayout cl= new ControlLayout(MainView);
                        cl.setBaselineAligned(false);
                        cl.setOrientation(LinearLayout.VERTICAL);
                        cl.setBackgroundColor(Color.TRANSPARENT);
                        cl.Name = c1.getString(c1.getColumnIndex("Name"));


                        mainRelativeLayout.addView(cl);
                        SetLayout(cl, RelativeLayout.ALIGN_PARENT_LEFT, c1.getInt(c1.getColumnIndex("x")), c1.getInt(c1.getColumnIndex("y")), 0, 0);
                        LinearLayout ll2 = (LinearLayout)cl;
                        LinearLayout ll=ll2;
                        Cursor c2 = DB.rawQuery("SELECT ID,FormID,ObjectID,Name,Type,x,y,width,height FROM YASFAControlV1 WHERE ObjectID =  '"+ObjectID+"'" , null);

                        EditText nnlabel=null;
                        if (c2.moveToFirst()) {
                            EditText TextField = null;
                            do {
                                if (c2.getString(c2.getColumnIndex("Type")).equals("LinearLayout")) {
                                    LinearLayout ll1=new LinearLayout(MainView);
                                    ll1.setBaselineAligned(false);
                                    ll.addView(ll1);
                                    ll = ll1;
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("Label")) {
                                    EditText label = new YEditText(MainView,false);
                                    label.setText(c2.getString(c2.getColumnIndex("Name")));
                                    ll.addView(label);
                                    nnlabel=label;
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("EditText")) {
                                    EditText label = new EditText(MainView);
                                    label.setText(c2.getString(c2.getColumnIndex("Name")));
                                    label.setClickable(true);
                                    label.setEnabled(true);
                                    label.setTextSize(14);
                                    label.setGravity(label.getGravity() | Gravity.TOP);
                                    label.setFocusable(true);
                                    label.setFocusableInTouchMode(true);
                                    TextField = label;
                                    ll.addView(label);
                                }

                                else if (c2.getString(c2.getColumnIndex("Type")).equals("CheckBox")) {
                                    CheckBox label = new CheckBox(MainView);
                                    label.setText(c2.getString(c2.getColumnIndex("Name")));
                                    label.setClickable(true);
                                    label.setPadding(1,1,1,1);
                                    // ll.setOrientation(LinearLayout.HORIZONTAL);
                                    label.setEnabled(true);
                                    label.setFocusable(false);
                                    label.setFocusableInTouchMode(false);
                                    ll.addView(label);
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("ToggleButton")) {
                                    ToggleButton label = new ToggleButton(MainView);
                                    label.setText(c2.getString(c2.getColumnIndex("Name")));
                                    label.setClickable(true);
                                    label.setEnabled(true);
                                    label.setFocusable(false);
                                    label.setFocusableInTouchMode(false);
                                    ll.addView(label);
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("Button")) {
                                    Button label = new Button(MainView);
                                    label.setText(c2.getString(c2.getColumnIndex("Name")));
                                    label.setClickable(true);
                                    label.setEnabled(true);
                                    label.setFocusable(false);
                                    label.setFocusableInTouchMode(false);
                                    ll.addView(label);
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("DateButton")) {
                                    DateButton label = new DateButton(MainView, TextField);
                                    label.setText("...");

                                    label.setClickable(true);
                                    label.setEnabled(true);
                                    label.setFocusable(false);
                                    label.setFocusableInTouchMode(false);
                                    ll.addView(label);
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("TimeButton")) {
                                    TimeButton label = new TimeButton(MainView,TextField);
                                    label.setText("...");

                                    label.setClickable(true);
                                    label.setEnabled(true);
                                    label.setFocusable(false);
                                    label.setFocusableInTouchMode(false);
                                    ll.addView(label);
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("NumberPicker")) {

                                    NumberPicker label = new CustomNumberPicker(MainView);


                                    label.setScaleX(.9f);
                                    label.setScaleY(.9f);
                                    label.setPadding(0, 0, 0, 0);
                                    LayoutParams params = new LayoutParams(70,140);
                                    params.resolveLayoutDirection(LayoutDirection.LTR);
                                    label.setLayoutParams(params);
                                    label.setGravity(Gravity.TOP);
                                    label.setValue(Integer.parseInt(c2.getString(c2.getColumnIndex("Name"))));
                                    label.setMaxValue(10000000);
                                    label.setMinValue(0);
                                    label.setClickable(true);
                                    label.setEnabled(true);
                                    label.setFocusable(false);
                                    label.setFocusableInTouchMode(false);
                                    ll.addView(label);
                                }
                            } while(c2.moveToNext());
                            if (c1.getInt(c1.getColumnIndex("width"))>20 &&  c1.getInt(c1.getColumnIndex("height"))>20)
                                cl.SetSize(c1.getInt(c1.getColumnIndex("width")), c1.getInt(c1.getColumnIndex("height")));

                            if (nnlabel!=null) {
                                RelativeLayout.LayoutParams LayoutParameters = (RelativeLayout.LayoutParams) ll2.getLayoutParams();
                                if (nnlabel.getText().toString().trim().equals("") && LayoutParameters.topMargin < 1) {
                                    //setOrientation(LinearLayout.HORIZONTAL);
                                    nnlabel.setVisibility(View.GONE);
                                } else {
                                    //setOrientation(LinearLayout.VERTICAL);
                                    nnlabel.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                } while(c1.moveToNext());
            }

            closeDB();
        } catch (Exception e) {
            int i=0;
        }
        return mainRelativeLayout;
    }

    private void SetLayout(View button, int centerInParent, int marginLeft, int marginTop, int marginRight, int marginBottom) {

        RelativeLayout.LayoutParams buttonLayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

	    // Add Margin to the LayoutParameters
	    buttonLayoutParameters.setMargins(marginLeft, marginTop, marginRight, marginBottom);

	    // Add Rule to Layout
	    buttonLayoutParameters.addRule(centerInParent);

	    // Setting the parameters on the Button
	    button.setLayoutParams(buttonLayoutParameters);     
	}

	private void SetLayout(View button, int centerInParent) {
	    // Just call the other SetLayout Method with Margin 0
	    SetLayout(button, centerInParent, 0 ,0 ,0 ,0);
	}

	public void setBaseContext(Context baseContext) {
		// TODO Auto-generated method stub
		ctx = baseContext;
	}
    @SuppressLint("NewApi")
    public class Fields {
        String fieldList = "";
        String image = "";
        String image1 = "";
        String sound1 = "";
    }

    public Fields FieldList(final String Name,boolean shortlist) {
        Fields retval = new Fields();
        String fieldList = "";
        try {

            if (Name.equals("")) return retval;
            openDB();

            Cursor c = DB.rawQuery("SELECT * FROM YASFAFormV1 WHERE Name like  '"+Name+"'" , null);
            if (!c.moveToFirst()) {
                return retval;
            }
            int FormID = c.getInt(c.getColumnIndex("ID"));

            Cursor c1 = DB.rawQuery("SELECT ID,FormID,Name,Type,x,y,width,height FROM YASFAObjectV1 WHERE FormID =  '"+FormID+"' ORDER BY ID" , null);
            if (c1.moveToFirst()) {
                do {
                    int ObjectID = c1.getInt(c.getColumnIndex("ID"));
                    if (c1.getString(c1.getColumnIndex("Type")).equals("SpinnerLayout")) {
                        fieldList = fieldList + c1.getString(c1.getColumnIndex("Name")) + "||','||";
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("CameraLayout")) {
                        if (retval.image.equals("")) {
                            retval.image = "," + c1.getString(c1.getColumnIndex("Name")) + " image";
                        } else if (retval.image1.equals("")) {
                            retval.image1 = "," + c1.getString(c1.getColumnIndex("Name")) + " image";
                        }
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("RecordPlay")) {
                        if (retval.sound1.equals("")) {
                            retval.sound1 = "," + c1.getString(c1.getColumnIndex("Name")) + " sound";
                        }
                    } else if (c1.getString(c1.getColumnIndex("Type")).equals("DrawLayout")) {
                            if (retval.image.equals("")) {
                                retval.image = "," + c1.getString(c1.getColumnIndex("Name")) + " image";
                            } else if (retval.image1.equals("")) {
                                retval.image1 = "," + c1.getString(c1.getColumnIndex("Name")) + " image";
                            }
                    }
                    else if (c1.getString(c1.getColumnIndex("Type")).equals("ControlLayout")) {
                        Cursor c2 = DB.rawQuery("SELECT ID,FormID,ObjectID,Name,Type,x,y,width,height FROM YASFAControlV1 WHERE ObjectID =  '"+ObjectID+"'" , null);
                        if (c2.moveToFirst()) {
                            EditText TextField = null;
                            do {
                                if (c2.getString(c2.getColumnIndex("Type")).equals("LinearLayout")) {
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("Label")) {
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("SayIt")) {
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("EditText")) {
                                    fieldList = fieldList + c1.getString(c1.getColumnIndex("Name")) + "||','||";
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("CheckBox")) {
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("ToggleButton")) {
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("Button")) {
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("DateButton")) {
                                    fieldList = fieldList + c1.getString(c1.getColumnIndex("Name")) + "||','||";
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("TimeButton")) {
                                    fieldList = fieldList + c1.getString(c1.getColumnIndex("Name")) + "||','||";
                                }
                                else if (c2.getString(c2.getColumnIndex("Type")).equals("NumberPicker")) {
                                    fieldList = fieldList + c1.getString(c1.getColumnIndex("Name")) + "||','||";
                                }
                            } while(c2.moveToNext());
                        }
                    }
                } while(c1.moveToNext());
            }

          closeDB();
        } catch (Exception e) {
            int i=0;
        }
        if (fieldList.length()>7)
            fieldList = fieldList.substring(0, fieldList.length()-7);
        retval.fieldList = fieldList;
        return retval;
    }

}
