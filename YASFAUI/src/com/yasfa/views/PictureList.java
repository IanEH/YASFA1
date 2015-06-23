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
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.yasfa.views.DBInterface.Direction;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;

import static java.lang.Thread.sleep;

public class PictureList extends YASFAControl {
	public String Name;
	InflateView mcontext;
    Boolean bnew=true;
    FormSerialize.Fields cols;
    private MediaPlayer mPlayer = null;
    EditText edit1;

    private void startPlaying(byte [] sound) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                }
            });
            String fileName= "/data/data/" + mcontext.getPackageName() + "/databases/"+"YASFAtempl.3gp";
            File yourFile = new File(fileName);
            yourFile.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
            bos.write(sound);
            bos.flush();
            bos.close();
            mPlayer.setDataSource(fileName);
            // Create the Visualizer object and attach it to our media player.

            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
        }
    }

    private void stopPlaying() {
        try {
            mPlayer.release();
        } catch (Exception ex) {}
    }
    public int setImageViewWithByteArray(final ImageView view, final byte[] data, final byte[] data1, final byte[] sound1) {
        int height=0;
        stopPlaying();
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
                            if (sound1.length>10) {
                                view.setOnClickListener(new OnClickListener() {
                                    public void onClick(View v) {
                                        stopPlaying();
                                        startPlaying(sound1);
                                    }
                                });
                            } else {
                                view.setOnClickListener(null);
                            }
                        } catch (Exception ex) {
                        }
                    }
                });
            } catch (Exception ex) {
            }
        } else {
            try {
            mcontext.runOnUiThread(new Runnable() //run on ui thread
            {
                public void run() {
                    try {
                        view.setImageBitmap(null);
                        view.setVisibility(View.GONE);
                        view.setOnClickListener(null);
                    } catch (Exception ex) {
                    }
                }
            });
            } catch (Exception ex) {
            }
        }
        return height+2;
    }

    public void Destroy() {
        try {
            ItemListBaseAdapter ad = (ItemListBaseAdapter) list.getAdapter();
            stopPlaying();
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
        public String getItemDescription() {
            return itemDescription;
        }
        public byte[] getimage() {
            return itemImage;
        }
        public byte[] getimage1() {
            return itemImage1;
        }
        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }
        public void setImage(byte[] image) {
            this.itemImage = image;
        }
        public void setImage1(byte[] image) {
            this.itemImage1 = image;
        }
		private String name ;
        private String itemDescription;
        private byte [] itemImage;
        private byte [] itemImage1;
        public boolean hasImages;
        public boolean hasSounds;

        int imgheight=0;
        ImageView img_image;
	}

	public class ItemListBaseAdapter extends BaseAdapter {
         public ArrayList<ItemDetails> itemDetailsrrayList;
		 Context lcontext;
		 private LayoutInflater l_Inflater;
		 public ItemListBaseAdapter(Context context, ArrayList<ItemDetails> results) {
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
                      holder.txt_itemDescription.setTextColor(Color.WHITE);
                      holder.txt_itemDescription.setShadowLayer(1.5f, -1, 1, Color.BLACK);
                      holder.img_image = new ImageView(lcontext);

                      if (itemDetailsrrayList.get(position).hasImages) { // It seem to work out how many cause of this!
                          holder.img_image.setVisibility(View.VISIBLE);
                      } else {
                          holder.img_image.setVisibility(View.GONE);
                      }
                      LinearLayout lbg = (LinearLayout)getChildAt(0);
                      LinearLayout lbg2 = (LinearLayout)lbg.getChildAt(0);
                      ListView list = (ListView)lbg2.getChildAt(0);
                      android.view.ViewGroup.LayoutParams params = list.getLayoutParams();

                      FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(params.width, params.height);

                      LinearLayout ih  = new LinearLayout(lcontext);
                      ih.addView(holder.txt_itemDescription);
                      ih.setOrientation(LinearLayout.VERTICAL);
                      ih.addView(holder.img_image, params1);
                      ((LinearLayout) convertView).addView(ih);
                      convertView.setTag(holder);
                  } catch (Exception ex) {
                      String test = ex.getMessage();
                  }
              } else {
                  holder = (ViewHolder) convertView.getTag();
              }
              if (itemDetailsrrayList.get(position).hasImages && mcontext.Editing==false) {
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

                      DBInterface dbz = new DBInterface();
                      dbz.setBaseContext(mcontext);
                      final DBInterface.Row row;

                      row = dbz.GetShortList(Integer.parseInt(itemDetailsrrayList.get(position).getName()), -1, edit1.getText().toString(), cols.fieldList,cols.image,cols.image1,cols.sound1,false,true);
                      Thread thread = new Thread() {
                          @Override
                          public void run() {
                              try {
                                  sleep(2);
                                  itemDetailsrrayList.get(position).imgheight = setImageViewWithByteArray(holder.img_image, row.image, row.image1, row.sound1);
                                  itemDetailsrrayList.get(position).img_image = holder.img_image;
                              } catch (Exception e) {
                              }
                          }
                      };
                      thread.start();
                  } catch (Exception ex) {
                      String test = ex.getMessage();
                  }
              }
              holder.txt_itemName.setText(itemDetailsrrayList.get(position).getName());
              holder.txt_itemDescription.setText(itemDetailsrrayList.get(position).getItemDescription());
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

    RelativeLayout mmainRelativeLayout;
    String mformName;
	ListView list;

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
	public boolean Refresh() {
        bupdateimage = false;
        listitems = new ArrayList<ItemDetails>();
        DBInterface dbz = new DBInterface();
        dbz.setBaseContext(mcontext);

        DBInterface.Row row;
        com.yasfa.views.FormSerialize fz = new com.yasfa.views.FormSerialize();
        fz.setBaseContext(mcontext);
        cols = fz.FieldList(edit1.getText().toString(), true);
        long ID = -1;

        while ((row = dbz.GetShortList(ID, -1, edit1.getText().toString(), cols.fieldList,cols.image,cols.image1,cols.sound1,true,true)) != null) {
            try {
                ItemDetails item_details = new ItemDetails();
                item_details.setName(row.ID.toString());
                item_details.setItemDescription(stripComa(row.text));
                item_details.hasImages = row.hasImages;
                item_details.hasSounds = row.hasSounds;
                listitems.add(item_details);
                ID = row.ID;
            } catch (Exception ex) {
                ID=ID+1;
            }
        }

		list.setAdapter(new ItemListBaseAdapter(mcontext, listitems));
		return true;
	}

    public void EditFocus (boolean edit) {
        if (edit) {
            this.setBackgroundColor(InflateView.editb);
            LinearLayout lbg = (LinearLayout) getChildAt(0);
            LinearLayout lbg2 = (LinearLayout) lbg.getChildAt(0);
            ListView list = (ListView) lbg2.getChildAt(0);
            list.setBackgroundColor(InflateView.editb);
            lbg.setBackgroundColor(InflateView.editb);
            lbg2.setBackgroundColor(InflateView.editb);
            list.setEnabled(false);
        } else {
            this.setBackgroundColor(InflateView.edite);
            LinearLayout lbg = (LinearLayout) getChildAt(0);
            LinearLayout lbg2 = (LinearLayout) lbg.getChildAt(0);
            ListView list = (ListView) lbg2.getChildAt(0);
            list.setBackgroundColor(InflateView.edite);
            lbg.setBackgroundColor(InflateView.edite);
            lbg2.setBackgroundColor(InflateView.edite);
            list.setEnabled(true);
        }
    }
    public void Edit (boolean edit) {
        if (edit) {
            this.setBackgroundColor(InflateView.edite);
            LinearLayout lbg = (LinearLayout)getChildAt(0);
            LinearLayout lbg2 = (LinearLayout)lbg.getChildAt(0);
            ListView list = (ListView)lbg2.getChildAt(0);

            list.setClickable(false);
            list.setEnabled(false);
            list.setFocusable(false);
            list.setFocusableInTouchMode(false);

            list.setEnabled(false);
            list.setVisibility(View.GONE);

            edit1.setVisibility(View.VISIBLE);

        } else {
            this.setBackgroundColor(Color.BLACK);
            LinearLayout lbg = (LinearLayout)getChildAt(0);
            LinearLayout lbg2 = (LinearLayout)lbg.getChildAt(0);
            ListView list = (ListView)lbg2.getChildAt(0);

            list.setClickable(true);
            list.setEnabled(true);
            list.setFocusable(true);
            list.setFocusableInTouchMode(true);

            list.setEnabled(true);
            list.setBackgroundColor(Color.BLACK);
            list.setVisibility(View.VISIBLE);

            edit1.setVisibility(View.GONE);
            Refresh();

        }
    }

	public PictureList(final String formName, final InflateView MainView, final RelativeLayout mainRelativeLayout,boolean edit) {
        super(MainView);
        mcontext=MainView;
        mmainRelativeLayout=mainRelativeLayout;
        mformName=formName;
        this.setBackgroundColor(Color.BLACK);

        setOrientation(LinearLayout.VERTICAL);
        LinearLayout listBG = new LinearLayout(MainView);
        listBG.setBackgroundColor(Color.WHITE);
        listBG.setPadding(1, 1, 1, 1);
        LinearLayout listBG2 = new LinearLayout(MainView);
        listBG2.setBackgroundColor(Color.BLACK);
        listBG2.setPadding(3, 3, 3, 3);



        list = new ListView(MainView);
        SetLayout(list,4,4,4,4);
        //mlp.setMargins(5,5,5,5);
        list.setPadding(2, 2, 2, 2);
        list.setBackgroundColor(Color.BLACK);
        listBG.addView(listBG2);
        listBG2.addView(list);

        addView(listBG);
        final LinearLayout bits = new LinearLayout(MainView);
        bits.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        bits.setOrientation(LinearLayout.VERTICAL);

        edit1 = new EditText(MainView);
        bits.addView(edit1);

        listBG2.addView(bits);

        list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			Object o = list.getItemAtPosition(position);
			ItemDetails obj_itemDetails = (ItemDetails)o;
				DBInterface dbz = new DBInterface();
				dbz.setBaseContext(mcontext);
                MainView.RowID = dbz.Get(Long.parseLong(obj_itemDetails.getName()),MainView.ParentRowID, formName, mainRelativeLayout,Direction.This);
                for(int i=mainRelativeLayout.getChildCount()-1;i>=0;i--) {
                    View child = mainRelativeLayout.getChildAt(i);
                    if (child instanceof DataLayout) {
                        ((DataLayout) child).Refresh("");
                    }
                }
			}
		});


        SetSize(200,200);
        Edit(edit);
		//SetLayout(list, RelativeLayout.ALIGN_PARENT_LEFT, 0, 80, 200, 200);
	}

    public void SetSize(int width,int height) {
        if (height<20) height=20;
        if (width<20) width=20;
        LinearLayout lbg = (LinearLayout)getChildAt(0);
        LinearLayout lbg2 = (LinearLayout)lbg.getChildAt(0);
        ListView list = (ListView)lbg2.getChildAt(0);

        // Changes the height and width to the specified *pixels*
        lbg.setPadding(1,1,1,1);
        lbg2.setPadding(1,1,1,1);
        list.setPadding(2,4,2,2);
        LayoutParams paramsa = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        paramsa.setMargins(1,4, 2, 2);
        paramsa.height = height-8;
        paramsa.width = width-6;
        list.setLayoutParams(paramsa);
        list.setBackgroundColor(Color.TRANSPARENT);
        android.view.ViewGroup.LayoutParams params = lbg2.getLayoutParams();
        params.height = height-2;
        params.width = width-2;
    }
    private void SetLayout(View button, int marginLeft, int marginTop, int marginRight, int marginBottom) {
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
