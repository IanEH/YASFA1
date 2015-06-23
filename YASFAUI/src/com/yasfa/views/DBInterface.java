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

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class DBInterface  {
		static SQLiteDatabase DB = null;
        Context ctx;

		public void setBaseContext(Context baseContext) {
			// TODO Auto-generated method stub
            ctx = baseContext;
		}

		public void openDB() { 
	        try {
	        	if (DB==null) {
	        		
		            DB = ctx.openOrCreateDatabase("YASFAD.db", ctx.MODE_PRIVATE, null);
	        	} 
	        } catch (Exception e) {
	        	int i=0;
	        }
	 	}

		public void CreteTable(String Name,RelativeLayout mainRelativeLayout) { 
	     	   openDB();
	           try {
		            //DB.execSQL("DROP TABLE IF EXISTS "+Name+"");
		            DB.execSQL("CREATE TABLE IF NOT EXISTS "+Name+" (ID INTEGER PRIMARY KEY,PARENTID INTEGER,DBID INTEGER)");
                   try { // rEMOVE THIS
                       DB.execSQL("ALTER TABLE  "+Name+" ADD COLUMN PARENTID INTEGER");
                   } catch (Exception e1) {
                       int i2=0;
                   }
                   try { // rEMOVE THIS
                       DB.execSQL("ALTER TABLE  "+Name+" ADD COLUMN DBID INTEGER");
                   } catch (Exception e1) {
                       int i2=0;
                   }

				    for(int i=0;i<mainRelativeLayout.getChildCount();i++){
			      	    View child=mainRelativeLayout.getChildAt(i);
                        if (child instanceof ControlLayout) {
                            try {
                                if (((ControlLayout)child).Name!=null && !((ControlLayout)child).Name.equals("")&& !((ControlLayout)child).Name.equals("null") ) {
                                    DB.execSQL("ALTER TABLE  " + Name + " ADD COLUMN " + ((ControlLayout) child).Name.replace("-", "") + " VARCHAR");
                                }
                            } catch (Exception e1) {
                                int i2 = 0;
                            }
                        } else if (child instanceof DataSpinner) {
                            try {
                                DB.execSQL("ALTER TABLE  "+Name+" ADD COLUMN "+((DataSpinner)child).Name.replace("-","")+ " VARCHAR");
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof SliderLayout) {
                            try {
                                DB.execSQL("ALTER TABLE  "+Name+" ADD COLUMN "+((SliderLayout)child).Name.replace("-","")+ " VARCHAR");
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof RadioLayout) {
                            try {
                                DB.execSQL("ALTER TABLE  "+Name+" ADD COLUMN "+((RadioLayout)child).Name.replace("-","")+ " VARCHAR");
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof DrawLayout) {
                            try {
                                DB.execSQL("ALTER TABLE  "+Name+" ADD COLUMN "+((DrawLayout)child).Name.replace("-","")+ " BLOB");
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof CameraLayout) {
                            try {
                                DB.execSQL("ALTER TABLE  "+Name+" ADD COLUMN "+((CameraLayout)child).Name.replace("-","")+ " BLOB");
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof RecordPlay) {
                            try {
                                DB.execSQL("ALTER TABLE  "+Name+" ADD COLUMN "+((RecordPlay)child).Name.replace("-","")+ " BLOB");
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof SpinnerLayout) {
		    		        try {
		    		        	DB.execSQL("ALTER TABLE  "+Name+" ADD COLUMN "+((SpinnerLayout)child).Name.replace("-","")+ " VARCHAR");
		    		        } catch (Exception e1) {
		    		        	int i2=0;
		    		        }
		         		}

		         	}		
		        } catch (Exception e) {
		        	int i=0;
		        }
				closeDB();
		}

		public long Save(long ID,long ParentID,String Name,RelativeLayout mainRelativeLayout) {
	     	   openDB();
	           try {
	        	    String columns="";
	        	    String values="";
	        	    String update="";
	        	    boolean first = true;
				    for(int i=0;i<mainRelativeLayout.getChildCount();i++){
			      	    View child=mainRelativeLayout.getChildAt(i);
                        if (child instanceof ControlLayout) {
                            try {
                                if (((ControlLayout)child).Name!=null && !((ControlLayout)child).Name.equals("") && !((ControlLayout)child).Name.equals("null")) {
                                    if (!first) {
                                        columns = columns + ",";
                                        values = values + ",";
                                        update = update + ",";
                                    }
                                    columns = columns + ((ControlLayout) child).Name.replace("-", "");
                                    values = values + "?";
                                    update = update + ((ControlLayout) child).Name.replace("-", "") + "=?";
                                    first = false;
                                }
                            } catch (Exception e1) {
                                int i2 = 0;
                            }
                        } else if (child instanceof DrawLayout) {
                            try {
                                if (!first) {
                                    columns = columns+",";
                                    values = values+",";
                                    update = update +",";
                                }
                                columns = columns + ((DrawLayout)child).Name.replace("-","");
                                values = values + "?";
                                update = update + ((DrawLayout)child).Name.replace("-","") + "=?";
                                first = false;
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof CameraLayout) {
                            try {
                                if (!first) {
                                    columns = columns+",";
                                    values = values+",";
                                    update = update +",";
                                }
                                columns = columns + ((CameraLayout)child).Name.replace("-","");
                                values = values + "?";
                                update = update + ((CameraLayout)child).Name.replace("-","") + "=?";
                                first = false;
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof RecordPlay) {
                            try {
                                if (!first) {
                                    columns = columns+",";
                                    values = values+",";
                                    update = update +",";
                                }
                                columns = columns + ((RecordPlay)child).Name.replace("-","");
                                values = values + "?";
                                update = update + ((RecordPlay)child).Name.replace("-","") + "=?";
                                first = false;
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof DataSpinner) {
                            try {
                                if (!first) {
                                    columns = columns+",";
                                    values = values+",";
                                    update = update +",";
                                }
                                columns = columns + ((DataSpinner)child).Name.replace("-","");
                                values = values + "?";
                                update = update + ((DataSpinner)child).Name.replace("-","") + "=?";
                                first = false;
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof SliderLayout) {
                            try {
                                if (!first) {
                                    columns = columns+",";
                                    values = values+",";
                                    update = update +",";
                                }
                                columns = columns + ((SliderLayout)child).Name.replace("-","");
                                values = values + "?";
                                update = update + ((SliderLayout)child).Name.replace("-","") + "=?";
                                first = false;
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof RadioLayout) {
                            try {
                                if (!first) {
                                    columns = columns+",";
                                    values = values+",";
                                    update = update +",";
                                }
                                columns = columns + ((RadioLayout)child).Name.replace("-","");
                                values = values + "?";
                                update = update + ((RadioLayout)child).Name.replace("-","") + "=?";
                                first = false;
                            } catch (Exception e1) {
                                int i2=0;
                            }
		         		} else 	if (child instanceof SpinnerLayout) {
		    		        try {
		    		        	if (!first) {
		    		        		columns = columns+",";
		    		        		values = values+",";
		    		        		update = update +",";
		    		        	}
		    		        	columns = columns + ((SpinnerLayout)child).Name.replace("-","");
                                values = values + "?";
                                update = update + ((SpinnerLayout)child).Name.replace("-","") + "=?";
                                //values = values + "'"+((SpinnerLayout)child).GetValue().replace("'","''")+"'";
                                //update = update + ((SpinnerLayout)child).Name.replace("-","") + "='"+((SpinnerLayout)child).GetValue().replace("'","''")+"'";
		    		        	first = false;
		    		        } catch (Exception e1) {
		    		        	int i2=0;
		    		        }
		         		}

		         	}		
				    if (ID<0) {
                        SQLiteStatement insertStmt = DB.compileStatement("INSERT INTO "+Name+" (ParentID,"+columns+") VALUES ("+ParentID+","+values+")");
                        insertStmt.clearBindings();
                        int p=1;
                        for(int i=0;i<mainRelativeLayout.getChildCount();i++) {
                            View child = mainRelativeLayout.getChildAt(i);
                            if (child instanceof ControlLayout) {
                                if (((ControlLayout)child).Name!=null && !((ControlLayout)child).Name.equals("") && !((ControlLayout)child).Name.equals("null")) {
                                    insertStmt.bindString(p, ((ControlLayout) child).GetValue());
                                    ++p;
                                }
                            } else if (child instanceof DrawLayout) {
                                insertStmt.bindBlob(p, ((DrawLayout) child).GetByteValue());
                                ++p;
                            } else if (child instanceof CameraLayout) {
                                insertStmt.bindBlob(p, ((CameraLayout) child).GetByteValue());
                                ++p;
                            } else if (child instanceof RecordPlay) {
                                insertStmt.bindBlob(p, ((RecordPlay) child).GetByteValue());
                                ++p;
                            } else if (child instanceof SpinnerLayout) {
                                insertStmt.bindString(p, ((SpinnerLayout) child).GetValue());
                                ++p;
                            } else if (child instanceof DataSpinner) {
                                insertStmt.bindString(p, ((DataSpinner) child).GetValue());
                                ++p;
                            } else if (child instanceof SliderLayout) {
                                insertStmt.bindString(p, ((SliderLayout) child).GetValue());
                                ++p;
                            } else if (child instanceof RadioLayout) {
                                insertStmt.bindString(p, ((RadioLayout) child).GetValue());
                                ++p;
                            }
                        }
                        insertStmt.executeInsert();

                        String query = "SELECT ID from "+Name+" WHERE ParentID = "+ParentID+" order by ID DESC limit 1";
	          			Cursor c1 = DB.rawQuery(query,null);
	          			if (c1 != null && c1.moveToFirst()) {
	          				ID = c1.getLong(0); 
	          			}
				    } else {
                        SQLiteStatement insertStmt = DB.compileStatement("UPDATE "+Name+" SET ParentID="+ParentID+","+update+" WHERE ID = '"+ID+"'");
                        insertStmt.clearBindings();
                        int p=1;
                        for(int i=0;i<mainRelativeLayout.getChildCount();i++) {
                            View child = mainRelativeLayout.getChildAt(i);
                            if (child instanceof ControlLayout) {
                                if (((ControlLayout)child).Name!=null && !((ControlLayout)child).Name.equals("") && !((ControlLayout)child).Name.equals("null")) {
                                    insertStmt.bindString(p, ((ControlLayout) child).GetValue());
                                    ++p;
                                }
                            } else if (child instanceof DataSpinner) {
                                insertStmt.bindString(p, ((DataSpinner) child).GetValue());
                                ++p;
                            } else if (child instanceof SliderLayout) {
                                insertStmt.bindString(p, ((SliderLayout) child).GetValue());
                                ++p;
                            } else if (child instanceof DrawLayout) {
                                insertStmt.bindBlob(p, ((DrawLayout) child).GetByteValue());
                                ++p;
                            } else if (child instanceof CameraLayout) {
                                insertStmt.bindBlob(p, ((CameraLayout) child).GetByteValue());
                                ++p;
                            } else if (child instanceof RecordPlay) {
                                insertStmt.bindBlob(p, ((RecordPlay) child).GetByteValue());
                                ++p;
                            } else if (child instanceof RadioLayout) {
                                insertStmt.bindString(p, ((RadioLayout) child).GetValue());
                                ++p;
                            } else if (child instanceof SpinnerLayout) {
                                insertStmt.bindString(p, ((SpinnerLayout) child).GetValue());
                                ++p;
                            }
                        }
                        insertStmt.executeUpdateDelete();
				    }
		        } catch (Exception e) {
		        	int i=0;
                   i=2;
		        }
				closeDB();
				return ID;
		}

		public long Delete(long ID,long PARENTID,String Name,RelativeLayout mainRelativeLayout) {
	     	   openDB();
		       if (ID<0) {
		    	   return ID;
		       }
	           try {
	        	    String columns="";
	        	    String values="";
	        	    String update="";
	        	    boolean first = true;
				    for(int i=0;i<mainRelativeLayout.getChildCount();i++){
			      	    View child=mainRelativeLayout.getChildAt(i);
                        if (child instanceof ControlLayout) {
                            try {
                                if (((ControlLayout)child).Name!=null && !((ControlLayout)child).Name.equals("") && !((ControlLayout)child).Name.equals("null")) {
                                    ((ControlLayout) child).DefaultValue();
                                }
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof CameraLayout) {
                            try {
                                ((CameraLayout)child).DefaultValue();
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof DrawLayout) {
                            try {
                                ((DrawLayout)child).DefaultValue();
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        }

                    }
			    	DB.execSQL("DELETE FROM "+Name+" WHERE ID = '"+ID+"'");
			    	long NEXTID= Get(ID,PARENTID,Name,mainRelativeLayout,Direction.Prev);
			    	if (NEXTID < 0 || NEXTID==ID) {
			    		NEXTID= Get(ID,PARENTID,Name,mainRelativeLayout,Direction.Next);
			    	}
			    	if (NEXTID < 0 || NEXTID==ID) {
			    		NEXTID= Get(ID,PARENTID,Name,mainRelativeLayout,Direction.Last);
			    	}
			    	ID = NEXTID;
		        } catch (Exception e) {
		        	int i=0;
		        }
				closeDB();
				return ID;
		}

		enum Direction {
			This,
			First,
			Next,
			Prev,
			Last,
			New,
		}

		class Row {
			public Long ID;
			public String text;
            public byte[] image;
            public byte[] image1;
            public byte[] sound1;
            public boolean hasImages;
            public boolean hasSounds;
		}

    public Row GetList(long ID,long PARENTID,String Name,RelativeLayout mainRelativeLayout,String Select,boolean next) {
        openDB();
        Row retval=null;
        try {
            String columns="ID";
            String image="";
            String image1="";
            String sel="";
            columns = columns+",";
            boolean first=true;

            for(int i=0;i<mainRelativeLayout.getChildCount();i++){
                View child=mainRelativeLayout.getChildAt(i);
                if (child instanceof ControlLayout) {
                    try {
                        if (((ControlLayout)child).Name!=null && !((ControlLayout)child).Name.equals("")&& !((ControlLayout)child).Name.equals("null") ) {
                            if (!Select.equals("")) {
                                if (!first) sel = sel + " OR ";
                                sel = sel + ((ControlLayout) child).Name.replace("-", "") + " LIKE '%" + Select + "%'";
                                first = false;
                            }
                            columns = columns + "IFNULL(" + ((ControlLayout) child).Name.replace("-", "") + ",'')";
                            //if (!first)
                            columns = columns + "||','||";
                        }

                    } catch (Exception e1) {
                        int i2=0;
                    }
                } else if (child instanceof CameraLayout) {
                    if (image.equals("")) {
                        image = ","+((CameraLayout)child).Name.replace(" - "," ")+" image";
                    } else if (image1.equals("")) {
                        image1 = ","+((CameraLayout)child).Name.replace(" - "," ")+" image1";
                    }
                } else if (child instanceof DrawLayout) {
                    if (image.equals("")) {
                        image = ","+((DrawLayout)child).Name.replace(" - "," ")+" image";
                    } else if (image1.equals("")) {
                        image1 = ","+((DrawLayout)child).Name.replace(" - "," ")+" image1";
                    }
                } else if (child instanceof SpinnerLayout) {
                    try {
                        if (!Select.equals("")) {
                            if (!first) sel=sel+" OR ";
                            sel=sel+((SpinnerLayout)child).Name.replace("-","")+" LIKE '%"+Select+"%'";
                            first=false;
                        }
                        columns = columns + "IFNULL("+((SpinnerLayout)child).Name.replace("-","")+",'')";
                        //if (!first)
                        columns = columns+"||','||";

                    } catch (Exception e1) {
                        int i2=0;
                    }
                } else if (child instanceof RadioLayout) {
                    try {
                        if (!Select.equals("")) {
                            if (!first) sel=sel+" OR ";
                            sel=sel+((RadioLayout)child).Name.replace("-","")+" LIKE '%"+Select+"%'";
                            first=false;
                        }
                        columns = columns + "IFNULL("+((RadioLayout)child).Name.replace("-","")+",'')";
                        //if (!first)
                        columns = columns+"||','||";

                    } catch (Exception e1) {
                        int i2=0;
                    }
                }
            }
            if (!sel.equals("")) sel=" AND ("+sel+")";
            Cursor c =null;
            columns=columns+"''";
            if (next) {
                c = DB.rawQuery("SELECT " + columns + " " + image+ " " + image1 + " FROM " + Name + " WHERE ID > '" + ID + "' AND (PARENTID IS NULL OR PARENTID ='" + PARENTID + "') " + sel + " ORDER BY ID limit 1", null);
            } else {
                c = DB.rawQuery("SELECT " + columns + " " + image + " " + image1 + " FROM " + Name + " WHERE ID = '" + ID + "' " + sel + " ORDER BY ID limit 1", null);
            }

            if (c.moveToFirst()) {
                retval = new Row();
                retval.image1 = null;
                retval.image = null;
                retval.hasImages=false;
                retval.ID = c.getLong(c.getColumnIndex("ID"));
                retval.text = c.getString(1);
                if (!next) {
                    if (!image.equals("")) {
                        retval.image = c.getBlob(2);
                        retval.hasImages = true;
                    }
                    if (!image1.equals("")) {
                        retval.image1 = c.getBlob(3);
                        retval.hasImages = true;
                    }
                } else {
                    if (!image.equals("")) {
                        retval.hasImages = true;
                    }
                    if (!image1.equals("")) {
                        retval.hasImages = true;
                    }
                }
            } else {
            }
        } catch (Exception e) {
            //Curupt ruw!
            if (retval==null) return retval;
            retval.ID=ID+1;
            retval.text="Corrupt Row!";
            int i=0;
        }
        closeDB();
        return retval;
    }

    public Row GetShortList(long ID,long PARENTID,String Name,String Fields,String image,String image1,String sound1, boolean next,boolean all) {
        openDB();
        Row retval=null;
        try {
            String columns="ID,"+Fields;

            Cursor c =null;
            columns=columns+"''";
            if (next && !all) {
                c = DB.rawQuery("SELECT " + columns + " " + image + " " + image1+ " " + sound1 + " FROM " + Name + " WHERE ID > '" + ID + "' AND (PARENTID IS NULL OR PARENTID ='" + PARENTID + "') ORDER BY ID limit 1", null);
            } else if (all && next) {
                c = DB.rawQuery("SELECT " + columns + " " + image + " " + image1+ " " + sound1 + " FROM " + Name + " WHERE ID > '" + ID + "' ORDER BY ID limit 1", null);
            } else {
                c = DB.rawQuery("SELECT " + columns + " " + image + " " + image1+ " " + sound1 + " FROM " + Name + " WHERE ID = '" + ID + "' ORDER BY ID limit 1", null);
            }
            if (c.moveToFirst()) {
                retval = new Row();
                retval.ID = c.getLong(c.getColumnIndex("ID"));

                if (!next) {
                    int n=2;
                    if (!image.equals("")) {
                        retval.image = c.getBlob(n);
                        ++n;
                        retval.hasImages = true;
                    }
                    if (!image1.equals("")) {
                        retval.image1 = c.getBlob(n);
                        ++n;
                        retval.hasImages = true;
                    }
                    if (!sound1.equals("")) {
                        retval.sound1 = c.getBlob(n);
                        ++n;
                        retval.hasSounds = true;
                    }
                } else {
                    if (!image.equals("")) {
                        retval.hasImages = true;
                    }
                    if (!image1.equals("")) {
                        retval.hasImages = true;
                    }
                    if (!sound1.equals("")) {
                        retval.hasSounds = true;
                    }
                }
                retval.text = c.getString(1);
            } else {
            }
        } catch (Exception e) {
            int i=0;
        }
        closeDB();
        return retval;
    }


    public long Get(long ID,long PARENTID,String Name,RelativeLayout mainRelativeLayout,Direction direction) {
	     	   openDB();
	           try {
	        	    String columns="ID";
				    for(int i=0;i<mainRelativeLayout.getChildCount();i++){
			      	    View child=mainRelativeLayout.getChildAt(i);
		         		if (child instanceof ControlLayout) {
		    		        try {
                                if (((ControlLayout)child).Name!=null && !((ControlLayout)child).Name.equals("") && !((ControlLayout)child).Name.equals("null")) {
                                    columns = columns + ",";
                                    columns = columns + ((ControlLayout) child).Name.replace("-", "");
                                }
		    		        } catch (Exception e1) {
		    		        	int i2=0;
		    		        }
                        } else if (child instanceof RadioLayout) {
                            try {
                                columns = columns+",";
                                columns = columns + ((RadioLayout)child).Name.replace("-","");
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof DrawLayout) {
                            try {
                                columns = columns+",";
                                columns = columns + ((DrawLayout)child).Name.replace("-","");
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof CameraLayout) {
                            try {
                                columns = columns+",";
                                columns = columns + ((CameraLayout)child).Name.replace("-","");
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof RecordPlay) {
                            try {
                                columns = columns+",";
                                columns = columns + ((RecordPlay)child).Name.replace("-","");
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof SliderLayout) {
                            try {
                                columns = columns+",";
                                columns = columns + ((SliderLayout)child).Name.replace("-","");
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof DataSpinner) {
                            try {
                                columns = columns+",";
                                columns = columns + ((DataSpinner)child).Name.replace("-","");
                            } catch (Exception e1) {
                                int i2=0;
                            }
                        } else if (child instanceof SpinnerLayout) {
                            try {
                                columns = columns+",";
                                columns = columns + ((SpinnerLayout)child).Name.replace("-","");
                            } catch (Exception e1) {
                                int i2=0;
                            }

                        }

		         	}
				    Cursor c =null;
				    if (direction==Direction.This) {
				    	c = DB.rawQuery("SELECT "+columns+" FROM "+Name +" WHERE ID = '"+ID+"' AND (PARENTID IS NULL OR PARENTID ='" +PARENTID+ "') ORDER BY ID limit 1", null);
				    } else if (direction==Direction.Next) {
					    	c = DB.rawQuery("SELECT "+columns+" FROM "+Name +" WHERE ID > '"+ID+"' AND (PARENTID IS NULL OR PARENTID ='" +PARENTID+ "')  ORDER BY ID limit 1", null);
				    } else if (direction==Direction.Prev) {
				    	c = DB.rawQuery("SELECT "+columns+" FROM "+Name +" WHERE ID < '"+ID+"' AND (PARENTID IS NULL OR PARENTID ='" +PARENTID+ "')  ORDER BY ID DESC limit 1", null);
				    } else if (direction==Direction.First) {
				    	c = DB.rawQuery("SELECT "+columns+" FROM "+Name +" WHERE (PARENTID IS NULL OR PARENTID ='" +PARENTID+ "') ORDER BY ID ASC limit 1", null);
				    } else if (direction==Direction.Last) {
				    	c = DB.rawQuery("SELECT "+columns+" FROM "+Name +" WHERE (PARENTID IS NULL OR PARENTID ='" +PARENTID+ "') ORDER BY ID DESC limit 1", null);
				    } else if (direction==Direction.New) {
				    	c =null; // Just explicit set!
				    }
				    if (c == null) { // Clear its new!
				    	ID = -1;
					    for(int i=0;i<mainRelativeLayout.getChildCount();i++){
				      	    View child=mainRelativeLayout.getChildAt(i);
			         		if (child instanceof ControlLayout) {
			    		        try {
                                    if (((ControlLayout)child).Name!=null && !((ControlLayout)child).Name.equals("") && !((ControlLayout)child).Name.equals("null")) {
                                        ((ControlLayout) child).DefaultValue();
                                    }
			    		        } catch (Exception e1) {
			    		        	int i2=0;
			    		        }
                            } else 	if (child instanceof DrawLayout) {
                                try {
                                    ((DrawLayout)child).DefaultValue();
                                } catch (Exception e1) {
                                    int i2=0;
                                }
                            } else 	if (child instanceof CameraLayout) {
                                try {
                                    ((CameraLayout)child).DefaultValue();
                                } catch (Exception e1) {
                                    int i2=0;
                                }
                            } else 	if (child instanceof RecordPlay) {
                                try {
                                    ((RecordPlay)child).DefaultValue();
                                } catch (Exception e1) {
                                    int i2=0;
                                }
                            } else 	if (child instanceof RadioLayout) {
                                try {
                                    ((RadioLayout)child).DefaultValue();
                                } catch (Exception e1) {
                                    int i2=0;
                                }
                            } else 	if (child instanceof SpinnerLayout) {
                                try {
                                    ((SpinnerLayout)child).DefaultValue();
                                } catch (Exception e1) {
                                    int i2=0;
                                }
                            }
			         	}
				    }
				    else if (c.moveToFirst()) {
					    for(int i=0;i<mainRelativeLayout.getChildCount();i++){
				      	    View child=mainRelativeLayout.getChildAt(i);
                            if (child instanceof ControlLayout) {
                                try {
                                    if (((ControlLayout)child).Name!=null && !((ControlLayout)child).Name.equals("")&& !((ControlLayout)child).Name.equals("null") ) {
                                        ((ControlLayout) child).SetValue(c.getString(c.getColumnIndex(((ControlLayout) child).Name.replace("-", ""))));
                                    }
                                } catch (Exception e1) {
                                    int i2 = 0;
                                }
                            } else if (child instanceof DataSpinner) {
                                try {
                                    ((DataSpinner)child).SetValue(c.getString(c.getColumnIndex(((DataSpinner) child).Name.replace("-", ""))));
                                } catch (Exception e1) {
                                    int i2=0;
                                }
                            } else if (child instanceof SliderLayout) {
                                try {
                                    ((SliderLayout)child).SetValue(c.getString(c.getColumnIndex(((SliderLayout) child).Name.replace("-", ""))));
                                } catch (Exception e1) {
                                    int i2=0;
                                }
                            } else if (child instanceof DrawLayout) {
                                try {
                                    ((DrawLayout)child).SetValue(c.getBlob(c.getColumnIndex(((DrawLayout) child).Name.replace("-", ""))));
                                } catch (Exception e1) {
                                    int i2=0;
                                }
                            } else if (child instanceof CameraLayout) {
                                try {
                                    ((CameraLayout)child).SetValue(c.getBlob(c.getColumnIndex(((CameraLayout) child).Name.replace("-", ""))));
                                } catch (Exception e1) {
                                    int i2=0;
                                }
                            } else if (child instanceof RecordPlay) {
                                try {
                                    ((RecordPlay)child).SetValue(c.getBlob(c.getColumnIndex(((RecordPlay) child).Name.replace("-", ""))));
                                } catch (Exception e1) {
                                    int i2=0;
                                }

                            } else if (child instanceof RadioLayout) {
                                try {
                                    ((RadioLayout)child).SetValue(c.getString(c.getColumnIndex(((RadioLayout) child).Name.replace("-", ""))));
                                } catch (Exception e1) {
                                    int i2=0;
                                }

                            } else 	if (child instanceof SpinnerLayout) {
			    		        try {
			    		        	((SpinnerLayout)child).SetValue(c.getString(c.getColumnIndex(((SpinnerLayout)child).Name.replace("-",""))));
			    		        } catch (Exception e1) {
			    		        	int i2=0;
			    		        }
			         		}

			         	}		
					    ID = c.getLong(c.getColumnIndex("ID"));
				    } else {
				    }
		        } catch (Exception e) {
		        	int i=0;
		        }
				closeDB();
				return ID;
		}

		public void closeDB() { 
	        try {
	            DB.close();
	            DB = null;
	        } catch (Exception e) {
	        }
	 	}
}
