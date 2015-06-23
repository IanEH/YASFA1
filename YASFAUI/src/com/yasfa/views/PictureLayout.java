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

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Ian on 08/02/15.
 */
public class PictureLayout extends YASFAControl {
    public String Name;
    byte[] ItemBytes = null;
    final Button btnCamera;
    InflateView mMainView;

    public void SetSize(int width, int height) {
        Button list = (Button) getChildAt(0);
        if (height<20) height=20;
        if (width<20) width=20;
        ViewGroup.LayoutParams params = list.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = height;
        params.width = width;
    }

    public void NewName() {
        Name = "S" + UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return mMainView.scrollable.onTouchEvent(ev);
    }

    public Bitmap SetRes(Bitmap bitmap, int Scale) {

        Drawable img = btnCamera.getBackground();
        if (img != null) {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, Scale, stream);
                byte[] bts = stream.toByteArray();
                Bitmap bitmapOrg = BitmapFactory.decodeByteArray(bts, 0, bts.length);
                return bitmapOrg;
            } catch (Exception ex) {
            }
        }
        return bitmap;
    }

    public byte[] GetByteValue() {
        Drawable img = btnCamera.getBackground();
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

    public void SetValue(byte[] ItemBytes) {
        if (ItemBytes != null && ItemBytes.length > 1) {
            btnCamera.setText("");
            Bitmap bitmapOrg = BitmapFactory.decodeByteArray(ItemBytes, 0, ItemBytes.length);
            BitmapDrawable bmd = new BitmapDrawable(bitmapOrg);
            btnCamera.setBackground(bmd);
        } else {
            DefaultValue();
        }
    }

    public void SetValue(byte[] ItemBytes, int Scale) {
        if (ItemBytes != null && ItemBytes.length > 1) {
            btnCamera.setText("");
            Bitmap bitmapOrg = BitmapFactory.decodeByteArray(ItemBytes, 0, ItemBytes.length);
            bitmapOrg = SetRes(bitmapOrg, 25);
            BitmapDrawable bmd = new BitmapDrawable(bitmapOrg);
            btnCamera.setBackground(bmd);
        } else {
            DefaultValue();
        }
    }

    public void DefaultValue() {
        btnCamera.setText("");
        btnCamera.setBackgroundColor(Color.LTGRAY);
    }

    public byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    public void getBitmapFromFile(String src) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(src, options);
            BitmapDrawable bmd = new BitmapDrawable(bitmap);
            btnCamera.setBackground(bmd);
        } catch (Exception e) {
            // Log exception
        }
    }

    public void getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            SetValue(readBytes(input), 25);
        } catch (IOException e) {
            // Log exception
        }
    }

    public PictureLayout(InflateView context,boolean Create) {
        super(context);

        mMainView = (InflateView) context;

        btnCamera = new Button(context);
        btnCamera.setText("");
        //btnCamera.setBackgroundColor(Color.RED);
        btnCamera.setGravity(Gravity.CENTER);
        btnCamera.setLayoutParams(new AbsoluteLayout.LayoutParams(100, 100, 100, 100));
        btnCamera.setVisibility(1);
        addView(btnCamera);
        loadit ld = new loadit();
        if (Create) {
            Dialog dg = mMainView.CreateDialog(mMainView.DIALOG_LOAD_FILE, ".jpg", ld);
        }
    }

    class loadit extends LoadAFile
    {
        @Override
        public void LoadFile (String file){
        getBitmapFromFile(file);
        ShowImage();
    }
    }

    public boolean ShowImage() {
        try {
            if (ItemBytes!=null ) {
                //Display display = getWindow().getWindowManager().getDefaultDisplay();
                int dwidth = 600;//display.getWidth();
                int dheight = 800;//display.getHeight();
                int l=0;
                if(dwidth>dheight) {
                    l=300;
                }


                Bitmap bitmapOrg = BitmapFactory.decodeByteArray(ItemBytes,0,ItemBytes.length);
                int width = bitmapOrg.getWidth();
                int height = bitmapOrg.getHeight();

                int newWidth = 398;

                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newWidth) / width;

                Matrix matrix = new Matrix();
                matrix.preRotate(90);
                matrix.postScale(scaleWidth, scaleHeight);

                Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
                        width, height, matrix, true);
                BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
                //imageView.setImageDrawable(bmd);
                btnCamera.setBackground(bmd);
                btnCamera.setText("");

                bitmapOrg.recycle();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

 }