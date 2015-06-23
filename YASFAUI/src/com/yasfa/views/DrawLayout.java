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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Created by Ian on 08/02/15.
 */
public class DrawLayout extends YASFAControl{
    public String Name;
    public Draw mdraw = null;
    Bitmap mBitmap;
    Bitmap mBitmapn;
    RelativeLayout mView;


    public void SetSize(int width, int height) {
        ViewGroup.LayoutParams  params = mdraw.getLayoutParams();params = mdraw.getLayoutParams();
        params.height = height;
        params.width = width;
        params = mView.getLayoutParams();
        params.height = height;
        params.width = width;
    }

    public void NewName() {
        Name="S"+ UUID.randomUUID().toString().replace("-","");
    }

    public byte [] GetByteValue() {
        mdraw.save(mView);
        if (mBitmapn != null) {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmapn.compress(Bitmap.CompressFormat.PNG, 100, stream);
                return stream.toByteArray();
            } catch (Exception ex) {}
        }
        return new byte [1];    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    public void SetValue(byte [] ItemBytes) {
        if  (ItemBytes !=null && ItemBytes.length > 1) {
            mBitmap = BitmapFactory.decodeByteArray(ItemBytes, 0, ItemBytes.length);
            mdraw.load();
        } else {
            DefaultValue();
        }
    }

    public void DefaultValue() {
        mdraw.clear();
    }

