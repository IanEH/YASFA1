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

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.util.ArrayList;

import com.yasfa.views.DBInterface.Direction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.Image;
import android.os.Looper;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.StateSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class DataList extends YASFAControl {
	public String Name;
    Boolean bnew=true;
    RelativeLayout mmainRelativeLayout;
    String mformName;
    ListView list;
    long  lastSelected=-1;
    final EditText Select;

    public String SayText() {
        return ""/*"Data List Control"*/;
    }

    public int setImageViewWithByteArray(final ImageView view, final byte[] data, final byte[] data1) {
        int height=0;
        if (data != null && data.length > 10) {
            try {
                Bitmap bitmap1=null,bm1=null;
                Bitmap bitmap2=null,bm2=null;
                int h1 = 0;
                android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
                try {
                    sleep(2);
                    bitmap1 = BitmapFactory.decodeByteArray(data, 0, data.length);
                    sleep(2);
                    final float swidth = (float) (params.width) / (float) (bitmap1.getWidth());
                    sleep(2);
                    height += (int)(bitmap1.getHeight() * swidth);
                    sleep(2);
                    h1= height;
                    sleep(2);
                    bm1=Bitmap.createScaledBitmap(bitmap1, (int) (bitmap1.getWidth() * swidth), (int) (bitmap1.getHeight() * swidth), true);
                } catch (Exception ex) {
                }
                try {
                    if (data1!=null) {
                        bitmap2 = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                        sleep(2);
                        final float swidth = (float) (params.width) / (float) (bitmap2.getWidth());
                        sleep(2);
                        height += (int) (bitmap2.getHeight() * swidth);
                        sleep(2);
                        bm2 = Bitmap.createScaledBitmap(bitmap2, (int) (bitmap2.getWidth() * swidth), (int) (bitmap2.getHeight() * swidth), true);
                    }
                } catch (Exception ex) {
                }
                params.height=height+2;

                sleep(2);
                Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
                sleep(2);
                final Bitmap bitmap = Bitmap.createBitmap(params.width,height,conf);
                sleep(2);
                Canvas c = new Canvas(bitmap);
                sleep(2);
                c.drawBitmap(bm1, 0, 0, null);
                if (bm2!=null) {
                    sleep(2);
                    c.drawBitmap(bm2, 0, h1+2, null);
                    sleep(2);
                    bm2.recycle();
                    bitmap2.recycle();
                }
                bm1.recycle();
                bitmap1.recycle();


                mcontext.runOnUiThread(new Runnable() //run on ui thread
                {
                    public void run() {

                        try {
                            view.setImageBitmap(bitmap);
                        } catch (Exception ex) {
                        }
                    }
                });
                } catch (Exception ex) {
                }
            } else {
                mcontext.runOnUiThread(new Runnable() //run on ui thread
                {
                    public void run() {
                        try {
                            view.setImageBitmap(null);
                            view.setVisibility(View.GONE);
                        } catch (Exception ex) {
                        }
                    }
                });
            }
        return height+2;
     }
    public void Destroy() {
        try {
            ItemListBaseAdapter ad = (ItemListBaseAdapter) list.getAdapter();

            for (int i = 0; i < ad.getCount(); ++i) {
                ad.getItem(i);
                for (int j = 0; j < ad.itemDetailsrrayList.size(); ++j) {
                    try {
                        ((BitmapDrawable) ad.itemDetailsrrayList.get(j).img_image.getDrawable()).getBitmap().recycle();
                        ad.itemDetailsrrayList.get(j).img_image.setImageDrawable(null);
                    } catch (Exception ex) {
                    }

                }
            }
        } catch (Exception ex) {
        }
    }

	public class ItemDetails {
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        private String name ;
        int imgheight=0;
        ImageView img_image;
	}

	public class ItemListBaseAdapter extends BaseAdapter {
         public ArrayList<ItemDetails> itemDetailsrrayList;
        InflateView lcontext;
		 private LayoutInflater l_Inflater;
		 public ItemListBaseAdapter(InflateView context, ArrayList<ItemDetails> results) {
		  itemDetailsrrayList = results;
		  l_Inflater = LayoutInflater.from(context);
		  lcontext=context;
		 }


		 public int getCount() {
		  return itemDetailsrrayList.size();
		 }
		 
		 public Object getItem(int position) {
		  return itemDetailsrrayList.get(position);
		 }
		 
		public long getItemId(int position) {
		  return position;
		 }

        ColorStateList std = new ColorStateList(
                new int[][] {
                        new int[] { android.R.attr.state_pressed},
                        new int[1]
                }, new int[] {
                Color.rgb(50, 50, 255),
                Color.BLACK,
        });

		 public View getView(final int position, View convertView, ViewGroup parent) {
		  final ViewHolder holder;
             try {
              if (convertView == null) {
                  holder = new ViewHolder();
                  try {

                      convertView = new LinearLayout(lcontext);
                      convertView.setBackgroundColor(Color.DKGRAY);
                      holder.txt_itemName = new TextView(lcontext);
                      ((LinearLayout) convertView).addView(holder.txt_itemName);
                      holder.txt_itemName.setTextColor(Color.WHITE);
                      holder.txt_itemName.setShadowLayer(1.5f, -1, 1, Color.BLACK);
                      holder.txt_itemName.setVisibility(View.GONE);
                      holder.txt_itemDescription = new TextView(lcontext);
                      holder.txt_itemDescription.setSingleLine();
                      holder.txt_itemDescription.setPadding(7, 0, 2, 0);
                      LayoutParams paramsa = new LayoutParams(
                              LayoutParams.WRAP_CONTENT,
                              LayoutParams.WRAP_CONTENT
                      );
                      paramsa.height = 22;
                      paramsa.width = 2000;
                      holder.txt_itemDescription.setLayoutParams(paramsa);

                      holder.txt_itemDescription.setTextColor(Color.WHITE);
                      holder.txt_itemDescription.setShadowLayer(1.5f, -1, 1, Color.BLACK);
                      holder.img_image = new ImageView(lcontext);

                     // if (itemDetailsrrayList.get(position).hasImages) { // It seem to work out how many cause of this!
                          holder.img_image.setVisibility(View.VISIBLE);
                     // } else {
                    //      holder.img_image.setVisibility(View.GONE);
                    //  }
                      FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(132, 170);

                      LinearLayout ih  = new LinearLayout(lcontext);
                      ih.setOrientation(LinearLayout.VERTICAL);
                      ih.addView(holder.txt_itemDescription);
                      ih.addView(holder.img_image, params1);
                      ((LinearLayout) convertView).addView(ih);
                      convertView.setTag(holder);
                  } catch (Exception ex) {
                      String test = ex.getMessage();
                  }
              } else {
                  holder = (ViewHolder) convertView.getTag();
              }
                 DBInterface dbz = new DBInterface();
                 dbz.setBaseContext(mcontext);
                 final DBInterface.Row row;
                 row = dbz.GetList(Integer.parseInt(itemDetailsrrayList.get(position).getName()), mcontext.ParentRowID, mformName, mmainRelativeLayout, "", false);
                 if (row.hasImages && mcontext.Editing==false) {
                    holder.img_image.setVisibility(View.VISIBLE);
                    try {
                      try {
                          ((BitmapDrawable) holder.img_image.getDrawable()).getBitmap().recycle();
                          holder.img_image.setImageBitmap(null);
                          if (itemDetailsrrayList.get(position).imgheight!=0) {
                              android.view.ViewGroup.LayoutParams params = holder.img_image.getLayoutParams();
                              params.height = itemDetailsrrayList.get(position).imgheight;
                          }
                      } catch (Exception ex) {
                      }
                     Thread thread = new Thread() {
                          @Override
                          public void run() {
                              try {
                                  sleep(2);
                                  itemDetailsrrayList.get(position).imgheight = setImageViewWithByteArray(holder.img_image, row.image, row.image1);
                                  itemDetailsrrayList.get(position).img_image = holder.img_image;
                              } catch (Exception e) {
                              }
                          }
                      };
                      thread.start();
                  } catch (Exception ex) {
                      String test = ex.getMessage();
                  }
              } else {
                     holder.img_image.setVisibility(View.GONE);
              }
              holder.txt_itemName.setText(itemDetailsrrayList.get(position).getName());
              holder.txt_itemDescription.setText(stripComa(row.text));

              } catch (Exception ex) {
                     String test = ex.getMessage();
              }
		  return convertView;
		 }
		 
		 class ViewHolder {
		     TextView txt_itemName;
             ImageView img_image;
		     TextView txt_itemDescription;
		 }
		}

    static String stripComa(String str)
    {
        str=str.trim();
        str=str.replace(",,",",");
        str=str.replace(",,",",");
        while (str.startsWith(","))
        {
            str = str.substring(1, str.length());
        }
        while (str.endsWith(","))
        {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
    ArrayList<ItemDetails> listitems;
    static boolean bupdateimage = false;
	public boolean Refresh(String select,Long ParentID) {
        bupdateimage = false;
        listitems = new ArrayList<ItemDetails>();
        DBInterface dbz = new DBInterface();
        dbz.setBaseContext(mcontext);

        long ID = -1;

        DBInterface.Row row;
        while ((row=dbz.GetList(ID, ParentID, mformName, mmainRelativeLayout, select, true))!=null) {
            ItemDetails item_details = new ItemDetails();
            item_details.setName(row.ID.toString());
            listitems.add(item_details);
            ID=row.ID;
        }
        list.setAdapter(new ItemListBaseAdapter(mcontext, listitems));

       // list.setItemChecked(3, true);
       // list.setSelection(3);
       // list.requestFocusFromTouch();
       // list.setSelection(3);
       // list.getAdapter().getView(3, null, null).performClick();
       // list.performItemClick(list.getAdapter().getView(3, null, null), 3, list.getItemIdAtPosition(3));
		return true;
	}

    public void EditFocus (boolean edit) {
        if (edit) {
            this.setBackgroundColor(Color.argb(150, 237, 100, 170));
            LinearLayout lbg = (LinearLayout) getChildAt(0);
            LinearLayout lbg2 = (LinearLayout) lbg.getChildAt(0);
            ListView list = (ListView) lbg2.getChildAt(0);
            list.setBackgroundColor(Color.argb(150, 237, 100, 170));
            list.setEnabled(false);
        } else {
            this.setBackgroundColor(InflateView.edite);
            LinearLayout lbg = (LinearLayout) getChildAt(0);
            LinearLayout lbg2 = (LinearLayout) lbg.getChildAt(0);
            ListView list = (ListView) lbg2.getChildAt(0);
            list.setBackgroundColor(InflateView.edite);
            list.setEnabled(true);
        }
    }
    public void Edit (boolean edit) {
                if (edit) {
                    this.setBackgroundColor(InflateView.edite);
                    LinearLayout lbg = (LinearLayout)getChildAt(0);
                    lbg.setEnabled(false);
                    LinearLayout lbg2 = (LinearLayout)lbg.getChildAt(0);
                    lbg2.setEnabled(false);
                    ListView list = (ListView)lbg2.getChildAt(0);
                    list.setEnabled(false);
                    Select.setFocusable(false);
                    Select.setFocusableInTouchMode(false);
                    Select.setEnabled(false);
                    list.setBackgroundColor(InflateView.edite);
                } else {
                    this.setBackgroundColor(Color.BLACK);
                    LinearLayout lbg = (LinearLayout)getChildAt(0);
                    lbg.setEnabled(true);
                    LinearLayout lbg2 = (LinearLayout)lbg.getChildAt(0);
                    lbg2.setEnabled(true);
                    ListView list = (ListView)lbg2.getChildAt(0);
                    list.setEnabled(true);
                    Select.setFocusable(true);
                    Select.setFocusableInTouchMode(true);
                    Select.setEnabled(true);
                    list.setBackgroundColor(Color.BLACK);

                }
    }

	public DataList(final String formName,final InflateView MainView,final RelativeLayout mainRelativeLayout) {
        super(MainView);
        mmainRelativeLayout=mainRelativeLayout;
        mformName=formName;
        this.setBackgroundColor(Color.BLACK);

        setOrientation(LinearLayout.VERTICAL);

        Select = new EditText(MainView);
        Select.setTextSize(13);
        Select.setSingleLine();
        Select.setTextColor(Color.WHITE);
        Select.setBackgroundColor(Color.GRAY);
        Select.setPadding(0, 0,0, 0);
        final EditText Parent = new EditText(MainView);
        Parent.setTextSize(14);
        Parent.setEnabled(false);
        Parent.setSingleLine();
        Parent.setBackgroundColor(Color.GRAY);
        Parent.setTextColor(Color.WHITE);
        Parent.setGravity(Gravity.CENTER_HORIZONTAL);
        Parent.setPadding(2, 2, 2, 2);
        if (!MainView.ParentRow.equals(""))
        {
           Parent.setText(stripComa(MainView.ParentRow));
        } else {
            Parent.setVisibility(View.GONE);
        }

        LinearLayout SelBG = new LinearLayout(MainView);
        SelBG.setBackgroundColor(Color.GRAY);
        SelBG.setPadding(1, 1, 1, 1);
        SelBG.setOrientation(LinearLayout.VERTICAL);
        SelBG.addView(Parent);

        LinearLayout SelBG1 = new LinearLayout(MainView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(140, 25);
        SelBG1.setBackgroundColor(Color.DKGRAY);
        SelBG1.setPadding(2, 2, 2, 2);
        SelBG.addView(SelBG1);
        LinearLayout SelBG2 = new LinearLayout(MainView);
        SelBG2.setBackgroundColor(Color.DKGRAY);
        SelBG2.setPadding(3, 3, 1, 1);
        SelBG1.addView(SelBG2);

        FButton Save = new FButton(MainView,"Save");
        Save.setBackgroundResource(R.drawable.mns);
        Save.setTextSize(12);
        Save.setShadowLayer(1.5f, -1, 1, Color.GRAY);
        Save.setPadding(7, 3, 1, 1);
        Save.setVisibility(View.VISIBLE);
        final FButton newi = new FButton(MainView,"Insert");

        Save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bnew=true;
                newi.setBackgroundResource(R.drawable.mnn);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                DBInterface dbz = new DBInterface();
                                dbz.setBaseContext(MainView);
                                if (MainView.RowID==-1) {
                                    MainView.RowID = dbz.Save(MainView.RowID, MainView.ParentRowID, formName, mainRelativeLayout);
                                    ItemDetails item_details = new ItemDetails();
                                    item_details.setName(MainView.RowID.toString());
                                    listitems.add(item_details);
                                    ((ItemListBaseAdapter) list.getAdapter()).notifyDataSetChanged();
                                } else {
                                    MainView.RowID = dbz.Save(MainView.RowID, MainView.ParentRowID, formName, mainRelativeLayout);
                                    ((ItemListBaseAdapter) list.getAdapter()).notifyDataSetChanged();
                                }

                                for(int i=mainRelativeLayout.getChildCount()-1;i>=0;i--) {
                                    View child = mainRelativeLayout.getChildAt(i);
                                    if (child instanceof DataLayout) {
                                        ((DataLayout) child).Refresh("");
                                    }
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                DBInterface dbzg = new DBInterface();
                                dbzg.setBaseContext(MainView);
                                if (MainView.RowID!=-1) {
                                    MainView.RowID = dbzg.Get(MainView.RowID, MainView.ParentRowID, formName, mainRelativeLayout, Direction.This);
                                } else {
                                    MainView.RowID = dbzg.Get(MainView.RowID, MainView.ParentRowID, formName, mainRelativeLayout, Direction.First);
                                    for(int i=mainRelativeLayout.getChildCount()-1;i>=0;i--) {
                                        View child = mainRelativeLayout.getChildAt(i);
                                        if (child instanceof DataLayout) {
                                            ((DataLayout) child).Refresh("");
                                        }
                                    }
                                }
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(MainView);
                builder.setMessage("Save item?").setPositiveButton("Yes",dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        bnew=true;
        newi.setBackgroundResource(R.drawable.mnn);
        newi.setTextSize(12);
        newi.setShadowLayer(1.5f, -1, 1, Color.GRAY);
        newi.setPadding(10, 3, 1, 3);
        newi.setTextColor(Color.WHITE);
        newi.setVisibility(View.VISIBLE);

        newi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DBInterface dbz = new DBInterface();
                dbz.setBaseContext(MainView);
                if (bnew==false) {
                    bnew=true;
                    MainView.RowID = dbz.Get(MainView.RowID, MainView.ParentRowID, formName, mainRelativeLayout, Direction.First);
                    for(int i=mainRelativeLayout.getChildCount()-1;i>=0;i--) {
                        View child = mainRelativeLayout.getChildAt(i);
                        if (child instanceof DataLayout) {
                            ((DataLayout) child).Refresh("");
                        }
                    }
                    newi.setBackgroundResource(R.drawable.mnn);
                } else {
                    MainView.RowID = dbz.Get(MainView.RowID, MainView.ParentRowID, formName, mainRelativeLayout, Direction.New);
                    for(int i=mainRelativeLayout.getChildCount()-1;i>=0;i--) {
                        View child = mainRelativeLayout.getChildAt(i);
                        if (child instanceof DataLayout) {
                            ((DataLayout) child).Refresh("");
                        }
                    }
                    bnew=false;
                    newi.setBackgroundResource(R.drawable.mnc);
                }
            }
        });

        FButton del = new FButton(MainView,"Delete");
      //  del.setText("Del");
        del.setBackgroundResource(R.drawable.mnd);

        del.setTextColor(Color.WHITE);
        del.setTextSize(12);
        del.setShadowLayer(1.5f, -1, 1, Color.GRAY);
        del.setPadding(10, 3, 1, 3);
        del.setVisibility(View.VISIBLE);

        del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                newi.setBackgroundResource(R.drawable.mnn);
                                bnew=true;
                                DBInterface dbz = new DBInterface();
                                dbz.setBaseContext(MainView);
                                Long deleteid= MainView.RowID;
                                MainView.RowID = dbz.Delete(MainView.RowID,MainView.ParentRowID, formName, mainRelativeLayout);
                                for(int i=mainRelativeLayout.getChildCount()-1;i>=0;i--) {
                                    View child = mainRelativeLayout.getChildAt(i);
                                    if (child instanceof DataLayout) {
                                        ((DataLayout) child).Refresh("");
                                    }
                                }

                                for(int i=0;i<listitems.size();++i) {
                                    if (((ItemDetails) listitems.get(i)).getName().equals(deleteid.toString())) {
                                        listitems.remove(i);
                                        break;
                                    }
                                }
                                ((ItemListBaseAdapter) list.getAdapter()).notifyDataSetChanged();
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


        SelBG1.setBaselineAligned(false);
        SelBG2.setBaselineAligned(false);
        SelBG2.addView(Select);

        params = new FrameLayout.LayoutParams(2, 25);
        EditText pad = new EditText(MainView);
        pad.setEnabled(false);
        SelBG1.addView(pad,params);

        params = new FrameLayout.LayoutParams(44, 25);
        params.setMargins(5, 1, 8, 0);

        params.gravity = Gravity.CENTER_VERTICAL;
        Save.setGravity(Gravity.TOP);
        SelBG1.addView(Save, params);

        params = new FrameLayout.LayoutParams(1, 25);
        EditText pad1 = new EditText(MainView);
        pad1.setEnabled(false);
        SelBG1.addView(pad1,params);

        params = new FrameLayout.LayoutParams(44, 25);
        params.setMargins(5, 1, 8, 0);

        newi.setGravity(Gravity.TOP);
        SelBG1.addView(newi, params);

        params = new FrameLayout.LayoutParams(1, 25);
        EditText pad2 = new EditText(MainView);
        pad2.setEnabled(false);
        SelBG1.addView(pad2,params);

        params = new FrameLayout.LayoutParams(44, 25);
        params.setMargins(5, 1, 8, 0);

        del.setGravity(Gravity.TOP);
        params.setMargins(5, 1, 8, 0);
        SelBG1.addView(del,params);

		Select.addTextChangedListener(new TextWatcher() {
			   public void afterTextChanged(Editable s) {}

			   public void beforeTextChanged(CharSequence s, int start,
			     int count, int after) {
			   }

			   public void onTextChanged(CharSequence s, int start,
			     int before, int count) {
				   Refresh(s.toString(),MainView.ParentRowID);
			   }
			  });



        LinearLayout listBG = new LinearLayout(MainView);
        listBG.setBackgroundColor(Color.GRAY);
        listBG.setPadding(1, 1, 1, 1);
        LinearLayout listBG2 = new LinearLayout(MainView);
        listBG2.setBackgroundColor(Color.BLACK);
        listBG2.setPadding(2, 2, 2, 2);

        list = new ListView(MainView);
        list.setDrawSelectorOnTop(true);
      //  list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setSelector(R.drawable.drawable);
        list.setPadding(1, 1, 1, 1);
        listBG.addView(listBG2);
        listBG2.addView(list);

        addView(listBG);
        addView(SelBG);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			Object o = list.getItemAtPosition(position);
			ItemDetails obj_itemDetails = (ItemDetails)o;
                DBInterface dbz = new DBInterface();
                dbz.setBaseContext(mcontext);

                // No dirty flag yet so just set it!
                if (MainView.RowID !=-1) {
                    //Just an experiment! if (lastSelected!=MainView.RowID && lastSelected!=-1)
                    {
                        dbz.Save(MainView.RowID, MainView.ParentRowID, formName, mainRelativeLayout);
                        ((ItemListBaseAdapter)list.getAdapter()).notifyDataSetChanged();
                    }
                    lastSelected=MainView.RowID;
                }

                if (bnew==false) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    DBInterface dbz = new DBInterface();
                                    dbz.setBaseContext(mcontext);
                                    Long id = dbz.Save(-1, MainView.ParentRowID, formName, mainRelativeLayout);
                                    ItemDetails item_details = new ItemDetails();
                                    item_details.setName(id.toString());
                                    listitems.add(item_details);
                                    ((ItemListBaseAdapter)list.getAdapter()).notifyDataSetChanged();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainView);
                    builder.setMessage("Save new item?").setPositiveButton("Yes",dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                    bnew=true;
                    newi.setBackgroundResource(R.drawable.mnn);
                } else {
                    MainView.RowID = dbz.Get(Long.parseLong(obj_itemDetails.getName()), MainView.ParentRowID, formName, mainRelativeLayout, Direction.This);

                    DBInterface.Row row = dbz.GetList(MainView.RowID, mcontext.ParentRowID, mformName, mmainRelativeLayout, "", false);
                    MainView.Say(stripComa(row.text));


                    for (int i = mainRelativeLayout.getChildCount() - 1; i >= 0; i--) {
                        View child = mainRelativeLayout.getChildAt(i);
                        if (child instanceof DataLayout) {
                           ((DataLayout) child).Refresh("");
                        }
                    }

                    Select.requestFocus();
                }
			}
		});


        SetSize(200,200);
		//SetLayout(list, RelativeLayout.ALIGN_PARENT_LEFT, 0, 80, 200, 200);
	}

    public void SetSize(int width,int height) {

        LinearLayout lbg = (LinearLayout)getChildAt(1);
        EditText Parent = (EditText)lbg.getChildAt(0);
        LinearLayout lbg2 = (LinearLayout)lbg.getChildAt(1);
        LinearLayout lbg3 = (LinearLayout)lbg2.getChildAt(0);
        EditText ed1 = (EditText)lbg3.getChildAt(0);
        android.view.ViewGroup.LayoutParams params = ed1.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        if (width-140<15) {
            ed1.setVisibility(View.GONE);
            lbg3.setVisibility(View.GONE);
        } else {
            ed1.setVisibility(View.VISIBLE);
            lbg3.setVisibility(View.VISIBLE);
            params.width = width - 140;
        }

        lbg = (LinearLayout)getChildAt(0);
        lbg2 = (LinearLayout)lbg.getChildAt(0);
        ListView list = (ListView)lbg2.getChildAt(0);
        params = list.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        int offset = 50;
        if (Parent.getVisibility()==VISIBLE) {
            offset += 25;
            android.view.ViewGroup.LayoutParams params1 = Parent.getLayoutParams();
            params1.width=width;

        }
        params.height = height-offset;
        params.width = width;
        list.setPadding(2,4,2,2);

        LayoutParams paramsa = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        paramsa.setMargins(1,4, 1, 1);
        paramsa.height = height-offset-4;
        paramsa.width = width;
        list.setLayoutParams(paramsa);
        list.setBackgroundColor(Color.TRANSPARENT);


    }
    private void SetLayout(View button, int centerInParent, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        // Defining the layout parameters of the Button
        //DBSerializer qq = new DBSerializer();
        //qq.storeWidget(button);

    	RelativeLayout.LayoutParams buttonLayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Add Margin to the LayoutParameters
        buttonLayoutParameters.setMargins(marginLeft, marginTop, marginRight, marginBottom);

        // Add Rule to Layout
        //buttonLayoutParameters.addRule(centerInParent);

        // Setting the parameters on the Button
        button.setLayoutParams(buttonLayoutParameters);     
    }

	

}
