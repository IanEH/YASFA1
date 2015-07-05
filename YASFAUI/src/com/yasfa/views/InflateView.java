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
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.yasfa.views.DBInterface.Direction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.HorizontalScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import static java.lang.Thread.sleep;


@SuppressLint("NewApi")
public class InflateView extends Activity {
    boolean Editing = false;
    boolean editMoveOnly = false;

    public static TextToSpeech mTts;


    Spinner Sp1;
	InflateView MainView;
    FButton movebutton;
    FButton buttonEdit;
	Button buttonAdd;
	Button buttonDel;

    EditText  dummy;

    static int gridx=1;
    static int gridy=1;
    float curX, curY;

    static boolean speak=false;
    static boolean holdspeak=false;

    public String formName = "";
    public Long RowID=-1L;
    public String ParentformName = "";
    public Long ParentRowID=-1L;
    public String ParentRow="";
    public String LockCode="";
    public String mLanguageCode="";


    static String lasttext="";
    public void Say(String text) {
        try {
            if (speak) {
                if (!text.equals(lasttext) && !text.equals("")) {
                    mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    lasttext = text;
                }
            }
        } catch (Exception ex) {}
    }


    class LScrollView extends ScrollView {

        public LScrollView(Context context) {
            super(context);

        }
        public boolean onInterceptTouchEvent(MotionEvent ev)
        {
            for(int i=0;i<mainRelativeLayout.getChildCount();++i) {
                View child = mainRelativeLayout.getChildAt(i);
                if  (child instanceof DrawLayout) {
                    return false;
                }
            }
            super.onInterceptTouchEvent(ev);
            return false;

        }
        @Override
        public boolean onTouchEvent(MotionEvent ev)
        {
            return false;
        }


    }



    class LHorizontalScrollView extends HorizontalScrollView {

