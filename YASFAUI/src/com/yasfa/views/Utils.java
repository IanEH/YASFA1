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
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Ian on 14/03/15.
 */
public class Utils {

    public static boolean defaltdb(Context context) {
        final String DB_DESTINATIONAPP = "/data/data/" + context.getPackageName() + "/databases/YASFA.db";
        final String DB_DESTINATIONDB = "/data/data/" + context.getPackageName() + "/databases/YASFAD.db";

        // Check if the database exists before copying
        if((new File(DB_DESTINATIONAPP)).exists()==false) {
            try {
                try {
                    File folder = new File("/data/data/" + context.getPackageName() + "/databases");
                    folder.mkdirs();
                } catch (Exception exn){
                    String sc=exn.getMessage();
                }
                // Open the .db file in your assets directory
                InputStream is = context.getAssets().open("default.app");

                // Copy the database into the destination
                OutputStream os = new FileOutputStream(DB_DESTINATIONAPP);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();

                SharedPreferences YASFAState = context.getSharedPreferences("YASFAState", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = YASFAState.edit();
                ed.clear();
                ed.putLong("ROWID", -1);
                ed.putLong("PARENTROWID", -1);
                ed.putString("FORMNAME", "Menu");
                ed.commit();

            } catch (Exception ex) {
                String sc=ex.getMessage();
            }
        }
        if((new File(DB_DESTINATIONDB)).exists()==false) {
            try {
                // Open the .db file in your assets directory
                InputStream is = context.getAssets().open("defaultD.db");

                // Copy the database into the destination
                OutputStream os = new FileOutputStream(DB_DESTINATIONDB);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (Exception ex) {
                String sc=ex.getMessage();
            }
        }
        return true;
    }

    public static boolean delete(Context context,String filename) {
        String from = "/data/data/" + context.getPackageName() + "/databases/"+filename;
        File file = new File(from);
        return file.delete();
    }

    public static boolean copyToSD(Context context,String filename,String toname) {
        String to = getSD()+"/"+toname;
        String from = "/data/data/" + context.getPackageName() + "/databases/"+filename;
        return copy(from,to);
    }

    public static boolean saveToSD(Context context,String toname,byte [] bytes) {
        File photo=new File(getSD(), toname);

        if (photo.exists()) {
            photo.delete();
        }

        try {
            FileOutputStream fos=new FileOutputStream(photo.getPath());

            fos.write(bytes);
            fos.close();
        }
        catch (java.io.IOException e) {
            String m=e.getMessage();
        }
        return true;
    }

    public static boolean copyFromSD(Context context,String fromfile,String filename) {
        String to =  "/data/data/" + context.getPackageName() + "/databases/"+filename;
        return copy(fromfile,to);
    }

    public static String getSD() {
       String ExternalDirectory = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        if (android.os.Build.DEVICE.contains("samsung")
                || android.os.Build.MANUFACTURER.contains("samsung")) {
            File f = new File(Environment.getExternalStorageDirectory()
                    .getParent() + "/extSdCard" + "/myDirectory");
            if (f.exists() && f.isDirectory()) {
                ExternalDirectory = Environment.getExternalStorageDirectory()
                        .getParent() + "/extSdCard";
            } else {
                f = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/external_sd" + "/myDirectory");
                if (f.exists() && f.isDirectory()) {
                    ExternalDirectory = Environment
                            .getExternalStorageDirectory().getAbsolutePath()
                            + "/external_sd";
                }
            }
        }
        return ExternalDirectory;
    }
    public static boolean copy(String srcfile, String dstfile)  {
        File src = new File(srcfile);
        File dst =   new File (dstfile);
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        } catch (Exception ex) {
            String test = ex.getMessage();
            return false;
        }
    }

}