    public DrawLayout(InflateView context) {
        super(context);


        mView =   new RelativeLayout(context);
        mView.setBackgroundColor(Color.WHITE);

        mdraw = new Draw(context);
        mdraw.setBackgroundColor(Color.WHITE);
        mView.addView(mdraw, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        addView(mView);

        final RelativeLayout menu =   new RelativeLayout(context);


        TextView md = new TextView(context);
        md.setText("M");
        md.setFocusable(false);
        md.setFocusableInTouchMode(false);
        md.setBackgroundColor(Color.TRANSPARENT);
        md.setShadowLayer(1.5f, -1, 1, Color.BLACK);
        md.setTextColor(Color.WHITE);
        md.setTextSize(17);
        md.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                    boolean removed=false;
                    for (int i=0;i<mView.getChildCount();++i) {
                        if (mView.getChildAt(i)==menu) {
                            mView.removeView(menu);
                            removed=true;
                            break;
                        }
                    }
                    if (!removed) mView.addView(menu);
            }
        });
        md.setLayoutParams(new AbsoluteLayout.LayoutParams(25, 25, 5, 5));

        mView.addView(md);

        TextView Clear = new TextView(context);
        Clear.setText("Clear");
        Clear.setShadowLayer(1.5f, -1, 1, Color.BLACK);
        Clear.setBackgroundColor(Color.BLACK);
        Clear.setTextColor(Color.WHITE);
        Clear.setTextSize(17);
        RelativeLayout.LayoutParams LayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutParameters.setMargins(25, 65, 0,0);
        LayoutParameters.width=62;
        LayoutParameters.height=22;
        Clear.setLayoutParams(LayoutParameters);
        menu.addView(Clear);

        TextView rub = new TextView(context);
        LayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutParameters.setMargins(25, 0, 0,0);
        LayoutParameters.width=62;
        LayoutParameters.height=22;
        rub.setLayoutParams(LayoutParameters);
        rub.setText("Rubber");
        rub.setBackgroundColor(Color.BLACK);
        rub.setTextSize(18);
        rub.setTextColor(Color.WHITE);
        rub.setShadowLayer(1.5f, -1, 1, Color.BLACK);
        rub.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                removeView(menu);
                SetValue(GetByteValue());
                mdraw.paint.setStrokeWidth(mdraw.THICK_WIDTH);
                mdraw.paint.setColor(Color.WHITE);
            }
        });
        menu.addView(rub);


        TextView b = new TextView(context);
        LayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutParameters.setMargins(25, 23, 0,0);
        LayoutParameters.width=20;
        LayoutParameters.height=20;
        b.setLayoutParams(LayoutParameters);
        b.setBackgroundColor(Color.BLACK);
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                removeView(menu);
                SetValue(GetByteValue());
                mdraw.paint.setStrokeWidth(mdraw.STROKE_WIDTH);
                mdraw.paint.setColor(Color.BLACK);
            }
        });
        menu.addView(b);

        TextView r = new TextView(context);
        LayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutParameters.setMargins(25+21, 23, 0,0);
        LayoutParameters.width=20;
        LayoutParameters.height=20;
        r.setLayoutParams(LayoutParameters);
        r.setBackgroundColor(Color.RED);
        r.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                removeView(menu);
                SetValue(GetByteValue());
                mdraw.paint.setStrokeWidth(mdraw.STROKE_WIDTH);
                mdraw.paint.setColor(Color.RED);
            }
        });
        menu.addView(r);

        TextView gr = new TextView(context);
        LayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutParameters.setMargins(25+42, 23, 0,0);
        LayoutParameters.width=20;
        LayoutParameters.height=20;
        gr.setLayoutParams(LayoutParameters);
        gr.setBackgroundColor(Color.GREEN);
        gr.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                removeView(menu);
                SetValue(GetByteValue());
                mdraw.paint.setStrokeWidth(mdraw.STROKE_WIDTH);
                mdraw.paint.setColor(Color.GREEN);
            }
        });
        menu.addView(gr);

        TextView bl = new TextView(context);
        LayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutParameters.setMargins(25,44, 0,0);
        LayoutParameters.width=20;
        LayoutParameters.height=20;
        bl.setLayoutParams(LayoutParameters);
        bl.setBackgroundColor(Color.BLUE);
        bl.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                removeView(menu);
                SetValue(GetByteValue());
                mdraw.paint.setStrokeWidth(mdraw.STROKE_WIDTH);
                mdraw.paint.setColor(Color.BLUE);
            }
        });
        menu.addView(bl);

        TextView y = new TextView(context);
        LayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutParameters.setMargins(25+21,44, 0,0);
        LayoutParameters.width=20;
        LayoutParameters.height=20;
        y.setLayoutParams(LayoutParameters);
        y.setBackgroundColor(Color.YELLOW);
        y.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                removeView(menu);
                SetValue(GetByteValue());
                mdraw.paint.setStrokeWidth(mdraw.STROKE_WIDTH);
                mdraw.paint.setColor(Color.YELLOW);
            }
        });
        menu.addView(y);

        TextView ma = new TextView(context);
        LayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LayoutParameters.setMargins(25+42,44, 0,0);
        LayoutParameters.width=20;
        LayoutParameters.height=20;
        ma.setLayoutParams(LayoutParameters);
        ma.setBackgroundColor(Color.MAGENTA);
        ma.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                removeView(menu);
                SetValue(GetByteValue());
                mdraw.paint.setStrokeWidth(mdraw.STROKE_WIDTH);
                mdraw.paint.setColor(Color.MAGENTA);
            }
        });
        menu.addView(ma);





        SetSize(200,200);

        Clear.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                mView.removeView(menu);
                mdraw.clear();
            }
        });
    }


    public class Draw extends View
    {
        private static final float STROKE_WIDTH = 2;
        private static final float THICK_WIDTH = 10;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

        public Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public Draw(Context context)
        {
            super(context);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v)
        {
            try
            {
                mBitmapn =  Bitmap.createBitmap (v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);;
                Canvas canvas = new Canvas(mBitmapn);
                v.draw(canvas);

            }
            catch(Exception e)
            {
            }
        }

        public void load()
        {
            try
            {
                path.reset();
                invalidate();
            }
            catch(Exception e)
            {
            }
        }

        public void clear()
        {
            mBitmap=null;
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            if (mBitmap!=null) {
                canvas.drawBitmap(mBitmap,0,0,paint);
            }
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event)
        //@Override
        //public boolean onTouchEvent(MotionEvent event)
        {
            float eventX = event.getX();
            float eventY = event.getY();

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++)
                    {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                   // debug("Ignored touch event: " + event.toString());
                    return true;
            }
            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return super.dispatchTouchEvent(event);
        }

        private void debug(String string){
        }

        private void expandDirtyRect(float historicalX, float historicalY)
        {
            if (historicalX < dirtyRect.left)
            {
                dirtyRect.left = historicalX;
            }
            else if (historicalX > dirtyRect.right)
            {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top)
            {
                dirtyRect.top = historicalY;
            }
            else if (historicalY > dirtyRect.bottom)
            {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY)
        {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
 }