        public LHorizontalScrollView(Context context) {
            super(context);

        }
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev)
        {
            for(int i=0;i<mainRelativeLayout.getChildCount();++i) {
                View child = mainRelativeLayout.getChildAt(i);
                 if  (child instanceof DrawLayout) {
                    return false;
                }
            }
            super.onInterceptTouchEvent(ev);
            return false;

        }
        @Override
        public boolean onTouchEvent(MotionEvent ev)
        {
            return false;
        }


    }


    public class Scrollable extends LinearLayout {

        private float mx, my;
        private float fastX, fastY;
        private int n=0;

        public LHorizontalScrollView vScroll;
        public LScrollView hScroll;

        public Scrollable(Context context) {
            super(context);
            vScroll = new LHorizontalScrollView(context);
            hScroll = new LScrollView(context);
            vScroll.addView(hScroll);
            this.addView(vScroll);
        }



        @Override
        public boolean onTouchEvent(MotionEvent event) {


                switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (Editing) {
                        try { // Clear any focus
                            View editText = getWindow().getCurrentFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            editText.clearFocus();
                        } catch (Exception ex) {}
                    }
                    holdspeak=speak;
                    speak = false;
                    mx = event.getX();
                    my = event.getY();
                    fastX = 0;
                    fastY = 0;
                    n = 0;
                    break;
                case MotionEvent.ACTION_MOVE:
                    curX = event.getX();
                    curY = event.getY();
                    if (!Editing) { // Accelerate
                        if (mx - curX > 4 || mx - curX < -4) {
                            fastX = fastX + (mx - curX) / 4;
                            n = n + 2;
                        } else {
                            fastX = 0;
                            n = 0;
                        }
                        if (my - curY > 4 || my - curY < -4) {
                            fastY = fastX + (my - curY) / 4;
                            n = n + 2;
                        } else {
                            fastY = 0;
                            n = 0;
                        }
                        if (fastX > 3) fastX = 3; // Not to fast!
                        if (fastY > 3) fastY = 3;
                        if (fastX < -3) fastX = -3; // Not to fast!
                        if (fastY < -3) fastY = -3;
                    }
                    vScroll.scrollBy((int) ((mx - curX)+fastX), (int) ((my - curY)+fastY));
                    hScroll.scrollBy((int) ((mx - curX)+fastX), (int) ((my - curY)+fastY));
                    mx = curX;
                    my = curY;
                    break;
                case MotionEvent.ACTION_UP:

                    speak = holdspeak;
                    if (Editing) {
                        try { // Clear any focus
                            View editText = getWindow().getCurrentFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            editText.clearFocus();
                        } catch (Exception ex) {}
                    }
                    if (Editing) { // Move the menu!
                        Display display = ((WindowManager)MainView.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                        SetLayout(topv, RelativeLayout.ALIGN_PARENT_LEFT, scrollable.vScroll.getScrollX()+5, scrollable.hScroll.getScrollY()+5, 0, 0, display.getWidth()-20, 0);
                    }

                    curX = event.getX();
                    curY = event.getY();
                    while (n>0) {
                        vScroll.scrollBy((int) ((mx - curX) + fastX), (int) ((my - curY) + fastY));
                        hScroll.scrollBy((int) ((mx - curX) + fastX), (int) ((my - curY) + fastY));
                        --n;
                    }
                    fastX = 0;
                    fastY = 0;
                    break;
            }
            return true;
        }

    }

    Scrollable scrollable;

    View Selected;
    View DelSelected;
    View LastSelected;

	public InflateView() {
	}
	
	RelativeLayout mainv;
	MMLayout topv;

	RelativeLayout mainRelativeLayout;

    int xoffset;
    int yoffset;

    int x1offset;
    int y1offset;

	public int menux;
	public int menuy;

    static int editb = Color.argb(250,60, 155 , 90);
    static int editc = Color.argb(250,155 , 90, 255);
    static int editd = Color.argb(250,60 , 190, 255);
    static int edite = Color.argb(250,255, 90,90 );


    private KillReceiver mKillReceiver;

    float[] orientation;

    private Locale GetLanguageLocale(String Code) {

        if (Code==null || Code.equals("") || Code.equals("UK")) {
            return Locale.UK;
        }
        Locale[] locales = Locale.getAvailableLocales();
        List<Locale> localeList = new ArrayList<Locale>();
        for (Locale locale : locales) {
            if (locale.getLanguage().toUpperCase().equals(Code.toUpperCase())) {
                return locale;
            }
        }
        return Locale.UK;
    }

    /** Called when the activity is first created. */
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        // Creating a new RelativeLayout
    	try {
    	  Intent intent = getIntent();
    	  if (intent != null) {
              if (intent.getStringExtra("formName") != null) {
                  formName = intent.getStringExtra("formName");
              }
              else
                  formName = "";
              if (intent.getLongExtra("ParentRowID",-1L) != -1)
                  ParentRowID = intent.getLongExtra("ParentRowID",-1L);
              else
                  ParentRowID = -1L;
              if (intent.getStringExtra("ParentformName") != null)
                  ParentformName = intent.getStringExtra("ParentformName");
              else
                  ParentformName = "";
              if (intent.getStringExtra("ParentRow") != null)
                  ParentRow = intent.getStringExtra("ParentRow");
              else
                  ParentRow = "";
          }
    	} catch (Exception ex) {

    	}

        // Kill it!
        mKillReceiver = new KillReceiver();
        try {
            registerReceiver(mKillReceiver,
                    IntentFilter.create("kill", "text/plain"));
        } catch (Exception ex) {
            String sex=ex.getMessage();
        }
    	mainv = new RelativeLayout(this);
        topv = new MMLayout(this);
        mainv.setBackgroundColor(Color.BLACK);

        if (formName.equals("")) formName="Menu";
        if (formName.equals("Menu")) {
            Utils.defaltdb(this,false);
        }
        setTitle(formName);

        scrollable = new  Scrollable(this);

        mainRelativeLayout = new RelativeLayout(this);
        scrollable.hScroll.addView(mainRelativeLayout);

        // Creating a new Left Button with Margin
        buttonAdd = new Button(this);
        buttonAdd.setText("Add");

        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("SayIt");
        spinnerArray.add("Label");
        spinnerArray.add("EditText");
        spinnerArray.add("Button");
        spinnerArray.add("CheckBox");
        spinnerArray.add("RadioGroup");
        spinnerArray.add("ToggleButton");
        spinnerArray.add("DatePicker");
        spinnerArray.add("TimePicker");
        spinnerArray.add("NumberPicker");
        spinnerArray.add("Spinner");
        spinnerArray.add("Slider");
        spinnerArray.add("Camera");
        spinnerArray.add("Record");
        spinnerArray.add("Draw");
        spinnerArray.add("Picture");
        spinnerArray.add("Data");
        spinnerArray.add("DataSpinner");
        spinnerArray.add("DataList");
        spinnerArray.add("PictureList");

        Sp1 = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sp1.setAdapter(spinnerArrayAdapter);

        movebutton = new FButton(this,"Move Menu");
        movebutton.setBackgroundResource(R.drawable.a3);
        movebutton.setVisibility(View.GONE);

        // Creating a new Bottom Button
        buttonEdit = new FButton(this,"Edit Text / Move");
        buttonEdit.setBackgroundResource(R.drawable.a1);
        buttonEdit.setVisibility(View.GONE);

        buttonDel = new Button(this);
        buttonDel.setText("Del");


        Sp1.setVisibility(View.GONE);

        SetLayout(movebutton, RelativeLayout.ALIGN_PARENT_TOP,1,0,0,10,30,45);
        SetLayout(Sp1, RelativeLayout.ALIGN_PARENT_TOP,34,0,0,10,145,0);
        buttonAdd.setVisibility(View.GONE);
        SetLayout(buttonAdd, RelativeLayout.ALIGN_PARENT_TOP, 180, 0, 0, 10, 0, 0);
        buttonDel.setVisibility(View.GONE);
        SetLayout(buttonDel, RelativeLayout.ALIGN_PARENT_TOP, 224, 0, 0, 10, 0, 0);
        SetLayout(buttonEdit, RelativeLayout.ALIGN_PARENT_TOP,270,0,0,10,30,45);
        // Add the Buttons to the View
        topv.addView(movebutton);
        topv.addView(Sp1);
        topv.addView(buttonAdd);
        topv.addView(buttonDel);
        topv.addView(buttonEdit);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editMoveOnly) {  // More Here!!!
                    editMoveOnly = false;
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    MainView.buttonEdit.setBackgroundResource(R.drawable.a1);
                } else {
                    editMoveOnly = true;
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    MainView.buttonEdit.setBackgroundResource(R.drawable.a2);

                    try {
                        View editText = getWindow().getCurrentFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        editText.clearFocus();


                    } catch (Exception ex) {}
                }
            }
        });
        MainView=this;
        // Add a Layout to the Button with Margin
        buttonDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
	           	 if (DelSelected!=null) {
	           		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	           		    @Override
	           		    public void onClick(DialogInterface dialog, int which) {
	           		        switch (which){
	           		        case DialogInterface.BUTTON_POSITIVE:
	           	           		mainRelativeLayout.removeView(DelSelected);
	           	                DelSelected=null;
	           		            //Yes button clicked
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
            }
        });
        final Context context;
        context = this;

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ControlManage.make(MainView,mainRelativeLayout);
            }
        });


        mainRelativeLayout.addView(topv);
        mainv.addView(scrollable);

        RelativeLayout.LayoutParams relativeLayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        setContentView(mainv, relativeLayoutParameters);

    	menux=0;menuy=0;

        FormSerialize fz = new FormSerialize();
        fz.setBaseContext(getBaseContext());
        fz.LoadForm(formName,mainRelativeLayout,MainView);

    	SetLayout(topv, RelativeLayout.ALIGN_PARENT_LEFT, menux, menuy, 0, 0,0,0);

    	topv.bringToFront();
        DBInterface dbz = new DBInterface();
        dbz.setBaseContext(MainView);


            if (MainView.RowID != -1) {
                dbz.Save(MainView.RowID, MainView.ParentRowID, formName, mainRelativeLayout);
            }

            SharedPreferences YASFAState = context.getSharedPreferences("YASFAState", MODE_PRIVATE);
            if (YASFAState.getString("FORMNAME", "").equals("")) {
                MainView.RowID = dbz.Get(MainView.RowID, MainView.ParentRowID, formName, mainRelativeLayout, Direction.First);
            }
            if (YASFAState.getString("FORMNAME", "").equals(formName) && MainView.ParentRowID == YASFAState.getLong("PARENTROWID", -1L)) {
                MainView.RowID = YASFAState.getLong("ROWID", -1L);
                MainView.RowID = dbz.Get(MainView.RowID, MainView.ParentRowID, formName, mainRelativeLayout, Direction.This);
            } else {
                MainView.RowID = dbz.Get(MainView.RowID, MainView.ParentRowID, formName, mainRelativeLayout, Direction.First);
            }

        Refresh(MainView.ParentRowID);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Voice say setup
        if (mTts==null) {
            try {
                if (mLanguageCode == null) {
                    mLanguageCode = "UK";
                }
                final Locale ll = GetLanguageLocale(mLanguageCode);

                mTts = new TextToSpeech(getApplicationContext(),
                        new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status != TextToSpeech.ERROR) {
                                    //Locale.UK;
                                    mTts.setLanguage(ll);
                                    mTts.setPitch(1f);
                                    mTts.setSpeechRate(1.1f);
                                }
                            }
                        });
            } catch(Exception ex) {
                // Language system gone bad!
            }
        }

        dummy = new EditText(this);
        SetLayout(dummy, RelativeLayout.ALIGN_PARENT_TOP,1,0,0,0,-1,-1);
        mainRelativeLayout.addView(dummy);
        dummy.requestFocus();
    }

    @Override
    public void onPause() {
        if (mainRelativeLayout != null)
        for(int i=0;i<mainRelativeLayout.getChildCount();++i) {
            View child = mainRelativeLayout.getChildAt(i);
            if (child instanceof RecordPlay) {
                ((RecordPlay) child).killit();
            }
        }
        if (MainView.RowID!=-1) {
            DBInterface dbz = new DBInterface();
            dbz.setBaseContext(MainView);
            dbz.Save(MainView.RowID, MainView.ParentRowID, formName, mainRelativeLayout);
        }

        /*if(mTts !=null){
            mTts.stop();
            mTts.shutdown();
        }*/
        SharedPreferences YASFAState = MainView.getSharedPreferences("YASFAState", MODE_PRIVATE);
           if (YASFAState.getLong("ROWID",-1L) != -99L) {
               SharedPreferences.Editor ed = YASFAState.edit();
               ed.clear();
               ed.putLong("ROWID", MainView.RowID);
               ed.putLong("PARENTROWID", MainView.ParentRowID);
               ed.putString("FORMNAME", formName);
               ed.commit();
           }
        super.onPause();
    }
    public void Refresh(long ParentID) {
        Zorder(ParentID);
        for(int i=0;i<mainRelativeLayout.getChildCount();++i) {
            View child = mainRelativeLayout.getChildAt(i);
            child.setFocusable(false);
            if  (child instanceof DataList) {
                ((DataList) child).Refresh("", ParentID);
                child.bringToFront();
            } else if  (child instanceof PictureList) {
                ((PictureList) child).Refresh();
                child.bringToFront();
            } else if (child instanceof DataLayout) {
                ((DataLayout) child).Refresh("");
                child.bringToFront();
                // } else if (child instanceof DataSpinner) {
                //      ((DataSpinner) child).Refresh(0);

            }
        }
        topv.bringToFront();
    }
    public void Zorder(long ParentID) {
        for(int i=mainRelativeLayout.getChildCount()-1;i>=0;--i) {
            View child = mainRelativeLayout.getChildAt(i);
            if (!(child instanceof PictureLayout) && !(child instanceof CameraLayout) && !(child instanceof DrawLayout)) {
                child.bringToFront();
            }
        }
        topv.bringToFront();
    }
    boolean wastwo = false;

    int roundX(int n) {
        return (n + (gridx-1)) / gridx * gridx;
    }
    int roundY(int n) {
        return (n + (gridy-1)) / gridy * gridy;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (Editing) {
            boolean ret= true;
            android.util.DisplayMetrics metrics = getResources().getDisplayMetrics();
            int action = event.getAction();
            int x = (int)event.getX() + scrollable.vScroll.getScrollX();
            int y = (int)event.getY() + scrollable.hScroll.getScrollY();
            int x1=-1;
            int y1=-1;
            if (event.getPointerCount() >1){
                wastwo=true;
                x1 = (int) event.getX(1)+ scrollable.vScroll.getScrollX();
                y1 = (int) event.getY(1)+ scrollable.hScroll.getScrollY();
            } else {
              if (wastwo) {
                  Selected=null;
                  action = MotionEvent.ACTION_DOWN;
              }
              wastwo=false;
            }
            switch (action) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    if (Selected!=null) {
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    x1offset = -1;
                    y1offset = -1;

		           	 for(int i=mainRelativeLayout.getChildCount()-1;i>=0;i--){
			      	       View child=mainRelativeLayout.getChildAt(i);
                         RelativeLayout.LayoutParams LayoutParameters = (RelativeLayout.LayoutParams) child.getLayoutParams();
		           		   int xpos = (int) x;
		           		   int ypos = y-50; // Find Offset! 
		           		   int width = child.getWidth();
		           		  // if (child == topv) width = 35;
			      	       if (xpos >= LayoutParameters.leftMargin &&  xpos <= LayoutParameters.leftMargin+width  
			      	       &&  ypos >= LayoutParameters.topMargin  &&  ypos <= LayoutParameters.topMargin +child.getHeight() ) {
				            	if (LastSelected!=null) {
				            		LastSelected.setBackgroundColor(editc);
					      	    	 if  (LastSelected instanceof YASFAControl) {
                                         ((YASFAControl)LastSelected).EditFocus(false);
                                     }
                                    DelSelected = LastSelected;
				            		LastSelected = null;
				            	}
                               xoffset = xpos - LayoutParameters.leftMargin;
                               yoffset = ypos - LayoutParameters.topMargin;
				      	        if (child instanceof MMLayout) {
                                    Selected = child;
                                    Selected .bringToFront();
                                    topv.bringToFront();
				      	    	    Selected.setBackgroundColor(Color.argb(150,60 , 90, 255));
                                   // return true;
				      	        } else {
                                     Selected = child;
					      	    	 Selected.bringToFront();
                                    topv.bringToFront();
					      	    	 Selected.setBackgroundColor(Color.argb(150, 237, 100, 170));
                                    if (!editMoveOnly) {
                                         if (Selected instanceof YASFAControl) {
                                             ((YASFAControl) Selected).EditFocus(true);
                                         }
                                     }
                              }
				      	      break;
			      	       } else {
			      	    	   // ((Button)(child)).setText(LayoutParameters.leftMargin);
			      	       }
		          	 	 }
                    if (Selected!=null) {
	           		     int xpos = x -xoffset;
 	           		     int ypos = y-50 -yoffset;
 	           		     if (xpos  < 0)
  	           		    	xpos=0;
 	           		     if (ypos  < 0)
 	           		    	ypos = 0;
                        if (editMoveOnly)
	           		        SetLayout(Selected, RelativeLayout.ALIGN_PARENT_LEFT, xpos, ypos, 0, 0,0,0);
                        if (Selected instanceof RadioLayout)
                        {
                            //Button label=(Button)((LinearLayout) Selected).getChildAt(0);
                            //label.dispatchTouchEvent(event);
                            // ret=false;
                        }
                        else if (Selected instanceof LinearLayout)
                        {
                            View label=((LinearLayout) Selected).getChildAt(0);
                            label.dispatchTouchEvent(event);
                        }
  	      	   	      break;
	            	 } else {
	            		 ret = super.dispatchTouchEvent(event);
	            	 }
	            	 return ret;
	            case MotionEvent.ACTION_MOVE:
		           	 if (Selected!=null) {
                         int xpos = roundX(x - xoffset);
                         int ypos = roundY(y - 50 - yoffset);
                         if (xpos < 0) xpos = 0;
                         if (ypos < 0) ypos = 0;
                         int xpos1 = roundX(x1 - x1offset);
                         int ypos1 = roundY(y1 - 50 - y1offset);
                         if (xpos1 < 0) xpos1 = 0;
                         if (ypos1 < 0) ypos1 = 0;
                         if (editMoveOnly)  { // Hard to position controls and edit text at the same time!
                             if (Selected instanceof YASFAControl && xpos1 > 0) {
                                 if (x1offset == -1) {
                                     xpos1 = x1 - x1offset;
                                     ypos1 = y1 - 50 - y1offset;
                                 }
                                 LayoutParams params = Selected.getLayoutParams();
                                 params.height = ypos1 - ypos;
                                 params.width = xpos1 - xpos;
                                 ((YASFAControl) Selected).SetSize(xpos1 - xpos, ypos1 - ypos);
                                 SetLayout(Selected, RelativeLayout.ALIGN_PARENT_LEFT, xpos, ypos, params.width, params.height, 0, 0);
                             } else {
                                 SetLayout(Selected, RelativeLayout.ALIGN_PARENT_LEFT, xpos, ypos, 0, 0, 0, 0);
                             }
                         } else if (Selected instanceof MMLayout) { // If its the menu more it anyway.
                             SetLayout(Selected, RelativeLayout.ALIGN_PARENT_LEFT, xpos, ypos, 0, 0, 0, 0);
                         }
                         if (Selected instanceof RadioLayout)
                         {
                             ret=false;
                         }
	            		 else if (Selected instanceof LinearLayout) {
	            			View label=((LinearLayout) Selected).getChildAt(0);
	            			label.dispatchTouchEvent(event);
	            		}		
		        	 } else {
		        		 ret = super.dispatchTouchEvent(event);
		        	 }
		             return  ret;
	            case MotionEvent.ACTION_UP:
	            	if (Selected!=null) {
		            	LastSelected = Selected;
                       if (Selected instanceof MMLayout) {
			      	    	 topv.bringToFront();
                           topv.bringToFront();
		      	    	 Selected.setBackgroundColor(Color.argb(240,255 , 255, 0));
		            	 LastSelected = null;
		      	       } else {
			      	    	 Selected.bringToFront();
                           topv.bringToFront();
		      	    	   Selected.setBackgroundColor(Color.argb(150, 237, 100, 170));
                           if (!editMoveOnly) {
                               if (Selected instanceof YASFAControl) {
                                   ((YASFAControl) Selected).EditFocus(true);
                               }
                           }
                       }
                        if (Selected instanceof RadioLayout)
                        {
                            ret=false;
                        }
            		   else if (Selected instanceof LinearLayout) {
	            			View label=((LinearLayout) Selected).getChildAt(0);
                            label.requestFocus();
                            label.dispatchTouchEvent(event);
	            		}
	            	   Selected=null;
	            	}
	             	 ret = super.dispatchTouchEvent(event);
	            	return ret;
	        }
    	} else {
            if (speak) {
                int x = (int)event.getX() + scrollable.vScroll.getScrollX();
                int y = (int)event.getY() + scrollable.hScroll.getScrollY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        for(int i=mainRelativeLayout.getChildCount()-1;i>=0;i--) {
                            View child = mainRelativeLayout.getChildAt(i);
                            RelativeLayout.LayoutParams LayoutParameters = (RelativeLayout.LayoutParams) child.getLayoutParams();
                            int xpos = (int) x;
                            int ypos = y - 50; // Find Offset!
                            int width = child.getWidth();
                            if (xpos >= LayoutParameters.leftMargin && xpos <= LayoutParameters.leftMargin + width
                                    && ypos >= LayoutParameters.topMargin && ypos <= LayoutParameters.topMargin + child.getHeight()) {
                                if (child instanceof PictureLayout) {
                                } else if (child instanceof YASFAControl) {
                                    if (event.getAction()==MotionEvent.ACTION_DOWN && (child instanceof ButtonLayout)) {
                                        Selected = child;
                                        // Suppress when button click!
                                    } else {
                                        if (!(child instanceof ButtonLayout)) {
                                            Selected = null;
                                        }
                                        if (Selected == child) {

                                        } else {
                                            if (((YASFAControl)child).DoSay(xpos -LayoutParameters.leftMargin , ypos - LayoutParameters.topMargin ))
                                            Say(((YASFAControl) child).SayText());
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        Selected = null;
                        lasttext="";
                        break;
                }

            }
        View v = getCurrentFocus();
        if (v instanceof DrawLayout) {
            DrawLayout.Draw list = ((DrawLayout)v).mdraw;
            list.dispatchTouchEvent(event);
        }}
        return super.dispatchTouchEvent(event);
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	return false;
    }
    */
    public void SetLayout(View item, int centerInParent, int marginLeft, int marginTop, int marginRight, int marginBottom,int width,int height) {

        RelativeLayout.LayoutParams LayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        LayoutParameters.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        if (width!=0)
            LayoutParameters.width=width;
        if (height!=0)
            LayoutParameters.height=height;

        if (width<0)
            LayoutParameters.width=0;
        if (height<0)
            LayoutParameters.height=0;

        LayoutParameters.addRule(centerInParent);
        item.setLayoutParams(LayoutParameters);
        }


    @Override
    public void onDestroy() {
        for (int i = 0; i < mainRelativeLayout.getChildCount(); ++i) {
            View child = mainRelativeLayout.getChildAt(i);
            if (child instanceof CameraLayout) {
                ((CameraLayout) child).Destroy();
            } else if (child instanceof DataList) {
                ((DataList) child).Destroy();
            } else if (child instanceof PictureList) {
                ((PictureList) child).Destroy();
            }
        }
        unregisterReceiver(mKillReceiver);
        super.onDestroy();
    }

    private final class KillReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                finish();
            } catch (Exception ex) {}
        }
    }

    @Override
    public void onBackPressed() {
       if (Editing) {
           ControlManage.Edit(MainView);
           SaveView();
       } else {
           super.onBackPressed();
       }
    }

    public void SaveView(){
        final Dialog mDialog;
        mDialog = new Dialog(MainView);
        mDialog.setTitle("Save View");
        LinearLayout ll = new LinearLayout(MainView);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout ll1 = new LinearLayout(MainView);
        final LinearLayout.LayoutParams lp = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mDialog.addContentView(ll, lp);

        Button ok, cancel;

        ok = (Button) new Button(MainView);
        ll1.addView(ok);
        ok.setText("Save");
        cancel = (Button) new Button(MainView);
        ll1.addView(cancel);
        ll.addView(ll1);
        cancel.setText("Cancel");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = ProgressDialog.show(MainView, "", "Save...",true);
                Thread t = new Thread(new Runnable()
                {
                    public void run()
                    {

                        FormSerialize fz = new FormSerialize();
                        fz.setBaseContext(MainView.getBaseContext());
                        fz.SaveForm(MainView.formName, MainView.mainRelativeLayout, MainView,MainView.LockCode,mLanguageCode);

                        DBInterface dbz = new DBInterface();
                        dbz.setBaseContext(MainView.getBaseContext());
                        dbz.CreteTable(MainView.formName, MainView.mainRelativeLayout);
                        progressDialog.dismiss();

                        MainView.runOnUiThread(new Runnable() //run on ui thread
                        {
                            public void run() {

                                try {
                                    DBInterface dbz = new DBInterface();
                                    dbz.setBaseContext(MainView);
                                    MainView.RowID = dbz.Get(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout,Direction.First);
                                    Refresh(MainView.ParentRowID);
                                } catch (Exception ex) {
                                }
                            }
                        });

                    }
                });
                t.start();
                mDialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(MainView, "", "Cancel...",true);
                Thread t = new Thread(new Runnable()
                {
                    public void run() {

                        FormSerialize fz = new FormSerialize();
                        fz.setBaseContext(MainView.getBaseContext());
                        fz.LoadForm(formName, mainRelativeLayout, MainView);

                        progressDialog.dismiss();

                        MainView.runOnUiThread(new Runnable() //run on ui thread
                        {

                            public void run() {

                                try {

                                    Intent myIntent = new Intent(MainView, InflateView.class);
                                    myIntent.putExtra("formName", MainView.formName); //Optional parameters
                                    myIntent.putExtra("ParentRowID", MainView.ParentRowID); //Optional parameters
                                    myIntent.putExtra("ParentformName", MainView.ParentformName); //Optional parameters
                                    myIntent.putExtra("ParentRow", MainView.ParentRow); //Optional parameters
                                    MainView.finish();
                                    MainView.startActivity(myIntent);

                                } catch (Exception ex) {
                                }
                            }
                        });
                    }
                });
                t.start();
                mDialog.cancel();
            }
        });
        mDialog.show();
    }

    public void SaveApp (final String filename) {
        final ProgressDialog progressDialog = ProgressDialog.show(MainView, "", "Backup...", true);
        Thread t = new Thread(new Runnable() {
            public void run() {
                Utils.copyToSD(MainView, "YASFA.db", filename + ".app");
                Utils.copyToSD(MainView, "YASFAD.db", filename + "D.db");
                progressDialog.dismiss();
            }
        });
        t.start();
    }

    public void BackupApp() {
        final Dialog mDialog;
        mDialog = new Dialog(MainView);
        mDialog.setTitle("      Backup      ");
        LinearLayout ll = new LinearLayout(MainView);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout ll1 = new LinearLayout(MainView);
        final LinearLayout.LayoutParams lp = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mDialog.addContentView(ll, lp);


        Button ok, cancel;
        final EditText filename;
        final TextView lfilename;

        lfilename = (TextView) new TextView(MainView);
        lfilename.setText("Name");
        filename = (EditText) new EditText(MainView);
        ll.addView(lfilename);
        ll.addView(filename);
        ok = (Button) new Button(MainView);
        ll1.addView(ok);
        ok.setText("Save");
        cancel = (Button) new Button(MainView);
        ll1.addView(cancel);
        ll.addView(ll1);
        cancel.setText("Cancel");
        ok.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                mDialog.cancel();
                if (Utils.FileExistsSD(MainView,filename.getText() + ".app")) {
                    new AlertDialog.Builder(MainView)
                            .setTitle("Application Exist")
                            .setMessage("Overwrite the Application?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SaveApp(filename.getText().toString());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                }
                else{
                    SaveApp(filename.getText().toString());
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        mDialog.show();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        if (!Editing)
        {
        MenuItem item6 = menu.add("New App");
        item6.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item6) {
                final Dialog mDialog;
                mDialog = new Dialog(MainView);
                mDialog.setTitle("New Application");
                LinearLayout ll = new LinearLayout(MainView);
                ll.setOrientation(LinearLayout.VERTICAL);

                final LinearLayout.LayoutParams lp = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mDialog.addContentView(ll, lp);

                Button backup,ok, cancel;
                backup = (Button) new Button(MainView);
                backup.setText("Backup First?");
                ll.addView(backup);

                backup.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        BackupApp();
                    }
                });

                ok = (Button) new Button(MainView);
                ok.setText("New App");
                ll.addView(ok);

                cancel = (Button) new Button(MainView);
                ll.addView(cancel);

                cancel.setText("Cancel");


                ok.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {


                        Utils.delete(MainView, "YASFA.db");
                        Utils.delete(MainView, "YASFAD.db");

                        DBInterface dbz = new DBInterface();
                        dbz.setBaseContext(MainView);
                        dbz.openDB();
                        dbz.closeDB();

                        FormSerialize dba = new FormSerialize();
                        dba.setBaseContext(MainView);
                        dba.openDB();
                        dba.closeDB();

                        Intent intent = new Intent("kill");
                        intent.setType("text/plain");
                        sendBroadcast(intent);
                        SharedPreferences YASFAState = MainView.getSharedPreferences("YASFAState", Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = YASFAState.edit();
                        ed.clear();
                        ed.putLong("ROWID", -99L);
                        ed.putLong("PARENTROWID", -1L);
                        ed.putString("FORMNAME", "");
                        ed.commit();
                        Intent myIntent = new Intent(MainView, InflateView.class);
                        myIntent.putExtra("formName", "Menu"); //Optional parameters
                        MainView.startActivity(myIntent);
                        mDialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                    }
                    });
                mDialog.show();

                return true;
            }

        });

            MenuItem item4 = menu.add("Backup");
            item4.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item4) {
                    BackupApp();
                return true;
            }

        });
        MenuItem item5 = menu.add("Restore");
        item5.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item4) {
                final Dialog mDialog;
                mDialog = new Dialog(MainView);
                mDialog.setTitle("Restore Application");
                LinearLayout ll = new LinearLayout(MainView);
                ll.setOrientation(LinearLayout.VERTICAL);
                final LinearLayout.LayoutParams lp = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mDialog.addContentView(ll, lp);

                Button backup,ok,defaultdb , cancel;
                backup = (Button) new Button(MainView);
                backup.setText("Backup First?");
                ll.addView(backup);

                defaultdb = (Button) new Button(MainView);
                defaultdb.setText("Default");
                ll.addView(defaultdb);

                defaultdb.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final ProgressDialog progressDialog = ProgressDialog.show(MainView, "", "Restore...", true);
                        Thread t = new Thread(new Runnable() {
                            public void run() {
                                Utils.defaltdb(MainView, true);

                                SharedPreferences YASFAState = MainView.getSharedPreferences("YASFAState", MODE_PRIVATE);
                                SharedPreferences.Editor ed = YASFAState.edit();
                                ed.clear();
                                ed.putLong("ROWID", -99L);
                                ed.putLong("PARENTROWID", -1L);
                                ed.putString("FORMNAME", "");
                                ed.commit();

                                Intent intent = new Intent("kill");
                                intent.setType("text/plain");
                                sendBroadcast(intent);

                                Intent myIntent = new Intent(MainView, InflateView.class);
                                myIntent.putExtra("formName", "Menu"); //Optional parameters
                                MainView.startActivity(myIntent);
                                progressDialog.dismiss();
                            }
                        });
                        t.start();
                        mDialog.cancel();
                    }
                });

                backup.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        BackupApp();
                    }
                });

                ok = (Button) new Button(MainView);
                ok.setText("Restore App");
                ll.addView(ok);

                cancel = (Button) new Button(MainView);
                ll.addView(cancel);
                cancel.setText("Cancel");


                ok.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {


                        class loadit extends LoadAFile {
                            @Override
                            public void LoadFile(final String file) {
                                final ProgressDialog progressDialog = ProgressDialog.show(MainView, "", "Restore...",true);
                                Thread t = new Thread(new Runnable()
                                {
                                    public void run()
                                    {
                                        String nfile=file;
                                        Utils.copyFromSD(MainView, nfile, "YASFA.db");
                                        nfile = nfile.replace(".app", "D.db");
                                        Utils.copyFromSD(MainView, nfile, "YASFAD.db");

                                        SharedPreferences YASFAState = MainView.getSharedPreferences("YASFAState", MODE_PRIVATE);
                                        SharedPreferences.Editor ed = YASFAState.edit();
                                        ed.clear();
                                        ed.putLong("ROWID", -99L);
                                        ed.putLong("PARENTROWID", -1L);
                                        ed.putString("FORMNAME", "");
                                        ed.commit();

                                        Intent intent = new Intent("kill");
                                        intent.setType("text/plain");
                                        sendBroadcast(intent);

                                        Intent myIntent = new Intent(MainView, InflateView.class);
                                        myIntent.putExtra("formName", "Menu"); //Optional parameters
                                        MainView.startActivity(myIntent);
                                        progressDialog.dismiss();
                                    }
                                });
                                t.start();
                            }
                        }
                        loadit ld = new loadit();
                        Dialog dg = MainView.CreateDialog(MainView.DIALOG_LOAD_FILE, ".app", ld);
                        mDialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                    }
                });
                mDialog.show();

                return true;
            }

        });
            final MenuItem item7 = menu.add("Speak");
            if (speak) {
                item7.setTitle("Mute");
            } else {
                item7.setTitle("Speak");
            }
            item7.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item4) {
                    if (speak) {
                        speak=false;
                        item7.setTitle("Speak");
                    } else {
                        speak=true;
                        item7.setTitle("Mute");
                    }
                    return true;
                }
            });

            MenuItem item2 = menu.add ("Lock");
            item2.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener(){
                @Override
                public boolean onMenuItemClick (MenuItem item2){
                    final Dialog mDialog;
                    mDialog=new Dialog(MainView);
                    mDialog.setTitle("Set Lock Code");
                    LinearLayout ll = new LinearLayout(MainView);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout ll1 = new LinearLayout(MainView);
                    final LinearLayout.LayoutParams lp = new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    mDialog.addContentView(ll,lp);


                    Button ok,cancel;
                    final EditText oldlockCode;
                    final EditText lockCode;
                    final TextView loldlockCode;
                    final TextView llockCode;

                    loldlockCode=(TextView) new TextView(MainView);
                    loldlockCode.setText("Old Lock Code");
                    llockCode=(TextView) new TextView(MainView);
                    llockCode.setText("New Lock Code");
                    oldlockCode=(EditText) new EditText(MainView);
                    ll.addView(loldlockCode);
                    ll.addView(oldlockCode);
                    lockCode=(EditText) new EditText(MainView);
                    ll.addView(llockCode);
                    ll.addView(lockCode);
                    ok=(Button) new Button(MainView);
                    ll1.addView(ok);
                    ok.setText("OK");
                    cancel=(Button) new Button(MainView);
                    ll1.addView(cancel);
                    ll.addView(ll1);
                    cancel.setText("Cancel");
                    ok.setOnClickListener(new View.OnClickListener() {


                        @Override
                        public void onClick(View v) {
                            if (oldlockCode.getText().toString().equals(LockCode)) {
                                LockCode = lockCode.getText().toString();
                                FormSerialize fz = new FormSerialize();
                                fz.setBaseContext(getBaseContext());
                                fz.SaveLock(MainView.formName, LockCode);
                                invalidateOptionsMenu();
                                mDialog.cancel();
                            }
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mDialog.cancel();
                        }
                    });
                    mDialog.show();
                    return true;
                }
            });
    }
    if (Editing)
    {

        MenuItem item8 = menu.add("Language");
        item8.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick (MenuItem item2){
                final Dialog mDialog;
                mDialog=new Dialog(MainView);
                mDialog.setTitle("Set Language Code");
                LinearLayout ll = new LinearLayout(MainView);
                ll.setOrientation(LinearLayout.VERTICAL);
                LinearLayout ll1 = new LinearLayout(MainView);
                final LinearLayout.LayoutParams lp = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mDialog.addContentView(ll,lp);


                Button ok,cancel;
                final EditText LanguageCode;
                final TextView lLanguageCode;

                lLanguageCode=(TextView) new TextView(MainView);
                lLanguageCode.setText("Language Code eg UK");
                LanguageCode=(EditText) new EditText(MainView);
                if (mLanguageCode.trim().equals("")) {
                    mLanguageCode = "UK";
                }
                LanguageCode.setText(mLanguageCode);
                ll.addView(lLanguageCode);
                ll.addView(LanguageCode);

                ok=(Button) new Button(MainView);
                ll1.addView(ok);
                ok.setText("OK");
                cancel=(Button) new Button(MainView);
                ll1.addView(cancel);
                ll.addView(ll1);
                cancel.setText("Cancel");
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            mLanguageCode = LanguageCode.getText().toString();
                            FormSerialize fz = new FormSerialize();
                            fz.setBaseContext(getBaseContext());
                            fz.SaveLanguageCode(MainView.formName, mLanguageCode);
                            invalidateOptionsMenu();
                            mDialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                    }
                });
                mDialog.show();
                return true;
            }
        });


        if (LockCode.equals("")) {
            MenuItem item7 = menu.add("Sync Flip");
            item7.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item6) {
                    FormSerialize fz = new FormSerialize();
                    fz.setBaseContext(getBaseContext());
                    fz.SyncFlip(formName);
                    return true;
                }
            });
        }

            MenuItem item3 = menu.add("Grid");
            item3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item2) {
                    final Dialog mDialog;
                    mDialog = new Dialog(MainView);
                    mDialog.setTitle("Set Edit Grid");
                    LinearLayout ll = new LinearLayout(MainView);
                    LinearLayout ll1 = new LinearLayout(MainView);
                    final LinearLayout.LayoutParams lp = new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    mDialog.addContentView(ll, lp);

                    Button ok, cancel;
                    final NumberPicker xoff;
                    final NumberPicker yoff;

                    final TextView loldlockCode;
                    final TextView llockCode;

                    loldlockCode = (TextView) new TextView(MainView);
                    loldlockCode.setText("X");
                    llockCode = (TextView) new TextView(MainView);
                    llockCode.setText("Y");
                    xoff = (NumberPicker) new NumberPicker(MainView);
                    xoff.setMinValue(1);
                    xoff.setMaxValue(10);
                    ll.addView(loldlockCode);
                    ll.addView(xoff);
                    yoff = (NumberPicker) new NumberPicker(MainView);
                    yoff.setMinValue(1);
                    yoff.setMaxValue(10);
                    ll.addView(llockCode);
                    ll.addView(yoff);
                    ok = (Button) new Button(MainView);
                    ll1.addView(ok);
                    ok.setText("OK");
                    cancel = (Button) new Button(MainView);
                    ll1.addView(cancel);
                    ll.addView(ll1);
                    xoff.setValue(gridx);
                    yoff.setValue(gridy);
                    cancel.setText("Cancel");
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gridx = xoff.getValue();
                            gridy = yoff.getValue();
                            mDialog.cancel();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mDialog.cancel();
                        }
                    });
                    mDialog.show();
                    return true;
                }
            });
        }
        if (LockCode==null ) LockCode="";
        if (LockCode.equals("\"+LockCode+\"")) LockCode="";

        if (LockCode.equals(""))  {
            final MenuItem item1 = menu.add ("Design");
            if (Editing) {
                dummy.setEnabled(false);
                item1.setTitle("Run");
            }
            else {
                dummy.setEnabled(true);
                dummy.requestFocus();
                item1.setTitle("Design");
            }

            item1.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener(){
                @Override
                public boolean onMenuItemClick (MenuItem item){
                    ControlManage.Edit(MainView);
                    if (Editing) {
                        /*if (topv.getX()<scrollable.vScroll.getScrollX() || topv.getY()<scrollable.hScroll.getScrollY())
                            SetLayout(topv, RelativeLayout.ALIGN_PARENT_LEFT, scrollable.vScroll.getScrollX()+5, scrollable.hScroll.getScrollY()+5, 0, 0,0,0);
                        Display display = ((WindowManager)MainView.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                        if (topv.getY() + topv.getHeight() > display.getHeight()+scrollable.hScroll.getScrollY() || topv.getX()+topv.getWidth()-5 > display.getWidth() +scrollable.vScroll.getScrollX())  {
                            SetLayout(topv, RelativeLayout.ALIGN_PARENT_LEFT, scrollable.vScroll.getScrollX(), scrollable.hScroll.getScrollY()+5, 0, 0,0,0);
                        }*/

                        Display display = ((WindowManager)MainView.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                        SetLayout(topv, RelativeLayout.ALIGN_PARENT_LEFT, scrollable.vScroll.getScrollX()+5, scrollable.hScroll.getScrollY()+5, 0, 0, display.getWidth()-20, 0);
                    }

                    if (!Editing) {
                        SaveView();
                    }

                    if (item1.getTitle().toString().equals("Design"))
                        item1.setTitle("Run");
                    else
                        item1.setTitle("Design");
                    invalidateOptionsMenu();
                    return true;

                }
            });
        }
        return true;
    }
    //In an Activity
    private String[] mFileList;
    private File mPath = new File(Environment.getExternalStorageDirectory() + "");
    public String mChosenFile;
    public static final int DIALOG_LOAD_FILE = 1000;

    private void loadFileList(final String FTYPE) {
        try {
            mPath.mkdirs();
        }
        catch(SecurityException e) {
        }
        if(mPath.exists()) {
            FilenameFilter filter = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    return filename.toUpperCase().contains(FTYPE.toUpperCase());
                }

            };
            mFileList = mPath.list(filter);
        }
        else {
            mFileList= new String[0];
        }
    }

    public Dialog CreateDialog(int id,final String type,final LoadAFile fileload) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_LOAD_FILE:
                loadFileList(type);
                builder.setTitle("Choose your file");
                if(mFileList == null) {
                    dialog = builder.create();
                    return dialog;
                }
                builder.setItems(mFileList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        fileload.LoadFile(mPath+"/"+mFileList[which]);
                        mChosenFile = mPath+"/"+mFileList[which];
                        //you can do stuff with the file here too
                    }
                });
                break;
        }
        dialog = builder.show();
        return dialog;
    }


}
